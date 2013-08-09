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
        super(10, 5);
        
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
        int x[] = {0,   484, 514, 721, 730, 2023, 2076, 2366, 2432, 2432, 0};
        int y[] = {177, 177, 201, 201, 177, 177,  113,  113,  177,  223,  223};
        
        //set the area that is in bounds
        super.setBoundary(new Polygon(x, y, x.length));
    }
}