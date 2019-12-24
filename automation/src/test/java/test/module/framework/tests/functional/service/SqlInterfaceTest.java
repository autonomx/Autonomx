package test.module.framework.tests.functional.service;


import java.util.concurrent.ConcurrentHashMap;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import core.apiCore.helpers.DataHelper;
import core.apiCore.interfaces.SqlInterface;
import core.helpers.Helper;
import core.support.configReader.Config;
import core.support.logger.TestLog;
import core.support.objects.DatabaseObject;
import core.support.objects.ServiceObject;
import core.support.objects.TestObject;
import test.module.framework.TestBase;

/**
 * @author ehsan matean
 *
 */
public class SqlInterfaceTest extends TestBase {
	
	
	@BeforeMethod(alwaysRun = true)
	public void beforeMethod() throws Exception {
		// reset databases hashmap
		DatabaseObject.DATABASES = new ConcurrentHashMap<Integer, DatabaseObject>();
	}
	
	@Test()
	public void verifySqlStatement() {	
		ServiceObject serviceObject = new ServiceObject()
				.withRequestBody("SELECT * FROM PARTY.INDIVIDUAL WHERE ID='AAAAAAAA-AAAA-AAAA-AAAA-AAAA11111111'");
		
		// replace parameters for request body
		serviceObject.withRequestBody(DataHelper.replaceParameters(serviceObject.getRequestBody()));

		
		// get query
		String sql = serviceObject.getRequestBody();
		Helper.assertEquals("SELECT * FROM PARTY.INDIVIDUAL WHERE ID='AAAAAAAA-AAAA-AAAA-AAAA-AAAA11111111'", sql);

	}
	
	@Test()
	public void setDatabaseMap_valid() {
		
		TestLog.When("I set values for db0, db1 and db2");

		// set db0
		Config.putValue("db.driver", "org.postgresql.Driver0");
		Config.putValue("db.url", "jdbc:postgresql://localhost:5555");
		Config.putValue("db.name", "testdb2");
		Config.putValue("db.username", "postgres2");
		Config.putValue("db.password", "789");
		
		// set db1
		Config.putValue("db.1.driver", "org.postgresql.Driver");
		Config.putValue("db.1.url", "jdbc:postgresql://localhost:5432");
		Config.putValue("db.1.name", "testdb");
		Config.putValue("db.1.username", "postgres");
		Config.putValue("db.1.password", "123");
		
		// set db2
		Config.putValue("db.2.driver", "mongodb.jdbc.MongoDriver");
		Config.putValue("db.2.url", "jdbc:mongo://ds029847.mongolab.com:29847");
		Config.putValue("db.2.name", "testdb3");
		Config.putValue("db.2.username", "mongodb");
		Config.putValue("db.2.password", "456");
		
		SqlInterface.setDatabaseMap();
		
		Helper.assertEquals(3, DatabaseObject.DATABASES.size());
		
		TestLog.Then("I validate dabatabse 0");
		Helper.assertEquals("org.postgresql.Driver0", DatabaseObject.DATABASES.get(0).getDriver());
		Helper.assertEquals("jdbc:postgresql://localhost:5555", DatabaseObject.DATABASES.get(0).getUrl());
		Helper.assertEquals("testdb2", DatabaseObject.DATABASES.get(0).getDatabaseName());
		Helper.assertEquals("postgres2", DatabaseObject.DATABASES.get(0).getUsername());
		Helper.assertEquals("789", DatabaseObject.DATABASES.get(0).getPassword());
		
		TestLog.Then("I validate dabatabse 1");
		Helper.assertEquals( "org.postgresql.Driver", DatabaseObject.DATABASES.get(1).getDriver());
		Helper.assertEquals("jdbc:postgresql://localhost:5432", DatabaseObject.DATABASES.get(1).getUrl());
		Helper.assertEquals("testdb", DatabaseObject.DATABASES.get(1).getDatabaseName());
		Helper.assertEquals("postgres", DatabaseObject.DATABASES.get(1).getUsername());
		Helper.assertEquals("123", DatabaseObject.DATABASES.get(1).getPassword());
		
		TestLog.And("I validate dabatabse 2");
		Helper.assertEquals( "mongodb.jdbc.MongoDriver", DatabaseObject.DATABASES.get(2).getDriver());
		Helper.assertEquals("jdbc:mongo://ds029847.mongolab.com:29847", DatabaseObject.DATABASES.get(2).getUrl());
		Helper.assertEquals("testdb3", DatabaseObject.DATABASES.get(2).getDatabaseName());
		Helper.assertEquals("mongodb", DatabaseObject.DATABASES.get(2).getUsername());
		Helper.assertEquals("456", DatabaseObject.DATABASES.get(2).getPassword());
	}
	
