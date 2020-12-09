package nuist.zjl.pojo;

import java.math.BigDecimal;

public class CorrelationResult {
    private BigDecimal r;
    private BigDecimal p;

    public BigDecimal getR() {
        return r;
    }

    public void setR(BigDecimal r) {
        this.r = r;
    }

    public BigDecimal getP() {
        return p;
    }

    public void setP(BigDecimal p) {
        this.p = p;
    }

    @Override
    public String toString() {
        return "CorrelationResult{" +
                "r=" + r +
                ", p=" + p +
                '}';
    }
}
