package org.apache.commons.codec.language;

import androidx.exifinterface.media.ExifInterface;
import java.util.Locale;
import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringEncoder;
import org.apache.commons.codec.binary.StringUtils;
import org.apache.http.message.TokenParser;

public class DoubleMetaphone implements StringEncoder {
    private static final String[] ES_EP_EB_EL_EY_IB_IL_IN_IE_EI_ER = {"ES", "EP", "EB", "EL", "EY", "IB", "IL", "IN", "IE", "EI", "ER"};
    private static final String[] L_R_N_M_B_H_F_V_W_SPACE = {"L", "R", "N", "M", "B", "H", "F", ExifInterface.GPS_MEASUREMENT_INTERRUPTED, ExifInterface.LONGITUDE_WEST, OAuth.SCOPE_DELIMITER};
    private static final String[] L_T_K_S_N_M_B_Z = {"L", ExifInterface.GPS_DIRECTION_TRUE, "K", ExifInterface.LATITUDE_SOUTH, "N", "M", "B", "Z"};
    private static final String[] SILENT_START = {"GN", "KN", "PN", "WR", "PS"};
    private static final String VOWELS = "AEIOUY";
    private int maxCodeLen = 4;

    public class DoubleMetaphoneResult {
        private final StringBuilder alternate = new StringBuilder(DoubleMetaphone.this.getMaxCodeLen());
        private final int maxLength;
        private final StringBuilder primary = new StringBuilder(DoubleMetaphone.this.getMaxCodeLen());

        public DoubleMetaphoneResult(int i) {
            this.maxLength = i;
        }

        public void append(char c) {
            appendPrimary(c);
            appendAlternate(c);
        }

        public void append(char c, char c2) {
            appendPrimary(c);
            appendAlternate(c2);
        }

        public void appendPrimary(char c) {
            if (this.primary.length() < this.maxLength) {
                this.primary.append(c);
            }
        }

        public void appendAlternate(char c) {
            if (this.alternate.length() < this.maxLength) {
                this.alternate.append(c);
            }
        }

        public void append(String str) {
            appendPrimary(str);
            appendAlternate(str);
        }

        public void append(String str, String str2) {
            appendPrimary(str);
            appendAlternate(str2);
        }

        public void appendPrimary(String str) {
            int length = this.maxLength - this.primary.length();
            if (str.length() <= length) {
                this.primary.append(str);
            } else {
                this.primary.append(str.substring(0, length));
            }
        }

        public void appendAlternate(String str) {
            int length = this.maxLength - this.alternate.length();
            if (str.length() <= length) {
                this.alternate.append(str);
            } else {
                this.alternate.append(str.substring(0, length));
            }
        }

        public String getPrimary() {
            return this.primary.toString();
        }

        public String getAlternate() {
            return this.alternate.toString();
        }

        public boolean isComplete() {
            return this.primary.length() >= this.maxLength && this.alternate.length() >= this.maxLength;
        }
    }

    public String doubleMetaphone(String str) {
        return doubleMetaphone(str, false);
    }

