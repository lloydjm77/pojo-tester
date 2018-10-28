package io.jlloyd.pojotester;

import static io.jlloyd.pojotester.ClassUtil.verifyPrivateNoArgConstructor;
import static io.jlloyd.pojotester.PojoUtil.verifyAll;
import static io.jlloyd.pojotester.PojoUtil.verifyAllFromInstance;
import static io.jlloyd.pojotester.PojoUtil.verifyEqualsAndHashCode;
import static io.jlloyd.pojotester.PojoUtil.verifyToString;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.List;

import io.jlloyd.pojotester.PojoUtil;
import io.jlloyd.pojotester.test.objects.pojo.PositivePojoTestObject;
import io.jlloyd.pojotester.test.objects.tostring.InvalidToStringTestObject;
import io.jlloyd.pojotester.test.objects.tostring.NullToStringTestObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.openpojo.validation.rule.Rule;
import com.openpojo.validation.rule.impl.GetterMustExistRule;
import com.openpojo.validation.rule.impl.NoFieldShadowingRule;
import com.openpojo.validation.rule.impl.NoPublicFieldsExceptStaticFinalRule;
import com.openpojo.validation.rule.impl.SerializableMustHaveSerialVersionUIDRule;
import com.openpojo.validation.rule.impl.SetterMustExistRule;
import com.openpojo.validation.test.Tester;
import com.openpojo.validation.test.impl.GetterTester;
import com.openpojo.validation.test.impl.SetterTester;

@RunWith(JUnit4.class)
public class PojoUtilTest {

	@Test
	public void testInstantiation() {
		verifyPrivateNoArgConstructor(PojoUtil.class);
	}

	@Test
	public void testVerifyPojosInPackage() {
		verifyAll("io.jlloyd.pojotester.test.objects.pojo");
	}

	@Test
	public void test() {
		try {
			verifyAll("io.jlloyd.pojotester.test.objects.tostring");
		} catch (Throwable t) {
			// Catching Throwable because AssertionError extends Error, not Exception.
			assertEquals(AssertionError.class, t.getClass());
			assertTrue(t.getMessage().contains("serialVersionUID"));
		}
	}

	@Test
	public void testVerifyPojoByClass() {
		verifyAll(PositivePojoTestObject.class);
	}

	@Test
	public void testVerifyPojoByInstance() {
		List<Rule> ruleList = Arrays.asList(new GetterMustExistRule(), new SetterMustExistRule(),
				new SerializableMustHaveSerialVersionUIDRule(), new NoFieldShadowingRule(),
				new NoPublicFieldsExceptStaticFinalRule());
		List<Tester> testerList = Arrays.asList(new SetterTester(), new GetterTester());

		verifyAllFromInstance(new PositivePojoTestObject(), ruleList, testerList);
	}

	@Test
	public void testVerifyEqualsAndHashCode() {
		verifyEqualsAndHashCode(PositivePojoTestObject.class);
	}

	@Test
	public void testVerifyToString() {
		verifyToString(PositivePojoTestObject.class);
	}

	@Test
	public void testVerifyToString_NoToString() {
		try {
			verifyToString(RuntimeException.class);
			fail();
		} catch (AssertionError e) {
			assertEquals("toString method is undefined.", e.getMessage());
		} catch (Exception e) {
			fail();
		}
	}

	@Test
	public void testVerifyToString_ToStringDoesNotOverrideObjectToString() {
		try {
			verifyToString(InvalidToStringTestObject.class);
			fail();
		} catch (AssertionError e) {
			assertEquals("toString method cannot be invoked.", e.getMessage());
		} catch (Exception e) {
			fail();
		}
	}

	@Test
	public void testVerifyToString_ToStringReturnsNull() {
		try {
			verifyToString(NullToStringTestObject.class);
			fail();
		} catch (AssertionError e) {
			assertEquals("toString is null.", e.getMessage());
		} catch (Exception e) {
			fail();
		}
	}
}