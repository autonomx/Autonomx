package test.module.framework.tests.functional.service;

import java.util.List;

import org.testng.annotations.Test;

import core.apiCore.TestDataProvider;
import core.apiCore.helpers.CsvReader;
import core.helpers.Helper;
import core.support.configReader.Config;

// these include actions tests. Action tests are added to existing tests when invoked in csv file
public class CsvReaderTest {
	
	@Test(description = "update csv list with action tests")
	public void getTestCasesFromCsvFile_action_valid_singleAction() throws Exception {
		Config.putValue(TestDataProvider.TEST_DATA_PARALLEL_PATH,  "../apiTestData/testCases/frameworkTests/actionTest/");
		Config.putValue(TestDataProvider.TEST_CASE_FILE, "TestCases_UserValidationSingleAction.csv");
		Config.putValue(TestDataProvider.TEST_DATA_ACTION_PATH, "../apiTestData/testCases/frameworkTests/actionTest/action");
		List<Object[]> csvTests = CsvReader.getTestCasesFromCsvFile();		
		Helper.assertEquals(4, csvTests.size());	
	}
	
	@Test(description = "update csv list with 2 action tests")
	public void getTestCasesFromCsvFile_action_valid_multipleAction() throws Exception {
		Config.putValue(TestDataProvider.TEST_DATA_PARALLEL_PATH,  "../apiTestData/testCases/frameworkTests/actionTest/");
		Config.putValue(TestDataProvider.TEST_CASE_FILE, "TestCases_UserValidationMultipleAction.csv");
		Config.putValue(TestDataProvider.TEST_DATA_ACTION_PATH, "../apiTestData/testCases/frameworkTests/actionTest/action");
		List<Object[]> csvTests = CsvReader.getTestCasesFromCsvFile();		
		Helper.assertEquals(7, csvTests.size());	
	}

	@Test(description = "update csv list with action tests", expectedExceptions = { AssertionError.class })
	public void getTestCasesFromCsvFile_action_invalid_wrong_folder() throws Exception {
		Config.putValue(TestDataProvider.TEST_DATA_PARALLEL_PATH,  "../apiTestData/testCases/frameworkTests/actionTest/");
		Config.putValue(TestDataProvider.TEST_CASE_FILE, "TestCases_UserValidationSingleAction.csv");
		Config.putValue(TestDataProvider.TEST_DATA_ACTION_PATH, "../apiTestData/testCases/frameworkTests/actionTest/action2");
		CsvReader.getTestCasesFromCsvFile();		
	}
	
	@Test(description = "verify wrong csv test folder throws assert error", expectedExceptions = { AssertionError.class })
	public void getTestCasesFromCsvFile_wrong_test_case_folder() throws Exception {
		Config.putValue(TestDataProvider.TEST_DATA_PARALLEL_PATH,  "../apiTestData/testCases/frameworkTests/actionTest2/");
		Config.putValue(TestDataProvider.TEST_CASE_FILE, "TestCases_UserValidationSingleAction.csv");
		Config.putValue(TestDataProvider.TEST_DATA_ACTION_PATH, "../apiTestData/testCases/frameworkTests/actionTest/action");
		CsvReader.getTestCasesFromCsvFile();		
	}
	
	@Test(description = "csv file with no action tests with empty action folder")
	public void getTestCasesFromCsvFile_no_action_invalid_wrong_folder() throws Exception {
		Config.putValue(TestDataProvider.TEST_DATA_PARALLEL_PATH,  "../apiTestData/testCases/frameworkTests/actionTest/");
		Config.putValue(TestDataProvider.TEST_CASE_FILE, "TestCases_UserValidationNoAction.csv");
		Config.putValue(TestDataProvider.TEST_DATA_ACTION_PATH, "../apiTestData/testCases/frameworkTests/actionTest/action2");
		CsvReader.getTestCasesFromCsvFile();		
	}
}
