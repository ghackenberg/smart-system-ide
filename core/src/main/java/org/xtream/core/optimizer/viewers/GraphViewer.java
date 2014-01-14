package org.xtream.core.optimizer.viewers;

import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTree;

import org.xtream.core.model.Component;
import org.xtream.core.optimizer.Viewer;
import org.xtream.core.optimizer.nodes.ComponentTreeNode;

public class GraphViewer<T extends Component> extends Viewer<T>
{
	
	private JTabbedPane tabs;
	
	public GraphViewer(JTabbedPane tabs)
	{
		this.tabs = tabs;
	}

	@Override
	public void view(T root)
	{
		JTree tree = new JTree(new ComponentTreeNode(null, root));
		
		JPanel graph = new JPanel();
		
		JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, tree, graph);
		split.setDividerLocation(0.25);
		
		tabs.addTab("Graph viewer", split);
	}

}
