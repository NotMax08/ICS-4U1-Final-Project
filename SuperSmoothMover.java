import greenfoot.Actor;
import greenfoot.World;

/**
 * A precise-coordinate Actor with sub-pixel accuracy.
 * Keeps left/right movement perfectly symmetric using
 * "round away from zero" logic for double→int conversion.
 * 
 * V2 
 */
public abstract class SuperSmoothMover extends Actor
{
    private double exactX;
    private double exactY;
    private double preciseRotation;
    private boolean staticRotation = false;
    private double cosRotation;
    private double sinRotation;

    public SuperSmoothMover() {
        staticRotation = false;
    }

    /** Ensure precise coords sync with actual position on spawn. */
    @Override
    protected void addedToWorld(World w) {
        exactX = getX();
        exactY = getY();
    }

    @Override
    public void move(int distance) {
        move((double) distance);
    }

    public void move(double distance) {
        if (cosRotation == 0 && sinRotation == 0) {
            setRotation(0);
        }
        double dx = cosRotation * distance;
        double dy = sinRotation * distance;
        setLocation(exactX + dx, exactY + dy);
    }

    /** Round halves away from zero for perfectly symmetric motion. */
    private static int roundAwayFromZero(double v) {
        return (int) (v + Math.signum(v) * 0.5);
    }

    public void setLocation(double x, double y) {
        exactX = x;
        exactY = y;
        super.setLocation(roundAwayFromZero(x), roundAwayFromZero(y));
    }

    @Override
    public void setLocation(int x, int y) {
        exactX = x;
        exactY = y;
        super.setLocation(x, y);
    }

    // --- rotation management ---
    public void setRotation(double angle) {
        preciseRotation = angle;
        if (!staticRotation)
            super.setRotation((int) (angle + 0.5));
        cosRotation = Math.cos(Math.toRadians(angle));
        sinRotation = Math.sin(Math.toRadians(angle));
    }

    @Override
    public void setRotation(int angle) { setRotation((double) angle); }

    public void turn(double angle) {
        preciseRotation += angle;
        if (!staticRotation)
            super.setRotation((int) (preciseRotation + 0.5));
        cosRotation = Math.cos(Math.toRadians(preciseRotation));
        sinRotation = Math.sin(Math.toRadians(preciseRotation));
    }

    @Override
    public void turn(int angle) { turn((double) angle); }

    @Override
    public void turnTowards(int x, int y) {
        setRotation(Math.toDegrees(Math.atan2(y - getY(), x - getX())));
    }

     /**
     * Moves this Actor 'n' pixels toward a target location (tx, ty).
     * If the distance is less than n, it moves directly to the target.
     */
    public void moveTowards(int tx, int ty, double n) {
        int x = getX();
        int y = getY();

        double dx = tx - x;
        double dy = ty - y;
        double distance = Math.hypot(dx, dy);

        // Avoid divide-by-zero and excessive movement
        if (distance < 1.0) return;

        // Scale movement vector to length n or less if close
        double step = Math.min(n, distance);
        double nx = x + (dx / distance) * step;
        double ny = y + (dy / distance) * step;

        setLocation((int)Math.round(nx), (int)Math.round(ny));
    }

    
    public void turnTowards(Actor a) { turnTowards(a.getX(), a.getY()); }

    public double getPreciseX() { return exactX; }
    public double getPreciseY() { return exactY; }
    public double getPreciseRotation() { return preciseRotation; }

    @Override
    public int getRotation() {
        return staticRotation ? (int) (preciseRotation + 0.5) : super.getRotation();
    }

    public void enableStaticRotation() {
        super.setRotation(0);
        staticRotation = true;
        preciseRotation = 0.0;
    }

    public void disableStaticRotation() {
        preciseRotation = (double) ((int) (preciseRotation + 0.5));
        super.setRotation((int) preciseRotation);
        staticRotation = false;
    }
}
