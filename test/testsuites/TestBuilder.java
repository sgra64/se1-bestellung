package testsuites;

import data_access.DataAccess;
import system.Logic;
import system.builder.Builder;


/**
 * Singleton class to build the Test-Environment. Class uses the
 * Builder-class of the main application to set up all component
 * instances (instances implementing Logic.java and DataAccess.java
 * interfaces) including data repositories with mock data.
 * 
 * @author sgra64
 *
 */
public class TestBuilder {

	private static TestBuilder instance = null;

	private final boolean runFromTestSuite;

	private final Builder builder;

	private Logic logic = null;

	private DataAccess dataAccess = null;


	/*
	 * Private constructor according to the Singleton pattern.
	 */
	private TestBuilder( boolean runFromTestSuite ) {
		this.runFromTestSuite = runFromTestSuite;
		System.out.println( "TestBuilder set up Unit Tests Bestellsystem SE1." );
		this.builder = Builder.getInstance();
	}


	/**
	 * Public access methods to singleton TestBuilder instance.
	 * 
	 * @param runFromTestSuite indicator that method is called from TestSuite
	 * @return singleton TestBuilder instance
	 */
	public static TestBuilder getInstance( boolean runFromTestSuite ) {
		if( instance == null ) {
			instance = new TestBuilder( runFromTestSuite );
			instance.buildTestEnvironment();
		}
		return instance;
	}

	public static TestBuilder getInstance() {
		return getInstance( false );
	}


	/**
	 * Provide access to instance that implements the Logic interface.
	 * 
	 * @return instance that implements the Logic interface
	 */
	public Logic logic() {
		return logic;
	}


	/**
	 * Provide access to instance that implements the DataAccess interface
	 * containing data repositories for Customer, Article and Order objects.
	 * 
	 * @return instance that implements the DataAccess interface
	 */
	public DataAccess dataAccess() {
		return dataAccess;
	}


	/**
	 * Public method called when test environment is torn down.
	 * 
	 */
	public void tearDown() {
		tearDown( ! runFromTestSuite );
	}


	/**
	 * Public method called when test environment is torn down.
	 * 
	 * @param runFromTestSuite indicator that method is called from TestSuite
	 */
	public void tearDown( boolean runFromTestSuite ) {
		if( runFromTestSuite ) {
			System.out.println( "TestBuilder tear down." );
			builder.shutdown();
		}
	}


	/*
	 * Private methods.
	 */

	private void buildTestEnvironment() {

		builder.startup();

		logic = builder.logic();
		dataAccess = builder.dataAccess();

		builder
			.build( 1, _builder -> {
				//logic.printInventory();
			})
			.build( 2 );
	}

}
