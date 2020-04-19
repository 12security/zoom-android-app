package com.zipow.videobox.markdown;

import android.text.SpannableStringBuilder;
import androidx.annotation.NonNull;
import java.util.ArrayList;
import org.apache.http.message.TokenParser;

class Emitter {
    private int findToken(String str, int i, MarkToken markToken) {
        while (i < str.length()) {
            if (getToken(str, i) == markToken) {
                return i;
            }
            i++;
        }
        return -1;
    }

    public int readQuotes(@NonNull SpannableStringBuilder spannableStringBuilder, @NonNull String str, int i) {
        ArrayList<SpannableStringBuilder> arrayList = new ArrayList<>();
        while (true) {
            if (i >= str.length()) {
                break;
            }
            SpannableStringBuilder spannableStringBuilder2 = new SpannableStringBuilder();
            int readUntil = MarkDownUtils.readUntil(spannableStringBuilder2, str, i, 10);
            if (readUntil >= 0) {
                arrayList.add(spannableStringBuilder2);
                i = readUntil + 1;
                if (getToken(str, i) != MarkToken.ZM_QUOTES) {
                    i = readUntil;
                    break;
                }
            } else {
                break;
            }
        }
        if (arrayList.isEmpty()) {
            return -1;
        }
        for (SpannableStringBuilder append : arrayList) {
            spannableStringBuilder.append(append).append(10);
        }
        return i;
    }

    public int readLink(@NonNull SpannableStringBuilder spannableStringBuilder, @NonNull String str, int i) {
        SpannableStringBuilder spannableStringBuilder2 = new SpannableStringBuilder();
        int readUntil = MarkDownUtils.readUntil(spannableStringBuilder2, str, i, '>');
        if (readUntil <= i) {
            return -1;
        }
        spannableStringBuilder.append(spannableStringBuilder2);
        return readUntil;
    }

    public int recursiveEmitLine(@NonNull SpannableStringBuilder spannableStringBuilder, @NonNull String str, int i, MarkToken markToken) {
        SpannableStringBuilder spannableStringBuilder2 = new SpannableStringBuilder();
        while (i < str.length()) {
            MarkToken token = getToken(str, i);
            if (markToken != MarkToken.NONE && (token == markToken || ((markToken == MarkToken.EM_STAR && token == MarkToken.STRONG_STAR) || (markToken == MarkToken.EM_UNDERSCORE && token == MarkToken.STRONG_UNDERSCORE)))) {
                return i;
            }
            switch (token) {
                case ZM_QUOTES:
                    spannableStringBuilder2.clear();
                    int readQuotes = readQuotes(spannableStringBuilder2, str, i + 1);
                    if (readQuotes <= 0) {
                        spannableStringBuilder.append(str.charAt(i));
                        break;
                    } else {
                        EmitterDecorator.emitterQuotes(spannableStringBuilder, spannableStringBuilder2);
                        i = readQuotes;
                        continue;
                    }
                case ZM_LINK:
                    spannableStringBuilder2.clear();
                    int readLink = readLink(spannableStringBuilder2, str, i + 1);
                    if (readLink <= 0) {
                        spannableStringBuilder.append(str.charAt(i));
                        break;
                    } else {
                        EmitterDecorator.emitterHyperlink(spannableStringBuilder, spannableStringBuilder2.toString());
                        i = readLink;
                        continue;
                    }
                case ZM_MENTION_LINK:
                case ZM_PROFILE_LINK:
                    spannableStringBuilder2.clear();
                    int readLink2 = readLink(spannableStringBuilder2, str, i + 2);
                    if (readLink2 <= 0) {
                        spannableStringBuilder.append(str.charAt(i));
                        break;
                    } else {
                        if (token == MarkToken.ZM_MENTION_LINK) {
                            EmitterDecorator.emitterMentionLink(spannableStringBuilder, spannableStringBuilder2.toString());
                        } else {
                            EmitterDecorator.emitterProfileLink(spannableStringBuilder, spannableStringBuilder2.toString());
                        }
                        i = readLink2;
                        continue;
                    }
                case EM_STAR:
                case EM_UNDERSCORE:
                case ZM_MONOSPACE:
                case ZM_STRIKE:
                case CODE_SINGLE:
                    spannableStringBuilder2.clear();
                    int recursiveEmitLine = recursiveEmitLine(spannableStringBuilder2, str, i + 1, token);
                    if (recursiveEmitLine <= 0) {
                        spannableStringBuilder.append(str.charAt(i));
                        break;
                    } else {
                        if (token == MarkToken.EM_STAR) {
                            EmitterDecorator.emitterBold(spannableStringBuilder, spannableStringBuilder2);
                        } else if (token == MarkToken.EM_UNDERSCORE) {
                            EmitterDecorator.emitterItalic(spannableStringBuilder, spannableStringBuilder2);
                        } else if (token == MarkToken.ZM_STRIKE) {
                            EmitterDecorator.emitterStrikethrough(spannableStringBuilder, spannableStringBuilder2);
                        } else {
                            EmitterDecorator.emitterMonospace(spannableStringBuilder, spannableStringBuilder2);
                        }
                        i = recursiveEmitLine;
                        continue;
                    }
                case CODE_DOUBLE:
                    spannableStringBuilder2.clear();
                    int recursiveEmitLine2 = recursiveEmitLine(spannableStringBuilder2, str, i + 2, token);
                    if (recursiveEmitLine2 <= 0) {
                        spannableStringBuilder.append(str.charAt(i));
                        break;
                    } else {
                        EmitterDecorator.emitterMonospace(spannableStringBuilder, spannableStringBuilder2);
                        i = recursiveEmitLine2 + 1;
                        continue;
                    }
                case ESCAPE:
                    i++;
                    break;
            }
            spannableStringBuilder.append(str.charAt(i));
            i++;
        }
        return -1;
    }

