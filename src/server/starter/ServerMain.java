package server.starter;

import server.controller.ServerController;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class ServerMain {

    /**
     * Starts the server. Takes no parameters.
     * @param args
     */
    public static void main(String[] args) {
        try {
            new ServerMain().startRegistry();
            Naming.rebind(ServerController.SERVER_REGISTRY_NAMESPACE, new ServerController());
            System.out.println("Server started");
        } catch (RemoteException | MalformedURLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Finds or starts the RMI registry.
     * @throws RemoteException If there was an RMI issue.
     */
    private void startRegistry() throws RemoteException {
        try {
            LocateRegistry.getRegistry().list();
        } catch (RemoteException re) {
            LocateRegistry.createRegistry(Registry.REGISTRY_PORT);
        }
    }
}
