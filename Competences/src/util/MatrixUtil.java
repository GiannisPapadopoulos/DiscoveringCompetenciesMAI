package util;


public class MatrixUtil {

	public static double[][] mult(double[][] m1, double[][] m2) {
		double[][] out = new double[m1.length][m2[0].length];
		for (int i = 0; i < m1.length; i++) {
			for (int j = 0; j < m2[i].length; j++) {
				for (int k = 0; k < m1[0].length; k++)
					out[i][j] += m1[i][k] * m2[k][j];
			}
		}
		return out;
	}
	
	public static double[][] transpose(double[][]m1){
		double[][]t = new double[m1[0].length][m1.length];
		for(int i=0;i<m1.length;i++){
			for(int j=0;j<m1[i].length;j++){
				t[i][j] = m1[j][i];
			}
		}
		return t;
	}
	
}
