package com.gamesbykevin.tmnt.menu.layer;

import com.gamesbykevin.framework.labyrinth.Labyrinth.Algorithm;
import com.gamesbykevin.framework.menu.Layer;
import com.gamesbykevin.framework.menu.Option;

import com.gamesbykevin.framework.util.Timer;
import com.gamesbykevin.framework.util.TimerCollection;
import com.gamesbykevin.tmnt.heroes.HeroManager;

import com.gamesbykevin.tmnt.main.Engine;
import com.gamesbykevin.tmnt.main.Resources;
import com.gamesbykevin.tmnt.menu.Game;

public class Options extends Layer implements LayerRules
{
    //maze will default to 5 row/col
    private static final int MAZE_DIMENSION_SIZE = 5;
    
    //maze limit will be 30 row/col
    private static final int MAZE_DIMENSION_LIMIT = 30;
    
    public Options(final Engine engine) throws Exception
    {
        super(Layer.Type.SCROLL_HORIZONTAL_WEST_REPEAT, engine.getMain().getScreen());
        
        super.setTitle("Options");
        super.setImage(engine.getResources().getMenuImage(Resources.MenuImage.TitleBackground));
        super.setTimer(new Timer(TimerCollection.toNanoSeconds(5000L)));
        super.setForce(false);
        super.setPause(true);
        
        setup(engine);
    }
    
    @Override
    public void setup(final Engine engine) throws Exception
    {
        //setup options here
        Option tmp;
        
        tmp = new Option("Hero: ");
        for (Resources.GamePlayers type : Resources.GamePlayers.values())
        {
            if (HeroManager.isHero(type))
                tmp.add(type.toString(), engine.getResources().getMenuAudio(Resources.MenuAudio.MenuChange));
        }
        super.add(Game.OptionKey.Hero, tmp);
        
        tmp = new Option("Level: ");
        tmp.add("1", engine.getResources().getMenuAudio(Resources.MenuAudio.MenuChange));
        tmp.add("2", engine.getResources().getMenuAudio(Resources.MenuAudio.MenuChange));
        tmp.add("3", engine.getResources().getMenuAudio(Resources.MenuAudio.MenuChange));
        tmp.add("4", engine.getResources().getMenuAudio(Resources.MenuAudio.MenuChange));
        tmp.add("5", engine.getResources().getMenuAudio(Resources.MenuAudio.MenuChange));
        tmp.add("6", engine.getResources().getMenuAudio(Resources.MenuAudio.MenuChange));
        super.add(Game.OptionKey.Level, tmp);
        
        tmp = new Option("Lives: ");
        tmp.add("5", engine.getResources().getMenuAudio(Resources.MenuAudio.MenuChange));
        tmp.add("6", engine.getResources().getMenuAudio(Resources.MenuAudio.MenuChange));
        tmp.add("7", engine.getResources().getMenuAudio(Resources.MenuAudio.MenuChange));
        tmp.add("8", engine.getResources().getMenuAudio(Resources.MenuAudio.MenuChange));
        tmp.add("9", engine.getResources().getMenuAudio(Resources.MenuAudio.MenuChange));
        tmp.add("10", engine.getResources().getMenuAudio(Resources.MenuAudio.MenuChange));
        super.add(Game.OptionKey.Lives, tmp);

        tmp = new Option("Sound: ");
        tmp.add("On", engine.getResources().getMenuAudio(Resources.MenuAudio.MenuChange));
        tmp.add("Off",engine.getResources().getMenuAudio(Resources.MenuAudio.MenuChange));
        super.add(Game.OptionKey.Sound, tmp);
        
        tmp = new Option("FullScreen: ");
        tmp.add("Off",engine.getResources().getMenuAudio(Resources.MenuAudio.MenuChange));
        tmp.add("On", engine.getResources().getMenuAudio(Resources.MenuAudio.MenuChange));
        super.add(Game.OptionKey.FullScreen, tmp);
        
        tmp = new Option(Game.LayerKey.MainTitle);
        tmp.add("Go Back", null);
        super.add(Game.OptionKey.GoBack, tmp);
    }
}