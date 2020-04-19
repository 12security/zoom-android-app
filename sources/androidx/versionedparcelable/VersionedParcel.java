package androidx.versionedparcelable;

import android.os.BadParcelableException;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.NetworkOnMainThreadException;
import android.os.Parcelable;
import android.util.Size;
import android.util.SizeF;
import android.util.SparseBooleanArray;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.annotation.RestrictTo;
import androidx.annotation.RestrictTo.Scope;
import androidx.collection.ArraySet;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamClass;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@RestrictTo({Scope.LIBRARY_GROUP})
public abstract class VersionedParcel {
    private static final int EX_BAD_PARCELABLE = -2;
    private static final int EX_ILLEGAL_ARGUMENT = -3;
    private static final int EX_ILLEGAL_STATE = -5;
    private static final int EX_NETWORK_MAIN_THREAD = -6;
    private static final int EX_NULL_POINTER = -4;
    private static final int EX_PARCELABLE = -9;
    private static final int EX_SECURITY = -1;
    private static final int EX_UNSUPPORTED_OPERATION = -7;
    private static final String TAG = "VersionedParcel";
    private static final int TYPE_BINDER = 5;
    private static final int TYPE_PARCELABLE = 2;
    private static final int TYPE_SERIALIZABLE = 3;
    private static final int TYPE_STRING = 4;
    private static final int TYPE_VERSIONED_PARCELABLE = 1;

    public static class ParcelException extends RuntimeException {
        public ParcelException(Throwable th) {
            super(th);
        }
    }

    /* access modifiers changed from: protected */
    public abstract void closeField();

    /* access modifiers changed from: protected */
    public abstract VersionedParcel createSubParcel();

    public boolean isStream() {
        return false;
    }

    /* access modifiers changed from: protected */
    public abstract boolean readBoolean();

    /* access modifiers changed from: protected */
    public abstract Bundle readBundle();

    /* access modifiers changed from: protected */
    public abstract byte[] readByteArray();

    /* access modifiers changed from: protected */
    public abstract double readDouble();

    /* access modifiers changed from: protected */
    public abstract boolean readField(int i);

    /* access modifiers changed from: protected */
    public abstract float readFloat();

    /* access modifiers changed from: protected */
    public abstract int readInt();

    /* access modifiers changed from: protected */
    public abstract long readLong();

    /* access modifiers changed from: protected */
    public abstract <T extends Parcelable> T readParcelable();

    /* access modifiers changed from: protected */
    public abstract String readString();

    /* access modifiers changed from: protected */
    public abstract IBinder readStrongBinder();

    /* access modifiers changed from: protected */
    public abstract void setOutputField(int i);

    public void setSerializationFlags(boolean z, boolean z2) {
    }

    /* access modifiers changed from: protected */
    public abstract void writeBoolean(boolean z);

    /* access modifiers changed from: protected */
    public abstract void writeBundle(Bundle bundle);

    /* access modifiers changed from: protected */
    public abstract void writeByteArray(byte[] bArr);

    /* access modifiers changed from: protected */
    public abstract void writeByteArray(byte[] bArr, int i, int i2);

    /* access modifiers changed from: protected */
    public abstract void writeDouble(double d);

    /* access modifiers changed from: protected */
    public abstract void writeFloat(float f);

    /* access modifiers changed from: protected */
    public abstract void writeInt(int i);

    /* access modifiers changed from: protected */
    public abstract void writeLong(long j);

    /* access modifiers changed from: protected */
    public abstract void writeParcelable(Parcelable parcelable);

    /* access modifiers changed from: protected */
    public abstract void writeString(String str);

    /* access modifiers changed from: protected */
    public abstract void writeStrongBinder(IBinder iBinder);

    /* access modifiers changed from: protected */
    public abstract void writeStrongInterface(IInterface iInterface);

