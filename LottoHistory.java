import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class LottoHistory {
	
	private ArrayList<List<Integer>> history;  
	
	public LottoHistory(){
		readStdin();
	}
	
	//parsing csv file
	public void readStdin(){
		try {
			Scanner stdinScan = new Scanner(System.in);
			Scanner splitLine;
			history = new ArrayList<List<Integer>>();
			
			while(fileScan.hasNextLine()){
				ArrayList<Integer> tmpArray = new ArrayList<Integer>();
				splitLine = new Scanner(stdinScan.nextLine());
				splitLine.useDelimiter(",");
				while(splitLine.hasNext()){
					tmpArray.add(Integer.parseInt(splitLine.next()));
				}
				splitLine.close();
				history.add(tmpArray);
			}
			stdinScan.close();
				
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public ArrayList<List<Integer>> getHistory(){
		return history;
	}

}

