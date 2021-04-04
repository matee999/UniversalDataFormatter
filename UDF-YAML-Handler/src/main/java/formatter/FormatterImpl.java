package formatter;

import java.io.File;
import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator.Feature;

import formatter.manager.UDFManager;
import formatter.models.Entity;

public class FormatterImpl extends DataFormatter {

	static {
		UDFManager.registerFormatter(new FormatterImpl());
	}

	@Override
	public void save(List<Entity> entities, File file) throws Exception {
		ObjectMapper mapper = new ObjectMapper(new YAMLFactory().disable(Feature.WRITE_DOC_START_MARKER));
		mapper.writeValue(file, entities);
	}

	@Override
	public List<Entity> read(File file) throws Exception {
		ObjectMapper objectMapper = new ObjectMapper(new YAMLFactory());
		List<Entity> entities = objectMapper.readValue(file, new TypeReference<List<Entity>>() {});

		return entities;
	}
	
	@Override
	protected String getDataFormatName() {
		return "YAML";
	}

	@Override
	protected String getDataFormatExtension() {
		return ".yaml";
	}
}
