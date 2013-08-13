/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gamesbykevin.tmnt.enemymanager;

import com.gamesbykevin.framework.base.Sprite;
import com.gamesbykevin.tmnt.grunt.Grunt;
import com.gamesbykevin.tmnt.heroes.Hero;
import com.gamesbykevin.tmnt.main.Engine;
import com.gamesbykevin.tmnt.main.ResourceManager;
import com.gamesbykevin.tmnt.main.ResourceManager.GamePlayers;
import com.gamesbykevin.tmnt.player.Player;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

public class EnemyManager 
{
    private List<Grunt> grunts;
    
    private static final int CONSECUTIVE_ATTACKERS = 2;
    
    public EnemyManager()
    {
        grunts = new ArrayList<>();
    }
    
    public void dispose()
    {
        for (Grunt grunt : grunts)
        {
            grunt.dispose();
            grunt = null;
        }
        
        grunts.clear();
        grunts = null;
    }
    
    public void addAllPlayerObjects(List<Sprite> levelObjects)
    {
        for (Player grunt : grunts)
        {
            levelObjects.add(grunt);
        }
    }
    
    public List<Grunt> getGrunts()
    {
        return this.grunts;
    }
    
    public void add(final Grunt grunt)
    {
        grunts.add(grunt);
    }
    
    /**
     * Get a list of grunts that are not assigned a target
     * @param type List of grunts
     */
    private List<Grunt> getUnassigned()
    {
        List<Grunt> unassigned = new ArrayList<>();
        
        for (Grunt grunt : grunts)
        {
            if (grunt.getAssignment() == null)
                unassigned.add(grunt);
        }
        
        return unassigned;
    }
    
    /**
     * Get a list of enemies that are targeting the specific hero of type
     * @param Type List of grunts
     */
    private List<Grunt> getAttackers(ResourceManager.GamePlayers assigned)
    {
        List<Grunt> attackers = new ArrayList<>();
        
        for (Grunt grunt : grunts)
        {
            if (grunt.getAssignment() == null)
                continue;
            if (grunt.getAssignment() == assigned)
                attackers.add(grunt);
        }
        
        return attackers;
    }
    
    public void update(final Engine engine) throws Exception
    {
        updateStrategy(engine);
        
        for (int i=0; i < grunts.size(); i++)
        {
            Grunt grunt = grunts.get(i);
            
            //if the grunt assets are not loaded yet
            if (grunt.getImage() == null)
            {
                grunt.setImage(engine.getResources().getGamePlayer(grunt.getType()));
                grunt.setDelay(engine.getMain().getTimeDeductionPerFrame());
                grunt.setDimensions();
                
                final Point start = engine.getLevelManager().getLevel().getStart(engine.getMain().getScreen(), grunt.getWidth(), grunt.getHeight());
                grunt.setLocation(start);
                
                final Rectangle screen = engine.getMain().getScreen();
                final boolean rightSide = start.x >= screen.x + screen.width;
                
                grunt.setAttackEast(!rightSide);
            }
            
            if (grunt.getDelay() < 0)
                grunt.setDelay(engine.getMain().getTimeDeductionPerFrame());
            
            grunt.update(engine);
            
            //if the death animation is complete and no more lives remove the grunt
            if (grunt.isDeadComplete() && !grunt.hasLives())
            {
                grunt.dispose();
                grunt = null;
                grunts.remove(i);
                i--;
            }
        }
    }
    
    /**
     * This method will check if there are at least x enemies that they are set to attack each hero
     */
    private void updateStrategy(final Engine engine)
    {
        final Rectangle screen = engine.getMain().getScreen();
        
        final List<Player> heroes = engine.getPlayerManager().getHeroManager().getHeroes();
        
        //enemies that are not assigned a target
        List<Grunt> unassigned = getUnassigned();
        
        //assign a target to every enemy
        for (Grunt grunt : unassigned)
        {
            grunt.setAssignment((heroes.get((int)(Math.random() * heroes.size()))).getType());
        }
        
        unassigned.clear();
        
        //check every hero and make sure a certain number of enemies are trying to attack each one
        for (Player hero : heroes)
        {
            //get list of enemies that are targeting this hero
            List<Grunt> tmp = getAttackers(hero.getType());
            
            //list of enemies that are attacking hero
            List<Grunt> tmpAttackers = new ArrayList<>();

            for (int i=0; i < tmp.size(); i++)
            {
                Grunt grunt = tmp.get(i);
                
                //if the grunt is no longer fully on the screen
                if (!screen.contains(grunt.getRectangle()))
                {
                    //if the enemy is on the left side of the screen and is set to attack the west side
                    if (grunt.getX() <= screen.x && !grunt.hasAttackEast())
                        grunt.setAttackEast(!grunt.hasAttackEast());
                    
                    //if the enemy is on the right side of the screen and is set to attack the east side
                    if (grunt.getX() >= screen.x + screen.width && grunt.hasAttackEast())
                        grunt.setAttackEast(!grunt.hasAttackEast());
                }
                
                //this grunt is attacking or getting close to attacking
                if (grunt.hasStep2() || grunt.hasStep3())
                {
                    //add grunt to list of attackers
                    tmpAttackers.add(grunt);
                    
                    //remove grunt from list
                    tmp.remove(i);
                    i--;
                    continue;
                }
            }
            
            //if there are more than x enemies atacking/preparing
            if (tmpAttackers.size() > CONSECUTIVE_ATTACKERS)
            {
                //don't allow more than x enemies ready to attack hero
                while(tmpAttackers.size() > CONSECUTIVE_ATTACKERS)
                {
                    //random grunt
                    final int randomIndex = (int)(Math.random() * tmpAttackers.size());
                    
                    //set available grunt to retreat for now
                    tmpAttackers.get(randomIndex).resetSteps();
                    
                    //remove from list
                    tmpAttackers.remove(randomIndex);
                }
            }
            else
            {
                //we want at least x enemies attacking at any time if possible
                while(tmpAttackers.size() < CONSECUTIVE_ATTACKERS && tmp.size() > 0)
                {
                    //random grunt
                    final int randomIndex = (int)(Math.random() * tmp.size());

                    //set available grunt to ready for attack
                    tmp.get(randomIndex).setStep2(true);
                    
                    //add grunt to list of attackers
                    tmpAttackers.add(tmp.get(randomIndex));
                    
                    //remove grunt from available list
                    tmp.remove(randomIndex);
                }
            }
            
            tmpAttackers.clear();
            tmp.clear();
        }
    }
    
    /**
     * Do we already have an enemy that can throw projectiles
     * 
     * @return boolean
     */
    public boolean hasProjectileGrunt()
    {
        for (Player grunt : grunts)
        {
            if (isProjectileGrunt(grunt.getType()))
                return true;
        }
        
        return false;
    }
    
    /**
     * Can this type throw projectiles
     * @param type
     * @return boolean
     */
    public static boolean isProjectileGrunt(final GamePlayers type)
    {
        switch (type)
        {
            case FootSoldier2:
            case FootSoldier7:
            case FootSoldier8:
            case FootSoldier9:
            case Bebop:
            case Krang:
            case Leatherhead:
            case Rocksteady:
            case Shredder:
                return true;
            
            default:
                return false;
        }
    }
    
    /**
     * Check if we already have an enemy of this type
     * @param type
     * @return boolean
     */
    public boolean hasType(final GamePlayers type)
    {
        for (Player grunt : grunts)
        {
            if (grunt.getType() == type)
                return true;
        }
        
        return false;
    }
}