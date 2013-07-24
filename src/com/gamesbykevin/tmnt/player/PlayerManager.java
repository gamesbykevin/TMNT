package com.gamesbykevin.tmnt.player;

import com.gamesbykevin.framework.base.Sprite;

import com.gamesbykevin.tmnt.enemies.Enemy;
import com.gamesbykevin.tmnt.heroes.Hero;
import com.gamesbykevin.tmnt.main.*;

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
        
        addTurtle(resources, delay, 100, 100);
        //addTurtle(resources, delay, 200, 100);
        //addTurtle(resources, delay, 200, 200);
        
        addEnemy(resources, delay, 250, 100);
    }
    
    private void addEnemy(final ResourceManager resources, final long delay, final int x, final int y)
    {
        com.gamesbykevin.tmnt.enemies.Enemy d = null;
        
        int rand = (int)(Math.random() * ResourceManager.GameEnemies.values().length);
        
        switch (rand)
        {
            case 0:
                d = new com.gamesbykevin.tmnt.enemies.FootSoldier1();
                break;
                
            case 1:
                d = new com.gamesbykevin.tmnt.enemies.FootSoldier2();
                break;
                
            case 2:
                d = new com.gamesbykevin.tmnt.enemies.FootSoldier3();
                break;
                
            case 3:
                d = new com.gamesbykevin.tmnt.enemies.FootSoldier4();
                break;
                
            case 4:
                d = new com.gamesbykevin.tmnt.enemies.FootSoldier5();
                break;
                
            case 5:
                d = new com.gamesbykevin.tmnt.enemies.FootSoldier6();
                break;
                
            case 6:
                d = new com.gamesbykevin.tmnt.enemies.FootSoldier7();
                break;
                
            case 7:
                d = new com.gamesbykevin.tmnt.enemies.FootSoldier8();
                break;
                
            default:
                d = new com.gamesbykevin.tmnt.enemies.FootSoldier9();
                break;
        }
        
        d.setImage(resources.getGameEnemy(ResourceManager.GameEnemies.values()[rand]));
        d.setDelay(delay);
        d.setLocation(x, y);
        
        enemies.add(d);
    }
    
    private void addTurtle(final ResourceManager resources, final long delay, final int x, final int y)
    {
        com.gamesbykevin.tmnt.heroes.Hero d = null;
        
        int rand = (int)(Math.random() * ResourceManager.GameHeroes.values().length);
        
        if (rand == 0)
            d = new com.gamesbykevin.tmnt.heroes.Donatello();
        if (rand == 1)
            d = new com.gamesbykevin.tmnt.heroes.Raphael();
        if (rand == 2)
            d = new com.gamesbykevin.tmnt.heroes.Leonardo();
        if (rand == 3)
            d = new com.gamesbykevin.tmnt.heroes.Michelangelo();
        
        d.setImage(resources.getGameHero(ResourceManager.GameHeroes.values()[rand]));
        d.setDelay(delay);
        d.setLocation(x, y);
        
        heroes.add(d);
    }
    
    /**
     * Add Player to list
     * @param player The player we want to add
     */
    public void add(Hero hero)
    {
        heroes.add(hero);
    }
    
    public void update(final Engine engine)
    {
        //all heroes are human
        for (Hero hero : heroes)
        {
            hero.update(engine.getKeyboard());
        }
        
        for (Enemy enemy : enemies)
        {
            enemy.update(heroes);
        }
    }
    
    public Graphics render(Graphics g)
    {
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