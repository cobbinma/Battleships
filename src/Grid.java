/**
* The class is used to create a console version
* of the classic Battleships game
* @version 1.0
* @author Matthew Cobbing
*/
import java.util.Scanner;

public class Grid {
	int playerArray[][] = new int[10][10];
	static int hits = 0;
	static int turns = 0;
	static boolean win = false;

	public static void main(String args[]) {
		Grid newGrid = new Grid();
		placeShips(newGrid);
		while (!win) {
			drawGrid(newGrid);
			fire(newGrid);
		}
		drawGrid(newGrid);
		System.out.println("\nYOU HAVE WON!");
		System.out.println("\nSCORE = " + turns);
	}

	//used to check user enter coordinates correctly then checks if user
	// has hit ship or not
	private static void fire(Grid newGrid) {
		boolean isValid = false;
		int x = 0;
		int y = 0;

		while (!isValid) {
			//loop until user inputs correct format
			System.out.println("\nEnter a target: ");
			Scanner reader = new Scanner(System.in);
			String coord = reader.nextLine();
			if (!(coord.length() == 2 || coord.length() == 3)) {//input is not correct length
				System.out.println("Incorrect target co-ordinate. ");
			} else if (coord.length() == 3 && !coord.substring(1).equals("10")) {//if length 3, needs to be 10
				System.out.println("Incorrect target co-ordinate. ");
			} else if (!(coord.substring(1) != null && coord.substring(1).matches("[-+]?\\d*\\.?\\d+"))) {
				System.out.println("Incorrect target co-ordinate. ");// non-numeric
			} else {
				if (coord.length() == 2) {
					y = Integer.parseInt(coord.substring(1)) - 1;//convert to array format
				} else if (coord.substring(1).equals("10")) {
					y = 9;
				} else {
					break;
				}
				x = ((coord.substring(0, 1).toLowerCase())).charAt(0) - 'a';//convert from letter to array format
				if (x < 0 || x > 9) {
					System.out.println("Incorrect target co-ordinate. ");
				} else if (y < 0 || y > 9) {
					System.out.println("Enter letter between A and J.");
				} else {
					isValid = true;
				}

			}
		}
		turns++;
		if (newGrid.playerArray[y][x] > 2) {//ship is found
			int shipId = newGrid.playerArray[y][x];
			newGrid.playerArray[y][x] = 1;//mark position as hit
			//check if any other instances of ship are remaining
			if(!anyOfShipLeft(newGrid, shipId)) {
				System.out.println("SHIP SUNK!");
			}
			hits++;
			if (hits == 13) {
				win = true;//no ships remain
			}
		} else if (newGrid.playerArray[y][x] == 0) {
			newGrid.playerArray[y][x] = 2;
		}

	}
	
	//uses ship ID to check if any more of the ship exists
	public static boolean anyOfShipLeft(Grid newGrid, int shipId) {
		for(int i=0;i<10;i++) {
			for(int j=0;j<10;j++) {
				if(newGrid.playerArray[i][j] == shipId) {
					return true;
				}
			}
			
		}
		return false;
	}

	//method is used to create 3 ships of different length in
	// random positions
	public static void placeShips(Grid newGrid) {
		createShip(newGrid, 5, 3);//long ship
		createShip(newGrid, 4, 4);
		createShip(newGrid, 4, 5);
	}

	//used to create a ship of given length of given length
	public static void createShip(Grid newGrid, int length, int shipId) {
		boolean sideways = Math.random() < 0.5;//randomly vertical or horizontal
		int x = (int) (Math.random() * 10);//random coordinates generated
		int y = (int) (Math.random() * 10);

		if (sideways && x > 6) {
			x = 6;//make sure ship does not go off grid
		}
		if (!sideways && y > 6) {
			y = 6;
		}

		while (checkGridForShip(newGrid, sideways, x, y, length)) {
			x = (int) (Math.random() * 10);//if another ships overlaps
			y = (int) (Math.random() * 10);//another position is generated

			if (sideways && x > 6) {
				x = 6;//make sure ship does not go off grid
			}
			if (!sideways && y > 6) {
				y = 6;
			}
		}

		if (sideways) {//enter ship into array sideways
			for (int i = 0; i < length; i++) {
				newGrid.playerArray[x + i][y] = shipId;
			}
		} else {//enter ship into array vertically
			for (int i = 0; i < length; i++) {
				newGrid.playerArray[x][y + i] = shipId;
			}
		}
	}

	//method is used to check the given grid position for any ships
	public static boolean checkGridForShip(Grid newGrid, boolean sideways, int x, int y, int length) {
		boolean isShipHere = false;
		if (sideways) {
			for (int i = 0; i < length; i++) {
				if (newGrid.playerArray[x + i][y] > 2) {
					isShipHere = true;//ship is here
				}
			}
		} else {
			for (int i = 0; i < length; i++) {
				if (newGrid.playerArray[x][y + i] > 2) {
					isShipHere = true;
				}
			}
		}
		return isShipHere;
	}

	//method is used to print out game grid every turn
	public static void drawGrid(Grid newGrid) {
		System.out.print("    ");
		for (int i = 0; i < 10; i++) {
			System.out.print("+---");
		}
		System.out.print("+\n    ");

		for (int i = 65; i < 75; i++) {//correct letters are printed A->J
			System.out.print("| " + (char) i + " ");
		}
		System.out.print("|\n");

		for (int j = 0; j < 10; j++) {
			System.out.print("----");
			for (int i = 0; i < 10; i++) {
				System.out.print("+---");
			}
			System.out.print("+");
			System.out.print("\n");
			if (j == 9) {
				System.out.print(" " + (j + 1) + " |");
			} else {
				System.out.print("  " + (j + 1) + " |");
			}
			for (int i = 0; i < 10; i++) {
				switch (newGrid.playerArray[j][i]) {
				case 0:
					System.out.print(" " + " " + " " + "+");
					break;
				case 1:
					System.out.print(" " + "H" + " " + "+");
					break;//will display 'H' when battleship is hit
				case 2:
					System.out.print(" " + "M" + " " + "+");
					break;//will display 'M' when shot is missed
				case 3:
					System.out.print(" " + " " + " " + "+");
					break;//hides battleships at beginning
				case 4:
					System.out.print(" " + " " + " " + "+");
					break;
				case 5:
					System.out.print(" " + " " + " " + "+");
					break;
				}

			}
			System.out.print("\n");
		}
		System.out.print("----");
		for (int i = 0; i < 10; i++) {
			System.out.print("+---");
		}
		System.out.print("+");
	}
}