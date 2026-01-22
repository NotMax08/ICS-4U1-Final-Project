import greenfoot.*;
import java.io.*;
import java.util.*;

public class HighScoreManager {
    private static final String SCORES_FILE = "leaderboard.txt";
    private static final int MAX_ENTRIES = 10;
    
    private static long runStartTime = 0;
    private static boolean runInProgress = false;
    private static String currentPlayerName = "Player";
    
    /**
     * starts timer for run
     */
    public static void startRun() {
        runStartTime = System.currentTimeMillis();
        runInProgress = true;
    }
    
    /**
     * ends timer and tracks run
     */
    public static void completeRun() {
        if (!runInProgress) return;
        
        long endTime = System.currentTimeMillis();
        int timeInSeconds = (int)((endTime - runStartTime) / 1000);
        
        runInProgress = false;
        
        // Save to local file
        saveScore(currentPlayerName, timeInSeconds);
    }
    /**
     * cancels run. called if you die
     */
    public static void cancelRun() {
        if (!runInProgress) return;
        
        runInProgress = false;
        System.out.println("Run cancelled - no score saved");
    }
    
    /**
     * checks for in progress run
     */
    public static boolean isRunInProgress() {
        return runInProgress;
    }
    /**
     * getter for current timer time
     */
    public static int getCurrentRunTime() {
        if (!runInProgress) return 0;
        
        long currentTime = System.currentTimeMillis();
        return (int)((currentTime - runStartTime) / 1000);
    }
    /**
     * sets username called after username screen saves input
     */
    public static void setPlayerName(String name) {
        if (name != null && !name.trim().isEmpty()) {
            currentPlayerName = name.trim();
        }
    }
    /**
     * getter for username saved
     */
    public static String getPlayerName() {
        return currentPlayerName;
    }
    
    // Save score to local file
    private static void saveScore(String playerName, int timeInSeconds) {
        try {
            List<ScoreEntry> scores = loadScores();
            
            // Add new score
            scores.add(new ScoreEntry(playerName, timeInSeconds));
            
            // Sort by time (ascending - lower is better)
            Collections.sort(scores);
            
            // Keep only top MAX_ENTRIES
            if (scores.size() > MAX_ENTRIES) {
                scores = scores.subList(0, MAX_ENTRIES);
            }
            
            // Write back to file
            writeScores(scores);
            
            System.out.println("Score saved: " + playerName + " - " + formatTime(timeInSeconds));
            
        } catch (IOException e) {
            System.err.println("Error saving score: " + e.getMessage());
        }
    }
    /**
     * loads saved scores
     */
    public static List<ScoreEntry> loadScores() {
        List<ScoreEntry> scores = new ArrayList<>();
        
        try (BufferedReader reader = new BufferedReader(new FileReader(SCORES_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 2) {
                    String name = parts[0];
                    int time = Integer.parseInt(parts[1]);
                    scores.add(new ScoreEntry(name, time));
                }
            }
        } catch (FileNotFoundException e) {
            // File doesn't exist yet, return empty list
            System.out.println("No leaderboard file found, creating new one");
        } catch (IOException e) {
            System.err.println("Error reading scores: " + e.getMessage());
        }
        
        return scores;
    }
    
    // Write scores to file
    private static void writeScores(List<ScoreEntry> scores) throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(SCORES_FILE))) {
            for (ScoreEntry entry : scores) {
                writer.println(entry.name + "," + entry.time);
            }
        }
    }
    /**
     * getter for top scores
     */
    public static int getPlayerRank(String playerName) {
        List<ScoreEntry> scores = loadScores();
        
        for (int i = 0; i < scores.size(); i++) {
            if (scores.get(i).name.equals(playerName)) {
                return i + 1; // 1-based ranking
            }
        }
        
        return -1; // Not on leaderboard
    }
    /**
     * getter for top time
     */
    public static int getPlayerBestTime(String playerName) {
        List<ScoreEntry> scores = loadScores();
        
        for (ScoreEntry entry : scores) {
            if (entry.name.equals(playerName)) {
                return entry.time;
            }
        }
        
        return 0; // No score found
    }
    /**
     * formats the time text
     */
    public static String formatTime(int seconds) {
        int minutes = seconds / 60;
        int secs = seconds % 60;
        return String.format("%d:%02d", minutes, secs);
    }
    /**
     * clears all scores saved
     */
    public static void clearScores() {
        try {
            new File(SCORES_FILE).delete();
            System.out.println("Leaderboard cleared");
        } catch (Exception e) {
            System.err.println("Error clearing scores: " + e.getMessage());
        }
    }
    
    /**
     * class for each score item
     * 
     * @author Paul and Claude
     */
    public static class ScoreEntry implements Comparable<ScoreEntry> {
        public String name;
        public int time;
        /**
         * constructor for score item to be saved
         */
        public ScoreEntry(String name, int time) {
            this.name = name;
            this.time = time;
        }
        /**
         * compares to other saved scores
         */
        @Override
        public int compareTo(ScoreEntry other) {
            // Sort by time ascending (lower is better)
            return Integer.compare(this.time, other.time);
        }
    }
}