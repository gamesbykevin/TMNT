/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gamesbykevin.tmnt.levels;

import com.gamesbykevin.framework.base.Sprite;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * All levels will 
 * @author GOD
 */
public abstract class Level extends Sprite
{
    //what is considered in bounds
    private Polygon bounds;
    
    //backgrounds
    private List<Sprite> backgrounds;
    
    //power-ups the hero can collect (will only likely be pizza)
    private List<Sprite> powerUps;
    
    //do we automatically scroll the background
    private boolean autoScrollBackground = false;
    
    private List<Integer> checkpoints;
    
    public Level()
    {
        
    }
    
    /**
     * Each level will get x number of power ups 
     * placed in random locations throughout the level
     * 
     * @param image The image of the power up
     * @throws Exception 
     */
    public void createPowerUps(final Image image) throws Exception
    {
        if (getWidth() <= 0)
        {
            throw new Exception("Dimensions have to be set first before calling this function");
        }
        else
        {
            if (getBoundary() == null)
            {
                throw new Exception("Level Bounds has to be set first before calling this function");
            }
            else
            {
                powerUps = new ArrayList<>();
                
                //each powerup will have its own section and can be anywhere in that section at random
                final int eachSectionWidth = (getWidth() / LevelManager.POWERUP_LIMIT);
                
                //continue to loop until we have reached our limit
                while (powerUps.size() < LevelManager.POWERUP_LIMIT)
                {
                    Sprite powerUp = new Sprite();
                    
                    //set the powerup image
                    powerUp.setImage(image);
                    
                    //set auto size to true so the dimensions will be set automatically based on the image width/height
                    powerUp.setAutoSize(true);
                    
                    //now choose a random location
                    final int randomX = (int)(Math.random() * eachSectionWidth) + (powerUps.size() * eachSectionWidth);
                    final int randomY = 200;
                    
                    //set random location
                    powerUp.setLocation(randomX, randomY);
                    
                    //add powerup to list
                    powerUps.add(powerUp);
                }
            }
        }
    }
    
    /**
     * Creates a number of check points when the hero hits the check point the enemies come
     */
    public void createCheckPoints(final int total) throws Exception
    {
        if (getWidth() <= 0)
        {
            throw new Exception("Dimensions have to be set first before calling this function");
        }
        else
        {
            checkpoints = new ArrayList<>();
            
            final int eachCheckpointLength = (getWidth() / total);
            
            while (checkpoints.size() < total)
            {
                checkpoints.add((checkpoints.size() * eachCheckpointLength) + eachCheckpointLength);
            }
        }
    }
    
    /**
     * Checks current scroll location to see if we have past a checkpoint.
     * If check point is found remove check point and return true
     * @return boolean
     */
    public boolean hasCheckpoint()
    {
        Rectangle r = getBoundary().getBounds();
        
        for (int i=0; i < checkpoints.size(); i++)
        {
            //if the scroll x coordinate has past a check point 
            if (r.x + r.width < checkpoints.get(i))
            {
                //remove the check point
                checkpoints.remove(i);
                
                //indicate we found one
                return true;
            }
        }
        
        return false;
    }
    
    //gets the list of powerups
    public List<Sprite> getPowerUps()
    {
        return this.powerUps;
    }
    
    /**
     * Gets the east most x-coordinate of the polygon
     * @return int x-coordinate
     */
    public int getEastBoundsX()
    {
        return (bounds.getBounds().x + bounds.getBounds().width);
    }
    
    
    
    /**
     * Get valid starting point for level
     * @return 
     */
    public Point getStartingPoint()
    {
        return new Point(bounds.getBounds().x, bounds.getBounds().y);
    }
    
