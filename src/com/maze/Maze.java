package com.maze;

import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import javax.swing.SwingUtilities;

public class Maze
{
	public Room[][] rooms;
	private Random rand = new Random();
	private enum Direction {NORTH,SOUTH,EAST,WEST};
	boolean[][] wasHere ;
	boolean[][] correctPath; // The solution to the maze
	int pathLength = 0;
	int startX, startY; // Starting X and Y values of maze
	int endX, endY;     // Ending X and Y values of maze
	
	public Maze(int x, int y)
	{
		rooms = new Room[x][y];
		wasHere = new boolean[x][y];
		correctPath = new boolean[x][y];
		startX = 0;
		startY = 0;
		endX = x-1;
		endY = y-1;
		for(int xs = 0; xs < x; xs++)
		{
			for(int ys = 0; ys < y; ys++)
			{
				rooms[xs][ys] = new Room();
			}
		}
		
		initializeMaze();
		solveMaze();		
	}
	
	public Maze(int x, int y, boolean clean)
	{
		rooms = new Room[x][y];
		wasHere = new boolean[x][y];
		correctPath = new boolean[x][y];
		startX = 0;
		startY = 0;
		endX = x-1;
		endY = y-1;
		
		if(clean)
		{
			
			for(int xs = 0; xs < x; xs++)
			{
				for(int ys = 0; ys < y; ys++)
				{
					rooms[xs][ys] = new Room();
				}
			}
		}
		else
		{
			new Maze(x,  y);
		}
	}
	
	public void resetMazeWithWalls()
	{
		for(int xs = 0; xs < rooms.length; xs++)
		{
			for(int ys = 0; ys < rooms[0].length; ys++)
			{
				rooms[xs][ys].resetSolutionKeepWall();
			}
		}
		initializeMaze();
		solveMaze();
	}
	
	public void initializeMaze()
	{
		
		
		List<Room> outsideMaze = new ArrayList<Room>();
		List<Room> insideMaze = new ArrayList<Room>();
		
		List<ArrayList<Room>> intermediates = new ArrayList<ArrayList<Room>>();
		
		// Load the outside array
		for(int x = 0; x < rooms.length; x++)
			for(int y = 0; y < rooms[0].length; y++)
			{
				if(!rooms[x][y].wall)
				{
					outsideMaze.add(rooms[x][y]);
				}
			}
		
		//Generate initial maze w/ sub mazes
		while(outsideMaze.size() > 0)
		{
			int startNumber = rand.nextInt(outsideMaze.size());
			Room startRoom = outsideMaze.get(startNumber);
			ArrayList<Room> newIntermediate = new ArrayList<Room>();
			newIntermediate.add(startRoom);
			startRoom.parentMaze = newIntermediate;
			outsideMaze.remove(startRoom);
			
			Room nextRoom = getValidRandomDirection(startRoom);
			if(nextRoom != null)
			{								
				List<Room> existingIntermidiate = nextRoom.parentMaze;
				if(existingIntermidiate != null && existingIntermidiate != outsideMaze)
				{
					existingIntermidiate.add(startRoom);
					startRoom.parentMaze = existingIntermidiate;					
				}
				else
				{					
					newIntermediate.add(nextRoom);
					nextRoom.parentMaze = newIntermediate;
					outsideMaze.remove(nextRoom);
					intermediates.add(newIntermediate);
				}
			}			
		}
		
		//for each submaze, start combining them to the final insideMaze
		
		while(intermediates.size() > 1)
		{
			Collections.shuffle(intermediates);
			List<Room> currentSub = intermediates.get(0);
			Collections.shuffle(currentSub);
			for(int x = 0; x < currentSub.size();x++)
			{
				Room currentRoom = currentSub.get(x);
				Room nextRoom = getValidRandomDirection(currentRoom);
				if(null != nextRoom)
				{
					List<Room> mergeFrom = nextRoom.parentMaze;
					while(!mergeFrom.isEmpty())
					{
						Room workingRoom = mergeFrom.get(0);
						workingRoom.parentMaze = currentSub;
						currentSub.add(workingRoom);
						mergeFrom.remove(0);
					}
					intermediates.remove(mergeFrom);
					break;
				}
				else
				{
					currentSub.remove(currentRoom);
					if(currentSub.size() == 0)
					{
						intermediates.remove(currentSub);
					}
				}
				
				
			}
		}
		
		//Finally, open outer walls
		
		rooms[0][0].westWall = false;
		rooms[0][0].playerVisited = true;
		rooms[rooms.length-1][rooms[0].length-1].eastWall = false;
	}
	
	
	private int[] getRoomCoords(Room room)
	{
		for(int x = 0; x<rooms.length;x++)
			for(int y = 0; y<rooms[0].length;y++)
			{
				if(rooms[x][y] == room)
						{
							return new int[]{x,y};
						}
			}
		return null;
	}
	
