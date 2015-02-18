/*******************************************************************************
 * This files was developed for CS4233: Object-Oriented Analysis & Design.
 * The course was taken at Worcester Polytechnic Institute.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/

package hanto.studentacclab.epsilon.test;

import static hanto.common.HantoPieceType.BUTTERFLY;
import static hanto.common.HantoPieceType.CRAB;
import static hanto.common.HantoPieceType.CRANE;
import static hanto.common.HantoPieceType.SPARROW;
import static hanto.common.HantoPlayerColor.BLUE;
import static hanto.common.HantoPlayerColor.RED;
import static hanto.common.MoveResult.OK;
import static hanto.common.MoveResult.RED_WINS;
import static org.junit.Assert.assertEquals;
import hanto.common.HantoCoordinate;
import hanto.common.HantoException;
import hanto.common.HantoGame;
import hanto.common.HantoGameID;
import hanto.common.HantoPiece;
import hanto.common.HantoPieceType;
import hanto.common.HantoPlayerColor;
import hanto.common.HantoPrematureResignationException;
import hanto.common.MoveResult;
import hanto.studentacclab.HantoGameFactory;
import hanto.studentacclab.common.Coordinate;
import hanto.studentacclab.epsilon.EpsilonHantoGame;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import common.HantoTestGame;
import common.HantoTestGame.PieceLocationPair;
import common.HantoTestGameFactory;

/**
 * Description
 * @version Oct 2, 2014
 */
public class StudentEpsilonHantoTest
{
	/**
	 * Internal class for these test cases.
	 * @version Sep 13, 2014
	 */
	class TestHantoCoordinate implements HantoCoordinate
	{
		private final int x, y;
		
		public TestHantoCoordinate(int x, int y)
		{
			this.x = x;
			this.y = y;
		}
		/*
		 * @see hanto.common.HantoCoordinate#getX()
		 */
		@Override
		public int getX()
		{
			return x;
		}

		/*
		 * @see hanto.common.HantoCoordinate#getY()
		 */
		@Override
		public int getY()
		{
			return y;
		}

	}
	
	private static HantoTestGameFactory factory;
	private HantoGame game;
	private HantoTestGame testGame;
	
	@BeforeClass
	public static void initializeClass()
	{
		factory = HantoTestGameFactory.getInstance();
	}
	
	@Before
	public void setup()
	{
		// By default, blue moves first.
		testGame = factory.makeHantoTestGame(HantoGameID.EPSILON_HANTO);
		game = testGame;
	}
	
	@Test
	public void bluePlacesButterflyFirst() throws HantoException
	{
		final MoveResult mr = game.makeMove(BUTTERFLY, null, makeCoordinate(0, 0));
		assertEquals(OK, mr);
		final HantoPiece piece = game.getPieceAt(makeCoordinate(0, 0));
		assertEquals(BLUE, piece.getColor());
		assertEquals(BUTTERFLY, piece.getType());
	}
	
	@Test(expected=HantoPrematureResignationException.class)
	public void blueResignsImmediately() throws HantoException
	{
		// use the factory for coverage
		game = HantoGameFactory.getInstance().makeHantoGame(HantoGameID.EPSILON_HANTO);
		 game.makeMove(null, null, null);
	}
	
	@Test(expected=HantoException.class)
	public void attemptToMoveAfterGameIsOver() throws HantoException
	{
		final PieceLocationPair[] board = new PieceLocationPair[] {
			    plPair(BLUE, BUTTERFLY, 0, 0), plPair(RED, BUTTERFLY, 0, 1),
			    plPair(BLUE, SPARROW, -1, 0), plPair(RED, SPARROW, 1, -1),
			    plPair(BLUE, SPARROW, -1, 1), plPair(RED, SPARROW, 0, -1),
			    plPair(RED, CRAB, 1, 1)
			    
		};
		testGame.initializeBoard(board);
		testGame.setPlayerMoving(RED);
		game.makeMove(CRAB, makeCoordinate(1, 1), makeCoordinate(1, 0));	// RED wins
		game.makeMove(SPARROW, makeCoordinate(-1, 1), makeCoordinate(-1, 2));
	}
	
	@Test
	public void placeACrab() throws HantoException
	{
		assertEquals(OK, game.makeMove(CRAB, null, makeCoordinate(0, 0)));
		final HantoPiece hp = game.getPieceAt(makeCoordinate(0, 0));
		assertEquals(BLUE, hp.getColor());
		assertEquals(CRAB, hp.getType());
	}
	
	@Test
	public void sparrowFliesMoreThanOneSpace() throws HantoException
	{
		final PieceLocationPair[] board = new PieceLocationPair[] {
			    plPair(BLUE, BUTTERFLY, 0, 0), plPair(RED, BUTTERFLY, 0, 1),
			    plPair(BLUE, SPARROW, -1, 0), plPair(RED, SPARROW, 1, -1),
			    plPair(BLUE, CRAB, -1, 1), plPair(RED, SPARROW, 0, -1),
			    plPair(RED, CRAB, 1, 1)
		};
		testGame.initializeBoard(board);
		testGame.setTurnNumber(4);
		assertEquals(OK, game.makeMove(SPARROW, makeCoordinate(-1, 0), makeCoordinate(2, 1)));
		final HantoPiece hp = game.getPieceAt(makeCoordinate(2, 1));
		assertEquals(BLUE, hp.getColor());
		assertEquals(SPARROW, hp.getType());
	}
	
