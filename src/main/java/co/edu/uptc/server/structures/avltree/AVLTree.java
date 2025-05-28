package co.edu.uptc.server.structures.avltree;

import java.util.ArrayList;
import java.util.Comparator;

/**
 *
 * @author HOYOSPI
 */
public class AVLTree<T> {
	AvlNode<T> root;
	Comparator<T> comparator;

	public AVLTree(Comparator<T> comparator) {
		root = null;
		this.comparator = comparator;
	}

	public AvlNode<T> treeroot() {
		return root;
	}

	// insertar nodo en arbolAVL

	public void insert(T data) throws Exception {
		Logical h = new Logical(false);
		root = insertAvl1(root, data, h);
	}

	private AvlNode<T> insertAvl1(AvlNode<T> root, T newData, Logical flag) throws Exception {

		if (root == null) {
			root = new AvlNode<T>(newData);
			flag.setLogical(true);
		} else if (comparator.compare(newData, root.getData()) < 0) {
			root = minorData(root, newData, flag);
		} else if (comparator.compare(newData, root.getData()) > 0) {
			root = greaterData(root, newData, flag);
		} else
			throw new Exception("No puede haber claves repetidas");

		return root;

	}

	private AvlNode<T> greaterData(AvlNode<T> root, T newData, Logical flag) throws Exception {
		AvlNode<T> n1;
		AvlNode<T> dr;

		dr = insertAvl((AvlNode<T>) root.getRightRoot(), newData, flag);
		root.setRightRoot(dr);// **********************************************************
		// regresa por los nodos del camino de busqueda
		if (flag.booleanValue()) {
			// decrementa el fe por aumento de la rama izquierda
			switch (root.fe) {
				case 1: // aplicar rotacion a la derecha
					n1 = (AvlNode<T>) root.getRightRoot();
					if (n1.fe == +1)
						root = rotacionDD(root, n1);
					else
						root = rotacionID(root, n1);
					flag.setLogical(false);
					break;
				case 0:
					root.fe = +1;
					break;
				case -1:
					root.fe = 0;
					flag.setLogical(false);
			}
		}
		return root;
	}

	private AvlNode<T> minorData(AvlNode<T> root, T newData, Logical flag) throws Exception {
		AvlNode<T> iz;
		AvlNode<T> n1;
		iz = insertAvl1((AvlNode<T>) root.getLeftRoot(), newData, flag);
		root.setLeftRoot(iz);
		if (flag.booleanValue()) {

			switch (root.fe) {
				case 1:
					root.fe = 0;
					flag.setLogical(false);
					break;
				case 0:
					root.fe = -1;
					break;
				case -1: // aplicar rotacion a la izquierda
					n1 = (AvlNode<T>) root.getLeftRoot();
					if (n1.fe == -1)
						root = rotacionII(root, n1);
					else
						root = rotacionDI(root, n1);
					flag.setLogical(false);
			}
		}
		return root;
	}

	private AvlNode<T> insertAvl(AvlNode<T> root, T newData, Logical flag) throws Exception {
		AvlNode<T> n1;

		if (root == null) {
			root = new AvlNode(newData);
			flag.setLogical(true);

		} else if (comparator.compare(newData, root.getData()) < 0) {
			AvlNode<T> iz;
			iz = insertAvl((AvlNode<T>) root.getLeftRoot(), newData, flag);
			root.setLeftRoot(iz);
			// ***************************************************
			// regresa por los nodos del camino de busqueda
			if (flag.booleanValue()) {
				// decrementa el fe por aumento de la rama izquierda
				switch (root.fe) {
					case 1:
						root.fe = 0;
						flag.setLogical(false);
						break;
					case 0:
						root.fe = -1;
						break;
					case -1: // aplicar rotacion a la izquierda
						n1 = (AvlNode<T>) root.getLeftRoot();
						if (n1.fe == -1)
							root = rotacionII(root, n1);
						else
							root = rotacionDI(root, n1);
						flag.setLogical(false);
				}
			}
		} else if (comparator.compare(newData, root.getData()) > 0) {
			AvlNode<T> dr;

			dr = insertAvl((AvlNode<T>) root.getRightRoot(), newData, flag);
			root.setRightRoot(dr);// **********************************************************
			// regresa por los nodos del camino de busqueda
			if (flag.booleanValue()) {
				// decrementa el fe por aumento de la rama izquierda
				switch (root.fe) {
					case 1: // aplicar rotacion a la derecha
						n1 = (AvlNode<T>) root.getRightRoot();
						if (n1.fe == +1)
							root = rotacionDD(root, n1);
						else
							root = rotacionID(root, n1);
						flag.setLogical(false);
						break;
					case 0:
						root.fe = +1;
						break;
					case -1:
						root.fe = 0;
						flag.setLogical(false);
				}
			}
		} else
			throw new Exception("No puede haber claves repetidas");
		return root;
	}
	// rotaciones en el arbolAVL

	private AvlNode<T> rotacionII(AvlNode<T> n, AvlNode<T> n1) {
		n.setLeftRoot(n1.getRightRoot());
		n1.setRightRoot(n);
		n = n1;
		// actualización de los factores de equilibrio
		if (n1.fe == -1) // se cumple en la inserción
		{
			n.fe = 0;
			n1.fe = 0;
		} else {
			n.fe = -1;
			n1.fe = 1;
		}
		return n1;
	}

	private AvlNode<T> rotacionDD(AvlNode<T> n, AvlNode<T> n1) {
		n.setRightRoot(n1.getLeftRoot());
		n1.setLeftRoot(n);
		// actualización de los factores de equilibrio
		if (n1.fe == +1) // se cumple en la inserción
		{
			n.fe = 0;
			n1.fe = 0;
		} else {
			n.fe = +1;
			n1.fe = -1;
		}
		return n1;
	}

