package com.gamesbykevin.tmnt.player;

import com.gamesbykevin.framework.base.Sprite;
import com.gamesbykevin.framework.util.TimerCollection;
import java.awt.Color;

import java.awt.Graphics;
import java.awt.Rectangle;

public abstract class Player extends Sprite
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
    public final static int VELOCITY_NONE = 0;
    
    //# of hits player can take before they die
    private int health = 0;
    
    //this is the projectile object
    private Sprite projectile;
    
    //the projectile speed will be a factor of the player walk speed
    private static final double PROJECTILE_SPEED_RATIO = 5;
    
    public Player()
    {
        super.createSpriteSheet();
        setState(State.IDLE);
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
     * Removes the projectile
     * @param projectile 
     */
    protected void removeProjectile()
    {
        this.projectile = null;
    }

    /**
     * For the sprite sheets they are tiled perfectly so we 
     * can use this formula to get the appropriate coordinates.
     * 
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
    public void setNextState(final State state)
    {
        this.nextState = state;
    }
    
    public State getNextState()
    {
        return this.nextState;
    }
    
    /**
     * Assigns the next state to the current state
     * and then sets the next state to null
     */
    public void applyNextState()
    {
        setState(getNextState());
        setNextState(null);
    }
    
    /**
     * Set the state of this player which also sets the appropriate sprite sheet
     * @param state set what the current Player is doing
     */
    public void setState(final State state)
    {
        this.getSpriteSheet().setCurrent(state);
    }
    
    /**
     * Make sure animation exists.
     * @param state The animation to check
     * @return boolean
     */
    public boolean hasState(final State state)
    {
        return getSpriteSheet().hasAnimation(state);
    }
    
    /**
     * Only reset sprite sheet animation
     */
    public void reset()
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
    
    /**
     * Sets the delay per each update
     * @param delay In nanoseconds
     */
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
    public void update() throws Exception
    {
        getSpriteSheet().update();
        super.update();
        
        if (projectile != null)
        {
            projectile.getSpriteSheet().update();
            projectile.update();
        }
        
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
            //have we reached the peak of our jump
            if (getY() <= getJumpPhase1())
            {
                setVelocityY(-getVelocityY());
            }
            
            //have we reached the landing spot of our jump
            if (getY() >= getJumpPhase2())
            {
                setY(getJumpPhase2());
                setVelocity(VELOCITY_NONE, VELOCITY_NONE);
                reset();
                setState(State.IDLE);
                nextState = null;
            }
        }
        
        if (isAttacking() && !isJumping())
        {
            //if the attack is projectile add the projectile
            if (getState() == State.THROW_PROJECTILE && getSpriteSheet().hasFinished())
            {
                addProjectile();
            }
        }
        
        if (isHurt())
        {
            if (getSpriteSheet().hasFinished())
            {
                setState(State.IDLE);
                reset();
            }
        }
    }
    
    /**
     * Add projectile to throw at player
     */
    private void addProjectile()
    {
        projectile = new Sprite();
        projectile.createSpriteSheet();
        projectile.setLocation(getX(), getY());
        projectile.setDimensions(getWidth(), getHeight());
        projectile.setImage(getImage());

        if (hasHorizontalFlip())
        {
            projectile.setHorizontalFlip(true);
            projectile.setVelocity(-getVelocityWalk() * PROJECTILE_SPEED_RATIO, VELOCITY_NONE);
        }
        else
        {
            projectile.setHorizontalFlip(false);
            projectile.setVelocity(getVelocityWalk() * PROJECTILE_SPEED_RATIO, VELOCITY_NONE);
        }

        //we need the delay or else the animation won't update
        projectile.getSpriteSheet().setDelay(getSpriteSheet().getDelay());
        
        //NOTE: all enemies not including bosses have 1 projectile
        projectile.getSpriteSheet().add(getSpriteSheet().getSpriteSheetAnimation(State.PROJECTILE1), State.PROJECTILE1);
        
        if (getSpriteSheet().hasAnimation(State.PROJECTILE1_FINISH))
        {
            projectile.getSpriteSheet().add(getSpriteSheet().getSpriteSheetAnimation(State.PROJECTILE1_FINISH), State.PROJECTILE1_FINISH);
        }
        
        projectile.getSpriteSheet().setCurrent(State.PROJECTILE1);
    }
    
    /**
     * Is the player jumping
     * @return boolean
     */
    public boolean isJumping()
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
     * Determine if the player is vulnerable to attack/projectile
     * @return boolean
     */
    public boolean canHurt()
    {
        switch(getState())
        {
            //any of these can't hurt the player
            case HURT:
            case DEAD:
            case DEFENSE:
            case JUMP:
            case JUMP_ATTACK:
                return false;
            
            //every thing else can
            default:
                return true;
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
            case THROW_PROJECTILE:
                return true;
                
            default:
                return false;
        }
    }
    
    /**
     * Checks to see if the player has the ability to throw a projectile
     * @return boolean
     */
    protected boolean canThrowProjectile()
    {
        return hasState(State.THROW_PROJECTILE);
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
     * @return Rectangle
     */
    public Rectangle getAnchorLocation()
    {
        offsetCenter();
        
        Rectangle anchor = new Rectangle(getX(), getY() + getHeight() - (int)(getHeight() * .1), getWidth(), (int)(getHeight() * .1));
        
        resetCenter();
            
        return anchor;
    }
    
    /**
     * Gets a Rectangle of the parameter sprite feet with a height of 10% of the total height
     * @param sprite
     * @return Rectangle
     */
    public static Rectangle getAnchorLocation(Sprite sprite)
    {
        offsetCenter(sprite);
        
        Rectangle anchor = new Rectangle(sprite.getX(), sprite.getY() + sprite.getHeight() - (int)(sprite.getHeight() * .1), sprite.getWidth(), (int)(sprite.getHeight() * .1));
        
        resetCenter(sprite);
        
        return anchor;
    }
    
    /**
     * Offsets the x,y coordinates by half the width/height
     */
    private void offsetCenter()
    {
        offsetCenter(this);
        
        //also include projectile
        if (projectile != null)
        {
            offsetCenter(projectile);
        }
    }
    
    private static void offsetCenter(Sprite sprite)
    {
        int halfWidth = (sprite.getWidth() / 2);
        int halfHeight = (sprite.getHeight() / 2);
        
        sprite.setX(sprite.getX() - halfWidth);
        sprite.setY(sprite.getY() - halfHeight);
    }
    
    /**
     * Resets the x,y back to its original coordinates
     */
    private void resetCenter()
    {
        resetCenter(this);
        
        //also include projectile
        if (projectile != null)
        {
            resetCenter(projectile);
        }
    }
    
    private static void resetCenter(Sprite sprite)
    {
        int halfWidth = (sprite.getWidth() / 2);
        int halfHeight = (sprite.getHeight() / 2);
        
        sprite.setX(sprite.getX() + halfWidth);
        sprite.setY(sprite.getY() + halfHeight);
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
        
        if (projectile != null)
            projectile.draw(g);
        
        g.setColor(Color.BLUE);
        g.drawRect(getX(), getY(), getWidth(), getHeight());
        
        if (projectile != null)
        {
            g.setColor(Color.BLUE);
            g.drawRect(projectile.getX(), projectile.getY(), projectile.getWidth(), projectile.getHeight());
        }
    
        resetCenter();
        
        g.setColor(Color.RED);
        Rectangle r = getAnchorLocation();
        g.drawRect(r.x, r.y, r.width, r.height);
        
        return g;
    }
}