    private static char whitespaceToSpace(char c) {
        return Character.isWhitespace(c) ? TokenParser.f498SP : c;
    }

    @NonNull
    private MarkToken getToken(String str, int i) {
        MarkToken markToken;
        MarkToken markToken2;
        char whitespaceToSpace = i > 0 ? whitespaceToSpace(str.charAt(i - 1)) : TokenParser.f498SP;
        char whitespaceToSpace2 = whitespaceToSpace(str.charAt(i));
        int i2 = i + 1;
        char whitespaceToSpace3 = i2 < str.length() ? whitespaceToSpace(str.charAt(i2)) : TokenParser.f498SP;
        int i3 = i + 2;
        char whitespaceToSpace4 = i3 < str.length() ? whitespaceToSpace(str.charAt(i3)) : TokenParser.f498SP;
        int i4 = i + 3;
        if (i4 < str.length()) {
            whitespaceToSpace(str.charAt(i4));
        }
        switch (whitespaceToSpace2) {
            case '!':
                if (whitespaceToSpace3 == '[') {
                    return MarkToken.IMAGE;
                }
                return MarkToken.NONE;
            case '&':
                return MarkToken.ENTITY;
            case '\'':
                return MarkToken.ZM_MONOSPACE;
            case '*':
                if (whitespaceToSpace3 == '*') {
                    return (whitespaceToSpace == ' ' && whitespaceToSpace4 == ' ') ? MarkToken.EM_STAR : MarkToken.STRONG_STAR;
                }
                if (whitespaceToSpace == ' ' && whitespaceToSpace3 == ' ') {
                    markToken = MarkToken.NONE;
                } else {
                    markToken = MarkToken.EM_STAR;
                }
                return markToken;
            case '<':
                if (whitespaceToSpace3 == '!') {
                    return MarkToken.ZM_MENTION_LINK;
                }
                if (whitespaceToSpace3 == '#') {
                    return MarkToken.ZM_PROFILE_LINK;
                }
                return MarkToken.ZM_LINK;
            case '>':
                return MarkToken.ZM_QUOTES;
            case '[':
                return MarkToken.LINK;
            case '\\':
                if (!(whitespaceToSpace3 == '<' || whitespaceToSpace3 == '>' || whitespaceToSpace3 == '{' || whitespaceToSpace3 == '}')) {
                    switch (whitespaceToSpace3) {
                        case '!':
                        case '\"':
                        case '#':
                            break;
                        default:
                            switch (whitespaceToSpace3) {
                                case '\'':
                                case '(':
                                case ')':
                                case '*':
                                case '+':
                                    break;
                                default:
                                    switch (whitespaceToSpace3) {
                                        case '-':
                                        case '.':
                                            break;
                                        default:
                                            switch (whitespaceToSpace3) {
                                                case '[':
                                                case '\\':
                                                case ']':
                                                case '^':
                                                case '_':
                                                case '`':
                                                    break;
                                                default:
                                                    return MarkToken.NONE;
                                            }
                                    }
                            }
                    }
                }
                return MarkToken.ESCAPE;
            case ']':
                return MarkToken.NONE;
            case '_':
                if (whitespaceToSpace3 == '_') {
                    return (whitespaceToSpace == ' ' && whitespaceToSpace4 == ' ') ? MarkToken.EM_UNDERSCORE : MarkToken.STRONG_UNDERSCORE;
                }
                return (whitespaceToSpace == ' ' && whitespaceToSpace3 == ' ') ? MarkToken.NONE : MarkToken.EM_UNDERSCORE;
            case '`':
                if (whitespaceToSpace3 == '`') {
                    markToken2 = MarkToken.CODE_DOUBLE;
                } else {
                    markToken2 = MarkToken.CODE_SINGLE;
                }
                return markToken2;
            case '~':
                return MarkToken.ZM_STRIKE;
            default:
                return MarkToken.NONE;
        }
    }
}
