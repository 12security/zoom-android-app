package com.google.thirdparty.publicsuffix;

import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;
import com.google.common.collect.Lists;
import java.util.List;

@GwtCompatible
final class TrieParser {
    private static final Joiner PREFIX_JOINER = Joiner.m82on("");

    TrieParser() {
    }

    static ImmutableMap<String, PublicSuffixType> parseTrie(CharSequence charSequence) {
        Builder builder = ImmutableMap.builder();
        int length = charSequence.length();
        int i = 0;
        while (i < length) {
            i += doParseTrieToBuilder(Lists.newLinkedList(), charSequence.subSequence(i, length), builder);
        }
        return builder.build();
    }

    private static int doParseTrieToBuilder(List<CharSequence> list, CharSequence charSequence, Builder<String, PublicSuffixType> builder) {
        int length = charSequence.length();
        int i = 0;
        char c = 0;
        while (i < length) {
            c = charSequence.charAt(i);
            if (c == '&' || c == '?' || c == '!' || c == ':' || c == ',') {
                break;
            }
            i++;
        }
        list.add(0, reverse(charSequence.subSequence(0, i)));
        if (c == '!' || c == '?' || c == ':' || c == ',') {
            String join = PREFIX_JOINER.join((Iterable<?>) list);
            if (join.length() > 0) {
                builder.put(join, PublicSuffixType.fromCode(c));
            }
        }
        int i2 = i + 1;
        if (c != '?' && c != ',') {
            while (true) {
                if (i2 >= length) {
                    break;
                }
                i2 += doParseTrieToBuilder(list, charSequence.subSequence(i2, length), builder);
                if (charSequence.charAt(i2) != '?') {
                    if (charSequence.charAt(i2) == ',') {
                        break;
                    }
                } else {
                    break;
                }
            }
            i2++;
        }
        list.remove(0);
        return i2;
    }

    private static CharSequence reverse(CharSequence charSequence) {
        return new StringBuilder(charSequence).reverse();
    }
}
