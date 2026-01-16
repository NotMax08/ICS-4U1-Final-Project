import greenfoot.*;

public class HighScoreManager {
    private static long runStartTime = 0;
    private static boolean runInProgress = false;
    
    // Start timing a run
    public static void startRun() {
        runStartTime = System.currentTimeMillis();
        runInProgress = true;
    }
    
    // Complete a run and save the score
    public static void completeRun() {
        if (!runInProgress) return;
        
        long endTime = System.currentTimeMillis();
        int timeInSeconds = (int)((endTime - runStartTime) / 1000);
        
        runInProgress = false;
        
        // Save to UserInfo (works in Greenfoot Gallery)
        saveScore(timeInSeconds);
    }
    
    // Save score to Greenfoot's online leaderboard
    private static void saveScore(int timeInSeconds) {
        UserInfo user = UserInfo.getMyInfo();
        
        if (user != null) {
            // Get current best time (lower is better)
            int currentBest = user.getScore();
            
            // Only update if this run was faster (or first run)
            if (currentBest == 0 || timeInSeconds < currentBest) {
                user.setScore(timeInSeconds);
                user.store(); // Upload to Greenfoot Gallery
                
                System.out.println("New best time: " + formatTime(timeInSeconds));
            }
        } else {
            System.out.println("Not logged in - score not saved");
        }
    }
    /**
    // Get the top scores from all players
    public static UserInfo[] getTopScores(int limit) {
        return UserInfo.getTop(limit);
    }
    
    // Get scores near the current player
    public static UserInfo[] getNearbyScores(int limit) {
        return UserInfo.getNearby(limit);
    }
    */
    // Get current player's rank
    public static int getMyRank() {
        UserInfo user = UserInfo.getMyInfo();
        return (user != null) ? user.getRank() : -1;
    }
    
    // Format time as MM:SS
    public static String formatTime(int seconds) {
        int minutes = seconds / 60;
        int secs = seconds % 60;
        return String.format("%d:%02d", minutes, secs);
    }
    
    // Get player's best time
    public static int getMyBestTime() {
        UserInfo user = UserInfo.getMyInfo();
        return (user != null) ? user.getScore() : 0;
    }
}