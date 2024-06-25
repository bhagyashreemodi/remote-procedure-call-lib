package application;

import remote.RemoteObjectException;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Task Manager implementation to manage the tasks.
 */
public class TasksManagerExecutor implements TasksManager {

    /**
     * Incrementer to generate the task id.
     */
    private int taskIdIncrementer;

    /**
     * Map to store the tasks.
     */
    private HashMap<Integer, Task> tasks;

    public TasksManagerExecutor() {
        taskIdIncrementer = 0;
        tasks = new HashMap<>();
    }


    @Override
    public int createTask(String title, String description) throws RemoteObjectException {
        Task task = new Task(++taskIdIncrementer, title, description, TaskStatus.OPEN, null);
        tasks.put(taskIdIncrementer, task);
        return taskIdIncrementer;
    }

    @Override
    public void assignTask(int id, String assignee) throws RemoteObjectException, TaskNotFoundException {
        Task task = tasks.get(id);
        if (task == null) {
            throw new TaskNotFoundException("Task not found");
        }
        task.setAssignee(assignee);
    }

    @Override
    public void updateStatus(int id, TaskStatus status) throws RemoteObjectException, TaskNotFoundException {
        Task task = tasks.get(id);
        if (task == null) {
            throw new TaskNotFoundException("Task not found");
        }
        task.setStatus(status);
    }

    @Override
    public List<Task> getAssignedTasks(String assignee) throws RemoteObjectException {
        return tasks.values().stream().filter(task -> assignee.equals(task.getAssignee())).toList();
    }
}