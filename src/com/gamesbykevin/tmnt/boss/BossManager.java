package com.gamesbykevin.tmnt.boss;

import com.gamesbykevin.tmnt.enemymanager.EnemyManager;
import com.gamesbykevin.tmnt.grunt.Grunt;
import com.gamesbykevin.tmnt.main.Engine;
import com.gamesbykevin.tmnt.main.Resources.GamePlayers;
import java.awt.Point;

import java.util.ArrayList;
import java.util.List;

public class BossManager extends EnemyManager
{
    
    
    public BossManager()
    {
        
    }
    
    public static boolean isBoss(final GamePlayers type)
    {
        switch (type)
        {
            case Slash:
            case Bebop:
            case Rocksteady:
            case Leatherhead:
            case Krang:
            case Shredder:
                return true;
                
            default:
                return false;
        }
    }
    
    public void add(final GamePlayers type) throws Exception
    {
        Boss boss;
        
        switch (type)
        {
            case Slash:
                boss = new Slash();
                break;
                
            case Bebop:
                boss = new Bebop();
                break;
                
            case Krang:
                boss = new Krang();
                break;
                
            case Leatherhead:
                boss = new Leatherhead();
                break;
                
            case Rocksteady:
                boss = new Rocksteady();
                break;
                
            case Shredder:
                boss = new Shredder();
                break;
                
            default:
                throw new Exception("Player not found");
        }
        
        //add boss to list
        super.add(boss);
    }
}