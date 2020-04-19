package com.zipow.videobox.ptapp;

import com.google.api.client.googleapis.media.MediaHttpDownloader;
import com.google.common.primitives.Ints;
import com.google.protobuf.ByteString;
import com.google.protobuf.CodedInputStream;
import com.google.protobuf.CodedOutputStream;
import com.google.protobuf.ExtensionRegistryLite;
import com.google.protobuf.GeneratedMessageLite;
import com.google.protobuf.Internal;
import com.google.protobuf.Internal.EnumLite;
import com.google.protobuf.Internal.EnumLiteMap;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.LazyStringArrayList;
import com.google.protobuf.LazyStringList;
import com.google.protobuf.MessageLite;
import com.google.protobuf.MessageLiteOrBuilder;
import com.google.protobuf.UnmodifiableLazyStringList;
import com.zipow.videobox.sip.CmmSIPCallFailReason;
import com.zipow.videobox.view.ConfToolbar;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectStreamException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class MeetingInfoProtos {

    public static final class AlterHost extends GeneratedMessageLite implements AlterHostOrBuilder {
        public static final int EMAIL_FIELD_NUMBER = 2;
        public static final int FIRSTNAME_FIELD_NUMBER = 3;
        public static final int HOSTID_FIELD_NUMBER = 1;
        public static final int LASTNAME_FIELD_NUMBER = 4;
        public static final int PICURL_FIELD_NUMBER = 5;
        public static final int PMI_FIELD_NUMBER = 6;
        private static final AlterHost defaultInstance = new AlterHost(true);
        private static final long serialVersionUID = 0;
        /* access modifiers changed from: private */
        public int bitField0_;
        /* access modifiers changed from: private */
        public Object email_;
        /* access modifiers changed from: private */
        public Object firstName_;
        /* access modifiers changed from: private */
        public Object hostID_;
        /* access modifiers changed from: private */
        public Object lastName_;
        private byte memoizedIsInitialized;
        private int memoizedSerializedSize;
        /* access modifiers changed from: private */
        public Object picUrl_;
        /* access modifiers changed from: private */
        public long pmi_;

        public static final class Builder extends com.google.protobuf.GeneratedMessageLite.Builder<AlterHost, Builder> implements AlterHostOrBuilder {
            private int bitField0_;
            private Object email_ = "";
            private Object firstName_ = "";
            private Object hostID_ = "";
            private Object lastName_ = "";
            private Object picUrl_ = "";
            private long pmi_;

            private void maybeForceBuilderInitialization() {
            }

            public final boolean isInitialized() {
                return true;
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
                this.hostID_ = "";
                this.bitField0_ &= -2;
                this.email_ = "";
                this.bitField0_ &= -3;
                this.firstName_ = "";
                this.bitField0_ &= -5;
                this.lastName_ = "";
                this.bitField0_ &= -9;
                this.picUrl_ = "";
                this.bitField0_ &= -17;
                this.pmi_ = 0;
                this.bitField0_ &= -33;
                return this;
            }

            public Builder clone() {
                return create().mergeFrom(buildPartial());
            }

            public AlterHost getDefaultInstanceForType() {
                return AlterHost.getDefaultInstance();
            }

            public AlterHost build() {
                AlterHost buildPartial = buildPartial();
                if (buildPartial.isInitialized()) {
                    return buildPartial;
                }
                throw newUninitializedMessageException(buildPartial);
            }

            /* access modifiers changed from: private */
            public AlterHost buildParsed() throws InvalidProtocolBufferException {
                AlterHost buildPartial = buildPartial();
                if (buildPartial.isInitialized()) {
                    return buildPartial;
                }
                throw newUninitializedMessageException(buildPartial).asInvalidProtocolBufferException();
            }

            public AlterHost buildPartial() {
                AlterHost alterHost = new AlterHost(this);
                int i = this.bitField0_;
                int i2 = 1;
                if ((i & 1) != 1) {
                    i2 = 0;
                }
                alterHost.hostID_ = this.hostID_;
                if ((i & 2) == 2) {
                    i2 |= 2;
                }
                alterHost.email_ = this.email_;
                if ((i & 4) == 4) {
                    i2 |= 4;
                }
                alterHost.firstName_ = this.firstName_;
                if ((i & 8) == 8) {
                    i2 |= 8;
                }
                alterHost.lastName_ = this.lastName_;
                if ((i & 16) == 16) {
                    i2 |= 16;
                }
                alterHost.picUrl_ = this.picUrl_;
                if ((i & 32) == 32) {
                    i2 |= 32;
                }
                alterHost.pmi_ = this.pmi_;
                alterHost.bitField0_ = i2;
                return alterHost;
            }

            public Builder mergeFrom(AlterHost alterHost) {
                if (alterHost == AlterHost.getDefaultInstance()) {
                    return this;
                }
                if (alterHost.hasHostID()) {
                    setHostID(alterHost.getHostID());
                }
                if (alterHost.hasEmail()) {
                    setEmail(alterHost.getEmail());
                }
                if (alterHost.hasFirstName()) {
                    setFirstName(alterHost.getFirstName());
                }
                if (alterHost.hasLastName()) {
                    setLastName(alterHost.getLastName());
                }
                if (alterHost.hasPicUrl()) {
                    setPicUrl(alterHost.getPicUrl());
                }
                if (alterHost.hasPmi()) {
                    setPmi(alterHost.getPmi());
                }
                return this;
            }

            public Builder mergeFrom(CodedInputStream codedInputStream, ExtensionRegistryLite extensionRegistryLite) throws IOException {
                while (true) {
                    int readTag = codedInputStream.readTag();
                    if (readTag == 0) {
                        return this;
                    }
                    if (readTag == 10) {
                        this.bitField0_ |= 1;
                        this.hostID_ = codedInputStream.readBytes();
                    } else if (readTag == 18) {
                        this.bitField0_ |= 2;
                        this.email_ = codedInputStream.readBytes();
                    } else if (readTag == 26) {
                        this.bitField0_ |= 4;
                        this.firstName_ = codedInputStream.readBytes();
                    } else if (readTag == 34) {
                        this.bitField0_ |= 8;
                        this.lastName_ = codedInputStream.readBytes();
                    } else if (readTag == 42) {
                        this.bitField0_ |= 16;
                        this.picUrl_ = codedInputStream.readBytes();
                    } else if (readTag == 48) {
                        this.bitField0_ |= 32;
                        this.pmi_ = codedInputStream.readInt64();
                    } else if (!parseUnknownField(codedInputStream, extensionRegistryLite, readTag)) {
                        return this;
                    }
                }
            }

            public boolean hasHostID() {
                return (this.bitField0_ & 1) == 1;
            }

            public String getHostID() {
                Object obj = this.hostID_;
                if (obj instanceof String) {
                    return (String) obj;
                }
                String stringUtf8 = ((ByteString) obj).toStringUtf8();
                this.hostID_ = stringUtf8;
                return stringUtf8;
            }

            public Builder setHostID(String str) {
                if (str != null) {
                    this.bitField0_ |= 1;
                    this.hostID_ = str;
                    return this;
                }
                throw new NullPointerException();
            }

            public Builder clearHostID() {
                this.bitField0_ &= -2;
                this.hostID_ = AlterHost.getDefaultInstance().getHostID();
                return this;
            }

            /* access modifiers changed from: 0000 */
            public void setHostID(ByteString byteString) {
                this.bitField0_ |= 1;
                this.hostID_ = byteString;
            }

            public boolean hasEmail() {
                return (this.bitField0_ & 2) == 2;
            }

            public String getEmail() {
                Object obj = this.email_;
                if (obj instanceof String) {
                    return (String) obj;
                }
                String stringUtf8 = ((ByteString) obj).toStringUtf8();
                this.email_ = stringUtf8;
                return stringUtf8;
            }

            public Builder setEmail(String str) {
                if (str != null) {
                    this.bitField0_ |= 2;
                    this.email_ = str;
                    return this;
                }
                throw new NullPointerException();
            }

            public Builder clearEmail() {
                this.bitField0_ &= -3;
                this.email_ = AlterHost.getDefaultInstance().getEmail();
                return this;
            }

            /* access modifiers changed from: 0000 */
            public void setEmail(ByteString byteString) {
                this.bitField0_ |= 2;
                this.email_ = byteString;
            }

            public boolean hasFirstName() {
                return (this.bitField0_ & 4) == 4;
            }

            public String getFirstName() {
                Object obj = this.firstName_;
                if (obj instanceof String) {
                    return (String) obj;
                }
                String stringUtf8 = ((ByteString) obj).toStringUtf8();
                this.firstName_ = stringUtf8;
                return stringUtf8;
            }

            public Builder setFirstName(String str) {
                if (str != null) {
                    this.bitField0_ |= 4;
                    this.firstName_ = str;
                    return this;
                }
                throw new NullPointerException();
            }

            public Builder clearFirstName() {
                this.bitField0_ &= -5;
                this.firstName_ = AlterHost.getDefaultInstance().getFirstName();
                return this;
            }

            /* access modifiers changed from: 0000 */
            public void setFirstName(ByteString byteString) {
                this.bitField0_ |= 4;
                this.firstName_ = byteString;
            }

            public boolean hasLastName() {
                return (this.bitField0_ & 8) == 8;
            }

            public String getLastName() {
                Object obj = this.lastName_;
                if (obj instanceof String) {
                    return (String) obj;
                }
                String stringUtf8 = ((ByteString) obj).toStringUtf8();
                this.lastName_ = stringUtf8;
                return stringUtf8;
            }

            public Builder setLastName(String str) {
                if (str != null) {
                    this.bitField0_ |= 8;
                    this.lastName_ = str;
                    return this;
                }
                throw new NullPointerException();
            }

            public Builder clearLastName() {
                this.bitField0_ &= -9;
                this.lastName_ = AlterHost.getDefaultInstance().getLastName();
                return this;
            }

            /* access modifiers changed from: 0000 */
            public void setLastName(ByteString byteString) {
                this.bitField0_ |= 8;
                this.lastName_ = byteString;
            }

            public boolean hasPicUrl() {
                return (this.bitField0_ & 16) == 16;
            }

            public String getPicUrl() {
                Object obj = this.picUrl_;
                if (obj instanceof String) {
                    return (String) obj;
                }
                String stringUtf8 = ((ByteString) obj).toStringUtf8();
                this.picUrl_ = stringUtf8;
                return stringUtf8;
            }

            public Builder setPicUrl(String str) {
                if (str != null) {
                    this.bitField0_ |= 16;
                    this.picUrl_ = str;
                    return this;
                }
                throw new NullPointerException();
            }

            public Builder clearPicUrl() {
                this.bitField0_ &= -17;
                this.picUrl_ = AlterHost.getDefaultInstance().getPicUrl();
                return this;
            }

            /* access modifiers changed from: 0000 */
            public void setPicUrl(ByteString byteString) {
                this.bitField0_ |= 16;
                this.picUrl_ = byteString;
            }

            public boolean hasPmi() {
                return (this.bitField0_ & 32) == 32;
            }

            public long getPmi() {
                return this.pmi_;
            }

            public Builder setPmi(long j) {
                this.bitField0_ |= 32;
                this.pmi_ = j;
                return this;
            }

            public Builder clearPmi() {
                this.bitField0_ &= -33;
                this.pmi_ = 0;
                return this;
            }
        }

        private AlterHost(Builder builder) {
            super(builder);
            this.memoizedIsInitialized = -1;
            this.memoizedSerializedSize = -1;
        }

        private AlterHost(boolean z) {
            this.memoizedIsInitialized = -1;
            this.memoizedSerializedSize = -1;
        }

        public static AlterHost getDefaultInstance() {
            return defaultInstance;
        }

        public AlterHost getDefaultInstanceForType() {
            return defaultInstance;
        }

        public boolean hasHostID() {
            return (this.bitField0_ & 1) == 1;
        }

        public String getHostID() {
            Object obj = this.hostID_;
            if (obj instanceof String) {
                return (String) obj;
            }
            ByteString byteString = (ByteString) obj;
            String stringUtf8 = byteString.toStringUtf8();
            if (Internal.isValidUtf8(byteString)) {
                this.hostID_ = stringUtf8;
            }
            return stringUtf8;
        }

        private ByteString getHostIDBytes() {
            Object obj = this.hostID_;
            if (!(obj instanceof String)) {
                return (ByteString) obj;
            }
            ByteString copyFromUtf8 = ByteString.copyFromUtf8((String) obj);
            this.hostID_ = copyFromUtf8;
            return copyFromUtf8;
        }

        public boolean hasEmail() {
            return (this.bitField0_ & 2) == 2;
        }

        public String getEmail() {
            Object obj = this.email_;
            if (obj instanceof String) {
                return (String) obj;
            }
            ByteString byteString = (ByteString) obj;
            String stringUtf8 = byteString.toStringUtf8();
            if (Internal.isValidUtf8(byteString)) {
                this.email_ = stringUtf8;
            }
            return stringUtf8;
        }

        private ByteString getEmailBytes() {
            Object obj = this.email_;
            if (!(obj instanceof String)) {
                return (ByteString) obj;
            }
            ByteString copyFromUtf8 = ByteString.copyFromUtf8((String) obj);
            this.email_ = copyFromUtf8;
            return copyFromUtf8;
        }

        public boolean hasFirstName() {
            return (this.bitField0_ & 4) == 4;
        }

        public String getFirstName() {
            Object obj = this.firstName_;
            if (obj instanceof String) {
                return (String) obj;
            }
            ByteString byteString = (ByteString) obj;
            String stringUtf8 = byteString.toStringUtf8();
            if (Internal.isValidUtf8(byteString)) {
                this.firstName_ = stringUtf8;
            }
            return stringUtf8;
        }

        private ByteString getFirstNameBytes() {
            Object obj = this.firstName_;
            if (!(obj instanceof String)) {
                return (ByteString) obj;
            }
            ByteString copyFromUtf8 = ByteString.copyFromUtf8((String) obj);
            this.firstName_ = copyFromUtf8;
            return copyFromUtf8;
        }

        public boolean hasLastName() {
            return (this.bitField0_ & 8) == 8;
        }

        public String getLastName() {
            Object obj = this.lastName_;
            if (obj instanceof String) {
                return (String) obj;
            }
            ByteString byteString = (ByteString) obj;
            String stringUtf8 = byteString.toStringUtf8();
            if (Internal.isValidUtf8(byteString)) {
                this.lastName_ = stringUtf8;
            }
            return stringUtf8;
        }

        private ByteString getLastNameBytes() {
            Object obj = this.lastName_;
            if (!(obj instanceof String)) {
                return (ByteString) obj;
            }
            ByteString copyFromUtf8 = ByteString.copyFromUtf8((String) obj);
            this.lastName_ = copyFromUtf8;
            return copyFromUtf8;
        }

        public boolean hasPicUrl() {
            return (this.bitField0_ & 16) == 16;
        }

        public String getPicUrl() {
            Object obj = this.picUrl_;
            if (obj instanceof String) {
                return (String) obj;
            }
            ByteString byteString = (ByteString) obj;
            String stringUtf8 = byteString.toStringUtf8();
            if (Internal.isValidUtf8(byteString)) {
                this.picUrl_ = stringUtf8;
            }
            return stringUtf8;
        }

        private ByteString getPicUrlBytes() {
            Object obj = this.picUrl_;
            if (!(obj instanceof String)) {
                return (ByteString) obj;
            }
            ByteString copyFromUtf8 = ByteString.copyFromUtf8((String) obj);
            this.picUrl_ = copyFromUtf8;
            return copyFromUtf8;
        }

        public boolean hasPmi() {
            return (this.bitField0_ & 32) == 32;
        }

        public long getPmi() {
            return this.pmi_;
        }

        private void initFields() {
            this.hostID_ = "";
            this.email_ = "";
            this.firstName_ = "";
            this.lastName_ = "";
            this.picUrl_ = "";
            this.pmi_ = 0;
        }

        public final boolean isInitialized() {
            byte b = this.memoizedIsInitialized;
            boolean z = true;
            if (b != -1) {
                if (b != 1) {
                    z = false;
                }
                return z;
            }
            this.memoizedIsInitialized = 1;
            return true;
        }

        public void writeTo(CodedOutputStream codedOutputStream) throws IOException {
            getSerializedSize();
            if ((this.bitField0_ & 1) == 1) {
                codedOutputStream.writeBytes(1, getHostIDBytes());
            }
            if ((this.bitField0_ & 2) == 2) {
                codedOutputStream.writeBytes(2, getEmailBytes());
            }
            if ((this.bitField0_ & 4) == 4) {
                codedOutputStream.writeBytes(3, getFirstNameBytes());
            }
            if ((this.bitField0_ & 8) == 8) {
                codedOutputStream.writeBytes(4, getLastNameBytes());
            }
            if ((this.bitField0_ & 16) == 16) {
                codedOutputStream.writeBytes(5, getPicUrlBytes());
            }
            if ((this.bitField0_ & 32) == 32) {
                codedOutputStream.writeInt64(6, this.pmi_);
            }
        }

        public int getSerializedSize() {
            int i = this.memoizedSerializedSize;
            if (i != -1) {
                return i;
            }
            int i2 = 0;
            if ((this.bitField0_ & 1) == 1) {
                i2 = 0 + CodedOutputStream.computeBytesSize(1, getHostIDBytes());
            }
            if ((this.bitField0_ & 2) == 2) {
                i2 += CodedOutputStream.computeBytesSize(2, getEmailBytes());
            }
            if ((this.bitField0_ & 4) == 4) {
                i2 += CodedOutputStream.computeBytesSize(3, getFirstNameBytes());
            }
            if ((this.bitField0_ & 8) == 8) {
                i2 += CodedOutputStream.computeBytesSize(4, getLastNameBytes());
            }
            if ((this.bitField0_ & 16) == 16) {
                i2 += CodedOutputStream.computeBytesSize(5, getPicUrlBytes());
            }
            if ((this.bitField0_ & 32) == 32) {
                i2 += CodedOutputStream.computeInt64Size(6, this.pmi_);
            }
            this.memoizedSerializedSize = i2;
            return i2;
        }

        /* access modifiers changed from: protected */
        public Object writeReplace() throws ObjectStreamException {
            return super.writeReplace();
        }

        public static AlterHost parseFrom(ByteString byteString) throws InvalidProtocolBufferException {
            return ((Builder) newBuilder().mergeFrom(byteString)).buildParsed();
        }

        public static AlterHost parseFrom(ByteString byteString, ExtensionRegistryLite extensionRegistryLite) throws InvalidProtocolBufferException {
            return ((Builder) newBuilder().mergeFrom(byteString, extensionRegistryLite)).buildParsed();
        }

        public static AlterHost parseFrom(byte[] bArr) throws InvalidProtocolBufferException {
            return ((Builder) newBuilder().mergeFrom(bArr)).buildParsed();
        }

        public static AlterHost parseFrom(byte[] bArr, ExtensionRegistryLite extensionRegistryLite) throws InvalidProtocolBufferException {
            return ((Builder) newBuilder().mergeFrom(bArr, extensionRegistryLite)).buildParsed();
        }

        public static AlterHost parseFrom(InputStream inputStream) throws IOException {
            return ((Builder) newBuilder().mergeFrom(inputStream)).buildParsed();
        }

        public static AlterHost parseFrom(InputStream inputStream, ExtensionRegistryLite extensionRegistryLite) throws IOException {
            return ((Builder) newBuilder().mergeFrom(inputStream, extensionRegistryLite)).buildParsed();
        }

        public static AlterHost parseDelimitedFrom(InputStream inputStream) throws IOException {
            Builder newBuilder = newBuilder();
            if (newBuilder.mergeDelimitedFrom(inputStream)) {
                return newBuilder.buildParsed();
            }
            return null;
        }

        public static AlterHost parseDelimitedFrom(InputStream inputStream, ExtensionRegistryLite extensionRegistryLite) throws IOException {
            Builder newBuilder = newBuilder();
            if (newBuilder.mergeDelimitedFrom(inputStream, extensionRegistryLite)) {
                return newBuilder.buildParsed();
            }
            return null;
        }

        public static AlterHost parseFrom(CodedInputStream codedInputStream) throws IOException {
            return ((Builder) newBuilder().mergeFrom(codedInputStream)).buildParsed();
        }

        public static AlterHost parseFrom(CodedInputStream codedInputStream, ExtensionRegistryLite extensionRegistryLite) throws IOException {
            return newBuilder().mergeFrom(codedInputStream, extensionRegistryLite).buildParsed();
        }

        public static Builder newBuilder() {
            return Builder.create();
        }

        public Builder newBuilderForType() {
            return newBuilder();
        }

        public static Builder newBuilder(AlterHost alterHost) {
            return newBuilder().mergeFrom(alterHost);
        }

        public Builder toBuilder() {
            return newBuilder(this);
        }

        static {
            defaultInstance.initFields();
        }
    }

    public interface AlterHostOrBuilder extends MessageLiteOrBuilder {
        String getEmail();

        String getFirstName();

        String getHostID();

        String getLastName();

        String getPicUrl();

        long getPmi();

        boolean hasEmail();

        boolean hasFirstName();

        boolean hasHostID();

        boolean hasLastName();

        boolean hasPicUrl();

        boolean hasPmi();
    }

    public static final class AuthProto extends GeneratedMessageLite implements AuthProtoOrBuilder {
        public static final int AUTHDOMAIN_FIELD_NUMBER = 4;
        public static final int AUTHID_FIELD_NUMBER = 1;
        public static final int AUTHNAME_FIELD_NUMBER = 2;
        public static final int AUTHTYPE_FIELD_NUMBER = 3;
        private static final AuthProto defaultInstance = new AuthProto(true);
        private static final long serialVersionUID = 0;
        /* access modifiers changed from: private */
        public Object authDomain_;
        /* access modifiers changed from: private */
        public Object authId_;
        /* access modifiers changed from: private */
        public Object authName_;
        /* access modifiers changed from: private */
        public int authType_;
        /* access modifiers changed from: private */
        public int bitField0_;
        private byte memoizedIsInitialized;
        private int memoizedSerializedSize;

        public static final class Builder extends com.google.protobuf.GeneratedMessageLite.Builder<AuthProto, Builder> implements AuthProtoOrBuilder {
            private Object authDomain_ = "";
            private Object authId_ = "";
            private Object authName_ = "";
            private int authType_;
            private int bitField0_;

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
                this.authId_ = "";
                this.bitField0_ &= -2;
                this.authName_ = "";
                this.bitField0_ &= -3;
                this.authType_ = 0;
                this.bitField0_ &= -5;
                this.authDomain_ = "";
                this.bitField0_ &= -9;
                return this;
            }

            public Builder clone() {
                return create().mergeFrom(buildPartial());
            }

            public AuthProto getDefaultInstanceForType() {
                return AuthProto.getDefaultInstance();
            }

            public AuthProto build() {
                AuthProto buildPartial = buildPartial();
                if (buildPartial.isInitialized()) {
                    return buildPartial;
                }
                throw newUninitializedMessageException(buildPartial);
            }

            /* access modifiers changed from: private */
            public AuthProto buildParsed() throws InvalidProtocolBufferException {
                AuthProto buildPartial = buildPartial();
                if (buildPartial.isInitialized()) {
                    return buildPartial;
                }
                throw newUninitializedMessageException(buildPartial).asInvalidProtocolBufferException();
            }

            public AuthProto buildPartial() {
                AuthProto authProto = new AuthProto(this);
                int i = this.bitField0_;
                int i2 = 1;
                if ((i & 1) != 1) {
                    i2 = 0;
                }
                authProto.authId_ = this.authId_;
                if ((i & 2) == 2) {
                    i2 |= 2;
                }
                authProto.authName_ = this.authName_;
                if ((i & 4) == 4) {
                    i2 |= 4;
                }
                authProto.authType_ = this.authType_;
                if ((i & 8) == 8) {
                    i2 |= 8;
                }
                authProto.authDomain_ = this.authDomain_;
                authProto.bitField0_ = i2;
                return authProto;
            }

            public Builder mergeFrom(AuthProto authProto) {
                if (authProto == AuthProto.getDefaultInstance()) {
                    return this;
                }
                if (authProto.hasAuthId()) {
                    setAuthId(authProto.getAuthId());
                }
                if (authProto.hasAuthName()) {
                    setAuthName(authProto.getAuthName());
                }
                if (authProto.hasAuthType()) {
                    setAuthType(authProto.getAuthType());
                }
                if (authProto.hasAuthDomain()) {
                    setAuthDomain(authProto.getAuthDomain());
                }
                return this;
            }

            public final boolean isInitialized() {
                return hasAuthType();
            }

            public Builder mergeFrom(CodedInputStream codedInputStream, ExtensionRegistryLite extensionRegistryLite) throws IOException {
                while (true) {
                    int readTag = codedInputStream.readTag();
                    if (readTag == 0) {
                        return this;
                    }
                    if (readTag == 10) {
                        this.bitField0_ |= 1;
                        this.authId_ = codedInputStream.readBytes();
                    } else if (readTag == 18) {
                        this.bitField0_ |= 2;
                        this.authName_ = codedInputStream.readBytes();
                    } else if (readTag == 24) {
                        this.bitField0_ |= 4;
                        this.authType_ = codedInputStream.readInt32();
                    } else if (readTag == 34) {
                        this.bitField0_ |= 8;
                        this.authDomain_ = codedInputStream.readBytes();
                    } else if (!parseUnknownField(codedInputStream, extensionRegistryLite, readTag)) {
                        return this;
                    }
                }
            }

            public boolean hasAuthId() {
                return (this.bitField0_ & 1) == 1;
            }

            public String getAuthId() {
                Object obj = this.authId_;
                if (obj instanceof String) {
                    return (String) obj;
                }
                String stringUtf8 = ((ByteString) obj).toStringUtf8();
                this.authId_ = stringUtf8;
                return stringUtf8;
            }

            public Builder setAuthId(String str) {
                if (str != null) {
                    this.bitField0_ |= 1;
                    this.authId_ = str;
                    return this;
                }
                throw new NullPointerException();
            }

            public Builder clearAuthId() {
                this.bitField0_ &= -2;
                this.authId_ = AuthProto.getDefaultInstance().getAuthId();
                return this;
            }

            /* access modifiers changed from: 0000 */
            public void setAuthId(ByteString byteString) {
                this.bitField0_ |= 1;
                this.authId_ = byteString;
            }

            public boolean hasAuthName() {
                return (this.bitField0_ & 2) == 2;
            }

            public String getAuthName() {
                Object obj = this.authName_;
                if (obj instanceof String) {
                    return (String) obj;
                }
                String stringUtf8 = ((ByteString) obj).toStringUtf8();
                this.authName_ = stringUtf8;
                return stringUtf8;
            }

            public Builder setAuthName(String str) {
                if (str != null) {
                    this.bitField0_ |= 2;
                    this.authName_ = str;
                    return this;
                }
                throw new NullPointerException();
            }

            public Builder clearAuthName() {
                this.bitField0_ &= -3;
                this.authName_ = AuthProto.getDefaultInstance().getAuthName();
                return this;
            }

            /* access modifiers changed from: 0000 */
            public void setAuthName(ByteString byteString) {
                this.bitField0_ |= 2;
                this.authName_ = byteString;
            }

            public boolean hasAuthType() {
                return (this.bitField0_ & 4) == 4;
            }

            public int getAuthType() {
                return this.authType_;
            }

            public Builder setAuthType(int i) {
                this.bitField0_ |= 4;
                this.authType_ = i;
                return this;
            }

            public Builder clearAuthType() {
                this.bitField0_ &= -5;
                this.authType_ = 0;
                return this;
            }

            public boolean hasAuthDomain() {
                return (this.bitField0_ & 8) == 8;
            }

            public String getAuthDomain() {
                Object obj = this.authDomain_;
                if (obj instanceof String) {
                    return (String) obj;
                }
                String stringUtf8 = ((ByteString) obj).toStringUtf8();
                this.authDomain_ = stringUtf8;
                return stringUtf8;
            }

            public Builder setAuthDomain(String str) {
                if (str != null) {
                    this.bitField0_ |= 8;
                    this.authDomain_ = str;
                    return this;
                }
                throw new NullPointerException();
            }

            public Builder clearAuthDomain() {
                this.bitField0_ &= -9;
                this.authDomain_ = AuthProto.getDefaultInstance().getAuthDomain();
                return this;
            }

            /* access modifiers changed from: 0000 */
            public void setAuthDomain(ByteString byteString) {
                this.bitField0_ |= 8;
                this.authDomain_ = byteString;
            }
        }

        private AuthProto(Builder builder) {
            super(builder);
            this.memoizedIsInitialized = -1;
            this.memoizedSerializedSize = -1;
        }

        private AuthProto(boolean z) {
            this.memoizedIsInitialized = -1;
            this.memoizedSerializedSize = -1;
        }

        public static AuthProto getDefaultInstance() {
            return defaultInstance;
        }

        public AuthProto getDefaultInstanceForType() {
            return defaultInstance;
        }

        public boolean hasAuthId() {
            return (this.bitField0_ & 1) == 1;
        }

        public String getAuthId() {
            Object obj = this.authId_;
            if (obj instanceof String) {
                return (String) obj;
            }
            ByteString byteString = (ByteString) obj;
            String stringUtf8 = byteString.toStringUtf8();
            if (Internal.isValidUtf8(byteString)) {
                this.authId_ = stringUtf8;
            }
            return stringUtf8;
        }

        private ByteString getAuthIdBytes() {
            Object obj = this.authId_;
            if (!(obj instanceof String)) {
                return (ByteString) obj;
            }
            ByteString copyFromUtf8 = ByteString.copyFromUtf8((String) obj);
            this.authId_ = copyFromUtf8;
            return copyFromUtf8;
        }

        public boolean hasAuthName() {
            return (this.bitField0_ & 2) == 2;
        }

        public String getAuthName() {
            Object obj = this.authName_;
            if (obj instanceof String) {
                return (String) obj;
            }
            ByteString byteString = (ByteString) obj;
            String stringUtf8 = byteString.toStringUtf8();
            if (Internal.isValidUtf8(byteString)) {
                this.authName_ = stringUtf8;
            }
            return stringUtf8;
        }

        private ByteString getAuthNameBytes() {
            Object obj = this.authName_;
            if (!(obj instanceof String)) {
                return (ByteString) obj;
            }
            ByteString copyFromUtf8 = ByteString.copyFromUtf8((String) obj);
            this.authName_ = copyFromUtf8;
            return copyFromUtf8;
        }

        public boolean hasAuthType() {
            return (this.bitField0_ & 4) == 4;
        }

        public int getAuthType() {
            return this.authType_;
        }

        public boolean hasAuthDomain() {
            return (this.bitField0_ & 8) == 8;
        }

        public String getAuthDomain() {
            Object obj = this.authDomain_;
            if (obj instanceof String) {
                return (String) obj;
            }
            ByteString byteString = (ByteString) obj;
            String stringUtf8 = byteString.toStringUtf8();
            if (Internal.isValidUtf8(byteString)) {
                this.authDomain_ = stringUtf8;
            }
            return stringUtf8;
        }

        private ByteString getAuthDomainBytes() {
            Object obj = this.authDomain_;
            if (!(obj instanceof String)) {
                return (ByteString) obj;
            }
            ByteString copyFromUtf8 = ByteString.copyFromUtf8((String) obj);
            this.authDomain_ = copyFromUtf8;
            return copyFromUtf8;
        }

        private void initFields() {
            this.authId_ = "";
            this.authName_ = "";
            this.authType_ = 0;
            this.authDomain_ = "";
        }

        public final boolean isInitialized() {
            byte b = this.memoizedIsInitialized;
            boolean z = false;
            if (b != -1) {
                if (b == 1) {
                    z = true;
                }
                return z;
            } else if (!hasAuthType()) {
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
                codedOutputStream.writeBytes(1, getAuthIdBytes());
            }
            if ((this.bitField0_ & 2) == 2) {
                codedOutputStream.writeBytes(2, getAuthNameBytes());
            }
            if ((this.bitField0_ & 4) == 4) {
                codedOutputStream.writeInt32(3, this.authType_);
            }
            if ((this.bitField0_ & 8) == 8) {
                codedOutputStream.writeBytes(4, getAuthDomainBytes());
            }
        }

        public int getSerializedSize() {
            int i = this.memoizedSerializedSize;
            if (i != -1) {
                return i;
            }
            int i2 = 0;
            if ((this.bitField0_ & 1) == 1) {
                i2 = 0 + CodedOutputStream.computeBytesSize(1, getAuthIdBytes());
            }
            if ((this.bitField0_ & 2) == 2) {
                i2 += CodedOutputStream.computeBytesSize(2, getAuthNameBytes());
            }
            if ((this.bitField0_ & 4) == 4) {
                i2 += CodedOutputStream.computeInt32Size(3, this.authType_);
            }
            if ((this.bitField0_ & 8) == 8) {
                i2 += CodedOutputStream.computeBytesSize(4, getAuthDomainBytes());
            }
            this.memoizedSerializedSize = i2;
            return i2;
        }

        /* access modifiers changed from: protected */
        public Object writeReplace() throws ObjectStreamException {
            return super.writeReplace();
        }

        public static AuthProto parseFrom(ByteString byteString) throws InvalidProtocolBufferException {
            return ((Builder) newBuilder().mergeFrom(byteString)).buildParsed();
        }

        public static AuthProto parseFrom(ByteString byteString, ExtensionRegistryLite extensionRegistryLite) throws InvalidProtocolBufferException {
            return ((Builder) newBuilder().mergeFrom(byteString, extensionRegistryLite)).buildParsed();
        }

        public static AuthProto parseFrom(byte[] bArr) throws InvalidProtocolBufferException {
            return ((Builder) newBuilder().mergeFrom(bArr)).buildParsed();
        }

        public static AuthProto parseFrom(byte[] bArr, ExtensionRegistryLite extensionRegistryLite) throws InvalidProtocolBufferException {
            return ((Builder) newBuilder().mergeFrom(bArr, extensionRegistryLite)).buildParsed();
        }

        public static AuthProto parseFrom(InputStream inputStream) throws IOException {
            return ((Builder) newBuilder().mergeFrom(inputStream)).buildParsed();
        }

        public static AuthProto parseFrom(InputStream inputStream, ExtensionRegistryLite extensionRegistryLite) throws IOException {
            return ((Builder) newBuilder().mergeFrom(inputStream, extensionRegistryLite)).buildParsed();
        }

        public static AuthProto parseDelimitedFrom(InputStream inputStream) throws IOException {
            Builder newBuilder = newBuilder();
            if (newBuilder.mergeDelimitedFrom(inputStream)) {
                return newBuilder.buildParsed();
            }
            return null;
        }

        public static AuthProto parseDelimitedFrom(InputStream inputStream, ExtensionRegistryLite extensionRegistryLite) throws IOException {
            Builder newBuilder = newBuilder();
            if (newBuilder.mergeDelimitedFrom(inputStream, extensionRegistryLite)) {
                return newBuilder.buildParsed();
            }
            return null;
        }

        public static AuthProto parseFrom(CodedInputStream codedInputStream) throws IOException {
            return ((Builder) newBuilder().mergeFrom(codedInputStream)).buildParsed();
        }

        public static AuthProto parseFrom(CodedInputStream codedInputStream, ExtensionRegistryLite extensionRegistryLite) throws IOException {
            return newBuilder().mergeFrom(codedInputStream, extensionRegistryLite).buildParsed();
        }

        public static Builder newBuilder() {
            return Builder.create();
        }

        public Builder newBuilderForType() {
            return newBuilder();
        }

        public static Builder newBuilder(AuthProto authProto) {
            return newBuilder().mergeFrom(authProto);
        }

        public Builder toBuilder() {
            return newBuilder(this);
        }

        static {
            defaultInstance.initFields();
        }
    }

    public interface AuthProtoOrBuilder extends MessageLiteOrBuilder {
        String getAuthDomain();

        String getAuthId();

        String getAuthName();

        int getAuthType();

        boolean hasAuthDomain();

        boolean hasAuthId();

        boolean hasAuthName();

        boolean hasAuthType();
    }

    public static final class AvailableDialinCountry extends GeneratedMessageLite implements AvailableDialinCountryOrBuilder {
        public static final int ALLCOUNTRIES_FIELD_NUMBER = 4;
        public static final int ENABLESHOWINCLUDETOLLFREE_FIELD_NUMBER = 5;
        public static final int HASH_FIELD_NUMBER = 1;
        public static final int INCLUDEDTOLLFREE_FIELD_NUMBER = 2;
        public static final int SELECTEDCOUNTRIES_FIELD_NUMBER = 3;
        private static final AvailableDialinCountry defaultInstance = new AvailableDialinCountry(true);
        private static final long serialVersionUID = 0;
        /* access modifiers changed from: private */
        public LazyStringList allCountries_;
        /* access modifiers changed from: private */
        public int bitField0_;
        /* access modifiers changed from: private */
        public boolean enableShowIncludeTollfree_;
        /* access modifiers changed from: private */
        public Object hash_;
        /* access modifiers changed from: private */
        public boolean includedTollfree_;
        private byte memoizedIsInitialized;
        private int memoizedSerializedSize;
        /* access modifiers changed from: private */
        public LazyStringList selectedCountries_;

        public static final class Builder extends com.google.protobuf.GeneratedMessageLite.Builder<AvailableDialinCountry, Builder> implements AvailableDialinCountryOrBuilder {
            private LazyStringList allCountries_ = LazyStringArrayList.EMPTY;
            private int bitField0_;
            private boolean enableShowIncludeTollfree_;
            private Object hash_ = "";
            private boolean includedTollfree_;
            private LazyStringList selectedCountries_ = LazyStringArrayList.EMPTY;

            private void maybeForceBuilderInitialization() {
            }

            public final boolean isInitialized() {
                return true;
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
                this.hash_ = "";
                this.bitField0_ &= -2;
                this.includedTollfree_ = false;
                this.bitField0_ &= -3;
                this.selectedCountries_ = LazyStringArrayList.EMPTY;
                this.bitField0_ &= -5;
                this.allCountries_ = LazyStringArrayList.EMPTY;
                this.bitField0_ &= -9;
                this.enableShowIncludeTollfree_ = false;
                this.bitField0_ &= -17;
                return this;
            }

            public Builder clone() {
                return create().mergeFrom(buildPartial());
            }

            public AvailableDialinCountry getDefaultInstanceForType() {
                return AvailableDialinCountry.getDefaultInstance();
            }

            public AvailableDialinCountry build() {
                AvailableDialinCountry buildPartial = buildPartial();
                if (buildPartial.isInitialized()) {
                    return buildPartial;
                }
                throw newUninitializedMessageException(buildPartial);
            }

            /* access modifiers changed from: private */
            public AvailableDialinCountry buildParsed() throws InvalidProtocolBufferException {
                AvailableDialinCountry buildPartial = buildPartial();
                if (buildPartial.isInitialized()) {
                    return buildPartial;
                }
                throw newUninitializedMessageException(buildPartial).asInvalidProtocolBufferException();
            }

            public AvailableDialinCountry buildPartial() {
                AvailableDialinCountry availableDialinCountry = new AvailableDialinCountry(this);
                int i = this.bitField0_;
                int i2 = 1;
                if ((i & 1) != 1) {
                    i2 = 0;
                }
                availableDialinCountry.hash_ = this.hash_;
                if ((i & 2) == 2) {
                    i2 |= 2;
                }
                availableDialinCountry.includedTollfree_ = this.includedTollfree_;
                if ((this.bitField0_ & 4) == 4) {
                    this.selectedCountries_ = new UnmodifiableLazyStringList(this.selectedCountries_);
                    this.bitField0_ &= -5;
                }
                availableDialinCountry.selectedCountries_ = this.selectedCountries_;
                if ((this.bitField0_ & 8) == 8) {
                    this.allCountries_ = new UnmodifiableLazyStringList(this.allCountries_);
                    this.bitField0_ &= -9;
                }
                availableDialinCountry.allCountries_ = this.allCountries_;
                if ((i & 16) == 16) {
                    i2 |= 4;
                }
                availableDialinCountry.enableShowIncludeTollfree_ = this.enableShowIncludeTollfree_;
                availableDialinCountry.bitField0_ = i2;
                return availableDialinCountry;
            }

            public Builder mergeFrom(AvailableDialinCountry availableDialinCountry) {
                if (availableDialinCountry == AvailableDialinCountry.getDefaultInstance()) {
                    return this;
                }
                if (availableDialinCountry.hasHash()) {
                    setHash(availableDialinCountry.getHash());
                }
                if (availableDialinCountry.hasIncludedTollfree()) {
                    setIncludedTollfree(availableDialinCountry.getIncludedTollfree());
                }
                if (!availableDialinCountry.selectedCountries_.isEmpty()) {
                    if (this.selectedCountries_.isEmpty()) {
                        this.selectedCountries_ = availableDialinCountry.selectedCountries_;
                        this.bitField0_ &= -5;
                    } else {
                        ensureSelectedCountriesIsMutable();
                        this.selectedCountries_.addAll(availableDialinCountry.selectedCountries_);
                    }
                }
                if (!availableDialinCountry.allCountries_.isEmpty()) {
                    if (this.allCountries_.isEmpty()) {
                        this.allCountries_ = availableDialinCountry.allCountries_;
                        this.bitField0_ &= -9;
                    } else {
                        ensureAllCountriesIsMutable();
                        this.allCountries_.addAll(availableDialinCountry.allCountries_);
                    }
                }
                if (availableDialinCountry.hasEnableShowIncludeTollfree()) {
                    setEnableShowIncludeTollfree(availableDialinCountry.getEnableShowIncludeTollfree());
                }
                return this;
            }

            public Builder mergeFrom(CodedInputStream codedInputStream, ExtensionRegistryLite extensionRegistryLite) throws IOException {
                while (true) {
                    int readTag = codedInputStream.readTag();
                    if (readTag == 0) {
                        return this;
                    }
                    if (readTag == 10) {
                        this.bitField0_ |= 1;
                        this.hash_ = codedInputStream.readBytes();
                    } else if (readTag == 16) {
                        this.bitField0_ |= 2;
                        this.includedTollfree_ = codedInputStream.readBool();
                    } else if (readTag == 26) {
                        ensureSelectedCountriesIsMutable();
                        this.selectedCountries_.add(codedInputStream.readBytes());
                    } else if (readTag == 34) {
                        ensureAllCountriesIsMutable();
                        this.allCountries_.add(codedInputStream.readBytes());
                    } else if (readTag == 40) {
                        this.bitField0_ |= 16;
                        this.enableShowIncludeTollfree_ = codedInputStream.readBool();
                    } else if (!parseUnknownField(codedInputStream, extensionRegistryLite, readTag)) {
                        return this;
                    }
                }
            }

            public boolean hasHash() {
                return (this.bitField0_ & 1) == 1;
            }

            public String getHash() {
                Object obj = this.hash_;
                if (obj instanceof String) {
                    return (String) obj;
                }
                String stringUtf8 = ((ByteString) obj).toStringUtf8();
                this.hash_ = stringUtf8;
                return stringUtf8;
            }

            public Builder setHash(String str) {
                if (str != null) {
                    this.bitField0_ |= 1;
                    this.hash_ = str;
                    return this;
                }
                throw new NullPointerException();
            }

            public Builder clearHash() {
                this.bitField0_ &= -2;
                this.hash_ = AvailableDialinCountry.getDefaultInstance().getHash();
                return this;
            }

            /* access modifiers changed from: 0000 */
            public void setHash(ByteString byteString) {
                this.bitField0_ |= 1;
                this.hash_ = byteString;
            }

            public boolean hasIncludedTollfree() {
                return (this.bitField0_ & 2) == 2;
            }

            public boolean getIncludedTollfree() {
                return this.includedTollfree_;
            }

            public Builder setIncludedTollfree(boolean z) {
                this.bitField0_ |= 2;
                this.includedTollfree_ = z;
                return this;
            }

            public Builder clearIncludedTollfree() {
                this.bitField0_ &= -3;
                this.includedTollfree_ = false;
                return this;
            }

            private void ensureSelectedCountriesIsMutable() {
                if ((this.bitField0_ & 4) != 4) {
                    this.selectedCountries_ = new LazyStringArrayList(this.selectedCountries_);
                    this.bitField0_ |= 4;
                }
            }

            public List<String> getSelectedCountriesList() {
                return Collections.unmodifiableList(this.selectedCountries_);
            }

            public int getSelectedCountriesCount() {
                return this.selectedCountries_.size();
            }

            public String getSelectedCountries(int i) {
                return (String) this.selectedCountries_.get(i);
            }

            public Builder setSelectedCountries(int i, String str) {
                if (str != null) {
                    ensureSelectedCountriesIsMutable();
                    this.selectedCountries_.set(i, str);
                    return this;
                }
                throw new NullPointerException();
            }

            public Builder addSelectedCountries(String str) {
                if (str != null) {
                    ensureSelectedCountriesIsMutable();
                    this.selectedCountries_.add(str);
                    return this;
                }
                throw new NullPointerException();
            }

            public Builder addAllSelectedCountries(Iterable<String> iterable) {
                ensureSelectedCountriesIsMutable();
                com.google.protobuf.GeneratedMessageLite.Builder.addAll(iterable, this.selectedCountries_);
                return this;
            }

            public Builder clearSelectedCountries() {
                this.selectedCountries_ = LazyStringArrayList.EMPTY;
                this.bitField0_ &= -5;
                return this;
            }

            /* access modifiers changed from: 0000 */
            public void addSelectedCountries(ByteString byteString) {
                ensureSelectedCountriesIsMutable();
                this.selectedCountries_.add(byteString);
            }

            private void ensureAllCountriesIsMutable() {
                if ((this.bitField0_ & 8) != 8) {
                    this.allCountries_ = new LazyStringArrayList(this.allCountries_);
                    this.bitField0_ |= 8;
                }
            }

            public List<String> getAllCountriesList() {
                return Collections.unmodifiableList(this.allCountries_);
            }

            public int getAllCountriesCount() {
                return this.allCountries_.size();
            }

            public String getAllCountries(int i) {
                return (String) this.allCountries_.get(i);
            }

            public Builder setAllCountries(int i, String str) {
                if (str != null) {
                    ensureAllCountriesIsMutable();
                    this.allCountries_.set(i, str);
                    return this;
                }
                throw new NullPointerException();
            }

            public Builder addAllCountries(String str) {
                if (str != null) {
                    ensureAllCountriesIsMutable();
                    this.allCountries_.add(str);
                    return this;
                }
                throw new NullPointerException();
            }

            public Builder addAllAllCountries(Iterable<String> iterable) {
                ensureAllCountriesIsMutable();
                com.google.protobuf.GeneratedMessageLite.Builder.addAll(iterable, this.allCountries_);
                return this;
            }

            public Builder clearAllCountries() {
                this.allCountries_ = LazyStringArrayList.EMPTY;
                this.bitField0_ &= -9;
                return this;
            }

            /* access modifiers changed from: 0000 */
            public void addAllCountries(ByteString byteString) {
                ensureAllCountriesIsMutable();
                this.allCountries_.add(byteString);
            }

            public boolean hasEnableShowIncludeTollfree() {
                return (this.bitField0_ & 16) == 16;
            }

            public boolean getEnableShowIncludeTollfree() {
                return this.enableShowIncludeTollfree_;
            }

            public Builder setEnableShowIncludeTollfree(boolean z) {
                this.bitField0_ |= 16;
                this.enableShowIncludeTollfree_ = z;
                return this;
            }

            public Builder clearEnableShowIncludeTollfree() {
                this.bitField0_ &= -17;
                this.enableShowIncludeTollfree_ = false;
                return this;
            }
        }

        private AvailableDialinCountry(Builder builder) {
            super(builder);
            this.memoizedIsInitialized = -1;
            this.memoizedSerializedSize = -1;
        }

        private AvailableDialinCountry(boolean z) {
            this.memoizedIsInitialized = -1;
            this.memoizedSerializedSize = -1;
        }

        public static AvailableDialinCountry getDefaultInstance() {
            return defaultInstance;
        }

        public AvailableDialinCountry getDefaultInstanceForType() {
            return defaultInstance;
        }

        public boolean hasHash() {
            return (this.bitField0_ & 1) == 1;
        }

        public String getHash() {
            Object obj = this.hash_;
            if (obj instanceof String) {
                return (String) obj;
            }
            ByteString byteString = (ByteString) obj;
            String stringUtf8 = byteString.toStringUtf8();
            if (Internal.isValidUtf8(byteString)) {
                this.hash_ = stringUtf8;
            }
            return stringUtf8;
        }

        private ByteString getHashBytes() {
            Object obj = this.hash_;
            if (!(obj instanceof String)) {
                return (ByteString) obj;
            }
            ByteString copyFromUtf8 = ByteString.copyFromUtf8((String) obj);
            this.hash_ = copyFromUtf8;
            return copyFromUtf8;
        }

        public boolean hasIncludedTollfree() {
            return (this.bitField0_ & 2) == 2;
        }

        public boolean getIncludedTollfree() {
            return this.includedTollfree_;
        }

        public List<String> getSelectedCountriesList() {
            return this.selectedCountries_;
        }

        public int getSelectedCountriesCount() {
            return this.selectedCountries_.size();
        }

        public String getSelectedCountries(int i) {
            return (String) this.selectedCountries_.get(i);
        }

        public List<String> getAllCountriesList() {
            return this.allCountries_;
        }

        public int getAllCountriesCount() {
            return this.allCountries_.size();
        }

        public String getAllCountries(int i) {
            return (String) this.allCountries_.get(i);
        }

        public boolean hasEnableShowIncludeTollfree() {
            return (this.bitField0_ & 4) == 4;
        }

        public boolean getEnableShowIncludeTollfree() {
            return this.enableShowIncludeTollfree_;
        }

        private void initFields() {
            this.hash_ = "";
            this.includedTollfree_ = false;
            this.selectedCountries_ = LazyStringArrayList.EMPTY;
            this.allCountries_ = LazyStringArrayList.EMPTY;
            this.enableShowIncludeTollfree_ = false;
        }

        public final boolean isInitialized() {
            byte b = this.memoizedIsInitialized;
            boolean z = true;
            if (b != -1) {
                if (b != 1) {
                    z = false;
                }
                return z;
            }
            this.memoizedIsInitialized = 1;
            return true;
        }

        public void writeTo(CodedOutputStream codedOutputStream) throws IOException {
            getSerializedSize();
            if ((this.bitField0_ & 1) == 1) {
                codedOutputStream.writeBytes(1, getHashBytes());
            }
            if ((this.bitField0_ & 2) == 2) {
                codedOutputStream.writeBool(2, this.includedTollfree_);
            }
            for (int i = 0; i < this.selectedCountries_.size(); i++) {
                codedOutputStream.writeBytes(3, this.selectedCountries_.getByteString(i));
            }
            for (int i2 = 0; i2 < this.allCountries_.size(); i2++) {
                codedOutputStream.writeBytes(4, this.allCountries_.getByteString(i2));
            }
            if ((this.bitField0_ & 4) == 4) {
                codedOutputStream.writeBool(5, this.enableShowIncludeTollfree_);
            }
        }

        public int getSerializedSize() {
            int i = this.memoizedSerializedSize;
            if (i != -1) {
                return i;
            }
            int computeBytesSize = (this.bitField0_ & 1) == 1 ? CodedOutputStream.computeBytesSize(1, getHashBytes()) + 0 : 0;
            if ((this.bitField0_ & 2) == 2) {
                computeBytesSize += CodedOutputStream.computeBoolSize(2, this.includedTollfree_);
            }
            int i2 = 0;
            for (int i3 = 0; i3 < this.selectedCountries_.size(); i3++) {
                i2 += CodedOutputStream.computeBytesSizeNoTag(this.selectedCountries_.getByteString(i3));
            }
            int size = computeBytesSize + i2 + (getSelectedCountriesList().size() * 1);
            int i4 = 0;
            for (int i5 = 0; i5 < this.allCountries_.size(); i5++) {
                i4 += CodedOutputStream.computeBytesSizeNoTag(this.allCountries_.getByteString(i5));
            }
            int size2 = size + i4 + (getAllCountriesList().size() * 1);
            if ((this.bitField0_ & 4) == 4) {
                size2 += CodedOutputStream.computeBoolSize(5, this.enableShowIncludeTollfree_);
            }
            this.memoizedSerializedSize = size2;
            return size2;
        }

        /* access modifiers changed from: protected */
        public Object writeReplace() throws ObjectStreamException {
            return super.writeReplace();
        }

        public static AvailableDialinCountry parseFrom(ByteString byteString) throws InvalidProtocolBufferException {
            return ((Builder) newBuilder().mergeFrom(byteString)).buildParsed();
        }

        public static AvailableDialinCountry parseFrom(ByteString byteString, ExtensionRegistryLite extensionRegistryLite) throws InvalidProtocolBufferException {
            return ((Builder) newBuilder().mergeFrom(byteString, extensionRegistryLite)).buildParsed();
        }

        public static AvailableDialinCountry parseFrom(byte[] bArr) throws InvalidProtocolBufferException {
            return ((Builder) newBuilder().mergeFrom(bArr)).buildParsed();
        }

        public static AvailableDialinCountry parseFrom(byte[] bArr, ExtensionRegistryLite extensionRegistryLite) throws InvalidProtocolBufferException {
            return ((Builder) newBuilder().mergeFrom(bArr, extensionRegistryLite)).buildParsed();
        }

        public static AvailableDialinCountry parseFrom(InputStream inputStream) throws IOException {
            return ((Builder) newBuilder().mergeFrom(inputStream)).buildParsed();
        }

        public static AvailableDialinCountry parseFrom(InputStream inputStream, ExtensionRegistryLite extensionRegistryLite) throws IOException {
            return ((Builder) newBuilder().mergeFrom(inputStream, extensionRegistryLite)).buildParsed();
        }

        public static AvailableDialinCountry parseDelimitedFrom(InputStream inputStream) throws IOException {
            Builder newBuilder = newBuilder();
            if (newBuilder.mergeDelimitedFrom(inputStream)) {
                return newBuilder.buildParsed();
            }
            return null;
        }

        public static AvailableDialinCountry parseDelimitedFrom(InputStream inputStream, ExtensionRegistryLite extensionRegistryLite) throws IOException {
            Builder newBuilder = newBuilder();
            if (newBuilder.mergeDelimitedFrom(inputStream, extensionRegistryLite)) {
                return newBuilder.buildParsed();
            }
            return null;
        }

        public static AvailableDialinCountry parseFrom(CodedInputStream codedInputStream) throws IOException {
            return ((Builder) newBuilder().mergeFrom(codedInputStream)).buildParsed();
        }

        public static AvailableDialinCountry parseFrom(CodedInputStream codedInputStream, ExtensionRegistryLite extensionRegistryLite) throws IOException {
            return newBuilder().mergeFrom(codedInputStream, extensionRegistryLite).buildParsed();
        }

        public static Builder newBuilder() {
            return Builder.create();
        }

        public Builder newBuilderForType() {
            return newBuilder();
        }

        public static Builder newBuilder(AvailableDialinCountry availableDialinCountry) {
            return newBuilder().mergeFrom(availableDialinCountry);
        }

        public Builder toBuilder() {
            return newBuilder(this);
        }

        static {
            defaultInstance.initFields();
        }
    }

    public interface AvailableDialinCountryOrBuilder extends MessageLiteOrBuilder {
        String getAllCountries(int i);

        int getAllCountriesCount();

        List<String> getAllCountriesList();

        boolean getEnableShowIncludeTollfree();

        String getHash();

        boolean getIncludedTollfree();

        String getSelectedCountries(int i);

        int getSelectedCountriesCount();

        List<String> getSelectedCountriesList();

        boolean hasEnableShowIncludeTollfree();

        boolean hasHash();

        boolean hasIncludedTollfree();
    }

    public static final class CountryCode extends GeneratedMessageLite implements CountryCodeOrBuilder {
        public static final int CALLTYPE_FIELD_NUMBER = 6;
        public static final int CODE_FIELD_NUMBER = 3;
        public static final int DISPLAYNUMBER_FIELD_NUMBER = 5;
        public static final int ID_FIELD_NUMBER = 1;
        public static final int NAME_FIELD_NUMBER = 2;
        public static final int NUMBER_FIELD_NUMBER = 4;
        private static final CountryCode defaultInstance = new CountryCode(true);
        private static final long serialVersionUID = 0;
        /* access modifiers changed from: private */
        public int bitField0_;
        /* access modifiers changed from: private */
        public int calltype_;
        /* access modifiers changed from: private */
        public Object code_;
        /* access modifiers changed from: private */
        public Object displaynumber_;
        /* access modifiers changed from: private */
        public Object id_;
        private byte memoizedIsInitialized;
        private int memoizedSerializedSize;
        /* access modifiers changed from: private */
        public Object name_;
        /* access modifiers changed from: private */
        public Object number_;

        public static final class Builder extends com.google.protobuf.GeneratedMessageLite.Builder<CountryCode, Builder> implements CountryCodeOrBuilder {
            private int bitField0_;
            private int calltype_;
            private Object code_ = "";
            private Object displaynumber_ = "";
            private Object id_ = "";
            private Object name_ = "";
            private Object number_ = "";

            private void maybeForceBuilderInitialization() {
            }

            public final boolean isInitialized() {
                return true;
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
                this.name_ = "";
                this.bitField0_ &= -3;
                this.code_ = "";
                this.bitField0_ &= -5;
                this.number_ = "";
                this.bitField0_ &= -9;
                this.displaynumber_ = "";
                this.bitField0_ &= -17;
                this.calltype_ = 0;
                this.bitField0_ &= -33;
                return this;
            }

            public Builder clone() {
                return create().mergeFrom(buildPartial());
            }

            public CountryCode getDefaultInstanceForType() {
                return CountryCode.getDefaultInstance();
            }

            public CountryCode build() {
                CountryCode buildPartial = buildPartial();
                if (buildPartial.isInitialized()) {
                    return buildPartial;
                }
                throw newUninitializedMessageException(buildPartial);
            }

            /* access modifiers changed from: private */
            public CountryCode buildParsed() throws InvalidProtocolBufferException {
                CountryCode buildPartial = buildPartial();
                if (buildPartial.isInitialized()) {
                    return buildPartial;
                }
                throw newUninitializedMessageException(buildPartial).asInvalidProtocolBufferException();
            }

            public CountryCode buildPartial() {
                CountryCode countryCode = new CountryCode(this);
                int i = this.bitField0_;
                int i2 = 1;
                if ((i & 1) != 1) {
                    i2 = 0;
                }
                countryCode.id_ = this.id_;
                if ((i & 2) == 2) {
                    i2 |= 2;
                }
                countryCode.name_ = this.name_;
                if ((i & 4) == 4) {
                    i2 |= 4;
                }
                countryCode.code_ = this.code_;
                if ((i & 8) == 8) {
                    i2 |= 8;
                }
                countryCode.number_ = this.number_;
                if ((i & 16) == 16) {
                    i2 |= 16;
                }
                countryCode.displaynumber_ = this.displaynumber_;
                if ((i & 32) == 32) {
                    i2 |= 32;
                }
                countryCode.calltype_ = this.calltype_;
                countryCode.bitField0_ = i2;
                return countryCode;
            }

            public Builder mergeFrom(CountryCode countryCode) {
                if (countryCode == CountryCode.getDefaultInstance()) {
                    return this;
                }
                if (countryCode.hasId()) {
                    setId(countryCode.getId());
                }
                if (countryCode.hasName()) {
                    setName(countryCode.getName());
                }
                if (countryCode.hasCode()) {
                    setCode(countryCode.getCode());
                }
                if (countryCode.hasNumber()) {
                    setNumber(countryCode.getNumber());
                }
                if (countryCode.hasDisplaynumber()) {
                    setDisplaynumber(countryCode.getDisplaynumber());
                }
                if (countryCode.hasCalltype()) {
                    setCalltype(countryCode.getCalltype());
                }
                return this;
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
                    } else if (readTag == 18) {
                        this.bitField0_ |= 2;
                        this.name_ = codedInputStream.readBytes();
                    } else if (readTag == 26) {
                        this.bitField0_ |= 4;
                        this.code_ = codedInputStream.readBytes();
                    } else if (readTag == 34) {
                        this.bitField0_ |= 8;
                        this.number_ = codedInputStream.readBytes();
                    } else if (readTag == 42) {
                        this.bitField0_ |= 16;
                        this.displaynumber_ = codedInputStream.readBytes();
                    } else if (readTag == 48) {
                        this.bitField0_ |= 32;
                        this.calltype_ = codedInputStream.readInt32();
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
                this.id_ = CountryCode.getDefaultInstance().getId();
                return this;
            }

            /* access modifiers changed from: 0000 */
            public void setId(ByteString byteString) {
                this.bitField0_ |= 1;
                this.id_ = byteString;
            }

            public boolean hasName() {
                return (this.bitField0_ & 2) == 2;
            }

            public String getName() {
                Object obj = this.name_;
                if (obj instanceof String) {
                    return (String) obj;
                }
                String stringUtf8 = ((ByteString) obj).toStringUtf8();
                this.name_ = stringUtf8;
                return stringUtf8;
            }

            public Builder setName(String str) {
                if (str != null) {
                    this.bitField0_ |= 2;
                    this.name_ = str;
                    return this;
                }
                throw new NullPointerException();
            }

            public Builder clearName() {
                this.bitField0_ &= -3;
                this.name_ = CountryCode.getDefaultInstance().getName();
                return this;
            }

            /* access modifiers changed from: 0000 */
            public void setName(ByteString byteString) {
                this.bitField0_ |= 2;
                this.name_ = byteString;
            }

            public boolean hasCode() {
                return (this.bitField0_ & 4) == 4;
            }

            public String getCode() {
                Object obj = this.code_;
                if (obj instanceof String) {
                    return (String) obj;
                }
                String stringUtf8 = ((ByteString) obj).toStringUtf8();
                this.code_ = stringUtf8;
                return stringUtf8;
            }

            public Builder setCode(String str) {
                if (str != null) {
                    this.bitField0_ |= 4;
                    this.code_ = str;
                    return this;
                }
                throw new NullPointerException();
            }

            public Builder clearCode() {
                this.bitField0_ &= -5;
                this.code_ = CountryCode.getDefaultInstance().getCode();
                return this;
            }

            /* access modifiers changed from: 0000 */
            public void setCode(ByteString byteString) {
                this.bitField0_ |= 4;
                this.code_ = byteString;
            }

            public boolean hasNumber() {
                return (this.bitField0_ & 8) == 8;
            }

            public String getNumber() {
                Object obj = this.number_;
                if (obj instanceof String) {
                    return (String) obj;
                }
                String stringUtf8 = ((ByteString) obj).toStringUtf8();
                this.number_ = stringUtf8;
                return stringUtf8;
            }

            public Builder setNumber(String str) {
                if (str != null) {
                    this.bitField0_ |= 8;
                    this.number_ = str;
                    return this;
                }
                throw new NullPointerException();
            }

            public Builder clearNumber() {
                this.bitField0_ &= -9;
                this.number_ = CountryCode.getDefaultInstance().getNumber();
                return this;
            }

            /* access modifiers changed from: 0000 */
            public void setNumber(ByteString byteString) {
                this.bitField0_ |= 8;
                this.number_ = byteString;
            }

            public boolean hasDisplaynumber() {
                return (this.bitField0_ & 16) == 16;
            }

            public String getDisplaynumber() {
                Object obj = this.displaynumber_;
                if (obj instanceof String) {
                    return (String) obj;
                }
                String stringUtf8 = ((ByteString) obj).toStringUtf8();
                this.displaynumber_ = stringUtf8;
                return stringUtf8;
            }

            public Builder setDisplaynumber(String str) {
                if (str != null) {
                    this.bitField0_ |= 16;
                    this.displaynumber_ = str;
                    return this;
                }
                throw new NullPointerException();
            }

            public Builder clearDisplaynumber() {
                this.bitField0_ &= -17;
                this.displaynumber_ = CountryCode.getDefaultInstance().getDisplaynumber();
                return this;
            }

            /* access modifiers changed from: 0000 */
            public void setDisplaynumber(ByteString byteString) {
                this.bitField0_ |= 16;
                this.displaynumber_ = byteString;
            }

            public boolean hasCalltype() {
                return (this.bitField0_ & 32) == 32;
            }

            public int getCalltype() {
                return this.calltype_;
            }

            public Builder setCalltype(int i) {
                this.bitField0_ |= 32;
                this.calltype_ = i;
                return this;
            }

            public Builder clearCalltype() {
                this.bitField0_ &= -33;
                this.calltype_ = 0;
                return this;
            }
        }

        private CountryCode(Builder builder) {
            super(builder);
            this.memoizedIsInitialized = -1;
            this.memoizedSerializedSize = -1;
        }

        private CountryCode(boolean z) {
            this.memoizedIsInitialized = -1;
            this.memoizedSerializedSize = -1;
        }

        public static CountryCode getDefaultInstance() {
            return defaultInstance;
        }

        public CountryCode getDefaultInstanceForType() {
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

        public boolean hasName() {
            return (this.bitField0_ & 2) == 2;
        }

        public String getName() {
            Object obj = this.name_;
            if (obj instanceof String) {
                return (String) obj;
            }
            ByteString byteString = (ByteString) obj;
            String stringUtf8 = byteString.toStringUtf8();
            if (Internal.isValidUtf8(byteString)) {
                this.name_ = stringUtf8;
            }
            return stringUtf8;
        }

        private ByteString getNameBytes() {
            Object obj = this.name_;
            if (!(obj instanceof String)) {
                return (ByteString) obj;
            }
            ByteString copyFromUtf8 = ByteString.copyFromUtf8((String) obj);
            this.name_ = copyFromUtf8;
            return copyFromUtf8;
        }

        public boolean hasCode() {
            return (this.bitField0_ & 4) == 4;
        }

        public String getCode() {
            Object obj = this.code_;
            if (obj instanceof String) {
                return (String) obj;
            }
            ByteString byteString = (ByteString) obj;
            String stringUtf8 = byteString.toStringUtf8();
            if (Internal.isValidUtf8(byteString)) {
                this.code_ = stringUtf8;
            }
            return stringUtf8;
        }

        private ByteString getCodeBytes() {
            Object obj = this.code_;
            if (!(obj instanceof String)) {
                return (ByteString) obj;
            }
            ByteString copyFromUtf8 = ByteString.copyFromUtf8((String) obj);
            this.code_ = copyFromUtf8;
            return copyFromUtf8;
        }

        public boolean hasNumber() {
            return (this.bitField0_ & 8) == 8;
        }

        public String getNumber() {
            Object obj = this.number_;
            if (obj instanceof String) {
                return (String) obj;
            }
            ByteString byteString = (ByteString) obj;
            String stringUtf8 = byteString.toStringUtf8();
            if (Internal.isValidUtf8(byteString)) {
                this.number_ = stringUtf8;
            }
            return stringUtf8;
        }

        private ByteString getNumberBytes() {
            Object obj = this.number_;
            if (!(obj instanceof String)) {
                return (ByteString) obj;
            }
            ByteString copyFromUtf8 = ByteString.copyFromUtf8((String) obj);
            this.number_ = copyFromUtf8;
            return copyFromUtf8;
        }

        public boolean hasDisplaynumber() {
            return (this.bitField0_ & 16) == 16;
        }

        public String getDisplaynumber() {
            Object obj = this.displaynumber_;
            if (obj instanceof String) {
                return (String) obj;
            }
            ByteString byteString = (ByteString) obj;
            String stringUtf8 = byteString.toStringUtf8();
            if (Internal.isValidUtf8(byteString)) {
                this.displaynumber_ = stringUtf8;
            }
            return stringUtf8;
        }

        private ByteString getDisplaynumberBytes() {
            Object obj = this.displaynumber_;
            if (!(obj instanceof String)) {
                return (ByteString) obj;
            }
            ByteString copyFromUtf8 = ByteString.copyFromUtf8((String) obj);
            this.displaynumber_ = copyFromUtf8;
            return copyFromUtf8;
        }

        public boolean hasCalltype() {
            return (this.bitField0_ & 32) == 32;
        }

        public int getCalltype() {
            return this.calltype_;
        }

        private void initFields() {
            this.id_ = "";
            this.name_ = "";
            this.code_ = "";
            this.number_ = "";
            this.displaynumber_ = "";
            this.calltype_ = 0;
        }

        public final boolean isInitialized() {
            byte b = this.memoizedIsInitialized;
            boolean z = true;
            if (b != -1) {
                if (b != 1) {
                    z = false;
                }
                return z;
            }
            this.memoizedIsInitialized = 1;
            return true;
        }

        public void writeTo(CodedOutputStream codedOutputStream) throws IOException {
            getSerializedSize();
            if ((this.bitField0_ & 1) == 1) {
                codedOutputStream.writeBytes(1, getIdBytes());
            }
            if ((this.bitField0_ & 2) == 2) {
                codedOutputStream.writeBytes(2, getNameBytes());
            }
            if ((this.bitField0_ & 4) == 4) {
                codedOutputStream.writeBytes(3, getCodeBytes());
            }
            if ((this.bitField0_ & 8) == 8) {
                codedOutputStream.writeBytes(4, getNumberBytes());
            }
            if ((this.bitField0_ & 16) == 16) {
                codedOutputStream.writeBytes(5, getDisplaynumberBytes());
            }
            if ((this.bitField0_ & 32) == 32) {
                codedOutputStream.writeInt32(6, this.calltype_);
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
                i2 += CodedOutputStream.computeBytesSize(2, getNameBytes());
            }
            if ((this.bitField0_ & 4) == 4) {
                i2 += CodedOutputStream.computeBytesSize(3, getCodeBytes());
            }
            if ((this.bitField0_ & 8) == 8) {
                i2 += CodedOutputStream.computeBytesSize(4, getNumberBytes());
            }
            if ((this.bitField0_ & 16) == 16) {
                i2 += CodedOutputStream.computeBytesSize(5, getDisplaynumberBytes());
            }
            if ((this.bitField0_ & 32) == 32) {
                i2 += CodedOutputStream.computeInt32Size(6, this.calltype_);
            }
            this.memoizedSerializedSize = i2;
            return i2;
        }

        /* access modifiers changed from: protected */
        public Object writeReplace() throws ObjectStreamException {
            return super.writeReplace();
        }

        public static CountryCode parseFrom(ByteString byteString) throws InvalidProtocolBufferException {
            return ((Builder) newBuilder().mergeFrom(byteString)).buildParsed();
        }

        public static CountryCode parseFrom(ByteString byteString, ExtensionRegistryLite extensionRegistryLite) throws InvalidProtocolBufferException {
            return ((Builder) newBuilder().mergeFrom(byteString, extensionRegistryLite)).buildParsed();
        }

        public static CountryCode parseFrom(byte[] bArr) throws InvalidProtocolBufferException {
            return ((Builder) newBuilder().mergeFrom(bArr)).buildParsed();
        }

        public static CountryCode parseFrom(byte[] bArr, ExtensionRegistryLite extensionRegistryLite) throws InvalidProtocolBufferException {
            return ((Builder) newBuilder().mergeFrom(bArr, extensionRegistryLite)).buildParsed();
        }

        public static CountryCode parseFrom(InputStream inputStream) throws IOException {
            return ((Builder) newBuilder().mergeFrom(inputStream)).buildParsed();
        }

        public static CountryCode parseFrom(InputStream inputStream, ExtensionRegistryLite extensionRegistryLite) throws IOException {
            return ((Builder) newBuilder().mergeFrom(inputStream, extensionRegistryLite)).buildParsed();
        }

        public static CountryCode parseDelimitedFrom(InputStream inputStream) throws IOException {
            Builder newBuilder = newBuilder();
            if (newBuilder.mergeDelimitedFrom(inputStream)) {
                return newBuilder.buildParsed();
            }
            return null;
        }

        public static CountryCode parseDelimitedFrom(InputStream inputStream, ExtensionRegistryLite extensionRegistryLite) throws IOException {
            Builder newBuilder = newBuilder();
            if (newBuilder.mergeDelimitedFrom(inputStream, extensionRegistryLite)) {
                return newBuilder.buildParsed();
            }
            return null;
        }

        public static CountryCode parseFrom(CodedInputStream codedInputStream) throws IOException {
            return ((Builder) newBuilder().mergeFrom(codedInputStream)).buildParsed();
        }

        public static CountryCode parseFrom(CodedInputStream codedInputStream, ExtensionRegistryLite extensionRegistryLite) throws IOException {
            return newBuilder().mergeFrom(codedInputStream, extensionRegistryLite).buildParsed();
        }

        public static Builder newBuilder() {
            return Builder.create();
        }

        public Builder newBuilderForType() {
            return newBuilder();
        }

        public static Builder newBuilder(CountryCode countryCode) {
            return newBuilder().mergeFrom(countryCode);
        }

        public Builder toBuilder() {
            return newBuilder(this);
        }

        static {
            defaultInstance.initFields();
        }
    }

    public interface CountryCodeOrBuilder extends MessageLiteOrBuilder {
        int getCalltype();

        String getCode();

        String getDisplaynumber();

        String getId();

        String getName();

        String getNumber();

        boolean hasCalltype();

        boolean hasCode();

        boolean hasDisplaynumber();

        boolean hasId();

        boolean hasName();

        boolean hasNumber();
    }

    public static final class MeetingInfoProto extends GeneratedMessageLite implements MeetingInfoProtoOrBuilder {
        public static final int ALTERHOST_FIELD_NUMBER = 50;
        public static final int ASSISTANTID_FIELD_NUMBER = 21;
        public static final int ATTENDEEVIDEOOFF_FIELD_NUMBER = 30;
        public static final int AUTHPROTO_FIELD_NUMBER = 46;
        public static final int AVAILABLEDIALINCOUNTRY_FIELD_NUMBER = 51;
        public static final int CALLINCOUNTRYCODES_FIELD_NUMBER = 36;
        public static final int CALLINNUMBER_FIELD_NUMBER = 15;
        public static final int CALLOUTCOUNTRYCODES_FIELD_NUMBER = 35;
        public static final int CANJOINBEFOREHOST_FIELD_NUMBER = 10;
        public static final int DAILINSTRING_FIELD_NUMBER = 59;
        public static final int DEFAULTCALLINCOUNTRY_FIELD_NUMBER = 43;
        public static final int DURATION_FIELD_NUMBER = 6;
        public static final int EXTENDMEETINGTYPE_FIELD_NUMBER = 22;
        public static final int GOOGLECALENDARURL_FIELD_NUMBER = 52;
        public static final int H323GATEWAY_FIELD_NUMBER = 17;
        public static final int HOSTVIDEOOFF_FIELD_NUMBER = 29;
        public static final int ID_FIELD_NUMBER = 1;
        public static final int INVITEEMAILCONTENTWITHTIME_FIELD_NUMBER = 27;
        public static final int INVITEEMAILCONTENT_FIELD_NUMBER = 8;
        public static final int INVITEEMAILSUBJECT_FIELD_NUMBER = 25;
        public static final int ISAUDIOONLYMEETING_FIELD_NUMBER = 18;
        public static final int ISCNMEETING_FIELD_NUMBER = 41;
        public static final int ISENABLEAUDIOWATERMARK_FIELD_NUMBER = 57;
        public static final int ISENABLEAUTORECORDINGCLOUD_FIELD_NUMBER = 55;
        public static final int ISENABLEAUTORECORDINGLOCAL_FIELD_NUMBER = 54;
        public static final int ISENABLEAUTORECORDINGMTGLEVELFIRST_FIELD_NUMBER = 56;
        public static final int ISENABLELANGUAGEINTERPRETATION_FIELD_NUMBER = 60;
        public static final int ISENABLEMEETINGTOPUBLIC_FIELD_NUMBER = 53;
        public static final int ISENABLEWAITINGROOM_FIELD_NUMBER = 61;
        public static final int ISH323ENABLED_FIELD_NUMBER = 34;
        public static final int ISONLYSIGNJOIN_FIELD_NUMBER = 45;
        public static final int ISSELFTELEPHONYON_FIELD_NUMBER = 38;
        public static final int ISSHAREONLYMEETING_FIELD_NUMBER = 19;
        public static final int ISSUPPORTWAITINGROOM_FIELD_NUMBER = 62;
        public static final int ISTURNONEXTERNALAUTH_FIELD_NUMBER = 44;
        public static final int ISWEBINAR_FIELD_NUMBER = 20;
        public static final int ISWEBRECURRENCEMEETING_FIELD_NUMBER = 58;
        public static final int JBHPRIORTIME_FIELD_NUMBER = 63;
        public static final int JOINMEETINGURLFORINVITE_FIELD_NUMBER = 64;
        public static final int JOINMEETINGURL_FIELD_NUMBER = 13;
        public static final int MEETINGHOSTID_FIELD_NUMBER = 23;
        public static final int MEETINGHOSTNAME_FIELD_NUMBER = 14;
        public static final int MEETINGNUMBER_FIELD_NUMBER = 2;
        public static final int MEETINGSTATUS_FIELD_NUMBER = 9;
        public static final int MEETINGWAITSTATUS_FIELD_NUMBER = 48;
        public static final int ORIGINALMEETINGNUMBER_FIELD_NUMBER = 40;
        public static final int OTHERTELECONFINFO_FIELD_NUMBER = 37;
        public static final int PASSWORD_FIELD_NUMBER = 4;
        public static final int PROGRESSINGMEETINGCOUNT_FIELD_NUMBER = 47;
        public static final int PSTNENABLED_FIELD_NUMBER = 16;
        public static final int PSTNHIDEINVITEBYPHONE_FIELD_NUMBER = 68;
        public static final int PSTNNEEDCONFIRM1_FIELD_NUMBER = 26;
        public static final int PSTNONLYUSETELEPHONE_FIELD_NUMBER = 65;
        public static final int PSTNPHONENUMBERNOTMATCHCALLOUT_FIELD_NUMBER = 67;
        public static final int PSTNUSEOWNPHONENUMBER_FIELD_NUMBER = 66;
        public static final int REPEATENDTIME_FIELD_NUMBER = 12;
        public static final int REPEATTYPE_FIELD_NUMBER = 11;
        public static final int STARTTIME_FIELD_NUMBER = 5;
        public static final int SUPPORTCALLOUTTYPE_FIELD_NUMBER = 33;
        public static final int TELEPHONYOFF_FIELD_NUMBER = 32;
        public static final int TIMEZONEID_FIELD_NUMBER = 42;
        public static final int TOPIC_FIELD_NUMBER = 3;
        public static final int TSPCALLININFO_FIELD_NUMBER = 49;
        public static final int TYPE_FIELD_NUMBER = 7;
        public static final int USEPMIASMEETINGID_FIELD_NUMBER = 39;
        public static final int VOIPOFF_FIELD_NUMBER = 31;
        public static final int WEBINARREGURL_FIELD_NUMBER = 28;
        private static final MeetingInfoProto defaultInstance = new MeetingInfoProto(true);
        private static final long serialVersionUID = 0;
        /* access modifiers changed from: private */
        public List<AlterHost> alterHost_;
        /* access modifiers changed from: private */
        public Object assistantId_;
        /* access modifiers changed from: private */
        public boolean attendeeVideoOff_;
        /* access modifiers changed from: private */
        public AuthProto authProto_;
        /* access modifiers changed from: private */
        public AvailableDialinCountry availableDialinCountry_;
        /* access modifiers changed from: private */
        public int bitField0_;
        /* access modifiers changed from: private */
        public int bitField1_;
        /* access modifiers changed from: private */
        public List<CountryCode> callinCountryCodes_;
        /* access modifiers changed from: private */
        public Object callinNumber_;
        /* access modifiers changed from: private */
        public List<CountryCode> calloutCountryCodes_;
        /* access modifiers changed from: private */
        public boolean canJoinBeforeHost_;
        /* access modifiers changed from: private */
        public Object dailinString_;
        /* access modifiers changed from: private */
        public Object defaultcallInCountry_;
        /* access modifiers changed from: private */
        public int duration_;
        /* access modifiers changed from: private */
        public int extendMeetingType_;
        /* access modifiers changed from: private */
        public Object googleCalendarUrl_;
        /* access modifiers changed from: private */
        public Object h323Gateway_;
        /* access modifiers changed from: private */
        public boolean hostVideoOff_;
        /* access modifiers changed from: private */
        public Object id_;
        /* access modifiers changed from: private */
        public Object inviteEmailContentWithTime_;
        /* access modifiers changed from: private */
        public Object inviteEmailContent_;
        /* access modifiers changed from: private */
        public Object inviteEmailSubject_;
        /* access modifiers changed from: private */
        public boolean isAudioOnlyMeeting_;
        /* access modifiers changed from: private */
        public boolean isCnMeeting_;
        /* access modifiers changed from: private */
        public boolean isEnableAudioWatermark_;
        /* access modifiers changed from: private */
        public boolean isEnableAutoRecordingCloud_;
        /* access modifiers changed from: private */
        public boolean isEnableAutoRecordingLocal_;
        /* access modifiers changed from: private */
        public boolean isEnableAutoRecordingMtgLevelFirst_;
        /* access modifiers changed from: private */
        public boolean isEnableLanguageInterpretation_;
        /* access modifiers changed from: private */
        public boolean isEnableMeetingToPublic_;
        /* access modifiers changed from: private */
        public boolean isEnableWaitingRoom_;
        /* access modifiers changed from: private */
        public boolean isH323Enabled_;
        /* access modifiers changed from: private */
        public boolean isOnlySignJoin_;
        /* access modifiers changed from: private */
        public boolean isSelfTelephonyOn_;
        /* access modifiers changed from: private */
        public boolean isShareOnlyMeeting_;
        /* access modifiers changed from: private */
        public boolean isSupportWaitingRoom_;
        /* access modifiers changed from: private */
        public boolean isTurnOnExternalAuth_;
        /* access modifiers changed from: private */
        public boolean isWebRecurrenceMeeting_;
        /* access modifiers changed from: private */
        public boolean isWebinar_;
        /* access modifiers changed from: private */
        public int jbhPriorTime_;
        /* access modifiers changed from: private */
        public Object joinMeetingUrlForInvite_;
        /* access modifiers changed from: private */
        public Object joinMeetingUrl_;
        /* access modifiers changed from: private */
        public Object meetingHostID_;
        /* access modifiers changed from: private */
        public Object meetingHostName_;
        /* access modifiers changed from: private */
        public long meetingNumber_;
        /* access modifiers changed from: private */
        public int meetingStatus_;
        /* access modifiers changed from: private */
        public int meetingWaitStatus_;
        private byte memoizedIsInitialized;
        private int memoizedSerializedSize;
        /* access modifiers changed from: private */
        public long originalMeetingNumber_;
        /* access modifiers changed from: private */
        public Object otherTeleConfInfo_;
        /* access modifiers changed from: private */
        public boolean pSTNEnabled_;
        /* access modifiers changed from: private */
        public boolean pSTNNeedConfirm1_;
        /* access modifiers changed from: private */
        public Object password_;
        /* access modifiers changed from: private */
        public int progressingMeetingCount_;
        /* access modifiers changed from: private */
        public boolean pstnHideInviteByPhone_;
        /* access modifiers changed from: private */
        public boolean pstnOnlyUseTelephone_;
        /* access modifiers changed from: private */
        public boolean pstnPhoneNumberNotMatchCallout_;
        /* access modifiers changed from: private */
        public boolean pstnUseOwnPhoneNumber_;
        /* access modifiers changed from: private */
        public long repeatEndTime_;
        /* access modifiers changed from: private */
        public int repeatType_;
        /* access modifiers changed from: private */
        public long startTime_;
        /* access modifiers changed from: private */
        public int supportCallOutType_;
        /* access modifiers changed from: private */
        public boolean telephonyOff_;
        /* access modifiers changed from: private */
        public Object timeZoneId_;
        /* access modifiers changed from: private */
        public Object topic_;
        /* access modifiers changed from: private */
        public List<TSPCallInInfo> tspCallinInfo_;
        /* access modifiers changed from: private */
        public MeetingType type_;
        /* access modifiers changed from: private */
        public boolean usePmiAsMeetingID_;
        /* access modifiers changed from: private */
        public boolean voipOff_;
        /* access modifiers changed from: private */
        public Object webinarRegUrl_;

        public static final class Builder extends com.google.protobuf.GeneratedMessageLite.Builder<MeetingInfoProto, Builder> implements MeetingInfoProtoOrBuilder {
            private List<AlterHost> alterHost_ = Collections.emptyList();
            private Object assistantId_ = "";
            private boolean attendeeVideoOff_;
            private AuthProto authProto_ = AuthProto.getDefaultInstance();
            private AvailableDialinCountry availableDialinCountry_ = AvailableDialinCountry.getDefaultInstance();
            private int bitField0_;
            private int bitField1_;
            private int bitField2_;
            private List<CountryCode> callinCountryCodes_ = Collections.emptyList();
            private Object callinNumber_ = "";
            private List<CountryCode> calloutCountryCodes_ = Collections.emptyList();
            private boolean canJoinBeforeHost_;
            private Object dailinString_ = "";
            private Object defaultcallInCountry_ = "";
            private int duration_;
            private int extendMeetingType_;
            private Object googleCalendarUrl_ = "";
            private Object h323Gateway_ = "";
            private boolean hostVideoOff_;
            private Object id_ = "";
            private Object inviteEmailContentWithTime_ = "";
            private Object inviteEmailContent_ = "";
            private Object inviteEmailSubject_ = "";
            private boolean isAudioOnlyMeeting_;
            private boolean isCnMeeting_;
            private boolean isEnableAudioWatermark_;
            private boolean isEnableAutoRecordingCloud_;
            private boolean isEnableAutoRecordingLocal_;
            private boolean isEnableAutoRecordingMtgLevelFirst_;
            private boolean isEnableLanguageInterpretation_;
            private boolean isEnableMeetingToPublic_;
            private boolean isEnableWaitingRoom_;
            private boolean isH323Enabled_;
            private boolean isOnlySignJoin_;
            private boolean isSelfTelephonyOn_;
            private boolean isShareOnlyMeeting_;
            private boolean isSupportWaitingRoom_;
            private boolean isTurnOnExternalAuth_;
            private boolean isWebRecurrenceMeeting_;
            private boolean isWebinar_;
            private int jbhPriorTime_;
            private Object joinMeetingUrlForInvite_ = "";
            private Object joinMeetingUrl_ = "";
            private Object meetingHostID_ = "";
            private Object meetingHostName_ = "";
            private long meetingNumber_;
            private int meetingStatus_;
            private int meetingWaitStatus_;
            private long originalMeetingNumber_;
            private Object otherTeleConfInfo_ = "";
            private boolean pSTNEnabled_;
            private boolean pSTNNeedConfirm1_;
            private Object password_ = "";
            private int progressingMeetingCount_;
            private boolean pstnHideInviteByPhone_;
            private boolean pstnOnlyUseTelephone_;
            private boolean pstnPhoneNumberNotMatchCallout_;
            private boolean pstnUseOwnPhoneNumber_;
            private long repeatEndTime_;
            private int repeatType_;
            private long startTime_;
            private int supportCallOutType_;
            private boolean telephonyOff_;
            private Object timeZoneId_ = "";
            private Object topic_ = "";
            private List<TSPCallInInfo> tspCallinInfo_ = Collections.emptyList();
            private MeetingType type_ = MeetingType.PRESCHEDULE;
            private boolean usePmiAsMeetingID_;
            private boolean voipOff_;
            private Object webinarRegUrl_ = "";

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
                this.meetingNumber_ = 0;
                this.bitField0_ &= -3;
                this.topic_ = "";
                this.bitField0_ &= -5;
                this.password_ = "";
                this.bitField0_ &= -9;
                this.startTime_ = 0;
                this.bitField0_ &= -17;
                this.duration_ = 0;
                this.bitField0_ &= -33;
                this.type_ = MeetingType.PRESCHEDULE;
                this.bitField0_ &= -65;
                this.inviteEmailContent_ = "";
                this.bitField0_ &= -129;
                this.meetingStatus_ = 0;
                this.bitField0_ &= -257;
                this.canJoinBeforeHost_ = false;
                this.bitField0_ &= -513;
                this.repeatType_ = 0;
                this.bitField0_ &= -1025;
                this.repeatEndTime_ = 0;
                this.bitField0_ &= -2049;
                this.joinMeetingUrl_ = "";
                this.bitField0_ &= -4097;
                this.meetingHostName_ = "";
                this.bitField0_ &= -8193;
                this.callinNumber_ = "";
                this.bitField0_ &= -16385;
                this.pSTNEnabled_ = false;
                this.bitField0_ &= -32769;
                this.h323Gateway_ = "";
                this.bitField0_ &= -65537;
                this.isAudioOnlyMeeting_ = false;
                this.bitField0_ &= -131073;
                this.isShareOnlyMeeting_ = false;
                this.bitField0_ &= -262145;
                this.isWebinar_ = false;
                this.bitField0_ &= -524289;
                this.assistantId_ = "";
                this.bitField0_ &= -1048577;
                this.extendMeetingType_ = 0;
                this.bitField0_ &= -2097153;
                this.meetingHostID_ = "";
                this.bitField0_ &= -4194305;
                this.inviteEmailSubject_ = "";
                this.bitField0_ &= -8388609;
                this.pSTNNeedConfirm1_ = false;
                this.bitField0_ &= -16777217;
                this.inviteEmailContentWithTime_ = "";
                this.bitField0_ &= -33554433;
                this.webinarRegUrl_ = "";
                this.bitField0_ &= -67108865;
                this.hostVideoOff_ = false;
                this.bitField0_ &= -134217729;
                this.attendeeVideoOff_ = false;
                this.bitField0_ &= -268435457;
                this.voipOff_ = false;
                this.bitField0_ &= -536870913;
                this.telephonyOff_ = false;
                this.bitField0_ &= -1073741825;
                this.supportCallOutType_ = 0;
                this.bitField0_ &= Integer.MAX_VALUE;
                this.isH323Enabled_ = false;
                this.bitField1_ &= -2;
                this.calloutCountryCodes_ = Collections.emptyList();
                this.bitField1_ &= -3;
                this.callinCountryCodes_ = Collections.emptyList();
                this.bitField1_ &= -5;
                this.otherTeleConfInfo_ = "";
                this.bitField1_ &= -9;
                this.isSelfTelephonyOn_ = false;
                this.bitField1_ &= -17;
                this.usePmiAsMeetingID_ = false;
                this.bitField1_ &= -33;
                this.originalMeetingNumber_ = 0;
                this.bitField1_ &= -65;
                this.isCnMeeting_ = false;
                this.bitField1_ &= -129;
                this.timeZoneId_ = "";
                this.bitField1_ &= -257;
                this.defaultcallInCountry_ = "";
                this.bitField1_ &= -513;
                this.isTurnOnExternalAuth_ = false;
                this.bitField1_ &= -1025;
                this.isOnlySignJoin_ = false;
                this.bitField1_ &= -2049;
                this.authProto_ = AuthProto.getDefaultInstance();
                this.bitField1_ &= -4097;
                this.progressingMeetingCount_ = 0;
                this.bitField1_ &= -8193;
                this.meetingWaitStatus_ = 0;
                this.bitField1_ &= -16385;
                this.tspCallinInfo_ = Collections.emptyList();
                this.bitField1_ &= -32769;
                this.alterHost_ = Collections.emptyList();
                this.bitField1_ &= -65537;
                this.availableDialinCountry_ = AvailableDialinCountry.getDefaultInstance();
                this.bitField1_ &= -131073;
                this.googleCalendarUrl_ = "";
                this.bitField1_ &= -262145;
                this.isEnableMeetingToPublic_ = false;
                this.bitField1_ &= -524289;
                this.isEnableAutoRecordingLocal_ = false;
                this.bitField1_ &= -1048577;
                this.isEnableAutoRecordingCloud_ = false;
                this.bitField1_ &= -2097153;
                this.isEnableAutoRecordingMtgLevelFirst_ = false;
                this.bitField1_ &= -4194305;
                this.isEnableAudioWatermark_ = false;
                this.bitField1_ &= -8388609;
                this.isWebRecurrenceMeeting_ = false;
                this.bitField1_ &= -16777217;
                this.dailinString_ = "";
                this.bitField1_ &= -33554433;
                this.isEnableLanguageInterpretation_ = false;
                this.bitField1_ &= -67108865;
                this.isEnableWaitingRoom_ = false;
                this.bitField1_ &= -134217729;
                this.isSupportWaitingRoom_ = false;
                this.bitField1_ &= -268435457;
                this.jbhPriorTime_ = 0;
                this.bitField1_ &= -536870913;
                this.joinMeetingUrlForInvite_ = "";
                this.bitField1_ &= -1073741825;
                this.pstnOnlyUseTelephone_ = false;
                this.bitField1_ &= Integer.MAX_VALUE;
                this.pstnUseOwnPhoneNumber_ = false;
                this.bitField2_ &= -2;
                this.pstnPhoneNumberNotMatchCallout_ = false;
                this.bitField2_ &= -3;
                this.pstnHideInviteByPhone_ = false;
                this.bitField2_ &= -5;
                return this;
            }

            public Builder clone() {
                return create().mergeFrom(buildPartial());
            }

            public MeetingInfoProto getDefaultInstanceForType() {
                return MeetingInfoProto.getDefaultInstance();
            }

            public MeetingInfoProto build() {
                MeetingInfoProto buildPartial = buildPartial();
                if (buildPartial.isInitialized()) {
                    return buildPartial;
                }
                throw newUninitializedMessageException(buildPartial);
            }

            /* access modifiers changed from: private */
            public MeetingInfoProto buildParsed() throws InvalidProtocolBufferException {
                MeetingInfoProto buildPartial = buildPartial();
                if (buildPartial.isInitialized()) {
                    return buildPartial;
                }
                throw newUninitializedMessageException(buildPartial).asInvalidProtocolBufferException();
            }

            public MeetingInfoProto buildPartial() {
                MeetingInfoProto meetingInfoProto = new MeetingInfoProto(this);
                int i = this.bitField0_;
                int i2 = this.bitField1_;
                int i3 = this.bitField2_;
                int i4 = (i & 1) == 1 ? 1 : 0;
                meetingInfoProto.id_ = this.id_;
                if ((i & 2) == 2) {
                    i4 |= 2;
                }
                meetingInfoProto.meetingNumber_ = this.meetingNumber_;
                if ((i & 4) == 4) {
                    i4 |= 4;
                }
                meetingInfoProto.topic_ = this.topic_;
                if ((i & 8) == 8) {
                    i4 |= 8;
                }
                meetingInfoProto.password_ = this.password_;
                if ((i & 16) == 16) {
                    i4 |= 16;
                }
                meetingInfoProto.startTime_ = this.startTime_;
                if ((i & 32) == 32) {
                    i4 |= 32;
                }
                meetingInfoProto.duration_ = this.duration_;
                if ((i & 64) == 64) {
                    i4 |= 64;
                }
                meetingInfoProto.type_ = this.type_;
                if ((i & 128) == 128) {
                    i4 |= 128;
                }
                meetingInfoProto.inviteEmailContent_ = this.inviteEmailContent_;
                if ((i & 256) == 256) {
                    i4 |= 256;
                }
                meetingInfoProto.meetingStatus_ = this.meetingStatus_;
                if ((i & 512) == 512) {
                    i4 |= 512;
                }
                meetingInfoProto.canJoinBeforeHost_ = this.canJoinBeforeHost_;
                if ((i & 1024) == 1024) {
                    i4 |= 1024;
                }
                meetingInfoProto.repeatType_ = this.repeatType_;
                if ((i & 2048) == 2048) {
                    i4 |= 2048;
                }
                meetingInfoProto.repeatEndTime_ = this.repeatEndTime_;
                if ((i & 4096) == 4096) {
                    i4 |= 4096;
                }
                meetingInfoProto.joinMeetingUrl_ = this.joinMeetingUrl_;
                if ((i & 8192) == 8192) {
                    i4 |= 8192;
                }
                meetingInfoProto.meetingHostName_ = this.meetingHostName_;
                if ((i & 16384) == 16384) {
                    i4 |= 16384;
                }
                meetingInfoProto.callinNumber_ = this.callinNumber_;
                if ((32768 & i) == 32768) {
                    i4 |= 32768;
                }
                meetingInfoProto.pSTNEnabled_ = this.pSTNEnabled_;
                if ((65536 & i) == 65536) {
                    i4 |= 65536;
                }
                meetingInfoProto.h323Gateway_ = this.h323Gateway_;
                if ((131072 & i) == 131072) {
                    i4 |= 131072;
                }
                meetingInfoProto.isAudioOnlyMeeting_ = this.isAudioOnlyMeeting_;
                if ((262144 & i) == 262144) {
                    i4 |= 262144;
                }
                meetingInfoProto.isShareOnlyMeeting_ = this.isShareOnlyMeeting_;
                if ((524288 & i) == 524288) {
                    i4 |= 524288;
                }
                meetingInfoProto.isWebinar_ = this.isWebinar_;
                if ((1048576 & i) == 1048576) {
                    i4 |= 1048576;
                }
                meetingInfoProto.assistantId_ = this.assistantId_;
                if ((2097152 & i) == 2097152) {
                    i4 |= 2097152;
                }
                meetingInfoProto.extendMeetingType_ = this.extendMeetingType_;
                if ((4194304 & i) == 4194304) {
                    i4 |= 4194304;
                }
                meetingInfoProto.meetingHostID_ = this.meetingHostID_;
                if ((8388608 & i) == 8388608) {
                    i4 |= 8388608;
                }
                meetingInfoProto.inviteEmailSubject_ = this.inviteEmailSubject_;
                if ((16777216 & i) == 16777216) {
                    i4 |= 16777216;
                }
                meetingInfoProto.pSTNNeedConfirm1_ = this.pSTNNeedConfirm1_;
                if ((33554432 & i) == 33554432) {
                    i4 |= MediaHttpDownloader.MAXIMUM_CHUNK_SIZE;
                }
                meetingInfoProto.inviteEmailContentWithTime_ = this.inviteEmailContentWithTime_;
                if ((67108864 & i) == 67108864) {
                    i4 |= 67108864;
                }
                meetingInfoProto.webinarRegUrl_ = this.webinarRegUrl_;
                if ((134217728 & i) == 134217728) {
                    i4 |= 134217728;
                }
                meetingInfoProto.hostVideoOff_ = this.hostVideoOff_;
                if ((268435456 & i) == 268435456) {
                    i4 |= 268435456;
                }
                meetingInfoProto.attendeeVideoOff_ = this.attendeeVideoOff_;
                if ((536870912 & i) == 536870912) {
                    i4 |= 536870912;
                }
                meetingInfoProto.voipOff_ = this.voipOff_;
                if ((1073741824 & i) == 1073741824) {
                    i4 |= Ints.MAX_POWER_OF_TWO;
                }
                meetingInfoProto.telephonyOff_ = this.telephonyOff_;
                if ((i & Integer.MIN_VALUE) == Integer.MIN_VALUE) {
                    i4 |= Integer.MIN_VALUE;
                }
                meetingInfoProto.supportCallOutType_ = this.supportCallOutType_;
                int i5 = (i2 & 1) == 1 ? 1 : 0;
                meetingInfoProto.isH323Enabled_ = this.isH323Enabled_;
                if ((this.bitField1_ & 2) == 2) {
                    this.calloutCountryCodes_ = Collections.unmodifiableList(this.calloutCountryCodes_);
                    this.bitField1_ &= -3;
                }
                meetingInfoProto.calloutCountryCodes_ = this.calloutCountryCodes_;
                if ((this.bitField1_ & 4) == 4) {
                    this.callinCountryCodes_ = Collections.unmodifiableList(this.callinCountryCodes_);
                    this.bitField1_ &= -5;
                }
                meetingInfoProto.callinCountryCodes_ = this.callinCountryCodes_;
                if ((i2 & 8) == 8) {
                    i5 |= 2;
                }
                meetingInfoProto.otherTeleConfInfo_ = this.otherTeleConfInfo_;
                if ((i2 & 16) == 16) {
                    i5 |= 4;
                }
                meetingInfoProto.isSelfTelephonyOn_ = this.isSelfTelephonyOn_;
                if ((i2 & 32) == 32) {
                    i5 |= 8;
                }
                meetingInfoProto.usePmiAsMeetingID_ = this.usePmiAsMeetingID_;
                if ((i2 & 64) == 64) {
                    i5 |= 16;
                }
                meetingInfoProto.originalMeetingNumber_ = this.originalMeetingNumber_;
                if ((i2 & 128) == 128) {
                    i5 |= 32;
                }
                meetingInfoProto.isCnMeeting_ = this.isCnMeeting_;
                if ((i2 & 256) == 256) {
                    i5 |= 64;
                }
                meetingInfoProto.timeZoneId_ = this.timeZoneId_;
                if ((i2 & 512) == 512) {
                    i5 |= 128;
                }
                meetingInfoProto.defaultcallInCountry_ = this.defaultcallInCountry_;
                if ((i2 & 1024) == 1024) {
                    i5 |= 256;
                }
                meetingInfoProto.isTurnOnExternalAuth_ = this.isTurnOnExternalAuth_;
                if ((i2 & 2048) == 2048) {
                    i5 |= 512;
                }
                meetingInfoProto.isOnlySignJoin_ = this.isOnlySignJoin_;
                if ((i2 & 4096) == 4096) {
                    i5 |= 1024;
                }
                meetingInfoProto.authProto_ = this.authProto_;
                if ((i2 & 8192) == 8192) {
                    i5 |= 2048;
                }
                meetingInfoProto.progressingMeetingCount_ = this.progressingMeetingCount_;
                if ((i2 & 16384) == 16384) {
                    i5 |= 4096;
                }
                meetingInfoProto.meetingWaitStatus_ = this.meetingWaitStatus_;
                if ((this.bitField1_ & 32768) == 32768) {
                    this.tspCallinInfo_ = Collections.unmodifiableList(this.tspCallinInfo_);
                    this.bitField1_ &= -32769;
                }
                meetingInfoProto.tspCallinInfo_ = this.tspCallinInfo_;
                if ((this.bitField1_ & 65536) == 65536) {
                    this.alterHost_ = Collections.unmodifiableList(this.alterHost_);
                    this.bitField1_ &= -65537;
                }
                meetingInfoProto.alterHost_ = this.alterHost_;
                if ((131072 & i2) == 131072) {
                    i5 |= 8192;
                }
                meetingInfoProto.availableDialinCountry_ = this.availableDialinCountry_;
                if ((262144 & i2) == 262144) {
                    i5 |= 16384;
                }
                meetingInfoProto.googleCalendarUrl_ = this.googleCalendarUrl_;
                if ((524288 & i2) == 524288) {
                    i5 |= 32768;
                }
                meetingInfoProto.isEnableMeetingToPublic_ = this.isEnableMeetingToPublic_;
                if ((1048576 & i2) == 1048576) {
                    i5 |= 65536;
                }
                meetingInfoProto.isEnableAutoRecordingLocal_ = this.isEnableAutoRecordingLocal_;
                if ((2097152 & i2) == 2097152) {
                    i5 |= 131072;
                }
                meetingInfoProto.isEnableAutoRecordingCloud_ = this.isEnableAutoRecordingCloud_;
                if ((4194304 & i2) == 4194304) {
                    i5 |= 262144;
                }
                meetingInfoProto.isEnableAutoRecordingMtgLevelFirst_ = this.isEnableAutoRecordingMtgLevelFirst_;
                if ((8388608 & i2) == 8388608) {
                    i5 |= 524288;
                }
                meetingInfoProto.isEnableAudioWatermark_ = this.isEnableAudioWatermark_;
                if ((16777216 & i2) == 16777216) {
                    i5 |= 1048576;
                }
                meetingInfoProto.isWebRecurrenceMeeting_ = this.isWebRecurrenceMeeting_;
                if ((33554432 & i2) == 33554432) {
                    i5 |= 2097152;
                }
                meetingInfoProto.dailinString_ = this.dailinString_;
                if ((67108864 & i2) == 67108864) {
                    i5 |= 4194304;
                }
                meetingInfoProto.isEnableLanguageInterpretation_ = this.isEnableLanguageInterpretation_;
                if ((134217728 & i2) == 134217728) {
                    i5 |= 8388608;
                }
                meetingInfoProto.isEnableWaitingRoom_ = this.isEnableWaitingRoom_;
                if ((268435456 & i2) == 268435456) {
                    i5 |= 16777216;
                }
                meetingInfoProto.isSupportWaitingRoom_ = this.isSupportWaitingRoom_;
                if ((536870912 & i2) == 536870912) {
                    i5 |= MediaHttpDownloader.MAXIMUM_CHUNK_SIZE;
                }
                meetingInfoProto.jbhPriorTime_ = this.jbhPriorTime_;
                if ((1073741824 & i2) == 1073741824) {
                    i5 |= 67108864;
                }
                meetingInfoProto.joinMeetingUrlForInvite_ = this.joinMeetingUrlForInvite_;
                if ((i2 & Integer.MIN_VALUE) == Integer.MIN_VALUE) {
                    i5 |= 134217728;
                }
                meetingInfoProto.pstnOnlyUseTelephone_ = this.pstnOnlyUseTelephone_;
                if ((i3 & 1) == 1) {
                    i5 |= 268435456;
                }
                meetingInfoProto.pstnUseOwnPhoneNumber_ = this.pstnUseOwnPhoneNumber_;
                if ((i3 & 2) == 2) {
                    i5 |= 536870912;
                }
                meetingInfoProto.pstnPhoneNumberNotMatchCallout_ = this.pstnPhoneNumberNotMatchCallout_;
                if ((i3 & 4) == 4) {
                    i5 |= Ints.MAX_POWER_OF_TWO;
                }
                meetingInfoProto.pstnHideInviteByPhone_ = this.pstnHideInviteByPhone_;
                meetingInfoProto.bitField0_ = i4;
                meetingInfoProto.bitField1_ = i5;
                return meetingInfoProto;
            }

            public Builder mergeFrom(MeetingInfoProto meetingInfoProto) {
                if (meetingInfoProto == MeetingInfoProto.getDefaultInstance()) {
                    return this;
                }
                if (meetingInfoProto.hasId()) {
                    setId(meetingInfoProto.getId());
                }
                if (meetingInfoProto.hasMeetingNumber()) {
                    setMeetingNumber(meetingInfoProto.getMeetingNumber());
                }
                if (meetingInfoProto.hasTopic()) {
                    setTopic(meetingInfoProto.getTopic());
                }
                if (meetingInfoProto.hasPassword()) {
                    setPassword(meetingInfoProto.getPassword());
                }
                if (meetingInfoProto.hasStartTime()) {
                    setStartTime(meetingInfoProto.getStartTime());
                }
                if (meetingInfoProto.hasDuration()) {
                    setDuration(meetingInfoProto.getDuration());
                }
                if (meetingInfoProto.hasType()) {
                    setType(meetingInfoProto.getType());
                }
                if (meetingInfoProto.hasInviteEmailContent()) {
                    setInviteEmailContent(meetingInfoProto.getInviteEmailContent());
                }
                if (meetingInfoProto.hasMeetingStatus()) {
                    setMeetingStatus(meetingInfoProto.getMeetingStatus());
                }
                if (meetingInfoProto.hasCanJoinBeforeHost()) {
                    setCanJoinBeforeHost(meetingInfoProto.getCanJoinBeforeHost());
                }
                if (meetingInfoProto.hasRepeatType()) {
                    setRepeatType(meetingInfoProto.getRepeatType());
                }
                if (meetingInfoProto.hasRepeatEndTime()) {
                    setRepeatEndTime(meetingInfoProto.getRepeatEndTime());
                }
                if (meetingInfoProto.hasJoinMeetingUrl()) {
                    setJoinMeetingUrl(meetingInfoProto.getJoinMeetingUrl());
                }
                if (meetingInfoProto.hasMeetingHostName()) {
                    setMeetingHostName(meetingInfoProto.getMeetingHostName());
                }
                if (meetingInfoProto.hasCallinNumber()) {
                    setCallinNumber(meetingInfoProto.getCallinNumber());
                }
                if (meetingInfoProto.hasPSTNEnabled()) {
                    setPSTNEnabled(meetingInfoProto.getPSTNEnabled());
                }
                if (meetingInfoProto.hasH323Gateway()) {
                    setH323Gateway(meetingInfoProto.getH323Gateway());
                }
                if (meetingInfoProto.hasIsAudioOnlyMeeting()) {
                    setIsAudioOnlyMeeting(meetingInfoProto.getIsAudioOnlyMeeting());
                }
                if (meetingInfoProto.hasIsShareOnlyMeeting()) {
                    setIsShareOnlyMeeting(meetingInfoProto.getIsShareOnlyMeeting());
                }
                if (meetingInfoProto.hasIsWebinar()) {
                    setIsWebinar(meetingInfoProto.getIsWebinar());
                }
                if (meetingInfoProto.hasAssistantId()) {
                    setAssistantId(meetingInfoProto.getAssistantId());
                }
                if (meetingInfoProto.hasExtendMeetingType()) {
                    setExtendMeetingType(meetingInfoProto.getExtendMeetingType());
                }
                if (meetingInfoProto.hasMeetingHostID()) {
                    setMeetingHostID(meetingInfoProto.getMeetingHostID());
                }
                if (meetingInfoProto.hasInviteEmailSubject()) {
                    setInviteEmailSubject(meetingInfoProto.getInviteEmailSubject());
                }
                if (meetingInfoProto.hasPSTNNeedConfirm1()) {
                    setPSTNNeedConfirm1(meetingInfoProto.getPSTNNeedConfirm1());
                }
                if (meetingInfoProto.hasInviteEmailContentWithTime()) {
                    setInviteEmailContentWithTime(meetingInfoProto.getInviteEmailContentWithTime());
                }
                if (meetingInfoProto.hasWebinarRegUrl()) {
                    setWebinarRegUrl(meetingInfoProto.getWebinarRegUrl());
                }
                if (meetingInfoProto.hasHostVideoOff()) {
                    setHostVideoOff(meetingInfoProto.getHostVideoOff());
                }
                if (meetingInfoProto.hasAttendeeVideoOff()) {
                    setAttendeeVideoOff(meetingInfoProto.getAttendeeVideoOff());
                }
                if (meetingInfoProto.hasVoipOff()) {
                    setVoipOff(meetingInfoProto.getVoipOff());
                }
                if (meetingInfoProto.hasTelephonyOff()) {
                    setTelephonyOff(meetingInfoProto.getTelephonyOff());
                }
                if (meetingInfoProto.hasSupportCallOutType()) {
                    setSupportCallOutType(meetingInfoProto.getSupportCallOutType());
                }
                if (meetingInfoProto.hasIsH323Enabled()) {
                    setIsH323Enabled(meetingInfoProto.getIsH323Enabled());
                }
                if (!meetingInfoProto.calloutCountryCodes_.isEmpty()) {
                    if (this.calloutCountryCodes_.isEmpty()) {
                        this.calloutCountryCodes_ = meetingInfoProto.calloutCountryCodes_;
                        this.bitField1_ &= -3;
                    } else {
                        ensureCalloutCountryCodesIsMutable();
                        this.calloutCountryCodes_.addAll(meetingInfoProto.calloutCountryCodes_);
                    }
                }
                if (!meetingInfoProto.callinCountryCodes_.isEmpty()) {
                    if (this.callinCountryCodes_.isEmpty()) {
                        this.callinCountryCodes_ = meetingInfoProto.callinCountryCodes_;
                        this.bitField1_ &= -5;
                    } else {
                        ensureCallinCountryCodesIsMutable();
                        this.callinCountryCodes_.addAll(meetingInfoProto.callinCountryCodes_);
                    }
                }
                if (meetingInfoProto.hasOtherTeleConfInfo()) {
                    setOtherTeleConfInfo(meetingInfoProto.getOtherTeleConfInfo());
                }
                if (meetingInfoProto.hasIsSelfTelephonyOn()) {
                    setIsSelfTelephonyOn(meetingInfoProto.getIsSelfTelephonyOn());
                }
                if (meetingInfoProto.hasUsePmiAsMeetingID()) {
                    setUsePmiAsMeetingID(meetingInfoProto.getUsePmiAsMeetingID());
                }
                if (meetingInfoProto.hasOriginalMeetingNumber()) {
                    setOriginalMeetingNumber(meetingInfoProto.getOriginalMeetingNumber());
                }
                if (meetingInfoProto.hasIsCnMeeting()) {
                    setIsCnMeeting(meetingInfoProto.getIsCnMeeting());
                }
                if (meetingInfoProto.hasTimeZoneId()) {
                    setTimeZoneId(meetingInfoProto.getTimeZoneId());
                }
                if (meetingInfoProto.hasDefaultcallInCountry()) {
                    setDefaultcallInCountry(meetingInfoProto.getDefaultcallInCountry());
                }
                if (meetingInfoProto.hasIsTurnOnExternalAuth()) {
                    setIsTurnOnExternalAuth(meetingInfoProto.getIsTurnOnExternalAuth());
                }
                if (meetingInfoProto.hasIsOnlySignJoin()) {
                    setIsOnlySignJoin(meetingInfoProto.getIsOnlySignJoin());
                }
                if (meetingInfoProto.hasAuthProto()) {
                    mergeAuthProto(meetingInfoProto.getAuthProto());
                }
                if (meetingInfoProto.hasProgressingMeetingCount()) {
                    setProgressingMeetingCount(meetingInfoProto.getProgressingMeetingCount());
                }
                if (meetingInfoProto.hasMeetingWaitStatus()) {
                    setMeetingWaitStatus(meetingInfoProto.getMeetingWaitStatus());
                }
                if (!meetingInfoProto.tspCallinInfo_.isEmpty()) {
                    if (this.tspCallinInfo_.isEmpty()) {
                        this.tspCallinInfo_ = meetingInfoProto.tspCallinInfo_;
                        this.bitField1_ &= -32769;
                    } else {
                        ensureTspCallinInfoIsMutable();
                        this.tspCallinInfo_.addAll(meetingInfoProto.tspCallinInfo_);
                    }
                }
                if (!meetingInfoProto.alterHost_.isEmpty()) {
                    if (this.alterHost_.isEmpty()) {
                        this.alterHost_ = meetingInfoProto.alterHost_;
                        this.bitField1_ &= -65537;
                    } else {
                        ensureAlterHostIsMutable();
                        this.alterHost_.addAll(meetingInfoProto.alterHost_);
                    }
                }
                if (meetingInfoProto.hasAvailableDialinCountry()) {
                    mergeAvailableDialinCountry(meetingInfoProto.getAvailableDialinCountry());
                }
                if (meetingInfoProto.hasGoogleCalendarUrl()) {
                    setGoogleCalendarUrl(meetingInfoProto.getGoogleCalendarUrl());
                }
                if (meetingInfoProto.hasIsEnableMeetingToPublic()) {
                    setIsEnableMeetingToPublic(meetingInfoProto.getIsEnableMeetingToPublic());
                }
                if (meetingInfoProto.hasIsEnableAutoRecordingLocal()) {
                    setIsEnableAutoRecordingLocal(meetingInfoProto.getIsEnableAutoRecordingLocal());
                }
                if (meetingInfoProto.hasIsEnableAutoRecordingCloud()) {
                    setIsEnableAutoRecordingCloud(meetingInfoProto.getIsEnableAutoRecordingCloud());
                }
                if (meetingInfoProto.hasIsEnableAutoRecordingMtgLevelFirst()) {
                    setIsEnableAutoRecordingMtgLevelFirst(meetingInfoProto.getIsEnableAutoRecordingMtgLevelFirst());
                }
                if (meetingInfoProto.hasIsEnableAudioWatermark()) {
                    setIsEnableAudioWatermark(meetingInfoProto.getIsEnableAudioWatermark());
                }
                if (meetingInfoProto.hasIsWebRecurrenceMeeting()) {
                    setIsWebRecurrenceMeeting(meetingInfoProto.getIsWebRecurrenceMeeting());
                }
                if (meetingInfoProto.hasDailinString()) {
                    setDailinString(meetingInfoProto.getDailinString());
                }
                if (meetingInfoProto.hasIsEnableLanguageInterpretation()) {
                    setIsEnableLanguageInterpretation(meetingInfoProto.getIsEnableLanguageInterpretation());
                }
                if (meetingInfoProto.hasIsEnableWaitingRoom()) {
                    setIsEnableWaitingRoom(meetingInfoProto.getIsEnableWaitingRoom());
                }
                if (meetingInfoProto.hasIsSupportWaitingRoom()) {
                    setIsSupportWaitingRoom(meetingInfoProto.getIsSupportWaitingRoom());
                }
                if (meetingInfoProto.hasJbhPriorTime()) {
                    setJbhPriorTime(meetingInfoProto.getJbhPriorTime());
                }
                if (meetingInfoProto.hasJoinMeetingUrlForInvite()) {
                    setJoinMeetingUrlForInvite(meetingInfoProto.getJoinMeetingUrlForInvite());
                }
                if (meetingInfoProto.hasPstnOnlyUseTelephone()) {
                    setPstnOnlyUseTelephone(meetingInfoProto.getPstnOnlyUseTelephone());
                }
                if (meetingInfoProto.hasPstnUseOwnPhoneNumber()) {
                    setPstnUseOwnPhoneNumber(meetingInfoProto.getPstnUseOwnPhoneNumber());
                }
                if (meetingInfoProto.hasPstnPhoneNumberNotMatchCallout()) {
                    setPstnPhoneNumberNotMatchCallout(meetingInfoProto.getPstnPhoneNumberNotMatchCallout());
                }
                if (meetingInfoProto.hasPstnHideInviteByPhone()) {
                    setPstnHideInviteByPhone(meetingInfoProto.getPstnHideInviteByPhone());
                }
                return this;
            }

            public final boolean isInitialized() {
                return !hasAuthProto() || getAuthProto().isInitialized();
            }

            public Builder mergeFrom(CodedInputStream codedInputStream, ExtensionRegistryLite extensionRegistryLite) throws IOException {
                while (true) {
                    int readTag = codedInputStream.readTag();
                    switch (readTag) {
                        case 0:
                            return this;
                        case 10:
                            this.bitField0_ |= 1;
                            this.id_ = codedInputStream.readBytes();
                            break;
                        case 16:
                            this.bitField0_ |= 2;
                            this.meetingNumber_ = codedInputStream.readInt64();
                            break;
                        case 26:
                            this.bitField0_ |= 4;
                            this.topic_ = codedInputStream.readBytes();
                            break;
                        case 34:
                            this.bitField0_ |= 8;
                            this.password_ = codedInputStream.readBytes();
                            break;
                        case 40:
                            this.bitField0_ |= 16;
                            this.startTime_ = codedInputStream.readInt64();
                            break;
                        case 48:
                            this.bitField0_ |= 32;
                            this.duration_ = codedInputStream.readInt32();
                            break;
                        case 56:
                            MeetingType valueOf = MeetingType.valueOf(codedInputStream.readEnum());
                            if (valueOf == null) {
                                break;
                            } else {
                                this.bitField0_ |= 64;
                                this.type_ = valueOf;
                                break;
                            }
                        case 66:
                            this.bitField0_ |= 128;
                            this.inviteEmailContent_ = codedInputStream.readBytes();
                            break;
                        case 72:
                            this.bitField0_ |= 256;
                            this.meetingStatus_ = codedInputStream.readInt32();
                            break;
                        case 80:
                            this.bitField0_ |= 512;
                            this.canJoinBeforeHost_ = codedInputStream.readBool();
                            break;
                        case 88:
                            this.bitField0_ |= 1024;
                            this.repeatType_ = codedInputStream.readInt32();
                            break;
                        case 96:
                            this.bitField0_ |= 2048;
                            this.repeatEndTime_ = codedInputStream.readInt64();
                            break;
                        case 106:
                            this.bitField0_ |= 4096;
                            this.joinMeetingUrl_ = codedInputStream.readBytes();
                            break;
                        case 114:
                            this.bitField0_ |= 8192;
                            this.meetingHostName_ = codedInputStream.readBytes();
                            break;
                        case 122:
                            this.bitField0_ |= 16384;
                            this.callinNumber_ = codedInputStream.readBytes();
                            break;
                        case 128:
                            this.bitField0_ |= 32768;
                            this.pSTNEnabled_ = codedInputStream.readBool();
                            break;
                        case 138:
                            this.bitField0_ |= 65536;
                            this.h323Gateway_ = codedInputStream.readBytes();
                            break;
                        case 144:
                            this.bitField0_ |= 131072;
                            this.isAudioOnlyMeeting_ = codedInputStream.readBool();
                            break;
                        case 152:
                            this.bitField0_ |= 262144;
                            this.isShareOnlyMeeting_ = codedInputStream.readBool();
                            break;
                        case 160:
                            this.bitField0_ |= 524288;
                            this.isWebinar_ = codedInputStream.readBool();
                            break;
                        case DummyPolicyIDType.zPolicy_SetRingSpeakerID /*170*/:
                            this.bitField0_ |= 1048576;
                            this.assistantId_ = codedInputStream.readBytes();
                            break;
                        case DummyPolicyIDType.zPolicy_SetCameraAlias /*176*/:
                            this.bitField0_ |= 2097152;
                            this.extendMeetingType_ = codedInputStream.readInt32();
                            break;
                        case DummyPolicyIDType.zPolicy_SetMessengerIdleMinutes /*186*/:
                            this.bitField0_ |= 4194304;
                            this.meetingHostID_ = codedInputStream.readBytes();
                            break;
                        case 202:
                            this.bitField0_ |= 8388608;
                            this.inviteEmailSubject_ = codedInputStream.readBytes();
                            break;
                        case DummyPolicyIDType.zPolicy_SetShortCuts_Show_Or_Hide_Chat /*208*/:
                            this.bitField0_ |= 16777216;
                            this.pSTNNeedConfirm1_ = codedInputStream.readBool();
                            break;
                        case DummyPolicyIDType.zPolicy_SetShortCuts_Reduce_ChatDisplayFontSize /*218*/:
                            this.bitField0_ |= MediaHttpDownloader.MAXIMUM_CHUNK_SIZE;
                            this.inviteEmailContentWithTime_ = codedInputStream.readBytes();
                            break;
                        case DummyPolicyIDType.zPolicy_SetShortCuts_AddNewLine /*226*/:
                            this.bitField0_ |= 67108864;
                            this.webinarRegUrl_ = codedInputStream.readBytes();
                            break;
                        case DummyPolicyIDType.zPolicy_NeedCallARoom /*232*/:
                            this.bitField0_ |= 134217728;
                            this.hostVideoOff_ = codedInputStream.readBool();
                            break;
                        case DummyPolicyIDType.zPolicy_EnableElevateForAdvDSCP /*240*/:
                            this.bitField0_ |= 268435456;
                            this.attendeeVideoOff_ = codedInputStream.readBool();
                            break;
                        case 248:
                            this.bitField0_ |= 536870912;
                            this.voipOff_ = codedInputStream.readBool();
                            break;
                        case 256:
                            this.bitField0_ |= Ints.MAX_POWER_OF_TWO;
                            this.telephonyOff_ = codedInputStream.readBool();
                            break;
                        case 264:
                            this.bitField0_ |= Integer.MIN_VALUE;
                            this.supportCallOutType_ = codedInputStream.readInt32();
                            break;
                        case 272:
                            this.bitField1_ |= 1;
                            this.isH323Enabled_ = codedInputStream.readBool();
                            break;
                        case 282:
                            Builder newBuilder = CountryCode.newBuilder();
                            codedInputStream.readMessage(newBuilder, extensionRegistryLite);
                            addCalloutCountryCodes(newBuilder.buildPartial());
                            break;
                        case 290:
                            Builder newBuilder2 = CountryCode.newBuilder();
                            codedInputStream.readMessage(newBuilder2, extensionRegistryLite);
                            addCallinCountryCodes(newBuilder2.buildPartial());
                            break;
                        case 298:
                            this.bitField1_ |= 8;
                            this.otherTeleConfInfo_ = codedInputStream.readBytes();
                            break;
                        case 304:
                            this.bitField1_ |= 16;
                            this.isSelfTelephonyOn_ = codedInputStream.readBool();
                            break;
                        case XmppError.XmppError_RemoteServerNotFound /*312*/:
                            this.bitField1_ |= 32;
                            this.usePmiAsMeetingID_ = codedInputStream.readBool();
                            break;
                        case 320:
                            this.bitField1_ |= 64;
                            this.originalMeetingNumber_ = codedInputStream.readInt64();
                            break;
                        case 328:
                            this.bitField1_ |= 128;
                            this.isCnMeeting_ = codedInputStream.readBool();
                            break;
                        case 338:
                            this.bitField1_ |= 256;
                            this.timeZoneId_ = codedInputStream.readBytes();
                            break;
                        case 346:
                            this.bitField1_ |= 512;
                            this.defaultcallInCountry_ = codedInputStream.readBytes();
                            break;
                        case 352:
                            this.bitField1_ |= 1024;
                            this.isTurnOnExternalAuth_ = codedInputStream.readBool();
                            break;
                        case 360:
                            this.bitField1_ |= 2048;
                            this.isOnlySignJoin_ = codedInputStream.readBool();
                            break;
                        case 370:
                            Builder newBuilder3 = AuthProto.newBuilder();
                            if (hasAuthProto()) {
                                newBuilder3.mergeFrom(getAuthProto());
                            }
                            codedInputStream.readMessage(newBuilder3, extensionRegistryLite);
                            setAuthProto(newBuilder3.buildPartial());
                            break;
                        case 376:
                            this.bitField1_ |= 8192;
                            this.progressingMeetingCount_ = codedInputStream.readInt32();
                            break;
                        case 384:
                            this.bitField1_ |= 16384;
                            this.meetingWaitStatus_ = codedInputStream.readInt32();
                            break;
                        case 394:
                            Builder newBuilder4 = TSPCallInInfo.newBuilder();
                            codedInputStream.readMessage(newBuilder4, extensionRegistryLite);
                            addTspCallinInfo(newBuilder4.buildPartial());
                            break;
                        case 402:
                            Builder newBuilder5 = AlterHost.newBuilder();
                            codedInputStream.readMessage(newBuilder5, extensionRegistryLite);
                            addAlterHost(newBuilder5.buildPartial());
                            break;
                        case 410:
                            Builder newBuilder6 = AvailableDialinCountry.newBuilder();
                            if (hasAvailableDialinCountry()) {
                                newBuilder6.mergeFrom(getAvailableDialinCountry());
                            }
                            codedInputStream.readMessage(newBuilder6, extensionRegistryLite);
                            setAvailableDialinCountry(newBuilder6.buildPartial());
                            break;
                        case 418:
                            this.bitField1_ |= 262144;
                            this.googleCalendarUrl_ = codedInputStream.readBytes();
                            break;
                        case 424:
                            this.bitField1_ |= 524288;
                            this.isEnableMeetingToPublic_ = codedInputStream.readBool();
                            break;
                        case 432:
                            this.bitField1_ |= 1048576;
                            this.isEnableAutoRecordingLocal_ = codedInputStream.readBool();
                            break;
                        case 440:
                            this.bitField1_ |= 2097152;
                            this.isEnableAutoRecordingCloud_ = codedInputStream.readBool();
                            break;
                        case ConfToolbar.BUTTON_VIEWONLY /*448*/:
                            this.bitField1_ |= 4194304;
                            this.isEnableAutoRecordingMtgLevelFirst_ = codedInputStream.readBool();
                            break;
                        case 456:
                            this.bitField1_ |= 8388608;
                            this.isEnableAudioWatermark_ = codedInputStream.readBool();
                            break;
                        case 464:
                            this.bitField1_ |= 16777216;
                            this.isWebRecurrenceMeeting_ = codedInputStream.readBool();
                            break;
                        case 474:
                            this.bitField1_ |= MediaHttpDownloader.MAXIMUM_CHUNK_SIZE;
                            this.dailinString_ = codedInputStream.readBytes();
                            break;
                        case CmmSIPCallFailReason.kSIPCall_FAIL_480_Temporarily_Unavailable /*480*/:
                            this.bitField1_ |= 67108864;
                            this.isEnableLanguageInterpretation_ = codedInputStream.readBool();
                            break;
                        case CmmSIPCallFailReason.kSIPCall_FAIL_488_Not_Acceptable_Here /*488*/:
                            this.bitField1_ |= 134217728;
                            this.isEnableWaitingRoom_ = codedInputStream.readBool();
                            break;
                        case 496:
                            this.bitField1_ |= 268435456;
                            this.isSupportWaitingRoom_ = codedInputStream.readBool();
                            break;
                        case 504:
                            this.bitField1_ |= 536870912;
                            this.jbhPriorTime_ = codedInputStream.readInt32();
                            break;
                        case 514:
                            this.bitField1_ |= Ints.MAX_POWER_OF_TWO;
                            this.joinMeetingUrlForInvite_ = codedInputStream.readBytes();
                            break;
                        case 520:
                            this.bitField1_ |= Integer.MIN_VALUE;
                            this.pstnOnlyUseTelephone_ = codedInputStream.readBool();
                            break;
                        case 528:
                            this.bitField2_ |= 1;
                            this.pstnUseOwnPhoneNumber_ = codedInputStream.readBool();
                            break;
                        case 536:
                            this.bitField2_ |= 2;
                            this.pstnPhoneNumberNotMatchCallout_ = codedInputStream.readBool();
                            break;
                        case 544:
                            this.bitField2_ |= 4;
                            this.pstnHideInviteByPhone_ = codedInputStream.readBool();
                            break;
                        default:
                            if (parseUnknownField(codedInputStream, extensionRegistryLite, readTag)) {
                                break;
                            } else {
                                return this;
                            }
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
                this.id_ = MeetingInfoProto.getDefaultInstance().getId();
                return this;
            }

            /* access modifiers changed from: 0000 */
            public void setId(ByteString byteString) {
                this.bitField0_ |= 1;
                this.id_ = byteString;
            }

            public boolean hasMeetingNumber() {
                return (this.bitField0_ & 2) == 2;
            }

            public long getMeetingNumber() {
                return this.meetingNumber_;
            }

            public Builder setMeetingNumber(long j) {
                this.bitField0_ |= 2;
                this.meetingNumber_ = j;
                return this;
            }

            public Builder clearMeetingNumber() {
                this.bitField0_ &= -3;
                this.meetingNumber_ = 0;
                return this;
            }

            public boolean hasTopic() {
                return (this.bitField0_ & 4) == 4;
            }

            public String getTopic() {
                Object obj = this.topic_;
                if (obj instanceof String) {
                    return (String) obj;
                }
                String stringUtf8 = ((ByteString) obj).toStringUtf8();
                this.topic_ = stringUtf8;
                return stringUtf8;
            }

            public Builder setTopic(String str) {
                if (str != null) {
                    this.bitField0_ |= 4;
                    this.topic_ = str;
                    return this;
                }
                throw new NullPointerException();
            }

            public Builder clearTopic() {
                this.bitField0_ &= -5;
                this.topic_ = MeetingInfoProto.getDefaultInstance().getTopic();
                return this;
            }

            /* access modifiers changed from: 0000 */
            public void setTopic(ByteString byteString) {
                this.bitField0_ |= 4;
                this.topic_ = byteString;
            }

            public boolean hasPassword() {
                return (this.bitField0_ & 8) == 8;
            }

            public String getPassword() {
                Object obj = this.password_;
                if (obj instanceof String) {
                    return (String) obj;
                }
                String stringUtf8 = ((ByteString) obj).toStringUtf8();
                this.password_ = stringUtf8;
                return stringUtf8;
            }

            public Builder setPassword(String str) {
                if (str != null) {
                    this.bitField0_ |= 8;
                    this.password_ = str;
                    return this;
                }
                throw new NullPointerException();
            }

            public Builder clearPassword() {
                this.bitField0_ &= -9;
                this.password_ = MeetingInfoProto.getDefaultInstance().getPassword();
                return this;
            }

            /* access modifiers changed from: 0000 */
            public void setPassword(ByteString byteString) {
                this.bitField0_ |= 8;
                this.password_ = byteString;
            }

            public boolean hasStartTime() {
                return (this.bitField0_ & 16) == 16;
            }

            public long getStartTime() {
                return this.startTime_;
            }

            public Builder setStartTime(long j) {
                this.bitField0_ |= 16;
                this.startTime_ = j;
                return this;
            }

            public Builder clearStartTime() {
                this.bitField0_ &= -17;
                this.startTime_ = 0;
                return this;
            }

            public boolean hasDuration() {
                return (this.bitField0_ & 32) == 32;
            }

            public int getDuration() {
                return this.duration_;
            }

            public Builder setDuration(int i) {
                this.bitField0_ |= 32;
                this.duration_ = i;
                return this;
            }

            public Builder clearDuration() {
                this.bitField0_ &= -33;
                this.duration_ = 0;
                return this;
            }

            public boolean hasType() {
                return (this.bitField0_ & 64) == 64;
            }

            public MeetingType getType() {
                return this.type_;
            }

            public Builder setType(MeetingType meetingType) {
                if (meetingType != null) {
                    this.bitField0_ |= 64;
                    this.type_ = meetingType;
                    return this;
                }
                throw new NullPointerException();
            }

            public Builder clearType() {
                this.bitField0_ &= -65;
                this.type_ = MeetingType.PRESCHEDULE;
                return this;
            }

            public boolean hasInviteEmailContent() {
                return (this.bitField0_ & 128) == 128;
            }

            public String getInviteEmailContent() {
                Object obj = this.inviteEmailContent_;
                if (obj instanceof String) {
                    return (String) obj;
                }
                String stringUtf8 = ((ByteString) obj).toStringUtf8();
                this.inviteEmailContent_ = stringUtf8;
                return stringUtf8;
            }

            public Builder setInviteEmailContent(String str) {
                if (str != null) {
                    this.bitField0_ |= 128;
                    this.inviteEmailContent_ = str;
                    return this;
                }
                throw new NullPointerException();
            }

            public Builder clearInviteEmailContent() {
                this.bitField0_ &= -129;
                this.inviteEmailContent_ = MeetingInfoProto.getDefaultInstance().getInviteEmailContent();
                return this;
            }

            /* access modifiers changed from: 0000 */
            public void setInviteEmailContent(ByteString byteString) {
                this.bitField0_ |= 128;
                this.inviteEmailContent_ = byteString;
            }

            public boolean hasMeetingStatus() {
                return (this.bitField0_ & 256) == 256;
            }

            public int getMeetingStatus() {
                return this.meetingStatus_;
            }

            public Builder setMeetingStatus(int i) {
                this.bitField0_ |= 256;
                this.meetingStatus_ = i;
                return this;
            }

            public Builder clearMeetingStatus() {
                this.bitField0_ &= -257;
                this.meetingStatus_ = 0;
                return this;
            }

            public boolean hasCanJoinBeforeHost() {
                return (this.bitField0_ & 512) == 512;
            }

            public boolean getCanJoinBeforeHost() {
                return this.canJoinBeforeHost_;
            }

            public Builder setCanJoinBeforeHost(boolean z) {
                this.bitField0_ |= 512;
                this.canJoinBeforeHost_ = z;
                return this;
            }

            public Builder clearCanJoinBeforeHost() {
                this.bitField0_ &= -513;
                this.canJoinBeforeHost_ = false;
                return this;
            }

            public boolean hasRepeatType() {
                return (this.bitField0_ & 1024) == 1024;
            }

            public int getRepeatType() {
                return this.repeatType_;
            }

            public Builder setRepeatType(int i) {
                this.bitField0_ |= 1024;
                this.repeatType_ = i;
                return this;
            }

            public Builder clearRepeatType() {
                this.bitField0_ &= -1025;
                this.repeatType_ = 0;
                return this;
            }

            public boolean hasRepeatEndTime() {
                return (this.bitField0_ & 2048) == 2048;
            }

            public long getRepeatEndTime() {
                return this.repeatEndTime_;
            }

            public Builder setRepeatEndTime(long j) {
                this.bitField0_ |= 2048;
                this.repeatEndTime_ = j;
                return this;
            }

            public Builder clearRepeatEndTime() {
                this.bitField0_ &= -2049;
                this.repeatEndTime_ = 0;
                return this;
            }

            public boolean hasJoinMeetingUrl() {
                return (this.bitField0_ & 4096) == 4096;
            }

            public String getJoinMeetingUrl() {
                Object obj = this.joinMeetingUrl_;
                if (obj instanceof String) {
                    return (String) obj;
                }
                String stringUtf8 = ((ByteString) obj).toStringUtf8();
                this.joinMeetingUrl_ = stringUtf8;
                return stringUtf8;
            }

            public Builder setJoinMeetingUrl(String str) {
                if (str != null) {
                    this.bitField0_ |= 4096;
                    this.joinMeetingUrl_ = str;
                    return this;
                }
                throw new NullPointerException();
            }

            public Builder clearJoinMeetingUrl() {
                this.bitField0_ &= -4097;
                this.joinMeetingUrl_ = MeetingInfoProto.getDefaultInstance().getJoinMeetingUrl();
                return this;
            }

            /* access modifiers changed from: 0000 */
            public void setJoinMeetingUrl(ByteString byteString) {
                this.bitField0_ |= 4096;
                this.joinMeetingUrl_ = byteString;
            }

            public boolean hasMeetingHostName() {
                return (this.bitField0_ & 8192) == 8192;
            }

            public String getMeetingHostName() {
                Object obj = this.meetingHostName_;
                if (obj instanceof String) {
                    return (String) obj;
                }
                String stringUtf8 = ((ByteString) obj).toStringUtf8();
                this.meetingHostName_ = stringUtf8;
                return stringUtf8;
            }

            public Builder setMeetingHostName(String str) {
                if (str != null) {
                    this.bitField0_ |= 8192;
                    this.meetingHostName_ = str;
                    return this;
                }
                throw new NullPointerException();
            }

            public Builder clearMeetingHostName() {
                this.bitField0_ &= -8193;
                this.meetingHostName_ = MeetingInfoProto.getDefaultInstance().getMeetingHostName();
                return this;
            }

            /* access modifiers changed from: 0000 */
            public void setMeetingHostName(ByteString byteString) {
                this.bitField0_ |= 8192;
                this.meetingHostName_ = byteString;
            }

            public boolean hasCallinNumber() {
                return (this.bitField0_ & 16384) == 16384;
            }

            public String getCallinNumber() {
                Object obj = this.callinNumber_;
                if (obj instanceof String) {
                    return (String) obj;
                }
                String stringUtf8 = ((ByteString) obj).toStringUtf8();
                this.callinNumber_ = stringUtf8;
                return stringUtf8;
            }

            public Builder setCallinNumber(String str) {
                if (str != null) {
                    this.bitField0_ |= 16384;
                    this.callinNumber_ = str;
                    return this;
                }
                throw new NullPointerException();
            }

            public Builder clearCallinNumber() {
                this.bitField0_ &= -16385;
                this.callinNumber_ = MeetingInfoProto.getDefaultInstance().getCallinNumber();
                return this;
            }

            /* access modifiers changed from: 0000 */
            public void setCallinNumber(ByteString byteString) {
                this.bitField0_ |= 16384;
                this.callinNumber_ = byteString;
            }

            public boolean hasPSTNEnabled() {
                return (this.bitField0_ & 32768) == 32768;
            }

            public boolean getPSTNEnabled() {
                return this.pSTNEnabled_;
            }

            public Builder setPSTNEnabled(boolean z) {
                this.bitField0_ |= 32768;
                this.pSTNEnabled_ = z;
                return this;
            }

            public Builder clearPSTNEnabled() {
                this.bitField0_ &= -32769;
                this.pSTNEnabled_ = false;
                return this;
            }

            public boolean hasH323Gateway() {
                return (this.bitField0_ & 65536) == 65536;
            }

            public String getH323Gateway() {
                Object obj = this.h323Gateway_;
                if (obj instanceof String) {
                    return (String) obj;
                }
                String stringUtf8 = ((ByteString) obj).toStringUtf8();
                this.h323Gateway_ = stringUtf8;
                return stringUtf8;
            }

            public Builder setH323Gateway(String str) {
                if (str != null) {
                    this.bitField0_ |= 65536;
                    this.h323Gateway_ = str;
                    return this;
                }
                throw new NullPointerException();
            }

            public Builder clearH323Gateway() {
                this.bitField0_ &= -65537;
                this.h323Gateway_ = MeetingInfoProto.getDefaultInstance().getH323Gateway();
                return this;
            }

            /* access modifiers changed from: 0000 */
            public void setH323Gateway(ByteString byteString) {
                this.bitField0_ |= 65536;
                this.h323Gateway_ = byteString;
            }

            public boolean hasIsAudioOnlyMeeting() {
                return (this.bitField0_ & 131072) == 131072;
            }

            public boolean getIsAudioOnlyMeeting() {
                return this.isAudioOnlyMeeting_;
            }

            public Builder setIsAudioOnlyMeeting(boolean z) {
                this.bitField0_ |= 131072;
                this.isAudioOnlyMeeting_ = z;
                return this;
            }

            public Builder clearIsAudioOnlyMeeting() {
                this.bitField0_ &= -131073;
                this.isAudioOnlyMeeting_ = false;
                return this;
            }

            public boolean hasIsShareOnlyMeeting() {
                return (this.bitField0_ & 262144) == 262144;
            }

            public boolean getIsShareOnlyMeeting() {
                return this.isShareOnlyMeeting_;
            }

            public Builder setIsShareOnlyMeeting(boolean z) {
                this.bitField0_ |= 262144;
                this.isShareOnlyMeeting_ = z;
                return this;
            }

            public Builder clearIsShareOnlyMeeting() {
                this.bitField0_ &= -262145;
                this.isShareOnlyMeeting_ = false;
                return this;
            }

            public boolean hasIsWebinar() {
                return (this.bitField0_ & 524288) == 524288;
            }

            public boolean getIsWebinar() {
                return this.isWebinar_;
            }

            public Builder setIsWebinar(boolean z) {
                this.bitField0_ |= 524288;
                this.isWebinar_ = z;
                return this;
            }

            public Builder clearIsWebinar() {
                this.bitField0_ &= -524289;
                this.isWebinar_ = false;
                return this;
            }

            public boolean hasAssistantId() {
                return (this.bitField0_ & 1048576) == 1048576;
            }

            public String getAssistantId() {
                Object obj = this.assistantId_;
                if (obj instanceof String) {
                    return (String) obj;
                }
                String stringUtf8 = ((ByteString) obj).toStringUtf8();
                this.assistantId_ = stringUtf8;
                return stringUtf8;
            }

            public Builder setAssistantId(String str) {
                if (str != null) {
                    this.bitField0_ |= 1048576;
                    this.assistantId_ = str;
                    return this;
                }
                throw new NullPointerException();
            }

            public Builder clearAssistantId() {
                this.bitField0_ &= -1048577;
                this.assistantId_ = MeetingInfoProto.getDefaultInstance().getAssistantId();
                return this;
            }

            /* access modifiers changed from: 0000 */
            public void setAssistantId(ByteString byteString) {
                this.bitField0_ |= 1048576;
                this.assistantId_ = byteString;
            }

            public boolean hasExtendMeetingType() {
                return (this.bitField0_ & 2097152) == 2097152;
            }

            public int getExtendMeetingType() {
                return this.extendMeetingType_;
            }

            public Builder setExtendMeetingType(int i) {
                this.bitField0_ |= 2097152;
                this.extendMeetingType_ = i;
                return this;
            }

            public Builder clearExtendMeetingType() {
                this.bitField0_ &= -2097153;
                this.extendMeetingType_ = 0;
                return this;
            }

            public boolean hasMeetingHostID() {
                return (this.bitField0_ & 4194304) == 4194304;
            }

            public String getMeetingHostID() {
                Object obj = this.meetingHostID_;
                if (obj instanceof String) {
                    return (String) obj;
                }
                String stringUtf8 = ((ByteString) obj).toStringUtf8();
                this.meetingHostID_ = stringUtf8;
                return stringUtf8;
            }

            public Builder setMeetingHostID(String str) {
                if (str != null) {
                    this.bitField0_ |= 4194304;
                    this.meetingHostID_ = str;
                    return this;
                }
                throw new NullPointerException();
            }

            public Builder clearMeetingHostID() {
                this.bitField0_ &= -4194305;
                this.meetingHostID_ = MeetingInfoProto.getDefaultInstance().getMeetingHostID();
                return this;
            }

            /* access modifiers changed from: 0000 */
            public void setMeetingHostID(ByteString byteString) {
                this.bitField0_ |= 4194304;
                this.meetingHostID_ = byteString;
            }

            public boolean hasInviteEmailSubject() {
                return (this.bitField0_ & 8388608) == 8388608;
            }

            public String getInviteEmailSubject() {
                Object obj = this.inviteEmailSubject_;
                if (obj instanceof String) {
                    return (String) obj;
                }
                String stringUtf8 = ((ByteString) obj).toStringUtf8();
                this.inviteEmailSubject_ = stringUtf8;
                return stringUtf8;
            }

            public Builder setInviteEmailSubject(String str) {
                if (str != null) {
                    this.bitField0_ |= 8388608;
                    this.inviteEmailSubject_ = str;
                    return this;
                }
                throw new NullPointerException();
            }

            public Builder clearInviteEmailSubject() {
                this.bitField0_ &= -8388609;
                this.inviteEmailSubject_ = MeetingInfoProto.getDefaultInstance().getInviteEmailSubject();
                return this;
            }

            /* access modifiers changed from: 0000 */
            public void setInviteEmailSubject(ByteString byteString) {
                this.bitField0_ |= 8388608;
                this.inviteEmailSubject_ = byteString;
            }

            public boolean hasPSTNNeedConfirm1() {
                return (this.bitField0_ & 16777216) == 16777216;
            }

            public boolean getPSTNNeedConfirm1() {
                return this.pSTNNeedConfirm1_;
            }

            public Builder setPSTNNeedConfirm1(boolean z) {
                this.bitField0_ |= 16777216;
                this.pSTNNeedConfirm1_ = z;
                return this;
            }

            public Builder clearPSTNNeedConfirm1() {
                this.bitField0_ &= -16777217;
                this.pSTNNeedConfirm1_ = false;
                return this;
            }

            public boolean hasInviteEmailContentWithTime() {
                return (this.bitField0_ & MediaHttpDownloader.MAXIMUM_CHUNK_SIZE) == 33554432;
            }

            public String getInviteEmailContentWithTime() {
                Object obj = this.inviteEmailContentWithTime_;
                if (obj instanceof String) {
                    return (String) obj;
                }
                String stringUtf8 = ((ByteString) obj).toStringUtf8();
                this.inviteEmailContentWithTime_ = stringUtf8;
                return stringUtf8;
            }

            public Builder setInviteEmailContentWithTime(String str) {
                if (str != null) {
                    this.bitField0_ |= MediaHttpDownloader.MAXIMUM_CHUNK_SIZE;
                    this.inviteEmailContentWithTime_ = str;
                    return this;
                }
                throw new NullPointerException();
            }

            public Builder clearInviteEmailContentWithTime() {
                this.bitField0_ &= -33554433;
                this.inviteEmailContentWithTime_ = MeetingInfoProto.getDefaultInstance().getInviteEmailContentWithTime();
                return this;
            }

            /* access modifiers changed from: 0000 */
            public void setInviteEmailContentWithTime(ByteString byteString) {
                this.bitField0_ |= MediaHttpDownloader.MAXIMUM_CHUNK_SIZE;
                this.inviteEmailContentWithTime_ = byteString;
            }

            public boolean hasWebinarRegUrl() {
                return (this.bitField0_ & 67108864) == 67108864;
            }

            public String getWebinarRegUrl() {
                Object obj = this.webinarRegUrl_;
                if (obj instanceof String) {
                    return (String) obj;
                }
                String stringUtf8 = ((ByteString) obj).toStringUtf8();
                this.webinarRegUrl_ = stringUtf8;
                return stringUtf8;
            }

            public Builder setWebinarRegUrl(String str) {
                if (str != null) {
                    this.bitField0_ |= 67108864;
                    this.webinarRegUrl_ = str;
                    return this;
                }
                throw new NullPointerException();
            }

            public Builder clearWebinarRegUrl() {
                this.bitField0_ &= -67108865;
                this.webinarRegUrl_ = MeetingInfoProto.getDefaultInstance().getWebinarRegUrl();
                return this;
            }

            /* access modifiers changed from: 0000 */
            public void setWebinarRegUrl(ByteString byteString) {
                this.bitField0_ |= 67108864;
                this.webinarRegUrl_ = byteString;
            }

            public boolean hasHostVideoOff() {
                return (this.bitField0_ & 134217728) == 134217728;
            }

            public boolean getHostVideoOff() {
                return this.hostVideoOff_;
            }

            public Builder setHostVideoOff(boolean z) {
                this.bitField0_ |= 134217728;
                this.hostVideoOff_ = z;
                return this;
            }

            public Builder clearHostVideoOff() {
                this.bitField0_ &= -134217729;
                this.hostVideoOff_ = false;
                return this;
            }

            public boolean hasAttendeeVideoOff() {
                return (this.bitField0_ & 268435456) == 268435456;
            }

            public boolean getAttendeeVideoOff() {
                return this.attendeeVideoOff_;
            }

            public Builder setAttendeeVideoOff(boolean z) {
                this.bitField0_ |= 268435456;
                this.attendeeVideoOff_ = z;
                return this;
            }

            public Builder clearAttendeeVideoOff() {
                this.bitField0_ &= -268435457;
                this.attendeeVideoOff_ = false;
                return this;
            }

            public boolean hasVoipOff() {
                return (this.bitField0_ & 536870912) == 536870912;
            }

            public boolean getVoipOff() {
                return this.voipOff_;
            }

            public Builder setVoipOff(boolean z) {
                this.bitField0_ |= 536870912;
                this.voipOff_ = z;
                return this;
            }

            public Builder clearVoipOff() {
                this.bitField0_ &= -536870913;
                this.voipOff_ = false;
                return this;
            }

            public boolean hasTelephonyOff() {
                return (this.bitField0_ & Ints.MAX_POWER_OF_TWO) == 1073741824;
            }

            public boolean getTelephonyOff() {
                return this.telephonyOff_;
            }

            public Builder setTelephonyOff(boolean z) {
                this.bitField0_ |= Ints.MAX_POWER_OF_TWO;
                this.telephonyOff_ = z;
                return this;
            }

            public Builder clearTelephonyOff() {
                this.bitField0_ &= -1073741825;
                this.telephonyOff_ = false;
                return this;
            }

            public boolean hasSupportCallOutType() {
                return (this.bitField0_ & Integer.MIN_VALUE) == Integer.MIN_VALUE;
            }

            public int getSupportCallOutType() {
                return this.supportCallOutType_;
            }

            public Builder setSupportCallOutType(int i) {
                this.bitField0_ |= Integer.MIN_VALUE;
                this.supportCallOutType_ = i;
                return this;
            }

            public Builder clearSupportCallOutType() {
                this.bitField0_ &= Integer.MAX_VALUE;
                this.supportCallOutType_ = 0;
                return this;
            }

            public boolean hasIsH323Enabled() {
                return (this.bitField1_ & 1) == 1;
            }

            public boolean getIsH323Enabled() {
                return this.isH323Enabled_;
            }

            public Builder setIsH323Enabled(boolean z) {
                this.bitField1_ |= 1;
                this.isH323Enabled_ = z;
                return this;
            }

            public Builder clearIsH323Enabled() {
                this.bitField1_ &= -2;
                this.isH323Enabled_ = false;
                return this;
            }

            private void ensureCalloutCountryCodesIsMutable() {
                if ((this.bitField1_ & 2) != 2) {
                    this.calloutCountryCodes_ = new ArrayList(this.calloutCountryCodes_);
                    this.bitField1_ |= 2;
                }
            }

            public List<CountryCode> getCalloutCountryCodesList() {
                return Collections.unmodifiableList(this.calloutCountryCodes_);
            }

            public int getCalloutCountryCodesCount() {
                return this.calloutCountryCodes_.size();
            }

            public CountryCode getCalloutCountryCodes(int i) {
                return (CountryCode) this.calloutCountryCodes_.get(i);
            }

            public Builder setCalloutCountryCodes(int i, CountryCode countryCode) {
                if (countryCode != null) {
                    ensureCalloutCountryCodesIsMutable();
                    this.calloutCountryCodes_.set(i, countryCode);
                    return this;
                }
                throw new NullPointerException();
            }

            public Builder setCalloutCountryCodes(int i, Builder builder) {
                ensureCalloutCountryCodesIsMutable();
                this.calloutCountryCodes_.set(i, builder.build());
                return this;
            }

            public Builder addCalloutCountryCodes(CountryCode countryCode) {
                if (countryCode != null) {
                    ensureCalloutCountryCodesIsMutable();
                    this.calloutCountryCodes_.add(countryCode);
                    return this;
                }
                throw new NullPointerException();
            }

            public Builder addCalloutCountryCodes(int i, CountryCode countryCode) {
                if (countryCode != null) {
                    ensureCalloutCountryCodesIsMutable();
                    this.calloutCountryCodes_.add(i, countryCode);
                    return this;
                }
                throw new NullPointerException();
            }

            public Builder addCalloutCountryCodes(Builder builder) {
                ensureCalloutCountryCodesIsMutable();
                this.calloutCountryCodes_.add(builder.build());
                return this;
            }

            public Builder addCalloutCountryCodes(int i, Builder builder) {
                ensureCalloutCountryCodesIsMutable();
                this.calloutCountryCodes_.add(i, builder.build());
                return this;
            }

            public Builder addAllCalloutCountryCodes(Iterable<? extends CountryCode> iterable) {
                ensureCalloutCountryCodesIsMutable();
                com.google.protobuf.GeneratedMessageLite.Builder.addAll(iterable, this.calloutCountryCodes_);
                return this;
            }

            public Builder clearCalloutCountryCodes() {
                this.calloutCountryCodes_ = Collections.emptyList();
                this.bitField1_ &= -3;
                return this;
            }

            public Builder removeCalloutCountryCodes(int i) {
                ensureCalloutCountryCodesIsMutable();
                this.calloutCountryCodes_.remove(i);
                return this;
            }

            private void ensureCallinCountryCodesIsMutable() {
                if ((this.bitField1_ & 4) != 4) {
                    this.callinCountryCodes_ = new ArrayList(this.callinCountryCodes_);
                    this.bitField1_ |= 4;
                }
            }

            public List<CountryCode> getCallinCountryCodesList() {
                return Collections.unmodifiableList(this.callinCountryCodes_);
            }

            public int getCallinCountryCodesCount() {
                return this.callinCountryCodes_.size();
            }

            public CountryCode getCallinCountryCodes(int i) {
                return (CountryCode) this.callinCountryCodes_.get(i);
            }

            public Builder setCallinCountryCodes(int i, CountryCode countryCode) {
                if (countryCode != null) {
                    ensureCallinCountryCodesIsMutable();
                    this.callinCountryCodes_.set(i, countryCode);
                    return this;
                }
                throw new NullPointerException();
            }

            public Builder setCallinCountryCodes(int i, Builder builder) {
                ensureCallinCountryCodesIsMutable();
                this.callinCountryCodes_.set(i, builder.build());
                return this;
            }

            public Builder addCallinCountryCodes(CountryCode countryCode) {
                if (countryCode != null) {
                    ensureCallinCountryCodesIsMutable();
                    this.callinCountryCodes_.add(countryCode);
                    return this;
                }
                throw new NullPointerException();
            }

            public Builder addCallinCountryCodes(int i, CountryCode countryCode) {
                if (countryCode != null) {
                    ensureCallinCountryCodesIsMutable();
                    this.callinCountryCodes_.add(i, countryCode);
                    return this;
                }
                throw new NullPointerException();
            }

            public Builder addCallinCountryCodes(Builder builder) {
                ensureCallinCountryCodesIsMutable();
                this.callinCountryCodes_.add(builder.build());
                return this;
            }

            public Builder addCallinCountryCodes(int i, Builder builder) {
                ensureCallinCountryCodesIsMutable();
                this.callinCountryCodes_.add(i, builder.build());
                return this;
            }

            public Builder addAllCallinCountryCodes(Iterable<? extends CountryCode> iterable) {
                ensureCallinCountryCodesIsMutable();
                com.google.protobuf.GeneratedMessageLite.Builder.addAll(iterable, this.callinCountryCodes_);
                return this;
            }

            public Builder clearCallinCountryCodes() {
                this.callinCountryCodes_ = Collections.emptyList();
                this.bitField1_ &= -5;
                return this;
            }

            public Builder removeCallinCountryCodes(int i) {
                ensureCallinCountryCodesIsMutable();
                this.callinCountryCodes_.remove(i);
                return this;
            }

            public boolean hasOtherTeleConfInfo() {
                return (this.bitField1_ & 8) == 8;
            }

            public String getOtherTeleConfInfo() {
                Object obj = this.otherTeleConfInfo_;
                if (obj instanceof String) {
                    return (String) obj;
                }
                String stringUtf8 = ((ByteString) obj).toStringUtf8();
                this.otherTeleConfInfo_ = stringUtf8;
                return stringUtf8;
            }

            public Builder setOtherTeleConfInfo(String str) {
                if (str != null) {
                    this.bitField1_ |= 8;
                    this.otherTeleConfInfo_ = str;
                    return this;
                }
                throw new NullPointerException();
            }

            public Builder clearOtherTeleConfInfo() {
                this.bitField1_ &= -9;
                this.otherTeleConfInfo_ = MeetingInfoProto.getDefaultInstance().getOtherTeleConfInfo();
                return this;
            }

            /* access modifiers changed from: 0000 */
            public void setOtherTeleConfInfo(ByteString byteString) {
                this.bitField1_ |= 8;
                this.otherTeleConfInfo_ = byteString;
            }

            public boolean hasIsSelfTelephonyOn() {
                return (this.bitField1_ & 16) == 16;
            }

            public boolean getIsSelfTelephonyOn() {
                return this.isSelfTelephonyOn_;
            }

            public Builder setIsSelfTelephonyOn(boolean z) {
                this.bitField1_ |= 16;
                this.isSelfTelephonyOn_ = z;
                return this;
            }

            public Builder clearIsSelfTelephonyOn() {
                this.bitField1_ &= -17;
                this.isSelfTelephonyOn_ = false;
                return this;
            }

            public boolean hasUsePmiAsMeetingID() {
                return (this.bitField1_ & 32) == 32;
            }

            public boolean getUsePmiAsMeetingID() {
                return this.usePmiAsMeetingID_;
            }

            public Builder setUsePmiAsMeetingID(boolean z) {
                this.bitField1_ |= 32;
                this.usePmiAsMeetingID_ = z;
                return this;
            }

            public Builder clearUsePmiAsMeetingID() {
                this.bitField1_ &= -33;
                this.usePmiAsMeetingID_ = false;
                return this;
            }

            public boolean hasOriginalMeetingNumber() {
                return (this.bitField1_ & 64) == 64;
            }

            public long getOriginalMeetingNumber() {
                return this.originalMeetingNumber_;
            }

            public Builder setOriginalMeetingNumber(long j) {
                this.bitField1_ |= 64;
                this.originalMeetingNumber_ = j;
                return this;
            }

            public Builder clearOriginalMeetingNumber() {
                this.bitField1_ &= -65;
                this.originalMeetingNumber_ = 0;
                return this;
            }

            public boolean hasIsCnMeeting() {
                return (this.bitField1_ & 128) == 128;
            }

            public boolean getIsCnMeeting() {
                return this.isCnMeeting_;
            }

            public Builder setIsCnMeeting(boolean z) {
                this.bitField1_ |= 128;
                this.isCnMeeting_ = z;
                return this;
            }

            public Builder clearIsCnMeeting() {
                this.bitField1_ &= -129;
                this.isCnMeeting_ = false;
                return this;
            }

            public boolean hasTimeZoneId() {
                return (this.bitField1_ & 256) == 256;
            }

            public String getTimeZoneId() {
                Object obj = this.timeZoneId_;
                if (obj instanceof String) {
                    return (String) obj;
                }
                String stringUtf8 = ((ByteString) obj).toStringUtf8();
                this.timeZoneId_ = stringUtf8;
                return stringUtf8;
            }

            public Builder setTimeZoneId(String str) {
                if (str != null) {
                    this.bitField1_ |= 256;
                    this.timeZoneId_ = str;
                    return this;
                }
                throw new NullPointerException();
            }

            public Builder clearTimeZoneId() {
                this.bitField1_ &= -257;
                this.timeZoneId_ = MeetingInfoProto.getDefaultInstance().getTimeZoneId();
                return this;
            }

            /* access modifiers changed from: 0000 */
            public void setTimeZoneId(ByteString byteString) {
                this.bitField1_ |= 256;
                this.timeZoneId_ = byteString;
            }

            public boolean hasDefaultcallInCountry() {
                return (this.bitField1_ & 512) == 512;
            }

            public String getDefaultcallInCountry() {
                Object obj = this.defaultcallInCountry_;
                if (obj instanceof String) {
                    return (String) obj;
                }
                String stringUtf8 = ((ByteString) obj).toStringUtf8();
                this.defaultcallInCountry_ = stringUtf8;
                return stringUtf8;
            }

            public Builder setDefaultcallInCountry(String str) {
                if (str != null) {
                    this.bitField1_ |= 512;
                    this.defaultcallInCountry_ = str;
                    return this;
                }
                throw new NullPointerException();
            }

            public Builder clearDefaultcallInCountry() {
                this.bitField1_ &= -513;
                this.defaultcallInCountry_ = MeetingInfoProto.getDefaultInstance().getDefaultcallInCountry();
                return this;
            }

            /* access modifiers changed from: 0000 */
            public void setDefaultcallInCountry(ByteString byteString) {
                this.bitField1_ |= 512;
                this.defaultcallInCountry_ = byteString;
            }

            public boolean hasIsTurnOnExternalAuth() {
                return (this.bitField1_ & 1024) == 1024;
            }

            public boolean getIsTurnOnExternalAuth() {
                return this.isTurnOnExternalAuth_;
            }

            public Builder setIsTurnOnExternalAuth(boolean z) {
                this.bitField1_ |= 1024;
                this.isTurnOnExternalAuth_ = z;
                return this;
            }

            public Builder clearIsTurnOnExternalAuth() {
                this.bitField1_ &= -1025;
                this.isTurnOnExternalAuth_ = false;
                return this;
            }

            public boolean hasIsOnlySignJoin() {
                return (this.bitField1_ & 2048) == 2048;
            }

            public boolean getIsOnlySignJoin() {
                return this.isOnlySignJoin_;
            }

            public Builder setIsOnlySignJoin(boolean z) {
                this.bitField1_ |= 2048;
                this.isOnlySignJoin_ = z;
                return this;
            }

            public Builder clearIsOnlySignJoin() {
                this.bitField1_ &= -2049;
                this.isOnlySignJoin_ = false;
                return this;
            }

            public boolean hasAuthProto() {
                return (this.bitField1_ & 4096) == 4096;
            }

            public AuthProto getAuthProto() {
                return this.authProto_;
            }

            public Builder setAuthProto(AuthProto authProto) {
                if (authProto != null) {
                    this.authProto_ = authProto;
                    this.bitField1_ |= 4096;
                    return this;
                }
                throw new NullPointerException();
            }

            public Builder setAuthProto(Builder builder) {
                this.authProto_ = builder.build();
                this.bitField1_ |= 4096;
                return this;
            }

            public Builder mergeAuthProto(AuthProto authProto) {
                if ((this.bitField1_ & 4096) != 4096 || this.authProto_ == AuthProto.getDefaultInstance()) {
                    this.authProto_ = authProto;
                } else {
                    this.authProto_ = AuthProto.newBuilder(this.authProto_).mergeFrom(authProto).buildPartial();
                }
                this.bitField1_ |= 4096;
                return this;
            }

            public Builder clearAuthProto() {
                this.authProto_ = AuthProto.getDefaultInstance();
                this.bitField1_ &= -4097;
                return this;
            }

            public boolean hasProgressingMeetingCount() {
                return (this.bitField1_ & 8192) == 8192;
            }

            public int getProgressingMeetingCount() {
                return this.progressingMeetingCount_;
            }

            public Builder setProgressingMeetingCount(int i) {
                this.bitField1_ |= 8192;
                this.progressingMeetingCount_ = i;
                return this;
            }

            public Builder clearProgressingMeetingCount() {
                this.bitField1_ &= -8193;
                this.progressingMeetingCount_ = 0;
                return this;
            }

            public boolean hasMeetingWaitStatus() {
                return (this.bitField1_ & 16384) == 16384;
            }

            public int getMeetingWaitStatus() {
                return this.meetingWaitStatus_;
            }

            public Builder setMeetingWaitStatus(int i) {
                this.bitField1_ |= 16384;
                this.meetingWaitStatus_ = i;
                return this;
            }

            public Builder clearMeetingWaitStatus() {
                this.bitField1_ &= -16385;
                this.meetingWaitStatus_ = 0;
                return this;
            }

            private void ensureTspCallinInfoIsMutable() {
                if ((this.bitField1_ & 32768) != 32768) {
                    this.tspCallinInfo_ = new ArrayList(this.tspCallinInfo_);
                    this.bitField1_ |= 32768;
                }
            }

            public List<TSPCallInInfo> getTspCallinInfoList() {
                return Collections.unmodifiableList(this.tspCallinInfo_);
            }

            public int getTspCallinInfoCount() {
                return this.tspCallinInfo_.size();
            }

            public TSPCallInInfo getTspCallinInfo(int i) {
                return (TSPCallInInfo) this.tspCallinInfo_.get(i);
            }

            public Builder setTspCallinInfo(int i, TSPCallInInfo tSPCallInInfo) {
                if (tSPCallInInfo != null) {
                    ensureTspCallinInfoIsMutable();
                    this.tspCallinInfo_.set(i, tSPCallInInfo);
                    return this;
                }
                throw new NullPointerException();
            }

            public Builder setTspCallinInfo(int i, Builder builder) {
                ensureTspCallinInfoIsMutable();
                this.tspCallinInfo_.set(i, builder.build());
                return this;
            }

            public Builder addTspCallinInfo(TSPCallInInfo tSPCallInInfo) {
                if (tSPCallInInfo != null) {
                    ensureTspCallinInfoIsMutable();
                    this.tspCallinInfo_.add(tSPCallInInfo);
                    return this;
                }
                throw new NullPointerException();
            }

            public Builder addTspCallinInfo(int i, TSPCallInInfo tSPCallInInfo) {
                if (tSPCallInInfo != null) {
                    ensureTspCallinInfoIsMutable();
                    this.tspCallinInfo_.add(i, tSPCallInInfo);
                    return this;
                }
                throw new NullPointerException();
            }

            public Builder addTspCallinInfo(Builder builder) {
                ensureTspCallinInfoIsMutable();
                this.tspCallinInfo_.add(builder.build());
                return this;
            }

            public Builder addTspCallinInfo(int i, Builder builder) {
                ensureTspCallinInfoIsMutable();
                this.tspCallinInfo_.add(i, builder.build());
                return this;
            }

            public Builder addAllTspCallinInfo(Iterable<? extends TSPCallInInfo> iterable) {
                ensureTspCallinInfoIsMutable();
                com.google.protobuf.GeneratedMessageLite.Builder.addAll(iterable, this.tspCallinInfo_);
                return this;
            }

            public Builder clearTspCallinInfo() {
                this.tspCallinInfo_ = Collections.emptyList();
                this.bitField1_ &= -32769;
                return this;
            }

            public Builder removeTspCallinInfo(int i) {
                ensureTspCallinInfoIsMutable();
                this.tspCallinInfo_.remove(i);
                return this;
            }

            private void ensureAlterHostIsMutable() {
                if ((this.bitField1_ & 65536) != 65536) {
                    this.alterHost_ = new ArrayList(this.alterHost_);
                    this.bitField1_ |= 65536;
                }
            }

            public List<AlterHost> getAlterHostList() {
                return Collections.unmodifiableList(this.alterHost_);
            }

            public int getAlterHostCount() {
                return this.alterHost_.size();
            }

            public AlterHost getAlterHost(int i) {
                return (AlterHost) this.alterHost_.get(i);
            }

            public Builder setAlterHost(int i, AlterHost alterHost) {
                if (alterHost != null) {
                    ensureAlterHostIsMutable();
                    this.alterHost_.set(i, alterHost);
                    return this;
                }
                throw new NullPointerException();
            }

            public Builder setAlterHost(int i, Builder builder) {
                ensureAlterHostIsMutable();
                this.alterHost_.set(i, builder.build());
                return this;
            }

            public Builder addAlterHost(AlterHost alterHost) {
                if (alterHost != null) {
                    ensureAlterHostIsMutable();
                    this.alterHost_.add(alterHost);
                    return this;
                }
                throw new NullPointerException();
            }

            public Builder addAlterHost(int i, AlterHost alterHost) {
                if (alterHost != null) {
                    ensureAlterHostIsMutable();
                    this.alterHost_.add(i, alterHost);
                    return this;
                }
                throw new NullPointerException();
            }

            public Builder addAlterHost(Builder builder) {
                ensureAlterHostIsMutable();
                this.alterHost_.add(builder.build());
                return this;
            }

            public Builder addAlterHost(int i, Builder builder) {
                ensureAlterHostIsMutable();
                this.alterHost_.add(i, builder.build());
                return this;
            }

            public Builder addAllAlterHost(Iterable<? extends AlterHost> iterable) {
                ensureAlterHostIsMutable();
                com.google.protobuf.GeneratedMessageLite.Builder.addAll(iterable, this.alterHost_);
                return this;
            }

            public Builder clearAlterHost() {
                this.alterHost_ = Collections.emptyList();
                this.bitField1_ &= -65537;
                return this;
            }

            public Builder removeAlterHost(int i) {
                ensureAlterHostIsMutable();
                this.alterHost_.remove(i);
                return this;
            }

            public boolean hasAvailableDialinCountry() {
                return (this.bitField1_ & 131072) == 131072;
            }

            public AvailableDialinCountry getAvailableDialinCountry() {
                return this.availableDialinCountry_;
            }

            public Builder setAvailableDialinCountry(AvailableDialinCountry availableDialinCountry) {
                if (availableDialinCountry != null) {
                    this.availableDialinCountry_ = availableDialinCountry;
                    this.bitField1_ |= 131072;
                    return this;
                }
                throw new NullPointerException();
            }

            public Builder setAvailableDialinCountry(Builder builder) {
                this.availableDialinCountry_ = builder.build();
                this.bitField1_ |= 131072;
                return this;
            }

            public Builder mergeAvailableDialinCountry(AvailableDialinCountry availableDialinCountry) {
                if ((this.bitField1_ & 131072) != 131072 || this.availableDialinCountry_ == AvailableDialinCountry.getDefaultInstance()) {
                    this.availableDialinCountry_ = availableDialinCountry;
                } else {
                    this.availableDialinCountry_ = AvailableDialinCountry.newBuilder(this.availableDialinCountry_).mergeFrom(availableDialinCountry).buildPartial();
                }
                this.bitField1_ |= 131072;
                return this;
            }

            public Builder clearAvailableDialinCountry() {
                this.availableDialinCountry_ = AvailableDialinCountry.getDefaultInstance();
                this.bitField1_ &= -131073;
                return this;
            }

            public boolean hasGoogleCalendarUrl() {
                return (this.bitField1_ & 262144) == 262144;
            }

            public String getGoogleCalendarUrl() {
                Object obj = this.googleCalendarUrl_;
                if (obj instanceof String) {
                    return (String) obj;
                }
                String stringUtf8 = ((ByteString) obj).toStringUtf8();
                this.googleCalendarUrl_ = stringUtf8;
                return stringUtf8;
            }

            public Builder setGoogleCalendarUrl(String str) {
                if (str != null) {
                    this.bitField1_ |= 262144;
                    this.googleCalendarUrl_ = str;
                    return this;
                }
                throw new NullPointerException();
            }

            public Builder clearGoogleCalendarUrl() {
                this.bitField1_ &= -262145;
                this.googleCalendarUrl_ = MeetingInfoProto.getDefaultInstance().getGoogleCalendarUrl();
                return this;
            }

            /* access modifiers changed from: 0000 */
            public void setGoogleCalendarUrl(ByteString byteString) {
                this.bitField1_ |= 262144;
                this.googleCalendarUrl_ = byteString;
            }

            public boolean hasIsEnableMeetingToPublic() {
                return (this.bitField1_ & 524288) == 524288;
            }

            public boolean getIsEnableMeetingToPublic() {
                return this.isEnableMeetingToPublic_;
            }

            public Builder setIsEnableMeetingToPublic(boolean z) {
                this.bitField1_ |= 524288;
                this.isEnableMeetingToPublic_ = z;
                return this;
            }

            public Builder clearIsEnableMeetingToPublic() {
                this.bitField1_ &= -524289;
                this.isEnableMeetingToPublic_ = false;
                return this;
            }

            public boolean hasIsEnableAutoRecordingLocal() {
                return (this.bitField1_ & 1048576) == 1048576;
            }

            public boolean getIsEnableAutoRecordingLocal() {
                return this.isEnableAutoRecordingLocal_;
            }

            public Builder setIsEnableAutoRecordingLocal(boolean z) {
                this.bitField1_ |= 1048576;
                this.isEnableAutoRecordingLocal_ = z;
                return this;
            }

            public Builder clearIsEnableAutoRecordingLocal() {
                this.bitField1_ &= -1048577;
                this.isEnableAutoRecordingLocal_ = false;
                return this;
            }

            public boolean hasIsEnableAutoRecordingCloud() {
                return (this.bitField1_ & 2097152) == 2097152;
            }

            public boolean getIsEnableAutoRecordingCloud() {
                return this.isEnableAutoRecordingCloud_;
            }

            public Builder setIsEnableAutoRecordingCloud(boolean z) {
                this.bitField1_ |= 2097152;
                this.isEnableAutoRecordingCloud_ = z;
                return this;
            }

            public Builder clearIsEnableAutoRecordingCloud() {
                this.bitField1_ &= -2097153;
                this.isEnableAutoRecordingCloud_ = false;
                return this;
            }

            public boolean hasIsEnableAutoRecordingMtgLevelFirst() {
                return (this.bitField1_ & 4194304) == 4194304;
            }

            public boolean getIsEnableAutoRecordingMtgLevelFirst() {
                return this.isEnableAutoRecordingMtgLevelFirst_;
            }

            public Builder setIsEnableAutoRecordingMtgLevelFirst(boolean z) {
                this.bitField1_ |= 4194304;
                this.isEnableAutoRecordingMtgLevelFirst_ = z;
                return this;
            }

            public Builder clearIsEnableAutoRecordingMtgLevelFirst() {
                this.bitField1_ &= -4194305;
                this.isEnableAutoRecordingMtgLevelFirst_ = false;
                return this;
            }

            public boolean hasIsEnableAudioWatermark() {
                return (this.bitField1_ & 8388608) == 8388608;
            }

            public boolean getIsEnableAudioWatermark() {
                return this.isEnableAudioWatermark_;
            }

            public Builder setIsEnableAudioWatermark(boolean z) {
                this.bitField1_ |= 8388608;
                this.isEnableAudioWatermark_ = z;
                return this;
            }

            public Builder clearIsEnableAudioWatermark() {
                this.bitField1_ &= -8388609;
                this.isEnableAudioWatermark_ = false;
                return this;
            }

            public boolean hasIsWebRecurrenceMeeting() {
                return (this.bitField1_ & 16777216) == 16777216;
            }

            public boolean getIsWebRecurrenceMeeting() {
                return this.isWebRecurrenceMeeting_;
            }

            public Builder setIsWebRecurrenceMeeting(boolean z) {
                this.bitField1_ |= 16777216;
                this.isWebRecurrenceMeeting_ = z;
                return this;
            }

            public Builder clearIsWebRecurrenceMeeting() {
                this.bitField1_ &= -16777217;
                this.isWebRecurrenceMeeting_ = false;
                return this;
            }

            public boolean hasDailinString() {
                return (this.bitField1_ & MediaHttpDownloader.MAXIMUM_CHUNK_SIZE) == 33554432;
            }

            public String getDailinString() {
                Object obj = this.dailinString_;
                if (obj instanceof String) {
                    return (String) obj;
                }
                String stringUtf8 = ((ByteString) obj).toStringUtf8();
                this.dailinString_ = stringUtf8;
                return stringUtf8;
            }

            public Builder setDailinString(String str) {
                if (str != null) {
                    this.bitField1_ |= MediaHttpDownloader.MAXIMUM_CHUNK_SIZE;
                    this.dailinString_ = str;
                    return this;
                }
                throw new NullPointerException();
            }

            public Builder clearDailinString() {
                this.bitField1_ &= -33554433;
                this.dailinString_ = MeetingInfoProto.getDefaultInstance().getDailinString();
                return this;
            }

            /* access modifiers changed from: 0000 */
            public void setDailinString(ByteString byteString) {
                this.bitField1_ |= MediaHttpDownloader.MAXIMUM_CHUNK_SIZE;
                this.dailinString_ = byteString;
            }

            public boolean hasIsEnableLanguageInterpretation() {
                return (this.bitField1_ & 67108864) == 67108864;
            }

            public boolean getIsEnableLanguageInterpretation() {
                return this.isEnableLanguageInterpretation_;
            }

            public Builder setIsEnableLanguageInterpretation(boolean z) {
                this.bitField1_ |= 67108864;
                this.isEnableLanguageInterpretation_ = z;
                return this;
            }

            public Builder clearIsEnableLanguageInterpretation() {
                this.bitField1_ &= -67108865;
                this.isEnableLanguageInterpretation_ = false;
                return this;
            }

            public boolean hasIsEnableWaitingRoom() {
                return (this.bitField1_ & 134217728) == 134217728;
            }

            public boolean getIsEnableWaitingRoom() {
                return this.isEnableWaitingRoom_;
            }

            public Builder setIsEnableWaitingRoom(boolean z) {
                this.bitField1_ |= 134217728;
                this.isEnableWaitingRoom_ = z;
                return this;
            }

            public Builder clearIsEnableWaitingRoom() {
                this.bitField1_ &= -134217729;
                this.isEnableWaitingRoom_ = false;
                return this;
            }

            public boolean hasIsSupportWaitingRoom() {
                return (this.bitField1_ & 268435456) == 268435456;
            }

            public boolean getIsSupportWaitingRoom() {
                return this.isSupportWaitingRoom_;
            }

            public Builder setIsSupportWaitingRoom(boolean z) {
                this.bitField1_ |= 268435456;
                this.isSupportWaitingRoom_ = z;
                return this;
            }

            public Builder clearIsSupportWaitingRoom() {
                this.bitField1_ &= -268435457;
                this.isSupportWaitingRoom_ = false;
                return this;
            }

            public boolean hasJbhPriorTime() {
                return (this.bitField1_ & 536870912) == 536870912;
            }

            public int getJbhPriorTime() {
                return this.jbhPriorTime_;
            }

            public Builder setJbhPriorTime(int i) {
                this.bitField1_ |= 536870912;
                this.jbhPriorTime_ = i;
                return this;
            }

            public Builder clearJbhPriorTime() {
                this.bitField1_ &= -536870913;
                this.jbhPriorTime_ = 0;
                return this;
            }

            public boolean hasJoinMeetingUrlForInvite() {
                return (this.bitField1_ & Ints.MAX_POWER_OF_TWO) == 1073741824;
            }

            public String getJoinMeetingUrlForInvite() {
                Object obj = this.joinMeetingUrlForInvite_;
                if (obj instanceof String) {
                    return (String) obj;
                }
                String stringUtf8 = ((ByteString) obj).toStringUtf8();
                this.joinMeetingUrlForInvite_ = stringUtf8;
                return stringUtf8;
            }

            public Builder setJoinMeetingUrlForInvite(String str) {
                if (str != null) {
                    this.bitField1_ |= Ints.MAX_POWER_OF_TWO;
                    this.joinMeetingUrlForInvite_ = str;
                    return this;
                }
                throw new NullPointerException();
            }

            public Builder clearJoinMeetingUrlForInvite() {
                this.bitField1_ &= -1073741825;
                this.joinMeetingUrlForInvite_ = MeetingInfoProto.getDefaultInstance().getJoinMeetingUrlForInvite();
                return this;
            }

            /* access modifiers changed from: 0000 */
            public void setJoinMeetingUrlForInvite(ByteString byteString) {
                this.bitField1_ |= Ints.MAX_POWER_OF_TWO;
                this.joinMeetingUrlForInvite_ = byteString;
            }

            public boolean hasPstnOnlyUseTelephone() {
                return (this.bitField1_ & Integer.MIN_VALUE) == Integer.MIN_VALUE;
            }

            public boolean getPstnOnlyUseTelephone() {
                return this.pstnOnlyUseTelephone_;
            }

            public Builder setPstnOnlyUseTelephone(boolean z) {
                this.bitField1_ |= Integer.MIN_VALUE;
                this.pstnOnlyUseTelephone_ = z;
                return this;
            }

            public Builder clearPstnOnlyUseTelephone() {
                this.bitField1_ &= Integer.MAX_VALUE;
                this.pstnOnlyUseTelephone_ = false;
                return this;
            }

            public boolean hasPstnUseOwnPhoneNumber() {
                return (this.bitField2_ & 1) == 1;
            }

            public boolean getPstnUseOwnPhoneNumber() {
                return this.pstnUseOwnPhoneNumber_;
            }

            public Builder setPstnUseOwnPhoneNumber(boolean z) {
                this.bitField2_ |= 1;
                this.pstnUseOwnPhoneNumber_ = z;
                return this;
            }

            public Builder clearPstnUseOwnPhoneNumber() {
                this.bitField2_ &= -2;
                this.pstnUseOwnPhoneNumber_ = false;
                return this;
            }

            public boolean hasPstnPhoneNumberNotMatchCallout() {
                return (this.bitField2_ & 2) == 2;
            }

            public boolean getPstnPhoneNumberNotMatchCallout() {
                return this.pstnPhoneNumberNotMatchCallout_;
            }

            public Builder setPstnPhoneNumberNotMatchCallout(boolean z) {
                this.bitField2_ |= 2;
                this.pstnPhoneNumberNotMatchCallout_ = z;
                return this;
            }

            public Builder clearPstnPhoneNumberNotMatchCallout() {
                this.bitField2_ &= -3;
                this.pstnPhoneNumberNotMatchCallout_ = false;
                return this;
            }

            public boolean hasPstnHideInviteByPhone() {
                return (this.bitField2_ & 4) == 4;
            }

            public boolean getPstnHideInviteByPhone() {
                return this.pstnHideInviteByPhone_;
            }

            public Builder setPstnHideInviteByPhone(boolean z) {
                this.bitField2_ |= 4;
                this.pstnHideInviteByPhone_ = z;
                return this;
            }

            public Builder clearPstnHideInviteByPhone() {
                this.bitField2_ &= -5;
                this.pstnHideInviteByPhone_ = false;
                return this;
            }
        }

        public enum MeetingType implements EnumLite {
            PRESCHEDULE(0, 0),
            INSTANT(1, 1),
            SCHEDULE(2, 2),
            REPEAT(3, 3);
            
            public static final int INSTANT_VALUE = 1;
            public static final int PRESCHEDULE_VALUE = 0;
            public static final int REPEAT_VALUE = 3;
            public static final int SCHEDULE_VALUE = 2;
            private static EnumLiteMap<MeetingType> internalValueMap;
            private final int value;

            static {
                internalValueMap = new EnumLiteMap<MeetingType>() {
                    public MeetingType findValueByNumber(int i) {
                        return MeetingType.valueOf(i);
                    }
                };
            }

            public final int getNumber() {
                return this.value;
            }

            public static MeetingType valueOf(int i) {
                switch (i) {
                    case 0:
                        return PRESCHEDULE;
                    case 1:
                        return INSTANT;
                    case 2:
                        return SCHEDULE;
                    case 3:
                        return REPEAT;
                    default:
                        return null;
                }
            }

            public static EnumLiteMap<MeetingType> internalGetValueMap() {
                return internalValueMap;
            }

            private MeetingType(int i, int i2) {
                this.value = i2;
            }
        }

        private MeetingInfoProto(Builder builder) {
            super(builder);
            this.memoizedIsInitialized = -1;
            this.memoizedSerializedSize = -1;
        }

        private MeetingInfoProto(boolean z) {
            this.memoizedIsInitialized = -1;
            this.memoizedSerializedSize = -1;
        }

        public static MeetingInfoProto getDefaultInstance() {
            return defaultInstance;
        }

        public MeetingInfoProto getDefaultInstanceForType() {
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

        public boolean hasMeetingNumber() {
            return (this.bitField0_ & 2) == 2;
        }

        public long getMeetingNumber() {
            return this.meetingNumber_;
        }

        public boolean hasTopic() {
            return (this.bitField0_ & 4) == 4;
        }

        public String getTopic() {
            Object obj = this.topic_;
            if (obj instanceof String) {
                return (String) obj;
            }
            ByteString byteString = (ByteString) obj;
            String stringUtf8 = byteString.toStringUtf8();
            if (Internal.isValidUtf8(byteString)) {
                this.topic_ = stringUtf8;
            }
            return stringUtf8;
        }

        private ByteString getTopicBytes() {
            Object obj = this.topic_;
            if (!(obj instanceof String)) {
                return (ByteString) obj;
            }
            ByteString copyFromUtf8 = ByteString.copyFromUtf8((String) obj);
            this.topic_ = copyFromUtf8;
            return copyFromUtf8;
        }

        public boolean hasPassword() {
            return (this.bitField0_ & 8) == 8;
        }

        public String getPassword() {
            Object obj = this.password_;
            if (obj instanceof String) {
                return (String) obj;
            }
            ByteString byteString = (ByteString) obj;
            String stringUtf8 = byteString.toStringUtf8();
            if (Internal.isValidUtf8(byteString)) {
                this.password_ = stringUtf8;
            }
            return stringUtf8;
        }

        private ByteString getPasswordBytes() {
            Object obj = this.password_;
            if (!(obj instanceof String)) {
                return (ByteString) obj;
            }
            ByteString copyFromUtf8 = ByteString.copyFromUtf8((String) obj);
            this.password_ = copyFromUtf8;
            return copyFromUtf8;
        }

        public boolean hasStartTime() {
            return (this.bitField0_ & 16) == 16;
        }

        public long getStartTime() {
            return this.startTime_;
        }

        public boolean hasDuration() {
            return (this.bitField0_ & 32) == 32;
        }

        public int getDuration() {
            return this.duration_;
        }

        public boolean hasType() {
            return (this.bitField0_ & 64) == 64;
        }

        public MeetingType getType() {
            return this.type_;
        }

        public boolean hasInviteEmailContent() {
            return (this.bitField0_ & 128) == 128;
        }

        public String getInviteEmailContent() {
            Object obj = this.inviteEmailContent_;
            if (obj instanceof String) {
                return (String) obj;
            }
            ByteString byteString = (ByteString) obj;
            String stringUtf8 = byteString.toStringUtf8();
            if (Internal.isValidUtf8(byteString)) {
                this.inviteEmailContent_ = stringUtf8;
            }
            return stringUtf8;
        }

        private ByteString getInviteEmailContentBytes() {
            Object obj = this.inviteEmailContent_;
            if (!(obj instanceof String)) {
                return (ByteString) obj;
            }
            ByteString copyFromUtf8 = ByteString.copyFromUtf8((String) obj);
            this.inviteEmailContent_ = copyFromUtf8;
            return copyFromUtf8;
        }

        public boolean hasMeetingStatus() {
            return (this.bitField0_ & 256) == 256;
        }

        public int getMeetingStatus() {
            return this.meetingStatus_;
        }

        public boolean hasCanJoinBeforeHost() {
            return (this.bitField0_ & 512) == 512;
        }

        public boolean getCanJoinBeforeHost() {
            return this.canJoinBeforeHost_;
        }

        public boolean hasRepeatType() {
            return (this.bitField0_ & 1024) == 1024;
        }

        public int getRepeatType() {
            return this.repeatType_;
        }

        public boolean hasRepeatEndTime() {
            return (this.bitField0_ & 2048) == 2048;
        }

        public long getRepeatEndTime() {
            return this.repeatEndTime_;
        }

        public boolean hasJoinMeetingUrl() {
            return (this.bitField0_ & 4096) == 4096;
        }

        public String getJoinMeetingUrl() {
            Object obj = this.joinMeetingUrl_;
            if (obj instanceof String) {
                return (String) obj;
            }
            ByteString byteString = (ByteString) obj;
            String stringUtf8 = byteString.toStringUtf8();
            if (Internal.isValidUtf8(byteString)) {
                this.joinMeetingUrl_ = stringUtf8;
            }
            return stringUtf8;
        }

        private ByteString getJoinMeetingUrlBytes() {
            Object obj = this.joinMeetingUrl_;
            if (!(obj instanceof String)) {
                return (ByteString) obj;
            }
            ByteString copyFromUtf8 = ByteString.copyFromUtf8((String) obj);
            this.joinMeetingUrl_ = copyFromUtf8;
            return copyFromUtf8;
        }

        public boolean hasMeetingHostName() {
            return (this.bitField0_ & 8192) == 8192;
        }

        public String getMeetingHostName() {
            Object obj = this.meetingHostName_;
            if (obj instanceof String) {
                return (String) obj;
            }
            ByteString byteString = (ByteString) obj;
            String stringUtf8 = byteString.toStringUtf8();
            if (Internal.isValidUtf8(byteString)) {
                this.meetingHostName_ = stringUtf8;
            }
            return stringUtf8;
        }

        private ByteString getMeetingHostNameBytes() {
            Object obj = this.meetingHostName_;
            if (!(obj instanceof String)) {
                return (ByteString) obj;
            }
            ByteString copyFromUtf8 = ByteString.copyFromUtf8((String) obj);
            this.meetingHostName_ = copyFromUtf8;
            return copyFromUtf8;
        }

        public boolean hasCallinNumber() {
            return (this.bitField0_ & 16384) == 16384;
        }

        public String getCallinNumber() {
            Object obj = this.callinNumber_;
            if (obj instanceof String) {
                return (String) obj;
            }
            ByteString byteString = (ByteString) obj;
            String stringUtf8 = byteString.toStringUtf8();
            if (Internal.isValidUtf8(byteString)) {
                this.callinNumber_ = stringUtf8;
            }
            return stringUtf8;
        }

        private ByteString getCallinNumberBytes() {
            Object obj = this.callinNumber_;
            if (!(obj instanceof String)) {
                return (ByteString) obj;
            }
            ByteString copyFromUtf8 = ByteString.copyFromUtf8((String) obj);
            this.callinNumber_ = copyFromUtf8;
            return copyFromUtf8;
        }

        public boolean hasPSTNEnabled() {
            return (this.bitField0_ & 32768) == 32768;
        }

        public boolean getPSTNEnabled() {
            return this.pSTNEnabled_;
        }

        public boolean hasH323Gateway() {
            return (this.bitField0_ & 65536) == 65536;
        }

        public String getH323Gateway() {
            Object obj = this.h323Gateway_;
            if (obj instanceof String) {
                return (String) obj;
            }
            ByteString byteString = (ByteString) obj;
            String stringUtf8 = byteString.toStringUtf8();
            if (Internal.isValidUtf8(byteString)) {
                this.h323Gateway_ = stringUtf8;
            }
            return stringUtf8;
        }

        private ByteString getH323GatewayBytes() {
            Object obj = this.h323Gateway_;
            if (!(obj instanceof String)) {
                return (ByteString) obj;
            }
            ByteString copyFromUtf8 = ByteString.copyFromUtf8((String) obj);
            this.h323Gateway_ = copyFromUtf8;
            return copyFromUtf8;
        }

        public boolean hasIsAudioOnlyMeeting() {
            return (this.bitField0_ & 131072) == 131072;
        }

        public boolean getIsAudioOnlyMeeting() {
            return this.isAudioOnlyMeeting_;
        }

        public boolean hasIsShareOnlyMeeting() {
            return (this.bitField0_ & 262144) == 262144;
        }

        public boolean getIsShareOnlyMeeting() {
            return this.isShareOnlyMeeting_;
        }

        public boolean hasIsWebinar() {
            return (this.bitField0_ & 524288) == 524288;
        }

        public boolean getIsWebinar() {
            return this.isWebinar_;
        }

        public boolean hasAssistantId() {
            return (this.bitField0_ & 1048576) == 1048576;
        }

        public String getAssistantId() {
            Object obj = this.assistantId_;
            if (obj instanceof String) {
                return (String) obj;
            }
            ByteString byteString = (ByteString) obj;
            String stringUtf8 = byteString.toStringUtf8();
            if (Internal.isValidUtf8(byteString)) {
                this.assistantId_ = stringUtf8;
            }
            return stringUtf8;
        }

        private ByteString getAssistantIdBytes() {
            Object obj = this.assistantId_;
            if (!(obj instanceof String)) {
                return (ByteString) obj;
            }
            ByteString copyFromUtf8 = ByteString.copyFromUtf8((String) obj);
            this.assistantId_ = copyFromUtf8;
            return copyFromUtf8;
        }

        public boolean hasExtendMeetingType() {
            return (this.bitField0_ & 2097152) == 2097152;
        }

        public int getExtendMeetingType() {
            return this.extendMeetingType_;
        }

        public boolean hasMeetingHostID() {
            return (this.bitField0_ & 4194304) == 4194304;
        }

        public String getMeetingHostID() {
            Object obj = this.meetingHostID_;
            if (obj instanceof String) {
                return (String) obj;
            }
            ByteString byteString = (ByteString) obj;
            String stringUtf8 = byteString.toStringUtf8();
            if (Internal.isValidUtf8(byteString)) {
                this.meetingHostID_ = stringUtf8;
            }
            return stringUtf8;
        }

        private ByteString getMeetingHostIDBytes() {
            Object obj = this.meetingHostID_;
            if (!(obj instanceof String)) {
                return (ByteString) obj;
            }
            ByteString copyFromUtf8 = ByteString.copyFromUtf8((String) obj);
            this.meetingHostID_ = copyFromUtf8;
            return copyFromUtf8;
        }

        public boolean hasInviteEmailSubject() {
            return (this.bitField0_ & 8388608) == 8388608;
        }

        public String getInviteEmailSubject() {
            Object obj = this.inviteEmailSubject_;
            if (obj instanceof String) {
                return (String) obj;
            }
            ByteString byteString = (ByteString) obj;
            String stringUtf8 = byteString.toStringUtf8();
            if (Internal.isValidUtf8(byteString)) {
                this.inviteEmailSubject_ = stringUtf8;
            }
            return stringUtf8;
        }

        private ByteString getInviteEmailSubjectBytes() {
            Object obj = this.inviteEmailSubject_;
            if (!(obj instanceof String)) {
                return (ByteString) obj;
            }
            ByteString copyFromUtf8 = ByteString.copyFromUtf8((String) obj);
            this.inviteEmailSubject_ = copyFromUtf8;
            return copyFromUtf8;
        }

        public boolean hasPSTNNeedConfirm1() {
            return (this.bitField0_ & 16777216) == 16777216;
        }

        public boolean getPSTNNeedConfirm1() {
            return this.pSTNNeedConfirm1_;
        }

        public boolean hasInviteEmailContentWithTime() {
            return (this.bitField0_ & MediaHttpDownloader.MAXIMUM_CHUNK_SIZE) == 33554432;
        }

        public String getInviteEmailContentWithTime() {
            Object obj = this.inviteEmailContentWithTime_;
            if (obj instanceof String) {
                return (String) obj;
            }
            ByteString byteString = (ByteString) obj;
            String stringUtf8 = byteString.toStringUtf8();
            if (Internal.isValidUtf8(byteString)) {
                this.inviteEmailContentWithTime_ = stringUtf8;
            }
            return stringUtf8;
        }

        private ByteString getInviteEmailContentWithTimeBytes() {
            Object obj = this.inviteEmailContentWithTime_;
            if (!(obj instanceof String)) {
                return (ByteString) obj;
            }
            ByteString copyFromUtf8 = ByteString.copyFromUtf8((String) obj);
            this.inviteEmailContentWithTime_ = copyFromUtf8;
            return copyFromUtf8;
        }

        public boolean hasWebinarRegUrl() {
            return (this.bitField0_ & 67108864) == 67108864;
        }

        public String getWebinarRegUrl() {
            Object obj = this.webinarRegUrl_;
            if (obj instanceof String) {
                return (String) obj;
            }
            ByteString byteString = (ByteString) obj;
            String stringUtf8 = byteString.toStringUtf8();
            if (Internal.isValidUtf8(byteString)) {
                this.webinarRegUrl_ = stringUtf8;
            }
            return stringUtf8;
        }

        private ByteString getWebinarRegUrlBytes() {
            Object obj = this.webinarRegUrl_;
            if (!(obj instanceof String)) {
                return (ByteString) obj;
            }
            ByteString copyFromUtf8 = ByteString.copyFromUtf8((String) obj);
            this.webinarRegUrl_ = copyFromUtf8;
            return copyFromUtf8;
        }

        public boolean hasHostVideoOff() {
            return (this.bitField0_ & 134217728) == 134217728;
        }

        public boolean getHostVideoOff() {
            return this.hostVideoOff_;
        }

        public boolean hasAttendeeVideoOff() {
            return (this.bitField0_ & 268435456) == 268435456;
        }

        public boolean getAttendeeVideoOff() {
            return this.attendeeVideoOff_;
        }

        public boolean hasVoipOff() {
            return (this.bitField0_ & 536870912) == 536870912;
        }

        public boolean getVoipOff() {
            return this.voipOff_;
        }

        public boolean hasTelephonyOff() {
            return (this.bitField0_ & Ints.MAX_POWER_OF_TWO) == 1073741824;
        }

        public boolean getTelephonyOff() {
            return this.telephonyOff_;
        }

        public boolean hasSupportCallOutType() {
            return (this.bitField0_ & Integer.MIN_VALUE) == Integer.MIN_VALUE;
        }

        public int getSupportCallOutType() {
            return this.supportCallOutType_;
        }

        public boolean hasIsH323Enabled() {
            return (this.bitField1_ & 1) == 1;
        }

        public boolean getIsH323Enabled() {
            return this.isH323Enabled_;
        }

        public List<CountryCode> getCalloutCountryCodesList() {
            return this.calloutCountryCodes_;
        }

        public List<? extends CountryCodeOrBuilder> getCalloutCountryCodesOrBuilderList() {
            return this.calloutCountryCodes_;
        }

        public int getCalloutCountryCodesCount() {
            return this.calloutCountryCodes_.size();
        }

        public CountryCode getCalloutCountryCodes(int i) {
            return (CountryCode) this.calloutCountryCodes_.get(i);
        }

        public CountryCodeOrBuilder getCalloutCountryCodesOrBuilder(int i) {
            return (CountryCodeOrBuilder) this.calloutCountryCodes_.get(i);
        }

        public List<CountryCode> getCallinCountryCodesList() {
            return this.callinCountryCodes_;
        }

        public List<? extends CountryCodeOrBuilder> getCallinCountryCodesOrBuilderList() {
            return this.callinCountryCodes_;
        }

        public int getCallinCountryCodesCount() {
            return this.callinCountryCodes_.size();
        }

        public CountryCode getCallinCountryCodes(int i) {
            return (CountryCode) this.callinCountryCodes_.get(i);
        }

        public CountryCodeOrBuilder getCallinCountryCodesOrBuilder(int i) {
            return (CountryCodeOrBuilder) this.callinCountryCodes_.get(i);
        }

        public boolean hasOtherTeleConfInfo() {
            return (this.bitField1_ & 2) == 2;
        }

        public String getOtherTeleConfInfo() {
            Object obj = this.otherTeleConfInfo_;
            if (obj instanceof String) {
                return (String) obj;
            }
            ByteString byteString = (ByteString) obj;
            String stringUtf8 = byteString.toStringUtf8();
            if (Internal.isValidUtf8(byteString)) {
                this.otherTeleConfInfo_ = stringUtf8;
            }
            return stringUtf8;
        }

        private ByteString getOtherTeleConfInfoBytes() {
            Object obj = this.otherTeleConfInfo_;
            if (!(obj instanceof String)) {
                return (ByteString) obj;
            }
            ByteString copyFromUtf8 = ByteString.copyFromUtf8((String) obj);
            this.otherTeleConfInfo_ = copyFromUtf8;
            return copyFromUtf8;
        }

        public boolean hasIsSelfTelephonyOn() {
            return (this.bitField1_ & 4) == 4;
        }

        public boolean getIsSelfTelephonyOn() {
            return this.isSelfTelephonyOn_;
        }

        public boolean hasUsePmiAsMeetingID() {
            return (this.bitField1_ & 8) == 8;
        }

        public boolean getUsePmiAsMeetingID() {
            return this.usePmiAsMeetingID_;
        }

        public boolean hasOriginalMeetingNumber() {
            return (this.bitField1_ & 16) == 16;
        }

        public long getOriginalMeetingNumber() {
            return this.originalMeetingNumber_;
        }

        public boolean hasIsCnMeeting() {
            return (this.bitField1_ & 32) == 32;
        }

        public boolean getIsCnMeeting() {
            return this.isCnMeeting_;
        }

        public boolean hasTimeZoneId() {
            return (this.bitField1_ & 64) == 64;
        }

        public String getTimeZoneId() {
            Object obj = this.timeZoneId_;
            if (obj instanceof String) {
                return (String) obj;
            }
            ByteString byteString = (ByteString) obj;
            String stringUtf8 = byteString.toStringUtf8();
            if (Internal.isValidUtf8(byteString)) {
                this.timeZoneId_ = stringUtf8;
            }
            return stringUtf8;
        }

        private ByteString getTimeZoneIdBytes() {
            Object obj = this.timeZoneId_;
            if (!(obj instanceof String)) {
                return (ByteString) obj;
            }
            ByteString copyFromUtf8 = ByteString.copyFromUtf8((String) obj);
            this.timeZoneId_ = copyFromUtf8;
            return copyFromUtf8;
        }

        public boolean hasDefaultcallInCountry() {
            return (this.bitField1_ & 128) == 128;
        }

        public String getDefaultcallInCountry() {
            Object obj = this.defaultcallInCountry_;
            if (obj instanceof String) {
                return (String) obj;
            }
            ByteString byteString = (ByteString) obj;
            String stringUtf8 = byteString.toStringUtf8();
            if (Internal.isValidUtf8(byteString)) {
                this.defaultcallInCountry_ = stringUtf8;
            }
            return stringUtf8;
        }

        private ByteString getDefaultcallInCountryBytes() {
            Object obj = this.defaultcallInCountry_;
            if (!(obj instanceof String)) {
                return (ByteString) obj;
            }
            ByteString copyFromUtf8 = ByteString.copyFromUtf8((String) obj);
            this.defaultcallInCountry_ = copyFromUtf8;
            return copyFromUtf8;
        }

        public boolean hasIsTurnOnExternalAuth() {
            return (this.bitField1_ & 256) == 256;
        }

        public boolean getIsTurnOnExternalAuth() {
            return this.isTurnOnExternalAuth_;
        }

        public boolean hasIsOnlySignJoin() {
            return (this.bitField1_ & 512) == 512;
        }

        public boolean getIsOnlySignJoin() {
            return this.isOnlySignJoin_;
        }

        public boolean hasAuthProto() {
            return (this.bitField1_ & 1024) == 1024;
        }

        public AuthProto getAuthProto() {
            return this.authProto_;
        }

        public boolean hasProgressingMeetingCount() {
            return (this.bitField1_ & 2048) == 2048;
        }

        public int getProgressingMeetingCount() {
            return this.progressingMeetingCount_;
        }

        public boolean hasMeetingWaitStatus() {
            return (this.bitField1_ & 4096) == 4096;
        }

        public int getMeetingWaitStatus() {
            return this.meetingWaitStatus_;
        }

        public List<TSPCallInInfo> getTspCallinInfoList() {
            return this.tspCallinInfo_;
        }

        public List<? extends TSPCallInInfoOrBuilder> getTspCallinInfoOrBuilderList() {
            return this.tspCallinInfo_;
        }

        public int getTspCallinInfoCount() {
            return this.tspCallinInfo_.size();
        }

        public TSPCallInInfo getTspCallinInfo(int i) {
            return (TSPCallInInfo) this.tspCallinInfo_.get(i);
        }

        public TSPCallInInfoOrBuilder getTspCallinInfoOrBuilder(int i) {
            return (TSPCallInInfoOrBuilder) this.tspCallinInfo_.get(i);
        }

        public List<AlterHost> getAlterHostList() {
            return this.alterHost_;
        }

        public List<? extends AlterHostOrBuilder> getAlterHostOrBuilderList() {
            return this.alterHost_;
        }

        public int getAlterHostCount() {
            return this.alterHost_.size();
        }

        public AlterHost getAlterHost(int i) {
            return (AlterHost) this.alterHost_.get(i);
        }

        public AlterHostOrBuilder getAlterHostOrBuilder(int i) {
            return (AlterHostOrBuilder) this.alterHost_.get(i);
        }

        public boolean hasAvailableDialinCountry() {
            return (this.bitField1_ & 8192) == 8192;
        }

        public AvailableDialinCountry getAvailableDialinCountry() {
            return this.availableDialinCountry_;
        }

        public boolean hasGoogleCalendarUrl() {
            return (this.bitField1_ & 16384) == 16384;
        }

        public String getGoogleCalendarUrl() {
            Object obj = this.googleCalendarUrl_;
            if (obj instanceof String) {
                return (String) obj;
            }
            ByteString byteString = (ByteString) obj;
            String stringUtf8 = byteString.toStringUtf8();
            if (Internal.isValidUtf8(byteString)) {
                this.googleCalendarUrl_ = stringUtf8;
            }
            return stringUtf8;
        }

        private ByteString getGoogleCalendarUrlBytes() {
            Object obj = this.googleCalendarUrl_;
            if (!(obj instanceof String)) {
                return (ByteString) obj;
            }
            ByteString copyFromUtf8 = ByteString.copyFromUtf8((String) obj);
            this.googleCalendarUrl_ = copyFromUtf8;
            return copyFromUtf8;
        }

        public boolean hasIsEnableMeetingToPublic() {
            return (this.bitField1_ & 32768) == 32768;
        }

        public boolean getIsEnableMeetingToPublic() {
            return this.isEnableMeetingToPublic_;
        }

        public boolean hasIsEnableAutoRecordingLocal() {
            return (this.bitField1_ & 65536) == 65536;
        }

        public boolean getIsEnableAutoRecordingLocal() {
            return this.isEnableAutoRecordingLocal_;
        }

        public boolean hasIsEnableAutoRecordingCloud() {
            return (this.bitField1_ & 131072) == 131072;
        }

        public boolean getIsEnableAutoRecordingCloud() {
            return this.isEnableAutoRecordingCloud_;
        }

        public boolean hasIsEnableAutoRecordingMtgLevelFirst() {
            return (this.bitField1_ & 262144) == 262144;
        }

        public boolean getIsEnableAutoRecordingMtgLevelFirst() {
            return this.isEnableAutoRecordingMtgLevelFirst_;
        }

        public boolean hasIsEnableAudioWatermark() {
            return (this.bitField1_ & 524288) == 524288;
        }

        public boolean getIsEnableAudioWatermark() {
            return this.isEnableAudioWatermark_;
        }

        public boolean hasIsWebRecurrenceMeeting() {
            return (this.bitField1_ & 1048576) == 1048576;
        }

        public boolean getIsWebRecurrenceMeeting() {
            return this.isWebRecurrenceMeeting_;
        }

        public boolean hasDailinString() {
            return (this.bitField1_ & 2097152) == 2097152;
        }

        public String getDailinString() {
            Object obj = this.dailinString_;
            if (obj instanceof String) {
                return (String) obj;
            }
            ByteString byteString = (ByteString) obj;
            String stringUtf8 = byteString.toStringUtf8();
            if (Internal.isValidUtf8(byteString)) {
                this.dailinString_ = stringUtf8;
            }
            return stringUtf8;
        }

        private ByteString getDailinStringBytes() {
            Object obj = this.dailinString_;
            if (!(obj instanceof String)) {
                return (ByteString) obj;
            }
            ByteString copyFromUtf8 = ByteString.copyFromUtf8((String) obj);
            this.dailinString_ = copyFromUtf8;
            return copyFromUtf8;
        }

        public boolean hasIsEnableLanguageInterpretation() {
            return (this.bitField1_ & 4194304) == 4194304;
        }

        public boolean getIsEnableLanguageInterpretation() {
            return this.isEnableLanguageInterpretation_;
        }

        public boolean hasIsEnableWaitingRoom() {
            return (this.bitField1_ & 8388608) == 8388608;
        }

        public boolean getIsEnableWaitingRoom() {
            return this.isEnableWaitingRoom_;
        }

        public boolean hasIsSupportWaitingRoom() {
            return (this.bitField1_ & 16777216) == 16777216;
        }

        public boolean getIsSupportWaitingRoom() {
            return this.isSupportWaitingRoom_;
        }

        public boolean hasJbhPriorTime() {
            return (this.bitField1_ & MediaHttpDownloader.MAXIMUM_CHUNK_SIZE) == 33554432;
        }

        public int getJbhPriorTime() {
            return this.jbhPriorTime_;
        }

        public boolean hasJoinMeetingUrlForInvite() {
            return (this.bitField1_ & 67108864) == 67108864;
        }

        public String getJoinMeetingUrlForInvite() {
            Object obj = this.joinMeetingUrlForInvite_;
            if (obj instanceof String) {
                return (String) obj;
            }
            ByteString byteString = (ByteString) obj;
            String stringUtf8 = byteString.toStringUtf8();
            if (Internal.isValidUtf8(byteString)) {
                this.joinMeetingUrlForInvite_ = stringUtf8;
            }
            return stringUtf8;
        }

        private ByteString getJoinMeetingUrlForInviteBytes() {
            Object obj = this.joinMeetingUrlForInvite_;
            if (!(obj instanceof String)) {
                return (ByteString) obj;
            }
            ByteString copyFromUtf8 = ByteString.copyFromUtf8((String) obj);
            this.joinMeetingUrlForInvite_ = copyFromUtf8;
            return copyFromUtf8;
        }

        public boolean hasPstnOnlyUseTelephone() {
            return (this.bitField1_ & 134217728) == 134217728;
        }

        public boolean getPstnOnlyUseTelephone() {
            return this.pstnOnlyUseTelephone_;
        }

        public boolean hasPstnUseOwnPhoneNumber() {
            return (this.bitField1_ & 268435456) == 268435456;
        }

        public boolean getPstnUseOwnPhoneNumber() {
            return this.pstnUseOwnPhoneNumber_;
        }

        public boolean hasPstnPhoneNumberNotMatchCallout() {
            return (this.bitField1_ & 536870912) == 536870912;
        }

        public boolean getPstnPhoneNumberNotMatchCallout() {
            return this.pstnPhoneNumberNotMatchCallout_;
        }

        public boolean hasPstnHideInviteByPhone() {
            return (this.bitField1_ & Ints.MAX_POWER_OF_TWO) == 1073741824;
        }

        public boolean getPstnHideInviteByPhone() {
            return this.pstnHideInviteByPhone_;
        }

        private void initFields() {
            this.id_ = "";
            this.meetingNumber_ = 0;
            this.topic_ = "";
            this.password_ = "";
            this.startTime_ = 0;
            this.duration_ = 0;
            this.type_ = MeetingType.PRESCHEDULE;
            this.inviteEmailContent_ = "";
            this.meetingStatus_ = 0;
            this.canJoinBeforeHost_ = false;
            this.repeatType_ = 0;
            this.repeatEndTime_ = 0;
            this.joinMeetingUrl_ = "";
            this.meetingHostName_ = "";
            this.callinNumber_ = "";
            this.pSTNEnabled_ = false;
            this.h323Gateway_ = "";
            this.isAudioOnlyMeeting_ = false;
            this.isShareOnlyMeeting_ = false;
            this.isWebinar_ = false;
            this.assistantId_ = "";
            this.extendMeetingType_ = 0;
            this.meetingHostID_ = "";
            this.inviteEmailSubject_ = "";
            this.pSTNNeedConfirm1_ = false;
            this.inviteEmailContentWithTime_ = "";
            this.webinarRegUrl_ = "";
            this.hostVideoOff_ = false;
            this.attendeeVideoOff_ = false;
            this.voipOff_ = false;
            this.telephonyOff_ = false;
            this.supportCallOutType_ = 0;
            this.isH323Enabled_ = false;
            this.calloutCountryCodes_ = Collections.emptyList();
            this.callinCountryCodes_ = Collections.emptyList();
            this.otherTeleConfInfo_ = "";
            this.isSelfTelephonyOn_ = false;
            this.usePmiAsMeetingID_ = false;
            this.originalMeetingNumber_ = 0;
            this.isCnMeeting_ = false;
            this.timeZoneId_ = "";
            this.defaultcallInCountry_ = "";
            this.isTurnOnExternalAuth_ = false;
            this.isOnlySignJoin_ = false;
            this.authProto_ = AuthProto.getDefaultInstance();
            this.progressingMeetingCount_ = 0;
            this.meetingWaitStatus_ = 0;
            this.tspCallinInfo_ = Collections.emptyList();
            this.alterHost_ = Collections.emptyList();
            this.availableDialinCountry_ = AvailableDialinCountry.getDefaultInstance();
            this.googleCalendarUrl_ = "";
            this.isEnableMeetingToPublic_ = false;
            this.isEnableAutoRecordingLocal_ = false;
            this.isEnableAutoRecordingCloud_ = false;
            this.isEnableAutoRecordingMtgLevelFirst_ = false;
            this.isEnableAudioWatermark_ = false;
            this.isWebRecurrenceMeeting_ = false;
            this.dailinString_ = "";
            this.isEnableLanguageInterpretation_ = false;
            this.isEnableWaitingRoom_ = false;
            this.isSupportWaitingRoom_ = false;
            this.jbhPriorTime_ = 0;
            this.joinMeetingUrlForInvite_ = "";
            this.pstnOnlyUseTelephone_ = false;
            this.pstnUseOwnPhoneNumber_ = false;
            this.pstnPhoneNumberNotMatchCallout_ = false;
            this.pstnHideInviteByPhone_ = false;
        }

        public final boolean isInitialized() {
            byte b = this.memoizedIsInitialized;
            boolean z = false;
            if (b != -1) {
                if (b == 1) {
                    z = true;
                }
                return z;
            } else if (!hasAuthProto() || getAuthProto().isInitialized()) {
                this.memoizedIsInitialized = 1;
                return true;
            } else {
                this.memoizedIsInitialized = 0;
                return false;
            }
        }

        public void writeTo(CodedOutputStream codedOutputStream) throws IOException {
            CodedOutputStream codedOutputStream2 = codedOutputStream;
            getSerializedSize();
            if ((this.bitField0_ & 1) == 1) {
                codedOutputStream2.writeBytes(1, getIdBytes());
            }
            if ((this.bitField0_ & 2) == 2) {
                codedOutputStream2.writeInt64(2, this.meetingNumber_);
            }
            if ((this.bitField0_ & 4) == 4) {
                codedOutputStream2.writeBytes(3, getTopicBytes());
            }
            if ((this.bitField0_ & 8) == 8) {
                codedOutputStream2.writeBytes(4, getPasswordBytes());
            }
            if ((this.bitField0_ & 16) == 16) {
                codedOutputStream2.writeInt64(5, this.startTime_);
            }
            if ((this.bitField0_ & 32) == 32) {
                codedOutputStream2.writeInt32(6, this.duration_);
            }
            if ((this.bitField0_ & 64) == 64) {
                codedOutputStream2.writeEnum(7, this.type_.getNumber());
            }
            if ((this.bitField0_ & 128) == 128) {
                codedOutputStream2.writeBytes(8, getInviteEmailContentBytes());
            }
            if ((this.bitField0_ & 256) == 256) {
                codedOutputStream2.writeInt32(9, this.meetingStatus_);
            }
            if ((this.bitField0_ & 512) == 512) {
                codedOutputStream2.writeBool(10, this.canJoinBeforeHost_);
            }
            if ((this.bitField0_ & 1024) == 1024) {
                codedOutputStream2.writeInt32(11, this.repeatType_);
            }
            if ((this.bitField0_ & 2048) == 2048) {
                codedOutputStream2.writeInt64(12, this.repeatEndTime_);
            }
            if ((this.bitField0_ & 4096) == 4096) {
                codedOutputStream2.writeBytes(13, getJoinMeetingUrlBytes());
            }
            if ((this.bitField0_ & 8192) == 8192) {
                codedOutputStream2.writeBytes(14, getMeetingHostNameBytes());
            }
            if ((this.bitField0_ & 16384) == 16384) {
                codedOutputStream2.writeBytes(15, getCallinNumberBytes());
            }
            if ((this.bitField0_ & 32768) == 32768) {
                codedOutputStream2.writeBool(16, this.pSTNEnabled_);
            }
            if ((this.bitField0_ & 65536) == 65536) {
                codedOutputStream2.writeBytes(17, getH323GatewayBytes());
            }
            if ((this.bitField0_ & 131072) == 131072) {
                codedOutputStream2.writeBool(18, this.isAudioOnlyMeeting_);
            }
            if ((this.bitField0_ & 262144) == 262144) {
                codedOutputStream2.writeBool(19, this.isShareOnlyMeeting_);
            }
            if ((this.bitField0_ & 524288) == 524288) {
                codedOutputStream2.writeBool(20, this.isWebinar_);
            }
            if ((this.bitField0_ & 1048576) == 1048576) {
                codedOutputStream2.writeBytes(21, getAssistantIdBytes());
            }
            if ((this.bitField0_ & 2097152) == 2097152) {
                codedOutputStream2.writeInt32(22, this.extendMeetingType_);
            }
            if ((this.bitField0_ & 4194304) == 4194304) {
                codedOutputStream2.writeBytes(23, getMeetingHostIDBytes());
            }
            if ((this.bitField0_ & 8388608) == 8388608) {
                codedOutputStream2.writeBytes(25, getInviteEmailSubjectBytes());
            }
            if ((this.bitField0_ & 16777216) == 16777216) {
                codedOutputStream2.writeBool(26, this.pSTNNeedConfirm1_);
            }
            if ((this.bitField0_ & MediaHttpDownloader.MAXIMUM_CHUNK_SIZE) == 33554432) {
                codedOutputStream2.writeBytes(27, getInviteEmailContentWithTimeBytes());
            }
            if ((this.bitField0_ & 67108864) == 67108864) {
                codedOutputStream2.writeBytes(28, getWebinarRegUrlBytes());
            }
            if ((this.bitField0_ & 134217728) == 134217728) {
                codedOutputStream2.writeBool(29, this.hostVideoOff_);
            }
            if ((this.bitField0_ & 268435456) == 268435456) {
                codedOutputStream2.writeBool(30, this.attendeeVideoOff_);
            }
            if ((this.bitField0_ & 536870912) == 536870912) {
                codedOutputStream2.writeBool(31, this.voipOff_);
            }
            if ((this.bitField0_ & Ints.MAX_POWER_OF_TWO) == 1073741824) {
                codedOutputStream2.writeBool(32, this.telephonyOff_);
            }
            if ((this.bitField0_ & Integer.MIN_VALUE) == Integer.MIN_VALUE) {
                codedOutputStream2.writeInt32(33, this.supportCallOutType_);
            }
            if ((this.bitField1_ & 1) == 1) {
                codedOutputStream2.writeBool(34, this.isH323Enabled_);
            }
            for (int i = 0; i < this.calloutCountryCodes_.size(); i++) {
                codedOutputStream2.writeMessage(35, (MessageLite) this.calloutCountryCodes_.get(i));
            }
            for (int i2 = 0; i2 < this.callinCountryCodes_.size(); i2++) {
                codedOutputStream2.writeMessage(36, (MessageLite) this.callinCountryCodes_.get(i2));
            }
            if ((this.bitField1_ & 2) == 2) {
                codedOutputStream2.writeBytes(37, getOtherTeleConfInfoBytes());
            }
            if ((this.bitField1_ & 4) == 4) {
                codedOutputStream2.writeBool(38, this.isSelfTelephonyOn_);
            }
            if ((this.bitField1_ & 8) == 8) {
                codedOutputStream2.writeBool(39, this.usePmiAsMeetingID_);
            }
            if ((this.bitField1_ & 16) == 16) {
                codedOutputStream2.writeInt64(40, this.originalMeetingNumber_);
            }
            if ((this.bitField1_ & 32) == 32) {
                codedOutputStream2.writeBool(41, this.isCnMeeting_);
            }
            if ((this.bitField1_ & 64) == 64) {
                codedOutputStream2.writeBytes(42, getTimeZoneIdBytes());
            }
            if ((this.bitField1_ & 128) == 128) {
                codedOutputStream2.writeBytes(43, getDefaultcallInCountryBytes());
            }
            if ((this.bitField1_ & 256) == 256) {
                codedOutputStream2.writeBool(44, this.isTurnOnExternalAuth_);
            }
            if ((this.bitField1_ & 512) == 512) {
                codedOutputStream2.writeBool(45, this.isOnlySignJoin_);
            }
            if ((this.bitField1_ & 1024) == 1024) {
                codedOutputStream2.writeMessage(46, this.authProto_);
            }
            if ((this.bitField1_ & 2048) == 2048) {
                codedOutputStream2.writeInt32(47, this.progressingMeetingCount_);
            }
            if ((this.bitField1_ & 4096) == 4096) {
                codedOutputStream2.writeInt32(48, this.meetingWaitStatus_);
            }
            for (int i3 = 0; i3 < this.tspCallinInfo_.size(); i3++) {
                codedOutputStream2.writeMessage(49, (MessageLite) this.tspCallinInfo_.get(i3));
            }
            for (int i4 = 0; i4 < this.alterHost_.size(); i4++) {
                codedOutputStream2.writeMessage(50, (MessageLite) this.alterHost_.get(i4));
            }
            if ((this.bitField1_ & 8192) == 8192) {
                codedOutputStream2.writeMessage(51, this.availableDialinCountry_);
            }
            if ((this.bitField1_ & 16384) == 16384) {
                codedOutputStream2.writeBytes(52, getGoogleCalendarUrlBytes());
            }
            if ((this.bitField1_ & 32768) == 32768) {
                codedOutputStream2.writeBool(53, this.isEnableMeetingToPublic_);
            }
            if ((this.bitField1_ & 65536) == 65536) {
                codedOutputStream2.writeBool(54, this.isEnableAutoRecordingLocal_);
            }
            if ((this.bitField1_ & 131072) == 131072) {
                codedOutputStream2.writeBool(55, this.isEnableAutoRecordingCloud_);
            }
            if ((this.bitField1_ & 262144) == 262144) {
                codedOutputStream2.writeBool(56, this.isEnableAutoRecordingMtgLevelFirst_);
            }
            if ((this.bitField1_ & 524288) == 524288) {
                codedOutputStream2.writeBool(57, this.isEnableAudioWatermark_);
            }
            if ((this.bitField1_ & 1048576) == 1048576) {
                codedOutputStream2.writeBool(58, this.isWebRecurrenceMeeting_);
            }
            if ((this.bitField1_ & 2097152) == 2097152) {
                codedOutputStream2.writeBytes(59, getDailinStringBytes());
            }
            if ((this.bitField1_ & 4194304) == 4194304) {
                codedOutputStream2.writeBool(60, this.isEnableLanguageInterpretation_);
            }
            if ((this.bitField1_ & 8388608) == 8388608) {
                codedOutputStream2.writeBool(61, this.isEnableWaitingRoom_);
            }
            if ((this.bitField1_ & 16777216) == 16777216) {
                codedOutputStream2.writeBool(62, this.isSupportWaitingRoom_);
            }
            if ((this.bitField1_ & MediaHttpDownloader.MAXIMUM_CHUNK_SIZE) == 33554432) {
                codedOutputStream2.writeInt32(63, this.jbhPriorTime_);
            }
            if ((this.bitField1_ & 67108864) == 67108864) {
                codedOutputStream2.writeBytes(64, getJoinMeetingUrlForInviteBytes());
            }
            if ((this.bitField1_ & 134217728) == 134217728) {
                codedOutputStream2.writeBool(65, this.pstnOnlyUseTelephone_);
            }
            if ((this.bitField1_ & 268435456) == 268435456) {
                codedOutputStream2.writeBool(66, this.pstnUseOwnPhoneNumber_);
            }
            if ((this.bitField1_ & 536870912) == 536870912) {
                codedOutputStream2.writeBool(67, this.pstnPhoneNumberNotMatchCallout_);
            }
            if ((this.bitField1_ & Ints.MAX_POWER_OF_TWO) == 1073741824) {
                codedOutputStream2.writeBool(68, this.pstnHideInviteByPhone_);
            }
        }

        public int getSerializedSize() {
            int i = this.memoizedSerializedSize;
            if (i != -1) {
                return i;
            }
            int computeBytesSize = (this.bitField0_ & 1) == 1 ? CodedOutputStream.computeBytesSize(1, getIdBytes()) + 0 : 0;
            if ((this.bitField0_ & 2) == 2) {
                computeBytesSize += CodedOutputStream.computeInt64Size(2, this.meetingNumber_);
            }
            if ((this.bitField0_ & 4) == 4) {
                computeBytesSize += CodedOutputStream.computeBytesSize(3, getTopicBytes());
            }
            if ((this.bitField0_ & 8) == 8) {
                computeBytesSize += CodedOutputStream.computeBytesSize(4, getPasswordBytes());
            }
            if ((this.bitField0_ & 16) == 16) {
                computeBytesSize += CodedOutputStream.computeInt64Size(5, this.startTime_);
            }
            if ((this.bitField0_ & 32) == 32) {
                computeBytesSize += CodedOutputStream.computeInt32Size(6, this.duration_);
            }
            if ((this.bitField0_ & 64) == 64) {
                computeBytesSize += CodedOutputStream.computeEnumSize(7, this.type_.getNumber());
            }
            if ((this.bitField0_ & 128) == 128) {
                computeBytesSize += CodedOutputStream.computeBytesSize(8, getInviteEmailContentBytes());
            }
            if ((this.bitField0_ & 256) == 256) {
                computeBytesSize += CodedOutputStream.computeInt32Size(9, this.meetingStatus_);
            }
            if ((this.bitField0_ & 512) == 512) {
                computeBytesSize += CodedOutputStream.computeBoolSize(10, this.canJoinBeforeHost_);
            }
            if ((this.bitField0_ & 1024) == 1024) {
                computeBytesSize += CodedOutputStream.computeInt32Size(11, this.repeatType_);
            }
            if ((this.bitField0_ & 2048) == 2048) {
                computeBytesSize += CodedOutputStream.computeInt64Size(12, this.repeatEndTime_);
            }
            if ((this.bitField0_ & 4096) == 4096) {
                computeBytesSize += CodedOutputStream.computeBytesSize(13, getJoinMeetingUrlBytes());
            }
            if ((this.bitField0_ & 8192) == 8192) {
                computeBytesSize += CodedOutputStream.computeBytesSize(14, getMeetingHostNameBytes());
            }
            if ((this.bitField0_ & 16384) == 16384) {
                computeBytesSize += CodedOutputStream.computeBytesSize(15, getCallinNumberBytes());
            }
            if ((this.bitField0_ & 32768) == 32768) {
                computeBytesSize += CodedOutputStream.computeBoolSize(16, this.pSTNEnabled_);
            }
            if ((this.bitField0_ & 65536) == 65536) {
                computeBytesSize += CodedOutputStream.computeBytesSize(17, getH323GatewayBytes());
            }
            if ((this.bitField0_ & 131072) == 131072) {
                computeBytesSize += CodedOutputStream.computeBoolSize(18, this.isAudioOnlyMeeting_);
            }
            if ((this.bitField0_ & 262144) == 262144) {
                computeBytesSize += CodedOutputStream.computeBoolSize(19, this.isShareOnlyMeeting_);
            }
            if ((this.bitField0_ & 524288) == 524288) {
                computeBytesSize += CodedOutputStream.computeBoolSize(20, this.isWebinar_);
            }
            if ((this.bitField0_ & 1048576) == 1048576) {
                computeBytesSize += CodedOutputStream.computeBytesSize(21, getAssistantIdBytes());
            }
            if ((this.bitField0_ & 2097152) == 2097152) {
                computeBytesSize += CodedOutputStream.computeInt32Size(22, this.extendMeetingType_);
            }
            if ((this.bitField0_ & 4194304) == 4194304) {
                computeBytesSize += CodedOutputStream.computeBytesSize(23, getMeetingHostIDBytes());
            }
            if ((this.bitField0_ & 8388608) == 8388608) {
                computeBytesSize += CodedOutputStream.computeBytesSize(25, getInviteEmailSubjectBytes());
            }
            if ((this.bitField0_ & 16777216) == 16777216) {
                computeBytesSize += CodedOutputStream.computeBoolSize(26, this.pSTNNeedConfirm1_);
            }
            if ((this.bitField0_ & MediaHttpDownloader.MAXIMUM_CHUNK_SIZE) == 33554432) {
                computeBytesSize += CodedOutputStream.computeBytesSize(27, getInviteEmailContentWithTimeBytes());
            }
            if ((this.bitField0_ & 67108864) == 67108864) {
                computeBytesSize += CodedOutputStream.computeBytesSize(28, getWebinarRegUrlBytes());
            }
            if ((this.bitField0_ & 134217728) == 134217728) {
                computeBytesSize += CodedOutputStream.computeBoolSize(29, this.hostVideoOff_);
            }
            if ((this.bitField0_ & 268435456) == 268435456) {
                computeBytesSize += CodedOutputStream.computeBoolSize(30, this.attendeeVideoOff_);
            }
            if ((this.bitField0_ & 536870912) == 536870912) {
                computeBytesSize += CodedOutputStream.computeBoolSize(31, this.voipOff_);
            }
            if ((this.bitField0_ & Ints.MAX_POWER_OF_TWO) == 1073741824) {
                computeBytesSize += CodedOutputStream.computeBoolSize(32, this.telephonyOff_);
            }
            if ((this.bitField0_ & Integer.MIN_VALUE) == Integer.MIN_VALUE) {
                computeBytesSize += CodedOutputStream.computeInt32Size(33, this.supportCallOutType_);
            }
            if ((this.bitField1_ & 1) == 1) {
                computeBytesSize += CodedOutputStream.computeBoolSize(34, this.isH323Enabled_);
            }
            int i2 = computeBytesSize;
            for (int i3 = 0; i3 < this.calloutCountryCodes_.size(); i3++) {
                i2 += CodedOutputStream.computeMessageSize(35, (MessageLite) this.calloutCountryCodes_.get(i3));
            }
            for (int i4 = 0; i4 < this.callinCountryCodes_.size(); i4++) {
                i2 += CodedOutputStream.computeMessageSize(36, (MessageLite) this.callinCountryCodes_.get(i4));
            }
            if ((this.bitField1_ & 2) == 2) {
                i2 += CodedOutputStream.computeBytesSize(37, getOtherTeleConfInfoBytes());
            }
            if ((this.bitField1_ & 4) == 4) {
                i2 += CodedOutputStream.computeBoolSize(38, this.isSelfTelephonyOn_);
            }
            if ((this.bitField1_ & 8) == 8) {
                i2 += CodedOutputStream.computeBoolSize(39, this.usePmiAsMeetingID_);
            }
            if ((this.bitField1_ & 16) == 16) {
                i2 += CodedOutputStream.computeInt64Size(40, this.originalMeetingNumber_);
            }
            if ((this.bitField1_ & 32) == 32) {
                i2 += CodedOutputStream.computeBoolSize(41, this.isCnMeeting_);
            }
            if ((this.bitField1_ & 64) == 64) {
                i2 += CodedOutputStream.computeBytesSize(42, getTimeZoneIdBytes());
            }
            if ((this.bitField1_ & 128) == 128) {
                i2 += CodedOutputStream.computeBytesSize(43, getDefaultcallInCountryBytes());
            }
            if ((this.bitField1_ & 256) == 256) {
                i2 += CodedOutputStream.computeBoolSize(44, this.isTurnOnExternalAuth_);
            }
            if ((this.bitField1_ & 512) == 512) {
                i2 += CodedOutputStream.computeBoolSize(45, this.isOnlySignJoin_);
            }
            if ((this.bitField1_ & 1024) == 1024) {
                i2 += CodedOutputStream.computeMessageSize(46, this.authProto_);
            }
            if ((this.bitField1_ & 2048) == 2048) {
                i2 += CodedOutputStream.computeInt32Size(47, this.progressingMeetingCount_);
            }
            if ((this.bitField1_ & 4096) == 4096) {
                i2 += CodedOutputStream.computeInt32Size(48, this.meetingWaitStatus_);
            }
            for (int i5 = 0; i5 < this.tspCallinInfo_.size(); i5++) {
                i2 += CodedOutputStream.computeMessageSize(49, (MessageLite) this.tspCallinInfo_.get(i5));
            }
            for (int i6 = 0; i6 < this.alterHost_.size(); i6++) {
                i2 += CodedOutputStream.computeMessageSize(50, (MessageLite) this.alterHost_.get(i6));
            }
            if ((this.bitField1_ & 8192) == 8192) {
                i2 += CodedOutputStream.computeMessageSize(51, this.availableDialinCountry_);
            }
            if ((this.bitField1_ & 16384) == 16384) {
                i2 += CodedOutputStream.computeBytesSize(52, getGoogleCalendarUrlBytes());
            }
            if ((this.bitField1_ & 32768) == 32768) {
                i2 += CodedOutputStream.computeBoolSize(53, this.isEnableMeetingToPublic_);
            }
            if ((this.bitField1_ & 65536) == 65536) {
                i2 += CodedOutputStream.computeBoolSize(54, this.isEnableAutoRecordingLocal_);
            }
            if ((this.bitField1_ & 131072) == 131072) {
                i2 += CodedOutputStream.computeBoolSize(55, this.isEnableAutoRecordingCloud_);
            }
            if ((this.bitField1_ & 262144) == 262144) {
                i2 += CodedOutputStream.computeBoolSize(56, this.isEnableAutoRecordingMtgLevelFirst_);
            }
            if ((this.bitField1_ & 524288) == 524288) {
                i2 += CodedOutputStream.computeBoolSize(57, this.isEnableAudioWatermark_);
            }
            if ((this.bitField1_ & 1048576) == 1048576) {
                i2 += CodedOutputStream.computeBoolSize(58, this.isWebRecurrenceMeeting_);
            }
            if ((this.bitField1_ & 2097152) == 2097152) {
                i2 += CodedOutputStream.computeBytesSize(59, getDailinStringBytes());
            }
            if ((this.bitField1_ & 4194304) == 4194304) {
                i2 += CodedOutputStream.computeBoolSize(60, this.isEnableLanguageInterpretation_);
            }
            if ((this.bitField1_ & 8388608) == 8388608) {
                i2 += CodedOutputStream.computeBoolSize(61, this.isEnableWaitingRoom_);
            }
            if ((this.bitField1_ & 16777216) == 16777216) {
                i2 += CodedOutputStream.computeBoolSize(62, this.isSupportWaitingRoom_);
            }
            if ((this.bitField1_ & MediaHttpDownloader.MAXIMUM_CHUNK_SIZE) == 33554432) {
                i2 += CodedOutputStream.computeInt32Size(63, this.jbhPriorTime_);
            }
            if ((this.bitField1_ & 67108864) == 67108864) {
                i2 += CodedOutputStream.computeBytesSize(64, getJoinMeetingUrlForInviteBytes());
            }
            if ((this.bitField1_ & 134217728) == 134217728) {
                i2 += CodedOutputStream.computeBoolSize(65, this.pstnOnlyUseTelephone_);
            }
            if ((this.bitField1_ & 268435456) == 268435456) {
                i2 += CodedOutputStream.computeBoolSize(66, this.pstnUseOwnPhoneNumber_);
            }
            if ((this.bitField1_ & 536870912) == 536870912) {
                i2 += CodedOutputStream.computeBoolSize(67, this.pstnPhoneNumberNotMatchCallout_);
            }
            if ((this.bitField1_ & Ints.MAX_POWER_OF_TWO) == 1073741824) {
                i2 += CodedOutputStream.computeBoolSize(68, this.pstnHideInviteByPhone_);
            }
            this.memoizedSerializedSize = i2;
            return i2;
        }

        /* access modifiers changed from: protected */
        public Object writeReplace() throws ObjectStreamException {
            return super.writeReplace();
        }

        public static MeetingInfoProto parseFrom(ByteString byteString) throws InvalidProtocolBufferException {
            return ((Builder) newBuilder().mergeFrom(byteString)).buildParsed();
        }

        public static MeetingInfoProto parseFrom(ByteString byteString, ExtensionRegistryLite extensionRegistryLite) throws InvalidProtocolBufferException {
            return ((Builder) newBuilder().mergeFrom(byteString, extensionRegistryLite)).buildParsed();
        }

        public static MeetingInfoProto parseFrom(byte[] bArr) throws InvalidProtocolBufferException {
            return ((Builder) newBuilder().mergeFrom(bArr)).buildParsed();
        }

        public static MeetingInfoProto parseFrom(byte[] bArr, ExtensionRegistryLite extensionRegistryLite) throws InvalidProtocolBufferException {
            return ((Builder) newBuilder().mergeFrom(bArr, extensionRegistryLite)).buildParsed();
        }

        public static MeetingInfoProto parseFrom(InputStream inputStream) throws IOException {
            return ((Builder) newBuilder().mergeFrom(inputStream)).buildParsed();
        }

        public static MeetingInfoProto parseFrom(InputStream inputStream, ExtensionRegistryLite extensionRegistryLite) throws IOException {
            return ((Builder) newBuilder().mergeFrom(inputStream, extensionRegistryLite)).buildParsed();
        }

        public static MeetingInfoProto parseDelimitedFrom(InputStream inputStream) throws IOException {
            Builder newBuilder = newBuilder();
            if (newBuilder.mergeDelimitedFrom(inputStream)) {
                return newBuilder.buildParsed();
            }
            return null;
        }

        public static MeetingInfoProto parseDelimitedFrom(InputStream inputStream, ExtensionRegistryLite extensionRegistryLite) throws IOException {
            Builder newBuilder = newBuilder();
            if (newBuilder.mergeDelimitedFrom(inputStream, extensionRegistryLite)) {
                return newBuilder.buildParsed();
            }
            return null;
        }

        public static MeetingInfoProto parseFrom(CodedInputStream codedInputStream) throws IOException {
            return ((Builder) newBuilder().mergeFrom(codedInputStream)).buildParsed();
        }

        public static MeetingInfoProto parseFrom(CodedInputStream codedInputStream, ExtensionRegistryLite extensionRegistryLite) throws IOException {
            return newBuilder().mergeFrom(codedInputStream, extensionRegistryLite).buildParsed();
        }

        public static Builder newBuilder() {
            return Builder.create();
        }

        public Builder newBuilderForType() {
            return newBuilder();
        }

        public static Builder newBuilder(MeetingInfoProto meetingInfoProto) {
            return newBuilder().mergeFrom(meetingInfoProto);
        }

        public Builder toBuilder() {
            return newBuilder(this);
        }

        static {
            defaultInstance.initFields();
        }
    }

    public interface MeetingInfoProtoOrBuilder extends MessageLiteOrBuilder {
        AlterHost getAlterHost(int i);

        int getAlterHostCount();

        List<AlterHost> getAlterHostList();

        String getAssistantId();

        boolean getAttendeeVideoOff();

        AuthProto getAuthProto();

        AvailableDialinCountry getAvailableDialinCountry();

        CountryCode getCallinCountryCodes(int i);

        int getCallinCountryCodesCount();

        List<CountryCode> getCallinCountryCodesList();

        String getCallinNumber();

        CountryCode getCalloutCountryCodes(int i);

        int getCalloutCountryCodesCount();

        List<CountryCode> getCalloutCountryCodesList();

        boolean getCanJoinBeforeHost();

        String getDailinString();

        String getDefaultcallInCountry();

        int getDuration();

        int getExtendMeetingType();

        String getGoogleCalendarUrl();

        String getH323Gateway();

        boolean getHostVideoOff();

        String getId();

        String getInviteEmailContent();

        String getInviteEmailContentWithTime();

        String getInviteEmailSubject();

        boolean getIsAudioOnlyMeeting();

        boolean getIsCnMeeting();

        boolean getIsEnableAudioWatermark();

        boolean getIsEnableAutoRecordingCloud();

        boolean getIsEnableAutoRecordingLocal();

        boolean getIsEnableAutoRecordingMtgLevelFirst();

        boolean getIsEnableLanguageInterpretation();

        boolean getIsEnableMeetingToPublic();

        boolean getIsEnableWaitingRoom();

        boolean getIsH323Enabled();

        boolean getIsOnlySignJoin();

        boolean getIsSelfTelephonyOn();

        boolean getIsShareOnlyMeeting();

        boolean getIsSupportWaitingRoom();

        boolean getIsTurnOnExternalAuth();

        boolean getIsWebRecurrenceMeeting();

        boolean getIsWebinar();

        int getJbhPriorTime();

        String getJoinMeetingUrl();

        String getJoinMeetingUrlForInvite();

        String getMeetingHostID();

        String getMeetingHostName();

        long getMeetingNumber();

        int getMeetingStatus();

        int getMeetingWaitStatus();

        long getOriginalMeetingNumber();

        String getOtherTeleConfInfo();

        boolean getPSTNEnabled();

        boolean getPSTNNeedConfirm1();

        String getPassword();

        int getProgressingMeetingCount();

        boolean getPstnHideInviteByPhone();

        boolean getPstnOnlyUseTelephone();

        boolean getPstnPhoneNumberNotMatchCallout();

        boolean getPstnUseOwnPhoneNumber();

        long getRepeatEndTime();

        int getRepeatType();

        long getStartTime();

        int getSupportCallOutType();

        boolean getTelephonyOff();

        String getTimeZoneId();

        String getTopic();

        TSPCallInInfo getTspCallinInfo(int i);

        int getTspCallinInfoCount();

        List<TSPCallInInfo> getTspCallinInfoList();

        MeetingType getType();

        boolean getUsePmiAsMeetingID();

        boolean getVoipOff();

        String getWebinarRegUrl();

        boolean hasAssistantId();

        boolean hasAttendeeVideoOff();

        boolean hasAuthProto();

        boolean hasAvailableDialinCountry();

        boolean hasCallinNumber();

        boolean hasCanJoinBeforeHost();

        boolean hasDailinString();

        boolean hasDefaultcallInCountry();

        boolean hasDuration();

        boolean hasExtendMeetingType();

        boolean hasGoogleCalendarUrl();

        boolean hasH323Gateway();

        boolean hasHostVideoOff();

        boolean hasId();

        boolean hasInviteEmailContent();

        boolean hasInviteEmailContentWithTime();

        boolean hasInviteEmailSubject();

        boolean hasIsAudioOnlyMeeting();

        boolean hasIsCnMeeting();

        boolean hasIsEnableAudioWatermark();

        boolean hasIsEnableAutoRecordingCloud();

        boolean hasIsEnableAutoRecordingLocal();

        boolean hasIsEnableAutoRecordingMtgLevelFirst();

        boolean hasIsEnableLanguageInterpretation();

        boolean hasIsEnableMeetingToPublic();

        boolean hasIsEnableWaitingRoom();

        boolean hasIsH323Enabled();

        boolean hasIsOnlySignJoin();

        boolean hasIsSelfTelephonyOn();

        boolean hasIsShareOnlyMeeting();

        boolean hasIsSupportWaitingRoom();

        boolean hasIsTurnOnExternalAuth();

        boolean hasIsWebRecurrenceMeeting();

        boolean hasIsWebinar();

        boolean hasJbhPriorTime();

        boolean hasJoinMeetingUrl();

        boolean hasJoinMeetingUrlForInvite();

        boolean hasMeetingHostID();

        boolean hasMeetingHostName();

        boolean hasMeetingNumber();

        boolean hasMeetingStatus();

        boolean hasMeetingWaitStatus();

        boolean hasOriginalMeetingNumber();

        boolean hasOtherTeleConfInfo();

        boolean hasPSTNEnabled();

        boolean hasPSTNNeedConfirm1();

        boolean hasPassword();

        boolean hasProgressingMeetingCount();

        boolean hasPstnHideInviteByPhone();

        boolean hasPstnOnlyUseTelephone();

        boolean hasPstnPhoneNumberNotMatchCallout();

        boolean hasPstnUseOwnPhoneNumber();

        boolean hasRepeatEndTime();

        boolean hasRepeatType();

        boolean hasStartTime();

        boolean hasSupportCallOutType();

        boolean hasTelephonyOff();

        boolean hasTimeZoneId();

        boolean hasTopic();

        boolean hasType();

        boolean hasUsePmiAsMeetingID();

        boolean hasVoipOff();

        boolean hasWebinarRegUrl();
    }

    public static final class RealNameAuthCountryCodes extends GeneratedMessageLite implements RealNameAuthCountryCodesOrBuilder {
        public static final int REALNAMEAUTHCOUNTRYCODES_FIELD_NUMBER = 1;
        private static final RealNameAuthCountryCodes defaultInstance = new RealNameAuthCountryCodes(true);
        private static final long serialVersionUID = 0;
        private byte memoizedIsInitialized;
        private int memoizedSerializedSize;
        /* access modifiers changed from: private */
        public List<CountryCode> realNameAuthCountryCodes_;

        public static final class Builder extends com.google.protobuf.GeneratedMessageLite.Builder<RealNameAuthCountryCodes, Builder> implements RealNameAuthCountryCodesOrBuilder {
            private int bitField0_;
            private List<CountryCode> realNameAuthCountryCodes_ = Collections.emptyList();

            private void maybeForceBuilderInitialization() {
            }

            public final boolean isInitialized() {
                return true;
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
                this.realNameAuthCountryCodes_ = Collections.emptyList();
                this.bitField0_ &= -2;
                return this;
            }

            public Builder clone() {
                return create().mergeFrom(buildPartial());
            }

            public RealNameAuthCountryCodes getDefaultInstanceForType() {
                return RealNameAuthCountryCodes.getDefaultInstance();
            }

            public RealNameAuthCountryCodes build() {
                RealNameAuthCountryCodes buildPartial = buildPartial();
                if (buildPartial.isInitialized()) {
                    return buildPartial;
                }
                throw newUninitializedMessageException(buildPartial);
            }

            /* access modifiers changed from: private */
            public RealNameAuthCountryCodes buildParsed() throws InvalidProtocolBufferException {
                RealNameAuthCountryCodes buildPartial = buildPartial();
                if (buildPartial.isInitialized()) {
                    return buildPartial;
                }
                throw newUninitializedMessageException(buildPartial).asInvalidProtocolBufferException();
            }

            public RealNameAuthCountryCodes buildPartial() {
                RealNameAuthCountryCodes realNameAuthCountryCodes = new RealNameAuthCountryCodes(this);
                if ((this.bitField0_ & 1) == 1) {
                    this.realNameAuthCountryCodes_ = Collections.unmodifiableList(this.realNameAuthCountryCodes_);
                    this.bitField0_ &= -2;
                }
                realNameAuthCountryCodes.realNameAuthCountryCodes_ = this.realNameAuthCountryCodes_;
                return realNameAuthCountryCodes;
            }

            public Builder mergeFrom(RealNameAuthCountryCodes realNameAuthCountryCodes) {
                if (realNameAuthCountryCodes != RealNameAuthCountryCodes.getDefaultInstance() && !realNameAuthCountryCodes.realNameAuthCountryCodes_.isEmpty()) {
                    if (this.realNameAuthCountryCodes_.isEmpty()) {
                        this.realNameAuthCountryCodes_ = realNameAuthCountryCodes.realNameAuthCountryCodes_;
                        this.bitField0_ &= -2;
                    } else {
                        ensureRealNameAuthCountryCodesIsMutable();
                        this.realNameAuthCountryCodes_.addAll(realNameAuthCountryCodes.realNameAuthCountryCodes_);
                    }
                }
                return this;
            }

            public Builder mergeFrom(CodedInputStream codedInputStream, ExtensionRegistryLite extensionRegistryLite) throws IOException {
                while (true) {
                    int readTag = codedInputStream.readTag();
                    if (readTag == 0) {
                        return this;
                    }
                    if (readTag == 10) {
                        Builder newBuilder = CountryCode.newBuilder();
                        codedInputStream.readMessage(newBuilder, extensionRegistryLite);
                        addRealNameAuthCountryCodes(newBuilder.buildPartial());
                    } else if (!parseUnknownField(codedInputStream, extensionRegistryLite, readTag)) {
                        return this;
                    }
                }
            }

            private void ensureRealNameAuthCountryCodesIsMutable() {
                if ((this.bitField0_ & 1) != 1) {
                    this.realNameAuthCountryCodes_ = new ArrayList(this.realNameAuthCountryCodes_);
                    this.bitField0_ |= 1;
                }
            }

            public List<CountryCode> getRealNameAuthCountryCodesList() {
                return Collections.unmodifiableList(this.realNameAuthCountryCodes_);
            }

            public int getRealNameAuthCountryCodesCount() {
                return this.realNameAuthCountryCodes_.size();
            }

            public CountryCode getRealNameAuthCountryCodes(int i) {
                return (CountryCode) this.realNameAuthCountryCodes_.get(i);
            }

            public Builder setRealNameAuthCountryCodes(int i, CountryCode countryCode) {
                if (countryCode != null) {
                    ensureRealNameAuthCountryCodesIsMutable();
                    this.realNameAuthCountryCodes_.set(i, countryCode);
                    return this;
                }
                throw new NullPointerException();
            }

            public Builder setRealNameAuthCountryCodes(int i, Builder builder) {
                ensureRealNameAuthCountryCodesIsMutable();
                this.realNameAuthCountryCodes_.set(i, builder.build());
                return this;
            }

            public Builder addRealNameAuthCountryCodes(CountryCode countryCode) {
                if (countryCode != null) {
                    ensureRealNameAuthCountryCodesIsMutable();
                    this.realNameAuthCountryCodes_.add(countryCode);
                    return this;
                }
                throw new NullPointerException();
            }

            public Builder addRealNameAuthCountryCodes(int i, CountryCode countryCode) {
                if (countryCode != null) {
                    ensureRealNameAuthCountryCodesIsMutable();
                    this.realNameAuthCountryCodes_.add(i, countryCode);
                    return this;
                }
                throw new NullPointerException();
            }

            public Builder addRealNameAuthCountryCodes(Builder builder) {
                ensureRealNameAuthCountryCodesIsMutable();
                this.realNameAuthCountryCodes_.add(builder.build());
                return this;
            }

            public Builder addRealNameAuthCountryCodes(int i, Builder builder) {
                ensureRealNameAuthCountryCodesIsMutable();
                this.realNameAuthCountryCodes_.add(i, builder.build());
                return this;
            }

            public Builder addAllRealNameAuthCountryCodes(Iterable<? extends CountryCode> iterable) {
                ensureRealNameAuthCountryCodesIsMutable();
                com.google.protobuf.GeneratedMessageLite.Builder.addAll(iterable, this.realNameAuthCountryCodes_);
                return this;
            }

            public Builder clearRealNameAuthCountryCodes() {
                this.realNameAuthCountryCodes_ = Collections.emptyList();
                this.bitField0_ &= -2;
                return this;
            }

            public Builder removeRealNameAuthCountryCodes(int i) {
                ensureRealNameAuthCountryCodesIsMutable();
                this.realNameAuthCountryCodes_.remove(i);
                return this;
            }
        }

        private RealNameAuthCountryCodes(Builder builder) {
            super(builder);
            this.memoizedIsInitialized = -1;
            this.memoizedSerializedSize = -1;
        }

        private RealNameAuthCountryCodes(boolean z) {
            this.memoizedIsInitialized = -1;
            this.memoizedSerializedSize = -1;
        }

        public static RealNameAuthCountryCodes getDefaultInstance() {
            return defaultInstance;
        }

        public RealNameAuthCountryCodes getDefaultInstanceForType() {
            return defaultInstance;
        }

        public List<CountryCode> getRealNameAuthCountryCodesList() {
            return this.realNameAuthCountryCodes_;
        }

        public List<? extends CountryCodeOrBuilder> getRealNameAuthCountryCodesOrBuilderList() {
            return this.realNameAuthCountryCodes_;
        }

        public int getRealNameAuthCountryCodesCount() {
            return this.realNameAuthCountryCodes_.size();
        }

        public CountryCode getRealNameAuthCountryCodes(int i) {
            return (CountryCode) this.realNameAuthCountryCodes_.get(i);
        }

        public CountryCodeOrBuilder getRealNameAuthCountryCodesOrBuilder(int i) {
            return (CountryCodeOrBuilder) this.realNameAuthCountryCodes_.get(i);
        }

        private void initFields() {
            this.realNameAuthCountryCodes_ = Collections.emptyList();
        }

        public final boolean isInitialized() {
            byte b = this.memoizedIsInitialized;
            boolean z = true;
            if (b != -1) {
                if (b != 1) {
                    z = false;
                }
                return z;
            }
            this.memoizedIsInitialized = 1;
            return true;
        }

        public void writeTo(CodedOutputStream codedOutputStream) throws IOException {
            getSerializedSize();
            for (int i = 0; i < this.realNameAuthCountryCodes_.size(); i++) {
                codedOutputStream.writeMessage(1, (MessageLite) this.realNameAuthCountryCodes_.get(i));
            }
        }

        public int getSerializedSize() {
            int i = this.memoizedSerializedSize;
            if (i != -1) {
                return i;
            }
            int i2 = 0;
            for (int i3 = 0; i3 < this.realNameAuthCountryCodes_.size(); i3++) {
                i2 += CodedOutputStream.computeMessageSize(1, (MessageLite) this.realNameAuthCountryCodes_.get(i3));
            }
            this.memoizedSerializedSize = i2;
            return i2;
        }

        /* access modifiers changed from: protected */
        public Object writeReplace() throws ObjectStreamException {
            return super.writeReplace();
        }

        public static RealNameAuthCountryCodes parseFrom(ByteString byteString) throws InvalidProtocolBufferException {
            return ((Builder) newBuilder().mergeFrom(byteString)).buildParsed();
        }

        public static RealNameAuthCountryCodes parseFrom(ByteString byteString, ExtensionRegistryLite extensionRegistryLite) throws InvalidProtocolBufferException {
            return ((Builder) newBuilder().mergeFrom(byteString, extensionRegistryLite)).buildParsed();
        }

        public static RealNameAuthCountryCodes parseFrom(byte[] bArr) throws InvalidProtocolBufferException {
            return ((Builder) newBuilder().mergeFrom(bArr)).buildParsed();
        }

        public static RealNameAuthCountryCodes parseFrom(byte[] bArr, ExtensionRegistryLite extensionRegistryLite) throws InvalidProtocolBufferException {
            return ((Builder) newBuilder().mergeFrom(bArr, extensionRegistryLite)).buildParsed();
        }

        public static RealNameAuthCountryCodes parseFrom(InputStream inputStream) throws IOException {
            return ((Builder) newBuilder().mergeFrom(inputStream)).buildParsed();
        }

        public static RealNameAuthCountryCodes parseFrom(InputStream inputStream, ExtensionRegistryLite extensionRegistryLite) throws IOException {
            return ((Builder) newBuilder().mergeFrom(inputStream, extensionRegistryLite)).buildParsed();
        }

        public static RealNameAuthCountryCodes parseDelimitedFrom(InputStream inputStream) throws IOException {
            Builder newBuilder = newBuilder();
            if (newBuilder.mergeDelimitedFrom(inputStream)) {
                return newBuilder.buildParsed();
            }
            return null;
        }

        public static RealNameAuthCountryCodes parseDelimitedFrom(InputStream inputStream, ExtensionRegistryLite extensionRegistryLite) throws IOException {
            Builder newBuilder = newBuilder();
            if (newBuilder.mergeDelimitedFrom(inputStream, extensionRegistryLite)) {
                return newBuilder.buildParsed();
            }
            return null;
        }

        public static RealNameAuthCountryCodes parseFrom(CodedInputStream codedInputStream) throws IOException {
            return ((Builder) newBuilder().mergeFrom(codedInputStream)).buildParsed();
        }

        public static RealNameAuthCountryCodes parseFrom(CodedInputStream codedInputStream, ExtensionRegistryLite extensionRegistryLite) throws IOException {
            return newBuilder().mergeFrom(codedInputStream, extensionRegistryLite).buildParsed();
        }

        public static Builder newBuilder() {
            return Builder.create();
        }

        public Builder newBuilderForType() {
            return newBuilder();
        }

        public static Builder newBuilder(RealNameAuthCountryCodes realNameAuthCountryCodes) {
            return newBuilder().mergeFrom(realNameAuthCountryCodes);
        }

        public Builder toBuilder() {
            return newBuilder(this);
        }

        static {
            defaultInstance.initFields();
        }
    }

    public interface RealNameAuthCountryCodesOrBuilder extends MessageLiteOrBuilder {
        CountryCode getRealNameAuthCountryCodes(int i);

        int getRealNameAuthCountryCodesCount();

        List<CountryCode> getRealNameAuthCountryCodesList();
    }

    public static final class TSPCallInInfo extends GeneratedMessageLite implements TSPCallInInfoOrBuilder {
        public static final int KEY_FIELD_NUMBER = 1;
        public static final int VALUE_FIELD_NUMBER = 2;
        private static final TSPCallInInfo defaultInstance = new TSPCallInInfo(true);
        private static final long serialVersionUID = 0;
        /* access modifiers changed from: private */
        public int bitField0_;
        /* access modifiers changed from: private */
        public Object key_;
        private byte memoizedIsInitialized;
        private int memoizedSerializedSize;
        /* access modifiers changed from: private */
        public Object value_;

        public static final class Builder extends com.google.protobuf.GeneratedMessageLite.Builder<TSPCallInInfo, Builder> implements TSPCallInInfoOrBuilder {
            private int bitField0_;
            private Object key_ = "";
            private Object value_ = "";

            private void maybeForceBuilderInitialization() {
            }

            public final boolean isInitialized() {
                return true;
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
                this.key_ = "";
                this.bitField0_ &= -2;
                this.value_ = "";
                this.bitField0_ &= -3;
                return this;
            }

            public Builder clone() {
                return create().mergeFrom(buildPartial());
            }

            public TSPCallInInfo getDefaultInstanceForType() {
                return TSPCallInInfo.getDefaultInstance();
            }

            public TSPCallInInfo build() {
                TSPCallInInfo buildPartial = buildPartial();
                if (buildPartial.isInitialized()) {
                    return buildPartial;
                }
                throw newUninitializedMessageException(buildPartial);
            }

            /* access modifiers changed from: private */
            public TSPCallInInfo buildParsed() throws InvalidProtocolBufferException {
                TSPCallInInfo buildPartial = buildPartial();
                if (buildPartial.isInitialized()) {
                    return buildPartial;
                }
                throw newUninitializedMessageException(buildPartial).asInvalidProtocolBufferException();
            }

            public TSPCallInInfo buildPartial() {
                TSPCallInInfo tSPCallInInfo = new TSPCallInInfo(this);
                int i = this.bitField0_;
                int i2 = 1;
                if ((i & 1) != 1) {
                    i2 = 0;
                }
                tSPCallInInfo.key_ = this.key_;
                if ((i & 2) == 2) {
                    i2 |= 2;
                }
                tSPCallInInfo.value_ = this.value_;
                tSPCallInInfo.bitField0_ = i2;
                return tSPCallInInfo;
            }

            public Builder mergeFrom(TSPCallInInfo tSPCallInInfo) {
                if (tSPCallInInfo == TSPCallInInfo.getDefaultInstance()) {
                    return this;
                }
                if (tSPCallInInfo.hasKey()) {
                    setKey(tSPCallInInfo.getKey());
                }
                if (tSPCallInInfo.hasValue()) {
                    setValue(tSPCallInInfo.getValue());
                }
                return this;
            }

            public Builder mergeFrom(CodedInputStream codedInputStream, ExtensionRegistryLite extensionRegistryLite) throws IOException {
                while (true) {
                    int readTag = codedInputStream.readTag();
                    if (readTag == 0) {
                        return this;
                    }
                    if (readTag == 10) {
                        this.bitField0_ |= 1;
                        this.key_ = codedInputStream.readBytes();
                    } else if (readTag == 18) {
                        this.bitField0_ |= 2;
                        this.value_ = codedInputStream.readBytes();
                    } else if (!parseUnknownField(codedInputStream, extensionRegistryLite, readTag)) {
                        return this;
                    }
                }
            }

            public boolean hasKey() {
                return (this.bitField0_ & 1) == 1;
            }

            public String getKey() {
                Object obj = this.key_;
                if (obj instanceof String) {
                    return (String) obj;
                }
                String stringUtf8 = ((ByteString) obj).toStringUtf8();
                this.key_ = stringUtf8;
                return stringUtf8;
            }

            public Builder setKey(String str) {
                if (str != null) {
                    this.bitField0_ |= 1;
                    this.key_ = str;
                    return this;
                }
                throw new NullPointerException();
            }

            public Builder clearKey() {
                this.bitField0_ &= -2;
                this.key_ = TSPCallInInfo.getDefaultInstance().getKey();
                return this;
            }

            /* access modifiers changed from: 0000 */
            public void setKey(ByteString byteString) {
                this.bitField0_ |= 1;
                this.key_ = byteString;
            }

            public boolean hasValue() {
                return (this.bitField0_ & 2) == 2;
            }

            public String getValue() {
                Object obj = this.value_;
                if (obj instanceof String) {
                    return (String) obj;
                }
                String stringUtf8 = ((ByteString) obj).toStringUtf8();
                this.value_ = stringUtf8;
                return stringUtf8;
            }

            public Builder setValue(String str) {
                if (str != null) {
                    this.bitField0_ |= 2;
                    this.value_ = str;
                    return this;
                }
                throw new NullPointerException();
            }

            public Builder clearValue() {
                this.bitField0_ &= -3;
                this.value_ = TSPCallInInfo.getDefaultInstance().getValue();
                return this;
            }

            /* access modifiers changed from: 0000 */
            public void setValue(ByteString byteString) {
                this.bitField0_ |= 2;
                this.value_ = byteString;
            }
        }

        private TSPCallInInfo(Builder builder) {
            super(builder);
            this.memoizedIsInitialized = -1;
            this.memoizedSerializedSize = -1;
        }

        private TSPCallInInfo(boolean z) {
            this.memoizedIsInitialized = -1;
            this.memoizedSerializedSize = -1;
        }

        public static TSPCallInInfo getDefaultInstance() {
            return defaultInstance;
        }

        public TSPCallInInfo getDefaultInstanceForType() {
            return defaultInstance;
        }

        public boolean hasKey() {
            return (this.bitField0_ & 1) == 1;
        }

        public String getKey() {
            Object obj = this.key_;
            if (obj instanceof String) {
                return (String) obj;
            }
            ByteString byteString = (ByteString) obj;
            String stringUtf8 = byteString.toStringUtf8();
            if (Internal.isValidUtf8(byteString)) {
                this.key_ = stringUtf8;
            }
            return stringUtf8;
        }

        private ByteString getKeyBytes() {
            Object obj = this.key_;
            if (!(obj instanceof String)) {
                return (ByteString) obj;
            }
            ByteString copyFromUtf8 = ByteString.copyFromUtf8((String) obj);
            this.key_ = copyFromUtf8;
            return copyFromUtf8;
        }

        public boolean hasValue() {
            return (this.bitField0_ & 2) == 2;
        }

        public String getValue() {
            Object obj = this.value_;
            if (obj instanceof String) {
                return (String) obj;
            }
            ByteString byteString = (ByteString) obj;
            String stringUtf8 = byteString.toStringUtf8();
            if (Internal.isValidUtf8(byteString)) {
                this.value_ = stringUtf8;
            }
            return stringUtf8;
        }

        private ByteString getValueBytes() {
            Object obj = this.value_;
            if (!(obj instanceof String)) {
                return (ByteString) obj;
            }
            ByteString copyFromUtf8 = ByteString.copyFromUtf8((String) obj);
            this.value_ = copyFromUtf8;
            return copyFromUtf8;
        }

        private void initFields() {
            this.key_ = "";
            this.value_ = "";
        }

        public final boolean isInitialized() {
            byte b = this.memoizedIsInitialized;
            boolean z = true;
            if (b != -1) {
                if (b != 1) {
                    z = false;
                }
                return z;
            }
            this.memoizedIsInitialized = 1;
            return true;
        }

        public void writeTo(CodedOutputStream codedOutputStream) throws IOException {
            getSerializedSize();
            if ((this.bitField0_ & 1) == 1) {
                codedOutputStream.writeBytes(1, getKeyBytes());
            }
            if ((this.bitField0_ & 2) == 2) {
                codedOutputStream.writeBytes(2, getValueBytes());
            }
        }

        public int getSerializedSize() {
            int i = this.memoizedSerializedSize;
            if (i != -1) {
                return i;
            }
            int i2 = 0;
            if ((this.bitField0_ & 1) == 1) {
                i2 = 0 + CodedOutputStream.computeBytesSize(1, getKeyBytes());
            }
            if ((this.bitField0_ & 2) == 2) {
                i2 += CodedOutputStream.computeBytesSize(2, getValueBytes());
            }
            this.memoizedSerializedSize = i2;
            return i2;
        }

        /* access modifiers changed from: protected */
        public Object writeReplace() throws ObjectStreamException {
            return super.writeReplace();
        }

        public static TSPCallInInfo parseFrom(ByteString byteString) throws InvalidProtocolBufferException {
            return ((Builder) newBuilder().mergeFrom(byteString)).buildParsed();
        }

        public static TSPCallInInfo parseFrom(ByteString byteString, ExtensionRegistryLite extensionRegistryLite) throws InvalidProtocolBufferException {
            return ((Builder) newBuilder().mergeFrom(byteString, extensionRegistryLite)).buildParsed();
        }

        public static TSPCallInInfo parseFrom(byte[] bArr) throws InvalidProtocolBufferException {
            return ((Builder) newBuilder().mergeFrom(bArr)).buildParsed();
        }

        public static TSPCallInInfo parseFrom(byte[] bArr, ExtensionRegistryLite extensionRegistryLite) throws InvalidProtocolBufferException {
            return ((Builder) newBuilder().mergeFrom(bArr, extensionRegistryLite)).buildParsed();
        }

        public static TSPCallInInfo parseFrom(InputStream inputStream) throws IOException {
            return ((Builder) newBuilder().mergeFrom(inputStream)).buildParsed();
        }

        public static TSPCallInInfo parseFrom(InputStream inputStream, ExtensionRegistryLite extensionRegistryLite) throws IOException {
            return ((Builder) newBuilder().mergeFrom(inputStream, extensionRegistryLite)).buildParsed();
        }

        public static TSPCallInInfo parseDelimitedFrom(InputStream inputStream) throws IOException {
            Builder newBuilder = newBuilder();
            if (newBuilder.mergeDelimitedFrom(inputStream)) {
                return newBuilder.buildParsed();
            }
            return null;
        }

        public static TSPCallInInfo parseDelimitedFrom(InputStream inputStream, ExtensionRegistryLite extensionRegistryLite) throws IOException {
            Builder newBuilder = newBuilder();
            if (newBuilder.mergeDelimitedFrom(inputStream, extensionRegistryLite)) {
                return newBuilder.buildParsed();
            }
            return null;
        }

        public static TSPCallInInfo parseFrom(CodedInputStream codedInputStream) throws IOException {
            return ((Builder) newBuilder().mergeFrom(codedInputStream)).buildParsed();
        }

        public static TSPCallInInfo parseFrom(CodedInputStream codedInputStream, ExtensionRegistryLite extensionRegistryLite) throws IOException {
            return newBuilder().mergeFrom(codedInputStream, extensionRegistryLite).buildParsed();
        }

        public static Builder newBuilder() {
            return Builder.create();
        }

        public Builder newBuilderForType() {
            return newBuilder();
        }

        public static Builder newBuilder(TSPCallInInfo tSPCallInInfo) {
            return newBuilder().mergeFrom(tSPCallInInfo);
        }

        public Builder toBuilder() {
            return newBuilder(this);
        }

        static {
            defaultInstance.initFields();
        }
    }

    public interface TSPCallInInfoOrBuilder extends MessageLiteOrBuilder {
        String getKey();

        String getValue();

        boolean hasKey();

        boolean hasValue();
    }

    public static final class UserPhoneInfo extends GeneratedMessageLite implements UserPhoneInfoOrBuilder {
        public static final int COUNTRYCODE_FIELD_NUMBER = 3;
        public static final int COUNTRYID_FIELD_NUMBER = 2;
        public static final int PHONENUMBER_FIELD_NUMBER = 1;
        private static final UserPhoneInfo defaultInstance = new UserPhoneInfo(true);
        private static final long serialVersionUID = 0;
        /* access modifiers changed from: private */
        public int bitField0_;
        /* access modifiers changed from: private */
        public Object countryCode_;
        /* access modifiers changed from: private */
        public Object countryId_;
        private byte memoizedIsInitialized;
        private int memoizedSerializedSize;
        /* access modifiers changed from: private */
        public Object phoneNumber_;

        public static final class Builder extends com.google.protobuf.GeneratedMessageLite.Builder<UserPhoneInfo, Builder> implements UserPhoneInfoOrBuilder {
            private int bitField0_;
            private Object countryCode_ = "";
            private Object countryId_ = "";
            private Object phoneNumber_ = "";

            private void maybeForceBuilderInitialization() {
            }

            public final boolean isInitialized() {
                return true;
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
                this.phoneNumber_ = "";
                this.bitField0_ &= -2;
                this.countryId_ = "";
                this.bitField0_ &= -3;
                this.countryCode_ = "";
                this.bitField0_ &= -5;
                return this;
            }

            public Builder clone() {
                return create().mergeFrom(buildPartial());
            }

            public UserPhoneInfo getDefaultInstanceForType() {
                return UserPhoneInfo.getDefaultInstance();
            }

            public UserPhoneInfo build() {
                UserPhoneInfo buildPartial = buildPartial();
                if (buildPartial.isInitialized()) {
                    return buildPartial;
                }
                throw newUninitializedMessageException(buildPartial);
            }

            /* access modifiers changed from: private */
            public UserPhoneInfo buildParsed() throws InvalidProtocolBufferException {
                UserPhoneInfo buildPartial = buildPartial();
                if (buildPartial.isInitialized()) {
                    return buildPartial;
                }
                throw newUninitializedMessageException(buildPartial).asInvalidProtocolBufferException();
            }

            public UserPhoneInfo buildPartial() {
                UserPhoneInfo userPhoneInfo = new UserPhoneInfo(this);
                int i = this.bitField0_;
                int i2 = 1;
                if ((i & 1) != 1) {
                    i2 = 0;
                }
                userPhoneInfo.phoneNumber_ = this.phoneNumber_;
                if ((i & 2) == 2) {
                    i2 |= 2;
                }
                userPhoneInfo.countryId_ = this.countryId_;
                if ((i & 4) == 4) {
                    i2 |= 4;
                }
                userPhoneInfo.countryCode_ = this.countryCode_;
                userPhoneInfo.bitField0_ = i2;
                return userPhoneInfo;
            }

            public Builder mergeFrom(UserPhoneInfo userPhoneInfo) {
                if (userPhoneInfo == UserPhoneInfo.getDefaultInstance()) {
                    return this;
                }
                if (userPhoneInfo.hasPhoneNumber()) {
                    setPhoneNumber(userPhoneInfo.getPhoneNumber());
                }
                if (userPhoneInfo.hasCountryId()) {
                    setCountryId(userPhoneInfo.getCountryId());
                }
                if (userPhoneInfo.hasCountryCode()) {
                    setCountryCode(userPhoneInfo.getCountryCode());
                }
                return this;
            }

            public Builder mergeFrom(CodedInputStream codedInputStream, ExtensionRegistryLite extensionRegistryLite) throws IOException {
                while (true) {
                    int readTag = codedInputStream.readTag();
                    if (readTag == 0) {
                        return this;
                    }
                    if (readTag == 10) {
                        this.bitField0_ |= 1;
                        this.phoneNumber_ = codedInputStream.readBytes();
                    } else if (readTag == 18) {
                        this.bitField0_ |= 2;
                        this.countryId_ = codedInputStream.readBytes();
                    } else if (readTag == 26) {
                        this.bitField0_ |= 4;
                        this.countryCode_ = codedInputStream.readBytes();
                    } else if (!parseUnknownField(codedInputStream, extensionRegistryLite, readTag)) {
                        return this;
                    }
                }
            }

            public boolean hasPhoneNumber() {
                return (this.bitField0_ & 1) == 1;
            }

            public String getPhoneNumber() {
                Object obj = this.phoneNumber_;
                if (obj instanceof String) {
                    return (String) obj;
                }
                String stringUtf8 = ((ByteString) obj).toStringUtf8();
                this.phoneNumber_ = stringUtf8;
                return stringUtf8;
            }

            public Builder setPhoneNumber(String str) {
                if (str != null) {
                    this.bitField0_ |= 1;
                    this.phoneNumber_ = str;
                    return this;
                }
                throw new NullPointerException();
            }

            public Builder clearPhoneNumber() {
                this.bitField0_ &= -2;
                this.phoneNumber_ = UserPhoneInfo.getDefaultInstance().getPhoneNumber();
                return this;
            }

            /* access modifiers changed from: 0000 */
            public void setPhoneNumber(ByteString byteString) {
                this.bitField0_ |= 1;
                this.phoneNumber_ = byteString;
            }

            public boolean hasCountryId() {
                return (this.bitField0_ & 2) == 2;
            }

            public String getCountryId() {
                Object obj = this.countryId_;
                if (obj instanceof String) {
                    return (String) obj;
                }
                String stringUtf8 = ((ByteString) obj).toStringUtf8();
                this.countryId_ = stringUtf8;
                return stringUtf8;
            }

            public Builder setCountryId(String str) {
                if (str != null) {
                    this.bitField0_ |= 2;
                    this.countryId_ = str;
                    return this;
                }
                throw new NullPointerException();
            }

            public Builder clearCountryId() {
                this.bitField0_ &= -3;
                this.countryId_ = UserPhoneInfo.getDefaultInstance().getCountryId();
                return this;
            }

            /* access modifiers changed from: 0000 */
            public void setCountryId(ByteString byteString) {
                this.bitField0_ |= 2;
                this.countryId_ = byteString;
            }

            public boolean hasCountryCode() {
                return (this.bitField0_ & 4) == 4;
            }

            public String getCountryCode() {
                Object obj = this.countryCode_;
                if (obj instanceof String) {
                    return (String) obj;
                }
                String stringUtf8 = ((ByteString) obj).toStringUtf8();
                this.countryCode_ = stringUtf8;
                return stringUtf8;
            }

            public Builder setCountryCode(String str) {
                if (str != null) {
                    this.bitField0_ |= 4;
                    this.countryCode_ = str;
                    return this;
                }
                throw new NullPointerException();
            }

            public Builder clearCountryCode() {
                this.bitField0_ &= -5;
                this.countryCode_ = UserPhoneInfo.getDefaultInstance().getCountryCode();
                return this;
            }

            /* access modifiers changed from: 0000 */
            public void setCountryCode(ByteString byteString) {
                this.bitField0_ |= 4;
                this.countryCode_ = byteString;
            }
        }

        private UserPhoneInfo(Builder builder) {
            super(builder);
            this.memoizedIsInitialized = -1;
            this.memoizedSerializedSize = -1;
        }

        private UserPhoneInfo(boolean z) {
            this.memoizedIsInitialized = -1;
            this.memoizedSerializedSize = -1;
        }

        public static UserPhoneInfo getDefaultInstance() {
            return defaultInstance;
        }

        public UserPhoneInfo getDefaultInstanceForType() {
            return defaultInstance;
        }

        public boolean hasPhoneNumber() {
            return (this.bitField0_ & 1) == 1;
        }

        public String getPhoneNumber() {
            Object obj = this.phoneNumber_;
            if (obj instanceof String) {
                return (String) obj;
            }
            ByteString byteString = (ByteString) obj;
            String stringUtf8 = byteString.toStringUtf8();
            if (Internal.isValidUtf8(byteString)) {
                this.phoneNumber_ = stringUtf8;
            }
            return stringUtf8;
        }

        private ByteString getPhoneNumberBytes() {
            Object obj = this.phoneNumber_;
            if (!(obj instanceof String)) {
                return (ByteString) obj;
            }
            ByteString copyFromUtf8 = ByteString.copyFromUtf8((String) obj);
            this.phoneNumber_ = copyFromUtf8;
            return copyFromUtf8;
        }

        public boolean hasCountryId() {
            return (this.bitField0_ & 2) == 2;
        }

        public String getCountryId() {
            Object obj = this.countryId_;
            if (obj instanceof String) {
                return (String) obj;
            }
            ByteString byteString = (ByteString) obj;
            String stringUtf8 = byteString.toStringUtf8();
            if (Internal.isValidUtf8(byteString)) {
                this.countryId_ = stringUtf8;
            }
            return stringUtf8;
        }

        private ByteString getCountryIdBytes() {
            Object obj = this.countryId_;
            if (!(obj instanceof String)) {
                return (ByteString) obj;
            }
            ByteString copyFromUtf8 = ByteString.copyFromUtf8((String) obj);
            this.countryId_ = copyFromUtf8;
            return copyFromUtf8;
        }

        public boolean hasCountryCode() {
            return (this.bitField0_ & 4) == 4;
        }

        public String getCountryCode() {
            Object obj = this.countryCode_;
            if (obj instanceof String) {
                return (String) obj;
            }
            ByteString byteString = (ByteString) obj;
            String stringUtf8 = byteString.toStringUtf8();
            if (Internal.isValidUtf8(byteString)) {
                this.countryCode_ = stringUtf8;
            }
            return stringUtf8;
        }

        private ByteString getCountryCodeBytes() {
            Object obj = this.countryCode_;
            if (!(obj instanceof String)) {
                return (ByteString) obj;
            }
            ByteString copyFromUtf8 = ByteString.copyFromUtf8((String) obj);
            this.countryCode_ = copyFromUtf8;
            return copyFromUtf8;
        }

        private void initFields() {
            this.phoneNumber_ = "";
            this.countryId_ = "";
            this.countryCode_ = "";
        }

        public final boolean isInitialized() {
            byte b = this.memoizedIsInitialized;
            boolean z = true;
            if (b != -1) {
                if (b != 1) {
                    z = false;
                }
                return z;
            }
            this.memoizedIsInitialized = 1;
            return true;
        }

        public void writeTo(CodedOutputStream codedOutputStream) throws IOException {
            getSerializedSize();
            if ((this.bitField0_ & 1) == 1) {
                codedOutputStream.writeBytes(1, getPhoneNumberBytes());
            }
            if ((this.bitField0_ & 2) == 2) {
                codedOutputStream.writeBytes(2, getCountryIdBytes());
            }
            if ((this.bitField0_ & 4) == 4) {
                codedOutputStream.writeBytes(3, getCountryCodeBytes());
            }
        }

        public int getSerializedSize() {
            int i = this.memoizedSerializedSize;
            if (i != -1) {
                return i;
            }
            int i2 = 0;
            if ((this.bitField0_ & 1) == 1) {
                i2 = 0 + CodedOutputStream.computeBytesSize(1, getPhoneNumberBytes());
            }
            if ((this.bitField0_ & 2) == 2) {
                i2 += CodedOutputStream.computeBytesSize(2, getCountryIdBytes());
            }
            if ((this.bitField0_ & 4) == 4) {
                i2 += CodedOutputStream.computeBytesSize(3, getCountryCodeBytes());
            }
            this.memoizedSerializedSize = i2;
            return i2;
        }

        /* access modifiers changed from: protected */
        public Object writeReplace() throws ObjectStreamException {
            return super.writeReplace();
        }

        public static UserPhoneInfo parseFrom(ByteString byteString) throws InvalidProtocolBufferException {
            return ((Builder) newBuilder().mergeFrom(byteString)).buildParsed();
        }

        public static UserPhoneInfo parseFrom(ByteString byteString, ExtensionRegistryLite extensionRegistryLite) throws InvalidProtocolBufferException {
            return ((Builder) newBuilder().mergeFrom(byteString, extensionRegistryLite)).buildParsed();
        }

        public static UserPhoneInfo parseFrom(byte[] bArr) throws InvalidProtocolBufferException {
            return ((Builder) newBuilder().mergeFrom(bArr)).buildParsed();
        }

        public static UserPhoneInfo parseFrom(byte[] bArr, ExtensionRegistryLite extensionRegistryLite) throws InvalidProtocolBufferException {
            return ((Builder) newBuilder().mergeFrom(bArr, extensionRegistryLite)).buildParsed();
        }

        public static UserPhoneInfo parseFrom(InputStream inputStream) throws IOException {
            return ((Builder) newBuilder().mergeFrom(inputStream)).buildParsed();
        }

        public static UserPhoneInfo parseFrom(InputStream inputStream, ExtensionRegistryLite extensionRegistryLite) throws IOException {
            return ((Builder) newBuilder().mergeFrom(inputStream, extensionRegistryLite)).buildParsed();
        }

        public static UserPhoneInfo parseDelimitedFrom(InputStream inputStream) throws IOException {
            Builder newBuilder = newBuilder();
            if (newBuilder.mergeDelimitedFrom(inputStream)) {
                return newBuilder.buildParsed();
            }
            return null;
        }

        public static UserPhoneInfo parseDelimitedFrom(InputStream inputStream, ExtensionRegistryLite extensionRegistryLite) throws IOException {
            Builder newBuilder = newBuilder();
            if (newBuilder.mergeDelimitedFrom(inputStream, extensionRegistryLite)) {
                return newBuilder.buildParsed();
            }
            return null;
        }

        public static UserPhoneInfo parseFrom(CodedInputStream codedInputStream) throws IOException {
            return ((Builder) newBuilder().mergeFrom(codedInputStream)).buildParsed();
        }

        public static UserPhoneInfo parseFrom(CodedInputStream codedInputStream, ExtensionRegistryLite extensionRegistryLite) throws IOException {
            return newBuilder().mergeFrom(codedInputStream, extensionRegistryLite).buildParsed();
        }

        public static Builder newBuilder() {
            return Builder.create();
        }

        public Builder newBuilderForType() {
            return newBuilder();
        }

        public static Builder newBuilder(UserPhoneInfo userPhoneInfo) {
            return newBuilder().mergeFrom(userPhoneInfo);
        }

        public Builder toBuilder() {
            return newBuilder(this);
        }

        static {
            defaultInstance.initFields();
        }
    }

    public static final class UserPhoneInfoList extends GeneratedMessageLite implements UserPhoneInfoListOrBuilder {
        public static final int USERPHONEINFOS_FIELD_NUMBER = 1;
        private static final UserPhoneInfoList defaultInstance = new UserPhoneInfoList(true);
        private static final long serialVersionUID = 0;
        private byte memoizedIsInitialized;
        private int memoizedSerializedSize;
        /* access modifiers changed from: private */
        public List<UserPhoneInfo> userPhoneInfos_;

        public static final class Builder extends com.google.protobuf.GeneratedMessageLite.Builder<UserPhoneInfoList, Builder> implements UserPhoneInfoListOrBuilder {
            private int bitField0_;
            private List<UserPhoneInfo> userPhoneInfos_ = Collections.emptyList();

            private void maybeForceBuilderInitialization() {
            }

            public final boolean isInitialized() {
                return true;
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
                this.userPhoneInfos_ = Collections.emptyList();
                this.bitField0_ &= -2;
                return this;
            }

            public Builder clone() {
                return create().mergeFrom(buildPartial());
            }

            public UserPhoneInfoList getDefaultInstanceForType() {
                return UserPhoneInfoList.getDefaultInstance();
            }

            public UserPhoneInfoList build() {
                UserPhoneInfoList buildPartial = buildPartial();
                if (buildPartial.isInitialized()) {
                    return buildPartial;
                }
                throw newUninitializedMessageException(buildPartial);
            }

            /* access modifiers changed from: private */
            public UserPhoneInfoList buildParsed() throws InvalidProtocolBufferException {
                UserPhoneInfoList buildPartial = buildPartial();
                if (buildPartial.isInitialized()) {
                    return buildPartial;
                }
                throw newUninitializedMessageException(buildPartial).asInvalidProtocolBufferException();
            }

            public UserPhoneInfoList buildPartial() {
                UserPhoneInfoList userPhoneInfoList = new UserPhoneInfoList(this);
                if ((this.bitField0_ & 1) == 1) {
                    this.userPhoneInfos_ = Collections.unmodifiableList(this.userPhoneInfos_);
                    this.bitField0_ &= -2;
                }
                userPhoneInfoList.userPhoneInfos_ = this.userPhoneInfos_;
                return userPhoneInfoList;
            }

            public Builder mergeFrom(UserPhoneInfoList userPhoneInfoList) {
                if (userPhoneInfoList != UserPhoneInfoList.getDefaultInstance() && !userPhoneInfoList.userPhoneInfos_.isEmpty()) {
                    if (this.userPhoneInfos_.isEmpty()) {
                        this.userPhoneInfos_ = userPhoneInfoList.userPhoneInfos_;
                        this.bitField0_ &= -2;
                    } else {
                        ensureUserPhoneInfosIsMutable();
                        this.userPhoneInfos_.addAll(userPhoneInfoList.userPhoneInfos_);
                    }
                }
                return this;
            }

            public Builder mergeFrom(CodedInputStream codedInputStream, ExtensionRegistryLite extensionRegistryLite) throws IOException {
                while (true) {
                    int readTag = codedInputStream.readTag();
                    if (readTag == 0) {
                        return this;
                    }
                    if (readTag == 10) {
                        Builder newBuilder = UserPhoneInfo.newBuilder();
                        codedInputStream.readMessage(newBuilder, extensionRegistryLite);
                        addUserPhoneInfos(newBuilder.buildPartial());
                    } else if (!parseUnknownField(codedInputStream, extensionRegistryLite, readTag)) {
                        return this;
                    }
                }
            }

            private void ensureUserPhoneInfosIsMutable() {
                if ((this.bitField0_ & 1) != 1) {
                    this.userPhoneInfos_ = new ArrayList(this.userPhoneInfos_);
                    this.bitField0_ |= 1;
                }
            }

            public List<UserPhoneInfo> getUserPhoneInfosList() {
                return Collections.unmodifiableList(this.userPhoneInfos_);
            }

            public int getUserPhoneInfosCount() {
                return this.userPhoneInfos_.size();
            }

            public UserPhoneInfo getUserPhoneInfos(int i) {
                return (UserPhoneInfo) this.userPhoneInfos_.get(i);
            }

            public Builder setUserPhoneInfos(int i, UserPhoneInfo userPhoneInfo) {
                if (userPhoneInfo != null) {
                    ensureUserPhoneInfosIsMutable();
                    this.userPhoneInfos_.set(i, userPhoneInfo);
                    return this;
                }
                throw new NullPointerException();
            }

            public Builder setUserPhoneInfos(int i, Builder builder) {
                ensureUserPhoneInfosIsMutable();
                this.userPhoneInfos_.set(i, builder.build());
                return this;
            }

            public Builder addUserPhoneInfos(UserPhoneInfo userPhoneInfo) {
                if (userPhoneInfo != null) {
                    ensureUserPhoneInfosIsMutable();
                    this.userPhoneInfos_.add(userPhoneInfo);
                    return this;
                }
                throw new NullPointerException();
            }

            public Builder addUserPhoneInfos(int i, UserPhoneInfo userPhoneInfo) {
                if (userPhoneInfo != null) {
                    ensureUserPhoneInfosIsMutable();
                    this.userPhoneInfos_.add(i, userPhoneInfo);
                    return this;
                }
                throw new NullPointerException();
            }

            public Builder addUserPhoneInfos(Builder builder) {
                ensureUserPhoneInfosIsMutable();
                this.userPhoneInfos_.add(builder.build());
                return this;
            }

            public Builder addUserPhoneInfos(int i, Builder builder) {
                ensureUserPhoneInfosIsMutable();
                this.userPhoneInfos_.add(i, builder.build());
                return this;
            }

            public Builder addAllUserPhoneInfos(Iterable<? extends UserPhoneInfo> iterable) {
                ensureUserPhoneInfosIsMutable();
                com.google.protobuf.GeneratedMessageLite.Builder.addAll(iterable, this.userPhoneInfos_);
                return this;
            }

            public Builder clearUserPhoneInfos() {
                this.userPhoneInfos_ = Collections.emptyList();
                this.bitField0_ &= -2;
                return this;
            }

            public Builder removeUserPhoneInfos(int i) {
                ensureUserPhoneInfosIsMutable();
                this.userPhoneInfos_.remove(i);
                return this;
            }
        }

        private UserPhoneInfoList(Builder builder) {
            super(builder);
            this.memoizedIsInitialized = -1;
            this.memoizedSerializedSize = -1;
        }

        private UserPhoneInfoList(boolean z) {
            this.memoizedIsInitialized = -1;
            this.memoizedSerializedSize = -1;
        }

        public static UserPhoneInfoList getDefaultInstance() {
            return defaultInstance;
        }

        public UserPhoneInfoList getDefaultInstanceForType() {
            return defaultInstance;
        }

        public List<UserPhoneInfo> getUserPhoneInfosList() {
            return this.userPhoneInfos_;
        }

        public List<? extends UserPhoneInfoOrBuilder> getUserPhoneInfosOrBuilderList() {
            return this.userPhoneInfos_;
        }

        public int getUserPhoneInfosCount() {
            return this.userPhoneInfos_.size();
        }

        public UserPhoneInfo getUserPhoneInfos(int i) {
            return (UserPhoneInfo) this.userPhoneInfos_.get(i);
        }

        public UserPhoneInfoOrBuilder getUserPhoneInfosOrBuilder(int i) {
            return (UserPhoneInfoOrBuilder) this.userPhoneInfos_.get(i);
        }

        private void initFields() {
            this.userPhoneInfos_ = Collections.emptyList();
        }

        public final boolean isInitialized() {
            byte b = this.memoizedIsInitialized;
            boolean z = true;
            if (b != -1) {
                if (b != 1) {
                    z = false;
                }
                return z;
            }
            this.memoizedIsInitialized = 1;
            return true;
        }

        public void writeTo(CodedOutputStream codedOutputStream) throws IOException {
            getSerializedSize();
            for (int i = 0; i < this.userPhoneInfos_.size(); i++) {
                codedOutputStream.writeMessage(1, (MessageLite) this.userPhoneInfos_.get(i));
            }
        }

        public int getSerializedSize() {
            int i = this.memoizedSerializedSize;
            if (i != -1) {
                return i;
            }
            int i2 = 0;
            for (int i3 = 0; i3 < this.userPhoneInfos_.size(); i3++) {
                i2 += CodedOutputStream.computeMessageSize(1, (MessageLite) this.userPhoneInfos_.get(i3));
            }
            this.memoizedSerializedSize = i2;
            return i2;
        }

        /* access modifiers changed from: protected */
        public Object writeReplace() throws ObjectStreamException {
            return super.writeReplace();
        }

        public static UserPhoneInfoList parseFrom(ByteString byteString) throws InvalidProtocolBufferException {
            return ((Builder) newBuilder().mergeFrom(byteString)).buildParsed();
        }

        public static UserPhoneInfoList parseFrom(ByteString byteString, ExtensionRegistryLite extensionRegistryLite) throws InvalidProtocolBufferException {
            return ((Builder) newBuilder().mergeFrom(byteString, extensionRegistryLite)).buildParsed();
        }

        public static UserPhoneInfoList parseFrom(byte[] bArr) throws InvalidProtocolBufferException {
            return ((Builder) newBuilder().mergeFrom(bArr)).buildParsed();
        }

        public static UserPhoneInfoList parseFrom(byte[] bArr, ExtensionRegistryLite extensionRegistryLite) throws InvalidProtocolBufferException {
            return ((Builder) newBuilder().mergeFrom(bArr, extensionRegistryLite)).buildParsed();
        }

        public static UserPhoneInfoList parseFrom(InputStream inputStream) throws IOException {
            return ((Builder) newBuilder().mergeFrom(inputStream)).buildParsed();
        }

        public static UserPhoneInfoList parseFrom(InputStream inputStream, ExtensionRegistryLite extensionRegistryLite) throws IOException {
            return ((Builder) newBuilder().mergeFrom(inputStream, extensionRegistryLite)).buildParsed();
        }

        public static UserPhoneInfoList parseDelimitedFrom(InputStream inputStream) throws IOException {
            Builder newBuilder = newBuilder();
            if (newBuilder.mergeDelimitedFrom(inputStream)) {
                return newBuilder.buildParsed();
            }
            return null;
        }

        public static UserPhoneInfoList parseDelimitedFrom(InputStream inputStream, ExtensionRegistryLite extensionRegistryLite) throws IOException {
            Builder newBuilder = newBuilder();
            if (newBuilder.mergeDelimitedFrom(inputStream, extensionRegistryLite)) {
                return newBuilder.buildParsed();
            }
            return null;
        }

        public static UserPhoneInfoList parseFrom(CodedInputStream codedInputStream) throws IOException {
            return ((Builder) newBuilder().mergeFrom(codedInputStream)).buildParsed();
        }

        public static UserPhoneInfoList parseFrom(CodedInputStream codedInputStream, ExtensionRegistryLite extensionRegistryLite) throws IOException {
            return newBuilder().mergeFrom(codedInputStream, extensionRegistryLite).buildParsed();
        }

        public static Builder newBuilder() {
            return Builder.create();
        }

        public Builder newBuilderForType() {
            return newBuilder();
        }

        public static Builder newBuilder(UserPhoneInfoList userPhoneInfoList) {
            return newBuilder().mergeFrom(userPhoneInfoList);
        }

        public Builder toBuilder() {
            return newBuilder(this);
        }

        static {
            defaultInstance.initFields();
        }
    }

    public interface UserPhoneInfoListOrBuilder extends MessageLiteOrBuilder {
        UserPhoneInfo getUserPhoneInfos(int i);

        int getUserPhoneInfosCount();

        List<UserPhoneInfo> getUserPhoneInfosList();
    }

    public interface UserPhoneInfoOrBuilder extends MessageLiteOrBuilder {
        String getCountryCode();

        String getCountryId();

        String getPhoneNumber();

        boolean hasCountryCode();

        boolean hasCountryId();

        boolean hasPhoneNumber();
    }

    public static void registerAllExtensions(ExtensionRegistryLite extensionRegistryLite) {
    }

    private MeetingInfoProtos() {
    }
}
