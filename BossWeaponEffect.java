import greenfoot.*;
/**
 * @author Angelina
 */
public class BossWeaponEffect extends Actor
{
    private int counter;
    private int type;
    private boolean lethal;
    public BossWeaponEffect(int type){
        this.type = type;
        counter = 0;
        if (type==1){
            setI("Down1");
            lethal = true;
        }
        else if (type==2){
            setI("Down2");
            lethal = true;
        }
        else if (type==3){
            setI("Motion1");
            this.getImage().setTransparency(20);
            lethal = false;
        }
        else if (type==4){
            setI("Motion2");
            this.getImage().setTransparency(20);
            lethal = false;
        }
    }
    
    public void act(){
        if (type==1 || type==2){
            checkCollision();
        }
        
        if (type==1 && counter == 7){
            getWorld().removeObject(this);
        }
        else if (type==2 && counter == 40){
            getWorld().removeObject(this);
        }
        else if (type==3 || type==4){
            if (counter==60){
                this.getImage().setTransparency(255);
            }
            else if (counter==80){
                getWorld().removeObject(this);
            }
        }
        counter++;
    }
    
    private void setI(String imageName){
        GreenfootImage image = new GreenfootImage("StaBee/Attack/" + imageName + ".PNG");
        this.setImage(image);
    }
    
    private boolean checkCollision(){
        if (lethal){
            Player p;
            p = (Player)getOneIntersectingObject(Player.class);
            if (p == null){
                return false;
            }
            else{
                System.out.println("Hit 2");
                lethal = false;
                return true;
            }
        }
        else{
            return false;
        }
    }
}
