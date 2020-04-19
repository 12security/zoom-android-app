package com.microsoft.services.msa;

interface OAuthResponse {
    void accept(OAuthResponseVisitor oAuthResponseVisitor);
}
