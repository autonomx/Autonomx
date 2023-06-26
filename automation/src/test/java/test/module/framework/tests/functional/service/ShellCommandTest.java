package test.module.framework.tests.functional.service;

import java.lang.reflect.Method;
import java.util.concurrent.atomic.AtomicInteger;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import core.apiCore.TestDataProvider;
import test.module.framework.TestBase;

// these include actions tests. Action tests are added to existing tests when invoked in csv file
public class ShellCommandTest extends TestBase {
	
	
	@BeforeMethod()
	public void beforeMethod(Method method) {
		TestDataProvider.csvFileIndex = new AtomicInteger(0);
	}
	
	// dependency is not updated anymore
	@Test()
	public void shellCommandTest() throws Exception {
//		String shellResults = Helper.runShellCommand("mvn -version","1");
//		System.out.println("maven -version shell results: " + shellResults);
		//Helper.assertTrue("shell command is empty " +  shellResults,shellResults.isEmpty());	
	}
}