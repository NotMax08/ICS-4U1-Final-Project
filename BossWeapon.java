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
    private int teleportRotation;
    
    private boolean left = true;
    
    private double teleportFrames = Boss.teleportFrames;
    
    private boolean attack1 = false;
    private boolean attack2 = false;
    
    private int weaponSpeed = 45;
    private int attackCounter;
    
    private int playerLocation;
    
    private boolean lethal = false;
    
    private boolean facingLeft;
    
    
    //Constructor for objects of class BossWeapon
     
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
        else if (attack2){
            attackFlying2();
        }
        checkCollision();
    }
    
    public void teleport(int x, int y, boolean left, int rotation){
        setI(!left);
        teleportX = x;
        teleportY = y;
        
        teleportRotation = rotation;
        
        teleporting = true;
        teleported = false;
    }
    
    public void attackFly2(){
        attack2=true;
        attackCounter = 0;
        playerLocation = BossRoom.player.getX();
    }
    
    private void attackFlying2(){
        if (attackCounter == 45){
            lethal = true;
        }
        if (attackCounter >= 45 && attackCounter<=52){
            this.setLocation(this.getX(), this.getY() + weaponSpeed + 2);
        }
        else if (attackCounter == 53){
            lethal=false;
            getWorld().addObject(new BossWeaponEffect(1), playerLocation, 500);
        }
        else if (attackCounter == 93){
            getWorld().addObject(new BossWeaponEffect(2), playerLocation, 531);
        }
        else if (attackCounter == 133){
            attack2 = false;
        }
        attackCounter++;
    }
    
    public void attackFly1(boolean left){
        this.left = left;
        attack1=true;
        attackCounter = 0;
    }
    
    private void attackFlying1(boolean left){
        if (attackCounter == 0){
            lethal = true;
        }
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
            attack1 = false;
            lethal = false;
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
            this.setRotation(teleportRotation);
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
            facingLeft = true;
        }
        else{
            image = new GreenfootImage("StaBee/WeaponGlowingRight.PNG");
            facingLeft = false;
        }
        image.scale((int)(image.getWidth()/Boss.scale),(int)(image.getHeight()/Boss.scale));
        this.setImage(image);
    }
    
    private boolean checkCollision(){
        Player p;
        if (lethal){
            p = (Player)getOneIntersectingObject(Player.class);
            }
        else{
            p = null;
        }
        if (p == null){
            return false;
        }
        else{
            if (attack1){
                p.takeDamage(1);
                System.out.println("Hit 1");
            }
            else if (attack2){
                p.takeDamage(1);
                System.out.println("Hit 2");
            }
            lethal = false;
            return true;
        }
    }
    
}
