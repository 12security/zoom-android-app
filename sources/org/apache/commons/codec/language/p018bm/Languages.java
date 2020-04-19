package org.apache.commons.codec.language.p018bm;

import java.io.InputStream;
import java.util.Collections;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.Set;

/* renamed from: org.apache.commons.codec.language.bm.Languages */
public class Languages {
    public static final String ANY = "any";
    public static final LanguageSet ANY_LANGUAGE = new LanguageSet() {
        public boolean contains(String str) {
            return true;
        }

        public boolean isEmpty() {
            return false;
        }

        public boolean isSingleton() {
            return false;
        }

        public LanguageSet merge(LanguageSet languageSet) {
            return languageSet;
        }

        public LanguageSet restrictTo(LanguageSet languageSet) {
            return languageSet;
        }

        public String toString() {
            return "ANY_LANGUAGE";
        }

        public String getAny() {
            throw new NoSuchElementException("Can't fetch any language from the any language set.");
        }
    };
    private static final Map<NameType, Languages> LANGUAGES = new EnumMap(NameType.class);
    public static final LanguageSet NO_LANGUAGES = new LanguageSet() {
        public boolean contains(String str) {
            return false;
        }

        public boolean isEmpty() {
            return true;
        }

        public boolean isSingleton() {
            return false;
        }

        public LanguageSet merge(LanguageSet languageSet) {
            return languageSet;
        }

        public LanguageSet restrictTo(LanguageSet languageSet) {
            return this;
        }

        public String toString() {
            return "NO_LANGUAGES";
        }

        public String getAny() {
            throw new NoSuchElementException("Can't fetch any language from the empty language set.");
        }
    };
    private final Set<String> languages;

    /* renamed from: org.apache.commons.codec.language.bm.Languages$LanguageSet */
    public static abstract class LanguageSet {
        public abstract boolean contains(String str);

        public abstract String getAny();

        public abstract boolean isEmpty();

        public abstract boolean isSingleton();

        /* access modifiers changed from: 0000 */
        public abstract LanguageSet merge(LanguageSet languageSet);

        public abstract LanguageSet restrictTo(LanguageSet languageSet);

        public static LanguageSet from(Set<String> set) {
            return set.isEmpty() ? Languages.NO_LANGUAGES : new SomeLanguages(set);
        }
    }

    /* renamed from: org.apache.commons.codec.language.bm.Languages$SomeLanguages */
    public static final class SomeLanguages extends LanguageSet {
        private final Set<String> languages;

        private SomeLanguages(Set<String> set) {
            this.languages = Collections.unmodifiableSet(set);
        }

        public boolean contains(String str) {
            return this.languages.contains(str);
        }

        public String getAny() {
            return (String) this.languages.iterator().next();
        }

        public Set<String> getLanguages() {
            return this.languages;
        }

        public boolean isEmpty() {
            return this.languages.isEmpty();
        }

        public boolean isSingleton() {
            return this.languages.size() == 1;
        }

        public LanguageSet restrictTo(LanguageSet languageSet) {
            if (languageSet == Languages.NO_LANGUAGES) {
                return languageSet;
            }
            if (languageSet == Languages.ANY_LANGUAGE) {
                return this;
            }
            SomeLanguages someLanguages = (SomeLanguages) languageSet;
            HashSet hashSet = new HashSet(Math.min(this.languages.size(), someLanguages.languages.size()));
            for (String str : this.languages) {
                if (someLanguages.languages.contains(str)) {
                    hashSet.add(str);
                }
            }
            return from(hashSet);
        }

        public LanguageSet merge(LanguageSet languageSet) {
            if (languageSet == Languages.NO_LANGUAGES) {
                return this;
            }
            if (languageSet == Languages.ANY_LANGUAGE) {
                return languageSet;
            }
            SomeLanguages someLanguages = (SomeLanguages) languageSet;
            HashSet hashSet = new HashSet(this.languages);
            for (String add : someLanguages.languages) {
                hashSet.add(add);
            }
            return from(hashSet);
        }

        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("Languages(");
            sb.append(this.languages.toString());
            sb.append(")");
            return sb.toString();
        }
    }

    static {
        NameType[] values;
        for (NameType nameType : NameType.values()) {
            LANGUAGES.put(nameType, getInstance(langResourceName(nameType)));
        }
    }

    public static Languages getInstance(NameType nameType) {
        return (Languages) LANGUAGES.get(nameType);
    }

    /* JADX INFO: finally extract failed */
    public static Languages getInstance(String str) {
        HashSet hashSet = new HashSet();
        InputStream resourceAsStream = Languages.class.getClassLoader().getResourceAsStream(str);
        if (resourceAsStream != null) {
            Scanner scanner = new Scanner(resourceAsStream, "UTF-8");
            boolean z = false;
            while (scanner.hasNextLine()) {
                try {
                    String trim = scanner.nextLine().trim();
                    if (z) {
                        if (trim.endsWith("*/")) {
                            z = false;
                        }
                    } else if (trim.startsWith("/*")) {
                        z = true;
                    } else if (trim.length() > 0) {
                        hashSet.add(trim);
                    }
                } catch (Throwable th) {
                    scanner.close();
                    throw th;
                }
            }
            scanner.close();
            return new Languages(Collections.unmodifiableSet(hashSet));
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Unable to resolve required resource: ");
        sb.append(str);
        throw new IllegalArgumentException(sb.toString());
    }

    private static String langResourceName(NameType nameType) {
        return String.format("org/apache/commons/codec/language/bm/%s_languages.txt", new Object[]{nameType.getName()});
    }

    private Languages(Set<String> set) {
        this.languages = set;
    }

    public Set<String> getLanguages() {
        return this.languages;
    }
}
