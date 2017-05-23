package elte.heating;

import java.util.Arrays;

/**
 * Created by dobreffandras on 2017. 05. 22..
 */
public class SequentialCalculator implements HeatCalculator {

    public ReadableMatrix calculate(ReadableMatrix m, int iterationCount){
        for(int i = 0; i< iterationCount; ++i){
            m = calculateNextMatrix(m);
        }
        return m;
    }

    private ReadableMatrix calculateNextMatrix(ReadableMatrix m) {
        WritableMatrix newMatrix = new WritableMatrix(m.N, m.N, new Point(0, 0));
        for(int i = 0; i < m.N; ++i){
            for(int j = 0; j < m.N; ++j){;
                newMatrix.set(i, j , new CalculateCell(i,j, m).call());
            }
        }
        return MatrixSplitterJoiner.join(Arrays.asList(newMatrix));
    }
}
