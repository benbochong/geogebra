package org.geogebra.common.kernel.arithmetic.variable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.geogebra.common.kernel.Kernel;
import org.geogebra.common.kernel.arithmetic.FunctionVariable;
import org.geogebra.common.kernel.geos.GeoElement;
import org.geogebra.common.kernel.geos.GeoFunction;
import org.geogebra.common.kernel.geos.GeoFunctionNVar;
import org.geogebra.common.util.StringUtil;

public class InputTokenizer {
	private final List<String> varStrings;
	private Kernel kernel;
	private String input;

	public InputTokenizer(String input) {
		this.input = input;
		varStrings = Collections.emptyList();
	}

	public InputTokenizer(Kernel kernel, String input) {
		this.kernel = kernel;
		this.input = input;
		varStrings = getVarStrings();
	}

	private List<String> getVarStrings() {
		if (kernel == null || kernel.getConstruction() == null ||
				kernel.getConstruction().getGeoSetConstructionOrder() == null) {
			return Collections.emptyList();
		}

		ArrayList<String> list = new ArrayList<>();
		for (GeoElement geo: kernel.getConstruction().getGeoSetConstructionOrder()) {
			for (FunctionVariable variable: getFunctionVariables(geo)) {
				list.add(variable.getSetVarString());
			}
		}
		return list;
	}

	private FunctionVariable[] getFunctionVariables(GeoElement geo) {
		if (geo.isGeoFunction()) {
			return ((GeoFunction) geo).getFunctionVariables();
		}
		if (geo.isGeoFunctionNVar()) {
			return ((GeoFunctionNVar)geo).getFunctionVariables();
		}
		return new FunctionVariable[0];
	}

	public List<String> getTokens() {
		ArrayList<String> tokens = new ArrayList<>();
		while (!StringUtil.empty(input)) {
			String nextToken = next();
			tokens.add(nextToken);
		}

		return tokens;
	}

	public String next() {
		String token = getToken();
		input = !StringUtil.empty(token)  ? input.substring(token.length()) : null;
		return token;
	}

	private String getToken() {
		if (noInputLeft()) {
			return null;
		}

		if (isDigitNext()) {
			return getNumberToken(0);
		}

		String geoLabel = getGeoLabel();
		if (!StringUtil.empty(geoLabel)) {
			return geoLabel;
		}


		String variable = getVariable();
		if (!"".equals(variable)) {
			return variable;
		}


		if (isPiNext()) {
			return "pi";
		}

		if (isSingleCharOrLetterNext()) {
			return String.valueOf(input.charAt(0));
		}

		if (isQuoteMarkNext()) {
			return input.charAt(0) + "'";
		}

		if (isIndexNext()) {
			return getTokenWithIndex();
		}

		if (isExponentialNext()) {
			return getTokenWithExponential();
		}

		return "";
	}

	private String getGeoLabel() {
		if (input.length() == 1) {
			return input;
		}

		String label = "";
		for (int i=0; i < input.length(); i++) {
			if (kernel.lookupLabel(label) != null) {
				return label;
			}
			label += input.charAt(i);
		}
		return null;
	}

	private boolean isPiNext() {
		if (input.length() < 2) {
			return false;
		}

		return "pi".equals(input.substring(0, 2).toLowerCase());
	}

	private String getVariable() {
		for (String var: varStrings) {
			if (input.startsWith(var)) {
				return var;
			}
		}
		return "";
	}
	private String getNumberToken(int from) {
		if (noInputLeft()) {
			return "";
		}

		StringBuilder result = new StringBuilder();
		int i = from;
		while (StringUtil.isDigit(input.charAt(i))) {
			result.append(input.charAt(i));
			i++;
		}
		return result.toString();

	}

	private boolean isDigitNext() {
		return input.length() > 1 && StringUtil.isDigit(nextChar());
	}

	private char nextChar() {
		return input.charAt(1);
	}

	private boolean isQuoteMarkNext() {
		return nextChar() == '\'';
	}

	private boolean isSingleCharOrLetterNext() {
		return input.length() == 1 || StringUtil.isLetter(nextChar())
				|| isBracket(nextChar());
	}

	private boolean isBracket(char ch) {
		return ch == '(' || ch == ')';
	}

	private String getTokenWithIndex() {
		if ("_{".equals(input.substring(1, 3))) {
			return tokenWithCurlyBracketIndex();
		}
		return tokenWithUnderscoreIndex();
	}

	private String tokenWithUnderscoreIndex() {
		String token = input.substring(0, 2);
		return token + getNumberToken(2);
	}

	private String tokenWithCurlyBracketIndex() {
		int idxClose = input.indexOf("}");
		return idxClose != -1 ? input.substring(0, idxClose + 1) : "";
	}

	private boolean isIndexNext() {
		return nextChar() == '_';
	}

	private boolean isExponentialNext() {
		return "^(".equals(input.substring(1, 3));
	}

	private String getTokenWithExponential() {
		int idxClose = input.indexOf(")");
		return idxClose != -1 ? input.substring(0, idxClose + 1) : "";
	}

	public boolean noInputLeft() {
		return input == null || input.length() == 0;
	}

	public String getInputRemaining() {
		return input;
	}

	public boolean hasToken() {
		return !(input == null || "".equals(input));

	}
}
