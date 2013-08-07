package com.gamesbykevin.tmnt.main;

import com.gamesbykevin.framework.resources.*;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.text.MessageFormat;
import java.util.LinkedHashMap;

/**
 * This class will load all resources and provide ways to access them
 * @author GOD
 */
public class ResourceManager 
{   
    //this will contain all resources
    private LinkedHashMap everyResource = new LinkedHashMap();
    
    //collections of resources
    private enum Type
    {
        MenuImage, MenuAudio, GameFont, GameAudioEffects, GameAudioMusic, GamePlayers, LevelMisc
    }
    
    //root directory of all resources
    public static final String RESOURCE_DIR = "resources/"; 
    
    public enum GamePlayers
    {
        FootSoldier1, FootSoldier2, FootSoldier3, FootSoldier4, 
        FootSoldier5, FootSoldier6, FootSoldier7, FootSoldier8, 
        FootSoldier9,
        
        Donatello, Raphael, Leonardo, Michelangelo,
        
        Boss1, Boss2, Boss3, Boss4, Boss5, Boss6
    }
    
    public enum LevelMisc
    {
        Level1, Level1Background, 
        Level2, 
        Level3, Level3Background, 
        Level4, Level4Background, 
        Level5, Level5Background, 
        Level6, Level6Background, 
        Pizza
    }
    
    public enum MenuAudio
    {
        MenuChange
    }
    
    public enum MenuImage
    {
        TitleScreen, Credits, AppletFocus, TitleBackground, Mouse, MouseDrag, Instructions1, Controls
    }
    
    public enum GameFont
    {
        Dialog
    }
    
    public enum GameAudioEffects
    {
        Connection
    }
    
    public enum GameAudioMusic
    {
        Song1
    }
    
    //indicates wether or not we are still loading resources
    private boolean loading = true;
    
    public ResourceManager()
    {
        //load all menu images
        add(Type.MenuImage, (Object[])MenuImage.values(), RESOURCE_DIR + "images/menu/{0}.gif", "Loading Menu Image Resources", Resources.Type.Image);
        
        //load all players
        add(Type.GamePlayers, (Object[])GamePlayers.values(), RESOURCE_DIR + "images/game/players/{0}.png", "Loading Game Player Image Resources", Resources.Type.Image);
        
        //load all levels
        add(Type.LevelMisc, (Object[])LevelMisc.values(), RESOURCE_DIR + "images/game/level/{0}.gif", "Loading Game Level Image Resources", Resources.Type.Image);
        
        //load all game fonts
        add(Type.GameFont, (Object[])GameFont.values(), RESOURCE_DIR + "font/{0}.ttf", "Loading Game Font Resources", Resources.Type.Font);
        
        //load all menu audio
        add(Type.MenuAudio, (Object[])MenuAudio.values(), RESOURCE_DIR + "audio/menu/{0}.wav", "Loading Menu Audio Resources", Resources.Type.Audio);
        
        //load all game audio
        add(Type.GameAudioEffects, (Object[])GameAudioEffects.values(), RESOURCE_DIR + "audio/game_effects/{0}.wav", "Loading Game Audio Resources", Resources.Type.Audio);

        //load all game audio
        add(Type.GameAudioMusic,  (Object[])GameAudioMusic.values(),   RESOURCE_DIR + "audio/game_music/{0}.mp3", "Loading Game Audio Resources", Resources.Type.Audio);
    }
    
    //add a collection of resources audio/image/font/text
    private void add(final Object key, final Object[] eachResourceKey, final String directory, final String loadDesc, final Resources.Type resourceType)
    {
        String[] locations = new String[eachResourceKey.length];
        for (int i=0; i < locations.length; i++)
        {
            locations[i] = MessageFormat.format(directory, i);
        }

        Resources resources = new Resources(Resources.LoadMethod.OnePerFrame, locations, eachResourceKey, resourceType);
        resources.setDesc(loadDesc);
        
        everyResource.put(key, resources);
    }
    
    public boolean isLoading()
    {
        return loading;
    }
    
    private Resources getResources(final Object key)
    {
        return (Resources)everyResource.get(key);
    }
    
    public Font getGameFont(final Object key)
    {
        return getResources(Type.GameFont).getFont(key);
    }
    
    public Image getGamePlayer(final Object key)
    {
        return getResources(Type.GamePlayers).getImage(key);
    }
    
    public Image getLevelObject(final Object key)
    {
        return getResources(Type.LevelMisc).getImage(key);
    }
    
    /*
    public Image getGameImage(Object key)
    {
        return getResources(Type.GameImage).getImage(key);
    }
    */
    
    /**
     * Load an Image on the fly
     * @param index where is the image located
     * @return Image Puzzle Image that we want
     */
    public Image getImage(Class<?> source, final int index)
    {
        final String location = MessageFormat.format(RESOURCE_DIR + "images/game/puzzles/{0}.jpg", String.valueOf(index));
        return ImageResource.getImageResource(source, location);
    }
    
    public Image getMenuImage(final Object key)
    {
        return getResources(Type.MenuImage).getImage(key);
    }
    
    public AudioResource getMenuAudio(final Object key)
    {
        return getResources(Type.MenuAudio).getAudio(key);
    }
    
    public void playMusic(final Object key, final boolean loop)
    {
        getResources(Type.GameAudioMusic).playAudio(key, loop);
    }
    
    public void playSound(final Object key, final boolean loop)
    {
        getResources(Type.GameAudioEffects).playAudio(key, loop);
    }
    
    public void stopSound(final Object key)
    {
        getResources(Type.GameAudioEffects).getAudio(key).stop();
    }
    
    public void stopAllSound()
    {
        getResources(Type.GameAudioEffects).stopAllAudio();
        getResources(Type.GameAudioMusic).stopAllAudio();
    }
    
    public void update(final Class source) 
    {
        Object[] keys = everyResource.keySet().toArray();
        
        for (Object key : keys)
        {
            Resources r = getResources(key);
            
            if (!r.isLoadingComplete())
            {
                r.loadResources(source);
                return;
            }
        }

        //if this line is reached we are done loading every resource
        loading = false;
    }
    
    public boolean isAudioEnabled()
    {
        return getResources(Type.GameAudioEffects).isAudioEnabled();
    }
    
    public void setAudioEnabled(boolean soundEnabled)
    {
        Resources r1 = getResources(Type.GameAudioEffects);
        Resources r2 = getResources(Type.GameAudioMusic);
        
        if ((r1.isAudioEnabled() && soundEnabled) || (!r1.isAudioEnabled() && !soundEnabled))
            return;
        
        r1.setAudioEnabled(soundEnabled);
        r2.setAudioEnabled(soundEnabled);
        
        if (!soundEnabled)
        {
            r1.stopAllAudio();
            r2.stopAllAudio();
        }
    }
    
    public void dispose()
    {
        Object[] keys = everyResource.keySet().toArray();
        
        for (Object key : keys)
        {
            Resources r = getResources(key);
            
            if (r != null)
                r.dispose();
            
            r = null;
            
            everyResource.put(key, r);
        }
        
        everyResource.clear();
        everyResource = null;
    }
    
    public Graphics draw(Graphics g, final Rectangle screen)
    {
        if (!loading)
            return g;
        
        Object[] keys = everyResource.keySet().toArray();
        
        for (Object key : keys)
        {
            Resources r = getResources(key);
            
            if (!r.isLoadingComplete())
            {
                Progress.draw(g, screen, r.getProgress(), r.getDesc());
                return g;
            }
        }
        
        return g;
    }
}