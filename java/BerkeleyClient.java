package java;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class BerkeleyClient {
    private static BerkeleyServer berkeleyServer;

    public static void main(String[] args) {
        try (Socket socket = new Socket(berkeleyServer.getEndereco(), berkeleyServer.getPorta())) {
            DataInputStream in = new DataInputStream(socket.getInputStream());
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());

            long serverTime = in.readLong();

            long localTime = System.currentTimeMillis() + (long)(Math.random() * 5000 - 2500);
            out.writeLong(localTime);
            System.out.println("Hora local enviada: " + localTime);

            long ajuste = in.readLong();
            localTime += ajuste;
            System.out.println("Ajuste recebido: " + ajuste);
            System.out.println("Nova hora local: " + localTime);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
