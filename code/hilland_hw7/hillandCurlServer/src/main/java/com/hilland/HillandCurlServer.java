package com.hilland;

import com.hilland.domain.Console;
import fi.iki.elonen.NanoHTTPD;
import fi.iki.elonen.NanoHTTPD.IHTTPSession;
import fi.iki.elonen.NanoHTTPD.Response;
import java.io.IOException;
import java.util.List;
import com.google.gson.Gson;
import java.util.HashMap;

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

            String jsonResp = null;
            String param = getIndex(session.getUri());
            Gson gson = new Gson();

            if (param != null && !param.equals("")) {
                Console console = connection.getConsole(param);
                if (console == null) {
                    return failedAttempt();
                }
                jsonResp = gson.toJson(console);
            } else {
                List<Console> consoles = connection.getConsoles();
                if (consoles.isEmpty()) {
                    return failedAttempt();
                }
                jsonResp = gson.toJson(consoles);
            }
            
            return newFixedLengthResponse(jsonResp);

        } else if (session.getMethod() == Method.POST) {

            System.out.println("received a post");
            try {
                session.parseBody(new HashMap<>());
                String requestBody = session.getQueryParameterString();
                connection.addConsole(requestBody);
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

    private String getIndex(String param) {
        return param.replaceAll("[^0-9]", "");
    }

    private Response failedAttempt() {
        // all attempts have failed
        return newFixedLengthResponse(Response.Status.NOT_FOUND, MIME_PLAINTEXT,
                "The requested resource does not exist");
    }
}
