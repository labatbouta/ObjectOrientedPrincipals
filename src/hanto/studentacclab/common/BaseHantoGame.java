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
import hanto.common.HantoGame;
import hanto.common.HantoPiece;
import hanto.common.HantoPieceType;
import hanto.common.HantoPlayerColor;
import hanto.common.MoveResult;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class contains default logic and fields for all hanto games. Superclassed by HantoGame versions
 * @author acclab
 *
 */
public abstract class BaseHantoGame implements HantoGame {

	protected HantoPlayerColor movesFirst;
	protected HantoPlayerColor currentMove = null;
	protected int moveCount = 1;
	protected Coordinate redButterfly = null;
	protected Coordinate blueButterfly = null;
	protected Map<Coordinate, HantoPiece> board; 
	protected Coordinate cTo;
	protected Coordinate cFrom;
	protected boolean gameOver;

	/**
	 * Constructor for a HantoGame
	 * @param movesFirst first player to move, red or blue
	 */
	protected BaseHantoGame(HantoPlayerColor movesFirst) {
		this.movesFirst = movesFirst;
		board = new HashMap<Coordinate, HantoPiece>();
	}

	/**
	 * @see hanto.common.HantoGame#makeMove(HantoPieceType, HantoCoordinate, HantoCoordinate)
	 */
	@Override
	public abstract MoveResult makeMove(HantoPieceType pieceType, HantoCoordinate from,
			HantoCoordinate to) throws HantoException;

	/**
	 * @see hanto.common.HantoGame#getPieceAt(hanto.common.HantoCoordinate)
	 */
	@Override
	public HantoPiece getPieceAt(HantoCoordinate where) {
		return board.get(new Coordinate(where));
	}

	/**
	 * @see hanto.common.HantoGame#getPrintableBoard()
	 */
	@Override
	public String getPrintableBoard() {
		String pBoard = "";

		for (Map.Entry<Coordinate, HantoPiece> entry : board.entrySet()) {
			pBoard += "coordinate:" + "(" + entry.getKey().getX() + ","
					+ +entry.getKey().getY() + ")" + ", piece: "
					+ entry.getValue().getType() + "\n";
		}
		return pBoard;
	}

