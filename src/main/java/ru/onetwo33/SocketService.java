package ru.onetwo33;

import ru.onetwo33.domain.HttpResponse;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Deque;
import java.util.LinkedList;

public class SocketService implements Closeable {

    private final Socket socket;

    public SocketService(Socket socket) {
        this.socket = socket;
    }

    public Deque<String> readRequest() {
        try {
            BufferedReader input = new BufferedReader(new InputStreamReader(
                    socket.getInputStream(), StandardCharsets.UTF_8));

            while (!input.ready()) ;

            Deque<String> request = new LinkedList<>();
            while (input.ready()) {
                String line = input.readLine();
                System.out.println(line);
                request.add(line);
            }
            return request;
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    public void sendResponse(HttpResponse httpResponse) {
        try {
            PrintWriter output = new PrintWriter(socket.getOutputStream());

            output.write(httpResponse.getStatusCode());
            for (String pair : httpResponse.getHeaders().values()) {
                output.write(pair);
            }
            output.write(httpResponse.getBody());
            output.flush();
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public void close() throws IOException {
        if (!socket.isClosed()) {
            socket.close();
        }
    }
}
