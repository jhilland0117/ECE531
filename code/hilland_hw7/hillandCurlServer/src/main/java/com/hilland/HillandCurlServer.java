package com.hilland;

import fi.iki.elonen.NanoHTTPD;
import fi.iki.elonen.NanoHTTPD.IHTTPSession;
import fi.iki.elonen.NanoHTTPD.Response;
import static fi.iki.elonen.NanoHTTPD.newFixedLengthResponse;
import java.io.IOException;

/**
 * Base application class for setting up NanoHTTPD server.
 *
 * @author jhilland
 */
public class HillandCurlServer extends NanoHTTPD {

    public HillandCurlServer() throws IOException {
        super(8080);
        start(NanoHTTPD.SOCKET_READ_TIMEOUT, false);
    }

    public static void main(String[] args) {
        try {
            // connect to the database
            JDBCConnection connection = new JDBCConnection();
            
            // create new instance of server
            new HillandCurlServer();
        } catch (IOException ioe) {
            System.err.println("Couldn't start server:\n" + ioe);
        }
    }

    @Override
    public Response serve(IHTTPSession session) {
        if (session.getMethod() == Method.GET) {
            String parameter = session.getParms().get("itemId");
            System.out.println("HERE IT IS: " + parameter);
            return newFixedLengthResponse("Requested itemId = " + parameter);
        }
        return newFixedLengthResponse(Response.Status.NOT_FOUND, MIME_PLAINTEXT,
                "The requested resource does not exist");
    }
}
