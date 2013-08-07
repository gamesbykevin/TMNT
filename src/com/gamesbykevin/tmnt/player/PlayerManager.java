package com.gamesbykevin.tmnt.player;

import com.gamesbykevin.tmnt.grunt.*;
import com.gamesbykevin.framework.base.Sprite;

import com.gamesbykevin.tmnt.heroes.*;
import com.gamesbykevin.tmnt.main.*;
import com.gamesbykevin.tmnt.main.ResourceManager.GamePlayers;
import java.awt.Color;

import java.awt.Graphics;
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
    private List<Grunt> grunts;
    
    public PlayerManager()
    {
        heroes = new ArrayList<>();
        grunts = new ArrayList<>();
    }
    
    public static boolean isEnemy(final GamePlayers type)
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
    
    public static boolean isHero(final GamePlayers type)
    {
        switch (type)
        {
            case Donatello:
            case Raphael:
            case Leonardo:
            case Michelangelo:
                return true;
                
            default:
                return false;
        }
    }
    
    /**
     * Add a random grunt not including bosses
     * @throws Exception 
     */
    public void addRandomEnemy() throws Exception
    {
        ArrayList possibleEnemies = new ArrayList();
        
        for (GamePlayers type : GamePlayers.values())
        {
            if (isEnemy(type))
                possibleEnemies.add(type);
        }
        
        GamePlayers randomEnemy = (GamePlayers)possibleEnemies.get((int)(Math.random() * possibleEnemies.size()));
        
        addEnemy(randomEnemy);
        
        possibleEnemies.clear();
        possibleEnemies = null;
    }
    
    public void addEnemy(final GamePlayers type) throws Exception
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
        
        grunts.add(grunt);
    }
    
    public void addHero(final GamePlayers type, final int lives) throws Exception
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
        
        hero.setLives(lives);
        heroes.add(hero);
    }
    
    /**
     * Get a list of all our grunts
     * @return List<Enemy>
     */
    public List<Grunt> getEnemies()
    {
        return this.grunts;
    }
    
    public List<Player> getPlayerEnemies()
    {
        List<Player> result = new ArrayList<>();
        
        for (Player player : grunts)
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
     * Get a list of grunts that are targeting the specific hero of type
     * @param type List of grunts
     */
    private List<Grunt> getEnemyTargets(GamePlayers assigned)
    {
        List<Grunt> gruntTargets = new ArrayList<>();
        
        for (Grunt grunt : grunts)
        {
            if (grunt.getAssignment() == null)
                continue;
            if (grunt.getAssignment() == assigned)
                gruntTargets.add(grunt);
        }
        
        return gruntTargets;
    }
    
    /**
     * Get a list of grunts that are not assigned a target
     * @param type List of grunts
     */
    private List<Grunt> getEnemyUnassigned()
    {
        List<Grunt> gruntTargets = new ArrayList<>();
        
        for (Grunt grunt : grunts)
        {
            if (grunt.getAssignment() == null)
                gruntTargets.add(grunt);
        }
        
        return gruntTargets;
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
            levelObjects.add(hero);
        }
        
        for (Player grunt : getEnemies())
        {
            levelObjects.add(grunt);
        }
    }
    
    public List<Player> getAllPlayers()
    {
        List<Player> players = new ArrayList<>();
        
        for (Player hero : getHeroes())
        {
            players.add(hero);
        }
        
        for (Player grunt : getEnemies())
        {
            players.add(grunt);
        }
        
        return players;
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
                hero.setDimensions();
                
                final Rectangle r = engine.getLevelManager().getLevel().getBoundary().getBounds();
                
                hero.setLocation(r.x + hero.getWidth() + 1, r.y);
            }
            
            //if the death animation is complete and no more lives
            if (hero.isDeadComplete())
            {
                //if the hero does not have any more lives
                if (!hero.hasLives())
                {
                    /*
                     * if there are more than 1 hero remove the dead hero
                     * because once all heroes are dead we want to leave the last one on the screen
                     */
                    if (heroes.size() > 1)
                    {
                        heroes.remove(i);
                        i--;
                    }
                }
                else
                {
                    hero.setNewState(Player.State.IDLE);
                }
            }
            
            hero.update(engine.getProjectileManager(), getPlayerEnemies(), engine.getKeyboard(), engine.getLevelManager().getLevel().getBoundary());
        }
        
        //make sure grunts are targeting heroes
        updateEnemyStrategy();
        
        for (int i=0; i < grunts.size(); i++)
        {
            Grunt grunt = grunts.get(i);
            
            //if the grunt assets are not loaded yet
            if (grunt.getImage() == null)
            {
                grunt.setImage(engine.getResources().getGamePlayer(grunt.getType()));
                grunt.setDelay(engine.getMain().getTimeDeductionPerFrame());
                grunt.setDimensions();
                grunt.setLocation(engine.getLevelManager().getLevel().getStart(engine.getMain().getScreen(), grunt.getHeight()));
            }
            
            grunt.update(engine.getProjectileManager(), getPlayerHeroes(), engine.getLevelManager().getLevel().getBoundary());
            
            //if the death animation is complete and no more lives remove the grunt
            if (grunt.isDeadComplete() && !grunt.hasLives())
            {
                grunts.remove(i);
                i--;
            }
        }
    }
    
    /**
     * This method will make if there are at least 2 grunts that they are set to attack each hero
     */
    private void updateEnemyStrategy()
    {
        //make sure there are enough grunts assigned to each player
        for (Hero hero : heroes)
        {
            List<Grunt> tmp = getEnemyTargets(hero.getType());
            List<Grunt> unassigned = getEnemyUnassigned();
            
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
                
                //get attack side so other grunt can be assigned the other
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
        
        List<Grunt> unassigned = getEnemyUnassigned();
        
        //set remaining grunts to random heroes
        for (Grunt grunt : unassigned)
        {
            grunt.setAssignment(heroes.get((int)(Math.random() * heroes.size())).getType());
        }
    }
    
    /**
     * Here we will draw the health bars for all the heroes
     * @param g Graphics object we write the information to
     * @return Graphics
     */
    public Graphics render(Graphics g, final Rectangle screen)
    {
        int x = (int)((screen.width  * .05) + screen.x);
        int y = (int)((screen.height * .1) + screen.y);
        
        for (Hero hero : heroes)
        {
            hero.renderHealthInformation(g, x, y);
            
            x += (screen.width / heroes.size());
        }
        
        return g;
    }
}