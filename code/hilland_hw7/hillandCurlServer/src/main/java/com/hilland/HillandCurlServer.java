package com.hilland;

import fi.iki.elonen.NanoHTTPD;
import fi.iki.elonen.NanoHTTPD.IHTTPSession;
import fi.iki.elonen.NanoHTTPD.Response;
import java.io.IOException;

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
            return CurlCommandsUtil.performGet(connection, session);
        } else if (session.getMethod() == Method.POST) {
            return CurlCommandsUtil.performPost(connection, session);
        } else if (session.getMethod() == Method.PUT) {
            return CurlCommandsUtil.performPost(connection, session);
        } else if (session.getMethod() == Method.DELETE) {
            return CurlCommandsUtil.performDelete(connection, session);
        }

        return CurlCommandsUtil.failedAttempt();
    }
}
