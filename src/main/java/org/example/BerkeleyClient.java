package src.main.java.org.example;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

public class BerkeleyClient {

    public static void main(String[] args) {
        try (Socket socket = new Socket("localhost", 8080)) {
            DataInputStream in = new DataInputStream(socket.getInputStream());
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());

            long serverTime = in.readLong();
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss.SSS");
            System.out.println("Hora recebida do servidor: " + sdf.format(new Date(serverTime)));

            long localTime = System.currentTimeMillis() + (long)(Math.random() * 5000 - 2500);
            out.writeLong(localTime);

            String horaFormatada = sdf.format(new Date(localTime));
            System.out.println("Hora local enviada: " + horaFormatada);

            long diferencaInicial = localTime - serverTime;
            System.out.println("Diferença inicial: " + diferencaInicial + " ms");

            long ajuste = in.readLong();
            localTime += ajuste;

            String novaHoraFormatada = sdf.format(new Date(localTime));
            System.out.println("Ajuste recebido: " + ajuste + " ms");
            System.out.println("Nova hora local: " + novaHoraFormatada);

            long diferencaFinal = localTime - serverTime;
            System.out.println("Diferença final após ajuste: " + diferencaFinal + " ms");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
