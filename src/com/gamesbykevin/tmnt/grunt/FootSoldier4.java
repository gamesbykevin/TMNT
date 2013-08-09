package com.gamesbykevin.tmnt.grunt;

import com.gamesbykevin.tmnt.main.ResourceManager.GamePlayers;
import com.gamesbykevin.framework.base.SpriteSheetAnimation;
import com.gamesbykevin.tmnt.player.PlayerRules;

public final class FootSoldier4 extends Grunt implements PlayerRules
{
    private static final int SPRITE_WIDTH  = 80;
    private static final int SPRITE_HEIGHT = 80;
    
    private static final int VELOCITY_WALK = 1;
    
    /**
     * In this constructor we want to setup all of the animations for this Player
     */
    public FootSoldier4()
    {
        super(GamePlayers.FootSoldier4);
        
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
        animation.add(getSpriteRectangle(1,1), getNanoSeconds(250));
        animation.add(getSpriteRectangle(0,1), getNanoSeconds(250));
        animation.setLoop(true);
        getSpriteSheet().add(animation, State.IDLE);
        
        //attack1 animation
        animation = new SpriteSheetAnimation();
        animation.add(getSpriteRectangle(1,2), getNanoSeconds(650));
        animation.add(getSpriteRectangle(0,0), getNanoSeconds(150));
        animation.add(getSpriteRectangle(2,2), getNanoSeconds(150));
        getSpriteSheet().add(animation, State.ATTACK1);
        
        //walk horizontal animation
        animation = new SpriteSheetAnimation();
        animation.add(getSpriteRectangle(4,1), getNanoSeconds(125));
        animation.add(getSpriteRectangle(3,1), getNanoSeconds(125));
        animation.add(getSpriteRectangle(0,2), getNanoSeconds(125));
        animation.add(getSpriteRectangle(2,1), getNanoSeconds(125));
        animation.setLoop(true);
        getSpriteSheet().add(animation, State.WALK_HORIZONTAL);
        
        //walk vertical animation
        animation = new SpriteSheetAnimation();
        animation.add(getSpriteRectangle(4,1), getNanoSeconds(125));
        animation.add(getSpriteRectangle(3,1), getNanoSeconds(125));
        animation.add(getSpriteRectangle(0,2), getNanoSeconds(125));
        animation.add(getSpriteRectangle(2,1), getNanoSeconds(125));
        animation.setLoop(true);
        getSpriteSheet().add(animation, State.WALK_VERTICAL);
        
        //hurt animation
        animation = new SpriteSheetAnimation();
        animation.add(getSpriteRectangle(1,0), getNanoSeconds(150));
        animation.add(getSpriteRectangle(2,0), getNanoSeconds(275));
        getSpriteSheet().add(animation, State.HURT);
        
        //dead animation
        animation = new SpriteSheetAnimation();
        animation.add(getSpriteRectangle(3,0), getNanoSeconds(200));
        animation.add(getSpriteRectangle(4,0), getNanoSeconds(1800));
        getSpriteSheet().add(animation, State.DEAD);
    }
}