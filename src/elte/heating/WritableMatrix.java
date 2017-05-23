package elte.heating;

public class WritableMatrix {
    public final Point topleft;
    private final double[][] internalMatrix;
    public final int N;
    public final int M;

    public WritableMatrix(int n, int m, Point topleft) {
        this.topleft = topleft;
        N = n;
        M = m;
        this.internalMatrix = new double[N][M];
    }

    public synchronized  void set(int x, int y, Double value){
        this.internalMatrix[x-topleft.x][y-topleft.y] = value;
    }

    public synchronized double[][] getInternalMatrix(){
        return internalMatrix.clone();
    }
}
