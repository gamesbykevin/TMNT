package com.gamesbykevin.tmnt.projectile;

import com.gamesbykevin.framework.base.Sprite;

import com.gamesbykevin.tmnt.main.ResourceManager.GamePlayers;
import com.gamesbykevin.tmnt.player.PlayerManager;

public class Projectile extends Sprite
{
    //which type of enemy created the projectile
    private final GamePlayers source;
    
    public Projectile(final GamePlayers source)
    {
        this.source = source;
    }
    
    /**
     * Return the type of Player that created the projectile
     * @return 
     */
    public GamePlayers getSource()
    {
        return this.source;
    }
    
    /**
     * Tells if this projectile came from an enemy or boss
     * @return boolean
     */
    public boolean isEnemySource()
    {
        return PlayerManager.isEnemy(getSource());
    }
}
