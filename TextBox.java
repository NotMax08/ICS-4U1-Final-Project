import greenfoot.*; 
import java.awt.Font; // Standard Java Font class
import java.awt.GraphicsEnvironment; // Used to register the font to the system
import java.io.InputStream; // Used to load the file so it works on the Gallery

/**
 * TextBox is an Actor that displays text using the 'Press Start 2P' custom font.
 * 
 * @author Julian and Gemini 
 */
public class TextBox extends Actor
{
    /**
     * STATIC BLOCK:
     * This code runs only ONCE when the game first starts.
     * It loads the font file into the computer's memory so Greenfoot can find it.
     */
    static {
        try {
            // 1. Look for the font file in the project folder
            InputStream is = TextBox.class.getClassLoader().getResourceAsStream("PressStart2P-Regular.ttf");
            
            if (is == null) {
                // If the file is missing or misspelled, show an error in the terminal
                System.out.println("FILE NOT FOUND: Make sure PressStart2P-Regular.ttf is in the project folder!");
            } else {
                // 2. Convert the file stream into a Java Font object
                Font awtFont = Font.createFont(Font.TRUETYPE_FONT, is);
                
                // 3. Register the font with the Graphics Environment.
                // This "installs" it temporarily so Java can find it by its name: "Press Start 2P"
                GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(awtFont);
                
                // Confirm it worked in the terminal
                System.out.println("Font loaded successfully: " + awtFont.getName());
            }
        } catch (Exception e) {
            // If anything goes wrong (corrupt file, etc.), print the error details
            e.printStackTrace();
        }
    }
    
    /**
     * Constructor to create the TextBox
     * 
     * @params text --> text you want to write using the font
     * @params size --> size of the font
     */
    public TextBox(String text, int size)
    {
        updateImage(text, size);
    }
    
    /**
     * This method creates the visuals of the text
     */
    public void updateImage(String text, int size)
    {
        // 1. Create a Greenfoot version of the font using the name we registered above
        greenfoot.Font retroFont = new greenfoot.Font("Press Start 2P", size);
        
        // 2. Calculate how big the image needs to be based on the text length
        // This is a simple estimation: characters * font size
        int width = text.length() * size; 
        int height = size + 10; // Add a little extra room for spacing
        
        // 3. Safety check: An image must be at least 1 pixel wide or Greenfoot crashes
        if (width <= 0) width = 1; 
        
        // 4. Create a new blank, transparent image (the "canvas")
        GreenfootImage img = new GreenfootImage(width, height);
        
        // 5. Apply the font and color settings to the blank image
        img.setFont(retroFont);
        img.setColor(Color.WHITE);
        
        // 6. Draw the text onto the image
        // (text, x-position, y-position)
        img.drawString(text, 0, size);
        
        // 7. Tell this Actor to use the image we just created
        setImage(img);
    }

    public void act()
    {
        // Currently empty - used if you want the text to move or change later
    }
}