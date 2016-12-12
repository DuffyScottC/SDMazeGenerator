package dpr212;

import static dpr212.Direction.*;

/**
 * Positions go (row, col)
 * 
 * @author Scott
 */
public class Step {
	private int row;
	private int col;
	private int dim;
	private Direction direction;
	private boolean isIntersection;
	private boolean isDeadEnd;
	
	/**
	 * Create a new step object with specified row, col, direction, and dim
	 * @param nRow
	 * @param nCol
	 * @param nDirection
	 * @param nDim
	 */
	public Step(int nRow, int nCol, Direction nDirection, int nDim) {
		row = nRow;
		col = nCol;
		direction = nDirection;
		isIntersection = false;
		isDeadEnd = false;
		dim = nDim;
	}
	
	/**
	 * Create a new Step object with specified isDeadEnd
	 * @param nRow
	 * @param nCol
	 * @param nDirection
	 * @param nDim
	 * @param nIsDeadEnd
	 */
	public Step(int nRow, int nCol, Direction nDirection, int nDim, boolean nIsDeadEnd) {
		row = nRow;
		col = nCol;
		direction = nDirection;
		isIntersection = false;
		isDeadEnd = nIsDeadEnd;
		dim = nDim;
	}
	
	/**
	 * Returns the next step in the specified direction (or null if out of bounds)
	 * @return - a step object one position in the specified direction; null if the next step in the specified direction is out of bounds
	 */
	public Step getNextStepDir(Direction newDirection) {
		int nextRow = 0;
		int nextCol = 0;
		
		switch (newDirection) {
		case NORTH:
			nextRow = row - 1;
			nextCol = col;
			break;
		case EAST:
			nextRow = row;
			nextCol = col + 1;
			break;
		case SOUTH:
			nextRow = row + 1;
			nextCol = col;
			break;
		case WEST:
			nextRow = row;
			nextCol = col - 1;
			break;
		default: //On the off-chance that something goes wrong:
			GenerateRunner.die("Error: Invalid argument for direction to move. Please use the Direction enum values.");
		}
		
		if (isInBounds(nextRow, nextCol)) { //If this is a valid position in the maze
			return new Step(nextRow, nextCol, newDirection, dim); //Create the step
		} else {
			return null;
		}
	}
	
	/**
	 * Tests if a next step in the specified direction from the current step is within the bounds of the maze
	 * @param newDirection - The specified direction
	 * @return true if the step in the specified direction is within the bounds of the maze
	 */
	public boolean testStepInDir(Direction newDirection) {
		int nextRow = 0;
		int nextCol = 0;
		
		switch (newDirection) {
		case NORTH:
			nextRow = row - 1;
			nextCol = col;
			break;
		case EAST:
			nextRow = row;
			nextCol = col + 1;
			break;
		case SOUTH:
			nextRow = row + 1;
			nextCol = col;
			break;
		case WEST:
			nextRow = row;
			nextCol = col - 1;
			break;
		default: //On the off-chance that something goes wrong:
			GenerateRunner.die("Error: Invalid argument for direction to move. Please use the Direction enum values.");
		}
		
		if (isInBounds(nextRow, nextCol)) { //If this is a valid position in the maze
			return true;
		} else { //If this is not a valid position in the maze
			return false;
		}
	}
	