	@Test()
	public void setDatabaseMap_defaultDb_valid_position_0() {
		
		TestLog.When("I set values for db0, db1");

		// set db0
		Config.putValue("db.driver", "org.postgresql.Driver0");
		Config.putValue("db.url", "jdbc:postgresql://localhost:5555");
		Config.putValue("db.name", "testdb2");
		Config.putValue("db.username", "postgres2");
		Config.putValue("db.password", "789");
		
		// set db1
		Config.putValue("db.1.driver", "org.postgresql.Driver");
		Config.putValue("db.1.url", "jdbc:postgresql://localhost:5432");
		Config.putValue("db.1.name", "testdb");
		Config.putValue("db.1.username", "postgres");
		Config.putValue("db.1.password", "123");
		
		SqlInterface.setDatabaseMap();
		
		// set default database at position 0(no position) or 1(position 1)
		SqlInterface.setDefaultDatabase();
		
		DatabaseObject currentDb = (DatabaseObject) Config.getObjectValue(SqlInterface.SQL_CURRENT_DATABASE);
		Helper.assertEquals("org.postgresql.Driver0", currentDb.getDriver());
		Helper.assertEquals("jdbc:postgresql://localhost:5555", currentDb.getUrl());
		Helper.assertEquals("testdb2", currentDb.getDatabaseName());
		Helper.assertEquals("postgres2", currentDb.getUsername());
		Helper.assertEquals("789", currentDb.getPassword());
	}
	
	@Test()
	public void setDatabaseMap_defaultDb_valid_position_1() {
		
		TestLog.When("I set values for db1, db2");
		
		// set db1
		Config.putValue("db.1.driver", "org.postgresql.Driver");
		Config.putValue("db.1.url", "jdbc:postgresql://localhost:5432");
		Config.putValue("db.1.name", "testdb");
		Config.putValue("db.1.username", "postgres");
		Config.putValue("db.1.password", "123");
		
		// set db2
		Config.putValue("db.2.driver", "mongodb.jdbc.MongoDriver");
		Config.putValue("db.2.url", "jdbc:mongo://ds029847.mongolab.com:29847");
		Config.putValue("db.2.name", "testdb3");
		Config.putValue("db.2.username", "mongodb");
		Config.putValue("db.2.password", "456");
		
		SqlInterface.setDatabaseMap();
		
		// set default database at position 0(no position) or 1(position 1)
		SqlInterface.setDefaultDatabase();
		
		DatabaseObject currentDb = (DatabaseObject) Config.getObjectValue(SqlInterface.SQL_CURRENT_DATABASE);
		Helper.assertEquals( "org.postgresql.Driver", currentDb.getDriver());
		Helper.assertEquals("jdbc:postgresql://localhost:5432", currentDb.getUrl());
		Helper.assertEquals("testdb", currentDb.getDatabaseName());
		Helper.assertEquals("postgres", currentDb.getUsername());
		Helper.assertEquals("123", currentDb.getPassword());
	}
	
	@Test()
	public void evaluateOption_update_database() {
		
		TestLog.When("I set values for db1, db2");
		
		// set db1
		Config.putValue("db.1.driver", "org.postgresql.Driver");
		Config.putValue("db.1.url", "jdbc:postgresql://localhost:5432");
		Config.putValue("db.1.name", "testdb");
		Config.putValue("db.1.username", "postgres");
		Config.putValue("db.1.password", "123");
		
		// set db2
		Config.putValue("db.2.driver", "mongodb.jdbc.MongoDriver");
		Config.putValue("db.2.url", "jdbc:mongo://ds029847.mongolab.com:29847");
		Config.putValue("db.2.name", "testdb3");
		Config.putValue("db.2.username", "mongodb");
		Config.putValue("db.2.password", "456");
		
		SqlInterface.setDatabaseMap();
		
		// set default database at position 0(no position) or 1(position 1)
		SqlInterface.setDefaultDatabase();
		
		// update database to db2
		ServiceObject service = new ServiceObject().withOption("database:db.2");
		SqlInterface.evaluateOption(service);
		
		DatabaseObject currentDb = (DatabaseObject) Config.getObjectValue(SqlInterface.SQL_CURRENT_DATABASE);
		Helper.assertEquals( "mongodb.jdbc.MongoDriver", currentDb.getDriver());
		Helper.assertEquals("jdbc:mongo://ds029847.mongolab.com:29847", currentDb.getUrl());
		Helper.assertEquals("testdb3", currentDb.getDatabaseName());
		Helper.assertEquals("mongodb", currentDb.getUsername());
		Helper.assertEquals("456", currentDb.getPassword());
	}
	
