package org.apache.http.message;

import java.util.Locale;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.ProtocolVersion;
import org.apache.http.ReasonPhraseCatalog;
import org.apache.http.StatusLine;
import org.apache.http.util.Args;

public class BasicHttpResponse extends AbstractHttpMessage implements HttpResponse {
    private int code;
    private HttpEntity entity;
    private Locale locale;
    private final ReasonPhraseCatalog reasonCatalog;
    private String reasonPhrase;
    private StatusLine statusline;
    private ProtocolVersion ver;

    public BasicHttpResponse(StatusLine statusLine, ReasonPhraseCatalog reasonPhraseCatalog, Locale locale2) {
        this.statusline = (StatusLine) Args.notNull(statusLine, "Status line");
        this.ver = statusLine.getProtocolVersion();
        this.code = statusLine.getStatusCode();
        this.reasonPhrase = statusLine.getReasonPhrase();
        this.reasonCatalog = reasonPhraseCatalog;
        this.locale = locale2;
    }

    public BasicHttpResponse(StatusLine statusLine) {
        this.statusline = (StatusLine) Args.notNull(statusLine, "Status line");
        this.ver = statusLine.getProtocolVersion();
        this.code = statusLine.getStatusCode();
        this.reasonPhrase = statusLine.getReasonPhrase();
        this.reasonCatalog = null;
        this.locale = null;
    }

    public BasicHttpResponse(ProtocolVersion protocolVersion, int i, String str) {
        Args.notNegative(i, "Status code");
        this.statusline = null;
        this.ver = protocolVersion;
        this.code = i;
        this.reasonPhrase = str;
        this.reasonCatalog = null;
        this.locale = null;
    }

    public ProtocolVersion getProtocolVersion() {
        return this.ver;
    }

    public StatusLine getStatusLine() {
        if (this.statusline == null) {
            ProtocolVersion protocolVersion = this.ver;
            if (protocolVersion == null) {
                protocolVersion = HttpVersion.HTTP_1_1;
            }
            int i = this.code;
            String str = this.reasonPhrase;
            if (str == null) {
                str = getReason(i);
            }
            this.statusline = new BasicStatusLine(protocolVersion, i, str);
        }
        return this.statusline;
    }

    public HttpEntity getEntity() {
        return this.entity;
    }

    public Locale getLocale() {
        return this.locale;
    }

    public void setStatusLine(StatusLine statusLine) {
        this.statusline = (StatusLine) Args.notNull(statusLine, "Status line");
        this.ver = statusLine.getProtocolVersion();
        this.code = statusLine.getStatusCode();
        this.reasonPhrase = statusLine.getReasonPhrase();
    }

    public void setStatusLine(ProtocolVersion protocolVersion, int i) {
        Args.notNegative(i, "Status code");
        this.statusline = null;
        this.ver = protocolVersion;
        this.code = i;
        this.reasonPhrase = null;
    }

    public void setStatusLine(ProtocolVersion protocolVersion, int i, String str) {
        Args.notNegative(i, "Status code");
        this.statusline = null;
        this.ver = protocolVersion;
        this.code = i;
        this.reasonPhrase = str;
    }

    public void setStatusCode(int i) {
        Args.notNegative(i, "Status code");
        this.statusline = null;
        this.code = i;
        this.reasonPhrase = null;
    }

    public void setReasonPhrase(String str) {
        this.statusline = null;
        this.reasonPhrase = str;
    }

    public void setEntity(HttpEntity httpEntity) {
        this.entity = httpEntity;
    }

    public void setLocale(Locale locale2) {
        this.locale = (Locale) Args.notNull(locale2, "Locale");
        this.statusline = null;
    }

    /* access modifiers changed from: protected */
    public String getReason(int i) {
        ReasonPhraseCatalog reasonPhraseCatalog = this.reasonCatalog;
        if (reasonPhraseCatalog == null) {
            return null;
        }
        Locale locale2 = this.locale;
        if (locale2 == null) {
            locale2 = Locale.getDefault();
        }
        return reasonPhraseCatalog.getReason(i, locale2);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getStatusLine());
        sb.append(TokenParser.f498SP);
        sb.append(this.headergroup);
        if (this.entity != null) {
            sb.append(TokenParser.f498SP);
            sb.append(this.entity);
        }
        return sb.toString();
    }
}
