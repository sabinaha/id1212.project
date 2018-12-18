package client.starter;

import client.controller.ClientController;
import client.net.ServerHandler;


public class ClientMain {
    public static void main(String[] args) {
        new ClientController().start();
    }
}