    public void writeStrongInterface(IInterface iInterface, int i) {
        setOutputField(i);
        writeStrongInterface(iInterface);
    }

    public void writeBundle(Bundle bundle, int i) {
        setOutputField(i);
        writeBundle(bundle);
    }

    public void writeBoolean(boolean z, int i) {
        setOutputField(i);
        writeBoolean(z);
    }

    public void writeByteArray(byte[] bArr, int i) {
        setOutputField(i);
        writeByteArray(bArr);
    }

    public void writeByteArray(byte[] bArr, int i, int i2, int i3) {
        setOutputField(i3);
        writeByteArray(bArr, i, i2);
    }

    public void writeInt(int i, int i2) {
        setOutputField(i2);
        writeInt(i);
    }

    public void writeLong(long j, int i) {
        setOutputField(i);
        writeLong(j);
    }

    public void writeFloat(float f, int i) {
        setOutputField(i);
        writeFloat(f);
    }

    public void writeDouble(double d, int i) {
        setOutputField(i);
        writeDouble(d);
    }

    public void writeString(String str, int i) {
        setOutputField(i);
        writeString(str);
    }

    public void writeStrongBinder(IBinder iBinder, int i) {
        setOutputField(i);
        writeStrongBinder(iBinder);
    }

    public void writeParcelable(Parcelable parcelable, int i) {
        setOutputField(i);
        writeParcelable(parcelable);
    }

    public boolean readBoolean(boolean z, int i) {
        if (!readField(i)) {
            return z;
        }
        return readBoolean();
    }

    public int readInt(int i, int i2) {
        if (!readField(i2)) {
            return i;
        }
        return readInt();
    }

    public long readLong(long j, int i) {
        if (!readField(i)) {
            return j;
        }
        return readLong();
    }

    public float readFloat(float f, int i) {
        if (!readField(i)) {
            return f;
        }
        return readFloat();
    }

    public double readDouble(double d, int i) {
        if (!readField(i)) {
            return d;
        }
        return readDouble();
    }

    public String readString(String str, int i) {
        if (!readField(i)) {
            return str;
        }
        return readString();
    }

    public IBinder readStrongBinder(IBinder iBinder, int i) {
        if (!readField(i)) {
            return iBinder;
        }
        return readStrongBinder();
    }

    public byte[] readByteArray(byte[] bArr, int i) {
        if (!readField(i)) {
            return bArr;
        }
        return readByteArray();
    }

    public <T extends Parcelable> T readParcelable(T t, int i) {
        if (!readField(i)) {
            return t;
        }
        return readParcelable();
    }

    public Bundle readBundle(Bundle bundle, int i) {
        if (!readField(i)) {
            return bundle;
        }
        return readBundle();
    }

    public void writeByte(byte b, int i) {
        setOutputField(i);
        writeInt(b);
    }

    @RequiresApi(api = 21)
    public void writeSize(Size size, int i) {
        setOutputField(i);
        writeBoolean(size != null);
        if (size != null) {
            writeInt(size.getWidth());
            writeInt(size.getHeight());
        }
    }

    @RequiresApi(api = 21)
    public void writeSizeF(SizeF sizeF, int i) {
        setOutputField(i);
        writeBoolean(sizeF != null);
        if (sizeF != null) {
            writeFloat(sizeF.getWidth());
            writeFloat(sizeF.getHeight());
        }
    }

    public void writeSparseBooleanArray(SparseBooleanArray sparseBooleanArray, int i) {
        setOutputField(i);
        if (sparseBooleanArray == null) {
            writeInt(-1);
            return;
        }
        int size = sparseBooleanArray.size();
        writeInt(size);
        for (int i2 = 0; i2 < size; i2++) {
            writeInt(sparseBooleanArray.keyAt(i2));
            writeBoolean(sparseBooleanArray.valueAt(i2));
        }
    }

    public void writeBooleanArray(boolean[] zArr, int i) {
        setOutputField(i);
        writeBooleanArray(zArr);
    }

