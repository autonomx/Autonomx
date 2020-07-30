package test.module.framework.tests.functional;


import java.io.File;
import java.lang.reflect.Method;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import core.helpers.Helper;
import core.support.configReader.PropertiesReader;
import test.module.framework.TestBase;

public class PropertiesReaderTest extends TestBase {
	
	@BeforeClass
	public void beforeClass()  {
	}

	@BeforeMethod()
	public void beforeMethod(Method method) {
	}
	
	@Test
	public void getPropertyPathTest() {
	
		// root path
		String path = "./resources/properties/";
		String fullPath = PropertiesReader.getPropertyPath(path);
		path = path.replace("\\", File.separator).replace("//", File.separator);
		Helper.assertEquals(Helper.getRootDir() + path, fullPath);
		
		// qa path
		path = "./resources/properties/environment/qa";
		fullPath = PropertiesReader.getPropertyPath(path);
		path = path.replace("\\", File.separator).replace("//", File.separator);
		Helper.assertEquals(Helper.getRootDir() + path + ".property", fullPath);
		
		path = "resources\\\\properties\\\\";
		fullPath = PropertiesReader.getPropertyPath(path);
		path = path.replace("\\", File.separator).replace("//", File.separator);
		Helper.assertEquals(Helper.getRootDir() + path, fullPath);
		
		path = "resources////properties////";
		fullPath = PropertiesReader.getPropertyPath(path);
		path = path.replace("\\", File.separator).replace("//", File.separator);
		Helper.assertEquals(Helper.getRootDir() + path, fullPath);
	}
}