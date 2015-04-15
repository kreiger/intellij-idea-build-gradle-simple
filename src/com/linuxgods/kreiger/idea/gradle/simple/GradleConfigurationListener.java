package com.linuxgods.kreiger.idea.gradle.simple;

import com.intellij.openapi.vfs.VirtualFile;

import java.util.Collection;

public interface GradleConfigurationListener {
    void buildFileAdded(VirtualFile virtualFile);
}
