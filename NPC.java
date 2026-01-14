import greenfoot.*;
import java.util.*;

/**
 * @author Julian
 * @version 2026
 */
public abstract class NPC extends Actor
{    
    private int range = 100; 
    private boolean promptVisible = false; 
    private TextBox prompt;
    public int fontSize = 15;

    public void act()
    {
        interactWithPlayer();
    }

    /**
     * Method to show prompt and handle dialogue.
     * @author Julian
     */
    private void interactWithPlayer()
    {
        ArrayList<Player> playerInRange = (ArrayList<Player>)getObjectsInRange(range, Player.class);
        boolean playerNear = !playerInRange.isEmpty();
        
        if(playerNear)
        {
            if(!promptVisible)
            {
                prompt = new TextBox("Press 'E' to interact", fontSize);
                textBoxWriter(prompt);
                promptVisible = true;
            }
            if (("e").equals(Greenfoot.getKey()) && prompt != null) {
                getWorld().removeObject(prompt);
                dialogue();
            }
        }
        else {
            if (promptVisible && prompt != null) {
                getWorld().removeObject(prompt);
                promptVisible = false;
            }
        }
    }

    //What the individual npcs do
    public abstract void dialogue();

    public void textBoxWriter(TextBox dialogue)
    {
        getWorld().addObject(dialogue, getX(), getY() - 65);
    }
}