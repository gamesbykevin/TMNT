package com.gamesbykevin.tmnt.player;

import com.gamesbykevin.tmnt.grunt.*;
import com.gamesbykevin.framework.base.Sprite;

import com.gamesbykevin.tmnt.heroes.*;
import com.gamesbykevin.tmnt.main.*;
import com.gamesbykevin.tmnt.main.ResourceManager.GamePlayers;
import com.gamesbykevin.tmnt.main.ResourceManager.LevelMisc;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

/**
 * This class will contain all of the actors in the game
 * @author GOD
 */
public class PlayerManager 
{
    private List<Hero> heroes;
    private List<Grunt> grunts;
    
    private static final int CONSECUTIVE_ATTACKERS = 2;
    
    public PlayerManager()
    {
        heroes = new ArrayList<>();
        grunts = new ArrayList<>();
    }
    
    /**
     * Proper house keeping
     */
    public void dispose()
    {
        heroes.clear();
        heroes = null;
        
        grunts.clear();
        grunts = null;
    }
    
    public static boolean isEnemy(final GamePlayers type)
    {
        switch (type)
        {
            case FootSoldier1:
            case FootSoldier2:
            case FootSoldier3:
            case FootSoldier4:
            case FootSoldier5:
            case FootSoldier6:
            case FootSoldier7:
            case FootSoldier8:
            case FootSoldier9:
                return true;
                
            default:
                return false;
        }
    }
    
    public static boolean isEnemyProjectile(final GamePlayers type)
    {
        switch (type)
        {
            case FootSoldier2:
            case FootSoldier7:
            case FootSoldier8:
            case FootSoldier9:
                return true;
            
            default:
                return false;
        }
    }
    
    public static boolean isHero(final GamePlayers type)
    {
        switch (type)
        {
            case Donatello:
            case Raphael:
            case Leonardo:
            case Michelangelo:
                return true;
                
            default:
                return false;
        }
    }
    
    /**
     * Add a random grunt not including bosses.
     * A random grunt capable of throwing a projectile 
     * is a possibility as long as another one doesn't exist
     * 
     * @throws Exception 
     */
    public void addRandomEnemy() throws Exception
    {
        ArrayList possibleEnemies = new ArrayList();
        
        for (GamePlayers type : GamePlayers.values())
        {
            //first make sure this type is an enemy and we didn't already add this enemy
            if (isEnemy(type) && !hasEnemyType(type))
            {
                //make sure an enemy capable of throwing projectiles doesn't already exist
                if (!hasEnemyProjectile())
                {
                    possibleEnemies.add(type);
                }
                else
                {
                    //if this enemy can't throw projectiles add them for sure
                    if (!isEnemyProjectile(type))
                        possibleEnemies.add(type);
                }
            }
        }
        
        GamePlayers randomEnemy = (GamePlayers)possibleEnemies.get((int)(Math.random() * possibleEnemies.size()));
        
        addEnemy(randomEnemy);
        
        possibleEnemies.clear();
        possibleEnemies = null;
    }
    
    private void addEnemy(final GamePlayers type) throws Exception
    {
        Grunt grunt = null;
        
        switch (type)
        {
            case FootSoldier1:
                grunt = new FootSoldier1();
                break;
                
            case FootSoldier2:
                grunt = new FootSoldier2();
                break;
                
            case FootSoldier3:
                grunt = new FootSoldier3();
                break;
                
            case FootSoldier4:
                grunt = new FootSoldier4();
                break;
                
            case FootSoldier5:
                grunt = new FootSoldier5();
                break;
                
            case FootSoldier6:
                grunt = new FootSoldier6();
                break;
                
            case FootSoldier7:
                grunt = new FootSoldier7();
                break;
                
            case FootSoldier8:
                grunt = new FootSoldier8();
                break;
                
            case FootSoldier9:
                grunt = new FootSoldier9();
                break;
                
            default:
                throw new Exception("Player not found");
        }
        
        grunts.add(grunt);
    }
    
    public void addHero(final GamePlayers type, final int lives) throws Exception
    {
        Hero hero;
        
        switch(type)
        {
            case Donatello:
                hero = new Donatello();
                break;
                
            case Raphael:
                hero = new Raphael();
                break;
                
            case Leonardo:
                hero = new Leonardo();
                break;
                
            case Michelangelo:
                hero = new Michelangelo();
                break;
                
            default:
                throw new Exception("Player not found");
        }
        
        hero.setLives(lives);
        heroes.add(hero);
    }
    
    /**
     * Do we already have an enemy that can throw projectiles
     * 
     * @return boolean
     */
    public boolean hasEnemyProjectile()
    {
        for (Grunt grunt : grunts)
        {
            if (isEnemyProjectile(grunt.getType()))
                return true;
        }
        
        return false;
    }
    
    /**
     * Check if we already have an enemy of this type
     * @param type
     * @return boolean
     */
    public boolean hasEnemyType(final GamePlayers type)
    {
        for (Grunt grunt : grunts)
        {
            if (grunt.getType() == type)
                return true;
        }
        
        return false;
    }
    
    /**
     * Get a list of all our grunts
     * @return List<Enemy>
     */
    public List<Grunt> getEnemies()
    {
        return this.grunts;
    }
    
    public List<Player> getPlayerEnemies()
    {
        List<Player> result = new ArrayList<>();
        
        for (Player player : grunts)
        {
            result.add(player);
        }
        
        return result;
    }
    
    public List<Player> getPlayerHeroes()
    {
        List<Player> result = new ArrayList<>();
        
        for (Player player : heroes)
        {
            result.add(player);
        }
        
        return result;
    }
    