    public String doubleMetaphone(String str, boolean z) {
        String cleanInput = cleanInput(str);
        if (cleanInput == null) {
            return null;
        }
        boolean isSlavoGermanic = isSlavoGermanic(cleanInput);
        int isSilentStart = isSilentStart(cleanInput);
        DoubleMetaphoneResult doubleMetaphoneResult = new DoubleMetaphoneResult(getMaxCodeLen());
        while (!doubleMetaphoneResult.isComplete() && isSilentStart <= cleanInput.length() - 1) {
            char charAt = cleanInput.charAt(isSilentStart);
            if (charAt == 199) {
                doubleMetaphoneResult.append('S');
                isSilentStart++;
            } else if (charAt != 209) {
                switch (charAt) {
                    case 'A':
                    case 'E':
                    case 'I':
                    case 'O':
                    case 'U':
                    case 'Y':
                        isSilentStart = handleAEIOUY(doubleMetaphoneResult, isSilentStart);
                        break;
                    case 'B':
                        doubleMetaphoneResult.append('P');
                        int i = isSilentStart + 1;
                        if (charAt(cleanInput, i) != 'B') {
                            isSilentStart = i;
                            break;
                        } else {
                            isSilentStart += 2;
                            break;
                        }
                    case 'C':
                        isSilentStart = handleC(cleanInput, doubleMetaphoneResult, isSilentStart);
                        break;
                    case 'D':
                        isSilentStart = handleD(cleanInput, doubleMetaphoneResult, isSilentStart);
                        break;
                    case 'F':
                        doubleMetaphoneResult.append('F');
                        int i2 = isSilentStart + 1;
                        if (charAt(cleanInput, i2) != 'F') {
                            isSilentStart = i2;
                            break;
                        } else {
                            isSilentStart += 2;
                            break;
                        }
                    case 'G':
                        isSilentStart = handleG(cleanInput, doubleMetaphoneResult, isSilentStart, isSlavoGermanic);
                        break;
                    case 'H':
                        isSilentStart = handleH(cleanInput, doubleMetaphoneResult, isSilentStart);
                        break;
                    case 'J':
                        isSilentStart = handleJ(cleanInput, doubleMetaphoneResult, isSilentStart, isSlavoGermanic);
                        break;
                    case 'K':
                        doubleMetaphoneResult.append('K');
                        int i3 = isSilentStart + 1;
                        if (charAt(cleanInput, i3) != 'K') {
                            isSilentStart = i3;
                            break;
                        } else {
                            isSilentStart += 2;
                            break;
                        }
                    case 'L':
                        isSilentStart = handleL(cleanInput, doubleMetaphoneResult, isSilentStart);
                        break;
                    case 'M':
                        doubleMetaphoneResult.append('M');
                        if (!conditionM0(cleanInput, isSilentStart)) {
                            isSilentStart++;
                            break;
                        } else {
                            isSilentStart += 2;
                            break;
                        }
                    case 'N':
                        doubleMetaphoneResult.append('N');
                        int i4 = isSilentStart + 1;
                        if (charAt(cleanInput, i4) != 'N') {
                            isSilentStart = i4;
                            break;
                        } else {
                            isSilentStart += 2;
                            break;
                        }
                    case 'P':
                        isSilentStart = handleP(cleanInput, doubleMetaphoneResult, isSilentStart);
                        break;
                    case 'Q':
                        doubleMetaphoneResult.append('K');
                        int i5 = isSilentStart + 1;
                        if (charAt(cleanInput, i5) != 'Q') {
                            isSilentStart = i5;
                            break;
                        } else {
                            isSilentStart += 2;
                            break;
                        }
                    case 'R':
                        isSilentStart = handleR(cleanInput, doubleMetaphoneResult, isSilentStart, isSlavoGermanic);
                        break;
                    case 'S':
                        isSilentStart = handleS(cleanInput, doubleMetaphoneResult, isSilentStart, isSlavoGermanic);
                        break;
                    case 'T':
                        isSilentStart = handleT(cleanInput, doubleMetaphoneResult, isSilentStart);
                        break;
                    case 'V':
                        doubleMetaphoneResult.append('F');
                        int i6 = isSilentStart + 1;
                        if (charAt(cleanInput, i6) != 'V') {
                            isSilentStart = i6;
                            break;
                        } else {
                            isSilentStart += 2;
                            break;
                        }
                    case 'W':
                        isSilentStart = handleW(cleanInput, doubleMetaphoneResult, isSilentStart);
                        break;
                    case 'X':
                        isSilentStart = handleX(cleanInput, doubleMetaphoneResult, isSilentStart);
                        break;
                    case 'Z':
                        isSilentStart = handleZ(cleanInput, doubleMetaphoneResult, isSilentStart, isSlavoGermanic);
                        break;
                    default:
                        isSilentStart++;
                        break;
                }
            } else {
                doubleMetaphoneResult.append('N');
                isSilentStart++;
            }
        }
        return z ? doubleMetaphoneResult.getAlternate() : doubleMetaphoneResult.getPrimary();
    }

    public Object encode(Object obj) throws EncoderException {
        if (obj instanceof String) {
            return doubleMetaphone((String) obj);
        }
        throw new EncoderException("DoubleMetaphone encode parameter is not of type String");
    }

    public String encode(String str) {
        return doubleMetaphone(str);
    }

