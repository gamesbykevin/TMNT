package com.gamesbykevin.tmnt.main;

import java.awt.*;
import javax.swing.*;

import com.gamesbykevin.tmnt.shared.Shared;

public class Main extends Thread
{
    //image where all game/menu elements will be written to
    private Image bufferedImage;
    
    //our dimensions for the regular and full screen window
    private Rectangle originalSizeWindow;
    private Rectangle fullSizeWindow;
    
    //our main game engine
    private Engine engine;
    
    //determine if debug mode is enabled
    //public static final boolean DEBUG_MODE = !System.getProperty("java.vm.info", "").contains("sharing");
    
    //do we hide mouse when the menu is not visible and actual gameplay started
    public static boolean hideMouse = true;//(DEBUG_MODE);//true;
    
    //do we want to display our fps/ups counter, true by default
    private final boolean showCounter;
    
    //how many nanoseconds bewteen each engine update
    private double nanoSecondsPerUpdate;
    
    //frames per second
    private int frames = 0;
    
    //updates per second
    private int updates = 0;
    
    //display this variable every second
    private int framesDisplay  = 0;
    
    //display this variable every second
    private int updatesDisplay = 0;
    
    //how many nanoseconds are there in one second
    private static final double NANO_SECONDS_PER_SECOND = 1000000000.0;
    
    //reference to our applet
    private JApplet applet;
    
    //reference to our panel
    private JPanel panel;
    
    public Main(final int ups, final boolean showCounter)
    {
        this.showCounter = showCounter;
        
        //the dimensions used for original and full screen
        originalSizeWindow = new Rectangle(0, 0, Shared.WINDOW_WIDTH, Shared.WINDOW_HEIGHT);
        fullSizeWindow     = new Rectangle(originalSizeWindow);

        //duration of nanoSeconds per update
        nanoSecondsPerUpdate = NANO_SECONDS_PER_SECOND / ups;
    }
    
    /**
     * Create our main game engine and apply input listeners
     */
    public void createGameEngine() throws Exception
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
        long timer = System.currentTimeMillis();
        
        double delta = 0;
        
        while(true)
        {
            try
            {
                long now = System.nanoTime();
                delta += (now - lastRun) / nanoSecondsPerUpdate;
                lastRun = now;
                
                while(delta >= 1)
                {
                    engine.update(this);

                    updates++;
                    delta--;
                }
                
                renderImage();
                drawScreen();
                frames++;
                
                if (System.currentTimeMillis() - timer > 1000)
                {
                    timer += 1000;
                    
                    updatesDisplay = updates;
                    framesDisplay = frames;
                    
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
    
    private Image getBufferedImage()
    {
        if (bufferedImage == null)
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
        
        return bufferedImage;
    }
    
    public Rectangle getScreen()
    {
        return this.originalSizeWindow;
    }
    
    public Rectangle getFullScreen()
    {
        return this.fullSizeWindow;
    }
    
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
    }
    
    /**
     * Gets the number of nanoseconds between each update
     * 
     * @return long The nanosecond duration between each update
     */
    public long getTimeDeductionPerFrame()
    {
        return (long)nanoSecondsPerUpdate;
    }
    
    /**
     * Writes all game/menu elements in our engine to our bufferedImage.
     * If set true also write our ups/fps counter
     * 
     * @throws Exception 
     */
    private void renderImage() throws Exception
    {
        Image tmp = getBufferedImage();
        
        if (tmp != null)
        {
            Graphics g = tmp.getGraphics();

            g.setColor(Color.BLACK);
            g.fillRect(originalSizeWindow.x, originalSizeWindow.y, originalSizeWindow.width, originalSizeWindow.height);

            engine.render(g);

            if (showCounter)
                renderCounter(g);
        }
    }
    
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
    
    private Graphics renderCounter(Graphics g)
    {
        final String result = updatesDisplay + " UPS, " + framesDisplay + " FPS";
        final int width = g.getFontMetrics().stringWidth(result);
        final int height = g.getFontMetrics().getHeight() + 1;
        final Rectangle tmp;
        
        if (applet != null)
        {
            tmp = new Rectangle(applet.getWidth() - width, applet.getHeight() - height, width, height);
        }
        else
        {
            tmp = new Rectangle(panel.getWidth() - width, panel.getHeight() - height, width, height);
        }

        g.setColor(Color.BLACK);
        g.fillRect(tmp.x, tmp.y, tmp.width, tmp.height);
        g.setColor(Color.WHITE);
        g.drawString(result, tmp.x, tmp.y + height - 2);
        
        return g;
    }
    
    private void drawScreen()
    {
        if (bufferedImage == null)
            return;
        
        Graphics g;
        
        //create the rectangle on the fly for full screen
        Rectangle r;
        
        if (applet != null)
        {
            g = applet.getGraphics();
            r = new Rectangle(0, 0, applet.getWidth(), applet.getHeight());
        }
        else
        {
            g = panel.getGraphics();
            r = new Rectangle(0, 0, panel.getWidth(), panel.getHeight());
        }
        
        try
        {
            int dx1 = r.x;
            int dy1 = r.y;
            int dx2 = r.x + r.width;
            int dy2 = r.y + r.height;

            int sx1 = 0;
            int sy1 = 0;
            int sx2 = bufferedImage.getWidth(null);
            int sy2 = bufferedImage.getHeight(null);

            g.drawImage(bufferedImage, dx1, dy1, dx2, dy2, sx1, sy1, sx2, sy2, null);

            Toolkit.getDefaultToolkit().sync();

            bufferedImage.flush();
            bufferedImage = null;

            g.dispose();
            g = null;
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
    
    /**
     * Set all objects null for garbage collection
     */
    @Override
    public void destroy()
    {
        if (bufferedImage != null)
            bufferedImage.flush();
        
        bufferedImage = null;
        
        if (engine != null)
            engine.dispose();
        
        engine = null;
        
        originalSizeWindow = null;
        fullSizeWindow = null;
    }
}