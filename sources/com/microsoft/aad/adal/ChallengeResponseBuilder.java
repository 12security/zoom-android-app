package com.microsoft.aad.adal;

import com.microsoft.aad.adal.AuthenticationConstants.Broker;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.security.interfaces.RSAPrivateKey;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

class ChallengeResponseBuilder {
    private static final String TAG = "ChallengeResponseBuilder";
    private final IJWSBuilder mJWSBuilder;

    class ChallengeRequest {
        /* access modifiers changed from: private */
        public List<String> mCertAuthorities;
        /* access modifiers changed from: private */
        public String mContext = "";
        /* access modifiers changed from: private */
        public String mNonce = "";
        /* access modifiers changed from: private */
        public String mSubmitUrl = "";
        /* access modifiers changed from: private */
        public String mThumbprint = "";
        /* access modifiers changed from: private */
        public String mVersion = null;

        ChallengeRequest() {
        }
    }

    class ChallengeResponse {
        /* access modifiers changed from: private */
        public String mAuthorizationHeaderValue;
        /* access modifiers changed from: private */
        public String mSubmitUrl;

        ChallengeResponse() {
        }

        /* access modifiers changed from: 0000 */
        public String getSubmitUrl() {
            return this.mSubmitUrl;
        }

        /* access modifiers changed from: 0000 */
        public void setSubmitUrl(String str) {
            this.mSubmitUrl = str;
        }

        /* access modifiers changed from: 0000 */
        public String getAuthorizationHeaderValue() {
            return this.mAuthorizationHeaderValue;
        }

        /* access modifiers changed from: 0000 */
        public void setAuthorizationHeaderValue(String str) {
            this.mAuthorizationHeaderValue = str;
        }
    }

    enum RequestField {
        Nonce,
        CertAuthorities,
        Version,
        SubmitUrl,
        Context,
        CertThumbprint
    }

    ChallengeResponseBuilder(IJWSBuilder iJWSBuilder) {
        this.mJWSBuilder = iJWSBuilder;
    }

    public ChallengeResponse getChallengeResponseFromUri(String str) throws AuthenticationException {
        return getDeviceCertResponse(getChallengeRequest(str));
    }

    public ChallengeResponse getChallengeResponseFromHeader(String str, String str2) throws UnsupportedEncodingException, AuthenticationException {
        ChallengeRequest challengeRequestFromHeader = getChallengeRequestFromHeader(str);
        challengeRequestFromHeader.mSubmitUrl = str2;
        return getDeviceCertResponse(challengeRequestFromHeader);
    }

    private ChallengeResponse getDeviceCertResponse(ChallengeRequest challengeRequest) throws AuthenticationException {
        ChallengeResponse noDeviceCertResponse = getNoDeviceCertResponse(challengeRequest);
        noDeviceCertResponse.mSubmitUrl = challengeRequest.mSubmitUrl;
        Class deviceCertificateProxy = AuthenticationSettings.INSTANCE.getDeviceCertificateProxy();
        if (deviceCertificateProxy != null) {
            IDeviceCertificate wPJAPIInstance = getWPJAPIInstance(deviceCertificateProxy);
            if (wPJAPIInstance.isValidIssuer(challengeRequest.mCertAuthorities) || (wPJAPIInstance.getThumbPrint() != null && wPJAPIInstance.getThumbPrint().equalsIgnoreCase(challengeRequest.mThumbprint))) {
                RSAPrivateKey rSAPrivateKey = wPJAPIInstance.getRSAPrivateKey();
                if (rSAPrivateKey != null) {
                    noDeviceCertResponse.mAuthorizationHeaderValue = String.format("%s AuthToken=\"%s\",Context=\"%s\",Version=\"%s\"", new Object[]{Broker.CHALLENGE_RESPONSE_TYPE, this.mJWSBuilder.generateSignedJWT(challengeRequest.mNonce, challengeRequest.mSubmitUrl, rSAPrivateKey, wPJAPIInstance.getRSAPublicKey(), wPJAPIInstance.getCertificate()), challengeRequest.mContext, challengeRequest.mVersion});
                    StringBuilder sb = new StringBuilder();
                    sb.append("Challenge response:");
                    sb.append(noDeviceCertResponse.mAuthorizationHeaderValue);
                    Logger.m237v(TAG, "Receive challenge response. ", sb.toString(), null);
                } else {
                    throw new AuthenticationException(ADALError.KEY_CHAIN_PRIVATE_KEY_EXCEPTION);
                }
            }
        }
        return noDeviceCertResponse;
    }

