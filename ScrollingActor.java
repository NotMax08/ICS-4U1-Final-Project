import greenfoot.*;
import java.util.*;

/**
 * @author Claude
 */
public abstract class ScrollingActor extends SuperSmoothMover {
    protected int worldX, worldY;
    protected Camera camera;
    
    public ScrollingActor(Camera camera) {
        this.camera = camera;
        this.worldX = 0;
        this.worldY = 0;
    }
    
    public void setWorldPosition(int x, int y) {
        this.worldX = x;
        this.worldY = y;
        updateScreenPosition();
    }
    
    public void updateScreenPosition() {
        if (camera != null) {
            int screenX = camera.worldToScreenX(worldX);
            int screenY = camera.worldToScreenY(worldY);
            setLocation(screenX, screenY);
        }
    }
    
    public int getWorldX() { return worldX; }
    public int getWorldY() { return worldY; }
    public Camera getCamera() { return camera; }
    
    public void setCamera(Camera newCamera) {
        this.camera = newCamera;
    }
    
    @Override
    public int getX(){
        return worldX;
    }
    
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
}