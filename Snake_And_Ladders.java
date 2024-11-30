import java.util.Scanner;
import java.util.Random;

public class Snake_And_Ladders 
{
	// Array representing the game board with 100 squares
	public static String[] gameBoard = new String[100];
	
	// Keep track of how many times each player wins
    public static int playerXWin = 0;
    public static int playerOWin = 0;
    public static int playerZWin = 0;
	
	// Player progress variables to track the positions of each player
	public static int playerXProgress = 0; // Position of Player X on the board
	public static int playerOProgress = 0; // Position of Player O on the board
	public static int playerZProgress = 0; // Position of Player Z on the board
	
	// Keep track of whether the game has ended or not
    public static boolean gameEnd = false;
	
    // Create an instance of the Scanner class
    private static Scanner scanner = new Scanner(System.in);
    
    // Create an instance of the Random class
    private static Random random = new Random();
        
	public static void main(String[] args) 
	{
		// Display the main menu
		MAIN_MENU();
		
		// Close the scanner after the entire program has finished executing
        scanner.close();
	}
	
	public static void MAIN_MENU()
	{
		// Display the game's title
    	DISPLAY_TITLE();
        
        // Prompt the user to choose between tutorial and play
        System.out.print("\n\t\t\t\t\t\tWelcome to Snakes & Ladders!!!\n\n\t\t\t\t\t      Choose one of the options below: \n\t\t\t\t\t \t\t - Tutorial\n\t\t\t\t\t\t\t - Play\n\n");
    
        // Read the user's choice
        String choice = scanner.nextLine().toLowerCase().trim();
    
        // Validate user input
        while (!(choice.equals("tutorial") || choice.equals("play")))
        {
            System.out.print("\nInvalid choice. Please choose one of the options. ");
            choice = scanner.nextLine().toLowerCase().trim();
        }
    
        // Call the appropriate method based on user's choice
        if (choice.equals("tutorial")) 
        {
        	CLEAR_SCREEN();
        	TUTORIAL();
        } 
        if (choice.equals("play"))
        {
        	CLEAR_SCREEN();
            PLAY_GAME();
        }
	}
	
	public static void TUTORIAL()
	{
	    // Display game rules and board layout
	    System.out.println("\nHow to Play Snakes & Ladders:\n");
	    System.out.println("- Snakes and Ladders is a classic board game played on a 10x10 grid with 100 squares.");
	    System.out.println("- Players take turns rolling a die to advance their pieces along the board.");
	    System.out.println("- If a player lands on a square with the bottom of a ladder (L), they climb up to the square at the top of the ladder (the number corresponding to the L) .");
	    System.out.println("- If a player lands on a square with the head of a snake (S), they slide down to the square at the snake's tail (the number corresponding to the S) .");
	    System.out.println("- The first player to reach or exceed square 100 wins the game.");
	    
	    System.out.print("\nPress Enter to return to the main menu. ");
	    scanner.nextLine();
	    CLEAR_SCREEN();
	    MAIN_MENU();
	}
	
	public static void PLAY_GAME()
	{
		for (int i = 0; i < gameBoard.length; i++)
			gameBoard[i] = String.valueOf(i + 1);
		
		while (!gameEnd) 
        {
            TAKE_TURN("X"); // Player X's turn

            if (gameEnd) break; // Exit if the game has ended

            TAKE_TURN("O"); // Player O's turn
            
            if (gameEnd) break; // Exit if the game has ended
            
            TAKE_TURN("Z"); // Player Z's turn
        }
		
		System.out.println("\nWould you like to play again? Yes or no? ");
        String wantPlayAgain = scanner.nextLine().toLowerCase().trim();
       
        // Validate User Input
        while (!(wantPlayAgain.charAt(0) == 'y' || wantPlayAgain.charAt(0) == 'n'))
        {
           System.out.println("Please choose yes or no: ");
           wantPlayAgain = scanner.nextLine().toLowerCase().trim();
        }
       
        if (wantPlayAgain.charAt(0) == 'y')
        {
            RESET_GAME();
            CLEAR_SCREEN();
            MAIN_MENU();
        }
        else
        {
        	DISPLAY_STATISTICS();
            System.out.println("\nHave a great day!");
            System.exit(0);        
        }
    }
	
