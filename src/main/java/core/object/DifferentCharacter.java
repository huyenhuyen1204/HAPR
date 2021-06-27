package core.object;

import java.util.List;

public class DifferentCharacter {
    private Integer[] indexExpected;
    private Integer[] indexActual;

    public DifferentCharacter(Integer[] indexExpected, Integer[] indexActual) {
        this.indexExpected = indexExpected;
        this.indexActual = indexActual;
    }

    public Integer[] getIndexExpected() {
        return indexExpected;
    }

    public void setIndexExpected(Integer[] indexExpected) {
        this.indexExpected = indexExpected;
    }

    public Integer[] getIndexActual() {
        return indexActual;
    }

    public void setIndexActual(Integer[] indexActual) {
        this.indexActual = indexActual;
    }
}
