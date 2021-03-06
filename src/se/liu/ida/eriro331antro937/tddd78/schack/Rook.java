package se.liu.ida.eriro331antro937.tddd78.schack;

/**
 * Created by eriro331 on 2014-03-06.
 */
public class Rook extends AbstractPiece
{
    public Rook() {
	setType(PieceType.ROOK);
    }

    public boolean isMoveLegal(final int oldX, final int oldY, final int newX, final int newY,
			       Board board)//returns true if a move is legal (does not move anything)
    {
	PieceColor color = board.getPieceAt(oldX, oldY).getPieceColor();

	if ((oldX == newX && oldY != newY) || (oldY == newY && oldX != newX)) {
	    if (board.hasPieceAt(newX,newY)) { //There is a targetpiece
		if (!board.hasPieceAt(newX, newY, color)) { //Targetpiece is enemypiece
		    if (!checkCollision(oldX, oldY, newX, newY, board)) {
			return true;
		    }
		}
	    } else {
		if (!checkCollision(oldX, oldY, newX, newY, board)) {
		    return true;
		}
	    }
	}

	return false;
    }

    @Override public String toString() {
	return "R  ";
    }
}
