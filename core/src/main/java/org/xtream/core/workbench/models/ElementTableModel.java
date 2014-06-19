package org.xtream.core.workbench.models;

import javax.swing.ImageIcon;
import javax.swing.table.AbstractTableModel;

import org.xtream.core.model.Element;

public class ElementTableModel extends AbstractTableModel
{
	
	private static final long serialVersionUID = -848924145662568840L;
	
	private Element element;
	
	public ElementTableModel(Element element)
	{
		this.element = element;
	}

	@Override
	public int getColumnCount()
	{
		return 3;
	}

	@Override
	public int getRowCount()
	{
		return element.getChildren().size();
	}

	@Override
	public Object getValueAt(int row, int column)
	{
		Element child = element.getChildren().get(row);
		
		if (column == 0)
		{
			return new ImageIcon(child.getIcon());
		}
		if (column == 1)
		{
			return child.getName();
		}
		if (column == 2)
		{
			return child.getDescription();
		}
		
		throw new IllegalStateException();
	}
	
	@Override
	public String getColumnName(int column)
	{
		if (column == 0)
		{
			return "Type";
		}
		if (column == 1)
		{
			return "Name";
		}
		if (column == 2)
		{
			return "Description";
		}
		
		throw new IllegalStateException();
	}
	
	@Override
	public Class<?> getColumnClass(int column)
	{
		if (column == 0)
		{
			return ImageIcon.class;
		}
		if (column == 1)
		{
			return String.class;
		}
		if (column == 2)
		{
			return String.class;
		}
		
		throw new IllegalStateException();
	}

}
