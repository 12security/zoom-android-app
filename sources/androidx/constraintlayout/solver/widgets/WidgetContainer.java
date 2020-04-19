package androidx.constraintlayout.solver.widgets;

import androidx.constraintlayout.solver.Cache;
import java.util.ArrayList;

public class WidgetContainer extends ConstraintWidget {
    protected ArrayList<ConstraintWidget> mChildren = new ArrayList<>();

    public WidgetContainer() {
    }

    public WidgetContainer(int i, int i2, int i3, int i4) {
        super(i, i2, i3, i4);
    }

    public WidgetContainer(int i, int i2) {
        super(i, i2);
    }

    public void reset() {
        this.mChildren.clear();
        super.reset();
    }

    public void add(ConstraintWidget constraintWidget) {
        this.mChildren.add(constraintWidget);
        if (constraintWidget.getParent() != null) {
            ((WidgetContainer) constraintWidget.getParent()).remove(constraintWidget);
        }
        constraintWidget.setParent(this);
    }

    public void add(ConstraintWidget... constraintWidgetArr) {
        for (ConstraintWidget add : constraintWidgetArr) {
            add(add);
        }
    }

    public void remove(ConstraintWidget constraintWidget) {
        this.mChildren.remove(constraintWidget);
        constraintWidget.setParent(null);
    }

    public ArrayList<ConstraintWidget> getChildren() {
        return this.mChildren;
    }

    public ConstraintWidgetContainer getRootConstraintContainer() {
        ConstraintWidget parent = getParent();
        ConstraintWidgetContainer constraintWidgetContainer = this instanceof ConstraintWidgetContainer ? (ConstraintWidgetContainer) this : null;
        while (parent != null) {
            ConstraintWidget parent2 = parent.getParent();
            if (parent instanceof ConstraintWidgetContainer) {
                constraintWidgetContainer = (ConstraintWidgetContainer) parent;
                parent = parent2;
            } else {
                parent = parent2;
            }
        }
        return constraintWidgetContainer;
    }

