package com.gamesbykevin.tmnt.levels;

import java.awt.Polygon;

/**
 * Specifications for Level1
 * @author GOD
 */
public final class Level2 extends Level implements LevelRules
{
    public Level2()
    {
        setupDefaults();
    }
    
    @Override
    public void setupDefaults()
    {
        int x[] = {0,   484, 514, 721, 730, 2023, 2076, 2366, 2432, 2432, 0};
        int y[] = {177, 177, 201, 201, 177, 177,  113,  113,  177,  223,  223};
        
        //set the area that is in bounds
        super.setBounds(new Polygon(x, y, x.length));
        
        //set true or false if we are using a separate background
        super.setAutoScrollBackground(false);
    }
}