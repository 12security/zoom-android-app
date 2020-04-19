package com.google.api.client.http;

import com.google.api.client.util.Preconditions;
import com.google.api.client.util.StreamingContent;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

public class MultipartContent extends AbstractHttpContent {
    static final String NEWLINE = "\r\n";
    private static final String TWO_DASHES = "--";
    private ArrayList<Part> parts = new ArrayList<>();

    public static final class Part {
        HttpContent content;
        HttpEncoding encoding;
        HttpHeaders headers;

        public Part() {
            this(null);
        }

        public Part(HttpContent httpContent) {
            this(null, httpContent);
        }

        public Part(HttpHeaders httpHeaders, HttpContent httpContent) {
            setHeaders(httpHeaders);
            setContent(httpContent);
        }

        public Part setContent(HttpContent httpContent) {
            this.content = httpContent;
            return this;
        }

        public HttpContent getContent() {
            return this.content;
        }

        public Part setHeaders(HttpHeaders httpHeaders) {
            this.headers = httpHeaders;
            return this;
        }

        public HttpHeaders getHeaders() {
            return this.headers;
        }

        public Part setEncoding(HttpEncoding httpEncoding) {
            this.encoding = httpEncoding;
            return this;
        }

        public HttpEncoding getEncoding() {
            return this.encoding;
        }
    }

    public MultipartContent() {
        super(new HttpMediaType("multipart/related").setParameter("boundary", "__END_OF_PART__"));
    }

    public void writeTo(OutputStream outputStream) throws IOException {
        StreamingContent streamingContent;
        long j;
        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream, getCharset());
        String boundary = getBoundary();
        Iterator it = this.parts.iterator();
        while (it.hasNext()) {
            Part part = (Part) it.next();
            HttpHeaders acceptEncoding = new HttpHeaders().setAcceptEncoding(null);
            if (part.headers != null) {
                acceptEncoding.fromHttpHeaders(part.headers);
            }
            acceptEncoding.setContentEncoding(null).setUserAgent(null).setContentType(null).setContentLength(null).set("Content-Transfer-Encoding", (Object) null);
            HttpContent httpContent = part.content;
            if (httpContent != 0) {
                acceptEncoding.set("Content-Transfer-Encoding", (Object) Arrays.asList(new String[]{"binary"}));
                acceptEncoding.setContentType(httpContent.getType());
                HttpEncoding httpEncoding = part.encoding;
                if (httpEncoding == null) {
                    j = httpContent.getLength();
                    streamingContent = httpContent;
                } else {
                    acceptEncoding.setContentEncoding(httpEncoding.getName());
                    StreamingContent httpEncodingStreamingContent = new HttpEncodingStreamingContent(httpContent, httpEncoding);
                    long computeLength = AbstractHttpContent.computeLength(httpContent);
                    streamingContent = httpEncodingStreamingContent;
                    j = computeLength;
                }
                if (j != -1) {
                    acceptEncoding.setContentLength(Long.valueOf(j));
                }
            } else {
                streamingContent = 0;
            }
            outputStreamWriter.write(TWO_DASHES);
            outputStreamWriter.write(boundary);
            outputStreamWriter.write(NEWLINE);
            HttpHeaders.serializeHeadersForMultipartRequests(acceptEncoding, null, null, outputStreamWriter);
            if (streamingContent != 0) {
                outputStreamWriter.write(NEWLINE);
                outputStreamWriter.flush();
                streamingContent.writeTo(outputStream);
            }
            outputStreamWriter.write(NEWLINE);
        }
        outputStreamWriter.write(TWO_DASHES);
        outputStreamWriter.write(boundary);
        outputStreamWriter.write(TWO_DASHES);
        outputStreamWriter.write(NEWLINE);
        outputStreamWriter.flush();
    }

    public boolean retrySupported() {
        Iterator it = this.parts.iterator();
        while (it.hasNext()) {
            if (!((Part) it.next()).content.retrySupported()) {
                return false;
            }
        }
        return true;
    }

    public MultipartContent setMediaType(HttpMediaType httpMediaType) {
        super.setMediaType(httpMediaType);
        return this;
    }

    public final Collection<Part> getParts() {
        return Collections.unmodifiableCollection(this.parts);
    }

    public MultipartContent addPart(Part part) {
        this.parts.add(Preconditions.checkNotNull(part));
        return this;
    }

    public MultipartContent setParts(Collection<Part> collection) {
        this.parts = new ArrayList<>(collection);
        return this;
    }

    public MultipartContent setContentParts(Collection<? extends HttpContent> collection) {
        this.parts = new ArrayList<>(collection.size());
        for (HttpContent part : collection) {
            addPart(new Part(part));
        }
        return this;
    }

    public final String getBoundary() {
        return getMediaType().getParameter("boundary");
    }

    public MultipartContent setBoundary(String str) {
        getMediaType().setParameter("boundary", (String) Preconditions.checkNotNull(str));
        return this;
    }
}
