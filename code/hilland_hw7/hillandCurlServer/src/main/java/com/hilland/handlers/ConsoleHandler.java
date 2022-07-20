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
//        String params = urlParams.get("storeId");
//        if (params != null) {
//            Console console = connection.getConsole(params);
//            Gson gson = new Gson();
//            String jsonResp = gson.toJson(console);
//            return newFixedLengthResponse(jsonResp);
//        } else {

        List<Console> consoles = connection.getConsoles();
        Gson gson = new Gson();
        String jsonResp = gson.toJson(consoles);
        return newFixedLengthResponse(jsonResp);
//        }
    }

    @Override
    public Response delete(UriResource uriResource, Map<String, String> urlParams, IHTTPSession session) {
        return super.delete(uriResource, urlParams, session); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/OverriddenMethodBody
    }

    @Override
    public Response put(UriResource uriResource, Map<String, String> urlParams, IHTTPSession session) {
        return super.put(uriResource, urlParams, session); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/OverriddenMethodBody
    }

    @Override
    public Response post(
            UriResource uriResource, 
            Map<String, String> urlParams, 
            IHTTPSession session) {
                
        String baseUri = uriResource.getUri();
        System.out.println("here " + baseUri);
        for (String value : urlParams.values()) {
            System.out.println("shoot " + value);
        }
        JDBCConnection connection = new JDBCConnection();
        
        return newFixedLengthResponse("post was successful");
    }

}
