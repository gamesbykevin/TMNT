package com.gamesbykevin.tmnt.levels;

import com.gamesbykevin.framework.base.Sprite;
import com.gamesbykevin.tmnt.heroes.Hero;
import com.gamesbykevin.tmnt.main.Engine;
import com.gamesbykevin.tmnt.main.Resources;
import com.gamesbykevin.tmnt.main.Resources.GameAudioEffects;
import com.gamesbykevin.tmnt.main.Resources.GameAudioMusic;
import com.gamesbykevin.tmnt.main.Resources.LevelMisc;
import com.gamesbykevin.tmnt.player.*;

import static com.gamesbykevin.tmnt.player.Player.VELOCITY_NONE;

import java.awt.*;
import java.util.List;

public class LevelManager 
{
    //our current level
    private Level level;
    
    //scroll speed when auto scroll is enabled
    private static final int SCROLL_SPEED = 10;
    
    //temporary anchor for player
    private Rectangle tmpAnchor;
    
    //list of the power ups
    private List<Sprite> powerUps;
    
    //is the current level complete
    private boolean complete = false;
    
    //the current level unique key
    private LevelMisc current;
    
    //if this game is over the hero has won
    private boolean gameover = false;
    
    public LevelManager()
    {
        
    }
    
    /**
     * Proper house keeping
     */
    public void dispose()
    {
        if (level != null)
        {
            level.dispose();
            level = null;
        }
    }
    
    /**
     * Get the current level
     * @return Level
     */
    public Level getLevel()
    {
        return this.level;
    }
    
    /**
     * Checks if the scroll boundary has reached the end
     * 
     * @param screen Window user can see
     * @param extraPixels Extra player pixels to account in the boundary
     * @return boolean
     */
    public boolean hasFinished(final Rectangle screen)
    {
        return (screen.x + screen.width > getLevel().getEastBoundsX() - (screen.width / 3));
    }
    
    /**
     * Get the next level
     * @return LevelMisc
     */
    private LevelMisc getNextLevel()
    {
        switch(current)
        {
            case Level1:
                return LevelMisc.Level2;

            case Level2:
                return LevelMisc.Level3;
                
            case Level3:
                return LevelMisc.Level4;
                
            case Level4:
                return LevelMisc.Level5;
                
            case Level5:
                return LevelMisc.Level6;
        }
        
        return null;
    }
    
    /**
     * Set the level which will create a new instance of Level
     * @param num 
     */
    public void setLevel(final LevelMisc chosenLevel, final Resources resources, final Rectangle screen) throws Exception
    {
        //set the current level
        this.current = chosenLevel;
        
        //since this is a new level it is not complete yet
        this.complete = false;
        
        //image for the level and the srolling background (if exists)
        Image image = null, background = null;
        
        int numCheckPoints = 0, scrollSpeed = 0;
        
        switch(chosenLevel)
        {
            case Level1:
                image = resources.getLevelObject(Resources.LevelMisc.Level1);
                background = resources.getLevelObject(Resources.LevelMisc.Level1Background);
                numCheckPoints = 4;
                level = new Level1();
                break;
                
            case Level2:
                image = resources.getLevelObject(Resources.LevelMisc.Level2);
                numCheckPoints = 4;
                level = new Level2();
                break;
                
            case Level3:
                image = resources.getLevelObject(Resources.LevelMisc.Level3);
                background = resources.getLevelObject(Resources.LevelMisc.Level3Background);
                numCheckPoints = 4;
                scrollSpeed = SCROLL_SPEED;
                level = new Level3();
                break;
                
            case Level4:
                image = resources.getLevelObject(Resources.LevelMisc.Level4);
                background = resources.getLevelObject(Resources.LevelMisc.Level4Background);
                numCheckPoints = 5;
                level = new Level4();
                break;
                
            case Level5:
                image = resources.getLevelObject(Resources.LevelMisc.Level5);
                background = resources.getLevelObject(Resources.LevelMisc.Level5Background);
                numCheckPoints = 3;
                level = new Level5();
                break;
                
            case Level6:
                image = resources.getLevelObject(Resources.LevelMisc.Level6);
                background = resources.getLevelObject(Resources.LevelMisc.Level6Background);
                numCheckPoints = 0;
                level = new Level6();
                break;
                
            default:
                throw new Exception("Level not found");
        }
        
        //set the level dimensions based on image width/height NOTE: every level has very close height
        level.setDimensions(image.getWidth(null), image.getHeight(null));
        
        //set the image for the current level
        level.setImage(image);
        
        //set the background Image if it exists
        if (background != null)
            level.setBackgroundImage(background, screen);

        //add checkpoints to level
        level.createCheckPoints(numCheckPoints, screen);
        
        if (scrollSpeed != 0)
        {
            //if scroll speed exists set value
            level.setAutoScrollSpeed(scrollSpeed);
        }
        
        //there is only 1 unique power up and all levels will get power ups, so add them now 
        level.createPowerUps(resources.getLevelObject(Resources.LevelMisc.Pizza), screen);
        
        //stop all other sound if applicable
        resources.stopAllSound();
        
        //start playing the main level music
        resources.playGameMusic(level.getMusic(), true);
        
        //play sound effect here right when level begins
        resources.playSoundEffect(GameAudioEffects.LetsKickShell);
    }
    
