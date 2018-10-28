package io.jlloyd.pojotester;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;

import com.openpojo.reflection.PojoClass;
import com.openpojo.reflection.filters.FilterBasedOnInheritance;
import com.openpojo.reflection.impl.PojoClassFactory;
import com.openpojo.validation.Validator;
import com.openpojo.validation.ValidatorBuilder;
import com.openpojo.validation.rule.Rule;
import com.openpojo.validation.rule.impl.GetterMustExistRule;
import com.openpojo.validation.rule.impl.NoFieldShadowingRule;
import com.openpojo.validation.rule.impl.NoPublicFieldsExceptStaticFinalRule;
import com.openpojo.validation.rule.impl.SerializableMustHaveSerialVersionUIDRule;
import com.openpojo.validation.rule.impl.SetterMustExistRule;
import com.openpojo.validation.test.Tester;
import com.openpojo.validation.test.impl.GetterTester;
import com.openpojo.validation.test.impl.SetterTester;

/**
 * Utility for testing pojos.
 * 
 * @author lloydjm77
 */
public final class PojoUtil {

	private PojoUtil() {
		throw new UnsupportedOperationException("This class should not be instantiated.");
	}

	/**
	 * <p>
	 * This method will verify the POJOs in the specified package. Only classes that are {@link Serializable} will be
	 * evaluated. The following rules will be processed:
	 * </p>
	 * <ol>
	 * <li>Getters and setters have been created and work as expected.</li>
	 * <li>A serialVersionUID has been defined.</li>
	 * <li>There is no field shadowing. (i.e. name = name, but rather this.name = name)</li>
	 * <li>There are no non-static public fields.</li>
	 * <li>Equals and hashCode have been defined and are working correctly.</li>
	 * <li>toString has been defined.</li>
	 * </ol>
	 * 
	 * @param packageName
	 *            - The package name to scan for {@link Serializable} beans.
	 * @throws AssertionError
	 *             If any exception occurs during the verification, including {@link AssertionError} if the validation
	 *             fails.
	 */
	public static void verifyAll(String packageName) {
		List<Rule> ruleList = Arrays.asList(new GetterMustExistRule(), new SetterMustExistRule(),
				new SerializableMustHaveSerialVersionUIDRule(), new NoFieldShadowingRule(),
				new NoPublicFieldsExceptStaticFinalRule());
		List<Tester> testerList = Arrays.asList(new SetterTester(), new GetterTester());

		verifyAll(packageName, ruleList, testerList);
	}

	/**
	 * <p>
	 * This method will verify the POJOs in the specified package. Only classes that are {@link Serializable} will be
	 * evaluated. The specified {@link Rule}s and {@link Tester}s will be used for verification, as well as applying the
	 * following:
	 * </p>
	 * <ol>
	 * <li>Equals and hashCode have been defined and are working correctly.</li>
	 * <li>toString has been defined.</li>
	 * </ol>
	 * 
	 * @param packageName
	 *            - The package name to scan for {@link Serializable} beans.
	 * @param ruleList
	 *            - A list of {@link Rule}s to run during the verification.
	 * @param testerList
	 *            - A list of {@link Tester}s to run during the verification.
	 * @throws AssertionError
	 *             If any exception occurs during the verification, including {@link AssertionError} if the validation
	 *             fails.
	 */
	public static void verifyAll(String packageName, List<Rule> ruleList, List<Tester> testerList) {
		Validator validator = buildValidator(ruleList, testerList);

		List<PojoClass> pojoClasses = PojoClassFactory.getPojoClassesRecursively(packageName,
				new FilterBasedOnInheritance(Serializable.class));

		for (PojoClass pojoClass : pojoClasses) {
			verifyAllImpl(pojoClass, ClassUtil.getClass(pojoClass.getName()), validator);
		}
	}

	/**
	 * <p>
	 * This method will verify the class specified. The following rules will be processed:
	 * </p>
	 * <ol>
	 * <li>Getters and setters have been created and work as expected.</li>
	 * <li>A serialVersionUID has been defined.</li>
	 * <li>There is no field shadowing. (i.e. name = name, but rather this.name = name)</li>
	 * <li>There are no non-static public fields.</li>
	 * <li>Equals and hashCode have been defined and are working correctly.</li>
	 * <li>toString has been defined.</li>
	 * </ol>
	 * 
	 * @param clazz
	 *            - The class to test. <i>This will not work for nested classes.</i>
	 * @throws AssertionError
	 *             If any exception occurs during the verification, including {@link AssertionError} if the validation
	 *             fails.
	 */
	public static void verifyAll(Class<?> clazz) {
		List<Rule> ruleList = Arrays.asList(new GetterMustExistRule(), new SetterMustExistRule(),
				new SerializableMustHaveSerialVersionUIDRule(), new NoFieldShadowingRule(),
				new NoPublicFieldsExceptStaticFinalRule());
		List<Tester> testerList = Arrays.asList(new SetterTester(), new GetterTester());

		verifyAll(clazz, ruleList, testerList);
	}

