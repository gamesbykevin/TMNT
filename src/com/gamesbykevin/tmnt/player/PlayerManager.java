package com.gamesbykevin.tmnt.player;

import com.gamesbykevin.framework.util.TimerCollection;

import com.gamesbykevin.tmnt.enemies.*;
import com.gamesbykevin.tmnt.heroes.*;
import com.gamesbykevin.tmnt.main.*;
import com.gamesbykevin.tmnt.main.ResourceManager.GameHeroes;
import com.gamesbykevin.tmnt.main.ResourceManager.GameEnemies;

import java.awt.Graphics;
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
        
        addHero(resources, delay, 200, 150);
        //addHero(resources, delay, 200, 100);
        //addHero(resources, delay, 200, 200);
        
        addEnemy(resources, delay, 350, 100);
        //addEnemy(resources, delay, 350, 200);
        //addEnemy(resources, delay, 50, 200);
        //addEnemy(resources, delay, 50, 100);
    }
    
    private void addEnemy(final ResourceManager resources, final long delay, final int x, final int y)
    {
        Enemy enemy = null;
        
        int rand = (int)(Math.random() * GameEnemies.values().length);
        
        switch (GameEnemies.values()[rand])
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
                enemy = new FootSoldier9();
                break;
        }
        
        enemy.setImage(resources.getGameEnemy(GameEnemies.values()[rand]));
        enemy.setDelay(delay);
        enemy.setLocation(x, y);
        
        enemies.add(enemy);
    }
    
    private void addHero(final ResourceManager resources, final long delay, final int x, final int y)
    {
        Hero hero;
        
        int heroIndex = (int)(Math.random() * GameHeroes.values().length);
        
        switch(GameHeroes.values()[heroIndex])
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
                hero = new Michelangelo();
                break;
        }
        
        hero.setImage(resources.getGameHero(GameHeroes.values()[heroIndex]));
        hero.setDelay(delay);
        hero.setLocation(x, y);
        
        heroes.add(hero);
    }
    
    /**
     * Add Player to list
     * @param player The player we want to add
     */
    public void add(Hero hero)
    {
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
    
    public void update(final Engine engine) throws Exception
    {
        //NOTE: all heroes are human for now, we may have AI friends
        for (Hero hero : heroes)
        {
            hero.update(engine.getKeyboard(), enemies, engine.getLevelManager().getLevel());
        }
        
        //make sure we have enemies attacking the heroes
        updateStrategy();
        
        for (Enemy enemy : enemies)
        {
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
    
    public Graphics render(Graphics g)
    {
        //all players will be contained in this list and sorted so the lowest y value is drawn first
        List<Player> players = new ArrayList<>();
        
        for (Hero hero : heroes)
        {
            players.add(hero);
        }
        
        for (Enemy enemy : enemies)
        {
            players.add(enemy);
        }
        
        for (int i=0; i < players.size(); i++)
        {
            for (int x=0; x < players.size(); x++)
            {
                if (i == x)
                    continue;
                
                if (players.get(i).getY() + players.get(i).getHeight() < players.get(x).getY() + players.get(x).getHeight())
                {
                    Player temp = players.get(i);
                    
                    players.set(i, players.get(x));
                    players.set(x, temp);
                }
            }
        }
        
        for (Player player : players)
        {
            player.render(g);
        }
        
        return g;
    }
}