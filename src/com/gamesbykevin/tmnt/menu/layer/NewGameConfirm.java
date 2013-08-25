package com.gamesbykevin.tmnt.menu.layer;

import com.gamesbykevin.framework.menu.Layer;
import com.gamesbykevin.framework.menu.Option;

import com.gamesbykevin.tmnt.main.Engine;
import com.gamesbykevin.tmnt.menu.Game;

public class NewGameConfirm extends Layer implements LayerRules
{
    public NewGameConfirm(final Engine engine) throws Exception
    {
        super(Layer.Type.NONE, engine.getMain().getScreen());
        
        super.setTitle("Confirm New");
        super.setForce(false);
        super.setPause(true);
        
        setup(engine);
    }
    
    @Override
    public void setup(final Engine engine) throws Exception
    {
        //setup options here
        Option tmp;
        
        tmp = new Option(Game.LayerKey.NewGameConfirmed);
        tmp.add("Yes", null);
        super.add(Game.OptionKey.NewGameConfim, tmp);
        
        tmp = new Option(Game.LayerKey.StartGame);
        tmp.add("No", null);
        super.add(Game.OptionKey.NewGameDeny, tmp);
    }
}