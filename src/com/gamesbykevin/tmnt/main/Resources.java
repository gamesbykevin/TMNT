package com.gamesbykevin.tmnt.main;

import com.gamesbykevin.framework.resources.*;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * This class will load all resources and provide ways to access them
 * @author GOD
 */
public class Resources 
{   
    //this will contain all resources
    private LinkedHashMap<Object, Manager> everyResource;
    
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
        Attack1, Attack2, Attack3,
        Cowabunga, 
        Explosion, 
        Hit1, Hit2, 
        ProjectileHit1, ProjectileHit2, ProjectileHit3, ProjectileHit4, 
        Jump, JumpKick,
        LetsKickShell,
        PizzaTime, 
        ShellShock
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
        everyResource = new LinkedHashMap<>();
        
        //load all menu images
        add(Type.MenuImage, (Object[])MenuImage.values(), RESOURCE_DIR + "images/menu/{0}.gif", "Loading Menu Image Resources", Manager.Type.Image);
        
        //load all players
        add(Type.GamePlayers, (Object[])GamePlayers.values(), RESOURCE_DIR + "images/game/players/{0}.png", "Loading Game Player Image Resources", Manager.Type.Image);
        
        //load all levels
        add(Type.LevelMisc, (Object[])LevelMisc.values(), RESOURCE_DIR + "images/game/level/{0}.gif", "Loading Game Level Image Resources", Manager.Type.Image);
        
        //load all game fonts
        add(Type.GameFont, (Object[])GameFont.values(), RESOURCE_DIR + "font/{0}.ttf", "Loading Game Font Resources", Manager.Type.Font);
        
        //load all menu audio
        add(Type.MenuAudio, (Object[])MenuAudio.values(), RESOURCE_DIR + "audio/menu/{0}.wav", "Loading Menu Audio Resources", Manager.Type.Audio);
        
        //load all game audio
        add(Type.GameAudioEffects, (Object[])GameAudioEffects.values(), RESOURCE_DIR + "audio/game_effects/{0}.wav", "Loading Game Audio Sound Effects Resources", Manager.Type.Audio);

        //load all game audio
        add(Type.GameAudioMusic,  (Object[])GameAudioMusic.values(),   RESOURCE_DIR + "audio/game_music/{0}.mid", "Loading Game Audio Music Resources", Manager.Type.Audio);
    }
    
    //add a collection of resources audio/image/font/text
    private void add(final Object key, final Object[] eachResourceKey, final String directory, final String loadDesc, final Manager.Type resourceType)
    {
        String[] locations = new String[eachResourceKey.length];
        for (int i=0; i < locations.length; i++)
        {
            locations[i] = MessageFormat.format(directory, i);
        }

        Manager resources = new Manager(Manager.LoadMethod.OnePerFrame, locations, eachResourceKey, resourceType);
        resources.setDesc(loadDesc);
        
        everyResource.put(key, resources);
    }
    
    public boolean isLoading()
    {
        return loading;
    }
    
    private Manager getResources(final Object key)
    {
        return everyResource.get(key);
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
    
    /**
     * Plays a random attack sound effect with no loop
     */
    public void playSoundEffectRandomAttack()
    {
        List<GameAudioEffects> sounds = new ArrayList<>();
        sounds.add(GameAudioEffects.Attack1);
        sounds.add(GameAudioEffects.Attack2);
        sounds.add(GameAudioEffects.Attack3);
        
        final int randomIndex = (int)(Math.random() * sounds.size());
        
        playSoundEffect(sounds.get(randomIndex), false);
    }
    
    /**
     * Plays a random hit sound effect with no loop
     */
    public void playSoundEffectRandomHit()
    {
        List<GameAudioEffects> sounds = new ArrayList<>();
        sounds.add(GameAudioEffects.Hit1);
        sounds.add(GameAudioEffects.Hit2);
        
        final int randomIndex = (int)(Math.random() * sounds.size());
        
        playSoundEffect(sounds.get(randomIndex), false);
    }
    
    /**
     * Plays a random hit sound effect
     * @param loop 
     */
    public void playSoundEffectRandomProjectileHit()
    {
        List<GameAudioEffects> sounds = new ArrayList<>();
        sounds.add(GameAudioEffects.ProjectileHit1);
        sounds.add(GameAudioEffects.ProjectileHit2);
        sounds.add(GameAudioEffects.ProjectileHit3);
        sounds.add(GameAudioEffects.ProjectileHit4);
        
        final int randomIndex = (int)(Math.random() * sounds.size());
        
        playSoundEffect(sounds.get(randomIndex), false);
    }
    
    public void playSoundEffect(final Object key, final boolean loop)
    {
        getResources(Type.GameAudioEffects).playAudio(key, loop);
    }
    
    public void playSoundEffect(final Object key)
    {
        playSoundEffect(key, false);
    }
    
    public void stopGameAudioMusic(final Object key)
    {
        getResources(Type.GameAudioMusic).getAudio(key).stopSound();
    }
    
    
    public void stopGameAudioEffect(final Object key)
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
        Manager audioEffectResources = getResources(Type.GameAudioEffects);
        Manager audioMusicResources = getResources(Type.GameAudioMusic);
        
        if ((audioEffectResources.isAudioEnabled() && soundEnabled) || (!audioEffectResources.isAudioEnabled() && !soundEnabled))
            return;
        
        audioEffectResources.setAudioEnabled(soundEnabled);
        audioMusicResources.setAudioEnabled(soundEnabled);
        
        //if the sound is not enabled
        if (!soundEnabled)
        {
            //stop sound effects and music
            audioEffectResources.stopAllAudio();
            audioMusicResources.stopAllAudio();
        }
    }
    
    public void dispose()
    {
        for (Object key : everyResource.keySet().toArray())
        {
            Manager resources = getResources(key);
            
            if (resources != null)
                resources.dispose();
            
            resources = null;
            
            everyResource.put(key, null);
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