package com.google.protobuf;

import com.google.protobuf.FieldSet.FieldDescriptorLite;
import com.google.protobuf.Internal.EnumLite;
import com.google.protobuf.Internal.EnumLiteMap;
import com.google.protobuf.WireFormat.FieldType;
import com.google.protobuf.WireFormat.JavaType;
import java.io.IOException;
import java.io.ObjectStreamException;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

public abstract class GeneratedMessageLite extends AbstractMessageLite implements Serializable {
    private static final long serialVersionUID = 1;

    public static abstract class Builder<MessageType extends GeneratedMessageLite, BuilderType extends Builder> extends com.google.protobuf.AbstractMessageLite.Builder<BuilderType> {
        public BuilderType clear() {
            return this;
        }

        public abstract MessageType getDefaultInstanceForType();

        public abstract BuilderType mergeFrom(MessageType messagetype);

        protected Builder() {
        }

        public BuilderType clone() {
            throw new UnsupportedOperationException("This is supposed to be overridden by subclasses.");
        }

        /* access modifiers changed from: protected */
        public boolean parseUnknownField(CodedInputStream codedInputStream, ExtensionRegistryLite extensionRegistryLite, int i) throws IOException {
            return codedInputStream.skipField(i);
        }
    }

    public static abstract class ExtendableBuilder<MessageType extends ExtendableMessage<MessageType>, BuilderType extends ExtendableBuilder<MessageType, BuilderType>> extends Builder<MessageType, BuilderType> implements ExtendableMessageOrBuilder<MessageType> {
        private FieldSet<ExtensionDescriptor> extensions = FieldSet.emptySet();
        private boolean extensionsIsMutable;

        protected ExtendableBuilder() {
        }

        public BuilderType clear() {
            this.extensions.clear();
            this.extensionsIsMutable = false;
            return (ExtendableBuilder) super.clear();
        }

        private void ensureExtensionsIsMutable() {
            if (!this.extensionsIsMutable) {
                this.extensions = this.extensions.clone();
                this.extensionsIsMutable = true;
            }
        }

        /* access modifiers changed from: private */
        public FieldSet<ExtensionDescriptor> buildExtensions() {
            this.extensions.makeImmutable();
            this.extensionsIsMutable = false;
            return this.extensions;
        }

        private void verifyExtensionContainingType(GeneratedExtension<MessageType, ?> generatedExtension) {
            if (generatedExtension.getContainingTypeDefaultInstance() != getDefaultInstanceForType()) {
                throw new IllegalArgumentException("This extension is for a different message type.  Please make sure that you are not suppressing any generics type warnings.");
            }
        }

        public final <Type> boolean hasExtension(GeneratedExtension<MessageType, Type> generatedExtension) {
            verifyExtensionContainingType(generatedExtension);
            return this.extensions.hasField(generatedExtension.descriptor);
        }

        public final <Type> int getExtensionCount(GeneratedExtension<MessageType, List<Type>> generatedExtension) {
            verifyExtensionContainingType(generatedExtension);
            return this.extensions.getRepeatedFieldCount(generatedExtension.descriptor);
        }

        public final <Type> Type getExtension(GeneratedExtension<MessageType, Type> generatedExtension) {
            verifyExtensionContainingType(generatedExtension);
            Type field = this.extensions.getField(generatedExtension.descriptor);
            return field == null ? generatedExtension.defaultValue : field;
        }

        public final <Type> Type getExtension(GeneratedExtension<MessageType, List<Type>> generatedExtension, int i) {
            verifyExtensionContainingType(generatedExtension);
            return this.extensions.getRepeatedField(generatedExtension.descriptor, i);
        }

        public BuilderType clone() {
            throw new UnsupportedOperationException("This is supposed to be overridden by subclasses.");
        }

