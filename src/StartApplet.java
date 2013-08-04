import com.gamesbykevin.tmnt.shared.Shared;
import javax.swing.JApplet;

import com.gamesbykevin.tmnt.main.Main;

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
        setSize(Shared.INITIAL_WIDTH, Shared.INITIAL_HEIGHT);

        //allow focus on our applet
        setFocusable(true);

        //request the focus so input will be detected withoug having to click the applet window
        requestFocusInWindow();
        
        //use cursor from Shared class
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