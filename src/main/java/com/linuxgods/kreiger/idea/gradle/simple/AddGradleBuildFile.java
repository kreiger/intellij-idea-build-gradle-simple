package com.linuxgods.kreiger.idea.gradle.simple;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.wm.ToolWindowManager;

import java.util.HashSet;
import java.util.Set;

import static java.util.Arrays.asList;

public class AddGradleBuildFile extends AnAction {
    @Override
    public void actionPerformed(AnActionEvent event) {
        final DataContext dataContext = event.getDataContext();
        final Project project = CommonDataKeys.PROJECT.getData(dataContext);
        if (project == null) {
            return;
        }
        final VirtualFile[] contextFiles = CommonDataKeys.VIRTUAL_FILE_ARRAY.getData(dataContext);
        if (contextFiles == null || contextFiles.length == 0) {
            return;
        }
        final Set<VirtualFile> files = new HashSet<>();
        files.addAll(asList(contextFiles));
        GradleConfiguration gradleConfiguration = GradleConfiguration.getInstance(project);
        for (VirtualFile file : files) {
            gradleConfiguration.addBuildFile(file);
        }
        ToolWindowManager.getInstance(project).getToolWindow("Gradle Build").activate(null);
    }

    @Override
    public void update(AnActionEvent e) {
        VirtualFile file = e.getData(CommonDataKeys.VIRTUAL_FILE);
        boolean enabled = file != null && file.getName().equals("build.gradle");
        e.getPresentation().setEnabled(enabled);
        e.getPresentation().setVisible(enabled);
    }

}
