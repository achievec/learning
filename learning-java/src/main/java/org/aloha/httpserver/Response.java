package org.aloha.httpserver;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;

import org.apache.commons.io.IOUtils;

import lombok.Data;

/**
 * represent a http response
 * 
 * @author aloha
 * @Date:2017年5月28日 下午10:41:31
 */
@Data
public class Response {
    private Request request;
    private OutputStream outputStream;

    public Response(OutputStream os, Request request) {
        this.outputStream = os;
        this.request = request;
    }

    public void sendStaticResource() throws IOException {

        File file = new File(HttpServer.WEB_ROOT, request.getUri());
        if (file.exists()) {
            String content = IOUtils.toString(new FileInputStream(file), Charset.forName("utf-8"));
            StringBuffer sb = new StringBuffer(2048);
            sb.append("HTTP1.1 200 OK\r\n").append("Content-Type:text/html\r\n").append("ContentLength:23\r\n")
                    .append("\r\n").append(content);
            outputStream.write(sb.toString().getBytes());
        } else {
            StringBuffer sb = new StringBuffer(2048);
            sb.append("HTTP1.1 404 FILE NOT FOUND\r\n").append("Content-Type:text/html\r\n")
                    .append("ContentLength:23\r\n").append("\r\n")
                    .append("<h1>file '" + file.getPath() + "' not found</h1>");
            outputStream.write(sb.toString().getBytes());
        }
    }
}