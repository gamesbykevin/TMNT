package com.gamesbykevin.tmnt.player;

import com.gamesbykevin.framework.base.Sprite;
import com.gamesbykevin.framework.util.TimerCollection;

import com.gamesbykevin.tmnt.main.ResourceManager.GamePlayers;

import java.awt.Point;
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
    
    //we want to know what player this is so we can assign the appropriate assets
    private GamePlayers type;
    
    //velocity for the appropriate state
    private int velocityWalk = 0;
    private int velocityJump = 0;
    
    //jump phase 1, y coordinate where jump needs to peak
    //jump phase 2, y coordinate where jump needs to stop
    private Point jumpPhase1;
    private Point jumpPhase2;
    
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
    
    //number of frames to execute jump
    public static final int NUM_FRAMES_JUMP = 30;
    
    public Player()
    {
        //create sprite sheet
        super.createSpriteSheet();
        
        //assign default state of idle
        super.getSpriteSheet().setCurrent(State.IDLE);
    }
    
    public void setType(final GamePlayers type)
    {
        this.type = type;
    }
    
    public GamePlayers getType()
    {
        return this.type;
    }
    
    /**
     * Get the projectile the enemy has. If the enemy 
     * does not have a projectile null will be returned.
     * @return Sprite
     */
    public Sprite getProjectile()
    {
        return this.projectile;
    }
    
    public boolean hasProjectile()
    {
        return (projectile != null);
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
    protected void setJumpPhase1(final int x, final int y)
    {
        this.jumpPhase1 = new Point(x, y);
    }
    
    /**
     * This will contain the Y peak coordinate
     * @return Point with the Y coordinate where we the jump has peaked and we need to start declining
     */
    protected Point getJumpPhase1()
    {
        return this.jumpPhase1;
    }
    
    /**
     * Set the jump phase 2 so we know when the jump finish is
     * @param jumpPhase2 This is the jump finish
     */
    protected void setJumpPhase2(final int x, final int y)
    {
        this.jumpPhase2 = new Point(x, y);
    }
    
    /**
     * This will contain the Y finish coordinate
     * @return Point with the Y coordinate where we need to stop the jumping at
     */
    protected Point getJumpPhase2()
    {
        return this.jumpPhase2;
    }
    
    /**
     * Is the player's next move another attack
     * so we can chain together a combo then set 
     * the next attack here.
     * @param state Set what the current Player should do next
     */
    public void setNextState(final State nextState)
    {
        this.nextState = nextState;
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
        setNewState(getNextState());
        setNextState(null);
    }
    
    /**
     * This method calls setState() and once 
     * setState() is called the sprite sheet is reset.
     * 
     * @param state What the current Player is doing
     */
    public void setNewState(final State state)
    {
        setState(state);
        reset();
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
    private void reset()
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
     * Check the current animation and handle accordingly
     */
    private void manageState()
    {
        if (isJumping())
        {
            //have we reached the peak of our jump
            if (getY() <= getJumpPhase1().getY())
            {
                setVelocityY(-getVelocityY());
            }
            
            //have we reached the landing spot of our jump
            if (getY() >= getJumpPhase2().getY())
            {
                setY(getJumpPhase2().getY());
                setVelocity(VELOCITY_NONE, VELOCITY_NONE);
                setNewState(State.IDLE);
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
                setNewState(State.IDLE);
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
        projectile.getSpriteSheet().reset();
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
     * Gets a Rectangle of the parameter sprite feet with a height of 10% of the total height
     * 
     * @return Rectangle The rectangle containing the players feet
     */
    public Rectangle getAnchorLocation()
    {
        return getAnchorLocation(this);
    }
    
    /**
     * Gets a Rectangle of the parameter sprite feet with a height of 10% of the total height
     * @param sprite The object we want the anchor location from
     * @return Rectangle The rectangle containing the players feet
     */
    public static Rectangle getAnchorLocation(Sprite sprite)
    {
        final int halfWidth  = (sprite.getWidth()  / 2);
        final int halfHeight = (sprite.getHeight() / 2);
        
        sprite.setX(sprite.getX() - halfWidth);
        sprite.setY(sprite.getY() - halfHeight);
        
        Rectangle tmp = new Rectangle(sprite.getX(), sprite.getY() + sprite.getHeight() - (int)(sprite.getHeight() * .1), sprite.getWidth(), (int)(sprite.getHeight() * .1));
        
        sprite.setX(sprite.getX() + halfWidth);
        sprite.setY(sprite.getY() + halfHeight);
        
        return tmp;
    }
}