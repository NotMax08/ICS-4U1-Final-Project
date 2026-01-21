import greenfoot.*;
import java.util.HashMap;
import java.util.ArrayList;

/**
 * SoundManager handles all sound effects and background music for the game.
 * Provides methods to play, stop, and manage audio with volume control.
 * 
 * @author Claude
 */
public class SoundManager {
    private static SoundManager instance;
    private HashMap<String, ArrayList<GreenfootSound>> soundArrays;
    private HashMap<String, Integer> soundIndices;
    private GreenfootSound backgroundMusic;
    private int masterVolume = 100;
    private boolean soundEnabled = true;
    private boolean musicEnabled = true;
    
    private SoundManager() {
        soundArrays = new HashMap<>();
        soundIndices = new HashMap<>();
    }
    
    /**
     * Gets the singleton instance of SoundManager.
     */
    public static SoundManager getInstance() {
        if (instance == null) {
            instance = new SoundManager();
        }
        return instance;
    }
    
    /**
     * Loads multiple copies of a sound file for overlapping playback.
     * @param name The identifier for this sound
     * @param filename The sound file name (e.g., "explosion.wav")
     * @param copies Number of sound instances to create
     */
    public void loadSound(String name, String filename, int copies) {
        try {
            ArrayList<GreenfootSound> sounds = new ArrayList<>();
            for (int i = 0; i < copies; i++) {
                sounds.add(new GreenfootSound(filename));
            }
            soundArrays.put(name, sounds);
            soundIndices.put(name, 0);
        } catch (IllegalArgumentException e) {
            System.out.println("Error loading sound: " + filename);
        }
    }
    
    /**
     * Loads a single sound file.
     * @param name The identifier for this sound
     * @param filename The sound file name
     */
    public void loadSound(String name, String filename) {
        loadSound(name, filename, 1);
    }
    
    /**
     * Plays a sound effect once, cycling through available instances.
     * @param name The identifier of the sound to play
     */
    public void playSound(String name) {
        if (!soundEnabled) return;
        
        ArrayList<GreenfootSound> sounds = soundArrays.get(name);
        if (sounds != null && !sounds.isEmpty()) {
            int index = soundIndices.get(name);
            GreenfootSound sound = sounds.get(index);
            sound.setVolume(masterVolume);
            sound.play();
            
            // Cycle to next sound instance
            soundIndices.put(name, (index + 1) % sounds.size());
        }
    }
    
    /**
     * Plays a sound effect on loop.
     * @param name The identifier of the sound to loop
     */
    public void loopSound(String name) {
        if (!soundEnabled) return;
        
        ArrayList<GreenfootSound> sounds = soundArrays.get(name);
        if (sounds != null && !sounds.isEmpty()) {
            GreenfootSound sound = sounds.get(0);
            sound.setVolume(masterVolume);
            sound.playLoop();
        }
    }
    
    /**
     * Stops a currently playing sound.
     * @param name The identifier of the sound to stop
     */
    public void stopSound(String name) {
        ArrayList<GreenfootSound> sounds = soundArrays.get(name);
        if (sounds != null) {
            for (GreenfootSound sound : sounds) {
                sound.stop();
            }
        }
    }
    
    /**
     * Plays background music on loop.
     * @param filename The music file name
     */
    public void playBackgroundMusic(String filename) {
        if (!musicEnabled) return;
        
        if (backgroundMusic != null) {
            backgroundMusic.stop();
        }
        
        try {
            backgroundMusic = new GreenfootSound(filename);
            backgroundMusic.setVolume(masterVolume - 20);
            backgroundMusic.playLoop();
        } catch (IllegalArgumentException e) {
            System.out.println("Error loading music: " + filename);
        }
    }
    
    /**
     * Stops the background music.
     */
    public void stopBackgroundMusic() {
        if (backgroundMusic != null) {
            backgroundMusic.stop();
        }
    }
    
    /**
     * Pauses the background music.
     */
    public void pauseBackgroundMusic() {
        if (backgroundMusic != null) {
            backgroundMusic.pause();
        }
    }
    
    /**
     * Sets the master volume for all sounds.
     * @param volume Volume level (0-100)
     */
    public void setVolume(int volume) {
        masterVolume = Math.max(0, Math.min(100, volume));
        
        if (backgroundMusic != null) {
            backgroundMusic.setVolume(masterVolume - 20);
        }
    }
    
    /**
     * Gets the current master volume.
     * @return Current volume (0-100)
     */
    public int getVolume() {
        return masterVolume;
    }
    
    /**
     * Toggles sound effects on/off.
     */
    public void toggleSound() {
        soundEnabled = !soundEnabled;
        if (!soundEnabled) {
            stopAllSounds();
        }
    }
    
    /**
     * Toggles background music on/off.
     */
    public void toggleMusic() {
        musicEnabled = !musicEnabled;
        if (!musicEnabled) {
            stopBackgroundMusic();
        } else if (backgroundMusic != null) {
            backgroundMusic.playLoop();
        }
    }
    
    /**
     * Stops all currently playing sounds (except background music).
     */
    public void stopAllSounds() {
        for (ArrayList<GreenfootSound> sounds : soundArrays.values()) {
            for (GreenfootSound sound : sounds) {
                sound.stop();
            }
        }
    }
    
    /**
     * Stops everything including background music.
     */
    public void stopAll() {
        stopAllSounds();
        stopBackgroundMusic();
    }
    
    /**
     * Checks if sound effects are enabled.
     */
    public boolean isSoundEnabled() {
        return soundEnabled;
    }
    
    /**
     * Checks if background music is enabled.
     */
    public boolean isMusicEnabled() {
        return musicEnabled;
    }
}