    /* access modifiers changed from: protected */
    public void writeBooleanArray(boolean[] zArr) {
        if (zArr != null) {
            int length = zArr.length;
            writeInt(length);
            for (int i = 0; i < length; i++) {
                writeInt(zArr[i] ? 1 : 0);
            }
            return;
        }
        writeInt(-1);
    }

    public boolean[] readBooleanArray(boolean[] zArr, int i) {
        if (!readField(i)) {
            return zArr;
        }
        return readBooleanArray();
    }

    /* access modifiers changed from: protected */
    public boolean[] readBooleanArray() {
        int readInt = readInt();
        if (readInt < 0) {
            return null;
        }
        boolean[] zArr = new boolean[readInt];
        for (int i = 0; i < readInt; i++) {
            zArr[i] = readInt() != 0;
        }
        return zArr;
    }

    public void writeCharArray(char[] cArr, int i) {
        setOutputField(i);
        if (cArr != null) {
            writeInt(r4);
            for (char writeInt : cArr) {
                writeInt(writeInt);
            }
            return;
        }
        writeInt(-1);
    }

    public char[] readCharArray(char[] cArr, int i) {
        if (!readField(i)) {
            return cArr;
        }
        int readInt = readInt();
        if (readInt < 0) {
            return null;
        }
        char[] cArr2 = new char[readInt];
        for (int i2 = 0; i2 < readInt; i2++) {
            cArr2[i2] = (char) readInt();
        }
        return cArr2;
    }

    public void writeIntArray(int[] iArr, int i) {
        setOutputField(i);
        writeIntArray(iArr);
    }

    /* access modifiers changed from: protected */
    public void writeIntArray(int[] iArr) {
        if (iArr != null) {
            writeInt(r0);
            for (int writeInt : iArr) {
                writeInt(writeInt);
            }
            return;
        }
        writeInt(-1);
    }

    public int[] readIntArray(int[] iArr, int i) {
        if (!readField(i)) {
            return iArr;
        }
        return readIntArray();
    }

    /* access modifiers changed from: protected */
    public int[] readIntArray() {
        int readInt = readInt();
        if (readInt < 0) {
            return null;
        }
        int[] iArr = new int[readInt];
        for (int i = 0; i < readInt; i++) {
            iArr[i] = readInt();
        }
        return iArr;
    }

    public void writeLongArray(long[] jArr, int i) {
        setOutputField(i);
        writeLongArray(jArr);
    }

    /* access modifiers changed from: protected */
    public void writeLongArray(long[] jArr) {
        if (jArr != null) {
            writeInt(r0);
            for (long writeLong : jArr) {
                writeLong(writeLong);
            }
            return;
        }
        writeInt(-1);
    }

    public long[] readLongArray(long[] jArr, int i) {
        if (!readField(i)) {
            return jArr;
        }
        return readLongArray();
    }

    /* access modifiers changed from: protected */
    public long[] readLongArray() {
        int readInt = readInt();
        if (readInt < 0) {
            return null;
        }
        long[] jArr = new long[readInt];
        for (int i = 0; i < readInt; i++) {
            jArr[i] = readLong();
        }
        return jArr;
    }

    public void writeFloatArray(float[] fArr, int i) {
        setOutputField(i);
        writeFloatArray(fArr);
    }

    /* access modifiers changed from: protected */
    public void writeFloatArray(float[] fArr) {
        if (fArr != null) {
            writeInt(r0);
            for (float writeFloat : fArr) {
                writeFloat(writeFloat);
            }
            return;
        }
        writeInt(-1);
    }

    public float[] readFloatArray(float[] fArr, int i) {
        if (!readField(i)) {
            return fArr;
        }
        return readFloatArray();
    }

