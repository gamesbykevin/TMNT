package com.gamesbykevin.tmnt.levels;

import java.awt.Polygon;

/**
 * Specifications for Level1
 * @author GOD
 */
public final class Level3 extends Level implements LevelRules
{
    public Level3()
    {
        setupDefaults();
    }
    
    @Override
    public void setupDefaults()
    {
        int x[] = {30,  3330, 3330, 30};
        int y[] = {123, 123,  190,  190};
        
        //set the area that is in bounds
        super.setBounds(new Polygon(x, y, x.length));
        
        //set true or false if we are using a separate background
        super.setAutoScrollBackground(true);
    }
}