/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package co.edu.uptc.server.structures.avltree;

/**
 *
 * @author HOYOSPI
 */
public class Logical {
	private boolean v;

	public Logical(boolean f) {
		v = f;
	}

	/**
	 * @return the v
	 */
	public boolean booleanValue() {
		return v;
	}

	/**
	 * @param v the v to set
	 */
	public void setLogical(boolean v) {
		this.v = v;
	}
}
