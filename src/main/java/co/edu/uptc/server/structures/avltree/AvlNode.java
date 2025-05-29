package co.edu.uptc.server.structures.avltree;

/**
 *
 * @author HOYOSPI
 */
public class AvlNode<T> {
	private T data;
	private AvlNode<T> leftNode;
	private AvlNode<T> rightNode;
	protected int fe;

	public AvlNode() {
	}

	public AvlNode(T date) {
		leftNode = null;
		this.data = date;
		rightNode = null;
		fe = 0;
	}

	public AvlNode(AvlNode<T> leftBreanch, T date, AvlNode<T> rightBreanch) {
		leftNode = leftBreanch;
		this.data = date;
		rightNode = rightBreanch;
		fe = 0;
	}

	/**
	 * @return the date
	 */
	public T getData() {
		return data;
	}

	/**
	 * @param date the date to set
	 */
	public void setData(T date) {
		this.data = date;
	}

	/**
	 * @return the izquierda
	 */
	public AvlNode<T> getLeftRoot() {
		return leftNode;
	}

	/**
	 * @param left the izquierda to set
	 */
	public void setLeftRoot(AvlNode<T> left) {
		this.leftNode = left;
	}

	/**
	 * @return the derecha
	 */
	public AvlNode<T> getRightRoot() {
		return rightNode;
	}

	/**
	 * @param right the derecha to set
	 */
	public void setRightRoot(AvlNode<T> right) {
		this.rightNode = right;
	}

	@Override
	public String toString() {
		return "[date=" + data + ", fe=" + fe + "]";
	}

	
}
