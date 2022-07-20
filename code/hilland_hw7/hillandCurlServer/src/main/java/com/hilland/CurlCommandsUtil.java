package com.hilland;

import com.google.gson.Gson;
import com.hilland.domain.Console;
import fi.iki.elonen.NanoHTTPD;
import static fi.iki.elonen.NanoHTTPD.MIME_PLAINTEXT;
import static fi.iki.elonen.NanoHTTPD.newFixedLengthResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author jhilland
 */
public final class CurlCommandsUtil {

    private CurlCommandsUtil() {
    }

    public static NanoHTTPD.Response performGet(JDBCConnection connection, NanoHTTPD.IHTTPSession session) {
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
    }

    public static NanoHTTPD.Response performPost(JDBCConnection connection, NanoHTTPD.IHTTPSession session) {
        try {
            session.parseBody(new HashMap<>());
            String requestBody = session.getQueryParameterString();
            String result = connection.addConsole(requestBody);
            return newFixedLengthResponse(result);
        } catch (IOException | NanoHTTPD.ResponseException e) {
            return failedAttempt();
        }
    }

    public static NanoHTTPD.Response performDelete(JDBCConnection connection, NanoHTTPD.IHTTPSession session) {
        String param = getIndex(session.getUri());
        String result = connection.deleteConsole(param);
        return newFixedLengthResponse(result);
    }

    private static NanoHTTPD.Response failedAttempt() {
        return newFixedLengthResponse(NanoHTTPD.Response.Status.NOT_FOUND, MIME_PLAINTEXT,
                "The requested resource does not exist");
    }

    private static String getIndex(String param) {
        return param.replaceAll("[^0-9]", "");
    }
}
