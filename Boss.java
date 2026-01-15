import greenfoot.*;
/**
 * @author Angelina
 */
public class Boss extends Actor
{   
    private boolean entering = false;
    private boolean teleporting = false;
    private boolean attackOne = false;
    private boolean attackTwo = false;
    
    private int currentX;
    private int currentY;
    private String currentImage;
    
    private boolean teleported = false;
    private int teleportX;
    private int teleportY;    
    private String teleportImage;
    
    public static final double scale = 4;
    public static final int weaponYOffset = 115;
    private boolean isNew;
    private boolean attackDirectionLeft;
    
    public static double teleportFrames = 25;
    private int enterSpeedMultiplier = 8;
    
    private int attackCounter;
    
    public Boss(int variant)
    {
        this.setI("Front");
        currentImage = "Front";
        
        isNew = true;
        
        if (variant == 1){
            enterScreen();
        }
    }
    
    private void addedToWorld(){
        currentX = this.getX();
        currentY = this.getY();
        isNew=false;
    }
    
    public void act(){
        if (entering){
            entering();
        }
        if (teleporting){
            teleporting(teleportX, teleportY,teleportImage);
        }
        if (attackOne){
            attackingOne(attackDirectionLeft);
        }
        else if (attackTwo){
            attackingTwo();
        }
    }
    
    private void attackingOne(boolean left){
        if(attackCounter==30){
            if(attackDirectionLeft){
                setI("RightThrow");
            }
            else{
                setI("LeftThrow");
            }
            BossRoom.weapon1.attackFly1(attackDirectionLeft);
        }
        attackCounter++;
    }
    
    private void attackingTwo(){
        
    }
    
    private void attack1(boolean left){
        attackDirectionLeft = left;
        attackCounter = 0;
        attackOne = true;
        
        // Initial teleport
        if(attackDirectionLeft){
            teleport(60,465,"RightUp");
            BossRoom.weapon1.teleport(60 + 25,465 - weaponYOffset,attackDirectionLeft);
        }
        else{
            teleport(740,465,"LeftUp");
            BossRoom.weapon1.teleport(740 - 25,465 - weaponYOffset,attackDirectionLeft);
        }
    }
    
    private void attack2(){
        attackCounter = 0;
        attackTwo = true;
        
        // Initial teleport
        teleport(BossRoom.player.getX(),200,"DownUp");
    }
   
    private void teleport(int x, int y, String newPose){
        teleportX = x;
        teleportY = y;
        teleportImage = newPose;
        
        teleporting = true;
        teleported = false;
    }
    
    private void teleporting(int x, int y, String newPose){
        int newTransparency;
        if (!teleported){
           newTransparency = this.getImage().getTransparency() - (int)(255/(teleportFrames/2));
        }
        else{
           newTransparency = this.getImage().getTransparency() + (int)(255/(teleportFrames/2));
        }
        if (newTransparency <= 0){
            newTransparency = 0;
            this.setI(newPose);
            this.setLocation(teleportX,teleportY);
            teleported = true;
        }
        else if(newTransparency >= 255){
            newTransparency = 255;
            teleporting = false;
        }
        this.getImage().setTransparency(newTransparency);
    }
    
    private void enterScreen(){
        entering = true;
    }
    
    private void entering(){
        this.setLocation(BossRoom.BOSS_WORLD_WIDTH/2,this.getY()+enterSpeedMultiplier);
        if (this.getY() >= 300){
            entering = false;
            attack1(true);    //////////////////////////////
        }
    }
    
    private void setI(String imageName){
        GreenfootImage image = new GreenfootImage("StaBee/Character/" + imageName + ".PNG");
        image.scale((int)(image.getWidth()/scale),(int)(image.getHeight()/scale));
        this.setImage(image);
    }
    
    public void setAttackOneState(boolean state){
        attackOne = state;
    }
}
