package util;

public class Wnnf {

  private Matrix A;
  private Matrix W;
  private Matrix H;

  /** Matrix with 0 in the positions corresponding to missing values and 1 for the rest */
  private Matrix weights;

  /**
   * @param A
   *        The matrix to be factorized as W*H, nmissing values represented by -1
   * @param Winit
   *        Initial estimate for W
   * @param Hinit
   *        Initial estimate for H
   */
  public Wnnf(Matrix A, Matrix Winit, Matrix Hinit, int numIterations) {
    this.A = A;
    this.W = Winit;
    this.H = Hinit;
    double[][] weightValues = new double[A.length()][A.width()];
    for (int i = 0; i < weightValues.length; i++) {
      for (int j = 0; j < weightValues[0].length; j++) {
        weightValues[i][j] = A.saved[i][j] < 0 ? 0 : 1;
      }
    }
    weights = new Matrix(weightValues);
    run(numIterations);
  }

  /** Returns the original matrix */
  public Matrix getA() {
    return A;
  }

  /** Returns the first element of the product */
  public Matrix getW() {
    return W;
  }

  /** Returns the second element of the product */
  public Matrix getH() {
    return H;
  }

  public Matrix getReconctructedMatrix() {
    return W.mult(H);
  }

  private void run(int numIterations) {
    System.out.println(calculateAbsoluteError(A, W, H, weights));
    for (int i = 0; i < numIterations; i++) {
      performUpdate();
    }
    System.out.println("sum of errors " + calculateAbsoluteError(A, W, H, weights));

    // System.out.println(W.mult(H));
  }

  /** Implementation from http://www.siam.org/meetings/sdm06/proceedings/059zhangs2.pdf */
  private void performUpdate() {
    Matrix weightedA = weights.dotMult(A);
    Matrix wUpdateNumerator = weightedA.mult(H.transpose());
    Matrix wUpdateDenominator = (weights.dotMult(W.mult(H))).mult(H.transpose());

    Matrix Wnew = W.dotMult(wUpdateNumerator).dotDivide(wUpdateDenominator);

    W = Wnew;

    if (W.containsNaN()) {
      System.out.println("Error");
    }

    Matrix hUpdateNumerator = W.transpose().mult(weightedA);
    Matrix hUpdateDenominator = W.transpose().mult(weights.dotMult(W.mult(H)));

    double[][] Hnew = new double[H.length()][H.width()];

    for (int i = 0; i < H.length(); i++) {
      for (int j = 0; j < H.width(); j++) {
        double Hij = H.saved[i][j] * hUpdateNumerator.saved[i][j];
        Hnew[i][j] = (Hij == 0) ? 0 : Hij / hUpdateDenominator.saved[i][j];
        // if (hUpdateDenominator.saved[i][j] == 0) {
        // System.out.println(hUpdateNumerator.saved[i][j] + " " + hUpdateDenominator.saved[i][j] + " " +
        // H.saved[i][j]);
        // }
      }
    }

    // Matrix Hnew = H.dotMult(hUpdateNumerator).dotDivide(hUpdateDenominator);

    H = new Matrix(Hnew);

    if (H.containsNaN()) {
      System.out.println("Error");
    }

  }

  /**
   * Calculates the absolute error of the decomposition of matrix A into W and H, abs(A - W*H)
   */
  public static double calculateAbsoluteError(Matrix A, Matrix W, Matrix H, Matrix weights) {
    double[][] result = W.mult(H).saved;

    double absoluteError = 0;
    for (int i = 0; i < A.saved.length; i++) {
      for (int j = 0; j < A.saved[0].length; j++) {
        absoluteError += weights.saved[i][j] * Math.abs(A.saved[i][j] - result[i][j]);
      }
    }
    return absoluteError;
  }
}
