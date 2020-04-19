package com.google.api.client.util.store;

import com.google.api.client.util.IOUtils;
import com.google.api.client.util.Maps;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.logging.Logger;

public class FileDataStoreFactory extends AbstractDataStoreFactory {
    private static final Logger LOGGER = Logger.getLogger(FileDataStoreFactory.class.getName());
    private final File dataDirectory;

    static class FileDataStore<V extends Serializable> extends AbstractMemoryDataStore<V> {
        private final File dataFile;

        FileDataStore(FileDataStoreFactory fileDataStoreFactory, File file, String str) throws IOException {
            super(fileDataStoreFactory, str);
            this.dataFile = new File(file, str);
            if (IOUtils.isSymbolicLink(this.dataFile)) {
                StringBuilder sb = new StringBuilder();
                sb.append("unable to use a symbolic link: ");
                sb.append(this.dataFile);
                throw new IOException(sb.toString());
            } else if (this.dataFile.createNewFile()) {
                this.keyValueMap = Maps.newHashMap();
                save();
            } else {
                this.keyValueMap = (HashMap) IOUtils.deserialize((InputStream) new FileInputStream(this.dataFile));
            }
        }

        public void save() throws IOException {
            IOUtils.serialize(this.keyValueMap, new FileOutputStream(this.dataFile));
        }

        public FileDataStoreFactory getDataStoreFactory() {
            return (FileDataStoreFactory) super.getDataStoreFactory();
        }
    }

    public FileDataStoreFactory(File file) throws IOException {
        File canonicalFile = file.getCanonicalFile();
        this.dataDirectory = canonicalFile;
        if (IOUtils.isSymbolicLink(canonicalFile)) {
            StringBuilder sb = new StringBuilder();
            sb.append("unable to use a symbolic link: ");
            sb.append(canonicalFile);
            throw new IOException(sb.toString());
        } else if (canonicalFile.exists() || canonicalFile.mkdirs()) {
            setPermissionsToOwnerOnly(canonicalFile);
        } else {
            StringBuilder sb2 = new StringBuilder();
            sb2.append("unable to create directory: ");
            sb2.append(canonicalFile);
            throw new IOException(sb2.toString());
        }
    }

    public final File getDataDirectory() {
        return this.dataDirectory;
    }

