import greenfoot.*;

/**
 * TextManager used to handle and manage text. Makes creating text on screen easier
 * 
 * @author Julian and Google AI Studio
 * @verson 2026
 */
public class TextManager
{    
    // We keep these static so that new text replaces old text globally
    static TextBox text; 
    static TextBox textOne;
    static TextBox textTwo;

    /**
     * @param world The world to add the text to (pass 'getWorld()' from your actor)
     * @param x The X position (pass 'getX()' from your actor)
     * @param y The Y position (pass 'getY()' from your actor)
     * @param offX Manual offset X
     * @param offY Manual offset Y
     * @param dialogue The string
     * @param fontSize The size
     * @param split Whether to split at '|'
     */
    static void textBoxWriter(World world, int x, int y, int offX, int offY, String dialogue, int fontSize, boolean split)
    {
        // Must pass world to the remove method too
        removeText(world);
        
        if (split)
        {
            int index = 0;
            String lineOne = "";
            String lineTwo = "";

            for (int i = 0; i < dialogue.length(); i++)
            {
                index++; 
                if(dialogue.charAt(i) == '|')
                {
                    lineOne = dialogue.substring(0, index - 1);
                    lineTwo = dialogue.substring(index);
                    break;
                }
            }

            textOne = new TextBox(lineOne, fontSize);
            textTwo = new TextBox(lineTwo, fontSize);

            // Using the passed variables to place the objects
            world.addObject(textOne, x + offX, y + offY - 15);
            world.addObject(textTwo, x + offX, y + offY);
        }
        else
        { 
            text = new TextBox(dialogue, fontSize);
            world.addObject(text, x + offX, y + offY - 15);
        }
    }

    /**
     * removes text from the world
     */
    static void removeText(World world)
    {
        // We check world != null because static methods don't have a 'built-in' world
        if (world == null) return;

        if (text != null && text.getWorld() != null) {
            world.removeObject(text);
        }
        if (textOne != null && textOne.getWorld() != null) {
            world.removeObject(textOne);
        }
        if (textTwo != null && textTwo.getWorld() != null) {
            world.removeObject(textTwo);
        }
    }
}