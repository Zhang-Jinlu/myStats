package nuist.zjl.algorithm;

import nuist.zjl.exception.VectorDimensionalityException;
import nuist.zjl.pojo.CorrelationResult;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.*;

public class Correlation {

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
        BigDecimal r = numOfR.divide(denOfR, new MathContext(PRECISION));
        System.out.println("pearson: " + r.toString());
        return null;
    }

    public CorrelationResult spearman(List<BigDecimal> v1, List<BigDecimal> v2) throws VectorDimensionalityException {
        BigDecimal[] rank1 = getRankList(v1);
        BigDecimal[] rank2 = getRankList(v2);
        int n = rank1.length;
        BigDecimal sumOfD2 = BigDecimal.valueOf(0.0);
        for (int i = 0; i < n; i++) {
            sumOfD2 = sumOfD2.add(rank1[i].subtract(rank2[i]).pow(2));
        }
        BigDecimal numOfR = sumOfD2.multiply(BigDecimal.valueOf(6));
        BigDecimal denOfR = BigDecimal.valueOf(n*(Math.pow(n, 2)-1));
        BigDecimal r = BigDecimal.valueOf(1).subtract(numOfR.divide(denOfR, new MathContext(PRECISION)));
        System.out.println("spearman: " + r.toString());
        return null;
    }

    public BigDecimal[] getRankList(List<BigDecimal> v) {
        List<BigDecimal> sorted = new ArrayList<>();
        // 复制原向量
        for (BigDecimal bigDecimal : v) {
            sorted.add(bigDecimal);
        }
        Collections.sort(sorted);  // 将向量排序
        List<Integer> rankList = new ArrayList<>();
        Map<BigDecimal, Integer> preRankMap = new HashMap<>();
        Integer tempRank;
        Integer preRank;
        // 获取向量等级列表
        for (BigDecimal temp : v) {
            tempRank = sorted.indexOf(temp) + 1;
            preRank = preRankMap.get(temp);
            if (preRank != null) {
                tempRank = preRank + 1;
            }
            preRankMap.put(temp, tempRank);
            rankList.add(tempRank);
        }
        // 将rankList复制到res
        BigDecimal[] res = new BigDecimal[rankList.size()];
        for (int i = 0; i < rankList.size(); i++) {
            res[i] = BigDecimal.valueOf(rankList.get(i));
        }
        BigDecimal pre1 = null;
        List<Integer> tempList = new ArrayList<>();
        // 将值相同的项的rank调整为相同
        for (int i = 0; i < sorted.size(); i++) {
            BigDecimal temp = sorted.get(i);
            if (pre1 == null) {
                tempList.add(i + 1);
                pre1 = temp;
            } else {
                if (temp.equals(pre1)) {
                    tempList.add(i + 1);
                } else {
                    BigDecimal rank = BigDecimal.valueOf(tempList.get(0) + tempList.get(tempList.size() - 1)).divide(BigDecimal.valueOf(2));
                    for (int j = 0; j < tempList.size(); j++) {
                        res[rankList.indexOf(tempList.get(j))] = rank;
                    }
                    pre1 = temp;
                    tempList.clear();
                    tempList.add(i + 1);
                }
            }
        }
        return res;
    }

    public static void main(String[] args) throws VectorDimensionalityException {
        Correlation correlationAnalysis = new Correlation();
        int[] x = {1, 3, 6, 8, 10, 19};
        int[] y = {7, 10, 18, 17, 31, 33};
        List<BigDecimal> v1 = new ArrayList<>();
        List<BigDecimal> v2 = new ArrayList<>();
        for (int i = 0; i < x.length; i++) {
            v1.add(new BigDecimal(x[i]));
        }
        for (int i = 0; i < y.length; i++) {
            v2.add(new BigDecimal(y[i]));
        }
        correlationAnalysis.spearman(v1, v2);
        correlationAnalysis.pearson(v1, v2);
    }
}
