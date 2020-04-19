package com.google.api.client.googleapis.auth.oauth2;

import com.box.androidsdk.content.BoxConstants;
import com.google.api.client.auth.oauth2.BearerToken;
import com.google.api.client.auth.oauth2.ClientParametersAuthentication;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.auth.oauth2.CredentialRefreshListener;
import com.google.api.client.auth.oauth2.TokenRequest;
import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets.Details;
import com.google.api.client.googleapis.util.Utils;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpExecuteInterceptor;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.GenericJson;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.JsonObjectParser;
import com.google.api.client.json.webtoken.JsonWebSignature;
import com.google.api.client.json.webtoken.JsonWebSignature.Header;
import com.google.api.client.json.webtoken.JsonWebToken.Payload;
import com.google.api.client.util.Beta;
import com.google.api.client.util.Clock;
import com.google.api.client.util.Joiner;
import com.google.api.client.util.PemReader;
import com.google.api.client.util.PemReader.Section;
import com.google.api.client.util.Preconditions;
import com.google.api.client.util.SecurityUtils;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Collection;
import java.util.Collections;
import org.apache.http.message.TokenParser;
import p021us.zoom.androidlib.util.TimeUtil;

public class GoogleCredential extends Credential {
    static final String SERVICE_ACCOUNT_FILE_TYPE = "service_account";
    static final String USER_FILE_TYPE = "authorized_user";
    @Beta
    private static DefaultCredentialProvider defaultCredentialProvider = new DefaultCredentialProvider();
    private String serviceAccountId;
    private PrivateKey serviceAccountPrivateKey;
    private String serviceAccountPrivateKeyId;
    private String serviceAccountProjectId;
    private Collection<String> serviceAccountScopes;
    private String serviceAccountUser;

    public static class Builder extends com.google.api.client.auth.oauth2.Credential.Builder {
        String serviceAccountId;
        PrivateKey serviceAccountPrivateKey;
        String serviceAccountPrivateKeyId;
        String serviceAccountProjectId;
        Collection<String> serviceAccountScopes;
        String serviceAccountUser;

        public Builder() {
            super(BearerToken.authorizationHeaderAccessMethod());
            setTokenServerEncodedUrl(GoogleOAuthConstants.TOKEN_SERVER_URL);
        }

        public GoogleCredential build() {
            return new GoogleCredential(this);
        }

        public Builder setTransport(HttpTransport httpTransport) {
            return (Builder) super.setTransport(httpTransport);
        }

        public Builder setJsonFactory(JsonFactory jsonFactory) {
            return (Builder) super.setJsonFactory(jsonFactory);
        }

        public Builder setClock(Clock clock) {
            return (Builder) super.setClock(clock);
        }

        public Builder setClientSecrets(String str, String str2) {
            setClientAuthentication((HttpExecuteInterceptor) new ClientParametersAuthentication(str, str2));
            return this;
        }

        public Builder setClientSecrets(GoogleClientSecrets googleClientSecrets) {
            Details details = googleClientSecrets.getDetails();
            setClientAuthentication((HttpExecuteInterceptor) new ClientParametersAuthentication(details.getClientId(), details.getClientSecret()));
            return this;
        }

        public final String getServiceAccountId() {
            return this.serviceAccountId;
        }

        public Builder setServiceAccountId(String str) {
            this.serviceAccountId = str;
            return this;
        }

        public final String getServiceAccountProjectId() {
            return this.serviceAccountProjectId;
        }

        public Builder setServiceAccountProjectId(String str) {
            this.serviceAccountProjectId = str;
            return this;
        }

        public final Collection<String> getServiceAccountScopes() {
            return this.serviceAccountScopes;
        }

        public Builder setServiceAccountScopes(Collection<String> collection) {
            this.serviceAccountScopes = collection;
            return this;
        }

        public final PrivateKey getServiceAccountPrivateKey() {
            return this.serviceAccountPrivateKey;
        }

        public Builder setServiceAccountPrivateKey(PrivateKey privateKey) {
            this.serviceAccountPrivateKey = privateKey;
            return this;
        }

        @Beta
        public final String getServiceAccountPrivateKeyId() {
            return this.serviceAccountPrivateKeyId;
        }

        @Beta
        public Builder setServiceAccountPrivateKeyId(String str) {
            this.serviceAccountPrivateKeyId = str;
            return this;
        }

