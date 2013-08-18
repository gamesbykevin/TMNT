package com.gamesbykevin.tmnt.grunt;

import com.gamesbykevin.tmnt.boss.Boss;
import com.gamesbykevin.tmnt.boss.BossManager;
import com.gamesbykevin.tmnt.main.Engine;
import com.gamesbykevin.tmnt.main.Resources.GamePlayers;
import com.gamesbykevin.tmnt.player.Player;
import com.gamesbykevin.tmnt.player.PlayerManager.Keys;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.awt.Image;
import java.util.ArrayList;
import java.util.List;

public class Grunt extends Player
{
    //the direction the player will attempt to attack from if not east then west
    private boolean attackEast;
    
    //the steps the enemy will take to attack hero
    private boolean step1 = true, step2 = false, step3 = false;
    
    //each enemy will die after 4 hits
    private static final int HEALTH_DEFAULT = 4;
    
    //each enemy will start at 0 extra lives
    private static final int LIVES_DEFAULT = 0;
    
    //heroes we are targeting
    private List<Player> heroes;
    
    //the flashing image of the enemy boss
    private BufferedImage flashing;
    
    //keep track if we are displaying flashing image or not
    private boolean flash;
    
    //the original image
    private Image original;
    
    //the hero the enemy is targeting
    private Player hero;
    
    public static final int PROJECTILE_LIMIT_DEFAULT = 1;
    
    protected static final int VELOCITY_WALK = 1;

    public Grunt(GamePlayers type)
    {
        super(type);
        
        super.setHealthDefault(HEALTH_DEFAULT);
        super.setLives(LIVES_DEFAULT);
        
        this.resetSteps();
    }
    
    @Override
    public void dispose()
    {
        super.dispose();
        
        if (original != null)
            original.flush();
        if (flashing != null)
            flashing.flush();
        
        original = null;
        flashing = null;
    }
    
    /**
     * Run the AI here for the enemy
     */
    public void update(final Engine engine) throws Exception
    {
        heroes = engine.getPlayerManager().getHeroManager().getPlayerHeroes();
        final Rectangle screen = engine.getMain().getScreen();
        
        //call this update first as it manages the animation and player state
        super.update(engine, heroes);
        
        //if this grunt is a boss check if we need to apply the flashing image
        if (BossManager.isBoss(getType()))
            checkFlash(engine);
        
        //no heroes
        if (heroes.size() < 1 || getAssignment() == null)
            return;
        
        //get the hero that the enemy is targeting
        hero = getAssignment(heroes);
        
        //no assigned target
        if (hero == null)
            return;
        
        //make sure the hero isn't hurt or dead
        if (!hero.isDead() && !hero.isHurt() && !isDead() && !isHurt())
        {
            //face the hero
            if (getX() < hero.getX())
                setHorizontalFlip(false);
            else
                setHorizontalFlip(true);
            
            //if we are attacking check for collision as well as reset animation
            if (!isAttacking())
            {
                //make sure enemy can walk or if they are walking to follow the logic below
                if (canWalk() || isWalking())
                {
                    //reset state and velocity
                    setState(State.IDLE);
                    setVelocity(Player.VELOCITY_NONE, Player.VELOCITY_NONE);

                    Rectangle anchor = getAnchorLocation();
                    Rectangle anchorHero = hero.getAnchorLocation();

                    //surround the enemy first
                    if (step1)
                        surroundEnemy(hero);

                    //next, line up the y-cooridnate to get ready to attack
                    if (!step1 && step2)
                        lineAttack(anchor, anchorHero, hero.isJumping());

                    //then, close/spread the gap
                    if (!step1 && !step2 && step3)
                        closeGap(anchor, anchorHero, hero.getX());

                    //here we check for the opportunity to attack
                    final State attackState = getAttackOpportunity(anchor, anchorHero, hero.getCenter(), hero.isJumping());

                    //if there's an opportunity, take it as long as they are on the screen
                    if (attackState != null && screen.contains(super.getCenter()))
                    {
                        //while attacking the enemy isn't moving
                        setVelocityX(VELOCITY_NONE);
                        setVelocityY(VELOCITY_NONE);

                        //when the player attacks it is no longer their turn
                        resetSteps();

                        //start attack here
                        setNewState(attackState);
                    }
                }
            }
        }
        else
        {
            //is the hero hurt reset animation back to idle
            if (hero.isHurt())
            {
                if (isAttacking() && !getSpriteSheet().hasStarted())
                {
                    setNewState(State.IDLE);
                    setVelocity(Player.VELOCITY_NONE, Player.VELOCITY_NONE);
                }
            }
            
            //make sure this enemy is not dead and not hurt
            if (!isDead() && !isHurt())
            {
                //if the hero is dead and the enemy is not jumping set them to an idle state
                if (hero.isDead() && !isJumping())
                {
                    setState(State.IDLE);
                    setVelocity(Player.VELOCITY_NONE, Player.VELOCITY_NONE);
                }

                //if the enemy is attacking and animation is finished reset back to idle
                if (isAttacking() && getSpriteSheet().hasFinished())
                {
                    setNewState(State.IDLE);
                    setVelocity(Player.VELOCITY_NONE, Player.VELOCITY_NONE);
                }
            }
        }
    }
    
