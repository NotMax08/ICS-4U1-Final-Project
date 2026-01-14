import greenfoot.*;
import java.util.*;
/**
 * @author Julian
 * @version 2026
 */
public abstract class NPC extends Actor
{    
<<<<<<< Updated upstream
<<<<<<< Updated upstream
<<<<<<< Updated upstream
<<<<<<< Updated upstream
    private int range = 100; 
    private boolean promptVisible = false; 
    private TextBox prompt;
    public int fontSize = 15;

=======
=======
>>>>>>> Stashed changes
=======
>>>>>>> Stashed changes
=======
>>>>>>> Stashed changes
    private ArrayList<Player> playerInRange; // Stores player(s) that are within the interaction hitbox 
    private int range = 100; // range at which the npc will have an option to interact
    private boolean promptVisible = false; // boolean to show if player is within range to see the interact prompt
    private TextBox prompt;
    public int fontSize = 15;
    
>>>>>>> Stashed changes
    public void act()
    {
        interactWithPlayer();
    }

    /**
<<<<<<< Updated upstream
<<<<<<< Updated upstream
<<<<<<< Updated upstream
<<<<<<< Updated upstream
     * method to check if player is near NPC to allow player to interact with it
     * @return true if a Player is within range; otherwise returns false.
=======
>>>>>>> Stashed changes
=======
>>>>>>> Stashed changes
=======
>>>>>>> Stashed changes
=======
>>>>>>> Stashed changes
     * Method to show prompt and handle dialogue.
     * @author Julian
     */
    private void interactWithPlayer()
    {
<<<<<<< Updated upstream
        ArrayList<Player> playerInRange = (ArrayList<Player>)getObjectsInRange(range, Player.class);
=======
        playerInRange = (ArrayList<Player>)getObjectsInRange(range, Player.class);
<<<<<<< Updated upstream
<<<<<<< Updated upstream
<<<<<<< Updated upstream
>>>>>>> Stashed changes
=======
>>>>>>> Stashed changes
=======
>>>>>>> Stashed changes
=======
>>>>>>> Stashed changes
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
<<<<<<< Updated upstream
    //What the individual npcs do
    public abstract void startDialogue();

=======
    
    //What the individual npcs do
    public abstract void startDialogue();
    
<<<<<<< Updated upstream
<<<<<<< Updated upstream
<<<<<<< Updated upstream
>>>>>>> Stashed changes
=======
>>>>>>> Stashed changes
=======
>>>>>>> Stashed changes
=======
>>>>>>> Stashed changes
    public void textBoxWriter(TextBox dialogue)
    {
        getWorld().addObject(dialogue, getX(), getY() - 65);
    }
<<<<<<< Updated upstream
<<<<<<< Updated upstream
<<<<<<< Updated upstream
<<<<<<< Updated upstream

=======
>>>>>>> Stashed changes
=======
>>>>>>> Stashed changes
=======
>>>>>>> Stashed changes
}
=======
}
>>>>>>> Stashed changes
