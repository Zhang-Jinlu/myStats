package nuist.zjl.algorithm;

import nuist.zjl.exception.VectorDimensionalityException;
import nuist.zjl.pojo.CorrelationResult;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.*;

public class CorrelationAnalysis {

    private final int PRECISION = 10;

    public CorrelationResult pearson(List<BigDecimal> v1, List<BigDecimal> v2) throws VectorDimensionalityException {
        if (v1.size() != v2.size()) {
            throw new VectorDimensionalityException();
        }
        int n = v1.size();
        BigDecimal sumX = new BigDecimal(0.0);
        BigDecimal sumY = new BigDecimal(0.0);
        BigDecimal sumXY = new BigDecimal(0.0);
        BigDecimal sumXPow = new BigDecimal(0.0);
        BigDecimal sumYPow = new BigDecimal(0.0);

        for (int i = 0; i < n; i++) {
            BigDecimal x = v1.get(i);
            BigDecimal y = v2.get(i);
            sumX = sumX.add(x);
            sumY = sumY.add(y);
            sumXY = sumXY.add(x.multiply(y));
            sumXPow = sumXPow.add(x.pow(2));
            sumYPow = sumYPow.add(y.pow(2));
        }

        BigDecimal numOfR = sumXY.subtract(sumX.multiply(sumY).divide(new BigDecimal(n), new MathContext(PRECISION)));
        BigDecimal denOfR = sumXPow.subtract(sumX.pow(2).divide(new BigDecimal(n), new MathContext(PRECISION))).multiply(
                sumYPow.subtract(sumY.pow(2).divide(new BigDecimal(n), new MathContext(PRECISION)))).sqrt(new MathContext(PRECISION));
        System.out.println(numOfR.divide(denOfR, new MathContext(PRECISION)));

        return null;
    }

    public CorrelationResult spearman(List<BigDecimal> v1, List<BigDecimal> v2) throws VectorDimensionalityException {
        List<BigDecimal> rank1 = getRankList(v1);
        List<BigDecimal> rank2 = getRankList(v2);

        return null;
    }

    public List<BigDecimal> getRankList(List<BigDecimal> v) {
        List<BigDecimal> sorted = new ArrayList<>();
        for (BigDecimal bigDecimal : v) {
            sorted.add(bigDecimal);
        }
        Collections.sort(sorted);
        List<Integer> rankList = new ArrayList<>();
        for (BigDecimal bigDecimal : v) {
            rankList.add(sorted.indexOf(bigDecimal) + 1);
        }
        List<BigDecimal> res = new ArrayList<>();
        BigDecimal pre = null;
        List<Integer> tempList = new ArrayList<>();
        for (int i = 0; i < sorted.size(); i++) {
            BigDecimal temp = sorted.get(i);
            if (pre == null) {
                tempList.add(i+1);
                pre = temp;
            } else {
                if (temp.equals(pre)) {
                    tempList.add(i+1);
                } else {
                    BigDecimal rank = BigDecimal.valueOf(tempList.get(0) + tempList.get(tempList.size() - 1)).divide(BigDecimal.valueOf(2));
                    for (int j = 0; j < tempList.size(); j++) {
                        res.add(rank);
                    }
                    pre = temp;
                    tempList.clear();
                    tempList.add(i+1);
                }
            }
        }
        return res;
    }

    public static void main(String[] args) throws VectorDimensionalityException {
        CorrelationAnalysis correlationAnalysis = new CorrelationAnalysis();
        int[] x = {2, 7, 7, 7, 7, 90, 177, 570};
        int[] y = {999, 3, 5, 15, 90, 180, 88, 160, 580};
        List<BigDecimal> v1 = new ArrayList<>();
        List<BigDecimal> v2 = new ArrayList<>();
        for (int i = 0; i < x.length; i++) {
            v1.add(new BigDecimal(x[i]));
            v2.add(new BigDecimal(y[i]));
        }
        v2.add(BigDecimal.valueOf(y[y.length-1]));
        Collections.sort(v1);
        System.out.println(v2);
        List<BigDecimal> rankList = correlationAnalysis.getRankList(v2);
        System.out.println(rankList);
    }
}