    /* access modifiers changed from: protected */
    public float[] readFloatArray() {
        int readInt = readInt();
        if (readInt < 0) {
            return null;
        }
        float[] fArr = new float[readInt];
        for (int i = 0; i < readInt; i++) {
            fArr[i] = readFloat();
        }
        return fArr;
    }

    public void writeDoubleArray(double[] dArr, int i) {
        setOutputField(i);
        writeDoubleArray(dArr);
    }

    /* access modifiers changed from: protected */
    public void writeDoubleArray(double[] dArr) {
        if (dArr != null) {
            writeInt(r0);
            for (double writeDouble : dArr) {
                writeDouble(writeDouble);
            }
            return;
        }
        writeInt(-1);
    }

    public double[] readDoubleArray(double[] dArr, int i) {
        if (!readField(i)) {
            return dArr;
        }
        return readDoubleArray();
    }

    /* access modifiers changed from: protected */
    public double[] readDoubleArray() {
        int readInt = readInt();
        if (readInt < 0) {
            return null;
        }
        double[] dArr = new double[readInt];
        for (int i = 0; i < readInt; i++) {
            dArr[i] = readDouble();
        }
        return dArr;
    }

    public <T> void writeSet(Set<T> set, int i) {
        writeCollection(set, i);
    }

    public <T> void writeList(List<T> list, int i) {
        writeCollection(list, i);
    }

    private <T> void writeCollection(Collection<T> collection, int i) {
        setOutputField(i);
        if (collection == null) {
            writeInt(-1);
            return;
        }
        int size = collection.size();
        writeInt(size);
        if (size > 0) {
            int type = getType(collection.iterator().next());
            writeInt(type);
            switch (type) {
                case 1:
                    for (T writeVersionedParcelable : collection) {
                        writeVersionedParcelable(writeVersionedParcelable);
                    }
                    break;
                case 2:
                    for (T writeParcelable : collection) {
                        writeParcelable(writeParcelable);
                    }
                    break;
                case 3:
                    for (T writeSerializable : collection) {
                        writeSerializable(writeSerializable);
                    }
                    break;
                case 4:
                    for (T writeString : collection) {
                        writeString(writeString);
                    }
                    break;
                case 5:
                    for (T writeStrongBinder : collection) {
                        writeStrongBinder(writeStrongBinder);
                    }
                    break;
            }
        }
    }

    public <T> void writeArray(T[] tArr, int i) {
        setOutputField(i);
        writeArray(tArr);
    }