        public Builder setServiceAccountPrivateKeyFromP12File(File file) throws GeneralSecurityException, IOException {
            setServiceAccountPrivateKeyFromP12File((InputStream) new FileInputStream(file));
            return this;
        }

        public Builder setServiceAccountPrivateKeyFromP12File(InputStream inputStream) throws GeneralSecurityException, IOException {
            this.serviceAccountPrivateKey = SecurityUtils.loadPrivateKeyFromKeyStore(SecurityUtils.getPkcs12KeyStore(), inputStream, "notasecret", "privatekey", "notasecret");
            return this;
        }

        @Beta
        public Builder setServiceAccountPrivateKeyFromPemFile(File file) throws GeneralSecurityException, IOException {
            this.serviceAccountPrivateKey = SecurityUtils.getRsaKeyFactory().generatePrivate(new PKCS8EncodedKeySpec(PemReader.readFirstSectionAndClose(new FileReader(file), "PRIVATE KEY").getBase64DecodedBytes()));
            return this;
        }

        public final String getServiceAccountUser() {
            return this.serviceAccountUser;
        }

        public Builder setServiceAccountUser(String str) {
            this.serviceAccountUser = str;
            return this;
        }

        public Builder setRequestInitializer(HttpRequestInitializer httpRequestInitializer) {
            return (Builder) super.setRequestInitializer(httpRequestInitializer);
        }

        public Builder addRefreshListener(CredentialRefreshListener credentialRefreshListener) {
            return (Builder) super.addRefreshListener(credentialRefreshListener);
        }

        public Builder setRefreshListeners(Collection<CredentialRefreshListener> collection) {
            return (Builder) super.setRefreshListeners(collection);
        }

        public Builder setTokenServerUrl(GenericUrl genericUrl) {
            return (Builder) super.setTokenServerUrl(genericUrl);
        }

        public Builder setTokenServerEncodedUrl(String str) {
            return (Builder) super.setTokenServerEncodedUrl(str);
        }

        public Builder setClientAuthentication(HttpExecuteInterceptor httpExecuteInterceptor) {
            return (Builder) super.setClientAuthentication(httpExecuteInterceptor);
        }
    }

    @Beta
    public static GoogleCredential getApplicationDefault() throws IOException {
        return getApplicationDefault(Utils.getDefaultTransport(), Utils.getDefaultJsonFactory());
    }

    @Beta
    public static GoogleCredential getApplicationDefault(HttpTransport httpTransport, JsonFactory jsonFactory) throws IOException {
        Preconditions.checkNotNull(httpTransport);
        Preconditions.checkNotNull(jsonFactory);
        return defaultCredentialProvider.getDefaultCredential(httpTransport, jsonFactory);
    }

    @Beta
    public static GoogleCredential fromStream(InputStream inputStream) throws IOException {
        return fromStream(inputStream, Utils.getDefaultTransport(), Utils.getDefaultJsonFactory());
    }

    @Beta
    public static GoogleCredential fromStream(InputStream inputStream, HttpTransport httpTransport, JsonFactory jsonFactory) throws IOException {
        Preconditions.checkNotNull(inputStream);
        Preconditions.checkNotNull(httpTransport);
        Preconditions.checkNotNull(jsonFactory);
        GenericJson genericJson = (GenericJson) new JsonObjectParser(jsonFactory).parseAndClose(inputStream, OAuth2Utils.UTF_8, GenericJson.class);
        String str = (String) genericJson.get("type");
        if (str == null) {
            throw new IOException("Error reading credentials from stream, 'type' field not specified.");
        } else if (USER_FILE_TYPE.equals(str)) {
            return fromStreamUser(genericJson, httpTransport, jsonFactory);
        } else {
            if (SERVICE_ACCOUNT_FILE_TYPE.equals(str)) {
                return fromStreamServiceAccount(genericJson, httpTransport, jsonFactory);
            }
            throw new IOException(String.format("Error reading credentials from stream, 'type' value '%s' not recognized. Expecting '%s' or '%s'.", new Object[]{str, USER_FILE_TYPE, SERVICE_ACCOUNT_FILE_TYPE}));
        }
    }

    public GoogleCredential() {
        this(new Builder());
    }

