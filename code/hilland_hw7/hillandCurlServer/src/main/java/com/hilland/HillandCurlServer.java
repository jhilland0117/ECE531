package com.hilland;

import com.hilland.domain.Console;
import fi.iki.elonen.NanoHTTPD;
import fi.iki.elonen.NanoHTTPD.IHTTPSession;
import fi.iki.elonen.NanoHTTPD.Response;
import java.io.IOException;
import java.util.List;
import com.google.gson.Gson;
import com.hilland.handlers.ConsoleHandler;
import fi.iki.elonen.router.RouterNanoHTTPD;
import fi.iki.elonen.router.RouterNanoHTTPD.IndexHandler;
import java.util.HashMap;

/**
 * Base application class for setting up NanoHTTPD server.
 *
 * @author jhilland
 */
public class HillandCurlServer extends RouterNanoHTTPD {

    public HillandCurlServer() throws IOException {
        super(8080);
        addMappings();
        start(NanoHTTPD.SOCKET_READ_TIMEOUT, false);
    }

    public static void main(String[] args) {
        try {

            // create new instance of server
            new HillandCurlServer();
        } catch (IOException ioe) {
            System.err.println("Couldn't start server:\n" + ioe);
        }
    }
    
    @Override
    public void addMappings() {
        addRoute("/", IndexHandler.class);
        addRoute("/consoles", ConsoleHandler.class);
    }

    private Response failedAttempt() {
        // all attempts have failed
        return newFixedLengthResponse(Response.Status.NOT_FOUND, MIME_PLAINTEXT,
                "The requested resource does not exist");
    }
}
