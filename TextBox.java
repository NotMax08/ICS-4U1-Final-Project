import greenfoot.*; 
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.io.InputStream;
import java.io.IOException;

/**
 * TextBox is an Actor that displays text using the 'Press Start 2P' custom font.
 * 
 * @author Julian and Gemini 
 * 
 * "Press Start 2P" font by Cody "Zone 38" Cuellar (SIL Open Font License).
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
            //finds font file in project folder
            InputStream is = TextBox.class.getClassLoader().getResourceAsStream("PressStart2P-Regular.ttf");

            if (is == null) {
                // if file is misspelled or missing, show error
                System.out.println("FILE NOT FOUND: Make sure PressStart2P-Regular.ttf is in the project folder!");
            } else {
                // convert file into java font objects
                Font awtFont = Font.createFont(Font.TRUETYPE_FONT, is);

                // Register the font with the Graphics Environment.
                // This makes the font available to the JVM by its internal name: "Press Start 2P"
                GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(awtFont);

                // Confirm it worked in the terminal
                System.out.println("Font loaded successfully: " + awtFont.getName());
            }
        } // Correctly closing the try block before catching specific exceptions
        catch (FontFormatException e) {
            // specifically handles cases where the file is not a valid font
            System.out.println("Error: The font file is not formatted correctly.");
            e.printStackTrace();
        } 
        catch (IOException e) {
            // specifically handles cases where the file cannot be read or found
            System.out.println("Error: Could not read the font file from the disk.");
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
        // Creates greenfoot version of font using the new name
        greenfoot.Font retroFont = new greenfoot.Font("Press Start 2P", size);

        // calculates how big the image needs to be
        // this is a simple estimation: characters * font size
        int width = text.length() * size; 
        int height = size + 10; // Add a little extra room for spacing

        // safety check to make sure the program doesnt crash. Has to be atleast 1 pixel wide
        if (width <= 0) width = 1; 

        // Creates a new blank transparent image to use as the canvas
        GreenfootImage img = new GreenfootImage(width, height);

        // apply the font and color to the blank image
        img.setFont(retroFont);
        img.setColor(Color.WHITE);

        // draws the text onto the image
        // (text, x-position, y-position)
        img.drawString(text, 0, size);

        // updates the actors appearance to the image we just created
        setImage(img);
    }

    public void act()
    {
        // Currently empty - used if you want the text to move or change later
    }
}