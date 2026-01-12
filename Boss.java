import greenfoot.*;
/**
 * @author Angelina
 */
public class Boss extends Actor
{
    private static final GreenfootImage staBeeFront = new GreenfootImage("StaBee/Character/Front.PNG");
    
    private boolean entering = false;
    
    public Boss(int variant)
    {
        this.setI(staBeeFront);
        
        if (variant == 1){
            enterScreen();
        }
    }
    
    public void act(){
        if (entering){
            this.setLocation(BossRoom.BOSS_WORLD_WIDTH/2,this.getY()+2);
            if (this.getY() == 300){
                entering = false;
            }
        }
    }
    
    private void setI(GreenfootImage image){
        double scale = 4;
        image.scale((int)(image.getWidth()/scale),(int)(image.getHeight()/scale));
        this.setImage(image);
    }
    
    private void enterScreen(){
        this.setLocation(BossRoom.BOSS_WORLD_WIDTH/2,this.getImage().getHeight());
        entering = true;
    }
}
