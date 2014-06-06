package org.xtream.core.workbench.viewers;

import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreeSelectionModel;

import org.xtream.core.model.Component;
import org.xtream.core.optimizer.Viewer;
import org.xtream.core.workbench.Part;
import org.xtream.core.workbench.nodes.AbstractComponentTreeNode;
import org.xtream.core.workbench.nodes.ChartComponentTreeNode;
import org.xtream.core.workbench.renderers.ComponentTreeCellRenderer;

public class TreeViewer<T extends Component> extends Part implements Viewer<T>
{
	
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
	}

	@Override
	public void view(T root)
	{
		// Tree pane
		
		final JTree tree = new JTree(new ChartComponentTreeNode(null, root));

		tree.setCellRenderer(new ComponentTreeCellRenderer());
		tree.addTreeSelectionListener(new TreeSelectionListener()
			{
				@Override
				public void valueChanged(TreeSelectionEvent event)
				{
					AbstractComponentTreeNode node = (AbstractComponentTreeNode) tree.getLastSelectedPathComponent();
					
					System.out.println(node.component.qualifiedName);
				}
			}
		);
		tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		
		for (int i = 0; i < tree.getRowCount(); i++)
		{
			tree.expandRow(i);
		}
		
		tree.setSelectionRow(0);
		
		// Set component
		
		getPanel().add(tree);
	}

}
