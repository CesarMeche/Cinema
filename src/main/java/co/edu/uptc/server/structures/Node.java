package co.edu.uptc.server.structures;

public class Node<T> {
    private T data;
    private Node<T> nextNode;

    public Node(T data) {
        this.data = data;
    }

    public T getValue() {
        return data;
    }

    public void setValue(T data) {
        this.data = data;
    }

    public Node<T> getNext() {
        return nextNode;
    }

    public void setNext(Node<T> node) {
        this.nextNode = node;
    }

}