    public boolean isDoubleMetaphoneEqual(String str, String str2) {
        return isDoubleMetaphoneEqual(str, str2, false);
    }

    public boolean isDoubleMetaphoneEqual(String str, String str2, boolean z) {
        return StringUtils.equals(doubleMetaphone(str, z), doubleMetaphone(str2, z));
    }

    public int getMaxCodeLen() {
        return this.maxCodeLen;
    }

    public void setMaxCodeLen(int i) {
        this.maxCodeLen = i;
    }

    private int handleAEIOUY(DoubleMetaphoneResult doubleMetaphoneResult, int i) {
        if (i == 0) {
            doubleMetaphoneResult.append('A');
        }
        return i + 1;
    }

    private int handleC(String str, DoubleMetaphoneResult doubleMetaphoneResult, int i) {
        int i2;
        if (conditionC0(str, i)) {
            doubleMetaphoneResult.append('K');
            i2 = i + 2;
        } else if (i == 0 && contains(str, i, 6, "CAESAR")) {
            doubleMetaphoneResult.append('S');
            i2 = i + 2;
        } else if (contains(str, i, 2, "CH")) {
            i2 = handleCH(str, doubleMetaphoneResult, i);
        } else if (!contains(str, i, 2, "CZ") || contains(str, i - 2, 4, "WICZ")) {
            int i3 = i + 1;
            if (contains(str, i3, 3, "CIA")) {
                doubleMetaphoneResult.append('X');
                i2 = i + 3;
            } else if (contains(str, i, 2, "CC") && (i != 1 || charAt(str, 0) != 'M')) {
                return handleCC(str, doubleMetaphoneResult, i);
            } else {
                if (contains(str, i, 2, "CK", "CG", "CQ")) {
                    doubleMetaphoneResult.append('K');
                    i2 = i + 2;
                } else if (contains(str, i, 2, "CI", "CE", "CY")) {
                    if (contains(str, i, 3, "CIO", "CIE", "CIA")) {
                        doubleMetaphoneResult.append('S', 'X');
                    } else {
                        doubleMetaphoneResult.append('S');
                    }
                    i2 = i + 2;
                } else {
                    doubleMetaphoneResult.append('K');
                    i2 = contains(str, i3, 2, " C", " Q", " G") ? i + 3 : (!contains(str, i3, 1, "C", "K", "Q") || contains(str, i3, 2, "CE", "CI")) ? i3 : i + 2;
                }
            }
        } else {
            doubleMetaphoneResult.append('S', 'X');
            i2 = i + 2;
        }
        return i2;
    }

    private int handleCC(String str, DoubleMetaphoneResult doubleMetaphoneResult, int i) {
        int i2 = i + 2;
        if (!contains(str, i2, 1, "I", ExifInterface.LONGITUDE_EAST, "H") || contains(str, i2, 2, "HU")) {
            doubleMetaphoneResult.append('K');
            return i2;
        }
        if (!(i == 1 && charAt(str, i - 1) == 'A') && !contains(str, i - 1, 5, "UCCEE", "UCCES")) {
            doubleMetaphoneResult.append('X');
        } else {
            doubleMetaphoneResult.append("KS");
        }
        return i + 3;
    }

    private int handleCH(String str, DoubleMetaphoneResult doubleMetaphoneResult, int i) {
        if (i > 0 && contains(str, i, 4, "CHAE")) {
            doubleMetaphoneResult.append('K', 'X');
            return i + 2;
        } else if (conditionCH0(str, i)) {
            doubleMetaphoneResult.append('K');
            return i + 2;
        } else if (conditionCH1(str, i)) {
            doubleMetaphoneResult.append('K');
            return i + 2;
        } else {
            if (i <= 0) {
                doubleMetaphoneResult.append('X');
            } else if (contains(str, 0, 2, "MC")) {
                doubleMetaphoneResult.append('K');
            } else {
                doubleMetaphoneResult.append('X', 'K');
            }
            return i + 2;
        }
    }

