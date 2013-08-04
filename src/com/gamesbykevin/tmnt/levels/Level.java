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
    
    //do we automatically scroll the background
    private boolean autoScrollBackground = false;
    
    public Level()
    {
    }
    
    /**
     * Gets the east most x-coordinate of the polygon
     * @return int x-coordinate
     */
    public int getEastBoundsX()
    {
        return (bounds.getBounds().x + bounds.getBounds().width);
    }
    
    public void setBackgroundImage(final Image background)
    {
        this.backgrounds = new ArrayList<>();
        
        Sprite tmp = new Sprite();
        
        tmp.setImage(background);
        tmp.setDimensions(background.getWidth(null), background.getHeight(null));
        tmp.setX(0 - tmp.getWidth());
        this.backgrounds.add(tmp);
        
        tmp = new Sprite();
        tmp.setImage(background);
        tmp.setDimensions(background.getWidth(null), background.getHeight(null));
        tmp.setX(0);
        this.backgrounds.add(tmp);
        
        tmp = new Sprite();
        tmp.setImage(background);
        tmp.setDimensions(background.getWidth(null), background.getHeight(null));
        tmp.setX(0 + tmp.getWidth());
        this.backgrounds.add(tmp);
        
        tmp = new Sprite();
        tmp.setImage(background);
        tmp.setDimensions(background.getWidth(null), background.getHeight(null));
        tmp.setX(0 + (tmp.getWidth() * 2));
        this.backgrounds.add(tmp);
    }
    
    /**
     * If we set the autoscroll, then that means there is a separate background image
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
    
    protected void setBounds(final Polygon bounds)
    {
        this.bounds = bounds;
    }
    
    public Polygon getBounds()
    {
        return this.bounds;
    }
    
    public boolean hasBounds(Rectangle rectangle)
    {
        return (this.bounds.contains(rectangle));
    }
    
    public void setScrollSpeed(final int scrollSpeed)
    {
        if (backgrounds != null)
        {
            //make sure auto scroll is not enabled
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
        
        super.setVelocityX(-scrollSpeed);
    }
    
    /**
     * Scroll the level and background, and check if background needs to repeat
     * @param screen
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
            //draw bounds so we can see
            g.setColor(Color.RED);
            g.drawPolygon(bounds);
        }
        
        //return result
        return g;
    }
}