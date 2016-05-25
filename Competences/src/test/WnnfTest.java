package test;

import java.io.IOException;
import java.util.List;

import preprocessing.*;
import util.Matrix;
import util.Wnnf;

public class WnnfTest {

  public static void main(String[] args) throws IOException {
    List<String[]> content = DataReader.readContent();
    String timeLimit = args.length > 0 ? args[0] : null;
    int min = args.length > 1 ? Integer.parseInt(args[1]) : -1;
    min = 100;
    List<String> studentIDs = DataExtractor.getValuesAt(content, 0);
    List<String> courseIDs = min != -1 ? DataExtractor.getSufficientCourses(content, min)
                                      : DataExtractor.getValuesAt(content, 1);
    double[][] matrix = DataExtractor.getValueMatrix(content, studentIDs, courseIDs, timeLimit);

    // String[][] out = DataWriter.combineCSV(studentIDs, courseIDs, outMatrix(matrix));
    // DataWriter.printCSVData(out, "output/cleanded.csv");

    // Transpose so that each stundent's grades are 1 column
    Matrix A = new Matrix(matrix).transpose();

    // System.out.println(W);

    DataWriter.exportMatrixToCsv(A, "output/W.csv");

    // Should be set equal to the number of competences defined
    int dimension = 4;

    Matrix Winit = Matrix.random(A.length(), dimension);
    Matrix Hinit = Matrix.random(dimension, A.width());

    int numIterations = 100000;
    Wnnf factorizer = new Wnnf(A, Winit, Hinit, numIterations);

    Matrix result = factorizer.getReconctructedMatrix();

    DataWriter.exportMatrixToCsv(result, "output/Wnnf.csv");

    // System.out.println(factorizer.getW());
    // System.out.println(factorizer.getH());
  }

}
