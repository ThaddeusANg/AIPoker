import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Random;
import java.util.Scanner;

public class handProbToFile {
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//build training set
		int totalRight=0;
		int totalWrong=0;
		
		Random rand = new Random();
		DecimalFormat df = new DecimalFormat("#.000");
		
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
		
		int rounds =1000;
		int y=0;
		for(y=0;y<rounds;y++)
		{
		int trainingIterations=50;
		double rightGuess=0;
		double wrongGuess=0;
		
		
		for(int count=0;count<trainingIterations;count++){
			int bet=rand.nextInt(100);
			
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
			
			int[] evalTestHand = testHand.evaluateHand(houseHand, testDeck.remainingCards);
			//System.out.println("control hand below");
			int[] evalContHand = controlHand.evaluateHand(houseHand, testDeck.remainingCards);

			String thisHand = winningHandCode[evalTestHand[0]]+"|"+evalTestHand[1];

			boolean score=false;
			if(evalTestHand[0]<evalContHand[0])
				//System.out.println("Test Hand wins");
				score=true;
			String scoredHand="";
			if(score==true){
				 scoredHand= thisHand+"|W";
			}else{
				 scoredHand= thisHand+"|L";
			}
			
			//System.out.println(thisHand);
			String gName = "newGambleLog.txt";
			String fName = "newHandLog.txt";
			writeFile gambleData = new writeFile(gName,true);
			writeFile winningData = new writeFile(fName,true);
			
			
			try {
				if(winningData.totalRows(scoredHand)>0 )
					winningData.increaseScore(fName,scoredHand, "\\|");
				else
					winningData.writeToFile(scoredHand+"|1");
				

				float total = winningData.totalSum(3);
				if(total==0)
					total=1;
				float hand = winningData.totalSum(3, thisHand);
				float handWin = winningData.totalSum(3, (thisHand+"|W"));
				float handLose = winningData.totalSum(3, (thisHand+"|L"));
				float handTotal=winningData.totalSum(3,thisHand);
				if(handLose==0)
					{handLose=1;}
				float probability = (hand/total);
				float winning = handWin/handTotal;
				/*
				System.out.printf("\n\nchance of winning: %f, handWin: %f, handLose: %f\n",winning,handWin, handLose);
				System.out.printf("myHand eval-ed: %s \n controlhand eval-ed: %s\n", winningHandCode[evalTestHand[0]], 
						winningHandCode[evalContHand[0]]);
				*/
				
				if((1-winning)<winning){
					//System.out.println("Betting");
					thisHand+="|B";
					if(score==true){
						{thisHand+="|W";
						rightGuess++;
						//System.out.println("  :  Correct Guess\n");
						}
					}else
						{thisHand+="|L";
						wrongGuess++;
						System.out.println("Gambled Wrong prob: "+winning+" with " +thisHand+"\n");
						}
				}else{
					//System.out.println("Folding");
					thisHand+="|F";
					if(score==false){
						{thisHand+="|W";
						rightGuess++;
						//System.out.println("  :  Correct Guess\n");
						}
					}else
						{thisHand+="|L";
						wrongGuess++;
						System.out.println("Folded Wrong prob: "+winning+" with " +thisHand+"\n");
						}
					}
				
				if(gambleData.totalRows(thisHand)>0 )
					gambleData.increaseScore(gName, thisHand, "\\|");
				else
					gambleData.writeToFile(thisHand+"|1");
				

				/*System.out.println("Total games: \n"+total+"\nhand wins: \n"+
				hand+"\nrarity of hand: "+df.format(probability)+"\nwinning percentage: "+df.format(winning));
				*/
			} catch (IOException e) {
				System.out.println("Error from host reading File"+e);
			}
			totalRight+=rightGuess;
			totalWrong+=wrongGuess;
		}
		System.out.println("percent right: "+df.format(rightGuess/(wrongGuess+rightGuess)) );
		
		}
		System.out.printf("\n\nRight Guesses: "
				+ totalRight+"\n Wrong Guesses: "+totalWrong+"\nPercentage: "+df.format(totalRight/totalWrong));
	}
	

}

