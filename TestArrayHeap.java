package datastructures;

import datastructures.concrete.ArrayHeap;
import datastructures.interfaces.IPriorityQueue;
import misc.BaseTest;
import misc.exceptions.EmptyContainerException;
import misc.exceptions.InvalidElementException;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.util.Arrays;
import java.util.Random;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;

/**
 * See spec for details on what kinds of tests this class should include.
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestArrayHeap extends BaseTest {
    protected <T extends Comparable<T>> IPriorityQueue<T> makeInstance() {
        return new ArrayHeap<>();
    }

    @Test(timeout=SECOND)
    public void testDuplicateException() {
        IntWrapper[] values = IntWrapper.createArray(new int[]{4, 6, 8, 10, 12});
        IPriorityQueue<IntWrapper> heap = this.makeInstance();

        for (IntWrapper value : values) {
            heap.add(value);
        }

        IntWrapper newValue = values[1];
        try { //duplicate item
            heap.add(newValue);
            fail("Expected InvalidElementException");
        } catch (InvalidElementException ex) {
            // Do nothing: this is ok
        }

        try { //duplicate item
            heap.replace(values[3], newValue);
            fail("Expected InvalidElementException");
        } catch (InvalidElementException ex) {
            // Do nothing: this is ok
        }

    }

    @Test(timeout=SECOND)
    public void testNullException() {
        IPriorityQueue<IntWrapper> heap = this.makeInstance();
        try { //null item
            heap.add(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException ex) {
            // Do nothing: this is ok
        }
        try { //null item
            heap.contains(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException ex) {
            // Do nothing: this is ok
        }
        try { //null item
            heap.remove(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException ex) {
            // Do nothing: this is ok
        }
    }

    @Test(timeout=SECOND)
    public void testEmptyException() {
        IPriorityQueue<IntWrapper> heap = this.makeInstance();
        try {
            heap.removeMin();
            fail("Expected EmptyContainerException");
        } catch (EmptyContainerException ex) {
            // Do nothing: this is ok
        }

        try {
            heap.peekMin();
            fail("Expected EmptyContainerException");
        } catch (EmptyContainerException ex) {
            // Do nothing: this is ok
        }
    }

    @Test(timeout=SECOND)
    public void testNotExisted() {
        IPriorityQueue<IntWrapper> heap = this.makeInstance();
        IntWrapper newValue = new IntWrapper(6);
        try {
            heap.remove(newValue);
            fail("Expected InvalidElementException");
        } catch (InvalidElementException ex) {
            // Do nothing: this is ok
        }

        try {
            heap.replace(newValue, newValue);
            fail("Expected InvalidElementException");
        } catch (InvalidElementException ex) {
            // Do nothing: this is ok
        }

        IntWrapper[] values = IntWrapper.createArray(new int[]{1, 2, 3, 4, 5});
        for (IntWrapper value : values) {
            heap.add(value);
        }
        try {
            heap.remove(newValue);
            fail("Expected InvalidElementException");
        } catch (InvalidElementException ex) {
            // Do nothing: this is ok
        }

        try {
            heap.replace(newValue, newValue);
            fail("Expected InvalidElementException");
        } catch (InvalidElementException ex) {
            // Do nothing: this is ok
        }
    }

    @Test(timeout=SECOND)
    public void testReplaceLast() {
        IntWrapper[] values = IntWrapper.createArray(new int[]{3, 0, 9, -5, 7, 4, 2, -6, 5, 8});
        IntWrapper[] input = new IntWrapper[10];
        IntWrapper[] output = new IntWrapper[10];
        for (int i = 0; i < 10; i++) {
            input[i] = values[i];
        }
        Arrays.sort(values);
        for (int i = 0; i < 10; i++) {
            output[i] = values[i];
        }
        IntWrapper replaceValue = new IntWrapper(-7);

        IPriorityQueue<IntWrapper> heap = this.makeInstance();
        for (IntWrapper value : input) {
            heap.add(value);
        }
        heap.replace(input[2], replaceValue);
        assertEquals(replaceValue, heap.removeMin());
        for (int i = 0; i < output.length-1; i++) {
            assertEquals(output[i], heap.removeMin());
        }
    }

    @Test(timeout=SECOND)
    public void testReplaceFirst() {
        IntWrapper[] values = IntWrapper.createArray(new int[]{3, 0, 9, -5, 7, 4, 2, -6, 5, 8});
        IntWrapper[] input = new IntWrapper[10];
        IntWrapper[] output = new IntWrapper[10];
        for (int i = 0; i < 10; i++) {
            input[i] = values[i];
        }
        Arrays.sort(values);
        for (int i = 0; i < 10; i++) {
            output[i] = values[i];
        }
        IntWrapper replaceValue = new IntWrapper(10);

        IPriorityQueue<IntWrapper> heap = this.makeInstance();
        for (IntWrapper value : input) {
            heap.add(value);
        }
        heap.replace(input[7], replaceValue);
        for (int i = 0; i < output.length-1; i++) {
            assertEquals(output[i+1], heap.removeMin());
        }
        assertEquals(replaceValue, heap.removeMin());
    }

    @Test(timeout=SECOND)
    public void testPercolateDownChecksAllChildren() {
        IntWrapper[] values = IntWrapper.createArray(new int[]{3, 0, 9, 7, -5, 4, 2, 5, -6,
                8, 11, 12, -14, 13, 19});
        IntWrapper[] input = new IntWrapper[15];
        IntWrapper[] output = new IntWrapper[15];
        for (int i = 0; i < 15; i++) {
            input[i] = values[i];
        }
        Arrays.sort(values);
        for (int i = 0; i < 15; i++) {
            output[i] = values[i];
        }

        IPriorityQueue<IntWrapper> heap = this.makeInstance();
        for (IntWrapper value : input) {
            heap.add(value);
        }
        for (int i = 0; i < output.length; i++) {
            assertEquals(output[i], heap.removeMin());
        }
    }

    @Test//(timeout=SECOND)
    public void complexCheck1() {

        IntWrapper[] values = IntWrapper.createArray(new int[]{3,
                0, 9, 7, 5,
                4, 2, -5, -6,
                8, 11, 12, -14,
                13, 19, -10, -2,
                35, 200, 37, 77,
                -100, -190, 30, 99,
                14, -9});
        IntWrapper[] input = new IntWrapper[27];
        IntWrapper[] output = new IntWrapper[27];
        for (int i = 0; i < 27; i++) {
            input[i] = values[i];
        }
        Arrays.sort(values);
        for (int i = 0; i < 27; i++) {
            output[i] = values[i];
        }

        IPriorityQueue<IntWrapper> heap = this.makeInstance();
        for (IntWrapper value : input) {
            heap.add(value);
        }
        for (int i = 0; i < output.length; i++) {
            assertEquals(output[i], heap.removeMin());
        }
    }

    @Test(timeout=SECOND)
    public void testBasicSize() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        heap.add(3);
        assertEquals(1, heap.size());
        assertFalse(heap.isEmpty());
    }

    @Test(timeout=SECOND)
    public void testSizeAfterRemoveMin() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        heap.add(3);
        heap.removeMin();
        assertEquals(0, heap.size());
        for (int i = 0; i < 100000; i++) {
            heap.add(i);
            assertEquals(i+1, heap.size());
        }
        for (int i = 99999; i >= 0; i--) {
            heap.removeMin();
            assertEquals(i, heap.size());
        }
    }

    @Test(timeout=SECOND)
    public void testSizeAfterAddandRemove() {
        IntWrapper[] values = IntWrapper.createArray(new int[]{1, 2, 3, 4, 5});
        IPriorityQueue<IntWrapper> heap = this.makeInstance();
        for (IntWrapper value : values) {
            heap.add(value);
        }
        assertEquals(5, heap.size());
        heap.remove(values[2]);
        assertEquals(4, heap.size());
    }

    @Test(timeout=SECOND)
    public void testSizeAfterPeekMin() {
        IntWrapper[] values = IntWrapper.createArray(new int[]{1, 2, 3, 4, 5});
        IPriorityQueue<IntWrapper> heap = this.makeInstance();
        for (IntWrapper value : values) {
            heap.add(value);
        }
        assertEquals(5, heap.size());
        heap.peekMin();
        assertEquals(5, heap.size());
    }

    @Test(timeout=SECOND)
    public void testPeekAndRemoveMin() {
        IntWrapper[] values = IntWrapper.createArray(new int[]{3, 0, 9, -5, 7, 4, 2, -6, 5, 8});
        IntWrapper[] input = new IntWrapper[10];
        IntWrapper[] output = new IntWrapper[10];
        for (int i = 0; i < 10; i++) {
            input[i] = values[i];
        }
        Arrays.sort(values);
        for (int i = 0; i < 10; i++) {
            output[i] = values[i];
        }

        IPriorityQueue<IntWrapper> heap = this.makeInstance();
        for (IntWrapper value : input) {
            heap.add(value);
        }

        for (int i = 0; i < input.length; i++) {
            assertEquals(output[i], heap.peekMin());
            assertEquals(output[i], heap.removeMin());
        }
    }


    @Test(timeout=SECOND)
    public void testRemoveAndContains() {
        IntWrapper[] values = IntWrapper.createArray(new int[]{4, -2, -3, 7, -5});
        IPriorityQueue<IntWrapper> heap = this.makeInstance();
        for (IntWrapper value : values) {
            heap.add(value);
        }
        IntWrapper[] input = new IntWrapper[10];
        IntWrapper[] output = new IntWrapper[10];
        Arrays.sort(values);
        for (int i = 0; i < 5; i++) {
            output[i] = values[i];
        }

        heap.remove(values[3]);
        assertFalse(heap.contains(values[3]));

        assertEquals(output[0], heap.removeMin());
        assertFalse(heap.contains(output[0]));
        assertEquals(output[1], heap.removeMin());
        assertFalse(heap.contains(output[1]));
        assertEquals(output[2], heap.removeMin());
        assertFalse(heap.contains(output[3]));
        assertEquals(output[4], heap.removeMin());
    }

    @Test(timeout=SECOND)
    public void testAddFront() {
        IntWrapper[] values = IntWrapper.createArray(new int[]{1, 2, 3, 4});
        IntWrapper addValue = new IntWrapper(0);
        IPriorityQueue<IntWrapper> heap = this.makeInstance();
        for (IntWrapper value : values) {
            heap.add(value);
        }
        heap.add(addValue);
        assertEquals(addValue, heap.removeMin());
        for (int i = 0; i < values.length; i++) {
            assertEquals(values[i], heap.removeMin());
        }
    }

    @Test(timeout=SECOND)
    public void testAddBack() {
        IntWrapper[] values = IntWrapper.createArray(new int[]{1, 2, 3, 4});
        IntWrapper addValue = new IntWrapper(5);
        IPriorityQueue<IntWrapper> heap = this.makeInstance();
        for (IntWrapper value : values) {
            heap.add(value);
        }
        heap.add(addValue);
        for (int i = 0; i < values.length; i++) {
            assertEquals(values[i], heap.removeMin());
        }
        assertEquals(addValue, heap.removeMin());
    }

    @Test(timeout=SECOND)
    public void testAddMiddle() {
        IntWrapper[] values = IntWrapper.createArray(new int[]{1, 2, 4, 5});
        IntWrapper addValue = new IntWrapper(3);
        IPriorityQueue<IntWrapper> heap = this.makeInstance();
        for (IntWrapper value : values) {
            heap.add(value);
        }
        heap.add(addValue);
        for (int i = 0; i < 2; i++) {
            assertEquals(values[i], heap.removeMin());
        }
        assertEquals(addValue, heap.removeMin());
        for (int i = 2; i < values.length; i++) {
            assertEquals(values[i], heap.removeMin());
        }
    }

    @Test(timeout=SECOND)
    public void testRemoveFront() {
        IntWrapper[] values = IntWrapper.createArray(new int[]{3, 0, 9, -5, 7, 4, 2, -6, 5, 8});
        IntWrapper[] input = new IntWrapper[10];
        IntWrapper[] output = new IntWrapper[9];
        for (int i = 0; i < 10; i++) {
            input[i] = values[i];
        }
        Arrays.sort(values);
        for (int i = 0; i < 9; i++) {
            output[i] = values[i+1];
        }
        IPriorityQueue<IntWrapper> heap = this.makeInstance();
        for (IntWrapper value : input) {
            heap.add(value);
        }
        heap.remove(values[0]);

        for (int i = 0; i < output.length; i++) {
            assertEquals(output[i], heap.removeMin());
        }
    }

    @Test(timeout=SECOND)
    public void testRemoveBack() {
        IntWrapper[] values = IntWrapper.createArray(new int[]{3, 0, 9, -5, 7, 4, 2, -6, 5, 8});
        IntWrapper[] input = new IntWrapper[10];
        IntWrapper[] output = new IntWrapper[9];
        for (int i = 0; i < 10; i++) {
            input[i] = values[i];
        }
        Arrays.sort(values);
        for (int i = 0; i < 9; i++) {
            output[i] = values[i];
        }
        IPriorityQueue<IntWrapper> heap = this.makeInstance();
        for (IntWrapper value : input) {
            heap.add(value);
        }
        heap.remove(values[9]);

        for (int i = 0; i < output.length; i++) {
            assertEquals(output[i], heap.removeMin());
        }

    }

    @Test(timeout=SECOND)
    public void testRemoveMiddle() {
        IntWrapper[] values = IntWrapper.createArray(new int[]{3, 0, 9, -5, 7, 4, 2, -6, 5, 8});
        IntWrapper[] input = new IntWrapper[10];
        IntWrapper[] output = new IntWrapper[9];
        for (int i = 0; i < 10; i++) {
            input[i] = values[i];
        }
        Arrays.sort(values);
        for (int i = 0; i < 9; i++) {
            output[i] = values[i];
        }
        IPriorityQueue<IntWrapper> heap = this.makeInstance();
        for (IntWrapper value : input) {
            heap.add(value);
        }
        heap.remove(values[4]);

        for (int i = 0; i < 4; i++) {
            assertEquals(output[i], heap.removeMin());
        }
        for (int i = 5; i < output.length; i++) {
            assertEquals(output[i], heap.removeMin());
        }
    }

    @Test(timeout=SECOND)
    public void testRemoveAll() {
        IntWrapper[] values = IntWrapper.createArray(new int[]{3, 0, 9, -5, 7, 4, 2, -6, 5, 8});

        IPriorityQueue<IntWrapper> heap = this.makeInstance();
        for (IntWrapper value : values) {
            heap.add(value);
        }
        for (IntWrapper value : values) {
            heap.remove(value);
        }
        assertEquals(0, heap.size());
    }

    @Test(timeout=SECOND)
    public void testRemoveMinAll() {
        IntWrapper[] values = IntWrapper.createArray(new int[]{3, 0, 9, -5, 7, 4, 2, -6, 5, 8});

        IPriorityQueue<IntWrapper> heap = this.makeInstance();
        for (IntWrapper value : values) {
            heap.add(value);
        }
        for (IntWrapper value : values) {
            heap.removeMin();
        }
        assertEquals(0, heap.size());
    }

    @Test(timeout=SECOND)
    public void testBasicAddReflection() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        heap.add(3);
        Comparable<Integer>[] array = getArray(heap);
        assertEquals(3, array[0]);
    }


    @Test(timeout=SECOND)
    public void containsRemove() {
        IntWrapper[] values = IntWrapper.createArray(new int[]{49, 42, 31});
        IPriorityQueue<IntWrapper> heap = this.makeInstance();

        for (IntWrapper value : values) {
            heap.add(value);
        }
        heap.remove(values[0]);
        assertFalse(heap.contains(values[0]));

        IntWrapper min = heap.removeMin();
        assertFalse(heap.contains(min));

        heap.remove(values[1]);
        assertFalse(heap.contains(values[1]));

    }


    @Test(timeout=SECOND)
    public void containsRandom() {
        int[] array = new int[10000];
        Random rand = new Random();
        for (int i = 0; i< 10000; i++) {
            array[i] = rand.nextInt(10000);
        }

        IntWrapper[] values = IntWrapper.createArray(array);
        IPriorityQueue<IntWrapper> heap = this.makeInstance();

        for (IntWrapper value : values) {
            heap.add(value);
        }
        for (IntWrapper shu: values) {
            heap.remove(shu);
            assertFalse(heap.contains(shu));
        }
    }


    @Test(timeout=SECOND)
    public void containsRemoveMin() {
        int[] array = new int[10000];
        Random rand = new Random();
        for (int i = 0; i< 10000; i++) {
            array[i] = rand.nextInt(10000);
        }

        IntWrapper[] values = IntWrapper.createArray(array);
        IPriorityQueue<IntWrapper> heap = this.makeInstance();

        for (IntWrapper value : values) {
            heap.add(value);
        }
        for (IntWrapper shu: values) {
            IntWrapper min = heap.removeMin();
            assertFalse(heap.contains(min));
        }
    }

    @Test(timeout=SECOND)
    public void testUpdateDecrease() {
        IntWrapper[] values = IntWrapper.createArray(new int[]{1, 2, 3, 4, 5});
        IPriorityQueue<IntWrapper> heap = this.makeInstance();

        for (IntWrapper value : values) {
            heap.add(value);
        }
        assertEquals(5, heap.size());

        IntWrapper newValue = new IntWrapper(0);
        heap.replace(values[2], newValue);

        assertEquals(newValue, heap.removeMin());
        assertEquals(values[0], heap.removeMin());
        assertEquals(values[1], heap.removeMin());
        assertEquals(values[3], heap.removeMin());
        assertEquals(values[4], heap.removeMin());
    }


    @Test(timeout=SECOND)
    public void testUpdateIncrease() {
        IntWrapper[] values = IntWrapper.createArray(new int[]{0, 2, 4, 6, 8});
        IPriorityQueue<IntWrapper> heap = this.makeInstance();

        for (IntWrapper value : values) {
            heap.add(value);
        }

        IntWrapper newValue = new IntWrapper(5);
        heap.replace(values[0], newValue);
        assertEquals(5, heap.size());

        assertEquals(values[1], heap.removeMin());
        assertEquals(values[2], heap.removeMin());
        assertEquals(newValue, heap.removeMin());
        assertEquals(values[3], heap.removeMin());
        assertEquals(values[4], heap.removeMin());
    }

    /**
     * A comparable wrapper class for ints. Uses reference equality so that two different IntWrappers
     * with the same value are not necessarily equal--this means that you may have multiple different
     * IntWrappers with the same value in a heap.
     */
    public static class IntWrapper implements Comparable<IntWrapper> {
        private final int val;

        public IntWrapper(int value) {
            this.val = value;
        }

        public static IntWrapper[] createArray(int[] values) {
            IntWrapper[] output = new IntWrapper[values.length];
            for (int i = 0; i < values.length; i++) {
                output[i] = new IntWrapper(values[i]);
            }
            return output;
        }

        @Override
        public int compareTo(IntWrapper o) {
            return Integer.compare(val, o.val);
        }

        @Override
        public boolean equals(Object obj) {
            return this == obj;
        }

        @Override
        public int hashCode() {
            return this.val;
        }

        @Override
        public String toString() {
            return Integer.toString(this.val);
        }
    }

    /**
     * A helper method for accessing the private array inside a heap using reflection.
     */
    @SuppressWarnings("unchecked")
    private static <T extends Comparable<T>> Comparable<T>[] getArray(IPriorityQueue<T> heap) {
        return getField(heap, "heap", Comparable[].class);
    }

}
