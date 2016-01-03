package se.liu.ida.eriro331antro937.tddd78.schack;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by eriro331 and antro937 on 2014-03-06.
 */
public class Board
{
    private final int width;
    private final int height;
    private final static int BOARDSIZE = 8;
    private static final int SQUARE_SIZE = 60;
    private final List<BoardListener> listeners;
    private PieceColor turn = PieceColor.WHITE;
    private Piece kingWhite = null;
    private Piece kingBlack = null;

    private Piece[][] board = null;
    private boolean check = false;

    public Board() {
	this.width = BOARDSIZE;
	this.height = BOARDSIZE;
	board = new Piece[BOARDSIZE][BOARDSIZE];
	listeners = new ArrayList<>();
    }


    public Piece getPieceAt(int x, int y) {
	return board[x][y];
    }

    public void setPieceAt(int x, int y, Piece piece) {
	if (piece != null) {
	    piece.setX(x);
	    piece.setY(y);
	}
	board[x][y] = piece;
    }

    public void setCheck(final boolean check) {
	this.check = check;
    }

    public boolean isCheck() {
	return check;
    }

    public boolean hasPieceAt(int x, int y) {
	if (board[x][y] != null) {
	    return true;
	}
	return false;
    }

    public boolean hasPieceAt(int x, int y, PieceType type) {
	if (board[x][y] != null && board[x][y].getType() == type) {
	    return true;
	}
	return false;
    }

    public boolean hasPieceAt(int x, int y, PieceColor color) {
	if (board[x][y] != null && board[x][y].getPieceColor() == color) {
	    return true;
	}
	return false;
    }

    public void addBoardListener(BoardListener bl) {
	listeners.add(bl);
    }

    public void notifyListeners() {

	for (BoardListener listenersList : listeners) {
	    listenersList.boardChanged();

	}
    }

    public void placePiece(int x, int y, PieceColor color, Piece piece) {
	piece.setX(x);
	piece.setY(y);
	piece.setPieceColor(color);
	board[x][y] = piece;
    }

    public void fillBoard() {
	//fills the board with the pieces in starting location

	//fills with pawns
	for (int x = 0; x < 8; x++) {

	    placePiece(x, 1, PieceColor.BLACK, new Pawn());
	    placePiece(x, 6, PieceColor.WHITE, new Pawn());
	}
	//fills with rooks
	for (int x = 0; x < 8; x += 7) {
	    placePiece(x, 0, PieceColor.BLACK, new Rook());
	    placePiece(x, 7, PieceColor.WHITE, new Rook());
	}
	//fills with knights
	for (int x = 1; x < 7; x += 5) {
	    placePiece(x, 0, PieceColor.BLACK, new Knight());
	    placePiece(x, 7, PieceColor.WHITE, new Knight());
	}
	//fills with bishops
	for (int x = 2; x < 7; x += 3) {
	    placePiece(x, 0, PieceColor.BLACK, new Bishop());
	    placePiece(x, 7, PieceColor.WHITE, new Bishop());
	}
	King kingBlack = new King();
	this.kingBlack = kingBlack;
	placePiece(4, 0, PieceColor.BLACK, kingBlack);

	King kingWhite = new King();
	this.kingWhite = kingWhite;
	placePiece(4, 7, PieceColor.WHITE, kingWhite);

	placePiece(3, 0, PieceColor.BLACK, new Queen());
	placePiece(3, 7, PieceColor.WHITE, new Queen());

    }

    public void moveRequest(int oldX, int oldY, int newX, int newY, Board myboard) {
	oldX = (int) Math.floor((float) oldX / SQUARE_SIZE);
	oldY = (int) Math.floor((float) oldY / SQUARE_SIZE);
	newX = (int) Math.floor((float) newX / SQUARE_SIZE);
	newY = (int) Math.floor((float) newY / SQUARE_SIZE);
	if (newX < width && newY < height && oldX < width && oldY < height) {
	    Piece thePiece = getPieceAt(oldX, oldY);
	    if (thePiece != null) {
		thePiece.movePiece(oldX, oldY, newX, newY, myboard);
	    }
	}
    }

    public int getWidth() {
	return width;
    }

    public int getHeight() {
	return height;
    }

    public PieceColor getTurn() {
	return turn;
    }

    public void setTurn(final PieceColor turn) {
	this.turn = turn;
    }
    /*@Override public String toString() {
 		return "Board{" +
    	           	       "board=" + Arrays.deepToString(board) + "\n" +
    	           	       "}\n";
        }*/

    public Piece getKingWhite() {
	return kingWhite;
    }

    public Piece getKingBlack() {
	return kingBlack;
    }

