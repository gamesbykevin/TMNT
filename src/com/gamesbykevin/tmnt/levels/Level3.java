package com.gamesbykevin.tmnt.levels;

import java.awt.Polygon;

/**
 * Specifications for Level 3
 * @author GOD
 */
public final class Level3 extends Level implements LevelRules
{
    public Level3()
    {
        super(0, 5);
        
        setupDefaults();
    }
    
    @Override
    public void setupDefaults()
    {
        setLevelBounds();
        
        //set true or false if we are using a separate background
        super.setAutoScrollBackground(true);
    }
    
    @Override
    public void setLevelBounds()
    {
        int x[] = {0,   3330, 3330, 0};
        int y[] = {123, 123,  190,  190};
        
        //set the area that is in bounds
        super.setBoundary(new Polygon(x, y, x.length));
    }
}