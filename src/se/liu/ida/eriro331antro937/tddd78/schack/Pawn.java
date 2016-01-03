package se.liu.ida.eriro331antro937.tddd78.schack;


/**
 * Created by eriro331 on 2014-03-06.
 */
public class Pawn extends AbstractPiece
{
    public Pawn() {
	setType(PieceType.PAWN);
    }

    public boolean isMoveLegal(final int oldX, final int oldY, final int newX, final int newY,
			       Board board)//returns true if a move is legal (does not move anything)
    //method split into two parts due to the fact that opposing pawns starts at diffrent rows
    {
	PieceColor color = board.getPieceAt(oldX, oldY).getPieceColor();
	if (color == PieceColor.BLACK) {
	    if (((oldX - newX) == 1 || (oldX - newX) == -1) && (newY - oldY == 1)) { //Pawn tries to take another Piece
		if ((board.hasPieceAt(newX, newY)) &&
		    (!board.hasPieceAt(newX, newY, color))) { //Targetpiece is enemypiece
		    return true;
		}
		return false;
	    } else if (oldX == newX) { //Walks forward
		if (!board.getPieceAt(oldX,oldY).isMoved()) { //Pawn stands at startposition
		    if (newY - oldY > 0 && newY - oldY <= 2) {//Able to move forward
			if (!checkCollision(oldX, oldY, newX, newY + 1, board)) {
			    return true;
			}
		    }
		    return false;

		} else { //Not startposition
		    if (newY - oldY == 1) {//Able to move forward
			if (!checkCollision(oldX, oldY, newX, newY + 1, board)) {
			    return true;
			}
		    }
		}
	    }
	    return false;
	} else if (color == PieceColor.WHITE) {
	    if (((oldX - newX) == 1 || (oldX - newX) == -1) && (newY - oldY == -1)) { //Pawn tries to take another Piece
		if ((board.hasPieceAt(newX, newY)) &&
		    (!board.hasPieceAt(newX, newY, color))) { //Targetpiece is enemypiece
		    return true;
		}
		return false;
	    } else if (oldX == newX) { //Walks forward
		if (!board.getPieceAt(oldX,oldY).isMoved()) { //Pawn stands at startposition
		    if (newY - oldY < 0 && newY - oldY >= -2) {//Able to move forward
			if (!checkCollision(oldX, oldY, newX, newY - 1, board)) {
			    return true;
			}
		    }
		    return false;
		} else { //Not startposition
		    if (newY - oldY == -1) {//Able to move forward
			if (!checkCollision(oldX, oldY, newX, newY - 1, board)) {
			    return true;
			}
		    }
		}
	    }
	    return false;
	}
	return false;
    }

    @Override public String toString() {
	return "P  ";
    }

}
