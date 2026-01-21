import greenfoot.*;
import java.util.List;

/**
 * @author Paul and Claude
 */
public class LeaderboardScreen extends World {
    GreenfootImage lbscreen = new GreenfootImage("lbscreen.png");
    public LeaderboardScreen() {
        super(800, 600, 1);
        lbscreen.scale(800,600);
        setBackground(lbscreen);
        displayLeaderboard();
    }
    
    private void displayLeaderboard() {
        List<HighScoreManager.ScoreEntry> scores = HighScoreManager.loadScores();
        
        if (scores.isEmpty()) {
            showText("No scores yet - be the first!", 400, 300);
            showText("Press ESC to return", 400, 550);
            return;
        }
        
        // Display title
        showText("LEADERBOARD", 400, 80);
        
        int startY = 150;
        int spacing = 40;
        
        // Display top scores
        for (int i = 0; i < scores.size(); i++) {
            HighScoreManager.ScoreEntry entry = scores.get(i);
            String rank = (i + 1) + ".";
            String name = entry.name;
            String time = HighScoreManager.formatTime(entry.time);
            
            String text = rank + " " + name + " - " + time;
            showText(text, 400, startY + (i * spacing));
        }
        
        // Show current player's rank if on leaderboard
        String playerName = HighScoreManager.getPlayerName();
        int rank = HighScoreManager.getPlayerRank(playerName);
        
        if (rank > 0) {
            int bestTime = HighScoreManager.getPlayerBestTime(playerName);
            String rankText = "Your Best: #" + rank + " - " + HighScoreManager.formatTime(bestTime);
            showText(rankText, 400, 550);
        } else {
            showText("Complete a run to get on the leaderboard!", 400, 550);
        }
        
        // Instructions
        showText("Press ESC to return", 400, 580);
    }
    
    public void act() {
        if (Greenfoot.isKeyDown("escape")) {
            // Replace with your actual title screen class
            // Greenfoot.setWorld(new TitleScreen());
        }
    }
}