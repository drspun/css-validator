//
// Author: Yves Lafon <ylafon@w3.org>
//
// (c) COPYRIGHT MIT, ERCIM, Keio, Beihang 2015.
// Please first read the full copyright statement in file COPYRIGHT.html
package org.w3c.css.properties.css3;

import org.w3c.css.util.ApplContext;
import org.w3c.css.util.InvalidParamException;
import org.w3c.css.values.CssExpression;
import org.w3c.css.values.CssIdent;
import org.w3c.css.values.CssOperator;
import org.w3c.css.values.CssTypes;
import org.w3c.css.values.CssValue;
import org.w3c.css.values.CssValueList;

import java.util.ArrayList;

/**
 * @spec http://www.w3.org/TR/2015/WD-css-inline-3-20150917/#propdef-vertical-align
 */
public class CssVerticalAlign extends org.w3c.css.properties.css.CssVerticalAlign {

	/**
	 * Create a new CssVerticalAlign
	 */
	public CssVerticalAlign() {
		value = initial;
	}

	/**
	 * Set the value of the property
	 *
	 * @param expression The expression for this property
	 * @param check      set it to true to check the number of values
	 * @throws org.w3c.css.util.InvalidParamException
	 *          The expression is incorrect
	 */
	public CssVerticalAlign(ApplContext ac, CssExpression expression,
							boolean check) throws InvalidParamException {

		if (check && expression.getCount() > 2) {
			throw new InvalidParamException("unrecognize", ac);
		}

		ArrayList<CssValue> v = new ArrayList<>(2);
		setByUser();
		char op;
		CssValue val;
		CssIdent res;
		boolean got_shift = false;
		boolean got_alignment = false;

		while (!expression.end()) {
			val = expression.getValue();
			op = expression.getOperator();

			switch (val.getType()) {
				case CssTypes.CSS_NUMBER:
					val.getLength();
				case CssTypes.CSS_LENGTH:
				case CssTypes.CSS_PERCENTAGE:
					if (got_shift) {
						throw new InvalidParamException("value", expression.getValue(),
								getPropertyName(), ac);
					}
					got_shift = true;
					v.add(val);
					break;
				case CssTypes.CSS_IDENT:
					CssIdent id = (CssIdent) val;
					if (inherit.equals(id)) {
						// inherit can only be alone
						if (expression.getCount() > 1) {
							throw new InvalidParamException("value", expression.getValue(),
									getPropertyName(), ac);
						}
						value = inherit;
						break;
					}
					if (!got_shift) {
						res = CssBaselineShift.getAllowedIdent(id);
						if (res != null) {
							v.add(res);
							got_shift = true;
							break;
						}
					}
					if (!got_alignment) {
						res = CssAlignmentBaseline.getAllowedIdent(id);
						if (res != null) {
							got_alignment = true;
							v.add(res);
							break;
						}
					}
					// failed...
				default:
					throw new InvalidParamException("value", val,
							getPropertyName(), ac);
			}
			if (op != CssOperator.SPACE) {
				throw new InvalidParamException("operator",
						((new Character(op)).toString()), ac);
			}
			expression.next();
		}
		if (value != inherit) {
			value = (v.size() == 1) ? v.get(0) : new CssValueList(v);
		}
	}

	public CssVerticalAlign(ApplContext ac, CssExpression expression)
			throws InvalidParamException {
		this(ac, expression, false);
	}


}

