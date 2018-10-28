[![Build Status](https://travis-ci.com/lloydjm77/pojo-tester.svg?branch=master)](https://travis-ci.com/lloydjm77/pojo-tester)
[![codecov](https://codecov.io/gh/lloydjm77/pojo-tester/branch/master/graph/badge.svg)](https://codecov.io/gh/lloydjm77/pojo-tester)

# POJO and Class Testing Utilities

## About

This project provides utilities for testing POJOs and constants classes.  This is an opinionated wrapper around the [equalsverifier](https://github.com/jqno/equalsverifier) and [openpojo](https://github.com/OpenPojo/openpojo) projects.   It simplifies the calls by applying default rules (that can be overridden) and adding methods for easily testing all classes in a package as well as testing based on the class itself or an instance of the class.

## Usage

To verify all POJOs in a package:

```java
PojoUtil.verifyAll("io.jlloyd.pojotester.test.objects.pojo");
```

By default this will verify the following:

* Getters and setters have been created and work as expected.
* A serialVersionUID has been defined.
* There is no field shadowing. (i.e. name = name, but rather this.name = name)
* There are no non-static public fields.
* Equals and hashCode have been defined and are working correctly.
* toString has been defined.

Settings can be overridden, for example if you don't want to test setters, but execute all other tests:

```java
List<Rule> ruleList = Arrays.asList(new GetterMustExistRule(), 
        new SerializableMustHaveSerialVersionUIDRule(),
        new NoFieldShadowingRule(),
        new NoPublicFieldsExceptStaticFinalRule());
List<Tester> testerList = Arrays.asList(new GetterTester());

PojoUtil.verifyAll("io.jlloyd.pojotester.test.objects.pojo", ruleList, testerList);
```

For more information on what specific utilities are available in the code, view the Javadocs.