	@Test()
	public void sqlInterface_valid() throws Exception {

		Config.putValue("db.1.driver", "org.postgresql.Driver");
		Config.putValue("db.1.url", "jdbc:postgresql://localhost:5432");
		Config.putValue("db.1.name", "world-db");
		Config.putValue("db.1.username", "world");
		Config.putValue("db.1.password", "world123");
		
		TestObject.getDefaultTestInfo().config.put(SqlInterface.DB_TIMEOUT_VALIDATION_ENABLED, "false");
		TestObject.getDefaultTestInfo().config.put(SqlInterface.DB_TIMEOUT_VALIDATION_SECONDS, "4");
		Config.putValue(SqlInterface.DB_TIMEOUT_VALIDATION_ENABLED, "false");
		Config.putValue(SqlInterface.DB_TIMEOUT_VALIDATION_SECONDS, "4");

		ServiceObject serviceObject = new ServiceObject()
				.withRequestBody("select * From city where NAME='Herat'")
				.withExpectedResponse("name:1:equalTo(Herat)");
				
		SqlInterface.DataBaseInterface(serviceObject);
		
		serviceObject = new ServiceObject()
				.withRequestBody("select * From city where NAME='Kabul'")
				.withExpectedResponse("name:1:equalTo(Kabul)");
				
		SqlInterface.DataBaseInterface(serviceObject);
	}
	
	@Test()
	public void sqlInterface_valid_multiple_expected() throws Exception {

		Config.putValue("db.1.driver", "org.postgresql.Driver");
		Config.putValue("db.1.url", "jdbc:postgresql://localhost:5432");
		Config.putValue("db.1.name", "world-db");
		Config.putValue("db.1.username", "world");
		Config.putValue("db.1.password", "world123");

		ServiceObject serviceObject = new ServiceObject()
				.withRequestBody("select * From city")
				.withExpectedResponse("name:1:equalTo(Kabul); DISTRICT:1:equalTo(Kabol); ID:1:equalTo(1)");
				
		SqlInterface.DataBaseInterface(serviceObject);
	}
	
	@Test(expectedExceptions = { AssertionError.class } )
	public void sqlInterface_valid_invalid_response() throws Exception {

		Config.putValue("db.1.driver", "org.postgresql.Driver");
		Config.putValue("db.1.url", "jdbc:postgresql://localhost:5432");
		Config.putValue("db.1.name", "world-db");
		Config.putValue("db.1.username", "world");
		Config.putValue("db.1.password", "world123");

		ServiceObject serviceObject = new ServiceObject()
				.withRequestBody("select * From city")
				.withExpectedResponse("name:1:equalTo(Kabul2);");
				
		SqlInterface.DataBaseInterface(serviceObject);
	}
	
	@Test(expectedExceptions = { AssertionError.class } )
	public void sqlInterface_valid_invalid_invalid_query() throws Exception {

		Config.putValue("db.1.driver", "org.postgresql.Driver");
		Config.putValue("db.1.url", "jdbc:postgresql://localhost:5432");
		Config.putValue("db.1.name", "world-db");
		Config.putValue("db.1.username", "world");
		Config.putValue("db.1.password", "world123");

		ServiceObject serviceObject = new ServiceObject()
				.withRequestBody("select * From city where NAME=Kabul2")
				.withExpectedResponse("name:1:equalTo(Kabul2); DISTRICT:1:equalTo(Kabol);");
				
		SqlInterface.DataBaseInterface(serviceObject);
	}
	
