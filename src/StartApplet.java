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
        
        //set default values in case no parameters have been passed
        int ups = Shared.DEFAULT_UPS;
        int fps = Shared.DEFAULT_FPS;
        
        try
        {
            //get parameters here
            ups = Integer.parseInt(getParameter("ups"));
            fps = Integer.parseInt(getParameter("fps"));
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        
        //create new instance of Main with specified ups/fps
        main = new Main(ups, fps);
        
        //add applet to instance so  we can perform certain features like "full-screen"
        main.setApplet(this);
    }
    
    @Override
    public void start()
    {
        try
        {
            //create a new instance of our engine
            main.create();
            
            //start the thread
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
        //free up resources
        main.dispose();
        main = null;
    }
}