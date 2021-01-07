//***************************************************************************************************************
// Assignment 4 
// Written by: Farnaz Zaveh, ID: 40032389
// For COMP 248-R - Fall 2020  
//
//This program uses an array of object and includes four classes and one driver class to simulate the battleship game between a human user and the computer.
// This class contains the methods that place computer pieces, method that fire the rocket and check if it hits a piece, 
// method that switch players turn, method that check if the game is over and the one that gets the winner.  
//***************************************************************************************************************

import java.util.Random;

public class Game {
    
   private Grid[][] cells = new Grid[8][8]; // The game board grid
   public static final int TOTAL_NUMBER_OF_SHIP = 6; //Total number of ship that user owns
   public static final int TOTAL_NUMBER_OF_GRENADE = 4; //Total number of grenade that user owns
   private int numberOfShipOfComputer; //The number of ship that computer possesses now
   private int numberOfShipOfUser; //The number of ship that user possesses now
   private int currentPlayersTurn; //How many times that the current player can call a position 
   private int nextPlayersTurn; //How many times that the following player can call a position
   private Owner currentOwner; //Whose turn to play

   public Game() { //Initiate all cells in the game board
       this.createCells(); 
   }
   
   //accessor method for Grid
   public Grid[][] getGrid() {
       return cells; //@return cells of type Grid
   }
   
   //accessor method for currentPlayer
   public Owner getCurrentOwner() {
       return currentOwner;
   }
   //mutator method for currentOwner
   public void setCurrentOwner(Owner currentOwner) {
       this.currentOwner = currentOwner;
   }
   
   //accessor method for currentPlayersTurn
   public int getCurrentPlayersTurn() {
       return currentPlayersTurn;
   }
   
   
   //Place all computer pieces. Initiate the counters
   public void getReady() {
       this.placeComputerPieces();
       this.numberOfShipOfComputer = TOTAL_NUMBER_OF_SHIP;
       this.numberOfShipOfUser = TOTAL_NUMBER_OF_SHIP; 
       this.currentOwner = Owner.USER; //user starts the play by firing the first rocket
       this.currentPlayersTurn = 1;
       this.nextPlayersTurn = 1;
   }

    
  //Call a position for the the rocket to fire on
   public Grid shoot(int x, int y) {  //@param x first dimension to be called, @param y second dimension to be called
	   Grid cell = this.getGrid(x, y);
	   Grid firedOnCell = new Grid(cell);
	   
       if (cell.isAlreadyCalled()) // If the rocket falls on a coordinate that has been called before, nothing happens (regardless of what was there before)
       {
           --this.currentPlayersTurn; //give the turn to the next player
       } 
       else // if the coordinate is not called before 
       {
           cell.setaAlreadyCalled(true); //set it as a called coordinate
           
           if (cell.getType() == Type.GRENADE) {// If the rocket hit a grenade
               this.currentPlayersTurn = 0;	//then the player loses a turn
               this.nextPlayersTurn = 2;  //and the opponent will play twice in a row
               cell.setType(Type.NOTHING); //set the cell after hit back to nothing
               cell.setOwner(Owner.NONE);	//set the cell Owner after hit back to none
               
           } 
           else if (cell.getType() == Type.SHIP) // If the rocket hit a ship, then that ship sinks
           {
               if (cell.getOwner() == Owner.COMPUTER) //if the ship belonged to the computer
               {
                   --this.numberOfShipOfComputer; //decrease the number of computer's ship by one
               } 
               else //if the ship belonged to the user
               {
                   --this.numberOfShipOfUser; //(otherwise) decrease the number of user's ship by one
               }
               cell.setType(Type.NOTHING); //set the cell back to nothing, after the hit
               cell.setOwner(Owner.NONE);  //set the cell back to nothing, after the hit
               --this.currentPlayersTurn; //give the turn to the opponent
           }
           else// If the rocket falls on a position where there is nothing, nothing happens 
           {
               --this.currentPlayersTurn; //give the turn to the opponent
           }
       }
       return firedOnCell;  // @return the cell that has been called

   }

   //Switch player when one players's turn is over
   public void switchPlayer() {
       this.currentOwner = (this.currentOwner == Owner.USER ? Owner.COMPUTER : Owner.USER); //switch between computer and user
       this.currentPlayersTurn = this.nextPlayersTurn;
       this.nextPlayersTurn = 1;
   }

   //Check whether all ships of a player have sunk
   public boolean isDone() {
       return this.numberOfShipOfComputer == 0 || this.numberOfShipOfUser == 0; //@return true, if either side has no ship left; false, otherwise
   }

   //Get the winner of the game
   public Owner getWinner() {
       if (this.isDone()) //true when all ships of a player have sunk
       { 
           return this.numberOfShipOfComputer == 0 ? Owner.USER : Owner.COMPUTER; //@return the winner
       } 
       else // when there are ships of a player left
       {
           return Owner.NONE; 
       }
   }

   //Get the cell from the input position
   public Grid getGrid(int x, int y) { //@param int first dimension, @param int second dimension
       return this.cells[x][y]; //@return the cell from the input position
   }

   //Check whether the cell is taken
   public boolean isTaken(int x, int y) {  // @param int first dimension, @param int second dimension
       return !(this.cells[x][y].getOwner() == Owner.NONE);  //@return true, if taken
   }

   //Put a cell into the cell array
   public void setGrid(Grid c) { //@param c of type cell to be put
       this.cells[c.getX()][c.getY()] = c;
   }
   
   //visits all cells of the 2D array of type Grid
   private void createCells() {
       for (int i = 0; i < 8; i++) {
           for (int j = 0; j < 8; j++) {
               this.cells[i][j] = new Grid(i, j);
           }
       }
   }

   //Place computer's pieces
   private void placeComputerPieces() {
       placeComputersShips(); //call the method that place C's ship
       placeComputersGrenades(); //call the method that place C's grenade
   }

   //Place computer's ships randomly
   private void placeComputersShips() {
       int shipHasBeenPlaced = 0; //initialize the counter
       while (shipHasBeenPlaced < Game.TOTAL_NUMBER_OF_SHIP) { //while all ships are not placed
           int[] position = this.getRandomPosition(); //call this method to assign a random position to computer's ship
           if (this.isTaken(position[0], position[1])) { //check if the cell is already taken 
               continue;
           } 
           else { // if the cell is not taken 
               this.setGrid(new Grid(position[0], position[1], Type.SHIP, Owner.COMPUTER)); // update the corresponding position, type and Owner
               ++shipHasBeenPlaced; //increase the counter
           }
       }
   }

   //Place computer's grenades randomly
   private void placeComputersGrenades() {
       int grenadeHasBeenPlaced = 0; //initialize the counter
       while (grenadeHasBeenPlaced < Game.TOTAL_NUMBER_OF_GRENADE) { //while all grenades are not placed
           int[] position = this.getRandomPosition(); //call this method to assign a random position to computer's grenades
           if (this.isTaken(position[0], position[1])) { //check if the cell is already taken
               continue;
           } 
           else { // if the cell is not taken 
               this.setGrid(new Grid(position[0], position[1], Type.GRENADE, Owner.COMPUTER)); // update the corresponding position, type and Owner
               ++grenadeHasBeenPlaced; //increase the counter
           }
       }
   }

   //Generate random number
   private int[] getRandomPosition() {
       Random r = new Random(); //generates random numbers in the range 0 to 7
       int[] position = { r.nextInt(8), r.nextInt(8) };//set the upper bound for the method Random
       return position; //@return the random position (array of int)
   }
}