package com.lloydcompiles.nhs.patient;

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;
import java.net.InetSocketAddress;

public class Main {
    public static void main(String[] args) throws IOException {
        // Create an HTTP server listening on localhost:8080
        HttpServer server = HttpServer.create(new InetSocketAddress("localhost", 8080), 0);

        // Register a handler for the root path "/"
        server.createContext("/", exchange -> {
            String response = "Hello";
            exchange.getResponseHeaders().set("Content-Type", "text/plain");
            exchange.sendResponseHeaders(200, response.getBytes().length);
            exchange.getResponseBody().write(response.getBytes());
            exchange.close();
        });

        // Start the server
        server.start();
        System.out.println("Server started on http://localhost:8080");
    }
}