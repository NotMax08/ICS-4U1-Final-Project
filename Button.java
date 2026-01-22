import greenfoot.*;
/**
 * Author Paul assisted by Claude (toggleable feature and drawing the checkmark)
 * 
 * Takes in paramaters to fully customize the button
 * text is the button text, height and width are the button dimensions,
 * color is the button color, border width determines the border (subtracted
 * from the main dimensions not added), borderColor will fill the border,
 * fontSize and fontColor adjust the text on the button, id is an identification
 * that can be called elsewhere to trigger an event and toggleable determines
 * if you want the button to have a checkmark when clicked or not (true to have 
 * this feature and false to not) used to show selections from the user.
 * 
 * 
 * To use for anything other than the original Zombie Apocolypse game alter the 
 * handleClick() method, delete GreenfootImages and add your own click sound.
 */
public class Button extends Actor
{
    private String text;
    private String buttonID;
    private int width, height;
    private Color color; 
    private Color borderColor;
    private int borderWidth;
    private int fontSize;
    private Color fontColor;
    private boolean isSelected = false;
    private boolean toggleable;
    private GreenfootImage normalImage;
    private GreenfootImage selectedImage;
    //GreenfootSound click = new GreenfootSound("mouseclick.mp3");
    
    private int clickCounter = 0;
    /**
     * Accepts paramaters to fully customize a button and has a toggleable boolean that 
     * shows if its been clicked if the button is meant to select something instead of change
     * world screens.
     * 
     * @param text is the button text
     * @param height is the button height
     * @param width is the button width
     * @param color is the button's main color
     * @param borderWidth is the width of the border (subtracted from total width not added)
     * @param borderColor is the color of the border
     * @param fontSize is the size of the text
     * @param fontColor is the color of the text
     * @param id is the buttons id to be used in handleClick
     * @param toggleable is the boolean for if you want the feature to show if the button was clicked when selecting items
     */
    public Button(String text, int height, int width, Color color, int borderWidth, Color borderColor, int fontSize, Color fontColor, String id, boolean toggleable){
        this.text = text;
        this.width = width;
        this.height = height;
        this.color = color;
        this.borderWidth = borderWidth;
        this.borderColor = borderColor;
        this.buttonID = id;
        this.fontSize = fontSize;
        this.fontColor = fontColor;
        this.toggleable = toggleable;
        
        
        normalImage = createNormalImage();
        if(toggleable) {
            selectedImage = createSelectedImage();
        }
        setImage(normalImage);
    }
    //Creates default image
    private GreenfootImage createNormalImage() {
        GreenfootImage button = new GreenfootImage(width, height);
        
        if (borderWidth > 0) {
            button.setColor(borderColor);
            button.fillRect(0, 0, width, height);
            
            button.setColor(color);
            button.fillRect(borderWidth, borderWidth, width - (borderWidth * 2), height - (borderWidth * 2));
        } else {
            button.setColor(color);
            button.fillRect(0, 0, width, height);
        }
        
        Font font = new Font("Arial", true, false, fontSize);
        GreenfootImage textImage = new GreenfootImage(text, fontSize, fontColor, new Color(0, 0, 0, 0));
        
        int x = (width - textImage.getWidth()) / 2;
        int y = (height - textImage.getHeight()) / 2;
        
        button.drawImage(textImage, x, y);
        
        return button;
    }
    //Creates image for when button is selected using toggleable feature
    private GreenfootImage createSelectedImage() {
        GreenfootImage button = new GreenfootImage(width, height);
        
        button.setColor(color);
        button.fillRect(0, 0, width, height);
        
        Font font = new Font("Arial", true, false, fontSize);
        GreenfootImage textImage = new GreenfootImage(text, fontSize, fontColor, new Color(0, 0, 0, 0));
        
        int x = (width - textImage.getWidth()) / 2;
        int y = (height - textImage.getHeight()) / 2;
        
        button.drawImage(textImage, x, y);
        
        drawCheckmark(button);
        
        return button;
    }
    //Draws the checkmark on the toggleable button
    private void drawCheckmark(GreenfootImage img) {
        img.setColor(Color.GREEN);
        int checkSize = Math.min(width, height) / 4;
        int checkX = width - checkSize - 10;
        int checkY = 10;
        
        img.fillOval(checkX - 5, checkY - 5, checkSize + 10, checkSize + 10);
        
        img.setColor(Color.WHITE);
        int startX = checkX;
        int startY = checkY + checkSize / 2;
        int midX = checkX + checkSize / 3;
        int midY = checkY + checkSize - 5;
        int endX = checkX + checkSize;
        int endY = checkY;
        
        img.drawLine(startX, startY, midX, midY);
        img.drawLine(midX, midY, endX, endY);
        img.drawLine(startX + 1, startY, midX + 1, midY);
        img.drawLine(midX + 1, midY, endX + 1, endY);
        img.drawLine(startX, startY + 1, midX, midY + 1);
        img.drawLine(midX, midY + 1, endX, endY + 1);
    }
    /**
     * Accepts mouse click on each button, determines if toggleable is true if the button
     * is used to select an item or to change world screens, then calls handleClick to 
     * determine which action should be performed based on the button id and then plays a
     * clicking sound.
     */
    public void act()
    {
        if(Greenfoot.mouseClicked(this)){
            if(toggleable) {
                toggleSelection();
            }
            handleClick();
            //click.play();
        }
    }
    
    private void toggleSelection() {
        isSelected = !isSelected;
        if(isSelected) {
            setImage(selectedImage);
        } else {
            setImage(normalImage);
        }
    }
    /**
     * gets button id to be used in handleClick and give the correct actions when each
     * button is clicked
     * 
     * @returns buttonID
     */
    public String getButtonID(){
        return buttonID;
    }
    /**
     * @return boolean isSelected for buttons in ChooseWorld to update the booleans for 
     * each item. For example if shield is selected SHIELD is true and if clicked again
     * SHIELD is now false.
     */
    public boolean isSelected(){
        return isSelected;
    }
    
    /**
     * 
     * Handles actions when clicking all buttons in this project. Uses buttonID to 
     * ensure each button functions as intended when clicked.
     */
    public void handleClick(){
        
        // The button in ChooseWorld that leads to the GameWorld
        if(buttonID.equals("startworld")){
           
            System.gc(); // Force garbage collection
            Greenfoot.setWorld(new RoomOne());
            HighScoreManager.startRun();
            return;
        }
        if(buttonID.equals("backtostart")){
            Greenfoot.setWorld(new StartWorld());
            return;
        }
        if (buttonID.equals("lb")){
            Greenfoot.setWorld(new LeaderboardScreen());
            return;
        }
    }
    private Button findContinueButton(World world) {
        for (Button button : world.getObjects(Button.class)) {
            if (button.getButtonID().equals("continue")) {
                return button;
            }
        }
        return null;
    }
    
    private Button findBackButton(World world) {
        for (Button button : world.getObjects(Button.class)) {
            if (button.getButtonID().equals("back")) {
                return button;
            }
        }
        return null;
    }
}
