package com.gamesbykevin.tmnt.levels;

import java.awt.Polygon;

/**
 * Specifications for Level1
 * @author GOD
 */
public final class Level6 extends Level implements LevelRules
{
    public Level6()
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
        int x[] = {0,   505, 505, 0};
        int y[] = {142, 142, 219, 219};
        
        //set the area that is in bounds
        super.setBoundary(new Polygon(x, y, x.length));
    }
}