/*******************************************************************************
 * This files was developed for CS4233: Object-Oriented Analysis & Design.
 * The course was taken at Worcester Polytechnic Institute.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/

package hanto.studentacclab.common;

import hanto.common.HantoCoordinate;

/**
 * This is the coordinates class for each piece
 * @author acclab
 *
 */
public class Coordinate implements HantoCoordinate {

	private int x;
	private int y;

	/**
	 * Coordinates Constructor
	 * @param x the x coordinate of a given piece
	 * @param y the y coordinate of a given piece
	 */
	public Coordinate(int x, int y) { 
		this.x = x;
		this.y = y;
	}

	/**
	 * Copy Constructor that returns a Coordinate from a HantoCoordinate
	 * @param coordinate the HantoCoordinate to be converted to a Coordinate
	 */
	public Coordinate(HantoCoordinate coordinate) { 
		x = coordinate.getX();
		y = coordinate.getY();
	}

	/**
	 * @see hanto.common.HantoCoordinate#getX()
	 */
	@Override
	public int getX() {
		return x;
	}

	/**
	 * @see hanto.common.HantoCoordinate#getY()
	 */
	@Override
	public int getY() {
		return y;
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		int hash = 7;
		hash = 71 * hash + x;
		hash = 71 * hash + y;
		return hash;
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override 
	public boolean equals(Object obj) {
		if (!(obj instanceof Coordinate)) {
			return false;
		}
		if (obj == this) {
			return true;
		}
		if(((Coordinate) obj).getX() == this.getX() && ((Coordinate) obj).getY() == this.getY()) {
			return true;
		}
		return false;
	}

	@Override
	public String toString() {
		return "(" + getX() + "," + getY() + ")";
	}
	
	/**
	 * Checks to see if a given coordinate is adjacent to another coordinate
	 * @param coordinate an x and y location
	 * @return true if two coordinates are adjacent
	 */
	public boolean isAdjacent(Coordinate coordinate) {
		boolean result = false;

		if(x == coordinate.getX() && y == coordinate.getY() + 1) {
			result = true;
		}
		else if(x == coordinate.getX() + 1 && y == coordinate.getY()) {
			result = true;
		}
		else if(x == coordinate.getX() + 1 && y == coordinate.getY() - 1) {
			result = true;
		}
		else if(x == coordinate.getX() && y == coordinate.getY() -1) {
			result = true;
		}
		else if(x == coordinate.getX() -1 && y == coordinate.getY()) {
			result = true;
		}
		else if(x == coordinate.getX() -1 && y == coordinate.getY() + 1) {
			result = true;
		}
		return result;
	}


}
