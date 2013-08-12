package com.gamesbykevin.tmnt.grunt;

import com.gamesbykevin.framework.base.SpriteSheetAnimation;

import com.gamesbykevin.tmnt.main.ResourceManager.GamePlayers;
import com.gamesbykevin.tmnt.player.Player;
import com.gamesbykevin.tmnt.player.PlayerRules;

public final class FootSoldier8 extends Grunt implements PlayerRules
{
    private static final int SPRITE_WIDTH  = 60;
    private static final int SPRITE_HEIGHT = 60;
    
    private static final int VELOCITY_WALK = 1;
    
    /**
     * In this constructor we want to setup all of the animations for this Player
     */
    public FootSoldier8()
    {
        super(GamePlayers.FootSoldier8);
        
        //all default settings for this player
        setupDefaults();
    }
    
    @Override
    public void setupDefaults()
    {
        //setup dimensions
        super.setDimensions(SPRITE_WIDTH, SPRITE_HEIGHT);
        
        //setup all animations for this player
        setupAnimations();
        
        //setup all velocity for this player
        setupVelocity();
    }
    
    @Override
    public void setupVelocity()
    {
        this.setVelocityWalk(VELOCITY_WALK);
    }
    
    @Override
    public void setupAnimations()
    {
        SpriteSheetAnimation animation;
        
        //idle animation
        animation = new SpriteSheetAnimation();
        animation.add(getSpriteRectangle(3,1), getNanoSeconds(250));
        animation.add(getSpriteRectangle(4,1), getNanoSeconds(250));
        animation.setLoop(true);
        getSpriteSheet().add(animation, Player.State.IDLE);
        
        //attack1 animation
        animation = new SpriteSheetAnimation();
        animation.add(getSpriteRectangle(4,2), getNanoSeconds(350));
        animation.add(getSpriteRectangle(3,2), getNanoSeconds(350));
        getSpriteSheet().add(animation, Player.State.ATTACK1);
        
        //throw projectile animation
        animation = new SpriteSheetAnimation();
        animation.add(getSpriteRectangle(2,0), getNanoSeconds(850));
        animation.add(getSpriteRectangle(0,0), getNanoSeconds(150));
        getSpriteSheet().add(animation, Player.State.THROW_PROJECTILE);
        
        //projectile animation
        animation = new SpriteSheetAnimation();
        animation.add(getSpriteRectangle(1,0), getNanoSeconds(75));
        animation.add(getSpriteRectangle(3,0), getNanoSeconds(75));
        animation.setLoop(true);
        getSpriteSheet().add(animation, Player.State.PROJECTILE1);
        
        //walk horizontal animation
        animation = new SpriteSheetAnimation();
        animation.add(getSpriteRectangle(1,2), getNanoSeconds(125));
        animation.add(getSpriteRectangle(0,2), getNanoSeconds(125));
        animation.add(getSpriteRectangle(2,2), getNanoSeconds(125));
        animation.setLoop(true);
        getSpriteSheet().add(animation, Player.State.WALK_HORIZONTAL);
        
        //walk vertical animation
        animation = new SpriteSheetAnimation();
        animation.add(getSpriteRectangle(1,2), getNanoSeconds(125));
        animation.add(getSpriteRectangle(0,2), getNanoSeconds(125));
        animation.add(getSpriteRectangle(2,2), getNanoSeconds(125));
        animation.setLoop(true);
        getSpriteSheet().add(animation, Player.State.WALK_VERTICAL);
        
        //hurt animation
        animation = new SpriteSheetAnimation();
        animation.add(getSpriteRectangle(4,0), getNanoSeconds(150));
        animation.add(getSpriteRectangle(0,1), getNanoSeconds(275));
        getSpriteSheet().add(animation, Player.State.HURT);
        
        //dead animation
        animation = new SpriteSheetAnimation();
        animation.add(getSpriteRectangle(1,1), getNanoSeconds(200));
        animation.add(getSpriteRectangle(2,1), getNanoSeconds(1800));
        getSpriteSheet().add(animation, Player.State.DEAD);
    }
}
