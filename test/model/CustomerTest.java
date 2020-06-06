package model;

import java.util.NoSuchElementException;
import java.util.Optional;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;

import model.Customer;
import testsuites.TestBuilder;
import data_access.DataRepository;


/**
 * 
 * JUnit4 tests for Customer class.
 * 
 * Use of assertions, see:
 *   https://junit.org/junit4/javadoc/latest/org/junit/Assert.html
 * 
 * @author sgra64
 */
public class CustomerTest {

	/*
	 * Constants to compare expected results.
	 */
	private final static String idLarry = "C44698";
	private final static String nameLarry = "Larry Hagman";
	private final static String firstNameLarry = "Larry";
	private final static String lastNameLarry = "Hagman";
	private final static String contactLarry = "lh812@gmx.de";

	public final static String idEric = "C86516";
	public final static String firstNameEric = "Eric";
	public final static String lastNameEric = "Schulz-Mueller";
	public final static String contactEric = "eric2346@gmail.com";

	private final static String idAnne = "C64327";
	private final static String firstNameAnne = "Anne";
	private final static String lastNameAnne = "Meyer";
	private final static String contactAnne = "+4917223524";

	private final static String idNadine = "C12396";
	private final static String firstNameNadine = "Nadine Ulla";
	private final static String lastNameNadine = "Blumenfeld";
	private final static String contactNadine = "+4915292454";

	private DataRepository<Customer,String> customerData = null;


	/**
	 * JUnit4 Test Setup Code
	 * ------------------------------------------------------------------------
	 * Setup method invoked once before any @Test method is executed.
	 * @throws Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		//System.out.println( CustomerTest.class.getSimpleName() + ".setUpBeforeClass() called." );
	}

	/**
	 * Setup method executed each time before a @Test method executes. Each @Test
	 * method executes with a new instance of this class.
	 * @throws Exception
	 */
	@Before
	public void setUp() throws Exception {
		//System.out.println( this.getClass().getSimpleName() + ".setUp() called." );
		this.customerData = TestBuilder.getInstance().dataAccess().getCustomerData();
	}


	/**
	 * JUnit4 Test Code
	 * ------------------------------------------------------------------------
	 */

	@Test
	public void testConstructor() {
		Customer cLarry = new Customer( idLarry, nameLarry, contactLarry );
		assertNotNull( cLarry.getId() );				// Customer id must not be null
		assertEquals( cLarry.getId(), idLarry );		// assert that correct ID is returned
		assertEquals( cLarry.getFirstName(), firstNameLarry );
		assertEquals( cLarry.getLastName(), lastNameLarry );
		assertEquals( cLarry.getContact(), contactLarry );
	}

	@Test
	public void testEmptyStringConstructor() {
		Customer c1 = new Customer( "", "", "" );
		assertEquals( c1.getId(), "" );
		assertEquals( c1.getFirstName(), "" );		// "" is returned, not null
		assertEquals( c1.getLastName(), "" );		// "" is returned, not null
		assertEquals( c1.getContact(), "" );
	}

	@Test
	public void testNullConstructor() {
		Customer c1 = new Customer( null, null, null );
		assertNull( c1.getId() );
		assertEquals( c1.getFirstName(), "" );		// "" is returned, not null
		assertEquals( c1.getLastName(), "" );		// "" is returned, not null
		assertNull( c1.getContact() );
	}

	@Test
	public void testCustomerData() {

		assertNotNull( customerData );

		Optional<Customer> optEric = customerData.findById( idEric );
		assertTrue( optEric.isPresent() );
		Customer cEric = optEric.get();
		assertNotNull( cEric );
		assertEquals( cEric.getFirstName(), firstNameEric );
		assertEquals( cEric.getLastName(), lastNameEric );
		assertEquals( cEric.getContact(), contactEric );

		Optional<Customer> optAnne = customerData.findById( idAnne );
		assertTrue( optAnne.isPresent() );
		Customer cAnne = optAnne.get();
		assertNotNull( cAnne );
		assertEquals( cAnne.getFirstName(), firstNameAnne );
		assertEquals( cAnne.getLastName(), lastNameAnne );
		assertEquals( cAnne.getContact(), contactAnne );

		Optional<Customer> optNadine = customerData.findById( idNadine );
		assertTrue( optNadine.isPresent() );
		Customer cNadine = optNadine.get();
		assertNotNull( cNadine );
		assertEquals( cNadine.getFirstName(), firstNameNadine );
		assertEquals( cNadine.getLastName(), lastNameNadine );
		assertEquals( cNadine.getContact(), contactNadine );
	}