    private boolean isWorkplaceJoined() {
        return AuthenticationSettings.INSTANCE.getDeviceCertificateProxy() != null;
    }

    private IDeviceCertificate getWPJAPIInstance(Class<IDeviceCertificate> cls) throws AuthenticationException {
        try {
            return (IDeviceCertificate) cls.getDeclaredConstructor(new Class[0]).newInstance(null);
        } catch (IllegalAccessException | IllegalArgumentException | InstantiationException | NoSuchMethodException | InvocationTargetException e) {
            throw new AuthenticationException(ADALError.DEVICE_CERTIFICATE_API_EXCEPTION, "WPJ Api constructor is not defined", e);
        }
    }

    private ChallengeResponse getNoDeviceCertResponse(ChallengeRequest challengeRequest) {
        ChallengeResponse challengeResponse = new ChallengeResponse();
        challengeResponse.mSubmitUrl = challengeRequest.mSubmitUrl;
        challengeResponse.mAuthorizationHeaderValue = String.format("%s Context=\"%s\",Version=\"%s\"", new Object[]{Broker.CHALLENGE_RESPONSE_TYPE, challengeRequest.mContext, challengeRequest.mVersion});
        return challengeResponse;
    }

    private ChallengeRequest getChallengeRequestFromHeader(String str) throws UnsupportedEncodingException, AuthenticationException {
        if (StringExtensions.isNullOrBlank(str)) {
            throw new AuthenticationServerProtocolException("headerValue");
        } else if (StringExtensions.hasPrefixInHeader(str, Broker.CHALLENGE_RESPONSE_TYPE)) {
            ChallengeRequest challengeRequest = new ChallengeRequest();
            String substring = str.substring(8);
            ArrayList splitWithQuotes = StringExtensions.splitWithQuotes(substring, ',');
            HashMap hashMap = new HashMap();
            Iterator it = splitWithQuotes.iterator();
            while (it.hasNext()) {
                ArrayList splitWithQuotes2 = StringExtensions.splitWithQuotes((String) it.next(), '=');
                if (splitWithQuotes2.size() == 2 && !StringExtensions.isNullOrBlank((String) splitWithQuotes2.get(0)) && !StringExtensions.isNullOrBlank((String) splitWithQuotes2.get(1))) {
                    String str2 = (String) splitWithQuotes2.get(0);
                    String str3 = (String) splitWithQuotes2.get(1);
                    hashMap.put(StringExtensions.urlFormDecode(str2).trim(), StringExtensions.removeQuoteInHeaderValue(StringExtensions.urlFormDecode(str3).trim()));
                } else if (splitWithQuotes2.size() != 1 || StringExtensions.isNullOrBlank((String) splitWithQuotes2.get(0))) {
                    throw new AuthenticationException(ADALError.DEVICE_CERTIFICATE_REQUEST_INVALID, substring);
                } else {
                    hashMap.put(StringExtensions.urlFormDecode((String) splitWithQuotes2.get(0)).trim(), StringExtensions.urlFormDecode(""));
                }
            }
            validateChallengeRequest(hashMap, false);
            challengeRequest.mNonce = (String) hashMap.get(RequestField.Nonce.name());
            if (StringExtensions.isNullOrBlank(challengeRequest.mNonce)) {
                challengeRequest.mNonce = (String) hashMap.get(RequestField.Nonce.name().toLowerCase(Locale.US));
            }
            if (!isWorkplaceJoined()) {
                Logger.m236v("ChallengeResponseBuilder:getChallengeRequestFromHeader", "Device is not workplace joined. ");
            } else if (!StringExtensions.isNullOrBlank((String) hashMap.get(RequestField.CertThumbprint.name()))) {
                Logger.m236v("ChallengeResponseBuilder:getChallengeRequestFromHeader", "CertThumbprint exists in the device auth challenge.");
                challengeRequest.mThumbprint = (String) hashMap.get(RequestField.CertThumbprint.name());
            } else if (hashMap.containsKey(RequestField.CertAuthorities.name())) {
                Logger.m236v("ChallengeResponseBuilder:getChallengeRequestFromHeader", "CertAuthorities exists in the device auth challenge.");
                challengeRequest.mCertAuthorities = StringExtensions.getStringTokens((String) hashMap.get(RequestField.CertAuthorities.name()), ";");
            } else {
                throw new AuthenticationException(ADALError.DEVICE_CERTIFICATE_REQUEST_INVALID, "Both certThumbprint and certauthorities are not present");
            }
            challengeRequest.mVersion = (String) hashMap.get(RequestField.Version.name());
            challengeRequest.mContext = (String) hashMap.get(RequestField.Context.name());
            return challengeRequest;
        } else {
            throw new AuthenticationException(ADALError.DEVICE_CERTIFICATE_REQUEST_INVALID, str);
        }
    }