        public final <Type> BuilderType setExtension(GeneratedExtension<MessageType, Type> generatedExtension, Type type) {
            verifyExtensionContainingType(generatedExtension);
            ensureExtensionsIsMutable();
            this.extensions.setField(generatedExtension.descriptor, type);
            return this;
        }

        public final <Type> BuilderType setExtension(GeneratedExtension<MessageType, List<Type>> generatedExtension, int i, Type type) {
            verifyExtensionContainingType(generatedExtension);
            ensureExtensionsIsMutable();
            this.extensions.setRepeatedField(generatedExtension.descriptor, i, type);
            return this;
        }

        public final <Type> BuilderType addExtension(GeneratedExtension<MessageType, List<Type>> generatedExtension, Type type) {
            verifyExtensionContainingType(generatedExtension);
            ensureExtensionsIsMutable();
            this.extensions.addRepeatedField(generatedExtension.descriptor, type);
            return this;
        }

        public final <Type> BuilderType clearExtension(GeneratedExtension<MessageType, ?> generatedExtension) {
            verifyExtensionContainingType(generatedExtension);
            ensureExtensionsIsMutable();
            this.extensions.clearField(generatedExtension.descriptor);
            return this;
        }

        /* access modifiers changed from: protected */
        public boolean extensionsAreInitialized() {
            return this.extensions.isInitialized();
        }

        /* access modifiers changed from: protected */
        public boolean parseUnknownField(CodedInputStream codedInputStream, ExtensionRegistryLite extensionRegistryLite, int i) throws IOException {
            boolean z;
            Object obj;
            int tagWireType = WireFormat.getTagWireType(i);
            GeneratedExtension findLiteExtensionByNumber = extensionRegistryLite.findLiteExtensionByNumber(getDefaultInstanceForType(), WireFormat.getTagFieldNumber(i));
            boolean z2 = false;
            if (findLiteExtensionByNumber == null) {
                z = false;
                z2 = true;
            } else if (tagWireType == FieldSet.getWireFormatForFieldType(findLiteExtensionByNumber.descriptor.getLiteType(), false)) {
                z = false;
            } else if (!findLiteExtensionByNumber.descriptor.isRepeated || !findLiteExtensionByNumber.descriptor.type.isPackable() || tagWireType != FieldSet.getWireFormatForFieldType(findLiteExtensionByNumber.descriptor.getLiteType(), true)) {
                z = false;
                z2 = true;
            } else {
                z = true;
            }
            if (z2) {
                return codedInputStream.skipField(i);
            }
            if (z) {
                int pushLimit = codedInputStream.pushLimit(codedInputStream.readRawVarint32());
                if (findLiteExtensionByNumber.descriptor.getLiteType() == FieldType.ENUM) {
                    while (codedInputStream.getBytesUntilLimit() > 0) {
                        EnumLite findValueByNumber = findLiteExtensionByNumber.descriptor.getEnumType().findValueByNumber(codedInputStream.readEnum());
                        if (findValueByNumber == null) {
                            return true;
                        }
                        ensureExtensionsIsMutable();
                        this.extensions.addRepeatedField(findLiteExtensionByNumber.descriptor, findValueByNumber);
                    }
                } else {
                    while (codedInputStream.getBytesUntilLimit() > 0) {
                        Object readPrimitiveField = FieldSet.readPrimitiveField(codedInputStream, findLiteExtensionByNumber.descriptor.getLiteType());
                        ensureExtensionsIsMutable();
                        this.extensions.addRepeatedField(findLiteExtensionByNumber.descriptor, readPrimitiveField);
                    }
                }
                codedInputStream.popLimit(pushLimit);
            } else {
                switch (findLiteExtensionByNumber.descriptor.getLiteJavaType()) {
                    case MESSAGE:
                        com.google.protobuf.MessageLite.Builder builder = null;
                        if (!findLiteExtensionByNumber.descriptor.isRepeated()) {
                            MessageLite messageLite = (MessageLite) this.extensions.getField(findLiteExtensionByNumber.descriptor);
                            if (messageLite != null) {
                                builder = messageLite.toBuilder();
                            }
                        }
                        if (builder == null) {
                            builder = findLiteExtensionByNumber.messageDefaultInstance.newBuilderForType();
                        }
                        if (findLiteExtensionByNumber.descriptor.getLiteType() == FieldType.GROUP) {
                            codedInputStream.readGroup(findLiteExtensionByNumber.getNumber(), builder, extensionRegistryLite);
                        } else {
                            codedInputStream.readMessage(builder, extensionRegistryLite);
                        }
                        obj = builder.build();
                        break;
                    case ENUM:
                        obj = findLiteExtensionByNumber.descriptor.getEnumType().findValueByNumber(codedInputStream.readEnum());
                        if (obj == null) {
                            return true;
                        }
                        break;
                    default:
                        obj = FieldSet.readPrimitiveField(codedInputStream, findLiteExtensionByNumber.descriptor.getLiteType());
                        break;
                }
                if (findLiteExtensionByNumber.descriptor.isRepeated()) {
                    ensureExtensionsIsMutable();
                    this.extensions.addRepeatedField(findLiteExtensionByNumber.descriptor, obj);
                } else {
                    ensureExtensionsIsMutable();
                    this.extensions.setField(findLiteExtensionByNumber.descriptor, obj);
                }
            }
            return true;
        }

