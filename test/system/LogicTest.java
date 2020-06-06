package system;

import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import data_access.DataAccess;
import data_access.DataRepository;
import model.Article;
import model.Customer;
import model.CustomerTest;
import model.Order;
import model.OrderItem;
import testsuites.TestBuilder;


/**
 * 
 * JUnit4 tests for Logic interface.
 * 
 * Use of assertions, see:
 *   https://junit.org/junit4/javadoc/latest/org/junit/Assert.html
 * 
 * @author sgra64
 */
public class LogicTest {

	/*
	 * Constants to compare expected results.
	 */
	private final static long idO5234 = 5234968294L;	// id of Eric's 1st order
	private final static long idO8592 = 8592356245L;	// id of Eric's 2nd order
	private final static long idO3563 = 3563561357L;	// id of Anne's order
	private final static long idO6135 = 6135735635L;	// id of Nadine's order
	private final static long idO9235 = 9235856245L;	// id of Timo's order
	private final static long idO7335 = 7335856245L;	// id of Sandra's order
	private final static long idO7356 = 7356613535L;	// id of Nadine's 2nd order

	private Logic logic = null;

	private DataRepository<Customer,String> customerData = null;

	private DataRepository<Article,String> articleData = null;

	private DataRepository<Order,Long> orderData = null;


	/**
	 * JUnit4 Test Setup Code
	 * ------------------------------------------------------------------------
	 * Setup method invoked once before any @Test method is executed.
	 * @throws Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		//System.out.println( LogicTest.class.getSimpleName() + ".setUpBeforeClass() called." );
	}

	/**
	 * Setup method executed each time before a @Test method executes. Each @Test
	 * method executes with a new instance of this class.
	 * @throws Exception
	 */
	@Before
	public void setUp() throws Exception {
		//System.out.println( this.getClass().getSimpleName() + ".setUp() called." );
		TestBuilder tb = TestBuilder.getInstance();
		DataAccess da = tb.dataAccess();
		this.logic = tb.logic();
		this.customerData = da.getCustomerData();
		this.articleData = da.getArticleData();
		this.orderData = da.getOrderData();
	}


	/**
	 * JUnit4 Test Code
	 * ------------------------------------------------------------------------
	 */

	@Test
	public void testOrderItems() {

		assertNotNull( orderData );

		// Eric's 1st order
		Optional<Order> opt5234 = orderData.findById( idO5234 );
		assertTrue( opt5234.isPresent() );
		Order o5234 = opt5234.get();
		assertNotNull( o5234 );
		Customer cEric = o5234.getCustomer();
		Customer cEricx = customerData.findById( CustomerTest.idEric ).get();
		assertSame( cEric, cEricx );

		List<OrderItem> items5234 = o5234.getItems();
		assertEquals( items5234.size(), 1 );

		OrderItem oi5234_1 = items5234.get( 0 );
		Article a3 = oi5234_1.getArticle();
		Article a3x = articleData.findById( "SKU-518957" ).get();
		assertSame( a3, a3x );
	}

	@Test
	public void testOrderCalculations() {
		Order o5234 = orderData.findById( idO5234 ).get();	// 2000, 319
		long total5234 = logic.calculateTotal( o5234 );
		assertEquals( total5234, 2000L );
		long vat5234 = logic.calculateIncludedVAT( o5234 );
		assertEquals( vat5234, 319L );

		Order o8592 = orderData.findById( idO8592 ).get();	// 4984, 796
		assertEquals( logic.calculateTotal( o8592 ), 4984L );
		assertEquals( logic.calculateIncludedVAT( o8592 ), 796 );

		Order o3563 = orderData.findById( idO3563 ).get();	// 2000, 319
		assertEquals( logic.calculateTotal( o3563 ), 2000L );
		assertEquals( logic.calculateIncludedVAT( o3563 ), 319 );

		Order o6135 = orderData.findById( idO6135 ).get();	// 7788,1243
		assertEquals( logic.calculateTotal( o6135 ), 7788L );
		assertEquals( logic.calculateIncludedVAT( o6135 ), 1243 );

		Order o9235 = orderData.findById( idO9235 ).get();	// 4793, 765
		assertEquals( logic.calculateTotal( o9235 ), 4793L );
		assertEquals( logic.calculateIncludedVAT( o9235 ), 765 );

		Order o7335 = orderData.findById( idO7335 ).get();	// 5191, 829
		assertEquals( logic.calculateTotal( o7335 ), 5191L );
		assertEquals( logic.calculateIncludedVAT( o7335 ), 829 );

		Order o7356 = orderData.findById( idO7356 ).get();	// 3894, 622
		assertEquals( logic.calculateTotal( o7356 ), 3894L );
		assertEquals( logic.calculateIncludedVAT( o7356 ), 622 );
	}

	@Test
	public void testTotalOrderValue() {
		long total = 0L;
		long vat = 0L;
		for( Order order : orderData.findAll() ) {
			long orderTotal = 0L;
			for( OrderItem item : order.getItems() ) {
				orderTotal += item.getUnitsOrdered() * item.getArticle().getUnitPrice();
			}
			long orderTotalCalculated = logic.calculateTotal( order );
			assertEquals( orderTotal, orderTotalCalculated );
			total += orderTotalCalculated;
			long vatCalculated = logic.calculateIncludedVAT( order );
			vat += vatCalculated;
		}
		assertEquals( total, 30650L );	// total order value: 306,50; 48,93 VAT
		assertEquals( vat, 4893L );
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
		//System.out.println( LogicTest.class.getSimpleName() + ".tearDownAfterClass() called." );
		TestBuilder.getInstance().tearDown();
	}

}
