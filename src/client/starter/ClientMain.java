package client.starter;

import client.controller.CommandController;
import client.net.ServerHandler;

public class ClientMain {
    public static void main(String[] args) {
        ServerHandler serverHandler = new ServerHandler();
        CommandController commandController = null;
    }
}
