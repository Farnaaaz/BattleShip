//***********************************************************************************************************************************
// Assignment 4 
// Written by: Farnaz Zaveh, ID: 40032389
// For COMP 248-R - Fall 2020  
//
// This program uses an array of object and includes four classes and one driver class to simulate the battleship game between a human user and the computer
// The Driver class start the program by running a method that asks for users input to place their pieces, then calls other method to place computer's pieces
// then it asks for user's input to fire on a rocket and computer't rocket will be chosen randomly. Every time a rocket is fired, the fired-on position will be check for the placed piece and its owner
// if a rocket hit a ship it will be sunk and if i hits a grenade the opponent get two times their turn. They fire rockets until a user loses all their ships. 
//************************************************************************************************************************************

import java.util.Scanner;
import java.util.Arrays;
import java.util.Random;

public class Driver {
	
	//declare the variables to be used later
   private Game game;  
   private Scanner keyboard;
   private String[][] gameBoard = new String[8][8]; 

     
   public Driver() { 
   game = new Game(); //Initiate the Game object
   this.createGameBoard(); // call the method to initiate the grid.
   }
      
   public static void main(String[] args) { //Driver class to test the complex class
	   Driver run = new Driver(); // declare the run object 
       run.start(); // call the start method to start the game
   }
   
   //fill the 2D array of String with "_"
   private void createGameBoard() {
       for (String[] pb : this.gameBoard) {
           Arrays.fill(pb, "_");
       }
   }

   //start the game
   // prompt the user, get ready,  set the pieces, fires rockets
   private void start() {
       keyboard = new Scanner(System.in); //declare the scanner object
       System.out.println("Hi, let’s play Battleship!\n"); //greetings
       
       this.initUserPieces();// place the user ships and grenade
       game.getReady();// initiate computer's ship and grenade AND Initiate the ship and grenade counters for the game 
       System.out.println("OK, the computer placed its ships and grenades at random. Let’s play."); //prompt the user
       System.out.println();
       
       while (!game.isDone()) {// until the game is done(number of ships of one player = 0, keep calling position
           while (game.getCurrentPlayersTurn() != 0) {// while it's the current player's turn
               System.out.print("Position of " + (game.getCurrentOwner() == Owner.COMPUTER ? "Computer's" : "your") + " rocket: "); //print out the message to call a position (for both C and User)
               
               int[] position = { 0, 0 }; //declare and initialize the called position
               
               if (game.getCurrentOwner() == Owner.COMPUTER) {//if the Owner is computer
                   Random r = new Random(); //declare the random object 
                   position[0] = r.nextInt(8); //assign the random number (between 0 and 7) to position's first element
                   char firstLetter = (char) (position[0] + (int) ('A')); // convert it to a letter (between A and H)
                   position[1] = r.nextInt(8); //assign  the random number (between 0 and 7) to position's second element
                   System.out.println(firstLetter + "" + (position[1] + 1)); //print out the C's rocket position
               } 
               else { //if it's the user's turn
                   String positionStr = keyboard.nextLine(); //read the user input
                   char[] chars = positionStr.toUpperCase().toCharArray(); //assign to an char array regarding lower/upper case
                   if (this.isOutOfRange(chars)) {//check if the position is outside the grid 
                       System.out.println("Sorry, coordinates outside the grid. Try again."); //if outside, print out the error message
                       continue;
                   }
                   position = castCharToInt(positionStr.toUpperCase().toCharArray()); //convert String to array of int
               }

               Grid firedOnCell = game.shoot(position[0], position[1]); //allocate the position to the grid (as the rocket hits)
               if (!firedOnCell.isAlreadyCalled()) // if the position is not yet called
               {
                   switch (firedOnCell.getType()) //check 3 types of cells in the grid
                   {
                   case SHIP: //if the cell contains a ship, check its owner
                       this.gameBoard[position[0]][position[1]] = (firedOnCell.getOwner() == Owner.COMPUTER ? "S" : "s"); //'s':the sunk human ship, 'S':the sunk Computer ship
                       System.out.println("Ship hit."); //print out the shoot result, regardless of its Owner
                       break; 
                   case GRENADE://if the cell contains a grenade, check its owner
                       this.gameBoard[position[0]][position[1]] = (firedOnCell.getOwner() == Owner.COMPUTER ? "G" : "g"); //'g':exploded Human grenade, 'G':exploded Computer grenade
                       System.out.println("Boom! Grenade.");//print out the shoot result, regardless of its Owner
                       break;
                   case NOTHING:
                       this.gameBoard[position[0]][position[1]] = "*"; // if there is nothing in the cell
                       System.out.println("Nothing.");//print out the shoot result
                       break;
                   }
               } 
               else { //print this out when the point is already called
                   System.out.println("Position already called."); 
               }
               if (!game.isDone()) { // if the game is not over
                   this.printCurrentStates(); //print out the grid
               }
           }//end of inner while loop, currentPlayers turn
           
           game.switchPlayer();//switch players

       }//End of while loop (when number of ships of one player = 0)
       
       //print out the winner
       System.out.println(game.getWinner() == Owner.COMPUTER ? "Computer wins!" : "You win!");
       this.printCurrentStates(); //print the grid status
       this.keyboard.close(); //close the scanner
       System.out.println("\nWell played! Bye");
   }
   
      
   //Initiate user's ship and grenade
   private void initUserPieces() { 
       this.placeShips(); //call the method to set the user's ships
       this.placeGrenades(); //call the method to set the user's Grenade
   }

