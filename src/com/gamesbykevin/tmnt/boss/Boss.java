package com.gamesbykevin.tmnt.boss;

import com.gamesbykevin.framework.util.TimerCollection;

import com.gamesbykevin.tmnt.grunt.Grunt;
import com.gamesbykevin.tmnt.main.Engine;
import com.gamesbykevin.tmnt.main.Resources.GamePlayers;

public class Boss extends Grunt
{
    //health and lives default
    private static final int HEALTH_DEFAULT = 20;
    private static final int LIVES_DEFAULT = 0;
    
    //velocity
    protected static final int VELOCITY_WALK = 2;
    
    //the line we need to determine the rate of the flashing image
    public static final int HEALTH_WARNING_1 = 15;
    public static final int HEALTH_WARNING_2 = 10;
    public static final int HEALTH_WARNING_3 = 05;
    public static final int HEALTH_WARNING_4 = 01;
    
    //the delay for the boss depending on remaining health
    public static final long BOSS_FLASH_DELAY_1 = TimerCollection.toNanoSeconds(550L);
    public static final long BOSS_FLASH_DELAY_2 = TimerCollection.toNanoSeconds(300L);
    public static final long BOSS_FLASH_DELAY_3 = TimerCollection.toNanoSeconds(100L);
    public static final long BOSS_FLASH_DELAY_4 = TimerCollection.toNanoSeconds(025L);
    
    public Boss(final GamePlayers type)
    {
        super(type);
        
        super.setHealthDefault(HEALTH_DEFAULT);
        super.setLives(LIVES_DEFAULT);
    }
    
    /**
     * Release all resources
     */
    @Override
    public void dispose()
    {
        super.dispose();
    }
    
    /**
     * Run the AI in Grunt class for this enemy
     */
    @Override
    public void update(final Engine engine) throws Exception
    {
        super.update(engine);
    }
}