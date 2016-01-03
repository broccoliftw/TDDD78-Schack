package se.liu.ida.eriro331antro937.tddd78.schack;

/**
 * Created by eriro331 on 2014-03-06.
 */
public class King extends AbstractPiece

{
    private static final int BOTTOMROW = 7,TOPROW = 0,LEFTCOLUMN = 0,RIGHTCOLUMN = 7, KINGSTARTPOSX = 4;
    public King() {
	setType(PieceType.KING);
    }

    public boolean isMoveLegal(final int oldX, final int oldY, final int newX, final int newY,
			       Board board)
    { //returns true if a move is legal (does not move anything)
	PieceColor color = board.getPieceAt(oldX, oldY).getPieceColor();

	if ((oldX == newX && Math.abs(oldY - newY) == 1) || (oldY == newY && Math.abs(oldX - newX) == 1)) {
	    //Move is vertical or horisontal and only 1 step
	    if (board.hasPieceAt(newX, newY)) { //There is a targetpiece
		if (!board.hasPieceAt(newX, newY, color)) { //Targetpiece is enemypiece
		    return true;
		} else return false; //Targetpiece is same color
	    } else {
		return true; //No targetpiece
	    }
	} else if ((oldX == KINGSTARTPOSX && oldY == TOPROW)) {//starting position
	    if (!board.hasPieceAt(KINGSTARTPOSX+1, TOPROW) && !board.hasPieceAt(KINGSTARTPOSX+2, TOPROW) &&
		board.hasPieceAt(RIGHTCOLUMN, TOPROW, PieceType.ROOK) && newX == KINGSTARTPOSX+2 && newY == TOPROW) {
		//rook is at right place and nothing between king and rook
		return true;
	    } else if (!board.hasPieceAt(KINGSTARTPOSX-3, TOPROW) && !board.hasPieceAt(KINGSTARTPOSX-2, TOPROW) && !board.hasPieceAt(KINGSTARTPOSX-1, TOPROW) &&
		       board.hasPieceAt(LEFTCOLUMN, TOPROW,PieceType.ROOK) && newX == KINGSTARTPOSX-2 && newY == TOPROW) {
		//rook is at right place and nothing between king and rook
		return true;
	    }
	} else if ((oldX == KINGSTARTPOSX && oldY == BOTTOMROW)) {//starting position
	    if (!board.hasPieceAt(KINGSTARTPOSX+1, BOTTOMROW) && !board.hasPieceAt(KINGSTARTPOSX+2, BOTTOMROW) &&
		board.hasPieceAt(RIGHTCOLUMN, BOTTOMROW,PieceType.ROOK) && newX == KINGSTARTPOSX+2 && newY == BOTTOMROW) {
		//rook is at right place and nothing between king and rook
		return true;
	    } else if (!board.hasPieceAt(KINGSTARTPOSX-3, BOTTOMROW) && !board.hasPieceAt(KINGSTARTPOSX-2, BOTTOMROW) && !board.hasPieceAt(KINGSTARTPOSX-1, BOTTOMROW) &&
		       board.hasPieceAt(LEFTCOLUMN, BOTTOMROW,PieceType.ROOK) && newX == KINGSTARTPOSX-2 && newY == BOTTOMROW) {
		//rook is at right place and nothing between king and rook
		return true;
	    }
	}

	if ((oldY - newY) == 0) return false;
	else if (Math.abs(Math.ceil(oldX - newX) / Math.ceil(oldY - newY)) > 1) {
	    return false;
	} else if (Math.abs((oldX - newX) / (oldY - newY)) == 1 && (Math.abs(oldX - newX) == 1)) {
	    //move is diagonal and only 1 step
	    if (board.hasPieceAt(newX, newY)) {//piece at target location
		if (!board.hasPieceAt(newX, newY, color)) {//enemy piece
		    return true;
		}
		return false;
	    }
	    return true;
	}

	return false;
    }


    @Override public String toString() {
	return "K  ";
    }

}



