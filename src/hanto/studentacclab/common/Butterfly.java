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
 * This is the butterfly piece class for the Alpha and Beta Hanto Game
 * @author acclab
 *
 */
public class Butterfly extends BaseHantoPiece  implements HantoPiece {

	/**
	 * Constructor for a Butterfly
	 * @param color the color of the player
	 * @param type the type of piece the player is using
	 */
	public Butterfly(HantoPlayerColor color, HantoPieceType type) {
		super(color, type);
	}

	/**
	 * Checks in the Alpha Hanto Game to see if a piece can move from one set of coordinates to another using the move count to check the color of the piece
	 * @param from the current location of the piece
	 * @param to the location the piece would like to move to
	 * @param moveCount the amount of moves made in the game starting at 0
	 * @return the result of the move OK or DRAW or an exception
	 * @throws HantoException if the move is not valid
	 */
	@Override
	public MoveResult isMoveValidAlpha(HantoCoordinate from, HantoCoordinate to, int moveCount) throws HantoException {
		MoveResult result = null;
		if (this.getColor() == HantoPlayerColor.BLUE) { 
			if (from == null && to.getX() == 0 && to.getY() == 0) { // first move of the game
				result = MoveResult.OK;
			}
			else {
				throw new HantoException("Bad Move");

			}
		}
	

	else if(this.getColor() == HantoPlayerColor.RED) {
		if (from == null && new Coordinate(0, 0).isAdjacent(new Coordinate(to))) {
			result = MoveResult.DRAW;
		}
		else {

			throw new HantoException("Bad Move");

		}
	}
	return result;
}


/**
 * Checks in the Beta Hanto Game to see if a piece can move from one set of coordinates to another using the move count to check the color of the piece
 * @param from the current location of the piece
 * @param to the location the piece would like to move to
 * @param moveCount the amount of moves made in the game starting at 0
 * @return the result of the move OK, DRAW, RED_WIN, BLUE_WIN or an exception
 */
public MoveResult isMoveValidBeta(HantoCoordinate from, HantoCoordinate to,
		int moveCount) {
	MoveResult result = null;

	if(from == null) {
		result = MoveResult.OK;
	}
	else {
		try {
			throw new HantoException("Bad Move");
		} catch (HantoException e) {
			e.printStackTrace();
		}
	}
	return result;

}
}
