// $Id$
// Author: Yves Lafon <ylafon@w3.org>
//
// (c) COPYRIGHT MIT, ERCIM and Keio University, 2012.
// Please first read the full copyright statement in file COPYRIGHT.html
package org.w3c.css.properties.css3;

import org.w3c.css.util.ApplContext;
import org.w3c.css.util.InvalidParamException;
import org.w3c.css.values.CssCheckableValue;
import org.w3c.css.values.CssExpression;
import org.w3c.css.values.CssIdent;
import org.w3c.css.values.CssTypes;
import org.w3c.css.values.CssValue;

import java.util.Arrays;

/**
 * @spec http://www.w3.org/TR/2011/WD-css3-fonts-20111004/#font-size-prop
 */
public class CssFontSize extends org.w3c.css.properties.css.CssFontSize {

	public static final CssIdent[] allowed_values;


	static {
		String[] absolute_values = {"xx-small", "x-small", "small", "medium", "large", "x-large", "xx-large"};
		String[] relative_values = {"smaller", "larger"};

		allowed_values = new CssIdent[absolute_values.length + relative_values.length];
		int i = 0;
		for (String s : absolute_values) {
			allowed_values[i++] = CssIdent.getIdent(s);
		}
		for (String s : relative_values) {
			allowed_values[i++] = CssIdent.getIdent(s);
		}
		Arrays.sort(allowed_values);
	}

	public static CssIdent getAllowedValue(CssIdent ident) {
		int idx = Arrays.binarySearch(allowed_values, ident);
		if (idx >= 0) {
			return allowed_values[idx];
		}
		return null;
	}

	/**
	 * Create a new CssFontSize
	 */
	public CssFontSize() {
		value = initial;
	}

	/**
	 * Creates a new CssFontSize
	 *
	 * @param expression The expression for this property
	 * @throws org.w3c.css.util.InvalidParamException
	 *          Expressions are incorrect
	 */
	public CssFontSize(ApplContext ac, CssExpression expression, boolean check)
			throws InvalidParamException {
		if (check && expression.getCount() > 1) {
			throw new InvalidParamException("unrecognize", ac);
		}
		setByUser();

		CssValue val;
		char op;

		val = expression.getValue();
		op = expression.getOperator();

		switch (val.getType()) {
			case CssTypes.CSS_NUMBER:
				val.getLength();
			case CssTypes.CSS_LENGTH:
			case CssTypes.CSS_PERCENTAGE:
				CssCheckableValue l = val.getCheckableValue();
				l.checkPositiveness(ac, this);
				value = l;
				break;
			case CssTypes.CSS_IDENT:
				CssIdent ident = (CssIdent) val;
				if (inherit.equals(ident)) {
					value = inherit;
					break;
				}
				value = getAllowedValue(ident);
				if (value == null) {
					throw new InvalidParamException("value",
							expression.getValue().toString(),
							getPropertyName(), ac);
				}
				break;
			default:
				throw new InvalidParamException("value",
						expression.getValue().toString(),
						getPropertyName(), ac);
		}
		expression.next();
	}

	public CssFontSize(ApplContext ac, CssExpression expression)
			throws InvalidParamException {
		this(ac, expression, false);
	}


}