	private AvlNode<T> rotacionDI(AvlNode<T> n, AvlNode<T> n1) {
		AvlNode<T> n2;

		n2 = (AvlNode<T>) n1.getRightRoot();
		n.setLeftRoot(n2.getRightRoot());
		n2.setRightRoot(n);
		n1.setRightRoot(n2.getLeftRoot());
		n2.setLeftRoot(n1);
		// actualización de los factores de equilibrio
		if (n2.fe == +1)
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

	private AvlNode<T> rotacionID(AvlNode<T> n, AvlNode<T> n1) {
		AvlNode<T> n2;

		n2 = (AvlNode<T>) n1.getLeftRoot();
		n.setRightRoot(n2.getLeftRoot());
		n2.setLeftRoot(n);
		n1.setLeftRoot(n2.getRightRoot());
		n2.setRightRoot(n1);
		// actualización de los factores de equilibrio
		if (n2.fe == +1)
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

	// Borrado de un nodo en árbol AVL

	public void eliminar(T data) throws Exception {
		T dato;
		dato = (T) data;
		Logical flag = new Logical(false);
		root = borrarAvl(root, dato, flag);
	}

	private AvlNode<T> borrarAvl(AvlNode<T> raiz, T data, Logical heigthchange) throws Exception {
		if (raiz == null) {
			throw new Exception(" Nodo no encontrado ");
		} else if (comparator.compare(data, raiz.getData()) < 0) {
			AvlNode<T> iz;
			iz = borrarAvl((AvlNode<T>) raiz.getLeftRoot(), data, heigthchange);
			raiz.setLeftRoot(iz);
			if (heigthchange.booleanValue())
				raiz = equilibrar1(raiz, heigthchange);
		} else if (comparator.compare(data, raiz.getData()) > 0) {
			AvlNode<T> dr;
			dr = borrarAvl((AvlNode<T>) raiz.getRightRoot(), data, heigthchange);
			raiz.setRightRoot(dr);
			if (heigthchange.booleanValue())
				raiz = equilibrar2(raiz, heigthchange);
		} else // Nodo encontrado
		{
			AvlNode<T> q;
			q = raiz; // nodo a quitar del árbol
			if (q.getLeftRoot() == null) {
				raiz = (AvlNode<T>) q.getRightRoot();
				heigthchange.setLogical(true);
			} else if (q.getRightRoot() == null) {
				raiz = (AvlNode<T>) q.getLeftRoot();
				heigthchange.setLogical(true);
			} else { // tiene rama izquierda y derecha
				AvlNode<T> iz;
				iz = reemplazar(raiz, (AvlNode<T>) raiz.getLeftRoot(), heigthchange);
				raiz.setLeftRoot(iz);
				if (heigthchange.booleanValue())
					raiz = equilibrar1(raiz, heigthchange);
			}
			q = null;
		}
		return raiz;
	}

	private AvlNode<T> reemplazar(AvlNode<T> n, AvlNode<T> actuAvlNode, Logical heigthchange) {
		if (actuAvlNode.getRightRoot() != null) {
			AvlNode<T> d;
			d = reemplazar(n, (AvlNode<T>) actuAvlNode.getRightRoot(), heigthchange);
			actuAvlNode.setRightRoot(d);
			if (heigthchange.booleanValue())
			actuAvlNode = equilibrar2(actuAvlNode, heigthchange);
		} else {
			n.setData(actuAvlNode.getData());
			n = actuAvlNode;
			actuAvlNode = (AvlNode<T>) actuAvlNode.getLeftRoot();
			n = null;
			heigthchange.setLogical(true);
		}
		return actuAvlNode;
	}

	private AvlNode<T> equilibrar1(AvlNode<T> n, Logical heigthchange) {
		AvlNode<T> n1;
		switch (n.fe) {
			case -1:
				n.fe = 0;
				break;
			case 0:
				n.fe = 1;
				heigthchange.setLogical(false);
				break;
			case +1: // se aplicar un tipo de rotación derecha
				n1 = (AvlNode<T>) n.getRightRoot();
				if (n1.fe >= 0) {
					if (n1.fe == 0) // la altura no vuelve a disminuir
						heigthchange.setLogical(false);
					n = rotacionDD(n, n1);
				} else
					n = rotacionID(n, n1);
				break;
		}
		return n;
	}

	private AvlNode<T> equilibrar2(AvlNode<T> n, Logical cambiaAltura) {
		AvlNode<T> n1;

		switch (n.fe) {
			case -1: // Se aplica un tipo de rotación izquierda
				n1 = (AvlNode<T>) n.getLeftRoot();
				if (n1.fe <= 0) {
					if (n1.fe == 0)
						cambiaAltura.setLogical(false);
					n = rotacionII(n, n1);
				} else
					n = rotacionDI(n, n1);
				break;
			case 0:
				n.fe = -1;
				cambiaAltura.setLogical(false);
				break;
			case +1:
				n.fe = 0;
				break;
		}

		return n;
	}

	public void inOrden(AvlNode<T> node, ArrayList<T> list) {
		if (node != null) {
			inOrden(node.getLeftRoot(), list);
			list.add(node.getData());
			inOrden(node.getRightRoot(), list);
		}
	}

	public void preOrden(AvlNode<T> node, ArrayList<T> list) {
		if (node != null) {
			list.add(node.getData());
			preOrden(node.getLeftRoot(), list);
			preOrden(node.getRightRoot(), list);
		}
	}


}
