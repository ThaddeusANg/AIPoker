import java.io.PrintStream;
import java.util.Scanner;

public class poker
{
  public static void main(String[] args)
  {
    Scanner input = new Scanner(System.in);
    String action = "";
	
    System.out.println("Please enter the number of players, minimum is 1");
	int totalPlayers = input.nextInt()+1;
    int lastStanding = totalPlayers-1;
	
	//MUST INITIALIZE EACH BET OBJECT FIRST;
	Bet[] players = new Bet[totalPlayers];
	
	//receive player names and set bet status to pre-game value
	System.out.println("In Counter Clockwise Order");
	players[0]=new Bet(0);
	players[0].setName("house");
	
	for(int player=1; player < totalPlayers; player++){
    	System.out.println("Please enter Player "+ player +"'s name.");
    	double availMoney=1000;
    	players[player] = new Bet(availMoney);
    	players[player].setName(input.next());
    	System.out.printf("%s has $%s", players[player].getName(), players[player].getTotal());
	}
	
	
	System.out.println("Type 1 for basic blackjack, \nType 2 for poker");
    action = input.next();
    
    if(action.equals("2"))
    {
    	/*
    	 * 2D array to hold player Name and bet Status
    	 * player name => [x][0]
    	 * player bet status => [x][1]
    	 */
    	
    	//creates playerHand 2D array for tracking cards, human players use cells 0 and 1, house [0][x] uses cells 0-4
    	pokerHand[] masterPlayerHands = new pokerHand[totalPlayers];
    	masterPlayerHands[0]= new pokerHand(5);
    	for(int x=1;x<totalPlayers;x++){
    		masterPlayerHands[x]= new pokerHand(2);	
    	}
    	//receives minimum bet value for Blind 
    	System.out.println("Please enter the minimum bet");
    	double Blind = Double.parseDouble(input.next());
    	
    	double pot = 0;
    	double lastBet =Blind;
    	
    	deck pokerDeck = new deck();
    	
    	//game runs for 3 turns
    	for(int turn = 0; turn<3; turn++)
    	{
    		//set current player to 1 to skip the house
    		int currentPlayer = 1;
    		//if first turn, first two players are little, big blind.
    		// ------------------------------------------------------Turn 1/ BLIND BETS ----------------------
    		if(turn==0){
    			System.out.println(players[currentPlayer].getName()+",  you are little blind \nPlease put $"+(Blind/2)+" into the pot");
    			pot+=players[currentPlayer].blind(Blind/2);
    			currentPlayer++;
    			
    			if(totalPlayers>2){
    				System.out.println(players[currentPlayer].getName()+",  you are big blind \nPlease put $"+Blind+" into the pot\n");
    				pot+=players[currentPlayer].blind(Blind);
    				currentPlayer++;
    			}
    			    			
    			//deal cards writing to playerHand array starting at 1 to skip the house
    			for(int player=1;player<totalPlayers;player++)
    			{
    				masterPlayerHands[player].addCard(pokerDeck.draw());
    				masterPlayerHands[player].addCard(pokerDeck.draw());
    			}
    			
    			//-----------START BETTING AFTER THE BIG BLIND WITH POCKET CARDS ---------------------------
  			  for(int x=currentPlayer; x<players.length;x++)
				  {
  				  //-----prints hand and prompts for bets-----
						  System.out.println(players[x].getName()+"'s hand"+masterPlayerHands[x].printString());
						  System.out.println("Current Pot is: "+pot+"\n"+"Current Bet is: "+lastBet+"\n"+
								  "current round/player \n"+turn+"/"+x+"\n"+players[x].getName()+", you have"+players[x].getTotal()+
								  ", Would you like to check/fold, call, raise, or all?");
						  //boolean value checks if money is put into the pot
						  boolean moneyBet=false;
						  //pay receives results of gamble, first cell holds status, second cell holds money involved
						  String pay[];
						  String betSelect = input.next();
						  //if pot == the blind and a half, no one's bet and checking is still valid
						  if(betSelect.equalsIgnoreCase("check")&&pot>(lastBet*1.5))
							  betSelect="fold";
						  
						  //switch on betSelect for check/fold, call, raise, and all commands
						  switch(betSelect){
						  case "call":
							  moneyBet=true;
							  pay = players[x].gamble(betSelect, lastBet);
							  break;
						  case "raise":
							  System.out.println("How much would you like to raise by");
							  lastBet += input.nextDouble();
							  pay = players[x].gamble(betSelect, lastBet);
							  moneyBet=true;
							  break;
						  case "all":
							  moneyBet=true;
							  pay = players[x].gamble(betSelect);
							  lastStanding--;
							  break;
						  default:
							  pay = players[x].gamble(betSelect);
							  if(pay.equals("fold"))
								  lastStanding--;
							  break;
						  }
						  if(moneyBet==true){
							  double payDouble = Double.parseDouble(pay[1]);
							  if(payDouble>lastBet)
								  lastBet=payDouble;
							  pot+=payDouble;
						  }
		}
    			//house draw three card flop
				  for(int x=0; x<3;x++){
					  masterPlayerHands[0].addCard(pokerDeck.draw());
				  }
    		}else{
    			
    			//------------------IF NOT ROUND 1, DRAW ONE COMMUNAL CARD
    			//house draw single card
    			masterPlayerHands[0].addCard(pokerDeck.draw());
    		//if turn==0
    		}
    		
    		//----------------------------------------END PRE-FLOP BETTING BEGIN, ROUNDS 1-3 WILL PLAY BELOW
			//start bet
				  for(int x=1; x<players.length&&lastStanding>=2;x++)
				  {
					  //check if player can bet
					  if(players[x].canBet()==true){
						  //print communal and pocket cards and evaluate options
						  System.out.println(players[0].getName()+"'s communal cards"+masterPlayerHands[0].printString());
						  System.out.println(players[x].getName()+"'s hand"+masterPlayerHands[x].printString());
						  masterPlayerHands[x].evaluateHand(masterPlayerHands[0], pokerDeck.remainingCards);
						  
						  //prompt user for betting selection
						  System.out.println("Current Pot is: "+pot+"\n"+"Current Bet is: "+lastBet+"\n"+
								  "current round/player \n"+turn+"/"+x+"\n"+players[x].getName()+", you have"+players[x].getTotal()+
								  ", Would you like to check/fold, call, raise, or all?");
						  //boolean value checks if money is put into the pot
						  boolean moneyBet=false;
						  //pay receives results of gamble, first cell holds status, second cell holds money involved
						  String pay[];
						  String betSelect = input.next();
						  //if pot == the blind and a half, no one's bet and checking is still valid
						  if(betSelect.equalsIgnoreCase("check")&&pot>(lastBet*1.5))
							  betSelect="fold";
						  
						  //switch on betSelect for check/fold, call, raise, and all commands
						  switch(betSelect){
						  case "call":
							  moneyBet=true;
							  pay = players[x].gamble(betSelect, lastBet);
							  break;
						  case "raise":
							  System.out.println("How much would you like to raise by");
							  lastBet += input.nextDouble();
							  pay = players[x].gamble(betSelect, lastBet);
							  moneyBet=true;
							  break;
						  case "all":
							  moneyBet=true;
							  pay = players[x].gamble(betSelect);
							  lastStanding--;
							  break;
						  default:
							  pay = players[x].gamble(betSelect);
							  if(pay.equals("fold"))
								  lastStanding--;
							  break;
						  }
						  if(moneyBet==true){
							  double payDouble = Double.parseDouble(pay[1]);
							  if(payDouble>lastBet)
								  lastBet=payDouble;
							  pot+=payDouble;
						  }
				}

				  currentPlayer=0;
		}
			//end bet Check
    	
    	}
    	//for loop for 3 turns of texas hold'em
    	System.out.println("final pot "+pot);
    	for(int x=0;x<totalPlayers;x++){
    		System.out.println(players[x].getName()+"'s hand"+masterPlayerHands[x].printString());
    	}
    	for(int x=1;x<totalPlayers;x++){
    		System.out.println(players[x].getName()+"'s highest hand");
    		masterPlayerHands[x].evaluateHand(masterPlayerHands[0],pokerDeck.remainingCards);
    	}
    }
    //if user submits 2 for poker
    
    
    if (action.equals("1"))
    {
        deck bjDeck=new deck();	
        /*
    	 * 2D array to hold player Name and bet Status
    	 * player name => [x][0]
    	 * player bet status => [x][1]
    	 */
        pokerHand[] masterPlayerHands = new pokerHand[totalPlayers];
        int temp[];
        	//cell[x][0]==score
        	//cell[x][1]==aces
        int[][] scoreArray = new int[totalPlayers][2];
        
        for(int x=0;x<totalPlayers;x++){
        	card newCard=bjDeck.draw();
        	masterPlayerHands[x].addCard(newCard);
        	
        	temp=masterPlayerHands[x].bjScore(newCard);
        	System.out.println(players[x].getName()+":"+temp[0]);
        	scoreArray[x][0]+=temp[0];
        	scoreArray[x][1]+=temp[1];
        	
        	//second card is dealt face Up
        	newCard=bjDeck.draw();
        	masterPlayerHands[x].addCard(newCard);
        	
        	temp=masterPlayerHands[x].bjScore(newCard);
        	System.out.println(players[x].getName()+":"+temp[0]);
        	scoreArray[x][0]+=temp[0];
        	scoreArray[x][1]+=temp[1];
        	}
        	
        for(int x=0; x<totalPlayers;x++){
        	boolean blackjack=false;
        	boolean bust = false;
        	if(x==0){}
        		else{
        			boolean done=false;
        			while(done==false&&scoreArray[x][0]<21 && bust==false){
            			String output="";
            			for(int a=0; a<totalPlayers;a++)
            				output += players[a].getName()+ " face up: "+masterPlayerHands[a].getCard(1).printCard()+"\n";
            			
            			System.out.println("Up Cards" + output);
            			System.out.println("\nYour Cards:"+masterPlayerHands[x].printString());
            			System.out.println("Your Score: "+scoreArray[x][0]+" total Aces" + scoreArray[x][1]);
            			System.out.println(players[x].playerName+", Would you like another card? type hit or stay");
            			
            			String req = input.next();
            			if(req.equals("hit")||req.equals("stay")){
            				if(req.equals("hit")){
            					card newCard = bjDeck.draw();
            					masterPlayerHands[x].addCard(newCard);
            		        	temp=masterPlayerHands[x].bjScore(newCard);
            		        	scoreArray[x][0]+=temp[0];
            		        	scoreArray[x][1]+=temp[1];
            		        	
            		        	if(scoreArray[x][0]==21)
                					{
            		        		System.out.println("BlackJack "+temp[0]);
            		        		blackjack=true;
            		        		break;
                					}
            		        	if(scoreArray[x][0]>21)
            		        		{bust=false;
            		        		System.out.println("You busted with "+temp[0]);
            		        		break;
            		        		}
            				}//end if else req = hit or stay
                			else{
                				done=true;
                			}
            			}//end check if input = hit or stay	
        			}//end while loop
        		}//end player controlled round
        	}//end for all players loop
    }
 
}
}