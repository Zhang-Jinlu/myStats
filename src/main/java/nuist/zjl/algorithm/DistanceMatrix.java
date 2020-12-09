package nuist.zjl.algorithm;
import nuist.zjl.exception.VectorDimensionalityException;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import nuist.zjl.utils.POIUtils;
import java.io.File;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Bray-Curtis矩阵求解类
 */
public class DistanceMatrix {
    private Map<String, Method> methodMap;
    private final int PRECISION = 10;  //精度，即保留有效数字的位数

    public DistanceMatrix() {
        //类初始化时将所有方法，放入methodMap
        Method[] methods = this.getClass().getDeclaredMethods();
        methodMap = new HashMap<>();
        for (Method method : methods) {
            methodMap.put(method.getName(), method);
        }
    }

    /**
     * 计算向量间的Bray-Curtis距离
     * @param v1 A处理的OTU数目列表
     * @param v2 B处理的OTU数目列表
     * @return 返回处理A与处理B微生物群落的Bray-Curtis距离值
     * @throws Exception
     */
    public BigDecimal brayCurtisDis(List<BigDecimal> v1, List<BigDecimal> v2) throws Exception {
        if (v1.size() != v2.size()) throw new VectorDimensionalityException();
        BigDecimal sumOfMin = new BigDecimal(0);  // 对应公式中 ∑ min(S_(A,i),S_(B,i))
        BigDecimal sumOfSiteA = new BigDecimal(0);  // 对应公式中 ∑ S_(A,i)
        BigDecimal sumOfSiteB = new BigDecimal(0);  // 对应公式中 ∑ S_(B,i)
        for (int i = 0; i < Math.min(v1.size(), v2.size()); i++) {
            BigDecimal numOfOTUA = v1.get(i);
            BigDecimal numOfOTUB = v2.get(i);
            sumOfMin = sumOfMin.add(numOfOTUA.min(numOfOTUB));
            sumOfSiteA = sumOfSiteA.add(numOfOTUA);
            sumOfSiteB = sumOfSiteB.add(numOfOTUB);
        }
        BigDecimal firstStep = sumOfMin.divide(sumOfSiteA.add(sumOfSiteB), new MathContext(PRECISION)).multiply(new BigDecimal(2));
        BigDecimal brayCurtisDistance = new BigDecimal(1).subtract(firstStep);
        return brayCurtisDistance;
    }

    /**
     * 计算两向量间的欧氏距离
     * @param v1
     * @param v2
     * @return
     * @throws Exception
     */
    public BigDecimal euclideanDis(List<BigDecimal> v1, List<BigDecimal> v2) throws Exception {
        if (v1.size() != v2.size()) throw new VectorDimensionalityException();
        BigDecimal quadraticSum = new BigDecimal(0);
        for (int i = 0; i < Math.min(v1.size(), v2.size()); i++) {
            BigDecimal numA = v1.get(i);
            BigDecimal numB = v2.get(i);
            quadraticSum = quadraticSum.add(numA.subtract(numB).pow(2));
        }
        System.out.println(quadraticSum);
        return quadraticSum.sqrt(new MathContext(PRECISION));
    }


    /**
     * 将OTU列表中数据转换为BigDecimal类型，保证计算精度
     * @param list
     * @return OTU数目列表
     */
    public List<BigDecimal> getBigDecimalList(List<String> list) {
        List<BigDecimal> result = new ArrayList<>();
        for (int i = 1; i < list.size(); i++) {
            result.add(new BigDecimal(list.get(i)));
        }
        return result;
    }

    /**
     * 计算距离矩阵
     * @param columnList
     * @param distanceType 可选 ： euclideanDis，brayCurtisDis
     * @return
     * @throws Exception
     */
    public List<List<String>> getDistanceMatrix(List<List<String>> columnList, String distanceType) throws Exception {
        List<List<String>> result = new ArrayList<>();
        List<String> firstLine = new ArrayList<>();
        firstLine.add("x1");
        for (int i = 1; i < columnList.size(); i++) {
            firstLine.add(columnList.get(i).get(0));
        }
        result.add(firstLine);
        for (int i = 1; i < columnList.size(); i++) {
            List<String> resultLine = new ArrayList<>();
            String groupName = columnList.get(i).get(0);
            resultLine.add(groupName);
            for (int j = 1; j < columnList.size(); j++) {
                List<BigDecimal> v1 = getBigDecimalList(columnList.get(i));
                List<BigDecimal> v2 = getBigDecimalList(columnList.get(j));
                BigDecimal distanceValue = (BigDecimal) methodMap.get(distanceType).invoke(this, v1, v2);
                resultLine.add(distanceValue.toString());
            }
            result.add(resultLine);
        }
        return result;
    }

    public void test(String str) {
        System.out.println(str + "test success...");
    }

    public static void main(String[] args) throws Exception {
        //获取列数据列表
        List<List<String>> colList = POIUtils.readDataByCol("C:\\Users\\36085\\Desktop\\microbial_sci\\raw_figures\\mental_test\\jsen.xlsx");
        DistanceMatrix brayCurtis = new DistanceMatrix();
        List<List<String>> resultList = brayCurtis.getDistanceMatrix(colList, "euclideanDis");
        XSSFWorkbook workbook = POIUtils.getWorkBookFromList(resultList);
        POIUtils.getExcelFileFromWorkbook(workbook, new File("C:\\Users\\36085\\Desktop\\microbial_sci\\raw_figures\\mental_test\\xxxxx.xlsx"));
    }
}
