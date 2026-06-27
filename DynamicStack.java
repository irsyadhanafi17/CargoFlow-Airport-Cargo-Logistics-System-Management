package cargoflow;

public class DynamicStack<T> {
    private class Node {
        T data;
        Node next;
        Node(T data) { this.data = data; this.next = null; }
    }

    private Node top;
    private int size;

    public DynamicStack() {
        this.top = null;
        this.size = 0;
    }

    public boolean isEmpty() { return top == null; }
    public int getSize() { return size; }

    public void push(T item) {
        Node newNode = new Node(item);
        newNode.next = top;
        top = newNode;
        size++;
    }

    public T pop() {
        if (isEmpty()) return null;
        T item = top.data;
        top = top.next;
        size--;
        return item;
    }
}
