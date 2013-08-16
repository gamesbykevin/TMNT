package com.gamesbykevin.tmnt.main;

import java.awt.*;
import javax.swing.*;

import com.gamesbykevin.tmnt.shared.Shared;

public class Main extends Thread
{
    //image where all game/menu elements will be written to
    private Image bufferedImage;
    
    //Graphics object used to draw buffered image
    private Graphics bufferedImageGraphics;
    
    //our dimensions for the original screen window
    private Rectangle originalSizeWindow;
    
    //our dimensions for the full screen window
    private Rectangle fullSizeWindow;
    
    //our dimensions for keeping track of the size of the current window
    private Rectangle currentWindow;
    
    //our main game engine
    private Engine engine;
    
    //do we hide mouse when the menu is not visible and actual gameplay started
    public static boolean hideMouse = true;
    
    //how many nanoseconds bewteen each engine update
    private double nanoSecondsPerUpdate;
    
    //how many nanoseconds between each frame render
    private double nanoSecondsPerFrame;
    
    //frames per second
    private int frames = 0;
    
    //updates per second
    private int updates = 0;
    
    //frames per second current count
    private int currentFPS  = 0;
    
    //updates per second current count
    private int currentUPS = 0;
    
    //how many nanoseconds are there in one second
    private static final double NANO_SECONDS_PER_SECOND = 1000000000.0;
    
    //reference to our applet
    private JApplet applet;
    
    //reference to our panel
    private JPanel panel;
    
    //cache this graphics object so we aren't constantly creating it
    private Graphics graphics;
    
    /**
     * Main class that runs the game engine
     * 
     * @param ups Engine updates per second
     * @param fps Frame renders per second
     */
    public Main(final int ups, final int fps)
    {
        //the dimensions used for original/full screen
        originalSizeWindow = new Rectangle(0, 0, Shared.ORIGINAL_WIDTH, Shared.ORIGINAL_HEIGHT);
        fullSizeWindow     = new Rectangle(originalSizeWindow);

        //duration per each engine update in nanoseconds
        nanoSecondsPerUpdate = NANO_SECONDS_PER_SECOND / ups;
        
        //duration per each frame render in nanoseconds
        nanoSecondsPerFrame = NANO_SECONDS_PER_SECOND / fps;
    }
    
    /**
     * Create our main game engine and apply input listeners
     */
    public void create() throws Exception
    {
        engine = new Engine(this);
        
        //now that engine is created apply listeners so we can detect key/mouse input
        if (applet != null)
        {
            applet.addKeyListener(engine);
            applet.addMouseMotionListener(engine);
            applet.addMouseListener(engine);
        }
        else
        {
            panel.addKeyListener(engine);
            panel.addMouseMotionListener(engine);
            panel.addMouseListener(engine);
        }
    }
    