	public static void TAKE_TURN(String playerSymbol) 
	{
		// Displays the board each turn
	    DISPLAY_BOARD();
	    
	    System.out.println("\nPlayer " + playerSymbol + ": Press enter to roll the dice.");
	    scanner.nextLine();

	    // Dice roll between 1 and 6
	    int roll = random.nextInt(6) + 1;

	    System.out.println("\nPlayer " + playerSymbol + ": rolled a " + roll + ". Press enter to continue.");
	    scanner.nextLine();
	    
	    // Saves the players previous and new position to use throughout the game
	    int previousPosition = 0;
	    int newPosition = 0;

	    // Update player progress based on symbol
	    switch (playerSymbol) 
	    {
	        case "X":
	            previousPosition = playerXProgress;
	            playerXProgress += roll;
	            newPosition = playerXProgress;
	            break;
	        case "O":
	            previousPosition = playerOProgress;
	            playerOProgress += roll;
	            newPosition = playerOProgress;
	            break;
	        case "Z":
	            previousPosition = playerZProgress;
	            playerZProgress += roll;
	            newPosition = playerZProgress;
	            break;
	    }

	    // Ensure the new position is within the board bounds
	    if (newPosition >= gameBoard.length) 
	    	newPosition = gameBoard.length - 1;

	    // Check for ladders or snakes
	    newPosition = CHECK_FOR_SNAKES_OR_LADDERS(newPosition);

	    // Update the player's progress based on ladders or snakes
	    switch (playerSymbol) 
	    {
	        case "X":
	            playerXProgress = newPosition;
	            break;
	        case "O":
	            playerOProgress = newPosition;
	            break;
	        case "Z":
	            playerZProgress = newPosition;
	            break;
	    }

	    // Remove the player symbol from the previous position
	    if (previousPosition > 0 && previousPosition <= gameBoard.length) 
	    {
	        gameBoard[previousPosition - 1] = gameBoard[previousPosition - 1].replace(playerSymbol, "").trim();
	        
	        // If after removing the player's symbol, the space is empty, revert to the original number
	        if (gameBoard[previousPosition - 1].isEmpty()) 
	            gameBoard[previousPosition - 1] = String.valueOf(previousPosition);
	    }

	    // Add player to the new position
	    if (newPosition < gameBoard.length) 
	    {
	        if (gameBoard[newPosition - 1].equals(String.valueOf(newPosition + 1))) 
	            gameBoard[newPosition - 1] = playerSymbol;
	        else 
	            gameBoard[newPosition - 1] += " " + playerSymbol; // Add player symbol without removing others
	    }

	    // Check for win condition
	    if (newPosition >= gameBoard.length) 
	    {
	        System.out.println("Player " + playerSymbol + ": Wins!!!");
	        
	        switch (playerSymbol) 
		    {
		        case "X":
		        	playerXWin++;
		            break;
		        case "O":
		        	playerOWin++;
		            break;
		        case "Z":
		        	playerZWin++;
		            break;
		    }
	        
	        gameEnd = true;
	    }

	    CLEAR_SCREEN();
	}

