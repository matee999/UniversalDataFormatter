package formatter.manager;

import formatter.DataFormatter;

/**
* Universal data formatter manager registers and returns
* active implementation of the {@link formatter.DataFormatter}
*/
public class UDFManager {

	/**
	 * Active registered {@link formatter.DataFormatter}
	 */
	private static DataFormatter formatter;
	
	/**
	 * <p>Registers given {@link formatter.DataFormatter}</p>
	 * @param dataFormatter formatter implementation
	 */
	public static void registerFormatter(DataFormatter dataFormatter) {
		formatter = dataFormatter;
	}
	
	/**
	 * <p>Provides active {@link formatter.DataFormatter}</p>
	 * @return dataFormatter formatter implementation
	 */
	public static DataFormatter getFormatter() {
		return formatter;
	}
}