   //Place user's ships, according the position offered by user.
   private void placeShips() {
       int havePlaced = 0; //declare and initiate number of the placed pieces
       
       while (havePlaced < Game.TOTAL_NUMBER_OF_SHIP) { // while all ships are placed
           System.out.print("Enter the coordinates of your ship #" + (++havePlaced) + ": "); //prompt the user
           String positionStr = keyboard.nextLine(); //read the input
           char[] chars = positionStr.toUpperCase().toCharArray(); //convert it to array of char
           
           if (this.isOutOfRange(chars)) { //if the position is outside the grid
               System.out.println("Sorry, coordinates outside the grid. Try again."); //print out the error message
               --havePlaced; // decrease the counter, since it was not placed
               continue;
           } 
           else if (this.isTaken(chars)) { //if it's been taken already
               System.out.println("Sorry, coordinates already used. Try again."); //print out the error message
               --havePlaced; // decrease the counter, since it was not placed
               continue;
           } 
           else { //if the position is inside the grid or if it's not been taken 
               int[] position = castCharToInt(chars); // assign the char input into position, as an array of int
               game.setGrid(new Grid(position[0], position[1], Type.SHIP, Owner.USER)); // update the corresponding type and Owner
           }
       }
       System.out.println();
   }
   
   //Place user's grenades, according the position offered by user.
   private void placeGrenades() {
       int havePlaced = 0; //declare and initiate number of the placed pieces
       
       while (havePlaced < Game.TOTAL_NUMBER_OF_GRENADE) { // until all grenades are placed
           System.out.print("Enter the coordinates of your grenade #" + (++havePlaced) + ": "); //prompt the user
           String positionStr = keyboard.nextLine(); //read the input
           char[] chars = positionStr.toUpperCase().toCharArray(); //convert it to array of char
           
           if (this.isOutOfRange(chars)) { //if the position is outside the grid
               System.out.println("Sorry, coordinates outside the grid. Try again."); //print out the error message
               --havePlaced; // decrease the counter, since it was not placed
               continue;
           } 
           else if (this.isTaken(chars)) { //if it's been already taken
               System.out.println("Sorry, coordinates already used. Try again."); //print out the error message
               --havePlaced; // decrease the counter, since it was not placed
               continue;
           } 
           else { //if the position is inside the grid or if it's not been taken 
               int[] position = castCharToInt(chars); // assign the char input into position, as an array of int
               game.setGrid(new Grid(position[0], position[1], Type.GRENADE, Owner.USER)); // update the corresponding type and player
           }
       }
       System.out.println();
   }

   //if the input is inside the grid range
   private boolean isOutOfRange(char[] chars) { //@param char
       if ('A' <= chars[0] && chars[0] <= 'H' && 1 <= Character.getNumericValue(chars[1]) && Character.getNumericValue(chars[1]) <= 8) {
           return false; //@return false
       } 
       else {
           return true;  //@return false, if out of range.
       }
   }

   //Check whether the target position is already used.
   private boolean isTaken(char[] chars) {     //@param chars
       int[] position = castCharToInt(chars); 
       return game.isTaken(position[0], position[1]);  //@return true, if taken; otherwise, false.
   }

    
   //Convert input array of char to an array of integers
   private int[] castCharToInt(char[] chars) {
       int x = (int) (chars[0]) - (int) ('A'); //@param chars
       int y = Character.getNumericValue(chars[1]) - 1;
       return new int[] { x, y }; //@return the position in array of int
   }

   
   //Print out the current grid
   private void printCurrentStates() {
       for (int i = 0; i < 8; i++) {  //nested loop to visit rows and columns of the grid
           for (int j = 0; j < 8; j++) {
               System.out.print(gameBoard[i][j] + "\t"); //print the cells of the play board where the pieces are placed
           }
           System.out.println();
       }
   }

}