    protected GoogleCredential(Builder builder) {
        Collection<String> collection;
        super((com.google.api.client.auth.oauth2.Credential.Builder) builder);
        if (builder.serviceAccountPrivateKey == null) {
            Preconditions.checkArgument(builder.serviceAccountId == null && builder.serviceAccountScopes == null && builder.serviceAccountUser == null);
            return;
        }
        this.serviceAccountId = (String) Preconditions.checkNotNull(builder.serviceAccountId);
        this.serviceAccountProjectId = builder.serviceAccountProjectId;
        if (builder.serviceAccountScopes == null) {
            collection = Collections.emptyList();
        } else {
            collection = Collections.unmodifiableCollection(builder.serviceAccountScopes);
        }
        this.serviceAccountScopes = collection;
        this.serviceAccountPrivateKey = builder.serviceAccountPrivateKey;
        this.serviceAccountPrivateKeyId = builder.serviceAccountPrivateKeyId;
        this.serviceAccountUser = builder.serviceAccountUser;
    }

    public GoogleCredential setAccessToken(String str) {
        return (GoogleCredential) super.setAccessToken(str);
    }

    public GoogleCredential setRefreshToken(String str) {
        if (str != null) {
            Preconditions.checkArgument((getJsonFactory() == null || getTransport() == null || getClientAuthentication() == null) ? false : true, "Please use the Builder and call setJsonFactory, setTransport and setClientSecrets");
        }
        return (GoogleCredential) super.setRefreshToken(str);
    }

    public GoogleCredential setExpirationTimeMilliseconds(Long l) {
        return (GoogleCredential) super.setExpirationTimeMilliseconds(l);
    }

    public GoogleCredential setExpiresInSeconds(Long l) {
        return (GoogleCredential) super.setExpiresInSeconds(l);
    }

    public GoogleCredential setFromTokenResponse(TokenResponse tokenResponse) {
        return (GoogleCredential) super.setFromTokenResponse(tokenResponse);
    }

    /* access modifiers changed from: protected */
    @Beta
    public TokenResponse executeRefreshToken() throws IOException {
        if (this.serviceAccountPrivateKey == null) {
            return super.executeRefreshToken();
        }
        Header header = new Header();
        header.setAlgorithm("RS256");
        header.setType("JWT");
        header.setKeyId(this.serviceAccountPrivateKeyId);
        Payload payload = new Payload();
        long currentTimeMillis = getClock().currentTimeMillis();
        payload.setIssuer(this.serviceAccountId);
        payload.setAudience(getTokenServerEncodedUrl());
        long j = currentTimeMillis / 1000;
        payload.setIssuedAtTimeSeconds(Long.valueOf(j));
        payload.setExpirationTimeSeconds(Long.valueOf(j + TimeUtil.ONE_HOUR_IN_SECONDS));
        payload.setSubject(this.serviceAccountUser);
        payload.put("scope", (Object) Joiner.m70on(TokenParser.f498SP).join(this.serviceAccountScopes));
        try {
            String signUsingRsaSha256 = JsonWebSignature.signUsingRsaSha256(this.serviceAccountPrivateKey, getJsonFactory(), header, payload);
            TokenRequest tokenRequest = new TokenRequest(getTransport(), getJsonFactory(), new GenericUrl(getTokenServerEncodedUrl()), "urn:ietf:params:oauth:grant-type:jwt-bearer");
            tokenRequest.put("assertion", (Object) signUsingRsaSha256);
            return tokenRequest.execute();
        } catch (GeneralSecurityException e) {
            IOException iOException = new IOException();
            iOException.initCause(e);
            throw iOException;
        }
    }

    public final String getServiceAccountId() {
        return this.serviceAccountId;
    }

    public final String getServiceAccountProjectId() {
        return this.serviceAccountProjectId;
    }

    public final Collection<String> getServiceAccountScopes() {
        return this.serviceAccountScopes;
    }

    public final String getServiceAccountScopesAsString() {
        if (this.serviceAccountScopes == null) {
            return null;
        }
        return Joiner.m70on(TokenParser.f498SP).join(this.serviceAccountScopes);
    }

    public final PrivateKey getServiceAccountPrivateKey() {
        return this.serviceAccountPrivateKey;
    }

    @Beta
    public final String getServiceAccountPrivateKeyId() {
        return this.serviceAccountPrivateKeyId;
    }