    private static int CHECK_FOR_SNAKES_OR_LADDERS(int position) 
    {
        // Check for ladders
        switch (position) 
        {
            case 1:
            	MESSAGE_DISPLAY("ladder", 1, 38);
                return 38;
            case 4:
            	MESSAGE_DISPLAY("ladder", 4, 14);
                return 14;
            case 9:
            	MESSAGE_DISPLAY("ladder", 9, 31);
                return 31;
            case 21:
            	MESSAGE_DISPLAY("ladder", 21, 42);
                return 42;
            case 28: 
            	MESSAGE_DISPLAY("ladder", 28, 84);
                return 84;
            case 36:
            	MESSAGE_DISPLAY("ladder", 36, 44);
                return 44;
            case 51: 
            	MESSAGE_DISPLAY("ladder", 51, 67);
                return 67;
            case 71:
            	MESSAGE_DISPLAY("ladder", 71, 91);
                return 91;
            case 80: 
            	MESSAGE_DISPLAY("ladder", 80, 100);
                return 100;
        }

        // Check for snakes
        switch (position) 
        {
            case 16:
            	MESSAGE_DISPLAY("snake", 16, 6);
                return 6;
            case 48:
            	MESSAGE_DISPLAY("snake", 48, 30);
                return 30;
            case 64:
            	MESSAGE_DISPLAY("snake", 64, 60);
                return 60;
            case 93:
            	MESSAGE_DISPLAY("snake", 93, 68);
                return 68;
            case 95:
            	MESSAGE_DISPLAY("snake", 95, 24);
                return 24;
            case 97:
            	MESSAGE_DISPLAY("snake", 97, 76);
                return 76;
            case 98:
            	MESSAGE_DISPLAY("snake", 98, 78);
                return 78;
        }

        return position; // No change in position if not a ladder or snake
    }
    
    public static void MESSAGE_DISPLAY(String type, int start, int end)
    {
    	System.out.println("\nYou landed on a " + type + "! You now go from " + start + " to " + end + ". Press enter to continue. ");
    	scanner.nextLine();
    }
	
    public static void RESET_GAME()
    {
        // Reset the game board to its initial state
        for (int i = 0; i < gameBoard.length; i++)
            gameBoard[i] = String.valueOf(i + 1);

        // Reset player progress
        playerXProgress = 0;
        playerOProgress = 0;
        playerZProgress = 0;

        // Reset game end flag
        gameEnd = false;
    }
    
    public static void CLEAR_SCREEN()
	{
		for (int i = 0; i < 75; i++)
			System.out.println();
	}
    
    // Display game statistics
    public static void DISPLAY_STATISTICS() 
    {
        // Display player win counts and tie counts
        System.out.println("\nPlayer X win count: " + playerXWin + "\nPlayer O win count: " + playerOWin + "\nPlayer Z win count: " + playerZWin);
    }
	
	public static void DISPLAY_TITLE()
	{
		System.out.println("\t     ::::::::    ::::    :::       :::       ::::    :::   ::::::::::    ::::::::   ");
    	System.out.println("\t    :+:    :+:   :+:+:   :+:    :+:   :+:    :+:+   :+:    :+:          :+:    :+:  ");
    	System.out.println("\t    +:+          :+:+:+  +:+   +:+     +:+   :+:+  +:+     +:+          +:+         ");
    	System.out.println("\t     #++:++#+    +#+ +:+ +#+   +#++:++#++:   +#+ +:+       +#++:++#      #++:++#+   ");
    	System.out.println("\t           +#+   +#+  +#+#+#   +#+     +#+   +#+  +#+      +#+                 +#+  ");
    	System.out.println("\t    #+#    #+#   #+#   #+#+#   #+#     #+#   #+#   #+#     #+#          #+#    #+#  ");
    	System.out.println("\t     ########    ###    ####   ###     ###   ###    ###    ##########    ########   ");
    	System.out.println();
    	System.out.println("  :::::::     :::              :::       :::::::::    :::::::::    ::::::::::   :::::::::     ::::::::     ");
    	System.out.println(" :+:   :+:    :+:           :+:   :+:    :+:    :+:   :+:    :+:   :+:          :+:    :+:   :+:    :+:    ");
    	System.out.println(" +:+          +:+          +:+     +:+   +:+    +:+   +:+    +:+   +:+          +:+    +:+   +:+           ");
    	System.out.println("  ::::::::    +#+          +#++:++#++:   +#+    +:+   +#+    +:+   +#++:++#     +#++:++#+     #++:++#+     ");
    	System.out.println(" +#+    +#+   #+#          +#+     +#+   +#+    +#+   +#+    +#+   +#+          +#+    +#+          +#+    ");
    	System.out.println(" #+#    #+#   #+#    #+#   #+#     #+#   #+#    #+#   #+#    #+#   #+#          #+#    #+#   #+#    #+#    ");
    	System.out.println("  #########    ########    ###     ###   #########    #########    ##########   ###    ###    ########     ");
	}
	
