import greenfoot.*;
/**
 * @author Angelina
 */
public class Boss extends Actor
{   
    private boolean entering = false;
    
    private boolean teleporting = false;
    private int teleportPhase;
    
    private int teleportX;
    private int teleportY;    
    private String teleportImage;
    
    private int currentX;
    private int currentY;
    private String currentImage;
    
    private double scale = 4;
    
    private boolean isNew;
    
    GreenfootImage staBeeFront;
    GreenfootImage staBeeLeftUp;
    
    private double teleportSpeed = 60;
    private double teleportFrameCounter = 0;
    
    public Boss(int variant)
    {
        staBeeFront = new GreenfootImage("StaBee/Character/Front.PNG");
        staBeeLeftUp = new GreenfootImage("StaBee/Character/LeftUp.PNG");
        
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
            this.setLocation(BossRoom.BOSS_WORLD_WIDTH/2,this.getY()+2);
            if (this.getY() == 300){
                entering = false;
                startTeleport(600, this.getY(), "LeftUp");
            }
        }
        if (teleporting){
            teleport(teleportX, teleportY);
        }
    }
    
    private void startTeleport(int x, int y, String newPose){
        teleportX = x;
        teleportY = y;
        teleportImage = newPose;
        
        teleportPhase = 1;
        teleporting = true;
    }
    
    private void teleport(int x, int y){
        teleportFrameCounter++;
        if (teleportFrameCounter==teleportSpeed){
            if(teleportPhase<=3){
                setI(currentImage);
                setPhase();
            }
            else if (teleportPhase==4){
                setI(teleportImage);
                currentImage = teleportImage;
                setPhase();
                this.setLocation(x,y);
            }
            else if (teleportPhase==5){
                setI(teleportImage);
                setPhase();
            }
            else{
                setI(teleportImage);
                teleporting = false;
            }
            
            teleportPhase++;
            teleportFrameCounter = 0;
        }
    }
    
    private void setPhase(){
        this.getImage().drawImage(new GreenfootImage("StaBee/Teleport/Phase" + teleportPhase + ".PNG"),0,0);
    }
    
    private void enterScreen(){
        entering = true;
    }
    
    private void setI(String imageName){
        GreenfootImage image = new GreenfootImage("StaBee/Character/" + imageName + ".PNG");
        image.scale((int)(image.getWidth()/scale),(int)(image.getHeight()/scale));
        this.setImage(image);
    }
}
