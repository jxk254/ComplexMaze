package com.maze;

import java.util.List;

public class Room
{
	public static final int ROOM_SIZE =	15;
	public boolean northWall;
	public boolean eastWall;
	public boolean southWall;
	public boolean westWall;
	public boolean inSolution;
	public boolean playerVisited;
	public List<Room> parentMaze;
	public boolean wall;
	
	public Room()
	{
		northWall = true;
		eastWall = true;
		southWall = true;
		westWall = true;		
		inSolution = false;
		playerVisited = false;
		wall = false;
	}
	
	public void resetSolutionKeepWall()
	{
		northWall = true;
		eastWall = true;
		southWall = true;
		westWall = true;		
		inSolution = false;
		playerVisited = false;		
	}
		
}
