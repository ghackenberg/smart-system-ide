package org.xtream.core.optimizer.printers;

import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.ScrollPaneConstants;
import javax.swing.table.TableCellRenderer;

import org.jdesktop.swingx.JXTreeTable;
import org.jdesktop.swingx.treetable.DefaultTreeTableModel;
import org.xtream.core.model.Component;
import org.xtream.core.optimizer.Printer;
import org.xtream.core.optimizer.printers.table.ComponentNode;

public class TablePrinter<T extends Component> extends Printer<T>
{
	
	private JTabbedPane tabs;
	
	public TablePrinter(JTabbedPane pane)
	{
		this.tabs = pane;
	}

	@Override
	public void print(T component, int timepoint)
	{
		
		// Initialize table
		
		ComponentNode node = new ComponentNode(null, component, timepoint);
		
		DefaultTreeTableModel model = new DefaultTreeTableModel(node);
		
		JXTreeTable table = new JXTreeTable(model);
		
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		
		table.getColumn(0).setPreferredWidth(200);
		table.getColumn(0).setHeaderValue("Tree");
		
		for (int i = 0; i < timepoint; i++)
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
		
		// Initialize scroll
		
		JScrollPane scroll = new JScrollPane(table, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		
		// Create tab
		
		tabs.addTab("Table printer", scroll);
	}

}