    private int handleD(String str, DoubleMetaphoneResult doubleMetaphoneResult, int i) {
        if (contains(str, i, 2, "DG")) {
            int i2 = i + 2;
            if (contains(str, i2, 1, "I", ExifInterface.LONGITUDE_EAST, "Y")) {
                doubleMetaphoneResult.append('J');
                return i + 3;
            }
            doubleMetaphoneResult.append("TK");
            return i2;
        } else if (contains(str, i, 2, "DT", "DD")) {
            doubleMetaphoneResult.append('T');
            return i + 2;
        } else {
            doubleMetaphoneResult.append('T');
            return i + 1;
        }
    }

    private int handleG(String str, DoubleMetaphoneResult doubleMetaphoneResult, int i, boolean z) {
        int i2 = i + 1;
        if (charAt(str, i2) == 'H') {
            return handleGH(str, doubleMetaphoneResult, i);
        }
        if (charAt(str, i2) == 'N') {
            if (i == 1 && isVowel(charAt(str, 0)) && !z) {
                doubleMetaphoneResult.append("KN", "N");
            } else if (contains(str, i + 2, 2, "EY") || charAt(str, i2) == 'Y' || z) {
                doubleMetaphoneResult.append("KN");
            } else {
                doubleMetaphoneResult.append("N", "KN");
            }
            return i + 2;
        } else if (contains(str, i2, 2, "LI") && !z) {
            doubleMetaphoneResult.append("KL", "L");
            return i + 2;
        } else if (i != 0 || (charAt(str, i2) != 'Y' && !contains(str, i2, 2, ES_EP_EB_EL_EY_IB_IL_IN_IE_EI_ER))) {
            if ((contains(str, i2, 2, "ER") || charAt(str, i2) == 'Y') && !contains(str, 0, 6, "DANGER", "RANGER", "MANGER")) {
                int i3 = i - 1;
                if (!contains(str, i3, 1, ExifInterface.LONGITUDE_EAST, "I") && !contains(str, i3, 3, "RGY", "OGY")) {
                    doubleMetaphoneResult.append('K', 'J');
                    return i + 2;
                }
            }
            if (contains(str, i2, 1, ExifInterface.LONGITUDE_EAST, "I", "Y") || contains(str, i - 1, 4, "AGGI", "OGGI")) {
                if (contains(str, 0, 4, "VAN ", "VON ") || contains(str, 0, 3, "SCH") || contains(str, i2, 2, "ET")) {
                    doubleMetaphoneResult.append('K');
                } else if (contains(str, i2, 3, "IER")) {
                    doubleMetaphoneResult.append('J');
                } else {
                    doubleMetaphoneResult.append('J', 'K');
                }
                return i + 2;
            } else if (charAt(str, i2) == 'G') {
                int i4 = i + 2;
                doubleMetaphoneResult.append('K');
                return i4;
            } else {
                doubleMetaphoneResult.append('K');
                return i2;
            }
        } else {
            doubleMetaphoneResult.append('K', 'J');
            return i + 2;
        }
    }

    private int handleGH(String str, DoubleMetaphoneResult doubleMetaphoneResult, int i) {
        if (i > 0 && !isVowel(charAt(str, i - 1))) {
            doubleMetaphoneResult.append('K');
            return i + 2;
        } else if (i == 0) {
            int i2 = i + 2;
            if (charAt(str, i2) == 'I') {
                doubleMetaphoneResult.append('J');
                return i2;
            }
            doubleMetaphoneResult.append('K');
            return i2;
        } else if ((i > 1 && contains(str, i - 2, 1, "B", "H", "D")) || ((i > 2 && contains(str, i - 3, 1, "B", "H", "D")) || (i > 3 && contains(str, i - 4, 1, "B", "H")))) {
            return i + 2;
        } else {
            if (i > 2 && charAt(str, i - 1) == 'U' && contains(str, i - 3, 1, "C", "G", "L", "R", ExifInterface.GPS_DIRECTION_TRUE)) {
                doubleMetaphoneResult.append('F');
            } else if (i > 0 && charAt(str, i - 1) != 'I') {
                doubleMetaphoneResult.append('K');
            }
            return i + 2;
        }
    }

    private int handleH(String str, DoubleMetaphoneResult doubleMetaphoneResult, int i) {
        if ((i != 0 && !isVowel(charAt(str, i - 1))) || !isVowel(charAt(str, i + 1))) {
            return i + 1;
        }
        doubleMetaphoneResult.append('H');
        return i + 2;
    }

