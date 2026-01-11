/**
 * A helper class for managing counters and timers.
 * Each Counter instance tracks its own count and can be incremented, reset, or checked.
 * 
 * @author Claude
 */
public class Counter {
    private int count;
    private int threshold;
    private boolean autoReset;
    
    /**
     * Create a counter with no threshold (just counts up)
     */
    public Counter() {
        this(0, -1, false);
    }
    
    /**
     * Create a counter with a threshold
     * 
     * @param threshold The value at which the counter is "complete"
     */
    public Counter(int threshold) {
        this(0, threshold, false);
    }
    
    /**
     * Create a counter with a threshold and auto-reset option
     * 
     * @param threshold The value at which the counter is "complete"
     * @param autoReset If true, counter resets to 0 when threshold is reached
     */
    public Counter(int threshold, boolean autoReset) {
        this(0, threshold, autoReset);
    }
    
    /**
     * Full constructor - set initial count, threshold, and auto-reset
     * 
     * @param initialCount Starting count value
     * @param threshold The value at which the counter is "complete"
     * @param autoReset If true, counter resets to 0 when threshold is reached
     */
    public Counter(int initialCount, int threshold, boolean autoReset) {
        this.count = initialCount;
        this.threshold = threshold;
        this.autoReset = autoReset;
    }
    
    /**
     * Increment the counter by 1
     */
    public void increment() {
        increment(1);
    }
    
    /**
     * Increment the counter by a specific amount
     * 
     * @param amount How much to add to the counter
     */
    public void increment(int amount) {
        count += amount;
        
        // Auto-reset if enabled and threshold reached
        if (autoReset && threshold > 0 && count >= threshold) {
            count = 0;
        }
    }
    
    /**
     * Decrement the counter by 1
     */
    public void decrement() {
        decrement(1);
    }
    
    /**
     * Decrement the counter by a specific amount (won't go below 0)
     * 
     * @param amount How much to subtract from the counter
     */
    public void decrement(int amount) {
        count -= amount;
        if (count < 0) {
            count = 0;
        }
    }
    
    /**
     * Reset the counter to 0
     */
    public void reset() {
        count = 0;
    }
    
    /**
     * Set the counter to a specific value
     * 
     * @param value The new count value
     */
    public void set(int value) {
        count = value;
    }
    
    /**
     * Get the current count
     * 
     * @return Current count value
     */
    public int getCount() {
        return count;
    }
    
    /**
     * Check if counter has reached its threshold
     * 
     * @return true if count >= threshold (or threshold is not set)
     */
    public boolean isComplete() {
        if (threshold < 0) return false; // No threshold set
        return count >= threshold;
    }
    
    /**
     * Check if counter is at zero
     * 
     * @return true if count == 0
     */
    public boolean isZero() {
        return count == 0;
    }
    
    /**
     * Check if counter equals a specific value
     * 
     * @param value Value to check against
     * @return true if count equals value
     */
    public boolean equals(int value) {
        return count == value;
    }
    
    /**
     * Check if counter is greater than a value
     * 
     * @param value Value to compare
     * @return true if count > value
     */
    public boolean greaterThan(int value) {
        return count > value;
    }
    
    /**
     * Check if counter is less than a value
     * 
     * @param value Value to compare
     * @return true if count < value
     */
    public boolean lessThan(int value) {
        return count < value;
    }
    
    /**
     * Set a new threshold for this counter
     * 
     * @param newThreshold The new threshold value
     */
    public void setThreshold(int newThreshold) {
        this.threshold = newThreshold;
    }
    
    /**
     * Get the current threshold
     * 
     * @return The threshold value
     */
    public int getThreshold() {
        return threshold;
    }
    
    /**
     * Check if counter has been incrementing and should trigger an action at regular intervals
     * This is useful for animation frames that cycle
     * 
     * @param interval The interval to check (e.g., every 5 counts)
     * @return true if count is a multiple of interval and count > 0
     */
    public boolean hitInterval(int interval) {
        return count > 0 && count % interval == 0;
    }
    
    @Override
    public String toString() {
        if (threshold > 0) {
            return count + "/" + threshold;
        }
        return String.valueOf(count);
    }
}