package dpr212;

import static dpr212.Direction.*;

import java.util.ArrayList;
import java.util.Stack;
import java.util.Random; //For random number generation

public class MazeGenerator {
//	private char [][] walls;
	public static int DEAD = 0;
	public static int INTER = 0;
	private Walls walls;
	private int dim; //The dimensions of the maze
	private ArrayList<Step> truePath; //This contains the true path through the maze
	
	/**
	 * Create a new instance of a MazeGenerator
	 * @param newDim
	 */
	public MazeGenerator (int newDim) {
		if (newDim >= 3) {
			dim = newDim;
		} else {
			GenerateRunner.die("Error: Maze dimension too small. Please only generate a maze of size 3 or greater.");
		}
//		walls = new char[dim][dim];
		walls = new Walls(newDim);
	}
	
	public int generateTruePathIntersections(final ArrayList<Step> path) {
		int inters = randomInt(1, path.size() - 2); // A random number from 0 to 2 less than the path size
		// (the minus two happens because the start and goal cannot be intersections)
		int numOfInters = inters; // This one is used to return how many intersections the given path argument has

		while (inters > 0) {
			int index = randomInt(1, path.size() - 2); // Get a random index, excluding the start and goal positions
			if (index >= path.size()) {
				System.out.print("");
			}
			if (!(path.get(index).getIsIntersection())) { // If this is not already an intersection
				path.get(index).setIsIntersection(true); // Label this as an intersection
				// walls.set(path.get(index).getRow(),
				// path.get(index).getCol(), 'X');
				INTER++;
				inters--; // One fewer intersection
			}
		}

		return numOfInters;
	}
	
	/**
	 * Marks a random number of intersections on the given path
	 * @param path
	 * @return The number of intersections generated (0 if there are no intersections generated)
	 */
	public int generateBranchIntersections(final ArrayList<Step> path) {
//		if (path.size() >= 4) { // If there are at least 4 steps in the path
			int inters = randomInt(1, path.size()-1); // A random number of intersections
			int numOfInters = inters; // This one is used to return how many intersections the given path argument has
			
			while (inters > 0) {
				int index = randomInt(0, path.size() - 1); // Get a random index, excluding the start and goal positions
				if (index >= path.size()) {
					System.out.print("");
				}
				System.out.println("Index: " + index);
				if (!(path.get(index).getIsIntersection())) { // If this is not already an intersection
					path.get(index).setIsIntersection(true); // Label this as an intersection
					// walls.set(path.get(index).getRow(),
					// path.get(index).getCol(), 'X');
					inters--; // One fewer intersection
				}
			}
			
			return numOfInters;
//		} else { // If there are 3 or fewer steps in the path
//			if (path.size() == 0) { // If there are no steps in the path
//				return 0; // Return 0 (no intersections)
//			} else if (path.size() == 1) { // if there is only one step in the path
//				int decide = randomInt(0, 1); // Get a random in from 0 to 1 to decide whether this should be an intersection or not
//				if (decide == 0) { // If this should not be an intersection
//					return 0;
//				} else { // If this should be an intersection
//					path.get(0).setIsIntersection(true); // Label the only step in the path as an intersection
//					return 1;
//				}
//			} else if (path.size() == 2) { //If the branch has only two steps
//				
//			}
//		}
	}
	
	/**
	 * Generates branches for each intersections of a given path (array list of steps)
	 * @param truePath
	 */
	private void generateBranches(ArrayList<Step> path) {
		for (Step step : path) { //We will loop through all of the steps of the true path
			if (step.getIsIntersection() == true) { //If this is a designated intersection
				generateBranch(step);
			}
		}
	}
	
	/**
	 * Generate a branch from a specified step object (usually an intersection)
	 * @param intersectionStep - The intersection we plan on branching from
	 * @return An ArrayList of steps that make up the branch
	 */
	private ArrayList<Step> generateBranch(Step intersectionStep) {
		ArrayList<Step> branchPath = new ArrayList<Step>();
		boolean keepGoing = true; //Decides when to end the loop
		Step nextStep = intersectionStep; //Initialize nextStep
		while (keepGoing == true) { //This loop generates a new branch for a given step
			nextStep = nextStep.getNextStepRandDirection(walls); //Get a new step in a random direction
			if (nextStep != null) { //If we successfully generated a step
				walls.set(nextStep.getRow(), nextStep.getCol(), '0');
				branchPath.add(nextStep); //Add this step to the path of the branch
//				if (nextStep.getIsDeadEnd() == true) { //If this is a dead end
//					DEAD++; //For debugging
//					keepGoing = false; //We must move to the next intersection
//				}
			} else { //If nextStep is null (we were unable to step from the last step)
				keepGoing = false; //We must move to the next intersection
			}
		}
		if (branchPath.size() == 0) {
			System.out.print("");
		}
		return branchPath; //An ArrayList of steps that make up the new branch.
	}
	