	/**
	 * Moves in a random direction, ensuring that this is not an invalid position
	 * @param walls
	 * @return A step object in a random direction from the current step object (or null if the current step
	 * is surrounded by open spaces or is pinned against the edge
	 */
	public Step getNextStepRandDirection(Walls walls) {
		boolean [] triedThis = new boolean[4]; //This will help me determine whether everything has already been tried or not
		for (int i = 0; i < 4; i++) { //Initialize all the elements as false to begin with
			triedThis[i] = false;
		}
		
		Direction newDirection = direction;
		int nextRow = 0;
		int nextCol = 0;
		boolean keepGoing = true;
		boolean successful = true; //Signals whether we successfully generated a new random step or not
		while (keepGoing == true) {
			int decide = MazeGenerator.randomInt(1, 4); //A random number from one to 4
			switch (decide) {
			case 1:
				triedThis[0] = true; //We don't need to repeat this
				
				//if a step NORTH is in-bounds AND if we didn't just come from NORTH
				if(testStepInDir(NORTH)) {
					nextRow = row - 1;
					nextCol = col;
					newDirection = NORTH;
					
				}
				break;
			case 2:
				triedThis[1] = true; //We don't need to repeat this
				
				//if a step WEST is in-bounds AND if we didn't just come from WEST
				if(testStepInDir(WEST)) {
					nextRow = row;
					nextCol = col - 1;
					newDirection = WEST;
					
				}
				break;
			case 3:
				triedThis[2] = true; //We don't need to repeat this
				//if a step SOUTH is in-bounds AND if we didn't just come from SOUTH
				if(testStepInDir(SOUTH)) {
					nextRow = row + 1;
					nextCol = col;
					newDirection = SOUTH;
					
				}
				break;
			default:
				triedThis[3] = true; //We don't need to repeat this
				//if a step EAST is in-bounds AND if we didn't just come from EAST
				if(testStepInDir(EAST)) {
					nextRow = row;
					nextCol = col + 1;
					newDirection = EAST;
					
				}
				break;
			}
			
			if (isInInterior(nextRow, nextCol) == true) { //If this is in the interior of the maze
				if (walls.get(nextRow, nextCol) != '0') { //If this is not already an open space
					if (isAllowed(nextRow, nextCol, walls)) {
						keepGoing = false; //If this is not already an open space, then we're good
					}
				}
			}
			
			if (keepGoing == true) { //If we're about to loop again
				//Check to see if we've tried every available direction
				for (int i = 0; i < 4; i++) { //Loop 4 times to check each of the 4 items in tryedThis
					if (triedThis[i] == true) { //If this one has already been tried
						keepGoing = false; //If all 4 are true, this will stay false
						successful = false; //We were not successful in generating a step
					} else {
						keepGoing = true; //We should keep trying if even one is not true
						successful = true; //Until otherwise stated, we are successful in generating a step
						i = 5; //Stop this loop once we've found just one that we've already tried
					}
				}
			}
		}
		
		if (successful == true) { //If we successfully generated a step
			int decide = MazeGenerator.randomInt(5);
			if (decide == 1) { //Will be selected as dead end 20% (1/5) of the time
				return new Step(nextRow, nextCol, newDirection, dim, true); //This will be the dead end of this row
			} //Otherwise, will be selected as not the dead end 80% (4/5) of the time
			return new Step(nextRow, nextCol, newDirection, dim); //
		} else { //If no step could be generated because the current step is surrounded (or pinned against the edge and surrounded)
			return null; //We need to signal that we cannot branch further from this step
		}
	}
	
	/**
	 * Checks whether a position is in the interior of the maze. e.g. all 0's in the following:
	 * 1 1 1 1
	 * 1 0 0 1
	 * 1 0 0 1
	 * 1 1 1 1
	 * @return true if this is in the interior, false if not
	 */
	private boolean isInInterior(int newRow, int newCol) {
		if (1 <= newRow && newRow <= dim-2) { //If the row is in the interior (e.g. 0's in this: 1 0 0 1)
			if (1 <= newCol && newCol <= dim-2) { //If the col is in the interior (e.g. 0's in below:)
																						//1
																						//0
																						//0
																						//1
				return true; //Then this is within the interior
			}
		}
		
		return false; //Otherwise, this step is not within the interior
	}
	