    /* JADX WARNING: type inference failed for: r0v6 */
    /* JADX WARNING: type inference failed for: r3v6, types: [androidx.constraintlayout.solver.widgets.ConstraintWidget] */
    /* JADX WARNING: type inference failed for: r0v7 */
    /* JADX WARNING: Multi-variable type inference failed */
    /* JADX WARNING: Unknown variable types count: 1 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public androidx.constraintlayout.solver.widgets.ConstraintWidget findWidget(float r9, float r10) {
        /*
            r8 = this;
            int r0 = r8.getDrawX()
            int r1 = r8.getDrawY()
            int r2 = r8.getWidth()
            int r2 = r2 + r0
            int r3 = r8.getHeight()
            int r3 = r3 + r1
            float r0 = (float) r0
            int r0 = (r9 > r0 ? 1 : (r9 == r0 ? 0 : -1))
            if (r0 < 0) goto L_0x0028
            float r0 = (float) r2
            int r0 = (r9 > r0 ? 1 : (r9 == r0 ? 0 : -1))
            if (r0 > 0) goto L_0x0028
            float r0 = (float) r1
            int r0 = (r10 > r0 ? 1 : (r10 == r0 ? 0 : -1))
            if (r0 < 0) goto L_0x0028
            float r0 = (float) r3
            int r0 = (r10 > r0 ? 1 : (r10 == r0 ? 0 : -1))
            if (r0 > 0) goto L_0x0028
            r0 = r8
            goto L_0x0029
        L_0x0028:
            r0 = 0
        L_0x0029:
            r1 = 0
            java.util.ArrayList<androidx.constraintlayout.solver.widgets.ConstraintWidget> r2 = r8.mChildren
            int r2 = r2.size()
        L_0x0030:
            if (r1 >= r2) goto L_0x0072
            java.util.ArrayList<androidx.constraintlayout.solver.widgets.ConstraintWidget> r3 = r8.mChildren
            java.lang.Object r3 = r3.get(r1)
            androidx.constraintlayout.solver.widgets.ConstraintWidget r3 = (androidx.constraintlayout.solver.widgets.ConstraintWidget) r3
            boolean r4 = r3 instanceof androidx.constraintlayout.solver.widgets.WidgetContainer
            if (r4 == 0) goto L_0x0048
            androidx.constraintlayout.solver.widgets.WidgetContainer r3 = (androidx.constraintlayout.solver.widgets.WidgetContainer) r3
            androidx.constraintlayout.solver.widgets.ConstraintWidget r3 = r3.findWidget(r9, r10)
            if (r3 == 0) goto L_0x006f
            r0 = r3
            goto L_0x006f
        L_0x0048:
            int r4 = r3.getDrawX()
            int r5 = r3.getDrawY()
            int r6 = r3.getWidth()
            int r6 = r6 + r4
            int r7 = r3.getHeight()
            int r7 = r7 + r5
            float r4 = (float) r4
            int r4 = (r9 > r4 ? 1 : (r9 == r4 ? 0 : -1))
            if (r4 < 0) goto L_0x006f
            float r4 = (float) r6
            int r4 = (r9 > r4 ? 1 : (r9 == r4 ? 0 : -1))
            if (r4 > 0) goto L_0x006f
            float r4 = (float) r5
            int r4 = (r10 > r4 ? 1 : (r10 == r4 ? 0 : -1))
            if (r4 < 0) goto L_0x006f
            float r4 = (float) r7
            int r4 = (r10 > r4 ? 1 : (r10 == r4 ? 0 : -1))
            if (r4 > 0) goto L_0x006f
            r0 = r3
        L_0x006f:
            int r1 = r1 + 1
            goto L_0x0030
        L_0x0072:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.constraintlayout.solver.widgets.WidgetContainer.findWidget(float, float):androidx.constraintlayout.solver.widgets.ConstraintWidget");
    }

    public ArrayList<ConstraintWidget> findWidgets(int i, int i2, int i3, int i4) {
        ArrayList<ConstraintWidget> arrayList = new ArrayList<>();
        Rectangle rectangle = new Rectangle();
        rectangle.setBounds(i, i2, i3, i4);
        int size = this.mChildren.size();
        for (int i5 = 0; i5 < size; i5++) {
            ConstraintWidget constraintWidget = (ConstraintWidget) this.mChildren.get(i5);
            Rectangle rectangle2 = new Rectangle();
            rectangle2.setBounds(constraintWidget.getDrawX(), constraintWidget.getDrawY(), constraintWidget.getWidth(), constraintWidget.getHeight());
            if (rectangle.intersects(rectangle2)) {
                arrayList.add(constraintWidget);
            }
        }
        return arrayList;
    }

    public static Rectangle getBounds(ArrayList<ConstraintWidget> arrayList) {
        Rectangle rectangle = new Rectangle();
        if (arrayList.size() == 0) {
            return rectangle;
        }
        int size = arrayList.size();
        int i = Integer.MAX_VALUE;
        int i2 = Integer.MAX_VALUE;
        int i3 = 0;
        int i4 = 0;
        for (int i5 = 0; i5 < size; i5++) {
            ConstraintWidget constraintWidget = (ConstraintWidget) arrayList.get(i5);
            if (constraintWidget.getX() < i) {
                i = constraintWidget.getX();
            }
            if (constraintWidget.getY() < i2) {
                i2 = constraintWidget.getY();
            }
            if (constraintWidget.getRight() > i3) {
                i3 = constraintWidget.getRight();
            }
            if (constraintWidget.getBottom() > i4) {
                i4 = constraintWidget.getBottom();
            }
        }
        rectangle.setBounds(i, i2, i3 - i, i4 - i2);
        return rectangle;
    }

    public void setOffset(int i, int i2) {
        super.setOffset(i, i2);
        int size = this.mChildren.size();
        for (int i3 = 0; i3 < size; i3++) {
            ((ConstraintWidget) this.mChildren.get(i3)).setOffset(getRootX(), getRootY());
        }
    }

    public void updateDrawPosition() {
        super.updateDrawPosition();
        ArrayList<ConstraintWidget> arrayList = this.mChildren;
        if (arrayList != null) {
            int size = arrayList.size();
            for (int i = 0; i < size; i++) {
                ConstraintWidget constraintWidget = (ConstraintWidget) this.mChildren.get(i);
                constraintWidget.setOffset(getDrawX(), getDrawY());
                if (!(constraintWidget instanceof ConstraintWidgetContainer)) {
                    constraintWidget.updateDrawPosition();
                }
            }
        }
    }

    public void layout() {
        updateDrawPosition();
        ArrayList<ConstraintWidget> arrayList = this.mChildren;
        if (arrayList != null) {
            int size = arrayList.size();
            for (int i = 0; i < size; i++) {
                ConstraintWidget constraintWidget = (ConstraintWidget) this.mChildren.get(i);
                if (constraintWidget instanceof WidgetContainer) {
                    ((WidgetContainer) constraintWidget).layout();
                }
            }
        }
    }

    public void resetSolverVariables(Cache cache) {
        super.resetSolverVariables(cache);
        int size = this.mChildren.size();
        for (int i = 0; i < size; i++) {
            ((ConstraintWidget) this.mChildren.get(i)).resetSolverVariables(cache);
        }
    }

    public void removeAllChildren() {
        this.mChildren.clear();
    }
}