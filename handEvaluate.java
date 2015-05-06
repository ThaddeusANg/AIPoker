import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;


public class handEvaluate {
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//build training set
		int trainingIterations=1000000;
		for(int count=0;count<trainingIterations;count++){
			
			deck testDeck = new deck();
			pokerHand testHand = new pokerHand(2);
			pokerHand controlHand = new pokerHand(2);
			
			int handSize=5;
			pokerHand houseHand = new pokerHand(handSize);
			
			for(int x=0; x<2;x++){
				testHand.addCard(testDeck.draw());
				controlHand.addCard(testDeck.draw());
			}
			
			for(int x=0; x<handSize;x++){
				houseHand.addCard(testDeck.draw());
			}
			String encodedHand = testHand.encodeHand(houseHand);
			//System.out.println("test hand\n"+encodedHand);
			//System.out.println("control hand\n"+controlHand.encodeHand(houseHand));
			
			int[] evalTestHand = testHand.evaluateHand(houseHand, testDeck.remainingCards);
			//System.out.println("control hand below");
			int[] evalContHand = controlHand.evaluateHand(houseHand, testDeck.remainingCards);
			
			String[] winningHandCode = new String[9];
			winningHandCode[0]="Straight_Flush";
			winningHandCode[1]="Four_of_a_Kind";
			winningHandCode[2]="Full_House";
			winningHandCode[3]="Four_Suit";
			winningHandCode[4]="Sequence";
			winningHandCode[5]="Three_of_a_Kind";
			winningHandCode[6]="Two_Pair";
			winningHandCode[7]="One_Pair";
			winningHandCode[8]="High_Card";
			
			String thisHand = winningHandCode[evalTestHand[0]]+"|"+evalTestHand[1];

			boolean score=false;
			if(evalTestHand[0]<evalContHand[0]){
				if(evalTestHand[1]==0&&evalTestHand[1]==0){
					System.out.printf("test hand|    %s\ncontrol hand| %s\ntest wins with %s\n control hand loses with%s\n"
							,encodedHand, controlHand.encodeHand(houseHand),winningHandCode[0], winningHandCode[evalContHand[0]]);
				}
				//System.out.println("Test Hand wins");
				score=true;
			}
			String scoredHand="";
			if(score==true){
				 scoredHand= thisHand+"|W";	
			}else{
				 scoredHand= thisHand+"|L";
			}
			//System.out.println("scored hand"+scoredHand);
			//System.out.println(thisHand);
			//writeFile data = new writeFile("NNLog.txt",true);
			String fName="handNNLog.txt";
			writeFile winningData = new writeFile(fName,true);
			
			try {
				
				if(winningData.totalRows(scoredHand)>0 ){
					winningData.increaseScore(fName, scoredHand, "\\|");
				}else{
					System.out.println("new hand"+"\n"+scoredHand);
				
					winningData.writeToFile(scoredHand+"|1");
				
				}
			} catch (IOException e) {
				System.out.println("Error from host reading File"+e);
			}
		}

	}

}