        /* access modifiers changed from: protected */
        public final void mergeExtensionFields(MessageType messagetype) {
            ensureExtensionsIsMutable();
            this.extensions.mergeFrom(messagetype.extensions);
        }
    }

    public static abstract class ExtendableMessage<MessageType extends ExtendableMessage<MessageType>> extends GeneratedMessageLite implements ExtendableMessageOrBuilder<MessageType> {
        /* access modifiers changed from: private */
        public final FieldSet<ExtensionDescriptor> extensions;

        protected class ExtensionWriter {
            private final Iterator<Entry<ExtensionDescriptor, Object>> iter;
            private final boolean messageSetWireFormat;
            private Entry<ExtensionDescriptor, Object> next;

            private ExtensionWriter(boolean z) {
                this.iter = ExtendableMessage.this.extensions.iterator();
                if (this.iter.hasNext()) {
                    this.next = (Entry) this.iter.next();
                }
                this.messageSetWireFormat = z;
            }

            public void writeUntil(int i, CodedOutputStream codedOutputStream) throws IOException {
                while (true) {
                    Entry<ExtensionDescriptor, Object> entry = this.next;
                    if (entry != null && ((ExtensionDescriptor) entry.getKey()).getNumber() < i) {
                        ExtensionDescriptor extensionDescriptor = (ExtensionDescriptor) this.next.getKey();
                        if (!this.messageSetWireFormat || extensionDescriptor.getLiteJavaType() != JavaType.MESSAGE || extensionDescriptor.isRepeated()) {
                            FieldSet.writeField(extensionDescriptor, this.next.getValue(), codedOutputStream);
                        } else {
                            codedOutputStream.writeMessageSetExtension(extensionDescriptor.getNumber(), (MessageLite) this.next.getValue());
                        }
                        if (this.iter.hasNext()) {
                            this.next = (Entry) this.iter.next();
                        } else {
                            this.next = null;
                        }
                    } else {
                        return;
                    }
                }
            }
        }

        protected ExtendableMessage() {
            this.extensions = FieldSet.newFieldSet();
        }

        protected ExtendableMessage(ExtendableBuilder<MessageType, ?> extendableBuilder) {
            this.extensions = extendableBuilder.buildExtensions();
        }

        private void verifyExtensionContainingType(GeneratedExtension<MessageType, ?> generatedExtension) {
            if (generatedExtension.getContainingTypeDefaultInstance() != getDefaultInstanceForType()) {
                throw new IllegalArgumentException("This extension is for a different message type.  Please make sure that you are not suppressing any generics type warnings.");
            }
        }

