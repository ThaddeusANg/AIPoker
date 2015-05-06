import java.util.Arrays;
import java.util.Scanner;

public class sampleHandGenerator {
	deck sampleDeck = new deck();
	pokerHand samplePlayer = new pokerHand(2);
	pokerHand sampleControl = new pokerHand(2);
	pokerHand sampleHouse = new pokerHand(5);
	Scanner input = new Scanner(System.in);
	
	public double[][]simpleBet(){
		double [][] results = new double[2][12];
		Arrays.fill(results[0], -1);
		Arrays.fill(results[1], -1);
		results[0][11]=1;
		int playerEval[];
		int controlEval[];
		for(int x=0;x<7;x++){
			if(x<2)
				{
				samplePlayer.addCard(sampleDeck.draw());
				sampleControl.addCard(sampleDeck.draw());
				}
			else
				sampleHouse.addCard(sampleDeck.draw());
		}
		playerEval = samplePlayer.evaluateHand(sampleHouse, sampleDeck.remainingCards);
		System.out.println("Hand Code 0-8, 0 is highest:"+playerEval[0]);
		controlEval = sampleControl.evaluateHand(sampleHouse, sampleDeck.remainingCards);
		results[0][playerEval[0]]=1;
		
		if(playerEval[0]<controlEval[0])
			results[1][0]=1;
		else
			results[1][0]=-1;

		return results;
	}
	
	public double[][] generate(){
		double[][] results = new double[2][12];
		Arrays.fill(results[0], -1);
		Arrays.fill(results[1], -1);
		results[0][11]=1;
		int [] playerEval;
		int [] controlEval;
		for(int x=0;x<7;x++){
			if(x<2)
				{
				samplePlayer.addCard(sampleDeck.draw());
				sampleControl.addCard(sampleDeck.draw());
				}
			else
				sampleHouse.addCard(sampleDeck.draw());
		}
		playerEval = samplePlayer.evaluateHand(sampleHouse, sampleDeck.remainingCards);
		controlEval = sampleControl.evaluateHand(sampleHouse, sampleDeck.remainingCards);
		results[0][playerEval[0]]=1;
		
		switch (playerEval[1]){
		case 0:
			results[0][8]=1;
			break;
		case 1:
			results[0][9]=1;
			break;
		case 2:
			results[0][10]=1;
			break;
		}
		String output="";
		for(int x=0;x<12;x++){
			output+=results[0][x]+" | ";
		}
		
		if(playerEval[0]<controlEval[0])
			results[1][0]=1;
		else
			results[1][0]=-1;
		//System.out.println(output);
		return results;
	}
	
	public static void main(String[] args){
		sampleHandGenerator thisGenerator = new sampleHandGenerator();
		thisGenerator.generate();		
	}
}