	/**
	 * Generate the first true path through the maze.
	 */
	public void generateTruePath() {
		Step start = generateStartingPos(); //We need a place to start from
		truePath = new ArrayList<Step>();
//		walls[start.getRow()][start.getCol()] = '0'; //Add this starting position to the walls
		walls.set(start.getRow(), start.getCol(), '0'); //Add this starting position to the walls
		truePath.add(start); //Add this starting position to the solution path
		System.out.println(start);
		Step step = start.getNextStepDir(start.getDirecion()); //take one initial step in the same direction
//		walls[step.getRow()][step.getCol()] = '0'; //Add this step to the walls
		walls.set(step.getRow(), step.getCol(), '0');  //Add this step to the walls
		truePath.add(step); //Add this step to the solution path
		System.out.println(step);
		boolean keepGoing = true;
		int numNonNull = 0;
		int numAllowed = 0;
		Step keep; //Used for remembering the last valid step (in case we generate a non-valid step)
		
		while ( keepGoing == true ) { //As long as we have not yet reached the goal
			keep = step; //Pass this step to the keep step so that if we run into a not-allowed step
						  //we can put this back
			step = step.getNextStepRandDirection(); //Generate a random next step
			
			if (step != null) { //If the step is not null (because it will be null if we've reached the goal)
				numNonNull++;
				if (step.isAllowed(walls)) { //Check if that step is allowed (since we now know it's not the end)
					numAllowed++;
					if (step.isGoal()) { //If this is a goal
						keepGoing = false; //We can stop generating
					}
					walls.set(step.getRow(), step.getCol(), '0');
					truePath.add(step);
					System.out.println(walls);
				} else {
					step = keep;
				}
				System.out.println(step);
			}
			System.out.println("Number of non-null steps generated: " + numNonNull);
			System.out.println("Number of allowed steps generated: " + numAllowed);
		}
		
//		for (Step s : path) {
//			walls[s.getRow()][s.getCol()] = '0';
//		}
	}
	
	public Step generateStartingPos() {
		int row = randomInt(1, (dim-2) ); //we need a random row number between 1 and dim-1 inclusive
		return new Step(row, 0, EAST, dim);
	}
	
	/**
	 * Generates a random integer between low and high, inclusive
	 * @param low - The lower bound of the random integer
	 * @param high - The upper bound of the random integer
	 * @return - A random integer between low and high, inclusive
	 */
	public static int randomInt(int low, int high) {
//		return low + (int)(Math.random() * ((high - low) + 1));
		int range = high - low + 1; //Find out the range of numbers to generate (the +1 helps the high value be included in the range)
		int result = (int) ( Math.random() * range ); //generate a random number in that range
		result += low; //Bring that random number up into the proper range
		return result; //return the result
	}
	
	/**
	 * Overloaded method so that you only have to specify the upper bound of the random number
	 * @param high - The highest you want the number to be
	 * @return - A random number from 0 to high, inclusive.
	 */
	public static int randomInt(int high) {
		return randomInt(0, high);
	}
	
	/**
	 * This method actually generates the random pathways through the maze
	 */
	public void generateSpaces() {
		/*
		 * 1. Never reach the edge
		 * 2. Have good branches
		 */
		generateTruePath();
		int truePathIntersections = generateTruePathIntersections(truePath);
		for (Step s : truePath) {
			if (s.getIsIntersection() == true) {
				ArrayList<Step> branch = generateBranch(s); //Get a new branch from the truePath intersection
				System.out.println("Branchsize: " + branch.size());
				if (branch.size() > 0) { //If this is not an empty branch
					generateBranchIntersections(branch); //Generate intersections on the new branch
					generateBranches(branch); //Generate more branches for each branch
				} //Otherwise, somehow we were unable to generate a branch of any size greater than 0, so we should skip over this one.
			}
		}
		
		//For debugging
//		for (Step s : truePath) {
//			if (s.getIsIntersection() == true) {
//				walls.set(s.getRow(), s.getCol(), 'X');
//			}
//		}
		
	}
	
	public char [][] getWalls() {
		return walls.getWalls();
	}
	
	public int getDim() {
		return dim;
	}
}
