package org.aloha.httpserver;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;

import lombok.Data;

/**
 * represent a http request
 * 
 * @author aloha
 * @Date:2017年5月28日 下午10:39:58
 */
@Data
public class Request {

    private String uri;
    private InputStream inputStream;

    public Request(InputStream is) {
        this.inputStream = is;
    }

    public void parse() throws IOException {
        StringBuffer sb = new StringBuffer(2048);
        byte[] buffer = new byte[2048];
        int length = inputStream.read(buffer);
        if (length == -1) {
            throw new IOException("requst has no content, at:" + LocalDateTime.now());
        }
        for (int j = 0; j < length; ++j) {
            sb.append((char) buffer[j]);
        }

        uri = parseURI(sb.toString());

        System.out.println("requst uri:" + uri);

    }

    public String parseURI(String requestString) {
        int firstIndex, secondIndex;
        firstIndex = requestString.indexOf(" ");
        if (firstIndex != -1) {
            secondIndex = requestString.indexOf(" ", firstIndex + 1);
            if (secondIndex != -1) {
                return requestString.substring(firstIndex + 1, secondIndex);
            }
        }
        return null;
    }

}
