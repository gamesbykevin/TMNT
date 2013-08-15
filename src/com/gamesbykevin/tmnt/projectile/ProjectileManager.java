package com.gamesbykevin.tmnt.projectile;

import com.gamesbykevin.framework.base.Sprite;

import com.gamesbykevin.tmnt.player.Player;
import static com.gamesbykevin.tmnt.player.Player.VELOCITY_NONE;
import com.gamesbykevin.tmnt.main.Resources.GamePlayers;
import com.gamesbykevin.tmnt.player.PlayerManager;

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
    
    //the amount of existing projectiles at one time
    private static final int PROJECTILE_LIMIT = 1;
    
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
    
    public boolean canAddProjectile()
    {
        return (projectiles.size() < PROJECTILE_LIMIT);
    }
    
    public boolean hasProjectile(GamePlayers type)
    {
        if (projectiles.size() < 1)
            return false;
        
        for (Projectile projectile : projectiles)
        {
            if (projectile.getSource() == type)
                return true;
        }
        
        return false;
    }
    
    public void addAllProjectiles(List<Sprite> levelObjects)
    {
        for (Sprite projectile : projectiles)
        {
            levelObjects.add(projectile);
        }
    }
    
    public void add(final Player sprite)
    {
        //make sure we don't have too many projectiles already
        if (!canAddProjectile())
            return;
            
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
        
        //NOTE: all enemies not including bosses have 1 projectile
        projectile.getSpriteSheet().add(sprite.getSpriteSheet().getSpriteSheetAnimation(Player.State.PROJECTILE1), Player.State.PROJECTILE1);
        
        if (sprite.getSpriteSheet().hasAnimation(Player.State.PROJECTILE1_FINISH))
        {
            projectile.getSpriteSheet().add(sprite.getSpriteSheet().getSpriteSheetAnimation(Player.State.PROJECTILE1_FINISH), Player.State.PROJECTILE1_FINISH);
        }
        
        projectile.getSpriteSheet().setCurrent(Player.State.PROJECTILE1);
        projectile.getSpriteSheet().reset();
        
        //now that projectile is setup add to the list
        projectiles.add(projectile);
    }
    
    public void update(final Rectangle screen, PlayerManager players) throws Exception
    {
        for (int i=0; i < projectiles.size(); i++)
        {
            Projectile projectile = projectiles.get(i);
            
            projectile.getSpriteSheet().update();
            projectile.update();
            
            //is this projectile currently displaying the finish animation if it has one
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
            if (!screen.intersects(projectile.getRectangle()))
            {
                projectiles.remove(i);
                i--;
                continue;
            }
            
            if (projectile.getSpriteSheet().getCurrent() == Player.State.PROJECTILE1)
            {
                if (!projectile.hasVelocity())
                    continue;
                
                boolean result = false;
                
                //if the projectile came from enemy or boss
                if (projectile.isEnemySource() || projectile.isBossSource())
                {
                    result = checkPlayers(players.getHeroManager().getPlayerHeroes(), projectile);
                    
                    if (result)
                    {
                        projectiles.remove(i);
                        i--;
                        continue;
                    }
                }
            }
        }
    }
    
    private boolean checkPlayers(final List<Player> players, final Projectile projectile)
    {
        for (Player player : players)
        {
            boolean result = checkPlayer(player, projectile);
            
            if (result)
                return result;
        }
        
        return false;
    }
    
    /**
     * Check the player and return true
     * if they have hit and do not have 
     * a finish animation so we can remove 
     * them from the list of projectiles.
     * 
     * @param target List of player we want to check for collision
     * @param projectile Projectile we are testing
     * @return boolean
     */
    private boolean checkPlayer(final Player target, final Projectile projectile)
    {
        //the target has to be vulnerable
        if (!target.canHurt())
            return false;

        //get anchor location of target and projectile to determine if they y-coordinate is level
        Rectangle anchorProjectile = Player.getAnchorLocation(projectile);
        Rectangle anchor = target.getAnchorLocation();

        //projectile has hit target
        if (anchorProjectile.intersects(anchor) && projectile.getRectangle().contains(target.getCenter()))
        {
            target.setNewState(Player.State.HURT);

            //check if there is an additional animation now that the projectile has hit
            if (projectile.getSpriteSheet().hasAnimation(Player.State.PROJECTILE1_FINISH))
            {
                if (projectile.getSpriteSheet().getCurrent() != Player.State.PROJECTILE1_FINISH)
                {
                    projectile.setVelocity(VELOCITY_NONE, VELOCITY_NONE);
                    projectile.getSpriteSheet().setCurrent(Player.State.PROJECTILE1_FINISH);
                    projectile.getSpriteSheet().reset();
                    return false;
                }
            }
            else
            {
                //if another animation does not exist then remove the projectile
                return true;
            }
        }
        
        return false;
    }
}