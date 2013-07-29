package com.gamesbykevin.tmnt.player;

import java.util.List;

public interface PlayerRules 
{
    /**
     * We need to setup the animations for each Player
     */
    public void setupAnimations();
    
    /**
     * We need to set the default velocity speeds for the player walking, jumping, etc...
     */
    public void setupVelocity();
    
    /**
     * Setup all default values for player
     */
    public void setupDefaults();
}
