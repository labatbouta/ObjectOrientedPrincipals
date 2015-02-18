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
 *  This is the Sparrow piece class for the Alpha and Beta Hanto Game
 * @author acclab
 *
 */
public class Sparrow  extends BaseHantoPiece implements HantoPiece{

	/**
	 * This the is the Sparrow constructor
	 * @param color the player color 
	 * @param type the type of piece
	 */
	public Sparrow(HantoPlayerColor color, HantoPieceType type) {
		super(color, type);
	}

	/**
	 * @see hanto.common.HantoPiece#isMoveValidAlpha(hanto.common.HantoCoordinate, hanto.common.HantoCoordinate, int)
	 */
	public MoveResult isMoveValidAlpha(HantoCoordinate from, HantoCoordinate to,
			int moveCount) {
		/* Invalid piece type for Alpha */
		return null;
	}

	/**
	 * @see hanto.common.HantoPiece#isMoveValidBeta(hanto.common.HantoCoordinate, hanto.common.HantoCoordinate, int)
	 */
	@Override
	public MoveResult isMoveValidBeta(HantoCoordinate from, HantoCoordinate to, int moveCount) {

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
