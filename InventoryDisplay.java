import greenfoot.*;
import java.util.ArrayList;

/**
 * Fixed-position inventory display that appears on screen regardless of camera position.
 * Always stays in the same screen position while the world scrolls.
 * 
 * @author Paul & Claude
 */
public class InventoryDisplay extends ScrollingActor {
    private GreenfootImage image;
    private ArrayList<InventoryItem> items;
    private int maxSlots = 6;
    private int slotSize = 50;
    private int padding = 5;
    private int screenX, screenY; // Fixed screen position
    private boolean isOpen = false;
    
    /**
     * Create inventory display at a fixed screen position
     * 
     * @param screenX X position on screen (not world position)
     * @param screenY Y position on screen (not world position)
     */
    public InventoryDisplay(int screenX, int screenY, Camera camera) {
        super(camera);
        this.screenX = screenX;
        this.screenY = screenY;
        this.items = new ArrayList<InventoryItem>();
        drawImage();
        updateImage();
    }
    
    private boolean tabPressed = false; // Track if TAB was already pressed
    
    public void drawImage(){
        image = new GreenfootImage("inventory.png");
        setImage(image);
    }
    
    /**
     * Keep inventory at fixed screen position
     */
    public void act() {
        // Always stay at the same screen coordinates
        setLocation(screenX, screenY);
        
        // Toggle inventory with TAB key (press once to toggle)
        if (Greenfoot.isKeyDown("tab")) {
            if (!tabPressed) {
                isOpen = !isOpen; // Toggle between open/closed
                updateImage();
                tabPressed = true;
            }
        } else {
            tabPressed = false; // Reset when key is released
        }
    }
    
    /**
     * Add an item to the inventory
     */
    public boolean addItem(InventoryItem item) {
        if (items.size() < maxSlots) {
            items.add(item);
            updateImage();
            return true;
        }
        return false; // Inventory full
    }
    
    /**
     * Remove an item from inventory
     */
    public boolean removeItem(InventoryItem item) {
        boolean removed = items.remove(item);
        if (removed) {
            updateImage();
        }
        return removed;
    }
    
    /**
     * Get item at specific slot
     */
    public InventoryItem getItem(int slot) {
        if (slot >= 0 && slot < items.size()) {
            return items.get(slot);
        }
        return null;
    }
    
    /**
     * Check if inventory contains an item type
     */
    public boolean hasItem(String itemName) {
        for (InventoryItem item : items) {
            if (item.getName().equals(itemName)) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Redraw the inventory display
     */
    private void updateImage() {
        if (!isOpen) {
            // Show minimized version
            GreenfootImage img = new GreenfootImage(60, 30);
            img.setColor(new Color(0, 0, 0, 180));
            img.fillRect(0, 0, 60, 30);
            img.setColor(Color.WHITE);
            img.drawRect(0, 0, 59, 29);
            img.setFont(new Font("Arial", false, false, 14));
            img.drawString("TAB", 15, 20);
            setImage(img);
            return;
        }
        
        // Calculate dimensions
        int width = (slotSize * maxSlots) + (padding * (maxSlots + 1));
        int height = slotSize + (padding * 2) + 30; // Extra space for title
        
        GreenfootImage img = new GreenfootImage(width, height);
        
        // Draw background
        img.setColor(new Color(40, 40, 40, 220));
        img.fillRect(0, 0, width, height);
        
        // Draw border
        img.setColor(Color.WHITE);
        img.drawRect(0, 0, width - 1, height - 1);
        
        // Draw title
        img.setFont(new Font("Arial", true, false, 16));
        img.drawString("Inventory", padding, 20);
        
        // Draw slots
        for (int i = 0; i < maxSlots; i++) {
            int x = padding + (i * (slotSize + padding));
            int y = 30 + padding;
            
            // Draw slot background
            img.setColor(new Color(60, 60, 60));
            img.fillRect(x, y, slotSize, slotSize);
            
            // Draw slot border
            img.setColor(Color.GRAY);
            img.drawRect(x, y, slotSize, slotSize);
            
            // Draw item if present
            if (i < items.size()) {
                InventoryItem item = items.get(i);
                GreenfootImage itemImg = item.getIcon();
                if (itemImg != null) {
                    // Scale item image to fit slot
                    GreenfootImage scaledImg = new GreenfootImage(itemImg);
                    scaledImg.scale(slotSize - 10, slotSize - 10);
                    img.drawImage(scaledImg, x + 5, y + 5);
                }
                
                // Draw item count if stackable
                if (item.getCount() > 1) {
                    img.setColor(Color.WHITE);
                    img.setFont(new Font("Arial", true, false, 12));
                    img.drawString("x" + item.getCount(), x + 5, y + slotSize - 5);
                }
            }
        }
        
        setImage(image);
    }
    
    public int getItemCount() {
        return items.size();
    }
    
    public boolean isFull() {
        return items.size() >= maxSlots;
    }
    
    public void clear() {
        items.clear();
        updateImage();
    }
}

/**
 * Represents an item in the inventory
 */
class InventoryItem {
    private String name;
    private GreenfootImage icon;
    private int count;
    
    public InventoryItem(String name, GreenfootImage icon) {
        this(name, icon, 1);
    }
    
    public InventoryItem(String name, GreenfootImage icon, int count) {
        this.name = name;
        this.icon = icon;
        this.count = count;
    }
    
    public String getName() {
        return name;
    }
    
    public GreenfootImage getIcon() {
        return icon;
    }
    
    public int getCount() {
        return count;
    }
    
    public void setCount(int count) {
        this.count = count;
    }
    
    public void incrementCount() {
        count++;
    }
    
    public void decrementCount() {
        if (count > 0) count--;
    }
}