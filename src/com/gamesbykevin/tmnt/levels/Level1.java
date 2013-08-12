package com.gamesbykevin.tmnt.levels;

import com.gamesbykevin.tmnt.main.ResourceManager.GamePlayers;

import java.awt.Polygon;

/**
 * Specifications for Level 1
 * @author GOD
 */
public final class Level1 extends Level implements LevelRules
{
    public Level1() throws Exception
    {
        super(0, 0, GamePlayers.Bebop);
        //super(10, 4);
        
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
        int x[] = {0,   2364, 2364, 0};
        int y[] = {142,  142, 224,  224};
        
        //set the area that is in bounds
        super.setBoundary(new Polygon(x, y, x.length));
    }
}