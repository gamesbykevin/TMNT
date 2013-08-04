package com.gamesbykevin.tmnt.levels;

import com.gamesbykevin.tmnt.enemies.Enemy;
import com.gamesbykevin.tmnt.heroes.Hero;
import com.gamesbykevin.tmnt.main.ResourceManager;
import com.gamesbykevin.tmnt.player.Player;

import java.awt.*;
import java.util.List;

/**
 *
 * @author GOD
 */
public class LevelManager 
{
    private Level level;
    
    public LevelManager()
    {
        
    }
    
    /**
     * Get the current level
     * @return Level
     */
    public Level getLevel()
    {
        return this.level;
    }
    
    /**
     * Set the level number which will create a new instance of level
     * @param num 
     */
    public void setLevel(final int num, ResourceManager resources)
    {
        switch(num)
        {
            case 0:
                Image image = resources.getLevelObject(ResourceManager.LevelObjects.Level1);
                
                level = new Level1();
                level.setImage(image);
                level.setDimensions(image.getWidth(null), image.getHeight(null));
                level.setBackgroundImage(resources.getLevelObject(ResourceManager.LevelObjects.Level1Background));
                break;
                
            default:
                break;
        }
    }
    
    /**
     * Check if the level needs to be scrolled and if the hero has collision with power-up
     * @param enemies List of existing enemies
     * @param heroes List of existing heroes
     */
    public void update(List<Enemy> enemies, List<Hero> heroes, final Rectangle screen) throws Exception
    {
        //update level scrolling etc..
        getLevel().update(screen);
        
        //make sure level has been created
        if (getLevel() != null)
        {
            boolean hasEnemies = false;
            
            for (Enemy enemy : enemies)
            {
                if (!enemy.isDead())
                {
                    //hasEnemies = true;
                    break;
                }
            }
            
            if (!hasEnemies)
            {
                for (Hero hero : heroes)
                {
                    getLevel().setScrollSpeed(Player.VELOCITY_NONE);
                    
                    //right edge where hero can't go past
                    final int rightSide = screen.x + (int)(screen.width * .9);
                    
                    //if the hero is moving east make sure they still stay on screen
                    if (hero.getVelocityX() > 0 && hero.getX() >= rightSide)
                    {
                        getLevel().setScrollSpeed(hero.getVelocityX());
                        
                        //stop level scroll if at end of level
                        if (getLevel().getEastBoundsX() <= screen.x + screen.width)
                            getLevel().setScrollSpeed(Player.VELOCITY_NONE);
                        
                        hero.setX(rightSide);
                    }
                    
                    //left edge where hero can't go past
                    final int leftSide = screen.x + (int)(screen.width * .1);
                    
                    //if the hero is moving west make sure they still stay on screen
                    if (hero.getVelocityX() < 0 && hero.getX() <= leftSide)
                    {
                        hero.setX(leftSide);
                    }
                }
            }
        }
    }
    
    public Graphics render(Graphics g)
    {
        if (level != null)
        {
            level.render(g);
        }
        
        return g;
    }
}