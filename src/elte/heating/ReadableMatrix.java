package elte.heating;

/**
 * Created by dobreffandras on 2017. 05. 23..
 */
public class ReadableMatrix {

    public final int N;
    public final int M;
    private final double[][] internalMatrix;

    public ReadableMatrix(int N, int M, double[][] internalRepresentation) {
        this.N = N;
        this.M = M;
        this.internalMatrix = internalRepresentation;
    }

    public double get(int x, int y) {
        return this.internalMatrix[x+1][y+1];
    }
}
