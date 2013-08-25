package com.gamesbykevin.tmnt.menu.layer;

import com.gamesbykevin.framework.menu.Layer;
import com.gamesbykevin.framework.util.Timer;
import com.gamesbykevin.framework.util.TimerCollection;
import com.gamesbykevin.tmnt.main.Engine;
import com.gamesbykevin.tmnt.main.Resources;
import com.gamesbykevin.tmnt.menu.Game;

public class Credits extends Layer implements LayerRules
{
    public Credits(final Engine engine)
    {
        super(Layer.Type.SCROLL_VERTICAL_NORTH, engine.getMain().getScreen());
        
        setImage(engine.getResources().getMenuImage(Resources.MenuImage.Credits));
        setForce(false);
        setPause(false);
        setNextLayer(Game.LayerKey.MainTitle);
        setTimer(new Timer(TimerCollection.toNanoSeconds(7500L)));
    }
    
    @Override
    public void setup(final Engine engine)
    {
        //no options here to setup
    }
}