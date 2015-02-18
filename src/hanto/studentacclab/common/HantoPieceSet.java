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

import hanto.common.HantoException;
import hanto.common.HantoPieceType;
import hanto.common.HantoPlayerColor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Piece set for Epsilon Hanto Specifically
 */
public class HantoPieceSet {

	protected Map<HantoPieceType, Integer> bluePlayerPieceCollection;
	protected Map<HantoPieceType, Integer> redPlayerPieceCollection;
	protected HantoPlayerColor color;


	/* constructor for Epsilon Piece Set */
	public HantoPieceSet() {
		bluePlayerPieceCollection = new HashMap<HantoPieceType, Integer>();
		redPlayerPieceCollection = new HashMap<HantoPieceType, Integer>();

		bluePlayerPieceCollection.put(HantoPieceType.BUTTERFLY, 1);
		bluePlayerPieceCollection.put(HantoPieceType.SPARROW, 2);
		bluePlayerPieceCollection.put(HantoPieceType.CRAB, 6);
		bluePlayerPieceCollection.put(HantoPieceType.HORSE, 4);

		redPlayerPieceCollection.put(HantoPieceType.BUTTERFLY, 1);
		redPlayerPieceCollection.put(HantoPieceType.SPARROW, 2);
		redPlayerPieceCollection.put(HantoPieceType.CRAB, 6);
		redPlayerPieceCollection.put(HantoPieceType.HORSE, 4);

	}

	/**
	 * Method to update piece collection when a piece is played on the board, and NOT moved
	 * @param color
	 * @param pieceType
	 * @throws HantoException if you do not have any more of this type remaining to be placed onto the board
	 */
	public void updatePieceCollection(HantoPlayerColor color, HantoPieceType pieceType)
			throws HantoException{

		int value = 0;
		if (color == HantoPlayerColor.BLUE) {
			value = bluePlayerPieceCollection.get(pieceType) - 1;

			if(value < 0) {
				throw new HantoException("Cannot play another " + pieceType);
			}
			bluePlayerPieceCollection.put(pieceType, value);

		}


		else {
			value = redPlayerPieceCollection.get(pieceType) - 1;

			if(value < 0) {
				throw new HantoException("Cannot play another " + pieceType);
			}
			redPlayerPieceCollection.put(pieceType, value);
		}

	}

	/**
	 * Finds if the player's piece collection is empty or not
	 * @param color Player color
	 * @return True if no pieces left to play off the board, false if pieces can be put on the board
	 */
	public boolean isPieceCollectionEmpty(HantoPlayerColor color) {
		boolean result = true;

		if(color.equals(HantoPlayerColor.BLUE)) {
			for (Map.Entry<HantoPieceType, Integer> entry : bluePlayerPieceCollection.entrySet()) {
				if(entry.getValue() > 0) {
					result = false;
					break;
				}
			}
		} else {
			for (Map.Entry<HantoPieceType, Integer> entry : redPlayerPieceCollection.entrySet()) {
				if(entry.getValue() > 0) {
					result = false;
					break;
				}
			}
		}
		return result;
	}

	/**
	 * Gets an available piece from the piece set
	 * @param color player who requests a piece
	 * @return piece type able to be played on the board
	 */
	public HantoPieceType getPieceFromSet(HantoPlayerColor color) {
		HantoPieceType piece = null;

		if(color.equals(HantoPlayerColor.BLUE)) {
			for (Map.Entry<HantoPieceType, Integer> entry : bluePlayerPieceCollection.entrySet()) {
				if(entry.getValue() > 0) {
					piece = entry.getKey();
					break;
				}
			}
		} else {
			for (Map.Entry<HantoPieceType, Integer> entry : redPlayerPieceCollection.entrySet()) {
				if(entry.getValue() > 0) {
					piece = entry.getKey();
					break;
				}
			}
		}
		try {
			updatePieceCollection(color, piece);
		} catch (HantoException e) {
			e.printStackTrace();
		}
		return piece;
	}

	/**
	 * Gets a random piece from the piece set
	 * @param color player who requests a piece
	 * @return piece type able to be played on the board
	 */
	public HantoPieceType getRandomPiece(HantoPlayerColor color) {
		HantoPieceType piece = null;
		if(color.equals(HantoPlayerColor.BLUE)) {
			while(piece == null) {
				Random generator = new Random();
				List<HantoPieceType> types = new ArrayList<HantoPieceType>(bluePlayerPieceCollection.keySet());
				HantoPieceType randomKey = types.get(generator.nextInt(types.size()));
				int value = bluePlayerPieceCollection.get(randomKey);
				
				if(value > 0) {
					piece = randomKey;
					break;
				}
			}
		} else {
			while(piece == null) {
				Random generator = new Random();
				List<HantoPieceType> types = new ArrayList<HantoPieceType>(redPlayerPieceCollection.keySet());
				HantoPieceType randomKey = types.get(generator.nextInt(types.size()));
				int value = redPlayerPieceCollection.get(randomKey);
				
				if(value > 0) {
					piece = randomKey;
					break;
				}
			}
		}
		try {
			updatePieceCollection(color, piece);
		} catch (HantoException e) {
			e.printStackTrace();
		}
		return piece;
	}

}
