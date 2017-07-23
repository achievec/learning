package org.aloha;

import java.util.concurrent.atomic.AtomicReference;

/**
 * 使用原子引用实现非阻塞的栈数据结构
 * 
 *
 * @author aloha
 *
 * @param <E>
 */
public class ThreadSafeStake<E> {

    private static class Node<E> {
        public final E value;
        public Node<E> next;

        public Node(E v) {
            value = v;
        }
    }

    private AtomicReference<Node<E>> top = new AtomicReference<Node<E>>();

    public void push(E value) {
        Node<E> newHead = new Node<>(value);
        Node<E> oldHead;
        do {
            oldHead = top.get();
            newHead.next = oldHead;
        } while (!top.compareAndSet(oldHead, newHead));
    }

    public E pop() {
        Node<E> oldHead;
        Node<E> newHead;
        do {
            oldHead = top.get();
            if (oldHead == null) {
                return null;
            }
            newHead = oldHead.next;
        } while (!top.compareAndSet(oldHead, newHead));
        return oldHead.value;
    }

}
