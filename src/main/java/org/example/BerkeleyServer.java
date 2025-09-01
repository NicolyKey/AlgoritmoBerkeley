package src.main.java.org.example;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class BerkeleyServer {
    private static int porta = 8080;
    private static int clientes = 5;

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(porta);
        System.out.println("Servidor de Berkeley iniciado na porta " + porta);

        List<Socket> clients = new ArrayList<>();
        List<Long> offsets = new ArrayList<>();

        for (int i = 0; i < clientes; i++) {
            Socket client = serverSocket.accept();
            clients.add(client);
            System.out.println("Cliente conectado: " + client.getInetAddress());
        }

        long serverTime = System.currentTimeMillis();
        System.out.println("Hora do servidor: " + serverTime);

        for (Socket client : clients) {
            try {
                DataInputStream in = new DataInputStream(client.getInputStream());
                DataOutputStream out = new DataOutputStream(client.getOutputStream());

                out.writeLong(serverTime);

                long clientTime = in.readLong();
                long offset = clientTime - serverTime;
                offsets.add(offset);

                System.out.println("Hora cliente " + client.getInetAddress() + ": " + clientTime + " (offset=" + offset + ")");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        long soma = 0;
        for (long offset : offsets) soma += offset;
        long media = soma / (offsets.size() + 1);
        System.out.println("Média de ajuste: " + media);

        for (int i = 0; i < clients.size(); i++) {
            try {
                DataOutputStream out = new DataOutputStream(clients.get(i).getOutputStream());
                long ajuste = media - offsets.get(i);
                out.writeLong(ajuste);
                System.out.println("Enviado ajuste para " + clients.get(i).getInetAddress() + ": " + ajuste);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        for (Socket client : clients) client.close();
        serverSocket.close();
    }
}
