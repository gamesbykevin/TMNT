package com.gamesbykevin.tmnt.boss;

import com.gamesbykevin.tmnt.grunt.Grunt;
import com.gamesbykevin.tmnt.main.Engine;
import com.gamesbykevin.tmnt.main.Resources.GamePlayers;

import java.awt.color.ColorSpace;
import java.awt.image.*;

public class Boss extends Grunt
{
    private static final int HEALTH_DEFAULT = 20;
    private static final int LIVES_DEFAULT = 0;
    
    //store the original spritesheet and the same image with a flashing effect applied
    private BufferedImage original, flashing;
    
    public Boss(final GamePlayers type)
    {
        super(type);
        
        super.setHealthDefault(HEALTH_DEFAULT);
        super.setLives(LIVES_DEFAULT);
    }
    
    /**
     * This method will take the original image
     * and apply a bit mask to create a new image. 
     * The purpose of this is to have a copy of the 
     * original sprite sheet as well a copy with 
     * every animation appearing to have a flash applied.
     * Then when the boss is low on health we can 
     * apply a flashing effect.
     * 
     * @throws Exception 
     */
    public void createImages() throws Exception
    {
        if (super.getImage() == null)
            throw new Exception("Image has not been set yet");
        
        this.original = new BufferedImage(getImage().getWidth(null), getImage().getHeight(null), BufferedImage.TYPE_INT_ARGB);
        this.original.getGraphics().drawImage(getImage(), 0, 0, null);
        
        //
        BufferedImageOp op = new ColorConvertOp(ColorSpace.getInstance(ColorSpace.CS_GRAY), null);
        flashing = op.filter(original, flashing);
        
        super.setImage(flashing);
    }
    
    /**
     * Release all resources
     */
    public void dispose()
    {
        super.dispose();
        
        if (original != null)
            original.flush();
        if (flashing != null)
            flashing.flush();
        
        original = null;
        flashing = null;
    }
    
    /**
     * Run the AI in Grunt class for this enemy
     */
    public void update(final Engine engine) throws Exception
    {
        super.update(engine);
    }
}