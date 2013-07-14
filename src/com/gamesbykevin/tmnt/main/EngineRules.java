package com.gamesbykevin.tmnt.main;

import java.awt.Graphics;
import java.awt.Rectangle;

public interface EngineRules 
{
    /**
     * Recycles the appropriate variables for garbage collection
     */
    public void dispose();
    
    /**
     * This method needs to reset the appropriate game elements so the game can restart
     * 
     * @throws Exception 
     */
    public void reset() throws Exception;
    
    /**
     * 
     * @param g Graphics object that game will be written to
     * @param originalSizeWindow Rectangle containing the original dimensions of the game
     * @return Graphics object containing game/menu elements
     * @throws Exception 
     */
    public Graphics render(Graphics g) throws Exception;
    
    /**
     * The Main class where the application is initialized
     * and contains our main loop so we need a method for the
     * Main class to be able to update our game engine
     * 
     * @param main The main class
     */
    public void update(Main main);
}