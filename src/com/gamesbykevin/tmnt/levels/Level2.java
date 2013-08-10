package com.gamesbykevin.tmnt.levels;

import java.awt.Polygon;

/**
 * Specifications for Level 2
 * @author GOD
 */
public final class Level2 extends Level implements LevelRules
{
    public Level2()
    {
        super(0, 0);
        
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
        int x[] = {0,   525, 525, 700, 700, 2023, 2023, 2432, 2432, 0};
        int y[] = {157, 157, 191, 191, 157, 157,  99,   99,   223,  223};
        
        //set the area that is in bounds
        super.setBoundary(new Polygon(x, y, x.length));
    }
}