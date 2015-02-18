/*******************************************************************************
 * This files was developed for CS4233: Object-Oriented Analysis & Design.
 * The course was taken at Worcester Polytechnic Institute.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/

package hanto.studentacclab.tournament;

import hanto.common.HantoException;
import hanto.common.HantoGameID;
import hanto.common.HantoPlayerColor;
import hanto.studentacclab.common.Coordinate;
import hanto.studentacclab.common.HantoPieceFactory;
import hanto.studentacclab.epsilon.EpsilonHantoGame;
import hanto.tournament.HantoGamePlayer;
import hanto.tournament.HantoMoveRecord;

/**
 * HantoPlayer for Epsilon
 * @author acclab
 *
 */
public class HantoPlayer implements HantoGamePlayer {

	protected HantoPlayerColor myColor;
	protected boolean doIMoveFirst;
	protected EpsilonHantoGame game;
	protected HantoPlayerColor opponentColor;

	/**
	 * Empty constructor, initialized by tournament runner
	 */
	public HantoPlayer () {
	}

	@Override
	public void startGame(HantoGameID version, HantoPlayerColor myColor,
			boolean doIMoveFirst) {

		this.myColor = myColor;
		this.doIMoveFirst = doIMoveFirst;

		if(myColor == HantoPlayerColor.BLUE) {
			opponentColor = HantoPlayerColor.RED;
		} else {
			opponentColor = HantoPlayerColor.BLUE;
		}

		if(doIMoveFirst) {
			game = new EpsilonHantoGame(myColor);
		} else {
			if(myColor == HantoPlayerColor.BLUE) {
				game = new EpsilonHantoGame(HantoPlayerColor.RED);
			} else {
				game = new EpsilonHantoGame(HantoPlayerColor.BLUE);
			}
		}
	}

	@Override
	public HantoMoveRecord makeMove(HantoMoveRecord opponentsMove) {

		try {
			if(opponentsMove != null) {
				game.makeMove(opponentsMove.getPiece(), opponentsMove.getFrom(), opponentsMove.getTo());
			}	
		} catch (HantoException e) {
			e.printStackTrace();
		}

		HantoMoveRecord record = game.doesMoveExistForPlayer(myColor);

		if(record != null) {
			HantoPieceFactory
					.getInstance();
			game.setLocation(new Coordinate(record.getTo()), HantoPieceFactory.makeHantoPiece(myColor, record.getPiece()));
			if(record.getFrom() != null) {
				game.getBoard().remove(record.getFrom());
			}
		} else {
			record = new HantoMoveRecord(null, null, null);
		}
		return record;
	}

}
