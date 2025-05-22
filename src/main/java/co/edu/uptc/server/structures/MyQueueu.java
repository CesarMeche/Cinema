package co.edu.uptc.server.structures;

public class MyQueueu<T> {

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

}