import greenfoot.*;
/**
 * @author Angelina
 */
public class Boss extends Actor
{   
    // Current State Variables
    private boolean entering = false;
    private boolean teleporting = false;
    private boolean attackOne = false;
    private boolean attackTwo = false;
    private boolean attackThree = false;
    private boolean waiting = false;
    
    // Variables relating to teleporting
    private boolean teleported = false;
    private int teleportX;
    private int teleportY;    
    private String teleportImage;
    
    // Attack related variables
    private boolean attackDirectionLeft;
    private int attackCounter;
    private int attackNumber;
    private int waitFrames;
    
    // Boss Health
    private int currentHealth;
    private SuperStatBar bossHealthBar;
    
    // Colours
    private Color green = new Color(200,255,200);
    private Color black = new Color(0,0,0);
    
    // Final variables (Do not change values)
    public static final double scale = 4;
    public static final int weaponYOffset = 115;
    
    // Modifyable variables
    public static double teleportFrames = 25; // Number of frames it takes to teleport
    private int enterSpeedMultiplier = 4; // How fast the boss enters the screen
    private static final int totalHealth = 60; // Boss total starting health
    
    // Other variables
    private static int bossesDefeated; // When counter reaches 3, win screen is triggered
    private int variant; // Boss variant determines attack sequence
    private int[] attackPattern; // Attack sequence
    private BossWeapon weapon; // Each boss has it's own weapon instance
    
    private boolean isNew;
    
    /**
     * Constructor for boss instances
     *
     * @param variant Determines the attack sequence for this boss instance
     */
    public Boss(int variant)
    {
        // Initial values
        this.setI("Front");
        this.variant = variant;
        attackNumber = 0;
        currentHealth = totalHealth;
        
        isNew = true;
        
        /**
         * Attack sequence based on variant
         * 1 & 2 (attackOne), 
         * 3 (attackTwo), 
         * 4 & 5 (attackThree w/ teleport), 
         * 6 & 7 (attackThree no teleport),
         * 8 (wait for 15 frames),
         * 9 (wait for 45 frames)
         */
        if (variant==1){
            attackPattern = new int[]{1,2,3,3,8,4,5,4,5,9};
            bossesDefeated = 0;
        }
        else if (variant==2){
            attackPattern = new int[]{1,2,3,9,3,9,4,7,9,9,9,9,9};
            bossesDefeated = 1;
        }
        else{
            attackPattern = new int[]{9,1,2,3,9,9,9,3,9,9,5,6,9};
            bossesDefeated = 1;
        }
        enterScreen();
    }
    
    protected void addedToWorld(World world){
        if (isNew){
            isNew=false;
            
            // Spawn weapon
            weapon = new BossWeapon();
            getWorld().addObject(weapon, -200, -200);
        }
    }
    
