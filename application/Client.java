package application;

import remote.StubFactory;

import java.util.Scanner;

/**
 * Client application to interact with the Task Manager.
 * It can be used to create, assign, update status and list tasks assigned to the give user.
 * It can be run with the server address as argument to interact with the remote Task Manager.
 * If no argument is provided, it will run the default sample behaviour of the application.
 */
public class Client {

    /**
     * Main method to run the client application.
     * @param args  Server address to connect to.
     * Usage Client <host:port>
     * Commands:
     * CREATE title description
     *  - To create a new task with title and description.
     *  - Returns the task id.
     *  - Example: CREATE Task1 Description1
     *  - Output: Task with 1 created in OPEN status.
     *ASSIGN taskId assignee
     *  - To assign a task to the given assignee.
     *  - Example: ASSIGN 1 Assignee1
     *  - Output: Task 1 assigned to Assignee1
     *  - Throws exception if task not found.
     *UPDATE_STATUS taskId status
     *  - To update the status of the task.
     *  - Example: UPDATE_STATUS 1 IN_PROGRESS/OPEN/CLOSED
     *  - Output: Task 1 status updated to IN_PROGRESS
     *  - Throws exception if task not found.
     *GET_ASSIGNED_TASKS username
     *  - To get the list of tasks assigned to the given user.
     *  - Example: GET_ASSIGNED_TASKS Assignee1
     *  - Output: Assigned tasks: [Task 1]
     *QUIT
     *  - To quit the application.
     */
    public static void main(String[] args) {

        if(args.length != 1) {
            System.out.println("Usage: Client <host:port>");
        }
        else {
            Scanner scanner = new Scanner((System.in));
            TasksManager tasksManager = StubFactory.create(TasksManager.class, args[0]);
            String input = "";
            System.out.println("Task Manager is available for your project. Run commands create, assign, update, list or quit to perform action on the task board.");
            while(true) {
                System.out.println("> ");
                input = scanner.nextLine();
                String[] command = input.split(" ");
                try {
                    switch (command[0].toUpperCase()) {
                        case "CREATE":
                            int taskId = tasksManager.createTask(command[1], command[2]);
                            System.out.println("Task with "+ taskId + " created.");
                            break;
                        case "ASSIGN":
                            tasksManager.assignTask(Integer.parseInt(command[1]), command[2]);
                            break;
                        case "UPDATE_STATUS":
                            tasksManager.updateStatus(Integer.parseInt(command[1]), TaskStatus.getStatus(command[2]));
                            break;
                        case "GET_ASSIGNED_TASKS":
                            System.out.println("Assigned tasks: " + tasksManager.getAssignedTasks(command[1]));
                            break;
                        case "QUIT":
                            System.out.println("Quitting...");
                            return;
                        default:
                            System.out.println("Usage: CREATE <title> <description>\n<ASSIGN <taskId> <assignee>" +
                                    "\nUPDATE_STATUS <taskId> <status>\nGET_ASSIGNED_TASKS <username>");
                    }
                } catch (Exception e) {
                    System.out.println("Exception while executing command: " + input + " "+ e.getMessage());
                }
            }
        }
    }
}
