package se.liu.ida.eriro331antro937.tddd78.schack;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * Created by Wilhelm on 2014-03-11.
 */
public class ChessFrame extends JFrame implements MouseListener
{
    private JFrame frame;
    private int xCoordinate;
    private int yCoordinate;
    private int newX, newY;
    private int clickCount = 0;
    private Board board;
    private ChessComponent comp;

    ChessFrame(Board board) {// constructs frame and add listeners
	super("Chess");
	final JFrame frame = new JFrame("Chess!");
	this.frame = frame;
	createMenus();
	comp = new ChessComponent(board);
	board.addBoardListener(comp);
	comp.addMouseListener(this);
	frame.add(comp, BorderLayout.CENTER);
	frame.pack();
	frame.setVisible(true);
	this.board = board;
    }


    private void createMenus() {// creates a simple Exit menu
	final JMenu file = new JMenu("File");
	file.addSeparator();
	JMenuItem exit = file.add(new JMenuItem("Exit"));
	exit.addActionListener(new ActionListener()
	{
	    @Override public void actionPerformed(final ActionEvent e) {
		int option = JOptionPane.showConfirmDialog(null, "Exit?", "Choose", JOptionPane.YES_NO_OPTION);
		if (option == JOptionPane.YES_OPTION) System.exit(0);
	    }
	});
	JMenuItem save = file.add(new JMenuItem("Save"));
		save.addActionListener(new ActionListener()
		{
		    @Override public void actionPerformed(final ActionEvent e) {
			board.saveGame();
		    }
		});
	JMenuItem load = file.add(new JMenuItem("Load"));
		load.addActionListener(new ActionListener()
		{
		    @Override public void actionPerformed(final ActionEvent e) {
			int option = JOptionPane.showConfirmDialog(null, "Load?", "Choose", JOptionPane.YES_NO_OPTION);
			if (option == JOptionPane.YES_OPTION) board.loadGame();
		    }
		});


	file.add(save);
	file.add(load);
	file.add(exit);
	final JMenuBar jMenuBar = new JMenuBar();
	jMenuBar.add(file);
	frame.setJMenuBar(jMenuBar);
    }

    @Override public void mouseClicked(final MouseEvent e) {// if the user clicks with the mouse his method is ran.
	if (SwingUtilities.isLeftMouseButton(e) && clickCount == 0) {// User selects a piece
	    xCoordinate = e.getX();
	    yCoordinate = e.getY();
	    clickCount++;
	    comp.highlight(xCoordinate, yCoordinate);//highlight possible moves

	  
	}
	else if (SwingUtilities.isRightMouseButton(e)){// the user wants to deselect his current target
	    clickCount =0;
	    comp.highlight(-1,-1);// sending in negative coordinates will remove highlighting
	}
	else if (SwingUtilities.isLeftMouseButton(e) && clickCount == 1) {// user wants to move the target piece to another location
	    newX = e.getX();
	    newY = e.getY();
	    clickCount = 0;
	    board.moveRequest(xCoordinate,yCoordinate,newX,newY,board);// requests to move
	    comp.highlight(-1,-1);// sending in negative coordinates will remove highlighting
	}
    }

    @Override public void mousePressed(final MouseEvent e) {

    }

    @Override public void mouseReleased(final MouseEvent e) {

    }

    @Override public void mouseEntered(final MouseEvent e) {

    }

    @Override public void mouseExited(final MouseEvent e) {

    }

}