	@Test(expected=HantoException.class)
	public void sparrowFliesToInvalidLocation() throws HantoException
	{
		final PieceLocationPair[] board = new PieceLocationPair[] {
			    plPair(BLUE, BUTTERFLY, 0, 0), plPair(RED, BUTTERFLY, 0, 1),
			    plPair(BLUE, SPARROW, -1, 0), plPair(RED, SPARROW, 1, -1)
		};
		testGame.initializeBoard(board);
		testGame.setTurnNumber(3);
		game.makeMove(SPARROW, makeCoordinate(-1, 0), makeCoordinate(0, 3));
	}
	
	@Test(expected=HantoException.class)
	public void crabWalksAndCreatesDisconnectedConfiguration() throws HantoException
	{
		final PieceLocationPair[] board = new PieceLocationPair[] {
			    plPair(BLUE, BUTTERFLY, 0, 0), plPair(RED, BUTTERFLY, 0, 1),
			    plPair(BLUE, SPARROW, -1, 0), plPair(RED, CRAB, 1, 0),
			    plPair(BLUE, CRAB, -2, 0), plPair(RED, CRAB, 2, 0),
			    plPair(BLUE, SPARROW, -3, 0)
		};
		testGame.initializeBoard(board);
		testGame.setTurnNumber(4);
		testGame.setPlayerMoving(RED);
		game.makeMove(CRAB, makeCoordinate(1, 0), makeCoordinate(1, -1));
	}
	
	@Test(expected=HantoException.class)
	public void attemptToUsePieceNotInGame() throws HantoException
	{
		game.makeMove(CRANE, null, makeCoordinate(0, 0));
	}
	
	@Test
	public void moveByFlying() throws HantoException
	{

		final PieceLocationPair[] board = new PieceLocationPair[] {
			    plPair(BLUE, BUTTERFLY, 0, 0), plPair(RED, BUTTERFLY, 0, 1),
			    plPair(BLUE, SPARROW, -1, 0), plPair(RED, CRAB, 1, -1),
			    plPair(BLUE, CRAB, -1, 1), plPair(RED, CRAB, 0, -1),
			    plPair(RED, SPARROW, -2, 1)
			    
		};
		testGame.initializeBoard(board);
		testGame.setPlayerMoving(RED);
		testGame.setTurnNumber(4);
		assertEquals(RED_WINS, game.makeMove(SPARROW, makeCoordinate(-2, 1), makeCoordinate(1, 0)));
	}
	
	
	
	@Test
	public void spacesMoved(){
		
		Coordinate to = new Coordinate(0,0);
		Coordinate from = new Coordinate(0,0);
		Coordinate toBelowOneSpace = new Coordinate(0,-1);
		Coordinate toUp = new Coordinate(0,1);
		Coordinate toUpRight = new Coordinate(1,0);
		Coordinate toBelow= new Coordinate(-1, 0);
		Coordinate toRightDown = new Coordinate(1,-1);
		Coordinate toLeftUp = new Coordinate(-1,1);
		Coordinate toBelowRightStraight = new Coordinate(1,-2);
		Coordinate toBelowRight = new Coordinate(2,-2);
		Coordinate toAboveLeft = new Coordinate(-3, 3);
		assertEquals(0, ((EpsilonHantoGame) testGame).numberOfSpacesMoved(from, to));
		assertEquals(1,((EpsilonHantoGame) testGame).numberOfSpacesMoved(from, toBelowOneSpace) );
		assertEquals(3,((EpsilonHantoGame) testGame).numberOfSpacesMoved(from, toAboveLeft) );
		assertEquals(2,((EpsilonHantoGame) testGame).numberOfSpacesMoved(from, toBelowRightStraight) );
		assertEquals(2,((EpsilonHantoGame) testGame).numberOfSpacesMoved(from, toBelowRight) );
		assertEquals(1,((EpsilonHantoGame) testGame).numberOfSpacesMoved(from, toBelow) );
		assertEquals(1,((EpsilonHantoGame) testGame).numberOfSpacesMoved(from, toLeftUp) );
		assertEquals(1,((EpsilonHantoGame) testGame).numberOfSpacesMoved(from, toRightDown) );
		assertEquals(1,((EpsilonHantoGame) testGame).numberOfSpacesMoved(from, toUpRight) );
		assertEquals(1,((EpsilonHantoGame) testGame).numberOfSpacesMoved(from, toUp) );
		assertEquals(5,((EpsilonHantoGame) testGame).numberOfSpacesMoved(toBelowRight, toAboveLeft)); //(2,-2) (-3, 3)
		assertEquals(3,((EpsilonHantoGame) testGame).numberOfSpacesMoved(new Coordinate(-2, 0), new Coordinate(0,1)));
		
		
	}
	
	@Test
	public void testPieceCount() {
		
	}
	
	
	
	// Helper methods
	private HantoCoordinate makeCoordinate(int x, int y)
	{
		return new TestHantoCoordinate(x, y);
	}
	
	/**
	 * Factory method to create a piece location pair.
	 * @param player the player color
	 * @param pieceType the piece type
	 * @param x starting location
	 * @param y end location
	 * @return
	 */
	private PieceLocationPair plPair(HantoPlayerColor player, HantoPieceType pieceType, 
			int x, int y)
	{
		return new PieceLocationPair(player, pieceType, new TestHantoCoordinate(x, y));
	}
	
	
	
}
