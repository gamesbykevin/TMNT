package com.gamesbykevin.tmnt.levels;

import java.awt.Polygon;

/**
 * Specifications for Level1
 * @author GOD
 */
public final class Level5 extends Level implements LevelRules
{
    public Level5()
    {
        super(10, 6);
        
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
        int x[] = {0,   1457, 1464, 1555, 1601, 1662, 1707, 0};
        int y[] = {133, 133,  105,  105,  136,  136,  223,  223};
        
        //set the area that is in bounds
        super.setBoundary(new Polygon(x, y, x.length));
    }
}