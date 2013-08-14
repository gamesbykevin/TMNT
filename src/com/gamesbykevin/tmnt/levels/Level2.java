package com.gamesbykevin.tmnt.levels;

import com.gamesbykevin.tmnt.main.Resources.GameAudioMusic;
import com.gamesbykevin.tmnt.main.Resources.GamePlayers;

import java.awt.Polygon;

/**
 * Specifications for Level 2
 * @author GOD
 */
public final class Level2 extends Level implements LevelRules
{
    public Level2() throws Exception
    {
        super(GamePlayers.Rocksteady);
        
        setLevelBounds();
        
        setMusic();
        
        setPowerupLimit();
        
        setEnemyLimits();
        
        super.setAutoScrollBackground(false);
    }
    
    @Override
    public void setEnemyLimits()
    {
        super.setEnemiesAtOnce(5);
        super.setEnemiesPerCheckpoint(9);
    }
    
    @Override
    public void setLevelBounds()
    {
        int x[] = {0,   3860, 3860, 0};
        int y[] = {130, 130,  224,  224};
        
        //set the area that is in bounds
        super.setBoundary(new Polygon(x, y, x.length));
    }
    
    @Override
    public void setPowerupLimit()
    {
        //this level will have x power ups
        super.setPowerUpLimit(3);
    }
    
    @Override
    public void setMusic()
    {
        super.setMusic(GameAudioMusic.Level2);
        super.setMusicBoss(GameAudioMusic.Level2Boss);
    }
}