	/**
	 * Moves in a random direction.
	 * @param walls
	 * @return A step object in a random direction from the current step object
	 */
	public Step getNextStepRandDirection() {
		
		Direction newDirection = direction;
		int nextRow = 0;
		int nextCol = 0;
		boolean keepGoing = true;
		while (keepGoing == true) {
			int decide = MazeGenerator.randomInt(1, 4); //A random number from one to 4
			switch (decide) {
			case 1:
				//if a step NORTH is in-bounds AND if we didn't just come from NORTH
				if(testStepInDir(NORTH) && direction != SOUTH) {
					nextRow = row - 1;
					nextCol = col;
					newDirection = NORTH;
					keepGoing = false;
				}
				break;
			case 2:
				//if a step WEST is in-bounds AND if we didn't just come from WEST
				if(testStepInDir(WEST) && direction != EAST) {
					nextRow = row;
					nextCol = col - 1;
					newDirection = WEST;
					keepGoing = false;
				}
				break;
			case 3:
				//if a step SOUTH is in-bounds AND if we didn't just come from SOUTH
				if(testStepInDir(SOUTH) && direction != NORTH) {
					nextRow = row + 1;
					nextCol = col;
					newDirection = SOUTH;
					keepGoing = false;
				}
				break;
			default:
				//if a step EAST is in-bounds AND if we didn't just come from EAST
				if(testStepInDir(EAST) && direction != WEST) {
					nextRow = row;
					nextCol = col + 1;
					newDirection = EAST;
					keepGoing = false;
				}
				break;
			}
		}
		
		return new Step(nextRow, nextCol, newDirection, dim);
	}
	
	/**
	 * Checks whether a step is isolated. B is a non-isolated step:
	 * 1 1 1 1 1
	 * 0 0 B 1 1
	 * 1 0 0 0 1
	 * 1 1 1 0 1
	 * 1 1 1 0 1
	 * Because B is directly adjacent to two open spaces
	 * @param step
	 * @param walls
	 * @return true if there is only one adjacent space, false if there are more than one
	 */
	public boolean isIsolated(Step step, final char [][] walls) {
		int numOfOpenAdjSpaces = 0; //Keeps track of the number of open adjacent spaces
		int nRow = step.getRow(); //The row of the step we're checking
		int nCol = step.getCol(); //The col of the step we're checking
		
		if (isInBounds(nRow - 1, nCol)) { //Check if this is a valid direction to check
			if (walls[nRow - 1][nCol] == '0') { //If NORTH is an open space
				numOfOpenAdjSpaces++; //Add one open space
			}
		}
		if (isInBounds(nRow, nCol + 1)) { //Check if this is a valid direction to check
			if (walls[nRow][nCol + 1] == '0') { //If EAST is an open space
				numOfOpenAdjSpaces++; //Add one open space
			}
		}
		if (isInBounds(nRow + 1, nCol)) { //Check if this is a valid direction to check
			if (walls[nRow + 1][nCol] == '0') { //If SOUTH is an open space
				numOfOpenAdjSpaces++; //Add one open space
			}
		}
		if (isInBounds(nRow, nCol - 1)) { //Check if this is a valid direction to check
			if (walls[nRow][nCol - 1] == '0') { //If WEST is an open space
				numOfOpenAdjSpaces++; //Add one open space
			}
		}
		
		if (numOfOpenAdjSpaces > 1) { //If there are more than one open adjacent spaces
			return false; //This is not isolated
		} else { //If there is only one open adjacent space
			return true; //This is isolated
		}
	}
	
	/**
	 * (variation for use on coordinates, not a step) Checks to make sure this new step is allowed meaning:
	 * It does not result in a block of open spaces being formed
	 * @param nextRow
	 * @param nextCol
	 * @param walls
	 * @return True if this step is allowed, false if not
	 */
	public boolean isAllowed(int nextRow, int nextCol, Walls walls) {
		/*
		 * Reference
		 * row: up=(-1) down=(+1)
		 * col: left=(-1) right=(+1)
		 */
		
		/*
		 * First check if the step is B in this config:
		 * 1 0 0
		 * 1 B 0
		 * 1 1 1
		 */
		if (isOpen(nextRow-1,nextCol, walls)) {
			if (isOpen(nextRow,nextCol+1, walls)) {
				if (isOpen(nextRow-1,nextCol+1, walls)) {
					return false;
				}
			}
		}
		
		/*
		 * Next check if the step is B in this config:
		 * 0 0 1
		 * 0 B 1
		 * 1 1 1
		 */
		if (isOpen(nextRow-1,nextCol, walls)) {
			if (isOpen(nextRow,nextCol-1, walls)) {
				if (isOpen(nextRow-1,nextCol-1, walls)) {
					return false;
				}
			}
		}
		
		/*
		 * Next check if the step is B in this config:
		 * 1 1 1
		 * 0 B 1
		 * 0 0 1
		 */
		if (isOpen(nextRow+1,nextCol, walls)) {
			if (isOpen(nextRow,nextCol-1, walls)) {
				if (isOpen(nextRow+1,nextCol-1, walls)) {
					return false;
				}
			}
		}
		
		/*
		 * Next check if the step is B in this config:
		 * 1 1 1
		 * 1 B 0
		 * 1 0 0
		 */
		if (isOpen(nextRow+1,nextCol, walls)) {
			if (isOpen(nextRow,nextCol+1, walls)) {
				if (isOpen(nextRow+1,nextCol+1, walls)) {
					return false;
				}
			}
		}
		return true;
	}
	
