package test.module.framework.tests.functional;


import java.io.File;

import org.apache.commons.lang.StringUtils;
import org.testng.annotations.Test;

import core.helpers.Helper;
import core.helpers.csvHelper.CsvObject;
import core.support.logger.TestLog;
import data.Data;
import data.framework.User;
import test.module.framework.TestBase;

/**
 * @author ehsan matean
 *
 */
public class DataFileTest extends TestBase {
	
	String email1 = StringUtils.EMPTY;
	
	@Test()
	public void verifyUserCsvFileUpdate() throws Exception {
		String path = Helper.getRootDir() + "src" + File.separator + "main" + File.separator + "java" + File.separator + "module" + File.separator + "framework" + File.separator + "data" + File.separator;
		String csvFile = "User.csv";
		
		TestLog.When("I update name to james");
		User user = Data.framework.user().admin();
		user = user.withName("james");
		
		TestLog.Then("I verify name has changed to james");
		Helper.assertEquals("james", user.getName()); 

		TestLog.When("I update name in csv to dave");
		user.updateCsvName("dave");
		
		TestLog.Then("I verify name has changed to dave in csv file");
		int column = Helper.csv.getColumnIndex("name", path + csvFile);
		int row = Helper.csv.getRowIndex("admin", path + csvFile);
		CsvObject csv = new CsvObject().withCsvPath(path).withCsvFile(csvFile).withRow(row).withColumn(column);
		String newName = Helper.csv.getCellData(csv);
		Helper.assertEquals("dave", newName); 
	}
	
	@Test(description = "value replacement gets new values per test method")
	public void verifyCsvRandDataTest_part1() {
		User user = Data.framework.user().admin();
		email1 = user.getEmail();
		String email2 = user.getEmail();
		
		Helper.assertEquals(email1, email2);
	}
	
	@Test(dependsOnMethods = "verifyCsvRandDataTest_part1")
	public void verifyCsvRandDataTest_part2() {
		User user = Data.framework.user().admin();
		String email = user.getEmail();
		String email2 = user.getEmail();
		
		Helper.assertEquals(email, email2);
		Helper.assertTrue("passwords should not be the same", !email1.equals(email));
	}
}