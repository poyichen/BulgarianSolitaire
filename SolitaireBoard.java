// Name: Po-Yi Chen
// USC NetID: poyichen
// CSCI455 PA2
// Spring 2019

import java.util.ArrayList;
import java.util.Random;

/*
  class SolitaireBoard
  The board for Bulgarian Solitaire.  You can change what the total number of cards is for the game
  by changing NUM_FINAL_PILES, below.  Don't change CARD_TOTAL directly, because there are only some values
  for CARD_TOTAL that result in a game that terminates.
  (See comments below next to named constant declarations for more details on this.)
*/


public class SolitaireBoard {
   
   public static final int NUM_FINAL_PILES = 9;
   // number of piles in a final configuration
   // (note: if NUM_FINAL_PILES is 9, then CARD_TOTAL below will be 45)
   
   public static final int CARD_TOTAL = NUM_FINAL_PILES * (NUM_FINAL_PILES + 1) / 2;
   // bulgarian solitaire only terminates if CARD_TOTAL is a triangular number.
   // see: http://en.wikipedia.org/wiki/Bulgarian_solitaire for more details
   // the above formula is the closed form for 1 + 2 + 3 + . . . + NUM_FINAL_PILES

   // Note to students: you may not use an ArrayList -- see assgt description for details.
   
   
   /**
      Representation invariant:

      <put rep. invar. comment here>
      0 <= numOfPiles (This is index) < UPPER_BOUND_PILES
      1 <= numOfCards[i] <= CARD_TOTAL, where i <= numOfPiles
      Sum of all values in numOfCards[i] == CARD_TOTAL, where i <= numOfPiles
   */
   
   // <add instance variables here>
   /**   
    * @param UPPER_BOUND_PILES   maximum number of piles 
    * @param numOfCards[i]       the number of cards in i pile
    * @param numOfPiles          the number of piles with cards    
       (it would be equal to CARD_TOTAL if there is only a card in each piles)
   */
   int UPPER_BOUND_PILES = CARD_TOTAL;
   int[] numOfCards = new int[UPPER_BOUND_PILES];
   int numOfPiles = 0;
 
   /**
      Creates a solitaire board with the configuration specified in piles.
      piles has the number of cards in the first pile, then the number of cards in the second pile, etc.
      PRE: piles contains a sequence of positive numbers that sum to SolitaireBoard.CARD_TOTAL
   */
   public SolitaireBoard(ArrayList<Integer> piles) {
      for (int i = 0; i < piles.size(); i++) {
         numOfCards[i] = piles.get(i);
         numOfPiles = i;
      }
      assert isValidSolitaireBoard();   // sample assert statement (you will be adding more of these calls)
   }
 
   
   /**
      Creates a solitaire board with a random initial configuration.
    * @param ranCard       recording the random number of cards in each random step
    * @param remainCards   the number of remaining cards needed to random   
   */
   public SolitaireBoard() {
      Random numCards = new Random();
      int ranCard;
      int remainCards = CARD_TOTAL;
      while (remainCards != 0) {
         ranCard = numCards.nextInt(remainCards) + 1;
         numOfCards[numOfPiles] = ranCard;
         remainCards -= ranCard;
         numOfPiles++;
      }
      numOfPiles--;
      assert isValidSolitaireBoard();
   }
     
   /**
      Plays one round of Bulgarian solitaire.  Updates the configuration according to the rules
      of Bulgarian solitaire: Takes one card from each pile, and puts them all together in a new pile.
      The old piles that are left will be in the same relative order as before, 
      and the new pile will be at the end.
    * Calling rmEmptyPile() function to remove empty piles  
    * @param numOfTransCards   the number of cards transferred in one running step
    * The space complexity is O(1) [It is used in rmEmptyPile function]
   */
   public void playRound() {
      int numOfTransCards = 0;
      for (int pile = 0; pile < numOfPiles + 1; pile++) {
         numOfCards[pile]--;
         numOfTransCards++;
      }
      rmEmptyPile();
      numOfCards[++numOfPiles] = numOfTransCards;
      assert isValidSolitaireBoard();
   }
   
   /**
      Returns true iff the current board is at the end of the game.  That is, there are NUM_FINAL_PILES
      piles that are of sizes 1, 2, 3, . . . , NUM_FINAL_PILES, in any order.
      @end          the status of the game
      @isNumExist   recording whether number 1 to 9 appears
      The time complexity is O(n)
      The space complexity is O(n)
   */
   
   public boolean isDone() {
      boolean end;
      if (numOfPiles != NUM_FINAL_PILES - 1) {
         assert isValidSolitaireBoard();
         return false;
      }
      else {
         boolean[] isNumExist = new boolean[NUM_FINAL_PILES];
         for (int pile = 0; pile < isNumExist.length; pile++) {           
            if (numOfCards[pile] <= NUM_FINAL_PILES) {
               isNumExist[numOfCards[pile]-1] = true;
            }
         }
         for (int pile = 0; pile < isNumExist.length; pile++) {
            if(!isNumExist[pile]) {
               assert isValidSolitaireBoard();
               return false;
            }
         }
         assert isValidSolitaireBoard();
         return true;
      }
   }

   
   /**
      Returns current board configuration as a string with the format of
      a space-separated list of numbers with no leading or trailing spaces.
      The numbers represent the number of cards in each non-empty pile.
   */
   public String configString() {
      String currConfig = "";
      for (int pile = 0; pile < numOfPiles + 1; pile++){
         currConfig += numOfCards[pile] + " ";
      }
      assert isValidSolitaireBoard();
      return currConfig.trim(); 
   }
   
   
   /**
      Returns true iff the solitaire board data is in a valid state
      (See representation invariant comment for more details.)
   */
   private boolean isValidSolitaireBoard() {
      int totalCards = 0;
      // 0 <= numOfPiles < UPPER_BOUND_PILES
      if (numOfPiles < 0 || numOfPiles >= UPPER_BOUND_PILES) {
         return false;
      }      
      // 1 <= numOfCards[i] <= CARD_TOTAL, where i <= numOfPiles
      for (int pile = 0; pile <= numOfPiles; pile++) {
         if (numOfCards[pile] < 1 || numOfCards[pile] > CARD_TOTAL) {
            return false;
         }
         totalCards += numOfCards[pile];
      }
      // Sum of all values in numOfCards[i] == CARD_TOTAL
      if (totalCards != CARD_TOTAL) {
         return false;
      }
      return true;
   }
   

   // <add any additional private methods here>
   
   /** 
      This function is only used in playRound()
      Check the empty pile after taking one card from it
      Remove it by shift the other non-empty piles
      One extra space judgeIndex is used in here
      @param judgeIndex   a pointer used to shift piles
   */
   private void rmEmptyPile() {
      int judgeIndex;
      for (int pile = 0; pile < numOfPiles + 1; pile++) {
         judgeIndex = pile + 1;
         while(numOfCards[pile] == 0) {
            if (judgeIndex > numOfPiles) {
               numOfPiles = pile;
            }
            if (judgeIndex == UPPER_BOUND_PILES) {
               numOfPiles--;
               break;
            }
            numOfCards[pile] = numOfCards[judgeIndex];
            numOfCards[judgeIndex] = 0;
            judgeIndex++;         
         }
      }
   }

}
