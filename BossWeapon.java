import greenfoot.*;
/**
 * @author Angelina
 */
public class BossWeapon extends Actor
{
    // State variables
    private boolean teleporting = false;
    private boolean attack1 = false;
    private boolean attack2 = false;
    
    private boolean left = true;
    private boolean lethal = false;
    
    // Variables relating to teleporting
    private boolean teleported = false;
    private int teleportX;
    private int teleportY;   
    private int teleportRotation;
    private double teleportFrames = Boss.teleportFrames;
    
    private int weaponSpeed = 45;
    private int attackCounter;
    private int playerLocation;
    
    private GreenfootSound strikeSound = new GreenfootSound("strikeSound.wav");
    private GreenfootSound throwSound = new GreenfootSound("throwSound.wav");
    
    /**
     * Boss weapon constructor
     *
     * @param damage Number of damage points to take
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
        else if (attack2){
            attackFlying2();
        }
        checkCollision();
    }
    
    /**
     * To call teleport in attack sequence & set initial values
     *
     * @param x Teleport to location, x coordinate
     * @param y Teleport to location, y coordinate
     * @param left Direction to teleport to
     * @param rotation Rotation to teleport to
     */
    public void teleport(int x, int y, boolean left, int rotation){
        setI(!left);
        teleportX = x;
        teleportY = y;
        
        teleportRotation = rotation;
        
        teleporting = true;
        teleported = false;
    }
    
    /**
     * Action method for teleporting
     *
     * @param x Teleport to location, x coordinate
     * @param y Teleport to location, y coordinate
     * @param left Direction to teleport to
     */
    private void teleporting(int x, int y, boolean left){
        int newTransparency;
        // Fade in/out
        if (!teleported){
           newTransparency = this.getImage().getTransparency() - (int)(255/(teleportFrames/2));
        }
        else{
           newTransparency = this.getImage().getTransparency() + (int)(255/(teleportFrames/2));
        }
        
        if (newTransparency <= 0){
            // If invisible, teleport to new location
            newTransparency = 0;
            this.setLocation(teleportX,teleportY);
            this.setRotation(teleportRotation);
            teleported = true;
        }
        else if(newTransparency >= 255){
            // If teleport is complete
            newTransparency = 255;
            teleporting = false;
        }
        this.getImage().setTransparency(newTransparency);
    }
    
    /**
     * To call action in attack sequence & set initial values
     *
     * @param left Whether the attack begins from the left or right
     */
    public void attackFly1(boolean left){
        this.left = left;
        attack1=true;
        attackCounter = 0;
    }
    
    /**
     * Action method for attack1
     * 
     * @param left Whether the attack begins from the left or right
     */
    private void attackFlying1(boolean left){
        if (attackCounter == 0){
            lethal = true;
            throwSound.play();
        }
        
        if (attackCounter <= 13){
            // Travel horizontally
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
            // Travel diagonally downwards
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
            // Travel diagonally upwards
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
            // Finish attack
            attack1 = false; // State
            lethal = false;
            BossRoom.staBee.setAttackOneState(false);
        }
        attackCounter++;
    }
    
    /**
     * To call action in attack sequence & set initial values
     */
    public void attackFly2(){
        attack2=true; // State
        attackCounter = 0;
        playerLocation = BossRoom.player.getX();
        strikeSound.play();
    }
    
    /**
     * Action method for attack2
     */
    private void attackFlying2(){
        if (attackCounter == 45){
            lethal = true;
        }
        
        if (attackCounter >= 45 && attackCounter<=52){
            // Travel downwards
            this.setLocation(this.getX(), this.getY() + weaponSpeed + 2);
        }
        else if (attackCounter == 53){
            // Initial barrier stopping weapon's movement
            lethal=false;
            getWorld().addObject(new BossWeaponEffect(1), playerLocation, 500);
        }
        else if (attackCounter == 118){
            // Lethal magic effect
            getWorld().addObject(new BossWeaponEffect(2), playerLocation, 531);
        }
        else if (attackCounter == 138){
            // Complete attack
            attack2 = false; // State
        }
        attackCounter++;
    }
    
    /**
     * Checks if player has collided with the weapon, and does damage to player if so
     *
     * @return If player collides with weapon
     */
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
            }
            else if (attack2){
                p.takeDamage(1);
            }
            lethal = false;
            return true;
        }
    }
    
    /**
     * Sets boss weapon's image
     *
     * @param left Weapon direction
     */
    private void setI(boolean left){
        GreenfootImage image;
        if (left){
            image = new GreenfootImage("StaBee/WeaponGlowingLeft.PNG");
        }
        else{
            image = new GreenfootImage("StaBee/WeaponGlowingRight.PNG");
        }
        this.setImage(image);
    }
    
    /**
     * Removes this weapon from the world
     */
    public void removeSelf(){
        getWorld().removeObject(this);
    }
}
