package se.liu.ida.eriro331antro937.tddd78.schack;

/**
 * Created by eriro331 on 2014-03-06.
 */
public interface Piece
{
    public void setMoved(boolean moved);

    public boolean isMoved();
    public void movePiece(final int oldX, final int oldY, final int newX, final int newY, Board board);

    public boolean isMoveLegal(int oldX, int oldY, int newX, int newY, Board board);

    public void setPieceColor(final PieceColor pieceColor);

    public boolean checkForStalemate(PieceColor color, Board board);

    public PieceColor getPieceColor();

    public boolean testMove(final int oldX, final int oldY, final int newX, final int newY, Board board);

    public boolean testCastling(final int oldX, final int oldY, final int newX, final int newY,final Board board, final PieceColor color,
        			       final Piece pieceAtStartPos);


    public int getX();

    public int getY();

    public PieceType getType();

    public void setX(final int x);

    public void setY(final int y);

}
