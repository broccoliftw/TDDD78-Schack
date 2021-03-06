package se.liu.ida.eriro331antro937.tddd78.schack;


import javax.swing.*;


/**
 * Created by eriro331 on 2014-03-06.
 */
public abstract class AbstractPiece implements Piece // Would be more Complex to split the class (also less abstract)
{
    private int x;
    private int y;
    private PieceColor pieceColor = null;
    private PieceType type = null;
    private boolean moved = false;
    private static final int BOTTOMROW = 7, TOPROW = 0, LEFTCOLUMN = 0, RIGHTCOLUMN = 7, KINGSTARTPOSX = 4;

    public int getX() {
	return x;
    }

    public int getY() {
	return y;
    }

    public PieceType getType() {
	return type;
    }

    public PieceColor getPieceColor() {
	return pieceColor;
    }

    public void setType(final PieceType type) {
	this.type = type;
    }

    public void setPieceColor(final PieceColor pieceColor) {
	this.pieceColor = pieceColor;
    }

    public void setX(final int x) {
	this.x = x;
    }

    public void setY(final int y) {
	this.y = y;
    }

    public void setMoved(final boolean moved) {
	this.moved = moved;
    }

    public boolean isMoved() {
	return moved;
    }

    public boolean checkForStalemate(PieceColor color,
				     Board board)
    {// Checks if a player is unable to move any piece while not in check,

	for (int x = 0; x < board.getWidth(); x++) {
	    for (int y = 0; y < board.getHeight(); y++) {
		Piece thepiece = board.getPieceAt(x, y);//finds all remaining pieces
		if (thepiece != null && thepiece.getPieceColor() == color) {// if piece is of right color
		    for (int i = 0; i < board.getWidth(); i++) {
			for (int j = 0; j < board.getWidth(); j++) {
			    if (thepiece.testMove(x, y, i, j, board) &&
				thepiece.isMoveLegal(x, y, i, j, board)) {//piece is able to move
				return false;
			    }
			}
		    }
		}
	    }
	}
	return true;//no moves were able to be done
    }

    public boolean testMove(final int oldX, final int oldY, final int newX, final int newY,
			    Board board)
    {//test possible moves (for Highlighting, no moves are actually done in this method)
	if (board.hasPieceAt(oldX, oldY)) {
	    PieceColor color = board.getPieceAt(oldX, oldY).getPieceColor();
	    Piece pieceAtStartPos = board.getPieceAt(oldX, oldY);// saves the piece at start position
	    Piece pieceAtEndPos = board.getPieceAt(newX, newY);// saves the pieces at end position


	    board.setPieceAt(newX, newY, (board.getPieceAt(oldX, oldY)));// does the move
	    board.setPieceAt(oldX, oldY, null);

	    if (checkForCheck(color, board)) {// analyse if the done move results in check
		board.setPieceAt(oldX, oldY, pieceAtStartPos);
		board.setPieceAt(newX, newY, pieceAtEndPos);
		return false;
	    }

	    board.setPieceAt(oldX, oldY, pieceAtStartPos);
	    board.setPieceAt(newX, newY, pieceAtEndPos);// resets the pieces

	    return true;
	}
	return false;
    }

    public void movePiece(final int oldX, final int oldY, final int newX, final int newY,
			  Board board)
    {//Checks if move is legal and moves the piece if it is

	PieceColor turn = board.getTurn();
	PieceColor color = board.getPieceAt(oldX, oldY).getPieceColor();
	if (board.getPieceAt(oldX, oldY).isMoveLegal(oldX, oldY, newX, newY, board) && color == turn) {
	    Piece pieceAtStartPos = board.getPieceAt(oldX, oldY);
	    Piece pieceAtEndPos = board.getPieceAt(newX, newY);
	    board.setPieceAt(newX, newY, (board.getPieceAt(oldX, oldY)));//do the move
	    board.setPieceAt(oldX, oldY, null);

	    if (board.hasPieceAt(newX, newY, PieceType.KING)) {
		castling(oldX, oldY, newX, newY, board, color, pieceAtStartPos);
	    }


	    /*if (!isCastling(oldX, oldY, newX, board, color,
			    pieceAtStartPos)) {//if the move is not a castling move(Special move)


		if (checkForCheck(color, board)) { //Move is not legal, reset piece
		    if (pieceAtStartPos.getType() == PieceType.KING && oldX == 4 && newX == 6) {//rokad right
			if (color == PieceColor.BLACK) {
			    board.setPieceAt(7, 0, board.getPieceAt(5, 0));
			    board.setPieceAt(5, 0, null);
			} else {
			    board.setPieceAt(7, 7, board.getPieceAt(5, 7));
			    board.setPieceAt(5, 7, null);
			}
		    } else if (pieceAtStartPos.getType() == PieceType.KING && oldX == 4 && newX == 2) {//rokad left
			if (color == PieceColor.BLACK) {
			    board.setPieceAt(0, 0, board.getPieceAt(3, 0));
			    board.setPieceAt(3, 0, null);
			} else {
			    board.setPieceAt(0, 7, board.getPieceAt(3, 7));
			    board.setPieceAt(3, 7, null);
			}
		    }

		    board.setPieceAt(oldX, oldY, pieceAtStartPos);
		    board.setPieceAt(newX, newY, pieceAtEndPos);

		} //else { //Move OK
		// nextTurnChecks(newX, newY, board, color);//checks check, stalemate, checkmate
		//}

	    }*/
	    board.setCheck(false);
	    if (!checkForCheck(color, board)) {

		nextTurnChecks(newX, newY, board, color);//checks check, stalemate, checkmate
	    } else {
		board.setPieceAt(oldX, oldY, pieceAtStartPos);
		board.setPieceAt(newX, newY, pieceAtEndPos);
	    }
	}

	pawnAtEndRow(newX, newY, board, color); // checks if a pawn was moved to the last row
    }

