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

import hanto.common.HantoPiece;
import hanto.common.HantoPieceType;
import hanto.common.HantoPlayerColor;

/**
 * This is a singleton class that provides a factory to create an instance of any version
 * of a Hanto game.
 * 
 * @author acclab
 */
public class HantoPieceFactory {
	
	private static final HantoPieceFactory INSTANCE = new HantoPieceFactory();

	/**
	 * Default private descriptor.
	 */
	private HantoPieceFactory()
	{
		// Empty, but the private constructor is necessary for the singleton.
	}

	/**
	 * @return the instance
	 */
	public static HantoPieceFactory getInstance()
	{
		return INSTANCE;
	}

	/**
	 * Factory method that returns the appropriately configured Hanto piece.
	 * @param color color of the piece to be made
	 * @param pieceType type of the piece to be made
	 * @return a HantoPiece
	 */
	public static HantoPiece makeHantoPiece(HantoPlayerColor color, HantoPieceType pieceType) {
		HantoPiece piece = null;
		switch (pieceType) {
		case BUTTERFLY:
			piece = new Butterfly(color, pieceType);
			break;
		case SPARROW:
			piece = new Sparrow(color, pieceType);
			break;
		case CRAB:
			piece = new Crab(color, pieceType);
			break;
		case HORSE:
			piece = new Horse(color, pieceType);
			break;
		default:
			break;
		}
		return piece;
	}

}
