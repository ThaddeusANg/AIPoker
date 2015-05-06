import java.util.Random;

public class deck
{
  int[][] deck = new int[4][13];
  static String cardSuit = "null";
  static String cardValue = "null";
  static int pointValue = 0;
  static int remainingCards =52;
  public card draw()
  {
    Random rand = new Random();
    int drawX = rand.nextInt(13);
    int drawY = rand.nextInt(4);

    if (deck[drawY][drawX] == 0)
    {
      int rangeY = drawY + 1;
      switch (rangeY)
      {
      case 1: 
        cardSuit = "spades";
        break;
      case 2: 
        cardSuit = "hearts";
        break;
      case 3: 
        cardSuit = "clubs";
        break;
      case 4: 
        cardSuit = "diamonds";
      }
      
      int rangeX = drawX + 1;
      switch (rangeX)
      {
      case 1: 
        cardValue = "ace";
        break;
      case 2: 
      case 3: 
      case 4: 
      case 5: 
      case 6: 
      case 7: 
      case 8: 
      case 9: 
      case 10:
    	  cardValue = rangeX+"";
    	  break;
      case 11: 
        cardValue = "jack";
        break;
      case 12: 
        cardValue = "queen";
        break;
      case 13: 
        cardValue = "king";
      }
      deck[drawY][drawX] = 1;
      card thisCard = new card();
      thisCard.setValue(cardValue, cardSuit);
      remainingCards--;
      return thisCard;
    }
    //System.out.println("XX DUPLICATE CARD: " + cardValue + " of " + cardSuit + " XX");
    return draw();
  }
  
  public static int cardRemainder(){
	  return remainingCards;
  }
  /*
  public static int[][] drawnCard()
  {
    System.out.println("_ A 2 3 4 5 6 7 8 9 10 J Q K");
    for (int y = 0; y < 4; y++)
    {
      String cardPrint = "";
      switch (y)
      {
      case 0: 
        cardPrint = "S";
        break;
      case 1: 
        cardPrint = "H";
        break;
      case 2: 
        cardPrint = "C";
        break;
      case 3: 
        cardPrint = "D";
        break;
      }
      for (int x = 0; x < 13; x++) {
        cardPrint = cardPrint + " " + deck[y][x];
      }
      System.out.println(cardPrint);
    }
    return deck;
  }
  
  public static void resetDeck()
  {
    for (int y = 0; y < 4; y++) {
      for (int x = 0; x < 13; x++) {
        deck[y][x] = 0;
      }
    }
    
  }
  */
}