	public static void DISPLAY_BOARD() 
	{
	    System.out.println(" _________________________________________________________________________________________________________________________________ ");
	    System.out.println("|            |            |            |            |            |            |            |            |            |           | ");
	    System.out.println("|            |            |   (S 78)   |   (S 76)   |            |   (S 24)   |            |   (S 68)   |            |           | ");
	    System.out.println("|    " + gameBoard[99] + "     |     " + gameBoard[98] + "     |     " + gameBoard[97] + "     |     " + gameBoard[96] + "     |     " + gameBoard[95] + "     |     " + gameBoard[94] + "     |     " + gameBoard[93] + "     |     " + gameBoard[92] + "     |     " + gameBoard[91] + "     |     " + gameBoard[90] + "    | ");
	    System.out.println("|            |            |            |            |            |            |            |            |            |           | ");
	    System.out.println("|____________|____________|____________|____________|____________|____________|____________|____________|____________|___________| ");
	    System.out.println("|            |            |            |            |            |            |            |            |            |           | ");
	    System.out.println("|            |            |            |            |            |            |            |            |            |           | ");
	    System.out.println("|     " + gameBoard[80] + "     |     " + gameBoard[81] + "     |     " + gameBoard[82] + "     |     " + gameBoard[83] + "     |     " + gameBoard[84] + "     |     " + gameBoard[85] + "     |     " + gameBoard[86] + "     |     " + gameBoard[87] + "     |     " + gameBoard[88] + "     |     " + gameBoard[89] + "    | ");
	    System.out.println("|            |            |            |            |            |            |            |            |            |           | ");
	    System.out.println("|____________|____________|____________|____________|____________|____________|____________|____________|____________|___________| ");
	    System.out.println("|            |            |            |            |            |            |            |            |            |           | ");
	    System.out.println("|   (L 100)  |   (S 19)   |            |            |            |            |            |            |            |   (L 91)  | ");
	    System.out.println("|     " + gameBoard[79] + "     |     " + gameBoard[78] + "     |     " + gameBoard[77] + "     |     " + gameBoard[76] + "     |     " + gameBoard[75] + "     |     " + gameBoard[74] + "     |     " + gameBoard[73] + "     |     " + gameBoard[72] + "     |     " + gameBoard[71] + "     |     " + gameBoard[70] + "    | ");
	    System.out.println("|            |            |            |            |            |            |            |            |            |           | ");
	    System.out.println("|____________|____________|____________|____________|____________|____________|____________|____________|____________|___________| ");
	    System.out.println("|            |            |            |            |            |            |            |            |            |           | ");
	    System.out.println("|            |            |            |    (S 60)  |            |            |            |            |            |           | ");
	    System.out.println("|     " + gameBoard[60] + "     |     " + gameBoard[61] + "     |     " + gameBoard[62] + "     |     " + gameBoard[63] + "     |     " + gameBoard[64] + "     |     " + gameBoard[65] + "     |     " + gameBoard[66] + "     |     " + gameBoard[67] + "     |     " + gameBoard[68] + "     |     " + gameBoard[69] + "    | ");
	    System.out.println("|            |            |            |            |            |            |            |            |            |           | ");
	    System.out.println("|____________|____________|____________|____________|____________|____________|____________|____________|____________|___________| ");
	    System.out.println("|            |            |            |            |            |            |            |            |            |           | ");
	    System.out.println("|            |            |            |            |            |            |            |            |            |   (L 67)  | ");
	    System.out.println("|     " + gameBoard[59] + "     |     " + gameBoard[58] + "     |     " + gameBoard[57] + "     |     " + gameBoard[56] + "     |     " + gameBoard[55] + "     |     " + gameBoard[54] + "     |     " + gameBoard[53] + "     |     " + gameBoard[52] + "     |     " + gameBoard[51] + "     |     " + gameBoard[50] + "    | ");
	    System.out.println("|            |            |            |            |            |            |            |            |            |           | ");
	    System.out.println("|____________|____________|____________|____________|____________|____________|____________|____________|____________|___________| ");
	    System.out.println("|            |            |            |            |            |            |            |            |            |           | ");
	    System.out.println("|            |            |            |            |            |            |            |   (S 30)   |            |           | ");
	    System.out.println("|     " + gameBoard[40] + "     |     " + gameBoard[41] + "     |     " + gameBoard[42] + "     |     " + gameBoard[43] + "     |     " + gameBoard[44] + "     |     " + gameBoard[45] + "     |     " + gameBoard[46] + "     |     " + gameBoard[47] + "     |     " + gameBoard[48] + "     |     " + gameBoard[49] + "    | ");
	    System.out.println("|            |            |            |            |            |            |            |            |            |           | ");
	    System.out.println("|____________|____________|____________|____________|____________|____________|____________|____________|____________|___________| ");
	    System.out.println("|            |            |            |            |            |            |            |            |            |           | ");
	    System.out.println("|            |            |            |            |   (L 44)   |            |            |            |            |           | ");
	    System.out.println("|     " + gameBoard[39] + "     |     " + gameBoard[38] + "     |     " + gameBoard[37] + "     |     " + gameBoard[36] + "     |     " + gameBoard[35] + "     |     " + gameBoard[34] + "     |     " + gameBoard[33] + "     |     " + gameBoard[32] + "     |     " + gameBoard[31] + "     |     " + gameBoard[30] + "    | ");
	    System.out.println("|            |            |            |            |            |            |            |            |            |           | ");
	    System.out.println("|____________|____________|____________|____________|____________|____________|____________|____________|____________|___________| ");
	    System.out.println("|            |            |            |            |            |            |            |            |            |           | ");
	    System.out.println("|   (L 42)   |            |            |            |            |            |            |   (L 84)   |            |           | ");
	    System.out.println("|     " + gameBoard[20] + "     |     " + gameBoard[21] + "     |     " + gameBoard[22] + "     |     " + gameBoard[23] + "     |     " + gameBoard[24] + "     |     " + gameBoard[25] + "     |     " + gameBoard[26] + "     |     " + gameBoard[27] + "     |     " + gameBoard[28] + "     |     " + gameBoard[29] + "    | ");
	    System.out.println("|            |            |            |            |            |            |            |            |            |           | ");
	    System.out.println("|____________|____________|____________|____________|____________|____________|____________|____________|____________|___________| ");
	    System.out.println("|            |            |            |            |            |            |            |            |            |           | ");
	    System.out.println("|            |            |            |            |    (S 6)   |            |            |            |            |           | ");
	    System.out.println("|     " + gameBoard[19] + "     |     " + gameBoard[18] + "     |     " + gameBoard[17] + "     |     " + gameBoard[16] + "     |     " + gameBoard[15] + "     |     " + gameBoard[14] + "     |     " + gameBoard[13] + "     |     " + gameBoard[12] + "     |     " + gameBoard[11] + "     |     " + gameBoard[10] + "    | ");
	    System.out.println("|            |            |            |            |            |            |            |            |            |           | ");
	    System.out.println("|____________|____________|____________|____________|____________|____________|____________|____________|____________|___________| ");
	    System.out.println("|            |            |            |            |            |            |            |            |            |           | ");
	    System.out.println("|  (L 38)    |            |            |  (L 14)    |            |            |            |            |  (L 31)    |           | ");
	    System.out.println("|     " + gameBoard[0] + "      |     " + gameBoard[1] + "      |     " + gameBoard[2] + "      |     " + gameBoard[3] + "      |     " + gameBoard[4] + "      |     " + gameBoard[5] + "      |     " + gameBoard[6] + "      |     " + gameBoard[7] + "      |     " + gameBoard[8] + "      |     " + gameBoard[9] + "    | ");
	    System.out.println("|            |            |            |            |            |            |            |            |            |           | ");
	    System.out.println("|____________|____________|____________|____________|____________|____________|____________|____________|____________|___________| ");
	}
}