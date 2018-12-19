package shared;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Client extends Remote {
    void displayInfo(Object object) throws RemoteException;
    void receiveResponse(Response response) throws RemoteException;
}
