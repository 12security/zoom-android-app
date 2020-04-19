package com.box.androidsdk.content.requests;

import com.box.androidsdk.content.BoxException;
import com.box.androidsdk.content.listeners.ProgressListener;
import com.box.androidsdk.content.requests.BoxRequest.Methods;
import com.box.androidsdk.content.utils.BoxDateFormat;
import com.box.androidsdk.content.utils.ProgressOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;

class BoxRequestMultipart extends BoxHttpRequest {
    private static final String BOUNDARY = "da39a3ee5e6b4b0d3255bfef95601890afd80709";
    private static final int BUFFER_SIZE = 8192;
    private static final Logger LOGGER = Logger.getLogger(BoxRequestMultipart.class.getName());
    private Map<String, String> fields = new HashMap();
    private long fileSize;
    private String filename;
    private boolean firstBoundary = true;
    private InputStream inputStream;
    private final StringBuilder loggedRequest = new StringBuilder();
    private OutputStream outputStream;

    public BoxRequestMultipart(URL url, Methods methods, ProgressListener progressListener) throws IOException {
        super(url, methods, progressListener);
        addHeader("Content-Type", "multipart/form-data; boundary=da39a3ee5e6b4b0d3255bfef95601890afd80709");
    }

    public void putField(String str, String str2) {
        this.fields.put(str, str2);
    }

    public void putField(String str, Date date) {
        this.fields.put(str, BoxDateFormat.format(date));
    }

    public void setFile(InputStream inputStream2, String str) {
        this.inputStream = inputStream2;
        this.filename = str;
    }

    public void setFile(InputStream inputStream2, String str, long j) {
        setFile(inputStream2, str);
        this.fileSize = j;
    }

    public BoxHttpRequest setBody(InputStream inputStream2) throws IOException {
        throw new UnsupportedOperationException();
    }

    public void setBody(String str) {
        throw new UnsupportedOperationException();
    }

    /* access modifiers changed from: protected */
    public void writeBody(HttpURLConnection httpURLConnection, ProgressListener progressListener) throws BoxException {
        try {
            httpURLConnection.setChunkedStreamingMode(0);
            httpURLConnection.setDoOutput(true);
            this.outputStream = httpURLConnection.getOutputStream();
            writePartHeader(new String[][]{new String[]{"name", "filename"}, new String[]{"filename", this.filename}}, "application/octet-stream");
            OutputStream outputStream2 = this.outputStream;
            if (progressListener != null) {
                outputStream2 = new ProgressOutputStream(this.outputStream, progressListener, this.fileSize);
            }
            byte[] bArr = new byte[8192];
            int read = this.inputStream.read(bArr);
            while (read != -1) {
                outputStream2.write(bArr, 0, read);
                read = this.inputStream.read(bArr);
            }
            if (LOGGER.isLoggable(Level.FINE)) {
                this.loggedRequest.append("<File Contents Omitted>");
            }
            for (Entry entry : this.fields.entrySet()) {
                writePartHeader(new String[][]{new String[]{"name", (String) entry.getKey()}});
                writeOutput((String) entry.getValue());
            }
            writeBoundary();
        } catch (IOException e) {
            throw new BoxException("Couldn't connect to the Box API due to a network error.", (Throwable) e);
        }
    }

    /* access modifiers changed from: protected */
    public void resetBody() throws IOException {
        this.firstBoundary = true;
        this.inputStream.reset();
        this.loggedRequest.setLength(0);
    }

    /* access modifiers changed from: protected */
    public String bodyToString() {
        return this.loggedRequest.toString();
    }

    private void writeBoundary() throws IOException {
        if (!this.firstBoundary) {
            writeOutput("\r\n");
        }
        this.firstBoundary = false;
        writeOutput("--");
        writeOutput(BOUNDARY);
    }

    private void writePartHeader(String[][] strArr) throws IOException {
        writePartHeader(strArr, null);
    }

    private void writePartHeader(String[][] strArr, String str) throws IOException {
        writeBoundary();
        writeOutput("\r\n");
        writeOutput("Content-Disposition: form-data");
        for (int i = 0; i < strArr.length; i++) {
            writeOutput("; ");
            writeOutput(strArr[i][0]);
            writeOutput("=\"");
            writeOutput(strArr[i][1]);
            writeOutput("\"");
        }
        if (str != null) {
            writeOutput("\r\nContent-Type: ");
            writeOutput(str);
        }
        writeOutput("\r\n\r\n");
    }

    private void writeOutput(String str) throws IOException {
        this.outputStream.write(str.getBytes(Charset.forName("UTF-8")));
        if (LOGGER.isLoggable(Level.FINE)) {
            this.loggedRequest.append(str);
        }
    }

    private void writeOutput(int i) throws IOException {
        this.outputStream.write(i);
    }
}
