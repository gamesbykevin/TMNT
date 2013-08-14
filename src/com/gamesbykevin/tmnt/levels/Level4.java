package com.gamesbykevin.tmnt.levels;

import com.gamesbykevin.tmnt.main.Resources.GameAudioMusic;
import com.gamesbykevin.tmnt.main.Resources.GamePlayers;

import java.awt.Polygon;

/**
 * Specifications for Level 4
 * @author GOD
 */
public final class Level4 extends Level implements LevelRules
{
    public Level4() throws Exception
    {
        super(GamePlayers.Krang);
        
        setLevelBounds();
        
        setMusic();
        
        setPowerupLimit();
        
        setEnemyLimits();
        
        super.setAutoScrollBackground(false);
    }
    
    @Override
    public void setEnemyLimits()
    {
        super.setEnemiesAtOnce(6);
        super.setEnemiesPerCheckpoint(10);
    }
    
    @Override
    public void setLevelBounds()
    {
        int x[] = {0,   4223, 4223, 0};
        int y[] = {133, 133,  221,  221};
        
        //set the area that is in bounds
        super.setBoundary(new Polygon(x, y, x.length));
    }
    
    @Override
    public void setPowerupLimit()
    {
        //this level will have x power ups
        super.setPowerUpLimit(4);
    }
    
    @Override
    public void setMusic()
    {
        super.setMusic(GameAudioMusic.Level4);
        super.setMusicBoss(GameAudioMusic.Level4Boss);
    }
}