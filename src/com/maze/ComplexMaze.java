package com.maze;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;



public class ComplexMaze
{
	public static final String TITLE = "Maze";
	
	public static final int SMALL_SIZE = 10;
	public static final int MEDIUM_SIZE = 25;
	public static final int LARGE_SIZE = 60;
	public static int MAZE_SIZE = SMALL_SIZE;
	public static int CANVAS_WIDTH = Room.ROOM_SIZE * SMALL_SIZE;
	public static int CANVAS_HEIGHT = Room.ROOM_SIZE * SMALL_SIZE;		
	
	public static void main(String[] args) throws InterruptedException {		 
		final MazePanel mazePanel = new MazePanel(MAZE_SIZE);    
        final JFrame frame = new JFrame(TITLE);	
        final JPanel panel = new JPanel();
        panel.setPreferredSize(new Dimension(CANVAS_WIDTH+50, CANVAS_HEIGHT+50));
        
        panel.add(mazePanel);
        mazePanel.addMouseListener(new MouseListener()
		{
			
			@Override
			public void mouseReleased(MouseEvent e)
			{
			
			}
			
			@Override
			public void mousePressed(MouseEvent e)
			{
				Point mouseLoc = mazePanel.getMousePosition();
				mazePanel.toggleSetWall(mouseLoc.x, mouseLoc.y);
				if(mazePanel.editMode)
				{
					mazePanel.toggleWall(mouseLoc.x, mouseLoc.y);		
				}
				else
				{
					if(mouseLoc != null)				
					mazePanel.setPlayerTravel(mouseLoc.x, mouseLoc.y, e);
				}
				
				
			}
			
			@Override
			public void mouseExited(MouseEvent e)
			{
				
			}
			
			@Override
			public void mouseEntered(MouseEvent e)
			{
				
			}
			
			@Override
			public void mouseClicked(MouseEvent e)
			{
				
			}
		});
        mazePanel.addMouseMotionListener(new MouseMotionListener()
		{
			
			@Override
			public void mouseMoved(MouseEvent e)
			{
				
			}
			
			@Override
			public void mouseDragged(MouseEvent e)
			{
				
				Point mouseLoc = mazePanel.getMousePosition();
				if(mazePanel.editMode)
				{
					mazePanel.toggleWall(mouseLoc.x, mouseLoc.y);		
				}
				else
				{
					if(mouseLoc != null)				
					mazePanel.setPlayerTravel(mouseLoc.x, mouseLoc.y, e);
				}
			}
		});
        final JButton restart = new JButton("Restart");
        restart.addActionListener(new ActionListener()
        {
        	  public void actionPerformed(ActionEvent e)
        	  {
        		  mazePanel.regenMaze();	            		  
        	  }
        	});
        
        panel.add(restart);              
        
        final JButton showSolution = new JButton("Solution");
        showSolution.addActionListener(new ActionListener()
        {
        	  public void actionPerformed(ActionEvent e)
        	  {
        		  mazePanel.toggleShowSolution();	            		  
        	  }
        	});
        panel.add(showSolution);
        
        final JRadioButton smallButton = new JRadioButton("Small");                
        smallButton.setSelected(true);

        final JRadioButton mediumButton = new JRadioButton("Medium");
        
        final JRadioButton largeButton = new JRadioButton("Large");
        
        final ButtonGroup group = new ButtonGroup();
        group.add(smallButton);
        group.add(mediumButton);
        group.add(largeButton);   
        
        
        JButton edit = new JButton("Edit");
        edit.addActionListener(new ActionListener()
        {
        	  public void actionPerformed(ActionEvent e)
        	  {
        		  mazePanel.toggleEdit();	
        		  if(mazePanel.editMode)
        		  {
        			  restart.setEnabled(false);
        			  showSolution.setEnabled(false);
        			  smallButton.setEnabled(false);
        			  mediumButton.setEnabled(false);
        			  largeButton.setEnabled(false);
        			  
        		  }
        		  else
        		  {
        			  restart.setEnabled(true);
        			  showSolution.setEnabled(true);
        			  smallButton.setEnabled(true);
        			  mediumButton.setEnabled(true);
        			  largeButton.setEnabled(true);
        		  }
        	  }
        	});
        
        panel.add(edit);                     
        
        //Register a listener for the radio buttons.
        smallButton.addActionListener(new ActionListener()
        {
      	  public void actionPerformed(ActionEvent e)
      	  {
      		  mazePanel.reSizePanel(SMALL_SIZE);	
      		  panel.setPreferredSize(new Dimension(Room.ROOM_SIZE*SMALL_SIZE+50, Room.ROOM_SIZE*SMALL_SIZE+50));
      		  panel.setSize(new Dimension(Room.ROOM_SIZE*SMALL_SIZE+50, Room.ROOM_SIZE*SMALL_SIZE+50));
      		  mazePanel.regenMaze();
      	  }
      	});
        mediumButton.addActionListener(new ActionListener()
        {
      	  public void actionPerformed(ActionEvent e)
      	  {
      		  mazePanel.reSizePanel(MEDIUM_SIZE);
      		  panel.setPreferredSize(new Dimension(Room.ROOM_SIZE*MEDIUM_SIZE+50, Room.ROOM_SIZE*MEDIUM_SIZE+50));
    		  panel.setSize(new Dimension(Room.ROOM_SIZE*MEDIUM_SIZE+50, Room.ROOM_SIZE*MEDIUM_SIZE+50));
    		  mazePanel.regenMaze();            		  
      	  }
      	});
        largeButton.addActionListener(new ActionListener()
        {
      	  public void actionPerformed(ActionEvent e)
      	  {
      		  mazePanel.reSizePanel(LARGE_SIZE);
      		  panel.setPreferredSize(new Dimension(Room.ROOM_SIZE*LARGE_SIZE+50, Room.ROOM_SIZE*LARGE_SIZE+50));
    		  panel.setSize(new Dimension(Room.ROOM_SIZE*LARGE_SIZE+50, Room.ROOM_SIZE*LARGE_SIZE+50));
    		  mazePanel.regenMaze();	            		  
      	  }
      	});
        panel.add(smallButton);
        panel.add(mediumButton);  
        panel.add(largeButton);  
        JScrollPane scrollPane=new JScrollPane(panel);
        scrollPane.setPreferredSize(new Dimension(CANVAS_WIDTH+100, CANVAS_HEIGHT+100));	 
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        frame.add(scrollPane);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();             // "this" JFrame packs its components
        frame.setLocationRelativeTo(null); // center the application window	            
        frame.setVisible(true);            // show it	   
        while (true) {            	
            mazePanel.repaint();
            // modify this to do 30 fps
            Thread.sleep(50);
        }		
     }
	private void addMousePanelListeners(MazePanel mazePanel)
	{
		
	}
}
