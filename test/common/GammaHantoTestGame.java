/*******************************************************************************
 * This files was developed for CS4233: Object-Oriented Analysis & Design.
 * The course was taken at Worcester Polytechnic Institute.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/

package common;

import hanto.common.HantoCoordinate;
import hanto.common.HantoException;
import hanto.common.HantoPiece;
import hanto.common.HantoPieceType;
import hanto.common.HantoPlayerColor;
import hanto.common.MoveResult;
import hanto.studentacclab.common.Coordinate;
import hanto.studentacclab.common.HantoPieceFactory;
import hanto.studentacclab.gamma.GammaHantoGame;

/**
 * Test class for DeltaHanto
 * @author acclab
 */
public class GammaHantoTestGame extends GammaHantoGame implements HantoTestGame {

	/**
	 * Constructor for a GammaHantoTestGame
	 * @param movesFirst first player color to move
	 */
	public GammaHantoTestGame(HantoPlayerColor movesFirst) {
		super(movesFirst);
	}

	@Override
	public MoveResult makeMove(HantoPieceType pieceType, HantoCoordinate from,
			HantoCoordinate to) throws HantoException {
		return super.makeMove(pieceType, from, to);
	}

	@Override
	public HantoPiece getPieceAt(HantoCoordinate where) {
		return super.getPieceAt(where); 
	}


	@Override
	public void initializeBoard(PieceLocationPair[] initialPieces) {
		super.board.clear();

		for (PieceLocationPair piece : initialPieces) {
			super.setLocation(new Coordinate(piece.location),
					HantoPieceFactory.makeHantoPiece(piece.player, piece.pieceType));
			
			if (currentMove == null) {
				currentMove = movesFirst;
			}
			else {
				super.setCurrentMove(currentMove);
			}
			
			if(piece.pieceType == HantoPieceType.BUTTERFLY) {
				if (currentMove == HantoPlayerColor.RED) {
					redButterfly = new Coordinate(piece.location);
				}
				if (currentMove == HantoPlayerColor.BLUE) {
					blueButterfly = new Coordinate(piece.location);
				}
			}
		}

	}

	@Override
	public void setTurnNumber(int turnNumber) {
		super.moveCount = turnNumber;

	}

	@Override
	public void setPlayerMoving(HantoPlayerColor player) {
		currentMove = player;

	}

	@Override
	public String getPrintableBoard() {
		return super.getPrintableBoard();
	}

}
