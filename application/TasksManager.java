package application;

import remote.RemoteObjectException;

import java.util.List;

/**
 * Interface to manage the tasks.
 */
public interface TasksManager {

    int createTask(String title, String description) throws RemoteObjectException;

    void assignTask(int id, String assignee) throws RemoteObjectException, TaskNotFoundException;

    void updateStatus(int id, TaskStatus status) throws RemoteObjectException, TaskNotFoundException;

    List<Task> getAssignedTasks(String assignee) throws RemoteObjectException;
}
