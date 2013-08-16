package com.gamesbykevin.tmnt.heroes;

import com.gamesbykevin.framework.base.Sprite;

import com.gamesbykevin.tmnt.main.Engine;
import com.gamesbykevin.tmnt.main.Resources;
import com.gamesbykevin.tmnt.main.Resources.GameAudioMusic;
import com.gamesbykevin.tmnt.main.Resources.GamePlayers;
import com.gamesbykevin.tmnt.player.Player;
import com.gamesbykevin.tmnt.player.PlayerManager.Keys;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

public class HeroManager 
{
    //list of all heroes
    private List<Hero> heroes;
    
    //list of all heores
    private List<Player> allHeroes;
    
    //is the game over
    private boolean gameover = false;
    
    public HeroManager()
    {
        heroes = new ArrayList<>();
    }
    
    public void dispose()
    {
        for (Hero hero : heroes)
        {
            if (hero != null)
                hero.dispose();
            
            hero = null;
        }
        
        heroes.clear();
        heroes = null;
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
    
    public void add(final GamePlayers type, final int lives) throws Exception
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
     * Get a list of all heroes
     * @return List<Hero>
     */
    public List<Hero> getHeroes()
    {
        return heroes;
    }
    
    /**
     * Get a list of all heroes
     * @return List<Player>
     */
    public List<Player> getPlayerHeroes()
    {
        if (allHeroes == null)
            allHeroes = new ArrayList<>();
        
        allHeroes.clear();
        
        for (Hero hero : heroes)
        {
            allHeroes.add(hero);
        }
        
        return allHeroes;
    }
    
    /**
     * Add all player related objects to the existing levelObjects list.
     * 
     * @param levelObjects List of all objects in the level
     */
    public void addAllPlayerObjects(List<Sprite> levelObjects)
    {
        for (Player hero : heroes)
        {
            levelObjects.add(hero);
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
                hero.setDelay(engine.getMain().getTimeDeductionPerUpdate());
                hero.setDimensions();
                
                //set start location
                final Rectangle r = engine.getLevelManager().getLevel().getBoundary().getBounds();
                hero.setLocation(r.x + hero.getWidth() + 1, r.y + r.height - (hero.getHeight()));
            }
            
            //every hero should have the time deduction set in their spritesheet to avoid Exception
            if (hero.getDelay() < 0)
                hero.setDelay(engine.getMain().getTimeDeductionPerUpdate());
            
            hero.update(engine);
            
            //if the death animation is complete
            if (hero.isDeadComplete())
            {
                //if the hero does not have any more lives
                if (!hero.hasLives())
                {
                    //if there is more than 1 hero remove the dead because we want to leave the last one on the screen
                    if (heroes.size() > 1)
                    {
                        hero.dispose();
                        hero = null;
                        heroes.remove(i);
                        i--;
                    }
                    else
                    {
                        //make sure gameover isn't set yet before we set it
                        if (!gameover)
                        {
                            //since all lives lost game over stop timer
                            engine.getPlayerManager().getTimer(Keys.GamePlay).setPause(true);

                            //If all heroes are dead and no lives then the game will be over
                            gameover = true;

                            //stop all existing sound and play game over win sound
                            engine.getResources().stopAllSound();
                            engine.getResources().playGameMusic(GameAudioMusic.GameOverLose, false);
                        }
                    }
                }
                else
                {
                    //now that death is over set the state back to idle
                    hero.setNewState(Player.State.IDLE);
                    
                    //reset location so not cornered
                    final Rectangle r = engine.getLevelManager().getLevel().getBoundary().getBounds();
                    hero.setLocation(r.x + hero.getWidth() + 1, r.y + r.height - (hero.getHeight()));
                }
            }
        }
    }
    
    /**
     * If all heroes are dead and no lives then the game will be over LOSE
     * @return boolean
     */
    public boolean hasGameOver()
    {
        return this.gameover;
    }
    
    /**
     * Here we will draw the health bars for all the heroes
     * @param g Graphics object we write the information to
     * @return Graphics
     */
    public Graphics render(Graphics g, final Rectangle screen, final Resources resources)
    {
        int x = (int)((screen.width  * .05) + screen.x);
        int y = (int)((screen.height * .1) + screen.y);
        
        for (Hero hero : heroes)
        {
            final Image image;
            
            switch (hero.getType())
            {
                case Donatello:
                    image = resources.getLevelObject(Resources.LevelMisc.DonInfo);
                    break;
                    
                case Leonardo: 
                    image = resources.getLevelObject(Resources.LevelMisc.LeoInfo);
                    break;
                    
                case Raphael: 
                    image = resources.getLevelObject(Resources.LevelMisc.RaphInfo);
                    break;
                    
                case Michelangelo: 
                    image = resources.getLevelObject(Resources.LevelMisc.MikeInfo);
                    break;
                    
                default:
                    image = resources.getLevelObject(Resources.LevelMisc.LeoInfo);
                    break;
            }
            
            g.drawImage(image, x, y, null);
            
            hero.renderHealthInformation(g, x + (int)(image.getWidth(null) * .05), y + image.getHeight(null) - (int)(image.getHeight(null) * .15));
            
            x += (screen.width / heroes.size());
        }
        
        return g;
    }
}