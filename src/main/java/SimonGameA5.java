import java.net.Authenticator;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import swiftbot.SwiftBotAPI;
import swiftbot.Button;

public class SimonGameA5 {

    static SwiftBotAPI swiftBot;
    static int[][] colours = {
            {255, 0, 0}, // Red for A
            {0, 0, 255}, // Blue for B
            {0, 255, 0}, // Green for X
            {255, 255, 0} // Yellow for Y
        };

    public static void main(String[] args) throws InterruptedException {
        // initialize swiftBot
        try {
            swiftBot = new SwiftBotAPI();
        } catch (Exception e) {
            System.out.println("Failed to initialize SwiftBot.");
            e.printStackTrace();
            System.exit(1);
        }

        // dynamic array to store random color sequences for Simon game
        List<int[]> randomColourSequence = new ArrayList<>();

        int remainingChances = 3; // players initially have three chances to restart
        int score = 0; // current score
        int highestScore = 0; // the highest score
        int seconds = 5; // total count-down time
        
        String[] SimonAsciiArt = { // Define as array
                "                                                          ",
                " ____ ___ __  __  ___  _   _    ____    _    __  __ _____ ",
                "/ ___|_ _|  \\/  |/ _ \\| \\ | |  / ___|  / \\  |  \\/  | ____|",
                "\\___ \\| || |\\/| | | | |  \\| | | |  _  / _ \\ | |\\/| |  _|  ",
                " ___) | || |  | | |_| | |\\  | | |_| |/ ___ \\| |  | | |___ ",
                "|____|___|_|  |_|\\___/|_| \\_|  \\____/_/   \\_|_|  |_|_____|",
                "                                                          "
            };
        String[] gameOverAsciiArt = { // Define the AsciiArt array
			    "\u001B[31m", //  Color code for red
			    "                                                          ",
			    "  ____    _    __  __ _____    _____     _______ ____    ",
			    " / ___|  / \\  |  \\/  | ____|  / _ \\ \\   / | ____|  _ \\   ",
			    "| |  _  / _ \\ | |\\/| |  _|   | | | \\ \\ / /|  _| | |_) |  ",
			    "| |_| |/ ___ \\| |  | | |___  | |_| |\\ V / | |___|  _ <   ",
			    " \\____/_/   \\_|_|  |_|_____|  \\___/  \\_/  |_____|_| \\_\\  ",
			    "                                                          ",
			    "\u001B[0m" // Reset format
			};
        String[] seeYouAgainAscii = { // Define See you again Ascii Art as an array
    		    "                                                                                                             ",
    		    " ____  _____ _____  __   _____  _   _      _    ____    _    ___ _   _    ____ _   _    _    __  __ ____  _ ",
    		    "/ ___|| ____| ____| \\ \\ / / _ \\| | | |    / \\  / ___|  / \\  |_ _| \\ | |  / ___| | | |  / \\  |  \\/  |  _ \\| |",
    		    "\\___ \\|  _| |  _|    \\ V | | | | | | |   / _ \\| |  _  / _ \\  | ||  \\| | | |   | |_| | / _ \\ | |\\/| | |_) | |",
    		    " ___) | |___| |___    | || |_| | |_| |  / ___ | |_| |/ ___ \\ | || |\\  | | |___|  _  |/ ___ \\| |  | |  __/|_|",
    		    "|____/|_____|_____|   |_| \\___/ \\___/  /_/   \\_\\____/_/   \\_|___|_| \\_|  \\____|_| |_/_/   \\_|_|  |_|_|   (_)",
    		    "                                                                                                             "
    		};
        
        for (String line : SimonAsciiArt) {
            System.out.println(line); // Print out all lines
        }
        
        System.out.println("Welcome to the Simon Game!");
        System.out.println("Button A = Red (Top-left), Button B = Blue (Bottom-left), Button X = Green (Top-right), Button Y = Yellow (Bottom-right).");
        System.out.println("RULES:");
        System.out.println("1. The SwiftBot will light up in a random sequence.");
        System.out.println("2. Repeat the sequence by pressing the buttons in the correct order.");
        System.out.println("3. The sequence grows longer with each correct round.");
        System.out.println("4. Your score increases by 1 for every correct sequence.");
        System.out.println("5. The game ends if you press the wrong button.");
        System.out.println("Countdown starting...");
        for (int i = seconds; i > 0; i--) {
            System.out.println("Time remaining: " + i + " second(s)");
            Thread.sleep(1000); // Wait for 1 second
        }
        
        while (true) {
            System.out.println("Starting Simon Game...");
            randomColourSequence.clear(); // eliminate the list of color
            score = 0; // reset the score

            while (true) {
                // call the method of creating random colors
                int[] generatedColour = RandomUnderlight();

                // store the returned color into the sequence
                randomColourSequence.add(generatedColour);

                // show the completely sequence of the latest round
                System.out.println("\nRound " + (score + 1) + ": Simon says:");
                for (int[] colour : randomColourSequence) {
                    DisplayColour(colour); // display color
                }

                // check the user input if it is correct
                System.out.println("Your turn! Repeat the sequence:");
                if (generateUserInput(randomColourSequence) == null) {
                    // game over if the answer is wrong
                    System.out.println("Game Over! You made a mistake.");
                    System.out.println("Your final score: " + score);
                    for (String line : gameOverAsciiArt) {
	                    System.out.println(line); // Print out all the lines
	                }

                    // update the highest score
                    if (score > highestScore) {
                        highestScore = score;
                        System.out.println("Congratulations! New highest score: " + highestScore);
                    } else {
                        System.out.println("Highest score so far: " + highestScore);
                    }

                    if (score >= 5) {
                        // call the method of celebration
                        System.out.println("Celebrating your effort!");
                        celebrationDive(score);
                    }

                    // check if there are chances to restart
                    if (remainingChances > 0) {
                        if (askToRestart(remainingChances)) {
                            remainingChances--; // decline one chance
                            break; // jump this loop out and restart
                        } else {
                            System.out.println("Thank you for playing! Goodbye.");
                            for (String line : seeYouAgainAscii) {
			                    System.out.println(line); // Print out all the lines
			                }
                            return; // quit
                        }
                    } else {
                        System.out.println("No more chances left. Thank you for playing!");
                        for (String line : seeYouAgainAscii) {
		                    System.out.println(line); // Print out all the lines
		                }
                        return; // quit
                    }
                }

                // user input correct, add one score and enter the nest round
                score++;
                System.out.println("Correct! Your current score is: " + score);
                System.out.println("Get ready for the next round.");
                System.out.println("Highest score so far: " + highestScore);
            }
        }
    }

