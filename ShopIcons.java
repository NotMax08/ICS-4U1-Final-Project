import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import greenfoot.Actor;
/**
 * Class that creates the shop icons.
 * Creates outlines when mouse is hovering above icon
 * and displays potion and description of it. 
 * 
 * @author Julian
 * @version 2025
 */
public abstract class ShopIcons extends Actor
{
    protected GreenfootImage image;
    protected GreenfootImage outlinedImage;
    protected String potionType; 
    protected boolean isHovering = false; 
    private int fontSize = 12;
    protected int offsetX;
    protected int offsetY = 175;
    protected String description;

    protected int potionX = - 147;
    protected int potionY = -30;

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
            cleanUp(getWorld());
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
                setImage(outlinedImage);
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

    protected void textWriter(String description, boolean split)
    {
        textManager.textBoxWriter(getWorld(), getWorld().getWidth()/2, getWorld().getHeight()/2, offsetX, offsetY, description, fontSize, split);
    }

    protected void removeText()
    {
        textManager.removeText(getWorld());
    }

    protected abstract void description();

    protected abstract void cleanUp(World world);
}
