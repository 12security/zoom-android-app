package com.dropbox.core.p005v2.teamlog;

import com.box.androidsdk.content.models.BoxFile;
import com.box.androidsdk.content.models.BoxFolder;
import com.dropbox.core.stone.UnionSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;
import java.util.Arrays;

/* renamed from: com.dropbox.core.v2.teamlog.AssetLogInfo */
public final class AssetLogInfo {
    public static final AssetLogInfo OTHER = new AssetLogInfo().withTag(Tag.OTHER);
    private Tag _tag;
    /* access modifiers changed from: private */
    public FileLogInfo fileValue;
    /* access modifiers changed from: private */
    public FolderLogInfo folderValue;
    /* access modifiers changed from: private */
    public PaperDocumentLogInfo paperDocumentValue;
    /* access modifiers changed from: private */
    public PaperFolderLogInfo paperFolderValue;
    /* access modifiers changed from: private */
    public ShowcaseDocumentLogInfo showcaseDocumentValue;

    /* renamed from: com.dropbox.core.v2.teamlog.AssetLogInfo$Serializer */
    static class Serializer extends UnionSerializer<AssetLogInfo> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(AssetLogInfo assetLogInfo, JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
            switch (assetLogInfo.tag()) {
                case FILE:
                    jsonGenerator.writeStartObject();
                    writeTag(BoxFile.TYPE, jsonGenerator);
                    Serializer.INSTANCE.serialize(assetLogInfo.fileValue, jsonGenerator, true);
                    jsonGenerator.writeEndObject();
                    return;
                case FOLDER:
                    jsonGenerator.writeStartObject();
                    writeTag(BoxFolder.TYPE, jsonGenerator);
                    Serializer.INSTANCE.serialize(assetLogInfo.folderValue, jsonGenerator, true);
                    jsonGenerator.writeEndObject();
                    return;
                case PAPER_DOCUMENT:
                    jsonGenerator.writeStartObject();
                    writeTag("paper_document", jsonGenerator);
                    Serializer.INSTANCE.serialize(assetLogInfo.paperDocumentValue, jsonGenerator, true);
                    jsonGenerator.writeEndObject();
                    return;
                case PAPER_FOLDER:
                    jsonGenerator.writeStartObject();
                    writeTag("paper_folder", jsonGenerator);
                    Serializer.INSTANCE.serialize(assetLogInfo.paperFolderValue, jsonGenerator, true);
                    jsonGenerator.writeEndObject();
                    return;
                case SHOWCASE_DOCUMENT:
                    jsonGenerator.writeStartObject();
                    writeTag("showcase_document", jsonGenerator);
                    Serializer.INSTANCE.serialize(assetLogInfo.showcaseDocumentValue, jsonGenerator, true);
                    jsonGenerator.writeEndObject();
                    return;
                default:
                    jsonGenerator.writeString("other");
                    return;
            }
        }