    private int handleJ(String str, DoubleMetaphoneResult doubleMetaphoneResult, int i, boolean z) {
        if (contains(str, i, 4, "JOSE") || contains(str, 0, 4, "SAN ")) {
            if ((i == 0 && charAt(str, i + 4) == ' ') || str.length() == 4 || contains(str, 0, 4, "SAN ")) {
                doubleMetaphoneResult.append('H');
            } else {
                doubleMetaphoneResult.append('J', 'H');
            }
            return i + 1;
        }
        if (i != 0 || contains(str, i, 4, "JOSE")) {
            int i2 = i - 1;
            if (isVowel(charAt(str, i2)) && !z) {
                int i3 = i + 1;
                if (charAt(str, i3) == 'A' || charAt(str, i3) == 'O') {
                    doubleMetaphoneResult.append('J', 'H');
                }
            }
            if (i == str.length() - 1) {
                doubleMetaphoneResult.append('J', (char) TokenParser.f498SP);
            } else if (!contains(str, i + 1, 1, L_T_K_S_N_M_B_Z) && !contains(str, i2, 1, ExifInterface.LATITUDE_SOUTH, "K", "L")) {
                doubleMetaphoneResult.append('J');
            }
        } else {
            doubleMetaphoneResult.append('J', 'A');
        }
        int i4 = i + 1;
        if (charAt(str, i4) == 'J') {
            return i + 2;
        }
        return i4;
    }

    private int handleL(String str, DoubleMetaphoneResult doubleMetaphoneResult, int i) {
        int i2 = i + 1;
        if (charAt(str, i2) == 'L') {
            if (conditionL0(str, i)) {
                doubleMetaphoneResult.appendPrimary('L');
            } else {
                doubleMetaphoneResult.append('L');
            }
            return i + 2;
        }
        doubleMetaphoneResult.append('L');
        return i2;
    }

    private int handleP(String str, DoubleMetaphoneResult doubleMetaphoneResult, int i) {
        int i2 = i + 1;
        if (charAt(str, i2) == 'H') {
            doubleMetaphoneResult.append('F');
            return i + 2;
        }
        doubleMetaphoneResult.append('P');
        if (contains(str, i2, 1, "P", "B")) {
            i2 = i + 2;
        }
        return i2;
    }

    private int handleR(String str, DoubleMetaphoneResult doubleMetaphoneResult, int i, boolean z) {
        if (i != str.length() - 1 || z || !contains(str, i - 2, 2, "IE") || contains(str, i - 4, 2, "ME", "MA")) {
            doubleMetaphoneResult.append('R');
        } else {
            doubleMetaphoneResult.appendAlternate('R');
        }
        int i2 = i + 1;
        return charAt(str, i2) == 'R' ? i + 2 : i2;
    }

    private int handleS(String str, DoubleMetaphoneResult doubleMetaphoneResult, int i, boolean z) {
        if (contains(str, i - 1, 3, "ISL", "YSL")) {
            return i + 1;
        }
        if (i == 0 && contains(str, i, 5, "SUGAR")) {
            doubleMetaphoneResult.append('X', 'S');
            return i + 1;
        } else if (contains(str, i, 2, "SH")) {
            if (contains(str, i + 1, 4, "HEIM", "HOEK", "HOLM", "HOLZ")) {
                doubleMetaphoneResult.append('S');
            } else {
                doubleMetaphoneResult.append('X');
            }
            return i + 2;
        } else if (contains(str, i, 3, "SIO", "SIA") || contains(str, i, 4, "SIAN")) {
            if (z) {
                doubleMetaphoneResult.append('S');
            } else {
                doubleMetaphoneResult.append('S', 'X');
            }
            return i + 3;
        } else {
            if (i != 0 || !contains(str, i + 1, 1, "M", "N", "L", ExifInterface.LONGITUDE_WEST)) {
                int i2 = i + 1;
                if (!contains(str, i2, 1, "Z")) {
                    if (contains(str, i, 2, "SC")) {
                        return handleSC(str, doubleMetaphoneResult, i);
                    }
                    if (i != str.length() - 1 || !contains(str, i - 2, 2, "AI", "OI")) {
                        doubleMetaphoneResult.append('S');
                    } else {
                        doubleMetaphoneResult.appendAlternate('S');
                    }
                    return contains(str, i2, 1, ExifInterface.LATITUDE_SOUTH, "Z") ? i + 2 : i2;
                }
            }
            doubleMetaphoneResult.append('S', 'X');
            int i3 = i + 1;
            return contains(str, i3, 1, "Z") ? i + 2 : i3;
        }
    }

