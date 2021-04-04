package app.views;

import java.util.List;

import formatter.models.Entity;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;

public interface MainStageListener {
	
	public List<Entity> getTableEntities();

	public List<Entity> getSelectedEntities();
	
	public void clearTable();
	
	public void clearTableSelection();
	
	public void addEntitiesToTable(List<Entity> entities);
		
	public void enableCreateChildButton(boolean enable);
	
	public void enableUpdateButton(boolean enable);
	
	public void enableDeleteButton(boolean enable);
	
	public void enableDeleteMultipleButton(boolean enable);

	public List<TableColumn<Entity, ?>> getTableColumns();
	
	public void setSortRadioButtons(List<RadioButton> radioButtons);
	
	public void populateColumns(List<TableColumn<Entity, ?>> columns);
	
	public void sortTableByColumn(TableColumn<Entity, ?> column);
	
	public String getActiveSortColumnName();
	
	public String getIdSearchText();
	
	public String getNameEqualsSearchText();
	
	public String getNameStartsWithSearchText();
	
	public String getNameEndsWithSearchText();
	
	public String getContainsAttributeKeySearchText();
	
	public String getContainsAttributeValueSearchText();
	
	public String getContainsChildKeyWithAttributeValuePartOneSearchText();
	
	public String getContainsChildKeyWithAttributeValuePartTwoSearchText();
	
	public String getContainsChildKeyWithAttributeValuePartThreeSearchText();
}
