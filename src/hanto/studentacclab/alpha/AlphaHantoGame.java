/*******************************************************************************
 * This files was developed for CS4233: Object-Oriented Analysis & Design.
 * The course was taken at Worcester Polytechnic Institute.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/

package hanto.studentacclab.alpha;

import hanto.common.HantoCoordinate;
import hanto.common.HantoException;
import hanto.common.HantoGame;
import hanto.common.HantoPiece;
import hanto.common.HantoPieceType;
import hanto.common.HantoPlayerColor;
import hanto.common.MoveResult;
import hanto.studentacclab.common.BaseHantoGame;
import hanto.studentacclab.common.BaseHantoPiece;
import hanto.studentacclab.common.Butterfly;
import hanto.studentacclab.common.Coordinate;

import java.util.HashMap;
/**
 *  This class contains the rules for a Alpha Hanto game
 * @author acclab
 *
 */

public class AlphaHantoGame extends BaseHantoGame implements HantoGame {

	/**
	 * alpha hanto constructor
	 * @param movesFirst the color of the first player that moves either blue or red
	 */
	public AlphaHantoGame(HantoPlayerColor movesFirst) {
		super(movesFirst);
		board = new HashMap<Coordinate, HantoPiece>();
	}

	/**
	 * @see hanto.common.HantoGame#makeMove(hanto.common.HantoPieceType, hanto.common.HantoCoordinate, hanto.common.HantoCoordinate)
	 */
	@Override
	public MoveResult makeMove(HantoPieceType pieceType, HantoCoordinate from,
			HantoCoordinate to) throws HantoException {

		if(moveCount == 0 && (to.getX() != 0 || to.getY() != 0)) {
			throw new HantoException("First move must be at (0,0)");
		}

		if(from != null){
			throw new HantoException("Piece already placed on board cannot be moved again");
		}
		MoveResult result = null;
		if (currentMove == null) {
			currentMove = movesFirst;
		}

		BaseHantoPiece piece = null;
		switch (pieceType) {
		case BUTTERFLY:
			piece = new Butterfly(currentMove, HantoPieceType.BUTTERFLY);
			result =  piece.isMoveValidAlpha(from, to, moveCount);
			break;
		default:
			throw new HantoException("Not a valid piece");
		}

		

		super.setLocation(new Coordinate(to), piece);
		setCurrentMove(currentMove);
		moveCount++;

		return result;

	}

	/**
	 * Alternates moves between the blue and red player
	 * @param currentMove color to be set as the current move
	 */
	public void setCurrentMove(HantoPlayerColor currentMove) {
		switch (currentMove) {
		case BLUE:
			this.currentMove = HantoPlayerColor.RED;
			break;
		case RED:
			this.currentMove = HantoPlayerColor.BLUE;
			break;
		}
	}

}