        public final <Type> boolean hasExtension(GeneratedExtension<MessageType, Type> generatedExtension) {
            verifyExtensionContainingType(generatedExtension);
            return this.extensions.hasField(generatedExtension.descriptor);
        }

        public final <Type> int getExtensionCount(GeneratedExtension<MessageType, List<Type>> generatedExtension) {
            verifyExtensionContainingType(generatedExtension);
            return this.extensions.getRepeatedFieldCount(generatedExtension.descriptor);
        }

        public final <Type> Type getExtension(GeneratedExtension<MessageType, Type> generatedExtension) {
            verifyExtensionContainingType(generatedExtension);
            Type field = this.extensions.getField(generatedExtension.descriptor);
            return field == null ? generatedExtension.defaultValue : field;
        }

        public final <Type> Type getExtension(GeneratedExtension<MessageType, List<Type>> generatedExtension, int i) {
            verifyExtensionContainingType(generatedExtension);
            return this.extensions.getRepeatedField(generatedExtension.descriptor, i);
        }

        /* access modifiers changed from: protected */
        public boolean extensionsAreInitialized() {
            return this.extensions.isInitialized();
        }

        /* access modifiers changed from: protected */
        public ExtensionWriter newExtensionWriter() {
            return new ExtensionWriter<>(false);
        }

        /* access modifiers changed from: protected */
        public ExtensionWriter newMessageSetExtensionWriter() {
            return new ExtensionWriter<>(true);
        }

        /* access modifiers changed from: protected */
        public int extensionsSerializedSize() {
            return this.extensions.getSerializedSize();
        }

        /* access modifiers changed from: protected */
        public int extensionsSerializedSizeAsMessageSet() {
            return this.extensions.getMessageSetSerializedSize();
        }
    }

    public interface ExtendableMessageOrBuilder<MessageType extends ExtendableMessage> extends MessageLiteOrBuilder {
        <Type> Type getExtension(GeneratedExtension<MessageType, Type> generatedExtension);

        <Type> Type getExtension(GeneratedExtension<MessageType, List<Type>> generatedExtension, int i);

        <Type> int getExtensionCount(GeneratedExtension<MessageType, List<Type>> generatedExtension);

        <Type> boolean hasExtension(GeneratedExtension<MessageType, Type> generatedExtension);
    }

    private static final class ExtensionDescriptor implements FieldDescriptorLite<ExtensionDescriptor> {
        private final EnumLiteMap<?> enumTypeMap;
        private final boolean isPacked;
        /* access modifiers changed from: private */
        public final boolean isRepeated;
        private final int number;
        /* access modifiers changed from: private */
        public final FieldType type;

        private ExtensionDescriptor(EnumLiteMap<?> enumLiteMap, int i, FieldType fieldType, boolean z, boolean z2) {
            this.enumTypeMap = enumLiteMap;
            this.number = i;
            this.type = fieldType;
            this.isRepeated = z;
            this.isPacked = z2;
        }

        public int getNumber() {
            return this.number;
        }

        public FieldType getLiteType() {
            return this.type;
        }

        public JavaType getLiteJavaType() {
            return this.type.getJavaType();
        }

        public boolean isRepeated() {
            return this.isRepeated;
        }

        public boolean isPacked() {
            return this.isPacked;
        }

        public EnumLiteMap<?> getEnumType() {
            return this.enumTypeMap;
        }

        public com.google.protobuf.MessageLite.Builder internalMergeFrom(com.google.protobuf.MessageLite.Builder builder, MessageLite messageLite) {
            return ((Builder) builder).mergeFrom((GeneratedMessageLite) messageLite);
        }

        public int compareTo(ExtensionDescriptor extensionDescriptor) {
            return this.number - extensionDescriptor.number;
        }
    }

