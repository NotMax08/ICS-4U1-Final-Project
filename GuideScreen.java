import greenfoot.*;

/**
 * Lore and controls screen for The Forgotten Catacombs
 * 
 * @author Paul and Claude
 */
public class GuideScreen extends World {
    /**
     * Constructor for lore and keybinds screen
     */
    public GuideScreen() {    
        super(1200, 800, 1);
        
        // Create black background
        GreenfootImage bg = new GreenfootImage(1200, 800);
        bg.setColor(Color.BLACK);
        bg.fill();
        
        // Draw all text on background
        drawLoreAndControls(bg);
        
        setBackground(bg);
    }
    
    private void drawLoreAndControls(GreenfootImage bg) {
        int leftMargin = 80;
        int rightMargin = 650;
        int startY = 60;
        int lineSpacing = 30;
        int sectionSpacing = 40;
        
        // Title
        bg.setColor(new Color(200, 150, 50)); // Gold color
        bg.setFont(new Font(40));
        bg.drawString("The Forgotten Catacombs", leftMargin, startY);
        
        // Lore text
        bg.setColor(Color.WHITE);
        bg.setFont(new Font(16));
        
        int y = startY + 60;
        bg.drawString("Long ago, monks beneath the kingdom of Hallownest sought immortality", leftMargin, y);
        y += lineSpacing;
        bg.drawString("through forbidden magic. Their ritual tore open a rift to the shadow", leftMargin, y);
        y += lineSpacing;
        bg.drawString("realm, corrupting everything within. Monks twisted into hollow knights,", leftMargin, y);
        y += lineSpacing;
        bg.drawString("fungal horrors spawned in the depths, and their leader became a", leftMargin, y);
        y += lineSpacing;
        bg.drawString("monstrous demon.", leftMargin, y);
        
        y += sectionSpacing;
        bg.drawString("The kingdom sealed the catacombs, but the evil only grew stronger.", leftMargin, y);
        
        y += sectionSpacing;
        bg.drawString("You are a condemned prisoner offered one chance at redemption:", leftMargin, y);
        y += lineSpacing;
        bg.drawString("descend into the cursed halls and slay the demon that dwells within.", leftMargin, y);
        y += lineSpacing;
        bg.drawString("Armed with only a blade and courage, you must venture through", leftMargin, y);
        y += lineSpacing;
        bg.drawString("chambers filled with reanimated knights, spawning fungi, and fallen", leftMargin, y);
        y += lineSpacing;
        bg.drawString("warriors.", leftMargin, y);
        
        y += sectionSpacing;
        bg.drawString("Legends speak of a celestial weapon hidden in the depths - your only", leftMargin, y);
        y += lineSpacing;
        bg.drawString("hope against the demon that awaits in its throne room, where reality", leftMargin, y);
        y += lineSpacing;
        bg.drawString("itself fractures.", leftMargin, y);
        
        y += sectionSpacing + 10;
        bg.setColor(new Color(200, 150, 50));
        bg.setFont(new Font(20));
        bg.drawString("Will you claim your freedom, or become another lost soul?", leftMargin, y);
        
        // Controls section
        y = startY + 60;
        bg.setColor(new Color(200, 150, 50));
        bg.setFont(new Font(28));
        bg.drawString("Controls", rightMargin, y);
        
        bg.setColor(Color.WHITE);
        bg.setFont(new Font(16));
        y += 50;
        
        // Movement
        bg.setColor(new Color(150, 200, 255));
        bg.drawString("Movement:", rightMargin, y);
        bg.setColor(Color.WHITE);
        y += lineSpacing;
        bg.drawString("  SPACE/W - Jump", rightMargin, y);
        y += lineSpacing;
        bg.drawString("  A/D or Arrow Keys - Move", rightMargin, y);
        y += lineSpacing;
        bg.drawString("  S/Down - Fall faster", rightMargin, y);
        
        y += sectionSpacing;
        
        // Combat
        bg.setColor(new Color(255, 150, 150));
        bg.drawString("Combat:", rightMargin, y);
        bg.setColor(Color.WHITE);
        y += lineSpacing;
        bg.drawString("  J/E - Basic Attack", rightMargin, y);
        y += lineSpacing;
        bg.drawString("  K/Left Shift - Magic Attack", rightMargin, y);
        
        y += sectionSpacing;
        
        // Abilities
        bg.setColor(new Color(150, 255, 150));
        bg.drawString("Abilities:", rightMargin, y);
        bg.setColor(Color.WHITE);
        y += lineSpacing;
        bg.drawString("  Q/H - Heal (hold 2 sec)", rightMargin, y);
        
        y += sectionSpacing;
        
        // Interface
        bg.setColor(new Color(255, 200, 100));
        bg.drawString("Interface:", rightMargin, y);
        bg.setColor(Color.WHITE);
        y += lineSpacing;
        bg.drawString("  TAB - Inventory", rightMargin, y);
        y += lineSpacing;
        bg.drawString("  M - Map", rightMargin, y);
        y += lineSpacing;
        bg.drawString("  F - Interact/Enter Door", rightMargin, y);
        y += lineSpacing;
        bg.drawString("  Mouse Click - Buy/Use Items", rightMargin, y);
        
        y += sectionSpacing;
        
        // Debug
        bg.setColor(new Color(150, 150, 150));
        bg.drawString("Debug:", rightMargin, y);
        bg.setColor(new Color(180, 180, 180));
        y += lineSpacing;
        bg.drawString("  G - Toggle Grid", rightMargin, y);
        y += lineSpacing;
        bg.drawString("  Y - Room Position Test", rightMargin, y);
        
        // Bottom instruction
        bg.setColor(new Color(200, 150, 50));
        bg.setFont(new Font(22));
        bg.drawString("Press ESC to return to start", 420, 760);
    }
    /**
     * tracks esc key to return to startworld
     */
    public void act() {
        if (Greenfoot.isKeyDown("escape")) {
            Greenfoot.setWorld(new StartWorld()); // Change to your start world
        }
    }
}