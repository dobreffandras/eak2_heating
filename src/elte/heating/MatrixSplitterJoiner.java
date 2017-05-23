package elte.heating;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by dobreffandras on 2017. 05. 23..
 */
public class MatrixSplitterJoiner {

    public static Map<Point, Double> boundaryConditions;

    public static ReadableMatrix join(List<WritableMatrix> matrices) {
        int N = matrices.stream().map(m -> m.N).mapToInt(x -> x).sum();
        int M = matrices.get(0).M;

        double[][] A = new double[N + 2][M + 2];

        boundaryConditions.forEach((p, v) -> {
            A[p.x + 1][p.y + 1] = v;
        });

        for(WritableMatrix m : matrices){
            Point p = m.topleft;
            double[][] mMatrix = m.getInternalMatrix();
            for (int i = 0; i < mMatrix.length; ++i) {
                for (int j = 0; j < mMatrix[i].length; ++j) {
                    A[p.x + i + 1][p.y + j + 1] = mMatrix[i][j];
                }
            }
        }

        return new ReadableMatrix(N, M, A);
    }

    public static List<WritableMatrix> split(int N, int M, int blockNumber) {
        int splittedMatrixrowCount = N / blockNumber;
        List<WritableMatrix> matrices = new ArrayList<>();
        for (int i = 0; i < blockNumber; ++i) {
            matrices.add(new WritableMatrix(splittedMatrixrowCount, M, new Point(i * splittedMatrixrowCount, 0)));
        }
        return matrices;
    }
}
