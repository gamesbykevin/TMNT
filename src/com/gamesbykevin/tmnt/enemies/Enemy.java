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
        checkAssignment(heroes);
        
        //get the hero the enemy is assigned to attack
        Hero hero = heroes.get(assigned);
        
        //make sure the hero isn't hurt or dead
        if (!hero.isDead() && !hero.isHurt())
        {
            if (canWalk() || isWalking())
            {
                //face the direction walking
                super.setHorizontalFlip(false);
                
                if (getX() < hero.getX())
                {
                    super.setState(State.WALK_HORIZONTAL);
                    super.setVelocityX(super.getVelocityWalk());
                }
                
                if (getX() > hero.getX())
                {
                    super.setHorizontalFlip(true);
                    super.setState(State.WALK_HORIZONTAL);
                    super.setVelocityX(-super.getVelocityWalk());
                }
            }
        }
        else
        {
            //if the hero is dead
        }
    }
    
    /**
     * If the enemy is not assigned a hero to attack do so now.
     * @param size Total number of heroes to choose from
     */
    private void checkAssignment(final List<Hero> heroes)
    {
        if (assigned < 0 || assigned >= heroes.size())
        {
            //now we have an assigned target
            assigned = (int)(Math.random() * heroes.size());
            
            //choose attack pattern
            //1. attack from front
            //2. attack from back
            
            
        }
    }
}