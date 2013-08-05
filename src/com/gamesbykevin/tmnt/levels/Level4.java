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
        setupDefaults();
    }
    
    @Override
    public void setupDefaults()
    {
        int x[] = {0,   3847, 3867, 4223, 4223, 0};
        int y[] = {133, 133,  111,  111,  221,  221};
        
        //set the area that is in bounds
        super.setBounds(new Polygon(x, y, x.length));
        
        //set true or false if we are using a separate background
        super.setAutoScrollBackground(false);
    }
}