    /**
     * Get a list of all our heroes
     * @return List<Hero>
     */
    public List<Hero> getHeroes()
    {
        return this.heroes;
    }
    
    /**
     * Get a list of grunts that are targeting the specific hero of type
     * @param type List of grunts
     */
    private List<Grunt> getEnemyTargets(GamePlayers assigned)
    {
        List<Grunt> gruntTargets = new ArrayList<>();
        
        for (Grunt grunt : grunts)
        {
            if (grunt.getAssignment() == null)
                continue;
            if (grunt.getAssignment() == assigned)
                gruntTargets.add(grunt);
        }
        
        return gruntTargets;
    }
    
    /**
     * Get a list of grunts that are not assigned a target
     * @param type List of grunts
     */
    private List<Grunt> getEnemyUnassigned()
    {
        List<Grunt> gruntTargets = new ArrayList<>();
        
        for (Grunt grunt : grunts)
        {
            if (grunt.getAssignment() == null)
                gruntTargets.add(grunt);
        }
        
        return gruntTargets;
    }
    
    /**
     * Add all player related objects to the existing levelObjects list.
     * 
     * @param levelObjects List of all objects in the level
     */
    public void addAllPlayerObjects(List<Sprite> levelObjects)
    {
        //all objects will be contained in this list and sorted so the lowest y value is drawn first
        
        for (Player hero : getHeroes())
        {
            levelObjects.add(hero);
        }
        
        for (Player grunt : getEnemies())
        {
            levelObjects.add(grunt);
        }
    }
    
    public List<Player> getAllPlayers()
    {
        List<Player> players = new ArrayList<>();
        
        for (Player hero : getHeroes())
        {
            players.add(hero);
        }
        
        for (Player grunt : getEnemies())
        {
            players.add(grunt);
        }
        
        return players;
    }
    
    public void update(final Engine engine) throws Exception
    {
        //NOTE: all heroes are human for now, we may have AI friends
        for (int i=0; i < heroes.size(); i++)
        {
            Hero hero = heroes.get(i);
            
            //if the hero assets are not loaded yet
            if (hero.getImage() == null)
            {
                hero.setImage(engine.getResources().getGamePlayer(hero.getType()));
                hero.setDelay(engine.getMain().getTimeDeductionPerFrame());
                hero.setDimensions();
                
                final Rectangle r = engine.getLevelManager().getLevel().getBoundary().getBounds();
                
                hero.setLocation(r.x + hero.getWidth() + 1, r.y + r.height - (hero.getHeight()));
            }
            
            hero.update(engine.getProjectileManager(), getPlayerEnemies(), engine.getKeyboard(), engine.getLevelManager().getLevel().getBoundary());
            
            //if the death animation is complete and no more lives
            if (hero.isDeadComplete())
            {
                //if the hero does not have any more lives
                if (!hero.hasLives())
                {
                    /*
                     * if there are more than 1 hero remove the dead hero
                     * because once all heroes are dead we want to leave the last one on the screen
                     */
                    if (heroes.size() > 1)
                    {
                        heroes.remove(i);
                        i--;
                    }
                }
                else
                {
                    hero.setNewState(Player.State.IDLE);
                }
            }
        }
        
        //make sure grunts are targeting heroes
        updateEnemyStrategy(engine.getMain().getScreen());
        
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
            
            grunt.update(engine.getProjectileManager(), getPlayerHeroes(), engine.getLevelManager().getLevel().getBoundary());
            
            //if the death animation is complete and no more lives remove the grunt
            if (grunt.isDeadComplete() && !grunt.hasLives())
            {
                grunts.remove(i);
                i--;
            }
        }
    }
    
    /**
     * This method will check if there are at least x grunts that they are set to attack each hero
     */
    private void updateEnemyStrategy(final Rectangle screen)
    {
        //enemies that are not assigned a target
        List<Grunt> unassigned = getEnemyUnassigned();
        
        //assign a target to every enemy
        for (Grunt grunt : unassigned)
        {
            grunt.setAssignment((heroes.get((int)(Math.random() * heroes.size()))).getType());
        }
        
        unassigned.clear();
        
        //check every hero and make sure a certain number of enemies are trying to attack each one
        for (Hero hero : heroes)
        {
            //get list of enemies that are targeting this hero
            List<Grunt> tmp = getEnemyTargets(hero.getType());
            
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
     * Here we will draw the health bars for all the heroes
     * @param g Graphics object we write the information to
     * @return Graphics
     */
    public Graphics render(Graphics g, final Rectangle screen, final ResourceManager resources)
    {
        int x = (int)((screen.width  * .05) + screen.x);
        int y = (int)((screen.height * .1) + screen.y);
        
        for (Hero hero : heroes)
        {
            final Image image;
            
            switch (hero.getType())
            {
                case Donatello:
                    image = resources.getLevelObject(LevelMisc.DonInfo);
                    break;
                    
                case Leonardo: 
                    image = resources.getLevelObject(LevelMisc.LeoInfo);
                    break;
                    
                case Raphael: 
                    image = resources.getLevelObject(LevelMisc.RaphInfo);
                    break;
                    
                case Michelangelo: 
                    image = resources.getLevelObject(LevelMisc.MikeInfo);
                    break;
                    
                default:
                    image = resources.getLevelObject(LevelMisc.LeoInfo);
                    break;
            }
            
            g.drawImage(image, x, y, null);
            
            hero.renderHealthInformation(g, x + (int)(image.getWidth(null) * .05), y + image.getHeight(null) - (int)(image.getHeight(null) * .15));
            
            x += (screen.width / heroes.size());
        }
        
        return g;
    }
}