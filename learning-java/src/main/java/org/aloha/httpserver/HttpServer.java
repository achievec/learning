package org.aloha.httpserver;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDateTime;

import lombok.Data;

/**
 * a simple http server
 * 
 * @author aloha
 * @Date:2017年5月28日 下午10:40:09
 */
@Data
public class HttpServer {

    public static final String WEB_ROOT = System.getProperty("user.dir") + File.separator + "webroot";

    public static final String SHUTDOWN_URI = "/SHUTDOWN";

    private static final int PORT = 8888;

    private static final String HOST = "localhost";

    private boolean shutdown = false;

    public static void main(String[] args) {
        HttpServer server = new HttpServer();
        server.await();
    }

    public void await() {
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(PORT, 1, InetAddress.getByName(HOST));
        } catch (IOException e) {

            e.printStackTrace();
            System.exit(1);
        }
        while (!shutdown) {

            try (Socket socket = serverSocket.accept();
                    InputStream inputStream = socket.getInputStream();
                    OutputStream outputStream = socket.getOutputStream();) {

                System.out.println("request accepted, at:" + LocalDateTime.now());

                Request request = new Request(inputStream);
                request.parse();

                Response response = new Response(outputStream, request);
                response.sendStaticResource();

                shutdown = request.getUri().equals(SHUTDOWN_URI);
                if (shutdown) {
                    System.out.println("shut down server, at:" + LocalDateTime.now());
                }

            } catch (IOException e) {
                e.printStackTrace();
                continue;
            }
        }

    }
}