    /**
     * Alternate between images as the boss health declines.
     * 
     * @param engine 
     */
    private void checkFlash(final Engine engine)
    {
        //if the health is below the first warning, we don't want to pause animation
        if (super.getHealth() <= Boss.HEALTH_WARNING_1)
            engine.getPlayerManager().getTimer(Keys.BossFlash).setPause(false);
        
        //set the appropriate remaining time
        if (super.getHealth() <= Boss.HEALTH_WARNING_1)
            engine.getPlayerManager().getTimer(Keys.BossFlash).setReset(Boss.BOSS_FLASH_DELAY_1);
        if (super.getHealth() <= Boss.HEALTH_WARNING_2)
            engine.getPlayerManager().getTimer(Keys.BossFlash).setReset(Boss.BOSS_FLASH_DELAY_2);
        if (super.getHealth() <= Boss.HEALTH_WARNING_3)
            engine.getPlayerManager().getTimer(Keys.BossFlash).setReset(Boss.BOSS_FLASH_DELAY_3);
        if (super.getHealth() <= Boss.HEALTH_WARNING_4)
            engine.getPlayerManager().getTimer(Keys.BossFlash).setReset(Boss.BOSS_FLASH_DELAY_4);
        
        if (engine.getPlayerManager().getTimer(Keys.BossFlash).hasTimePassed())
        {
            if (!flash)
            {
                super.setImage(flashing);
            }
            else
            {
                super.setImage(original);
            }
            
            //alter flash
            flash = !flash;
            
            //reset time
            engine.getPlayerManager().getTimer(Keys.BossFlash).reset();
        }
    }
    
    /**
     * Start back at step 1
     */
    public void resetSteps()
    {
        setStep1(true);
        setStep2(false);
        setStep3(false);
    }
    
    /**
     * This step is where the enemy surrounds the hero
     * @return boolean
     */
    public boolean hasStep1()
    {
        return step1;
    }
    
    public void setStep1(final boolean step1)
    {
        this.step1 = step1;
        
    }
    
    /**
     * This step is when the enemy lines up to attack
     * @return boolean
     */
    public boolean hasStep2()
    {
        return step2;
    }
    
    public void setStep2(final boolean step2)
    {
        this.step2 = step2;
    }
    
    /**
     * This step is where the enemy closes the gap to attack the hero
     * @return boolean
     */
    public boolean hasStep3()
    {
        return step3;
    }
    
    public void setStep3(final boolean step3)
    {
        this.step3 = step3;
    }
    
