package com.gamesbykevin.tmnt.enemies;

import com.gamesbykevin.framework.base.Sprite;

import com.gamesbykevin.tmnt.heroes.Hero;
import com.gamesbykevin.tmnt.player.Player;

import java.util.ArrayList;
import java.util.List;

public class Enemy extends Player
{
    //this is the hero the enemy has targeted
    private int assigned = -1;
    
    //this is the projectile object
    private Sprite projectile;
    
    //the direction the player will attempt to attack from
    private boolean attackEast = false, attackWest = false;
    
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
                if (getX() < hero.getX())
                    setHorizontalFlip(false);
                else
                    setHorizontalFlip(true);
                
                setState(State.IDLE);
                setVelocityX(VELOCITY_NONE);
                setVelocityY(VELOCITY_NONE);
                
                if (attackEast)
                {
                    if (getX() < hero.getX() + hero.getWidth())
                    {
                        setState(State.WALK_HORIZONTAL);
                        setVelocityX(getVelocityWalk());
                        setVelocityY(VELOCITY_NONE);
                    }
                    else
                    {
                        //now that we are on the correct side we can fix the y coordinate
                        if (getY() < hero.getY() - getVelocityWalk())
                        {
                            setState(State.WALK_VERTICAL);
                            setVelocityX(VELOCITY_NONE);
                            setVelocityY(getVelocityWalk());
                        }
                        
                        if (getY() > hero.getY() + getVelocityWalk())
                        {
                            setState(State.WALK_VERTICAL);
                            setVelocityX(VELOCITY_NONE);
                            setVelocityY(-getVelocityWalk());
                        }
                    }
                }
                
                if (attackWest)
                {
                    if (getX() > hero.getX() - hero.getWidth())
                    {
                        setState(State.WALK_HORIZONTAL);
                        setVelocityX(-getVelocityWalk());
                        setVelocityY(VELOCITY_NONE);
                    }
                    else
                    {
                        //now that we are on the correct side we can fix the y coordinate
                        if (!hero.isJumping())
                        if (getY() < hero.getY() - getVelocityWalk())
                        {
                            setState(State.WALK_VERTICAL);
                            setVelocityX(VELOCITY_NONE);
                            setVelocityY(getVelocityWalk());
                        }
                        
                        if (getY() > hero.getY() + getVelocityWalk())
                        {
                            setState(State.WALK_VERTICAL);
                            setVelocityX(VELOCITY_NONE);
                            setVelocityY(-getVelocityWalk());
                        }
                    }
                }
                
                //if we have an attack opportunity go for it
                if (hasAttackOpportunity(hero))
                    performAttack(hero);
            }
        }
        else
        {
            if (hero.isDead())
            {
                //if the hero is dead and the enemy is not jumping set them to idle state
                if (!isJumping())
                {
                    setState(State.IDLE);
                    setVelocity(Player.VELOCITY_NONE, Player.VELOCITY_NONE);
                }
            }
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
            
            //attack pattern is random
            this.attackEast = (Math.random() < .5);
            this.attackWest = !this.attackEast;
        }
    }
    
    /**
     * Checks if the enemy can attack the hero
     * @param hero
     * @return 
     */
    private boolean hasAttackOpportunity(final Hero hero)
    {
        //if the enemy is currently attacking then there is no new opportunity to attack
        if (isAttacking())
            return false;
        
        //if the enemy bounds contains the center of the hero we can attack
        if (getRectangle().contains(hero.getPoint()))
            return true;
        
        //if the enemy y coordinate is within the hero y coordinate
        if (getY() > hero.getY() - getVelocityWalk() && getY() < hero.getY() + getVelocityWalk())
        {
            //check if player has ability to throw a projectile
            if (super.canThrowProjectile())
                return true;
        }
        
        return false;
    }
    
    /**
     * Choose the appropriate attack at random
     * @param hero Hero we need
     */
    private void performAttack(final Hero hero)
    {
        List<State> possible = new ArrayList<>();
        
        if (hasState(State.THROW_PROJECTILE))
            possible.add(State.THROW_PROJECTILE);
        if (hasState(State.ATTACK1))
            possible.add(State.ATTACK1);
        if (hasState(State.ATTACK2))
            possible.add(State.ATTACK2);
        if (hasState(State.ATTACK3))
            possible.add(State.ATTACK3);
        if (hasState(State.ATTACK4))
            possible.add(State.ATTACK4);
        if (hasState(State.ATTACK5))
            possible.add(State.ATTACK5);
        
        final int rand = (int)(Math.random() * possible.size());
        
        setState(possible.get(rand));
        
        //if random attack is projectile we need to add projectile
        if (possible.get(rand) == State.THROW_PROJECTILE)
        {
            
        }
    }
}