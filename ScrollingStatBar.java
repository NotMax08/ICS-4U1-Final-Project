import greenfoot.*;

public class ScrollingStatBar extends ScrollingActor {
    private SuperStatBar statBar;
    private Actor target;
    private int offsetY;
    
    public ScrollingStatBar(Camera camera, Actor target, int maxVal, int width, int height, int offsetY) {
        super(camera);
        this.target = target;
        this.offsetY = offsetY;
        
        // Create the stat bar without a target (we'll handle positioning)
        statBar = new SuperStatBar(maxVal, maxVal, null, width, height, 0, 
                                    Color.RED, Color.DARK_GRAY, false, Color.BLACK, 1);
        
        // Don't try to get the image yet - wait until we're in the world
    }
    
    @Override
    protected void addedToWorld(World world) {
        super.addedToWorld(world);
        // NOW we can safely get and set the image
        setImage(statBar.getImage());
    }
    
    public void act() {
        if (target != null && target.getWorld() != null && target instanceof ScrollingActor) {
            ScrollingActor scrollTarget = (ScrollingActor) target;
            setWorldPosition(scrollTarget.getWorldX(), scrollTarget.getWorldY() + offsetY);
        } else if (target == null || target.getWorld() == null) {
            if (getWorld() != null) {
                getWorld().removeObject(this);
            }
        }
    }
    
    public void update(int newVal) {
        statBar.update(newVal);
        if (getWorld() != null) {
            setImage(statBar.getImage());
        }
    }
}