    private void pawnAtEndRow(final int newX, final int newY, final Board board, final PieceColor color) {
	if (board.hasPieceAt(newX, newY) && board.hasPieceAt(newX, newY, PieceType.PAWN) &&
	    (newY == TOPROW || newY == BOTTOMROW)) { // Pawn was moved to last row, open popup with transformations
	    JList<String> list = new JList<>(new String[] { "Queen", "Bishop", "Rook", "Knight" });
	    JOptionPane.showMessageDialog(null, list, "Choose!", JOptionPane.PLAIN_MESSAGE);

	    if (list.getSelectedIndices()[0] == 0) {
		board.placePiece(newX, newY, color, new Queen());
	    } else if (list.getSelectedIndices()[0] == 1) {
		board.placePiece(newX, newY, color, new Bishop());
	    } else if (list.getSelectedIndices()[0] == 2) {
		board.placePiece(newX, newY, color, new Rook());
	    } else if (list.getSelectedIndices()[0] == 3) {
		board.placePiece(newX, newY, color, new Knight());

	    }

	}
    }

    private void nextTurnChecks(final int newX, final int newY, final Board board, final PieceColor color) {
	if (board.hasPieceAt(newX, newY)) {
	    board.getPieceAt(newX, newY).setMoved(true);//Disables special move castling
	    this.x = newX;
	    this.y = newY;
	    board.notifyListeners();
	    if (color == PieceColor.BLACK) {// change turn
		board.setTurn(PieceColor.WHITE);

		if (checkForCheck(PieceColor.WHITE, board)) {
		    if (checkForCheckMate(PieceColor.WHITE, board)) {//game over
			String winner = "Black player";
			JOptionPane.showMessageDialog(null, winner + " wins", "CheckMate", JOptionPane.INFORMATION_MESSAGE);
			System.exit(0);

		    }
		    board.setCheck(true);
		    board.notifyListeners();

		    //ChessComponent.setCheck(true);//draws highlight on king
		}

	    } else {
		board.setTurn(PieceColor.BLACK);

		if (checkForCheck(PieceColor.BLACK, board)) {
		    if (checkForCheckMate(PieceColor.BLACK, board)) {//game over
			String winner = "White player";
			JOptionPane.showMessageDialog(null, winner + " wins", "checkmate", JOptionPane.INFORMATION_MESSAGE);
			System.exit(0);
		    }
		    //ljudfil för schack
		    board.setCheck(true);
		    board.notifyListeners();

		    //ChessComponent.setCheck(true);//draws highlight on king
		}
		if (board.getPieceAt(newX, newY).checkForStalemate(PieceColor.BLACK, board)) {//game over draw
		    JOptionPane.showMessageDialog(null, "Black player unable to move, draw", "Draw",
						  JOptionPane.INFORMATION_MESSAGE);
		    System.exit(0);
		}
	    }
	}
    }


    public void castling(final int oldX, final int oldY, final int newX, final int newY, final Board board,
			 final PieceColor color, final Piece pieceAtStartPos) //Special move, Castling
    {

	castlingRight(oldX, oldY, newX, newY, board, color, pieceAtStartPos);
	castlingLeft(oldX, oldY, newX, newY, board, color, pieceAtStartPos);
    }

