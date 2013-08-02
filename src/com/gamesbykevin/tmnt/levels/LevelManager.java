package com.gamesbykevin.tmnt.levels;

import com.gamesbykevin.tmnt.main.ResourceManager;
import java.awt.*;

/**
 *
 * @author GOD
 */
public class LevelManager 
{
    private Level level;
    
    public LevelManager(final ResourceManager res)
    {
        setLevel(0, res);
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
    public void setLevel(final int num, final ResourceManager res)
    {
        switch(num)
        {
            case 0:
                Image image = res.getLevelObject(ResourceManager.LevelObjects.Level1);
                
                level = new Level1();
                level.setImage(image);
                level.setDimensions(image.getWidth(null), image.getHeight(null));
                level.setBackgroundImage(res.getLevelObject(ResourceManager.LevelObjects.Level1Background));
                break;
                
            default:
                break;
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