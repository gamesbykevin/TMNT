package com.gamesbykevin.tmnt.player;

import com.gamesbykevin.tmnt.grunt.*;
import com.gamesbykevin.framework.base.Sprite;

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
    private HeroManager heroManager;
    private GruntManager gruntManager;
    private BossManager bossManager;
    
    public PlayerManager()
    {
        heroManager = new HeroManager();
        gruntManager = new GruntManager();
        bossManager = new BossManager();
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
    }
    
    /**
     * Get a list of all enemies and bosses
     * @return List<Player>
     */
    public List<Player> getEnemies()
    {
        List<Player> allEnemies = new ArrayList<>();
        
        for (Grunt grunt : gruntManager.getGrunts())
        {
            allEnemies.add(grunt);
        }
        
        for (Grunt grunt : bossManager.getGrunts())
        {
            allEnemies.add(grunt);
        }
        
        return allEnemies;
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