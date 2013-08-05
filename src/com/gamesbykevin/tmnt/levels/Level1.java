package com.gamesbykevin.tmnt.levels;

import java.awt.Polygon;

/**
 * Specifications for Level1
 * @author GOD
 */
public final class Level1 extends Level implements LevelRules
{
    public Level1()
    {
        setupDefaults();
    }
    
    @Override
    public void setupDefaults()
    {
        int x[] = {0,   866, 879, 1160, 1191, 2304, 2392, 0};
        int y[] = {137, 137, 160,  160, 137, 137, 224, 224};
        
        
        //set the area that is in bounds
        super.setBounds(new Polygon(x, y, x.length));
        
        //set true or false if we are using a separate background
        super.setAutoScrollBackground(false);
    }
}