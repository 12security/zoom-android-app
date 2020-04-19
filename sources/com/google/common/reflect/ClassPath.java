package com.google.common.reflect;

import com.box.androidsdk.content.models.BoxFile;
import com.google.common.annotations.Beta;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.CharMatcher;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.base.Splitter;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSet.Builder;
import com.google.common.collect.Maps;
import com.google.common.collect.MultimapBuilder;
import com.google.common.collect.SetMultimap;
import com.google.common.collect.Sets;
import com.google.common.p007io.ByteSource;
import com.google.common.p007io.CharSource;
import com.google.common.p007io.Resources;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.charset.Charset;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.jar.Attributes.Name;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.Manifest;
import java.util.logging.Logger;
import javax.annotation.Nullable;

@Beta
public final class ClassPath {
    private static final String CLASS_FILE_NAME_EXTENSION = ".class";
    /* access modifiers changed from: private */
    public static final Splitter CLASS_PATH_ATTRIBUTE_SEPARATOR = Splitter.m97on(OAuth.SCOPE_DELIMITER).omitEmptyStrings();
    private static final Predicate<ClassInfo> IS_TOP_LEVEL = new Predicate<ClassInfo>() {
        public boolean apply(ClassInfo classInfo) {
            return classInfo.className.indexOf(36) == -1;
        }
    };
    /* access modifiers changed from: private */
    public static final Logger logger = Logger.getLogger(ClassPath.class.getName());
    private final ImmutableSet<ResourceInfo> resources;

    @Beta
    public static final class ClassInfo extends ResourceInfo {
        /* access modifiers changed from: private */
        public final String className;

        ClassInfo(String str, ClassLoader classLoader) {
            super(str, classLoader);
            this.className = ClassPath.getClassName(str);
        }

        public String getPackageName() {
            return Reflection.getPackageName(this.className);
        }

        public String getSimpleName() {
            int lastIndexOf = this.className.lastIndexOf(36);
            if (lastIndexOf != -1) {
                return CharMatcher.digit().trimLeadingFrom(this.className.substring(lastIndexOf + 1));
            }
            String packageName = getPackageName();
            if (packageName.isEmpty()) {
                return this.className;
            }
            return this.className.substring(packageName.length() + 1);
        }

        public String getName() {
            return this.className;
        }

        public Class<?> load() {
            try {
                return this.loader.loadClass(this.className);
            } catch (ClassNotFoundException e) {
                throw new IllegalStateException(e);
            }
        }

        public String toString() {
            return this.className;
        }
    }

    @VisibleForTesting
    static final class DefaultScanner extends Scanner {
        private final SetMultimap<ClassLoader, String> resources = MultimapBuilder.hashKeys().linkedHashSetValues().build();

        DefaultScanner() {
        }

        /* access modifiers changed from: 0000 */
        public ImmutableSet<ResourceInfo> getResources() {
            Builder builder = ImmutableSet.builder();
            for (Entry entry : this.resources.entries()) {
                builder.add((Object) ResourceInfo.m223of((String) entry.getValue(), (ClassLoader) entry.getKey()));
            }
            return builder.build();
        }

        /* access modifiers changed from: protected */
        public void scanJarFile(ClassLoader classLoader, JarFile jarFile) {
            Enumeration entries = jarFile.entries();
            while (entries.hasMoreElements()) {
                JarEntry jarEntry = (JarEntry) entries.nextElement();
                if (!jarEntry.isDirectory() && !jarEntry.getName().equals("META-INF/MANIFEST.MF")) {
                    this.resources.get(classLoader).add(jarEntry.getName());
                }
            }
        }

        /* access modifiers changed from: protected */
        public void scanDirectory(ClassLoader classLoader, File file) throws IOException {
            scanDirectory(file, classLoader, "");
        }

