package com.google.common.collect;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import java.util.ArrayDeque;
import java.util.BitSet;
import java.util.Deque;
import java.util.Iterator;

@GwtCompatible
@Beta
public abstract class BinaryTreeTraverser<T> extends TreeTraverser<T> {

    private final class InOrderIterator extends AbstractIterator<T> {
        private final BitSet hasExpandedLeft = new BitSet();
        private final Deque<T> stack = new ArrayDeque(8);

        InOrderIterator(T t) {
            this.stack.addLast(t);
        }

        /* access modifiers changed from: protected */
        public T computeNext() {
            while (!this.stack.isEmpty()) {
                T last = this.stack.getLast();
                if (this.hasExpandedLeft.get(this.stack.size() - 1)) {
                    this.stack.removeLast();
                    this.hasExpandedLeft.clear(this.stack.size());
                    BinaryTreeTraverser.pushIfPresent(this.stack, BinaryTreeTraverser.this.rightChild(last));
                    return last;
                }
                this.hasExpandedLeft.set(this.stack.size() - 1);
                BinaryTreeTraverser.pushIfPresent(this.stack, BinaryTreeTraverser.this.leftChild(last));
            }
            return endOfData();
        }
    }

    private final class PostOrderIterator extends UnmodifiableIterator<T> {
        private final BitSet hasExpanded;
        private final Deque<T> stack = new ArrayDeque(8);

        PostOrderIterator(T t) {
            this.stack.addLast(t);
            this.hasExpanded = new BitSet();
        }

        public boolean hasNext() {
            return !this.stack.isEmpty();
        }

        public T next() {
            while (true) {
                T last = this.stack.getLast();
                if (this.hasExpanded.get(this.stack.size() - 1)) {
                    this.stack.removeLast();
                    this.hasExpanded.clear(this.stack.size());
                    return last;
                }
                this.hasExpanded.set(this.stack.size() - 1);
                BinaryTreeTraverser.pushIfPresent(this.stack, BinaryTreeTraverser.this.rightChild(last));
                BinaryTreeTraverser.pushIfPresent(this.stack, BinaryTreeTraverser.this.leftChild(last));
            }
        }
    }

    private final class PreOrderIterator extends UnmodifiableIterator<T> implements PeekingIterator<T> {
        private final Deque<T> stack = new ArrayDeque(8);

        PreOrderIterator(T t) {
            this.stack.addLast(t);
        }

        public boolean hasNext() {
            return !this.stack.isEmpty();
        }

        public T next() {
            T removeLast = this.stack.removeLast();
            BinaryTreeTraverser.pushIfPresent(this.stack, BinaryTreeTraverser.this.rightChild(removeLast));
            BinaryTreeTraverser.pushIfPresent(this.stack, BinaryTreeTraverser.this.leftChild(removeLast));
            return removeLast;
        }

        public T peek() {
            return this.stack.getLast();
        }
    }

    public abstract Optional<T> leftChild(T t);

    public abstract Optional<T> rightChild(T t);

    public final Iterable<T> children(final T t) {
        Preconditions.checkNotNull(t);
        return new FluentIterable<T>() {
            public Iterator<T> iterator() {
                return new AbstractIterator<T>() {
                    boolean doneLeft;
                    boolean doneRight;

                    /* access modifiers changed from: protected */
                    public T computeNext() {
                        if (!this.doneLeft) {
                            this.doneLeft = true;
                            Optional leftChild = BinaryTreeTraverser.this.leftChild(t);
                            if (leftChild.isPresent()) {
                                return leftChild.get();
                            }
                        }
                        if (!this.doneRight) {
                            this.doneRight = true;
                            Optional rightChild = BinaryTreeTraverser.this.rightChild(t);
                            if (rightChild.isPresent()) {
                                return rightChild.get();
                            }
                        }
                        return endOfData();
                    }
                };
            }
        };
    }

    /* access modifiers changed from: 0000 */
    public UnmodifiableIterator<T> preOrderIterator(T t) {
        return new PreOrderIterator(t);
    }

    /* access modifiers changed from: 0000 */
    public UnmodifiableIterator<T> postOrderIterator(T t) {
        return new PostOrderIterator(t);
    }

    public final FluentIterable<T> inOrderTraversal(final T t) {
        Preconditions.checkNotNull(t);
        return new FluentIterable<T>() {
            public UnmodifiableIterator<T> iterator() {
                return new InOrderIterator(t);
            }
        };
    }

    /* access modifiers changed from: private */
    public static <T> void pushIfPresent(Deque<T> deque, Optional<T> optional) {
        if (optional.isPresent()) {
            deque.addLast(optional.get());
        }
    }
}
