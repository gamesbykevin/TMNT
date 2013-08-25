package com.gamesbykevin.tmnt.menu;

import com.gamesbykevin.framework.display.FullScreen;
import com.gamesbykevin.framework.menu.*;

import com.gamesbykevin.tmnt.main.Engine;
import com.gamesbykevin.tmnt.menu.layer.*;

import java.awt.Graphics;
import java.awt.event.KeyEvent;

public class Game extends Menu
{
    //enabled = is full screen turned on, reset game = create a new game, focus = does the container have focus
    private boolean enabled = false, reset = true, focus = false;
    
    //object used to switch container to full screen
    private FullScreen fullScreen;
    
    //previous Layer key used so when container loses focus we remember where we were at
    private Object previousLayerKey;
    
    //unique object to identify each Option
    public enum OptionKey
    {
        Sound, FullScreen, StartGame, Options, Controls, Instructions, Credits, GoBack, Resume, NewGame, ExitGame, NewGameConfim, NewGameDeny, ExitGameConfirm, ExitGameDeny, Algorithm, MazeColumnsRows, Hero, Lives, Level
    }
    
    //unique key to indentify each Layer
    public enum LayerKey 
    {
        Title, Credits, MainTitle, Options, Controls, Instructions, OptionsInGame, NewGameConfirm, ExitGameConfirm, NoFocus, StartGame, NewGameConfirmed
    }
    
    //if index has a value it will be greater than -1
    private static final int NO_VALUE = -1;
    
    //assuming the menu isn't re-arranged the index will be 0
    private static final int SOUND_ENABLED = 0;

    //assuming the menu isn't re-arranged the index will be 1
    private static final int FULL_SCREEN_ENABLED = 1;
    
    public Game(final Engine engine) throws Exception
    {
        super(engine.getMain().getScreen());
        
        //add each layer to menu below
        super.add(LayerKey.Title,           new Title(engine));
        super.add(LayerKey.Credits,         new Credits(engine));
        super.add(LayerKey.MainTitle,       new MainTitle(engine));
        super.add(LayerKey.Options,         new Options(engine));
        super.add(LayerKey.Controls,        new Controls(engine));
        super.add(LayerKey.Instructions,    new Instructions(engine));
        super.add(LayerKey.OptionsInGame,   new OptionsInGame(engine));
        super.add(LayerKey.NewGameConfirm,  new NewGameConfirm(engine));
        super.add(LayerKey.ExitGameConfirm, new ExitGameConfirm(engine));
        super.add(LayerKey.NoFocus,         new NoFocus(engine));
        super.add(LayerKey.StartGame,       new StartGame(engine));
        super.add(LayerKey.NewGameConfirmed,new NewGameConfirmed(engine));
        
        //set the first layer
        super.setLayer(LayerKey.Title);
        
        //set the last layer so we know when the menu has completed
        super.setFinish(LayerKey.StartGame);
    }        
        
    /**
     * Update game menu
     * @param engine Our game engine containing all resources etc... needed to update menu
     * 
     * @throws Exception 
     */
    public void update(final Engine engine) throws Exception
    {
        //if the menu is not on the last layer we need to check for changes made in the menu
        if (!super.hasFinished())
        {
            //if we are on the main title screen and reset is not enabled
            if (super.hasCurrent(LayerKey.MainTitle) && !reset)
            {
                reset = true;
                engine.getResources().stopAllSound();
            }
            
            int soundIndex = -1, fullscreenIndex = -1;
            
            //if on the options screen check if sound/fullScreen enabled
            if (super.hasCurrent(LayerKey.Options))
            {
                soundIndex      = getOptionSelectionIndex(LayerKey.Options, OptionKey.Sound);
                fullscreenIndex = getOptionSelectionIndex(LayerKey.Options, OptionKey.FullScreen);
            }
            
            //if on the in-game options screen check if sound/fullScreen enabled
            if (super.hasCurrent(LayerKey.OptionsInGame))
            {
                soundIndex      = getOptionSelectionIndex(LayerKey.OptionsInGame, OptionKey.Sound);
                fullscreenIndex = getOptionSelectionIndex(LayerKey.OptionsInGame, OptionKey.FullScreen);
            }
            
            //if starting a new game change layer, stop sound
            if (super.hasCurrent(LayerKey.NewGameConfirmed))
            {
                super.setLayer(LayerKey.StartGame);
                reset = true;
                engine.getResources().stopAllSound();
            }
            
            if (soundIndex != NO_VALUE)
            {
                engine.getResources().setAudioEnabled(soundIndex == SOUND_ENABLED);
            }
            
            if (fullscreenIndex != NO_VALUE)
            {
                if (fullScreen == null)
                    fullScreen = new FullScreen();
                
                if (fullscreenIndex == FULL_SCREEN_ENABLED && !enabled || fullscreenIndex != FULL_SCREEN_ENABLED && enabled)
                {
                    fullScreen.switchFullScreen(engine.getMain().getApplet(), engine.getMain().getPanel());
                    enabled = !enabled;
                    engine.getMain().setFullScreen();
                }
            }
            
            //if the applet has focus and did not previously set menu layer back to cached value
            if (!focus && engine.getMain().hasFocus() && previousLayerKey != null)
            {
                super.setLayer(previousLayerKey);
                previousLayerKey = null;
            }

            //does the container have focus
            focus = engine.getMain().hasFocus();

            //if we don't have focus and the previous layer is not set, store it and display the no focus layer
            if (!focus && previousLayerKey == null)
            {
                previousLayerKey = getKey();
                super.setLayer(LayerKey.NoFocus);
            }
            
            super.update(engine.getMouse(), engine.getKeyboard(), engine.getMain().getTimeDeductionPerUpdate());
        }
        else
        {
            //if resetGame is enabled and the menu is finished reset all game objects within engine
            if (reset)
            {
                reset = false;
                engine.reset();
            }
            
            //the menu has finished and the user has pressed 'escape' so we will bring up the in game options
            if (engine.getKeyboard().hasKeyPressed(KeyEvent.VK_ESCAPE))
            {
                super.setLayer(LayerKey.OptionsInGame);
                engine.getKeyboard().reset();
            }
        }
    }
    
    public boolean hasFocus()
    {
        return focus;
    }
    
    @Override
    public Graphics render(Graphics g) throws Exception
    {
        super.render(g);
        
        return g;
    }
    
    @Override
    public void dispose()
    {
        super.dispose();
        
        if (fullScreen != null)
            fullScreen.dispose();
        
        fullScreen = null;
        
        previousLayerKey = null;
    }
}