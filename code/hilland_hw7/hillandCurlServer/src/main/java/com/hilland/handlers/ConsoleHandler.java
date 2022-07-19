package com.hilland.handlers;

import com.google.gson.Gson;
import com.hilland.JDBCConnection;
import com.hilland.domain.Console;
import fi.iki.elonen.NanoHTTPD.IHTTPSession;
import fi.iki.elonen.NanoHTTPD.Response;
import fi.iki.elonen.router.RouterNanoHTTPD.GeneralHandler;
import fi.iki.elonen.router.RouterNanoHTTPD.UriResource;
import java.util.Map;
import java.util.List;

import static fi.iki.elonen.NanoHTTPD.newFixedLengthResponse;

/**
 *
 * @author jhilland
 */
public class ConsoleHandler extends GeneralHandler {

    @Override
    public Response get(
            UriResource uriResource,
            Map<String, String> urlParams,
            IHTTPSession session) {

        JDBCConnection connection = new JDBCConnection();

        String params = urlParams.get("id");
        if (params != null) {
            Console console = connection.getConsole(params);
            Gson gson = new Gson();
            String jsonResp = gson.toJson(console);
            return newFixedLengthResponse(jsonResp);
        } else {

            List<Console> consoles = connection.getConsoles();
            Gson gson = new Gson();
            String jsonResp = gson.toJson(consoles);
            return newFixedLengthResponse(jsonResp);
        }
    }
}
