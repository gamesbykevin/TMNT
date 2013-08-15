package com.gamesbykevin.tmnt.enemymanager;

import com.gamesbykevin.framework.base.Sprite;
import com.gamesbykevin.tmnt.grunt.Grunt;
import com.gamesbykevin.tmnt.main.Engine;
import com.gamesbykevin.tmnt.main.Resources;
import com.gamesbykevin.tmnt.main.Resources.GamePlayers;
import com.gamesbykevin.tmnt.player.Player;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

public class EnemyManager 
{
    //list of enemies
    private List<Grunt> grunts;
    
    //list of enemies that are not assigned a target
    private List<Grunt> unassigned;
    
    //list of enemies that have been assigned a target
    private List<Grunt> attackers;
    
    //a temporary list of enemies attacking a specific target
    private List<Grunt> tmpAttackers;
    
    //our temporary list containing the heroes
    private List<Player> heroes;
    
    //the number of attackers per target are limited
    private static final int CONSECUTIVE_ATTACKERS = 2;
    
    public EnemyManager()
    {
        grunts = new ArrayList<>();
    }
    
    /**
     * Free up resources
     */
    public void dispose()
    {
        for (Grunt grunt : grunts)
        {
            if (grunt != null)
                grunt.dispose();
            
            grunt = null;
        }
        
        grunts.clear();
        grunts = null;
    }
    
    public void addAllPlayerObjects(final List<Sprite> levelObjects)
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
        if (unassigned == null)
            unassigned = new ArrayList<>();
        
        unassigned.clear();
        
        for (Grunt grunt : grunts)
        {
            if (grunt.getAssignment() == null)
                unassigned.add(grunt);
        }
        
        return unassigned;
    }
    
    /**
     * Populate a list of enemies that are targeting 
     * a hero of a specific type.
     * 
     * @param Type List of grunts
     */
    private void setAttackers(Resources.GamePlayers assigned)
    {
        if (attackers == null)
            attackers = new ArrayList<>();
        
        attackers.clear();
        
        for (Grunt grunt : grunts)
        {
            if (grunt.getAssignment() == null)
                continue;
            if (grunt.getAssignment() == assigned)
                attackers.add(grunt);
        }
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
        final int screenLeftSide = engine.getMain().getScreen().x;
        final int screenRightSide = engine.getMain().getScreen().x + engine.getMain().getScreen().width;
        
        heroes = engine.getPlayerManager().getHeroManager().getPlayerHeroes();
        
        //enemies that are not assigned a target
        unassigned = getUnassigned();
        
        //assign a target to every enemy
        for (Grunt grunt : unassigned)
        {
            grunt.setAssignment((heroes.get((int)(Math.random() * heroes.size()))).getType());
        }
        
        //clear list since we are now done
        unassigned.clear();
        
        //check every hero and make sure a certain number of enemies are trying to attack each one
        for (Player hero : heroes)
        {
            //set the current list of enemies based on the hero that they are targeting
            setAttackers(hero.getType());
            
            if (tmpAttackers == null)
                tmpAttackers = new ArrayList<>();
            
            tmpAttackers.clear();
            
            for (int i=0; i < attackers.size(); i++)
            {
                Grunt grunt = attackers.get(i);
                
                //if the grunt is no longer fully on the screen
                if (!engine.getMain().getScreen().contains(grunt.getRectangle()))
                {
                    //if the enemy is on the left side of the screen and is set to attack the west side
                    if (grunt.getX() <= screenLeftSide && !grunt.hasAttackEast())
                        grunt.setAttackEast(!grunt.hasAttackEast());
                    
                    //if the enemy is on the right side of the screen and is set to attack the east side
                    if (grunt.getX() >= screenRightSide && grunt.hasAttackEast())
                        grunt.setAttackEast(!grunt.hasAttackEast());
                }
                
                //this grunt is attacking or getting close to attacking
                if (grunt.hasStep2() || grunt.hasStep3())
                {
                    //add grunt to list of attackers
                    tmpAttackers.add(grunt);
                    
                    //remove grunt from list
                    attackers.remove(i);
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
                while(tmpAttackers.size() < CONSECUTIVE_ATTACKERS && attackers.size() > 0)
                {
                    //random grunt
                    final int randomIndex = (int)(Math.random() * attackers.size());

                    //set available grunt to ready for attack
                    attackers.get(randomIndex).setStep2(true);
                    
                    //add grunt to list of attackers
                    tmpAttackers.add(attackers.get(randomIndex));
                    
                    //remove grunt from available list
                    attackers.remove(randomIndex);
                }
            }
            
            tmpAttackers.clear();
            attackers.clear();
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