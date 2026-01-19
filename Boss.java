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
    private boolean attackThree = false;
    
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
    
    private int attackNumber;
    
    private static final int totalHealth = 80;
    private int currentHealth;
    
    private SuperStatBar bossHealthBar;
    
    private Color green = new Color(200,255,200);
    private Color black = new Color(0,0,0);
    
    /**
     * 1 & 2 (attackOne), 
     * 3 (attackTwo), 
     * 4 & 5 (attackThree w/ teleport), 
     * 6 & 7 (attackThree no teleport)
     */
    private int[] attackPattern = {1,2,3,3,4,5,4,5};
    
    public Boss(int variant)
    {
        this.setI("Front");
        currentImage = "Front";
        
        isNew = true;
        
        attackNumber = 0;
        
        currentHealth = totalHealth;
        
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
    }
    
    private void attack3(boolean teleport,boolean invert){
        attackCounter = 0;
        attackThree = true;
        int type;
        
        // Initial teleport
        if (teleport){
            teleport(400,300,"FrontUp");
            BossRoom.weapon1.teleport(490,280,true,270);
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
            BossRoom.weapon1.attackFly1(attackDirectionLeft);
        }
        if(attackCounter==62){
            attackOne = false;
        }
        attackCounter++;
    }
    
    private void attackingTwo(){
        if (attackCounter==0){
            BossRoom.weapon1.attackFly2();
        }
        if(attackCounter==45){
            setI("DownDown");
        }
        if(attackCounter==134){
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
            BossRoom.weapon1.teleport(60 + 25,465 - weaponYOffset,attackDirectionLeft,0);
        }
        else{
            teleport(740,465,"LeftUp");
            BossRoom.weapon1.teleport(740 - 25,465 - weaponYOffset,attackDirectionLeft,0);
        }
    }
    
    private void attack2(){
        attackCounter = 0;
        attackTwo = true;
        
        // Initial teleport
        int playerLocation = BossRoom.player.getX();
        teleport(playerLocation,200,"DownUp");
        BossRoom.weapon1.teleport(playerLocation,200 - weaponYOffset - 40,true,90);
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
            bossHealthBar = new SuperStatBar(totalHealth, totalHealth, null, 400, 30, 0, green, black, false, black, 3);
            getWorld().addObject(bossHealthBar,400,50);
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
        if (attackOne==false && attackTwo==false && attackThree==false){
            return false;
        }
        else{
            return true;
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
    }
    
    public void takeDamage(int damage){
        currentHealth -= damage;
        bossHealthBar.update(currentHealth);
        
        if (currentHealth <= 0){
            getWorld().removeObject(this);
            BossRoom.weapon1.removeSelf();
        }
    }
}
