package com.gamesbykevin.tmnt.levels;

import com.gamesbykevin.tmnt.main.Resources.GameAudioMusic;
import com.gamesbykevin.tmnt.main.Resources.GamePlayers;

import java.awt.Polygon;

/**
 * Specifications for Level 3
 * @author GOD
 */
public final class Level3 extends Level implements LevelRules
{
    public Level3() throws Exception
    {
        super(GamePlayers.Slash);
        
        setLevelBounds();
        
        setMusic();
        
        setPowerupLimit();
        
        setEnemyLimits();
        
        super.setAutoScrollBackground(true);
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
        int x[] = {0,   3330, 3330, 0};
        int y[] = {123, 123,  190,  190};
        
        //set the area that is in bounds
        super.setBoundary(new Polygon(x, y, x.length));
    }
    
    @Override
    public void setPowerupLimit()
    {
        //this level will have x power ups
        super.setPowerUpLimit(2);
    }
    
    @Override
    public void setMusic()
    {
        super.setMusic(GameAudioMusic.Level3);
        super.setMusicBoss(GameAudioMusic.Level3Boss);
    }
}