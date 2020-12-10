package nuist.zjl.pojo;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public class AnovaParam {
    private int k;  //处理数
    private int n;  //重复数
    private Map<String, List<BigDecimal>> dataMap;  // key: 处理名称；value：某处理的重复数据列表

    public int getK() {
        return k;
    }

    public void setK(int k) {
        this.k = k;
    }

    public int getN() {
        return n;
    }

    public void setN(int n) {
        this.n = n;
    }

    public Map<String, List<BigDecimal>> getDataMap() {
        return dataMap;
    }

    public void setDataMap(Map<String, List<BigDecimal>> dataMap) throws Exception {
        //检验每组处理重复数是否相同
        int r = -1;
        for (String key : dataMap.keySet()) {
            if (r == -1) r = dataMap.get(key).size();
            else {
                if (dataMap.get(key).size() != r) {
                    throw new Exception("处理间重复数不相同");
                }
            }
        }
        this.dataMap = dataMap;
        k = dataMap.size();
        n = r;
    }
}
