package com.gymfox.httpserver;

import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

final class HTTPServerUtils {
    public static final int MIN_SYSTEM_PORT_VALUE = 0;
    public static final int MAX_SYSTEM_PORT_VALUE = 65536;
    public static final List<Double> VALID_HTTP_VERSIONS = Arrays.asList(0.9, 1.0, 1.1, 2.0);
    static final String INDEX_HTML = "index.html";
    static final File CONFIG_FILE = new File("http.conf");

    private HTTPServerUtils() {}

    public static class FileIsEmptyException extends Exception {
        public FileIsEmptyException(String errorMessage) {
            super(errorMessage);
        }
    }

    public static class InvalidPortException extends Exception {
        public InvalidPortException(String errorMessage) {
            super(errorMessage);
        }
    }

    public static class InvalidPathToCurrentFileException extends Exception {
        public InvalidPathToCurrentFileException(String errorMessage) {
            super(errorMessage);
        }
    }

    public static class NotAllowedMethodException extends Exception {
        public NotAllowedMethodException(String errorMessage) {
            super(errorMessage);
        }
    }

    public static class InvalidHttpVersionException extends Exception {
        public InvalidHttpVersionException(String errorMessage) {
            super(errorMessage);
        }
    }

    public static class HttpServerIsRunningException extends Exception {
        public HttpServerIsRunningException(String errorMessage) {
            super(errorMessage);
        }
    }

    public static class InvalidPartsHTTPVersionException extends Throwable {
        public InvalidPartsHTTPVersionException(String errorMessage) {
            super(errorMessage);
        }
    }

    public static void startServer(HTTPServer httpServer) throws InterruptedException {
        Runnable r = () -> {
            try {
                httpServer.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        };

        ExecutorService t = Executors.newFixedThreadPool(1);
        t.execute(r);
        Thread.sleep(15000);
    }

    public static void closeSocket(Socket clientSocket) {
        try {
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static File validateIsNotEmpty(File configFile) throws FileIsEmptyException {
        if ( configFile.length() == 0 ) {
            throw new FileIsEmptyException("File doesn't have any parameters.");
        }
        return configFile;
    }

    static void validatePath(File file) throws InvalidPathToCurrentFileException {
        if ( !file.exists() ) {
            throw new InvalidPathToCurrentFileException(String.format("File \"%s\" isn't exist", file.getName()));
        }
    }

    static void validatePort(int port) throws InvalidPortException {
        if ( port < MIN_SYSTEM_PORT_VALUE || port > MAX_SYSTEM_PORT_VALUE ) {
            throw new InvalidPortException(String.format("%d is invalid port. Value beetwen %d and %d is expected",
                        port, MIN_SYSTEM_PORT_VALUE, MAX_SYSTEM_PORT_VALUE));
        }
    }

    static String checkRequestMethod(String requestMethod) throws NotAllowedMethodException {
        String newRequestMethod = requestMethod.toUpperCase();
        validateRequestMethod(newRequestMethod);

        return newRequestMethod;
    }

    static void validateRequestMethod(String requestMethod) throws NotAllowedMethodException {
        if ( !requestMethod.equals("GET") ) {
            throw new NotAllowedMethodException("405 Method Not Allowed");
        }
    }

    static String checkHttpVersion(String requestHttpVersion) throws InvalidHttpVersionException,
            InvalidPartsHTTPVersionException {
        String newRequestHttpVersion = requestHttpVersion.toUpperCase();
        validateRequestHttpVersion(newRequestHttpVersion);

        return newRequestHttpVersion;
    }

    static void validateRequestHttpVersion(String requestHttpVersion) throws InvalidHttpVersionException,
            InvalidPartsHTTPVersionException {
        String[] httpVersionParts = requestHttpVersion.split("/");
        validateParts(httpVersionParts);
        double httpVersion = Double.parseDouble(httpVersionParts[1]);

        if ( !httpVersionParts[0].equals("HTTP") ) {
            throw new InvalidHttpVersionException("Invalid protocol name");
        }

        if ( !VALID_HTTP_VERSIONS.contains(httpVersion) ) {
            throw new InvalidHttpVersionException("Invalid protocol version");
        }
    }

    static void validateParts(String[] httpVersionParts) throws InvalidPartsHTTPVersionException {
        if ( httpVersionParts.length > 2 ) {
            throw new InvalidPartsHTTPVersionException(String.format("Invalid HTTP version parts. Expected 2, but " +
                    "found %d", httpVersionParts.length));
        }
    }
}