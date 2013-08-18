package com.gamesbykevin.tmnt.levels;

/**
 * Rules to set for each level
 * @author GOD
 */
public interface LevelRules 
{
    /**
     * This is required to set the boundaries for each level
     */
    public void setLevelBounds();
    
    /**
     * Set the appropriate music for the level and for the boss
     */
    public void setMusic();
    
    /**
     * Set the number of power ups for this level
     */
    public void setPowerupLimit();
    
    /**
     * Set the number of enemies that can be created once
     * and the number of enemies that will spawn at each 
     * checkpoint.
     * 
     * @throws Exception 
     */
    public void setEnemyLimits() throws Exception;
}