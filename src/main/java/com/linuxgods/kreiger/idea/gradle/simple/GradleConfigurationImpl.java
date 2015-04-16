package com.linuxgods.kreiger.idea.gradle.simple;

import com.intellij.openapi.vfs.VirtualFile;

import java.util.ArrayList;
import java.util.List;

public class GradleConfigurationImpl extends GradleConfiguration {
    private List<GradleBuildFile> buildFiles = new ArrayList<>();
    private List<GradleConfigurationListener> listeners = new ArrayList<>();

    @Override
    public List<GradleBuildFile> getBuildFiles() {
        return buildFiles;
    }

    @Override
    public void addBuildFile(VirtualFile buildFile) {
        this.buildFiles.add(new GradleBuildFile(buildFile));
        for (GradleConfigurationListener listener : listeners) {
            listener.buildFileAdded(buildFile);
        }
    }

    @Override
    public void addGradleConfigurationListener(GradleConfigurationListener gradleConfigurationListener) {
        this.listeners.add(gradleConfigurationListener);
    }
}
