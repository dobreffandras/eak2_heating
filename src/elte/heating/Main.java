package elte.heating;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.util.*;

public class Main {

    @NotNull
    public static ReadableMatrix createInitialMatrix(){
        int N = 1024;
        int M = 1024;
        MatrixSplitterJoiner.boundaryConditions = Collections.unmodifiableMap(createBoundaryConditions(N, M, new Point(-1, 512)));

        WritableMatrix m = new WritableMatrix(N,M, new Point(0, 0));
        return MatrixSplitterJoiner.join(Arrays.asList(m));
    }

    private static Map<Point,Double> createBoundaryConditions(int N, int M, Point initialPoint) {
        Map<Point, Double> result = new HashMap<>();
        result.put(initialPoint, 100.0);
        return result;
    }

    public static void main(String[] args) {
        int iterationCount = 512;
        int replicationCount = 4;
        Map<HeatCalculatorFactory.CalculatingStretegy, List<Long>> calculationTimes = new HashMap<>();

        //sequental
        Run(calculationTimes, iterationCount, replicationCount, HeatCalculatorFactory.CalculatingStretegy.NotSplitted);

        //parallel matrix segmented into 2 parts
        Run(calculationTimes, iterationCount, replicationCount, HeatCalculatorFactory.CalculatingStretegy.Parallel_2);

        //parallel matrix segmented into 4 parts
        Run(calculationTimes, iterationCount, replicationCount, HeatCalculatorFactory.CalculatingStretegy.Parallel_4);

        //parallel matrix segmented into 8 parts
        Run(calculationTimes, iterationCount, replicationCount, HeatCalculatorFactory.CalculatingStretegy.Parallel_8);

        //parallel matrix segmented into 16 parts
        Run(calculationTimes, iterationCount, replicationCount, HeatCalculatorFactory.CalculatingStretegy.Parallel_16);

        //parallel matrix segmented into 1024 parts
        Run(calculationTimes, iterationCount, replicationCount, HeatCalculatorFactory.CalculatingStretegy.Parallel_1024);

        calculationTimes.forEach((k,v)->{
            double average = v.stream().mapToLong(x -> x).average().getAsDouble();
            double averageSeconds = (double)average / 1000000000.0;
            System.out.println("Calculation time average of "+ k.toString() + " is " + average + "("+averageSeconds+" s)");

            v.stream().mapToLong(x -> x).forEach( val -> {
                double seconds = (double)val / 1000000000.0;
                System.out.println("\t "+val + "("+seconds+" s)");
            });
        });
    }

    private static void Run(Map<HeatCalculatorFactory.CalculatingStretegy, List<Long>> calculationTimes, int iterationCount, int replicationCount, HeatCalculatorFactory.CalculatingStretegy strategy ) {
        HeatCalculator calculator = HeatCalculatorFactory.createCalculator(strategy);
        calculationTimes.put(strategy, new ArrayList<>());

        for(int i = 0; i<replicationCount; ++i){
            long start = System.nanoTime();
            ReadableMatrix m = Main.createInitialMatrix();
            ReadableMatrix resultMatrix = calculator.calculate(m, iterationCount);
            long end = System.nanoTime();

            Double resultHeat = resultMatrix.get(0,512);
            System.out.println("The value of (0,512) after iteration 512 is "+resultHeat + " (started at " + start + ")");
            calculationTimes.get(strategy).add(end-start);
            //writeResultMatrixToFile(resultMatrix, start);
        }
    }

    private static void writeResultMatrixToFile(ReadableMatrix resultMatrix, long startnanos) {
        try{
            PrintWriter writer = new PrintWriter("resultmatrix_"+startnanos+".txt", "UTF-8");
            for(int i = 0; i<resultMatrix.N; ++i){
                for(int j = 0; j<resultMatrix.M; ++j){
                    double v = resultMatrix.get(i,j);
                    DecimalFormat df = new DecimalFormat("#.#########");
                    writer.print(df.format(v)+" ");
                }
                writer.print(System.lineSeparator());
            }
            writer.close();
        } catch (IOException e) {
            // do something
        }
    }


}