    public static int[] RandomUnderlight() {

        try {
            Random random = new Random();
            int randomIndex = random.nextInt(colours.length); // randomly select a color
            int[] rgb = colours[randomIndex]; // get the corresponding color value

            // print the color
            System.out.println("Generated RGB: [" + rgb[0] + ", " + rgb[1] + ", " + rgb[2] + "]");

            return rgb; // return the generated color
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("ERROR: Failed to generate a random color");
            System.exit(5); // exit the program
            return null; 
        }
    }

    
    public static void DisplayColour(int[] colour) throws InterruptedException {
        swiftBot.fillUnderlights(colour); //display color
        System.out.println("Displaying: [" + colour[0] + ", " + colour[1] + ", " + colour[2] + "]");
        Thread.sleep(500); // keep displaying color
        swiftBot.disableUnderlights(); // turn off the color
        Thread.sleep(200); // add a short gap
    }

    // generate user input and validate
    public static List<int[]> generateUserInput(List<int[]> targetSequence) throws InterruptedException {
        List<int[]> userInputSequence = new ArrayList<>(); // dynamic array to store user input colors
        int inputCount = targetSequence.size(); // the number of times the player needs to enter is equal to the target sequence length


        System.out.println("Please press buttons on the SwiftBot (A, B, X, Y):");

        try {
            // get variables of user input
            final char[] lastPressedButton = { '\0' }; // using an array so that the anonymous function can modify its value

            // enable the button and bind the callback function
            swiftBot.enableButton(Button.A, () -> {
                System.out.println("Button A Pressed.");
                lastPressedButton[0] = 'A';
            });

            swiftBot.enableButton(Button.B, () -> {
                System.out.println("Button B Pressed.");
                lastPressedButton[0] = 'B';
            });

            swiftBot.enableButton(Button.X, () -> {
                System.out.println("Button X Pressed.");
                lastPressedButton[0] = 'X';
            });

            swiftBot.enableButton(Button.Y, () -> {
                System.out.println("Button Y Pressed.");
                lastPressedButton[0] = 'Y';
            });

            // poll user actions by input count
            for (int i = 0; i < inputCount; i++) {
                System.out.println("Waiting for input " + (i + 1) + " of " + inputCount + "...");

                // waiting for the user to press a valid button
                while (lastPressedButton[0] == '\0') {
                    Thread.sleep(50); // prevent high-frequency polling from occupying resources
                }

                // map buttons to colors
                int[] selectedColor;
                switch (lastPressedButton[0]) {
                    case 'A':
                        selectedColor = colours[0];
                        break;
                    case 'B':
                        selectedColor = colours[1];
                        break;
                    case 'X':
                        selectedColor = colours[2];
                        break;
                    case 'Y':
                        selectedColor = colours[3];
                        break;
                    default:
                        System.out.println("Invalid button detected. Please press A, B, X, or Y.");
                        i--; // retry current input
                        lastPressedButton[0] = '\0'; // reset the condition of buttons
                        continue;
                }

                // verify that user input is correct
                if (!isCorrect(selectedColor, targetSequence.get(i))) {
                    System.out.println("Wrong input! Game Over.");
                    swiftBot.disableAllButtons(); // disable all buttons
                    return null; // when the input is wrong, return null
                }

                // add to the user input sequence
                userInputSequence.add(selectedColor);
                lastPressedButton[0] = '\0'; // reset the condition of buttons
            }

            System.out.println("User input sequence recording completed.");
            swiftBot.disableAllButtons(); // disable all buttons
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error in capturing button press. Try again.");
            swiftBot.disableAllButtons(); // disable all buttons
            return null;
        }

        return userInputSequence; // return the completely sequence of user input
    }
    
   

