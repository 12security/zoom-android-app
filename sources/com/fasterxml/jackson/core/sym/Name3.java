package com.fasterxml.jackson.core.sym;

public final class Name3 extends Name {

    /* renamed from: q1 */
    private final int f177q1;

    /* renamed from: q2 */
    private final int f178q2;

    /* renamed from: q3 */
    private final int f179q3;

    public boolean equals(int i) {
        return false;
    }

    public boolean equals(int i, int i2) {
        return false;
    }

    Name3(String str, int i, int i2, int i3, int i4) {
        super(str, i);
        this.f177q1 = i2;
        this.f178q2 = i3;
        this.f179q3 = i4;
    }

    public boolean equals(int i, int i2, int i3) {
        return this.f177q1 == i && this.f178q2 == i2 && this.f179q3 == i3;
    }

    public boolean equals(int[] iArr, int i) {
        return i == 3 && iArr[0] == this.f177q1 && iArr[1] == this.f178q2 && iArr[2] == this.f179q3;
    }
}