    /**
     * Add all level related objects to the 
     * existing levelObjects list.
     * 
     * @param levelObjects
     */
    public void addAllStageObjects(List<Sprite> levelObjects)
    {
        if (getLevel().getPowerUps() != null)
        {
            for (Sprite powerUp : getLevel().getPowerUps())
            {
                levelObjects.add(powerUp);
            }
        }
    }
    
    /**
     * Does the following
     * 
     * 1. Update the level
     * 2. Check if the level needs scrolling
     * 3. Hero has collision with power-up
     * 4. Hero stays within Level boundary
     * 5. Enemies stay within Level boundary
     * 
     * @param enemies List of existing enemies
     * @param heroes List of existing heroes
     */
    public void update(final Engine engine) throws Exception
    {
        //if the level object does not exist we have nothing to update
        if (getLevel() == null)
            return;
        
        //update level scrolling etc..
        getLevel().update(engine.getMain().getScreen());

        //get hero with the most width
        int playerWidth = 0;

        for (Hero hero : engine.getPlayerManager().getHeroManager().getHeroes())
        {
            //find the hero with the most width
            if (hero.getWidth() > playerWidth)
                playerWidth = hero.getWidth() + 1;

            //default the scroll speed to 0 before we check
            getLevel().setScrollSpeed(Player.VELOCITY_NONE);
            
            //don't check this hero for level scrolling if they have no velocity
            if (!hero.hasVelocity())
                continue;

            //right edge where hero can't go past
            final int rightSide = engine.getMain().getScreen().x + (int)(engine.getMain().getScreen().width * .8);

            //get temp anchor and make sure the hero doesn't go out of bounds
            tmpAnchor = hero.getAnchorLocation();

            //the hero can't collect a power up if they are jumping
            if (hero.isJumping())
            {
                //tmpAnchor.y = hero.getJumpPhase2().y + (hero.getHeight() / 2);
                tmpAnchor.y = hero.getJumpPhase2().y + (hero.getHeight() / 2) - (int)(hero.getHeight() * Player.ANCHOR_HEIGHT_RATIO);
            }
            else
            {
                //get the list of power ups
                powerUps = getLevel().getPowerUps();

                //check each power up for collision
                for (int i=0; i < powerUps.size(); i++)
                {
                    if (tmpAnchor.contains(powerUps.get(i).getX() + (powerUps.get(i).getWidth() / 2), powerUps.get(i).getY()))
                    {
                        //play sound effect
                        engine.getResources().playSoundEffect(Resources.GameAudioEffects.PizzaTime);

                        //reset health back to full
                        hero.resetHealth();

                        //update health display that the user sees
                        hero.setHealthDisplay();

                        //remove power up from list
                        powerUps.remove(i);
                        i--;
                    }
                }
            }

            //calculate future position and stop velocity if no longer in boundary
            if (hero.getVelocityX() != VELOCITY_NONE)
            {
                tmpAnchor.translate(hero.getVelocityX(), VELOCITY_NONE);

                if (!getLevel().getBoundary().contains(tmpAnchor) || !engine.getMain().getScreen().contains(tmpAnchor))
                {
                    tmpAnchor.translate(-hero.getVelocityX(), VELOCITY_NONE);
                    hero.setVelocityX(VELOCITY_NONE);
                }
            }
            else
            {
                //make sure hero is not jumping
                if (!hero.isJumping())
                {
                    //move anchor according to y velocity set
                    tmpAnchor.translate(VELOCITY_NONE, hero.getVelocityY());

                    //if the anchor is no longer inside the level boundary
                    if (!getLevel().getBoundary().contains(tmpAnchor)) // || !screen.contains(tmpAnchor))
                    {
                        //move anchor back according to y velocity set
                        tmpAnchor.translate(VELOCITY_NONE, -hero.getVelocityY());
                        
                        //since we can no longer move in the current direction set y velocity to none
                        hero.setVelocityY(VELOCITY_NONE);
                    }
                }
            }

            //if the hero is moving east make sure they still stay on screen
            if (hero.getVelocityX() > 0 && hero.getX() >= rightSide)
            {
                boolean hasEnemies = engine.getPlayerManager().hasEnemies();
                
                //move temp anchor to check for collision before we scroll
                tmpAnchor.translate(hero.getVelocityX(), Player.VELOCITY_NONE);

                //if there are no enemies on the screen and we are withing the boundary scroll accordingly
                if (!hasEnemies && getLevel().getBoundary().contains(tmpAnchor))
                    getLevel().setScrollSpeed(hero.getVelocityX());

                //stop level scroll if at end of level or enemies exist
                if (getLevel().getEastBoundsX() <= engine.getMain().getScreen().x + engine.getMain().getScreen().width || hasEnemies)
                    getLevel().setScrollSpeed(Player.VELOCITY_NONE);

                //set the hero x coordinate so they don't leave the screen
                hero.setX(rightSide);
            }
        }

        //keep enemies within level boudnary
        for (Player grunt : engine.getPlayerManager().getEnemies())
        {
            //get temp anchor and make sure the hero doesn't go out of bounds
            tmpAnchor = grunt.getAnchorLocation();

            //calculate future position and stop velocity if no longer in boundary
            if (grunt.getVelocityX() != VELOCITY_NONE)
            {
                tmpAnchor.translate(grunt.getVelocityX(), VELOCITY_NONE);

                if (!getLevel().getBoundary().contains(tmpAnchor))
                {
                    tmpAnchor.translate(-grunt.getVelocityX(), VELOCITY_NONE);
                    grunt.setVelocityX(VELOCITY_NONE);
                }
            }
            else
            {
                tmpAnchor.translate(VELOCITY_NONE, grunt.getVelocityY());

                if (!getLevel().getBoundary().contains(tmpAnchor))
                {
                    tmpAnchor.translate(VELOCITY_NONE, -grunt.getVelocityY());
                    grunt.setVelocityY(VELOCITY_NONE);
                }
            }
        }

        //there has to be at least 2 checkpoints remaining to determine if enemies are added (the last checkpoint is for the boss)
        if (getLevel().getCheckpointCount() > 1)
            checkAddEnemies(engine, playerWidth);
        
        //if there is 1 checkpoint left it will be for the boss
        if (getLevel().getCheckpointCount() == 1)
            checkAddBoss(engine, playerWidth);
        
        //if there are no more checkpoints
        if (getLevel().getCheckpointCount() < 1)
            checkLevelComplete(engine);
    }
    
