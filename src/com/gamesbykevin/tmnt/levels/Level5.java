package com.gamesbykevin.tmnt.levels;

import com.gamesbykevin.tmnt.main.Resources.GameAudioMusic;
import com.gamesbykevin.tmnt.main.Resources.GamePlayers;

import java.awt.Polygon;

/**
 * Specifications for Level 5
 * @author GOD
 */
public final class Level5 extends Level implements LevelRules
{
    public Level5() throws Exception
    {
        super(GamePlayers.Leatherhead);
        
        setLevelBounds();
        
        setMusic();
        
        setPowerupLimit();
        
        setEnemyLimits();

        super.setAutoScrollBackground(false);
    }
    
    @Override
    public void setEnemyLimits() throws Exception
    {
        super.setEnemiesAtOnce(6);
        super.setEnemiesPerCheckpoint(8);
    }
    
    @Override
    public void setLevelBounds()
    {
        int x[] = {0,   1718, 1718, 0};
        int y[] = {128, 128,  223,  223};
        
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
        super.setMusic(GameAudioMusic.Level5);
        super.setMusicBoss(GameAudioMusic.Level5Boss);
    }
}