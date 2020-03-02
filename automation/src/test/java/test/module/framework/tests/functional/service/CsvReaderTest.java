package test.module.framework.tests.functional.service;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.testng.annotations.Test;

import core.apiCore.TestDataProvider;
import core.apiCore.helpers.CsvReader;
import core.helpers.Helper;
import core.support.configReader.Config;
import test.module.framework.TestBase;

// these include actions tests. Action tests are added to existing tests when invoked in csv file
public class CsvReaderTest extends TestBase {
	
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
		List<Object[]> list = CsvReader.getTestCasesFromCsvFile();	
		Helper.assertEquals(2, list.size());
	}
	
	@Test(description = "csv file multiple runs set for different tests")
	public void getTestCasesFromCsvFile_csv_multirun() throws Exception {
		Config.putValue(TestDataProvider.TEST_DATA_PARALLEL_PATH,  "../apiTestData/testCases/frameworkTests/multirun/");
		Config.putValue(TestDataProvider.TEST_CASE_FILE, "TestCases_UserValidation_multirun.csv");

		List<Object[]> list = CsvReader.getTestCasesFromCsvFile();	
		Helper.assertEquals(12, list.size());
	}
	
	@Test(description = "csv file with tests combined using test steps postfix")
	public void getTestCasesFromCsvFile_csv_test_steps() throws Exception {
		Config.putValue(TestDataProvider.TEST_DATA_PARALLEL_PATH,  "../apiTestData/testCases/frameworkTests/testStep/");
		Config.putValue(TestDataProvider.TEST_CASE_FILE, "TestCases_UserValidation_step.csv");

		List<Object[]> list = CsvReader.getTestCasesFromCsvFile();	
		Helper.assertEquals(4, list.size());
	}
	
	@Test(description = "csv file escape characters and quotes")
	public void getTestCasesFromCsvFile_csv_escape_character() throws Exception {
		Config.putValue(TestDataProvider.TEST_DATA_PARALLEL_PATH,  "../apiTestData/testCases/frameworkTests/escapeChar/");
		Config.putValue(TestDataProvider.TEST_CASE_FILE, "TestCases_escapeChar.csv");

		List<Object[]> list = CsvReader.getTestCasesFromCsvFile();	
		Helper.assertEquals(1, list.size());
		Helper.assertTrue("list doesnt include escape chars: " + Arrays.toString(list.get(0)), Arrays.toString(list.get(0)).contains("\\\""));
	}
	
	@Test(description = "csv file from subdirectories")
	public void getTestCasesFromCsvFile_sub_directories() throws Exception {
		Config.putValue(TestDataProvider.TEST_DATA_PARALLEL_PATH,  "../apiTestData/testCases/frameworkTests/testSubDir/");
		Config.putValue(CsvReader.SERVICE_CSV_INCLUDE_SUB_DIR, true);
		
		ArrayList<File> testCsvList = CsvReader.getTestDataCsvFileList();
		Helper.assertEquals(6, testCsvList.size());
		
		Config.putValue(CsvReader.SERVICE_CSV_INCLUDE_SUB_DIR, false);	
		testCsvList = CsvReader.getTestDataCsvFileList();
		Helper.assertEquals(2, testCsvList.size());
	}
}