    /* access modifiers changed from: protected */
    public <V extends Serializable> DataStore<V> createDataStore(String str) throws IOException {
        return new FileDataStore(this, this.dataDirectory, str);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:7:0x0085, code lost:
        if (((java.lang.Boolean) r3.invoke(r9, new java.lang.Object[]{java.lang.Boolean.valueOf(false), java.lang.Boolean.valueOf(false)})).booleanValue() == false) goto L_0x0087;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    static void setPermissionsToOwnerOnly(java.io.File r9) throws java.io.IOException {
        /*
            java.lang.Class<java.io.File> r0 = java.io.File.class
            java.lang.String r1 = "setReadable"
            r2 = 2
            java.lang.Class[] r3 = new java.lang.Class[r2]     // Catch:{ InvocationTargetException -> 0x011e, NoSuchMethodException -> 0x0102, IllegalAccessException | IllegalArgumentException | SecurityException -> 0x011d }
            java.lang.Class r4 = java.lang.Boolean.TYPE     // Catch:{ InvocationTargetException -> 0x011e, NoSuchMethodException -> 0x0102, IllegalAccessException | IllegalArgumentException | SecurityException -> 0x011d }
            r5 = 0
            r3[r5] = r4     // Catch:{ InvocationTargetException -> 0x011e, NoSuchMethodException -> 0x0102, IllegalAccessException | IllegalArgumentException | SecurityException -> 0x011d }
            java.lang.Class r4 = java.lang.Boolean.TYPE     // Catch:{ InvocationTargetException -> 0x011e, NoSuchMethodException -> 0x0102, IllegalAccessException | IllegalArgumentException | SecurityException -> 0x011d }
            r6 = 1
            r3[r6] = r4     // Catch:{ InvocationTargetException -> 0x011e, NoSuchMethodException -> 0x0102, IllegalAccessException | IllegalArgumentException | SecurityException -> 0x011d }
            java.lang.reflect.Method r0 = r0.getMethod(r1, r3)     // Catch:{ InvocationTargetException -> 0x011e, NoSuchMethodException -> 0x0102, IllegalAccessException | IllegalArgumentException | SecurityException -> 0x011d }
            java.lang.Class<java.io.File> r1 = java.io.File.class
            java.lang.String r3 = "setWritable"
            java.lang.Class[] r4 = new java.lang.Class[r2]     // Catch:{ InvocationTargetException -> 0x011e, NoSuchMethodException -> 0x0102, IllegalAccessException | IllegalArgumentException | SecurityException -> 0x011d }
            java.lang.Class r7 = java.lang.Boolean.TYPE     // Catch:{ InvocationTargetException -> 0x011e, NoSuchMethodException -> 0x0102, IllegalAccessException | IllegalArgumentException | SecurityException -> 0x011d }
            r4[r5] = r7     // Catch:{ InvocationTargetException -> 0x011e, NoSuchMethodException -> 0x0102, IllegalAccessException | IllegalArgumentException | SecurityException -> 0x011d }
            java.lang.Class r7 = java.lang.Boolean.TYPE     // Catch:{ InvocationTargetException -> 0x011e, NoSuchMethodException -> 0x0102, IllegalAccessException | IllegalArgumentException | SecurityException -> 0x011d }
            r4[r6] = r7     // Catch:{ InvocationTargetException -> 0x011e, NoSuchMethodException -> 0x0102, IllegalAccessException | IllegalArgumentException | SecurityException -> 0x011d }
            java.lang.reflect.Method r1 = r1.getMethod(r3, r4)     // Catch:{ InvocationTargetException -> 0x011e, NoSuchMethodException -> 0x0102, IllegalAccessException | IllegalArgumentException | SecurityException -> 0x011d }
            java.lang.Class<java.io.File> r3 = java.io.File.class
            java.lang.String r4 = "setExecutable"
            java.lang.Class[] r7 = new java.lang.Class[r2]     // Catch:{ InvocationTargetException -> 0x011e, NoSuchMethodException -> 0x0102, IllegalAccessException | IllegalArgumentException | SecurityException -> 0x011d }
            java.lang.Class r8 = java.lang.Boolean.TYPE     // Catch:{ InvocationTargetException -> 0x011e, NoSuchMethodException -> 0x0102, IllegalAccessException | IllegalArgumentException | SecurityException -> 0x011d }
            r7[r5] = r8     // Catch:{ InvocationTargetException -> 0x011e, NoSuchMethodException -> 0x0102, IllegalAccessException | IllegalArgumentException | SecurityException -> 0x011d }
            java.lang.Class r8 = java.lang.Boolean.TYPE     // Catch:{ InvocationTargetException -> 0x011e, NoSuchMethodException -> 0x0102, IllegalAccessException | IllegalArgumentException | SecurityException -> 0x011d }
            r7[r6] = r8     // Catch:{ InvocationTargetException -> 0x011e, NoSuchMethodException -> 0x0102, IllegalAccessException | IllegalArgumentException | SecurityException -> 0x011d }
            java.lang.reflect.Method r3 = r3.getMethod(r4, r7)     // Catch:{ InvocationTargetException -> 0x011e, NoSuchMethodException -> 0x0102, IllegalAccessException | IllegalArgumentException | SecurityException -> 0x011d }
            java.lang.Object[] r4 = new java.lang.Object[r2]     // Catch:{ InvocationTargetException -> 0x011e, NoSuchMethodException -> 0x0102, IllegalAccessException | IllegalArgumentException | SecurityException -> 0x011d }
            java.lang.Boolean r7 = java.lang.Boolean.valueOf(r5)     // Catch:{ InvocationTargetException -> 0x011e, NoSuchMethodException -> 0x0102, IllegalAccessException | IllegalArgumentException | SecurityException -> 0x011d }
            r4[r5] = r7     // Catch:{ InvocationTargetException -> 0x011e, NoSuchMethodException -> 0x0102, IllegalAccessException | IllegalArgumentException | SecurityException -> 0x011d }
            java.lang.Boolean r7 = java.lang.Boolean.valueOf(r5)     // Catch:{ InvocationTargetException -> 0x011e, NoSuchMethodException -> 0x0102, IllegalAccessException | IllegalArgumentException | SecurityException -> 0x011d }
            r4[r6] = r7     // Catch:{ InvocationTargetException -> 0x011e, NoSuchMethodException -> 0x0102, IllegalAccessException | IllegalArgumentException | SecurityException -> 0x011d }
            java.lang.Object r4 = r0.invoke(r9, r4)     // Catch:{ InvocationTargetException -> 0x011e, NoSuchMethodException -> 0x0102, IllegalAccessException | IllegalArgumentException | SecurityException -> 0x011d }
            java.lang.Boolean r4 = (java.lang.Boolean) r4     // Catch:{ InvocationTargetException -> 0x011e, NoSuchMethodException -> 0x0102, IllegalAccessException | IllegalArgumentException | SecurityException -> 0x011d }
            boolean r4 = r4.booleanValue()     // Catch:{ InvocationTargetException -> 0x011e, NoSuchMethodException -> 0x0102, IllegalAccessException | IllegalArgumentException | SecurityException -> 0x011d }
            if (r4 == 0) goto L_0x0087
            java.lang.Object[] r4 = new java.lang.Object[r2]     // Catch:{ InvocationTargetException -> 0x011e, NoSuchMethodException -> 0x0102, IllegalAccessException | IllegalArgumentException | SecurityException -> 0x011d }
            java.lang.Boolean r7 = java.lang.Boolean.valueOf(r5)     // Catch:{ InvocationTargetException -> 0x011e, NoSuchMethodException -> 0x0102, IllegalAccessException | IllegalArgumentException | SecurityException -> 0x011d }
            r4[r5] = r7     // Catch:{ InvocationTargetException -> 0x011e, NoSuchMethodException -> 0x0102, IllegalAccessException | IllegalArgumentException | SecurityException -> 0x011d }
            java.lang.Boolean r7 = java.lang.Boolean.valueOf(r5)     // Catch:{ InvocationTargetException -> 0x011e, NoSuchMethodException -> 0x0102, IllegalAccessException | IllegalArgumentException | SecurityException -> 0x011d }
            r4[r6] = r7     // Catch:{ InvocationTargetException -> 0x011e, NoSuchMethodException -> 0x0102, IllegalAccessException | IllegalArgumentException | SecurityException -> 0x011d }
            java.lang.Object r4 = r1.invoke(r9, r4)     // Catch:{ InvocationTargetException -> 0x011e, NoSuchMethodException -> 0x0102, IllegalAccessException | IllegalArgumentException | SecurityException -> 0x011d }
            java.lang.Boolean r4 = (java.lang.Boolean) r4     // Catch:{ InvocationTargetException -> 0x011e, NoSuchMethodException -> 0x0102, IllegalAccessException | IllegalArgumentException | SecurityException -> 0x011d }
            boolean r4 = r4.booleanValue()     // Catch:{ InvocationTargetException -> 0x011e, NoSuchMethodException -> 0x0102, IllegalAccessException | IllegalArgumentException | SecurityException -> 0x011d }
            if (r4 == 0) goto L_0x0087
            java.lang.Object[] r4 = new java.lang.Object[r2]     // Catch:{ InvocationTargetException -> 0x011e, NoSuchMethodException -> 0x0102, IllegalAccessException | IllegalArgumentException | SecurityException -> 0x011d }
            java.lang.Boolean r7 = java.lang.Boolean.valueOf(r5)     // Catch:{ InvocationTargetException -> 0x011e, NoSuchMethodException -> 0x0102, IllegalAccessException | IllegalArgumentException | SecurityException -> 0x011d }
            r4[r5] = r7     // Catch:{ InvocationTargetException -> 0x011e, NoSuchMethodException -> 0x0102, IllegalAccessException | IllegalArgumentException | SecurityException -> 0x011d }
            java.lang.Boolean r7 = java.lang.Boolean.valueOf(r5)     // Catch:{ InvocationTargetException -> 0x011e, NoSuchMethodException -> 0x0102, IllegalAccessException | IllegalArgumentException | SecurityException -> 0x011d }
            r4[r6] = r7     // Catch:{ InvocationTargetException -> 0x011e, NoSuchMethodException -> 0x0102, IllegalAccessException | IllegalArgumentException | SecurityException -> 0x011d }
            java.lang.Object r4 = r3.invoke(r9, r4)     // Catch:{ InvocationTargetException -> 0x011e, NoSuchMethodException -> 0x0102, IllegalAccessException | IllegalArgumentException | SecurityException -> 0x011d }
            java.lang.Boolean r4 = (java.lang.Boolean) r4     // Catch:{ InvocationTargetException -> 0x011e, NoSuchMethodException -> 0x0102, IllegalAccessException | IllegalArgumentException | SecurityException -> 0x011d }
            boolean r4 = r4.booleanValue()     // Catch:{ InvocationTargetException -> 0x011e, NoSuchMethodException -> 0x0102, IllegalAccessException | IllegalArgumentException | SecurityException -> 0x011d }
            if (r4 != 0) goto L_0x009d
        L_0x0087:
            java.util.logging.Logger r4 = LOGGER     // Catch:{ InvocationTargetException -> 0x011e, NoSuchMethodException -> 0x0102, IllegalAccessException | IllegalArgumentException | SecurityException -> 0x011d }
            java.lang.StringBuilder r7 = new java.lang.StringBuilder     // Catch:{ InvocationTargetException -> 0x011e, NoSuchMethodException -> 0x0102, IllegalAccessException | IllegalArgumentException | SecurityException -> 0x011d }
            r7.<init>()     // Catch:{ InvocationTargetException -> 0x011e, NoSuchMethodException -> 0x0102, IllegalAccessException | IllegalArgumentException | SecurityException -> 0x011d }
            java.lang.String r8 = "unable to change permissions for everybody: "
            r7.append(r8)     // Catch:{ InvocationTargetException -> 0x011e, NoSuchMethodException -> 0x0102, IllegalAccessException | IllegalArgumentException | SecurityException -> 0x011d }
            r7.append(r9)     // Catch:{ InvocationTargetException -> 0x011e, NoSuchMethodException -> 0x0102, IllegalAccessException | IllegalArgumentException | SecurityException -> 0x011d }
            java.lang.String r7 = r7.toString()     // Catch:{ InvocationTargetException -> 0x011e, NoSuchMethodException -> 0x0102, IllegalAccessException | IllegalArgumentException | SecurityException -> 0x011d }
            r4.warning(r7)     // Catch:{ InvocationTargetException -> 0x011e, NoSuchMethodException -> 0x0102, IllegalAccessException | IllegalArgumentException | SecurityException -> 0x011d }
        L_0x009d:
            java.lang.Object[] r4 = new java.lang.Object[r2]     // Catch:{ InvocationTargetException -> 0x011e, NoSuchMethodException -> 0x0102, IllegalAccessException | IllegalArgumentException | SecurityException -> 0x011d }
            java.lang.Boolean r7 = java.lang.Boolean.valueOf(r6)     // Catch:{ InvocationTargetException -> 0x011e, NoSuchMethodException -> 0x0102, IllegalAccessException | IllegalArgumentException | SecurityException -> 0x011d }
            r4[r5] = r7     // Catch:{ InvocationTargetException -> 0x011e, NoSuchMethodException -> 0x0102, IllegalAccessException | IllegalArgumentException | SecurityException -> 0x011d }
            java.lang.Boolean r7 = java.lang.Boolean.valueOf(r6)     // Catch:{ InvocationTargetException -> 0x011e, NoSuchMethodException -> 0x0102, IllegalAccessException | IllegalArgumentException | SecurityException -> 0x011d }
            r4[r6] = r7     // Catch:{ InvocationTargetException -> 0x011e, NoSuchMethodException -> 0x0102, IllegalAccessException | IllegalArgumentException | SecurityException -> 0x011d }
            java.lang.Object r0 = r0.invoke(r9, r4)     // Catch:{ InvocationTargetException -> 0x011e, NoSuchMethodException -> 0x0102, IllegalAccessException | IllegalArgumentException | SecurityException -> 0x011d }
            java.lang.Boolean r0 = (java.lang.Boolean) r0     // Catch:{ InvocationTargetException -> 0x011e, NoSuchMethodException -> 0x0102, IllegalAccessException | IllegalArgumentException | SecurityException -> 0x011d }
            boolean r0 = r0.booleanValue()     // Catch:{ InvocationTargetException -> 0x011e, NoSuchMethodException -> 0x0102, IllegalAccessException | IllegalArgumentException | SecurityException -> 0x011d }
            if (r0 == 0) goto L_0x00eb
            java.lang.Object[] r0 = new java.lang.Object[r2]     // Catch:{ InvocationTargetException -> 0x011e, NoSuchMethodException -> 0x0102, IllegalAccessException | IllegalArgumentException | SecurityException -> 0x011d }
            java.lang.Boolean r4 = java.lang.Boolean.valueOf(r6)     // Catch:{ InvocationTargetException -> 0x011e, NoSuchMethodException -> 0x0102, IllegalAccessException | IllegalArgumentException | SecurityException -> 0x011d }
            r0[r5] = r4     // Catch:{ InvocationTargetException -> 0x011e, NoSuchMethodException -> 0x0102, IllegalAccessException | IllegalArgumentException | SecurityException -> 0x011d }
            java.lang.Boolean r4 = java.lang.Boolean.valueOf(r6)     // Catch:{ InvocationTargetException -> 0x011e, NoSuchMethodException -> 0x0102, IllegalAccessException | IllegalArgumentException | SecurityException -> 0x011d }
            r0[r6] = r4     // Catch:{ InvocationTargetException -> 0x011e, NoSuchMethodException -> 0x0102, IllegalAccessException | IllegalArgumentException | SecurityException -> 0x011d }
            java.lang.Object r0 = r1.invoke(r9, r0)     // Catch:{ InvocationTargetException -> 0x011e, NoSuchMethodException -> 0x0102, IllegalAccessException | IllegalArgumentException | SecurityException -> 0x011d }
            java.lang.Boolean r0 = (java.lang.Boolean) r0     // Catch:{ InvocationTargetException -> 0x011e, NoSuchMethodException -> 0x0102, IllegalAccessException | IllegalArgumentException | SecurityException -> 0x011d }
            boolean r0 = r0.booleanValue()     // Catch:{ InvocationTargetException -> 0x011e, NoSuchMethodException -> 0x0102, IllegalAccessException | IllegalArgumentException | SecurityException -> 0x011d }
            if (r0 == 0) goto L_0x00eb
            java.lang.Object[] r0 = new java.lang.Object[r2]     // Catch:{ InvocationTargetException -> 0x011e, NoSuchMethodException -> 0x0102, IllegalAccessException | IllegalArgumentException | SecurityException -> 0x011d }
            java.lang.Boolean r1 = java.lang.Boolean.valueOf(r6)     // Catch:{ InvocationTargetException -> 0x011e, NoSuchMethodException -> 0x0102, IllegalAccessException | IllegalArgumentException | SecurityException -> 0x011d }
            r0[r5] = r1     // Catch:{ InvocationTargetException -> 0x011e, NoSuchMethodException -> 0x0102, IllegalAccessException | IllegalArgumentException | SecurityException -> 0x011d }
            java.lang.Boolean r1 = java.lang.Boolean.valueOf(r6)     // Catch:{ InvocationTargetException -> 0x011e, NoSuchMethodException -> 0x0102, IllegalAccessException | IllegalArgumentException | SecurityException -> 0x011d }
            r0[r6] = r1     // Catch:{ InvocationTargetException -> 0x011e, NoSuchMethodException -> 0x0102, IllegalAccessException | IllegalArgumentException | SecurityException -> 0x011d }
            java.lang.Object r0 = r3.invoke(r9, r0)     // Catch:{ InvocationTargetException -> 0x011e, NoSuchMethodException -> 0x0102, IllegalAccessException | IllegalArgumentException | SecurityException -> 0x011d }
            java.lang.Boolean r0 = (java.lang.Boolean) r0     // Catch:{ InvocationTargetException -> 0x011e, NoSuchMethodException -> 0x0102, IllegalAccessException | IllegalArgumentException | SecurityException -> 0x011d }
            boolean r0 = r0.booleanValue()     // Catch:{ InvocationTargetException -> 0x011e, NoSuchMethodException -> 0x0102, IllegalAccessException | IllegalArgumentException | SecurityException -> 0x011d }
            if (r0 != 0) goto L_0x011d
        L_0x00eb:
            java.util.logging.Logger r0 = LOGGER     // Catch:{ InvocationTargetException -> 0x011e, NoSuchMethodException -> 0x0102, IllegalAccessException | IllegalArgumentException | SecurityException -> 0x011d }
            java.lang.StringBuilder r1 = new java.lang.StringBuilder     // Catch:{ InvocationTargetException -> 0x011e, NoSuchMethodException -> 0x0102, IllegalAccessException | IllegalArgumentException | SecurityException -> 0x011d }
            r1.<init>()     // Catch:{ InvocationTargetException -> 0x011e, NoSuchMethodException -> 0x0102, IllegalAccessException | IllegalArgumentException | SecurityException -> 0x011d }
            java.lang.String r2 = "unable to change permissions for owner: "
            r1.append(r2)     // Catch:{ InvocationTargetException -> 0x011e, NoSuchMethodException -> 0x0102, IllegalAccessException | IllegalArgumentException | SecurityException -> 0x011d }
            r1.append(r9)     // Catch:{ InvocationTargetException -> 0x011e, NoSuchMethodException -> 0x0102, IllegalAccessException | IllegalArgumentException | SecurityException -> 0x011d }
            java.lang.String r1 = r1.toString()     // Catch:{ InvocationTargetException -> 0x011e, NoSuchMethodException -> 0x0102, IllegalAccessException | IllegalArgumentException | SecurityException -> 0x011d }
            r0.warning(r1)     // Catch:{ InvocationTargetException -> 0x011e, NoSuchMethodException -> 0x0102, IllegalAccessException | IllegalArgumentException | SecurityException -> 0x011d }
            goto L_0x011d
        L_0x0102:
            java.util.logging.Logger r0 = LOGGER
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            java.lang.String r2 = "Unable to set permissions for "
            r1.append(r2)
            r1.append(r9)
            java.lang.String r9 = ", likely because you are running a version of Java prior to 1.6"
            r1.append(r9)
            java.lang.String r9 = r1.toString()
            r0.warning(r9)
        L_0x011d:
            return
        L_0x011e:
            r9 = move-exception
            java.lang.Throwable r9 = r9.getCause()
            java.lang.Class<java.io.IOException> r0 = java.io.IOException.class
            com.google.api.client.util.Throwables.propagateIfPossible(r9, r0)
            java.lang.RuntimeException r0 = new java.lang.RuntimeException
            r0.<init>(r9)
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.api.client.util.store.FileDataStoreFactory.setPermissionsToOwnerOnly(java.io.File):void");
    }
}
