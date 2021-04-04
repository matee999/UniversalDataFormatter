package formatter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import formatter.manager.UDFManager;
import formatter.models.Entity;

public class FormatterImpl extends DataFormatter {

	static {
		UDFManager.registerFormatter(new FormatterImpl());
	}

	@Override
	public void save(List<Entity> entities, File file) throws Exception {
		StringBuilder sb = new StringBuilder();
		for (Entity ent : entities) {
			sb.append("----------\n");
			sb.append("#id: " + ent.getId() + "\n");
			sb.append("#name: " + ent.getName() + "\n");
			sb.append("@attributes:");
			if (ent.getAttributes() == null) {
				sb.append(" null\n");
			} else {
				sb.append("\n");
				for (Map.Entry<String, Object> at : ent.getAttributes().entrySet()) {
					sb.append("\t#" + at.getKey() + ": " + at.getValue() + "\n");
				}
			}
			sb.append("@children:");

			Map<String, Entity> mapa = new HashMap<String, Entity>();
			mapa = ent.getChildren();
			if (mapa == null) {
				sb.append(" null\n");
			} else {
				sb.append("\n");
				for (HashMap.Entry<String, Entity> ch : mapa.entrySet()) {
					sb.append("\t-" + ch.getKey() + ":\n\t");
					Entity entVal = ch.getValue();
					sb.append("\t#id: " + entVal.getId() + "\n");
					sb.append("\t\t#name: " + entVal.getName() + "\n");
					sb.append("\t\t@attributes:");
					if (entVal.getAttributes() == null) {
						sb.append(" null\n");
					} else {
						sb.append("\n");
						for (Map.Entry<String, Object> entValAt : entVal.getAttributes().entrySet()) {
							sb.append("\t\t\t#" + entValAt.getKey() + ": " + entValAt.getValue() + "\n");
						}
					}
					sb.append("\t\t@children: null\n");
				}
			}
		}
		sb.append("----------\n");
		try {
			FileWriter myWriter = new FileWriter(file);
			myWriter.write(sb.toString());
			myWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public List<Entity> read(File file) throws Exception {
		List<Entity> entities = new ArrayList<Entity>();
		try {
			Scanner scanner = new Scanner(file);
			Entity ent = null;
			String key = null;
			Entity entForChild = null;
			Map<String, Entity> children = new HashMap<String, Entity>();
			Map<String, Object> attributes = new HashMap<String, Object>();
			Map<String, Object> attributesForChild = new HashMap<String, Object>();

			while (scanner.hasNextLine()) {
				String data = scanner.nextLine();
				if (data.contains("----------")) {
					if (ent != null) {
						if (attributes.isEmpty()) {
							ent.setAttributes(null);
						} else
							ent.setAttributes(attributes);
						if (children.isEmpty()) {
							ent.setChildren(null);
						} else
							ent.setChildren(children);
						entities.add(ent);
					}
					children = new HashMap<String, Entity>();
					attributes = new HashMap<String, Object>();
					attributesForChild = new HashMap<String, Object>();
					ent = new Entity();
					continue;
				}
				if (data.startsWith("\t\t#id: ")) {
					String cid = data.substring(7);
					entForChild.setId(Integer.parseInt(cid));
					continue;
				}
				if (data.contains("#id: ")) {
					int id = Integer.parseInt(data.substring(5));
					ent.setId(id);
					continue;
				}
				if (data.startsWith("\t\t#name: ")) {
					String cname = data.substring(9);
					entForChild.setName(cname);
					continue;
				}
				if (data.startsWith("\t\t@attributes:")) {
					continue;
				}
				if (data.startsWith("\t\t\t#")) {
					String[] att = data.substring(4).split(":");
					att[1] = att[1].substring(1);
					if (att[1].equals("null"))
						attributesForChild.put(att[0], null);
					else {
						try {
							int at = Integer.parseInt(att[1]);
							attributesForChild.put(att[0], at);
						} catch (Exception e) {
							try {
								boolean at = true;
								if (att[1].equals("false"))
									at = false;
								else if (att[1].equals("true"))
									at = true;
								else
									throw new Exception();
								attributesForChild.put(att[0], at);
							} catch (Exception e2) {
								try {
									double at = Double.parseDouble(att[1]);
									attributesForChild.put(att[0], at);
								} catch (Exception e3) {
									attributesForChild.put(att[0], att[1]);
								}
							}
						}
					}
					continue;
				}
				if (data.startsWith("\t\t@children: null")) {
					if (attributesForChild.isEmpty())
						entForChild.setAttributes(null);
					else
						entForChild.setAttributes(attributesForChild);
					entForChild.setChildren(null);
					children.put(key, entForChild);
					continue;
				}
				if (data.contains("@attributes:")) {
					continue;
				}
				if (data.startsWith("\t#")) {
					String[] att = data.substring(2).split(":");
					att[1] = att[1].substring(1);
					if (att[1].equals("null"))
						attributes.put(att[0], null);
					else {
						try {
							int at = Integer.parseInt(att[1]);
							attributes.put(att[0], at);
						} catch (Exception e) {
							try {
								boolean at = true;
								if (att[1].equals("false"))
									at = false;
								else if (att[1].equals("true"))
									at = true;
								else
									throw new Exception();
								attributes.put(att[0], at);
							} catch (Exception e2) {
								try {
									double at = Double.parseDouble(att[1]);
									attributes.put(att[0], at);
								} catch (Exception e3) {
									attributes.put(att[0], att[1]);
								}
							}
						}
					}
					continue;
				}
				if (data.contains("#name: ")) {
					String name = data.substring(7);
					ent.setName(name);
					continue;
				}
				if (data.startsWith("@children:")) {
					if (!(data.substring(10).equals("null"))) {
						entForChild = new Entity();
					}
					continue;
				}
				if (data.startsWith("\t-")) {
					key = data.substring(2);
					key = key.substring(0, key.length() - 1);
					entForChild = new Entity();
					attributesForChild = new HashMap<String, Object>();
					continue;
				}
			}
			scanner.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return entities;
	}
	
	@Override
	protected String getDataFormatName() {
		return "CUSTOM";
	}

	@Override
	protected String getDataFormatExtension() {
		return ".custom";
	}
}