    private void validateChallengeRequest(Map<String, String> map, boolean z) throws AuthenticationException {
        if (!map.containsKey(RequestField.Nonce.name()) && !map.containsKey(RequestField.Nonce.name().toLowerCase(Locale.US))) {
            throw new AuthenticationException(ADALError.DEVICE_CERTIFICATE_REQUEST_INVALID, "Nonce");
        } else if (!map.containsKey(RequestField.Version.name())) {
            throw new AuthenticationException(ADALError.DEVICE_CERTIFICATE_REQUEST_INVALID, "Version");
        } else if (z && !map.containsKey(RequestField.SubmitUrl.name())) {
            throw new AuthenticationException(ADALError.DEVICE_CERTIFICATE_REQUEST_INVALID, "SubmitUrl");
        } else if (!map.containsKey(RequestField.Context.name())) {
            throw new AuthenticationException(ADALError.DEVICE_CERTIFICATE_REQUEST_INVALID, Broker.CHALLENGE_RESPONSE_CONTEXT);
        } else if (z && !map.containsKey(RequestField.CertAuthorities.name())) {
            throw new AuthenticationException(ADALError.DEVICE_CERTIFICATE_REQUEST_INVALID, "CertAuthorities");
        }
    }

    private ChallengeRequest getChallengeRequest(String str) throws AuthenticationException {
        if (!StringExtensions.isNullOrBlank(str)) {
            ChallengeRequest challengeRequest = new ChallengeRequest();
            HashMap urlParameters = StringExtensions.getUrlParameters(str);
            validateChallengeRequest(urlParameters, true);
            challengeRequest.mNonce = (String) urlParameters.get(RequestField.Nonce.name());
            if (StringExtensions.isNullOrBlank(challengeRequest.mNonce)) {
                challengeRequest.mNonce = (String) urlParameters.get(RequestField.Nonce.name().toLowerCase(Locale.US));
            }
            String str2 = (String) urlParameters.get(RequestField.CertAuthorities.name());
            StringBuilder sb = new StringBuilder();
            sb.append("Authorities: ");
            sb.append(str2);
            Logger.m237v("ChallengeResponseBuilder:getChallengeRequest", "Get cert authorities. ", sb.toString(), null);
            challengeRequest.mCertAuthorities = StringExtensions.getStringTokens(str2, ";");
            challengeRequest.mVersion = (String) urlParameters.get(RequestField.Version.name());
            challengeRequest.mSubmitUrl = (String) urlParameters.get(RequestField.SubmitUrl.name());
            challengeRequest.mContext = (String) urlParameters.get(RequestField.Context.name());
            return challengeRequest;
        }
        throw new AuthenticationServerProtocolException("redirectUri");
    }
}
