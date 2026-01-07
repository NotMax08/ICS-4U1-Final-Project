import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.*;

/**
 * Write a description of class NPC here.
 * 
 * @author Julian
 * @version 2026
 */
public class NPC extends Actor
{    
    private String dialogue;
    private ArrayList<Player> playerInRange; // Stores player(s) that are within the interaction hitbox 
    private int range = 20; // range at which the npc will have an option to interact
    
    public NPC ()
    {
        
    }
    
    public void act()
    {
        if(interactWithPlayer() && interactOption())
        {
            System.out.println("e clicked");
        }
    }
    
    /**
     * method to check if player is near NPC to allow player to interact with it
     * @return true if a Player is within range; otherwise returns false.
     * @author Julian
     */
    private boolean interactWithPlayer()
    {
        playerInRange = (ArrayList<Player>)getObjectsInRange(range, Player.class);
        if(playerInRange == null)
        {
            return false;
        }
        return true;
    }
    
    /**
     * Method that returns whether the player wants to interact with the NPC
     * @return true if player wants to interact with npc; false otherwise
     * @author Julian
     */
    private boolean interactOption()
    {
        return Greenfoot.isKeyDown("e");
    }
    
    
}
