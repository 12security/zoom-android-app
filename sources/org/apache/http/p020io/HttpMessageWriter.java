package org.apache.http.p020io;

import java.io.IOException;
import org.apache.http.HttpException;
import org.apache.http.HttpMessage;

/* renamed from: org.apache.http.io.HttpMessageWriter */
public interface HttpMessageWriter<T extends HttpMessage> {
    void write(T t) throws IOException, HttpException;
}
