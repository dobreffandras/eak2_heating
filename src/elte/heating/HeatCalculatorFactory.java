package elte.heating;

/**
 * Created by dobreffandras on 2017. 05. 22..
 */
public class HeatCalculatorFactory {

    public enum CalculatingStretegy {
        NotSplitted(1),
        Parallel_2(2),
        Parallel_4(4),
        Parallel_8(8),
        Parallel_16(16),
        Parallel_215(215),
        Parallel_1024(1024);

        int separateBlockCount;

        CalculatingStretegy(int separateBlockCount) {
            this.separateBlockCount = separateBlockCount;
        }

        @Override
        public String toString() {
            return separateBlockCount == 1 ? "NotSplittedStategy" : separateBlockCount+"-SplittedStrategy";
        }
    }

    public static HeatCalculator createCalculator(CalculatingStretegy strategy) {
        if (strategy == CalculatingStretegy.NotSplitted) {
            return new SequentialCalculator();
        } else {
            return new ParallelCalculator(strategy.separateBlockCount);
        }

    }
}