    private void castlingLeft(final int oldX, final int oldY, final int newX, final int newY, final Board board,
			      final PieceColor color, final Piece pieceAtStartPos)
    {
	if (pieceAtStartPos.getType() == PieceType.KING && newX == 2 && (newY == TOPROW || newY == BOTTOMROW)) {//Castling left
	    board.setPieceAt(oldX, oldY, (board.getPieceAt(newX, newY)));
	    if (!pieceAtStartPos.isMoved()) {
		if (color == PieceColor.BLACK) {
		    board.setPieceAt(KINGSTARTPOSX - 1, TOPROW, (board.getPieceAt(oldX, oldY))); //Moves king 1 step
		    if (checkForCheck(PieceColor.BLACK, board)) {
			board.setPieceAt(oldX, oldY, (board.getPieceAt(KINGSTARTPOSX - 1, TOPROW))); //Moves king back
			board.setPieceAt(KINGSTARTPOSX - 1, TOPROW, null);
			board.setPieceAt(KINGSTARTPOSX - 2, TOPROW, null);
		    } else {
			board.setPieceAt(KINGSTARTPOSX - 2, TOPROW,
					 (board.getPieceAt(KINGSTARTPOSX - 1, TOPROW))); //Moves king 1 step
			if (checkForCheck(PieceColor.BLACK, board)) {
			    board.setPieceAt(oldX, oldY, (board.getPieceAt(KINGSTARTPOSX - 2, TOPROW))); //Moves king back
			    board.setPieceAt(KINGSTARTPOSX - 1, TOPROW, null);
			    board.setPieceAt(KINGSTARTPOSX - 2, TOPROW, null);

			} else {//Castling OK
			    board.setPieceAt(3, TOPROW, (board.getPieceAt(LEFTCOLUMN, TOPROW))); //Moves tower
			    board.setPieceAt(LEFTCOLUMN, TOPROW, null);
			    board.setPieceAt(oldX, oldY, null);
			}


		    }
		}

		if (color == PieceColor.WHITE) {
		    board.setPieceAt(KINGSTARTPOSX - 1, BOTTOMROW, (board.getPieceAt(oldX, oldY))); //Moves king 1 step
		    if (checkForCheck(PieceColor.WHITE, board)) {
			board.setPieceAt(oldX, oldY, (board.getPieceAt(KINGSTARTPOSX - 1, BOTTOMROW))); //Moves king back
			board.setPieceAt(KINGSTARTPOSX - 2, BOTTOMROW, null);
			board.setPieceAt(KINGSTARTPOSX - 1, BOTTOMROW, null);

		    } else {
			board.setPieceAt(KINGSTARTPOSX - 2, BOTTOMROW,
					 (board.getPieceAt(KINGSTARTPOSX - 1, BOTTOMROW))); //Moves king 1 step
			if (checkForCheck(PieceColor.WHITE, board)) {
			    board.setPieceAt(oldX, oldY, (board.getPieceAt(KINGSTARTPOSX - 2, BOTTOMROW))); //Moves king back
			    board.setPieceAt(KINGSTARTPOSX - 2, BOTTOMROW, null);
			    board.setPieceAt(KINGSTARTPOSX - 1, BOTTOMROW, null);

			} else { //Castling OK
			    board.setPieceAt(3, BOTTOMROW, (board.getPieceAt(LEFTCOLUMN, BOTTOMROW))); //Moves tower
			    board.setPieceAt(LEFTCOLUMN, BOTTOMROW, null);
			    board.setPieceAt(oldX, oldY, null);
			}


		    }
		}
	    } else {
		board.setPieceAt(newX, newY, null);
	    }

	}
    }

