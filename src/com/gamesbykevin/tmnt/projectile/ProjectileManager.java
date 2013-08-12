package com.gamesbykevin.tmnt.projectile;

import com.gamesbykevin.framework.base.Sprite;
import com.gamesbykevin.tmnt.player.Player;
import static com.gamesbykevin.tmnt.player.Player.VELOCITY_NONE;
import com.gamesbykevin.tmnt.main.ResourceManager.GamePlayers;
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
    
    public void update(final Rectangle screen, List<Player> targets) throws Exception
    {
        for (int i=0; i < projectiles.size(); i++)
        {
            Projectile projectile = projectiles.get(i);
            
            projectile.getSpriteSheet().update();
            projectile.update();
            
            //is this projectile targeting the heroes
            final boolean attackHero1 = PlayerManager.isEnemy(projectile.getSource());
            
            if (projectile.getSpriteSheet().getCurrent() == Player.State.PROJECTILE1)
            {
                for (Player target : targets)
                {
                    //is this player targeting the heroes
                    final boolean attackHero2 = PlayerManager.isEnemy(target.getType());
                    
                    //if the projectile came from a hero then it can't attack a hero, and same for enemy
                    if ((attackHero1 && attackHero2) || (!attackHero1 && !attackHero2))
                        continue;
                    
                    //all projectiles must be moving and the target has to be vulnerable
                    if (!target.canHurt() || !projectile.hasVelocity())
                        continue;

                    Rectangle anchorProjectile = Player.getAnchorLocation(projectile);
                    Rectangle anchorHero = target.getAnchorLocation();

                    //projectile has hit hero
                    if (anchorProjectile.intersects(anchorHero) && projectile.getRectangle().contains(target.getCenter()))
                    {
                        target.setNewState(Player.State.HURT);
                        target.deductHealth();

                        //check if there is an additional animation now that the projectile has hit
                        if (projectile.getSpriteSheet().hasAnimation(Player.State.PROJECTILE1_FINISH))
                        {
                            if (projectile.getSpriteSheet().getCurrent() != Player.State.PROJECTILE1_FINISH)
                            {
                                projectile.setVelocity(VELOCITY_NONE, VELOCITY_NONE);
                                projectile.getSpriteSheet().setCurrent(Player.State.PROJECTILE1_FINISH);
                                projectile.getSpriteSheet().reset();
                                break;
                            }
                        }
                        else
                        {
                            //if another animation does not exist then remove the projectile
                            projectiles.remove(i);
                            i--;
                            break;
                        }
                    }
                }
            }
            
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
            
        }
    }
}