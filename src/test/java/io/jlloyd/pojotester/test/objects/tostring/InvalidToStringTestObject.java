package io.jlloyd.pojotester.test.objects.tostring;

import java.io.Serializable;

/**
 * Class used for test cases.
 * 
 * <i>The serialVersionUID is intentionally left out for testing.</i>
 * 
 * @author b14951
 */
@SuppressWarnings("serial")
public class InvalidToStringTestObject implements Serializable {

	@Override
	public String toString() {
		throw new RuntimeException();
	}
}