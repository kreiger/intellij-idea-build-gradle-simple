package com.linuxgods.kreiger.idea.gradle.simple;

import com.intellij.openapi.vfs.VirtualFile;
import org.gradle.tooling.GradleConnector;
import org.gradle.tooling.ProjectConnection;
import org.gradle.tooling.model.DomainObjectSet;
import org.gradle.tooling.model.GradleProject;
import org.gradle.tooling.model.GradleTask;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class GradleBuildFile {
    private final String name;
    private final List<GradleBuildFile.Task> tasks = new ArrayList<>();
    private final File directory;

    public GradleBuildFile(VirtualFile buildFile) {
        GradleConnector gradleConnector = GradleConnector.newConnector();
        directory = new File(buildFile.getParent().getCanonicalPath());
        ProjectConnection projectConnection = gradleConnector.forProjectDirectory(directory).connect();
        try {
            GradleProject project = projectConnection.getModel(GradleProject.class);
            this.name = project.getName();
            DomainObjectSet<? extends GradleTask> tasks = project.getTasks();
            for (GradleTask task : tasks) {
                this.tasks.add(new Task(task));
            }
        } finally {
            projectConnection.close();
        }

    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return getName();
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public File getDirectory() {
        return directory;
    }

    class Task {

        private final String name;

        public Task(GradleTask task) {
            this.name = task.getName();
        }

        @Override
        public String toString() {
            return name;
        }

        public GradleBuildFile getBuildFile() {
            return GradleBuildFile.this;
        }

        public String getName() {
            return name;
        }
    }
}
