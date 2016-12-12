package dpr212;

public class Walls {
	private char [][] walls;
	private int dim;
	
	public Walls (int newDim) {
		dim = newDim;
		walls = new char[dim][dim];
		//Initialize the maze so that it is all walls ('1'):
		for (int i = 0; i < dim; i++) {
			for (int j = 0; j < dim; j++) {
				walls[i][j] = '1';
			}
		}
	}
	
	public void set(int row, int col, char value) {
		walls[row][col] = value;
	}
	
	public char get(int row, int col) {
		return walls[row][col];
	}
	
	public char[][] getWalls() {
		return walls;
	}
	
	@Override
	public String toString() {
		String s = "";
		s += dim + " " + dim + "\n";
		for (int i = 0; i < dim; i++) {
			String row = new String(walls[i]); //Put each row of the maze into a String
			for (int j = 0; j < row.length(); j++) {
				//get one character from the string and print it
				s += row.substring(j, j + 1) + " ";
			}
			//now move to the next row
			s += "\n";
		}
		
		return s;
	}
}
