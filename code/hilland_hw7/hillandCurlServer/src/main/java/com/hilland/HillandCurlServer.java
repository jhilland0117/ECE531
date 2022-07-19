package com.hilland;

import com.hilland.domain.Console;
import fi.iki.elonen.NanoHTTPD;
import fi.iki.elonen.NanoHTTPD.IHTTPSession;
import fi.iki.elonen.NanoHTTPD.Response;
import java.io.IOException;
import java.util.List;
import com.google.gson.Gson;
import java.util.HashMap;

import static fi.iki.elonen.NanoHTTPD.newFixedLengthResponse;

/**
 * Base application class for setting up NanoHTTPD server.
 *
 * @author jhilland
 */
public class HillandCurlServer extends NanoHTTPD {

    private JDBCConnection connection;

    public HillandCurlServer() throws IOException {
        super(8080);
        connection = new JDBCConnection();
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
    public Response serve(IHTTPSession session) {
        if (session.getMethod() == Method.GET) {

            List<Console> consoles = connection.getConsoles();
            Gson gson = new Gson();
            String jsonResp = gson.toJson(consoles);
            return newFixedLengthResponse(jsonResp);
        } else if (session.getMethod() == Method.POST) {
            System.out.println("received a post");
            try {
                session.parseBody(new HashMap<>());
                String requestBody = session.getQueryParameterString();
                return newFixedLengthResponse("Request body = " + requestBody);
            } catch (IOException | ResponseException e) {
                return failedAttempt();
            }
        } else if (session.getMethod() == Method.PUT) {
            System.out.println("received a put");
        } else if (session.getMethod() == Method.DELETE) {
            System.out.println("received a delete");
        }
        
        return failedAttempt();
    }

    private Response failedAttempt() {
        // all attempts have failed
        return newFixedLengthResponse(Response.Status.NOT_FOUND, MIME_PLAINTEXT,
                "The requested resource does not exist");
    }
}
