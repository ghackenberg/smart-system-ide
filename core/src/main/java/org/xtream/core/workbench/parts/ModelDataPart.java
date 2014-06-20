package org.xtream.core.workbench.parts;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ScrollPaneConstants;
import javax.swing.table.TableCellRenderer;

import org.jdesktop.swingx.JXTreeTable;
import org.jdesktop.swingx.treetable.DefaultTreeTableModel;
import org.xtream.core.model.Component;
import org.xtream.core.workbench.Event;
import org.xtream.core.workbench.Part;
import org.xtream.core.workbench.events.JumpEvent;
import org.xtream.core.workbench.models.nodes.ComponentTreeTableNode;

public class ModelDataPart<T extends Component> extends Part<T>
{
	
	private JXTreeTable table;
	
	public ModelDataPart()
	{
		this(0, 0);
	}
	public ModelDataPart(int x, int y)
	{
		this(x, y, 1, 1);
	}
	public ModelDataPart(int x, int y, int width, int height)
	{
		super("Model data", x, y, width, height);
		
		table = new JXTreeTable();
		
		JScrollPane scroll = new JScrollPane(table, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		
		getPanel().add(scroll);
	}

	@Override
	public void handle(Event<T> event)
	{
		if (event instanceof JumpEvent)
		{
			JumpEvent<T> jump = (JumpEvent<T>) event;
			
			// Initialize table
			
			ComponentTreeTableNode node = new ComponentTreeTableNode(null, getRoot(), getState(), jump.getTimepoint());
			
			DefaultTreeTableModel model = new DefaultTreeTableModel(node);
			
			table.setTreeTableModel(model);
			table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
			
			table.getColumn(0).setPreferredWidth(200);
			table.getColumn(0).setHeaderValue("Tree");
			
			for (int i = 0; i < jump.getTimepoint(); i++)
			{
				table.getColumn(i + 1).setPreferredWidth(100);
				table.getColumn(i + 1).setHeaderValue("" + i);
			}
			
			table.expandAll();
			
			for (int column = 0; column < table.getColumnCount(); column++)
			{
				int width = 0;
				
				for (int row = 0; row < table.getRowCount(); row++)
				{
					TableCellRenderer renderer = table.getCellRenderer(row, column);
					
					width = Math.max(table.prepareRenderer(renderer, row, column).getPreferredSize().width, width);
				}
				
				table.getColumn(column).setPreferredWidth(width);
			}
		}
	}

}