    /**
     * Here depending on whether or not the enemy can throw a projectile 
     * the enemy could move further back or close in to be able to attack
     * @param hero 
     */
    private void closeGap(final Rectangle anchor, final Rectangle anchorHero, final int heroX)
    {
        //if the hero is once again out of range we need to go back a step or two
        if (anchor.getY() <= anchorHero.getY() || anchor.getY() >= anchorHero.getY() + anchorHero.getHeight())
        {
            if (Math.random() > .5)
            {
                //restart from step one
                resetSteps();
            }
            else
            {
                //correct y coordinate again
                setStep2(true);
                setStep3(false);
            }
            
            return;
        }
        
        //can the enemy throw a projectile
        if (hasState(State.THROW_PROJECTILE))
        {
            resetSteps();
        }
        else
        {
            if (getX() <= heroX)
            {
                setState(State.WALK_HORIZONTAL);
                setVelocityX(getVelocityWalk());
                setVelocityY(VELOCITY_NONE);
            }
            
            if (getX() > heroX)
            {
                setState(State.WALK_HORIZONTAL);
                setVelocityX(-getVelocityWalk());
                setVelocityY(VELOCITY_NONE);
            }
        }
    }
    
    /**
     * Here the enemy will correct the y coordinate to 
     * lineup with the hero, basically getting ready to attack.
     * @param hero The hero we are targeting.
     */
    private void lineAttack(final Rectangle anchor, final Rectangle anchorHero, final boolean isJumping)
    {
        //if the x velocity is still active or the hero is jumping we will not move into attack position
        if (isJumping)
            return;
        
        if (isLinedUp(anchor, anchorHero))
        {
            setStep2(false);
            setStep3(true);
            return;
        }
        
        //now that we are on the correct side we can fix the y coordinate
        if (anchor.getY() <= anchorHero.getY())
        {
            setState(State.WALK_VERTICAL);
            setVelocityX(VELOCITY_NONE);
            setVelocityY(getVelocityWalk());
        }

        if (anchor.getY() >= anchorHero.getY() + anchorHero.getHeight())
        {
            setState(State.WALK_VERTICAL);
            setVelocityX(VELOCITY_NONE);
            setVelocityY(-getVelocityWalk());
        }
    }
    
    private boolean isLinedUp(final Rectangle anchor, final Rectangle anchorHero)
    {
        return (anchor.getY() > anchorHero.getY() && anchor.getY() < anchorHero.getY() + anchorHero.getHeight());
    }
    
