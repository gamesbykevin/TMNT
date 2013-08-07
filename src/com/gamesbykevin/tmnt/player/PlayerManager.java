package com.gamesbykevin.tmnt.player;

import com.gamesbykevin.framework.base.Sprite;

import com.gamesbykevin.tmnt.enemies.*;
import com.gamesbykevin.tmnt.heroes.*;
import com.gamesbykevin.tmnt.main.*;
import com.gamesbykevin.tmnt.main.ResourceManager.GamePlayers;

import java.awt.Rectangle;
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
    
    public PlayerManager()
    {
        heroes = new ArrayList<>();
        enemies = new ArrayList<>();
    }
    
    /**
     * Add a random enemy not including bosses
     * @throws Exception 
     */
    public void addRandomEnemy() throws Exception
    {
        ArrayList possibleEnemies = new ArrayList();
        
        possibleEnemies.add(GamePlayers.FootSoldier1);
        possibleEnemies.add(GamePlayers.FootSoldier2);
        possibleEnemies.add(GamePlayers.FootSoldier3);
        possibleEnemies.add(GamePlayers.FootSoldier4);
        possibleEnemies.add(GamePlayers.FootSoldier5);
        possibleEnemies.add(GamePlayers.FootSoldier6);
        possibleEnemies.add(GamePlayers.FootSoldier7);
        possibleEnemies.add(GamePlayers.FootSoldier8);
        possibleEnemies.add(GamePlayers.FootSoldier9);
        
        GamePlayers randomEnemy = (GamePlayers)possibleEnemies.get((int)(Math.random() * possibleEnemies.size()));
        
        addEnemy(randomEnemy);
        
        possibleEnemies.clear();
        possibleEnemies = null;
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
    
    public List<Player> getPlayerEnemies()
    {
        List<Player> result = new ArrayList<>();
        
        for (Player player : enemies)
        {
            result.add(player);
        }
        
        return result;
    }
    
    public List<Player> getPlayerHeroes()
    {
        List<Player> result = new ArrayList<>();
        
        for (Player player : heroes)
        {
            result.add(player);
        }
        
        return result;
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
     * Get a list of enemies that are targeting the specific hero of type
     * @param type List of enemies
     */
    private List<Enemy> getEnemyTargets(GamePlayers assigned)
    {
        List<Enemy> enemyTargets = new ArrayList<>();
        
        for (Enemy enemy : enemies)
        {
            if (enemy.getAssignment() == null)
                continue;
            if (enemy.getAssignment() == assigned)
                enemyTargets.add(enemy);
        }
        
        return enemyTargets;
    }
    
    /**
     * Get a list of enemies that are not assigned a target
     * @param type List of enemies
     */
    private List<Enemy> getEnemyUnassigned()
    {
        List<Enemy> enemyTargets = new ArrayList<>();
        
        for (Enemy enemy : enemies)
        {
            if (enemy.getAssignment() == null)
                enemyTargets.add(enemy);
        }
        
        return enemyTargets;
    }
    
    /**
     * Add all player related objects to the existing levelObjects list.
     * 
     * @param levelObjects List of all objects in the level
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
        for (int i=0; i < heroes.size(); i++)
        {
            Hero hero = heroes.get(i);
            
            //if the hero assets are not loaded yet
            if (hero.getImage() == null)
            {
                hero.setImage(engine.getResources().getGamePlayer(hero.getType()));
                hero.setDelay(engine.getMain().getTimeDeductionPerFrame());
                
                final Rectangle r = engine.getLevelManager().getLevel().getBoundary().getBounds();
                
                hero.setLocation(r.x + hero.getWidth() + 1, r.y);
            }
            
            //if the death animation is complete and no more lives
            if (hero.isDeadComplete())
            {
                if (!hero.hasLives())
                {
                    heroes.remove(i);
                    i--;
                }
                else
                {
                    hero.setNewState(Player.State.IDLE);
                }
            }
            hero.update(engine.getKeyboard(), getPlayerEnemies(), engine.getLevelManager().getLevel());
        }
        
        //make sure enemies are targeting heroes
        updateEnemyStrategy();
        
        for (int i=0; i < enemies.size(); i++)
        {
            Enemy enemy = enemies.get(i);
            
            //if the enemy assets are not loaded yet
            if (enemy.getImage() == null)
            {
                enemy.setImage(engine.getResources().getGamePlayer(enemy.getType()));
                enemy.setDelay(engine.getMain().getTimeDeductionPerFrame());
                
                
                //NOTE NEED TO SET A STARTING LOCATION HERE
                
                //pick a random starting location for the enemies that will be somewhere off the screen
            }
            
            enemy.update(engine.getMain().getScreen(), getPlayerHeroes());
            
            //if the death animation is complete and no more lives
            if (enemy.isDeadComplete() && !enemy.hasLives())
            {
                enemies.remove(i);
                i--;
            }
        }
    }
    
    /**
     * This method will make if there are at least 2 enemies that they are set to attack each hero
     */
    private void updateEnemyStrategy()
    {
        //make sure there are enough enemies assigned to each player
        for (Hero hero : heroes)
        {
            List<Enemy> tmp = getEnemyTargets(hero.getType());
            List<Enemy> unassigned = getEnemyUnassigned();
            
            //if there are at least 2 assigned to target hero 
            if (tmp.size() >= 2)
            {
                int count = 0;
                
                for (int i=0; i < tmp.size(); i++)
                {
                    if (tmp.get(i).hasStep2() || tmp.get(i).hasStep3())
                    {
                        count++;
                        tmp.remove(i);
                    }
                }
                
                while(count < 2)
                {
                    final int randomIndex = (int)(Math.random() * tmp.size());
                    
                    tmp.get(randomIndex).setStep2(true);
                    tmp.remove(randomIndex);
                    
                    count++;
                }
            }
            else
            {
                boolean attackEast = false;
                
                //get attack side so other enemy can be assigned the other
                if (tmp.size() == 1)
                    attackEast = tmp.get(0).hasAttackEast();
                
                while (tmp.size() < 2 && unassigned.size() > 0)
                {
                    final int randIndex = (int)(Math.random() * unassigned.size());
                    
                    if (!unassigned.get(randIndex).hasStep2() && !unassigned.get(randIndex).hasStep3())
                        unassigned.get(randIndex).setStep2(true);
                    
                    if (tmp.isEmpty())
                    {
                        attackEast = unassigned.get(randIndex).hasAttackEast();
                    }
                    else
                    {
                        unassigned.get(randIndex).setAttackEast(!attackEast);
                    }
                    
                    unassigned.get(randIndex).setAssignment(hero.getType());
                    
                    tmp.add(unassigned.get(randIndex));
                    
                    unassigned.remove(randIndex);
                }
            }
            
            tmp.clear();
            unassigned.clear();
        }
        
        List<Enemy> unassigned = getEnemyUnassigned();
        
        //set remaining enemies to random heroes
        for (Enemy enemy : unassigned)
        {
            enemy.setAssignment(heroes.get((int)(Math.random() * heroes.size())).getType());
        }
    }
    
}