    public final String getServiceAccountUser() {
        return this.serviceAccountUser;
    }

    @Beta
    public boolean createScopedRequired() {
        boolean z = false;
        if (this.serviceAccountPrivateKey == null) {
            return false;
        }
        Collection<String> collection = this.serviceAccountScopes;
        if (collection == null || collection.isEmpty()) {
            z = true;
        }
        return z;
    }

    @Beta
    public GoogleCredential createScoped(Collection<String> collection) {
        if (this.serviceAccountPrivateKey == null) {
            return this;
        }
        return toBuilder().setServiceAccountScopes(collection).build();
    }

    @Beta
    public GoogleCredential createDelegated(String str) {
        if (this.serviceAccountPrivateKey == null) {
            return this;
        }
        return toBuilder().setServiceAccountUser(str).build();
    }

    @Beta
    public Builder toBuilder() {
        Builder clock = new Builder().setServiceAccountPrivateKey(this.serviceAccountPrivateKey).setServiceAccountPrivateKeyId(this.serviceAccountPrivateKeyId).setServiceAccountId(this.serviceAccountId).setServiceAccountProjectId(this.serviceAccountProjectId).setServiceAccountUser(this.serviceAccountUser).setServiceAccountScopes(this.serviceAccountScopes).setTokenServerEncodedUrl(getTokenServerEncodedUrl()).setTransport(getTransport()).setJsonFactory(getJsonFactory()).setClock(getClock());
        clock.setClientAuthentication(getClientAuthentication());
        return clock;
    }

    @Beta
    private static GoogleCredential fromStreamUser(GenericJson genericJson, HttpTransport httpTransport, JsonFactory jsonFactory) throws IOException {
        String str = (String) genericJson.get("client_id");
        String str2 = (String) genericJson.get(BoxConstants.KEY_CLIENT_SECRET);
        String str3 = (String) genericJson.get("refresh_token");
        if (str == null || str2 == null || str3 == null) {
            throw new IOException("Error reading user credential from stream,  expecting 'client_id', 'client_secret' and 'refresh_token'.");
        }
        GoogleCredential build = new Builder().setClientSecrets(str, str2).setTransport(httpTransport).setJsonFactory(jsonFactory).build();
        build.setRefreshToken(str3);
        build.refreshToken();
        return build;
    }

    @Beta
    private static GoogleCredential fromStreamServiceAccount(GenericJson genericJson, HttpTransport httpTransport, JsonFactory jsonFactory) throws IOException {
        String str = (String) genericJson.get("client_email");
        String str2 = (String) genericJson.get("private_key");
        String str3 = (String) genericJson.get("private_key_id");
        if (((String) genericJson.get("client_id")) == null || str == null || str2 == null || str3 == null) {
            throw new IOException("Error reading service account credential from stream, expecting  'client_id', 'client_email', 'private_key' and 'private_key_id'.");
        }
        Builder serviceAccountPrivateKeyId2 = new Builder().setTransport(httpTransport).setJsonFactory(jsonFactory).setServiceAccountId(str).setServiceAccountScopes(Collections.emptyList()).setServiceAccountPrivateKey(privateKeyFromPkcs8(str2)).setServiceAccountPrivateKeyId(str3);
        String str4 = (String) genericJson.get("token_uri");
        if (str4 != null) {
            serviceAccountPrivateKeyId2.setTokenServerEncodedUrl(str4);
        }
        String str5 = (String) genericJson.get("project_id");
        if (str5 != null) {
            serviceAccountPrivateKeyId2.setServiceAccountProjectId(str5);
        }
        return serviceAccountPrivateKeyId2.build();
    }

    @Beta
    private static PrivateKey privateKeyFromPkcs8(String str) throws IOException {
        Section readFirstSectionAndClose = PemReader.readFirstSectionAndClose(new StringReader(str), "PRIVATE KEY");
        if (readFirstSectionAndClose != null) {
            try {
                return SecurityUtils.getRsaKeyFactory().generatePrivate(new PKCS8EncodedKeySpec(readFirstSectionAndClose.getBase64DecodedBytes()));
            } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
                throw ((IOException) OAuth2Utils.exceptionWithCause(new IOException("Unexpected exception reading PKCS data"), e));
            }
        } else {
            throw new IOException("Invalid PKCS8 data.");
        }
    }
}
