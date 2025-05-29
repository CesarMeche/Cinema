package co.edu.uptc.server.structures;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MyQueueu<T> implements Iterable<T> {

    private Node<T> front;
    private Node<T> last;

    public void push(T data) {
        if (last == null && front == null) {
            last = new Node<T>(data);
            front = last;
        } else {
            Node<T> newData = new Node<T>(data);
            last.setNext(newData);
            last = newData;
        }
    }

    public T poll() {
        T output = null;
        if (front != null) {
            output = front.getValue();
            front = front.getNext();
        }
        if (front == null) {
            last = null;
        }
        return output;
    }

    public T peak() {
        return front != null ? front.getValue() : null;
    }

    public boolean isEmpty() {
        return front == null;
    }

    public List<T> toList() {
        List<T> dataList = new ArrayList<>();
        Node<T> current = front;
        while (current != null) {
            dataList.add(current.getValue());
            current = current.getNext();
        }
        return dataList;
    }

    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {
            private Node<T> current = front;
            private Node<T> lastReturned = null;
            private Node<T> prev = null;

            @Override
            public boolean hasNext() {
                return current != null;
            }

            @Override
            public T next() {
                T data = current.getValue();
                lastReturned = current;
                current = current.getNext();
                return data;
            }

            @Override
            public void remove() {
                if (lastReturned == null) {
                    throw new IllegalStateException("next() must be called before remove()");
                }
                if (lastReturned == front) {
                    front = front.getNext();
                    if (front == null) {
                        last = null; 
                    }
                } else {
                    prev.setNext(lastReturned.getNext());
                    if (lastReturned == last) {
                        last = prev; 
                    }
                }
                lastReturned = null;
            }

        };
    }

}