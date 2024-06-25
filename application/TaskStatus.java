package application;

/**
 * Enum to represent the status of the task.
 */
public enum TaskStatus {
    OPEN,
    CLOSED,
    IN_PROGRESS;

    /**
     * Gets the status of the task.
     *
     * @param status The status of the task.
     * @return The status of the task.
     */
    public static TaskStatus getStatus(String status) {
        if(status.equalsIgnoreCase("OPEN"))
            return OPEN;
        else if(status.equalsIgnoreCase("CLOSED"))
            return CLOSED;
        else
            return IN_PROGRESS;
    }
}
