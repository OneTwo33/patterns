package ru.onetwo33;

import ru.onetwo33.domain.HttpRequest;
import ru.onetwo33.domain.HttpResponse;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Deque;

import static ru.onetwo33.Config.WWW;

public class RequestHandler implements Runnable {

    private final SocketService socketService;
    private final RequestParser requestParser;

    public RequestHandler(SocketService socketService, RequestParser requestParser) {
        this.socketService = socketService;
        this.requestParser = requestParser;
    }

    @Override
    public void run() {

        Deque<String> rawRequest = socketService.readRequest();
        HttpRequest httpRequest = requestParser.parseRequest(rawRequest);
        HttpResponse httpResponse = new HttpResponse();

        if (httpRequest.getMethod().equals("GET")) {
            Path path = Paths.get(WWW, httpRequest.getUrl());

            if (!Files.exists(path)) {
                httpResponse.setStatusCode("HTTP/1.1 404 NOT_FOUND\n");
                httpResponse.setHeaders("Content-Type: ", "text/html; charset=utf-8\n");
                httpResponse.setBody("\n<h1>Файл не найден!</h1>");
                socketService.sendResponse(httpResponse);
                return;
            }

            httpResponse.setStatusCode("HTTP/1.1 200 OK\n");
            httpResponse.setHeaders("Content-Type: ", "text/html; charset=utf-8\n");
            try {
                StringBuilder body = new StringBuilder();
                body.append("\n");
                Files.readAllLines(path).forEach(body::append);
                httpResponse.setBody(body.toString());
            } catch (IOException e) {
                throw new IllegalStateException(e);
            }
            socketService.sendResponse(httpResponse);
        } else {
            httpResponse.setStatusCode("HTTP/1.1 405 METHOD_NOT_ALLOWED\n");
            httpResponse.setHeaders("Content-Type: ", "text/html; charset=utf-8\n");
            httpResponse.setBody("\n<h1>Метод не поддерживается!</h1>");
            socketService.sendResponse(httpResponse);
            return;
        }
        try {
            socketService.close();
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
        System.out.println("Client disconnected!");
    }
}
