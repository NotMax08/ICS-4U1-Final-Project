import greenfoot.*;
import java.util.List;
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
        if (type==2){
            checkCollision2();
        }
        else if (type==3 || type==4){
            checkCollision3();
        }
        
        if (type==1 && counter == 7){
            getWorld().removeObject(this);
        }
        else if (type==2 && counter == 20){
            getWorld().removeObject(this);
        }
        else if (type==3 || type==4){
            if (counter==60){
                this.getImage().setTransparency(255);
                lethal = true;
            }
            else if (counter==65){
                lethal = false;
                getWorld().removeObject(this);
            }
        }
        counter++;
    }
    
    private void setI(String imageName){
        GreenfootImage image = new GreenfootImage("StaBee/Attack/" + imageName + ".PNG");
        this.setImage(image);
    }
    
    private boolean checkCollision2(){
        if (lethal){
            Player p;
            p = (Player)getOneIntersectingObject(Player.class);
            if (p == null){
                return false;
            }
            else{
                p.takeDamage(1);
                lethal = false;
                return true;
            }
        }
        else{
            return false;
        }
    }
    
    public boolean checkCollision3(){
        if (lethal==true){
            int[] points = {395, -265,
                            -395, -223,
                            395, -153,
                            -395, -37,
                            395, 27,
                            -395, 129,
                            395, 221,
                            0, 266};
                            
            if (type == 4) {
                for (int i = 0; i < points.length; i += 2) {
                    points[i] = -points[i];
                }
            }
                            
            for (int i=0; i<points.length-2; i+=2)
            {
                int startX = points[i];
                int startY = points[i + 1];
                int endX = points[i + 2];
                int endY = points[i + 3];
                int intervalX;
                
                // Check main point
                if (!getObjectsAtOffset(startX, startY, Player.class).isEmpty()){
                    getObjectsAtOffset(startX, startY, Player.class).get(0).takeDamage(1);
                    lethal = false;
                    return true;
                }
    
                if (endX > startX)
                    intervalX = 45;
                else{
                    intervalX = -45;
                }
    
                // Check points in intervals of 45x between the main points
                for (int interX=startX+intervalX ; (intervalX>0 && interX<endX) || (intervalX<0 && interX>endX) ; interX+=intervalX)
                {
                    double lineProgress = (double)(interX-startX) / (double)(endX-startX);
                    int interY = (int)(startY + (lineProgress*(endY-startY)));
    
                    if (!getObjectsAtOffset(interX, interY, Player.class).isEmpty()){
                        getObjectsAtOffset(interX, interY, Player.class).get(0).takeDamage(1);
                        lethal = false;
                        return true;
                    }
                }
            }
    
            // Finally check last point
            if(!getObjectsAtOffset(points[14], points[15], Player.class).isEmpty()){
                getObjectsAtOffset(points[14], points[15], Player.class).get(0).takeDamage(1);
                lethal = false;
                return true;
            }
            else{
                return false;
            }
        }
        else{
            return false;
        }
    }
}
