package com.zipow.videobox.confapp;

import com.google.protobuf.ByteString;
import com.google.protobuf.CodedInputStream;
import com.google.protobuf.CodedOutputStream;
import com.google.protobuf.ExtensionRegistryLite;
import com.google.protobuf.GeneratedMessageLite;
import com.google.protobuf.Internal;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.MessageLite;
import com.google.protobuf.MessageLiteOrBuilder;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectStreamException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class ConfAppProtos {

    public static final class CCMessage extends GeneratedMessageLite implements CCMessageOrBuilder {
        public static final int CONTENT_FIELD_NUMBER = 4;
        public static final int ID_FIELD_NUMBER = 1;
        public static final int SOURCE_FIELD_NUMBER = 2;
        public static final int SPEAKERID_FIELD_NUMBER = 3;
        public static final int TIME_FIELD_NUMBER = 5;
        private static final CCMessage defaultInstance = new CCMessage(true);
        private static final long serialVersionUID = 0;
        /* access modifiers changed from: private */
        public int bitField0_;
        /* access modifiers changed from: private */
        public Object content_;
        /* access modifiers changed from: private */
        public Object id_;
        private byte memoizedIsInitialized;
        private int memoizedSerializedSize;
        /* access modifiers changed from: private */
        public long source_;
        /* access modifiers changed from: private */
        public long speakerId_;
        /* access modifiers changed from: private */
        public long time_;

        public static final class Builder extends com.google.protobuf.GeneratedMessageLite.Builder<CCMessage, Builder> implements CCMessageOrBuilder {
            private int bitField0_;
            private Object content_ = "";
            private Object id_ = "";
            private long source_;
            private long speakerId_;
            private long time_;

            private void maybeForceBuilderInitialization() {
            }

            private Builder() {
                maybeForceBuilderInitialization();
            }

            /* access modifiers changed from: private */
            public static Builder create() {
                return new Builder();
            }

            public Builder clear() {
                super.clear();
                this.id_ = "";
                this.bitField0_ &= -2;
                this.source_ = 0;
                this.bitField0_ &= -3;
                this.speakerId_ = 0;
                this.bitField0_ &= -5;
                this.content_ = "";
                this.bitField0_ &= -9;
                this.time_ = 0;
                this.bitField0_ &= -17;
                return this;
            }

            public Builder clone() {
                return create().mergeFrom(buildPartial());
            }

            public CCMessage getDefaultInstanceForType() {
                return CCMessage.getDefaultInstance();
            }

            public CCMessage build() {
                CCMessage buildPartial = buildPartial();
                if (buildPartial.isInitialized()) {
                    return buildPartial;
                }
                throw newUninitializedMessageException(buildPartial);
            }

            /* access modifiers changed from: private */
            public CCMessage buildParsed() throws InvalidProtocolBufferException {
                CCMessage buildPartial = buildPartial();
                if (buildPartial.isInitialized()) {
                    return buildPartial;
                }
                throw newUninitializedMessageException(buildPartial).asInvalidProtocolBufferException();
            }

            public CCMessage buildPartial() {
                CCMessage cCMessage = new CCMessage(this);
                int i = this.bitField0_;
                int i2 = 1;
                if ((i & 1) != 1) {
                    i2 = 0;
                }
                cCMessage.id_ = this.id_;
                if ((i & 2) == 2) {
                    i2 |= 2;
                }
                cCMessage.source_ = this.source_;
                if ((i & 4) == 4) {
                    i2 |= 4;
                }
                cCMessage.speakerId_ = this.speakerId_;
                if ((i & 8) == 8) {
                    i2 |= 8;
                }
                cCMessage.content_ = this.content_;
                if ((i & 16) == 16) {
                    i2 |= 16;
                }
                cCMessage.time_ = this.time_;
                cCMessage.bitField0_ = i2;
                return cCMessage;
            }

            public Builder mergeFrom(CCMessage cCMessage) {
                if (cCMessage == CCMessage.getDefaultInstance()) {
                    return this;
                }
                if (cCMessage.hasId()) {
                    setId(cCMessage.getId());
                }
                if (cCMessage.hasSource()) {
                    setSource(cCMessage.getSource());
                }
                if (cCMessage.hasSpeakerId()) {
                    setSpeakerId(cCMessage.getSpeakerId());
                }
                if (cCMessage.hasContent()) {
                    setContent(cCMessage.getContent());
                }
                if (cCMessage.hasTime()) {
                    setTime(cCMessage.getTime());
                }
                return this;
            }

            public final boolean isInitialized() {
                if (hasId() && hasSource() && hasSpeakerId() && hasContent() && hasTime()) {
                    return true;
                }
                return false;
            }

            public Builder mergeFrom(CodedInputStream codedInputStream, ExtensionRegistryLite extensionRegistryLite) throws IOException {
                while (true) {
                    int readTag = codedInputStream.readTag();
                    if (readTag == 0) {
                        return this;
                    }
                    if (readTag == 10) {
                        this.bitField0_ |= 1;
                        this.id_ = codedInputStream.readBytes();
                    } else if (readTag == 16) {
                        this.bitField0_ |= 2;
                        this.source_ = codedInputStream.readInt64();
                    } else if (readTag == 24) {
                        this.bitField0_ |= 4;
                        this.speakerId_ = codedInputStream.readInt64();
                    } else if (readTag == 34) {
                        this.bitField0_ |= 8;
                        this.content_ = codedInputStream.readBytes();
                    } else if (readTag == 40) {
                        this.bitField0_ |= 16;
                        this.time_ = codedInputStream.readInt64();
                    } else if (!parseUnknownField(codedInputStream, extensionRegistryLite, readTag)) {
                        return this;
                    }
                }
            }

            public boolean hasId() {
                return (this.bitField0_ & 1) == 1;
            }

            public String getId() {
                Object obj = this.id_;
                if (obj instanceof String) {
                    return (String) obj;
                }
                String stringUtf8 = ((ByteString) obj).toStringUtf8();
                this.id_ = stringUtf8;
                return stringUtf8;
            }

            public Builder setId(String str) {
                if (str != null) {
                    this.bitField0_ |= 1;
                    this.id_ = str;
                    return this;
                }
                throw new NullPointerException();
            }

            public Builder clearId() {
                this.bitField0_ &= -2;
                this.id_ = CCMessage.getDefaultInstance().getId();
                return this;
            }

            /* access modifiers changed from: 0000 */
            public void setId(ByteString byteString) {
                this.bitField0_ |= 1;
                this.id_ = byteString;
            }

            public boolean hasSource() {
                return (this.bitField0_ & 2) == 2;
            }

            public long getSource() {
                return this.source_;
            }

            public Builder setSource(long j) {
                this.bitField0_ |= 2;
                this.source_ = j;
                return this;
            }

            public Builder clearSource() {
                this.bitField0_ &= -3;
                this.source_ = 0;
                return this;
            }

            public boolean hasSpeakerId() {
                return (this.bitField0_ & 4) == 4;
            }

            public long getSpeakerId() {
                return this.speakerId_;
            }

            public Builder setSpeakerId(long j) {
                this.bitField0_ |= 4;
                this.speakerId_ = j;
                return this;
            }

            public Builder clearSpeakerId() {
                this.bitField0_ &= -5;
                this.speakerId_ = 0;
                return this;
            }

            public boolean hasContent() {
                return (this.bitField0_ & 8) == 8;
            }

            public String getContent() {
                Object obj = this.content_;
                if (obj instanceof String) {
                    return (String) obj;
                }
                String stringUtf8 = ((ByteString) obj).toStringUtf8();
                this.content_ = stringUtf8;
                return stringUtf8;
            }

            public Builder setContent(String str) {
                if (str != null) {
                    this.bitField0_ |= 8;
                    this.content_ = str;
                    return this;
                }
                throw new NullPointerException();
            }

            public Builder clearContent() {
                this.bitField0_ &= -9;
                this.content_ = CCMessage.getDefaultInstance().getContent();
                return this;
            }

            /* access modifiers changed from: 0000 */
            public void setContent(ByteString byteString) {
                this.bitField0_ |= 8;
                this.content_ = byteString;
            }

            public boolean hasTime() {
                return (this.bitField0_ & 16) == 16;
            }

            public long getTime() {
                return this.time_;
            }

            public Builder setTime(long j) {
                this.bitField0_ |= 16;
                this.time_ = j;
                return this;
            }

            public Builder clearTime() {
                this.bitField0_ &= -17;
                this.time_ = 0;
                return this;
            }
        }

        private CCMessage(Builder builder) {
            super(builder);
            this.memoizedIsInitialized = -1;
            this.memoizedSerializedSize = -1;
        }

        private CCMessage(boolean z) {
            this.memoizedIsInitialized = -1;
            this.memoizedSerializedSize = -1;
        }

        public static CCMessage getDefaultInstance() {
            return defaultInstance;
        }

        public CCMessage getDefaultInstanceForType() {
            return defaultInstance;
        }

        public boolean hasId() {
            return (this.bitField0_ & 1) == 1;
        }

        public String getId() {
            Object obj = this.id_;
            if (obj instanceof String) {
                return (String) obj;
            }
            ByteString byteString = (ByteString) obj;
            String stringUtf8 = byteString.toStringUtf8();
            if (Internal.isValidUtf8(byteString)) {
                this.id_ = stringUtf8;
            }
            return stringUtf8;
        }

        private ByteString getIdBytes() {
            Object obj = this.id_;
            if (!(obj instanceof String)) {
                return (ByteString) obj;
            }
            ByteString copyFromUtf8 = ByteString.copyFromUtf8((String) obj);
            this.id_ = copyFromUtf8;
            return copyFromUtf8;
        }

        public boolean hasSource() {
            return (this.bitField0_ & 2) == 2;
        }

        public long getSource() {
            return this.source_;
        }

        public boolean hasSpeakerId() {
            return (this.bitField0_ & 4) == 4;
        }

        public long getSpeakerId() {
            return this.speakerId_;
        }

        public boolean hasContent() {
            return (this.bitField0_ & 8) == 8;
        }

        public String getContent() {
            Object obj = this.content_;
            if (obj instanceof String) {
                return (String) obj;
            }
            ByteString byteString = (ByteString) obj;
            String stringUtf8 = byteString.toStringUtf8();
            if (Internal.isValidUtf8(byteString)) {
                this.content_ = stringUtf8;
            }
            return stringUtf8;
        }

        private ByteString getContentBytes() {
            Object obj = this.content_;
            if (!(obj instanceof String)) {
                return (ByteString) obj;
            }
            ByteString copyFromUtf8 = ByteString.copyFromUtf8((String) obj);
            this.content_ = copyFromUtf8;
            return copyFromUtf8;
        }

        public boolean hasTime() {
            return (this.bitField0_ & 16) == 16;
        }

        public long getTime() {
            return this.time_;
        }

        private void initFields() {
            this.id_ = "";
            this.source_ = 0;
            this.speakerId_ = 0;
            this.content_ = "";
            this.time_ = 0;
        }

        public final boolean isInitialized() {
            byte b = this.memoizedIsInitialized;
            boolean z = true;
            if (b != -1) {
                if (b != 1) {
                    z = false;
                }
                return z;
            } else if (!hasId()) {
                this.memoizedIsInitialized = 0;
                return false;
            } else if (!hasSource()) {
                this.memoizedIsInitialized = 0;
                return false;
            } else if (!hasSpeakerId()) {
                this.memoizedIsInitialized = 0;
                return false;
            } else if (!hasContent()) {
                this.memoizedIsInitialized = 0;
                return false;
            } else if (!hasTime()) {
                this.memoizedIsInitialized = 0;
                return false;
            } else {
                this.memoizedIsInitialized = 1;
                return true;
            }
        }

        public void writeTo(CodedOutputStream codedOutputStream) throws IOException {
            getSerializedSize();
            if ((this.bitField0_ & 1) == 1) {
                codedOutputStream.writeBytes(1, getIdBytes());
            }
            if ((this.bitField0_ & 2) == 2) {
                codedOutputStream.writeInt64(2, this.source_);
            }
            if ((this.bitField0_ & 4) == 4) {
                codedOutputStream.writeInt64(3, this.speakerId_);
            }
            if ((this.bitField0_ & 8) == 8) {
                codedOutputStream.writeBytes(4, getContentBytes());
            }
            if ((this.bitField0_ & 16) == 16) {
                codedOutputStream.writeInt64(5, this.time_);
            }
        }

        public int getSerializedSize() {
            int i = this.memoizedSerializedSize;
            if (i != -1) {
                return i;
            }
            int i2 = 0;
            if ((this.bitField0_ & 1) == 1) {
                i2 = 0 + CodedOutputStream.computeBytesSize(1, getIdBytes());
            }
            if ((this.bitField0_ & 2) == 2) {
                i2 += CodedOutputStream.computeInt64Size(2, this.source_);
            }
            if ((this.bitField0_ & 4) == 4) {
                i2 += CodedOutputStream.computeInt64Size(3, this.speakerId_);
            }
            if ((this.bitField0_ & 8) == 8) {
                i2 += CodedOutputStream.computeBytesSize(4, getContentBytes());
            }
            if ((this.bitField0_ & 16) == 16) {
                i2 += CodedOutputStream.computeInt64Size(5, this.time_);
            }
            this.memoizedSerializedSize = i2;
            return i2;
        }

        /* access modifiers changed from: protected */
        public Object writeReplace() throws ObjectStreamException {
            return super.writeReplace();
        }

        public static CCMessage parseFrom(ByteString byteString) throws InvalidProtocolBufferException {
            return ((Builder) newBuilder().mergeFrom(byteString)).buildParsed();
        }

        public static CCMessage parseFrom(ByteString byteString, ExtensionRegistryLite extensionRegistryLite) throws InvalidProtocolBufferException {
            return ((Builder) newBuilder().mergeFrom(byteString, extensionRegistryLite)).buildParsed();
        }

        public static CCMessage parseFrom(byte[] bArr) throws InvalidProtocolBufferException {
            return ((Builder) newBuilder().mergeFrom(bArr)).buildParsed();
        }

        public static CCMessage parseFrom(byte[] bArr, ExtensionRegistryLite extensionRegistryLite) throws InvalidProtocolBufferException {
            return ((Builder) newBuilder().mergeFrom(bArr, extensionRegistryLite)).buildParsed();
        }

        public static CCMessage parseFrom(InputStream inputStream) throws IOException {
            return ((Builder) newBuilder().mergeFrom(inputStream)).buildParsed();
        }

        public static CCMessage parseFrom(InputStream inputStream, ExtensionRegistryLite extensionRegistryLite) throws IOException {
            return ((Builder) newBuilder().mergeFrom(inputStream, extensionRegistryLite)).buildParsed();
        }

        public static CCMessage parseDelimitedFrom(InputStream inputStream) throws IOException {
            Builder newBuilder = newBuilder();
            if (newBuilder.mergeDelimitedFrom(inputStream)) {
                return newBuilder.buildParsed();
            }
            return null;
        }

        public static CCMessage parseDelimitedFrom(InputStream inputStream, ExtensionRegistryLite extensionRegistryLite) throws IOException {
            Builder newBuilder = newBuilder();
            if (newBuilder.mergeDelimitedFrom(inputStream, extensionRegistryLite)) {
                return newBuilder.buildParsed();
            }
            return null;
        }

        public static CCMessage parseFrom(CodedInputStream codedInputStream) throws IOException {
            return ((Builder) newBuilder().mergeFrom(codedInputStream)).buildParsed();
        }

        public static CCMessage parseFrom(CodedInputStream codedInputStream, ExtensionRegistryLite extensionRegistryLite) throws IOException {
            return newBuilder().mergeFrom(codedInputStream, extensionRegistryLite).buildParsed();
        }

        public static Builder newBuilder() {
            return Builder.create();
        }

        public Builder newBuilderForType() {
            return newBuilder();
        }

        public static Builder newBuilder(CCMessage cCMessage) {
            return newBuilder().mergeFrom(cCMessage);
        }

        public Builder toBuilder() {
            return newBuilder(this);
        }

        static {
            defaultInstance.initFields();
        }
    }

    public interface CCMessageOrBuilder extends MessageLiteOrBuilder {
        String getContent();

        String getId();

        long getSource();

        long getSpeakerId();

        long getTime();

        boolean hasContent();

        boolean hasId();

        boolean hasSource();

        boolean hasSpeakerId();

        boolean hasTime();
    }

    public static final class ChatMessage extends GeneratedMessageLite implements ChatMessageOrBuilder {
        public static final int CONTENT_FIELD_NUMBER = 7;
        public static final int ID_FIELD_NUMBER = 1;
        public static final int ISNOTIFICATION_FIELD_NUMBER = 8;
        public static final int RECEIVERNAME_FIELD_NUMBER = 5;
        public static final int RECEIVER_FIELD_NUMBER = 3;
        public static final int SENDERNAME_FIELD_NUMBER = 4;
        public static final int SENDER_FIELD_NUMBER = 2;
        public static final int TIME_FIELD_NUMBER = 6;
        private static final ChatMessage defaultInstance = new ChatMessage(true);
        private static final long serialVersionUID = 0;
        /* access modifiers changed from: private */
        public int bitField0_;
        /* access modifiers changed from: private */
        public Object content_;
        /* access modifiers changed from: private */
        public Object id_;
        /* access modifiers changed from: private */
        public boolean isNotification_;
        private byte memoizedIsInitialized;
        private int memoizedSerializedSize;
        /* access modifiers changed from: private */
        public Object receiverName_;
        /* access modifiers changed from: private */
        public long receiver_;
        /* access modifiers changed from: private */
        public Object senderName_;
        /* access modifiers changed from: private */
        public long sender_;
        /* access modifiers changed from: private */
        public long time_;

        public static final class Builder extends com.google.protobuf.GeneratedMessageLite.Builder<ChatMessage, Builder> implements ChatMessageOrBuilder {
            private int bitField0_;
            private Object content_ = "";
            private Object id_ = "";
            private boolean isNotification_;
            private Object receiverName_ = "";
            private long receiver_;
            private Object senderName_ = "";
            private long sender_;
            private long time_;

            private void maybeForceBuilderInitialization() {
            }

            private Builder() {
                maybeForceBuilderInitialization();
            }

            /* access modifiers changed from: private */
            public static Builder create() {
                return new Builder();
            }

            public Builder clear() {
                super.clear();
                this.id_ = "";
                this.bitField0_ &= -2;
                this.sender_ = 0;
                this.bitField0_ &= -3;
                this.receiver_ = 0;
                this.bitField0_ &= -5;
                this.senderName_ = "";
                this.bitField0_ &= -9;
                this.receiverName_ = "";
                this.bitField0_ &= -17;
                this.time_ = 0;
                this.bitField0_ &= -33;
                this.content_ = "";
                this.bitField0_ &= -65;
                this.isNotification_ = false;
                this.bitField0_ &= -129;
                return this;
            }

            public Builder clone() {
                return create().mergeFrom(buildPartial());
            }

            public ChatMessage getDefaultInstanceForType() {
                return ChatMessage.getDefaultInstance();
            }

            public ChatMessage build() {
                ChatMessage buildPartial = buildPartial();
                if (buildPartial.isInitialized()) {
                    return buildPartial;
                }
                throw newUninitializedMessageException(buildPartial);
            }

            /* access modifiers changed from: private */
            public ChatMessage buildParsed() throws InvalidProtocolBufferException {
                ChatMessage buildPartial = buildPartial();
                if (buildPartial.isInitialized()) {
                    return buildPartial;
                }
                throw newUninitializedMessageException(buildPartial).asInvalidProtocolBufferException();
            }

            public ChatMessage buildPartial() {
                ChatMessage chatMessage = new ChatMessage(this);
                int i = this.bitField0_;
                int i2 = 1;
                if ((i & 1) != 1) {
                    i2 = 0;
                }
                chatMessage.id_ = this.id_;
                if ((i & 2) == 2) {
                    i2 |= 2;
                }
                chatMessage.sender_ = this.sender_;
                if ((i & 4) == 4) {
                    i2 |= 4;
                }
                chatMessage.receiver_ = this.receiver_;
                if ((i & 8) == 8) {
                    i2 |= 8;
                }
                chatMessage.senderName_ = this.senderName_;
                if ((i & 16) == 16) {
                    i2 |= 16;
                }
                chatMessage.receiverName_ = this.receiverName_;
                if ((i & 32) == 32) {
                    i2 |= 32;
                }
                chatMessage.time_ = this.time_;
                if ((i & 64) == 64) {
                    i2 |= 64;
                }
                chatMessage.content_ = this.content_;
                if ((i & 128) == 128) {
                    i2 |= 128;
                }
                chatMessage.isNotification_ = this.isNotification_;
                chatMessage.bitField0_ = i2;
                return chatMessage;
            }

            public Builder mergeFrom(ChatMessage chatMessage) {
                if (chatMessage == ChatMessage.getDefaultInstance()) {
                    return this;
                }
                if (chatMessage.hasId()) {
                    setId(chatMessage.getId());
                }
                if (chatMessage.hasSender()) {
                    setSender(chatMessage.getSender());
                }
                if (chatMessage.hasReceiver()) {
                    setReceiver(chatMessage.getReceiver());
                }
                if (chatMessage.hasSenderName()) {
                    setSenderName(chatMessage.getSenderName());
                }
                if (chatMessage.hasReceiverName()) {
                    setReceiverName(chatMessage.getReceiverName());
                }
                if (chatMessage.hasTime()) {
                    setTime(chatMessage.getTime());
                }
                if (chatMessage.hasContent()) {
                    setContent(chatMessage.getContent());
                }
                if (chatMessage.hasIsNotification()) {
                    setIsNotification(chatMessage.getIsNotification());
                }
                return this;
            }

            public final boolean isInitialized() {
                if (hasId() && hasSender() && hasReceiver() && hasSenderName() && hasReceiverName() && hasTime() && hasContent() && hasIsNotification()) {
                    return true;
                }
                return false;
            }

            public Builder mergeFrom(CodedInputStream codedInputStream, ExtensionRegistryLite extensionRegistryLite) throws IOException {
                while (true) {
                    int readTag = codedInputStream.readTag();
                    if (readTag == 0) {
                        return this;
                    }
                    if (readTag == 10) {
                        this.bitField0_ |= 1;
                        this.id_ = codedInputStream.readBytes();
                    } else if (readTag == 16) {
                        this.bitField0_ |= 2;
                        this.sender_ = codedInputStream.readInt64();
                    } else if (readTag == 24) {
                        this.bitField0_ |= 4;
                        this.receiver_ = codedInputStream.readInt64();
                    } else if (readTag == 34) {
                        this.bitField0_ |= 8;
                        this.senderName_ = codedInputStream.readBytes();
                    } else if (readTag == 42) {
                        this.bitField0_ |= 16;
                        this.receiverName_ = codedInputStream.readBytes();
                    } else if (readTag == 48) {
                        this.bitField0_ |= 32;
                        this.time_ = codedInputStream.readInt64();
                    } else if (readTag == 58) {
                        this.bitField0_ |= 64;
                        this.content_ = codedInputStream.readBytes();
                    } else if (readTag == 64) {
                        this.bitField0_ |= 128;
                        this.isNotification_ = codedInputStream.readBool();
                    } else if (!parseUnknownField(codedInputStream, extensionRegistryLite, readTag)) {
                        return this;
                    }
                }
            }

            public boolean hasId() {
                return (this.bitField0_ & 1) == 1;
            }

            public String getId() {
                Object obj = this.id_;
                if (obj instanceof String) {
                    return (String) obj;
                }
                String stringUtf8 = ((ByteString) obj).toStringUtf8();
                this.id_ = stringUtf8;
                return stringUtf8;
            }

            public Builder setId(String str) {
                if (str != null) {
                    this.bitField0_ |= 1;
                    this.id_ = str;
                    return this;
                }
                throw new NullPointerException();
            }

            public Builder clearId() {
                this.bitField0_ &= -2;
                this.id_ = ChatMessage.getDefaultInstance().getId();
                return this;
            }

            /* access modifiers changed from: 0000 */
            public void setId(ByteString byteString) {
                this.bitField0_ |= 1;
                this.id_ = byteString;
            }

            public boolean hasSender() {
                return (this.bitField0_ & 2) == 2;
            }

            public long getSender() {
                return this.sender_;
            }

            public Builder setSender(long j) {
                this.bitField0_ |= 2;
                this.sender_ = j;
                return this;
            }

            public Builder clearSender() {
                this.bitField0_ &= -3;
                this.sender_ = 0;
                return this;
            }

            public boolean hasReceiver() {
                return (this.bitField0_ & 4) == 4;
            }

            public long getReceiver() {
                return this.receiver_;
            }

            public Builder setReceiver(long j) {
                this.bitField0_ |= 4;
                this.receiver_ = j;
                return this;
            }

            public Builder clearReceiver() {
                this.bitField0_ &= -5;
                this.receiver_ = 0;
                return this;
            }

            public boolean hasSenderName() {
                return (this.bitField0_ & 8) == 8;
            }

            public String getSenderName() {
                Object obj = this.senderName_;
                if (obj instanceof String) {
                    return (String) obj;
                }
                String stringUtf8 = ((ByteString) obj).toStringUtf8();
                this.senderName_ = stringUtf8;
                return stringUtf8;
            }

            public Builder setSenderName(String str) {
                if (str != null) {
                    this.bitField0_ |= 8;
                    this.senderName_ = str;
                    return this;
                }
                throw new NullPointerException();
            }

            public Builder clearSenderName() {
                this.bitField0_ &= -9;
                this.senderName_ = ChatMessage.getDefaultInstance().getSenderName();
                return this;
            }

            /* access modifiers changed from: 0000 */
            public void setSenderName(ByteString byteString) {
                this.bitField0_ |= 8;
                this.senderName_ = byteString;
            }

            public boolean hasReceiverName() {
                return (this.bitField0_ & 16) == 16;
            }

            public String getReceiverName() {
                Object obj = this.receiverName_;
                if (obj instanceof String) {
                    return (String) obj;
                }
                String stringUtf8 = ((ByteString) obj).toStringUtf8();
                this.receiverName_ = stringUtf8;
                return stringUtf8;
            }

            public Builder setReceiverName(String str) {
                if (str != null) {
                    this.bitField0_ |= 16;
                    this.receiverName_ = str;
                    return this;
                }
                throw new NullPointerException();
            }

            public Builder clearReceiverName() {
                this.bitField0_ &= -17;
                this.receiverName_ = ChatMessage.getDefaultInstance().getReceiverName();
                return this;
            }

            /* access modifiers changed from: 0000 */
            public void setReceiverName(ByteString byteString) {
                this.bitField0_ |= 16;
                this.receiverName_ = byteString;
            }

            public boolean hasTime() {
                return (this.bitField0_ & 32) == 32;
            }

            public long getTime() {
                return this.time_;
            }

            public Builder setTime(long j) {
                this.bitField0_ |= 32;
                this.time_ = j;
                return this;
            }

            public Builder clearTime() {
                this.bitField0_ &= -33;
                this.time_ = 0;
                return this;
            }

            public boolean hasContent() {
                return (this.bitField0_ & 64) == 64;
            }

            public String getContent() {
                Object obj = this.content_;
                if (obj instanceof String) {
                    return (String) obj;
                }
                String stringUtf8 = ((ByteString) obj).toStringUtf8();
                this.content_ = stringUtf8;
                return stringUtf8;
            }

            public Builder setContent(String str) {
                if (str != null) {
                    this.bitField0_ |= 64;
                    this.content_ = str;
                    return this;
                }
                throw new NullPointerException();
            }

            public Builder clearContent() {
                this.bitField0_ &= -65;
                this.content_ = ChatMessage.getDefaultInstance().getContent();
                return this;
            }

            /* access modifiers changed from: 0000 */
            public void setContent(ByteString byteString) {
                this.bitField0_ |= 64;
                this.content_ = byteString;
            }

            public boolean hasIsNotification() {
                return (this.bitField0_ & 128) == 128;
            }

            public boolean getIsNotification() {
                return this.isNotification_;
            }

            public Builder setIsNotification(boolean z) {
                this.bitField0_ |= 128;
                this.isNotification_ = z;
                return this;
            }

            public Builder clearIsNotification() {
                this.bitField0_ &= -129;
                this.isNotification_ = false;
                return this;
            }
        }

        private ChatMessage(Builder builder) {
            super(builder);
            this.memoizedIsInitialized = -1;
            this.memoizedSerializedSize = -1;
        }

        private ChatMessage(boolean z) {
            this.memoizedIsInitialized = -1;
            this.memoizedSerializedSize = -1;
        }

        public static ChatMessage getDefaultInstance() {
            return defaultInstance;
        }

        public ChatMessage getDefaultInstanceForType() {
            return defaultInstance;
        }

        public boolean hasId() {
            return (this.bitField0_ & 1) == 1;
        }

        public String getId() {
            Object obj = this.id_;
            if (obj instanceof String) {
                return (String) obj;
            }
            ByteString byteString = (ByteString) obj;
            String stringUtf8 = byteString.toStringUtf8();
            if (Internal.isValidUtf8(byteString)) {
                this.id_ = stringUtf8;
            }
            return stringUtf8;
        }

        private ByteString getIdBytes() {
            Object obj = this.id_;
            if (!(obj instanceof String)) {
                return (ByteString) obj;
            }
            ByteString copyFromUtf8 = ByteString.copyFromUtf8((String) obj);
            this.id_ = copyFromUtf8;
            return copyFromUtf8;
        }

        public boolean hasSender() {
            return (this.bitField0_ & 2) == 2;
        }

        public long getSender() {
            return this.sender_;
        }

        public boolean hasReceiver() {
            return (this.bitField0_ & 4) == 4;
        }

        public long getReceiver() {
            return this.receiver_;
        }

        public boolean hasSenderName() {
            return (this.bitField0_ & 8) == 8;
        }

        public String getSenderName() {
            Object obj = this.senderName_;
            if (obj instanceof String) {
                return (String) obj;
            }
            ByteString byteString = (ByteString) obj;
            String stringUtf8 = byteString.toStringUtf8();
            if (Internal.isValidUtf8(byteString)) {
                this.senderName_ = stringUtf8;
            }
            return stringUtf8;
        }

        private ByteString getSenderNameBytes() {
            Object obj = this.senderName_;
            if (!(obj instanceof String)) {
                return (ByteString) obj;
            }
            ByteString copyFromUtf8 = ByteString.copyFromUtf8((String) obj);
            this.senderName_ = copyFromUtf8;
            return copyFromUtf8;
        }

        public boolean hasReceiverName() {
            return (this.bitField0_ & 16) == 16;
        }

        public String getReceiverName() {
            Object obj = this.receiverName_;
            if (obj instanceof String) {
                return (String) obj;
            }
            ByteString byteString = (ByteString) obj;
            String stringUtf8 = byteString.toStringUtf8();
            if (Internal.isValidUtf8(byteString)) {
                this.receiverName_ = stringUtf8;
            }
            return stringUtf8;
        }

        private ByteString getReceiverNameBytes() {
            Object obj = this.receiverName_;
            if (!(obj instanceof String)) {
                return (ByteString) obj;
            }
            ByteString copyFromUtf8 = ByteString.copyFromUtf8((String) obj);
            this.receiverName_ = copyFromUtf8;
            return copyFromUtf8;
        }

        public boolean hasTime() {
            return (this.bitField0_ & 32) == 32;
        }

        public long getTime() {
            return this.time_;
        }

        public boolean hasContent() {
            return (this.bitField0_ & 64) == 64;
        }

        public String getContent() {
            Object obj = this.content_;
            if (obj instanceof String) {
                return (String) obj;
            }
            ByteString byteString = (ByteString) obj;
            String stringUtf8 = byteString.toStringUtf8();
            if (Internal.isValidUtf8(byteString)) {
                this.content_ = stringUtf8;
            }
            return stringUtf8;
        }

        private ByteString getContentBytes() {
            Object obj = this.content_;
            if (!(obj instanceof String)) {
                return (ByteString) obj;
            }
            ByteString copyFromUtf8 = ByteString.copyFromUtf8((String) obj);
            this.content_ = copyFromUtf8;
            return copyFromUtf8;
        }

        public boolean hasIsNotification() {
            return (this.bitField0_ & 128) == 128;
        }

        public boolean getIsNotification() {
            return this.isNotification_;
        }

        private void initFields() {
            this.id_ = "";
            this.sender_ = 0;
            this.receiver_ = 0;
            this.senderName_ = "";
            this.receiverName_ = "";
            this.time_ = 0;
            this.content_ = "";
            this.isNotification_ = false;
        }

        public final boolean isInitialized() {
            byte b = this.memoizedIsInitialized;
            boolean z = true;
            if (b != -1) {
                if (b != 1) {
                    z = false;
                }
                return z;
            } else if (!hasId()) {
                this.memoizedIsInitialized = 0;
                return false;
            } else if (!hasSender()) {
                this.memoizedIsInitialized = 0;
                return false;
            } else if (!hasReceiver()) {
                this.memoizedIsInitialized = 0;
                return false;
            } else if (!hasSenderName()) {
                this.memoizedIsInitialized = 0;
                return false;
            } else if (!hasReceiverName()) {
                this.memoizedIsInitialized = 0;
                return false;
            } else if (!hasTime()) {
                this.memoizedIsInitialized = 0;
                return false;
            } else if (!hasContent()) {
                this.memoizedIsInitialized = 0;
                return false;
            } else if (!hasIsNotification()) {
                this.memoizedIsInitialized = 0;
                return false;
            } else {
                this.memoizedIsInitialized = 1;
                return true;
            }
        }

        public void writeTo(CodedOutputStream codedOutputStream) throws IOException {
            getSerializedSize();
            if ((this.bitField0_ & 1) == 1) {
                codedOutputStream.writeBytes(1, getIdBytes());
            }
            if ((this.bitField0_ & 2) == 2) {
                codedOutputStream.writeInt64(2, this.sender_);
            }
            if ((this.bitField0_ & 4) == 4) {
                codedOutputStream.writeInt64(3, this.receiver_);
            }
            if ((this.bitField0_ & 8) == 8) {
                codedOutputStream.writeBytes(4, getSenderNameBytes());
            }
            if ((this.bitField0_ & 16) == 16) {
                codedOutputStream.writeBytes(5, getReceiverNameBytes());
            }
            if ((this.bitField0_ & 32) == 32) {
                codedOutputStream.writeInt64(6, this.time_);
            }
            if ((this.bitField0_ & 64) == 64) {
                codedOutputStream.writeBytes(7, getContentBytes());
            }
            if ((this.bitField0_ & 128) == 128) {
                codedOutputStream.writeBool(8, this.isNotification_);
            }
        }

        public int getSerializedSize() {
            int i = this.memoizedSerializedSize;
            if (i != -1) {
                return i;
            }
            int i2 = 0;
            if ((this.bitField0_ & 1) == 1) {
                i2 = 0 + CodedOutputStream.computeBytesSize(1, getIdBytes());
            }
            if ((this.bitField0_ & 2) == 2) {
                i2 += CodedOutputStream.computeInt64Size(2, this.sender_);
            }
            if ((this.bitField0_ & 4) == 4) {
                i2 += CodedOutputStream.computeInt64Size(3, this.receiver_);
            }
            if ((this.bitField0_ & 8) == 8) {
                i2 += CodedOutputStream.computeBytesSize(4, getSenderNameBytes());
            }
            if ((this.bitField0_ & 16) == 16) {
                i2 += CodedOutputStream.computeBytesSize(5, getReceiverNameBytes());
            }
            if ((this.bitField0_ & 32) == 32) {
                i2 += CodedOutputStream.computeInt64Size(6, this.time_);
            }
            if ((this.bitField0_ & 64) == 64) {
                i2 += CodedOutputStream.computeBytesSize(7, getContentBytes());
            }
            if ((this.bitField0_ & 128) == 128) {
                i2 += CodedOutputStream.computeBoolSize(8, this.isNotification_);
            }
            this.memoizedSerializedSize = i2;
            return i2;
        }

        /* access modifiers changed from: protected */
        public Object writeReplace() throws ObjectStreamException {
            return super.writeReplace();
        }

        public static ChatMessage parseFrom(ByteString byteString) throws InvalidProtocolBufferException {
            return ((Builder) newBuilder().mergeFrom(byteString)).buildParsed();
        }

        public static ChatMessage parseFrom(ByteString byteString, ExtensionRegistryLite extensionRegistryLite) throws InvalidProtocolBufferException {
            return ((Builder) newBuilder().mergeFrom(byteString, extensionRegistryLite)).buildParsed();
        }

        public static ChatMessage parseFrom(byte[] bArr) throws InvalidProtocolBufferException {
            return ((Builder) newBuilder().mergeFrom(bArr)).buildParsed();
        }

        public static ChatMessage parseFrom(byte[] bArr, ExtensionRegistryLite extensionRegistryLite) throws InvalidProtocolBufferException {
            return ((Builder) newBuilder().mergeFrom(bArr, extensionRegistryLite)).buildParsed();
        }

        public static ChatMessage parseFrom(InputStream inputStream) throws IOException {
            return ((Builder) newBuilder().mergeFrom(inputStream)).buildParsed();
        }

        public static ChatMessage parseFrom(InputStream inputStream, ExtensionRegistryLite extensionRegistryLite) throws IOException {
            return ((Builder) newBuilder().mergeFrom(inputStream, extensionRegistryLite)).buildParsed();
        }

        public static ChatMessage parseDelimitedFrom(InputStream inputStream) throws IOException {
            Builder newBuilder = newBuilder();
            if (newBuilder.mergeDelimitedFrom(inputStream)) {
                return newBuilder.buildParsed();
            }
            return null;
        }

        public static ChatMessage parseDelimitedFrom(InputStream inputStream, ExtensionRegistryLite extensionRegistryLite) throws IOException {
            Builder newBuilder = newBuilder();
            if (newBuilder.mergeDelimitedFrom(inputStream, extensionRegistryLite)) {
                return newBuilder.buildParsed();
            }
            return null;
        }

        public static ChatMessage parseFrom(CodedInputStream codedInputStream) throws IOException {
            return ((Builder) newBuilder().mergeFrom(codedInputStream)).buildParsed();
        }

        public static ChatMessage parseFrom(CodedInputStream codedInputStream, ExtensionRegistryLite extensionRegistryLite) throws IOException {
            return newBuilder().mergeFrom(codedInputStream, extensionRegistryLite).buildParsed();
        }

        public static Builder newBuilder() {
            return Builder.create();
        }

        public Builder newBuilderForType() {
            return newBuilder();
        }

        public static Builder newBuilder(ChatMessage chatMessage) {
            return newBuilder().mergeFrom(chatMessage);
        }

        public Builder toBuilder() {
            return newBuilder(this);
        }

        static {
            defaultInstance.initFields();
        }
    }

    public interface ChatMessageOrBuilder extends MessageLiteOrBuilder {
        String getContent();

        String getId();

        boolean getIsNotification();

        long getReceiver();

        String getReceiverName();

        long getSender();

        String getSenderName();

        long getTime();

        boolean hasContent();

        boolean hasId();

        boolean hasIsNotification();

        boolean hasReceiver();

        boolean hasReceiverName();

        boolean hasSender();

        boolean hasSenderName();

        boolean hasTime();
    }

    public static final class CmmAudioStatus extends GeneratedMessageLite implements CmmAudioStatusOrBuilder {
        public static final int AUDIOTYPE_FIELD_NUMBER = 1;
        public static final int ISMUTED_FIELD_NUMBER = 3;
        public static final int ISTALKING_FIELD_NUMBER = 4;
        public static final int LEVEL_FIELD_NUMBER = 2;
        private static final CmmAudioStatus defaultInstance = new CmmAudioStatus(true);
        private static final long serialVersionUID = 0;
        /* access modifiers changed from: private */
        public long audiotype_;
        /* access modifiers changed from: private */
        public int bitField0_;
        /* access modifiers changed from: private */
        public boolean isMuted_;
        /* access modifiers changed from: private */
        public boolean isTalking_;
        /* access modifiers changed from: private */
        public long level_;
        private byte memoizedIsInitialized;
        private int memoizedSerializedSize;

        public static final class Builder extends com.google.protobuf.GeneratedMessageLite.Builder<CmmAudioStatus, Builder> implements CmmAudioStatusOrBuilder {
            private long audiotype_;
            private int bitField0_;
            private boolean isMuted_;
            private boolean isTalking_;
            private long level_;

            private void maybeForceBuilderInitialization() {
            }

            private Builder() {
                maybeForceBuilderInitialization();
            }

            /* access modifiers changed from: private */
            public static Builder create() {
                return new Builder();
            }

            public Builder clear() {
                super.clear();
                this.audiotype_ = 0;
                this.bitField0_ &= -2;
                this.level_ = 0;
                this.bitField0_ &= -3;
                this.isMuted_ = false;
                this.bitField0_ &= -5;
                this.isTalking_ = false;
                this.bitField0_ &= -9;
                return this;
            }

            public Builder clone() {
                return create().mergeFrom(buildPartial());
            }

            public CmmAudioStatus getDefaultInstanceForType() {
                return CmmAudioStatus.getDefaultInstance();
            }

            public CmmAudioStatus build() {
                CmmAudioStatus buildPartial = buildPartial();
                if (buildPartial.isInitialized()) {
                    return buildPartial;
                }
                throw newUninitializedMessageException(buildPartial);
            }

            /* access modifiers changed from: private */
            public CmmAudioStatus buildParsed() throws InvalidProtocolBufferException {
                CmmAudioStatus buildPartial = buildPartial();
                if (buildPartial.isInitialized()) {
                    return buildPartial;
                }
                throw newUninitializedMessageException(buildPartial).asInvalidProtocolBufferException();
            }

            public CmmAudioStatus buildPartial() {
                CmmAudioStatus cmmAudioStatus = new CmmAudioStatus(this);
                int i = this.bitField0_;
                int i2 = 1;
                if ((i & 1) != 1) {
                    i2 = 0;
                }
                cmmAudioStatus.audiotype_ = this.audiotype_;
                if ((i & 2) == 2) {
                    i2 |= 2;
                }
                cmmAudioStatus.level_ = this.level_;
                if ((i & 4) == 4) {
                    i2 |= 4;
                }
                cmmAudioStatus.isMuted_ = this.isMuted_;
                if ((i & 8) == 8) {
                    i2 |= 8;
                }
                cmmAudioStatus.isTalking_ = this.isTalking_;
                cmmAudioStatus.bitField0_ = i2;
                return cmmAudioStatus;
            }

            public Builder mergeFrom(CmmAudioStatus cmmAudioStatus) {
                if (cmmAudioStatus == CmmAudioStatus.getDefaultInstance()) {
                    return this;
                }
                if (cmmAudioStatus.hasAudiotype()) {
                    setAudiotype(cmmAudioStatus.getAudiotype());
                }
                if (cmmAudioStatus.hasLevel()) {
                    setLevel(cmmAudioStatus.getLevel());
                }
                if (cmmAudioStatus.hasIsMuted()) {
                    setIsMuted(cmmAudioStatus.getIsMuted());
                }
                if (cmmAudioStatus.hasIsTalking()) {
                    setIsTalking(cmmAudioStatus.getIsTalking());
                }
                return this;
            }

            public final boolean isInitialized() {
                if (hasAudiotype() && hasLevel() && hasIsMuted() && hasIsTalking()) {
                    return true;
                }
                return false;
            }

            public Builder mergeFrom(CodedInputStream codedInputStream, ExtensionRegistryLite extensionRegistryLite) throws IOException {
                while (true) {
                    int readTag = codedInputStream.readTag();
                    if (readTag == 0) {
                        return this;
                    }
                    if (readTag == 8) {
                        this.bitField0_ |= 1;
                        this.audiotype_ = codedInputStream.readInt64();
                    } else if (readTag == 16) {
                        this.bitField0_ |= 2;
                        this.level_ = codedInputStream.readInt64();
                    } else if (readTag == 24) {
                        this.bitField0_ |= 4;
                        this.isMuted_ = codedInputStream.readBool();
                    } else if (readTag == 32) {
                        this.bitField0_ |= 8;
                        this.isTalking_ = codedInputStream.readBool();
                    } else if (!parseUnknownField(codedInputStream, extensionRegistryLite, readTag)) {
                        return this;
                    }
                }
            }

            public boolean hasAudiotype() {
                return (this.bitField0_ & 1) == 1;
            }

            public long getAudiotype() {
                return this.audiotype_;
            }

            public Builder setAudiotype(long j) {
                this.bitField0_ |= 1;
                this.audiotype_ = j;
                return this;
            }

            public Builder clearAudiotype() {
                this.bitField0_ &= -2;
                this.audiotype_ = 0;
                return this;
            }

            public boolean hasLevel() {
                return (this.bitField0_ & 2) == 2;
            }

            public long getLevel() {
                return this.level_;
            }

            public Builder setLevel(long j) {
                this.bitField0_ |= 2;
                this.level_ = j;
                return this;
            }

            public Builder clearLevel() {
                this.bitField0_ &= -3;
                this.level_ = 0;
                return this;
            }

            public boolean hasIsMuted() {
                return (this.bitField0_ & 4) == 4;
            }

            public boolean getIsMuted() {
                return this.isMuted_;
            }

            public Builder setIsMuted(boolean z) {
                this.bitField0_ |= 4;
                this.isMuted_ = z;
                return this;
            }

            public Builder clearIsMuted() {
                this.bitField0_ &= -5;
                this.isMuted_ = false;
                return this;
            }

            public boolean hasIsTalking() {
                return (this.bitField0_ & 8) == 8;
            }

            public boolean getIsTalking() {
                return this.isTalking_;
            }

            public Builder setIsTalking(boolean z) {
                this.bitField0_ |= 8;
                this.isTalking_ = z;
                return this;
            }

            public Builder clearIsTalking() {
                this.bitField0_ &= -9;
                this.isTalking_ = false;
                return this;
            }
        }

        private CmmAudioStatus(Builder builder) {
            super(builder);
            this.memoizedIsInitialized = -1;
            this.memoizedSerializedSize = -1;
        }

        private CmmAudioStatus(boolean z) {
            this.memoizedIsInitialized = -1;
            this.memoizedSerializedSize = -1;
        }

        public static CmmAudioStatus getDefaultInstance() {
            return defaultInstance;
        }

        public CmmAudioStatus getDefaultInstanceForType() {
            return defaultInstance;
        }

        public boolean hasAudiotype() {
            return (this.bitField0_ & 1) == 1;
        }

        public long getAudiotype() {
            return this.audiotype_;
        }

        public boolean hasLevel() {
            return (this.bitField0_ & 2) == 2;
        }

        public long getLevel() {
            return this.level_;
        }

        public boolean hasIsMuted() {
            return (this.bitField0_ & 4) == 4;
        }

        public boolean getIsMuted() {
            return this.isMuted_;
        }

        public boolean hasIsTalking() {
            return (this.bitField0_ & 8) == 8;
        }

        public boolean getIsTalking() {
            return this.isTalking_;
        }

        private void initFields() {
            this.audiotype_ = 0;
            this.level_ = 0;
            this.isMuted_ = false;
            this.isTalking_ = false;
        }

        public final boolean isInitialized() {
            byte b = this.memoizedIsInitialized;
            boolean z = true;
            if (b != -1) {
                if (b != 1) {
                    z = false;
                }
                return z;
            } else if (!hasAudiotype()) {
                this.memoizedIsInitialized = 0;
                return false;
            } else if (!hasLevel()) {
                this.memoizedIsInitialized = 0;
                return false;
            } else if (!hasIsMuted()) {
                this.memoizedIsInitialized = 0;
                return false;
            } else if (!hasIsTalking()) {
                this.memoizedIsInitialized = 0;
                return false;
            } else {
                this.memoizedIsInitialized = 1;
                return true;
            }
        }

        public void writeTo(CodedOutputStream codedOutputStream) throws IOException {
            getSerializedSize();
            if ((this.bitField0_ & 1) == 1) {
                codedOutputStream.writeInt64(1, this.audiotype_);
            }
            if ((this.bitField0_ & 2) == 2) {
                codedOutputStream.writeInt64(2, this.level_);
            }
            if ((this.bitField0_ & 4) == 4) {
                codedOutputStream.writeBool(3, this.isMuted_);
            }
            if ((this.bitField0_ & 8) == 8) {
                codedOutputStream.writeBool(4, this.isTalking_);
            }
        }

        public int getSerializedSize() {
            int i = this.memoizedSerializedSize;
            if (i != -1) {
                return i;
            }
            int i2 = 0;
            if ((this.bitField0_ & 1) == 1) {
                i2 = 0 + CodedOutputStream.computeInt64Size(1, this.audiotype_);
            }
            if ((this.bitField0_ & 2) == 2) {
                i2 += CodedOutputStream.computeInt64Size(2, this.level_);
            }
            if ((this.bitField0_ & 4) == 4) {
                i2 += CodedOutputStream.computeBoolSize(3, this.isMuted_);
            }
            if ((this.bitField0_ & 8) == 8) {
                i2 += CodedOutputStream.computeBoolSize(4, this.isTalking_);
            }
            this.memoizedSerializedSize = i2;
            return i2;
        }

        /* access modifiers changed from: protected */
        public Object writeReplace() throws ObjectStreamException {
            return super.writeReplace();
        }

        public static CmmAudioStatus parseFrom(ByteString byteString) throws InvalidProtocolBufferException {
            return ((Builder) newBuilder().mergeFrom(byteString)).buildParsed();
        }

        public static CmmAudioStatus parseFrom(ByteString byteString, ExtensionRegistryLite extensionRegistryLite) throws InvalidProtocolBufferException {
            return ((Builder) newBuilder().mergeFrom(byteString, extensionRegistryLite)).buildParsed();
        }

        public static CmmAudioStatus parseFrom(byte[] bArr) throws InvalidProtocolBufferException {
            return ((Builder) newBuilder().mergeFrom(bArr)).buildParsed();
        }

        public static CmmAudioStatus parseFrom(byte[] bArr, ExtensionRegistryLite extensionRegistryLite) throws InvalidProtocolBufferException {
            return ((Builder) newBuilder().mergeFrom(bArr, extensionRegistryLite)).buildParsed();
        }

        public static CmmAudioStatus parseFrom(InputStream inputStream) throws IOException {
            return ((Builder) newBuilder().mergeFrom(inputStream)).buildParsed();
        }

        public static CmmAudioStatus parseFrom(InputStream inputStream, ExtensionRegistryLite extensionRegistryLite) throws IOException {
            return ((Builder) newBuilder().mergeFrom(inputStream, extensionRegistryLite)).buildParsed();
        }

        public static CmmAudioStatus parseDelimitedFrom(InputStream inputStream) throws IOException {
            Builder newBuilder = newBuilder();
            if (newBuilder.mergeDelimitedFrom(inputStream)) {
                return newBuilder.buildParsed();
            }
            return null;
        }

        public static CmmAudioStatus parseDelimitedFrom(InputStream inputStream, ExtensionRegistryLite extensionRegistryLite) throws IOException {
            Builder newBuilder = newBuilder();
            if (newBuilder.mergeDelimitedFrom(inputStream, extensionRegistryLite)) {
                return newBuilder.buildParsed();
            }
            return null;
        }

        public static CmmAudioStatus parseFrom(CodedInputStream codedInputStream) throws IOException {
            return ((Builder) newBuilder().mergeFrom(codedInputStream)).buildParsed();
        }

        public static CmmAudioStatus parseFrom(CodedInputStream codedInputStream, ExtensionRegistryLite extensionRegistryLite) throws IOException {
            return newBuilder().mergeFrom(codedInputStream, extensionRegistryLite).buildParsed();
        }

        public static Builder newBuilder() {
            return Builder.create();
        }

        public Builder newBuilderForType() {
            return newBuilder();
        }

        public static Builder newBuilder(CmmAudioStatus cmmAudioStatus) {
            return newBuilder().mergeFrom(cmmAudioStatus);
        }

        public Builder toBuilder() {
            return newBuilder(this);
        }

        static {
            defaultInstance.initFields();
        }
    }

    public interface CmmAudioStatusOrBuilder extends MessageLiteOrBuilder {
        long getAudiotype();

        boolean getIsMuted();

        boolean getIsTalking();

        long getLevel();

        boolean hasAudiotype();

        boolean hasIsMuted();

        boolean hasIsTalking();

        boolean hasLevel();
    }

    public static final class CmmShareStatus extends GeneratedMessageLite implements CmmShareStatusOrBuilder {
        public static final int ISRECEIVING_FIELD_NUMBER = 2;
        public static final int ISSHARING_FIELD_NUMBER = 1;
        private static final CmmShareStatus defaultInstance = new CmmShareStatus(true);
        private static final long serialVersionUID = 0;
        /* access modifiers changed from: private */
        public int bitField0_;
        /* access modifiers changed from: private */
        public boolean isReceiving_;
        /* access modifiers changed from: private */
        public boolean isSharing_;
        private byte memoizedIsInitialized;
        private int memoizedSerializedSize;

        public static final class Builder extends com.google.protobuf.GeneratedMessageLite.Builder<CmmShareStatus, Builder> implements CmmShareStatusOrBuilder {
            private int bitField0_;
            private boolean isReceiving_;
            private boolean isSharing_;

            private void maybeForceBuilderInitialization() {
            }

            private Builder() {
                maybeForceBuilderInitialization();
            }

            /* access modifiers changed from: private */
            public static Builder create() {
                return new Builder();
            }

            public Builder clear() {
                super.clear();
                this.isSharing_ = false;
                this.bitField0_ &= -2;
                this.isReceiving_ = false;
                this.bitField0_ &= -3;
                return this;
            }

            public Builder clone() {
                return create().mergeFrom(buildPartial());
            }

            public CmmShareStatus getDefaultInstanceForType() {
                return CmmShareStatus.getDefaultInstance();
            }

            public CmmShareStatus build() {
                CmmShareStatus buildPartial = buildPartial();
                if (buildPartial.isInitialized()) {
                    return buildPartial;
                }
                throw newUninitializedMessageException(buildPartial);
            }

            /* access modifiers changed from: private */
            public CmmShareStatus buildParsed() throws InvalidProtocolBufferException {
                CmmShareStatus buildPartial = buildPartial();
                if (buildPartial.isInitialized()) {
                    return buildPartial;
                }
                throw newUninitializedMessageException(buildPartial).asInvalidProtocolBufferException();
            }

            public CmmShareStatus buildPartial() {
                CmmShareStatus cmmShareStatus = new CmmShareStatus(this);
                int i = this.bitField0_;
                int i2 = 1;
                if ((i & 1) != 1) {
                    i2 = 0;
                }
                cmmShareStatus.isSharing_ = this.isSharing_;
                if ((i & 2) == 2) {
                    i2 |= 2;
                }
                cmmShareStatus.isReceiving_ = this.isReceiving_;
                cmmShareStatus.bitField0_ = i2;
                return cmmShareStatus;
            }

            public Builder mergeFrom(CmmShareStatus cmmShareStatus) {
                if (cmmShareStatus == CmmShareStatus.getDefaultInstance()) {
                    return this;
                }
                if (cmmShareStatus.hasIsSharing()) {
                    setIsSharing(cmmShareStatus.getIsSharing());
                }
                if (cmmShareStatus.hasIsReceiving()) {
                    setIsReceiving(cmmShareStatus.getIsReceiving());
                }
                return this;
            }

            public final boolean isInitialized() {
                if (hasIsSharing() && hasIsReceiving()) {
                    return true;
                }
                return false;
            }

            public Builder mergeFrom(CodedInputStream codedInputStream, ExtensionRegistryLite extensionRegistryLite) throws IOException {
                while (true) {
                    int readTag = codedInputStream.readTag();
                    if (readTag == 0) {
                        return this;
                    }
                    if (readTag == 8) {
                        this.bitField0_ |= 1;
                        this.isSharing_ = codedInputStream.readBool();
                    } else if (readTag == 16) {
                        this.bitField0_ |= 2;
                        this.isReceiving_ = codedInputStream.readBool();
                    } else if (!parseUnknownField(codedInputStream, extensionRegistryLite, readTag)) {
                        return this;
                    }
                }
            }

            public boolean hasIsSharing() {
                return (this.bitField0_ & 1) == 1;
            }

            public boolean getIsSharing() {
                return this.isSharing_;
            }

            public Builder setIsSharing(boolean z) {
                this.bitField0_ |= 1;
                this.isSharing_ = z;
                return this;
            }

            public Builder clearIsSharing() {
                this.bitField0_ &= -2;
                this.isSharing_ = false;
                return this;
            }

            public boolean hasIsReceiving() {
                return (this.bitField0_ & 2) == 2;
            }

            public boolean getIsReceiving() {
                return this.isReceiving_;
            }

            public Builder setIsReceiving(boolean z) {
                this.bitField0_ |= 2;
                this.isReceiving_ = z;
                return this;
            }

            public Builder clearIsReceiving() {
                this.bitField0_ &= -3;
                this.isReceiving_ = false;
                return this;
            }
        }

        private CmmShareStatus(Builder builder) {
            super(builder);
            this.memoizedIsInitialized = -1;
            this.memoizedSerializedSize = -1;
        }

        private CmmShareStatus(boolean z) {
            this.memoizedIsInitialized = -1;
            this.memoizedSerializedSize = -1;
        }

        public static CmmShareStatus getDefaultInstance() {
            return defaultInstance;
        }

        public CmmShareStatus getDefaultInstanceForType() {
            return defaultInstance;
        }

        public boolean hasIsSharing() {
            return (this.bitField0_ & 1) == 1;
        }

        public boolean getIsSharing() {
            return this.isSharing_;
        }

        public boolean hasIsReceiving() {
            return (this.bitField0_ & 2) == 2;
        }

        public boolean getIsReceiving() {
            return this.isReceiving_;
        }

        private void initFields() {
            this.isSharing_ = false;
            this.isReceiving_ = false;
        }

        public final boolean isInitialized() {
            byte b = this.memoizedIsInitialized;
            boolean z = true;
            if (b != -1) {
                if (b != 1) {
                    z = false;
                }
                return z;
            } else if (!hasIsSharing()) {
                this.memoizedIsInitialized = 0;
                return false;
            } else if (!hasIsReceiving()) {
                this.memoizedIsInitialized = 0;
                return false;
            } else {
                this.memoizedIsInitialized = 1;
                return true;
            }
        }

        public void writeTo(CodedOutputStream codedOutputStream) throws IOException {
            getSerializedSize();
            if ((this.bitField0_ & 1) == 1) {
                codedOutputStream.writeBool(1, this.isSharing_);
            }
            if ((this.bitField0_ & 2) == 2) {
                codedOutputStream.writeBool(2, this.isReceiving_);
            }
        }

        public int getSerializedSize() {
            int i = this.memoizedSerializedSize;
            if (i != -1) {
                return i;
            }
            int i2 = 0;
            if ((this.bitField0_ & 1) == 1) {
                i2 = 0 + CodedOutputStream.computeBoolSize(1, this.isSharing_);
            }
            if ((this.bitField0_ & 2) == 2) {
                i2 += CodedOutputStream.computeBoolSize(2, this.isReceiving_);
            }
            this.memoizedSerializedSize = i2;
            return i2;
        }

        /* access modifiers changed from: protected */
        public Object writeReplace() throws ObjectStreamException {
            return super.writeReplace();
        }

        public static CmmShareStatus parseFrom(ByteString byteString) throws InvalidProtocolBufferException {
            return ((Builder) newBuilder().mergeFrom(byteString)).buildParsed();
        }

        public static CmmShareStatus parseFrom(ByteString byteString, ExtensionRegistryLite extensionRegistryLite) throws InvalidProtocolBufferException {
            return ((Builder) newBuilder().mergeFrom(byteString, extensionRegistryLite)).buildParsed();
        }

        public static CmmShareStatus parseFrom(byte[] bArr) throws InvalidProtocolBufferException {
            return ((Builder) newBuilder().mergeFrom(bArr)).buildParsed();
        }

        public static CmmShareStatus parseFrom(byte[] bArr, ExtensionRegistryLite extensionRegistryLite) throws InvalidProtocolBufferException {
            return ((Builder) newBuilder().mergeFrom(bArr, extensionRegistryLite)).buildParsed();
        }

        public static CmmShareStatus parseFrom(InputStream inputStream) throws IOException {
            return ((Builder) newBuilder().mergeFrom(inputStream)).buildParsed();
        }

        public static CmmShareStatus parseFrom(InputStream inputStream, ExtensionRegistryLite extensionRegistryLite) throws IOException {
            return ((Builder) newBuilder().mergeFrom(inputStream, extensionRegistryLite)).buildParsed();
        }

        public static CmmShareStatus parseDelimitedFrom(InputStream inputStream) throws IOException {
            Builder newBuilder = newBuilder();
            if (newBuilder.mergeDelimitedFrom(inputStream)) {
                return newBuilder.buildParsed();
            }
            return null;
        }

        public static CmmShareStatus parseDelimitedFrom(InputStream inputStream, ExtensionRegistryLite extensionRegistryLite) throws IOException {
            Builder newBuilder = newBuilder();
            if (newBuilder.mergeDelimitedFrom(inputStream, extensionRegistryLite)) {
                return newBuilder.buildParsed();
            }
            return null;
        }

        public static CmmShareStatus parseFrom(CodedInputStream codedInputStream) throws IOException {
            return ((Builder) newBuilder().mergeFrom(codedInputStream)).buildParsed();
        }

        public static CmmShareStatus parseFrom(CodedInputStream codedInputStream, ExtensionRegistryLite extensionRegistryLite) throws IOException {
            return newBuilder().mergeFrom(codedInputStream, extensionRegistryLite).buildParsed();
        }

        public static Builder newBuilder() {
            return Builder.create();
        }

        public Builder newBuilderForType() {
            return newBuilder();
        }

        public static Builder newBuilder(CmmShareStatus cmmShareStatus) {
            return newBuilder().mergeFrom(cmmShareStatus);
        }

        public Builder toBuilder() {
            return newBuilder(this);
        }

        static {
            defaultInstance.initFields();
        }
    }

    public interface CmmShareStatusOrBuilder extends MessageLiteOrBuilder {
        boolean getIsReceiving();

        boolean getIsSharing();

        boolean hasIsReceiving();

        boolean hasIsSharing();
    }

    public static final class CmmVideoStatus extends GeneratedMessageLite implements CmmVideoStatusOrBuilder {
        public static final int BT_FIELD_NUMBER = 6;
        public static final int CAM_FECC_FIELD_NUMBER = 8;
        public static final int FPS_FIELD_NUMBER = 5;
        public static final int ISRECEVING_FIELD_NUMBER = 3;
        public static final int ISSENDING_FIELD_NUMBER = 2;
        public static final int ISSOURCE_FIELD_NUMBER = 1;
        public static final int RESOLUTION_FIELD_NUMBER = 4;
        public static final int VIDEOQUALITY_FIELD_NUMBER = 7;
        private static final CmmVideoStatus defaultInstance = new CmmVideoStatus(true);
        private static final long serialVersionUID = 0;
        /* access modifiers changed from: private */
        public int bitField0_;
        /* access modifiers changed from: private */
        public long bt_;
        /* access modifiers changed from: private */
        public int camFecc_;
        /* access modifiers changed from: private */
        public long fps_;
        /* access modifiers changed from: private */
        public boolean isReceving_;
        /* access modifiers changed from: private */
        public boolean isSending_;
        /* access modifiers changed from: private */
        public boolean isSource_;
        private byte memoizedIsInitialized;
        private int memoizedSerializedSize;
        /* access modifiers changed from: private */
        public long resolution_;
        /* access modifiers changed from: private */
        public int videoQuality_;

        public static final class Builder extends com.google.protobuf.GeneratedMessageLite.Builder<CmmVideoStatus, Builder> implements CmmVideoStatusOrBuilder {
            private int bitField0_;
            private long bt_;
            private int camFecc_;
            private long fps_;
            private boolean isReceving_;
            private boolean isSending_;
            private boolean isSource_;
            private long resolution_;
            private int videoQuality_;

            private void maybeForceBuilderInitialization() {
            }

            private Builder() {
                maybeForceBuilderInitialization();
            }

            /* access modifiers changed from: private */
            public static Builder create() {
                return new Builder();
            }

            public Builder clear() {
                super.clear();
                this.isSource_ = false;
                this.bitField0_ &= -2;
                this.isSending_ = false;
                this.bitField0_ &= -3;
                this.isReceving_ = false;
                this.bitField0_ &= -5;
                this.resolution_ = 0;
                this.bitField0_ &= -9;
                this.fps_ = 0;
                this.bitField0_ &= -17;
                this.bt_ = 0;
                this.bitField0_ &= -33;
                this.videoQuality_ = 0;
                this.bitField0_ &= -65;
                this.camFecc_ = 0;
                this.bitField0_ &= -129;
                return this;
            }

            public Builder clone() {
                return create().mergeFrom(buildPartial());
            }

            public CmmVideoStatus getDefaultInstanceForType() {
                return CmmVideoStatus.getDefaultInstance();
            }

            public CmmVideoStatus build() {
                CmmVideoStatus buildPartial = buildPartial();
                if (buildPartial.isInitialized()) {
                    return buildPartial;
                }
                throw newUninitializedMessageException(buildPartial);
            }

            /* access modifiers changed from: private */
            public CmmVideoStatus buildParsed() throws InvalidProtocolBufferException {
                CmmVideoStatus buildPartial = buildPartial();
                if (buildPartial.isInitialized()) {
                    return buildPartial;
                }
                throw newUninitializedMessageException(buildPartial).asInvalidProtocolBufferException();
            }

            public CmmVideoStatus buildPartial() {
                CmmVideoStatus cmmVideoStatus = new CmmVideoStatus(this);
                int i = this.bitField0_;
                int i2 = 1;
                if ((i & 1) != 1) {
                    i2 = 0;
                }
                cmmVideoStatus.isSource_ = this.isSource_;
                if ((i & 2) == 2) {
                    i2 |= 2;
                }
                cmmVideoStatus.isSending_ = this.isSending_;
                if ((i & 4) == 4) {
                    i2 |= 4;
                }
                cmmVideoStatus.isReceving_ = this.isReceving_;
                if ((i & 8) == 8) {
                    i2 |= 8;
                }
                cmmVideoStatus.resolution_ = this.resolution_;
                if ((i & 16) == 16) {
                    i2 |= 16;
                }
                cmmVideoStatus.fps_ = this.fps_;
                if ((i & 32) == 32) {
                    i2 |= 32;
                }
                cmmVideoStatus.bt_ = this.bt_;
                if ((i & 64) == 64) {
                    i2 |= 64;
                }
                cmmVideoStatus.videoQuality_ = this.videoQuality_;
                if ((i & 128) == 128) {
                    i2 |= 128;
                }
                cmmVideoStatus.camFecc_ = this.camFecc_;
                cmmVideoStatus.bitField0_ = i2;
                return cmmVideoStatus;
            }

            public Builder mergeFrom(CmmVideoStatus cmmVideoStatus) {
                if (cmmVideoStatus == CmmVideoStatus.getDefaultInstance()) {
                    return this;
                }
                if (cmmVideoStatus.hasIsSource()) {
                    setIsSource(cmmVideoStatus.getIsSource());
                }
                if (cmmVideoStatus.hasIsSending()) {
                    setIsSending(cmmVideoStatus.getIsSending());
                }
                if (cmmVideoStatus.hasIsReceving()) {
                    setIsReceving(cmmVideoStatus.getIsReceving());
                }
                if (cmmVideoStatus.hasResolution()) {
                    setResolution(cmmVideoStatus.getResolution());
                }
                if (cmmVideoStatus.hasFps()) {
                    setFps(cmmVideoStatus.getFps());
                }
                if (cmmVideoStatus.hasBt()) {
                    setBt(cmmVideoStatus.getBt());
                }
                if (cmmVideoStatus.hasVideoQuality()) {
                    setVideoQuality(cmmVideoStatus.getVideoQuality());
                }
                if (cmmVideoStatus.hasCamFecc()) {
                    setCamFecc(cmmVideoStatus.getCamFecc());
                }
                return this;
            }

            public final boolean isInitialized() {
                if (hasIsSource() && hasIsSending() && hasIsReceving() && hasResolution() && hasFps() && hasBt() && hasVideoQuality() && hasCamFecc()) {
                    return true;
                }
                return false;
            }

            public Builder mergeFrom(CodedInputStream codedInputStream, ExtensionRegistryLite extensionRegistryLite) throws IOException {
                while (true) {
                    int readTag = codedInputStream.readTag();
                    if (readTag == 0) {
                        return this;
                    }
                    if (readTag == 8) {
                        this.bitField0_ |= 1;
                        this.isSource_ = codedInputStream.readBool();
                    } else if (readTag == 16) {
                        this.bitField0_ |= 2;
                        this.isSending_ = codedInputStream.readBool();
                    } else if (readTag == 24) {
                        this.bitField0_ |= 4;
                        this.isReceving_ = codedInputStream.readBool();
                    } else if (readTag == 32) {
                        this.bitField0_ |= 8;
                        this.resolution_ = codedInputStream.readInt64();
                    } else if (readTag == 40) {
                        this.bitField0_ |= 16;
                        this.fps_ = codedInputStream.readInt64();
                    } else if (readTag == 48) {
                        this.bitField0_ |= 32;
                        this.bt_ = codedInputStream.readInt64();
                    } else if (readTag == 56) {
                        this.bitField0_ |= 64;
                        this.videoQuality_ = codedInputStream.readInt32();
                    } else if (readTag == 64) {
                        this.bitField0_ |= 128;
                        this.camFecc_ = codedInputStream.readInt32();
                    } else if (!parseUnknownField(codedInputStream, extensionRegistryLite, readTag)) {
                        return this;
                    }
                }
            }

            public boolean hasIsSource() {
                return (this.bitField0_ & 1) == 1;
            }

            public boolean getIsSource() {
                return this.isSource_;
            }

            public Builder setIsSource(boolean z) {
                this.bitField0_ |= 1;
                this.isSource_ = z;
                return this;
            }

            public Builder clearIsSource() {
                this.bitField0_ &= -2;
                this.isSource_ = false;
                return this;
            }

            public boolean hasIsSending() {
                return (this.bitField0_ & 2) == 2;
            }

            public boolean getIsSending() {
                return this.isSending_;
            }

            public Builder setIsSending(boolean z) {
                this.bitField0_ |= 2;
                this.isSending_ = z;
                return this;
            }

            public Builder clearIsSending() {
                this.bitField0_ &= -3;
                this.isSending_ = false;
                return this;
            }

            public boolean hasIsReceving() {
                return (this.bitField0_ & 4) == 4;
            }

            public boolean getIsReceving() {
                return this.isReceving_;
            }

            public Builder setIsReceving(boolean z) {
                this.bitField0_ |= 4;
                this.isReceving_ = z;
                return this;
            }

            public Builder clearIsReceving() {
                this.bitField0_ &= -5;
                this.isReceving_ = false;
                return this;
            }

            public boolean hasResolution() {
                return (this.bitField0_ & 8) == 8;
            }

            public long getResolution() {
                return this.resolution_;
            }

            public Builder setResolution(long j) {
                this.bitField0_ |= 8;
                this.resolution_ = j;
                return this;
            }

            public Builder clearResolution() {
                this.bitField0_ &= -9;
                this.resolution_ = 0;
                return this;
            }

            public boolean hasFps() {
                return (this.bitField0_ & 16) == 16;
            }

            public long getFps() {
                return this.fps_;
            }

            public Builder setFps(long j) {
                this.bitField0_ |= 16;
                this.fps_ = j;
                return this;
            }

            public Builder clearFps() {
                this.bitField0_ &= -17;
                this.fps_ = 0;
                return this;
            }

            public boolean hasBt() {
                return (this.bitField0_ & 32) == 32;
            }

            public long getBt() {
                return this.bt_;
            }

            public Builder setBt(long j) {
                this.bitField0_ |= 32;
                this.bt_ = j;
                return this;
            }

            public Builder clearBt() {
                this.bitField0_ &= -33;
                this.bt_ = 0;
                return this;
            }

            public boolean hasVideoQuality() {
                return (this.bitField0_ & 64) == 64;
            }

            public int getVideoQuality() {
                return this.videoQuality_;
            }

            public Builder setVideoQuality(int i) {
                this.bitField0_ |= 64;
                this.videoQuality_ = i;
                return this;
            }

            public Builder clearVideoQuality() {
                this.bitField0_ &= -65;
                this.videoQuality_ = 0;
                return this;
            }

            public boolean hasCamFecc() {
                return (this.bitField0_ & 128) == 128;
            }

            public int getCamFecc() {
                return this.camFecc_;
            }

            public Builder setCamFecc(int i) {
                this.bitField0_ |= 128;
                this.camFecc_ = i;
                return this;
            }

            public Builder clearCamFecc() {
                this.bitField0_ &= -129;
                this.camFecc_ = 0;
                return this;
            }
        }

        private CmmVideoStatus(Builder builder) {
            super(builder);
            this.memoizedIsInitialized = -1;
            this.memoizedSerializedSize = -1;
        }

        private CmmVideoStatus(boolean z) {
            this.memoizedIsInitialized = -1;
            this.memoizedSerializedSize = -1;
        }

        public static CmmVideoStatus getDefaultInstance() {
            return defaultInstance;
        }

        public CmmVideoStatus getDefaultInstanceForType() {
            return defaultInstance;
        }

        public boolean hasIsSource() {
            return (this.bitField0_ & 1) == 1;
        }

        public boolean getIsSource() {
            return this.isSource_;
        }

        public boolean hasIsSending() {
            return (this.bitField0_ & 2) == 2;
        }

        public boolean getIsSending() {
            return this.isSending_;
        }

        public boolean hasIsReceving() {
            return (this.bitField0_ & 4) == 4;
        }

        public boolean getIsReceving() {
            return this.isReceving_;
        }

        public boolean hasResolution() {
            return (this.bitField0_ & 8) == 8;
        }

        public long getResolution() {
            return this.resolution_;
        }

        public boolean hasFps() {
            return (this.bitField0_ & 16) == 16;
        }

        public long getFps() {
            return this.fps_;
        }

        public boolean hasBt() {
            return (this.bitField0_ & 32) == 32;
        }

        public long getBt() {
            return this.bt_;
        }

        public boolean hasVideoQuality() {
            return (this.bitField0_ & 64) == 64;
        }

        public int getVideoQuality() {
            return this.videoQuality_;
        }

        public boolean hasCamFecc() {
            return (this.bitField0_ & 128) == 128;
        }

        public int getCamFecc() {
            return this.camFecc_;
        }

        private void initFields() {
            this.isSource_ = false;
            this.isSending_ = false;
            this.isReceving_ = false;
            this.resolution_ = 0;
            this.fps_ = 0;
            this.bt_ = 0;
            this.videoQuality_ = 0;
            this.camFecc_ = 0;
        }

        public final boolean isInitialized() {
            byte b = this.memoizedIsInitialized;
            boolean z = true;
            if (b != -1) {
                if (b != 1) {
                    z = false;
                }
                return z;
            } else if (!hasIsSource()) {
                this.memoizedIsInitialized = 0;
                return false;
            } else if (!hasIsSending()) {
                this.memoizedIsInitialized = 0;
                return false;
            } else if (!hasIsReceving()) {
                this.memoizedIsInitialized = 0;
                return false;
            } else if (!hasResolution()) {
                this.memoizedIsInitialized = 0;
                return false;
            } else if (!hasFps()) {
                this.memoizedIsInitialized = 0;
                return false;
            } else if (!hasBt()) {
                this.memoizedIsInitialized = 0;
                return false;
            } else if (!hasVideoQuality()) {
                this.memoizedIsInitialized = 0;
                return false;
            } else if (!hasCamFecc()) {
                this.memoizedIsInitialized = 0;
                return false;
            } else {
                this.memoizedIsInitialized = 1;
                return true;
            }
        }

        public void writeTo(CodedOutputStream codedOutputStream) throws IOException {
            getSerializedSize();
            if ((this.bitField0_ & 1) == 1) {
                codedOutputStream.writeBool(1, this.isSource_);
            }
            if ((this.bitField0_ & 2) == 2) {
                codedOutputStream.writeBool(2, this.isSending_);
            }
            if ((this.bitField0_ & 4) == 4) {
                codedOutputStream.writeBool(3, this.isReceving_);
            }
            if ((this.bitField0_ & 8) == 8) {
                codedOutputStream.writeInt64(4, this.resolution_);
            }
            if ((this.bitField0_ & 16) == 16) {
                codedOutputStream.writeInt64(5, this.fps_);
            }
            if ((this.bitField0_ & 32) == 32) {
                codedOutputStream.writeInt64(6, this.bt_);
            }
            if ((this.bitField0_ & 64) == 64) {
                codedOutputStream.writeInt32(7, this.videoQuality_);
            }
            if ((this.bitField0_ & 128) == 128) {
                codedOutputStream.writeInt32(8, this.camFecc_);
            }
        }

        public int getSerializedSize() {
            int i = this.memoizedSerializedSize;
            if (i != -1) {
                return i;
            }
            int i2 = 0;
            if ((this.bitField0_ & 1) == 1) {
                i2 = 0 + CodedOutputStream.computeBoolSize(1, this.isSource_);
            }
            if ((this.bitField0_ & 2) == 2) {
                i2 += CodedOutputStream.computeBoolSize(2, this.isSending_);
            }
            if ((this.bitField0_ & 4) == 4) {
                i2 += CodedOutputStream.computeBoolSize(3, this.isReceving_);
            }
            if ((this.bitField0_ & 8) == 8) {
                i2 += CodedOutputStream.computeInt64Size(4, this.resolution_);
            }
            if ((this.bitField0_ & 16) == 16) {
                i2 += CodedOutputStream.computeInt64Size(5, this.fps_);
            }
            if ((this.bitField0_ & 32) == 32) {
                i2 += CodedOutputStream.computeInt64Size(6, this.bt_);
            }
            if ((this.bitField0_ & 64) == 64) {
                i2 += CodedOutputStream.computeInt32Size(7, this.videoQuality_);
            }
            if ((this.bitField0_ & 128) == 128) {
                i2 += CodedOutputStream.computeInt32Size(8, this.camFecc_);
            }
            this.memoizedSerializedSize = i2;
            return i2;
        }

        /* access modifiers changed from: protected */
        public Object writeReplace() throws ObjectStreamException {
            return super.writeReplace();
        }

        public static CmmVideoStatus parseFrom(ByteString byteString) throws InvalidProtocolBufferException {
            return ((Builder) newBuilder().mergeFrom(byteString)).buildParsed();
        }

        public static CmmVideoStatus parseFrom(ByteString byteString, ExtensionRegistryLite extensionRegistryLite) throws InvalidProtocolBufferException {
            return ((Builder) newBuilder().mergeFrom(byteString, extensionRegistryLite)).buildParsed();
        }

        public static CmmVideoStatus parseFrom(byte[] bArr) throws InvalidProtocolBufferException {
            return ((Builder) newBuilder().mergeFrom(bArr)).buildParsed();
        }

        public static CmmVideoStatus parseFrom(byte[] bArr, ExtensionRegistryLite extensionRegistryLite) throws InvalidProtocolBufferException {
            return ((Builder) newBuilder().mergeFrom(bArr, extensionRegistryLite)).buildParsed();
        }

        public static CmmVideoStatus parseFrom(InputStream inputStream) throws IOException {
            return ((Builder) newBuilder().mergeFrom(inputStream)).buildParsed();
        }

        public static CmmVideoStatus parseFrom(InputStream inputStream, ExtensionRegistryLite extensionRegistryLite) throws IOException {
            return ((Builder) newBuilder().mergeFrom(inputStream, extensionRegistryLite)).buildParsed();
        }

        public static CmmVideoStatus parseDelimitedFrom(InputStream inputStream) throws IOException {
            Builder newBuilder = newBuilder();
            if (newBuilder.mergeDelimitedFrom(inputStream)) {
                return newBuilder.buildParsed();
            }
            return null;
        }

        public static CmmVideoStatus parseDelimitedFrom(InputStream inputStream, ExtensionRegistryLite extensionRegistryLite) throws IOException {
            Builder newBuilder = newBuilder();
            if (newBuilder.mergeDelimitedFrom(inputStream, extensionRegistryLite)) {
                return newBuilder.buildParsed();
            }
            return null;
        }

        public static CmmVideoStatus parseFrom(CodedInputStream codedInputStream) throws IOException {
            return ((Builder) newBuilder().mergeFrom(codedInputStream)).buildParsed();
        }

        public static CmmVideoStatus parseFrom(CodedInputStream codedInputStream, ExtensionRegistryLite extensionRegistryLite) throws IOException {
            return newBuilder().mergeFrom(codedInputStream, extensionRegistryLite).buildParsed();
        }

        public static Builder newBuilder() {
            return Builder.create();
        }

        public Builder newBuilderForType() {
            return newBuilder();
        }

        public static Builder newBuilder(CmmVideoStatus cmmVideoStatus) {
            return newBuilder().mergeFrom(cmmVideoStatus);
        }

        public Builder toBuilder() {
            return newBuilder(this);
        }

        static {
            defaultInstance.initFields();
        }
    }

    public interface CmmVideoStatusOrBuilder extends MessageLiteOrBuilder {
        long getBt();

        int getCamFecc();

        long getFps();

        boolean getIsReceving();

        boolean getIsSending();

        boolean getIsSource();

        long getResolution();

        int getVideoQuality();

        boolean hasBt();

        boolean hasCamFecc();

        boolean hasFps();

        boolean hasIsReceving();

        boolean hasIsSending();

        boolean hasIsSource();

        boolean hasResolution();

        boolean hasVideoQuality();
    }

    public static final class VanityURLInfo extends GeneratedMessageLite implements VanityURLInfoOrBuilder {
        public static final int MEETINGNO_FIELD_NUMBER = 2;
        public static final int SAMEACCOUNT_FIELD_NUMBER = 3;
        public static final int VANITYURL_FIELD_NUMBER = 1;
        private static final VanityURLInfo defaultInstance = new VanityURLInfo(true);
        private static final long serialVersionUID = 0;
        /* access modifiers changed from: private */
        public int bitField0_;
        /* access modifiers changed from: private */
        public Object meetingNO_;
        private byte memoizedIsInitialized;
        private int memoizedSerializedSize;
        /* access modifiers changed from: private */
        public boolean sameAccount_;
        /* access modifiers changed from: private */
        public Object vanityURL_;

        public static final class Builder extends com.google.protobuf.GeneratedMessageLite.Builder<VanityURLInfo, Builder> implements VanityURLInfoOrBuilder {
            private int bitField0_;
            private Object meetingNO_ = "";
            private boolean sameAccount_;
            private Object vanityURL_ = "";

            private void maybeForceBuilderInitialization() {
            }

            private Builder() {
                maybeForceBuilderInitialization();
            }

            /* access modifiers changed from: private */
            public static Builder create() {
                return new Builder();
            }

            public Builder clear() {
                super.clear();
                this.vanityURL_ = "";
                this.bitField0_ &= -2;
                this.meetingNO_ = "";
                this.bitField0_ &= -3;
                this.sameAccount_ = false;
                this.bitField0_ &= -5;
                return this;
            }

            public Builder clone() {
                return create().mergeFrom(buildPartial());
            }

            public VanityURLInfo getDefaultInstanceForType() {
                return VanityURLInfo.getDefaultInstance();
            }

            public VanityURLInfo build() {
                VanityURLInfo buildPartial = buildPartial();
                if (buildPartial.isInitialized()) {
                    return buildPartial;
                }
                throw newUninitializedMessageException(buildPartial);
            }

            /* access modifiers changed from: private */
            public VanityURLInfo buildParsed() throws InvalidProtocolBufferException {
                VanityURLInfo buildPartial = buildPartial();
                if (buildPartial.isInitialized()) {
                    return buildPartial;
                }
                throw newUninitializedMessageException(buildPartial).asInvalidProtocolBufferException();
            }

            public VanityURLInfo buildPartial() {
                VanityURLInfo vanityURLInfo = new VanityURLInfo(this);
                int i = this.bitField0_;
                int i2 = 1;
                if ((i & 1) != 1) {
                    i2 = 0;
                }
                vanityURLInfo.vanityURL_ = this.vanityURL_;
                if ((i & 2) == 2) {
                    i2 |= 2;
                }
                vanityURLInfo.meetingNO_ = this.meetingNO_;
                if ((i & 4) == 4) {
                    i2 |= 4;
                }
                vanityURLInfo.sameAccount_ = this.sameAccount_;
                vanityURLInfo.bitField0_ = i2;
                return vanityURLInfo;
            }

            public Builder mergeFrom(VanityURLInfo vanityURLInfo) {
                if (vanityURLInfo == VanityURLInfo.getDefaultInstance()) {
                    return this;
                }
                if (vanityURLInfo.hasVanityURL()) {
                    setVanityURL(vanityURLInfo.getVanityURL());
                }
                if (vanityURLInfo.hasMeetingNO()) {
                    setMeetingNO(vanityURLInfo.getMeetingNO());
                }
                if (vanityURLInfo.hasSameAccount()) {
                    setSameAccount(vanityURLInfo.getSameAccount());
                }
                return this;
            }

            public final boolean isInitialized() {
                if (hasVanityURL() && hasMeetingNO() && hasSameAccount()) {
                    return true;
                }
                return false;
            }

            public Builder mergeFrom(CodedInputStream codedInputStream, ExtensionRegistryLite extensionRegistryLite) throws IOException {
                while (true) {
                    int readTag = codedInputStream.readTag();
                    if (readTag == 0) {
                        return this;
                    }
                    if (readTag == 10) {
                        this.bitField0_ |= 1;
                        this.vanityURL_ = codedInputStream.readBytes();
                    } else if (readTag == 18) {
                        this.bitField0_ |= 2;
                        this.meetingNO_ = codedInputStream.readBytes();
                    } else if (readTag == 24) {
                        this.bitField0_ |= 4;
                        this.sameAccount_ = codedInputStream.readBool();
                    } else if (!parseUnknownField(codedInputStream, extensionRegistryLite, readTag)) {
                        return this;
                    }
                }
            }

            public boolean hasVanityURL() {
                return (this.bitField0_ & 1) == 1;
            }

            public String getVanityURL() {
                Object obj = this.vanityURL_;
                if (obj instanceof String) {
                    return (String) obj;
                }
                String stringUtf8 = ((ByteString) obj).toStringUtf8();
                this.vanityURL_ = stringUtf8;
                return stringUtf8;
            }

            public Builder setVanityURL(String str) {
                if (str != null) {
                    this.bitField0_ |= 1;
                    this.vanityURL_ = str;
                    return this;
                }
                throw new NullPointerException();
            }

            public Builder clearVanityURL() {
                this.bitField0_ &= -2;
                this.vanityURL_ = VanityURLInfo.getDefaultInstance().getVanityURL();
                return this;
            }

            /* access modifiers changed from: 0000 */
            public void setVanityURL(ByteString byteString) {
                this.bitField0_ |= 1;
                this.vanityURL_ = byteString;
            }

            public boolean hasMeetingNO() {
                return (this.bitField0_ & 2) == 2;
            }

            public String getMeetingNO() {
                Object obj = this.meetingNO_;
                if (obj instanceof String) {
                    return (String) obj;
                }
                String stringUtf8 = ((ByteString) obj).toStringUtf8();
                this.meetingNO_ = stringUtf8;
                return stringUtf8;
            }

            public Builder setMeetingNO(String str) {
                if (str != null) {
                    this.bitField0_ |= 2;
                    this.meetingNO_ = str;
                    return this;
                }
                throw new NullPointerException();
            }

            public Builder clearMeetingNO() {
                this.bitField0_ &= -3;
                this.meetingNO_ = VanityURLInfo.getDefaultInstance().getMeetingNO();
                return this;
            }

            /* access modifiers changed from: 0000 */
            public void setMeetingNO(ByteString byteString) {
                this.bitField0_ |= 2;
                this.meetingNO_ = byteString;
            }

            public boolean hasSameAccount() {
                return (this.bitField0_ & 4) == 4;
            }

            public boolean getSameAccount() {
                return this.sameAccount_;
            }

            public Builder setSameAccount(boolean z) {
                this.bitField0_ |= 4;
                this.sameAccount_ = z;
                return this;
            }

            public Builder clearSameAccount() {
                this.bitField0_ &= -5;
                this.sameAccount_ = false;
                return this;
            }
        }

        private VanityURLInfo(Builder builder) {
            super(builder);
            this.memoizedIsInitialized = -1;
            this.memoizedSerializedSize = -1;
        }

        private VanityURLInfo(boolean z) {
            this.memoizedIsInitialized = -1;
            this.memoizedSerializedSize = -1;
        }

        public static VanityURLInfo getDefaultInstance() {
            return defaultInstance;
        }

        public VanityURLInfo getDefaultInstanceForType() {
            return defaultInstance;
        }

        public boolean hasVanityURL() {
            return (this.bitField0_ & 1) == 1;
        }

        public String getVanityURL() {
            Object obj = this.vanityURL_;
            if (obj instanceof String) {
                return (String) obj;
            }
            ByteString byteString = (ByteString) obj;
            String stringUtf8 = byteString.toStringUtf8();
            if (Internal.isValidUtf8(byteString)) {
                this.vanityURL_ = stringUtf8;
            }
            return stringUtf8;
        }

        private ByteString getVanityURLBytes() {
            Object obj = this.vanityURL_;
            if (!(obj instanceof String)) {
                return (ByteString) obj;
            }
            ByteString copyFromUtf8 = ByteString.copyFromUtf8((String) obj);
            this.vanityURL_ = copyFromUtf8;
            return copyFromUtf8;
        }

        public boolean hasMeetingNO() {
            return (this.bitField0_ & 2) == 2;
        }

        public String getMeetingNO() {
            Object obj = this.meetingNO_;
            if (obj instanceof String) {
                return (String) obj;
            }
            ByteString byteString = (ByteString) obj;
            String stringUtf8 = byteString.toStringUtf8();
            if (Internal.isValidUtf8(byteString)) {
                this.meetingNO_ = stringUtf8;
            }
            return stringUtf8;
        }

        private ByteString getMeetingNOBytes() {
            Object obj = this.meetingNO_;
            if (!(obj instanceof String)) {
                return (ByteString) obj;
            }
            ByteString copyFromUtf8 = ByteString.copyFromUtf8((String) obj);
            this.meetingNO_ = copyFromUtf8;
            return copyFromUtf8;
        }

        public boolean hasSameAccount() {
            return (this.bitField0_ & 4) == 4;
        }

        public boolean getSameAccount() {
            return this.sameAccount_;
        }

        private void initFields() {
            this.vanityURL_ = "";
            this.meetingNO_ = "";
            this.sameAccount_ = false;
        }

        public final boolean isInitialized() {
            byte b = this.memoizedIsInitialized;
            boolean z = true;
            if (b != -1) {
                if (b != 1) {
                    z = false;
                }
                return z;
            } else if (!hasVanityURL()) {
                this.memoizedIsInitialized = 0;
                return false;
            } else if (!hasMeetingNO()) {
                this.memoizedIsInitialized = 0;
                return false;
            } else if (!hasSameAccount()) {
                this.memoizedIsInitialized = 0;
                return false;
            } else {
                this.memoizedIsInitialized = 1;
                return true;
            }
        }

        public void writeTo(CodedOutputStream codedOutputStream) throws IOException {
            getSerializedSize();
            if ((this.bitField0_ & 1) == 1) {
                codedOutputStream.writeBytes(1, getVanityURLBytes());
            }
            if ((this.bitField0_ & 2) == 2) {
                codedOutputStream.writeBytes(2, getMeetingNOBytes());
            }
            if ((this.bitField0_ & 4) == 4) {
                codedOutputStream.writeBool(3, this.sameAccount_);
            }
        }

        public int getSerializedSize() {
            int i = this.memoizedSerializedSize;
            if (i != -1) {
                return i;
            }
            int i2 = 0;
            if ((this.bitField0_ & 1) == 1) {
                i2 = 0 + CodedOutputStream.computeBytesSize(1, getVanityURLBytes());
            }
            if ((this.bitField0_ & 2) == 2) {
                i2 += CodedOutputStream.computeBytesSize(2, getMeetingNOBytes());
            }
            if ((this.bitField0_ & 4) == 4) {
                i2 += CodedOutputStream.computeBoolSize(3, this.sameAccount_);
            }
            this.memoizedSerializedSize = i2;
            return i2;
        }

        /* access modifiers changed from: protected */
        public Object writeReplace() throws ObjectStreamException {
            return super.writeReplace();
        }

        public static VanityURLInfo parseFrom(ByteString byteString) throws InvalidProtocolBufferException {
            return ((Builder) newBuilder().mergeFrom(byteString)).buildParsed();
        }

        public static VanityURLInfo parseFrom(ByteString byteString, ExtensionRegistryLite extensionRegistryLite) throws InvalidProtocolBufferException {
            return ((Builder) newBuilder().mergeFrom(byteString, extensionRegistryLite)).buildParsed();
        }

        public static VanityURLInfo parseFrom(byte[] bArr) throws InvalidProtocolBufferException {
            return ((Builder) newBuilder().mergeFrom(bArr)).buildParsed();
        }

        public static VanityURLInfo parseFrom(byte[] bArr, ExtensionRegistryLite extensionRegistryLite) throws InvalidProtocolBufferException {
            return ((Builder) newBuilder().mergeFrom(bArr, extensionRegistryLite)).buildParsed();
        }

        public static VanityURLInfo parseFrom(InputStream inputStream) throws IOException {
            return ((Builder) newBuilder().mergeFrom(inputStream)).buildParsed();
        }

        public static VanityURLInfo parseFrom(InputStream inputStream, ExtensionRegistryLite extensionRegistryLite) throws IOException {
            return ((Builder) newBuilder().mergeFrom(inputStream, extensionRegistryLite)).buildParsed();
        }

        public static VanityURLInfo parseDelimitedFrom(InputStream inputStream) throws IOException {
            Builder newBuilder = newBuilder();
            if (newBuilder.mergeDelimitedFrom(inputStream)) {
                return newBuilder.buildParsed();
            }
            return null;
        }

        public static VanityURLInfo parseDelimitedFrom(InputStream inputStream, ExtensionRegistryLite extensionRegistryLite) throws IOException {
            Builder newBuilder = newBuilder();
            if (newBuilder.mergeDelimitedFrom(inputStream, extensionRegistryLite)) {
                return newBuilder.buildParsed();
            }
            return null;
        }

        public static VanityURLInfo parseFrom(CodedInputStream codedInputStream) throws IOException {
            return ((Builder) newBuilder().mergeFrom(codedInputStream)).buildParsed();
        }

        public static VanityURLInfo parseFrom(CodedInputStream codedInputStream, ExtensionRegistryLite extensionRegistryLite) throws IOException {
            return newBuilder().mergeFrom(codedInputStream, extensionRegistryLite).buildParsed();
        }

        public static Builder newBuilder() {
            return Builder.create();
        }

        public Builder newBuilderForType() {
            return newBuilder();
        }

        public static Builder newBuilder(VanityURLInfo vanityURLInfo) {
            return newBuilder().mergeFrom(vanityURLInfo);
        }

        public Builder toBuilder() {
            return newBuilder(this);
        }

        static {
            defaultInstance.initFields();
        }
    }

    public static final class VanityURLInfoList extends GeneratedMessageLite implements VanityURLInfoListOrBuilder {
        public static final int VANITYURLINFOS_FIELD_NUMBER = 1;
        private static final VanityURLInfoList defaultInstance = new VanityURLInfoList(true);
        private static final long serialVersionUID = 0;
        private byte memoizedIsInitialized;
        private int memoizedSerializedSize;
        /* access modifiers changed from: private */
        public List<VanityURLInfo> vanityURLInfos_;

        public static final class Builder extends com.google.protobuf.GeneratedMessageLite.Builder<VanityURLInfoList, Builder> implements VanityURLInfoListOrBuilder {
            private int bitField0_;
            private List<VanityURLInfo> vanityURLInfos_ = Collections.emptyList();

            private void maybeForceBuilderInitialization() {
            }

            private Builder() {
                maybeForceBuilderInitialization();
            }

            /* access modifiers changed from: private */
            public static Builder create() {
                return new Builder();
            }

            public Builder clear() {
                super.clear();
                this.vanityURLInfos_ = Collections.emptyList();
                this.bitField0_ &= -2;
                return this;
            }

            public Builder clone() {
                return create().mergeFrom(buildPartial());
            }

            public VanityURLInfoList getDefaultInstanceForType() {
                return VanityURLInfoList.getDefaultInstance();
            }

            public VanityURLInfoList build() {
                VanityURLInfoList buildPartial = buildPartial();
                if (buildPartial.isInitialized()) {
                    return buildPartial;
                }
                throw newUninitializedMessageException(buildPartial);
            }

            /* access modifiers changed from: private */
            public VanityURLInfoList buildParsed() throws InvalidProtocolBufferException {
                VanityURLInfoList buildPartial = buildPartial();
                if (buildPartial.isInitialized()) {
                    return buildPartial;
                }
                throw newUninitializedMessageException(buildPartial).asInvalidProtocolBufferException();
            }

            public VanityURLInfoList buildPartial() {
                VanityURLInfoList vanityURLInfoList = new VanityURLInfoList(this);
                if ((this.bitField0_ & 1) == 1) {
                    this.vanityURLInfos_ = Collections.unmodifiableList(this.vanityURLInfos_);
                    this.bitField0_ &= -2;
                }
                vanityURLInfoList.vanityURLInfos_ = this.vanityURLInfos_;
                return vanityURLInfoList;
            }

            public Builder mergeFrom(VanityURLInfoList vanityURLInfoList) {
                if (vanityURLInfoList != VanityURLInfoList.getDefaultInstance() && !vanityURLInfoList.vanityURLInfos_.isEmpty()) {
                    if (this.vanityURLInfos_.isEmpty()) {
                        this.vanityURLInfos_ = vanityURLInfoList.vanityURLInfos_;
                        this.bitField0_ &= -2;
                    } else {
                        ensureVanityURLInfosIsMutable();
                        this.vanityURLInfos_.addAll(vanityURLInfoList.vanityURLInfos_);
                    }
                }
                return this;
            }

            public final boolean isInitialized() {
                for (int i = 0; i < getVanityURLInfosCount(); i++) {
                    if (!getVanityURLInfos(i).isInitialized()) {
                        return false;
                    }
                }
                return true;
            }

            public Builder mergeFrom(CodedInputStream codedInputStream, ExtensionRegistryLite extensionRegistryLite) throws IOException {
                while (true) {
                    int readTag = codedInputStream.readTag();
                    if (readTag == 0) {
                        return this;
                    }
                    if (readTag == 10) {
                        Builder newBuilder = VanityURLInfo.newBuilder();
                        codedInputStream.readMessage(newBuilder, extensionRegistryLite);
                        addVanityURLInfos(newBuilder.buildPartial());
                    } else if (!parseUnknownField(codedInputStream, extensionRegistryLite, readTag)) {
                        return this;
                    }
                }
            }

            private void ensureVanityURLInfosIsMutable() {
                if ((this.bitField0_ & 1) != 1) {
                    this.vanityURLInfos_ = new ArrayList(this.vanityURLInfos_);
                    this.bitField0_ |= 1;
                }
            }

            public List<VanityURLInfo> getVanityURLInfosList() {
                return Collections.unmodifiableList(this.vanityURLInfos_);
            }

            public int getVanityURLInfosCount() {
                return this.vanityURLInfos_.size();
            }

            public VanityURLInfo getVanityURLInfos(int i) {
                return (VanityURLInfo) this.vanityURLInfos_.get(i);
            }

            public Builder setVanityURLInfos(int i, VanityURLInfo vanityURLInfo) {
                if (vanityURLInfo != null) {
                    ensureVanityURLInfosIsMutable();
                    this.vanityURLInfos_.set(i, vanityURLInfo);
                    return this;
                }
                throw new NullPointerException();
            }

            public Builder setVanityURLInfos(int i, Builder builder) {
                ensureVanityURLInfosIsMutable();
                this.vanityURLInfos_.set(i, builder.build());
                return this;
            }

            public Builder addVanityURLInfos(VanityURLInfo vanityURLInfo) {
                if (vanityURLInfo != null) {
                    ensureVanityURLInfosIsMutable();
                    this.vanityURLInfos_.add(vanityURLInfo);
                    return this;
                }
                throw new NullPointerException();
            }

            public Builder addVanityURLInfos(int i, VanityURLInfo vanityURLInfo) {
                if (vanityURLInfo != null) {
                    ensureVanityURLInfosIsMutable();
                    this.vanityURLInfos_.add(i, vanityURLInfo);
                    return this;
                }
                throw new NullPointerException();
            }

            public Builder addVanityURLInfos(Builder builder) {
                ensureVanityURLInfosIsMutable();
                this.vanityURLInfos_.add(builder.build());
                return this;
            }

            public Builder addVanityURLInfos(int i, Builder builder) {
                ensureVanityURLInfosIsMutable();
                this.vanityURLInfos_.add(i, builder.build());
                return this;
            }

            public Builder addAllVanityURLInfos(Iterable<? extends VanityURLInfo> iterable) {
                ensureVanityURLInfosIsMutable();
                com.google.protobuf.GeneratedMessageLite.Builder.addAll(iterable, this.vanityURLInfos_);
                return this;
            }

            public Builder clearVanityURLInfos() {
                this.vanityURLInfos_ = Collections.emptyList();
                this.bitField0_ &= -2;
                return this;
            }

            public Builder removeVanityURLInfos(int i) {
                ensureVanityURLInfosIsMutable();
                this.vanityURLInfos_.remove(i);
                return this;
            }
        }

        private VanityURLInfoList(Builder builder) {
            super(builder);
            this.memoizedIsInitialized = -1;
            this.memoizedSerializedSize = -1;
        }

        private VanityURLInfoList(boolean z) {
            this.memoizedIsInitialized = -1;
            this.memoizedSerializedSize = -1;
        }

        public static VanityURLInfoList getDefaultInstance() {
            return defaultInstance;
        }

        public VanityURLInfoList getDefaultInstanceForType() {
            return defaultInstance;
        }

        public List<VanityURLInfo> getVanityURLInfosList() {
            return this.vanityURLInfos_;
        }

        public List<? extends VanityURLInfoOrBuilder> getVanityURLInfosOrBuilderList() {
            return this.vanityURLInfos_;
        }

        public int getVanityURLInfosCount() {
            return this.vanityURLInfos_.size();
        }

        public VanityURLInfo getVanityURLInfos(int i) {
            return (VanityURLInfo) this.vanityURLInfos_.get(i);
        }

        public VanityURLInfoOrBuilder getVanityURLInfosOrBuilder(int i) {
            return (VanityURLInfoOrBuilder) this.vanityURLInfos_.get(i);
        }

        private void initFields() {
            this.vanityURLInfos_ = Collections.emptyList();
        }

        public final boolean isInitialized() {
            byte b = this.memoizedIsInitialized;
            boolean z = false;
            if (b != -1) {
                if (b == 1) {
                    z = true;
                }
                return z;
            }
            for (int i = 0; i < getVanityURLInfosCount(); i++) {
                if (!getVanityURLInfos(i).isInitialized()) {
                    this.memoizedIsInitialized = 0;
                    return false;
                }
            }
            this.memoizedIsInitialized = 1;
            return true;
        }

        public void writeTo(CodedOutputStream codedOutputStream) throws IOException {
            getSerializedSize();
            for (int i = 0; i < this.vanityURLInfos_.size(); i++) {
                codedOutputStream.writeMessage(1, (MessageLite) this.vanityURLInfos_.get(i));
            }
        }

        public int getSerializedSize() {
            int i = this.memoizedSerializedSize;
            if (i != -1) {
                return i;
            }
            int i2 = 0;
            for (int i3 = 0; i3 < this.vanityURLInfos_.size(); i3++) {
                i2 += CodedOutputStream.computeMessageSize(1, (MessageLite) this.vanityURLInfos_.get(i3));
            }
            this.memoizedSerializedSize = i2;
            return i2;
        }

        /* access modifiers changed from: protected */
        public Object writeReplace() throws ObjectStreamException {
            return super.writeReplace();
        }

        public static VanityURLInfoList parseFrom(ByteString byteString) throws InvalidProtocolBufferException {
            return ((Builder) newBuilder().mergeFrom(byteString)).buildParsed();
        }

        public static VanityURLInfoList parseFrom(ByteString byteString, ExtensionRegistryLite extensionRegistryLite) throws InvalidProtocolBufferException {
            return ((Builder) newBuilder().mergeFrom(byteString, extensionRegistryLite)).buildParsed();
        }

        public static VanityURLInfoList parseFrom(byte[] bArr) throws InvalidProtocolBufferException {
            return ((Builder) newBuilder().mergeFrom(bArr)).buildParsed();
        }

        public static VanityURLInfoList parseFrom(byte[] bArr, ExtensionRegistryLite extensionRegistryLite) throws InvalidProtocolBufferException {
            return ((Builder) newBuilder().mergeFrom(bArr, extensionRegistryLite)).buildParsed();
        }

        public static VanityURLInfoList parseFrom(InputStream inputStream) throws IOException {
            return ((Builder) newBuilder().mergeFrom(inputStream)).buildParsed();
        }

        public static VanityURLInfoList parseFrom(InputStream inputStream, ExtensionRegistryLite extensionRegistryLite) throws IOException {
            return ((Builder) newBuilder().mergeFrom(inputStream, extensionRegistryLite)).buildParsed();
        }

        public static VanityURLInfoList parseDelimitedFrom(InputStream inputStream) throws IOException {
            Builder newBuilder = newBuilder();
            if (newBuilder.mergeDelimitedFrom(inputStream)) {
                return newBuilder.buildParsed();
            }
            return null;
        }

        public static VanityURLInfoList parseDelimitedFrom(InputStream inputStream, ExtensionRegistryLite extensionRegistryLite) throws IOException {
            Builder newBuilder = newBuilder();
            if (newBuilder.mergeDelimitedFrom(inputStream, extensionRegistryLite)) {
                return newBuilder.buildParsed();
            }
            return null;
        }

        public static VanityURLInfoList parseFrom(CodedInputStream codedInputStream) throws IOException {
            return ((Builder) newBuilder().mergeFrom(codedInputStream)).buildParsed();
        }

        public static VanityURLInfoList parseFrom(CodedInputStream codedInputStream, ExtensionRegistryLite extensionRegistryLite) throws IOException {
            return newBuilder().mergeFrom(codedInputStream, extensionRegistryLite).buildParsed();
        }

        public static Builder newBuilder() {
            return Builder.create();
        }

        public Builder newBuilderForType() {
            return newBuilder();
        }

        public static Builder newBuilder(VanityURLInfoList vanityURLInfoList) {
            return newBuilder().mergeFrom(vanityURLInfoList);
        }

        public Builder toBuilder() {
            return newBuilder(this);
        }

        static {
            defaultInstance.initFields();
        }
    }

    public interface VanityURLInfoListOrBuilder extends MessageLiteOrBuilder {
        VanityURLInfo getVanityURLInfos(int i);

        int getVanityURLInfosCount();

        List<VanityURLInfo> getVanityURLInfosList();
    }

    public interface VanityURLInfoOrBuilder extends MessageLiteOrBuilder {
        String getMeetingNO();

        boolean getSameAccount();

        String getVanityURL();

        boolean hasMeetingNO();

        boolean hasSameAccount();

        boolean hasVanityURL();
    }

    public static void registerAllExtensions(ExtensionRegistryLite extensionRegistryLite) {
    }

    private ConfAppProtos() {
    }
}
