
public class pokerHand {
	
	card[] hand;
	private int totalSize=0;
	int currSize=0;
	
	pokerHand(int temp){
		totalSize =temp;
		hand = new card[totalSize];
	}
	
	void addCard(card thisCard){
		for(int x=0;x<totalSize;x++){
			if(hand[x]==null){
				hand[x] = thisCard;
				break;
			}
		}
	}
	
	public int totalInsertCards(){
		int total=0;
		for(int x=0; x<totalSize;x++){
			if(hand[x]==null)
				return total;
			else
				total++;	
		}
		return total;
	}
	
	card getCard(int x){
		return hand[x];
	}
	
	public int remainingCard(int value, card[][] visibleCards, int startVal){
		int remain=0;
		for(int x=0;x<visibleCards.length;x++){
			for(int y=startVal;y<visibleCards[x].length;y++){
				if(visibleCards[x][y].getRankValue()==value)
					remain++;
			}
		}
		return remain;
	}
	
	public int[] bjScore(card thisCard){
		int []convertScore={0,0};
		if(thisCard.getRankValue()==0)			
			convertScore[1]++;
		if(thisCard.getRankValue()>=10)
			convertScore[0]+=9;
		else 
			convertScore[0]+=thisCard.getRankValue();
		convertScore[0]+=1;
		return convertScore;
	}
	
	card[] returnHand(){
		return hand;
	}
	
	public String printString(){
		String result="\n";
		for(int x=0; x<totalSize;x++){
			if(hand[x]==null){
				return result;
			}else{
				result+=hand[x].printCard()+"\n";	
			}
		}
		return result;
	}
	
	public String encodeHand(pokerHand thisHand){
		card[] houseHand = thisHand.returnHand();
		String result="";
		for(int x=0; x<totalInsertCards();x++){
				result=result+hand[x].getSuitShort()+hand[x].getRankShort()+"|";	
		}
		for(int x=0;x<thisHand.totalInsertCards();x++){
			result=result+houseHand[x].getSuitShort()+houseHand[x].getRankShort()+"|";
		}
		return result;
	}
	
	public String encodeHand(pokerHand thisHand, String format){
		String invalid="";
		switch(format){
		case "binary":
			card[] houseHand = thisHand.returnHand();
			String result="";
			for(int x=0; x<totalInsertCards();x++){
					result=result+hand[x].getSuitShort()+hand[x].getRankShort()+"|";	
			}
			for(int x=0;x<thisHand.totalInsertCards();x++){
				result=result+houseHand[x].getSuitShort()+houseHand[x].getRankShort()+"|";
			}
			
		}
		return invalid;
}
	
