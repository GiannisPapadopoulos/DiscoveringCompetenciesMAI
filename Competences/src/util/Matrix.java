package util;

import java.util.Arrays;

import happy.coding.math.Maths;


public class Matrix {

	private int m, n;

	public double[][] saved;

	public Matrix(double[][] in) {
		m = in.length;
		n = in[0].length;
		saved = new double[m][n];
		for(int i=0;i<m;i++){
			for(int j=0;j<n;j++)
				this.saved[i][j] = in[i][j];
		}
	}
	
	public Matrix(Matrix m){
		this(m.saved);
	}

	public Matrix mult(Matrix m2) {
		double[][] out = new double[this.m][m2.n];
		for (int i = 0; i < this.m; i++) {
			for (int j = 0; j < m2.n; j++) {
				for (int k = 0; k < this.n; k++)
					out[i][j] += this.saved[i][k] * m2.saved[k][j];
			}
		}
		return new Matrix(out);
	}

	public Matrix transpose() {
		double[][] t = new double[n][m];
		for (int i = 0; i < m; i++) {
			for (int j = 0; j < n; j++) {
				t[j][i] = saved[i][j];
			}
		}
		return new Matrix(t);
	}

	public Matrix subst(Matrix m2) {
		double[][] out = new double[m][n];
		for (int i = 0; i < m; i++) {
			for (int j = 0; j < n; j++) {
				out[i][j] = saved[i][j] - m2.saved[i][j];
			}
		}
		return new Matrix(out);
	}

	public Matrix concatV(Matrix m2) {
		double[][] out = new double[m + m2.m][n];

		for (int j = 0; j < n; j++) {
			for (int i = 0; i < m; i++) {
				out[i][j] = saved[i][j];
			}
			for (int i = 0; i < m2.m; i++) {
				out[i + m][j] = m2.saved[i][j];
			}
		}
		return new Matrix(out);
	}

	public double norm() {
		double e = 0;
		for (int i = 0; i < m; i++) {
			for (int j = 0; j < n; j++) {
				e += saved[i][j] * saved[i][j];
			}
		}
		e = Math.sqrt(e);
		return e;
	}

	public double normF() {
		double f = 0;
		for (int i = 0; i < m; i++) {
			for (int j = 0; j < n; j++) {
				f = Maths.hypot(f, saved[i][j]);
			}
		}
		return f;
	}

	public Matrix booleanConcat(Matrix other) {
		double[][] out = new double[m][n];
		for (int i = 0; i < m; i++) {
			for (int j = 0; j < n; j++) {
				if (saved[i][j] < 0 || other.saved[i][j] > 0)
					out[i][j]=1;
				else
					out[i][j]=0;
			}
		}
		return new Matrix(out);
	}

	public Matrix mult(double alpha) {
		double[][] out = new double[m][n];
		for (int i = 0; i < m; i++)
			for (int j = 0; j < n; j++)
				out[i][j] = saved[i][j] * alpha;
		return new Matrix(out);
	}

	public Matrix discardZero() {
		double[][]out = new double[m][n];
		for (int i = 0; i < m; i++)
			for (int j = 0; j < n; j++)
				out[i][j] = saved[i][j]<0? 0: saved[i][j];
		return new Matrix(out);
	}

	public Matrix dotMult(Matrix d) {
		double[][]out = new double[m][n];
		for (int i = 0; i < m; i++)
			for (int j = 0; j < n; j++)
				out[i][j] = saved[i][j]*d.saved[i][j];
		return new Matrix(out);
	}

	public double sumValues() {
		double sum = 0;
		for (int i = 0; i < m; i++)
			for (int j = 0; j < n; j++)
				sum += saved[i][j];
		return sum;
	}
	
	public boolean equals(Matrix obj) {
		if(obj == null)
			return false;
		return Arrays.equals(saved,obj.saved);
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(m+" x "+n+"\n");
		for(double[] row: saved)
			builder.append(Arrays.toString(row)+"\n");
		return builder.toString();
	}
}
