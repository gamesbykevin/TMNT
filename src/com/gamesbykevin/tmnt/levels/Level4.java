package com.gamesbykevin.tmnt.levels;

import com.gamesbykevin.tmnt.main.ResourceManager.GamePlayers;

import java.awt.Polygon;

/**
 * Specifications for Level 4
 * @author GOD
 */
public final class Level4 extends Level implements LevelRules
{
    public Level4() throws Exception
    {
        super(0, 0, GamePlayers.Krang);
        //super(10, 6);
        
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
        int x[] = {0,   4223, 4223, 0};
        int y[] = {133, 133,  221,  221};
        
        //set the area that is in bounds
        super.setBoundary(new Polygon(x, y, x.length));
    }
}