package org.acme;

/**
 * WorldClock
 */
public class WorldClock {

    private String currentDateTime;
    /**
     * @return the currentDateTime
     */
    public String getCurrentDateTime() {
        return currentDateTime;
    }
    /**
     * @param currentDateTime the currentDateTime to set
     */
    public void setCurrentDateTime(String currentDateTime) {
        this.currentDateTime = currentDateTime;
    }
}