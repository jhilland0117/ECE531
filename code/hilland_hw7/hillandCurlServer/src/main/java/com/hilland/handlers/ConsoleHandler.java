package com.hilland.handlers;

import fi.iki.elonen.NanoHTTPD.Response;
import fi.iki.elonen.router.RouterNanoHTTPD.DefaultHandler;

import static fi.iki.elonen.NanoHTTPD.MIME_PLAINTEXT;

/**
 *
 * @author jhilland
 */
public class ConsoleHandler extends DefaultHandler {
    @Override
    public String getText() {
        return "UserA, UserB, UserC";
    }

    @Override
    public String getMimeType() {
        return MIME_PLAINTEXT;
    }

    @Override
    public Response.IStatus getStatus() {
        return Response.Status.OK;
    }
}
