import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.IOException;

public class writeFile {
	private String path;
	private boolean appendToFile=false;
	public writeFile(String filePath){
		path=filePath;
	}
	
	public writeFile(String filePath, boolean appendValue){
		path=filePath;
		appendToFile=appendValue;
		//System.out.println("file path"+path);
	}
	
	public void writeToFile(String textEntry) throws IOException{
		FileWriter write = new FileWriter(path, appendToFile);
		PrintWriter printLine = new PrintWriter(write);
		printLine.printf("%s\n", textEntry);
		
		printLine.close();
	}
	
	public long totalSum(int column){
		long result=0;
		try{
			BufferedReader file = new BufferedReader(new FileReader(path));
	        String line;
			while((line=file.readLine())!=null){
				String[] thisLine = line.split("\\|");
				result+=Long.parseLong(thisLine[column]);
			}
		}catch(Exception e){
			System.out.println("Error Reading File");
		}
		return result;
	}

	public long totalSum(int column, String contains){
		long result=0;
		try{
			BufferedReader file = new BufferedReader(new FileReader(path));
	        String line;
			while((line=file.readLine())!=null){
				if(line.contains(contains)){
					String[] thisLine = line.split("\\|");
					result+=Long.parseLong(thisLine[column]);					
				}
			}
		}catch(Exception e){
			System.out.println("Error Reading File");
		}
		return result;
	}
	
	public int totalRows(String ofThese){
		int total=0;
		try{
			BufferedReader file = new BufferedReader(new FileReader(path));
	        String line;
			while((line=file.readLine())!=null){
				if(line.contains(ofThese))
					total++;
			}
		}catch(Exception e){
			System.out.println("Error Reading File");
		}
		return total;
	}
		
	public static void increaseScore(String path, String addToThis, String delimiter){
		try{
			String[] increaseThis = addToThis.split(delimiter);
			boolean match;
			BufferedReader file = new BufferedReader(new FileReader(path));
	        String line;String input = "";
			
	        while((line=file.readLine())!=null){
	        	match=true;
	        	String[] recordArray = line.split(delimiter);
	        	//System.out.println("increaseThis length"+increaseThis.length + "recordArray"+recordArray.length);
	        	int currentVal = (recordArray.length-1);
	        	for(int x=0; x<increaseThis.length&&match==true;x++){
	        		//System.out.println("Comparing" + increaseThis[x]+" and "+recordArray[x]);
	        		if(increaseThis[x].equals(recordArray[x])==false)
	        			match=false;
	        	}
	        	
				if(match==true){
					line=recordArray[0];
					int temp = Integer.parseInt(recordArray[currentVal]);
					temp++;
					recordArray[currentVal]=temp+"";
					for(int x=1;x<recordArray.length;x++){
						line+="|"+recordArray[x];
					}
					//System.out.println(line);
				}
				input+=line+"\n";	
			}
	        FileOutputStream newFile = new FileOutputStream(path);
	        newFile.write(input.getBytes());
		}catch(Exception e){
			System.out.println("Error Reading File"+e);
		}
	}
	
	public static void main(String[]args) throws IOException{
		
	}
}
