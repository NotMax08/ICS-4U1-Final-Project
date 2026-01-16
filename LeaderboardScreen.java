import greenfoot.*;
public class LeaderboardScreen extends World {
    
    public LeaderboardScreen() {
        super(800, 600, 1);
        
        setBackground("leaderboard_bg.png");
        //displayLeaderboard();
    }
    /**
    private void displayLeaderboard() {
        // Get top 10 players
        UserInfo[] topScores = HighScoreManager.getTopScores(10);
        
        if (topScores == null) {
            showText("Not connected to Greenfoot Gallery", 400, 300);
            return;
        }
        
        int startY = 150;
        int spacing = 40;
        
        for (int i = 0; i < topScores.length; i++) {
            UserInfo player = topScores[i];
            String rank = (i + 1) + ".";
            String name = player.getUserName();
            String time = HighScoreManager.formatTime(player.getScore());
            
            String text = rank + " " + name + " - " + time;
            showText(text, 400, startY + (i * spacing));
        }
        
        // Show player's rank
        int myRank = HighScoreManager.getMyRank();
        if (myRank > 0) {
            showText("Your Rank: #" + myRank, 400, 550);
        }
    }
    
    public void act() {
        if (Greenfoot.isKeyDown("escape")) {
            Greenfoot.setWorld(new TitleScreen());
        }
    }
    */
}