import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class CurrencyDisplay here.
 * 
 * @author Julian
 * @version 2026
 */
public class CurrencyDisplay extends Display
{
    private TextManager textManager;

    public CurrencyDisplay(int screenX, int screenY, Camera camera, Player player)
    {
        super(screenX, screenY, camera, player);
        this.textManager = new TextManager();
        
        updateDisplay();
    }
    
    protected void updateDisplay()
    {
        int currency = player.getCurrency();
        
        //makes sure world is working
        if (getWorld() == null) return;

        int amount = player.getCurrency();

        String content = "$ " + amount;
        
        textManager.textBoxWriter(
            getWorld(), 
            getX(), 
            getY(),
            700,
            90,
            content,
            30,
            false
        );
    }
}