    /**
     * Check if we need to add enemies to the screen. If there are no 
     * enemies and we have hit a check point then we need to add x amount 
     * of enemies. Also if enemies already exist check if we still need 
     * to add x amount to meet the criteria of enemies allowed at once.
     * 
     * @param engine
     * @param playerWidth Extra pixels to account for when checking for the checkpoint
     * @param hasEnemies Do enemies already exist on the screen
     * @throws Exception 
     */
    private void checkAddEnemies(final Engine engine, final int playerWidth) throws Exception
    {
        //check for adding enemies if we hit a check point of if there are existing enemies on the screen
        if (!engine.getPlayerManager().hasEnemies() && getLevel().hasCheckpoint(playerWidth) || engine.getPlayerManager().hasEnemies())
        {
            //if the total amount of enemies created at checkpoint so far is less than the total per check point
            if (getLevel().getEnemiesCreatedAtCheckpoint() < getLevel().getEnemiesPerCheckpoint())
            {
                //if the current enemy count is less than the amount allowed on the screen at one time and we aren't over the limit allowed per check point, add enemies
                while (engine.getPlayerManager().getEnemyCount() < getLevel().getEnemiesAtOnce() && getLevel().getEnemiesCreatedAtCheckpoint() < getLevel().getEnemiesPerCheckpoint())
                {
                    //add random enemy to player manager
                    engine.getPlayerManager().getGruntManager().addRandom();

                    //increase the count to keep track
                    getLevel().addEnemiesCreatedAtCheckpoint();
                }
            }
        }
    }
    
