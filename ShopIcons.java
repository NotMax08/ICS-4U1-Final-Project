import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import greenfoot.Actor;
/**
 * Class that creates the shop icons.
 * Creates outlines when mouse is hovering above icon
 * and displays potion and description of it. 
 * Also manages puchasing logic
 * 
 * @author Julian
 * @version 2026
 */
public abstract class ShopIcons extends Actor
{
    protected GreenfootImage image;
    protected GreenfootImage outlinedImage; //outlines version of the image (white)
    protected boolean isHovering = false; // Boolean value to check if player is hovering over the icons
    private int fontSize = 12; // font size for the descriptions
    
    //offsets for the description
    protected int offsetX;
    protected int offsetY = 175;
    
    //string that stores the description
    protected String description;
    
    //where to draw the potion image
    protected int potionX = - 147;
    protected int potionY = -30;
    
    protected int price; // Price of the potion
    protected int itemIndex; // which Index of potion is it
    
    //declare and intialize text manager;
    private TextManager textManager = new TextManager();
    public void act()
    {
        hoverEffect();

        if(isHovering)
        {
            description();
        }
        else
        {
            cleanUp();
        }
        
        if (Greenfoot.mouseClicked(this))
        {
            purchase();
        }
    }

    /**
     * This method must be called by the subclass constructor to set up the images
     * Sets up the original image and the image with a white outline.
     */
    protected void imageSetup(GreenfootImage image)
    {
        this.image = image;

        //copies the base image
        this.outlinedImage = new GreenfootImage(image);

        // Draws a thick white border
        outlinedImage.setColor(Color.WHITE);
        int borderThickness = 3;

        for (int i = 0; i < borderThickness; i++) {
            outlinedImage.drawRect(i, i, outlinedImage.getWidth() - 1 - (i * 2), outlinedImage.getHeight() - 1 - (i * 2));
        }

        //sets the starting image
        setImage(image);
    }

    private void hoverEffect()
    {
        if(Greenfoot.mouseMoved(this))
        {
            if(!isHovering)
            {
                setImage(outlinedImage); //set image to outlined image
                isHovering = true;  
            }
        }

        if (Greenfoot.mouseMoved(null) && !Greenfoot.mouseMoved(this)) {
            if (isHovering) {
                setImage(image);
                isHovering = false;
            }
        }
    }

    
    /**
     * Method for making writing text easier
     * Tailored for description use
     */
    protected void textWriter(String description, boolean split)
    {
        textManager.textBoxWriter(getWorld(), getWorld().getWidth()/2, getWorld().getHeight()/2, offsetX, offsetY, description, fontSize, split);
    }

    /**
     * removes all text from world
     */
    protected void removeText()
    {
        textManager.removeText(getWorld());
    }

    protected abstract void description();

    protected abstract void cleanUp();
    
    /**
     * method that manages item purchasing logic
     */
    protected void purchase()
    {
        Player p = (Player) getWorld().getObjects(Player.class).get(0);
        if (p.getCurrency() >= price)
        {
            p.updateItemCount(itemIndex,1); // adds 1 to item count in inventory
            p.addToCurrency(-price); // removes money from player profile
        }
    }
}
