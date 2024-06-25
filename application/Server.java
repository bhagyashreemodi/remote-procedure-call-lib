package application;

import remote.RemoteObjectException;
import remote.Service;

/**
 * Application to start the service on the provided port.
 * It will start the service on port 8888 by default, if no port argument is passed.
 * it accepts only 1 command line argument as port number.
 * Sever port
 */
public class Server {

    /**
     * Main method to start the service with the TaskManagerExecutor instance.
     * @param args Port number to start the service.
     * @throws RemoteObjectException If any exception occurs while starting the service.
     */
    public static void main(String[] args) throws RemoteObjectException {
        if (args.length > 1) {
            System.out.println("usage: Server <port>");
            return;
        }
        int port = args.length == 0 ? 8888 : Integer.parseInt(args[0]);
        TasksManager tasksManager = new TasksManagerExecutor();
        Service<TasksManager> service = new Service<>(TasksManager.class, tasksManager, port);
        service.start();
        System.out.println("Server started. Listening on port " + port);
    }


}
