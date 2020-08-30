package test.module.framework.tests.functional.service;

import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import core.apiCore.TestDataProvider;
import core.apiCore.helpers.CsvReader;
import core.helpers.Helper;
import core.support.configReader.Config;
import core.support.logger.TestLog;
import test.module.framework.TestBase;

// these include actions tests. Action tests are added to existing tests when invoked in csv file
public class CsvReaderTest extends TestBase {
	
	
	@BeforeMethod()
	public void beforeMethod(Method method) {
		TestDataProvider.csvFileIndex = new AtomicInteger(0);
	}
	
	@Test(description = "update csv list with action tests")
	public void getTestCasesFromCsvFile_action_valid_singleAction() throws Exception {
		Config.putValue(TestDataProvider.TEST_DATA_PARALLEL_PATH,  "../apiTestData/testCases/frameworkTests/actionTest/");
		Config.putValue(CsvReader.SERVICE_INCLUDE_LIST, "TestCases_UserValidationSingleAction.csv");
		Config.putValue(TestDataProvider.TEST_DATA_ACTION_PATH, "../apiTestData/testCases/frameworkTests/actionTest/keywords/action");
		List<Object> csvTests = CsvReader.getTestCasesFromCsvFile();	
		Helper.assertEquals(5, csvTests.size());	
	}
	
	@Test(description = "update csv list with action tests")
	public void getTestCasesFromCsvFile_action_valid_singleAction_selectTestId() throws Exception {
		Config.putValue(TestDataProvider.TEST_DATA_PARALLEL_PATH,  "../apiTestData/testCases/frameworkTests/actionTest/");
		Config.putValue(CsvReader.SERVICE_INCLUDE_LIST, "TestCases_UserValidationSingleActionSelectTestId.csv");
		Config.putValue(TestDataProvider.TEST_DATA_ACTION_PATH, "../apiTestData/testCases/frameworkTests/actionTest/keywords/action");
		List<Object> csvTests = CsvReader.getTestCasesFromCsvFile();	
		Helper.assertEquals(4, csvTests.size());	
	}
	
	@Test(description = "update csv list with action tests")
	public void getTestCasesFromCsvFile_action_valid_AsOption() throws Exception {
		Config.putValue(TestDataProvider.TEST_DATA_PARALLEL_PATH,  "../apiTestData/testCases/frameworkTests/actionTest/");
		Config.putValue(CsvReader.SERVICE_INCLUDE_LIST, "TestCases_UserValidationActionAsOption.csv");
		Config.putValue(TestDataProvider.TEST_DATA_ACTION_PATH, "../apiTestData/testCases/frameworkTests/actionTest/keywords/action");
		List<Object> csvTests = CsvReader.getTestCasesFromCsvFile();	
		Helper.assertEquals(5, csvTests.size());	
	}
	
	@Test(description = "update csv list with 2 action tests")
	public void getTestCasesFromCsvFile_action_valid_multipleAction() throws Exception {
		Config.putValue(TestDataProvider.TEST_DATA_PARALLEL_PATH,  "../apiTestData/testCases/frameworkTests/actionTest/");
		Config.putValue(CsvReader.SERVICE_INCLUDE_LIST, "TestCases_UserValidationMultipleAction.csv");
		Config.putValue(TestDataProvider.TEST_DATA_ACTION_PATH, "../apiTestData/testCases/frameworkTests/actionTest/keywords/action");
		List<Object> csvTests = CsvReader.getTestCasesFromCsvFile();		
		Helper.assertEquals(6, csvTests.size());	
	}

	@Test(description = "update csv list with action tests", expectedExceptions = { AssertionError.class })
	public void getTestCasesFromCsvFile_action_invalid_wrong_folder() throws Exception {
		Config.putValue(TestDataProvider.TEST_DATA_PARALLEL_PATH,  "../apiTestData/testCases/frameworkTests/actionTest/");
		Config.putValue(CsvReader.SERVICE_INCLUDE_LIST, "TestCases_UserValidationSingleAction.csv");
		Config.putValue(TestDataProvider.TEST_DATA_ACTION_PATH, "../apiTestData/testCases/frameworkTests/actionTest/action2");
		CsvReader.getTestCasesFromCsvFile();		
	}
	
