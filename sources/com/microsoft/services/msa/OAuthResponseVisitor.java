package com.microsoft.services.msa;

interface OAuthResponseVisitor {
    void visit(OAuthErrorResponse oAuthErrorResponse);

    void visit(OAuthSuccessfulResponse oAuthSuccessfulResponse);
}
