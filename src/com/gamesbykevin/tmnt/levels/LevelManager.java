package com.gamesbykevin.tmnt.levels;

import com.gamesbykevin.framework.base.Sprite;
import com.gamesbykevin.tmnt.heroes.Hero;
import com.gamesbykevin.tmnt.main.ResourceManager;
import com.gamesbykevin.tmnt.player.*;

import java.awt.*;
import java.util.List;

/**
 *
 * @author GOD
 */
public class LevelManager 
{
    private Level level;
    
    //scroll speed when auto scroll is enabled
    private static final int SCROLL_SPEED = 10;
    
    //how many powerups can each level have
    public static final int POWERUP_LIMIT = 2;
    
    public LevelManager()
    {
        
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
     * Set the level number which will create a new instance of level
     * @param num 
     */
    public void setLevel(final ResourceManager.LevelMisc chosenLevel, final ResourceManager resources, final Rectangle screen) throws Exception
    {
        Image image;
        
        switch(chosenLevel)
        {
            case Level1:
                image = resources.getLevelObject(ResourceManager.LevelMisc.Level1);
                
                level = new Level1();
                level.setImage(image);
                level.setDimensions(image.getWidth(null), image.getHeight(null));
                level.createCheckPoints(3);
                level.setBackgroundImage(resources.getLevelObject(ResourceManager.LevelMisc.Level1Background), screen);
                break;
                
            case Level2:
                image = resources.getLevelObject(ResourceManager.LevelMisc.Level2);
                
                level = new Level2();
                level.setImage(image);
                level.setDimensions(image.getWidth(null), image.getHeight(null));
                level.createCheckPoints(3);
                break;
                
            case Level3:
                image = resources.getLevelObject(ResourceManager.LevelMisc.Level3);
                
                level = new Level3();
                level.setImage(image);
                level.setDimensions(image.getWidth(null), image.getHeight(null));
                level.createCheckPoints(4);
                level.setBackgroundImage(resources.getLevelObject(ResourceManager.LevelMisc.Level3Background), screen);
                level.setAutoScrollSpeed(SCROLL_SPEED);
                break;
                
            case Level4:
                image = resources.getLevelObject(ResourceManager.LevelMisc.Level4);
                
                level = new Level4();
                level.setImage(image);
                level.setDimensions(image.getWidth(null), image.getHeight(null));
                level.createCheckPoints(5);
                level.setBackgroundImage(resources.getLevelObject(ResourceManager.LevelMisc.Level4Background), screen);
                break;
                
            case Level5:
                image = resources.getLevelObject(ResourceManager.LevelMisc.Level5);
                
                level = new Level5();
                level.setImage(image);
                level.setDimensions(image.getWidth(null), image.getHeight(null));
                level.createCheckPoints(3);
                level.setBackgroundImage(resources.getLevelObject(ResourceManager.LevelMisc.Level5Background), screen);
                break;
                
            case Level6:
                image = resources.getLevelObject(ResourceManager.LevelMisc.Level6);
                
                level = new Level6();
                level.setImage(image);
                level.setDimensions(image.getWidth(null), image.getHeight(null));
                level.createCheckPoints(0);
                level.setBackgroundImage(resources.getLevelObject(ResourceManager.LevelMisc.Level6Background), screen);
                break;
                
            default:
                throw new Exception("Level not found");
        }
        
        //there is only 1 unique power up and all levels will get power ups, so add them now 
        level.createPowerUps(resources.getLevelObject(ResourceManager.LevelMisc.Pizza));
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
     * Check if the level needs to be scrolled and if the hero has collision with power-up
     * @param enemies List of existing enemies
     * @param heroes List of existing heroes
     */
    public void update(final PlayerManager players, final Rectangle screen) throws Exception
    {
        //make sure level has been created
        if (getLevel() != null)
        {
            //update level scrolling etc..
            getLevel().update(screen);
            
            //doesn't matter if enemy is dead
            boolean hasEnemies = (players.getEnemies().size() > 0);
            
            for (Hero hero : players.getHeroes())
            {
                //default the scroll speed to 0 before we check
                getLevel().setScrollSpeed(Player.VELOCITY_NONE);

                //right edge where hero can't go past
                final int rightSide = screen.x + (int)(screen.width * .9);

                //if the hero is moving east make sure they still stay on screen
                if (hero.getVelocityX() > 0 && hero.getX() >= rightSide)
                {
                    if (!hasEnemies)
                        getLevel().setScrollSpeed(hero.getVelocityX());

                    //stop level scroll if at end of level
                    if (getLevel().getEastBoundsX() <= screen.x + screen.width)
                        getLevel().setScrollSpeed(Player.VELOCITY_NONE);

                    hero.setX(rightSide);
                }

                //left edge where hero can't go past
                final int leftSide = screen.x + (int)(screen.width * .1);

                //if the hero is moving west make sure they still stay on screen
                if (hero.getVelocityX() < 0 && hero.getX() <= leftSide)
                {
                    hero.setX(leftSide);
                }
            }
            
            //check for adding enemies if we hit a check point of if there are existing enemies on the screen
            final boolean checkAddEnemies = (!hasEnemies && getLevel().hasCheckpoint() || hasEnemies);
            
            if (checkAddEnemies)
            {
                //if the total amount of enemies created so far is less than the total per check point
                if (getLevel().getEnemiesCreatedAtCheckpoint() < getLevel().getEnemiesPerCheckpoint())
                {
                    //if the current enemy count is less than the amount allowed on the screen at one time, add enemies
                    while (players.getEnemies().size() < getLevel().getEnemiesAtOnce() && getLevel().getEnemiesCreatedAtCheckpoint() < getLevel().getEnemiesPerCheckpoint())
                    {
                        //add random enemy to player manager
                        players.addRandomEnemy();
                        
                        //increase the count since we need to keep track
                        getLevel().addEnemiesCreatedAtCheckpoint();
                    }
                }
                else
                {
                    //we have defeated all enemies and will be able to continue forward
                    
                    //NOTE MAYBE HERE WE CAN ADD A GIF OF APRIL AND SHE WILL BE DISPLAYED ON/OFF EVERY SECOND UNTIL NEXT CHECK POINT
                }
            }
            
            //no more checkpoints need to add boss
            if (getLevel().getCheckpointCount() < 1)
            {
                
            }
        }
    }
    
    public Graphics render(Graphics g)
    {
        if (level != null)
        {
            level.render(g);
        }
        
        return g;
    }
}