	@Test(description = "verify wrong csv test folder throws assert error", expectedExceptions = { AssertionError.class })
	public void getTestCasesFromCsvFile_wrong_test_case_folder() throws Exception {
		Config.putValue(TestDataProvider.TEST_DATA_PARALLEL_PATH,  "../apiTestData/testCases/frameworkTests/actionTest2/");
		Config.putValue(CsvReader.SERVICE_INCLUDE_LIST, "TestCases_UserValidationSingleAction.csv");
		Config.putValue(TestDataProvider.TEST_DATA_ACTION_PATH, "../apiTestData/testCases/frameworkTests/actionTest/action");
		CsvReader.getTestCasesFromCsvFile();		
	}
	
	@Test(description = "csv file with no action tests with empty action folder")
	public void getTestCasesFromCsvFile_no_action_invalid_wrong_folder() throws Exception {
		Config.putValue(TestDataProvider.TEST_DATA_PARALLEL_PATH,  "../apiTestData/testCases/frameworkTests/actionTest/");
		Config.putValue(CsvReader.SERVICE_INCLUDE_LIST, "TestCases_UserValidationNoAction.csv");
		Config.putValue(TestDataProvider.TEST_DATA_ACTION_PATH, "../apiTestData/testCases/frameworkTests/actionTest/action2");
		List<Object> list = CsvReader.getTestCasesFromCsvFile();	
		Helper.assertEquals(2, list.size());
	}
	
	@Test(description = "csv file multiple runs set for different tests")
	public void getTestCasesFromCsvFile_csv_multirun() throws Exception {
		Config.putValue(TestDataProvider.TEST_DATA_PARALLEL_PATH,  "../apiTestData/testCases/frameworkTests/multirun/");
		Config.putValue(CsvReader.SERVICE_INCLUDE_LIST, "TestCases_UserValidation_multirun.csv");

		List<Object> list = CsvReader.getTestCasesFromCsvFile();	
		Helper.assertEquals(12, list.size());
	}
	
	@Test(description = "csv file with tests combined using test steps postfix")
	public void getTestCasesFromCsvFile_csv_test_steps() throws Exception {
		Config.putValue(TestDataProvider.TEST_DATA_PARALLEL_PATH,  "../apiTestData/testCases/frameworkTests/testStep/");
		Config.putValue(CsvReader.SERVICE_INCLUDE_LIST, "TestCases_UserValidation_step.csv");

		List<Object> list = CsvReader.getTestCasesFromCsvFile();	
		Helper.assertEquals(4, list.size());
	}
	