    public static final class GeneratedExtension<ContainingType extends MessageLite, Type> {
        private final ContainingType containingTypeDefaultInstance;
        /* access modifiers changed from: private */
        public final Type defaultValue;
        /* access modifiers changed from: private */
        public final ExtensionDescriptor descriptor;
        /* access modifiers changed from: private */
        public final MessageLite messageDefaultInstance;

        private GeneratedExtension(ContainingType containingtype, Type type, MessageLite messageLite, ExtensionDescriptor extensionDescriptor) {
            if (containingtype == null) {
                throw new IllegalArgumentException("Null containingTypeDefaultInstance");
            } else if (extensionDescriptor.getLiteType() == FieldType.MESSAGE && messageLite == null) {
                throw new IllegalArgumentException("Null messageDefaultInstance");
            } else {
                this.containingTypeDefaultInstance = containingtype;
                this.defaultValue = type;
                this.messageDefaultInstance = messageLite;
                this.descriptor = extensionDescriptor;
            }
        }

        public ContainingType getContainingTypeDefaultInstance() {
            return this.containingTypeDefaultInstance;
        }

        public int getNumber() {
            return this.descriptor.getNumber();
        }

        public MessageLite getMessageDefaultInstance() {
            return this.messageDefaultInstance;
        }
    }

    static final class SerializedForm implements Serializable {
        private static final long serialVersionUID = 0;
        private byte[] asBytes;
        private String messageClassName;

        SerializedForm(MessageLite messageLite) {
            this.messageClassName = messageLite.getClass().getName();
            this.asBytes = messageLite.toByteArray();
        }

        /* access modifiers changed from: protected */
        public Object readResolve() throws ObjectStreamException {
            try {
                com.google.protobuf.MessageLite.Builder builder = (com.google.protobuf.MessageLite.Builder) Class.forName(this.messageClassName).getMethod("newBuilder", new Class[0]).invoke(null, new Object[0]);
                builder.mergeFrom(this.asBytes);
                return builder.buildPartial();
            } catch (ClassNotFoundException e) {
                throw new RuntimeException("Unable to find proto buffer class", e);
            } catch (NoSuchMethodException e2) {
                throw new RuntimeException("Unable to find newBuilder method", e2);
            } catch (IllegalAccessException e3) {
                throw new RuntimeException("Unable to call newBuilder method", e3);
            } catch (InvocationTargetException e4) {
                throw new RuntimeException("Error calling newBuilder", e4.getCause());
            } catch (InvalidProtocolBufferException e5) {
                throw new RuntimeException("Unable to understand proto buffer", e5);
            }
        }
    }

    protected GeneratedMessageLite() {
    }

    protected GeneratedMessageLite(Builder builder) {
    }

    public static <ContainingType extends MessageLite, Type> GeneratedExtension<ContainingType, Type> newSingularGeneratedExtension(ContainingType containingtype, Type type, MessageLite messageLite, EnumLiteMap<?> enumLiteMap, int i, FieldType fieldType) {
        ExtensionDescriptor extensionDescriptor = new ExtensionDescriptor(enumLiteMap, i, fieldType, false, false);
        GeneratedExtension generatedExtension = new GeneratedExtension(containingtype, type, messageLite, extensionDescriptor);
        return generatedExtension;
    }

    public static <ContainingType extends MessageLite, Type> GeneratedExtension<ContainingType, Type> newRepeatedGeneratedExtension(ContainingType containingtype, MessageLite messageLite, EnumLiteMap<?> enumLiteMap, int i, FieldType fieldType, boolean z) {
        List emptyList = Collections.emptyList();
        ExtensionDescriptor extensionDescriptor = new ExtensionDescriptor(enumLiteMap, i, fieldType, true, z);
        GeneratedExtension generatedExtension = new GeneratedExtension(containingtype, emptyList, messageLite, extensionDescriptor);
        return generatedExtension;
    }

    /* access modifiers changed from: protected */
    public Object writeReplace() throws ObjectStreamException {
        return new SerializedForm(this);
    }
}