        private void scanDirectory(File file, ClassLoader classLoader, String str) throws IOException {
            File[] listFiles = file.listFiles();
            if (listFiles == null) {
                Logger access$100 = ClassPath.logger;
                StringBuilder sb = new StringBuilder();
                sb.append("Cannot read directory ");
                sb.append(file);
                access$100.warning(sb.toString());
                return;
            }
            for (File file2 : listFiles) {
                String name = file2.getName();
                if (file2.isDirectory()) {
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append(str);
                    sb2.append(name);
                    sb2.append("/");
                    scanDirectory(file2, classLoader, sb2.toString());
                } else {
                    StringBuilder sb3 = new StringBuilder();
                    sb3.append(str);
                    sb3.append(name);
                    String sb4 = sb3.toString();
                    if (!sb4.equals("META-INF/MANIFEST.MF")) {
                        this.resources.get(classLoader).add(sb4);
                    }
                }
            }
        }
    }

    @Beta
    public static class ResourceInfo {
        final ClassLoader loader;
        private final String resourceName;

        /* renamed from: of */
        static ResourceInfo m223of(String str, ClassLoader classLoader) {
            if (str.endsWith(ClassPath.CLASS_FILE_NAME_EXTENSION)) {
                return new ClassInfo(str, classLoader);
            }
            return new ResourceInfo(str, classLoader);
        }

        ResourceInfo(String str, ClassLoader classLoader) {
            this.resourceName = (String) Preconditions.checkNotNull(str);
            this.loader = (ClassLoader) Preconditions.checkNotNull(classLoader);
        }

        public final URL url() {
            URL resource = this.loader.getResource(this.resourceName);
            if (resource != null) {
                return resource;
            }
            throw new NoSuchElementException(this.resourceName);
        }

        public final ByteSource asByteSource() {
            return Resources.asByteSource(url());
        }

        public final CharSource asCharSource(Charset charset) {
            return Resources.asCharSource(url(), charset);
        }

        public final String getResourceName() {
            return this.resourceName;
        }

        public int hashCode() {
            return this.resourceName.hashCode();
        }

        public boolean equals(Object obj) {
            boolean z = false;
            if (!(obj instanceof ResourceInfo)) {
                return false;
            }
            ResourceInfo resourceInfo = (ResourceInfo) obj;
            if (this.resourceName.equals(resourceInfo.resourceName) && this.loader == resourceInfo.loader) {
                z = true;
            }
            return z;
        }

        public String toString() {
            return this.resourceName;
        }
    }

    static abstract class Scanner {
        private final Set<File> scannedUris = Sets.newHashSet();

        /* access modifiers changed from: protected */
        public abstract void scanDirectory(ClassLoader classLoader, File file) throws IOException;

        /* access modifiers changed from: protected */
        public abstract void scanJarFile(ClassLoader classLoader, JarFile jarFile) throws IOException;

        Scanner() {
        }

        public final void scan(ClassLoader classLoader) throws IOException {
            Iterator it = getClassPathEntries(classLoader).entrySet().iterator();
            while (it.hasNext()) {
                Entry entry = (Entry) it.next();
                scan((File) entry.getKey(), (ClassLoader) entry.getValue());
            }
        }

        /* access modifiers changed from: 0000 */
        @VisibleForTesting
        public final void scan(File file, ClassLoader classLoader) throws IOException {
            if (this.scannedUris.add(file.getCanonicalFile())) {
                scanFrom(file, classLoader);
            }
        }

        private void scanFrom(File file, ClassLoader classLoader) throws IOException {
            try {
                if (file.exists()) {
                    if (file.isDirectory()) {
                        scanDirectory(classLoader, file);
                    } else {
                        scanJar(file, classLoader);
                    }
                }
            } catch (SecurityException e) {
                Logger access$100 = ClassPath.logger;
                StringBuilder sb = new StringBuilder();
                sb.append("Cannot access ");
                sb.append(file);
                sb.append(": ");
                sb.append(e);
                access$100.warning(sb.toString());
            }
        }

        private void scanJar(File file, ClassLoader classLoader) throws IOException {
            try {
                JarFile jarFile = new JarFile(file);
                try {
                    Iterator it = getClassPathFromManifest(file, jarFile.getManifest()).iterator();
                    while (it.hasNext()) {
                        scan((File) it.next(), classLoader);
                    }
                    scanJarFile(classLoader, jarFile);
                } finally {
                    try {
                        jarFile.close();
                    } catch (IOException unused) {
                    }
                }
            } catch (IOException unused2) {
            }
        }

