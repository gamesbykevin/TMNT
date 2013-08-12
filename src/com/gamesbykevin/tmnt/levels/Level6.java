package com.gamesbykevin.tmnt.levels;

import java.awt.Polygon;

/**
 * Specifications for Level 6
 * @author GOD
 */
public final class Level6 extends Level implements LevelRules
{
    public Level6() throws Exception
    {
        //this level will not have any enemies besides the boss
        super(0, 0);
        
        //this level will only have 1 power up
        super.setPowerUpLimit(1);
        
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