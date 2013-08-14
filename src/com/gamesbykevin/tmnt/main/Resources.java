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
public class Resources 
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
        Slash, Krang, Leatherhead, Bebop, Shredder, Rocksteady
    }
    
    public enum LevelMisc
    {
        Level1, Level1Background, 
        Level2, 
        Level3, Level3Background, 
        Level4, Level4Background, 
        Level5, Level5Background, 
        Level6, Level6Background, 
        Pizza, 
        April, 
        DonInfo, RaphInfo, MikeInfo, LeoInfo 
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
        LevelClear, 
        Level1, Level1Boss, Level2, Level2Boss, Level3, Level3Boss, 
        Level4, Level4Boss, Level5, Level5Boss, Level6Boss, 
        GameOverWin, GameOverLose
    }
    
    //indicates wether or not we are still loading resources
    private boolean loading = true;
    
    public Resources()
    {
        //load all menu images
        add(Type.MenuImage, (Object[])MenuImage.values(), RESOURCE_DIR + "images/menu/{0}.gif", "Loading Menu Image Resources", com.gamesbykevin.framework.resources.Manager.Type.Image);
        
        //load all players
        add(Type.GamePlayers, (Object[])GamePlayers.values(), RESOURCE_DIR + "images/game/players/{0}.png", "Loading Game Player Image Resources", com.gamesbykevin.framework.resources.Manager.Type.Image);
        
        //load all levels
        add(Type.LevelMisc, (Object[])LevelMisc.values(), RESOURCE_DIR + "images/game/level/{0}.gif", "Loading Game Level Image Resources", com.gamesbykevin.framework.resources.Manager.Type.Image);
        
        //load all game fonts
        add(Type.GameFont, (Object[])GameFont.values(), RESOURCE_DIR + "font/{0}.ttf", "Loading Game Font Resources", com.gamesbykevin.framework.resources.Manager.Type.Font);
        
        //load all menu audio
        add(Type.MenuAudio, (Object[])MenuAudio.values(), RESOURCE_DIR + "audio/menu/{0}.wav", "Loading Menu Audio Resources", com.gamesbykevin.framework.resources.Manager.Type.Audio);
        
        //load all game audio
        add(Type.GameAudioEffects, (Object[])GameAudioEffects.values(), RESOURCE_DIR + "audio/game_effects/{0}.wav", "Loading Game Audio Sound Effects Resources", com.gamesbykevin.framework.resources.Manager.Type.Audio);

        //load all game audio
        add(Type.GameAudioMusic,  (Object[])GameAudioMusic.values(),   RESOURCE_DIR + "audio/game_music/{0}.mid", "Loading Game Audio Music Resources", com.gamesbykevin.framework.resources.Manager.Type.Audio);
    }
    
    //add a collection of resources audio/image/font/text
    private void add(final Object key, final Object[] eachResourceKey, final String directory, final String loadDesc, final com.gamesbykevin.framework.resources.Manager.Type resourceType)
    {
        String[] locations = new String[eachResourceKey.length];
        for (int i=0; i < locations.length; i++)
        {
            locations[i] = MessageFormat.format(directory, i);
        }

        com.gamesbykevin.framework.resources.Manager resources = new com.gamesbykevin.framework.resources.Manager(com.gamesbykevin.framework.resources.Manager.LoadMethod.OnePerFrame, locations, eachResourceKey, resourceType);
        resources.setDesc(loadDesc);
        
        everyResource.put(key, resources);
    }
    
    public boolean isLoading()
    {
        return loading;
    }
    
    private com.gamesbykevin.framework.resources.Manager getResources(final Object key)
    {
        return (com.gamesbykevin.framework.resources.Manager)everyResource.get(key);
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
    
    public Image getMenuImage(final Object key)
    {
        return getResources(Type.MenuImage).getImage(key);
    }
    
    public Audio getMenuAudio(final Object key)
    {
        return getResources(Type.MenuAudio).getAudio(key);
    }
    
    /**
     * Play game music according to parameter key
     * @param key The unique identifier used to play the music
     * @param loop Do we loop play once finished
     */
    public void playGameMusic(final Object key, final boolean loop)
    {
        getResources(Type.GameAudioMusic).playAudio(key, loop);
    }
    
    public void playSoundEffect(final Object key, final boolean loop)
    {
        getResources(Type.GameAudioEffects).playAudio(key, loop);
    }
    
    public void stopSoundEffect(final Object key)
    {
        getResources(Type.GameAudioEffects).getAudio(key).stopSound();
    }
    
    public void stopAllSound()
    {
        getResources(Type.GameAudioEffects).stopAllAudio();
        getResources(Type.GameAudioMusic).stopAllAudio();
    }
    
    public void update(final Class source) throws Exception
    {
        Object[] keys = everyResource.keySet().toArray();
        
        for (Object key : keys)
        {
            Manager resources = getResources(key);
            
            if (!resources.isLoadingComplete())
            {
                //load the resources
                resources.update(source);
                return;
            }
        }
        
        //if this line is reached we are done loading every resource
        loading = false;
    }
    
    public boolean isAudioEnabled()
    {
        return getResources(Type.GameAudioEffects).isAudioEnabled() || getResources(Type.GameAudioMusic).isAudioEnabled();
    }
    
    public void setAudioEnabled(boolean soundEnabled)
    {
        com.gamesbykevin.framework.resources.Manager r1 = getResources(Type.GameAudioEffects);
        com.gamesbykevin.framework.resources.Manager r2 = getResources(Type.GameAudioMusic);
        
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
            com.gamesbykevin.framework.resources.Manager r = getResources(key);
            
            if (r != null)
                r.dispose();
            
            r = null;
            
            everyResource.put(key, r);
        }
        
        everyResource.clear();
        everyResource = null;
    }
    
    public Graphics draw(final Graphics g, final Rectangle screen)
    {
        if (!loading)
            return g;
        
        Object[] keys = everyResource.keySet().toArray();
        
        for (Object key : keys)
        {
            Manager resources = getResources(key);
            
            if (!resources.isLoadingComplete())
            {
                Progress.draw(g, screen, resources.getProgress(), resources.getDesc());
                return g;
            }
        }
        
        return g;
    }
}