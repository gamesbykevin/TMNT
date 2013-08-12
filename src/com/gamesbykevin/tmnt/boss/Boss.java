package com.gamesbykevin.tmnt.boss;

import com.gamesbykevin.tmnt.grunt.Grunt;
import com.gamesbykevin.tmnt.main.ResourceManager.GamePlayers;
import com.gamesbykevin.tmnt.player.Player;
import static com.gamesbykevin.tmnt.player.Player.VELOCITY_NONE;
import com.gamesbykevin.tmnt.projectile.ProjectileManager;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.IndexColorModel;
import java.awt.Image;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

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
        
        /*
        byte[] r = new byte[]{(byte)0, (byte) 255};
        byte[] g = new byte[]{(byte)0, (byte) 255};
        byte[] b = new byte[]{(byte)0, (byte) 255};
        byte[] a = new byte[]{(byte)255, (byte) 0};
        
        IndexColorModel bitmaskColorModel = new IndexColorModel(1, 2, r, g, b, a);
        
        BufferedImage tmpFlashing = new BufferedImage(bitmaskColorModel, original.getRaster(), false, null);
        
        //get the graphics object of the buffered image
        Graphics2D g2d = flashing.createGraphics();
        
        //draw the bitmask to the buffered image
        g2d.drawImage(tmpFlashing, 0, 0, null);
        
        //release system resources for good house-keeping
        g2d.dispose();
        */
        
        super.setImage(original);
    }
    
    
    /**
     * Run the AI in Grunt class for this enemy
     */
    public void update(final ProjectileManager projectileManager, final List<Player> heroes, final Polygon boundary) throws Exception
    {
        super.update(projectileManager, heroes, boundary);
    }
}