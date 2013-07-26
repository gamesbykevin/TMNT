package com.gamesbykevin.tmnt.enemies;

import com.gamesbykevin.framework.base.Sprite;
import com.gamesbykevin.framework.base.SpriteSheetAnimation;

import com.gamesbykevin.tmnt.heroes.Hero;
import com.gamesbykevin.tmnt.player.Player;

import java.util.ArrayList;
import java.util.List;

public class Enemy extends Player
{
    //this is the hero the enemy has targeted
    private int assigned = -1;
    
    //the direction the player will attempt to attack from if not east then west
    private boolean attackEast = false;
    
    //attackTurn means the cpu was able to attack
    private boolean attackTurn = false;
    
    public Enemy()
    {
    }
    
    /**
     * Run the AI here for the enemy
     */
    public void update(List<Hero> heroes)
    {
        super.update();
        
        //make sure enemy has a hero targeted
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
                
                //if attacking from the east side and we aren't on the east side yet
                if (attackEast && getX() < hero.getX() + hero.getWidth())
                {
                    setState(State.WALK_HORIZONTAL);
                    setVelocityX(getVelocityWalk());
                    setVelocityY(VELOCITY_NONE);
                }
                
                //if attacking from the west side and we aren't on the west side yet
                if (!attackEast && getX() > hero.getX() - hero.getWidth())
                {
                    setState(State.WALK_HORIZONTAL);
                    setVelocityX(-getVelocityWalk());
                    setVelocityY(VELOCITY_NONE);
                }
                
                //verify we are on the correct sidenow that we are on the correct side we can fix the y coordinate
                if (attackTurn)
                {
                    if (getVelocityX() == VELOCITY_NONE && !hero.isJumping())
                    {
                        if (getY() + getHeight() < hero.getY() + hero.getHeight() - getVelocityWalk())
                        {
                            setState(State.WALK_VERTICAL);
                            setVelocityX(VELOCITY_NONE);
                            setVelocityY(getVelocityWalk());
                        }

                        if (getY() + getHeight() > hero.getY() + hero.getHeight() + getVelocityWalk())
                        {
                            setState(State.WALK_VERTICAL);
                            setVelocityX(VELOCITY_NONE);
                            setVelocityY(-getVelocityWalk());
                        }
                    }
                    
                    //if we have an attack opportunity go for it
                    if (hasAttackOpportunity(hero))
                    {
                        setVelocityX(VELOCITY_NONE);
                        setVelocityY(VELOCITY_NONE);
                        performAttack(hero);
                        
                        //after the player attacks allow another player the opportunity
                        setAttackTurn(false);
                    }
                }
                
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
     * Does this player attack from the east
     * @param attackEast 
     */
    public void setAttackEast(final boolean attackEast)
    {
        this.attackEast = attackEast;
    }
    
    public boolean hasAttackEast()
    {
        return this.attackEast;
    }
    
    /**
     * Is it the players turn to attack
     * @param attackTurn 
     */
    public void setAttackTurn(final boolean attackTurn)
    {
        this.attackTurn = attackTurn;
    }
    
    public boolean hasAttackTurn()
    {
        return this.attackTurn;
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
        
        //these attacks are close range and will only be applied if he player is close enough
        if (getRectangle().contains(hero.getPoint()))
        {
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
        }
        
        final int rand = (int)(Math.random() * possible.size());
        
        setState(possible.get(rand));
        
        //if random attack is projectile we need to add projectile
        if (possible.get(rand) == State.THROW_PROJECTILE)
        {
            Sprite projectile = new Sprite();
            projectile.setX(getX());
            projectile.setY(getY());
            
            SpriteSheetAnimation animation = super.getSpriteSheet().getSpriteSheetAnimation(State.PROJECTILE1);
            projectile.getSpriteSheet().add(animation, null);
            
            super.setProjectile(projectile);
        }
    }
}