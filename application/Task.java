package application;

import java.io.Serial;
import java.io.Serializable;

/**
 * Task class to represent the task object.
 */
public class Task implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L; // UID for serialization
    /**
     * ID of the task.
     */
    private int id;

    /**
     * Title of the task.
     */
    private String title;

    /**
     * Description of the task.
     */
    private String description;

    /**
     * Status of the task.
     */
    private TaskStatus status;

    /**
     * Assignee of the task.
     */
    private String assignee;

    /**
     * Constructs a new task with the given id, title, description, status and assignee.
     *
     * @param id          The id of the task.
     * @param title       The title of the task.
     * @param description The description of the task.
     * @param status      The status of the task.
     * @param assignee    The assignee of the task.
     */
    public Task(int id, String title, String description, TaskStatus status, String assignee) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.status = status;
        this.assignee = assignee;
    }

    /**
     * Gets the id of the task.
     *
     * @return The id of the task.
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the id of the task.
     *
     * @param id The id of the task.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Gets the title of the task.
     *
     * @return The title of the task.
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the title of the task.
     *
     * @param title The title of the task.
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Gets the description of the task.
     *
     * @return The description of the task.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the description of the task.
     *
     * @param description The description of the task.
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Gets the status of the task.
     *
     * @return The status of the task.
     */
    public TaskStatus getStatus() {
        return status;
    }

    /**
     * Sets the status of the task.
     *
     * @param status The status of the task.
     */
    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    /**
     * Gets the assignee of the task.
     *
     * @return The assignee of the task.
     */
    public String getAssignee() {
        return assignee;
    }

    /**
     * Sets the assignee of the task.
     *
     * @param assignee The assignee of the task.
     */
    public void setAssignee(String assignee) {
        this.assignee = assignee;
    }

    /**
     * Returns a string representation of the task.
     *
     * @return A string representation of the task.
     */
    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                ", assignee='" + assignee + '\'' +
                '}';
    }
}
