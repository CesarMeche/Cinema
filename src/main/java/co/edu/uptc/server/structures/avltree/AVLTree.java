package co.edu.uptc.server.structures.avltree;

import java.util.ArrayList;
import java.util.Comparator;

public class AVLTree<T> {
    AvlNode<T> root;
    Comparator<T> comparator;

    public AVLTree() {
    }

    public AVLTree(Comparator<T> comparator) {
        root = null;
        this.comparator = comparator;
    }

    public AvlNode<T> getRoot() {
        return root;
    }

    public void insert(T data) {
        Logical h = new Logical(false);
        try {
            root = insertAvl1(root, data, h);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private AvlNode<T> insertAvl1(AvlNode<T> root, T newData, Logical flag) throws Exception {
        if (root == null) {
            root = new AvlNode<>(newData);
            flag.setLogical(true);
        } else if (comparator.compare(newData, root.getData()) < 0) {
            root = insertLeft(root, newData, flag);
        } else if (comparator.compare(newData, root.getData()) > 0) {
            root = insertRight(root, newData, flag);
        } else {
            throw new Exception("Duplicate keys are not allowed");
        }
        return root;
    }

    private AvlNode<T> insertRight(AvlNode<T> root, T newData, Logical flag) throws Exception {
        AvlNode<T> rightNode = insertAvl((AvlNode<T>) root.getRightRoot(), newData, flag);
        root.setRightRoot(rightNode);
        if (flag.booleanValue()) {
            switch (root.fe) {
                case 1:
                    AvlNode<T> n1 = (AvlNode<T>) root.getRightRoot();
                    if (n1.fe == 1)
                        root = rotateRightRight(root, n1);
                    else
                        root = rotateLeftRight(root, n1);
                    flag.setLogical(false);
                    break;
                case 0:
                    root.fe = 1;
                    break;
                case -1:
                    root.fe = 0;
                    flag.setLogical(false);
                    break;
            }
        }
        return root;
    }

    private AvlNode<T> insertLeft(AvlNode<T> root, T newData, Logical flag) throws Exception {
        AvlNode<T> leftNode = insertAvl1((AvlNode<T>) root.getLeftRoot(), newData, flag);
        root.setLeftRoot(leftNode);
        if (flag.booleanValue()) {
            switch (root.fe) {
                case 1:
                    root.fe = 0;
                    flag.setLogical(false);
                    break;
                case 0:
                    root.fe = -1;
                    break;
                case -1:
                    AvlNode<T> n1 = (AvlNode<T>) root.getLeftRoot();
                    if (n1.fe == -1)
                        root = rotateLeftLeft(root, n1);
                    else
                        root = rotateRightLeft(root, n1);
                    flag.setLogical(false);
                    break;
            }
        }
        return root;
    }

    private AvlNode<T> insertAvl(AvlNode<T> root, T newData, Logical flag) throws Exception {
        if (root == null) {
            root = new AvlNode<>(newData);
            flag.setLogical(true);
        } else if (comparator.compare(newData, root.getData()) < 0) {
            AvlNode<T> leftNode = insertAvl((AvlNode<T>) root.getLeftRoot(), newData, flag);
            root.setLeftRoot(leftNode);
            if (flag.booleanValue()) {
                switch (root.fe) {
                    case 1:
                        root.fe = 0;
                        flag.setLogical(false);
                        break;
                    case 0:
                        root.fe = -1;
                        break;
                    case -1:
                        AvlNode<T> n1 = (AvlNode<T>) root.getLeftRoot();
                        if (n1.fe == -1)
                            root = rotateLeftLeft(root, n1);
                        else
                            root = rotateRightLeft(root, n1);
                        flag.setLogical(false);
                        break;
                }
            }
        } else if (comparator.compare(newData, root.getData()) > 0) {
            AvlNode<T> rightNode = insertAvl((AvlNode<T>) root.getRightRoot(), newData, flag);
            root.setRightRoot(rightNode);
            if (flag.booleanValue()) {
                switch (root.fe) {
                    case 1:
                        AvlNode<T> n1 = (AvlNode<T>) root.getRightRoot();
                        if (n1.fe == 1)
                            root = rotateRightRight(root, n1);
                        else
                            root = rotateLeftRight(root, n1);
                        flag.setLogical(false);
                        break;
                    case 0:
                        root.fe = 1;
                        break;
                    case -1:
                        root.fe = 0;
                        flag.setLogical(false);
                        break;
                }
            }
        } else {
            throw new Exception("Duplicate keys are not allowed");
        }
        return root;
    }

    // Rotations
    private AvlNode<T> rotateLeftLeft(AvlNode<T> n, AvlNode<T> n1) {
        n.setLeftRoot(n1.getRightRoot());
        n1.setRightRoot(n);
        if (n1.fe == -1) {
            n.fe = 0;
            n1.fe = 0;
        } else {
            n.fe = -1;
            n1.fe = 1;
        }
        return n1;
    }

    private AvlNode<T> rotateRightRight(AvlNode<T> n, AvlNode<T> n1) {
        n.setRightRoot(n1.getLeftRoot());
        n1.setLeftRoot(n);
        if (n1.fe == 1) {
            n.fe = 0;
            n1.fe = 0;
        } else {
            n.fe = 1;
            n1.fe = -1;
        }
        return n1;
    }

    private AvlNode<T> rotateRightLeft(AvlNode<T> n, AvlNode<T> n1) {
        AvlNode<T> n2 = (AvlNode<T>) n1.getRightRoot();
        n.setLeftRoot(n2.getRightRoot());
        n2.setRightRoot(n);
        n1.setRightRoot(n2.getLeftRoot());
        n2.setLeftRoot(n1);

        if (n2.fe == 1)
            n1.fe = -1;
        else
            n1.fe = 0;
        if (n2.fe == -1)
            n.fe = 1;
        else
            n.fe = 0;
        n2.fe = 0;
        return n2;
    }

    private AvlNode<T> rotateLeftRight(AvlNode<T> n, AvlNode<T> n1) {
        AvlNode<T> n2 = (AvlNode<T>) n1.getLeftRoot();
        n.setRightRoot(n2.getLeftRoot());
        n2.setLeftRoot(n);
        n1.setLeftRoot(n2.getRightRoot());
        n2.setRightRoot(n1);

        if (n2.fe == 1)
            n.fe = -1;
        else
            n.fe = 0;
        if (n2.fe == -1)
            n1.fe = 1;
        else
            n1.fe = 0;
        n2.fe = 0;
        return n2;
    }

    // Deletion
    public void delete(T data) {
        try {
            Logical flag = new Logical(false);
            root = deleteAvl(root, data, flag);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean remove(T data) {
        try {
            delete(data);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private AvlNode<T> deleteAvl(AvlNode<T> node, T data, Logical heightChange) throws Exception {
        if (node == null) {
            throw new Exception("Node not found");
        } else if (comparator.compare(data, node.getData()) < 0) {
            AvlNode<T> left = deleteAvl((AvlNode<T>) node.getLeftRoot(), data, heightChange);
            node.setLeftRoot(left);
            if (heightChange.booleanValue())
                node = balanceRight(node, heightChange);
        } else if (comparator.compare(data, node.getData()) > 0) {
            AvlNode<T> right = deleteAvl((AvlNode<T>) node.getRightRoot(), data, heightChange);
            node.setRightRoot(right);
            if (heightChange.booleanValue())
                node = balanceLeft(node, heightChange);
        } else {
            if (node.getLeftRoot() == null) {
                node = (AvlNode<T>) node.getRightRoot();
                heightChange.setLogical(true);
            } else if (node.getRightRoot() == null) {
                node = (AvlNode<T>) node.getLeftRoot();
                heightChange.setLogical(true);
            } else {
                AvlNode<T> left = replace(node, (AvlNode<T>) node.getLeftRoot(), heightChange);
                node.setLeftRoot(left);
                if (heightChange.booleanValue())
                    node = balanceRight(node, heightChange);
            }
        }
        return node;
    }

    private AvlNode<T> replace(AvlNode<T> n, AvlNode<T> current, Logical heightChange) {
        if (current.getRightRoot() != null) {
            AvlNode<T> right = replace(n, (AvlNode<T>) current.getRightRoot(), heightChange);
            current.setRightRoot(right);
            if (heightChange.booleanValue())
                current = balanceLeft(current, heightChange);
        } else {
            n.setData(current.getData());
            current = (AvlNode<T>) current.getLeftRoot();
            heightChange.setLogical(true);
        }
        return current;
    }

    private AvlNode<T> balanceRight(AvlNode<T> n, Logical heightChange) {
        AvlNode<T> n1;
        switch (n.fe) {
            case -1:
                n.fe = 0;
                break;
            case 0:
                n.fe = 1;
                heightChange.setLogical(false);
                break;
            case 1:
                n1 = (AvlNode<T>) n.getRightRoot();
                if (n1.fe >= 0) {
                    if (n1.fe == 0)
                        heightChange.setLogical(false);
                    n = rotateRightRight(n, n1);
                } else
                    n = rotateLeftRight(n, n1);
                break;
        }
        return n;
    }

    private AvlNode<T> balanceLeft(AvlNode<T> n, Logical heightChange) {
        AvlNode<T> n1;
        switch (n.fe) {
            case -1:
                n1 = (AvlNode<T>) n.getLeftRoot();
                if (n1.fe <= 0) {
                    if (n1.fe == 0)
                        heightChange.setLogical(false);
                    n = rotateLeftLeft(n, n1);
                } else
                    n = rotateRightLeft(n, n1);
                break;
            case 0:
                n.fe = -1;
                heightChange.setLogical(false);
                break;
            case 1:
                n.fe = 0;
                break;
        }
        return n;
    }

    // Traversals
    public void inOrder(AvlNode<T> node, ArrayList<T> list) {
        if (node != null) {
            inOrder(node.getLeftRoot(), list);
            list.add(node.getData());
            inOrder(node.getRightRoot(), list);
        }
    }

    public ArrayList<T> getInOrder() {
        ArrayList<T> list = new ArrayList<>();
        inOrder(root, list);
        return list;
    }

    public int size() {
        return size(root);
    }

    private int size(AvlNode<T> node) {
        if (node == null) return 0;
        return 1 + size(node.getLeftRoot()) + size(node.getRightRoot());
    }

    public T get(T data) {
        AvlNode<T> node = findNode(root, data);
        if (node != null)
            return node.getData();
        return null;
    }

    private AvlNode<T> findNode(AvlNode<T> node, T data) {
        if (node == null) return null;
        int comparison = comparator.compare(data, node.getData());
        if (comparison == 0)
            return node;
        else if (comparison < 0)
            return findNode((AvlNode<T>) node.getLeftRoot(), data);
        else
            return findNode((AvlNode<T>) node.getRightRoot(), data);
    }
}
