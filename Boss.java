import greenfoot.*;
/**
 * @author Angelina
 */
public class Boss extends Actor
{   
    // Current Action Variables
    private boolean entering = false;
    private boolean teleporting = false;
    private boolean attackOne = false;
    private boolean attackTwo = false;
    private boolean attackThree = false;
    private boolean waiting = false;
    
    // Current State Variables
    private int currentX;
    private int currentY;
    private String currentImage;
    
    // Variables relating to teleporting
    private boolean teleported = false;
    private int teleportX;
    private int teleportY;    
    private String teleportImage;
    
    // Final variables (Do not change values)
    public static final double scale = 4;
    public static final int weaponYOffset = 115;
    
    private boolean isNew;
    
    private boolean attackDirectionLeft;
    
    // Modifyable variables
    public static double teleportFrames = 25; // Number of frames it takes to teleport
    private int enterSpeedMultiplier = 4; // How fast the boss enters the screen
    
    private int attackCounter;
    
    private int attackNumber;
    
    private static final int totalHealth = 60;
    private int currentHealth;
    
    private SuperStatBar bossHealthBar;
    
    private Color green = new Color(200,255,200);
    private Color black = new Color(0,0,0);
    
    private int waitFrames;
    
    public static boolean secondPhase = false;
    
    private int variant;
    
    private BossWeapon weapon;
    
    private static int bossesDefeated;
    
    /**
     * 1 & 2 (attackOne), 
     * 3 (attackTwo), 
     * 4 & 5 (attackThree w/ teleport), 
     * 6 & 7 (attackThree no teleport),
     * 8 (wait for 15 frames),
     * 9 (wait for 45 frames)
     */
    private int[] attackPattern;
    
    public Boss(int variant)
    {
        this.setI("Front");
        currentImage = "Front";
        
        isNew = true;
        
        attackNumber = 0;
        
        currentHealth = totalHealth;
        
        this.variant = variant;
        
        if (variant==1){
            attackPattern = new int[]{1,2,3,3,8,4,5,4,5,9};
            bossesDefeated = 0;
        }
        else if (variant==2){
            attackPattern = new int[]{1,2,3,9,3,9,4,5,9,9,9,9,9};
            bossesDefeated = 1;
        }
        else{
            attackPattern = new int[]{9,1,2,3,9,9,9,3,9,9,5,4,9};
            bossesDefeated = 1;
        }
        enterScreen();
    }
    
    protected void addedToWorld(World world){
        currentX = this.getX();
        currentY = this.getY();
        isNew=false;
        weapon = new BossWeapon();
        world.addObject(weapon, -200, -200);
        getWorld().addObject(weapon, -200, -200);
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
    
    private void attackingThree(){
        if (attackCounter==66){
            attackThree = false;
        }
        attackCounter++;
    }
    
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
    
    private void attack2(){
        attackCounter = 0;
        attackTwo = true;
        
        // Initial teleport
        int playerLocation = BossRoom.player.getX();
        teleport(playerLocation,200,"DownUp");
        weapon.teleport(playerLocation,200 - weaponYOffset - 40,true,90);
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
    
    private void setI(String imageName){
        GreenfootImage image = new GreenfootImage("StaBee/Character/" + imageName + ".PNG");
        image.scale((int)(image.getWidth()/scale),(int)(image.getHeight()/scale));
        this.setImage(image);
    }
    
    public void setAttackOneState(boolean state){
        attackOne = state;
    }
    
    private boolean isAttacking(){
        if (attackOne==false && attackTwo==false && attackThree==false && waiting==false){
            return false;
        }
        else{
            return true;
        }
    }
    
    private void waitFrames(int frames){
        waiting = true;
        attackCounter = 0;
        waitFrames = frames;
    }
    
    private void waiting(){
        attackCounter++;
        if (attackCounter==waitFrames){
            waiting = false;
        }
    }
    
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
    
    public int getWorldX(){
        return this.getX();
    }
    
    public int getWorldY(){
        return this.getY();
    }
}
