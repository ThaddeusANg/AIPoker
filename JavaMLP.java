/*
MLP neural network in Java
by Phil Brierley
www.philbrierley.com

This code may be freely used and modified at will

Tanh hidden neurons
Linear output neuron

To include an input bias create an
extra input in the training data
and set to 1

Routines included:

calcNet()
WeightChangesHO()
WeightChangesIH()
initWeights()
initData()
tanh(double x)
displayResults()
calcOverallError()

compiled and tested on
Symantec Cafe Lite

*/

import java.io.IOException;
import java.lang.Math;
import java.util.Arrays;
import java.util.Scanner;
public class JavaMLP
{

 //user defineable variables
 public static int numEpochs = 5000; //number of training cycles
 public static int numInputs  = 13; //number of inputs - this includes the input bias
 public static int numHidden  = 10; //number of hidden units
 public static int numPatterns = 25; //number of training patterns
 public static double LR_IH = 0.1; //learning rate
 public static double LR_HO = 0.01; //learning rate

 //process variables
 public static int patNum;
 public static double errThisPat;
 public static double outPred;
 public static double RMSerror;

 //training data
 public static double[][] trainInputs  = new double[numPatterns][numInputs];
 public static double[] trainOutput = new double[numPatterns];
 public static double[][] trainOutputResults = new double[numPatterns][4];

 //the outputs of the hidden neurons
 public static double[] hiddenVal  = new double[numHidden];

 //the weights
 public static double[][] weightsIH = new double[numInputs][numHidden];
 public static double[] weightsHO = new double[numHidden];


//==============================================================
//********** THIS IS THE MAIN PROGRAM **************************
//==============================================================

