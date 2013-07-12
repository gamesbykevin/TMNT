package com.gamesbykevin.tmnt.shared;

import java.awt.Cursor;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;

/**
 * This Shared class will have shared objects
 * 
 * @author GOD
 */
public class Shared 
{
    //dimensions for the original window
    public static final int WINDOW_WIDTH  = 500;
    public static final int WINDOW_HEIGHT = 300;
    
    //how many updates per second, controls speed of game
    public static final int DEFAULT_UPS = 60;
    
    //what is the name of our game
    public static final String GAME_NAME = "TMNT";
    
    //blank cursor created here to hide the mouse cursor
    public static final Cursor CURSOR = Toolkit.getDefaultToolkit().createCustomCursor(new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB), new Point(0, 0), "blank cursor");
}
