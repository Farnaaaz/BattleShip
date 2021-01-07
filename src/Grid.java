
//***************************************************************************************************************
// Assignment 4 
// Written by: Farnaz Zaveh, ID: 40032389
// For COMP 248-R - Fall 2020  
//
//This program uses an array of object and includes four classes and one driver class to simulate the battleship game between a human user and the computer.
//This class create a class Grid that construct cells with 5 properties, position with two coordinates, type of the piece (ship/Grenade/Nothing), 
// the owner whose piece is placed (User/Computer/None) and alreadyCalled that shows if the position is already called during the game or not.
//***************************************************************************************************************

public class Grid {

   
   private final int x; //position of first dimension in the grid array
   private final int y; //position of second dimension in the grid array
   private Type type;  //Define the type of the cell: ship OR grenade OR nothing
   private Owner owner; //Define who owns the cell: Computer OR user OR none
   private boolean alreadyCalled;  //whether the cell is called

   // the default constructor with 2 @param
   public Grid(int x, int y) {
       this.x = x; //Set the x position passes by driver
       this.y = y; //Set the y position passes by driver
       this.type = Type.NOTHING; //Type nothing by default 
       this.owner = Owner.NONE; //Owner none by default 
       this.alreadyCalled = false;   //cell is not called in the beginning
   }
   
   //the constructor with 4 @param
   public Grid(int x, int y, Type type, Owner owner) {
       this.x = x; //Set the x position passes by driver
       this.y = y; //Set the y position passes by driver
       this.type = type;   //Set the type passed by driver
       this.owner = owner; //Set the owner passed by driver
       this.alreadyCalled = false; //cell is not called in the beginning
   }

   // the constructor with Grid type @param  
   public Grid(Grid cell) { //@param cell of type Grid to be copied
       this.x = cell.x; //Set the x position 
       this.y = cell.y; //Set the y position 
       this.type = cell.type;  //Set the type 
       this.owner = cell.owner; //Set the owner 
       this.alreadyCalled = cell.alreadyCalled; //Set the alreadyCalled value 
   }
   

   //accessor method for type
   public Type getType() {
       return type;
   }
   
   //mutator method for type
   public void setType(Type type) {
       this.type = type;
   }
   
 //accessor method for Owner
   public Owner getOwner() {
       return owner;
   }
   
   //mutator method for Owner
   public void setOwner(Owner owner) {
       this.owner = owner;
   }
   
   // method to check if the cell is already used
   public boolean isAlreadyCalled() {
       return alreadyCalled;	//@return true is the cell is used, false otherwise
   }
   
   //mutator method for Owner
   public void setaAlreadyCalled(boolean alreadyCalled) {
       this.alreadyCalled = alreadyCalled;
   }
   
   //accessor method for x
   public int getX() {
       return x;
   }
   
   //accessor method for y
   public int getY() {
       return y;
   }
}


