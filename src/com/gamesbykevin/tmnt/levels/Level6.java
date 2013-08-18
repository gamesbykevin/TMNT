package com.gamesbykevin.tmnt.levels;

import com.gamesbykevin.tmnt.main.Resources.GameAudioMusic;
import com.gamesbykevin.tmnt.main.Resources.GamePlayers;

import java.awt.Polygon;

/**
 * Specifications for Level 6
 * @author GOD
 */
public final class Level6 extends Level implements LevelRules
{
    public Level6() throws Exception
    {
        super(GamePlayers.Shredder);
        
        setLevelBounds();
        
        setMusic();
        
        setPowerupLimit();
        
        setEnemyLimits();
        
        //are using a separate background
        super.setAutoScrollBackground(false);
    }
    
    @Override
    public void setEnemyLimits() throws Exception
    {
        super.setEnemiesAtOnce(0);
        super.setEnemiesPerCheckpoint(0);
    }
    
    @Override
    public void setLevelBounds()
    {
        int x[] = {0,   1010, 1010, 0};
        int y[] = {142, 142,  222,  222};
        
        //set the area that is in bounds
        super.setBoundary(new Polygon(x, y, x.length));
    }
    
    @Override
    public void setPowerupLimit()
    {
        //this level will have x power ups
        super.setPowerUpLimit(1);
    }
    
    @Override
    public void setMusic()
    {
        //no level music
        super.setMusic(null);
        
        super.setMusicBoss(GameAudioMusic.Level6Boss);
    }
}