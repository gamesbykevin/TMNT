import javax.swing.JApplet;

import com.gamesbykevin.tmnt.main.Main;
import com.gamesbykevin.tmnt.shared.Shared;

/**
 * WE NEED THIS CLASS HERE FOR JAVA APPLETS OUTSIDE A PACKAGE SO IT CAN FIND THE STARTING POINT
 * @author GOD
 */
public class StartApplet extends JApplet
{
    private Main main;
    
    @Override
    public void init()
    {
        //the size of our game window
        setSize(Shared.WINDOW_WIDTH, Shared.WINDOW_HEIGHT);

        //allow focus on our applet
        setFocusable(true);

        //request the focus so input will be detected withoug having to click the applet window
        requestFocusInWindow();
        
        //we are going to use our own custom cursor so create transparent 1 x 1 pixel to hide default cursor
        setCursor(Shared.CURSOR);
            
        int ups = Shared.DEFAULT_UPS;

        try
        {
            //parameters here
            ups = Integer.parseInt(getParameter("ups"));
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        
        main = new Main(ups);
        main.setApplet(this);
    }
    
    @Override
    public void start()
    {
        try
        {
            main.createGameEngine();
            main.start();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
    
    @Override
    public void stop()
    {
        
    }
    
    @Override
    public void destroy()
    {
        main.destroy();
        main = null;
    }
}