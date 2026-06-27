package cargoflow;

public class PriorityQueueHeap<T extends Comparable<T>> {
    private T[] heap;
    private int size;
    private int capacity;

    @SuppressWarnings("unchecked")
    public PriorityQueueHeap(int capacity) {
        this.capacity = capacity;
        this.heap = (T[]) new Comparable[capacity];
        this.size = 0;
    }

    public boolean isEmpty() { return size == 0; }
    public boolean isFull() { return size == capacity; }
    public int getSize() { return size; }

    public void insert(T item) {
        if (isFull()) return;
        heap[size] = item;
        int current = size;
        size++;
        heapifyUp(current); //
    }

    public T removeMax() {
        if (isEmpty()) return null;
        T root = heap[0];
        heap[0] = heap[size - 1];
        heap[size - 1] = null;
        size--;
        if (size > 0) heapifyDown(0); //
        return root;
    }

    private void heapifyUp(int index) {
        int parent = (index - 1) / 2; //
        while (index > 0 && heap[index].compareTo(heap[parent]) > 0) {
            swap(index, parent);
            index = parent;
            parent = (index - 1) / 2;
        }
    }

    private void heapifyDown(int index) {
        int maxIndex = index;
        int left = 2 * index + 1; //
        int right = 2 * index + 2; //

        if (left < size && heap[left].compareTo(heap[maxIndex]) > 0) maxIndex = left;
        if (right < size && heap[right].compareTo(heap[maxIndex]) > 0) maxIndex = right;

        if (index != maxIndex) {
            swap(index, maxIndex);
            heapifyDown(maxIndex);
        }
    }

    private void swap(int i, int j) {
        T temp = heap[i];
        heap[i] = heap[j];
        heap[j] = temp;
    }

    public T getElement(int index) {
        if (index < 0 || index >= size) return null;
        return heap[index];
    }
}
