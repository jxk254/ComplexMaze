package com.maze;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

public class MazePanel extends JPanel
{		 
	 public int CANVAS_WIDTH = 100;
	 public int CANVAS_HEIGHT = 100;	 
	 public int buffer = 25;
	 public int MAZE_SIZE = 10;
	 public boolean showSolution = false;
	 public boolean editMode = false;
	 public boolean setWall = false;
	 
	 
	 public static Maze maze;
	 
	 public MazePanel(int MAZE_SIZE) {	
		 this.MAZE_SIZE = MAZE_SIZE;
		  regenMaze();
		  
	      CANVAS_WIDTH = Room.ROOM_SIZE * MAZE_SIZE;
	 	  CANVAS_HEIGHT = Room.ROOM_SIZE * MAZE_SIZE;	 	 
	 	  setPreferredSize(new Dimension(CANVAS_WIDTH+buffer, CANVAS_HEIGHT+buffer));
	 }
	 
	 public void reSizePanel(int newSize)
	 {
		 this.MAZE_SIZE = newSize;
		 CANVAS_WIDTH = Room.ROOM_SIZE * MAZE_SIZE;
	 	 CANVAS_HEIGHT = Room.ROOM_SIZE * MAZE_SIZE;	
	 	 setPreferredSize(new Dimension(CANVAS_WIDTH+buffer, CANVAS_HEIGHT+buffer));
	 	 setSize(new Dimension(CANVAS_WIDTH+buffer, CANVAS_HEIGHT+buffer));
	 	 
	 }
	 
	 public void regenMaze()
	 {
		 maze = new Maze(MAZE_SIZE,MAZE_SIZE);
		 //if(maze.pathLength < MAZE_SIZE * (MAZE_SIZE/10))
		 //{
		//	 regenMaze();
		 //}
	 }
	 
	 public void regenMazeWithWalls()
	 {
		 maze.resetMazeWithWalls();		 
	 }
	 
	 
	 
	 public void toggleShowSolution()
	 {
		 showSolution = !showSolution;
	 }
	 
	 public void toggleEdit()
	 {
		 editMode = !editMode;
		 // reset Maze
		 if(editMode)
		 {
			 maze = new Maze(MAZE_SIZE,MAZE_SIZE,true);
		 }
		 else
		 {
			 maze.initializeMaze();
			 maze.solveMaze();
		 }
		 this.repaint();
		 
	 }
	 
	 public void setPlayerTravel(int x, int y, MouseEvent e)
	 {
		 maze.setPlayerLocationByCoordOfRoom(x, y,e);
	 }
	 
	 public void toggleWall(int x, int y)
	 {
		 maze.toggleWallByCoordOfRoom(x, y, setWall);
	 }
	 
	 public void toggleSetWall(int x, int y)
	 {
		 setWall = !(maze.getRoomByCoord(x, y).wall);		 
	 }
	
	 @Override
	 public void paintComponent(Graphics g) {
	    super.paintComponent(g); 
	    setBackground(Color.white);
	    setForeground(Color.black);
	    for(int x = 0; x < maze.rooms.length; x++)
	    {
	    	for(int y = 0; y < maze.rooms[0].length; y++)
	    	{
	    		g.setColor(Color.black);
	    		if(maze.rooms[x][y].northWall)
	    		{
	    			g.drawLine(x*Room.ROOM_SIZE, y*Room.ROOM_SIZE, (x+1)*Room.ROOM_SIZE-1, y*Room.ROOM_SIZE);
	    		}
	    		if(maze.rooms[x][y].eastWall)
	    		{
	    			g.drawLine((x+1)*Room.ROOM_SIZE, y*Room.ROOM_SIZE, (x+1)*Room.ROOM_SIZE, (y+1)*Room.ROOM_SIZE-1);
	    		}
	    		if(maze.rooms[x][y].southWall)
	    		{
	    			g.drawLine(x*Room.ROOM_SIZE, (y+1)*Room.ROOM_SIZE, (x+1)*Room.ROOM_SIZE-1, (y+1)*Room.ROOM_SIZE);
	    		}
	    		if(maze.rooms[x][y].westWall)
	    		{
	    			g.drawLine(x*Room.ROOM_SIZE, y*Room.ROOM_SIZE, x*Room.ROOM_SIZE, (y+1)*Room.ROOM_SIZE-1);
	    		}	
	    		if(maze.rooms[x][y].playerVisited)
	    		{
	    			g.setColor(Color.GREEN);
	    			g.drawRect(x*Room.ROOM_SIZE+2, y*Room.ROOM_SIZE+2, Room.ROOM_SIZE-4,Room.ROOM_SIZE-4);
	    		}
	    		if(x == maze.rooms.length-1 && y == maze.rooms[0].length - 1)
	    		{
	    			g.setColor(Color.red);
	    			g.drawRect(x*Room.ROOM_SIZE+2, y*Room.ROOM_SIZE+2, Room.ROOM_SIZE-4,Room.ROOM_SIZE-4);
	    		}
	    		if(maze.rooms[x][y].inSolution && showSolution)
	    		{
	    			g.setColor(Color.LIGHT_GRAY);
	    			g.fillOval(x*Room.ROOM_SIZE+1, y*Room.ROOM_SIZE+1, Room.ROOM_SIZE-2,Room.ROOM_SIZE-2);
	    		}
	    		if(maze.rooms[x][y].wall)
	    		{
	    			g.setColor(Color.black);
	    			g.fillRect(x*Room.ROOM_SIZE, y*Room.ROOM_SIZE, Room.ROOM_SIZE, Room.ROOM_SIZE);
	    		}
	    	}
	    }
	 }	 	 
}
