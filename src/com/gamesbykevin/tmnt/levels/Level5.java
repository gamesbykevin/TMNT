package com.gamesbykevin.tmnt.levels;

import java.awt.Polygon;

/**
 * Specifications for Level 5
 * @author GOD
 */
public final class Level5 extends Level implements LevelRules
{
    public Level5() throws Exception
    {
        super(8, 6);
        
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
        int x[] = {0,   1718, 1718, 0};
        int y[] = {128, 128,  223,  223};
        
        //set the area that is in bounds
        super.setBoundary(new Polygon(x, y, x.length));
    }
}