	/**
	 * <p>
	 * This method will verify the class specified. The specified {@link Rule}s and {@link Tester}s will be used for
	 * verification, as well as applying the following:
	 * </p>
	 * <ol>
	 * <li>Equals and hashCode have been defined and are working correctly.</li>
	 * <li>toString has been defined.</li>
	 * </ol>
	 * 
	 * @param clazz
	 *            - The class to test. <i>This will not work for nested classes.</i>
	 * @param ruleList
	 *            - A list of {@link Rule}s to run during the verification.
	 * @param testerList
	 *            - A list of {@link Tester}s to run during the verification.
	 * @throws AssertionError
	 *             If any exception occurs during the verification, including {@link AssertionError} if the validation
	 *             fails.
	 */
	public static void verifyAll(Class<?> clazz, List<Rule> ruleList, List<Tester> testerList) {
		verifyAllImpl(PojoClassFactory.getPojoClass(clazz), clazz, buildValidator(ruleList, testerList));
	}

	/**
	 * <p>
	 * This method will verify rules specified using the supplied instance. The specified {@link Rule}s and
	 * {@link Tester}s will be used for verification, as well as applying the following:
	 * </p>
	 * <ol>
	 * <li>Equals and hashCode have been defined and are working correctly.</li>
	 * <li>toString has been defined.</li>
	 * </ol>
	 * 
	 * @param object
	 *            - The instance to test. <i>This will not work for nested classes.</i>
	 * @param ruleList
	 *            - A list of {@link Rule}s to run during the verification.
	 * @param testerList
	 *            - A list of {@link Tester}s to run during the verification.
	 * @throws AssertionError
	 *             If any exception occurs during the verification, including {@link AssertionError} if the validation
	 *             fails.
	 */
	public static void verifyAllFromInstance(Object object, List<Rule> ruleList, List<Tester> testerList) {
		Class<?> clazz = object.getClass();
		verifyAllFromInstanceImpl(PojoClassFactory.getPojoClass(clazz), clazz, object,
				buildValidator(ruleList, testerList));
	}

	/**
	 * Verifies equals and hashCode for a class.
	 * 
	 * @param clazz
	 *            - The class to test. <i>This will not work for nested classes.</i>
	 * @throws AssertionError
	 *             If any exception occurs during the verification, including {@link AssertionError} if the validation
	 *             fails.
	 */
	public static void verifyEqualsAndHashCode(Class<?> clazz) {
		// Test equals and hashCode.
		EqualsVerifier.forClass(clazz).suppress(Warning.NONFINAL_FIELDS).usingGetClass().verify();
	}

	/**
	 * Verifies toString has been overridden, and that it returns a non-null value. <i>This will only work for classes
	 * with a default no-arg constructor. If you have a parameterized constructor, use
	 * {@link #verifyToStringFromInstance(Object)} instead.</i>
	 * 
	 * @param clazz
	 *            - The class to test. <i>This will not work for nested classes.</i>
	 * @throws AssertionError
	 *             If any exception occurs during the verification, including {@link AssertionError} if the validation
	 *             fails.
	 */
	public static void verifyToString(Class<?> clazz) {
		verifyToStringFromInstance(ClassUtil.getInstance(clazz));
	}

	/**
	 * Verifies toString has been overridden, and that it returns a non-null value.
	 * 
	 * @param object
	 *            - The instance to test. <i>This will not work for nested classes.</i>
	 * @throws AssertionError
	 *             If any exception occurs during the verification, including {@link AssertionError} if the validation
	 *             fails.
	 */
	public static void verifyToStringFromInstance(Object object) {
		Class<?> clazz = object.getClass();

		Method method = ClassUtil.getMethod(clazz, "toString");

		// Check to make sure the class has overridden toString.
		if (!method.getDeclaringClass().getName().equals(clazz.getName())) {
			throw new AssertionError("toString method is undefined.");
		}

		// Call toString to cover it and verify it isn't null.
		String toString;
		try {
			toString = (String) method.invoke(object, (Object[]) null);
		} catch (Exception e) {
			throw new AssertionError("toString method cannot be invoked.");
		}

		if (toString == null) {
			throw new AssertionError("toString is null.");
		}
	}

	private static Validator buildValidator(List<Rule> ruleList, List<Tester> testerList) {
		return ValidatorBuilder.create().with(ruleList.toArray(new Rule[ruleList.size()]))
				.with(testerList.toArray(new Tester[testerList.size()])).build();
	}

	private static void verifyAllImpl(PojoClass pojoClass, Class<?> clazz, Validator validator) {
		validator.validate(pojoClass);
		verifyEqualsAndHashCode(clazz);
		verifyToString(clazz);
	}

	private static void verifyAllFromInstanceImpl(PojoClass pojoClass, Class<?> clazz, Object object,
			Validator validator) {
		validator.validate(pojoClass);
		verifyEqualsAndHashCode(clazz);
		verifyToStringFromInstance(object);
	}
}