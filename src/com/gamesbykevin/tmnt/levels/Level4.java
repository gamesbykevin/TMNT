package com.gamesbykevin.tmnt.levels;

import java.awt.Polygon;

/**
 * Specifications for Level1
 * @author GOD
 */
public final class Level4 extends Level implements LevelRules
{
    public Level4()
    {
        super(10, 7);
        
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
        int x[] = {0,   3847, 3867, 4223, 4223, 0};
        int y[] = {133, 133,  111,  111,  221,  221};
        
        //set the area that is in bounds
        super.setBoundary(new Polygon(x, y, x.length));
    }
}