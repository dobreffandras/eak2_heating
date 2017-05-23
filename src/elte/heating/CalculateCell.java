package elte.heating;

import java.util.concurrent.Callable;

/**
 * Created by dobreffandras on 2017. 05. 22..
 */
public class CalculateCell implements Callable<Double> {

    private final int x;
    private final int y;
    private final ReadableMatrix u;
    private static final double r = 0.2;

    public CalculateCell(int x, int y, ReadableMatrix prevoiusmatrix){
        this.x = x;
        this.y = y;
        u = prevoiusmatrix;
    }

    @Override
    public Double call() {
        Double prev = u.get(x, y);
        Double top = u.get(x-1, y);
        Double bottom = u.get(x+1, y);
        Double left = u.get(x, y-1);
        Double right = u.get(x, y+1);
        return prev + r * ( bottom - 2*prev + top) + r*(right - 2*prev + left);
    }
}
