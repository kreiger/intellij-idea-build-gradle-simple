package com.linuxgods.kreiger.idea.gradle.simple;

import com.intellij.execution.ExecutionException;
import com.intellij.execution.configurations.GeneralCommandLine;
import com.intellij.execution.junit.JUnitProcessHandler;
import com.intellij.execution.util.ExecutionErrorDialog;
import com.intellij.ide.util.treeView.AbstractTreeBuilder;
import com.intellij.ide.util.treeView.IndexComparator;
import com.intellij.ide.util.treeView.NodeDescriptor;
import com.intellij.openapi.Disposable;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.SimpleToolWindowPanel;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.ui.DoubleClickListener;
import com.intellij.ui.ScrollPaneFactory;
import com.intellij.ui.treeStructure.Tree;
import org.gradle.tooling.GradleConnector;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.event.MouseEvent;

public class GradleExplorer extends SimpleToolWindowPanel implements Disposable {

    private final AbstractTreeBuilder treeBuilder;

    public GradleExplorer(final Project project) {
        super(true, true);

        final DefaultMutableTreeNode root = new DefaultMutableTreeNode("Root");
        final DefaultTreeModel treeModel = new DefaultTreeModel(root);
        final Tree tree = new Tree(treeModel);
        tree.setRootVisible(false);
        tree.setShowsRootHandles(true);
        treeBuilder = new AbstractTreeBuilder(tree, treeModel, new GradleExplorerTreeStructure(project), IndexComparator.INSTANCE);
        setContent(ScrollPaneFactory.createScrollPane(tree));
        GradleConfiguration gradleConfiguration = GradleConfiguration.getInstance(project);
        gradleConfiguration.addGradleConfigurationListener(new GradleConfigurationListener() {
            @Override
            public void buildFileAdded(VirtualFile virtualFiles) {
                treeBuilder.queueUpdate();
            }
        });
        new DoubleClickListener() {
            @Override
            protected boolean onDoubleClick(MouseEvent e) {
                final int eventY = e.getY();
                final int row = tree.getClosestRowForLocation(e.getX(), eventY);
                if (row >= 0) {
                    final Rectangle bounds = tree.getRowBounds(row);
                    if (bounds != null && eventY > bounds.getY() && eventY < bounds.getY() + bounds.getHeight()) {
                        runSelection(tree, project);
                        return true;
                    }
                }
                return false;
            }
        }.installOn(tree);
    }

    private void runSelection(Tree tree, final Project project) {
        TreePath[] selectionPaths = tree.getSelectionPaths();
        for (TreePath selectionPath : selectionPaths) {
            final Object userObject = ((DefaultMutableTreeNode) selectionPath.getLastPathComponent()).getUserObject();
            NodeDescriptor nodeDescriptor = (NodeDescriptor) userObject;
            Object element = nodeDescriptor.getElement();
            if (element instanceof GradleBuildFile.Task) {
                GradleBuildFile.Task task = (GradleBuildFile.Task) element;
                task.execute();
            }
        }
    }

    @Override
    public void dispose() {

    }

}
