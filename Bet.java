
public class Bet {
	double totalMoney=0;
	boolean folded=false;
	boolean allIn=false;
	String status="";
	String playerName ="";
	
	public Bet(double myCash){
		totalMoney = myCash;
	}
	
	public void setName(String name){
		playerName = name;
	}
	
	public String getName(){
		return playerName;
	}
	
	public void buyIn(double cost){
			totalMoney+=cost;
	}
	
	public void gambleWin(double payment){
		if(folded==false)
			totalMoney+=payment;
	}
	
	public double blind(double blind){
		if(totalMoney>blind)
			totalMoney-=blind;
		else
			totalMoney=0;
		return blind;
	}
	
	public String[] gamble(String gamble){
		String[] result= new String[2];
				result[1]="Player has folded";
		double roundBet=0;
		if(folded==false&&allIn==false){
			switch(gamble){
			case "check":
				result[0] = gamble;
				break;
			case "fold":
				result[0] = gamble;
				folded=true;
				break;
			case "all":
				allIn=true;
				roundBet = totalMoney;
				result[0] = gamble;
				result[1] = roundBet+"";
				totalMoney=0;
				break;
			}
		}
		status = gamble;
		return result;
	}
	
	public String[] gamble(String gamble, double bet){
		String[] result= {"Player has folded or player submitted an invalid Bet",""};
		double roundBet=0;
		if(folded==false
				&&allIn==false
				&&bet>0){
			switch(gamble){
			case "check":
				result[0] = gamble;
				break;
			case "fold":
				result[0] = gamble;
				folded=true;
				break;
			case "call":
			case "raise":
				if(totalMoney>bet)
					{totalMoney=totalMoney-bet;}
				else{
					bet = totalMoney;
					totalMoney=0;
				}
				result[0] = gamble;
				result[1] = bet+"";
				break;
			case "all":
				allIn=true;
				bet = totalMoney;
				result[0] = gamble;
				result[1] = bet+"";
				totalMoney=0;
				break;
			}
		}
		return result;
	}
	
	public double getTotal(){
		return totalMoney;
	}
	
	public boolean canBet(){
		boolean status = true;
		if(folded==true||allIn==true||totalMoney==0)
			{status=false;}
		return status;
	}
}
