/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item> {


    private int size = 0;
    private Item[] items;

    // construct an empty randomized queue
    public RandomizedQueue() {
        items = (Item[]) new Object[1];
    }

    private void resizeArray(int resize) {
        Item[] newArray = (Item[]) new Object[resize];
        for (int i = 0; i < size; i++) {
            newArray[i] = items[i];
        }
        items = newArray;
    }

    private void swap(int index1, int index2) {
        Item tmp = items[index1];
        items[index1] = items[index2];
        items[index2] = tmp;
    }

    // is the randomized queue empty?
    public boolean isEmpty() {
        return size == 0;
    }

    // return the number of items on the randomized queue
    public int size() {
        return size;
    }

    // add the item
    public void enqueue(Item item) {
        if (item == null) {
            throw new IllegalArgumentException();
        }
        if (size >= items.length) {
            resizeArray(2 * items.length);
        }
        items[size] = item;
        int randomIndex = StdRandom.uniform(size + 1);
        swap(randomIndex, size);
        size++;
    }

    // remove and return a random item
    public Item dequeue() {
        if (size == 0) {
            throw new NoSuchElementException();
        }
        Item item = items[size - 1];
        size--;
        items[size] = null;
        if (size <= items.length / 4 && size != 0) {
            resizeArray(items.length / 2);
        }
        return item;
    }

    // return a random item (but do not remove it)
    public Item sample() {
        if (size == 0) {
            throw new NoSuchElementException();
        }
        int randomIdex = StdRandom.uniform(size);
        return items[randomIdex];
    }

    // return an independent iterator over items in random order
    public Iterator<Item> iterator() {
        return new ArrayIterator();
    }

    private class ArrayIterator implements Iterator<Item> {
        private int pointer = 0;
        private final int[] interatorIndex;

        private ArrayIterator() {
            interatorIndex = new int[size];
            for (int i = 0; i < size; i++) {
                interatorIndex[i] = i;
            }
            StdRandom.shuffle(interatorIndex);
        }

        public boolean hasNext() {
            return pointer < size;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }

        public Item next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            int randomIndex = interatorIndex[pointer];
            pointer++;
            return items[randomIndex];
        }
    }

    // unit testing (required)
    public static void main(String[] args) {
        RandomizedQueue<Integer> testClient = new RandomizedQueue<>();
        StdOut.println(testClient.isEmpty());
        testClient.enqueue(1);
        StdOut.println(testClient.isEmpty());
        testClient.enqueue(2);
        StdOut.println(testClient.size());
        StdOut.println(testClient.dequeue());
        StdOut.println(testClient.dequeue());
        testClient.enqueue(4);
        StdOut.println(testClient.sample());
        for (int i : testClient) {
            StdOut.print(i);
        }
    }

}
