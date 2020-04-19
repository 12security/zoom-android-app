package org.apache.commons.codec.language.p018bm;

import com.microsoft.aad.adal.AuthenticationConstants.Broker;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import org.apache.commons.codec.language.p018bm.Languages.LanguageSet;
import org.apache.commons.codec.language.p018bm.Rule.Phoneme;
import org.apache.commons.codec.language.p018bm.Rule.PhonemeExpr;
import org.apache.http.message.TokenParser;

/* renamed from: org.apache.commons.codec.language.bm.PhoneticEngine */
public class PhoneticEngine {
    private static final int DEFAULT_MAX_PHONEMES = 20;
    private static final Map<NameType, Set<String>> NAME_PREFIXES = new EnumMap(NameType.class);
    private final boolean concat;
    private final Lang lang;
    private final int maxPhonemes;
    private final NameType nameType;
    private final RuleType ruleType;

    /* renamed from: org.apache.commons.codec.language.bm.PhoneticEngine$PhonemeBuilder */
    static final class PhonemeBuilder {
        private final Set<Phoneme> phonemes;

        public static PhonemeBuilder empty(LanguageSet languageSet) {
            return new PhonemeBuilder(new Phoneme((CharSequence) "", languageSet));
        }

        private PhonemeBuilder(Phoneme phoneme) {
            this.phonemes = new LinkedHashSet();
            this.phonemes.add(phoneme);
        }

        private PhonemeBuilder(Set<Phoneme> set) {
            this.phonemes = set;
        }

        public void append(CharSequence charSequence) {
            for (Phoneme append : this.phonemes) {
                append.append(charSequence);
            }
        }

        public void apply(PhonemeExpr phonemeExpr, int i) {
            LinkedHashSet linkedHashSet = new LinkedHashSet(i);
            loop0:
            for (Phoneme phoneme : this.phonemes) {
                Iterator it = phonemeExpr.getPhonemes().iterator();
                while (true) {
                    if (it.hasNext()) {
                        Phoneme phoneme2 = (Phoneme) it.next();
                        LanguageSet restrictTo = phoneme.getLanguages().restrictTo(phoneme2.getLanguages());
                        if (!restrictTo.isEmpty()) {
                            Phoneme phoneme3 = new Phoneme(phoneme, phoneme2, restrictTo);
                            if (linkedHashSet.size() < i) {
                                linkedHashSet.add(phoneme3);
                                if (linkedHashSet.size() >= i) {
                                    break loop0;
                                }
                            } else {
                                continue;
                            }
                        }
                    }
                }
            }
            this.phonemes.clear();
            this.phonemes.addAll(linkedHashSet);
        }

        public Set<Phoneme> getPhonemes() {
            return this.phonemes;
        }

        public String makeString() {
            StringBuilder sb = new StringBuilder();
            for (Phoneme phoneme : this.phonemes) {
                if (sb.length() > 0) {
                    sb.append(Broker.CALLER_CACHEKEY_PREFIX);
                }
                sb.append(phoneme.getPhonemeText());
            }
            return sb.toString();
        }
    }

    /* renamed from: org.apache.commons.codec.language.bm.PhoneticEngine$RulesApplication */
    private static final class RulesApplication {
        private final Map<String, List<Rule>> finalRules;
        private boolean found;

        /* renamed from: i */
        private int f474i;
        private final CharSequence input;
        private final int maxPhonemes;
        private PhonemeBuilder phonemeBuilder;

        public RulesApplication(Map<String, List<Rule>> map, CharSequence charSequence, PhonemeBuilder phonemeBuilder2, int i, int i2) {
            if (map != null) {
                this.finalRules = map;
                this.phonemeBuilder = phonemeBuilder2;
                this.input = charSequence;
                this.f474i = i;
                this.maxPhonemes = i2;
                return;
            }
            throw new NullPointerException("The finalRules argument must not be null");
        }

        public int getI() {
            return this.f474i;
        }

        public PhonemeBuilder getPhonemeBuilder() {
            return this.phonemeBuilder;
        }

