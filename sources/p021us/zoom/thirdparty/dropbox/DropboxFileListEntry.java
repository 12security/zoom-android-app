package p021us.zoom.thirdparty.dropbox;

import com.dropbox.core.p005v2.files.Metadata;
import p021us.zoom.androidlib.app.ZMFileListEntry;

/* renamed from: us.zoom.thirdparty.dropbox.DropboxFileListEntry */
public class DropboxFileListEntry extends ZMFileListEntry {
    private Metadata mMetadata;

    public DropboxFileListEntry(Metadata metadata) {
        this.mMetadata = metadata;
    }

    public Metadata getMetadata() {
        return this.mMetadata;
    }
}