	private Room getValidRandomDirection(Room room)
	{		
		List<Direction> validDir = new ArrayList<Direction>();
		// check directions, if valid (still in bounds of maze, and not in this rooms set, add direction to array
		int[] roomCoords = getRoomCoords(room);
		int thisX = roomCoords[0];
		int thisY = roomCoords[1];
				
		if(thisX+1 <= rooms.length-1 && rooms[thisX+1][thisY].parentMaze != room.parentMaze && !rooms[thisX+1][thisY].wall)
		{
			validDir.add(Direction.EAST);
		}
		
		if(thisX-1 >= 0 && rooms[thisX-1][thisY].parentMaze != room.parentMaze && !rooms[thisX-1][thisY].wall)
		{
			validDir.add(Direction.WEST);
		}
		
		if(thisY+1 <= rooms[0].length-1 && rooms[thisX][thisY+1].parentMaze != room.parentMaze && !rooms[thisX][thisY+1].wall)
		{
			validDir.add(Direction.SOUTH);
		}
		
		if(thisY-1 >= 0 && rooms[thisX][thisY-1].parentMaze != room.parentMaze && !rooms[thisX][thisY-1].wall)
		{
			validDir.add(Direction.NORTH);
		}
		if(validDir.size() > 0)
		{
			int nextX = thisX;
			int nextY = thisY;
			switch (validDir.get(rand.nextInt(validDir.size())))
			{
			case NORTH:
				nextY--;
				room.northWall = false;
				rooms[nextX][nextY].southWall = false;
				break;
			case EAST:
				nextX++;
				room.eastWall = false;
				rooms[nextX][nextY].westWall = false;
				break;
			case SOUTH:
				nextY++;
				room.southWall = false;
				rooms[nextX][nextY].northWall = false;						
				break;
			case WEST:
				nextX--;
				room.westWall = false;
				rooms[nextX][nextY].eastWall = false;
				break;				
			}
			return rooms[nextX][nextY];
		}
		return null;	
	}
	
	public void solveMaze()
	{
		
	    for (int row = 0; row < rooms.length; row++)  
	        // Sets boolean Arrays to default values
	        for (int col = 0; col < rooms[row].length; col++){
	            wasHere[row][col] = false;
	            correctPath[row][col] = false;
	        }
	    boolean b = recursiveSolve(startX, startY);
	    // Will leave you with a boolean array (correctPath) 
	    // with the path indicated by true values.
	    // If b is false, there is no solution to the maze
	    rooms[0][0].inSolution = false;
	}
	
	public boolean recursiveSolve(int x, int y) {		
	    if (x == endX && y == endY) return true; // If you reached the end
	    if (wasHere[x][y]) return false;  
	    // If you are on a wall or already were here
	    wasHere[x][y] = true;
	    Room thisRoom = rooms[x][y];
	    if (x > 0) // Checks if not on left edge
	        if (!thisRoom.westWall && recursiveSolve(x-1, y)) { // Recalls method one to the left
	            correctPath[x][y] = true; // Sets that path value to true;
	            pathLength +=1;
	            thisRoom.inSolution = true;
	            return true;
	        }
	    if (x < rooms.length-1) // Checks if not on right edge
	        if (!thisRoom.eastWall && recursiveSolve(x+1, y)) { // Recalls method one to the right
	            correctPath[x][y] = true;
	            thisRoom.inSolution = true;
	            pathLength +=1;
	            return true;
	        }
	    if (y > 0)  // Checks if not on top edge
	        if (!thisRoom.northWall && recursiveSolve(x, y-1)) { // Recalls method one up
	            correctPath[x][y] = true;
	            thisRoom.inSolution = true;
	            pathLength +=1;
	            return true;
	        }
	    if (y < rooms[0].length-1) // Checks if not on bottom edge
	        if (!thisRoom.southWall && recursiveSolve(x, y+1)) { // Recalls method one down
	            correctPath[x][y] = true;
	            thisRoom.inSolution = true;
	            pathLength +=1;
	            return true;
	        }
	    return false;
	}
	
	public void toggleWallByCoordOfRoom(int x, int y, boolean setWall)
	{
		int convertedX = x/Room.ROOM_SIZE;
		int convertedY = y/Room.ROOM_SIZE;
		
		if(convertedY >= 0 && convertedX >= 0 && convertedX < rooms.length && convertedY < rooms[0].length)
		rooms[convertedX][convertedY].wall = setWall;
	}
	
	public Room getRoomByCoord(int x, int y)
	{
		int convertedX = x/Room.ROOM_SIZE;
		int convertedY = y/Room.ROOM_SIZE;
		if(convertedY >= 0 && convertedX >= 0 && convertedX < rooms.length && convertedY < rooms[0].length)
			return rooms[convertedX][convertedY];
		
		return rooms[0][0];
	}
	
	public void setPlayerLocationByCoordOfRoom(int x, int y, MouseEvent e)
	{
		int convertedX = x/Room.ROOM_SIZE;
		int convertedY = y/Room.ROOM_SIZE;
		int buttonPressed = e.getButton();
		
		boolean addRoom = true;
		
		
		if(buttonPressed == MouseEvent.BUTTON3)
		{
			addRoom = false;			
		}
		if(buttonPressed == 0)
		{
			if(SwingUtilities.isRightMouseButton(e))
			{
				addRoom = false;
			}
		}
				
		// now see if this room is connected to another room previously visted.  Just check all... it's easier.	
		if(addRoom)
		{
			if(convertedY >= 0 && convertedX >= 0 && convertedX < rooms.length && convertedY < rooms[0].length)
			{
				if(convertedY-1 >= 0)
					if(rooms[convertedX][convertedY-1].playerVisited && !rooms[convertedX][convertedY].northWall)
					{
						rooms[convertedX][convertedY].playerVisited = true;
					}
				if(convertedX-1 >= 0)
					if(rooms[convertedX-1][convertedY].playerVisited && !rooms[convertedX][convertedY].westWall)
					{
						rooms[convertedX][convertedY].playerVisited = true;
					}
				if(convertedX + 1 < rooms.length)				
					if(rooms[convertedX+1][convertedY].playerVisited && !rooms[convertedX][convertedY].eastWall)
					{
						rooms[convertedX][convertedY].playerVisited = true;
					}
				if(convertedY+1 < rooms[0].length)
					if(rooms[convertedX][convertedY+1].playerVisited && !rooms[convertedX][convertedY].southWall)
					{
						rooms[convertedX][convertedY].playerVisited = true;
					}
			}
		}
		else
		{
			rooms[convertedX][convertedY].playerVisited = false;
		}
				
					
		rooms[0][0].playerVisited = true;
	}
}
