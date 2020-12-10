package nuist.zjl.algorithm;

import nuist.zjl.pojo.AnovaParam;
import nuist.zjl.pojo.AnovaResult;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.xssf.usermodel.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Anova {

    private final int PRECISION = 10;

    /**
     * 方差分析
     * @param param
     * @return
     */
    public AnovaResult anova(AnovaParam param) throws IOException, InvalidFormatException {
        int n = param.getN();
        int k = param.getK();
        Map<String, List<BigDecimal>> dataMap = param.getDataMap();
        BigDecimal dfTotal = BigDecimal.valueOf(n*k-1);
        BigDecimal dft = BigDecimal.valueOf(k-1);
        BigDecimal dfe = BigDecimal.valueOf(k*(n-1));
        BigDecimal T = getT(dataMap);  //获取所有观测值的总和
        BigDecimal C = T.pow(2).divide(BigDecimal.valueOf(n*k), new MathContext(PRECISION));  // 计算矫正数
        BigDecimal ssTotal = getSsTotal(dataMap, C);  //计算总平方和
        BigDecimal sst = getSst(dataMap, C, n);  //计算组间平方和
        BigDecimal sse = getSse(ssTotal, sst);  //计算组内平方和
        BigDecimal mst = sst.divide(dft, new MathContext(PRECISION));
        BigDecimal mse = sse.divide(dfe, new MathContext(PRECISION));
        BigDecimal f = mst.divide(mse, new MathContext(PRECISION));

        AnovaResult res = new AnovaResult();
        res.setDft(dft);
        res.setDfe(dfe);
        res.setDfTotal(dfTotal);
        res.setSst(sst);
        res.setSse(sse);
        res.setSsTotal(ssTotal);
        res.setMst(mst);
        res.setMse(mse);
        res.setF(f);
        getP(dft, dfe, f);

        XSSFWorkbook workbook = getP(dft, dfe, f);
        XSSFSheet sheet = workbook.getSheetAt(0);
        XSSFRow row0 = sheet.createRow(0);
        row0.createCell(0).setCellValue("变异来源");
        row0.createCell(1).setCellValue("DF");
        row0.createCell(2).setCellValue("SS");
        row0.createCell(3).setCellValue("MS");
        row0.createCell(4).setCellValue("F");
        row0.createCell(5).setCellValue("P");
        XSSFRow row1 = sheet.getRow(1);
        row1.createCell(0).setCellValue("处理间");
        row1.createCell(1).setCellValue(dft.toString());
        row1.createCell(2).setCellValue(sst.toString());
        row1.createCell(3).setCellValue(mst.toString());
        row1.createCell(4).setCellValue(f.toString());
        XSSFRow row2 = sheet.createRow(2);
        row2.createCell(0).setCellValue("处理内");
        row2.createCell(1).setCellValue(dfe.toString());
        row2.createCell(2).setCellValue(sse.toString());
        row2.createCell(3).setCellValue(mse.toString());
        XSSFRow row3 = sheet.createRow(3);
        row3.createCell(0).setCellValue("总变异");
        row3.createCell(1).setCellValue(dfTotal.toString());
        row3.createCell(2).setCellValue(ssTotal.toString());
        FileOutputStream out = new FileOutputStream(new File("anovaResult.xlsx"));
        workbook.write(out);
        out.flush();
        out.close();
        workbook.close();
        return res;
    }

    /**
     * 计算组内平方和
     * @param ssTotal
     * @param sst
     * @return
     */
    private BigDecimal getSse(BigDecimal ssTotal, BigDecimal sst) {
        return ssTotal.subtract(sst);
    }

    /**
     * 计算组间平方和
     * @param dataMap
     * @param c
     * @param n
     * @return
     */
    private BigDecimal getSst(Map<String, List<BigDecimal>> dataMap, BigDecimal c, int n) {
        List<BigDecimal> tis = new ArrayList<>();
        BigDecimal ti;
        for (String k : dataMap.keySet()) {
            ti = BigDecimal.valueOf(0.0);
            for (BigDecimal value : dataMap.get(k)) {
                ti = ti.add(value);
            }
            tis.add(ti);
        }
        BigDecimal sumOfTi = BigDecimal.valueOf(0.0);
        for (BigDecimal t : tis) {
            sumOfTi = sumOfTi.add(t.pow(2));
        }
        return sumOfTi.divide(BigDecimal.valueOf(n), new MathContext(PRECISION)).subtract(c);
    }

    /**
     * 计算总体平方和
     * @param dataMap
     * @param c
     * @return
     */
    private BigDecimal getSsTotal(Map<String, List<BigDecimal>> dataMap, BigDecimal c) {
        BigDecimal res = BigDecimal.valueOf(0.0);
        for (String k : dataMap.keySet()) {
            for (BigDecimal value : dataMap.get(k)) {
                res = res.add(value.pow(2));
            }
        }
        return res.subtract(c);
    }

    /**
     * 计算所有观测值的总和
     * @param dataMap
     * @return
     */
    private BigDecimal getT(Map<String, List<BigDecimal>> dataMap) {
        BigDecimal res = BigDecimal.valueOf(0.0);
        for (String k : dataMap.keySet()) {
            for (BigDecimal value : dataMap.get(k)) {
                res = res.add(value);
            }
        }
        return res;
    }

    /**
     * 根据两自由度和f值计算p值
     * @param df1
     * @param df2
     * @param f
     * @return
     */
    private XSSFWorkbook getP(BigDecimal df1, BigDecimal df2, BigDecimal f) throws IOException, InvalidFormatException {
        File file = new File("getP.xlsx");
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("newSheet");
        XSSFRow row = sheet.createRow(1);
        XSSFCell cell = row.createCell(5);
        cell.setCellFormula("FDIST(" + f.toString() + ", " + df1.toString() + ", " + df2.toString() + ")");
        FileOutputStream ops = new FileOutputStream(file);
        workbook.write(ops);
        ops.flush();
        ops.close();
        return workbook;
    }

    public static void main(String[] args) throws Exception {
        int[] a = {18, 21, 20, 13};
        int[] b = {20, 24, 26, 22};
        int[] c = {10, 15, 17, 14};
        int[] d = {28, 27, 29, 32};
        List<int[]> list = List.of(a, b, c, d);
        String[] keys = {"A", "B", "C", "D"};
        Map<String, List<BigDecimal>> dataMap = new HashMap<>();
        List<BigDecimal> tempList;
        for (int i = 0; i < 4; i++) {
            tempList = new ArrayList<>();
            for (int i1 : list.get(i)) {
                tempList.add(BigDecimal.valueOf(i1));
            }
            dataMap.put(keys[i], tempList);
        }
        Anova anova = new Anova();
        AnovaParam anovaParam = new AnovaParam();
        anovaParam.setDataMap(dataMap);
        AnovaResult anovaResult = anova.anova(anovaParam);
        System.out.println(anovaResult);
    }

}
