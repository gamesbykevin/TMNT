/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gamesbykevin.tmnt.levels;

import com.gamesbykevin.framework.base.Sprite;

import com.gamesbykevin.tmnt.main.Engine;

import java.awt.*;

/**
 * All levels will 
 * @author GOD
 */
public abstract class Level extends Sprite
{
    //what is considered in bounds
    private Polygon bounds;
    
    //background image
    private Sprite background;
    
    //do we automatically scroll the background
    private boolean autoScrollBackground = false;
    
    public Level()
    {
    }
    
    public void setBackgroundImage(final Image background)
    {
        this.background = new Sprite();
        this.background.setImage(background);
        this.background.setDimensions(background.getWidth(null), background.getHeight(null));
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
    
    public void update(final int scrollSpeed) throws Exception
    {
        if (background != null)
        {
            //make sure auto scroll is not enabled
            if (!hasAutoScrollBackground())
            {
                //if there is a scrolling speed it needs to be more than the level scroll
                if (scrollSpeed != 0)
                    background.setVelocityX(-(scrollSpeed + 1));
            }

            //update background position
            background.update();
        }
        
        if (bounds != null)
        {
            //update level position and bounds
            bounds.translate(-scrollSpeed, 0);
        }
        
        super.setVelocityX(-scrollSpeed);
        super.update();
    }
    
    /**
     * Draw the level with the background
     * @param g Graphics object
     * @return Graphics object with level drawn to it
     */
    public Graphics render(Graphics g)
    {
        if (background != null)
        {
            //draw background first
            background.draw(g);
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