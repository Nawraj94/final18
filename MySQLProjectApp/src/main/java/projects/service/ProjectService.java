package projects.service;

import java.util.List;
import java.util.NoSuchElementException;
// import java.util.Optional;
import projects.dao.ProjectDao;
import projects.entity.Project;
import projects.exception.DbException;


public class ProjectService {
    private ProjectDao projectDao = new ProjectDao();

    public Project addProject(Project project) {
        return projectDao.insertProject(project);
    }

    public List<Project> fetchAllProjects() {
        return projectDao.fetchAllProjects();
    }

    public Project fetchProjectById(Integer projectId) {
        return projectDao.fetchProjectById(projectId).orElseThrow(
                () -> new NoSuchElementException("Project with project ID: "
                + projectId + " does not exist.")
        );
    }

    public Project modifyProjectDetails(Project project) {
        boolean success = projectDao.modifyProjectDetails(project);
        if (!success) {
            throw new DbException("Failed to modify project details.");
        }
        return projectDao.fetchProjectById(project.getProjectId()).orElseThrow(
                () -> new NoSuchElementException("Project with project ID: "
                        + project.getProjectId() + " does not exist.")
        );
    }

    public boolean deleteProject(Integer projectId) {
        boolean success = projectDao.deleteProject(projectId);
        if (!success) {
            throw new DbException("Project with project ID: " + projectId + " does not exist.");
        }
        return true;
    }
}
