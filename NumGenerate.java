import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NumGenerate {
	
	public static final int limit = 45;

	private int[][] lottoNum;
	private int[][] randomLottoNum;
	private Map<Integer, Integer> frequency;
	
	private int firstMax;
	private int lastMin;
	
	public NumGenerate(ArrayList<List<Integer>> history){
		
		firstMax = findFirstMax(getIdxNums(history,0));
		lastMin = findLastMin(getIdxNums(history,5));
		
		lottoNum = new int[10][6];
		double[][] getNum = getNum(history);
		for(int i=0;i<10;i++){
			lottoNum[i] = doubleToIntArr(getNum[i]);
		}
		
		randomLottoNum = new int[10][6];
		int[] randMinInput = new int[10];
		int[] randMaxInput = new int[10];
		for(int i=0;i<10;i++){
			randMinInput[i] = (int) Math.round((Math.random()*firstMax));
			randMaxInput[i] = (int) Math.round((Math.random()*(limit-lastMin))+lastMin);
		}
		
		double[][] getNumRand = getNumByInput(history,randMinInput,randMaxInput);
		for(int i=0;i<10;i++){
			randomLottoNum[i] = doubleToIntArr(getNumRand[i]);
		}
	}
	
	//count frequency from lotto data
	public void countFrequency(ArrayList<List<Integer>> input){
		
		for(List<Integer> x:input){
			for(int tmp:x){
				if(frequency.containsKey(tmp)){
					frequency.put(tmp, frequency.get(tmp)+1);
				}
				else{
					frequency.put(tmp, 1);
				}
			}
		}
		
	}
	
	//find max value in first row
	public int findFirstMax(int[] input){
		int max=0;
		
		for(int num:input){
			if(max<num) max=num;
		}
		
		return max;
	}
	
	//find min value in last row(except bonus number)
	public int findLastMin(int[] input){
		int min=limit;
		
		for(int num:input){
			if(min>num) min=num;
		}
		
		return min;
	}
	
	//find most frequent number in input row
	public int maxCountForIdx(int[] input){
		//그 순서에서 가장 많이 나온 숫자
		Map<Integer,Integer> fre = new HashMap<Integer,Integer>();
		
		
		for(int key:input){
			if(fre.containsKey(key)){
				fre.put(key,fre.get(key)+1);
			}
			else{
				fre.put(key, 1);
			}
		}
		
		int max_key=0;
		int max_fre=0;
		
		for(int key:fre.keySet()){
			if(fre.get(key)>=max_fre){
				max_key=key;
				max_fre=fre.get(key);
			}
		}
		
		return max_key;
		
	}
	
	//find least frequent number in input row
	public int minCountForIdx(int[] input){
		//그 순서에서 가장 많이 나온 숫자
		Map<Integer,Integer> fre = new HashMap<Integer,Integer>();
		
		
		for(int key:input){
			if(fre.containsKey(key)){
				fre.put(key,fre.get(key)+1);
			}
			else{
				fre.put(key, 1);
			}
		}
		
		int min_key=0;
		int min_fre=input.length;
		
		for(int key:fre.keySet()){
			if(fre.get(key)<min_fre){
				min_key=key;
				min_fre=fre.get(key);
			}
		}
		
		return min_key;
		
	}
	
	//find parameter for simple linear regression
	public double[] getLinearReg(int[] x, int[] y, int N){
		double w[]={0,0};
		
		double sumxy=0;
		double sumy=0;
		double sumx=0;
		double sumx2=0;
		
		for(int i=0;i<N;i++){
			sumxy+=x[i]*y[i];
			sumy+=y[i];
			sumx+=x[i];
			sumx2+=Math.pow(x[i], 2);
		}
		
		w[1] = (sumxy-(sumx/N)*sumy)/(sumx2-N*Math.pow(sumx/N, 2));
		w[0] = sumy/N-(w[1]*(sumx/N));
		
		return w;
		
	}
	
	//find double type lotto numbers start with min value
	public double[] getNumFromMin(ArrayList<List<Integer>> history, int minNum){
		double genNum[]= new double[6];
		
		double w[];
		
		genNum[0]=minNum;
		
		for(int i=0;i<5;i++){
			w = getLinearReg(getIdxNums(history,i),getIdxNums(history,i+1),history.size());
			
			genNum[i+1]=genNum[i]*w[1]+w[0];
			
		}
		
		return genNum;
		
	}
	
	//find double type lotto numbers start with max value 
	public double[] getNumFromMax(ArrayList<List<Integer>> history, int maxNum){
		double genNum[]= new double[6];
		
		double w[];
		
		genNum[5]=maxNum;
		
		for(int i=5;i>0;i--){
			w = getLinearReg(getIdxNums(history,i),getIdxNums(history,i-1),history.size());
			
			genNum[i-1]=genNum[i]*w[1]+w[0];
			
		}
		
		return genNum;
		
	}

	//generate double type lotto numbers with static values
	public double[][] getNum(ArrayList<List<Integer>> history){
		double getNumMin[][] = new double[3][6];
		double getNumMax[][] = new double[3][6];
		
		double getNum[][] = new double[10][6];
		
		getNumMin[0] = getNumFromMin(history,maxCountForIdx(getIdxNums(history,0)));
		getNumMin[1] = getNumFromMin(history,minCountForIdx(getIdxNums(history,0)));
		getNumMin[2] = getNumFromMin(history,(firstMax+1)/2);
		
		getNumMax[0] = getNumFromMax(history,maxCountForIdx(getIdxNums(history,5)));
		getNumMax[1] = getNumFromMax(history,minCountForIdx(getIdxNums(history,5)));
		getNumMax[2] = getNumFromMax(history,(lastMin+limit)/2);
		
		int idx=0;
		while(idx<9){
			for(int j=0;j<3;j++){
				for(int k=0;k<3;k++){
					for(int i=0;i<6;i++){
						getNum[idx][i] = (getNumMin[j][i]+getNumMax[k][i])/2;
					}
					idx++;
				}
			}
		}
	
		getNum[9] = getNumFromMin(history,firstMax+lastMin-limit);
		
		return getNum;
		
	}
	
	//generate double type lotto numbers with min inputs and max inputs (for random lotto numbers)
	public double[][] getNumByInput(ArrayList<List<Integer>> history,int[] minInput, int[] maxInput){
		double getNumMin[];
		double getNumMax[];
		
		double getNum[][] = new double[10][6];
		
		int idx=0;
		while(idx<10){
			getNumMin = getNumFromMin(history,minInput[idx]);
			getNumMax = getNumFromMax(history,maxInput[idx]);
			
			for(int i=0;i<6;i++){
				getNum[idx][i] = (getNumMin[i]+getNumMax[i])/2;
			}
			idx++;
		}
		
		return getNum;
		
	}
	
	//return select row
	public int[] getIdxNums(ArrayList<List<Integer>> history, int idx){
		ArrayList<Integer> retList = new ArrayList<Integer>();
		
		for(List<Integer> list:history){
			retList.add(list.get(idx));
		}
		
		int arr[] = new int[retList.size()];
		
		for(int i=0;i<retList.size();i++){
			arr[i] = retList.get(i);
		}
		
		return arr;
	}
	
	//convert double type array to int type array
	public int[] doubleToIntArr(double[] arr){
		int[] retArr = new int[arr.length];
		
		for(int i=0;i<arr.length;i++){
			retArr[i] = (int) Math.round(arr[i]);
		}
		
		return retArr;
	}
	
	//return static lotto number
	public int[][] getLottoNum(){
		
		//check duplication............
		for(int i=0;i<lottoNum.length;i++){
			lottoNum[i] = DelDuplicate(lottoNum[i]);
		}
		
		return lottoNum;
	}
	
	//return random lotto number
	public int[][] getRandLottoNum(){
		//check duplication............
		for(int i=0;i<randomLottoNum.length;i++){
			randomLottoNum[i] = DelDuplicate(randomLottoNum[i]);
		}
		
		return randomLottoNum;
	}
	
	//remove duplication
	public int[] DelDuplicate(int[] arr){
		boolean b=true;
		
		while(true){
			for(int i=0;i<arr.length-1;i++){
				if(arr[arr.length-1]==arr[i]){
					b=false;
					arr[arr.length-1]+=1;
				}
			}
			if(b) break;
			b = true;
		}
		
		return arr;
	}
	
	
}

