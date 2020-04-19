package com.microsoft.aad.adal;

import java.net.URI;
import java.net.URISyntaxException;

final class ADFSWebFingerValidator {
    private static final String TAG = "ADFSWebFingerValidator";
    private static final URI TRUSTED_REALM_REL;

    private ADFSWebFingerValidator() {
    }

    static {
        try {
            TRUSTED_REALM_REL = new URI("http://schemas.microsoft.com/rel/trusted-realm");
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException(e);
        }
    }

    static boolean realmIsTrusted(URI uri, WebFingerMetadata webFingerMetadata) {
        if (uri == null) {
            throw new IllegalArgumentException("Authority cannot be null");
        } else if (webFingerMetadata != null) {
            StringBuilder sb = new StringBuilder();
            sb.append(TAG);
            sb.append(":realmIsTrusted");
            StringBuilder sb2 = new StringBuilder();
            sb2.append(uri.toString());
            sb2.append(webFingerMetadata.toString());
            Logger.m237v(sb.toString(), "Verifying trust authority. ", sb2.toString(), null);
            if (webFingerMetadata.getLinks() != null) {
                for (Link link : webFingerMetadata.getLinks()) {
                    try {
                        URI uri2 = new URI(link.getHref());
                        URI uri3 = new URI(link.getRel());
                        if (uri2.getScheme().equalsIgnoreCase(uri.getScheme()) && uri2.getAuthority().equalsIgnoreCase(uri.getAuthority()) && uri3.equals(TRUSTED_REALM_REL)) {
                            return true;
                        }
                    } catch (URISyntaxException unused) {
                    }
                }
            }
            return false;
        } else {
            throw new IllegalArgumentException("WebFingerMetadata cannot be null");
        }
    }
}
