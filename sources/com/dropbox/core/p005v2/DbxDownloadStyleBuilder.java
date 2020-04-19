package com.dropbox.core.p005v2;

import com.dropbox.core.DbxDownloader;
import com.dropbox.core.DbxException;
import com.dropbox.core.http.HttpRequestor.Header;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/* renamed from: com.dropbox.core.v2.DbxDownloadStyleBuilder */
public abstract class DbxDownloadStyleBuilder<R> {
    private Long length = null;
    private Long start = null;

    public abstract DbxDownloader<R> start() throws DbxException;

    protected DbxDownloadStyleBuilder() {
    }

    /* access modifiers changed from: protected */
    public List<Header> getHeaders() {
        if (this.start == null) {
            return Collections.emptyList();
        }
        ArrayList arrayList = new ArrayList();
        String format = String.format("bytes=%d-", new Object[]{Long.valueOf(this.start.longValue())});
        if (this.length != null) {
            StringBuilder sb = new StringBuilder();
            sb.append(format);
            sb.append(Long.toString((this.start.longValue() + this.length.longValue()) - 1));
            format = sb.toString();
        }
        arrayList.add(new Header("Range", format));
        return arrayList;
    }

    public DbxDownloadStyleBuilder<R> range(long j, long j2) {
        if (j < 0) {
            throw new IllegalArgumentException("start must be non-negative");
        } else if (j2 >= 1) {
            this.start = Long.valueOf(j);
            this.length = Long.valueOf(j2);
            return this;
        } else {
            throw new IllegalArgumentException("length must be positive");
        }
    }

    public DbxDownloadStyleBuilder<R> range(long j) {
        if (j >= 0) {
            this.start = Long.valueOf(j);
            this.length = null;
            return this;
        }
        throw new IllegalArgumentException("start must be non-negative");
    }

    public R download(OutputStream outputStream) throws DbxException, IOException {
        return start().download(outputStream);
    }
}
