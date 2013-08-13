package com.gamesbykevin.tmnt.projectile;

import com.gamesbykevin.framework.base.Sprite;

import com.gamesbykevin.tmnt.main.ResourceManager.GamePlayers;
import com.gamesbykevin.tmnt.boss.BossManager;
import com.gamesbykevin.tmnt.grunt.GruntManager;

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
     * Tells if this projectile came from an enemy
     * @return boolean
     */
    public boolean isEnemySource()
    {
        return BossManager.isBoss(getSource());
    }
    
    /**
     * Tells if this projectile came from a boss
     * @return boolean
     */
    public boolean isBossSource()
    {
        return GruntManager.isGrunt(getSource());
    }
}
