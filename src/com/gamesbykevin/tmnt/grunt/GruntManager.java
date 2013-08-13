package com.gamesbykevin.tmnt.grunt;

import com.gamesbykevin.tmnt.enemymanager.EnemyManager;
import com.gamesbykevin.tmnt.main.ResourceManager.GamePlayers;

import java.util.ArrayList;

public class GruntManager extends EnemyManager
{
    public GruntManager()
    {
        
    }
    
    public static boolean isGrunt(final GamePlayers type)
    {
        switch (type)
        {
            case FootSoldier1:
            case FootSoldier2:
            case FootSoldier3:
            case FootSoldier4:
            case FootSoldier5:
            case FootSoldier6:
            case FootSoldier7:
            case FootSoldier8:
            case FootSoldier9:
                return true;
                
            default:
                return false;
        }
    }
    
    /**
     * Add a random grunt not including bosses.
     * A random grunt capable of throwing a projectile 
     * is a possibility as long as another one doesn't exist
     * 
     * @throws Exception 
     */
    public void addRandom() throws Exception
    {
        ArrayList possibleEnemies = new ArrayList();
        
        for (GamePlayers type : GamePlayers.values())
        {
            //first make sure this type is an enemy and we didn't already add this enemy
            if (isGrunt(type) && !super.hasType(type))
            {
                //make sure an enemy capable of throwing projectiles doesn't already exist
                if (!hasProjectileGrunt())
                {
                    possibleEnemies.add(type);
                }
                else
                {
                    //if this enemy can't throw projectiles add them for sure
                    if (!isProjectileGrunt(type))
                        possibleEnemies.add(type);
                }
            }
        }
        
        GamePlayers randomEnemy = (GamePlayers)possibleEnemies.get((int)(Math.random() * possibleEnemies.size()));
        
        add(randomEnemy);
        
        possibleEnemies.clear();
        possibleEnemies = null;
    }
    
    private void add(final GamePlayers type) throws Exception
    {
        Grunt grunt = null;
        
        switch (type)
        {
            case FootSoldier1:
                grunt = new FootSoldier1();
                break;
                
            case FootSoldier2:
                grunt = new FootSoldier2();
                break;
                
            case FootSoldier3:
                grunt = new FootSoldier3();
                break;
                
            case FootSoldier4:
                grunt = new FootSoldier4();
                break;
                
            case FootSoldier5:
                grunt = new FootSoldier5();
                break;
                
            case FootSoldier6:
                grunt = new FootSoldier6();
                break;
                
            case FootSoldier7:
                grunt = new FootSoldier7();
                break;
                
            case FootSoldier8:
                grunt = new FootSoldier8();
                break;
                
            case FootSoldier9:
                grunt = new FootSoldier9();
                break;
                
            default:
                throw new Exception("Player not found");
        }
        
        super.add(grunt);
    }
}