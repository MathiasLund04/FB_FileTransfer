package org.example;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileServer {
    private static final int PORT = 5000;
    private static final Path BASE_DIR = Path.of("testdata").toAbsolutePath().normalize();

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            while (true) {
                try (Socket socket = serverSocket.accept();
                     BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                     PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                     OutputStream fileOut = socket.getOutputStream()) {
                    String request = in.readLine();
                    handleRequest(request, out, fileOut);
                } catch (IOException e) {
                    System.err.println("Fejl ved håndtering af klientforbindelse: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.err.println("Kunne ikke starte server på port " + PORT + ": " + e.getMessage());
        }
    }

    private static void handleRequest(String request, PrintWriter out, OutputStream fileOut) throws IOException {
        if (request == null || !request.startsWith("GET|")) {
            out.println("ERROR|Ugyldig kommando");
            return;
        }

        String fileName = request.substring(4);
        if (fileName.isBlank()) {
            out.println("ERROR|Filnavn mangler");
            return;
        }

        Path requestedPath = BASE_DIR.resolve(fileName).normalize();
        if (!requestedPath.startsWith(BASE_DIR)) {
            out.println("ERROR|Ugyldigt filnavn");
            return;
        }

        if (!Files.exists(requestedPath)) {
            out.println("ERROR|Filen findes ikke");
            return;
        }

        out.println("OK");
        try (InputStream fileIn = Files.newInputStream(requestedPath)) {
            fileIn.transferTo(fileOut);
        }
    }
}
