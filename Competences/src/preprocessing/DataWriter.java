package preprocessing;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class DataWriter {

	public static String[][] combineCSV(List<String>studentIDs,List<String>courseIDs,double[][]matrix){
		String[][] out = new String[studentIDs.size() + 1][courseIDs.size() + 1];

		for (int i = 0; i < studentIDs.size(); i++) {
			out[i + 1][0] = studentIDs.get(i);
			for (int j = 1; j < out[i].length; j++) {
				if (i == 0)
					out[0][j] = courseIDs.get(j - 1);
				out[i + 1][j] = matrix[i][j - 1] + "";
			}
		}
		return out;
	}
	
	public static void printCSVData(String[][]cleandedData,String fileName) throws IOException{
		FileWriter writer = new FileWriter(new File(fileName));
		for(int i=0;i<cleandedData.length;i++){
			for(int j=0;j<cleandedData[i].length;j++){
				writer.write(cleandedData[i][j]==null ? "," : cleandedData[i][j]+",");
			}
			writer.write("\n");
		}
		writer.close();
	}
}
