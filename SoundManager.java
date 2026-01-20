import greenfoot.*;
import java.util.HashMap;

/**
 * SoundManager handles all sound effects and background music for the game.
 * Provides methods to play, stop, and manage audio with volume control.
 * 
 * @author Claude
 */
public class SoundManager {
    private static SoundManager instance;
    private HashMap<String, GreenfootSound> sounds;
    private GreenfootSound backgroundMusic;
    private int masterVolume = 70;
    private boolean soundEnabled = true;
    private boolean musicEnabled = true;
    
    private SoundManager() {
        sounds = new HashMap<>();
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
     * Loads a sound file and stores it in the sound library.
     * @param name The identifier for this sound
     * @param filename The sound file name (e.g., "explosion.wav")
     */
    public void loadSound(String name, String filename) {
        try {
            GreenfootSound sound = new GreenfootSound(filename);
            sounds.put(name, sound);
        } catch (IllegalArgumentException e) {
            System.out.println("Error loading sound: " + filename);
        }
    }
    
    /**
     * Plays a sound effect once.
     * @param name The identifier of the sound to play
     */
    public void playSound(String name) {
        if (!soundEnabled) return;
        
        GreenfootSound sound = sounds.get(name);
        if (sound != null) {
            sound.setVolume(masterVolume);
            sound.play();
        }
    }
    
    /**
     * Plays a sound effect on loop.
     * @param name The identifier of the sound to loop
     */
    public void loopSound(String name) {
        if (!soundEnabled) return;
        
        GreenfootSound sound = sounds.get(name);
        if (sound != null) {
            sound.setVolume(masterVolume);
            sound.playLoop();
        }
    }
    
    /**
     * Stops a currently playing sound.
     * @param name The identifier of the sound to stop
     */
    public void stopSound(String name) {
        GreenfootSound sound = sounds.get(name);
        if (sound != null) {
            sound.stop();
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
            backgroundMusic.setVolume(masterVolume - 20); // Music slightly quieter
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
        
        // Update volume for background music if playing
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
        for (GreenfootSound sound : sounds.values()) {
            sound.stop();
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