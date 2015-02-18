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
import hanto.common.HantoException;
import hanto.common.HantoPiece;
import hanto.common.HantoPieceType;
import hanto.common.HantoPlayerColor;
import hanto.common.MoveResult;

/**
 * Abstract class for all HantoPiece's. Contains all common code for pieces.
 * @author acclab
 *
 */
public abstract class BaseHantoPiece implements HantoPiece {

	private HantoPlayerColor color;
	private HantoPieceType type;

	/**
	 * Constuctor for HantoPieces
	 * @param color PlayerColor of the piece
	 * @param type Type of piece to be played
	 */
	protected BaseHantoPiece(final HantoPlayerColor color, final HantoPieceType type) {
		this.color = color;
		this.type = type;
	}

	/**
	 * @see hanto.common.HantoPiece#getColor()
	 */
	@Override
	public HantoPlayerColor getColor() {
		return color;
	}

	/**
	 * @see hanto.common.HantoPiece#getType()
	 */
	@Override
	public HantoPieceType getType() {
		return type;
	}

	/**
	 * Move validator for indiviual pieces for individual games
	 * @param from Location moved from
	 * @param to location moved to
	 * @param moveCount current move count
	 * @return MoveResult, OK if move is legal
	 * @throws HantoException 
	 */
	public MoveResult isMoveValidAlpha(HantoCoordinate from, HantoCoordinate to, int moveCount) throws HantoException {
		throw new HantoException("Override"); // to get rid of yellow flag. Overrided by piece classes
	}

	/**
	 * Move validator for indiviual pieces for individual games
	 * @param from Location moved from
	 * @param to location moved to
	 * @param moveCount current move count
	 * @return MoveResult, OK if move is legal
	 */
	public MoveResult isMoveValidBeta(HantoCoordinate from, HantoCoordinate to, int moveCount) {
		return null;
	}

}
