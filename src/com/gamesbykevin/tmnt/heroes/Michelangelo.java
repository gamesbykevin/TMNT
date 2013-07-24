package com.gamesbykevin.tmnt.heroes;

import com.gamesbykevin.tmnt.player.PlayerRules;
import com.gamesbykevin.framework.base.SpriteSheetAnimation;

public final class Michelangelo extends Hero implements PlayerRules
{
    private static final int SPRITE_WIDTH  = 82;
    private static final int SPRITE_HEIGHT = 95;
    
    private static final int VELOCITY_WALK = 3;
    private static final int VELOCITY_JUMP = 5;
    
    /**
     * In this constructor we want to setup all of the animations for this Player
     */
    public Michelangelo()
    {
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
        this.setVelocityJump(VELOCITY_JUMP);
        this.setVelocityWalk(VELOCITY_WALK);
    }
    
    @Override
    public void setupAnimations()
    {
        SpriteSheetAnimation animation;
        
        //idle animation
        animation = new SpriteSheetAnimation();
        animation.add(getSpriteRectangle(0,0), getNanoSeconds(1250));
        animation.add(getSpriteRectangle(1,0), getNanoSeconds(1250));
        animation.add(getSpriteRectangle(2,0), getNanoSeconds(1250));
        animation.add(getSpriteRectangle(3,0), getNanoSeconds(1250));
        animation.setLoop(true);
        getSpriteSheet().add(animation, State.IDLE);
        
        //attack1 animation
        animation = new SpriteSheetAnimation();
        animation.add(getSpriteRectangle(5,1), getNanoSeconds(50));
        animation.add(getSpriteRectangle(6,1), getNanoSeconds(50));
        animation.add(getSpriteRectangle(7,1), getNanoSeconds(50));
        animation.add(getSpriteRectangle(8,1), getNanoSeconds(50));
        animation.add(getSpriteRectangle(9,1), getNanoSeconds(50));
        animation.add(getSpriteRectangle(0,2), getNanoSeconds(200));
        getSpriteSheet().add(animation, State.ATTACK1);
        
        //attack2 animation
        animation = new SpriteSheetAnimation();
        animation.add(getSpriteRectangle(1,2), getNanoSeconds(50));
        animation.add(getSpriteRectangle(2,2), getNanoSeconds(50));
        animation.add(getSpriteRectangle(3,2), getNanoSeconds(50));
        animation.add(getSpriteRectangle(4,2), getNanoSeconds(50));
        animation.add(getSpriteRectangle(5,2), getNanoSeconds(50));
        animation.add(getSpriteRectangle(6,2), getNanoSeconds(200));
        getSpriteSheet().add(animation, State.ATTACK2);
        
        //attack3 animation
        animation = new SpriteSheetAnimation();
        animation.add(getSpriteRectangle(7,2), getNanoSeconds(50));
        animation.add(getSpriteRectangle(8,2), getNanoSeconds(50));
        animation.add(getSpriteRectangle(9,2), getNanoSeconds(50));
        animation.add(getSpriteRectangle(0,3), getNanoSeconds(50));
        animation.add(getSpriteRectangle(1,3), getNanoSeconds(50));
        animation.add(getSpriteRectangle(2,3), getNanoSeconds(50));
        animation.add(getSpriteRectangle(3,3), getNanoSeconds(200));
        getSpriteSheet().add(animation, State.ATTACK3);
        
        //attack4 animation
        animation = new SpriteSheetAnimation();
        animation.add(getSpriteRectangle(7,4), getNanoSeconds(50));
        animation.add(getSpriteRectangle(8,4), getNanoSeconds(50));
        animation.add(getSpriteRectangle(9,4), getNanoSeconds(50));
        animation.add(getSpriteRectangle(0,5), getNanoSeconds(50));
        animation.add(getSpriteRectangle(1,5), getNanoSeconds(50));
        animation.add(getSpriteRectangle(2,5), getNanoSeconds(50));
        animation.add(getSpriteRectangle(3,5), getNanoSeconds(200));
        getSpriteSheet().add(animation, State.ATTACK4);

        /*
         * HE DOESN'T HAVE ATTACK 5 IN THE SPRITE SHEET
        //attack5 animation
        animation = new SpriteSheetAnimation();
        animation.add(getSpriteRectangle(8,4), getNanoSeconds(50));
        animation.add(getSpriteRectangle(9,4), getNanoSeconds(50));
        animation.add(getSpriteRectangle(0,5), getNanoSeconds(50));
        animation.add(getSpriteRectangle(1,5), getNanoSeconds(50));
        animation.add(getSpriteRectangle(2,5), getNanoSeconds(50));
        animation.add(getSpriteRectangle(3,5), getNanoSeconds(50));
        animation.add(getSpriteRectangle(4,5), getNanoSeconds(200));
        getSpriteSheet().add(animation, State.ATTACK5);
        */
        
        //jump animation
        animation = new SpriteSheetAnimation();
        animation.add(getSpriteRectangle(4,3), getNanoSeconds(50));
        animation.add(getSpriteRectangle(5,3), getNanoSeconds(50));
        animation.add(getSpriteRectangle(6,3), getNanoSeconds(50));
        animation.add(getSpriteRectangle(7,3), getNanoSeconds(50));
        animation.add(getSpriteRectangle(8,3), getNanoSeconds(50));
        animation.add(getSpriteRectangle(9,3), getNanoSeconds(50));
        animation.add(getSpriteRectangle(0,4), getNanoSeconds(50));
        animation.add(getSpriteRectangle(1,4), getNanoSeconds(50));
        animation.add(getSpriteRectangle(2,4), getNanoSeconds(50));
        animation.add(getSpriteRectangle(3,4), getNanoSeconds(50));
        getSpriteSheet().add(animation, State.JUMP);
        
        //jump kick animation
        animation = new SpriteSheetAnimation();
        animation.add(getSpriteRectangle(4,4), getNanoSeconds(25));
        animation.add(getSpriteRectangle(5,4), getNanoSeconds(75));
        animation.add(getSpriteRectangle(6,4), getNanoSeconds(75));
        getSpriteSheet().add(animation, State.JUMP_ATTACK);
        
        //walk horizontal animation
        animation = new SpriteSheetAnimation();
        animation.add(getSpriteRectangle(7,5), getNanoSeconds(75));
        animation.add(getSpriteRectangle(8,5), getNanoSeconds(75));
        animation.add(getSpriteRectangle(9,5), getNanoSeconds(75));
        animation.add(getSpriteRectangle(0,6), getNanoSeconds(75));
        animation.add(getSpriteRectangle(1,6), getNanoSeconds(75));
        animation.add(getSpriteRectangle(2,6), getNanoSeconds(75));
        animation.add(getSpriteRectangle(3,6), getNanoSeconds(75));
        animation.add(getSpriteRectangle(4,6), getNanoSeconds(75));
        animation.setLoop(true);
        getSpriteSheet().add(animation, State.WALK_HORIZONTAL);
        
        //walk vertical animation
        animation = new SpriteSheetAnimation();
        animation.add(getSpriteRectangle(5,6), getNanoSeconds(75));
        animation.add(getSpriteRectangle(6,6), getNanoSeconds(75));
        animation.add(getSpriteRectangle(7,6), getNanoSeconds(75));
        animation.add(getSpriteRectangle(8,6), getNanoSeconds(75));
        animation.add(getSpriteRectangle(9,6), getNanoSeconds(75));
        animation.add(getSpriteRectangle(0,7), getNanoSeconds(75));
        animation.add(getSpriteRectangle(1,7), getNanoSeconds(75));
        animation.add(getSpriteRectangle(2,7), getNanoSeconds(75));
        animation.setLoop(true);
        getSpriteSheet().add(animation, State.WALK_VERTICAL);
        
        //hurt animation
        animation = new SpriteSheetAnimation();
        animation.add(getSpriteRectangle(9,7), getNanoSeconds(50));
        animation.add(getSpriteRectangle(0,8), getNanoSeconds(50));
        animation.add(getSpriteRectangle(1,8), getNanoSeconds(50));
        animation.add(getSpriteRectangle(2,8), getNanoSeconds(50));
        getSpriteSheet().add(animation, State.HURT);
        
        //dead animation
        animation = new SpriteSheetAnimation();
        animation.add(getSpriteRectangle(3,8), getNanoSeconds(50));
        animation.add(getSpriteRectangle(4,8), getNanoSeconds(50));
        animation.add(getSpriteRectangle(5,8), getNanoSeconds(50));
        animation.add(getSpriteRectangle(6,8), getNanoSeconds(50));
        animation.add(getSpriteRectangle(7,8), getNanoSeconds(50));
        animation.add(getSpriteRectangle(8,8), getNanoSeconds(50));
        getSpriteSheet().add(animation, State.DEAD);
    }
}