package formatter;

import java.io.File;
import java.io.FileWriter;
import java.util.Scanner;

/**
 * Configuration for {@link formatter.DataFormatter}
 */
class UDFConfigurator {

	private String storagePath = System.getProperty("user.dir") + "/src/main/resources";

	private DataFormatter formatter;

	private boolean autoIncrementIds;
	private int maxEntitiesPerFile = 5;

	public UDFConfigurator(DataFormatter formatter) {
		this.formatter = formatter;
		
		setupData();
	}
	
	private void setupData() {
		File propertiesFile = new File(getStoragePath(), "application.properties");
		if (!propertiesFile.exists())
			setAutoIncrementIds(true);
		else {
			autoIncrementIds = isAutoIncrementIds();
			maxEntitiesPerFile = getEntityLimitPerFile();
		}
	}

	/**
	 * <p>
	 * Returns storage location of stored {@link formatter.models.Entity}s
	 * </p>
	 * 
	 * @return storagePath storage location
	 */
	public String getStoragePath() {
		return storagePath;
	}

	/**
	 * <p>
	 * Sets storage location of stored {@link formatter.models.Entity}s
	 * </p>
	 * 
	 * @param storagePath storage location
	 * @throws Exception illegal storage location
	 */
	public void setStoragePath(String storagePath) throws Exception {
		File storageFolder = new File(storagePath);
		if (!storageFolder.exists())
			throw new Exception();

		boolean illegalStorage = false;
		for (File file : storageFolder.listFiles()) {
			if (file.getName().equals(".DS_Store"))
				continue;
			
			String extension = file.getName().substring(file.getName().lastIndexOf('.') + 1);
			if (!extension.equals(formatter.getDataFormatExtension().substring(1))
					&& !extension.equals("properties"))
				illegalStorage = true;
		}
		if (illegalStorage)
			throw new Exception();

		this.storagePath = storagePath;
		setupData();
	}

	/**
	 * <p>
	 * Returns all {@link formatter.models.Entity}s from storage
	 * </p>
	 * 
	 * @return entities filtered entities from storage
	 */
	public int getEntityLimitPerFile() {
		File propertiesFile = new File(getStoragePath(), "application.properties");
		try {
			Scanner scanner = new Scanner(propertiesFile);
			scanner.nextLine();
			String value = scanner.nextLine().split(": ")[1];
			maxEntitiesPerFile = Integer.parseInt(value);
			scanner.close();

			return maxEntitiesPerFile;
		} catch (Exception e) {
			e.printStackTrace();
			return maxEntitiesPerFile;
		}
	}

	/**
	 * <p>
	 * Sets the maximum number of {@link formatter.models.Entity}s per file
	 * </p>
	 * 
	 * @param entityLimitPerFile maximum number of entities per file
	 */
	public void setEntityLimitPerFile(int entityLimitPerFile) {
		File propertiesFile = new File(getStoragePath(), "application.properties");
		try {
			FileWriter myWriter = new FileWriter(propertiesFile);
			StringBuilder sb = new StringBuilder();
			sb.append("auto-increment-ids: " + autoIncrementIds + "\n");
			sb.append("max-entities-per-file: " + entityLimitPerFile);
			myWriter.write(sb.toString());
			myWriter.close();

			maxEntitiesPerFile = entityLimitPerFile;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * <p>
	 * Returns whether unique identifiers get automatically generated or must be
	 * provided along with {@link formatter.models.Entity} details
	 * </p>
	 * 
	 * @return autoIncrementIds is auto-increment enabled
	 */
	public boolean isAutoIncrementIds() {
		File propertiesFile = new File(getStoragePath(), "application.properties");
		try {
			Scanner scanner = new Scanner(propertiesFile);
			boolean enabled = scanner.nextLine().split(": ")[1].equals("true");
			autoIncrementIds = enabled;
			scanner.close();

			return enabled;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * <p>
	 * Sets whether unique identifiers get automatically generated or must be
	 * provided along with {@link formatter.models.Entity} details
	 * </p>
	 * 
	 * @return autoIncrementIds is auto-increment enabled
	 */
	public void setAutoIncrementIds(boolean autoIncrementIds) {
		File propertiesFile = new File(getStoragePath(), "application.properties");
		try {
			FileWriter myWriter = new FileWriter(propertiesFile);
			StringBuilder sb = new StringBuilder();
			sb.append("auto-increment-ids: " + autoIncrementIds + "\n");
			sb.append("max-entities-per-file: " + maxEntitiesPerFile);
			myWriter.write(sb.toString());
			myWriter.close();
			this.autoIncrementIds = autoIncrementIds;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