    public void run()
    {
        //we need to determine the last run so we can calculate ups/fps
        long lastRun = System.nanoTime();
        
        //this will reset ups/fps count every second
        long timer = System.nanoTime();
        
        //this variables will keep track of the time passed
        double deltaUpdate = 0;
        double deltaFrame = 0;
        
        while(true)
        {
            try
            {
                //get current system nano time
                long now = System.nanoTime();
                
                //update these variables
                deltaUpdate += ((now - lastRun) / nanoSecondsPerUpdate);
                deltaFrame += ((now - lastRun) / nanoSecondsPerFrame);
                
                //set the current time as the last run
                lastRun = now;
                
                while(deltaUpdate >= 1)
                {
                    engine.update(this);

                    updates++;
                    deltaUpdate--;
                }
                
                while(deltaFrame >= 1)
                {
                    renderImage();
                    drawScreen();
                    frames++;
                    
                    deltaFrame--;
                }
                
                //if 1 second has passed
                if (System.nanoTime() - timer > NANO_SECONDS_PER_SECOND)
                {
                    //add 1 second time for next update
                    timer += NANO_SECONDS_PER_SECOND;
                    
                    //store the current fps/ups to be displayed to the user
                    currentUPS = updates;
                    currentFPS = frames;
                    
                    //reset the counter
                    updates = 0;
                    frames = 0;
                }
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }
    }
    
    public void setApplet(final JApplet applet)
    {
        this.applet = applet;
    }
    
    public void setPanel(final JPanel panel)
    {
        this.panel = panel;
    }
    
    public JApplet getApplet()
    {
        return applet;
    }
    
    public JPanel getPanel()
    {
        return panel;
    }
    
    public Class<?> getContainerClass()
    {
        if (applet != null)
        {
            return applet.getClass();
        }
        else
        {
            return panel.getClass();
        }
    }
    
    /**
     * Create buffered Image
     */
    private void createBufferedImage()
    {
        if (applet != null)
        {
            bufferedImage = applet.createImage(originalSizeWindow.width, originalSizeWindow.height);
        }
        else
        {
            bufferedImage = panel.createImage(originalSizeWindow.width, originalSizeWindow.height);
        }
    }
    
    public Rectangle getScreen()
    {
        return this.originalSizeWindow;
    }
    
    public Rectangle getFullScreen()
    {
        return this.fullSizeWindow;
    }
    
    /**
     * This method will be called whenever the user turns full-screen on/off
     */
    public void setFullScreen()
    {
        if (applet != null)
        {
            fullSizeWindow = new Rectangle(0, 0, applet.getWidth(), applet.getHeight());
        }
        else
        {
            fullSizeWindow = new Rectangle(0, 0, panel.getWidth(), panel.getHeight());
        }
        
        //set the current window size
        currentWindow = new Rectangle(fullSizeWindow);
        
        //since full screen switched on/off create a new graphics object
        createGraphicsObject();
    }
    
    /**
     * Gets the number of nanoseconds between each update
     * 
     * @return long The nanosecond duration between each update
     */
    public long getTimeDeductionPerUpdate()
    {
        return (long)nanoSecondsPerUpdate;
    }
    
    /**
     * Writes all game/menu elements in our 
     * engine to our single bufferedImage.
     * 
     * @throws Exception 
     */
    private void renderImage() throws Exception
    {
        if (bufferedImage != null)
        {
            if (bufferedImageGraphics == null)
                bufferedImageGraphics = bufferedImage.getGraphics();
            
            //background by itself will be a black rectangle
            bufferedImageGraphics.setColor(Color.BLACK);
            bufferedImageGraphics.fillRect(0, 0, Shared.ORIGINAL_WIDTH, Shared.ORIGINAL_HEIGHT);

            engine.render(bufferedImageGraphics);

            if (Shared.DEBUG)
                renderCounter(bufferedImageGraphics);
        }
        else
        {
            //create the image that will be displayed to the user
            createBufferedImage();
        }
    }
    
    /**
     * Does the applet have focus, if this is a JPanel it will always return true
     * @return boolean
     */
    public boolean hasFocus()
    {
        if (applet != null)
        {
            return applet.hasFocus();
        }
        else
        {
            //jPanel will always have focus
            return true;
        }
    }
    
    /**
     * Draw frame counter onto Image.
     * This method should only be called when testing
     * @param g Graphics
     * @return Graphics
     */
    private Graphics renderCounter(Graphics g)
    {
        String result = currentUPS + " UPS, " + currentFPS + " FPS";
        int width = g.getFontMetrics().stringWidth(result);
        int height = g.getFontMetrics().getHeight() + 1;
        Rectangle tmp = new Rectangle(originalSizeWindow.width - width, originalSizeWindow.height - height, width, height);
        
        g.setColor(Color.BLACK);
        g.fillRect(tmp.x, tmp.y, tmp.width, tmp.height);
        g.setColor(Color.WHITE);
        g.drawString(result, tmp.x, tmp.y + height - 2);
        
        return g;
    }
    
    /**
     * Set the graphic object for drawing the rendered image
     */
    private void createGraphicsObject()
    {
        if (applet != null)
        {
            graphics = applet.getGraphics();
        }
        else
        {
            graphics = panel.getGraphics();
        }
    }
    
    /**
     * Draw Image onto screen
     */
    private void drawScreen()
    {
        //if no image has been rendered yet return
        if (bufferedImage == null)
            return;
        
        //cache graphics object to save resources
        if (graphics == null)
            createGraphicsObject();
        
        //make sure current window dimensions are set
        if (currentWindow == null)
            setFullScreen();
        
        try
        {
            //the destination will be the size of the window
            int dx1 = currentWindow.x;
            int dy1 = currentWindow.y;
            int dx2 = currentWindow.x + currentWindow.width;
            int dy2 = currentWindow.y + currentWindow.height;

            //the source will be the entire image
            int sx1 = 0;
            int sy1 = 0;
            int sx2 = bufferedImage.getWidth(null);
            int sy2 = bufferedImage.getHeight(null);
            
            //draw our rendered image at the specified location
            graphics.drawImage(bufferedImage, dx1, dy1, dx2, dy2, sx1, sy1, sx2, sy2, null);

            //release pixel data
            bufferedImage.flush();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
    
    /**
     * Set all objects null for garbage collection
     */
    public void dispose()
    {
        engine.dispose();
        engine = null;
        originalSizeWindow = null;
        fullSizeWindow = null;
        currentWindow = null;
        
        applet = null;
        panel = null;
        
        if (bufferedImage != null)
            bufferedImage.flush();
        
        bufferedImage = null;
        
        if (bufferedImageGraphics != null)
            bufferedImageGraphics.dispose();
        
        bufferedImageGraphics = null;
        
        if (graphics != null)
            graphics.dispose();
        
        graphics = null;
    }
}