package calcolomediaclient;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;
import java.util.Scanner;

public class CalcoloMediaClient {
    public static final int SERVER_PORT=54321;
    
    public static void main(String[] args) {
        DatagramSocket socket = null;

        try {
            // Creo un socket UDP con timeout a 5 secondi
            socket = new DatagramSocket();
            socket.setSoTimeout(5000);

            // Ottengo il messaggio da inviare
            Scanner scanner = new Scanner(System.in);
            
            while(true) {
                System.out.println("Scrivi i valori e i pesi per il calcolo della media divisi da spazi");
                System.out.println("Esempio: '7 100 4.5 30' corrisponderà ad un 7 al 100% e un 4.5 al 30%");
                String message = scanner.nextLine();

                // Indirizzo IP e porta del server
                InetAddress serverAddress = InetAddress.getByName("localhost");
                int serverPort = SERVER_PORT;

                // Creo un pacchetto Datagram per inviare i dati al server
                byte[] sendData = message.getBytes();
                DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, serverAddress, serverPort);

                // Invio il pacchetto al server
                socket.send(sendPacket);

                // Creo un pacchetto Datagram per ricevere la risposta dal server
                byte[] receiveData = new byte[1024];
                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);

                // Ricevo la risposta dal server
                socket.receive(receivePacket);

                // Ottengo la risposta dal pacchetto ricevuto
                String response = new String(receivePacket.getData(), 0, receivePacket.getLength());
                System.out.println("La media calcolata dal server è: " + response);
            }
        } catch (SocketTimeoutException ste) {
            System.err.println("Timeout scaduto. Nessuna risposta dal server.");
            return;
        } catch (IOException e) {
            System.out.println("ERRORE: " + e.getMessage());
        } finally {
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
        }
    }
}
