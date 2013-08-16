package com.gamesbykevin.tmnt.heroes;

import com.gamesbykevin.tmnt.main.Engine;
import com.gamesbykevin.tmnt.main.Resources;
import com.gamesbykevin.tmnt.main.Resources.GamePlayers;
import com.gamesbykevin.tmnt.player.Player;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.Graphics;
import java.util.List;

public class Hero extends Player
{
    private static final int HEALTH_DEFAULT = 10;
    private static final int LIVES_DEFAULT = 5;
    
    private int fullHealthWidth = -1;
    private int fontHeight = -1;
    
    //store the color of the turtle used to draw turtle info
    private Color color;
    
    //we will cache the health string until there is a changes
    private String healthDisplay = null;
    
    //we will cache the lives String until there is a change
    private String livesDisplay = null;
    
    //temp List for the current existing enemies
    private List<Player> enemies;
    
    public Hero(final GamePlayers type)
    {
        super(type);
        
        //all heroes will have the same health and lives
        super.setHealthDefault(HEALTH_DEFAULT);
        super.setLives(LIVES_DEFAULT);
        
        setHealthDisplay();
        setLivesDisplay();
    }
    
    /**
     * This player is human so we need to check keyboard input
     * @param keyboard Our object that records keyboard events
     */
    public void update(final Engine engine) throws Exception
    {
        enemies = engine.getPlayerManager().getEnemies();
        
        int previousHealth = super.getHealth();
        int previousLives = super.getLives();
        
        super.update(engine, enemies);
        
        enemies.clear();
        
        if (previousHealth != super.getHealth())
            setHealthDisplay();
        if (previousLives != super.getLives())
            setLivesDisplay();
        
        if (engine.getKeyboard().hasKeyPressed(KeyEvent.VK_RIGHT))
        {
            if (canWalk())
            {
                super.setHorizontalFlip(false);
                setVelocity(getVelocityWalk(), VELOCITY_NONE);
                getSpriteSheet().setCurrent(State.WALK_HORIZONTAL);
            }
        }

        if (engine.getKeyboard().hasKeyPressed(KeyEvent.VK_LEFT))
        {
            if (canWalk())
            {
                super.setHorizontalFlip(true);
                setVelocity(-getVelocityWalk(), VELOCITY_NONE);
                getSpriteSheet().setCurrent(State.WALK_HORIZONTAL);
            }
        }

        if (engine.getKeyboard().hasKeyPressed(KeyEvent.VK_UP))
        {
            if (canWalk())
            {
                setVelocity(VELOCITY_NONE, -getVelocityWalk());
                getSpriteSheet().setCurrent(State.WALK_VERTICAL);
            }
        }

        if (engine.getKeyboard().hasKeyPressed(KeyEvent.VK_DOWN))
        {
            if (canWalk())
            {
                setVelocity(VELOCITY_NONE, getVelocityWalk());
                getSpriteSheet().setCurrent(State.WALK_VERTICAL);
            }
        }

        if (engine.getKeyboard().hasKeyPressed(KeyEvent.VK_A))
        {
            if (canJump())
            {
                //play sound effect
                engine.getResources().playSoundEffect(Resources.GameAudioEffects.Jump);
                
                setVelocity(getVelocityX(), -getVelocityJump());
                setNewState(State.JUMP);
                setJumpPhase1(getX(), getY() - (getVelocityJump() * Player.NUM_FRAMES_JUMP));
                setJumpPhase2(getX(), getY());
            }
        }

        if (engine.getKeyboard().hasKeyPressed(KeyEvent.VK_S))
        {
            if (canAttack())
            {
                if (isJumping())
                {
                    //play sound effect
                    engine.getResources().playSoundEffect(Resources.GameAudioEffects.JumpKick);
                    
                    setState(State.JUMP_ATTACK);
                    
                    if (getVelocityY() < 0)
                        setVelocityY(-getVelocityY());
                }
                else
                {
                    //play sound effect
                    engine.getResources().playSoundEffectRandomAttack();
                    
                    setNewState(State.ATTACK1);
                    setVelocity(VELOCITY_NONE, VELOCITY_NONE);
                    engine.getKeyboard().removeKeyPressed(KeyEvent.VK_S);
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
                    
                    engine.getKeyboard().removeKeyPressed(KeyEvent.VK_S);
                }
            }
        }

        if (engine.getKeyboard().hasKeyReleased(KeyEvent.VK_LEFT) || engine.getKeyboard().hasKeyReleased(KeyEvent.VK_RIGHT))
        {
            if (isWalking() || isIdle())
            {
                setState(State.IDLE);
                setVelocity(VELOCITY_NONE, VELOCITY_NONE);
                engine.getKeyboard().reset();
            }
        }

        if (engine.getKeyboard().hasKeyReleased(KeyEvent.VK_UP) || engine.getKeyboard().hasKeyReleased(KeyEvent.VK_DOWN))
        {
            if (isWalking() || isIdle())
            {
                setState(State.IDLE);
                setVelocity(VELOCITY_NONE, VELOCITY_NONE);
                engine.getKeyboard().reset();
            }
        }
    }
    
    public void setHealthDisplay()
    {
        healthDisplay = "";

        for (int i=0; i < super.getHealth(); i++)
        {
            healthDisplay += "|";
        }
    }
    
    private void setLivesDisplay()
    {
        livesDisplay = super.getLives() + "";
    }
    
    public Graphics renderHealthInformation(final Graphics g, final int x, final int y)
    {
        if (color == null)
        {
            if (getType() == Resources.GamePlayers.Donatello)
                color = new Color(182, 52, 187);
            if (getType() == Resources.GamePlayers.Leonardo)
                color = new Color(0, 191, 255);
            if (getType() == Resources.GamePlayers.Michelangelo)
                color = new Color(255, 215, 0);
            if (getType() == Resources.GamePlayers.Raphael)
                color = new Color(255, 0, 0);
        }
        
        g.setColor(color);
        
        //these width and height values will be cached as well
        if (fullHealthWidth < 0)
            fullHealthWidth = g.getFontMetrics().stringWidth("IIIIIIII");
        if (fontHeight < 0)
            fontHeight = g.getFontMetrics().getHeight();
        
        g.drawString(healthDisplay, x, y);
        
        g.drawString(livesDisplay, x + fullHealthWidth, y - (int)(fontHeight * 1.2));
        
        return g;
    }
}