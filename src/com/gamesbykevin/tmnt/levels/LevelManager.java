package com.gamesbykevin.tmnt.levels;

import com.gamesbykevin.framework.base.Sprite;

import com.gamesbykevin.tmnt.grunt.Grunt;
import com.gamesbykevin.tmnt.heroes.Hero;
import com.gamesbykevin.tmnt.main.ResourceManager;
import com.gamesbykevin.tmnt.player.*;

import static com.gamesbykevin.tmnt.player.Player.VELOCITY_NONE;

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
        level.createPowerUps(resources.getLevelObject(ResourceManager.LevelMisc.Pizza), screen);
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
            
            //get hero with the most width
            int mostWidth = 0;
            
            for (Hero hero : players.getHeroes())
            {
                if (hero.getWidth() > mostWidth)
                    mostWidth = hero.getWidth() + 1;
                
                //default the scroll speed to 0 before we check
                getLevel().setScrollSpeed(Player.VELOCITY_NONE);

                //right edge where hero can't go past
                final int rightSide = screen.x + (int)(screen.width * .8);

                //get temp anchor and make sure the hero doesn't go out of bounds
                Rectangle tmpAnchor = hero.getAnchorLocation();

                if (hero.isJumping())
                    tmpAnchor.y = hero.getJumpPhase2().y + (hero.getHeight() / 2);

                //calculate future position and stop velocity if no longer in boundary
                if (hero.getVelocityX() != VELOCITY_NONE)
                {
                    tmpAnchor.translate(hero.getVelocityX(), VELOCITY_NONE);

                    if (!getLevel().getBoundary().contains(tmpAnchor) || !screen.contains(tmpAnchor))
                    {
                        tmpAnchor.translate(-hero.getVelocityX(), VELOCITY_NONE);
                        hero.setVelocityX(VELOCITY_NONE);
                    }
                }
                else
                {
                    if (!hero.isJumping())
                    {
                        tmpAnchor.translate(VELOCITY_NONE, hero.getVelocityY());

                        if (!getLevel().getBoundary().contains(tmpAnchor)) // || !screen.contains(tmpAnchor))
                        {
                            tmpAnchor.translate(VELOCITY_NONE, -hero.getVelocityY());
                            hero.setVelocityY(VELOCITY_NONE);
                        }
                    }
                }
                
                //if the hero is moving east make sure they still stay on screen
                if (hero.getVelocityX() > 0 && hero.getX() >= rightSide)
                {
                    //move temp anchor to check for collision before we scroll
                    tmpAnchor.translate(hero.getVelocityX(), Player.VELOCITY_NONE);
                    
                    if (!hasEnemies && getLevel().getBoundary().contains(tmpAnchor))
                        getLevel().setScrollSpeed(hero.getVelocityX());

                    //stop level scroll if at end of level
                    if (getLevel().getEastBoundsX() <= screen.x + screen.width)
                        getLevel().setScrollSpeed(Player.VELOCITY_NONE);

                    hero.setX(rightSide);
                }

                List<Sprite> powerUps = getLevel().getPowerUps();
                
                for (int i=0; i < powerUps.size(); i++)
                {
                    if (tmpAnchor.intersects(powerUps.get(i).getRectangle()))
                    {
                        hero.resetHealth();
                        powerUps.remove(i);
                        i--;
                    }
                }
            }
            
            for (Grunt grunt : players.getEnemies())
            {
                //get temp anchor and make sure the hero doesn't go out of bounds
                Rectangle tmpAnchor = grunt.getAnchorLocation();

                //calculate future position and stop velocity if no longer in boundary
                if (grunt.getVelocityX() != VELOCITY_NONE)
                {
                    tmpAnchor.translate(grunt.getVelocityX(), VELOCITY_NONE);

                    if (!getLevel().getBoundary().contains(tmpAnchor))// || !screen.contains(tmpAnchor))
                    {
                        tmpAnchor.translate(-grunt.getVelocityX(), VELOCITY_NONE);
                        grunt.setVelocityX(VELOCITY_NONE);
                    }
                }
                else
                {
                    tmpAnchor.translate(VELOCITY_NONE, grunt.getVelocityY());

                    if (!getLevel().getBoundary().contains(tmpAnchor))// || !screen.contains(tmpAnchor))
                    {
                        tmpAnchor.translate(VELOCITY_NONE, -grunt.getVelocityY());
                        grunt.setVelocityY(VELOCITY_NONE);
                    }
                }
            }
            
            //check for adding enemies if we hit a check point of if there are existing enemies on the screen
            final boolean checkAddEnemies = (!hasEnemies && getLevel().hasCheckpoint(mostWidth) || hasEnemies);
            
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
            }
            
            //no more check points so hero has reached end of level
            if (getLevel().getCheckpointCount() < 1)
            {
                
            }
        }
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
        {
            level.render(g);
        }
        
        //if scroll screen is to be displayed the image will not be null
        if (display && !hasFinished(screen))
        {
            final int x = screen.x + screen.width - image.getWidth(null);
            final int y = screen.y + (screen.height / 2) - (image.getHeight(null) / 2);
            
            g.drawImage(image, x, y, null);
        }
        
        return g;
    }
}