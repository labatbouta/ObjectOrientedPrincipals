/*******************************************************************************
 * This files was developed for CS4233: Object-Oriented Analysis & Design.
 * The course was taken at Worcester Polytechnic Institute.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/

package hanto.studentacclab.beta;

import hanto.common.HantoCoordinate;
import hanto.common.HantoException;
import hanto.common.HantoGame;
import hanto.common.HantoPieceType;
import hanto.common.HantoPlayerColor;
import hanto.common.MoveResult;
import hanto.studentacclab.common.BaseHantoGame;
import hanto.studentacclab.common.BaseHantoPiece;
import hanto.studentacclab.common.Coordinate;

/**
 * This class contains the rules for a beta hanto game
 * @author acclab
 *
 */
public class BetaHantoGame extends BaseHantoGame implements HantoGame {

	/* constant for end game by turn count condition */
	private int MAX_NUM_MOVES = 6;

	/**
	 * Constructor for BetaHantoGame
	 * @param movesFirst first player to move, red or blue
	 */
	public BetaHantoGame(HantoPlayerColor movesFirst) {
		super(movesFirst);
	}

	/**
	 * @see hanto.common.HantoGame#makeMove(hanto.common.HantoPieceType, hanto.common.HantoCoordinate, hanto.common.HantoCoordinate)
	 */
	@Override
	public MoveResult makeMove(HantoPieceType pieceType, HantoCoordinate from,
			HantoCoordinate to) throws HantoException {

		/* convert coordinates */ 
		super.convertCoordinates(from, to);
		
		/* pre move validation */
		BaseHantoPiece piece = super.preMoveValidation(pieceType, from, to);
		MoveResult moveResult = piece.isMoveValidBeta(from, to, moveCount);

		/* set piece location */
		super.setLocation(cTo, piece);
		
		/* check result of move for an end of game condition */
		MoveResult gameResult = super.isGameOver(moveResult, MAX_NUM_MOVES);
		
		/* gives the other player the next move */
		super.setCurrentMove(currentMove);

		return gameResult;
	}
	/**
	 * @see hanto.studentacclab.common.BaseHantoGame#checkForInvalidMove(HantoPieceType, Coordinate, Coordinate)
	 */
	@Override
	public void checkForInvalidMove(HantoPieceType pieceType, Coordinate from,
			Coordinate to) throws HantoException {
		
		if(!isMoveLocationEmpty(to)) {
			throw new HantoException("Cant move to an occupied location");
		}
		
		/* guarantees first move is at (0,0) */
		if(moveCount == 1 && (currentMove == movesFirst) && ((to.getX() != 0) || (to.getY() != 0))) {
			throw new HantoException("First move must be at (0,0)");
		}
		
		/* guarantees piece is placed adjacent to another piece and the 
		 location the piece is moved to is not occupied */
		if(!new Coordinate(0, 0).isAdjacent(to) && (moveCount == 1) && (currentMove != movesFirst)) { 
			throw new HantoException("2nd piece played must be next to the first");
		}

		if(moveCount == 3 && !(isButterflyOnBoard(HantoPlayerColor.BLUE) || pieceType.equals(HantoPieceType.BUTTERFLY))) {
			throw new HantoException("Player must play a butterfly");
		}

		if(moveCount == 3 && !(isButterflyOnBoard(HantoPlayerColor.RED) || pieceType.equals(HantoPieceType.BUTTERFLY))) {
			throw new HantoException("Player must play a butterfly");
		}

		if(pieceType.equals(HantoPieceType.BUTTERFLY) && (redButterfly != null) && currentMove == HantoPlayerColor.RED) {
			if(from == null) {
				throw new HantoException("Player cannot place two butterflies");
			}
		}

		if(pieceType.equals(HantoPieceType.BUTTERFLY) && (blueButterfly != null) && currentMove == HantoPlayerColor.BLUE) {
			if(from == null) {
				throw new HantoException("Player cannot place two butterflies");
			}
		}
		
		if(from != null){
			throw new HantoException("Piece already placed on board cannot be moved again");
		}
	}
}
