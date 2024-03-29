package com.gymfox.httpserver;

import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

import static com.gymfox.httpserver.HTTPRequest.checkHTTPVersion;
import static com.gymfox.httpserver.HTTPRequest.validateRequestHttpVersion;
import static com.gymfox.httpserver.HTTPServerExceptions.InvalidHTTPVersionException;
import static com.gymfox.httpserver.HTTPServerExceptions.InvalidPartsHTTPVersionException;

public class HTTPRequestTest {
    @Test
    public void getRequestParametersTest() throws IOException {
        HTTPRequest request = new HTTPRequest( "get", "/index.html", "HTTP/1.1", "localhost");

        Assert.assertEquals("GET", request.getRequestMethod());
        Assert.assertEquals("/index.html", request.getRequestURI());
        Assert.assertEquals("HTTP/1.1", request.getRequestHTTPVersion());
        Assert.assertEquals("localhost", request.getHost());
    }

    @Test
    public void processingRequestTest() throws IOException {
        Assert.assertEquals("/", new HTTPRequest( "get", "/", "http/1.1", "localhost").getRequestURI());
        Assert.assertEquals("/findex.html", new HTTPRequest( "get", "/findex.html", "http/1.1", "localhost")
                .getRequestURI());
        Assert.assertEquals("/style.css", new HTTPRequest( "get", "/style.css", "http/1.1", "localhost")
                .getRequestURI());
        Assert.assertEquals("/test/", new HTTPRequest( "get", "/test/", "http/1.1", "localhost").getRequestURI());
        Assert.assertEquals("/test/style.css", new HTTPRequest( "get", "/test/style.css", "http/1.1", "localhost")
                .getRequestURI());
    }

    @Test
    public void validateHTTPVersionTest() throws InvalidHTTPVersionException, InvalidPartsHTTPVersionException {
        validateRequestHttpVersion("HTTP/0.9");
        validateRequestHttpVersion("HTTP/1.0");
        validateRequestHttpVersion("HTTP/1.1");
        validateRequestHttpVersion("HTTP/2.0");
    }

    @Test
    public void checkHTTPVersionTest() throws InvalidHTTPVersionException, InvalidPartsHTTPVersionException {
        Assert.assertEquals("HTTP/0.9", checkHTTPVersion("http/0.9"));
        Assert.assertEquals("HTTP/1.0", checkHTTPVersion("hTTp/1.0"));
        Assert.assertEquals("HTTP/1.1", checkHTTPVersion("Http/1.1"));
        Assert.assertEquals("HTTP/2.0", checkHTTPVersion("HtTp/2.0"));
    }

    @Test
    public void requestStringRepresentaion() throws IOException {
        Assert.assertEquals("GET / HTTP/1.1\nHost: localhost",
                (new HTTPRequest("get", "/", "http/1.1", "localhost")).toString());
    }

    @Test ( expected = InvalidHTTPVersionException.class )
    public void validateHTTPNameTest() throws InvalidHTTPVersionException, InvalidPartsHTTPVersionException {
        validateRequestHttpVersion("HTSP/1.1");
    }

    @Test ( expected = InvalidHTTPVersionException.class )
    public void validateHTTPLessVersionTest() throws InvalidHTTPVersionException, InvalidPartsHTTPVersionException {
        validateRequestHttpVersion("HTTP/0.5");
    }

    @Test ( expected = InvalidHTTPVersionException.class )
    public void validateHTTPMoreVersionTest() throws InvalidHTTPVersionException, InvalidPartsHTTPVersionException {
        validateRequestHttpVersion("HTTP/2.5");
    }

    @Test ( expected = InvalidPartsHTTPVersionException.class )
    public void validatePartsTest() throws InvalidHTTPVersionException, InvalidPartsHTTPVersionException {
        validateRequestHttpVersion("HTTP/1.1/i/want/to/break/free");
    }

    @Test ( expected = InvalidPartsHTTPVersionException.class )
    public void validateNonePartsTest() throws InvalidHTTPVersionException, InvalidPartsHTTPVersionException {
        validateRequestHttpVersion("");
    }
}
