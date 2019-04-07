package test.module;

import core.uiCore.drivers.AbstractDriverTestNG;
import moduleManager.ModuleManager;

public class TestBase extends AbstractDriverTestNG {
	protected ModuleManager app = new ModuleManager();
}