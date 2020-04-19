package org.apache.commons.codec.language;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringEncoder;

public class DaitchMokotoffSoundex implements StringEncoder {
    private static final String COMMENT = "//";
    private static final String DOUBLE_QUOTE = "\"";
    private static final Map<Character, Character> FOLDINGS = new HashMap();
    private static final int MAX_LENGTH = 6;
    private static final String MULTILINE_COMMENT_END = "*/";
    private static final String MULTILINE_COMMENT_START = "/*";
    private static final String RESOURCE_FILE = "org/apache/commons/codec/language/dmrules.txt";
    private static final Map<Character, List<Rule>> RULES = new HashMap();
    private final boolean folding;

    private static final class Branch {
        private final StringBuilder builder;
        private String cachedString;
        private String lastReplacement;

        private Branch() {
            this.builder = new StringBuilder();
            this.lastReplacement = null;
            this.cachedString = null;
        }

        public Branch createBranch() {
            Branch branch = new Branch();
            branch.builder.append(toString());
            branch.lastReplacement = this.lastReplacement;
            return branch;
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof Branch)) {
                return false;
            }
            return toString().equals(((Branch) obj).toString());
        }

        public void finish() {
            while (this.builder.length() < 6) {
                this.builder.append('0');
                this.cachedString = null;
            }
        }

        public int hashCode() {
            return toString().hashCode();
        }

        public void processNextReplacement(String str, boolean z) {
            String str2 = this.lastReplacement;
            if ((str2 == null || !str2.endsWith(str) || z) && this.builder.length() < 6) {
                this.builder.append(str);
                if (this.builder.length() > 6) {
                    StringBuilder sb = this.builder;
                    sb.delete(6, sb.length());
                }
                this.cachedString = null;
            }
            this.lastReplacement = str;
        }

        public String toString() {
            if (this.cachedString == null) {
                this.cachedString = this.builder.toString();
            }
            return this.cachedString;
        }
    }

    private static final class Rule {
        /* access modifiers changed from: private */
        public final String pattern;
        private final String[] replacementAtStart;
        private final String[] replacementBeforeVowel;
        private final String[] replacementDefault;

        private boolean isVowel(char c) {
            return c == 'a' || c == 'e' || c == 'i' || c == 'o' || c == 'u';
        }

        protected Rule(String str, String str2, String str3, String str4) {
            this.pattern = str;
            this.replacementAtStart = str2.split("\\|");
            this.replacementBeforeVowel = str3.split("\\|");
            this.replacementDefault = str4.split("\\|");
        }

        public int getPatternLength() {
            return this.pattern.length();
        }

        public String[] getReplacements(String str, boolean z) {
            if (z) {
                return this.replacementAtStart;
            }
            int patternLength = getPatternLength();
            if (patternLength < str.length() ? isVowel(str.charAt(patternLength)) : false) {
                return this.replacementBeforeVowel;
            }
            return this.replacementDefault;
        }

        public boolean matches(String str) {
            return str.startsWith(this.pattern);
        }

        public String toString() {
            return String.format("%s=(%s,%s,%s)", new Object[]{this.pattern, Arrays.asList(this.replacementAtStart), Arrays.asList(this.replacementBeforeVowel), Arrays.asList(this.replacementDefault)});
        }
    }

    static {
        InputStream resourceAsStream = DaitchMokotoffSoundex.class.getClassLoader().getResourceAsStream(RESOURCE_FILE);
        if (resourceAsStream != null) {
            Scanner scanner = new Scanner(resourceAsStream, "UTF-8");
            parseRules(scanner, RESOURCE_FILE, RULES, FOLDINGS);
            scanner.close();
            for (Entry value : RULES.entrySet()) {
                Collections.sort((List) value.getValue(), new Comparator<Rule>() {
                    public int compare(Rule rule, Rule rule2) {
                        return rule2.getPatternLength() - rule.getPatternLength();
                    }
                });
            }
            return;
        }
        throw new IllegalArgumentException("Unable to load resource: org/apache/commons/codec/language/dmrules.txt");
    }

    private static void parseRules(Scanner scanner, String str, Map<Character, List<Rule>> map, Map<Character, Character> map2) {
        int i = 0;
        boolean z = false;
        while (scanner.hasNextLine()) {
            i++;
            String nextLine = scanner.nextLine();
            if (z) {
                if (nextLine.endsWith(MULTILINE_COMMENT_END)) {
                    z = false;
                }
            } else if (nextLine.startsWith(MULTILINE_COMMENT_START)) {
                z = true;
            } else {
                int indexOf = nextLine.indexOf(COMMENT);
                String trim = (indexOf >= 0 ? nextLine.substring(0, indexOf) : nextLine).trim();
                if (trim.length() == 0) {
                    continue;
                } else if (trim.contains("=")) {
                    String[] split = trim.split("=");
                    if (split.length == 2) {
                        String str2 = split[0];
                        String str3 = split[1];
                        if (str2.length() == 1 && str3.length() == 1) {
                            map2.put(Character.valueOf(str2.charAt(0)), Character.valueOf(str3.charAt(0)));
                        } else {
                            StringBuilder sb = new StringBuilder();
                            sb.append("Malformed folding statement - patterns are not single characters: ");
                            sb.append(nextLine);
                            sb.append(" in ");
                            sb.append(str);
                            throw new IllegalArgumentException(sb.toString());
                        }
                    } else {
                        StringBuilder sb2 = new StringBuilder();
                        sb2.append("Malformed folding statement split into ");
                        sb2.append(split.length);
                        sb2.append(" parts: ");
                        sb2.append(nextLine);
                        sb2.append(" in ");
                        sb2.append(str);
                        throw new IllegalArgumentException(sb2.toString());
                    }
                } else {
                    String[] split2 = trim.split("\\s+");
                    if (split2.length == 4) {
                        try {
                            Rule rule = new Rule(stripQuotes(split2[0]), stripQuotes(split2[1]), stripQuotes(split2[2]), stripQuotes(split2[3]));
                            char charAt = rule.pattern.charAt(0);
                            List list = (List) map.get(Character.valueOf(charAt));
                            if (list == null) {
                                list = new ArrayList();
                                map.put(Character.valueOf(charAt), list);
                            }
                            list.add(rule);
                        } catch (IllegalArgumentException e) {
                            StringBuilder sb3 = new StringBuilder();
                            sb3.append("Problem parsing line '");
                            sb3.append(i);
                            sb3.append("' in ");
                            sb3.append(str);
                            throw new IllegalStateException(sb3.toString(), e);
                        }
                    } else {
                        StringBuilder sb4 = new StringBuilder();
                        sb4.append("Malformed rule statement split into ");
                        sb4.append(split2.length);
                        sb4.append(" parts: ");
                        sb4.append(nextLine);
                        sb4.append(" in ");
                        sb4.append(str);
                        throw new IllegalArgumentException(sb4.toString());
                    }
                }
            }
        }
    }

    private static String stripQuotes(String str) {
        if (str.startsWith(DOUBLE_QUOTE)) {
            str = str.substring(1);
        }
        return str.endsWith(DOUBLE_QUOTE) ? str.substring(0, str.length() - 1) : str;
    }

    public DaitchMokotoffSoundex() {
        this(true);
    }

    public DaitchMokotoffSoundex(boolean z) {
        this.folding = z;
    }

    private String cleanup(String str) {
        char[] charArray;
        StringBuilder sb = new StringBuilder();
        for (char c : str.toCharArray()) {
            if (!Character.isWhitespace(c)) {
                char lowerCase = Character.toLowerCase(c);
                if (this.folding && FOLDINGS.containsKey(Character.valueOf(lowerCase))) {
                    lowerCase = ((Character) FOLDINGS.get(Character.valueOf(lowerCase))).charValue();
                }
                sb.append(lowerCase);
            }
        }
        return sb.toString();
    }

    public Object encode(Object obj) throws EncoderException {
        if (obj instanceof String) {
            return encode((String) obj);
        }
        throw new EncoderException("Parameter supplied to DaitchMokotoffSoundex encode is not of type java.lang.String");
    }

    public String encode(String str) {
        if (str == null) {
            return null;
        }
        return soundex(str, false)[0];
    }

    public String soundex(String str) {
        String[] soundex = soundex(str, true);
        StringBuilder sb = new StringBuilder();
        int i = 0;
        for (String append : soundex) {
            sb.append(append);
            i++;
            if (i < soundex.length) {
                sb.append('|');
            }
        }
        return sb.toString();
    }

    private String[] soundex(String str, boolean z) {
        String str2;
        int i;
        String str3;
        if (str == null) {
            return null;
        }
        String cleanup = cleanup(str);
        LinkedHashSet<Branch> linkedHashSet = new LinkedHashSet<>();
        linkedHashSet.add(new Branch());
        int i2 = 0;
        char c = 0;
        while (i2 < cleanup.length()) {
            char charAt = cleanup.charAt(i2);
            if (!Character.isWhitespace(charAt)) {
                String substring = cleanup.substring(i2);
                List list = (List) RULES.get(Character.valueOf(charAt));
                if (list != null) {
                    List arrayList = z ? new ArrayList() : Collections.EMPTY_LIST;
                    Iterator it = list.iterator();
                    while (true) {
                        if (!it.hasNext()) {
                            str2 = cleanup;
                            i = 1;
                            break;
                        }
                        Rule rule = (Rule) it.next();
                        if (rule.matches(substring)) {
                            if (z) {
                                arrayList.clear();
                            }
                            String[] replacements = rule.getReplacements(substring, c == 0);
                            boolean z2 = replacements.length > 1 && z;
                            for (Branch branch : linkedHashSet) {
                                int length = replacements.length;
                                int i3 = 0;
                                while (true) {
                                    if (i3 >= length) {
                                        str3 = cleanup;
                                        break;
                                    }
                                    String str4 = replacements[i3];
                                    Branch createBranch = z2 ? branch.createBranch() : branch;
                                    str3 = cleanup;
                                    createBranch.processNextReplacement(str4, (c == 'm' && charAt == 'n') || (c == 'n' && charAt == 'm'));
                                    if (!z) {
                                        break;
                                    }
                                    arrayList.add(createBranch);
                                    i3++;
                                    cleanup = str3;
                                }
                                cleanup = str3;
                            }
                            str2 = cleanup;
                            if (z) {
                                linkedHashSet.clear();
                                linkedHashSet.addAll(arrayList);
                            }
                            i = 1;
                            i2 += rule.getPatternLength() - 1;
                        }
                    }
                    c = charAt;
                    i2 += i;
                    cleanup = str2;
                }
            }
            str2 = cleanup;
            i = 1;
            i2 += i;
            cleanup = str2;
        }
        String[] strArr = new String[linkedHashSet.size()];
        int i4 = 0;
        for (Branch branch2 : linkedHashSet) {
            branch2.finish();
            int i5 = i4 + 1;
            strArr[i4] = branch2.toString();
            i4 = i5;
        }
        return strArr;
    }
}
