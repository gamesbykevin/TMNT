package com.gamesbykevin.tmnt.main;

import com.gamesbykevin.framework.base.Sprite;
import com.gamesbykevin.framework.input.*;
import com.gamesbykevin.framework.input.Keyboard;

import com.gamesbykevin.tmnt.levels.*;
import com.gamesbykevin.tmnt.main.Resources.LevelMisc;
import com.gamesbykevin.tmnt.menu.GameMenu;
import com.gamesbykevin.tmnt.player.PlayerManager;
import com.gamesbykevin.tmnt.projectile.ProjectileManager;

import java.awt.*;
import java.awt.event.*;
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
    private Resources resources;
    
    //mouse object that will be recording mouse input
    private Mouse mouse;
    
    //keyboard object that will be recording key input
    private Keyboard keyboard;
    
    //original font
    private Font font;
    
    //this player manager will contain all of our heroes, enemies, bosses
    private PlayerManager playerManager;
    
    //this will contain all information for each level
    private LevelManager levelManager;
    
    //this will contain/manage the projectiles in play
    private ProjectileManager projectileManager;
    
    //all objects will be contained in this list and sorted so the lowest y value is drawn first
    private List<Sprite> levelObjects;
    
    /**
     * The Engine that contains the game/menu objects
     * 
     * @param main Main object that contains important information so we need a reference to it
     * @throws CustomException 
     */
    public Engine(final Main main) 
    {
        this.main = main;
        this.mouse = new Mouse();
        this.keyboard = new Keyboard();
        this.resources = new Resources();
    }
    
    /**
     * Proper house-keeping
     */
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
            
            playerManager.dispose();
            playerManager = null;
            
            levelManager.dispose();
            levelManager = null;
            
            projectileManager.dispose();
            projectileManager = null;
            
            levelObjects.clear();
            levelObjects = null;
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
                //does the menu have focus
                if (!menu.hasFocus())
                {
                    //reset mouse and keyboard input
                    mouse.reset();
                    keyboard.reset();
                }

                //update the menu
                menu.update(this);

                //if the menu is on the last layer and the window has focus
                if (menu.isMenuFinished() && menu.hasFocus())
                {
                    //MAIN GAME LOGIC RUN HERE
                    if (getPlayerManager() != null)
                    {
                        getProjectileManager().update(getMain().getScreen(), getPlayerManager());
                        getPlayerManager().update(this);
                        getLevelManager().update(getPlayerManager(), main.getScreen());
                    }
                }
                
                if (mouse.isMouseReleased())
                    mouse.reset();
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
    
    public PlayerManager getPlayerManager()
    {
        return this.playerManager;
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
        Resources.LevelMisc level;
        
        switch (menu.getOptionSelectionIndex(GameMenu.LayerKey.Options, GameMenu.OptionKey.LevelSelect))
        {
            case 0:
                level = Resources.LevelMisc.Level1;
                break;
                
            case 1:
                level = Resources.LevelMisc.Level2;
                break;
                
            case 2:
                level = Resources.LevelMisc.Level3;
                break;
                
            case 3:
                level = Resources.LevelMisc.Level4;
                break;
                
            case 4:
                level = Resources.LevelMisc.Level5;
                break;
                
            case 5:
                level = Resources.LevelMisc.Level6;
                break;
                
            default:
                level = Resources.LevelMisc.Level1;
                break;
        }
        
        Resources.GamePlayers heroType;
        
        switch (menu.getOptionSelectionIndex(GameMenu.LayerKey.Options, GameMenu.OptionKey.HeroSelect))
        {
            case 0:
                heroType = Resources.GamePlayers.Donatello;
                break;
                
            case 1:
                heroType = Resources.GamePlayers.Raphael;
                break;
                
            case 2:
                heroType = Resources.GamePlayers.Leonardo;
                break;
                
            case 3:
                heroType = Resources.GamePlayers.Michelangelo;
                break;
                
            default:
                heroType = Resources.GamePlayers.Leonardo;
                break;
        }
        
        final int livesIndex = menu.getOptionSelectionIndex(GameMenu.LayerKey.Options, GameMenu.OptionKey.LivesSelect);
        
        this.projectileManager = new ProjectileManager();
        this.playerManager = new PlayerManager();
        this.levelManager = new LevelManager();
        this.levelManager.setLevel(level, resources, main.getScreen());
        
        this.playerManager.getHeroManager().add(heroType, livesIndex + 5);
        
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
        //if the resources are still loading
        if (resources.isLoading())
        {
            //draw loading screen
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
        //store the original font if we haven't already
        if (font == null)
            font = g2d.getFont();
        
        g2d.setFont(resources.getGameFont(Resources.GameFont.Dialog).deriveFont(Font.PLAIN, 8));
        
        if (getPlayerManager() != null && getLevelManager() != null && getProjectileManager() != null)
        {
            //draw the level first
            getLevelManager().render(g2d, getPlayerManager().getEnemyCount() < 1, getResources().getLevelObject(LevelMisc.April), getMain().getScreen());
            
            if (levelObjects == null)
                levelObjects = new ArrayList<>();
            
            //clear level objects list
            levelObjects.clear();
        
            //add all level related objects to List
            getLevelManager().addAllStageObjects(levelObjects);
            
            //add all player related objects to List
            getPlayerManager().addAllPlayerObjects(levelObjects);
        
            //add all of the projectiles to List
            getProjectileManager().addAllProjectiles(levelObjects);
            
            for (int i=0; i < levelObjects.size(); i++)
            {
                for (int x=0; x < levelObjects.size(); x++)
                {
                    if (i == x)
                        continue;

                    if (levelObjects.get(i).getY() + (levelObjects.get(i).getHeight() / 2) < levelObjects.get(x).getY() + (levelObjects.get(x).getHeight() / 2))
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
            
            //remove all objects in List
            levelObjects.clear();
            
            //draw hero info, etc....
            getPlayerManager().render(g2d, this);
        }
        
        //set the original font back so the menu will be rendered correctly
        g2d.setFont(font);
        
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

            if (p != null && resources.getMenuImage(Resources.MenuImage.Mouse) != null && resources.getMenuImage(Resources.MenuImage.MouseDrag) != null)
            {
                if (mouse.isMouseDragged())
                    g.drawImage(resources.getMenuImage(Resources.MenuImage.MouseDrag), p.x, p.y, null);
                else
                    g.drawImage(resources.getMenuImage(Resources.MenuImage.Mouse), p.x, p.y, null);
            }
        }

        return g;
    }
    
    public Resources getResources()
    {
        return resources;
    }
    
    @Override
    public void keyReleased(KeyEvent e)
    {
        keyboard.addKeyReleased(e.getKeyCode());
    }
    
    @Override
    public void keyPressed(KeyEvent e)
    {
        keyboard.addKeyPressed(e.getKeyCode());
    }
    
    @Override
    public void keyTyped(KeyEvent e)
    {
        keyboard.addKeyTyped(e.getKeyChar());
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