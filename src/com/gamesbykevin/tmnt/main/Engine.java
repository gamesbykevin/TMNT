package com.gamesbykevin.tmnt.main;

import com.gamesbykevin.framework.input.*;
import com.gamesbykevin.framework.input.Keyboard;

import com.gamesbykevin.tmnt.levels.*;
import com.gamesbykevin.tmnt.menu.GameMenu;
import com.gamesbykevin.tmnt.player.PlayerManager;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

//TODO here we need to have the resources object and the menu object

public class Engine implements KeyListener, MouseMotionListener, MouseListener, EngineRules 
{
    //our Main class has important information in it so we need a reference here
    private Main main;
    
    //access this menu here
    private GameMenu menu;
    
    //object that contains all image/audio resources in the game
    private ResourceManager resources;
    
    //mouse object that will be recording mouse input
    private Mouse mouse;
    
    //keyboard object that will be recording key input
    private Keyboard keyboard;
    
    private PlayerManager playerManager;
    
    private LevelManager levelManager;
    
    /**
     * The Engine that contains the game/menu objects
     * 
     * @param main Main object that contains important information so we need a reference to it
     * @throws CustomException 
     */
    public Engine(final Main main) 
    {
        //System.out.println("Debug Mode = " + Main.DEBUG_MODE);
        
        this.main = main;
        this.mouse = new Mouse();
        this.keyboard = new Keyboard();
        this.resources = new ResourceManager();
    }
    
    @Override
    public void dispose()
    {
        try
        {
            resources.dispose();
            resources = null;
            
            menu.dispose();
            menu = null;

            mouse.dispose();
            mouse = null;
            
            keyboard.dispose();
            keyboard = null;
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
    
    @Override
    public void update(Main main)
    {
        try
        {
            //if resources are still loading
            if (resources.isLoading())
            {
                resources.update(main.getContainerClass());

                //resources are now loaded so create the menu
                if (!resources.isLoading())
                    menu = new GameMenu(resources, main.getScreen());
            }
            else
            {
                if (!menu.hasFocus())
                {
                    mouse.resetMouseEvents();
                    keyboard.resetAllKeyEvents();
                }

                menu.update(main, this, resources, keyboard, mouse);

                //if the menu is on the last layer and the window has focus
                if (menu.isMenuFinished() && menu.hasFocus())
                {
                    //MAIN GAME LOGIC RUN HERE
                    
                    if (playerManager != null)
                    {
                        this.playerManager.update(this);
                        this.levelManager.update(playerManager.getEnemies(), playerManager.getHeroes(), main.getScreen());
                    }
                }
                
                if (mouse.isMouseReleased())
                    mouse.resetMouseEvents();
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
    
    public LevelManager getLevelManager()
    {
        return this.levelManager;
    }
    
    public Main getMain()
    {
        return main;
    }
    
    /**
     * Here we provide the logic to create a new game
     * @throws Exception 
     */
    @Override
    public void reset() throws Exception
    {
        this.playerManager = new PlayerManager(resources, getMain().getTimeDeductionPerFrame());
        this.levelManager = new LevelManager();
        this.levelManager.setLevel(ResourceManager.LevelObjects.Level3, resources);
        
        //final int wordPreferenceIndex = menu.getOptionSelectionIndex(GameMenu.LayerKey.Options, GameMenu.OptionKey.WordPreference);
        
        /*
        final int randomIndex = (int)(Math.random() * ResourceManager.GameAudioMusic.values().length);
        getResources().stopAllSound();
        getResources().playMusic(ResourceManager.GameAudioMusic.values()[randomIndex], true);
        */
    }
    
    /**
     * Draw our game to the Graphics object whether resources are still loading or the game is intact
     * @param g
     * @return
     * @throws Exception 
     */
    @Override
    public Graphics render(Graphics g) throws Exception
    {
        if (resources.isLoading())
        {
            //if we are loading resources draw loading screen
            resources.draw(g, main.getScreen());
        }
        else
        {
            //draw game elements
            renderGame((Graphics2D)g);
            
            //draw menu on top of the game if visible
            renderMenu(g);
        }
        
        return g;
    }
    
    /**
     * Draw our game elements
     * @param g2d Graphics2D object that game elements will be written to
     * @return Graphics the Graphics object with the appropriate game elements written to it
     * @throws Exception 
     */
    private Graphics renderGame(Graphics2D g2d) throws Exception
    {
        Font f = g2d.getFont();
        g2d.setFont(resources.getGameFont(ResourceManager.GameFont.Dialog).deriveFont(Font.PLAIN, 16));
        
        if (this.levelManager != null)
        {
            this.levelManager.render(g2d);
        }
        
        if (this.playerManager != null)
        {
            this.playerManager.render(g2d);
        }
        
        g2d.setFont(f);
        return g2d;
    }
    
    /**
     * Draw the Game Menu
     * 
     * @param g Graphics object where Images/Objects will be drawn to
     * @return Graphics The applied Graphics drawn to parameter object
     * @throws Exception 
     */
    private Graphics renderMenu(Graphics g) throws Exception
    {
        //if menu is setup draw menu
        if (menu.isMenuSetup())
            menu.render(g);

        //is menu is finished and we dont want to hide mouse cursor then draw it, or if the menu is not finished show mouse
        if (menu.isMenuFinished() && !Main.hideMouse || !menu.isMenuFinished())
        {
            Point p = mouse.getLocation();

            if (p != null && resources.getMenuImage(ResourceManager.MenuImage.Mouse) != null && resources.getMenuImage(ResourceManager.MenuImage.MouseDrag) != null)
            {
                if (mouse.isMouseDragged())
                    g.drawImage(resources.getMenuImage(ResourceManager.MenuImage.MouseDrag), p.x, p.y, null);
                else
                    g.drawImage(resources.getMenuImage(ResourceManager.MenuImage.Mouse), p.x, p.y, null);
            }
        }

        return g;
    }
    
    public ResourceManager getResources()
    {
        return resources;
    }
    
    @Override
    public void keyReleased(KeyEvent e)
    {
        keyboard.setKeyReleased(e.getKeyCode());
    }
    
    @Override
    public void keyPressed(KeyEvent e)
    {
        keyboard.setKeyPressed(e.getKeyCode());
    }
    
    @Override
    public void keyTyped(KeyEvent e)
    {
        keyboard.setKeyTyped(e.getKeyChar());
    }
    
    @Override
    public void mouseClicked(MouseEvent e)
    {
        mouse.setMouseClicked(e);
    }
    
    @Override
    public void mousePressed(MouseEvent e)
    {
        mouse.setMousePressed(e);
    }
    
    @Override
    public void mouseReleased(MouseEvent e)
    {
        mouse.setMouseReleased(e);
    }
    
    @Override
    public void mouseEntered(MouseEvent e)
    {
        mouse.setMouseEntered(e.getPoint());
    }
    
    @Override
    public void mouseExited(MouseEvent e)
    {
        mouse.setMouseExited(e.getPoint());
    }
    
    @Override
    public void mouseMoved(MouseEvent e)
    {
        mouse.setMouseMoved(e.getPoint());
    }
    
    @Override
    public void mouseDragged(MouseEvent e)
    {
        mouse.setMouseDragged(e.getPoint());
    }
    
    public Mouse getMouse()
    {
        return mouse;
    }
    
    public Keyboard getKeyboard()
    {
        return keyboard;
    }
}