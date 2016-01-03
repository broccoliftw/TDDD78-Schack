package se.liu.ida.eriro331antro937.tddd78.schack;

/**
 * Created by eriro331 on 2014-03-06.
 */
public class Bishop extends AbstractPiece
{
    public Bishop() {
	setType(PieceType.BISHOP);
    }

    public boolean isMoveLegal(final int oldX, final int oldY, final int newX, final int newY,
			       Board board)// returns true if a move is legal (does not move anything)
    {
	PieceColor color = board.getPieceAt(oldX, oldY).getPieceColor();
	if ((oldY - newY) == 0) return false;

	else if (Math.abs(oldX - newX) == Math.abs(oldY - newY)) {//move is diagonal, new in v2
	    if (board.hasPieceAt(newX, newY)) {//piece at target location
		if (!board.hasPieceAt(newX, newY, color)) {//enemy piece
		    if (!checkCollisionDiagonal(oldX, oldY, newX, newY, board)) {
			return true;
		    }

		}
		return false;
	    }
	    if (!checkCollisionDiagonal(oldX, oldY, newX, newY, board)) {
		return true;
	    }

	}

	return false;
    }

    @Override public String toString() {
	return "B  ";
    }
}
