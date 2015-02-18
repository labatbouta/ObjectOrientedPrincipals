/*******************************************************************************
 * This files was developed for CS4233: Object-Oriented Analysis & Design.
 * The course was taken at Worcester Polytechnic Institute.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/

package hanto.studentacclab.gamma;

import hanto.common.HantoCoordinate;
import hanto.common.HantoException;
import hanto.common.HantoGame;
import hanto.common.HantoPiece;
import hanto.common.HantoPieceType;
import hanto.common.HantoPlayerColor;
import hanto.common.MoveResult;
import hanto.studentacclab.common.BaseHantoGame;
import hanto.studentacclab.common.BaseHantoPiece;
import hanto.studentacclab.common.Coordinate;
import hanto.studentacclab.common.HantoPieceFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class contains the rules for a gamma hanto game
 * @author acclab
 *
 */
public class GammaHantoGame extends BaseHantoGame implements HantoGame {

	/* constant for end game by turn count condition */
	private int MAX_NUM_MOVES = 20;

	/**
	 * Constructor for GammaHantoGame
	 * @param movesFirst first player to move, red or blue
	 */
	public GammaHantoGame(HantoPlayerColor movesFirst) {
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
		MoveResult moveResult = isPieceMovementValid(pieceType, from, to);

		/* set piece location */
		super.setLocation(cTo, piece);

		if(!isBoardContiguous(board)) {
			throw new HantoException("Bad Move. Board is not contiguous");
		}

		/* check result of move for an end of game condition */
		MoveResult gameResult = super.isGameOver(moveResult, MAX_NUM_MOVES);


		/* gives the other player the next move */
		super.setCurrentMove(currentMove);

		return gameResult;
	}

	/**
	 * Method to determine if the way the piece moves is valid, whether walking or flying
	 * @param pieceType piece to be moved
	 * @param from location piece is moved from
	 * @param to location piece is moved to
	 * @return A MoveResult, OK or an end game condition
	 * @throws HantoException Thrown if a piece movement is invalid
	 */
	public MoveResult isPieceMovementValid(HantoPieceType pieceType, HantoCoordinate from,
			HantoCoordinate to) throws HantoException {

		MoveResult result = null;
		List<Coordinate> neighbors = super.getNeighbors(cTo);

		/* piece did not move */
		if(from == null) {
			if(cTo.equals(new Coordinate(0, 0)) || (moveCount ==  1)) {
				result = MoveResult.OK;
			}
			for(Coordinate neighbor : neighbors) {
				if(board.get(neighbor).getColor().equals(currentMove)) {
					result = MoveResult.OK;
					break;
				}
			}
			
			if(result == null) {
				throw new HantoException("Cannot place piece next to the opposing color");
			}
		}

		else if(from != null) {
			boolean walkValid = isWalkValid(pieceType, cFrom, cTo);
			if(walkValid) {
				result = MoveResult.OK;
			}
			else {
				throw new HantoException("Bad Move. Cannot walk this way.");
			}
		}

		return result;
	}

	/**
	 * Method to determine if walk is valid
	 * @param pieceType piece to be placed on the board
	 * @param from location piece is moved from
	 * @param to location piece is moved to
	 * @return True if walk is valid
	 * @throws HantoException Thrown if a piece walk is invalid
	 */
	public boolean isWalkValid(HantoPieceType pieceType, Coordinate from, Coordinate to) throws HantoException {
		
		boolean result = false;

		if(isMoveLocationEmpty(from)) {
			throw new HantoException("Cannot move a piece from an empty space");
		}
		/* get neighbors of from and to */
		List<Coordinate> fromNeighbors = getAdjacentLocations(from); 
		List<Coordinate> toNeighbors = getAdjacentLocations(to); 

		Map<Coordinate, HantoPiece> clonedBoard = new HashMap<Coordinate, HantoPiece>(board);

		board.put(to, HantoPieceFactory.makeHantoPiece(currentMove, pieceType));

		if(!(getPieceAt(from).getType().equals(getPieceAt(to).getType())) || 
				!(getPieceAt(from).getColor().equals(getPieceAt(to).getColor()))) {
			throw new HantoException("Cannot move the wrong piece");
		}

		board.remove(from);

		/* get intersect of the two lists */
		fromNeighbors.retainAll(toNeighbors);

		if (fromNeighbors.size() == 2) {
			if (isMoveLocationEmpty(fromNeighbors.get(0))
					|| isMoveLocationEmpty(fromNeighbors.get(1))) {
				result = true;
			}
		}

		if(!isBoardContiguous(board)) {
			result = false;

			/* if false, revert changes made to the board, use original board */
			board.clear();
			board.putAll(clonedBoard);
		}

		/* remove duplicate insert */
		board.remove(to);

		return result;
	}

	/**
	 * Determines if the board is contiguous or not
	 * @param board current board to be analyzed for continuity
	 * @return true of board is contiguous
	 */
	public boolean isBoardContiguous (final Map<Coordinate, HantoPiece> board) {
		boolean result = true;

		List<Coordinate> unvisited = new ArrayList<Coordinate>();
		unvisited.addAll(board.keySet());
		List<Coordinate> visited = new ArrayList<Coordinate>();

		if(!unvisited.isEmpty()) {
			visited.add(unvisited.remove(0));
			while(!visited.isEmpty()) {

				Coordinate current = visited.remove(0);
				List<Coordinate> adjacent = getNeighbors(current);

				for(Coordinate c : adjacent) {
					if(unvisited.contains(c)) {
						int i = unvisited.indexOf(c);
						visited.add(unvisited.remove(i));
					}
				}
			}

			if(!unvisited.isEmpty()) {
				result = false;
			}
		}

		return result;


	}


}
