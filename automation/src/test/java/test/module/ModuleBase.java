package test.module;

import core.uiCore.drivers.AbstractDriverTestNG;
import moduleManager.ModuleManager;

public class ModuleBase extends AbstractDriverTestNG {
	protected ModuleManager app = new ModuleManager();
}