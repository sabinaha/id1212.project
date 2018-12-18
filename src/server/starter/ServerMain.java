package server.starter;

import server.controller.ServerController;

public class ServerMain {
    private static final int SERVER_PORT = 54321;
    public static void main(String[] args) {
        new ServerController(SERVER_PORT);
    }
}
