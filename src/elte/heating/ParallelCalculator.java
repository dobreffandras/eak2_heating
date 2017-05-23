package elte.heating;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by dobreffandras on 2017. 05. 23..
 */
public class ParallelCalculator implements HeatCalculator{

    private final int blockNumber;
    private final ExecutorService executor;
    public ParallelCalculator(int blockCount){
        this.blockNumber = blockCount;
        this.executor = Executors.newFixedThreadPool(blockCount);
    }

    @Override
    public ReadableMatrix calculate(ReadableMatrix m, int iterationCount) {
        for(int i = 0; i< iterationCount; ++i){
            //System.out.println("Iteration: "+i);
            m = calculateNextMatrix(m);
        }
        return m;
    }

    private ReadableMatrix calculateNextMatrix(ReadableMatrix m) {
        try {
            List<WritableMatrix> matrices = MatrixSplitterJoiner.split(m.N, m.M, this.blockNumber);
            List<WritableMatrix> calculatedMatrices = calculateMatricesParallel(matrices, m);
            ReadableMatrix result = MatrixSplitterJoiner.join(calculatedMatrices);
            return result;
        } catch (InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }

    private List<WritableMatrix> calculateMatricesParallel(List<WritableMatrix> matrices, ReadableMatrix m) throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(matrices.size());
        final List<WritableMatrix> results = new ArrayList<>();
        for(WritableMatrix matrix : matrices) {
            executor.execute(() -> {
                WritableMatrix mtx = calculateMatrix(matrix, m);
                synchronized (results) {
                    results.add(mtx);
                }
                latch.countDown();
            });
        }

        latch.await();
        return results;
    }

    private static WritableMatrix calculateMatrix(WritableMatrix newMatrix, ReadableMatrix m){
        int startRow = newMatrix.topleft.x;
        int endRow = newMatrix.topleft.x +  newMatrix.N;
        for(int i = startRow; i < endRow; ++i){
            for(int j = 0; j < m.M; ++j){;
                newMatrix.set(i, j , new CalculateCell(i,j, m).call());
            }
        }
        return newMatrix;
    }
}
