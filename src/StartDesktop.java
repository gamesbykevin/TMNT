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
        setPreferredSize(new Dimension(Shared.WINDOW_WIDTH, Shared.WINDOW_HEIGHT));
        setFocusable(true);
        requestFocus();
        
        try
        {
            main = new Main(Shared.DEFAULT_UPS);
            main.setPanel(this);
            main.createGameEngine();
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
            JFrame window = new JFrame(Shared.GAME_NAME);

            window.setCursor(Shared.CURSOR);
            window.add(new StartDesktop());
            window.setResizable(false);
            window.pack();

            window.setLocationRelativeTo(null);
            window.setVisible(true);
            window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
}