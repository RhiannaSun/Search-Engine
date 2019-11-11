package datastructures.concrete;

import datastructures.concrete.dictionaries.ChainedHashDictionary;
import datastructures.interfaces.IDictionary;
import datastructures.interfaces.IPriorityQueue;
import misc.exceptions.EmptyContainerException;
import misc.exceptions.InvalidElementException;

/**
 * @see IPriorityQueue for details on what each method must do.
 */
public class ArrayHeap<T extends Comparable<T>> implements IPriorityQueue<T> {
    // See spec: you must implement a implement a 4-heap.
    private static final int NUM_CHILDREN = 4;

    // You MUST use this field to store the contents of your heap.
    // You may NOT rename this field: we will be inspecting it within
    // our private tests.
    private T[] heap;
    private int size;
    private IDictionary<T, Integer> indices;

    // Feel free to add more fields and constants.

    public ArrayHeap() {
        this.heap = makeArrayOfT(15);
        this.size = 0;
        this.indices = new ChainedHashDictionary<>();
    }

    /**
     * This method will return a new, empty array of the given size
     * that can contain elements of type T.
     *
     * Note that each element in the array will initially be null.
     */
    @SuppressWarnings("unchecked")
    private T[] makeArrayOfT(int arraySize) {
        // This helper method is basically the same one we gave you
        // in ArrayDictionary and ChainedHashDictionary.
        //
        // As before, you do not need to understand how this method
        // works, and should not modify it in any way.
        return (T[]) (new Comparable[arraySize]);
    }

    /**
     * A method stub that you may replace with a helper method for percolating
     * upwards from a given index, if necessary.
     */
    private void percolateUp(int index) {
        while (index != 0) { // has parent
            int parentIndex = (index-1)/NUM_CHILDREN;
            if (heap[index].compareTo(heap[parentIndex]) < 0) {
                swap(index, parentIndex);
            }
            index = parentIndex;
        }
    }

    /**
     * A method stub that you may replace with a helper method for percolating
     * downwards from a given index, if necessary.
     */
    private void percolateDown(int index) {
        while (index * NUM_CHILDREN + 1 < size) {  // has at least one child
            int smallest = findSmallestIndex(index);
            if (heap[index].compareTo(heap[smallest]) > 0) {
                swap(index, smallest);
            }
            index = smallest;
        }
    }

    /**
     * A method stub that you may replace with a helper method for determining
     * which direction an index needs to percolate and percolating accordingly.
     */
    private void percolate(int index) {
        if (index == 0) { // first level
            percolateDown(0);
        } else {
            int parent = (index-1)/NUM_CHILDREN;
            if (heap[index].compareTo(heap[parent]) < 0) {
                percolateUp(index);
            } else {
                percolateDown(index);
            }
        }
    }

    // at least the node has one child
    private int findSmallestIndex(int index){
        T smallest = heap[NUM_CHILDREN*index+1];
        int smallestIndex = NUM_CHILDREN*index+1;

        int curIndex = NUM_CHILDREN*index + 2;
        //     no more than 4 child                            index existed
        while (curIndex <= NUM_CHILDREN*index+NUM_CHILDREN && curIndex <size) {
            if (heap[curIndex].compareTo(smallest)<0) {
                smallest = heap[curIndex];
                smallestIndex = curIndex;
            }
            curIndex++;
        }
        return smallestIndex;
    }


    /**
     * A method stub that you may replace with a helper method for swapping
     * the elements at two indices in the 'heap' array.
     */
    private void swap(int a, int b) {
        T temp = heap[a];
        heap[a] = heap[b];
        heap[b] = temp;
        indices.put(heap[a], a);
        indices.put(heap[b], b);
    }

    @Override
    public T removeMin() {
        if (size() == 0) {
            throw new EmptyContainerException();
        }
        indices.remove(heap[0]);
        T temp = heap[0];
        heap[0] = heap[size-1];
        heap[size-1] = null;
        size--;
        if (size == 1) {
            indices.put(heap[0], 0);
        } else {
            percolate(0);
        }
        return temp;
    }

    @Override
    public T peekMin() {
        if (size() == 0) {
            throw new EmptyContainerException();
        }
        return heap[0];
    }

    @Override
    public void add(T item) {
        if (item == null) {
            throw new IllegalArgumentException();
        }
        if (contains(item)) {
            throw new InvalidElementException();
        }
        if (size == heap.length) {
            T[] newHeap = makeArrayOfT(size*2);
            for (int i = 0; i < size; i++) {
                newHeap[i] = heap[i];
            }
            heap = newHeap;
        }
        heap[size] = item;
        indices.put(item, size);
        percolate(size);
        size++;
    }

    @Override
    public boolean contains(T item) {
        if (item == null){
            throw new IllegalArgumentException();
        }
        return indices.containsKey(item);
    }

    @Override
    public void remove(T item) {
        if (item == null) {
            throw new IllegalArgumentException();
        }
        if (!contains(item)) {
            throw new InvalidElementException();
        }
        int index = indices.remove(item);
        if (index == size - 1) {
            heap[size-1] = null;
            size--;
        } else {
            heap[index] = heap[size - 1];
            heap[size - 1] = null;
            indices.put(heap[index], index);
            size--;
            percolate(index);
        }
    }

    @Override
    public void replace(T oldItem, T newItem) {
        if (!contains(oldItem) || contains(newItem)) {
            throw new InvalidElementException();
        }
        int index = indices.remove(oldItem);
        heap[index] = newItem;
        indices.put(heap[index], index);
        percolate(index);
    }

    @Override
    public int size() { return size;
    }
}