    /**
     * This is the initial state when an enemy starts. 
     * They will position themselves on the appropriate side
     * @param hero The hero to surround.
     */
    private void surroundEnemy(final Player hero)
    {
        if (attackEast)
        {
            //if attacking from the east side and we aren't on the east side yet
            if (getX() < hero.getX() + hero.getWidth())
            {
                setState(State.WALK_HORIZONTAL);
                setVelocityX(getVelocityWalk());
                setVelocityY(VELOCITY_NONE);
            }
            else
            {
                if (getX() > hero.getX() + hero.getWidth() * 2)
                {
                    setState(State.WALK_HORIZONTAL);
                    setVelocityX(-getVelocityWalk());
                    setVelocityY(VELOCITY_NONE);
                }
                else
                {
                    //we are done with the current step
                    setStep1(false);
                }
            }
        }
        
        if (!attackEast)
        {
            //if attacking from the west side and we aren't on the west side yet
            if (getX() > hero.getX() - hero.getWidth())
            {
                setState(State.WALK_HORIZONTAL);
                setVelocityX(-getVelocityWalk());
                setVelocityY(VELOCITY_NONE);
            }
            else
            {
                if (getX() < hero.getX() - (hero.getWidth() * 2))
                {
                    setState(State.WALK_HORIZONTAL);
                    setVelocityX(getVelocityWalk());
                    setVelocityY(VELOCITY_NONE);
                }
                else
                {
                    //we are done with the current step
                    setStep1(false);
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
     * Checks the list of heroes and returns the one assigned.
     * If nothing matches null is then returned.
     * @param heroes List of heroes we want to check
     * @return Player The player being targeted by the enemy
     */
    public Player getAssignment(List<Player> heroes)
    {
        if (heroes.size() < 1)
            return null;
        
        for (Player hero : heroes)
        {
            if (hero.getType() == getAssignment())
                return hero;
        }
        
        return null;
    }
    
    /**
     * If any enemy is within striking distance of the hero
     * they will take advantage of it. If there is no 
     * attack opportunity null will be returned.
     * @param hero The hero we are attacking
     * @return State, the action that will take place
     */
    private State getAttackOpportunity(final Rectangle anchor, final Rectangle anchorHero, final Point heroCenter, final boolean isJumping)
    {
        //if the enemy can't attack return false, or if the hero is jumping
        if (!canAttack() || isJumping)
            return null;
        
        //can the enemy attack close and far
        boolean canAttackClose = false, canAttackFar = false;
        
        //if the enemy bounds contains the center of the hero we can attack
        if (getRectangle().contains(heroCenter) && anchor.intersects(anchorHero))
            canAttackClose = true;
        
        //if enemy has ability to throw a projectile and if the enemy y is within the hero y
        if (canThrowProjectile() && anchor.getY() > anchorHero.getY() && anchor.getY() < anchorHero.getY() + anchorHero.getHeight())
            canAttackFar = true;
        
        if (canAttackClose || canAttackFar)
        {
            List<State> possible = getPossibleAttacks(canAttackClose);
            
            if (possible.size() > 0)
            {
                final int rand = (int)(Math.random() * possible.size());
                return possible.get(rand);
            }
        }
            
        return null;
    }
    
    /**
     * We want a list of all the possible attacks for this enemy
     * @return ArrayList of possible states
     */
    private List<State> getPossibleAttacks(final boolean canAttackClose)
    {
        List<State> possible = new ArrayList<>();
        
        //if the player can attack close then check if we have these attacks
        if (canAttackClose)
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
        
        if (hasState(State.THROW_PROJECTILE))
            possible.add(State.THROW_PROJECTILE);
        
        return possible;
    }
    
    /**
     * This method will take the original image
     * and apply a bit mask to create a new image. 
     * The purpose of this is to have a copy of the 
     * original sprite sheet as well a copy with 
     * every animation appearing to have a flash color 
     * applied. Then when the boss is low on health 
     * we can apply a flashing effect.
     * 
     * @throws Exception 
     */
    public void createImages() throws Exception
    {
        if (getImage() == null)
            throw new Exception("Image has not been set yet");
        
        //store original image because we will be alternating images
        this.original = super.getImage();
        
        //get the dimensions of the image
        final int width = getImage().getWidth(null);
        final int height = getImage().getHeight(null);
        
        //create a copy of image to this buffered image
        BufferedImage testImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        testImage.getGraphics().drawImage(getImage(), 0, 0, null);
        
        //create the flashing image with the same dimensions
        this.flashing = new BufferedImage(getImage().getWidth(null), getImage().getHeight(null), BufferedImage.TYPE_INT_ARGB);
        
        int[] imagePixels = flashing.getRGB( 0, 0, width, height, null, 0, width);
        int[] maskPixels  = testImage.getRGB(0, 0, width, height, null, 0, width);

        for (int i = 0; i < imagePixels.length; i++)
        {
            //take the current pixel data and apply the following modifications 
            int color = (imagePixels[i] & 0xff0000); 
            int alpha = maskPixels[i] >> 16; 
            imagePixels[i] = color | alpha; 
        }

        //now that the pixel data has been altered apply it to our flashing image
        flashing.setRGB(0, 0, width, height, imagePixels, 0, width);
    }
}