        @VisibleForTesting
        static ImmutableSet<File> getClassPathFromManifest(File file, @Nullable Manifest manifest) {
            if (manifest == null) {
                return ImmutableSet.m155of();
            }
            Builder builder = ImmutableSet.builder();
            String value = manifest.getMainAttributes().getValue(Name.CLASS_PATH.toString());
            if (value != null) {
                for (String str : ClassPath.CLASS_PATH_ATTRIBUTE_SEPARATOR.split(value)) {
                    try {
                        URL classPathEntry = getClassPathEntry(file, str);
                        if (classPathEntry.getProtocol().equals(BoxFile.TYPE)) {
                            builder.add((Object) new File(classPathEntry.getFile()));
                        }
                    } catch (MalformedURLException unused) {
                        Logger access$100 = ClassPath.logger;
                        StringBuilder sb = new StringBuilder();
                        sb.append("Invalid Class-Path entry: ");
                        sb.append(str);
                        access$100.warning(sb.toString());
                    }
                }
            }
            return builder.build();
        }

        @VisibleForTesting
        static ImmutableMap<File, ClassLoader> getClassPathEntries(ClassLoader classLoader) {
            URL[] uRLs;
            LinkedHashMap newLinkedHashMap = Maps.newLinkedHashMap();
            ClassLoader parent = classLoader.getParent();
            if (parent != null) {
                newLinkedHashMap.putAll(getClassPathEntries(parent));
            }
            if (classLoader instanceof URLClassLoader) {
                for (URL url : ((URLClassLoader) classLoader).getURLs()) {
                    if (url.getProtocol().equals(BoxFile.TYPE)) {
                        File file = new File(url.getFile());
                        if (!newLinkedHashMap.containsKey(file)) {
                            newLinkedHashMap.put(file, classLoader);
                        }
                    }
                }
            }
            return ImmutableMap.copyOf((Map<? extends K, ? extends V>) newLinkedHashMap);
        }

        @VisibleForTesting
        static URL getClassPathEntry(File file, String str) throws MalformedURLException {
            return new URL(file.toURI().toURL(), str);
        }
    }

    private ClassPath(ImmutableSet<ResourceInfo> immutableSet) {
        this.resources = immutableSet;
    }

    public static ClassPath from(ClassLoader classLoader) throws IOException {
        DefaultScanner defaultScanner = new DefaultScanner();
        defaultScanner.scan(classLoader);
        return new ClassPath(defaultScanner.getResources());
    }

    public ImmutableSet<ResourceInfo> getResources() {
        return this.resources;
    }

    public ImmutableSet<ClassInfo> getAllClasses() {
        return FluentIterable.from((Iterable<E>) this.resources).filter(ClassInfo.class).toSet();
    }

    public ImmutableSet<ClassInfo> getTopLevelClasses() {
        return FluentIterable.from((Iterable<E>) this.resources).filter(ClassInfo.class).filter(IS_TOP_LEVEL).toSet();
    }

    public ImmutableSet<ClassInfo> getTopLevelClasses(String str) {
        Preconditions.checkNotNull(str);
        Builder builder = ImmutableSet.builder();
        Iterator it = getTopLevelClasses().iterator();
        while (it.hasNext()) {
            ClassInfo classInfo = (ClassInfo) it.next();
            if (classInfo.getPackageName().equals(str)) {
                builder.add((Object) classInfo);
            }
        }
        return builder.build();
    }

    public ImmutableSet<ClassInfo> getTopLevelClassesRecursive(String str) {
        Preconditions.checkNotNull(str);
        StringBuilder sb = new StringBuilder();
        sb.append(str);
        sb.append('.');
        String sb2 = sb.toString();
        Builder builder = ImmutableSet.builder();
        Iterator it = getTopLevelClasses().iterator();
        while (it.hasNext()) {
            ClassInfo classInfo = (ClassInfo) it.next();
            if (classInfo.getName().startsWith(sb2)) {
                builder.add((Object) classInfo);
            }
        }
        return builder.build();
    }

    @VisibleForTesting
    static String getClassName(String str) {
        return str.substring(0, str.length() - 6).replace('/', '.');
    }
}
