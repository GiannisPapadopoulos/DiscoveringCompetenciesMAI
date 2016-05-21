package nmf;

import java.util.Date;

import util.Matrix;

public class NMF {

	public static MultResult nmf(double[][] Vinit, double[][] Winit, double[][] Hinit, double tol, int maxiter) {
		Matrix V = new Matrix(Vinit);
		Matrix W = new Matrix(Winit);
		Matrix H = new Matrix(Hinit);
		Matrix Vt = V.transpose();
		Matrix Ht = H.transpose();
		Matrix Wt = W.transpose();

		Matrix gradW = W.mult(H.mult(Ht)).subst(V.mult(Ht));
		Matrix gradH = Wt.mult(W).mult(H).subst(Wt.mult(V));

		double initGrad = gradW.concatV(gradH).normF();
		double tolW = Math.max(0.001, tol);
		double tolH = tolW;

		int i = 0;
		for (; i < maxiter; i++) {
			double projnorm = gradW.booleanConcat(W).concatV(gradH.booleanConcat(H)).norm();

			if (projnorm < tol * initGrad)
				break;

			MultResult w = nlssubprop(Vt, H.transpose(), W.transpose(), tolW, 1000);
			W = new Matrix(w.matrix.transpose());
			gradW = new Matrix(w.grad.transpose());

			if (w.iter == 0) {
				tolW *= 0.1;
			}

			MultResult h = nlssubprop(V, W, H, tolH, 1000);
			H = new Matrix(h.matrix);
			gradH = new Matrix(h.grad);

			if (h.iter == 0) {
				tolH *= 0.1;
			}
			if (i % 100 == 0)
				System.out.println("Step:" + i + "\t" + projnorm + "\t" + tolW + "\t" + tolH + "\t" + new Date());
		}

		return new MultResult(W, H, i);
	}

	public static MultResult nlssubprop(Matrix V, Matrix W, Matrix Hinit, double tol, int maxiter) {
		Matrix H = new Matrix(Hinit);
		Matrix WtV = W.transpose().mult(V);
		Matrix WtW = W.transpose().mult(W);

		double alpha = 1;
		double betha = 0.1;

		int iter = 0;
		Matrix grad = null;
		for (iter = 0; iter < maxiter; iter++) {
			grad = WtW.mult(H).subst(WtV);
			double projgrad = grad.booleanConcat(H).norm();

			if (projgrad < tol)
				break;

			boolean decr_alpha = false;
			Matrix Hp = null;

			for (int inner_iter = 0; inner_iter < 20; inner_iter++) {
				Matrix Hn = H.subst(grad.mult(alpha)).discardZero();
				Matrix d = Hn.subst(H);

				double gradd = grad.dotMult(d).sumValues();
				double dQd = WtW.mult(d).dotMult(d).sumValues();
				boolean suff_decr = (0.99 * gradd + 0.5 * dQd) < 0;
				if (inner_iter == 0) {
					decr_alpha = !suff_decr;
					Hp = new Matrix(H);
				}

				if (decr_alpha) {
					if (suff_decr) {
						H = new Matrix(Hn);
						break;
					} else {
						alpha *= betha;
					}
				} else {
					if (!suff_decr || Hn.equals(Hp)) {
						H = new Matrix(Hp);
						break;
					} else {
						alpha /= betha;
						Hp = new Matrix(Hn);
					}
				}
			}
		}

		return new MultResult(H, grad, iter);
	}

	public static class MultResult {
		public Matrix matrix;
		public Matrix grad;
		public int iter;

		public MultResult(Matrix m, Matrix g, int i) {
			matrix = m;
			grad = g;
			iter = i;
		}
	}
	
	public static double[][] random(int row, int col) {
		double[][] out = new double[row][col];
		for (int i = 0; i < row; i++)
			for (int j = 0; j < col; j++)
				out[i][j] = Math.random()*3;
		return out;
	}
}
