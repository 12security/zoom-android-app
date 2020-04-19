package com.google.api.client.googleapis.auth.oauth2;

import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.client.json.JsonFactory;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;

public class CloudShellCredential extends GoogleCredential {
    private static final int ACCESS_TOKEN_INDEX = 2;
    protected static final String GET_AUTH_TOKEN_REQUEST = "2\n[]";
    private static final int READ_TIMEOUT_MS = 5000;
    private final int authPort;
    private final JsonFactory jsonFactory;

    public CloudShellCredential(int i, JsonFactory jsonFactory2) {
        this.authPort = i;
        this.jsonFactory = jsonFactory2;
    }

    /* access modifiers changed from: protected */
    public int getAuthPort() {
        return this.authPort;
    }

    /* access modifiers changed from: protected */
    public TokenResponse executeRefreshToken() throws IOException {
        Socket socket = new Socket("localhost", getAuthPort());
        socket.setSoTimeout(5000);
        TokenResponse tokenResponse = new TokenResponse();
        try {
            new PrintWriter(socket.getOutputStream(), true).println(GET_AUTH_TOKEN_REQUEST);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            bufferedReader.readLine();
            tokenResponse.setAccessToken(((List) this.jsonFactory.createJsonParser((Reader) bufferedReader).parseArray(LinkedList.class, Object.class)).get(2).toString());
            return tokenResponse;
        } finally {
            socket.close();
        }
    }
}