    private void castlingRight(final int oldX, final int oldY, final int newX, final int newY, final Board board,
			       final PieceColor color, final Piece pieceAtStartPos)
    {
	if (pieceAtStartPos.getType() == PieceType.KING && newX == 6 && (newY == TOPROW || newY == BOTTOMROW)) {//Castling right
	    board.setPieceAt(oldX, oldY, (board.getPieceAt(newX, newY)));
	    if (!pieceAtStartPos.isMoved()) {
		if (color == PieceColor.BLACK) {
		    board.setPieceAt(KINGSTARTPOSX + 1, TOPROW, (board.getPieceAt(oldX, oldY))); //Moves king 1 step
		    if (checkForCheck(PieceColor.BLACK, board)) {
			board.setPieceAt(oldX, oldY, (board.getPieceAt(KINGSTARTPOSX + 1, TOPROW))); //Moves king back
			board.setPieceAt(KINGSTARTPOSX + 1, TOPROW, null);
			board.setPieceAt(KINGSTARTPOSX + 2, TOPROW, null);
		    } else {
			board.setPieceAt(KINGSTARTPOSX + 2, TOPROW,
					 (board.getPieceAt(KINGSTARTPOSX + 1, TOPROW))); //Moves king 1 step
			if (checkForCheck(PieceColor.BLACK, board)) {
			    board.setPieceAt(oldX, oldY, (board.getPieceAt(KINGSTARTPOSX + 2, TOPROW))); //Moves king back
			    board.setPieceAt(KINGSTARTPOSX + 1, TOPROW, null);
			    board.setPieceAt(KINGSTARTPOSX + 2, TOPROW, null);

			} else { //Castling OK
			    board.setPieceAt(5, TOPROW, (board.getPieceAt(RIGHTCOLUMN, TOPROW))); //Moves tower
			    board.setPieceAt(RIGHTCOLUMN, TOPROW, null);
			    board.setPieceAt(oldX, oldY, null);
			}

		    }
		}

		if (color == PieceColor.WHITE) {
		    board.setPieceAt(5, 7, (board.getPieceAt(oldX, oldY))); //Moves king 1 step

		    if (checkForCheck(PieceColor.WHITE, board)) {
			board.setPieceAt(oldX, oldY, (board.getPieceAt(KINGSTARTPOSX + 1, BOTTOMROW))); //Moves king back
			board.setPieceAt(KINGSTARTPOSX + 1, BOTTOMROW, null);
			board.setPieceAt(KINGSTARTPOSX + 2, BOTTOMROW, null);
		    } else {
			board.setPieceAt(KINGSTARTPOSX + 2, BOTTOMROW,
					 (board.getPieceAt(KINGSTARTPOSX + 1, BOTTOMROW))); //Moves king 1 step
			if (checkForCheck(PieceColor.WHITE, board)) {
			    board.setPieceAt(oldX, oldY, (board.getPieceAt(KINGSTARTPOSX + 2, BOTTOMROW))); //Moves king back
			    board.setPieceAt(KINGSTARTPOSX + 2, BOTTOMROW, null);
			    board.setPieceAt(KINGSTARTPOSX + 1, BOTTOMROW, null);
			} else { //Castling OK
			    board.setPieceAt(5, BOTTOMROW, (board.getPieceAt(RIGHTCOLUMN, BOTTOMROW))); //Moves tower
			    board.setPieceAt(RIGHTCOLUMN, BOTTOMROW, null);
			    board.setPieceAt(oldX, oldY, null);
			}

		    }
		}
	    } else {
		board.setPieceAt(newX, newY, null);
	    }

	}
    }

    public boolean testCastling(final int oldX, final int oldY, final int newX, final int newY, final Board board,
				final PieceColor color,
				final Piece pieceAtStartPos) // Made for highlighting the correct squares, does not move anything
    {
	boolean check = true;

	if (pieceAtStartPos.getType() == PieceType.KING && newX == 6 && (newY == TOPROW || newY == BOTTOMROW)) {//Castling right
	    if (pieceAtStartPos.isMoved()) {
		return false;
	    }
	    if (color == PieceColor.BLACK) {
		board.setPieceAt(KINGSTARTPOSX + 1, TOPROW, (board.getPieceAt(oldX, oldY)));
		if (checkForCheck(PieceColor.BLACK, board)) {
		    board.setPieceAt(oldX, oldY, (board.getPieceAt(KINGSTARTPOSX + 1, TOPROW)));
		    check = false;

		}
		board.setPieceAt(KINGSTARTPOSX + 1, TOPROW, null);

	    } else {
		board.setPieceAt(KINGSTARTPOSX + 1, BOTTOMROW, (board.getPieceAt(oldX, oldY)));
		if (checkForCheck(PieceColor.WHITE, board)) {
		    board.setPieceAt(oldX, oldY, (board.getPieceAt(KINGSTARTPOSX + 1, BOTTOMROW)));
		    check = false;
		}
		board.setPieceAt(KINGSTARTPOSX + 1, BOTTOMROW, null);

	    }
	} else if (pieceAtStartPos.getType() == PieceType.KING && newX == 2 &&
		   (newY == TOPROW || newY == BOTTOMROW)) {//Castling left
	    if (pieceAtStartPos.isMoved()) {
		return false;
	    }
	    if (color == PieceColor.BLACK) {
		board.setPieceAt(KINGSTARTPOSX - 1, TOPROW, (board.getPieceAt(oldX, oldY)));
		if (checkForCheck(PieceColor.BLACK, board)) {
		    board.setPieceAt(oldX, oldY, (board.getPieceAt(KINGSTARTPOSX - 1, TOPROW)));
		    check = false;
		}
		board.setPieceAt(KINGSTARTPOSX - 1, TOPROW, null);
	    } else {
		board.setPieceAt(KINGSTARTPOSX - 1, BOTTOMROW, (board.getPieceAt(oldX, oldY)));
		if (checkForCheck(PieceColor.WHITE, board)) {
		    board.setPieceAt(oldX, oldY, (board.getPieceAt(KINGSTARTPOSX - 1, BOTTOMROW)));
		    check = false;
		}
		board.setPieceAt(KINGSTARTPOSX - 1, BOTTOMROW, null);
	    }
	}
	return check;
    }