    public void saveGame() {
	try (BufferedWriter writer = new BufferedWriter(new FileWriter("savefile.txt"))) {
	    for (Piece[] item2 : board) {
		for (Piece item : item2) {
		    if (item == null) {
			writer.write("N  " + "_" + "     ");
		    } else {
			writer.write(item.toString()); //Saves the Pieces
			writer.write("_" + item.getPieceColor().toString());
		    }
		    writer.write("\n");
		}
	    }
	    writer.write(turn.toString()); //Saves the turn
	    writer.write("\n");
	    if (check) { //Saves if someone is in check
		writer.write("TRUE");
	    } else {
		writer.write("FALSE");
	    }
	    writer.write("\n");
	    if (kingWhite.isMoved()) { //Saves if the White King has moved
		writer.write("WhiteKingMovedTrue");
	    } else {
		writer.write("WhiteKingMovedFalse");
	    }
	    writer.write("\n");
	    if (kingBlack.isMoved()) { //Saves if the Blsck King has moved
		writer.write("BlackKingMovedTrue");
	    } else {
		writer.write("BlackKingMovedFalse");
	    }

	    writer.close();


	} catch (IOException e) {
	    e.printStackTrace();
	} /*
	    try {
		if (writer != null) writer.close();
	    } catch (IOException e) {
		e.printStackTrace();
	    }
	*/
    }

    public void loadGame() {
	try (BufferedReader reader = new BufferedReader(new FileReader("savefile.txt"))) {
	    loadPieces(reader);

	    String turn = reader.readLine();
	    if (turn.equals("WHITE")) {  //Loads turn
		this.turn = PieceColor.WHITE;
	    } else {
		this.turn = PieceColor.BLACK;
	    }
	    String check = reader.readLine();
	    if (check.equals("TRUE")) {  //Loads check
		this.check = true;
	    } else {
		this.check = false;
	    }

	    String whiteKingMoved = reader.readLine();
	    if (whiteKingMoved.equals("WhiteKingMovedTrue")) { //Loads the white king's move status
		kingWhite.setMoved(true);
	    } else if (whiteKingMoved.equals("WhiteKingMovedFalse")) {
		kingWhite.setMoved(false);
	    }
	    String blackKingMoved = reader.readLine();
	    if (blackKingMoved.equals("BlackKingMovedTrue")) {  //Loads the black king's move status
		kingBlack.setMoved(true);
	    } else if (blackKingMoved.equals("BlackKingMovedFalse")) {
		kingBlack.setMoved(false);
	    }

	    notifyListeners();


	} catch (IOException e) {
	    e.printStackTrace();
	}
    }

    private void loadPieces(final BufferedReader reader) throws IOException {
	for (int column = 0; column < 8; column++) {
	    for (int row = 0; row < 8; row++) {
		String s = reader.readLine();
		String[] parts = s.split("_");
		String piece = parts[0];
		String color = parts[1];
		switch (piece) {
		    case ("P  "): //Piece == Pawn
			if (color.equals("WHITE")) {
			    placePiece(column, row, PieceColor.WHITE, new Pawn());
			} else {
			    placePiece(column, row, PieceColor.BLACK, new Pawn());
			}
			break;

		    case ("R  "):
			if (color.equals("WHITE")) {
			    placePiece(column, row, PieceColor.WHITE, new Rook());
			} else {
			    placePiece(column, row, PieceColor.BLACK, new Rook());
			}
			break;

		    case ("Kn "):
			if (color.equals("WHITE")) {
			    placePiece(column, row, PieceColor.WHITE, new Knight());
			} else {
			    placePiece(column, row, PieceColor.BLACK, new Knight());
			}
			break;

		    case ("B  "):
			if (color.equals("WHITE")) {
			    placePiece(column, row, PieceColor.WHITE, new Bishop());
			} else {
			    placePiece(column, row, PieceColor.BLACK, new Bishop());
			}
			break;

		    case ("Q  "):
			if (color.equals("WHITE")) {
			    placePiece(column, row, PieceColor.WHITE, new Queen());
			} else {
			    placePiece(column, row, PieceColor.BLACK, new Queen());
			}
			break;
		    case ("K  "):
			if (color.equals("WHITE")) {
			    King kingWhite = new King();
			    placePiece(column, row, PieceColor.WHITE, kingWhite);
			    this.kingWhite = kingWhite;
			} else {
			    King kingBlack = new King();
			    placePiece(column, row, PieceColor.BLACK, kingBlack);
			    this.kingBlack = kingBlack;
			}
			break;

		    default:
			board[column][row] = null;
			//no piece at given position, defaults null.
		}


	    }
	}
    }

}
