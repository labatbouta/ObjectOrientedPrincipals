/*******************************************************************************
 * This files was developed for CS4233: Object-Oriented Analysis & Design.
 * The course was taken at Worcester Polytechnic Institute.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/

package hanto.studentacclab.epsilon;

import hanto.common.HantoCoordinate;
import hanto.common.HantoException;
import hanto.common.HantoGame;
import hanto.common.HantoPiece;
import hanto.common.HantoPieceType;
import hanto.common.HantoPlayerColor;
import hanto.common.HantoPrematureResignationException;
import hanto.common.MoveResult;
import hanto.studentacclab.common.BaseHantoGame;
import hanto.studentacclab.common.BaseHantoPiece;
import hanto.studentacclab.common.Butterfly;
import hanto.studentacclab.common.Coordinate;
import hanto.studentacclab.common.Crab;
import hanto.studentacclab.common.HantoPieceFactory;
import hanto.studentacclab.common.HantoPieceSet;
import hanto.studentacclab.common.Horse;
import hanto.studentacclab.common.Sparrow;
import hanto.tournament.HantoMoveRecord;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * This class contains the rules for an Epsilon hanto game
 * @author acclab
 */
public class EpsilonHantoGame extends BaseHantoGame implements HantoGame {


	/* constant for end game by turn count condition */
	private int MAX_NUM_MOVES = Integer.MAX_VALUE;

	/* piece set for Epsilon Hanto */
	protected HantoPieceSet pieceSet;

	/**
	 * Constructor for EpsilonHantoGame
	 * @param movesFirst first player to move, red or blue
	 */
	public EpsilonHantoGame(HantoPlayerColor movesFirst) {
		super(movesFirst);
		pieceSet = new HantoPieceSet();
	}

