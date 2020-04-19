package com.microsoft.services.msa;

public enum OverwriteOption {
    Overwrite {
        /* access modifiers changed from: protected */
        public String overwriteQueryParamValue() {
            return "true";
        }
    },
    DoNotOverwrite {
        /* access modifiers changed from: protected */
        public String overwriteQueryParamValue() {
            return "false";
        }
    },
    Rename {
        /* access modifiers changed from: protected */
        public String overwriteQueryParamValue() {
            return "choosenewname";
        }
    };

    /* access modifiers changed from: protected */
    public abstract String overwriteQueryParamValue();

    /* access modifiers changed from: 0000 */
    public void appendQueryParameterOnTo(UriBuilder uriBuilder) {
        uriBuilder.appendQueryParameter(QueryParameters.OVERWRITE, overwriteQueryParamValue());
    }
}
