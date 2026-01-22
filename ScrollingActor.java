import greenfoot.*;
import java.util.*;

/**
 * @author Claude and paul
 */
public abstract class ScrollingActor extends SuperSmoothMover {
    protected int worldX, worldY;
    protected Camera camera;
    /**
     * contructor for superclass for all actors in scrolling worlds
     * 
     * @param Camera camera for reference
     */
    public ScrollingActor(Camera camera) {
        this.camera = camera;
        this.worldX = 0;
        this.worldY = 0;
    }
    /**
     * sets position in world coordinates for larger maps not screen coordinates
     * 
     * @param x world x to set
     * @param y world y to set
     */
    public void setWorldPosition(int x, int y) {
        this.worldX = x;
        this.worldY = y;
        updateScreenPosition();
    }
    /**
     * updates screen position for camera to show items as the camera moves
     */
    public void updateScreenPosition() {
        if (camera != null) {
            int screenX = camera.worldToScreenX(worldX);
            int screenY = camera.worldToScreenY(worldY);
            setLocation(screenX, screenY);
        }
    }
    /**
     * getters
     */
    public int getWorldX() { return worldX; }
    public int getWorldY() { return worldY; }
    public Camera getCamera() { return camera; }
    /**
     * sets camera for referencing
     */
    public void setCamera(Camera newCamera) {
        this.camera = newCamera;
    }
    /**
     * getter for world coordinate x
     */
    @Override
    public int getX(){
        return worldX;
    }
    /**
     * getter for world coordinate y
     */
    @Override
    public int getY(){
        return worldY;
    }
    
    /**
     * Get one object at a world coordinate offset
     */
    protected <T> T getOneObjectAtWorldOffset(int dx, int dy, Class<T> cls) {
        int checkX = worldX + dx;
        int checkY = worldY + dy;
        
        List<T> objects = getWorld().getObjects(cls);
        for (T obj : objects) {
            if (obj instanceof ScrollingActor) {
                ScrollingActor scrollObj = (ScrollingActor) obj;
                
                // Calculate world bounds of the object
                int objLeft = scrollObj.getWorldX() - scrollObj.getImage().getWidth() / 2;
                int objRight = scrollObj.getWorldX() + scrollObj.getImage().getWidth() / 2;
                int objTop = scrollObj.getWorldY() - scrollObj.getImage().getHeight() / 2;
                int objBottom = scrollObj.getWorldY() + scrollObj.getImage().getHeight() / 2;
                
                // Check if point is inside object bounds
                if (checkX >= objLeft && checkX <= objRight &&
                    checkY >= objTop && checkY <= objBottom) {
                    return obj;
                }
            }
        }
        return null;
    }
    
    /**
     * Get all objects at a world coordinate offset
     */
    protected <T> List<T> getObjectsAtWorldOffset(int dx, int dy, Class<T> cls) {
        List<T> result = new ArrayList<>();
        int checkX = worldX + dx;
        int checkY = worldY + dy;
        
        List<T> objects = getWorld().getObjects(cls);
        for (T obj : objects) {
            if (obj instanceof ScrollingActor) {
                ScrollingActor scrollObj = (ScrollingActor) obj;
                
                int objLeft = scrollObj.getWorldX() - scrollObj.getImage().getWidth() / 2;
                int objRight = scrollObj.getWorldX() + scrollObj.getImage().getWidth() / 2;
                int objTop = scrollObj.getWorldY() - scrollObj.getImage().getHeight() / 2;
                int objBottom = scrollObj.getWorldY() + scrollObj.getImage().getHeight() / 2;
                
                if (checkX >= objLeft && checkX <= objRight &&
                    checkY >= objTop && checkY <= objBottom) {
                    result.add(obj);
                }
            }
        }
        return result;
    }
    
    // Add this method to Enemies class
    protected <T extends Actor> List<T> getObjectsInWorldRange(int worldRange, Class<T> cls) {
        List<T> allObjects = getWorld().getObjects(cls);
        List<T> objectsInRange = new ArrayList<>();
        
        for (T obj : allObjects) {
            if (obj instanceof ScrollingActor) {
                ScrollingActor scrollObj = (ScrollingActor) obj;
                int dx = scrollObj.getWorldX() - worldX;
                int dy = scrollObj.getWorldY() - worldY;
                double distance = Math.sqrt(dx * dx + dy * dy);
                
                if (distance <= worldRange) {
                    objectsInRange.add(obj);
                }
            }
            // For non-scrolling actors, use screen coordinates (fallback)
            else {
                int dx = obj.getX() - getX();
                int dy = obj.getY() - getY();
                double distance = Math.sqrt(dx * dx + dy * dy);
                
                if (distance <= worldRange) {
                    objectsInRange.add(obj);
                }
            }
        }
        
        // Sort by distance (closest first)
        objectsInRange.sort((a, b) -> {
            double distA = calculateDistanceTo(a);
            double distB = calculateDistanceTo(b);
            return Double.compare(distA, distB);
        });
        
        return objectsInRange;
    }
    
