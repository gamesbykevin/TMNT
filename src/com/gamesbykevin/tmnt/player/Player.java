package com.gamesbykevin.tmnt.player;

import com.gamesbykevin.framework.base.Sprite;
import com.gamesbykevin.framework.util.TimerCollection;
import java.awt.Color;

import java.awt.Graphics;
import java.awt.Rectangle;

public class Player extends Sprite
{
    /**
     * The state will define what the Player is doing
     */
    public enum State
    {
        WALK_HORIZONTAL, WALK_VERTICAL, 
        IDLE, 
        ATTACK1, ATTACK2, ATTACK3, ATTACK4, ATTACK5, 
        JUMP, JUMP_ATTACK, 
        THROW_PROJECTILE, 
        PROJECTILE1, PROJECTILE1_FINISH, 
        PROJECTILE2, 
        HURT, 
        DEAD,
        DEFENSE
    }
    
    //velocity for the appropriate state
    private int velocityWalk = 0;
    private int velocityJump = 0;
    
    //jump phase 1, y coordinate where jump needs to peak
    //jump phase 2, y coordinate where jump needs to stop
    private int jumpPhase1 = 0;
    private int jumpPhase2 = 0;
    
    //this is to check if we are to chain attack states together
    private State nextState;
    
    //no velocity
    protected final static int VELOCITY_NONE = 0;
    
    //# of hits player can take before they die
    private int health = 0;
    
    public Player()
    {
        setState(State.IDLE);
    }

    /**
     * For the sprite sheets they are tiled 
     * perfectly so we can use this formula 
     * to get the appropriate coordinates.
     * @param col Column
     * @param row Row
     * @return Rectangle of coordinates
     */
    protected final Rectangle getSpriteRectangle(final int col, final int row)
    {
        return new Rectangle(col * getWidth(), row * getHeight(), getWidth(), getHeight());
    }
    
    protected void setHealth(final int health)
    {
        this.health = health;
    }
    
    protected int getHealth()
    {
        return this.health;
    }
    
    /**
     * Set the speed itself that the player can move for walking
     * @param x Amount of pixels per step
     */
    protected void setVelocityWalk(final int x)
    {
        this.velocityWalk = x;
    }
    
    protected int getVelocityWalk()
    {
        return this.velocityWalk;
    }
    
    /**
     * Set the speed for jumping
     * @param y Amount of pixels per jump
     */
    protected void setVelocityJump(final int y)
    {
        this.velocityJump = y;
    }
    
    protected int getVelocityJump()
    {
        return this.velocityJump;
    }
    
    /**
     * Set the jump phase 1 so we know when the peak of the jump is
     * @param jumpPhase1 This is the jump peak
     */
    protected void setJumpPhase1(final int jumpPhase1)
    {
        this.jumpPhase1 = jumpPhase1;
    }
    
    /**
     * Get the Y peak coordinate
     * @return int The Y coordinate where we the jump has peaked and we need to start declining
     */
    protected int getJumpPhase1()
    {
        return this.jumpPhase1;
    }
    
    /**
     * Set the jump phase 2 so we know when the jump finish is
     * @param jumpPhase2 This is the jump finish
     */
    protected void setJumpPhase2(final int jumpPhase2)
    {
        this.jumpPhase2 = jumpPhase2;
    }
    
    /**
     * Get the Y finish coordinate
     * @return int The Y coordinate where we need to stop the jumping at
     */
    protected int getJumpPhase2()
    {
        return this.jumpPhase2;
    }
    
    /**
     * Is the player's next move another attack
     * so we can chain together a combo then set 
     * the next attack here.
     * @param state set what the current Player is doing
     */
    protected void setNextState(final State state)
    {
        this.nextState = state;
    }
    
    protected State getNextState()
    {
        return this.nextState;
    }
    
    /**
     * Set the state of this player
     * @param state set what the current Player is doing
     */
    public void setState(final State state)
    {
        this.getSpriteSheet().setCurrent(state);
    }
    
    protected void reset()
    {
        this.getSpriteSheet().reset();
    }
    
    /**
     * What is this player currently doing
     * @return State What the current Player is doing
     */
    public State getState()
    {
        return (State)this.getSpriteSheet().getCurrent();
    }
    
    /**
     * Convert milliseconds to nanoseconds
     * @param milliseconds Number of milliseconds
     * @return long nanoseconds
     */
    protected long getNanoSeconds(long milliseconds)
    {
        return TimerCollection.toNanoSeconds(milliseconds);
    }
    
    public void setDelay(final long delay)
    {
        this.getSpriteSheet().setDelay(delay);
    }
    
