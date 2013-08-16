package com.gamesbykevin.tmnt.player;

import com.gamesbykevin.tmnt.grunt.*;
import com.gamesbykevin.framework.base.Sprite;
import com.gamesbykevin.framework.util.Timer;
import com.gamesbykevin.framework.util.TimerCollection;

import com.gamesbykevin.tmnt.boss.*;
import com.gamesbykevin.tmnt.heroes.*;
import com.gamesbykevin.tmnt.main.*;

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
    
    //keep track of the number of enemies defeated
    private int defeatedCount;
    
    //timers here to track total time played, time to delay until next level
    private TimerCollection timers;
    
    public enum Keys
    {
        GamePlay, NextLevelStart
    }
    
    private static final long NEXT_LEVEL_START_DELAY = 11500L;
    
    public PlayerManager(final long timeDeductionPerUpdate)
    {
        heroManager =  new HeroManager();
        gruntManager = new GruntManager();
        bossManager =  new BossManager();
        
        timers = new TimerCollection(timeDeductionPerUpdate);
        timers.add(Keys.GamePlay);
        
        //wait about 11 seconds after level is complete before we start next level
        timers.add(Keys.NextLevelStart, TimerCollection.toNanoSeconds(NEXT_LEVEL_START_DELAY));
        
        //pause this timer because we only need it when the level is finished
        timers.setPause(Keys.NextLevelStart, true);
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
     * Add all player related objects to the existing levelObjects list.
     * 
     * @param levelObjects List of all objects in the level
     */
    public void addAllPlayerObjects(List<Sprite> levelObjects)
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
     * Here we will draw the health bars for all the heroes
     * @param g Graphics object we write the information to
     * @param  engine Game Engine containing Screen and Resources
     * @return Graphics
     */
    public Graphics render(final Graphics g, final Engine engine)
    {
        heroManager.render(g, engine.getMain().getScreen(), engine.getResources());
        
        return g;
    }
}