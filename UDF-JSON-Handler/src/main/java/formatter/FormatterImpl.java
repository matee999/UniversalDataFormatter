package formatter;

import java.io.File;
import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import formatter.manager.UDFManager;
import formatter.models.Entity;

public class FormatterImpl extends DataFormatter {
	
	static {
		UDFManager.registerFormatter(new FormatterImpl());
	}

	@Override
	public void save(List<Entity> entities, File file) throws Exception {
		new ObjectMapper().writeValue(file, entities);
	}

	@Override
	public List<Entity> read(File file) throws Exception {
		List<Entity> entities = new ObjectMapper().readValue(file, new TypeReference<List<Entity>>() {});

		return entities;
	}
	
	@Override
	protected String getDataFormatName() {
		return "JSON";
	}
	
	@Override
	protected String getDataFormatExtension() {
		return ".json";
	}
}
