package test;


import java.io.IOException;
import java.util.List;

import nmf.NMF;
import preprocessing.*;


public class Test {

	public static void main(String[] args) throws IOException {
		List<String[]>content = DataReader.readContent();
		String timeLimit = args.length>0 ? args[0] : null;
		int min = args.length>1 ? Integer.parseInt(args[1]) : -1;
		min = 100;
		List<String> studentIDs = DataExtractor.getValuesAt(content, 0);
		List<String> courseIDs = min!=-1 ? DataExtractor.getSufficientCourses(content, min) : DataExtractor.getValuesAt(content, 1);
		double[][] matrix = DataExtractor.getValueMatrix(content, studentIDs, courseIDs, timeLimit);
	
		
		
		String[][] out = DataWriter.combineCSV(studentIDs, courseIDs, outMatrix(matrix));
    DataWriter.printCSVData(out, "output/cleanded.csv");
		
		double[][] winit = NMF.random(matrix.length, 2);
		double[][] hinit = NMF.random(2, matrix[0].length);

		NMF.MultResult nmf = NMF.nmf(matrix, winit, hinit, 0.001, 1000);
		double[][] result = nmf.matrix.mult(nmf.grad).saved;
		
		out = DataWriter.combineCSV(studentIDs, courseIDs, outMatrix(result));
    DataWriter.printCSVData(out, "output/nmfResult.csv");
	}
	
	public static double[][] outMatrix(double[][] matrix){
		double[][] outMatrix = new double[matrix.length][matrix[0].length];
		for(int i=0;i<matrix.length;i++)
			for(int j=0;j<matrix[i].length;j++)
				outMatrix[i][j] = matrix[i][j]-1;
		
		return outMatrix;
	}
	
}
