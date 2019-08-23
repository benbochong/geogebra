package org.geogebra.common.properties.impl.graphics;

import org.geogebra.common.geogebra3D.euclidian3D.openGL.Renderer;
import org.geogebra.common.main.Localization;
import org.geogebra.common.properties.AbstractEnumerableProperty;
import org.geogebra.common.util.DoubleUtil;
import org.geogebra.common.util.StringUtil;
import org.geogebra.common.util.debug.Log;

public class RatioRoundingProperty extends AbstractEnumerableProperty {

    private Renderer renderer;
    private double[] roundingValues;

    /**
     * Constructs an ratio rounding property.
     *
     * @param renderer     renderer
     * @param localization localization
     */
    public RatioRoundingProperty(Renderer renderer, Localization localization) {
        super(localization, "Rounding");
        this.renderer = renderer;
        setValuesAndLocalize(getRatioClosestRounding());
    }

    @Override
    protected void setValueSafe(String value, int index) {
        renderer.setARRatio(roundingValues[index]);
    }

    @Override
    public int getIndex() {
        setValues(getRatioClosestRounding());
        setValuesAndLocalize(getRatioClosestRounding());
        if (roundingValues.length == 3) {
            return 1;
        } else {
            return 0;
        }
    }

    private String[] getRatioClosestRounding() {
        double arRatio = Double.parseDouble(renderer.getARRatio());

        int n = DoubleUtil.getExponentOfTen(arRatio);
        double ratioSimple = arRatio / Math.pow(10, n);

        int low, high;
        if (ratioSimple < 2) {
            low = 1;
            high = 2;
        } else if (ratioSimple < 5) {
            low = 2;
            high = 5;
        } else {
            low = 5;
            high = 10;
        }

        long rounded = Math.round(ratioSimple);

        String[] ret;
        if (low == rounded || high == rounded) {
            ret = new String[2];
            roundingValues = new double[2];
        } else {
            ret = new String[3];
            roundingValues = new double[3];
        }

        int index = 0;
        if (low != rounded) {
            index = appendValue(low, n, index, ret);
        }
        index = appendValue(rounded, n, index, ret);
        if (high != rounded) {
            appendValue(high, n, index, ret);
        }

        return ret;

    }

    private int appendValue(long rounded, int exp, int index, String[] ret) {
        int n = exp;
        if (rounded == 10) {
            n++;
            rounded = 1;
        }
        StringBuilder sb = new StringBuilder();
        if (n==0) {
            sb.append(rounded);
        } else if (n > 0) {
            sb.append(rounded);
            for (int i = 0; i < n; i++) {
                sb.append("0");
            }
        } else {
            sb.append("0.");
            for (int i = 0; i < -n - 1; i++) {
                sb.append("0");
            }
            sb.append(rounded);
        }
        ret[index] = sb.toString();
        roundingValues[index] = rounded * Math.pow(10, n);
        return index + 1;
    }
}
