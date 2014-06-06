package org.xtream.core.workbench.viewers;

import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeSelectionModel;

import org.xtream.core.model.Component;
import org.xtream.core.optimizer.Viewer;
import org.xtream.core.workbench.Part;
import org.xtream.core.workbench.events.SelectionEvent;
import org.xtream.core.workbench.nodes.AbstractComponentTreeNode;
import org.xtream.core.workbench.nodes.ChartComponentTreeNode;
import org.xtream.core.workbench.renderers.ComponentTreeCellRenderer;

public class TreeViewer<T extends Component> extends Part implements Viewer<T>
{
	
	private JTree tree;
	
	public TreeViewer()
	{
		this(0, 0);
	}
	public TreeViewer(int x, int y)
	{
		this(x, y, 1, 1);
	}
	public TreeViewer(int x, int y, int width, int height)
	{
		super("Tree viewer", x, y, width, height);
		
		tree = new JTree();
		
		getPanel().add(tree);
	}

	@Override
	public void view(T root)
	{
		// Tree pane
		final Part self = this;
		
		tree.setModel(new DefaultTreeModel(new ChartComponentTreeNode(null, root)));
		tree.setCellRenderer(new ComponentTreeCellRenderer());
		tree.addTreeSelectionListener(new TreeSelectionListener()
			{
				@Override
				public void valueChanged(TreeSelectionEvent event)
				{
					AbstractComponentTreeNode node = (AbstractComponentTreeNode) tree.getLastSelectedPathComponent();
					
					trigger(new SelectionEvent(self, node.component));
				}
			}
		);
		tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		
		for (int i = 0; i < tree.getRowCount(); i++)
		{
			tree.expandRow(i);
		}
		
		tree.setSelectionRow(0);
	}

}