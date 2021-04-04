package app.controllers;

import formatter.DataFormatter;
import formatter.manager.UDFManager;

public class SetupStageController {

	public DataFormatter formatter;
	
	public SetupStageController() {
		this.formatter = UDFManager.getFormatter();
	}
	
	public String getStoragePath() {
		return formatter.getStoragePath();
	}
	
	public void setStoragePath(String storagePath) throws Exception {
		formatter.setStoragePath(storagePath);
	}
	
	public boolean isAutoIncrementEnabled() {
		return formatter.isAutoIncrementEnabled();
	}
	
	public void setAutoIncrementEnabled(boolean enabled) {
		formatter.setAutoIncrementEnabled(enabled);
	}
	
	public int getEntityLimitPerFile() {
		return formatter.getEntityLimitPerFile();
	}

	public void setEntityLimitPerFile(int entityLimitPerFile) {
		formatter.setEntityLimitPerFile(entityLimitPerFile);
	}
}
