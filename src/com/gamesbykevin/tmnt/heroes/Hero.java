package com.gamesbykevin.tmnt.heroes;

import com.gamesbykevin.framework.input.Keyboard;

import com.gamesbykevin.tmnt.enemies.Enemy;
import com.gamesbykevin.tmnt.levels.Level;
import com.gamesbykevin.tmnt.player.Player;

import java.awt.event.KeyEvent;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.List;

public class Hero extends Player
{
    private Point validLocation;
    
    public Hero()
    {
        validLocation = new Point();
    }
    
    /**
     * This player is human so we need to check keyboard input
     * @param keyboard Our object that records keyboard events
     */
    public void update(final Keyboard keyboard, List<Enemy> enemies, final Level level) throws Exception
    {
        //record our location before update is called
        if (!isJumping())
        {
            //make note of last valid location in case player is out of bounds
            if (level.getBoundary().contains(getAnchorLocation()))
                validLocation = super.getPoint();
        }
        else
        {
            
        }
        
        super.update();
        
        //now that update has been called make sure we are still within the boundary
        if (!isJumping())
        {
            //if the player is not jumping their anchor location needs to be inside the level boundary
            if (!level.getBoundary().contains(getAnchorLocation()))
                super.setLocation(validLocation);
        }
        else
        {
            
        }
        
        if (isAttacking())
            checkAttack(enemies);
        
        if (keyboard.hasKeyPressed(KeyEvent.VK_RIGHT))
        {
            if (canWalk())
            {
                super.setHorizontalFlip(false);
                setVelocity(getVelocityWalk(), VELOCITY_NONE);
                getSpriteSheet().setCurrent(State.WALK_HORIZONTAL);
            }
        }

        if (keyboard.hasKeyPressed(KeyEvent.VK_LEFT))
        {
            if (canWalk())
            {
                super.setHorizontalFlip(true);
                setVelocity(-getVelocityWalk(), VELOCITY_NONE);
                getSpriteSheet().setCurrent(State.WALK_HORIZONTAL);
            }
        }

        if (keyboard.hasKeyPressed(KeyEvent.VK_UP))
        {
            if (canWalk())
            {
                setVelocity(VELOCITY_NONE, -getVelocityWalk());
                getSpriteSheet().setCurrent(State.WALK_VERTICAL);
            }
        }

        if (keyboard.hasKeyPressed(KeyEvent.VK_DOWN))
        {
            if (canWalk())
            {
                setVelocity(VELOCITY_NONE, getVelocityWalk());
                getSpriteSheet().setCurrent(State.WALK_VERTICAL);
            }
        }

        if (keyboard.hasKeyPressed(KeyEvent.VK_A))
        {
            if (canJump())
            {
                setVelocity(getVelocityX(), -getVelocityJump());
                setNewState(State.JUMP);
                setJumpPhase1(getX(), getY() - (getVelocityJump() * Player.NUM_FRAMES_JUMP));
                setJumpPhase2(getX(), getY());
            }
        }

        if (keyboard.hasKeyPressed(KeyEvent.VK_S))
        {
            if (canAttack())
            {
                if (isJumping())
                {
                    setState(State.JUMP_ATTACK);
                    
                    if (getVelocityY() < 0)
                        setVelocityY(-getVelocityY());
                }
                else
                {
                    setNewState(State.ATTACK1);
                    setVelocity(VELOCITY_NONE, VELOCITY_NONE);
                    keyboard.removeKeyPressed(KeyEvent.VK_S);
                }
            }
            else
            {
                //if already attacking we want to know to do the next attack move
                if (isAttacking())
                {
                    switch(getState())
                    {
                        case ATTACK1:
                            if (getSpriteSheet().hasAnimation(State.ATTACK2))
                                setNextState(State.ATTACK2);
                            break;
                            
                        case ATTACK2:
                            if (getSpriteSheet().hasAnimation(State.ATTACK3))
                                setNextState(State.ATTACK3);
                            break;
                            
                        case ATTACK3:
                            if (getSpriteSheet().hasAnimation(State.ATTACK4))
                                setNextState(State.ATTACK4);
                            break;
                            
                        case ATTACK4:
                            if (getSpriteSheet().hasAnimation(State.ATTACK5))
                                setNextState(State.ATTACK5);
                            break;
                    }
                    
                    keyboard.removeKeyPressed(KeyEvent.VK_S);
                }
            }
        }

        if (keyboard.hasKeyReleased(KeyEvent.VK_LEFT) || keyboard.hasKeyReleased(KeyEvent.VK_RIGHT))
        {
            if (isWalking() || isIdle())
            {
                setState(State.IDLE);
                setVelocity(VELOCITY_NONE, VELOCITY_NONE);
                keyboard.resetAllKeyEvents();
            }
        }

        if (keyboard.hasKeyReleased(KeyEvent.VK_UP) || keyboard.hasKeyReleased(KeyEvent.VK_DOWN))
        {
            if (isWalking() || isIdle())
            {
                setState(State.IDLE);
                setVelocity(VELOCITY_NONE, VELOCITY_NONE);
                keyboard.resetAllKeyEvents();
            }
        }
    }
    
    private void checkAttack(List<Enemy> enemies)
    {
        //if the attacking animation is finished check for collision and reset animation
        if (getSpriteSheet().hasFinished())
        {
            for (Enemy enemy : enemies)
            {
                if (!enemy.canHurt())
                    continue;

                Rectangle anchorHero = getAnchorLocation();
                Rectangle anchorEnemy = enemy.getAnchorLocation();

                //we have made collision with the enemy
                if (anchorHero.intersects(anchorEnemy) && getRectangle().contains(enemy.getCenter()) && !hasState(State.THROW_PROJECTILE))
                {
                    //make sure hero is facing the enemy, NOTE: even though we hit the enemy do not exit loop because we may damage multiple
                    if (enemy.getCenter().x >= getCenter().x && !hasHorizontalFlip() ||
                        enemy.getCenter().x <= getCenter().x && hasHorizontalFlip())
                    {
                        enemy.setHorizontalFlip(!hasHorizontalFlip());
                        enemy.setNewState(State.HURT);
                        enemy.setVelocity(VELOCITY_NONE, VELOCITY_NONE);
                    }
                }
            }

            //since the attack animation is finished reset it, but not if the hero is jumping
            if (!isJumping())
            {
                //once we are complete jumping we go back to idle
                setNewState(State.IDLE);
                
                //if there is another state
                if (getNextState() != null)
                    applyNextState();
            }
        }
    }
}