    public void act(){
        if (entering){
            entering();
        }
        else{
            attackSequence();
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
        else if (attackThree){
            attackingThree();
        }
        else if (waiting){
            waiting();
        }
    }
    
    // To call action in attack sequence & set initial values
    private void enterScreen(){
        entering = true;
    }
    
    /**
     * Action method for entering
     */
    private void entering(){
        this.setLocation(this.getX(),this.getY()+enterSpeedMultiplier);
        if (this.getY() >= 300){
            bossHealthBar = new SuperStatBar(totalHealth, totalHealth, null, 400, 30, 0, green, black, false, black, 3);
            if (variant == 3){
                getWorld().addObject(bossHealthBar,400,50);
            }
            else{
                getWorld().addObject(bossHealthBar,400,85);
            }
            entering = false;
        }
    }
    
    /**
     * To call action in attack sequence & set initial values
     *
     * @param x Teleport to location, x coordinate
     * @param y Teleport to location, y coordinate
     * @param newPose Pose to teleport to
     */
    private void teleport(int x, int y, String newPose){
        teleportX = x;
        teleportY = y;
        teleportImage = newPose;
        
        teleporting = true; // State
        teleported = false; // State
    }
    
    /**
     * Action method for teleporting
     *
     * @param x Teleport to location, x coordinate
     * @param y Teleport to location, y coordinate
     * @param newPose Pose to teleport to
     */
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
    
    /**
     * To call action in attack sequence & set initial values
     *
     * @param frames Number of frames to pause for
     */
    private void waitFrames(int frames){
        waiting = true;
        attackCounter = 0;
        waitFrames = frames;
    }
    
    /**
     * Action method for waiting
     */
    private void waiting(){
        attackCounter++;
        if (attackCounter==waitFrames){
            waiting = false;
        }
    }
    
    /**
     * To call action in attack sequence & set initial values
     *
     * @param left Attack1 starting from the left or right
     */
    private void attack1(boolean left){
        attackDirectionLeft = left;
        attackCounter = 0;
        attackOne = true;
        
        // Initial teleport
        if(attackDirectionLeft){
            teleport(60,465,"RightUp");
            weapon.teleport(60 + 25,465 - weaponYOffset,attackDirectionLeft,0);
        }
        else{
            teleport(740,465,"LeftUp");
            weapon.teleport(740 - 25,465 - weaponYOffset,attackDirectionLeft,0);
        }
    }
    
    /**
     * Action method for attack1
     *
     * @param left Attack1 starting from the left or right
     */
    private void attackingOne(boolean left){
        if(attackCounter==30){
            if(attackDirectionLeft){
                setI("RightThrow");
            }
            else{
                setI("LeftThrow");
            }
            weapon.attackFly1(attackDirectionLeft);
        }
        if(attackCounter==62){
            attackOne = false;
        }
        attackCounter++;
    }
    
    /**
     * To call action in attack sequence & set initial values
     */
    private void attack2(){
        attackCounter = 0;
        attackTwo = true;
        
        // Initial teleport
        int playerLocation = BossRoom.player.getX();
        teleport(playerLocation,200,"DownUp");
        weapon.teleport(playerLocation,200 - weaponYOffset - 40,true,90);
    }
    
    /**
     * Action method for attack2
     */
    private void attackingTwo(){
        if (attackCounter==0){
            weapon.attackFly2();
        }
        if(attackCounter==45){
            setI("DownDown");
        }
        if(attackCounter==139){
            attackTwo = false;
        }
        attackCounter++;
    }
    
    /**
     * To call action in attack sequence & set initial values
     *
     * @param teleport Whether the boss teleports before doing the attack
     * @param Invert Whether the attack is inverted or not
     */
    private void attack3(boolean teleport,boolean invert){
        attackCounter = 0;
        attackThree = true;
        int type;
        
        // Initial teleport
        if (teleport){
            teleport(400,300,"FrontUp");
            weapon.teleport(490,280,true,270);
        }
        if (!invert){
            type=3;
        }
        else{
            type=4;
        }
        getWorld().addObject(new BossWeaponEffect(type), 400, 290);
    }
    
    /**
     * Action method for attack3
     */
    private void attackingThree(){
        if (attackCounter==66){
            attackThree = false;
        }
        attackCounter++;
    }
    
    /**
     * Initiates next action in sequence after previous action is completed
     */
    private void attackSequence(){
        int attackType=-1;
        
        if (attackNumber == attackPattern.length){
            attackNumber=0;
        }
        
        if (!isAttacking()){
            attackType = attackPattern[attackNumber];
            attackNumber++;
        }
        
        if (attackType==1){
            attack1(false);
        }
        else if (attackType==2){
            attack1(true);
        }
        else if (attackType==3){
            attack2();
        }
        else if (attackType==4){
            attack3(true,false);
        }
        else if (attackType==5){
            attack3(true,true);
        }
        else if (attackType==6){
            attack3(false,false);
        }
        else if (attackType==7){
            attack3(false,true);
        }
        else if (attackType==8){
            waitFrames(15);
        }
        else if (attackType==9){
            waitFrames(45);
        }
    }
    
    /**
     * To make the boss take damage and updates health bar
     *
     * @param damage Number of damage points to take
     */
    public void takeDamage(int damage){
        if (!entering){
            currentHealth -= damage;
            bossHealthBar.update(currentHealth);
            if (currentHealth <= 0){
                if (variant == 1){
                    getWorld().addObject(BossRoom.staBeePhaseTwo1,200,-150);
                    getWorld().addObject(BossRoom.staBeePhaseTwo2,600,-150);
                }
                else{
                    bossesDefeated++;
                    System.out.println(bossesDefeated);
                }
                getWorld().removeObject(this);
                weapon.removeSelf();
                if (bossesDefeated == 3) {
                    Greenfoot.setWorld(new WinScreen());
                }
            }
        }
    }
    
    /**
     * Sets boss' image
     *
     * @param imageName Name of desired pose
     */
    private void setI(String imageName){
        GreenfootImage image = new GreenfootImage("StaBee/Character/" + imageName + ".PNG");
        image.scale((int)(image.getWidth()/scale),(int)(image.getHeight()/scale));
        this.setImage(image);
    }
    
    /**
     * Checks if the boss is currently in action
     *
     * @return result
     */
    private boolean isAttacking(){
        if (attackOne==false && attackTwo==false && attackThree==false && waiting==false){
            return false;
        }
        else{
            return true;
        }
    }
    
    public void setAttackOneState(boolean state){
        attackOne = state;
    }
    
    public int getWorldX(){
        return this.getX();
    }
    
    public int getWorldY(){
        return this.getY();
    }
}