    public boolean checkForCheck(PieceColor colorOfKing, Board board) {
	int kingX;
	int kingY;
	if (colorOfKing == PieceColor.WHITE) {//finds the king with the kingpointer
	    kingX = board.getKingWhite().getX();
	    kingY = board.getKingWhite().getY();
	} else {
	    kingX = board.getKingBlack().getX();
	    kingY = board.getKingBlack().getY();
	}


	for (int x = 0; x < board.getWidth(); x++) {
	    for (int y = 0; y < board.getHeight(); y++) {
		if (board.hasPieceAt(x, y) &&
		    !board.hasPieceAt(x, y, colorOfKing)) {//if piece existst and is an enemy of the king
		    if (board.getPieceAt(x, y).isMoveLegal(x, y, kingX, kingY, board)) {//if piece is able to take king
			return true;
		    }
		}
	    }
	}

	//no piece was able to take the king with their next move
	return false;
    }

    public boolean checkForCheckMate(PieceColor color, Board board) {//checks if king is in check and not able to move
	for (int x = 0; x < board.getWidth(); x++) {
	    for (int y = 0; y < board.getHeight(); y++) {
		Piece thepiece = board.getPieceAt(x, y);
		if (thepiece != null && thepiece.getPieceColor() == color) {
		    for (int i = 0; i < board.getWidth(); i++) {
			for (int j = 0; j < board.getWidth(); j++) {
			    if (thepiece.testMove(x, y, i, j, board) && thepiece.isMoveLegal(x, y, i, j, board)) {
				return false;
			    }

			}

		    }
		}

	    }

	}
	return true;
    }


    public boolean checkCollision(final int oldX, final int oldY, final int newX, final int newY,
				  Board board)
    {// returns true if a straight move makes a piece collide with another (of it's own color)
	int leftRight = newX - oldX;
	int upDown = newY - oldY;
	if (leftRight > 0) {//right
	    for (int i = oldX + 1; i < newX; i++) {
		if (board.hasPieceAt(i, oldY)) {
		    return true;//collision
		}
	    }
	    return false;
	} else if (leftRight < 0) {//left
	    for (int i = oldX - 1; i > newX; i--) {
		if (board.hasPieceAt(i, oldY)) {
		    return true;//collision
		}
	    }
	    return false;

	} else if (upDown > 0) {//down
	    for (int i = oldY + 1; i < newY; i++) {
		if (board.hasPieceAt(oldX, i)) {
		    return true;//collision
		}
	    }
	    return false;
	} else if (upDown < 0) {//up
	    for (int i = oldY - 1; i > newY; i--) {
		if (board.hasPieceAt(oldX, i)) {
		    return true;//collision
		}
	    }
	    return false;

	}
	return false;
    }

    public boolean checkCollisionDiagonal(final int oldX, final int oldY, final int newX, final int newY, Board board) {
	// returns true if a diagonal move makes a piece collide with another (of it's own color)
	int kx = newX - oldX;
	int ky = newY - oldY;
	if (ky > 0 && kx > 0) {// right down
	    for (int i = 1; i + oldY < newY; i++) {
		if (board.hasPieceAt(oldX + i, oldY + i)) {
		    return true;//collision
		}
	    }
	    return false;
	} else if (ky > 0 && kx < 0) {//left down
	    for (int i = 1; i + oldY < newY; i++) {
		if (board.hasPieceAt(oldX - i, oldY + i)) {
		    return true;//collision
		}
	    }
	    return false;
	} else if (ky < 0 && kx < 0) {//left up
	    for (int i = 1; oldY - i > newY; i++) {
		if (board.hasPieceAt(oldX - i, oldY - i)) {
		    return true;//collision
		}
	    }
	    return false;
	} else if (ky < 0 && kx > 0) {//right up
	    for (int i = 1; oldY - i > newY; i++) {
		if (board.hasPieceAt(oldX + i, oldY - i)) {
		    return true;//collision
		}
	    }
	    return false;
	}
	return false;
    }


}
