package com.gamesbykevin.tmnt.main;

import com.gamesbykevin.framework.base.Sprite;
import com.gamesbykevin.framework.input.*;
import com.gamesbykevin.framework.input.Keyboard;

import com.gamesbykevin.tmnt.levels.*;
import com.gamesbykevin.tmnt.menu.GameMenu;
import com.gamesbykevin.tmnt.player.PlayerManager;
import com.gamesbykevin.tmnt.projectile.ProjectileManager;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.List;

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
    
    private ProjectileManager projectileManager;
    
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

                menu.update(this);

                //if the menu is on the last layer and the window has focus
                if (menu.isMenuFinished() && menu.hasFocus())
                {
                    //MAIN GAME LOGIC RUN HERE
                    
                    if (playerManager != null)
                    {
                        this.projectileManager.update(getMain().getScreen(), playerManager.getAllPlayers());
                        this.playerManager.update(this);
                        this.levelManager.update(playerManager, main.getScreen());
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
    
    public ProjectileManager getProjectileManager()
    {
        return this.projectileManager;
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
        ResourceManager.LevelMisc level;
        
        switch (menu.getOptionSelectionIndex(GameMenu.LayerKey.Options, GameMenu.OptionKey.LevelSelect))
        {
            case 0:
                level = ResourceManager.LevelMisc.Level1;
                break;
                
            case 1:
                level = ResourceManager.LevelMisc.Level2;
                break;
                
            case 2:
                level = ResourceManager.LevelMisc.Level3;
                break;
                
            case 3:
                level = ResourceManager.LevelMisc.Level4;
                break;
                
            case 4:
                level = ResourceManager.LevelMisc.Level5;
                break;
                
            case 5:
                level = ResourceManager.LevelMisc.Level6;
                break;
                
            default:
                level = ResourceManager.LevelMisc.Level1;
                break;
        }
        
        ResourceManager.GamePlayers heroType;
        
        switch (menu.getOptionSelectionIndex(GameMenu.LayerKey.Options, GameMenu.OptionKey.HeroSelect))
        {
            case 0:
                heroType = ResourceManager.GamePlayers.Donatello;
                break;
                
            case 1:
                heroType = ResourceManager.GamePlayers.Raphael;
                break;
                
            case 2:
                heroType = ResourceManager.GamePlayers.Leonardo;
                break;
                
            case 3:
                heroType = ResourceManager.GamePlayers.Michelangelo;
                break;
                
            default:
                heroType = ResourceManager.GamePlayers.Leonardo;
                break;
        }
        
        final int livesIndex = menu.getOptionSelectionIndex(GameMenu.LayerKey.Options, GameMenu.OptionKey.LivesSelect);
        
        this.projectileManager = new ProjectileManager();
        this.playerManager = new PlayerManager();
        this.levelManager = new LevelManager();
        this.levelManager.setLevel(level, resources, main.getScreen());
        
        this.playerManager.addHero(heroType, livesIndex + 5);
        
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
        Font tmpFont = g2d.getFont();
        g2d.setFont(resources.getGameFont(ResourceManager.GameFont.Dialog).deriveFont(Font.PLAIN, 12));
        
        if (this.playerManager != null && this.levelManager != null && this.projectileManager != null)
        {
            //draw the level first
            this.levelManager.render(g2d);
            
            //all objects will be contained in this list and sorted so the lowest y value is drawn first
            List<Sprite> levelObjects = new ArrayList<>();
        
            //add all level related objects to List
            levelManager.addAllStageObjects(levelObjects);
            
            //add all player related objects to List
            playerManager.addAllPlayerObjects(levelObjects);
        
            //add all of the projectiles to List
            projectileManager.addAllProjectiles(levelObjects);
            
            for (int i=0; i < levelObjects.size(); i++)
            {
                for (int x=0; x < levelObjects.size(); x++)
                {
                    if (i == x)
                        continue;

                    if (levelObjects.get(i).getY() + levelObjects.get(i).getHeight() < levelObjects.get(x).getY() + levelObjects.get(x).getHeight())
                    {
                        Sprite temp = levelObjects.get(i);

                        levelObjects.set(i, levelObjects.get(x));
                        levelObjects.set(x, temp);
                    }
                }
            }
        
            for (Sprite levelObject : levelObjects)
            {
                if (levelObject.getImage() == null)
                    continue;
                
                //get half the dimensions so we can offset/reset the coordinates
                int halfWidth = (levelObject.getWidth() / 2);
                int halfHeight = (levelObject.getHeight() / 2);

                //we need to offset the object location before drawing
                levelObject.setX(levelObject.getX() - halfWidth);
                levelObject.setY(levelObject.getY() - halfHeight);

                levelObject.draw(g2d);

                //now that the object is drawn we need to reset the location
                levelObject.setX(levelObject.getX() + halfWidth);
                levelObject.setY(levelObject.getY() + halfHeight);
            }
            
            levelObjects.clear();
            
            //draw hero health bars, etc..
            playerManager.render(g2d, getMain().getScreen());
        }
        
        g2d.setFont(tmpFont);
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