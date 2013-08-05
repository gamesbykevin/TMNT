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
        setupDefaults();
    }
    
    @Override
    public void setupDefaults()
    {
        int x[] = {0,   505, 505, 0};
        int y[] = {142, 142, 219, 219};
        
        //set the area that is in bounds
        super.setBounds(new Polygon(x, y, x.length));
        
        //set true or false if we are using a separate background
        super.setAutoScrollBackground(false);
    }
}