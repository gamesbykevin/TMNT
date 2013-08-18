package com.gamesbykevin.tmnt.projectile;

import com.gamesbykevin.framework.base.Sprite;

import com.gamesbykevin.tmnt.main.Engine;
import com.gamesbykevin.tmnt.main.Resources.GameAudioEffects;
import com.gamesbykevin.tmnt.main.Resources.GamePlayers;
import com.gamesbykevin.tmnt.player.Player;
import static com.gamesbykevin.tmnt.player.Player.VELOCITY_NONE;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

/**
 * This class will keep track of all projectiles
 * @author GOD
 */
public class ProjectileManager 
{
    private List<Projectile> projectiles;
    
    //the projectile speed will be a factor of the player walk speed
    private static final double PROJECTILE_SPEED_RATIO = 5;
    
    public ProjectileManager()
    {
        this.projectiles = new ArrayList<>();
    }
    
    /**
     * Proper house keeping
     */
    public void dispose()
    {
        projectiles.clear();
        projectiles = null;
    }
    
    public void addAllProjectiles(List<Sprite> levelObjects)
    {
        for (Sprite projectile : projectiles)
        {
            levelObjects.add(projectile);
        }
    }
    
    /**
     * Get the total number of projectiles
     * 
     * @return int
     */
    public int getCount()
    {
        return this.projectiles.size();
    }
    
    /**
     * Add projectile to List
     * 
     * @param sprite 
     */
    public void add(final Player sprite)
    {
        Projectile projectile = new Projectile(sprite.getType());
        projectile.createSpriteSheet();
        projectile.setLocation(sprite.getX(), sprite.getY());
        projectile.setDimensions(sprite.getWidth(), sprite.getHeight());
        projectile.setImage(sprite.getImage());

        if (sprite.hasHorizontalFlip())
        {
            projectile.setHorizontalFlip(true);
            projectile.setVelocity(-sprite.getVelocityWalk() * PROJECTILE_SPEED_RATIO, VELOCITY_NONE);
        }
        else
        {
            projectile.setHorizontalFlip(false);
            projectile.setVelocity(sprite.getVelocityWalk() * PROJECTILE_SPEED_RATIO, VELOCITY_NONE);
        }

        //we need the delay or else the animation won't update
        projectile.getSpriteSheet().setDelay(sprite.getSpriteSheet().getDelay());
        
        
        //if the player has projectile 2 then they have 2 projectiles
        if (sprite.getSpriteSheet().hasAnimation(Player.State.PROJECTILE2))
        {
            if (Math.random() > .5)
            {
                if (sprite.getSpriteSheet().hasAnimation(Player.State.PROJECTILE1_FINISH))
                    projectile.getSpriteSheet().add(sprite.getSpriteSheet().getSpriteSheetAnimation(Player.State.PROJECTILE1_FINISH), Player.State.PROJECTILE1_FINISH);
                
                projectile.getSpriteSheet().add(sprite.getSpriteSheet().getSpriteSheetAnimation(Player.State.PROJECTILE1), Player.State.PROJECTILE1);
                projectile.getSpriteSheet().setCurrent(Player.State.PROJECTILE1);
            }
            else
            {
                projectile.getSpriteSheet().add(sprite.getSpriteSheet().getSpriteSheetAnimation(Player.State.PROJECTILE2), Player.State.PROJECTILE2);
                projectile.getSpriteSheet().setCurrent(Player.State.PROJECTILE2);
            }
        }
        else
        {
            if (sprite.getSpriteSheet().hasAnimation(Player.State.PROJECTILE1_FINISH))
                projectile.getSpriteSheet().add(sprite.getSpriteSheet().getSpriteSheetAnimation(Player.State.PROJECTILE1_FINISH), Player.State.PROJECTILE1_FINISH);

            projectile.getSpriteSheet().add(sprite.getSpriteSheet().getSpriteSheetAnimation(Player.State.PROJECTILE1), Player.State.PROJECTILE1);
            projectile.getSpriteSheet().setCurrent(Player.State.PROJECTILE1);
        }
        
        projectile.getSpriteSheet().reset();
        
        //now that projectile is setup add to the list
        projectiles.add(projectile);
    }
    
