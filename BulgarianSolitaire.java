// Name: Po-Yi Chen
// USC NetID: poyichen
// CSCI455 PA2
// Spring 2019

import java.util.ArrayList;
import java.util.Scanner;


/**
   <main program comment>
 * Tester for SolitaireBoard, and there are two commands
 * -u: User input
 * -s: Stops between every round of the game (Hit "enter" to continue)
 * In -u command, there is error checking function:
 * userinput must be non-negative number,
 * Total of the number user input shoud be equal to CARD_TOTAL (eg. 45)
 * @param singleStep   command switch of -s 
 * @param userConfig   command switch of -u 
 * @param runtime      total number of runs in the game

*/

public class BulgarianSolitaireSimulator {

   public static void main(String[] args) {
      Scanner in = new Scanner(System.in);
      ArrayList<Integer> initCards = new ArrayList<Integer>();
      boolean singleStep = false;
      boolean userConfig = false;
      int runTime;
      for (int i = 0; i < args.length; i++) {
         if (args[i].equals("-u")) {
            userConfig = true;
         }
         else if (args[i].equals("-s")) {
            singleStep = true;
         }
      }
      System.out.println("Number of total cards is " + SolitaireBoard.CARD_TOTAL);
      if (userConfig == true) {
         initialUserInput(in, initCards);
         // If passing all checking and then play the game
         SolitaireBoard board = new SolitaireBoard(initCards);
         System.out.println("Initial configuration: " + board.configString());
         startToPlay(singleStep, in, board);
      }
      // w/o -u command
      else {
         SolitaireBoard board = new SolitaireBoard();
         System.out.println("Initial configuration: " + board.configString());
         startToPlay(singleStep, in, board);
      }   
   }
   
   // <add private static methods here>
   
   /**
    * This method is used to initiate userinput and call error checking function
    * @param userInput    a series of number in string that user input
    * @param cards        use to extract number from userInput
    * @param error        check if there is invalid input
   */
   private static void initialUserInput(Scanner in, ArrayList<Integer> initCards) {
      String userInput;
      int cards;
      boolean error = true;                
      System.out.println("You will be entering the initial configuration of the cards (i.e., how many in each pile).");
      System.out.println("Please enter a space-separated list of positive integers followed by newline:");         
      while (error) {
         userInput = in.nextLine();
         Scanner lineScanner = new Scanner(userInput);
         while (lineScanner.hasNextInt()) {
            cards =  lineScanner.nextInt();
            initCards.add(cards);
         }
         // Execute error checking
         error = inputCheck(userInput, lineScanner, initCards);
      }   
   }
   
   /** 
    * This method is used to do error checking,
    * Call checkCardsAndTotal() and printErrorMessage() method 
    * If passing the checking, return true; 
    * Otherwise, clearn input data, show the error message and return false
   */   
   private static boolean inputCheck(String userInput, Scanner lineScanner, ArrayList<Integer> initCards) {
      if (lineScanner.hasNext()) {
         initCards.clear(); 
         printErrorMessage();  
         return true;
      }
      if (checkCardsAndTotal(initCards)) {
         initCards.clear();
         printErrorMessage(); 
         return true;
      }
      return false;
   }
   
   /** 
      Check the number of cards (shoud be positive integer) and the total number of cards
    * return true means there exists invalid input
    * @param totalUserSetCards   the total number of cards
   */
   private static boolean checkCardsAndTotal(ArrayList<Integer> initCards) {
      int totalUserSetCards = 0;
      // Check number of cards per pile
      for (int pile = 0; pile < initCards.size(); pile++) {
         if ((int)initCards.get(pile) < 1) {
            return true;
         }
         else {
            totalUserSetCards += (int)initCards.get(pile);
         }
      }
      // Check number of input total cards
      if (totalUserSetCards != SolitaireBoard.CARD_TOTAL) {
         return true;
      }
      return false;
   }
   
   /** 
      Error message printing function
   */
   private static void printErrorMessage() {
      System.out.println("ERROR: Each pile must have at least one card and the total number of cards must be " + SolitaireBoard.CARD_TOTAL);
      System.out.println("Please enter a space-separated list of positive integers followed by newline:");     
   }
   
   
   /** 
      Playing game function 
   */
   private static void startToPlay(boolean singleStep, Scanner in, SolitaireBoard board) {
      int runTime = 1;
      while (!board.isDone()) {
         board.playRound();
         System.out.println("[" + runTime + "] Current configuration: " + board.configString());
         if (singleStep) {
            System.out.print("<Type return to continue>");
            in.nextLine();            
         }
         runTime++;
      }
      System.out.println("Done!");
   }
  
}
