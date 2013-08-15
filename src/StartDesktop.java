import com.gamesbykevin.tmnt.main.Main;
import com.gamesbykevin.tmnt.shared.Shared;

import java.awt.Dimension;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * This file will run the game as a desktop application
 * @author GOD
 */
public class StartDesktop extends JPanel
{
    private Main main;
    
    public StartDesktop()
    {
        setCursor(Shared.CURSOR);
        setPreferredSize(new Dimension(Shared.INITIAL_WIDTH, Shared.INITIAL_HEIGHT));
        setFocusable(true);
        requestFocus();
        
        try
        {
            //create a new instance of main with the specified ups/fps
            main = new Main(Shared.DEFAULT_UPS, Shared.DEFAULT_FPS);
            
            //add JPanel instance to Main instance because some features like "full-screen" need this
            main.setPanel(this);
            
            //new instance of our main engine
            main.create();
            
            //start the thread
            main.start();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
    
    public static void main(String[] args)
    {
        try
        {
            //create a new jframe that will contain our application
            JFrame window = new JFrame(Shared.GAME_NAME);

            //use cursor from Shared class
            window.setCursor(Shared.CURSOR);
            
            //add component to window
            window.add(new StartDesktop());
            
            //do not allow user to resize window
            window.setResizable(false);
            
            //resize window based on dimensions set by JPanel StartDesktop
            window.pack();

            //set this null to place the panel in the center of the screen
            window.setLocationRelativeTo(null);
            
            //set visible to true so we can see panel
            window.setVisible(true);
            
            //dispose on close to free up resources
            window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
}