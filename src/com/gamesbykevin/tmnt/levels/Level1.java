package com.gamesbykevin.tmnt.levels;

import com.gamesbykevin.tmnt.main.ResourceManager;
import com.gamesbykevin.tmnt.main.ResourceManager.LevelObjects;

import java.awt.Image;
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
        int x[] = {0,   2299, 2299, 0  };
        int y[] = {162, 162,  224,  224};
        
        //set the area that is in bounds
        super.setBounds(new Polygon(x, y, x.length));
        
        //set true or false if we are using a separate background
        super.setAutoScrollBackground(false);
    }
}