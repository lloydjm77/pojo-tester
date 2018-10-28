package io.jlloyd.pojotester.test.objects.constructor;

/**
 * Class used for test cases.
 * 
 * @author b14951
 */
public class WithoutUnsupportedOperationException {

	private WithoutUnsupportedOperationException() {
		throw new RuntimeException();
	}
}