	@Test(description = "csv file escape characters and quotes")
	public void getTestCasesFromCsvFile_csv_escape_character() throws Exception {
		Config.putValue(TestDataProvider.TEST_DATA_PARALLEL_PATH,  "../apiTestData/testCases/frameworkTests/escapeChar/");
		Config.putValue(CsvReader.SERVICE_INCLUDE_LIST, "TestCases_escapeChar.csv");

		List<Object> list = CsvReader.getTestCasesFromCsvFile();	
		Helper.assertEquals(1, list.size());
		Object[] values = (Object[]) list.get(0);
		Helper.assertTrue("list doesnt include escape chars: " + Arrays.toString(values), Arrays.toString(values).contains("\\\""));
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
	
	@Test(description = "csv file range include")
	public void getTestCasesFromCsvFile_test_range_include() throws Exception {
		Config.putValue(TestDataProvider.TEST_DATA_PARALLEL_PATH,  "../apiTestData/testCases/frameworkTests/testRange/singleFile/");
		
		TestLog.When("I include TestCases_UserValidation.csv");
		Config.putValue(CsvReader.SERVICE_INCLUDE_LIST, "TestCases_UserValidation.csv");
		Config.putValue(CsvReader.SERVICE_EXCLUDE_LIST, "");
		List<Object> list = CsvReader.getTestCasesFromCsvFile();	
		Helper.assertEquals(6, list.size());
	}
	
	@Test(description = "csv file range include test")
	public void getTestCasesFromCsvFile_test_range_include_tests() throws Exception {
		Config.putValue(TestDataProvider.TEST_DATA_PARALLEL_PATH,  "../apiTestData/testCases/frameworkTests/testRange/singleFile/");
		
		TestLog.When("I include TestCases_UserValidation.csv:createUser-createUserNoToken, createUserInvalidToken;");
		Config.putValue(CsvReader.SERVICE_INCLUDE_LIST, "TestCases_UserValidation.csv:createUser-createUserNoToken, createUserInvalidToken;");
		Config.putValue(CsvReader.SERVICE_EXCLUDE_LIST, "");
		List<Object> list = CsvReader.getTestCasesFromCsvFile();	
		Helper.assertEquals(3, list.size());
	}
	
	@Test(description = "csv file range include test")
	public void getTestCasesFromCsvFile_test_range_include_multiple_range() throws Exception {
		Config.putValue(TestDataProvider.TEST_DATA_PARALLEL_PATH,  "../apiTestData/testCases/frameworkTests/testRange/singleFile/");
		
		TestLog.When("I include TestCases_UserValidation.csv:getAdminToken-createUserNoToken, updateUser-deleteUser;");
		Config.putValue(CsvReader.SERVICE_INCLUDE_LIST, "TestCases_UserValidation.csv:getAdminToken-createUserNoToken, updateUser-deleteUser;");
		Config.putValue(CsvReader.SERVICE_EXCLUDE_LIST, "");
		List<Object> list = CsvReader.getTestCasesFromCsvFile();	
		Helper.assertEquals(5, list.size());
	}
	
	@Test(description = "csv file range include test")
	public void getTestCasesFromCsvFile_test_range_include_singleTest() throws Exception {
		Config.putValue(TestDataProvider.TEST_DATA_PARALLEL_PATH,  "../apiTestData/testCases/frameworkTests/testRange/singleFile/");
		
		TestLog.When("I include TestCases_UserValidation.csv:createUserInvalidToken;");
		Config.putValue(CsvReader.SERVICE_INCLUDE_LIST, "TestCases_UserValidation.csv: createUserInvalidToken;");
		Config.putValue(CsvReader.SERVICE_EXCLUDE_LIST, "");
		List<Object> list = CsvReader.getTestCasesFromCsvFile();	
		Helper.assertEquals(1, list.size());
	}
	
	@Test(description = "csv file range include test")
	public void getTestCasesFromCsvFile_test_range_include_two_Test() throws Exception {
		Config.putValue(TestDataProvider.TEST_DATA_PARALLEL_PATH,  "../apiTestData/testCases/frameworkTests/testRange/singleFile/");
		
		TestLog.When("I include TestCases_UserValidation.csv:createUserNoToken,createUserInvalidToken;");
		Config.putValue(CsvReader.SERVICE_INCLUDE_LIST, "TestCases_UserValidation.csv: createUserNoToken,createUserInvalidToken;");
		Config.putValue(CsvReader.SERVICE_EXCLUDE_LIST, "");
		List<Object> list = CsvReader.getTestCasesFromCsvFile();	
		Helper.assertEquals(2, list.size());
	}
	
	@Test(description = "csv file range exclude test")
	public void getTestCasesFromCsvFile_test_range_exclude_test() throws Exception {
		Config.putValue(TestDataProvider.TEST_DATA_PARALLEL_PATH,  "../apiTestData/testCases/frameworkTests/testRange/singleFile/");
		
		TestLog.When("I include TestCases_UserValidation.csv");
		Config.putValue(CsvReader.SERVICE_INCLUDE_LIST, "");
		Config.putValue(CsvReader.SERVICE_EXCLUDE_LIST, "TestCases_UserValidation.csv");
		
		try {
			CsvReader.getTestCasesFromCsvFile();	
		}catch(AssertionError e) {
			Helper.assertTrue("tests available after fitlering", e.getMessage().contains("no tests available after fitlering"));
		}
	}
	
	@Test(description = "csv file range exclude test")
	public void getTestCasesFromCsvFile_test_range_exclude_tests() throws Exception {
		Config.putValue(TestDataProvider.TEST_DATA_PARALLEL_PATH,  "../apiTestData/testCases/frameworkTests/testRange/singleFile/");
		
		TestLog.When("I include TestCases_UserValidation.csv:createUser-createUserNoToken, createUserInvalidToken;");
		Config.putValue(CsvReader.SERVICE_INCLUDE_LIST, "");
		Config.putValue(CsvReader.SERVICE_EXCLUDE_LIST, "TestCases_UserValidation.csv:createUser-createUserNoToken, createUserInvalidToken;");
		List<Object> list = CsvReader.getTestCasesFromCsvFile();	
		Helper.assertEquals(3, list.size());
	}
	
	@Test(description = "csv file range exclude test multiple")
	public void getTestCasesFromCsvFile_test_range_exclude_multiple_range() throws Exception {
		Config.putValue(TestDataProvider.TEST_DATA_PARALLEL_PATH,  "../apiTestData/testCases/frameworkTests/testRange/singleFile/");
		
		TestLog.When("I include TestCases_UserValidation.csv:getAdminToken-createUserNoToken, updateUser-deleteUser;");
		Config.putValue(CsvReader.SERVICE_INCLUDE_LIST, "");
		Config.putValue(CsvReader.SERVICE_EXCLUDE_LIST, "TestCases_UserValidation.csv:getAdminToken-createUserNoToken, updateUser-deleteUser;");
		List<Object> list = CsvReader.getTestCasesFromCsvFile();	
		Helper.assertEquals(1, list.size());
	}
	
	@Test(description = "csv file range exclude test single")
	public void getTestCasesFromCsvFile_test_range_exclude_single_test() throws Exception {
		Config.putValue(TestDataProvider.TEST_DATA_PARALLEL_PATH,  "../apiTestData/testCases/frameworkTests/testRange/singleFile/");
		
		TestLog.When("I include TestCases_UserValidation.csv:createUserInvalidToken;");
		Config.putValue(CsvReader.SERVICE_INCLUDE_LIST, "");
		Config.putValue(CsvReader.SERVICE_EXCLUDE_LIST, "TestCases_UserValidation.csv:createUserInvalidToken");
		List<Object> list = CsvReader.getTestCasesFromCsvFile();	
		Helper.assertEquals(5, list.size());
	}
	
	@Test(description = "csv file range exclude test single")
	public void getTestCasesFromCsvFile_test_range_exclude_two_test() throws Exception {
		Config.putValue(TestDataProvider.TEST_DATA_PARALLEL_PATH,  "../apiTestData/testCases/frameworkTests/testRange/singleFile/");
		
		TestLog.When("I include TestCases_UserValidation.csv:createUserNoToken,createUserInvalidToken;");
		Config.putValue(CsvReader.SERVICE_INCLUDE_LIST, "");
		Config.putValue(CsvReader.SERVICE_EXCLUDE_LIST, "TestCases_UserValidation.csv:createUserNoToken,createUserInvalidToken;");
		List<Object> list = CsvReader.getTestCasesFromCsvFile();	
		Helper.assertEquals(4, list.size());
	}
	
	@Test(description = "csv file range include and exclude")
	public void getTestCasesFromCsvFile_test_range_include_exclude() throws Exception {
		Config.putValue(TestDataProvider.TEST_DATA_PARALLEL_PATH,  "../apiTestData/testCases/frameworkTests/testRange/singleFile/");
		
		TestLog.When("I include TestCases_UserValidation.csv");
		Config.putValue(CsvReader.SERVICE_INCLUDE_LIST, "TestCases_UserValidation.csv:getAdminToken-updateUser");
		Config.putValue(CsvReader.SERVICE_EXCLUDE_LIST, "TestCases_UserValidation.csv:createUser");
		List<Object> list = CsvReader.getTestCasesFromCsvFile();	
		Helper.assertEquals(4, list.size());
	}
	
	@Test()
	public void setTestRange_include() throws Exception {
		Config.putValue(TestDataProvider.TEST_DATA_PARALLEL_PATH,  "../apiTestData/testCases/frameworkTests/testRange/");
		
		TestLog.When("I include TestCases_UserValidation.csv");
		Config.putValue(CsvReader.SERVICE_INCLUDE_LIST, "TestCases_UserValidation.csv:getAdminToken-updateUser");
		Config.putValue(CsvReader.SERVICE_EXCLUDE_LIST, "");
		ArrayList<File> files = CsvReader.filterTests();	
		Helper.assertEquals(1, files.size());
		Helper.assertEquals("TestCases_UserValidation.csv", files.get(0).getName());
	}
	
	@Test()
	public void setTestRange_include_multiple_filter() throws Exception {
		Config.putValue(TestDataProvider.TEST_DATA_PARALLEL_PATH,  "../apiTestData/testCases/frameworkTests/testRange/");
		
		TestLog.When("I include TestCases_UserValidation.csv");
		Config.putValue(CsvReader.SERVICE_INCLUDE_LIST, "TestCases_UserValidation.csv;TestCases_UserValidation2.csv;TestCases_UserValidation3.csv");
		Config.putValue(CsvReader.SERVICE_EXCLUDE_LIST, "");
		ArrayList<File> files = CsvReader.filterTests();	
		Helper.assertEquals(3, files.size());
		Helper.assertEquals("TestCases_UserValidation.csv", files.get(0).getName());
		Helper.assertEquals("TestCases_UserValidation2.csv", files.get(1).getName());
		Helper.assertEquals("TestCases_UserValidation3.csv", files.get(2).getName());
	}
	
	@Test()
	public void setTestRange_exclude() throws Exception {
		Config.putValue(TestDataProvider.TEST_DATA_PARALLEL_PATH,  "../apiTestData/testCases/frameworkTests/testRange/");
		
		TestLog.When("I include TestCases_UserValidation.csv");
		Config.putValue(CsvReader.SERVICE_INCLUDE_LIST, "");
		Config.putValue(CsvReader.SERVICE_EXCLUDE_LIST, "TestCases_UserValidation.csv");
		ArrayList<File> files = CsvReader.filterTests();	
		Helper.assertEquals(4, files.size());
	}
	
	@Test()
	public void setTestRange_exclude_testcase() throws Exception {
		Config.putValue(TestDataProvider.TEST_DATA_PARALLEL_PATH,  "../apiTestData/testCases/frameworkTests/testRange/");
		
		TestLog.When("I include TestCases_UserValidation.csv");
		Config.putValue(CsvReader.SERVICE_INCLUDE_LIST, "");
		Config.putValue(CsvReader.SERVICE_EXCLUDE_LIST, "TestCases_UserValidation.csv:getAdminToken-updateUser");
		ArrayList<File> files = CsvReader.filterTests();	
		Helper.assertEquals(5, files.size());
	}
	
	@Test()
	public void setTestRange_exclude_multiple_files() throws Exception {
		Config.putValue(TestDataProvider.TEST_DATA_PARALLEL_PATH,  "../apiTestData/testCases/frameworkTests/testRange/");
		
		TestLog.When("I include TestCases_UserValidation.csv");
		Config.putValue(CsvReader.SERVICE_INCLUDE_LIST, "");
		Config.putValue(CsvReader.SERVICE_EXCLUDE_LIST, "TestCases_UserValidation.csv;TestCases_UserValidation2.csv;TestCases_UserValidation3.csv");
		ArrayList<File> files = CsvReader.filterTests();	
		Helper.assertEquals(2, files.size());
	}
	
	@Test()
	public void setTestRange_exclude_two_files() throws Exception {
		Config.putValue(TestDataProvider.TEST_DATA_PARALLEL_PATH,  "../apiTestData/testCases/frameworkTests/testRange/");
		
		TestLog.When("I include TestCases_UserValidation.csv");
		Config.putValue(CsvReader.SERVICE_INCLUDE_LIST, "");
		Config.putValue(CsvReader.SERVICE_EXCLUDE_LIST, "TestCases_UserValidation.csv:getAdminToken-updateUser;TestCases_UserValidation2.csv");
		ArrayList<File> files = CsvReader.filterTests();	
		Helper.assertEquals(4, files.size());
	}
	
	@Test()
	public void setTestRange_include_exclude() throws Exception {
		Config.putValue(TestDataProvider.TEST_DATA_PARALLEL_PATH,  "../apiTestData/testCases/frameworkTests/testRange/");
		
		TestLog.When("I include TestCases_UserValidation.csv");
		Config.putValue(CsvReader.SERVICE_INCLUDE_LIST, "TestCases_UserValidation.csv; TestCases_UserValidation2.csv; TestCases_UserValidation3.csv");
		Config.putValue(CsvReader.SERVICE_EXCLUDE_LIST, "TestCases_UserValidation.csv:getAdminToken-updateUser;TestCases_UserValidation2.csv");
		ArrayList<File> files = CsvReader.filterTests();	
		Helper.assertEquals(2, files.size());
	}
	
	@Test()
	public void setTestRange_include_exclude_all() throws Exception {
		Config.putValue(TestDataProvider.TEST_DATA_PARALLEL_PATH,  "../apiTestData/testCases/frameworkTests/testRange/");
		
		TestLog.When("I include TestCases_UserValidation.csv");
		Config.putValue(CsvReader.SERVICE_INCLUDE_LIST, "TestCases_UserValidation.csv; TestCases_UserValidation2.csv; TestCases_UserValidation3.csv");
		Config.putValue(CsvReader.SERVICE_EXCLUDE_LIST, "TestCases_UserValidation.csv; TestCases_UserValidation2.csv; TestCases_UserValidation3.csv");
		ArrayList<File> files = CsvReader.filterTests();	
		Helper.assertEquals(0, files.size());
	}
	
}