    private int handleSC(String str, DoubleMetaphoneResult doubleMetaphoneResult, int i) {
        int i2 = i + 2;
        if (charAt(str, i2) == 'H') {
            int i3 = i + 3;
            if (contains(str, i3, 2, "OO", "ER", "EN", "UY", "ED", "EM")) {
                if (contains(str, i3, 2, "ER", "EN")) {
                    doubleMetaphoneResult.append("X", "SK");
                } else {
                    doubleMetaphoneResult.append("SK");
                }
            } else if (i != 0 || isVowel(charAt(str, 3)) || charAt(str, 3) == 'W') {
                doubleMetaphoneResult.append('X');
            } else {
                doubleMetaphoneResult.append('X', 'S');
            }
        } else if (contains(str, i2, 1, "I", ExifInterface.LONGITUDE_EAST, "Y")) {
            doubleMetaphoneResult.append('S');
        } else {
            doubleMetaphoneResult.append("SK");
        }
        return i + 3;
    }

    private int handleT(String str, DoubleMetaphoneResult doubleMetaphoneResult, int i) {
        if (contains(str, i, 4, "TION")) {
            doubleMetaphoneResult.append('X');
            return i + 3;
        } else if (contains(str, i, 3, "TIA", "TCH")) {
            doubleMetaphoneResult.append('X');
            return i + 3;
        } else if (contains(str, i, 2, "TH") || contains(str, i, 3, "TTH")) {
            int i2 = i + 2;
            if (contains(str, i2, 2, "OM", "AM") || contains(str, 0, 4, "VAN ", "VON ") || contains(str, 0, 3, "SCH")) {
                doubleMetaphoneResult.append('T');
                return i2;
            }
            doubleMetaphoneResult.append('0', 'T');
            return i2;
        } else {
            doubleMetaphoneResult.append('T');
            int i3 = i + 1;
            return contains(str, i3, 1, ExifInterface.GPS_DIRECTION_TRUE, "D") ? i + 2 : i3;
        }
    }

    private int handleW(String str, DoubleMetaphoneResult doubleMetaphoneResult, int i) {
        if (contains(str, i, 2, "WR")) {
            doubleMetaphoneResult.append('R');
            return i + 2;
        }
        if (i == 0) {
            int i2 = i + 1;
            if (isVowel(charAt(str, i2)) || contains(str, i, 2, "WH")) {
                if (isVowel(charAt(str, i2))) {
                    doubleMetaphoneResult.append('A', 'F');
                } else {
                    doubleMetaphoneResult.append('A');
                }
                return i2;
            }
        }
        if ((i == str.length() - 1 && isVowel(charAt(str, i - 1))) || contains(str, i - 1, 5, "EWSKI", "EWSKY", "OWSKI", "OWSKY") || contains(str, 0, 3, "SCH")) {
            doubleMetaphoneResult.appendAlternate('F');
            return i + 1;
        } else if (!contains(str, i, 4, "WICZ", "WITZ")) {
            return i + 1;
        } else {
            doubleMetaphoneResult.append("TS", "FX");
            return i + 4;
        }
    }

    private int handleX(String str, DoubleMetaphoneResult doubleMetaphoneResult, int i) {
        if (i == 0) {
            doubleMetaphoneResult.append('S');
            return i + 1;
        }
        if (i != str.length() - 1 || (!contains(str, i - 3, 3, "IAU", "EAU") && !contains(str, i - 2, 2, "AU", "OU"))) {
            doubleMetaphoneResult.append("KS");
        }
        int i2 = i + 1;
        return contains(str, i2, 1, "C", "X") ? i + 2 : i2;
    }

