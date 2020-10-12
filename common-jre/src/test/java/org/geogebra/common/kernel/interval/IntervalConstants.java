package org.geogebra.common.kernel.interval;

public class IntervalConstants {
	public final static Interval WHOLE = new Interval(Double.NEGATIVE_INFINITY,
			Double.POSITIVE_INFINITY);
	public static final Interval ZERO = new Interval(0);
	public final static Interval EMPTY = new Interval(Double.POSITIVE_INFINITY,
			Double.NEGATIVE_INFINITY);
}