	/**
	 * Checks to make sure this new step is allowed meaning:
	 * It does not result in a block of open spaces being formed
	 * @return True if this step is allowed, false if not
	 */
	public boolean isAllowed(final Walls walls) {
		return isAllowed(row, col, walls);
	}
	
	public boolean isOpen(int newRow, int newCol, final Walls walls) {
		
		if (isInBounds(newRow, newCol)) {
			if (walls.get(newRow, newCol) == '0') {
				return true;
			} else {
				return false;
			}
		} else {
			return false; //Technically it's not open (this is the goal, but that will be caught later in the program. It's not this method's business)
		}
	}
	
	/**
	 * Checks to see if a position is in the bounds of the maze
	 * @param nRow
	 * @param nCol
	 * @return
	 */
	public boolean isInBounds(int nRow, int nCol) {
		if (0 <= nRow && nRow <= (dim-1) ) {
			if (0 <= nCol && nCol <= (dim-1) ) {
				return true; //the point is valid
			} else {
				return false; //the column is invalid
			}
		} else {
			return false; //the row is invalid
		}
	}
	
	/**
	 * Checks to see if a particular position is in a corner of the maze (may be unnecessary)
	 * @return
	 */
	private boolean isCorner (Walls walls) {
		/*
		 * 1 1 1 1
		 * 1 B 0 1
		 * 1 0 1 1
		 * 1 1 1 1
		 */
		if (row == 1 && col == 1) {
			return true;
		}
		
		/*
		 * 1 1 1 1
		 * 1 1 0 1
		 * 1 1 1 1
		 * 1 1 1 1
		 */
		if (row == 1 && col == dim - 1) {
			return true;
		}
		
		/*
		 * 1 1 1 1
		 * 1 1 1 1
		 * 1 1 0 1
		 * 1 1 1 1
		 */
		if (row == dim-1 && col == dim-1) {
			return true;
		}
		
		/*
		 * 1 1 1 1
		 * 1 1 1 1
		 * 1 0 1 1
		 * 1 1 1 1
		 */
		if (row == dim-1 && col == 1) {
			return true;
		}
		
		return false;
	}
	
	/**
	 * Checks if this step is a goal
	 * @return
	 */
	public boolean isGoal() {
		if (row == (dim-1) || row == 0) { //If the current row is on the edge
			return true; //We are at the goal
		} else if (col == (dim-1) || col == 0) { //If the current col is on the edge
			return true; //We are at the goal
		}
		return false; //Otherwise, we are not at the goal
	}

	/**
	 * @return the row
	 */
	public int getRow() {
		return row;
	}

	/**
	 * @return the col
	 */
	public int getCol() {
		return col;
	}
	
	@Override
	public String toString() {
		String s = "(" + row + ", " + col + ", " + direction;
		if (isIntersection == true) {
			s += ", intersection";
		}
		s += ")";
		return s;
	}

	public Direction getDirecion() {
		return direction;
	}

	public boolean getIsIntersection() {
		return isIntersection;
	}
	
	public void setIsIntersection(boolean newIsIntersection) {
		isIntersection = newIsIntersection;
	}

	public boolean getIsDeadEnd() {
		return isDeadEnd;
	}
}
