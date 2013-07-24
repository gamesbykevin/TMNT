package com.gamesbykevin.tmnt.enemies;

import com.gamesbykevin.framework.base.Sprite;

import com.gamesbykevin.tmnt.heroes.Hero;
import com.gamesbykevin.tmnt.player.Player;

import java.util.List;

public class Enemy extends Player
{
    //this is the hero the enemy has targeted
    private int assigned = -1;
    
    //this is the projectile object
    private Sprite projectile;
    
    public Enemy()
    {
    }
    
    /**
     * Get the projectile the enemy has. If the enemy 
     * does not have a projectile null will be returned.
     * @return Sprite
     */
    protected Sprite getProjectile()
    {
        return this.projectile;
    }
    
    /**
     * Run the AI here for the enemy
     */
    public void update(List<Hero> heroes)
    {
        super.update();
        
        //make sure enemy has hero targeted
        checkAssignment(heroes.size());
        
        Hero hero = heroes.get(assigned);
        
        //make sure the hero isn't hurt or dead
        if (!hero.isDead() && !hero.isHurt())
        {
            
        }
    }
    
    /**
     * If the enemy is not assigned a hero to attack do so now
     * @param size Total number of heroes to choose from
     */
    private void checkAssignment(final int size)
    {
        if (assigned < 0 || assigned >= size)
            assigned = (int)(Math.random() * size);
    }
}