	/**
	 * @see hanto.common.HantoGame#makeMove(hanto.common.HantoPieceType, hanto.common.HantoCoordinate, hanto.common.HantoCoordinate)
	 */
	@Override
	public MoveResult makeMove(HantoPieceType pieceType, HantoCoordinate from,
			HantoCoordinate to) throws HantoException {

		if(pieceType == null && from == null && to == null) {
			MoveResult resign = playerResigns();
			if(doesMoveExist()) {
				throw new HantoPrematureResignationException();
			}
			return resign;
		}

		/* convert coordinates */ 
		super.convertCoordinates(from, to);

		/* pre move validation */
		BaseHantoPiece piece = preMoveValidation(pieceType, from, to);
		MoveResult moveResult = isPieceMovementValid(pieceType, from, to);

		/* set piece location */
		super.setLocation(cTo, piece);


		/* update piece collection */
		pieceSet.updatePieceCollection(currentMove, pieceType);

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
	 * Determines if a piece can be played on the board. Piece is NOT placed on the board
	 * @param pieceType piece to be played on the board
	 * @param from location piece moves from
	 * @param to location piece moves from
	 * @return true if piece can be placed at the location
	 */
	public boolean isMoveLegal(HantoPieceType pieceType, HantoCoordinate from,
			HantoCoordinate to) {
		boolean result = true;

		convertCoordinates(from, to);

		if(!isMoveLocationEmpty(new Coordinate(to))) {
			result = false;
		}

		try {
			result = isPieceMovementValidBool(pieceType, from, to);	
		} catch (HantoException e) {
			result = false;
		}
		if(!isBoardContiguous(board)) {
			result = false;
		}


		if(!isMoveLocationAdjacent(new Coordinate(to))) {
			result = false;
		}

		if(!isMoveLocationEmpty(new Coordinate(to))) {
			result = false;
		}

		if(from != null && to != null) {
			if(new Coordinate(to).equals(new Coordinate(from))) {
				result = false;
			}
		}


		return result;
	}

	/**
	 * Checks to see if player resigns prematurely
	 * @throws HantoPrematureResignationException if a move exists
	 */
	private boolean doesMoveExist() {	

		boolean result = false;
		List<Coordinate> yourPieces = new ArrayList<Coordinate>();
		List<Coordinate> adjacentLocations = null;

		for (Map.Entry<Coordinate, HantoPiece> entry : board.entrySet()) {
			/* add your piece to the list */
			if(entry.getValue().getColor() == currentMove) {
				yourPieces.add(entry.getKey());
			}
		}

		/* checks to see if piece can be played onto the board */
		if(!pieceSet.isPieceCollectionEmpty(currentMove)) {

			/* find a piece to play */
			HantoPieceType piece = pieceSet.getPieceFromSet(currentMove);

			/* first move */
			if(yourPieces.isEmpty() && moveCount == 1) {
				result = true;
			}	

			for(Coordinate c : yourPieces) {				
				/* find adjacent locations for each piece */
				adjacentLocations = getAdjacentLocations(c);
				for(Coordinate co : adjacentLocations) {
					if(isMoveLegal(piece, null, co)) {
						result = true;
						return result;
					}
				}
			}
		}

		Map<Coordinate, HantoPiece> clonedBoard = new HashMap<Coordinate, HantoPiece>(board);
		HantoPiece piece;


		/* checks if pieces can be moved */
		for(Coordinate c : yourPieces) {// for all your pieces on the board		
			for (Map.Entry<Coordinate, HantoPiece> entry : clonedBoard.entrySet()) { // for all the pieces on the board

				/* find adjacent locations for each piece on the board */
				adjacentLocations = getAdjacentLocations(entry.getKey());
				for(Coordinate co : adjacentLocations) {	
					piece = clonedBoard.get(c);
					if(entry != piece) {
						if(isMoveLegal(piece.getType(), c, co)) {
							result = true;
							return result;
						}
					}
				}
			}
		}
		return result;
	}

	/**
	 * Method for our HantoPlayer in the tournament
	 * @param color player color, piece to be placed on the board
	 * @return A HantoMoveRecord for a possible move to be made
	 */
	public HantoMoveRecord doesMoveExistForPlayer(HantoPlayerColor color) {	

		HantoMoveRecord record = null;
		List<Coordinate> yourPieces = new ArrayList<Coordinate>();
		List<Coordinate> adjacentLocations = null;

		for (Map.Entry<Coordinate, HantoPiece> entry : board.entrySet()) {
			/* add your piece to the list */
			if(entry.getValue().getColor() == color) {
				yourPieces.add(entry.getKey());
			}
		}

		/* Places all of your pieces first */
		if(!pieceSet.isPieceCollectionEmpty(color)) {

			/* find a Random piece to play */
			HantoPieceType piece; 

			/* first move, place butterflies first*/
			if(yourPieces.isEmpty() && board.isEmpty() && moveCount == 1) {
				record = new HantoMoveRecord(HantoPieceType.BUTTERFLY, null, new Coordinate(0, 0));
				piece = HantoPieceType.BUTTERFLY;

				try {
					pieceSet.updatePieceCollection(color, piece);
				} catch (HantoException e) {
					e.printStackTrace();
				}
			}	
			/* place butterfly at a random location adjacent to 0,0 */
			else if(yourPieces.isEmpty() && !board.isEmpty() && moveCount == 1) {
				Random randomizer = new Random();
				List<Coordinate> coordinates = getAdjacentLocations(new Coordinate(
						0, 0));
				record = new HantoMoveRecord(HantoPieceType.BUTTERFLY, null,
						coordinates.get(randomizer.nextInt(coordinates.size())));
				piece = HantoPieceType.BUTTERFLY;

				try {
					pieceSet.updatePieceCollection(color, piece);
				} catch (HantoException e) {
					e.printStackTrace();
				}
			}
			else {
				piece = pieceSet.getRandomPiece(color);
				for(Coordinate c : yourPieces) {		
					/* find adjacent locations for each piece */
					adjacentLocations = getAdjacentLocations(c);
					for(Coordinate co : adjacentLocations) {
						if(isMoveLegal(piece, null, co)) {
							record = new HantoMoveRecord(piece, null, co);
							setCurrentMove(color);
							return record;
						}
					}
				}
			}
		}

		else {
			Map<Coordinate, HantoPiece> clonedBoard = new HashMap<Coordinate, HantoPiece>(board);
			HantoPiece piece;

			/* checks if pieces can be moved */
			for(Coordinate c : yourPieces) {// for all your pieces on the board		
				for (Map.Entry<Coordinate, HantoPiece> entry : clonedBoard.entrySet()) { // for all the pieces on the board

					/* find adjacent locations for each piece on the board */
					adjacentLocations = getAdjacentLocations(entry.getKey());
					for(Coordinate co : adjacentLocations) {	
						piece = clonedBoard.get(c);
						if(isMoveLocationEmpty(co)) {
							if(!entry.getKey().equals(c)) {
								if(isMoveLegal(piece.getType(), c, co)) {
									record = new HantoMoveRecord(piece.getType(), c, co);
									setCurrentMove(color);
									return record;
								}
							}
						}
					}
				}
			}
		}

		setCurrentMove(color);

		return record;
	}

	/**
	 * Determines victor if a player resigns
	 * @return Red or Blue wins based on who resigns
	 */
	private MoveResult playerResigns() {
		MoveResult result = null;

		if (currentMove == null) {
			currentMove = movesFirst;
		}

		switch (currentMove) {
		case BLUE:
			result = MoveResult.RED_WINS;
			break;
		case RED:
			result = MoveResult.BLUE_WINS;
			break;
		}

		return result;
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

		/* if piece moved */
		else if(from != null) {
			if(pieceType.equals(HantoPieceType.CRAB) || pieceType.equals(HantoPieceType.BUTTERFLY)) {
				boolean walkValid = isWalkValid(pieceType, cFrom, cTo); // rule set complained null pointer
				if(walkValid) {
					result = MoveResult.OK;
				}
				else {
					throw new HantoException("Bad Move. Cannot walk this way.");
				}
			}

			if(pieceType.equals(HantoPieceType.SPARROW)) {
				boolean flyValid = isFlyValid(cFrom, cTo); // rule set complained null pointer
				if(flyValid) { 
					result = MoveResult.OK;
				}
				else {
					throw new HantoException("Bad Move. Cannot fly to location");
				}
			}

			if(pieceType.equals(HantoPieceType.HORSE)) {
				boolean jumpValid = isJumpValid(pieceType, cFrom, cTo); // rule set complained null pointer
				if(jumpValid) { 
					result = MoveResult.OK;
				}
				else {
					throw new HantoException("Bad Move. Cannot jump to location");
				}
			}
		}

		return result;
	}

	/**
	 * Method to determine if the way the piece moves is valid, whether walking or flying
	 * @param pieceType piece to be moved
	 * @param from location piece is moved from
	 * @param to location piece is moved to
	 * @return A MoveResult, OK or an end game condition
	 * @throws HantoException Thrown if a piece movement is invalid
	 */
	public boolean isPieceMovementValidBool(HantoPieceType pieceType, HantoCoordinate from,
			HantoCoordinate to) throws HantoException {

		boolean result = false;
		List<Coordinate> neighbors = super.getNeighbors(cTo);

		/* piece did not move */
		if(from == null) {
			if(cTo.equals(new Coordinate(0, 0)) || (moveCount ==  1)) {
				result = true;
			}
			for(Coordinate neighbor : neighbors) {
				if(board.get(neighbor).getColor().equals(currentMove)) {
					result = true;
					break;
				}
			}

			if(!result) {
				throw new HantoException("Cannot place piece next to the opposing color");
			}
		}

		/* if piece moved */
		else if(from != null) {
			if(pieceType.equals(HantoPieceType.CRAB) || pieceType.equals(HantoPieceType.BUTTERFLY)) {
				boolean walkValid = isWalkValid(pieceType, cFrom, cTo); // rule set complained null pointer
				if(walkValid) {
					result = true;
				}
				else {
					result = false;
				}
			}

			if(pieceType.equals(HantoPieceType.SPARROW)) {
				boolean flyValid = isFlyValid(cFrom, cTo); // rule set complained null pointer
				if(flyValid) { 
					result = true;
				}
				else {
					result = false;
				}
			}

			if(pieceType.equals(HantoPieceType.HORSE)) {
				boolean jumpValid = isJumpValid(pieceType, cFrom, cTo); // rule set complained null pointer
				if(jumpValid) { 
					result = true;
				}
				else {
					result = false;
				}
			}
		}

		return result;
	}

	/**
	 * Method to determine if flight is valid
	 * @param from location piece is moved from
	 * @param to location piece is moved to
	 * @return True if flight is valid
	 * @throws HantoException Thrown if a piece flight is invalid
	 */
	public boolean isFlyValid(Coordinate from, Coordinate to) throws HantoException {
		boolean result = true;

		if(numberOfSpacesMoved(from, to) > 4) {
			throw new HantoException("Sparrow cannot fly more than 4 spaces!");
		}

		Map<Coordinate, HantoPiece> clonedBoard = new HashMap<Coordinate, HantoPiece>(board);

		board.put(to, new Sparrow(currentMove, HantoPieceType.SPARROW));

		/* check if piece at from is the same as to */
		if((getPieceAt(from) != null) && getPieceAt(to) != null) {
			if(!(getPieceAt(from).getType().equals(getPieceAt(to).getType())) || 
					!(getPieceAt(from).getColor().equals(getPieceAt(to).getColor()))) {
				board.remove(to);
				throw new HantoException("Cannot move the wrong piece");
			}
		}
		/* throw an exception if no piece at to or from */
		else {
			board.remove(to);
			throw new HantoException("No piece at location you are flying from or to");
		}

		board.remove(from);

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

		if(numberOfSpacesMoved(from, to) != 1) {
			throw new HantoException("Cannot walk more than one space");
		}
		/* get neighbors of from and to */
		List<Coordinate> fromNeighbors = getAdjacentLocations(from); 
		List<Coordinate> toNeighbors = getAdjacentLocations(to); 

		Map<Coordinate, HantoPiece> clonedBoard = new HashMap<Coordinate, HantoPiece>(board);

		board.put(to, HantoPieceFactory.makeHantoPiece(currentMove, pieceType));

		if(!(getPieceAt(from).getType().equals(getPieceAt(to).getType())) || 
				!(getPieceAt(from).getColor().equals(getPieceAt(to).getColor()))) {
			board.remove(to);
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
	 * Method to determine if jump is valid
	 * @param pieceType piece to be placed on the board
	 * @param from location piece is moved from
	 * @param to location piece is moved to
	 * @return True if jump is valid
	 */
	public boolean isJumpValid(HantoPieceType pieceType, Coordinate from, Coordinate to) {
		boolean result = false;

		int jumpLength = 0;
		int i = 0;
		int zfrom = -(from.getX()+from.getY());
		int zto = - (to.getX() + to.getY());

		if(from.getX() == to.getX()) {

			jumpLength = to.getY() - from.getY();

			if(jumpLength > 0) {

				for(i = from.getY() + 1; i < to.getY(); i++) {
					if(board.get(new Coordinate(from.getX(), i)) == null) {
						result = false;
						return result; 
					}
					result = true;
				}
			}
			else {
				for(i = from.getY() - 1; i < to.getY(); i--) {
					if(board.get(new Coordinate(from.getX(), i)) == null) {
						result = false;
						return result; 
					}
					result = true;
				}
			}

		}
		else if (from.getY() == to.getY()) {

			jumpLength = numberOfSpacesMoved(from, to);

			if(jumpLength > 0) {
				for(i = from.getX() - 1; i > to.getX(); i--) {
					if(board.get(new Coordinate(i, from.getY())) == null) {
						result = false;
						return result; 
					}
					result = true;
				}
			}
			else {
				for(i = from.getX() - 1; i < to.getX(); i--) {
					if(board.get(new Coordinate(i, from.getY())) == null) {
						result = false;
						return result; 
					}
					result = true;
				}
			}
		}
		else if (zfrom == zto) {
			jumpLength = from.getX() - to.getX();
			if(jumpLength > 0) {
				int j = from.getY() + 1;
				for(i = from.getX() - 1; i > to.getX(); i--) {
					if(!board.containsKey(new Coordinate(i, j))) {
						result = false;
						return result; 
					}
					j++;
					result = true;
				}
			}
			else {
				int j = from.getY() - 1;
				for(i = from.getX() + 1; i < to.getX(); i++) {
					if(!board.containsKey(new Coordinate(i, j))) {
						result = false;
						return result; 
					}
					j--;
					result = true;
				}
			}
		}

		else {
			result = false;

		}

		if(result) {
			board.remove(from);
		}

		return result;

	}

	/**
	 * Determines if the board is contiguous or not
	 * @param board current board to be analyzed for continuity
	 * @return true if board is contiguous
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

	/**
	 * @param pieceType type of piece to be played on a the board
	 * @param from coordinate piece is moved from
	 * @param to from coordinate piece is moved to
	 * @return an AbsHantoPiece that is placed on the board
	 * @throws HantoException
	 */
	@Override
	public BaseHantoPiece preMoveValidation(HantoPieceType pieceType, HantoCoordinate from, HantoCoordinate to) throws HantoException {
		BaseHantoPiece piece;
		if (currentMove == null) {
			currentMove = movesFirst;
		}

		/* Creates a piece depending on the type specified */
		switch (pieceType) {
		case BUTTERFLY:
			piece = new Butterfly(currentMove, HantoPieceType.BUTTERFLY);
			if (currentMove == HantoPlayerColor.RED) {
				redButterfly = cTo;
			}
			if (currentMove == HantoPlayerColor.BLUE) {
				blueButterfly = cTo;
			}
			break;
		case SPARROW:
			piece = new Sparrow(currentMove, HantoPieceType.SPARROW);
			break;
		case CRAB:
			piece = new Crab(currentMove, HantoPieceType.CRAB);
			break;
		case HORSE:
			piece = new Horse(currentMove, HantoPieceType.HORSE);
			break;
		default:
			throw new HantoException("Not a valid piece");
		}

		if(gameOver) {
			throw new HantoException("Cannot move, game is over!");
		}

		return piece;
	}

	public HantoPieceSet getPieceSet() {
		return pieceSet;
	}


}
