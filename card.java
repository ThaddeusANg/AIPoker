public class card {
	//valid Value and Suit organized in increasing pointValue, games like blackjack where Aces have fluctuating values will need to be handled manually
	String[] validValue = {"ace","2","3","4","5","6","7","8","9","10","jack","queen","king"};
	String[] validSuit ={"spades","hearts", "clubs","diamonds"};
	
	String value;
	String suit;

	void setValue(String tempValue, String tempSuit){
		for(int x=0;x<validValue.length;x++){
			if(tempValue.equalsIgnoreCase(validValue[x]))
				value = tempValue;
		}
		
		for(int x=0;x<validSuit.length;x++){
			if(tempSuit.equalsIgnoreCase(validSuit[x]))
				suit = tempSuit;
		}
	}
	
	String getRank(){
		return value;
	}
	
	String getSuit(){
		return suit;
	}
	
	char getSuitShort(){
		return suit.charAt(0);
	}
	
	char getRankShort(){
		return value.charAt(0);
	}
	
	String getShort(){
		String result = ""+getSuitShort()+getRankShort();
		return result;
	}
	int getRankValue(){
		int rankValue=-1;
		for(int x=0;x<validValue.length;x++){
			if(value.equals(validValue[x])==true)
				rankValue=x;
		}
		return rankValue;
	}
	
	int getSuitValue(){
		int suitCode=-1;
		for(int x=0;x<validSuit.length;x++){
			if(suit.equals(validSuit[x])==true)
				suitCode=x;
		}
		return suitCode;
	}
	
	int cardCode(){
		String temp=getSuitValue()+""+getRankValue();
		int tempInt = Integer.parseInt(temp);
		return tempInt;
	}
	
	String printCard(){
		String result = value+" of "+suit;
		return result;
	}
}
