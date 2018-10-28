package io.jlloyd.pojotester.test.objects.pojo;

import java.io.Serializable;

/**
 * Class used for test cases.
 * 
 * @author b14951
 */
public class PositivePojoTestObject implements Serializable {

	private static final long serialVersionUID = -6544977213887692902L;

	private String test;

	public String getTest() {
		return test;
	}

	public void setTest(String test) {
		this.test = test;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((test == null) ? 0 : test.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		PositivePojoTestObject other = (PositivePojoTestObject) obj;
		if (test == null) {
			if (other.test != null) {
				return false;
			}
		} else if (!test.equals(other.test)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ToString [test=").append(test).append("]");
		return builder.toString();
	}
}