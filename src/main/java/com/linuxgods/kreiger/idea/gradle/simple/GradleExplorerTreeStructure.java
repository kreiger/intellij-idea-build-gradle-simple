package com.linuxgods.kreiger.idea.gradle.simple;

import com.intellij.ide.util.treeView.AbstractTreeStructure;
import com.intellij.ide.util.treeView.NodeDescriptor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ui.CellAppearanceEx;
import com.intellij.ui.HtmlListCellRenderer;
import com.intellij.ui.SimpleColoredComponent;
import com.intellij.ui.SimpleTextAttributes;
import com.intellij.util.ArrayUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

class GradleExplorerTreeStructure extends AbstractTreeStructure {

    private Project project;
    private Object root = new Object();

    public GradleExplorerTreeStructure(Project project) {
        this.project = project;
    }

    @Override
    public Object getRootElement() {
        return root;
    }

    @Override
    public Object[] getChildElements(Object parent) {
        GradleConfiguration gradleConfiguration = GradleConfiguration.getInstance(project);
        if (parent == root) {
            return gradleConfiguration.getBuildFiles().toArray();
        }
        if (parent instanceof GradleBuildFile) {
            return ((GradleBuildFile) parent).getTasks().toArray();
        }
        return ArrayUtil.EMPTY_OBJECT_ARRAY;
    }

    @Nullable
    @Override
    public Object getParentElement(Object o) {
        if (o instanceof GradleBuildFile) {
            return root;
        }
        if (o instanceof GradleBuildFile.Task) {
            return ((GradleBuildFile.Task) o).getBuildFile();
        }
        return null;
    }

    @NotNull
    @Override
    public NodeDescriptor createDescriptor(final Object element, NodeDescriptor parentDescriptor) {
        return new SimpleNodeDescriptor(element, parentDescriptor, element != root);
    }

    @Override
    public void commit() {

    }

    @Override
    public boolean hasSomethingToCommit() {
        return false;
    }

    private class SimpleNodeDescriptor extends NodeDescriptor implements CellAppearanceEx {
        private final boolean update;
        private final Object element;

        public SimpleNodeDescriptor(Object element, NodeDescriptor parentDescriptor, boolean update) {
            super(GradleExplorerTreeStructure.this.project, parentDescriptor);
            this.element = element;
            this.update = update;
            this.myName = element.toString();
        }

        @Override
        public boolean update() {
            return update;
        }

        @Override
        public Object getElement() {
            return element;
        }

        @NotNull
        @Override
        public String getText() {
            return toString();
        }

        public void customize(@NotNull SimpleColoredComponent component) {
            component.append(toString(), SimpleTextAttributes.REGULAR_ATTRIBUTES);
        }

        @Override
        public void customize(@NotNull final HtmlListCellRenderer renderer) {
            renderer.append(toString(), SimpleTextAttributes.REGULAR_ATTRIBUTES);
        }
    }
}
