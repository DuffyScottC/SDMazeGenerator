package dpr212;

public class GenerateRunner {
	public static void main(String [] args) {
//		for (int i = 0; i < 50; i++) {
//			System.out.println(MazeGenerator.randomInt(3, 6));
//		}
		MazeGenerator m = new MazeGenerator(6);
		m.generateSpaces();
		MazePrinter.printOriginalMaze(m.getDim(), m.getWalls());
	}
	
	public static void die(String message) {
		System.out.println(message);
		System.exit(0);
	}
}
