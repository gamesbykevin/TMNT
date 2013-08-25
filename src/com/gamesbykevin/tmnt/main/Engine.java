package com.gamesbykevin.tmnt.main;

import com.gamesbykevin.framework.base.Sprite;
import com.gamesbykevin.framework.input.*;
import com.gamesbykevin.framework.input.Keyboard;
import com.gamesbykevin.framework.util.TimerCollection;

import com.gamesbykevin.tmnt.levels.*;
import com.gamesbykevin.tmnt.main.Resources.LevelMisc;
import com.gamesbykevin.tmnt.menu.Game;
import com.gamesbykevin.tmnt.player.PlayerManager;
import com.gamesbykevin.tmnt.projectile.ProjectileManager;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.List;

//TODO here we need to have the resources object and the menu object

public class Engine implements KeyListener, MouseMotionListener, MouseListener, EngineRules 
{
    //our Main class has important information in it so we need a reference here
    private Main main;
    
    //access this menu here
    private Game menu;
    
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
    
    //image for game over screen
    private BufferedImage gameover;
    
    /**
     * The Engine that contains the game/menu objects
     * 
     * @param main Main object that contains important information so we need a reference to it
     * @throws CustomException 
     */
    public Engine(final Main main) throws Exception
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
                    menu = new Game(this);
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
                if (menu.hasFinished() && menu.hasFocus())
                {
                    //MAIN GAME LOGIC RUN HERE
                    if (getPlayerManager() != null)
                    {
                        getProjectileManager().update(this);
                        getPlayerManager().update(this);
                        getLevelManager().update(this);
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
        //stop all sound before starting game
        getResources().stopAllSound();
        
        //the level the user selected
        Resources.LevelMisc level = Resources.LevelMisc.Level1;
        final int levelIndex = menu.getOptionSelectionIndex(Game.LayerKey.Options, Game.OptionKey.Level);
        
        if (levelIndex == 0)
            level = Resources.LevelMisc.Level1;
        if (levelIndex == 1)
            level = Resources.LevelMisc.Level2;
        if (levelIndex == 2)
            level = Resources.LevelMisc.Level3;
        if (levelIndex == 3)
            level = Resources.LevelMisc.Level4;
        if (levelIndex == 4)
            level = Resources.LevelMisc.Level5;
        if (levelIndex == 5)
            level = Resources.LevelMisc.Level6;
        
        //the hero the user chose
        Resources.GamePlayers heroType = Resources.GamePlayers.Michelangelo;
        final int heroIndex = menu.getOptionSelectionIndex(Game.LayerKey.Options, Game.OptionKey.Hero);
        
        if (heroIndex == 0)
            heroType = Resources.GamePlayers.Donatello;
        if (heroIndex == 1)
            heroType = Resources.GamePlayers.Raphael;
        if (heroIndex == 2)
            heroType = Resources.GamePlayers.Leonardo;
        if (heroIndex == 3)
            heroType = Resources.GamePlayers.Michelangelo;
        
        final int livesIndex = menu.getOptionSelectionIndex(Game.LayerKey.Options, Game.OptionKey.Lives);
        
        if (projectileManager != null)
            projectileManager.dispose();
        
        this.projectileManager = new ProjectileManager();
        
        if (playerManager != null)
            playerManager.dispose();
        
        this.playerManager = new PlayerManager(getMain().getTimeDeductionPerUpdate());
        this.playerManager.getHeroManager().add(heroType, livesIndex + 5);
        
        if (levelManager != null)
            levelManager.dispose();
        
        this.levelManager = new LevelManager();
        this.levelManager.setLevel(level, resources, main.getScreen());
        
        if (gameover != null)
            gameover.flush();
        
        gameover = null;
    }
    
    /**
     * Draw our game to the Graphics object whether resources are still loading or the game is intact
     * @param g
     * @return Graphics
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
            //if we beat the game or died trying
            if (getLevelManager().hasGameOver() || getPlayerManager().getHeroManager().hasGameOver())
            {
                if (gameover == null)
                {
                    final String result;

                    if (getLevelManager().hasGameOver())
                        result = "Game Over You Win";
                    else
                        result = "Game Over You Lose";

                    final int defeated = getPlayerManager().getEnemiesDefeatedCount();
                    final String timeDesc = getPlayerManager().getTimer(PlayerManager.Keys.GamePlay).getDescPassed(TimerCollection.FORMAT_6);
                    
                    gameover = new BufferedImage(getMain().getScreen().width, getMain().getScreen().height, BufferedImage.TYPE_INT_ARGB);
                    
                    Graphics tmp = gameover.getGraphics();
                    tmp.setFont(font.deriveFont(Font.BOLD, 16));
                    
                    int x = getMain().getScreen().x + (int)(getMain().getScreen().width  * .1);
                    int y = getMain().getScreen().y + (int)(getMain().getScreen().height * .1);
                    
                    tmp.drawString(result, x, y);
                    y += (tmp.getFontMetrics().getHeight() * 2);
                    tmp.drawString("Defeated: " + defeated, x, y);
                    y += (tmp.getFontMetrics().getHeight() * 2);
                    tmp.drawString("Play Time: " + timeDesc, x, y);
                    y += (tmp.getFontMetrics().getHeight() * 2);
                    tmp.drawString("Hit \"Esc\" to access menu.", x, y);
                }
                else
                {
                    //draw all player objects, projectiles, and level power ups
                    getPlayerManager().render(g2d, this);
                    
                    //draw game over screen
                    g2d.drawImage(gameover, 0, 0, null);
                }
            }
            else
            {
                //draw the level first
                getLevelManager().render(g2d, !getPlayerManager().hasEnemies(), getResources().getLevelObject(LevelMisc.April), getMain().getScreen());

                //draw all player objects, projectiles, and level power ups
                getPlayerManager().render(g2d, this);
            }
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
        if (menu.isSetup())
            menu.render(g);

        //is menu is finished and we dont want to hide mouse cursor then draw it, or if the menu is not finished show mouse
        if (menu.hasFinished() && !Main.hideMouse || !menu.hasFinished())
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