        public RulesApplication invoke() {
            int i;
            this.found = false;
            Map<String, List<Rule>> map = this.finalRules;
            CharSequence charSequence = this.input;
            int i2 = this.f474i;
            List list = (List) map.get(charSequence.subSequence(i2, i2 + 1));
            int i3 = 1;
            if (list != null) {
                Iterator it = list.iterator();
                i = 1;
                while (true) {
                    if (!it.hasNext()) {
                        break;
                    }
                    Rule rule = (Rule) it.next();
                    int length = rule.getPattern().length();
                    if (rule.patternAndContextMatches(this.input, this.f474i)) {
                        this.phonemeBuilder.apply(rule.getPhoneme(), this.maxPhonemes);
                        this.found = true;
                        i = length;
                        break;
                    }
                    i = length;
                }
            } else {
                i = 1;
            }
            if (this.found) {
                i3 = i;
            }
            this.f474i += i3;
            return this;
        }

        public boolean isFound() {
            return this.found;
        }
    }

    static {
        NAME_PREFIXES.put(NameType.ASHKENAZI, Collections.unmodifiableSet(new HashSet(Arrays.asList(new String[]{"bar", "ben", "da", "de", "van", "von"}))));
        NAME_PREFIXES.put(NameType.SEPHARDIC, Collections.unmodifiableSet(new HashSet(Arrays.asList(new String[]{"al", "el", "da", "dal", "de", "del", "dela", "de la", "della", "des", "di", "do", "dos", "du", "van", "von"}))));
        NAME_PREFIXES.put(NameType.GENERIC, Collections.unmodifiableSet(new HashSet(Arrays.asList(new String[]{"da", "dal", "de", "del", "dela", "de la", "della", "des", "di", "do", "dos", "du", "van", "von"}))));
    }

    private static String join(Iterable<String> iterable, String str) {
        StringBuilder sb = new StringBuilder();
        Iterator it = iterable.iterator();
        if (it.hasNext()) {
            sb.append((String) it.next());
        }
        while (it.hasNext()) {
            sb.append(str);
            sb.append((String) it.next());
        }
        return sb.toString();
    }

    public PhoneticEngine(NameType nameType2, RuleType ruleType2, boolean z) {
        this(nameType2, ruleType2, z, 20);
    }