    public static boolean isCorrect(int[] inputColor, int[] targetColor) {
        if (inputColor.length != targetColor.length) return false;
        for (int i = 0; i < inputColor.length; i++) {
            if (inputColor[i] != targetColor[i]) return false;
        }
        return true;
    }

    public static boolean askToRestart(int remainingChances) {
        System.out.println("You have " + remainingChances + " chance(s) remaining.");
        System.out.println("Do you want to restart the game? (yes/no):");

        java.util.Scanner scanner = new java.util.Scanner(System.in);
        String response = scanner.nextLine().trim().toLowerCase();

        return response.equals("yes");
    }

    	// Test Dive
	public static void celebrationDive(int userScore) throws InterruptedException {
 		int speed;
 			
 		// Speed based on user score
 		if (userScore >=10) {
 			speed = 100;
 		}
 		else {
 			speed = userScore * 10;
 		}
 			
 		try {
 			// Random RGBY blink
 			celebrationBlink();
 			
 			// Display Celebration speed
 			System.out.println("Celebration dive V at speed: " + speed);
 			
 			// \first leg of the V, rotate to the right
 			swiftBot.move(100,-100, speed);
 			// Forward
 			swiftBot.move(speed, speed, Time(150, speed));
 			// Reverse
 			swiftBot.move(-speed, -speed, Time(150, speed));
 			Thread.sleep(500);
 			swiftBot.move(-100, 100, speed); // original angle
 			Thread.sleep(500);
 			swiftBot.move(-100, 100, speed); // rotate anticlockwise for second leg
 			swiftBot.move(-100, 100, speed);
 			Thread.sleep(500);

 			// /second leg of the v 
 			swiftBot.move(speed, speed, Time(150, speed)); // Forward
 			swiftBot.move(-speed, -speed, Time(150, speed)); // Reverse
 			Thread.sleep(500);
 			swiftBot.move(100, -100, speed); // Anticlockwise
 			Thread.sleep(500);
 			
 			celebrationBlink();
 			System.out.println("Celebration dive complete :)");
 			
		} catch (Exception e) {
	        e.printStackTrace();
	        System.out.println("ERROR: Failed to celebtate!");
	        System.exit(5); // exit the program
	        return; 
	    }
 	}
 		
 	public static void celebrationBlink() throws InterruptedException {

 		Random random = new Random();
 		for (int i = 0; i < colours.length; i++) {
 			int randomIndex = random.nextInt(colours.length);
 			int[] rgb = colours[randomIndex];
 			swiftBot.fillUnderlights(rgb);
 			Thread.sleep(300);
 				
 				
 		}
 		swiftBot.disableUnderlights();
 	}
 		
 	public static int Time(int distanceCm, int speed) {
 		// T=D/S with speed conversion
 		return (distanceCm * 1000) / speed;
 			
 			
 	}
}