    /**
     * Updates the current animation for the player
     * as well as the location based on the current
     * velocity set. Also updates the timers
     */
    @Override
    public void update()
    {
        getSpriteSheet().update();
        super.update();
        
        //manage miscallaneous stuff here
        manageState();
    }
    
    /**
     * Check the animations for miscellaneous things
     */
    private void manageState()
    {
        if (isJumping())
        {
            if (getY() <= this.jumpPhase1)
            {
                setVelocityY(-getVelocityY());
            }
            
            if (getY() >= this.jumpPhase2)
            {
                setY(this.jumpPhase2);
                setVelocity(VELOCITY_NONE, VELOCITY_NONE);
                reset();
                setState(State.IDLE);
                nextState = null;
            }
        }
        
        if (isAttacking() && !isJumping())
        {
            //if the attacking animation is finished
            if (getSpriteSheet().hasFinished())
            {
                reset();
                setState(State.IDLE);
                
                if (nextState != null)
                {
                    setState(nextState);
                    nextState = null;
                }
            }
        }
    }
    
    /**
     * Is the player jumping
     * @return boolean
     */
    protected boolean isJumping()
    {
        switch(getState())
        {
            case JUMP:
            case JUMP_ATTACK:
                return true;
                
            default:
                return false;
        }
    }
    
    /**
     * Is the player dead
     * @return boolean
     */
    public boolean isDead()
    {
        switch(getState())
        {
            case DEAD:
                return true;
                
            default:
                return false;
        }
    }
    
    /**
     * Is the player hurt
     * @return boolean
     */
    public boolean isHurt()
    {
        switch(getState())
        {
            case HURT:
                return true;
                
            default:
                return false;
        }
    }
    
    /**
     * Is the player walking
     * @return boolean
     */
    protected boolean isWalking()
    {
        switch(getState())
        {
            case WALK_HORIZONTAL:
            case WALK_VERTICAL:
                return true;
                
            default:
                return false;
        }
    }
    
    /**
     * Determine if the player can walk
     * @return boolean
     */
    protected boolean canWalk()
    {
        switch(getState())
        {
            case IDLE:
                return true;
                
            default:
                return false;
        }
    }
    
    /**
     * CAn the player jump
     * @return boolean
     */
    protected boolean canJump()
    {
        switch(getState())
        {
            case IDLE:
            case WALK_HORIZONTAL:
            case WALK_VERTICAL:
                return true;
            
            default:
                return false;
        }
    }
    
    /**
     * Determine if the player can attack
     * @return boolean
     */
    protected boolean canAttack()
    {
        switch(getState())
        {
            case WALK_HORIZONTAL:
            case WALK_VERTICAL:
            case IDLE:
            case JUMP:
                return true;
                
            default:
                return false;
        }
    }
    
    /**
     * Is the player currently attacking
     * @return boolean
     */
    protected boolean isAttacking()
    {
        switch(getState())
        {
            case ATTACK1:
            case ATTACK2:
            case ATTACK3:
            case ATTACK4:
            case ATTACK5:
            case JUMP_ATTACK:
                return true;
                
            default:
                return false;
        }
    }
    
    /**
     * Is the player idle
     * @return boolean
     */
    protected boolean isIdle()
    {
        switch(getState())
        {
            case IDLE:
                return true;
                
            default:
                return false;
        }
    }
    
    /**
     * Gets a Rectangle of the players feet with a height of 10% of the total height
     * @return 
     */
    public Rectangle getAnchorLocation()
    {
        offsetCenter();
        
        Rectangle anchor = new Rectangle(getX(), getY() + getHeight() - (int)(getHeight() * .1), getWidth(), (int)(getHeight() * .1));
        
        resetCenter();
            
        return anchor;
    }
    
    /**
     * Offsets the x,y coordinates by half the width/height
     */
    private void offsetCenter()
    {
        int halfWidth = (getWidth() / 2);
        int halfHeight = (getHeight() / 2);
        
        super.setX(super.getX() - halfWidth);
        super.setY(super.getY() - halfHeight);
    }
    
    /**
     * Resets the x,y back to its original coordinates
     */
    private void resetCenter()
    {
        int halfWidth = (getWidth() / 2);
        int halfHeight = (getHeight() / 2);
        
        super.setX(super.getX() + halfWidth);
        super.setY(super.getY() + halfHeight);
    }
    
    /**
     * Draw the object
     * @param g Graphics
     * @return Graphics
     */
    public Graphics render(Graphics g)
    {
        offsetCenter();
        
        super.draw(g);
        
        g.setColor(Color.BLUE);
        g.drawRect(getX(), getY(), getWidth(), getHeight());
        
        resetCenter();
        
        g.setColor(Color.RED);
        Rectangle r = this.getAnchorLocation();
        g.drawRect(r.x, r.y, r.width, r.height);
        
        return g;
    }
}