    /* access modifiers changed from: protected */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public <T> void writeArray(T[] r4) {
        /*
            r3 = this;
            if (r4 != 0) goto L_0x0007
            r4 = -1
            r3.writeInt(r4)
            return
        L_0x0007:
            int r0 = r4.length
            r3.writeInt(r0)
            if (r0 <= 0) goto L_0x0057
            r1 = 0
            r2 = r4[r1]
            int r2 = r3.getType(r2)
            r3.writeInt(r2)
            switch(r2) {
                case 1: goto L_0x004b;
                case 2: goto L_0x003f;
                case 3: goto L_0x0033;
                case 4: goto L_0x0027;
                case 5: goto L_0x001b;
                default: goto L_0x001a;
            }
        L_0x001a:
            goto L_0x0057
        L_0x001b:
            if (r1 >= r0) goto L_0x0057
            r2 = r4[r1]
            android.os.IBinder r2 = (android.os.IBinder) r2
            r3.writeStrongBinder(r2)
            int r1 = r1 + 1
            goto L_0x001b
        L_0x0027:
            if (r1 >= r0) goto L_0x0057
            r2 = r4[r1]
            java.lang.String r2 = (java.lang.String) r2
            r3.writeString(r2)
            int r1 = r1 + 1
            goto L_0x0027
        L_0x0033:
            if (r1 >= r0) goto L_0x0057
            r2 = r4[r1]
            java.io.Serializable r2 = (java.io.Serializable) r2
            r3.writeSerializable(r2)
            int r1 = r1 + 1
            goto L_0x0033
        L_0x003f:
            if (r1 >= r0) goto L_0x0057
            r2 = r4[r1]
            android.os.Parcelable r2 = (android.os.Parcelable) r2
            r3.writeParcelable(r2)
            int r1 = r1 + 1
            goto L_0x003f
        L_0x004b:
            if (r1 >= r0) goto L_0x0057
            r2 = r4[r1]
            androidx.versionedparcelable.VersionedParcelable r2 = (androidx.versionedparcelable.VersionedParcelable) r2
            r3.writeVersionedParcelable(r2)
            int r1 = r1 + 1
            goto L_0x004b
        L_0x0057:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.versionedparcelable.VersionedParcel.writeArray(java.lang.Object[]):void");
    }

    private <T> int getType(T t) {
        if (t instanceof String) {
            return 4;
        }
        if (t instanceof Parcelable) {
            return 2;
        }
        if (t instanceof VersionedParcelable) {
            return 1;
        }
        if (t instanceof Serializable) {
            return 3;
        }
        if (t instanceof IBinder) {
            return 5;
        }
        StringBuilder sb = new StringBuilder();
        sb.append(t.getClass().getName());
        sb.append(" cannot be VersionedParcelled");
        throw new IllegalArgumentException(sb.toString());
    }

    public void writeVersionedParcelable(VersionedParcelable versionedParcelable, int i) {
        setOutputField(i);
        writeVersionedParcelable(versionedParcelable);
    }

    /* access modifiers changed from: protected */
    public void writeVersionedParcelable(VersionedParcelable versionedParcelable) {
        if (versionedParcelable == null) {
            writeString(null);
            return;
        }
        writeVersionedParcelableCreator(versionedParcelable);
        VersionedParcel createSubParcel = createSubParcel();
        writeToParcel(versionedParcelable, createSubParcel);
        createSubParcel.closeField();
    }

    private void writeVersionedParcelableCreator(VersionedParcelable versionedParcelable) {
        try {
            writeString(findParcelClass(versionedParcelable.getClass()).getName());
        } catch (ClassNotFoundException e) {
            StringBuilder sb = new StringBuilder();
            sb.append(versionedParcelable.getClass().getSimpleName());
            sb.append(" does not have a Parcelizer");
            throw new RuntimeException(sb.toString(), e);
        }
    }

    public void writeSerializable(Serializable serializable, int i) {
        setOutputField(i);
        writeSerializable(serializable);
    }

    private void writeSerializable(Serializable serializable) {
        if (serializable == null) {
            writeString(null);
            return;
        }
        String name = serializable.getClass().getName();
        writeString(name);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            objectOutputStream.writeObject(serializable);
            objectOutputStream.close();
            writeByteArray(byteArrayOutputStream.toByteArray());
        } catch (IOException e) {
            StringBuilder sb = new StringBuilder();
            sb.append("VersionedParcelable encountered IOException writing serializable object (name = ");
            sb.append(name);
            sb.append(")");
            throw new RuntimeException(sb.toString(), e);
        }
    }

    public void writeException(Exception exc, int i) {
        setOutputField(i);
        if (exc == null) {
            writeNoException();
            return;
        }
        int i2 = 0;
        if ((exc instanceof Parcelable) && exc.getClass().getClassLoader() == Parcelable.class.getClassLoader()) {
            i2 = -9;
        } else if (exc instanceof SecurityException) {
            i2 = -1;
        } else if (exc instanceof BadParcelableException) {
            i2 = -2;
        } else if (exc instanceof IllegalArgumentException) {
            i2 = -3;
        } else if (exc instanceof NullPointerException) {
            i2 = -4;
        } else if (exc instanceof IllegalStateException) {
            i2 = -5;
        } else if (exc instanceof NetworkOnMainThreadException) {
            i2 = -6;
        } else if (exc instanceof UnsupportedOperationException) {
            i2 = -7;
        }
        writeInt(i2);
        if (i2 != 0) {
            writeString(exc.getMessage());
            if (i2 == -9) {
                writeParcelable((Parcelable) exc);
            }
        } else if (exc instanceof RuntimeException) {
            throw ((RuntimeException) exc);
        } else {
            throw new RuntimeException(exc);
        }
    }

