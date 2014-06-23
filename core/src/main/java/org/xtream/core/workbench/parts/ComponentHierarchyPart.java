package org.xtream.core.workbench.parts;

import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeSelectionModel;

import org.xtream.core.model.Component;
import org.xtream.core.workbench.Part;
import org.xtream.core.workbench.events.SelectionEvent;
import org.xtream.core.workbench.models.nodes.ComponentTreeNode;
import org.xtream.core.workbench.renderers.ComponentTreeCellRenderer;

public class ComponentHierarchyPart<T extends Component> extends Part<T>
{
	
	private JTree tree;
	private Component selected;
	
	public ComponentHierarchyPart()
	{
		this(0, 0);
	}
	public ComponentHierarchyPart(int x, int y)
	{
		this(x, y, 1, 1);
	}
	public ComponentHierarchyPart(int x, int y, int width, int height)
	{
		super("Component hierarchy", x, y, width, height);
		
		tree = new JTree();
		
		getPanel().add(new JScrollPane(tree));
	}

	@Override
	public void setRoot(T root)
	{
		super.setRoot(root);
		
		// Tree pane
		final Part<T> self = this;
		
		tree.setModel(new DefaultTreeModel(new ComponentTreeNode(null, root)));
		tree.setCellRenderer(new ComponentTreeCellRenderer());
		tree.addTreeSelectionListener(new TreeSelectionListener()
			{
				@Override
				public void valueChanged(TreeSelectionEvent event)
				{
					ComponentTreeNode node = (ComponentTreeNode) tree.getLastSelectedPathComponent();
					
					if (node.component != selected)
					{
						selected = node.component;
						
						trigger(new SelectionEvent<T>(self, selected));
					}
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
