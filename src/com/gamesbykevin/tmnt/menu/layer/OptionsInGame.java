package com.gamesbykevin.tmnt.menu.layer;

import com.gamesbykevin.framework.menu.Layer;
import com.gamesbykevin.framework.menu.Option;
import com.gamesbykevin.tmnt.main.Engine;
import com.gamesbykevin.tmnt.main.Resources;
import com.gamesbykevin.tmnt.menu.Game;

public class OptionsInGame extends Layer implements LayerRules
{
    public OptionsInGame(final Engine engine) throws Exception
    {
        super(Layer.Type.NONE, engine.getMain().getScreen());
        
        super.setTitle("Options");
        super.setForce(false);
        super.setPause(true);
        
        setup(engine);
    }
    
    @Override
    public void setup(final Engine engine) throws Exception
    {
        //setup options here
        Option tmp;
        
        
        tmp = new Option(Game.LayerKey.StartGame);
        tmp.add("Resume", null);
        super.add(Game.OptionKey.Resume, tmp);
        
        tmp = new Option("Sound: ");
        tmp.add("On", engine.getResources().getMenuAudio(Resources.MenuAudio.MenuChange));
        tmp.add("Off",engine.getResources().getMenuAudio(Resources.MenuAudio.MenuChange));
        super.add(Game.OptionKey.Sound, tmp);
        
        tmp = new Option("FullScreen: ");
        tmp.add("Off",engine.getResources().getMenuAudio(Resources.MenuAudio.MenuChange));
        tmp.add("On", engine.getResources().getMenuAudio(Resources.MenuAudio.MenuChange));
        super.add(Game.OptionKey.FullScreen, tmp);
        
        tmp = new Option(Game.LayerKey.NewGameConfirm);
        tmp.add("New Game", null);
        super.add(Game.OptionKey.NewGame, tmp);

        tmp = new Option(Game.LayerKey.ExitGameConfirm);
        tmp.add("Exit Game", null);
        super.add(Game.OptionKey.ExitGame, tmp);
    }
}