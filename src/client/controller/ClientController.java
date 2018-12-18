package client.controller;

import server.controller.ServerController;
import shared.Server;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Scanner;

public class ClientController {

    private Server server;
    private boolean connected = true;
    public Scanner sc = new Scanner(System.in);
    volatile private String me;
    private String PROMPT = ">";

    /**
     * Will start the connection to the server and hold the connection running.
     */
    public void start(){
        try {
            Registry registry = LocateRegistry.getRegistry(null);
            server = (Server) registry.lookup(ServerController.SERVER_REGISTRY_NAMESPACE);
            System.out.println("Rock paper scissors");

            // Program loop
            while(connected){

            }
        } catch (RemoteException | NotBoundException e) {
            e.printStackTrace();
        }
    }
}
