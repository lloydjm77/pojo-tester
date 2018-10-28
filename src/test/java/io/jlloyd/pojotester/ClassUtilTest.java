package io.jlloyd.pojotester;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import io.jlloyd.pojotester.ClassUtil;
import io.jlloyd.pojotester.test.objects.constructor.WithUnsupportedOperationException;
import io.jlloyd.pojotester.test.objects.constructor.WithoutUnsupportedOperationException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class ClassUtilTest {

	@Test
	public void testVerifyPrivateNoArgConstructor() {
		ClassUtil.verifyPrivateNoArgConstructor(ClassUtil.class);
	}

	@Test
	public void testVerifyPrivateNoArgConstructor_NoArgConstructorNotFound() {
		Class<?> clazz = Integer.class;
		try {
			ClassUtil.verifyPrivateNoArgConstructor(clazz);
			fail();
		} catch (AssertionError e) {
			assertEquals("Constructor not found for class: " + clazz.getName(), e.getMessage());
		} catch (Exception e) {
			fail();
		}
	}

	@Test
	public void testVerifyPrivateNoArgConstructor_UnsupportedOperationExceptionNotThrown() {
		Class<?> clazz = WithoutUnsupportedOperationException.class;
		try {
			ClassUtil.verifyPrivateNoArgConstructor(clazz);
			fail();
		} catch (AssertionError e) {
			assertEquals(
					"UnsupportedOperationException should be thrown from constructor for class: " + clazz.getName(),
					e.getMessage());
		} catch (Exception e) {
			fail();
		}
	}

	@Test
	public void testVerifyPrivateNoArgConstructor_UnsupportedOperationExceptionThrownButWithoutMessage() {
		Class<?> clazz = WithUnsupportedOperationException.class;
		try {
			ClassUtil.verifyPrivateNoArgConstructor(clazz);
			fail();
		} catch (AssertionError e) {
			assertEquals(
					"UnsupportedOperationException should contain message: \"This class should not be instantiated.\" for class: "
							+ clazz.getName(),
					e.getMessage());
		} catch (Exception e) {
			fail();
		}
	}

	@Test
	public void testVerifyPrivateNoArgConstructor_NoExceptionThrown() {
		Class<?> clazz = String.class;
		try {
			ClassUtil.verifyPrivateNoArgConstructor(clazz);
			fail();
		} catch (AssertionError e) {
			assertEquals(
					"UnsupportedOperationException should be thrown from constructor for class: " + clazz.getName(),
					e.getMessage());
		} catch (Exception e) {
			fail();
		}
	}

	@Test
	public void testGetClass() {
		assertNotNull(ClassUtil.getClass("java.lang.String"));
	}

	@Test
	public void testGetClass_Exception() {
		try {
			ClassUtil.getClass("java.lang.DoesNotExist");
			fail();
		} catch (AssertionError e) {
			assertEquals("Class java.lang.DoesNotExist cannot be found.", e.getMessage());
		} catch (Exception e) {
			fail();
		}
	}

	@Test
	public void testGetInstance() {
		assertNotNull(ClassUtil.getInstance(String.class));
	}

	@Test
	public void testGetInstance_Exception() {
		try {
			ClassUtil.getInstance(System.class);
			fail();
		} catch (AssertionError e) {
			assertEquals("Class " + System.class.getName() + " cannot be instantiated.", e.getMessage());
		} catch (Exception e) {
			fail();
		}
	}

	@Test
	public void testGetMethod() {
		assertNotNull(ClassUtil.getMethod(String.class, "toString"));
	}

	@Test
	public void testGetMethod_Exception() {
		try {
			ClassUtil.getMethod(String.class, "doesNotExist");
			fail();
		} catch (AssertionError e) {
			assertEquals("Method doesNotExist not found or inaccessible.", e.getMessage());
		} catch (Exception e) {
			fail();
		}
	}
}