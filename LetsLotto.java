import java.io.File;

public class LetsLotto {

	public static void main(String[] args){
		
//		LottoHistory getHistory = new LottoHistory(new File("input_lotto.csv"));
		LottoHistory getHistory = new LottoHistory();		
		NumGenerate getNum = new NumGenerate(getHistory.getHistory());
		
				

		System.out.println("static results: ");
		int arr[][] = getNum.getLottoNum();
		
		for(int i[]:arr){
			for(int d:i){
				System.out.print(d+" ");
			}
			System.out.println();
		}
		
		System.out.println("-----------------------------------------");
		System.out.println("random results: ");
		
		arr = getNum.getRandLottoNum();
		
		for(int i[]:arr){
			for(int d:i){
				System.out.print(d+" ");
			}
			System.out.println();
		}
		
		
		
		
	}
}