    /* access modifiers changed from: protected */
    public void writeNoException() {
        writeInt(0);
    }

    public Exception readException(Exception exc, int i) {
        if (!readField(i)) {
            return exc;
        }
        int readExceptionCode = readExceptionCode();
        return readExceptionCode != 0 ? readException(readExceptionCode, readString()) : exc;
    }

    private int readExceptionCode() {
        return readInt();
    }

    private Exception readException(int i, String str) {
        return createException(i, str);
    }

    @NonNull
    protected static Throwable getRootCause(@NonNull Throwable th) {
        while (th.getCause() != null) {
            th = th.getCause();
        }
        return th;
    }

    private Exception createException(int i, String str) {
        switch (i) {
            case -9:
                return (Exception) readParcelable();
            case -7:
                return new UnsupportedOperationException(str);
            case -6:
                return new NetworkOnMainThreadException();
            case -5:
                return new IllegalStateException(str);
            case -4:
                return new NullPointerException(str);
            case -3:
                return new IllegalArgumentException(str);
            case -2:
                return new BadParcelableException(str);
            case -1:
                return new SecurityException(str);
            default:
                StringBuilder sb = new StringBuilder();
                sb.append("Unknown exception code: ");
                sb.append(i);
                sb.append(" msg ");
                sb.append(str);
                return new RuntimeException(sb.toString());
        }
    }

    public byte readByte(byte b, int i) {
        if (!readField(i)) {
            return b;
        }
        return (byte) (readInt() & 255);
    }

    @RequiresApi(api = 21)
    public Size readSize(Size size, int i) {
        if (!readField(i)) {
            return size;
        }
        if (readBoolean()) {
            return new Size(readInt(), readInt());
        }
        return null;
    }

    @RequiresApi(api = 21)
    public SizeF readSizeF(SizeF sizeF, int i) {
        if (!readField(i)) {
            return sizeF;
        }
        if (readBoolean()) {
            return new SizeF(readFloat(), readFloat());
        }
        return null;
    }

    public SparseBooleanArray readSparseBooleanArray(SparseBooleanArray sparseBooleanArray, int i) {
        if (!readField(i)) {
            return sparseBooleanArray;
        }
        int readInt = readInt();
        if (readInt < 0) {
            return null;
        }
        SparseBooleanArray sparseBooleanArray2 = new SparseBooleanArray(readInt);
        for (int i2 = 0; i2 < readInt; i2++) {
            sparseBooleanArray2.put(readInt(), readBoolean());
        }
        return sparseBooleanArray2;
    }

    public <T> Set<T> readSet(Set<T> set, int i) {
        if (!readField(i)) {
            return set;
        }
        return (Set) readCollection(i, new ArraySet());
    }

    public <T> List<T> readList(List<T> list, int i) {
        if (!readField(i)) {
            return list;
        }
        return (List) readCollection(i, new ArrayList());
    }

