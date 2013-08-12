package com.gamesbykevin.tmnt.levels;

import java.awt.Polygon;

/**
 * Specifications for Level 2
 * @author GOD
 */
public final class Level2 extends Level implements LevelRules
{
    public Level2() throws Exception
    {
        super(9, 5);
        
        setupDefaults();
    }
    
    @Override
    public void setupDefaults()
    {
        setLevelBounds();
        
        //set true or false if we are using a separate background
        super.setAutoScrollBackground(false);
    }
    
    @Override
    public void setLevelBounds()
    {
        int x[] = {0,   3860, 3860, 0};
        int y[] = {130, 130,  224,  224};
        
        //set the area that is in bounds
        super.setBoundary(new Polygon(x, y, x.length));
    }
}