package testsuites;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;


/**
 * 
 * JUnit4 TestSuite class.
 * 
 * @author sgra64
 *
 */
@RunWith( Suite.class )
@Suite.SuiteClasses({		// Annotation to configure suite's test classes

	model.CustomerTest.class,

	system.LogicTest.class,

	// add more test classes ...

})

public class TestSuite {

	/**
	 * Test setup method called by JUnit before tests execute.
	 * ------------------------------------------------------------------------
	 *
	 * Setup method invoked before test classes in test suite are executed.
	 * @throws Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		//System.out.println( TestSuite.class.getSimpleName() + ".setUpBeforeClass() called." );
		TestBuilder.getInstance( true );	// trigger TestBuilder initialization.
	}


	/**
	 * Test tear down method called by JUnit after tests execution.
	 * ------------------------------------------------------------------------
	 * 
	 * Tear-down method invoked after test classes in test suite have finished.
	 * @throws Exception
	 */
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		//System.out.println( TestSuite.class.getSimpleName() + ".tearDownAfterClass() called." );
		TestBuilder.getInstance().tearDown( true );
	}

}
