package com.linuxgods.kreiger.idea.gradle.simple;

import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;

import java.util.List;

public abstract class GradleConfiguration {
    abstract List<GradleBuildFile> getBuildFiles();
    abstract void addBuildFile(VirtualFile buildFiles);
    abstract void addGradleConfigurationListener(GradleConfigurationListener gradleConfigurationListener);

    public static GradleConfiguration getInstance(Project project) {
        return ServiceManager.getService(project, GradleConfiguration.class);
    }
}
