package dpr212;

public class GenerateRunner {
	public static void main(String [] args) {
//		for (int i = 0; i < 50; i++) {
//			System.out.println(MazeGenerator.randomInt(3, 6));
//		}
		MazeGenerator m = new MazeGenerator(12);
		m.generateSpaces();
		MazePrinter.printOriginalMaze(m.getDim(), m.getWalls());
		System.out.println("Dead ends selected: " + MazeGenerator.DEAD);
		System.out.println("Intersections created: " + MazeGenerator.INTER);
		//note to self: may need "read -p 'Press enter to close...'" at the end of run.command file (or whatever I've called it)
	}
	
	public static void die(String message) {
		System.out.println(message);
		System.exit(0);
	}
}