 public static void main(String[] args)
 {
  //initiate the weights
  initWeights();
  boolean wTrain=true;
if(wTrain==true){
  //load in the data
  initData();

  //train the network
  int [][] solutionArray = new int[9][3];
  solutionArray[0][0]=0;
  solutionArray[1][0]=1;
  solutionArray[2][0]=2;
  solutionArray[3][0]=3;
  solutionArray[4][0]=4;
  solutionArray[5][0]=5;
  solutionArray[6][0]=6;
  solutionArray[7][0]=7;
  solutionArray[0][1]=8;
  solutionArray[1][1]=9;
  solutionArray[2][1]=10;
  solutionArray[3][1]=11;
  solutionArray[4][1]=12;
  solutionArray[5][1]=13;
  solutionArray[6][1]=14;
  solutionArray[7][1]=15;
  solutionArray[8][1]=16;
  solutionArray[0][2]=17;
  solutionArray[1][2]=18;
  solutionArray[2][2]=19;
  solutionArray[3][2]=20;
  solutionArray[4][2]=21;
  solutionArray[5][2]=22;
  solutionArray[6][2]=23;
  solutionArray[7][2]=24;
    
    for(int j = 0;j <= numEpochs;j++)
    {

        for(int i = 0;i<numPatterns;i++)
        {

            //select a pattern at random
        	sampleHandGenerator myGen = new sampleHandGenerator();
        	double[][] generatedArray = myGen.generate();
        	double handCode=-1;
        	double pocketCard=-1;
        	

        	for(int x=0;x<11;x++){
        		if(generatedArray[0][x]==1){
        			if(handCode==-1)
        				handCode=x;
        			else
        				{pocketCard=(x-8);
        				break;}
        		}
        	}

            patNum = solutionArray[(int) handCode][(int) pocketCard];
            trainOutput[patNum]=generatedArray[1][0];
            //calculate the current network output
            //and error for this pattern
            //System.out.println("handCode: "+handCode+" pocketCard:"+pocketCard);
            calcNet();
            if(generatedArray[1][0]==1)
            	trainOutputResults[patNum][0]++;
            else
            	trainOutputResults[patNum][1]++;
            
            trainOutputResults[patNum][2]=handCode;
            trainOutputResults[patNum][3]=(pocketCard);            
            
            //change network weights
            WeightChangesHO();
            WeightChangesIH();
        }

        //display the overall network error
        //after each epoch
        calcOverallError();
        System.out.println("epoch = " + j + "  RMS Error = " + RMSerror);

    }
 }
    //test code
    int totalRuns=1000;
    double[][] resultArray = new double[2][5];
    for(int a=0;a<totalRuns;a++){
    	sampleHandGenerator playGen = new sampleHandGenerator();
    	double[][] gameArray= playGen.generate();
    	String output="";
    	for(int x=0;x<gameArray[0].length;x++){
    		output+=" | "+gameArray[0][x];
    	}
    	output+="length:"+gameArray[0].length;
    	//System.out.println(output);
    	int result = getResult(gameArray[0]);
    	System.out.println("1 is to bet, -1 is to fold| "+result+"\nCorrect response| "+gameArray[1][0]);
    	if(result==1&&gameArray[1][0]==1){
    		resultArray[0][0]++;
    	}else if(result==-1&&gameArray[1][0]==-1){
    		resultArray[0][1]++;
    	}else if(result==1&&gameArray[1][0]==-1){
    		resultArray[0][2]++;
    	}else{
    		resultArray[0][3]++;
    	}
    	resultArray[0][4]++;
    }
    resultArray[1][0]=(resultArray[0][0]/resultArray[0][4]);
    resultArray[1][1]=(resultArray[0][1]/resultArray[0][4]);
    resultArray[1][2]=(resultArray[0][2]/resultArray[0][4]);
    resultArray[1][3]=(resultArray[0][3]/resultArray[0][4]);
    
	System.out.println("O|X|FO|FX\n"+resultArray[0][0]+"|"+resultArray[0][1]+"|"+resultArray[0][2]+"|"+resultArray[0][3]);
	
	System.out.println("O|X|FO|FX\n"+resultArray[1][0]+"|"+resultArray[1][1]+"|"+resultArray[1][2]+"|"+resultArray[1][3]);
	System.out.println("O|X\n"+(resultArray[1][0]+resultArray[1][1])+"|"+(resultArray[1][2]+resultArray[1][3]));
	String fName = "NNResults.txt";
	writeFile NNData = new writeFile(fName, true);
	try {
		NNData.writeToFile("With Training"+ wTrain);
		NNData.writeToFile("O|X|FO|FX");
		NNData.writeToFile(resultArray[1][0]+"|"+resultArray[1][1]+"|"+resultArray[1][2]+"|"+resultArray[1][3]);
		NNData.writeToFile("O|X");
		NNData.writeToFile((resultArray[1][0]+resultArray[1][1])+"|"+(resultArray[1][2]+resultArray[1][3]));
		NNData.writeToFile("----------------");
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
    //training has finished
    //display the results
    displayResults();
 }

//============================================================
//********** END OF THE MAIN PROGRAM **************************
//=============================================================



//***********************************
 public static int getResult(double [] input)
 {
	 int response;
    //calculate the outputs of the hidden neurons
    //the hidden neurons are tanh
    for(int i = 0;i<numHidden;i++)
    {
 	hiddenVal[i] = 0.0;

        for(int j = 0;j<input.length;j++)
        	hiddenVal[i] = hiddenVal[i] + (input[j] * weightsIH[j][i]);
        hiddenVal[i] = tanh(hiddenVal[i]);
    }

   //calculate the output of the network
   //the output neuron is linear
   outPred = 0.0;

   for(int i = 0;i<numHidden;i++)
    outPred = outPred + hiddenVal[i] * weightsHO[i];

    //calculate result
    if(outPred>0)
    	response=1;
    else
    	response=-1;
    return response;
 }



//************************************
public static void calcNet()
{
   //calculate the outputs of the hidden neurons
   //the hidden neurons are tanh
   for(int i = 0;i<numHidden;i++)
   {
	hiddenVal[i] = 0.0;

       for(int j = 0;j<numInputs;j++)
       hiddenVal[i] = hiddenVal[i] + (trainInputs[patNum][j] * weightsIH[j][i]);

       hiddenVal[i] = tanh(hiddenVal[i]);
   }

  //calculate the output of the network
  //the output neuron is linear
  outPred = 0.0;

  for(int i = 0;i<numHidden;i++)
   outPred = outPred + hiddenVal[i] * weightsHO[i];

   //calculate the error
   errThisPat = outPred - trainOutput[patNum];
}
//************************************
public static void WeightChangesHO()
//adjust the weights hidden-output
{
  for(int k = 0;k<numHidden;k++)
  {
   double weightChange = LR_HO * errThisPat * hiddenVal[k];
   weightsHO[k] = weightsHO[k] - weightChange;

   //regularisation on the output weights
   if (weightsHO[k] < -5)
       weightsHO[k] = -5;
   else if (weightsHO[k] > 5)
       weightsHO[k] = 5;
  }
}


//************************************
public static void WeightChangesIH()
//adjust the weights input-hidden
{
 for(int i = 0;i<numHidden;i++)
 {
  for(int k = 0;k<numInputs;k++)
  {
   double x = 1 - (hiddenVal[i] * hiddenVal[i]);
   x = x * weightsHO[i] * errThisPat * LR_IH;
   x = x * trainInputs[patNum][k];
   double weightChange = x;
   weightsIH[k][i] = weightsIH[k][i] - weightChange;
  }
 }
}


//************************************
public static void initWeights()
{

 for(int j = 0;j<numHidden;j++)
 {
   weightsHO[j] = (Math.random() - 0.5)/2;
   for(int i = 0;i<numInputs;i++)
   weightsIH[i][j] = (Math.random() - 0.5)/5;
 }

}


//************************************
public static void initData()
{

   System.out.println("initialising data");

   // the data here is the XOR data
   // it has been rescaled to the range
   // [-1][1]
   // an extra input valued 1 is also added
   // to act as the bias
   for(int x=0;x<numPatterns;x++){
   	for(int y=0;y<numInputs;y++){
   		trainInputs[x][y]=0;
   	}
   	trainOutput[x]=-1;
   }
   int hand=0;
   int setPocketPattern=9;
   boolean increment=false;
   for(int x=0;x<25;x++){
	   trainInputs[x][numInputs-1]=1;
	   switch (x){
	   case 8:
		   increment=true;
		   break;
	   case 17:
		   increment=true;
		   break;
	   }
	   
	   if(increment==true){
		   hand=0;
		   setPocketPattern++;
		   increment=false;}
	   
	   trainInputs[x][setPocketPattern]=1;
	   trainInputs[x][hand]=1;
	   hand++;
   }
   String output="";
   for(int x=0;x<numPatterns;x++){
	   for(int y=0;y<numInputs;y++)
		   output+="|"+trainInputs[x][y];
	   output+="\n";
   }
   System.out.println(output);

}


//************************************
public static double tanh(double x)
{
   if (x > 20)
       return 1;
   else if (x < -20)
       return -1;
   else
       {
       double a = Math.exp(x);
       double b = Math.exp(-x);
       return (a-b)/(a+b);
       }
}


//************************************
public static void displayResults()
   {
    for(int i = 0;i<numPatterns;i++)
       {
       patNum = i;
       calcNet();
       //System.out.println("pat = " + (patNum+1) + " hand code" + trainOutputResults[patNum][2]+" pocket cards"+(trainOutputResults[patNum][3])+" actual = " + trainOutput[patNum] + " neural model = " + outPred);
       }
   }

//************************************
public static void calcOverallError()
   {
    RMSerror = 0.0;
    for(int i = 0;i<numPatterns;i++)
       {
       patNum = i;
       calcNet();
       RMSerror = RMSerror + (errThisPat * errThisPat);
       }
    RMSerror = RMSerror/numPatterns;
    RMSerror = java.lang.Math.sqrt(RMSerror);
   }

}
