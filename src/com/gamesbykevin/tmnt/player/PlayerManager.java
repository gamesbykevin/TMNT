package com.gamesbykevin.tmnt.player;

import com.gamesbykevin.tmnt.grunt.*;
import com.gamesbykevin.framework.base.Sprite;
import com.gamesbykevin.framework.util.Timer;
import com.gamesbykevin.framework.util.TimerCollection;

import com.gamesbykevin.tmnt.boss.*;
import com.gamesbykevin.tmnt.heroes.*;
import com.gamesbykevin.tmnt.main.*;
import java.awt.Color;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

/**
 * This class will contain all of the actors in the game
 * @author GOD
 */
public class PlayerManager 
{
    private HeroManager  heroManager;
    private GruntManager gruntManager;
    private BossManager  bossManager;
    
    //list of all enemies combined Grunt/Boss
    private List<Player> allEnemies;
    
    private List<Sprite> levelObjects;
    
    //keep track of the number of enemies defeated
    private int defeatedCount;
    
    //timers here to track total time played, time to delay until next level
    private TimerCollection timers;
    
    public enum Keys
    {
        GamePlay, NextLevelStart, BossFlash
    }
    
    private static final long NEXT_LEVEL_START_DELAY = TimerCollection.toNanoSeconds(12500L);
    
    public PlayerManager(final long timeDeductionPerUpdate)
    {
        heroManager =  new HeroManager();
        gruntManager = new GruntManager();
        bossManager =  new BossManager();
        
        timers = new TimerCollection(timeDeductionPerUpdate);
        timers.add(Keys.GamePlay);
        
        //setup delay and pause timer until we need it
        timers.add(Keys.NextLevelStart, NEXT_LEVEL_START_DELAY);
        timers.setPause(Keys.NextLevelStart, true);
        
        //setup delay and pause timer until we need it
        timers.add(Keys.BossFlash, Boss.BOSS_FLASH_DELAY_1);
        timers.setPause(Keys.BossFlash, true);
    }
    
    /**
     * Gets the specified Timer
     * @param key The unique identifier for the specified Timer
     * @return 
     */
    public Timer getTimer(final Object key)
    {
        return timers.getTimer(key);
    }
    
    /**
     * Add 1 to the total count of enemies defeated
     */
    public void addEnemiesDefeated()
    {
        this.defeatedCount++;
    }
    
    /**
     * Get the total number of enemies defeated
     * @return int
     */
    public int getEnemiesDefeatedCount()
    {
        return this.defeatedCount;
    }
    
    /**
     * Proper house keeping
     */
    public void dispose()
    {
        heroManager.dispose();
        heroManager = null;
        
        gruntManager.dispose();
        gruntManager = null;
        
        bossManager.dispose();
        bossManager = null;
        
        if (allEnemies != null)
        {
            for (Player player : allEnemies)
            {
                if (player != null)
                    player.dispose();
                
                player = null;
            }
            
            allEnemies.clear();
        }
        
        allEnemies = null;
    }
    
    /**
     * Get a list of all enemies and bosses
     * @return List<Player>
     */
    public List<Player> getEnemies()
    {
        if (allEnemies == null)
            allEnemies = new ArrayList<>();
        
        allEnemies.clear();
        
        for (Grunt grunt : gruntManager.get())
        {
            allEnemies.add(grunt);
        }
        
        for (Grunt grunt : bossManager.get())
        {
            allEnemies.add(grunt);
        }
        
        return allEnemies;
    }
    
    /**
     * Get the total count of all enemies (Grunts and Bosses)
     * @return int
     */
    public int getEnemyCount()
    {
        return (gruntManager.getCount() + bossManager.getCount());
    }
    
    /**
     * Are there any existing enemies regardless if alive or not
     * @return boolean
     */
    public boolean hasEnemies()
    {
        return (getEnemyCount() > 0);
    }
    
    /**
     * Add all player related objects to the existing levelObjects list.
     * 
     * @param levelObjects List of all objects in the level
     */
    public void addAllPlayerObjects()
    {
        heroManager.addAllPlayerObjects(levelObjects);
        gruntManager.addAllPlayerObjects(levelObjects);
        bossManager.addAllPlayerObjects(levelObjects);
    }
    
    public HeroManager getHeroManager()
    {
        return this.heroManager;
    }
    
    public GruntManager getGruntManager()
    {
        return this.gruntManager;
    }
    
    public BossManager getBossManager()
    {
        return this.bossManager;
    }
    
    public void update(final Engine engine) throws Exception
    {
        //update all timers
        timers.update();
        
        //update all heroes
        heroManager.update(engine);
        
        //update all grunts
        gruntManager.update(engine);
        
        //update all bosses
        bossManager.update(engine);
    }
    
    /**
     * Here we will draw all of the current player objects
     * as well as the health bars for all the heroes (Currently this game is only 1 Player)
     * @param g Graphics object we write the information to
     * @param  engine Game Engine containing Screen and Resources
     * @return Graphics
     */
    public Graphics render(final Graphics g, final Engine engine)
    {
        if (levelObjects == null)
            levelObjects = new ArrayList<>();

        //remove all objects in List
        levelObjects.clear();

        //add all level related objects to List
        engine.getLevelManager().addAllStageObjects(levelObjects);

        //add all player related objects to List
        addAllPlayerObjects();

        //add all of the projectiles to List
        engine.getProjectileManager().addAllProjectiles(levelObjects);

        for (int i=0; i < levelObjects.size(); i++)
        {
            for (int x=0; x < levelObjects.size(); x++)
            {
                if (i == x)
                    continue;

                //re-arrange the objects accordingly based on the y-coordinate and height
                if (levelObjects.get(i).getY() + (levelObjects.get(i).getHeight() / 2) < levelObjects.get(x).getY() + (levelObjects.get(x).getHeight() / 2))
                {
                    Sprite temp = levelObjects.get(i);

                    levelObjects.set(i, levelObjects.get(x));
                    levelObjects.set(x, temp);
                }
            }
        }

        for (Sprite levelObject : levelObjects)
        {
            //if there is no image to draw skip it
            if (levelObject.getImage() == null)
                continue;

            //get half the dimensions so we can offset/reset the coordinates
            int halfWidth  = (levelObject.getWidth()  / 2);
            int halfHeight = (levelObject.getHeight() / 2);

            //we need to offset the object location before drawing
            levelObject.setX(levelObject.getX() - halfWidth);
            levelObject.setY(levelObject.getY() - halfHeight);

            levelObject.draw(g);

            //now that the object is drawn we need to reset the location
            levelObject.setX(levelObject.getX() + halfWidth);
            levelObject.setY(levelObject.getY() + halfHeight);
        }

        //remove all objects in List
        levelObjects.clear();
        
        //draw hero info, etc....
        heroManager.render(g, engine.getMain().getScreen(), engine.getResources());
        
        return g;
    }
}