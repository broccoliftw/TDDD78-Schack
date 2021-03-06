package se.liu.ida.eriro331antro937.tddd78.schack;

/**
 * Created by eriro331 on 2014-03-06.
 */
public class Knight extends AbstractPiece
{
    public Knight() {
	setType(PieceType.KNIGHT);
    }


    public boolean isMoveLegal(final int oldX, final int oldY, final int newX, final int newY,
			       Board board)//returns true if a move is legal
    // (does not move anything, ignores collisions between moving squares)
    {
	PieceColor color = board.getPieceAt(oldX, oldY).getPieceColor();
	if ((Math.abs(oldX - newX) == 1 && Math.abs(oldY - newY) == 2) ||
	    (Math.abs(oldX - newX) == 2 && Math.abs(oldY - newY) == 1)) {
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
	return "Kn ";
    }
}
