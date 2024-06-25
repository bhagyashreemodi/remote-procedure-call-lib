package application;

/**
 * Exception to be thrown when a task is not found.
 */
public class TaskNotFoundException extends Exception {
    public TaskNotFoundException(String exceptionMessage) {
        super(exceptionMessage);
    }
}
