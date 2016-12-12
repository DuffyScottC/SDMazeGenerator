package dpr212;

public class MazePrinter {

	/**
	 * Prints the maze in its original state.
	 * @param maze
	 */
	public static void printOriginalMaze(int dim, char [][] walls) {
		System.out.println(dim + " " + dim);
		for (int i = 0; i < dim; i++) {
			String row = new String(walls[i]); //Put each row of the maze into a String
			for (int j = 0; j < row.length(); j++) {
				//get one character from the string and print it
				System.out.print(row.substring(j, j + 1));
				//now print a space
				System.out.print(" ");
			}
			//now move to the next row
			System.out.println();
		}
	}
}
