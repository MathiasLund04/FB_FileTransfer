package org.example;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class FileClient {
    private static final String HOST = "localhost";
    private static final int PORT = 5000;

    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in);
             Socket socket = new Socket(HOST, PORT)) {
            System.out.println("Forbundet til serveren.");
            try (PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                 BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
                System.out.print("Indtast forespørgsel (fx GET|Text): ");
                String request = scanner.nextLine();
                int separatorIndex = request.indexOf('|');
                if (separatorIndex < 0 || separatorIndex == request.length() - 1) {
                    System.err.println("Ugyldig forespørgsel. Brug formatet GET|filnavn.");
                    return;
                }
                out.println(request);
                String response = in.readLine();
                if (response != null && response.startsWith("ERROR|")) {
                    System.err.println("Fejl fra serveren: " + response.substring(6));
                } else if ("OK".equals(response)) {
                    String fileName = request.substring(separatorIndex + 1);
                    try (ByteArrayOutputStream buffer = new ByteArrayOutputStream()) {
                        socket.getInputStream().transferTo(buffer);
                        try (FileOutputStream fileOut = new FileOutputStream(fileName)) {
                            buffer.writeTo(fileOut);
                        }
                    }
                    System.out.println("Filen er gemt som: " + fileName);
                } else {
                    System.out.println("Server svarer: " + response);
                }
            }

        } catch (IOException e) {
            System.err.println("Kunne ikke forbinde til serveren: " + e.getMessage());
        }
    }
}
