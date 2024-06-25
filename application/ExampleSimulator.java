package application;

import remote.RemoteObjectException;
import remote.Service;
import remote.StubFactory;

/**
 * Example application to demonstrate the usage of the remote object.
 * It starts the server on port 8080 and 8888 and creates 3 clients to interact with the server.
 * It demonstrates the usage of the remote object to create, assign, update status and list tasks assigned to the give user.
 * It also demonstrates the usage of the remote object to interact with the server in parallel.
 * It also demonstrates the usage of the remote object to interact with the server on different ports.
 */
public class ExampleSimulator {

    /**
     * Main method to start the server on port 8080 and 8888 and create 3 clients to interact with the server.
     * @param args Command line arguments.
     */
    public static void main(String[] args) {
        System.out.println("This is an example application");

        Service<TasksManager> service1 = startServer(8080);
        Service<TasksManager> service2 = startServer(8888);

        if(service1 == null || service2 == null) {
            System.out.println("Error starting server. Exiting...");
            return;
        }
        // Creating client application to interact with server on port 8080
        Thread client1 = new Thread(ExampleSimulator::executeClient1);
        client1.start();

        // Creating client application to interact with server on port 8080 in parallel
        Thread client2 = new Thread(ExampleSimulator::executeClient2);
        client2.start();

        // Creating client application to interact with server on port 8888 in parallel
        Thread client3 = new Thread(ExampleSimulator::executeClient3);
        client3.start();


        try {
            client1.join();
            client2.join();
            client3.join();
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            System.out.println("Error waiting for threads to finish: " + e.getMessage());
        }

        System.out.println("All threads have finished. Exiting...");
        service1.stop();
        service2.stop();

    }

    /**
     * Method to execute the client application to interact with the server on port 8888.
     * It demonstrates the usage of the remote object to create, assign, update status and list tasks assigned to the give user.
     * It shows that the service maintains independent state for connections on different ports.
     * So, Client3 print statements should have empty entries for user1 and user2. And it should have the task assigned to user3.
     * It also demonstrates propagation of exception from remote object to the client.
     */
    private static void executeClient3() {
        try {
            TasksManager tasksManager = StubFactory.create(TasksManager.class, "localhost:8888");

            System.out.println("Client3 - Assigned Tasks: " + tasksManager.getAssignedTasks("user1"));
            System.out.println("Client3 - Assigned Tasks: " + tasksManager.getAssignedTasks("user2"));

            int taskId1 = tasksManager.createTask("Task 3", "Description 3");
            try {
                tasksManager.assignTask(2, "user1");
            } catch (TaskNotFoundException e) {
                System.out.println("Client3 - Error assigning task to user1 from remote object: " + e.getMessage());
            }
            tasksManager.assignTask(taskId1, "user3");
            tasksManager.updateStatus(taskId1, TaskStatus.CLOSED);


            System.out.println("Client3 - Assigned Tasks: " + tasksManager.getAssignedTasks("user3"));

        } catch (Exception e) {
            System.out.println("Client3 - " + e.getMessage());
        }
    }

    /**
     * Method to execute the client application to interact with the server on port 8080.
     * It demonstrates the usage of the remote object to create, assign, update status and list tasks assigned to the give user.
     * So, Client1 and Client2 print statements should have empty entries for user3. And it should have the task assigned to user1 and user2.
    */
    private static void executeClient1() {
        try {
            TasksManager tasksManager = StubFactory.create(TasksManager.class, "localhost:8080");
            int taskId1 = tasksManager.createTask("Task 1", "Description 1");
            tasksManager.assignTask(taskId1, "user1");
            tasksManager.updateStatus(taskId1, TaskStatus.IN_PROGRESS);

            System.out.println("Client1 - Assigned Tasks: " + tasksManager.getAssignedTasks("user1"));
            System.out.println("Client1 - Assigned Tasks: " + tasksManager.getAssignedTasks("user2"));
            System.out.println("Client1 - Assigned Tasks: " + tasksManager.getAssignedTasks("user3"));
        } catch (Exception e) {
            System.out.println("[Client1]: " + e.getMessage());
        }
    }

    /**
     * Method to execute the client application to interact with the server on port 8080 in parallel.
     * It demonstrates the usage of the remote object to create, assign, update status and list tasks assigned to the give user.
     * So, Client2 and Client1 print statements should have empty entries for user3. And it should have the task assigned to user1 and user2.
     */
    private static void executeClient2() {
        try {
            TasksManager tasksManager = StubFactory.create(TasksManager.class, "localhost:8080");
            int taskId2 = tasksManager.createTask("Task 2", "Description 2");
            tasksManager.assignTask(taskId2, "user2");
            System.out.println("Client2 - Assigned Tasks: " + tasksManager.getAssignedTasks("user1"));
            System.out.println("Client2 - Assigned Tasks: " + tasksManager.getAssignedTasks("user2"));
            System.out.println("Client2 - Assigned Tasks: " + tasksManager.getAssignedTasks("user3"));
        } catch (Exception e) {
            System.out.println("[Client2]: " + e.getMessage());
        }
    }

    /**
     * Method to start the service on the provided port.
     * @param port Port number to start the server.
     * @return Service instance to manage the server.
     */
    private static Service<TasksManager> startServer(int port) {
        try {
            TasksManager tasksManager = new TasksManagerExecutor();
            Service<TasksManager> service = new Service<>(TasksManager.class, tasksManager, port);
            service.start();
            return service;
        } catch (RemoteObjectException e) {
            System.out.println("Error starting server on port " + port + ": " + e.getMessage());
        }
        return null;
    }
}