    /**
     * Set the background image and creates a specific number
     * of duplicate background depending on the background
     * width and the width of the screen that the user will see.
     * 
     * @param background Image of our repeating background
     */
    public void setBackgroundImage(final Image background, final Rectangle screen) throws Exception
    {
        if (getWidth() == 0)
            throw new Exception("Dimensions have to be set first before calling this function");
        
        //the width of the original background image
        final int backgroundWidth = background.getWidth(null);
        
        //make sure we have enough backgrounds so when one background begins to scroll off the screen we are covered
        final int numBackgrounds = (screen.width / backgroundWidth) + 2;
        
        this.backgrounds = new ArrayList<>();
        
        //continue adding backgrounds until we have reached our limit
        while (backgrounds.size() < numBackgrounds)
        {
            Sprite tmp = new Sprite();

            tmp.setImage(background);
            tmp.setDimensions(backgroundWidth, background.getHeight(null));
            tmp.setX(backgrounds.size() * backgroundWidth);
            tmp.setY(0);
            this.backgrounds.add(tmp);
        }
    }
    
    /**
     * If we set the auto scroll, then that means there is a separate background image
     * @param autoScrollBackground 
     */
    protected void setAutoScrollBackground(final boolean autoScrollBackground)
    {
        this.autoScrollBackground = autoScrollBackground;
    }
    
    private boolean hasAutoScrollBackground()
    {
        return this.autoScrollBackground;
    }
    
    protected void setBoundary(final Polygon bounds)
    {
        this.bounds = bounds;
    }
    
    public Polygon getBoundary()
    {
        return this.bounds;
    }
    
    public boolean hasBounds(Rectangle rectangle)
    {
        return (this.bounds.contains(rectangle));
    }
    
    /**
     * Set the auto scroll speed of the background
     * @param autoScrollSpeed Pixels per frame to scroll
     */
    public void setAutoScrollSpeed(final int autoScrollSpeed)
    {
        for (Sprite background : backgrounds)
        {
            background.setVelocityX(-autoScrollSpeed);
        }
    }
    
    public void setScrollSpeed(final int scrollSpeed)
    {
        if (backgrounds != null)
        {
            //make sure auto scroll is not enabled so we can sroll the background accordingly
            if (!hasAutoScrollBackground())
            {
                for (Sprite background : backgrounds)
                {
                    //if there is a scrolling speed it needs to be more than the level scroll
                    if (scrollSpeed != 0)
                    {
                        background.setVelocityX(-(scrollSpeed - 2));
                    }
                    else
                    {
                        background.setVelocityX(scrollSpeed);
                    }
                }
            }
        }
        
        //we also need to set the scroll speed for the power ups as well so they move with the level
        for (Sprite powerUp : powerUps)
        {
            powerUp.setVelocityX(-scrollSpeed);
        }
        
        //set the scroll speed for the level
        super.setVelocityX(-scrollSpeed);
    }
    
    /**
     * Scroll the level and background, and check if background needs to repeat.
     * 
     * @param screen Window of the game the user can see
     * @throws Exception 
     */
    public void update(final Rectangle screen) throws Exception
    {
        if (backgrounds != null)
        {
            for (Sprite background : backgrounds)
            {
                //update background position
                background.update();

                //if background is off screen repeat on the east side
                if (background.getX() + background.getWidth() < screen.x)
                    background.setX(screen.x + screen.width);
            }
        }
        
        if (bounds != null)
        {
            //update level position and bounds
            bounds.translate(super.getVelocityX(), 0);
        }
        
        //we also need to set the scroll speed for the power ups as well so they move with the level
        for (Sprite powerUp : powerUps)
        {
            powerUp.update();
        }
        
        super.update();
    }
    
    /**
     * Draw the level with the background
     * @param g Graphics object
     * @return Graphics object with level drawn to it
     */
    public Graphics render(Graphics g)
    {
        if (backgrounds != null)
        {
            for (Sprite background : backgrounds)
            {
                background.draw(g);
            }
        }
        
        //now draw level on top of background
        super.draw(g);
        
        if (bounds != null)
        {
            //draw bounds so we can see for now
            g.setColor(Color.RED);
            g.drawPolygon(bounds);
        }
        
        //return result
        return g;
    }
}