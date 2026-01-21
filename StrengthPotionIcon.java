import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class StrengthPotionIcon here.
 * 
 * @author Julian 
 * @version 2026
 */
public class StrengthPotionIcon extends ShopIcons
{
    private StrengthPotion strengthPotion;
    public StrengthPotionIcon()
    {
        image = new GreenfootImage("strengthptIcon.jpg");
        image.scale(image.getWidth()/18, image.getHeight()/18);
        imageSetup(image);
        
        strengthPotion = new StrengthPotion();
        
        this.price = 25;
        this.itemIndex = 3;
        this.description = "Increases damage to enemies|Purchase for 25?";
    }
    
    public void act()
    {
        super.act();
    }
    
    protected void description()
    {
        textWriter(description, true);
        if (strengthPotion != null && strengthPotion.getWorld() == null) {
            getWorld().addObject(strengthPotion, getWorld().getWidth()/2 + potionX, getWorld().getHeight()/2 + potionY);
        }
    }
    
    protected void cleanUp()
    {
        removeText();
        if(getWorld() != null && strengthPotion != null && strengthPotion.getWorld() != null)
        {
            getWorld().removeObject(strengthPotion);
        }
    }
    
    public void removeIcon()
    {
        cleanUp();
        getWorld().removeObject(this);
    }
    
    protected void purchase()
    {
        Player p = (Player) getWorld().getObjects(Player.class).get(0);
        p.updateItemCount(3,1);
    }
}
