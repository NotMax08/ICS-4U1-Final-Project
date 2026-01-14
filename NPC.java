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
     * method to check if player is near NPC to allow player to interact with it
     * @return true if a Player is within range; otherwise returns false.
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
            if (Greenfoot.isKeyDown("e") && prompt != null) {
                getWorld().removeObject(prompt);
                startDialogue();
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
    public abstract void startDialogue();

    public void textBoxWriter(TextBox dialogue)
    {
        getWorld().addObject(dialogue, getX(), getY() - 65);
    }

}