    private int handleZ(String str, DoubleMetaphoneResult doubleMetaphoneResult, int i, boolean z) {
        int i2 = i + 1;
        if (charAt(str, i2) == 'H') {
            doubleMetaphoneResult.append('J');
            return i + 2;
        }
        if (contains(str, i2, 2, "ZO", "ZI", "ZA") || (z && i > 0 && charAt(str, i - 1) != 'T')) {
            doubleMetaphoneResult.append(ExifInterface.LATITUDE_SOUTH, "TS");
        } else {
            doubleMetaphoneResult.append('S');
        }
        if (charAt(str, i2) == 'Z') {
            i2 = i + 2;
        }
        return i2;
    }

    private boolean conditionC0(String str, int i) {
        if (contains(str, i, 4, "CHIA")) {
            return true;
        }
        boolean z = false;
        if (i <= 1) {
            return false;
        }
        int i2 = i - 2;
        if (isVowel(charAt(str, i2)) || !contains(str, i - 1, 3, "ACH")) {
            return false;
        }
        char charAt = charAt(str, i + 2);
        if (!(charAt == 'I' || charAt == 'E') || contains(str, i2, 6, "BACHER", "MACHER")) {
            z = true;
        }
        return z;
    }

    private boolean conditionCH0(String str, int i) {
        if (i != 0) {
            return false;
        }
        int i2 = i + 1;
        return (contains(str, i2, 5, "HARAC", "HARIS") || contains(str, i2, 3, "HOR", "HYM", "HIA", "HEM")) && !contains(str, 0, 5, "CHORE");
    }

    private boolean conditionCH1(String str, int i) {
        if (!contains(str, 0, 4, "VAN ", "VON ") && !contains(str, 0, 3, "SCH") && !contains(str, i - 2, 6, "ORCHES", "ARCHIT", "ORCHID")) {
            int i2 = i + 2;
            if (!contains(str, i2, 1, ExifInterface.GPS_DIRECTION_TRUE, ExifInterface.LATITUDE_SOUTH)) {
                if (!contains(str, i - 1, 1, ExifInterface.GPS_MEASUREMENT_IN_PROGRESS, "O", "U", ExifInterface.LONGITUDE_EAST) && i != 0) {
                    return false;
                }
                if (!contains(str, i2, 1, L_R_N_M_B_H_F_V_W_SPACE) && i + 1 != str.length() - 1) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean conditionL0(String str, int i) {
        if (i == str.length() - 3 && contains(str, i - 1, 4, "ILLO", "ILLA", "ALLE")) {
            return true;
        }
        if ((contains(str, str.length() - 2, 2, "AS", "OS") || contains(str, str.length() - 1, 1, ExifInterface.GPS_MEASUREMENT_IN_PROGRESS, "O")) && contains(str, i - 1, 4, "ALLE")) {
            return true;
        }
        return false;
    }

    private boolean conditionM0(String str, int i) {
        int i2 = i + 1;
        boolean z = true;
        if (charAt(str, i2) == 'M') {
            return true;
        }
        if (!contains(str, i - 1, 3, "UMB") || (i2 != str.length() - 1 && !contains(str, i + 2, 2, "ER"))) {
            z = false;
        }
        return z;
    }

    private boolean isSlavoGermanic(String str) {
        return str.indexOf(87) > -1 || str.indexOf(75) > -1 || str.indexOf("CZ") > -1 || str.indexOf("WITZ") > -1;
    }

    private boolean isVowel(char c) {
        return VOWELS.indexOf(c) != -1;
    }

    private boolean isSilentStart(String str) {
        for (String startsWith : SILENT_START) {
            if (str.startsWith(startsWith)) {
                return true;
            }
        }
        return false;
    }

    private String cleanInput(String str) {
        if (str == null) {
            return null;
        }
        String trim = str.trim();
        if (trim.length() == 0) {
            return null;
        }
        return trim.toUpperCase(Locale.ENGLISH);
    }

    /* access modifiers changed from: protected */
    public char charAt(String str, int i) {
        if (i < 0 || i >= str.length()) {
            return 0;
        }
        return str.charAt(i);
    }

    protected static boolean contains(String str, int i, int i2, String... strArr) {
        if (i < 0) {
            return false;
        }
        int i3 = i2 + i;
        if (i3 > str.length()) {
            return false;
        }
        String substring = str.substring(i, i3);
        for (String equals : strArr) {
            if (substring.equals(equals)) {
                return true;
            }
        }
        return false;
    }
}
