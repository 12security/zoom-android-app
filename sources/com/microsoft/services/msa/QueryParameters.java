package com.microsoft.services.msa;

final class QueryParameters {
    public static final String CALLBACK = "callback";
    public static final String METHOD = "method";
    public static final String OVERWRITE = "overwrite";
    public static final String PRETTY = "pretty";
    public static final String RETURN_SSL_RESOURCES = "return_ssl_resources";
    public static final String SUPPRESS_REDIRECTS = "suppress_redirects";
    public static final String SUPPRESS_RESPONSE_CODES = "suppress_response_codes";

    private QueryParameters() {
        throw new AssertionError(ErrorMessages.NON_INSTANTIABLE_CLASS);
    }
}