	@Test()
	public void sqlInterface_valid_save_outbound() throws Exception {

		Config.putValue("db.1.driver", "org.postgresql.Driver");
		Config.putValue("db.1.url", "jdbc:postgresql://localhost:5432");
		Config.putValue("db.1.name", "world-db");
		Config.putValue("db.1.username", "world");
		Config.putValue("db.1.password", "world123");

		ServiceObject serviceObject = new ServiceObject()
				.withRequestBody("select * From city where NAME='Herat'")
				.withOutputParams("name:1:<$city>")
				.withExpectedResponse("name:1:equalTo(Herat)");
				
				
		SqlInterface.DataBaseInterface(serviceObject);
		Helper.assertEquals("Herat", Config.getValue("city"));
	}
	
	@Test()
	public void sqlInterface_valid_list_response() throws Exception {

		Config.putValue("db.1.driver", "org.postgresql.Driver");
		Config.putValue("db.1.url", "jdbc:postgresql://localhost:5432");
		Config.putValue("db.1.name", "world-db");
		Config.putValue("db.1.username", "world");
		Config.putValue("db.1.password", "world123");

		ServiceObject serviceObject = new ServiceObject()
				.withRequestBody("select * From city where NAME='Herat'")
				.withExpectedResponse("name:1:equalTo(Herat)");
				
		SqlInterface.DataBaseInterface(serviceObject);
		
		serviceObject = new ServiceObject()
				.withRequestBody("select * From city")
				.withExpectedResponse("name:contains(Kabul);name:contains(Herat);name:1:equalTo(Kabul)");
				
		SqlInterface.DataBaseInterface(serviceObject);
	}
	
	@Test()
	public void sqlInterface_valid_option_select_db2() throws Exception {
		
		Config.setGlobalValue("db.1.driver", "mongodb.jdbc.MongoDriver");
		Config.setGlobalValue("db.1.url", "jdbc:mongo://ds029847.mongolab.com:29847");
		Config.setGlobalValue("db.1.name", "testdb3");
		Config.setGlobalValue("db.1.username", "mongodb");
		Config.setGlobalValue("db.1.password", "456");
		
		Config.putValue("db.1.driver", "mongodb.jdbc.MongoDriver");
		Config.putValue("db.1.url", "jdbc:mongo://ds029847.mongolab.com:29847");
		Config.putValue("db.1.name", "testdb3");
		Config.putValue("db.1.username", "mongodb");
		Config.putValue("db.1.password", "456");

		Config.setGlobalValue("db.2.driver", "org.postgresql.Driver");
		Config.setGlobalValue("db.2.url", "jdbc:postgresql://localhost:5432");
		Config.setGlobalValue("db.2.name", "world-db");
		Config.setGlobalValue("db.2.username", "world");
		Config.setGlobalValue("db.2.password", "world123");
		
		Config.putValue("db.2.driver", "org.postgresql.Driver");
		Config.putValue("db.2.url", "jdbc:postgresql://localhost:5432");
		Config.putValue("db.2.name", "world-db");
		Config.putValue("db.2.username", "world");
		Config.putValue("db.2.password", "world123");

		ServiceObject serviceObject = new ServiceObject()
				.withOption("database:2")
				.withRequestBody("select * From city where NAME='Herat'")
				.withExpectedResponse("name:1:equalTo(Herat)");
				
		SqlInterface.DataBaseInterface(serviceObject);
	}
	
	@Test(expectedExceptions = { AssertionError.class }, priority=1)
	public void sqlInterface_valid_validation_timeout() throws Exception {

		Config.putValue("db.1.driver", "org.postgresql.Driver");
		Config.putValue("db.1.url", "jdbc:postgresql://localhost:5432");
		Config.putValue("db.1.name", "world-db");
		Config.putValue("db.1.username", "world");
		Config.putValue("db.1.password", "world123");
		Config.setGlobalValue("db.timeout.validation.isEnabled", "true");
		Config.setGlobalValue("db.timeout.validation.seconds", "3");

		ServiceObject serviceObject = new ServiceObject()
				.withRequestBody("select * From city where NAME='Herat'")
				.withExpectedResponse("name:1:equalTo(Herat2)");
				
		SqlInterface.DataBaseInterface(serviceObject);
	}
	
	
}