    private double calculateDistanceTo(Actor actor) {
        if (actor instanceof ScrollingActor) {
            ScrollingActor scrollActor = (ScrollingActor) actor;
            int dx = scrollActor.getWorldX() - worldX;
            int dy = scrollActor.getWorldY() - worldY;
            return Math.sqrt(dx * dx + dy * dy);
        } else {
            int dx = actor.getX() - getX();
            int dy = actor.getY() - getY();
            return Math.sqrt(dx * dx + dy * dy);
        }
    }
    
    /**
     * Get intersecting objects (checking world coordinate bounding boxes)
     */
    protected <T> List<T> getIntersectingWorldObjects(Class<T> cls) {
        List<T> result = new ArrayList<>();
        List<T> objects = getWorld().getObjects(cls);
        
        // This actor's world bounds
        int myLeft = worldX - getImage().getWidth() / 2;
        int myRight = worldX + getImage().getWidth() / 2;
        int myTop = worldY - getImage().getHeight() / 2;
        int myBottom = worldY + getImage().getHeight() / 2;
        
        for (T obj : objects) {
            if (obj == this) continue;
            
            if (obj instanceof ScrollingActor) {
                ScrollingActor scrollObj = (ScrollingActor) obj;
                
                int objLeft = scrollObj.getWorldX() - scrollObj.getImage().getWidth() / 2;
                int objRight = scrollObj.getWorldX() + scrollObj.getImage().getWidth() / 2;
                int objTop = scrollObj.getWorldY() - scrollObj.getImage().getHeight() / 2;
                int objBottom = scrollObj.getWorldY() + scrollObj.getImage().getHeight() / 2;
                
                // Check for AABB collision
                if (myLeft < objRight && myRight > objLeft &&
                    myTop < objBottom && myBottom > objTop) {
                    result.add(obj);
                }
            }
        }
        return result;
    }
    
    /**
     * Get one intersecting object
     */
    protected <T> T getOneIntersectingWorldObject(Class<T> cls) {
        List<T> intersecting = getIntersectingWorldObjects(cls);
        return intersecting.isEmpty() ? null : intersecting.get(0);
    }
    
    /**
     * Check if touching another actor in world coordinates
     */
    protected boolean isTouchingWorldObject(Class<?> cls) {
        return getOneIntersectingWorldObject(cls) != null;
    }
    
    /**
     * Get world distance to another ScrollingActor
     */
    protected double getWorldDistanceTo(ScrollingActor other) {
        int dx = other.getWorldX() - worldX;
        int dy = other.getWorldY() - worldY;
        return Math.sqrt(dx * dx + dy * dy);
    }
     /**
     * Check if another scrolling actor is within a specific rectangular area in front
     * @param rangeX Horizontal range in front
     * @param rangeY Vertical range (height check)
     * @param cls The class to check for
     * @return List of actors in the attack area
     */
    protected <T> List<T> getObjectsInAttackArea(int rangeX, int rangeY, Class<T> cls) {
        List<T> result = new ArrayList<>();
        List<T> objects = getWorld().getObjects(cls);
        
        // Calculate attack area bounds based on facing direction
        int attackLeft, attackRight, attackTop, attackBottom;
        
        if (isFacingRight) {
            attackLeft = worldX;
            attackRight = worldX + rangeX;
        } else {
            attackLeft = worldX - rangeX;
            attackRight = worldX;
        }
        
        attackTop = worldY - rangeY / 2;
        attackBottom = worldY + rangeY / 2;
        
        // Ensure left < right
        if (attackLeft > attackRight) {
            int temp = attackLeft;
            attackLeft = attackRight;
            attackRight = temp;
        }
        
        for (T obj : objects) {
            if (obj instanceof ScrollingActor) {
                ScrollingActor scrollObj = (ScrollingActor) obj;
                
                int objX = scrollObj.getWorldX();
                int objY = scrollObj.getWorldY();
                
                // Check if object is within the attack area
                if (objX >= attackLeft && objX <= attackRight &&
                    objY >= attackTop && objY <= attackBottom) {
                    result.add(obj);
                }
            }
        }
        return result;
    }
    
    protected boolean isFacingRight = true;
    /**
     * returns direction facing
     */
    public boolean isFacingRight() {
        return isFacingRight;
    }
    /**
     * sets direction facing
     */
    public void setFacingRight(boolean facingRight) {
        this.isFacingRight = facingRight;
    }
}