	public int cardSimilar(int suitCode, int rankCode){
		int pocketUsed =0;
		/*cardCheck[0]: checking for same suit
		cardCheck[1]: checking for same rank
		cardCheck[2]: checking for sequence
		*/
		boolean[]cardCheck = {false, false, false};
		if(suitCode<4 && rankCode<0)
			cardCheck[0]=true;
		if(suitCode<0 && rankCode<13)
			cardCheck[1]=true;
		if(suitCode<4 && rankCode<13)
			cardCheck[2]=true;
		
		//check based on suit
		if(cardCheck[0]==true){
			if(suitCode==hand[0].getSuitValue()){
				pocketUsed++;
			}
			if(suitCode==hand[1].getSuitValue()){
				pocketUsed++;
			}
		}
		//check based on rank
		if(cardCheck[1]==true){
			if(rankCode==hand[0].getRankValue()){
				pocketUsed++;
			}
			if(rankCode==hand[1].getRankValue()){
				pocketUsed++;
			}
		}
		
		//check based on suit and rank
		if(cardCheck[2]==true){
			if(rankCode==hand[0].getRankValue() && suitCode==hand[0].getSuitValue()){
				pocketUsed++;
			}else{
				if(rankCode==hand[1].getRankValue() && suitCode==hand[1].getSuitValue()){
					pocketUsed++;
				}
			}
		}
		return pocketUsed;
	}
	
	
	public int[] evaluateHand(pokerHand houseHand, int remainingInDeck){
		/*9 cell array winning hands decreasing priority.
		 *0 => Straight Flush
		 *1 => Four of a Kind
		 *2 => Full House
		 *3 => Flush
		 *4 => Straight
		 *5 => Three of a Kind
		 *6 => Two Pair
		 *7 => One Pair
		 *8 => High Card 
		 */ 
		 
		int[][] winningHand = new int[9][2];
		//winningHand[x][0] = 1 for hand positive, 0 for hand negative
		//winningHand[x][1] = number of cards in winning hand using player hand cards;
		for(int x=0;x<9;x++){
			winningHand[x][0]=0;
			winningHand[x][1]=0;
		}
		//convert pokerhand object to arrays and get length
		card[] thisHand = houseHand.returnHand();
		int houseHandLength=0;
		for(int x=0;x<thisHand.length;x++)
		{
			if(thisHand[x]!=null){
				houseHandLength++;
			}
		}
		//int houseHandLength = thisHand.length;
		//System.out.println("combined hands "+ pocketHandLength+ " + "+houseHandLength);

		
		int[][] myCardArray =new int[4][13];
		for(int x=0;x<13;x++){
			for(int y=0;y<4;y++){
				myCardArray[y][x]=0;
			}
		}
		
		//System.out.println("crazy hand length ="+crazyHand.length);
		for(int x=0;x<hand.length;x++){
			myCardArray[hand[x].getSuitValue()][hand[x].getRankValue()]=1;
		}
		
		for(int x=0;x<houseHandLength;x++){
			myCardArray[thisHand[x].getSuitValue()][thisHand[x].getRankValue()]=1;
		}
		
		//suit and rank sum, for flush/same kind hands
		int[] suitArray=new int[4];
		int[] rankArray = new int[13];
		int maxSuit=0;
		for(int y=0;y<4;y++){
			for (int x=0; x<13;x++){
				if(myCardArray[y][x]==1){
					suitArray[y]++;
					rankArray[x]++;
				}
				if(suitArray[y]>=5){
					winningHand[3][1]=cardSimilar(y,-1);
					winningHand[3][0]=1;
				}else{
					if(maxSuit<suitArray[y]){
						maxSuit = suitArray[y];
					}
				}
			}
		}
		
		//check for percentage to flush
		/*
		float flushCardsNeeded = 5-maxSuit;
		float flushCardsRemaining = 13-maxSuit;
		float percentToFlush = (flushCardsNeeded/flushCardsRemaining)*(7-handSize);
		System.out.printf("suit Code %d|sample flush percentage check %f | %f| %f | hand size %f \n", 
				maxSuit, percentToFlush, flushCardsNeeded, flushCardsRemaining, handSize);
		winningHand[3][1]=percentToFlush;
		*/
		//check for straight and pairs
		
		int straight=0;
		int playerCard =0;
		for (int x=0; x<13;x++){
			 
			if(rankArray[x]>0){
				straight++;
				playerCard+=cardSimilar(-1,x);
				
				if(straight>=5){
					winningHand[4][0]=1;
					winningHand[4][1]=playerCard;}
				}else{
					if(straight<5){ 
						straight=0;
						playerCard=0;
						}
					}
			if(rankArray[x]==4){
					winningHand[1][0]=1;
					winningHand[1][1]=cardSimilar(-1,x);	
				}
			if(rankArray[x]==3){
				if(winningHand[5][0]==0){
					winningHand[5][0]=1;
					winningHand[5][1]=cardSimilar(-1,x);	
					}else{
						winningHand[7][0]=1;
						winningHand[7][1]=cardSimilar(-1,x);
					}
			}
			if(rankArray[x]==2){
				if(winningHand[7][0]==1){
					winningHand[6][1]=cardSimilar(-1,x);
					winningHand[6][1]+=winningHand[7][1];
					winningHand[6][0]=1;
				}else{
					winningHand[7][1]=cardSimilar(-1,x);
					winningHand[7][0]=1;}
			}
		}
		
		if(winningHand[5][0]==1&&winningHand[7][0]==1){
			winningHand[2][1]=winningHand[5][1]+winningHand[7][1];
			winningHand[2][0]=1;}
		
		int straightFlush=0;
		playerCard=0;
		for(int y=0;y<4;y++){
			for (int x=0; x<13;x++){
				if(myCardArray[y][x]==1){
					//System.out.println(x+"-"+y+"="+myCardArray[y][x]);
					straightFlush++;
					playerCard+=cardSimilar(y,x);
					
					if(straightFlush==5){
						winningHand[0][0]=1;
						winningHand[0][1]=playerCard;
						}
				}else{
					straightFlush=0;
					playerCard=0;}
			}
			straightFlush=0;
			playerCard=0;
		}

		
		//check boolean flags for highest possible hand
		String[] winningHandCode = new String[9];
		winningHandCode[0]="Straight_Flush";
		winningHandCode[1]="Four_of_a_Kind";
		winningHandCode[2]="Full_House";
		winningHandCode[3]="Flush";
		winningHandCode[4]="Sequence";
		winningHandCode[5]="Three_of_a_Kind";
		winningHandCode[6]="Two_Pair";
		winningHandCode[7]="One_Pair";
		winningHandCode[8]="High_Card";
		//set High Card to true
				
		//System.out.println("Winning Hand Array \n");
		
		int[] result = new int[2];
		result[0]=8;
		result[1]=1;
		for(int x=0;x<winningHand.length;x++){
			if(winningHand[x][1]>0)
			{
				//System.out.printf("%s | %s \n", winningHandCode[x],winningHand[x][1]);	
			}
			//System.out.println(winningHand[x][0]+" | "+winningHand[x][1]+" | "+winningHand[x][2]);
			if(winningHand[x][0]==1){
				result[0]=x;
				result[1]=winningHand[x][1];
				return result;
			}
		}
		//System.out.println("High Card");
		return result;
	}
}
