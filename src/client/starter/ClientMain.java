package client.starter;

import client.controller.ClientController;
import client.net.ServerHandler;

import java.rmi.RemoteException;


public class ClientMain {
    public static void main(String[] args) {
        try {
            new ClientController().start();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
