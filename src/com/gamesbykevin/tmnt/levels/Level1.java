package com.gamesbykevin.tmnt.levels;

import java.awt.Polygon;

/**
 * Specifications for Level 1
 * @author GOD
 */
public final class Level1 extends Level implements LevelRules
{
    public Level1()
    {
        super(0, 5);
        
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
        int x[] = {0,   930, 930, 1105, 1105, 2364, 2364, 0};
        int y[] = {137, 137, 160,  160, 137, 137, 224, 224};
        
        //set the area that is in bounds
        super.setBoundary(new Polygon(x, y, x.length));
    }
}