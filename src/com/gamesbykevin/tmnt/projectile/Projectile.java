package com.gamesbykevin.tmnt.projectile;

import com.gamesbykevin.framework.base.Sprite;

import com.gamesbykevin.tmnt.main.ResourceManager.GamePlayers;

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
}
