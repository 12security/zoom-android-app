package androidx.constraintlayout.solver;

import androidx.exifinterface.media.ExifInterface;
import java.util.Arrays;

public class SolverVariable {
    private static final boolean INTERNAL_DEBUG = false;
    static final int MAX_STRENGTH = 7;
    public static final int STRENGTH_BARRIER = 7;
    public static final int STRENGTH_EQUALITY = 5;
    public static final int STRENGTH_FIXED = 6;
    public static final int STRENGTH_HIGH = 3;
    public static final int STRENGTH_HIGHEST = 4;
    public static final int STRENGTH_LOW = 1;
    public static final int STRENGTH_MEDIUM = 2;
    public static final int STRENGTH_NONE = 0;
    private static int uniqueConstantId = 1;
    private static int uniqueErrorId = 1;
    private static int uniqueId = 1;
    private static int uniqueSlackId = 1;
    private static int uniqueUnrestrictedId = 1;
    public float computedValue;
    int definitionId = -1;

    /* renamed from: id */
    public int f12id = -1;
    ArrayRow[] mClientEquations = new ArrayRow[8];
    int mClientEquationsCount = 0;
    private String mName;
    Type mType;
    public int strength = 0;
    float[] strengthVector = new float[7];
    public int usageInRowCount = 0;

    public enum Type {
        UNRESTRICTED,
        CONSTANT,
        SLACK,
        ERROR,
        UNKNOWN
    }

    static void increaseErrorId() {
        uniqueErrorId++;
    }

    private static String getUniqueName(Type type, String str) {
        if (str != null) {
            StringBuilder sb = new StringBuilder();
            sb.append(str);
            sb.append(uniqueErrorId);
            return sb.toString();
        }
        switch (type) {
            case UNRESTRICTED:
                StringBuilder sb2 = new StringBuilder();
                sb2.append("U");
                int i = uniqueUnrestrictedId + 1;
                uniqueUnrestrictedId = i;
                sb2.append(i);
                return sb2.toString();
            case CONSTANT:
                StringBuilder sb3 = new StringBuilder();
                sb3.append("C");
                int i2 = uniqueConstantId + 1;
                uniqueConstantId = i2;
                sb3.append(i2);
                return sb3.toString();
            case SLACK:
                StringBuilder sb4 = new StringBuilder();
                sb4.append(ExifInterface.LATITUDE_SOUTH);
                int i3 = uniqueSlackId + 1;
                uniqueSlackId = i3;
                sb4.append(i3);
                return sb4.toString();
            case ERROR:
                StringBuilder sb5 = new StringBuilder();
                sb5.append("e");
                int i4 = uniqueErrorId + 1;
                uniqueErrorId = i4;
                sb5.append(i4);
                return sb5.toString();
            case UNKNOWN:
                StringBuilder sb6 = new StringBuilder();
                sb6.append(ExifInterface.GPS_MEASUREMENT_INTERRUPTED);
                int i5 = uniqueId + 1;
                uniqueId = i5;
                sb6.append(i5);
                return sb6.toString();
            default:
                throw new AssertionError(type.name());
        }
    }

    public SolverVariable(String str, Type type) {
        this.mName = str;
        this.mType = type;
    }

    public SolverVariable(Type type, String str) {
        this.mType = type;
    }

    /* access modifiers changed from: 0000 */
    public void clearStrengths() {
        for (int i = 0; i < 7; i++) {
            this.strengthVector[i] = 0.0f;
        }
    }

    /* access modifiers changed from: 0000 */
    public String strengthsToString() {
        StringBuilder sb = new StringBuilder();
        sb.append(this);
        sb.append("[");
        String sb2 = sb.toString();
        boolean z = false;
        boolean z2 = true;
        for (int i = 0; i < this.strengthVector.length; i++) {
            StringBuilder sb3 = new StringBuilder();
            sb3.append(sb2);
            sb3.append(this.strengthVector[i]);
            String sb4 = sb3.toString();
            float[] fArr = this.strengthVector;
            if (fArr[i] > 0.0f) {
                z = false;
            } else if (fArr[i] < 0.0f) {
                z = true;
            }
            if (this.strengthVector[i] != 0.0f) {
                z2 = false;
            }
            if (i < this.strengthVector.length - 1) {
                StringBuilder sb5 = new StringBuilder();
                sb5.append(sb4);
                sb5.append(", ");
                sb2 = sb5.toString();
            } else {
                StringBuilder sb6 = new StringBuilder();
                sb6.append(sb4);
                sb6.append("] ");
                sb2 = sb6.toString();
            }
        }
        if (z) {
            StringBuilder sb7 = new StringBuilder();
            sb7.append(sb2);
            sb7.append(" (-)");
            sb2 = sb7.toString();
        }
        if (!z2) {
            return sb2;
        }
        StringBuilder sb8 = new StringBuilder();
        sb8.append(sb2);
        sb8.append(" (*)");
        return sb8.toString();
    }

    public final void addToRow(ArrayRow arrayRow) {
        int i = 0;
        while (true) {
            int i2 = this.mClientEquationsCount;
            if (i >= i2) {
                ArrayRow[] arrayRowArr = this.mClientEquations;
                if (i2 >= arrayRowArr.length) {
                    this.mClientEquations = (ArrayRow[]) Arrays.copyOf(arrayRowArr, arrayRowArr.length * 2);
                }
                ArrayRow[] arrayRowArr2 = this.mClientEquations;
                int i3 = this.mClientEquationsCount;
                arrayRowArr2[i3] = arrayRow;
                this.mClientEquationsCount = i3 + 1;
                return;
            } else if (this.mClientEquations[i] != arrayRow) {
                i++;
            } else {
                return;
            }
        }
    }

    public final void removeFromRow(ArrayRow arrayRow) {
        int i = this.mClientEquationsCount;
        for (int i2 = 0; i2 < i; i2++) {
            if (this.mClientEquations[i2] == arrayRow) {
                for (int i3 = 0; i3 < (i - i2) - 1; i3++) {
                    ArrayRow[] arrayRowArr = this.mClientEquations;
                    int i4 = i2 + i3;
                    arrayRowArr[i4] = arrayRowArr[i4 + 1];
                }
                this.mClientEquationsCount--;
                return;
            }
        }
    }

    public final void updateReferencesWithNewDefinition(ArrayRow arrayRow) {
        int i = this.mClientEquationsCount;
        for (int i2 = 0; i2 < i; i2++) {
            this.mClientEquations[i2].variables.updateFromRow(this.mClientEquations[i2], arrayRow, false);
        }
        this.mClientEquationsCount = 0;
    }

    public void reset() {
        this.mName = null;
        this.mType = Type.UNKNOWN;
        this.strength = 0;
        this.f12id = -1;
        this.definitionId = -1;
        this.computedValue = 0.0f;
        this.mClientEquationsCount = 0;
        this.usageInRowCount = 0;
    }

    public String getName() {
        return this.mName;
    }

    public void setName(String str) {
        this.mName = str;
    }

    public void setType(Type type, String str) {
        this.mType = type;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("");
        sb.append(this.mName);
        return sb.toString();
    }
}
