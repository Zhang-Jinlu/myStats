package nuist.zjl.pojo;

import java.math.BigDecimal;

public class AnovaResult {
    private BigDecimal dft;  //处理间自由度
    private BigDecimal dfe;  //处理内自由度
    private BigDecimal dfTotal;  //总自由度
    private BigDecimal sst;  //处理间平方和
    private BigDecimal sse;  //处理内平方和
    private BigDecimal ssTotal; //总平方和
    private BigDecimal mst;  //处理间均方
    private BigDecimal mse;  //处理内均方
    private BigDecimal f;  //F检验结果
    private BigDecimal p;  //p值

    public BigDecimal getDft() {
        return dft;
    }

    public void setDft(BigDecimal dft) {
        this.dft = dft;
    }

    public BigDecimal getDfe() {
        return dfe;
    }

    public void setDfe(BigDecimal dfe) {
        this.dfe = dfe;
    }

    public BigDecimal getDfTotal() {
        return dfTotal;
    }

    public void setDfTotal(BigDecimal dfTotal) {
        this.dfTotal = dfTotal;
    }

    public BigDecimal getSst() {
        return sst;
    }

    public void setSst(BigDecimal sst) {
        this.sst = sst;
    }

    public BigDecimal getSse() {
        return sse;
    }

    public void setSse(BigDecimal sse) {
        this.sse = sse;
    }

    public BigDecimal getSsTotal() {
        return ssTotal;
    }

    public void setSsTotal(BigDecimal ssTotal) {
        this.ssTotal = ssTotal;
    }

    public BigDecimal getMst() {
        return mst;
    }

    public void setMst(BigDecimal mst) {
        this.mst = mst;
    }

    public BigDecimal getMse() {
        return mse;
    }

    public void setMse(BigDecimal mse) {
        this.mse = mse;
    }

    public BigDecimal getF() {
        return f;
    }

    public void setF(BigDecimal f) {
        this.f = f;
    }

    public BigDecimal getP() {
        return p;
    }

    public void setP(BigDecimal p) {
        this.p = p;
    }

    @Override
    public String toString() {
        return "AnovaResult{" +
                "dft=" + dft +
                ", dfe=" + dfe +
                ", dfTotal=" + dfTotal +
                ", sst=" + sst +
                ", sse=" + sse +
                ", ssTotal=" + ssTotal +
                ", mst=" + mst +
                ", mse=" + mse +
                ", f=" + f +
                ", p=" + p +
                '}';
    }
}
