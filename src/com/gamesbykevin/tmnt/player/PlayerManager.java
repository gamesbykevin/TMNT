package com.gamesbykevin.tmnt.player;

import com.gamesbykevin.framework.base.Sprite;

import com.gamesbykevin.tmnt.enemies.*;
import com.gamesbykevin.tmnt.heroes.*;
import com.gamesbykevin.tmnt.main.*;
import com.gamesbykevin.tmnt.main.ResourceManager.GamePlayers;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

/**
 * This class will contain all of the actors in the game
 * @author GOD
 */
public class PlayerManager 
{
    private List<Hero> heroes;
    private List<Enemy> enemies;
    
    public PlayerManager(final ResourceManager resources, final long delay)
    {
        heroes = new ArrayList<>();
        enemies = new ArrayList<>();
    }
    
    public void addEnemy(final GamePlayers type) throws Exception
    {
        Enemy enemy = null;
        
        switch (type)
        {
            case FootSoldier1:
                enemy = new FootSoldier1();
                break;
                
            case FootSoldier2:
                enemy = new FootSoldier2();
                break;
                
            case FootSoldier3:
                enemy = new FootSoldier3();
                break;
                
            case FootSoldier4:
                enemy = new FootSoldier4();
                break;
                
            case FootSoldier5:
                enemy = new FootSoldier5();
                break;
                
            case FootSoldier6:
                enemy = new FootSoldier6();
                break;
                
            case FootSoldier7:
                enemy = new FootSoldier7();
                break;
                
            case FootSoldier8:
                enemy = new FootSoldier8();
                break;
                
            case FootSoldier9:
                enemy = new FootSoldier9();
                break;
                
            default:
                throw new Exception("Player not found");
        }
        
        enemies.add(enemy);
    }
    
    public void addHero(final GamePlayers type) throws Exception
    {
        Hero hero;
        
        switch(type)
        {
            case Donatello:
                hero = new Donatello();
                break;
                
            case Raphael:
                hero = new Raphael();
                break;
                
            case Leonardo:
                hero = new Leonardo();
                break;
                
            case Michelangelo:
                hero = new Michelangelo();
                break;
                
            default:
                throw new Exception("Player not found");
        }
        
        heroes.add(hero);
    }
    
    /**
     * Get a list of all our enemies
     * @return List<Enemy>
     */
    public List<Enemy> getEnemies()
    {
        return this.enemies;
    }
    
    /**
     * Get a list of all our heroes
     * @return List<Hero>
     */
    public List<Hero> getHeroes()
    {
        return this.heroes;
    }
    
    /**
     * Add all player related objects to the 
     * existing levelObjects list.
     * 
     * @param levelObjects
     */
    public void addAllPlayerObjects(List<Sprite> levelObjects)
    {
        //all objects will be contained in this list and sorted so the lowest y value is drawn first
        
        for (Player hero : getHeroes())
        {
            if (hero.hasProjectile())
                levelObjects.add(hero.getProjectile());

            levelObjects.add(hero);
        }
        
        for (Player enemy : getEnemies())
        {
            if (enemy.hasProjectile())
                levelObjects.add(enemy.getProjectile());

            levelObjects.add(enemy);
        }
    }
    
    public void update(final Engine engine) throws Exception
    {
        //NOTE: all heroes are human for now, we may have AI friends
        for (Hero hero : heroes)
        {
            if (hero.getImage() == null)
            {
                hero.setImage(engine.getResources().getGamePlayer(hero.getType()));
                hero.setDelay(engine.getMain().getTimeDeductionPerFrame());
                hero.setLocation(200, 150);
            }
            
            hero.update(engine.getKeyboard(), enemies, engine.getLevelManager().getLevel());
        }
        
        //make sure we have enemies attacking the heroes
        updateStrategy();
        
        for (Enemy enemy : enemies)
        {
            if (enemy.getImage() == null)
            {
                enemy.setImage(engine.getResources().getGamePlayer(enemy.getType()));
                enemy.setDelay(engine.getMain().getTimeDeductionPerFrame());
                enemy.setLocation(300, 150);
            }
            
            enemy.update(engine.getMain().getScreen(), heroes);
        }
    }
    
    /**
     * This method will make if there are at least 2 enemies that they are set to attack
     */
    private void updateStrategy()
    {
        int count = 0;
        
        //is there an enemy attacking from the east
        boolean attackEast = false;
        
        //temp list
        List<Enemy> tmp = new ArrayList<>();
        
        for (Enemy enemy : enemies)
        {
            //if there is an existing enemy already in the process of attacking
            if (enemy.hasStep2() || enemy.hasStep3())
            {
                if (enemy.hasAttackEast())
                    attackEast = true;
                
                count++;
            }
            else
            {
                //if enemy is not attacking add them to possible list
                tmp.add(enemy);
            }
        }
        
        while (count < 2)
        {
            if (tmp.size() > 0)
            {
                final int rand = (int)(Math.random() * tmp.size());
                tmp.get(rand).setStep2(true);
                
                if (attackEast)
                {
                    //if there is an enemy already attacking the east we must attack west
                    tmp.get(rand).setAttackEast(false);
                }
                else
                {
                    //pick a random attack direction
                    final boolean result = (Math.random()> .5);
                    
                    //if result is true set east attack
                    if (result)
                        attackEast = true;
                    
                    tmp.get(rand).setAttackEast(result);
                }
                
                tmp.remove(rand);
                
                count++;
            }
            else
            {
                break;
            }
        }
        
        tmp.clear();
        tmp = null;
    }
}