package ru.onetwo33;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import static ru.onetwo33.Config.PORT;

public class WebServer {

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server started!");

            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("New client connected!");

                new Thread(new RequestHandler(new SocketService(socket), new RequestParser())).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