	@Test
	public void testCustomerDataUpdate() {

		assertNotNull( customerData );

		long count1 = customerData.count();

		Customer cLarry = new Customer( idLarry, nameLarry, contactLarry );
		customerData.save( cLarry );
		long count2 = customerData.count();
		assertEquals( count1 + 1, count2 );

		// MUST throw exception
		NoSuchElementException nse = assertThrows( NoSuchElementException.class, () -> {
			Customer cLarryNotFound = customerData.findById( "XX--invaild_id--??//" ).get();
			assertNull( cLarryNotFound );
		});
		assertNotNull( nse );

		Customer cLarryFound = customerData.findById( idLarry ).get();
		assertNotNull( cLarryFound );
		assertSame( cLarry, cLarryFound );	// same object found as saved, cLarry == cLarryFound
	}

	@Test
	public void testCustomerNameResolution() {

		Customer cEric = new Customer( "C86516", "Eric Meyer", "eric2346@gmail.com" );
		assertNotNull( cEric.getId() );
		assertEquals( cEric.getFirstName(), "Eric" );
		assertEquals( cEric.getLastName(), "Meyer" );
		cEric.setName( "Meyer, Eric" );
		assertEquals( cEric.getFirstName(), "Eric" );
		assertEquals( cEric.getLastName(), "Meyer" );

		Customer c1 = new Customer( "", "", "" );
		c1.setName( "Nadine Ulla-Blumenfeld" );
		assertEquals( c1.getFirstName(), "Nadine" );
		assertEquals( c1.getLastName(), "Ulla-Blumenfeld" );

		c1.setName( "Blumenfeld, Nadine Ulla" );
		assertEquals( c1.getFirstName(), "Nadine Ulla" );
		assertEquals( c1.getLastName(), "Blumenfeld" );

		c1.setName( "Blumenfeld" );
		assertEquals( c1.getFirstName(), "" );
		assertEquals( c1.getLastName(), "Blumenfeld" );

		/*
		 * Ambitionierte Tests
		 * /
		c1.setName( "Blumenfeld," );
		assertEquals( c1.getFirstName(), "" );
		assertEquals( c1.getLastName(), "Blumenfeld" );

		c1.setName( ",Blumenfeld" );
		assertEquals( c1.getFirstName(), "" );
		assertEquals( c1.getLastName(), "Blumenfeld" );

		c1.setName( " , , Blumenfeld , ,, " );
		assertEquals( c1.getFirstName(), "" );
		assertEquals( c1.getLastName(), "Blumenfeld" );

		c1.setName( "Nadine Ulla-Mona Blumenfeld-Meyer" );
		assertEquals( c1.getFirstName(), "Nadine Ulla-Mona" );
		assertEquals( c1.getLastName(), "Blumenfeld-Meyer" );

		c1.setName( "Nadine Ulla-Mona-Blumenfeld-Meyer" );
		assertEquals( c1.getFirstName(), "Nadine" );
		assertEquals( c1.getLastName(), "Ulla-Mona-Blumenfeld-Meyer" );
		/* */
	}


	/**
	 * JUnit4 Test Tear-down Code
	 * ------------------------------------------------------------------------
	 * 
	 * Tear-down method invoked each time after a @Test method has finished.
	 * @throws Exception
	 */
	@After
	public void tearDown() throws Exception {
		//System.out.println( this.getClass().getSimpleName() + ".tearDown() called." );
	}

	/**
	 * Tear-down method invoked once after all @Test methods in this class have finished.
	 * @throws Exception
	 */
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		//System.out.println( CustomerTest.class.getSimpleName() + ".tearDownAfterClass() called." );
		TestBuilder.getInstance().tearDown();
	}

}
