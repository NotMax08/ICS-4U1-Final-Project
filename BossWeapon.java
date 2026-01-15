import greenfoot.*;
/**
 * @author Angelina
 */
public class BossWeapon extends Actor
{
    private boolean teleporting = false;
    private boolean teleported = false;
    
    private int teleportX;
    private int teleportY;   
    
    private boolean left = true;
    
    private double teleportFrames = Boss.teleportFrames;
    
    private boolean attack1 = false;
    
    private int weaponSpeed = 45;
    private int attackCounter;
    
    /**
     * Constructor for objects of class BossWeapon
     */
    public BossWeapon()
    {
        this.setRotation(0);
    }
    
    public void act(){
        if (teleporting){
            teleporting(teleportX, teleportY, left);
        }
        if (attack1){
            attackFlying1(left);
        }
    }
    
    public void teleport(int x, int y, boolean left){
        setI(!left);
        teleportX = x;
        teleportY = y;
        
        teleporting = true;
        teleported = false;
    }
    
    public void attackFly1(boolean left){
        this.left = left;
        attack1=true;
        attackCounter = 0;
    }
    
    private void attackFlying1(boolean left){
        if (attackCounter <= 13){
            if (left){
                this.setLocation(this.getX() + weaponSpeed, this.getY());
            }
            else{
                this.setLocation(this.getX() - weaponSpeed, this.getY());
            }
        }
        else if (attackCounter == 14){
            setI(left);
        }
        else if (attackCounter <= 21){
            if (left){
                this.setRotation(344);
                this.setLocation(this.getX() - 38, this.getY() + 19);
            }
            else{
                this.setRotation(16);
                this.setLocation(this.getX() + 38, this.getY() + 19);
            }
        }
        else if (attackCounter <= 30){
            if (left){
                this.setRotation(16);
                this.setLocation(this.getX() - 38, this.getY() - 19);
            }
            else{
                this.setRotation(344);
                this.setLocation(this.getX() + 38, this.getY() - 19);
            }
        }
        else if (attackCounter == 31){
            this.setRotation(0);
            attack1 = false;
            BossRoom.staBee.setAttackOneState(false);
        }
        attackCounter++;
    }
    
    private void teleporting(int x, int y, boolean left){
        int newTransparency;
        if (!teleported){
           newTransparency = this.getImage().getTransparency() - (int)(255/(teleportFrames/2));
        }
        else{
           newTransparency = this.getImage().getTransparency() + (int)(255/(teleportFrames/2));
        }
        if (newTransparency <= 0){
            newTransparency = 0;
            this.setLocation(teleportX,teleportY);
            teleported = true;
        }
        else if(newTransparency >= 255){
            newTransparency = 255;
            teleporting = false;
        }
        this.getImage().setTransparency(newTransparency);
    }
    
    private void setI(boolean left){
        GreenfootImage image;
        if (left){
            image = new GreenfootImage("StaBee/WeaponGlowingLeft.PNG");
        }
        else{
            image = new GreenfootImage("StaBee/WeaponGlowingRight.PNG");
        }
        image.scale((int)(image.getWidth()/Boss.scale),(int)(image.getHeight()/Boss.scale));
        this.setImage(image);
    }
}
