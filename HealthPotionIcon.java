import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class HealthPotionIcon here.
 * 
 * @author Julian
 * @version 2025
 */
public class HealthPotionIcon extends ShopIcons
{
    private String description = "Restores 25 HP.|Purchase for 25 gems?"; 
    private HealthPotion healthPotion;

    public HealthPotionIcon()
    {
        image = new GreenfootImage("healthptIcon.jpg");
        image.scale(image.getWidth()/18, image.getHeight()/18);
        imageSetup(image);

        healthPotion = new HealthPotion();
    }

    public void act()
    {
        super.act();

    }

    protected void description()
    {
        textWriter(description, true);
        if (healthPotion != null && healthPotion.getWorld() == null) {
            getWorld().addObject(healthPotion, getWorld().getWidth()/2 + potionX, getWorld().getHeight()/2 + potionY);
        }
    }

    protected void cleanUp()
    {
        removeText();
        if(getWorld() != null && healthPotion != null && healthPotion.getWorld() != null)
        {
            getWorld().removeObject(healthPotion);
        }
    }
}
