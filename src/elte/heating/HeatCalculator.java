package elte.heating;

/**
 * Created by dobreffandras on 2017. 05. 22..
 */
public interface HeatCalculator {
    ReadableMatrix calculate(ReadableMatrix m, int iterationCount);
}