        public AssetLogInfo deserialize(JsonParser jsonParser) throws IOException, JsonParseException {
            boolean z;
            String str;
            AssetLogInfo assetLogInfo;
            if (jsonParser.getCurrentToken() == JsonToken.VALUE_STRING) {
                str = getStringValue(jsonParser);
                jsonParser.nextToken();
                z = true;
            } else {
                expectStartObject(jsonParser);
                str = readTag(jsonParser);
                z = false;
            }
            if (str != null) {
                if (BoxFile.TYPE.equals(str)) {
                    assetLogInfo = AssetLogInfo.file(Serializer.INSTANCE.deserialize(jsonParser, true));
                } else if (BoxFolder.TYPE.equals(str)) {
                    assetLogInfo = AssetLogInfo.folder(Serializer.INSTANCE.deserialize(jsonParser, true));
                } else if ("paper_document".equals(str)) {
                    assetLogInfo = AssetLogInfo.paperDocument(Serializer.INSTANCE.deserialize(jsonParser, true));
                } else if ("paper_folder".equals(str)) {
                    assetLogInfo = AssetLogInfo.paperFolder(Serializer.INSTANCE.deserialize(jsonParser, true));
                } else if ("showcase_document".equals(str)) {
                    assetLogInfo = AssetLogInfo.showcaseDocument(Serializer.INSTANCE.deserialize(jsonParser, true));
                } else {
                    assetLogInfo = AssetLogInfo.OTHER;
                }
                if (!z) {
                    skipFields(jsonParser);
                    expectEndObject(jsonParser);
                }
                return assetLogInfo;
            }
            throw new JsonParseException(jsonParser, "Required field missing: .tag");
        }
    }

    /* renamed from: com.dropbox.core.v2.teamlog.AssetLogInfo$Tag */
    public enum Tag {
        FILE,
        FOLDER,
        PAPER_DOCUMENT,
        PAPER_FOLDER,
        SHOWCASE_DOCUMENT,
        OTHER
    }

    private AssetLogInfo() {
    }

    private AssetLogInfo withTag(Tag tag) {
        AssetLogInfo assetLogInfo = new AssetLogInfo();
        assetLogInfo._tag = tag;
        return assetLogInfo;
    }

    private AssetLogInfo withTagAndFile(Tag tag, FileLogInfo fileLogInfo) {
        AssetLogInfo assetLogInfo = new AssetLogInfo();
        assetLogInfo._tag = tag;
        assetLogInfo.fileValue = fileLogInfo;
        return assetLogInfo;
    }

    private AssetLogInfo withTagAndFolder(Tag tag, FolderLogInfo folderLogInfo) {
        AssetLogInfo assetLogInfo = new AssetLogInfo();
        assetLogInfo._tag = tag;
        assetLogInfo.folderValue = folderLogInfo;
        return assetLogInfo;
    }

    private AssetLogInfo withTagAndPaperDocument(Tag tag, PaperDocumentLogInfo paperDocumentLogInfo) {
        AssetLogInfo assetLogInfo = new AssetLogInfo();
        assetLogInfo._tag = tag;
        assetLogInfo.paperDocumentValue = paperDocumentLogInfo;
        return assetLogInfo;
    }

    private AssetLogInfo withTagAndPaperFolder(Tag tag, PaperFolderLogInfo paperFolderLogInfo) {
        AssetLogInfo assetLogInfo = new AssetLogInfo();
        assetLogInfo._tag = tag;
        assetLogInfo.paperFolderValue = paperFolderLogInfo;
        return assetLogInfo;
    }

    private AssetLogInfo withTagAndShowcaseDocument(Tag tag, ShowcaseDocumentLogInfo showcaseDocumentLogInfo) {
        AssetLogInfo assetLogInfo = new AssetLogInfo();
        assetLogInfo._tag = tag;
        assetLogInfo.showcaseDocumentValue = showcaseDocumentLogInfo;
        return assetLogInfo;
    }

    public Tag tag() {
        return this._tag;
    }

    public boolean isFile() {
        return this._tag == Tag.FILE;
    }

    public static AssetLogInfo file(FileLogInfo fileLogInfo) {
        if (fileLogInfo != null) {
            return new AssetLogInfo().withTagAndFile(Tag.FILE, fileLogInfo);
        }
        throw new IllegalArgumentException("Value is null");
    }

    public FileLogInfo getFileValue() {
        if (this._tag == Tag.FILE) {
            return this.fileValue;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Invalid tag: required Tag.FILE, but was Tag.");
        sb.append(this._tag.name());
        throw new IllegalStateException(sb.toString());
    }

    public boolean isFolder() {
        return this._tag == Tag.FOLDER;
    }

    public static AssetLogInfo folder(FolderLogInfo folderLogInfo) {
        if (folderLogInfo != null) {
            return new AssetLogInfo().withTagAndFolder(Tag.FOLDER, folderLogInfo);
        }
        throw new IllegalArgumentException("Value is null");
    }

    public FolderLogInfo getFolderValue() {
        if (this._tag == Tag.FOLDER) {
            return this.folderValue;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Invalid tag: required Tag.FOLDER, but was Tag.");
        sb.append(this._tag.name());
        throw new IllegalStateException(sb.toString());
    }

    public boolean isPaperDocument() {
        return this._tag == Tag.PAPER_DOCUMENT;
    }

    public static AssetLogInfo paperDocument(PaperDocumentLogInfo paperDocumentLogInfo) {
        if (paperDocumentLogInfo != null) {
            return new AssetLogInfo().withTagAndPaperDocument(Tag.PAPER_DOCUMENT, paperDocumentLogInfo);
        }
        throw new IllegalArgumentException("Value is null");
    }

    public PaperDocumentLogInfo getPaperDocumentValue() {
        if (this._tag == Tag.PAPER_DOCUMENT) {
            return this.paperDocumentValue;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Invalid tag: required Tag.PAPER_DOCUMENT, but was Tag.");
        sb.append(this._tag.name());
        throw new IllegalStateException(sb.toString());
    }

    public boolean isPaperFolder() {
        return this._tag == Tag.PAPER_FOLDER;
    }

    public static AssetLogInfo paperFolder(PaperFolderLogInfo paperFolderLogInfo) {
        if (paperFolderLogInfo != null) {
            return new AssetLogInfo().withTagAndPaperFolder(Tag.PAPER_FOLDER, paperFolderLogInfo);
        }
        throw new IllegalArgumentException("Value is null");
    }

    public PaperFolderLogInfo getPaperFolderValue() {
        if (this._tag == Tag.PAPER_FOLDER) {
            return this.paperFolderValue;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Invalid tag: required Tag.PAPER_FOLDER, but was Tag.");
        sb.append(this._tag.name());
        throw new IllegalStateException(sb.toString());
    }

    public boolean isShowcaseDocument() {
        return this._tag == Tag.SHOWCASE_DOCUMENT;
    }

    public static AssetLogInfo showcaseDocument(ShowcaseDocumentLogInfo showcaseDocumentLogInfo) {
        if (showcaseDocumentLogInfo != null) {
            return new AssetLogInfo().withTagAndShowcaseDocument(Tag.SHOWCASE_DOCUMENT, showcaseDocumentLogInfo);
        }
        throw new IllegalArgumentException("Value is null");
    }

    public ShowcaseDocumentLogInfo getShowcaseDocumentValue() {
        if (this._tag == Tag.SHOWCASE_DOCUMENT) {
            return this.showcaseDocumentValue;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Invalid tag: required Tag.SHOWCASE_DOCUMENT, but was Tag.");
        sb.append(this._tag.name());
        throw new IllegalStateException(sb.toString());
    }

    public boolean isOther() {
        return this._tag == Tag.OTHER;
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this._tag, this.fileValue, this.folderValue, this.paperDocumentValue, this.paperFolderValue, this.showcaseDocumentValue});
    }

    public boolean equals(Object obj) {
        boolean z = true;
        if (obj == this) {
            return true;
        }
        if (obj == null || !(obj instanceof AssetLogInfo)) {
            return false;
        }
        AssetLogInfo assetLogInfo = (AssetLogInfo) obj;
        if (this._tag != assetLogInfo._tag) {
            return false;
        }
        switch (this._tag) {
            case FILE:
                FileLogInfo fileLogInfo = this.fileValue;
                FileLogInfo fileLogInfo2 = assetLogInfo.fileValue;
                if (fileLogInfo != fileLogInfo2 && !fileLogInfo.equals(fileLogInfo2)) {
                    z = false;
                }
                return z;
            case FOLDER:
                FolderLogInfo folderLogInfo = this.folderValue;
                FolderLogInfo folderLogInfo2 = assetLogInfo.folderValue;
                if (folderLogInfo != folderLogInfo2 && !folderLogInfo.equals(folderLogInfo2)) {
                    z = false;
                }
                return z;
            case PAPER_DOCUMENT:
                PaperDocumentLogInfo paperDocumentLogInfo = this.paperDocumentValue;
                PaperDocumentLogInfo paperDocumentLogInfo2 = assetLogInfo.paperDocumentValue;
                if (paperDocumentLogInfo != paperDocumentLogInfo2 && !paperDocumentLogInfo.equals(paperDocumentLogInfo2)) {
                    z = false;
                }
                return z;
            case PAPER_FOLDER:
                PaperFolderLogInfo paperFolderLogInfo = this.paperFolderValue;
                PaperFolderLogInfo paperFolderLogInfo2 = assetLogInfo.paperFolderValue;
                if (paperFolderLogInfo != paperFolderLogInfo2 && !paperFolderLogInfo.equals(paperFolderLogInfo2)) {
                    z = false;
                }
                return z;
            case SHOWCASE_DOCUMENT:
                ShowcaseDocumentLogInfo showcaseDocumentLogInfo = this.showcaseDocumentValue;
                ShowcaseDocumentLogInfo showcaseDocumentLogInfo2 = assetLogInfo.showcaseDocumentValue;
                if (showcaseDocumentLogInfo != showcaseDocumentLogInfo2 && !showcaseDocumentLogInfo.equals(showcaseDocumentLogInfo2)) {
                    z = false;
                }
                return z;
            case OTHER:
                return true;
            default:
                return false;
        }
    }

    public String toString() {
        return Serializer.INSTANCE.serialize(this, false);
    }

    public String toStringMultiline() {
        return Serializer.INSTANCE.serialize(this, true);
    }
}