    /**
     * Update every projectile we have in play.
     * 
     * @param engine
     * @throws Exception 
     */
    public void update(final Engine engine) throws Exception
    {
        //if there are no projectiles then there is no need to update
        if (projectiles.isEmpty())
            return;
        
        //update every projectile
        for (int i=0; i < projectiles.size(); i++)
        {
            //get the current projectile
            Projectile projectile = projectiles.get(i);
            
            //update animation
            projectile.getSpriteSheet().update();
            
            //update location
            projectile.update();
            
            //does this projectile have a finish animation
            if (projectile.getSpriteSheet().getCurrent() == Player.State.PROJECTILE1_FINISH)
            {
                //if the animation has finished remove projectile
                if (projectile.getSpriteSheet().hasFinished())
                {
                    projectiles.remove(i);
                    i--;
                    continue;
                }
            }
                
            //if the projectile is no longer in the current game window remove it
            if (!engine.getMain().getScreen().intersects(projectile.getRectangle()))
            {
                projectiles.remove(i);
                i--;
                continue;
            }
            
            //only check active projectiles here
            if (projectile.getSpriteSheet().getCurrent() == Player.State.PROJECTILE1 || projectile.getSpriteSheet().getCurrent() == Player.State.PROJECTILE2)
            {
                //the projectile has to be moving before we check collision
                if (!projectile.hasVelocity())
                    continue;
                
                //if the projectile came from enemy or boss
                if (projectile.isEnemySource() || projectile.isBossSource())
                {
                    boolean result = checkPlayers(engine.getPlayerManager().getHeroManager().getPlayerHeroes(), projectile);

                    //projectile has hit player
                    if (result)
                    {
                        //if projectile has finish animation there will be a different sound effect played
                        if (projectile.getSpriteSheet().getCurrent() == Player.State.PROJECTILE1_FINISH)
                        {
                            //play sound effect
                            engine.getResources().playSoundEffect(GameAudioEffects.Explosion);
                        }
                        else
                        {
                            //play other sound effect
                            engine.getResources().playSoundEffect(GameAudioEffects.ProjectileHit);
                            
                            //if projectile does not have a finish animation remove it from List
                            projectiles.remove(i);
                            i--;
                            continue;
                        }
                    }
                }
            }
        }
    }
    
    /**
     * Check the list of players to see if the projectile has collision.
     * Will return true if the projectile hit
     * 
     * @param players
     * @param projectile
     * @return boolean
     */
    private boolean checkPlayers(final List<Player> players, final Projectile projectile)
    {
        for (Player player : players)
        {
            //the target has to be vulnerable
            if (!player.canHurt())
                continue;
            
            boolean result = checkPlayer(player, projectile);
            
            if (result)
                return result;
        }
        
        return false;
    }
    
    /**
     * Check the player and return true
     * if they have hit.
     * 
     * @param target List of player we want to check for collision
     * @param projectile Projectile we are testing
     * @return boolean
     */
    private boolean checkPlayer(final Player target, final Projectile projectile)
    {
        //get anchor location of target and projectile to determine if the y-coordinate is level
        Rectangle anchorProjectile = Player.getAnchorLocation(projectile);
        Rectangle anchor = target.getAnchorLocation();

        //projectile has hit target
        if (anchorProjectile.intersects(anchor) && projectile.getRectangle().contains(target.getCenter()))
        {
            //the target has been hurt
            target.setNewState(Player.State.HURT);

            //if this is projectile 1 handle accordingly
            if (projectile.getSpriteSheet().getCurrent() == Player.State.PROJECTILE1)
            {
                //check if there is an additional animation now that the projectile has hit
                if (projectile.getSpriteSheet().hasAnimation(Player.State.PROJECTILE1_FINISH))
                {
                    if (projectile.getSpriteSheet().getCurrent() != Player.State.PROJECTILE1_FINISH)
                    {
                        projectile.setVelocity(VELOCITY_NONE, VELOCITY_NONE);
                        projectile.getSpriteSheet().setCurrent(Player.State.PROJECTILE1_FINISH);
                        projectile.getSpriteSheet().reset();
                    }
                }
            }
            else
            {
                //must be projectile 2 and this projectile kills the hero in 1 hit
                target.removeHealth();
            }
            //there has been collision return true
            return true;
        }
        
        return false;
    }
}