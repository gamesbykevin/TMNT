package com.gamesbykevin.tmnt.menu.layer;

import com.gamesbykevin.framework.menu.Layer;
import com.gamesbykevin.framework.util.Timer;
import com.gamesbykevin.framework.util.TimerCollection;
import com.gamesbykevin.tmnt.main.Engine;
import com.gamesbykevin.tmnt.main.Resources;
import com.gamesbykevin.tmnt.menu.Game;

public class NoFocus extends Layer implements LayerRules
{
    public NoFocus(final Engine engine)
    {
        super(Layer.Type.NONE, engine.getMain().getScreen());
        
        setImage(engine.getResources().getMenuImage(Resources.MenuImage.AppletFocus));
        setForce(false);
        setPause(true);
        
        setup(engine);
    }
    
    @Override
    public void setup(final Engine engine)
    {
        //no options here to setup
    }
}