    /* Code decompiled incorrectly, please refer to instructions dump. */
    private <T, S extends java.util.Collection<T>> S readCollection(int r3, S r4) {
        /*
            r2 = this;
            int r3 = r2.readInt()
            r0 = 0
            if (r3 >= 0) goto L_0x0008
            return r0
        L_0x0008:
            if (r3 == 0) goto L_0x0051
            int r1 = r2.readInt()
            if (r3 >= 0) goto L_0x0011
            return r0
        L_0x0011:
            switch(r1) {
                case 1: goto L_0x0045;
                case 2: goto L_0x0039;
                case 3: goto L_0x002d;
                case 4: goto L_0x0021;
                case 5: goto L_0x0015;
                default: goto L_0x0014;
            }
        L_0x0014:
            goto L_0x0051
        L_0x0015:
            if (r3 <= 0) goto L_0x0051
            android.os.IBinder r0 = r2.readStrongBinder()
            r4.add(r0)
            int r3 = r3 + -1
            goto L_0x0015
        L_0x0021:
            if (r3 <= 0) goto L_0x0051
            java.lang.String r0 = r2.readString()
            r4.add(r0)
            int r3 = r3 + -1
            goto L_0x0021
        L_0x002d:
            if (r3 <= 0) goto L_0x0051
            java.io.Serializable r0 = r2.readSerializable()
            r4.add(r0)
            int r3 = r3 + -1
            goto L_0x002d
        L_0x0039:
            if (r3 <= 0) goto L_0x0051
            android.os.Parcelable r0 = r2.readParcelable()
            r4.add(r0)
            int r3 = r3 + -1
            goto L_0x0039
        L_0x0045:
            if (r3 <= 0) goto L_0x0051
            androidx.versionedparcelable.VersionedParcelable r0 = r2.readVersionedParcelable()
            r4.add(r0)
            int r3 = r3 + -1
            goto L_0x0045
        L_0x0051:
            return r4
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.versionedparcelable.VersionedParcel.readCollection(int, java.util.Collection):java.util.Collection");
    }

    public <T> T[] readArray(T[] tArr, int i) {
        if (!readField(i)) {
            return tArr;
        }
        return readArray(tArr);
    }

    /* access modifiers changed from: protected */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public <T> T[] readArray(T[] r5) {
        /*
            r4 = this;
            int r0 = r4.readInt()
            r1 = 0
            if (r0 >= 0) goto L_0x0008
            return r1
        L_0x0008:
            java.util.ArrayList r2 = new java.util.ArrayList
            r2.<init>(r0)
            if (r0 == 0) goto L_0x0056
            int r3 = r4.readInt()
            if (r0 >= 0) goto L_0x0016
            return r1
        L_0x0016:
            switch(r3) {
                case 1: goto L_0x004a;
                case 2: goto L_0x003e;
                case 3: goto L_0x0032;
                case 4: goto L_0x0026;
                case 5: goto L_0x001a;
                default: goto L_0x0019;
            }
        L_0x0019:
            goto L_0x0056
        L_0x001a:
            if (r0 <= 0) goto L_0x0056
            android.os.IBinder r1 = r4.readStrongBinder()
            r2.add(r1)
            int r0 = r0 + -1
            goto L_0x001a
        L_0x0026:
            if (r0 <= 0) goto L_0x0056
            java.lang.String r1 = r4.readString()
            r2.add(r1)
            int r0 = r0 + -1
            goto L_0x0026
        L_0x0032:
            if (r0 <= 0) goto L_0x0056
            java.io.Serializable r1 = r4.readSerializable()
            r2.add(r1)
            int r0 = r0 + -1
            goto L_0x0032
        L_0x003e:
            if (r0 <= 0) goto L_0x0056
            android.os.Parcelable r1 = r4.readParcelable()
            r2.add(r1)
            int r0 = r0 + -1
            goto L_0x003e
        L_0x004a:
            if (r0 <= 0) goto L_0x0056
            androidx.versionedparcelable.VersionedParcelable r1 = r4.readVersionedParcelable()
            r2.add(r1)
            int r0 = r0 + -1
            goto L_0x004a
        L_0x0056:
            java.lang.Object[] r5 = r2.toArray(r5)
            return r5
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.versionedparcelable.VersionedParcel.readArray(java.lang.Object[]):java.lang.Object[]");
    }

    public <T extends VersionedParcelable> T readVersionedParcelable(T t, int i) {
        if (!readField(i)) {
            return t;
        }
        return readVersionedParcelable();
    }

    /* access modifiers changed from: protected */
    public <T extends VersionedParcelable> T readVersionedParcelable() {
        String readString = readString();
        if (readString == null) {
            return null;
        }
        return readFromParcel(readString, createSubParcel());
    }

    /* access modifiers changed from: protected */
    public Serializable readSerializable() {
        String readString = readString();
        if (readString == null) {
            return null;
        }
        try {
            return (Serializable) new ObjectInputStream(new ByteArrayInputStream(readByteArray())) {
                /* access modifiers changed from: protected */
                public Class<?> resolveClass(ObjectStreamClass objectStreamClass) throws IOException, ClassNotFoundException {
                    Class<?> cls = Class.forName(objectStreamClass.getName(), false, getClass().getClassLoader());
                    if (cls != null) {
                        return cls;
                    }
                    return super.resolveClass(objectStreamClass);
                }
            }.readObject();
        } catch (IOException e) {
            StringBuilder sb = new StringBuilder();
            sb.append("VersionedParcelable encountered IOException reading a Serializable object (name = ");
            sb.append(readString);
            sb.append(")");
            throw new RuntimeException(sb.toString(), e);
        } catch (ClassNotFoundException e2) {
            StringBuilder sb2 = new StringBuilder();
            sb2.append("VersionedParcelable encountered ClassNotFoundException reading a Serializable object (name = ");
            sb2.append(readString);
            sb2.append(")");
            throw new RuntimeException(sb2.toString(), e2);
        }
    }

    protected static <T extends VersionedParcelable> T readFromParcel(String str, VersionedParcel versionedParcel) {
        try {
            return (VersionedParcelable) Class.forName(str, true, VersionedParcel.class.getClassLoader()).getDeclaredMethod("read", new Class[]{VersionedParcel.class}).invoke(null, new Object[]{versionedParcel});
        } catch (IllegalAccessException e) {
            throw new RuntimeException("VersionedParcel encountered IllegalAccessException", e);
        } catch (InvocationTargetException e2) {
            if (e2.getCause() instanceof RuntimeException) {
                throw ((RuntimeException) e2.getCause());
            }
            throw new RuntimeException("VersionedParcel encountered InvocationTargetException", e2);
        } catch (NoSuchMethodException e3) {
            throw new RuntimeException("VersionedParcel encountered NoSuchMethodException", e3);
        } catch (ClassNotFoundException e4) {
            throw new RuntimeException("VersionedParcel encountered ClassNotFoundException", e4);
        }
    }

    protected static <T extends VersionedParcelable> void writeToParcel(T t, VersionedParcel versionedParcel) {
        try {
            findParcelClass(t).getDeclaredMethod("write", new Class[]{t.getClass(), VersionedParcel.class}).invoke(null, new Object[]{t, versionedParcel});
        } catch (IllegalAccessException e) {
            throw new RuntimeException("VersionedParcel encountered IllegalAccessException", e);
        } catch (InvocationTargetException e2) {
            if (e2.getCause() instanceof RuntimeException) {
                throw ((RuntimeException) e2.getCause());
            }
            throw new RuntimeException("VersionedParcel encountered InvocationTargetException", e2);
        } catch (NoSuchMethodException e3) {
            throw new RuntimeException("VersionedParcel encountered NoSuchMethodException", e3);
        } catch (ClassNotFoundException e4) {
            throw new RuntimeException("VersionedParcel encountered ClassNotFoundException", e4);
        }
    }

    private static <T extends VersionedParcelable> Class findParcelClass(T t) throws ClassNotFoundException {
        return findParcelClass(t.getClass());
    }

    private static Class findParcelClass(Class<? extends VersionedParcelable> cls) throws ClassNotFoundException {
        return Class.forName(String.format("%s.%sParcelizer", new Object[]{cls.getPackage().getName(), cls.getSimpleName()}), false, cls.getClassLoader());
    }
}
