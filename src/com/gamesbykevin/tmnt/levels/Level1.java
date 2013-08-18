package com.gamesbykevin.tmnt.levels;

import com.gamesbykevin.tmnt.main.Resources.GameAudioMusic;
import com.gamesbykevin.tmnt.main.Resources.GamePlayers;

import java.awt.Polygon;

/**
 * Specifications for Level 1
 * @author GOD
 */
public final class Level1 extends Level implements LevelRules
{
    public Level1() throws Exception
    {
        super(GamePlayers.Bebop);
        
        setLevelBounds();
        
        setMusic();
        
        setPowerupLimit();
        
        setEnemyLimits();
        
        super.setAutoScrollBackground(false);
    }
    
    @Override
    public void setEnemyLimits() throws Exception
    {
        super.setEnemiesAtOnce(4);
        super.setEnemiesPerCheckpoint(8);
    }
    
    @Override
    public void setLevelBounds()
    {
        int x[] = {0,   2364, 2364, 0};
        int y[] = {142,  142, 224,  224};
        
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
        super.setMusic(GameAudioMusic.Level1);
        super.setMusicBoss(GameAudioMusic.Level1Boss);
    }
}