    public PhoneticEngine(NameType nameType2, RuleType ruleType2, boolean z, int i) {
        if (ruleType2 != RuleType.RULES) {
            this.nameType = nameType2;
            this.ruleType = ruleType2;
            this.concat = z;
            this.lang = Lang.instance(nameType2);
            this.maxPhonemes = i;
            return;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("ruleType must not be ");
        sb.append(RuleType.RULES);
        throw new IllegalArgumentException(sb.toString());
    }

    private PhonemeBuilder applyFinalRules(PhonemeBuilder phonemeBuilder, Map<String, List<Rule>> map) {
        if (map == null) {
            throw new NullPointerException("finalRules can not be null");
        } else if (map.isEmpty()) {
            return phonemeBuilder;
        } else {
            TreeMap treeMap = new TreeMap(Phoneme.COMPARATOR);
            for (Phoneme phoneme : phonemeBuilder.getPhonemes()) {
                PhonemeBuilder empty = PhonemeBuilder.empty(phoneme.getLanguages());
                String charSequence = phoneme.getPhonemeText().toString();
                PhonemeBuilder phonemeBuilder2 = empty;
                int i = 0;
                while (i < charSequence.length()) {
                    RulesApplication rulesApplication = new RulesApplication(map, charSequence, phonemeBuilder2, i, this.maxPhonemes);
                    RulesApplication invoke = rulesApplication.invoke();
                    boolean isFound = invoke.isFound();
                    phonemeBuilder2 = invoke.getPhonemeBuilder();
                    if (!isFound) {
                        phonemeBuilder2.append(charSequence.subSequence(i, i + 1));
                    }
                    i = invoke.getI();
                }
                for (Phoneme phoneme2 : phonemeBuilder2.getPhonemes()) {
                    if (treeMap.containsKey(phoneme2)) {
                        Phoneme mergeWithLanguage = ((Phoneme) treeMap.remove(phoneme2)).mergeWithLanguage(phoneme2.getLanguages());
                        treeMap.put(mergeWithLanguage, mergeWithLanguage);
                    } else {
                        treeMap.put(phoneme2, phoneme2);
                    }
                }
            }
            return new PhonemeBuilder(treeMap.keySet());
        }
    }

    public String encode(String str) {
        return encode(str, this.lang.guessLanguages(str));
    }

    public String encode(String str, LanguageSet languageSet) {
        String str2;
        Map instanceMap = Rule.getInstanceMap(this.nameType, RuleType.RULES, languageSet);
        Map instanceMap2 = Rule.getInstanceMap(this.nameType, this.ruleType, "common");
        Map instanceMap3 = Rule.getInstanceMap(this.nameType, this.ruleType, languageSet);
        String trim = str.toLowerCase(Locale.ENGLISH).replace('-', TokenParser.f498SP).trim();
        if (this.nameType == NameType.GENERIC) {
            if (trim.length() < 2 || !trim.substring(0, 2).equals("d'")) {
                for (String str3 : (Set) NAME_PREFIXES.get(this.nameType)) {
                    StringBuilder sb = new StringBuilder();
                    sb.append(str3);
                    sb.append(OAuth.SCOPE_DELIMITER);
                    if (trim.startsWith(sb.toString())) {
                        String substring = trim.substring(str3.length() + 1);
                        StringBuilder sb2 = new StringBuilder();
                        sb2.append(str3);
                        sb2.append(substring);
                        String sb3 = sb2.toString();
                        StringBuilder sb4 = new StringBuilder();
                        sb4.append("(");
                        sb4.append(encode(substring));
                        sb4.append(")-(");
                        sb4.append(encode(sb3));
                        sb4.append(")");
                        return sb4.toString();
                    }
                }
            } else {
                String substring2 = trim.substring(2);
                StringBuilder sb5 = new StringBuilder();
                sb5.append("d");
                sb5.append(substring2);
                String sb6 = sb5.toString();
                StringBuilder sb7 = new StringBuilder();
                sb7.append("(");
                sb7.append(encode(substring2));
                sb7.append(")-(");
                sb7.append(encode(sb6));
                sb7.append(")");
                return sb7.toString();
            }
        }
        List<String> asList = Arrays.asList(trim.split("\\s+"));
        ArrayList<String> arrayList = new ArrayList<>();
        switch (this.nameType) {
            case SEPHARDIC:
                for (String split : asList) {
                    String[] split2 = split.split("'");
                    arrayList.add(split2[split2.length - 1]);
                }
                arrayList.removeAll((Collection) NAME_PREFIXES.get(this.nameType));
                break;
            case ASHKENAZI:
                arrayList.addAll(asList);
                arrayList.removeAll((Collection) NAME_PREFIXES.get(this.nameType));
                break;
            case GENERIC:
                arrayList.addAll(asList);
                break;
            default:
                StringBuilder sb8 = new StringBuilder();
                sb8.append("Unreachable case: ");
                sb8.append(this.nameType);
                throw new IllegalStateException(sb8.toString());
        }
        if (this.concat) {
            str2 = join(arrayList, OAuth.SCOPE_DELIMITER);
        } else if (arrayList.size() == 1) {
            str2 = (String) asList.iterator().next();
        } else {
            StringBuilder sb9 = new StringBuilder();
            for (String str4 : arrayList) {
                sb9.append("-");
                sb9.append(encode(str4));
            }
            return sb9.substring(1);
        }
        PhonemeBuilder empty = PhonemeBuilder.empty(languageSet);
        int i = 0;
        while (i < str2.length()) {
            RulesApplication rulesApplication = new RulesApplication(instanceMap, str2, empty, i, this.maxPhonemes);
            RulesApplication invoke = rulesApplication.invoke();
            i = invoke.getI();
            empty = invoke.getPhonemeBuilder();
        }
        return applyFinalRules(applyFinalRules(empty, instanceMap2), instanceMap3).makeString();
    }

    public Lang getLang() {
        return this.lang;
    }

    public NameType getNameType() {
        return this.nameType;
    }

    public RuleType getRuleType() {
        return this.ruleType;
    }

    public boolean isConcat() {
        return this.concat;
    }

    public int getMaxPhonemes() {
        return this.maxPhonemes;
    }
}