	/**
	 * Adds the piece to the board at the given location
	 * @param coordinate coordinate of the piece
	 * @param piece adds this piece to the board
	 */
	public void setLocation(Coordinate coordinate, HantoPiece piece) {
		board.put(coordinate, piece);
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
			moveCount++;
			break;
		}
	}

	/**
	 * Determines if the specified players butterfly is surrounded
	 * @param butterfly location of the butterfly
	 * @return true if the butterfly is surrounded
	 */
	public boolean isButterflySurrounded(Coordinate butterfly) {
		boolean result = true;
		int x = butterfly.getX();
		int y = butterfly.getY();

		if(!board.containsKey(new Coordinate(x, y + 1))) {
			result = false;
		}
		if(!board.containsKey(new Coordinate(x + 1, y))) {
			result = false;
		}
		if(!board.containsKey(new Coordinate(x + 1, y - 1))) {
			result = false;
		}
		if(!board.containsKey(new Coordinate(x, y - 1))) {
			result = false;
		}
		if(!board.containsKey(new Coordinate(x - 1, y))) {
			result = false;
		}
		if(!board.containsKey(new Coordinate(x - 1, y + 1))) {
			result = false;
		}
		return result;
	}

	/**
	 * Determines if the butterfly has been placed
	 * @param color color of the piece
	 * @return true if the butterfly for the color is on the board
	 */
	public boolean isButterflyOnBoard(HantoPlayerColor color) {
		boolean butterflyExists = false;
		for (Map.Entry<Coordinate, HantoPiece> entry : board.entrySet()) {
			if( entry.getValue().getType() == HantoPieceType.BUTTERFLY && entry.getValue().getColor() == color) {
				butterflyExists = true;
			}
		}

		return butterflyExists;	
	}

	/**
	 * @param from location the piece moves from
	 * @param to location the piece moves to
	 */
	public void convertCoordinates(HantoCoordinate from, HantoCoordinate to) {
		cTo = new Coordinate(to);

		if(from != null) {
			cFrom = new Coordinate(from);
		}
	}

	/**
	 * Determines if the piece moves to an empty location on the board
	 * @param to location the piece moves to
	 * @return boolean if the location is empty
	 */
	public boolean isMoveLocationEmpty(Coordinate to) {
		boolean result = false;
		if(board.get(to) == null) {
			result = true;
		}
		return result;

	}

	/**
	 * Determines if the piece can move to a certain location
	 * @param to location the piece will be moves to
	 * @return boolean determining if the there is an adjacent piece to move to
	 */
	public boolean isMoveLocationAdjacent(Coordinate to) {
		boolean pieceIsAdjacentToOwnColor = false;
		boolean pieceIsAdjacentToOppositeColor = false;
		Coordinate coordinateInHashMap;

		for (Map.Entry<Coordinate, HantoPiece> entry : board.entrySet()) {
			coordinateInHashMap = new Coordinate(entry.getKey().getX(), entry.getKey().getY());

			if (coordinateInHashMap.isAdjacent(to)
					&& board.get(coordinateInHashMap).getColor() == currentMove) {
				pieceIsAdjacentToOwnColor = true;
			} else if (coordinateInHashMap.isAdjacent(to)
					&& board.get(coordinateInHashMap).getColor() != currentMove) {
				pieceIsAdjacentToOppositeColor = true;
			}
		}
		return pieceIsAdjacentToOwnColor && !pieceIsAdjacentToOppositeColor;	
	}

	/**
	 * Checks to see if the game is a draw, or if a player has won
	 * @param moveResult 
	 * @param maxNumMoves total number of moves for this game
	 * @return Draw, BlueWins or RedWins
	 */
	public MoveResult isGameOver(MoveResult moveResult, final int maxNumMoves) {
		MoveResult gameResult = null;

		if(moveCount == maxNumMoves) {
			gameResult = MoveResult.DRAW;
			gameOver = true;
		}
		if(redButterfly != null && isButterflySurrounded(redButterfly)){
			gameResult = MoveResult.BLUE_WINS;
			gameOver = true;
		}
		if(blueButterfly != null && isButterflySurrounded(blueButterfly)) {
			gameResult = MoveResult.RED_WINS;
			gameOver = true;
		}
		if(gameResult != null) {
			moveResult = gameResult;
		}

		return moveResult;
	}

	/**
	 * @param pieceType type of piece to be played on a the board
	 * @param from coordinate piece is moved from
	 * @param to from coordinate piece is moved to
	 * @return an AbsHantoPiece that is placed on the board
	 * @throws HantoException
	 */
	public BaseHantoPiece preMoveValidation(HantoPieceType pieceType, HantoCoordinate from, HantoCoordinate to) throws HantoException {
		BaseHantoPiece piece;
		if (currentMove == null) {
			currentMove = movesFirst;
		}

		/* check for an incorrect move */
		checkForInvalidMove(pieceType, cFrom, cTo);

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
		default:
			throw new HantoException("Not a valid piece");
		}

		return piece;
	}

	/**
	 * Checks to see if the move is valid, if not it throws an exception
	 * @param pieceType Type of piece to be moved
	 * @param from Location piece moves from
	 * @param to Location piece moves to
	 * @throws HantoException
	 */
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
		/* guarantees piece is placed adjacent to another piece and the 
		 location the piece is moved to is not occupied */
		if(!isMoveLocationAdjacent(to) && (moveCount > 1) && (currentMove != movesFirst)) { 
			throw new HantoException("Piece cannot be placed at an invalid location");
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
	}

	/**
	 * Finds all the neighbors of a given coordinate
	 * @param coordinate location to find neighbors
	 * @return List of Coordinates of the neighbors
	 */
	public List<Coordinate> getNeighbors(Coordinate coordinate) {

		List<Coordinate> neighbors = new ArrayList<Coordinate>();
		int x = coordinate.getX();
		int y = coordinate.getY();

		if(board.containsKey(new Coordinate(x, y + 1))){
			neighbors.add(new Coordinate(x, y + 1));
		}
		if(board.containsKey(new Coordinate(x + 1, y))) {
			neighbors.add(new Coordinate(x + 1, y));
		}
		if(board.containsKey(new Coordinate(x + 1, y - 1))) {
			neighbors.add(new Coordinate(x + 1, y - 1));
		}
		if(board.containsKey(new Coordinate(x, y - 1))) {
			neighbors.add(new Coordinate(x, y - 1));
		}
		if(board.containsKey(new Coordinate(x - 1, y))) {
			neighbors.add(new Coordinate(x - 1, y));
		}
		if(board.containsKey(new Coordinate(x - 1, y + 1))) {
			neighbors.add(new Coordinate(x - 1, y + 1));
		}

		return neighbors;
	}

	/**
	 * gets all coordinates adjacent to the current coordinate. Not necessarily on the board
	 * @param coordinate location to find adjacent coordinates
	 * @return List of Coordinates of all locations adjacent to the current coordinate
	 */
	public List<Coordinate> getAdjacentLocations(Coordinate coordinate) {

		List<Coordinate> neighbors = new ArrayList<Coordinate>();
		int x = coordinate.getX();
		int y = coordinate.getY();

		neighbors.add(new Coordinate(x, y + 1));
		neighbors.add(new Coordinate(x + 1, y));
		neighbors.add(new Coordinate(x + 1, y - 1));
		neighbors.add(new Coordinate(x, y - 1));
		neighbors.add(new Coordinate(x - 1, y));
		neighbors.add(new Coordinate(x - 1, y + 1));

		return neighbors;
	}

	/**
	 * Returns the number of spaces moved on a hexagonal grid
	 *  @see <a href="http://devmag.org.za/2013/08/31/geometry-with-hex-coordinates/">
	 *      Hexagonal Coordinate System</a>
	 * @param from the given coordinate
	 * @param to another coordinate location
	 * @return number of spaces moved
	 */
	public int numberOfSpacesMoved(Coordinate from, Coordinate to) {

		/* convert to z coordinate with: z = -(x+y) */
		int zTo = -(to.getX() + to.getY());
		int zFrom = -(from.getX() + from.getY());

		/* find deltas for each axis */
		double deltaX = Math.abs(to.getX() - from.getX());
		double deltaY = Math.abs(to.getY() - from.getY());
		double deltaZ = Math.abs(zTo - zFrom);

		int spaces = (int) Math.max(deltaX, Math.max(deltaY, deltaZ));
		return spaces;	
	}
	/**
	 * Finds if the piece is placed next to the opponent
	 * @param to Coordinate piece wants to be placed at
	 * @return true if it is next to an opponent
	 */
	public boolean isPiecePlacedNextToOpponent(Coordinate to) {
		boolean result = false;
		
		List<Coordinate> adjacencies = getAdjacentLocations(to);
		for(Coordinate c : adjacencies) {
			HantoPiece p = board.get(c);
			if(p != null) {
				if(p.getColor() != currentMove) {
					System.out.println("adjacent to" + c.toString());
					System.out.println("TO" + to.toString());
					result = true;
					break;
				}
			}
		}
		
		
		return result;
	}
	
	public Map<Coordinate, HantoPiece> getBoard() {
		return board;
	}

}