    /**
     * Checks if we are past the last check point.
     * Then if we haven't added the boss yet do so.
     * 
     * @param engine
     * @param playerWidth Extra pixels to account for when passing check point
     * @throws Exception 
     */
    private void checkAddBoss(final Engine engine, final int playerWidth) throws Exception
    {
        //make sure hero has past the checkpoint
        if (getLevel().hasCheckpoint(playerWidth))
        {
            //check if we have added the boss to the level yet
            if (!getLevel().hasAddedBoss())
            {
                //set flag so we know that boss has been added
                getLevel().setAddedBoss();

                //add boss as specified by the level
                engine.getPlayerManager().getBossManager().add(getLevel().getBoss());

                //stop all sound
                engine.getResources().stopAllSound();

                //start boss music
                engine.getResources().playGameMusic(getLevel().getMusicBoss(), true);
            }
        }
    }
    
    /**
     * Check if we need to transition to 
     * the next level or if the game has 
     * been completed. At that point the 
     * appropriate flags will be set to 
     * indicate the game is won and start
     * the victory music.
     * 
     * @param engine 
     */
    private void checkLevelComplete(final Engine engine) throws Exception
    {
        //we have added the boss and the boss(es) have been destroyed
        if (getLevel().hasAddedBoss() && engine.getPlayerManager().getBossManager().getCount() < 1)
        {
            //the level is not set as complete yet
            if (!complete)
            {
                //stop all sound
                engine.getResources().stopAllSound();

                //play sound effect
                engine.getResources().playSoundEffect(GameAudioEffects.Cowabunga);
                
                //play level complete music
                engine.getResources().playGameMusic(GameAudioMusic.LevelClear, false);

                //un-pause the next level timer
                engine.getPlayerManager().getTimer(PlayerManager.Keys.NextLevelStart).reset();
                engine.getPlayerManager().getTimer(PlayerManager.Keys.NextLevelStart).setPause(false);

                //pause the game play timer
                engine.getPlayerManager().getTimer(PlayerManager.Keys.GamePlay).setPause(true);

                //set the level to complete
                complete = true;
            }
            else
            {
                //the level is complete now just wait for the timer to finish
                if (engine.getPlayerManager().getTimer(PlayerManager.Keys.NextLevelStart).hasTimePassed())
                {
                    //if there is a next level we continue the transition to next level
                    if (getNextLevel() != null)
                    {
                        //set next level
                        setLevel(getNextLevel(), engine.getResources(), engine.getMain().getScreen());

                        //pause next level timer
                        engine.getPlayerManager().getTimer(PlayerManager.Keys.NextLevelStart).setPause(true);

                        //un-pause game play timer
                        engine.getPlayerManager().getTimer(PlayerManager.Keys.GamePlay).setPause(false);

                        //reset each hero
                        for (Hero hero : engine.getPlayerManager().getHeroManager().getHeroes())
                        {
                            //reset player back to idle
                            hero.setNewState(Player.State.IDLE);

                            //reset all players to face east
                            hero.setHorizontalFlip(false);
                            
                            //set new start location
                            engine.getPlayerManager().getHeroManager().setStartLocation(getLevel().getBoundary().getBounds(), hero.getWidth(), hero.getHeight());
                            
                            //set hero to the start location
                            hero.setLocation(engine.getPlayerManager().getHeroManager().getStartLocation());
                        }
                    }
                    else
                    {
                        //if gameover flag is not set yet
                        if (!gameover)
                        {
                            //there is no next level so the game is over
                            gameover = true;

                            //un-pause game play timer
                            engine.getPlayerManager().getTimer(PlayerManager.Keys.GamePlay).setPause(true);

                            //stop all existing sound and play game over win sound
                            engine.getResources().stopAllSound();
                            engine.getResources().playGameMusic(GameAudioMusic.GameOverWin, true);
                        }
                    }
                }
            }
        }
    }
    
    /**
     * If all there is no next level then we have game over WIN
     * @return boolean
     */
    public boolean hasGameOver()
    {
        return this.gameover;
    }
    
    /**
     * Draw Level and Level Objects. Will
     * also draw an image so the user 
     * knows more scrolling is needed.
     * 
     * @param g Graphics objects will be drawn to
     * @param image Image to draw if more scrolling is needed
     * @param screen The portion of the window that displays the game
     * @return Graphics
     */
    public Graphics render(final Graphics g, final boolean display, final Image image, final Rectangle screen)
    {
        if (level != null)
            level.render(g);
        
        //if scroll screen is to be displayed the image will not be null
        if (display && !this.hasFinished(screen))
        {
            final int x = screen.x + screen.width - image.getWidth(null);
            final int y = screen.y + (screen.height / 2) - (image.getHeight(null) / 2);
            
            g.drawImage(image, x, y, null);
        }
        
        return g;
    }
}