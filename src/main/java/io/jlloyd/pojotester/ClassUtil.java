package io.jlloyd.pojotester;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

/**
 * Utility for testing classes.
 * 
 * @author lloydjm77
 */
public final class ClassUtil {

	private static final String THIS_CLASS_SHOULD_NOT_BE_INSTANTIATED = "This class should not be instantiated.";

	private ClassUtil() {
		throw new UnsupportedOperationException(THIS_CLASS_SHOULD_NOT_BE_INSTANTIATED);
	}

	/**
	 * <p>
	 * Verifies that a no-arg private constructor is defined for the specified class. The constructor should throw an
	 * UnsupportedOperationException with a message of {@value #THIS_CLASS_SHOULD_NOT_BE_INSTANTIATED}
	 * </p>
	 * <p>
	 * For example:
	 * </p>
	 * 
	 * <pre>
	 * private MyClass() {
	 *     throw new UnsupportedOperationException({@value #THIS_CLASS_SHOULD_NOT_BE_INSTANTIATED});
	 * }
	 * </pre>
	 * 
	 * @param <T> 
	 *            - The type.
	 * @param clazz
	 *            - The class to test. <i>This will not work for nested classes.</i>
	 * @throws AssertionError
	 *             If the verification fails.
	 */
	public static <T> void verifyPrivateNoArgConstructor(Class<T> clazz) {
		try {
			Constructor<T> constructor = clazz.getDeclaredConstructor();
			constructor.setAccessible(true);
			constructor.newInstance();
			// The newInstance call should have triggered an InvocationTargetException because
			// we should be throwing an UnsupportedOperationException in the constructor.
			// If the verification reaches this point, we are just throwing an AssertionError
			// because the constructor itself did not throw an exception at all.
			throw new AssertionError(
					"UnsupportedOperationException should be thrown from constructor for class: " + clazz.getName());
		} catch (Exception e) {
			// Get the target exception thrown by the constructor which
			// should be the UnsupportedOperationException with the
			// message we supplied.
			Throwable cause = e.getCause();

			if (cause == null) {
				throw new AssertionError("Constructor not found for class: " + clazz.getName());
			}

			if (UnsupportedOperationException.class != cause.getClass()) {
				throw new AssertionError("UnsupportedOperationException should be thrown from constructor for class: "
						+ clazz.getName());
			}

			if (!THIS_CLASS_SHOULD_NOT_BE_INSTANTIATED.equals(cause.getMessage())) {
				throw new AssertionError("UnsupportedOperationException should contain message: \""
						+ THIS_CLASS_SHOULD_NOT_BE_INSTANTIATED + "\" for class: " + clazz.getName());
			}
		}
	}

	/*
	 * This was intentionally made package-private until it is needed elsewhere. It would need to be made more generic
	 * before it can be used for other applications.
	 */
	static Class<?> getClass(String className) {
		try {
			return Class.forName(className);
		} catch (ClassNotFoundException e) {
			throw new AssertionError("Class " + className + " cannot be found.");
		}
	}

	/*
	 * This was intentionally made package-private until it is needed elsewhere. It would need to be made more generic
	 * before it can be used for other applications.
	 */
	static Object getInstance(Class<?> clazz) {
		try {
			return clazz.newInstance();
		} catch (Exception e) {
			throw new AssertionError("Class " + clazz.getName() + " cannot be instantiated.");
		}
	}

	/*
	 * This was intentionally made package-private until it is needed elsewhere. It would need to be made more generic
	 * before it can be used for other applications.
	 */
	static Method getMethod(Class<?> clazz, String methodName) {
		try {
			return clazz.getMethod(methodName);
		} catch (Exception e) {
			throw new AssertionError("Method " + methodName + " not found or inaccessible.");
		}
	}
}