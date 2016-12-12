package dpr212;

public class GenerateRunner {
	public static void main(String [] args) {
//		for (int i = 0; i < 50; i++) {
//			System.out.println(MazeGenerator.randomInt(3, 6));
//		}
		MazeGenerator m = new MazeGenerator(8);
		m.generateSpaces();
		MazePrinter.printOriginalMaze(m.getDim(), m.getWalls());
		System.out.println("Dead ends selected: " + MazeGenerator.DEAD);
		System.out.println("Intersections created: " + MazeGenerator.INTER);
	}
	
	public static void die(String message) {
		System.out.println(message);
		System.exit(0);
	}
}
