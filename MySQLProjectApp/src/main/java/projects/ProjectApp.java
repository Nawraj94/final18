package projects;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;
import projects.entity.Project;
import projects.exception.DbException;
import projects.service.ProjectService;

public class ProjectApp {
    private ProjectService projectService = new ProjectService();
    private Scanner scanner = new Scanner(System.in);
    private Project curProject;

    // @formatter:off
    private List<String> operations = List.of(
            "1) Add a project",
            "2) List projects",
            "3) Select a project",
            "4) Update project details",//ading new features
            "5) Delete a project");
    // @formatter:on

    public static void main(String[] args) {
        ProjectApp app = new ProjectApp();
        app.processUserSelections();
    }

    private void processUserSelections() {
        boolean done = false;

        while (!done) {
            try {
                int selection = getUserSelection();

                switch (selection) {
                    case -1:
                        done = exitMenu();
                        break;
                    case 1:
                        createProject();
                        break;
                    case 2:
                        listProject();
                        break;
                    case 3:
                        selectProject();
                        break;
                    case 4:
                        updateProjectDetails();
                        break;
                    case 5:
                        deleteProject();
                        break;
                    default:
                        System.out.println("\n" + selection + " is not a valid selection. Try again.");
                }
            } catch (Exception e) {
                System.out.println("\nError " + e + " Try again.");
            }
        }
    }

    private void updateProjectDetails() {
        if (Objects.isNull(curProject)) {
            System.out.println("\nPlease select a project.");
            return;
        }

        System.out.println("\nCurrent Project Details:");
        System.out.println("Project ID: " + curProject.getProjectId());
        System.out.println("Project Name: " + curProject.getProjectName());
        // Add similar lines for other project details

        Project updatedProject = new Project();
        updatedProject.setProjectId(curProject.getProjectId());

        String updatedName = getStringInput("Enter updated project name (press Enter to keep current)");
        updatedProject.setProjectName(updatedName != null ? updatedName : curProject.getProjectName());

        // Add similar lines for updating other project details

        projectService.modifyProjectDetails(updatedProject);

        // Refresh the current project
        curProject = projectService.fetchProjectById(curProject.getProjectId());
        System.out.println("\nProject details updated successfully.");
    }

    private void deleteProject() {
        listProject();

        Integer projectIdToDelete = getIntInput("\nEnter the project ID to delete");
        projectService.deleteProject(projectIdToDelete);

        if (Objects.nonNull(curProject) && curProject.getProjectId().equals(projectIdToDelete)) {
            curProject = null;
        }

        System.out.println("\nProject deleted successfully.");
    }

    private void selectProject() {
        listProject();

        Integer projectId = getIntInput("\nEnter a project ID to select a project");
        curProject = null;
        curProject = projectService.fetchProjectById(projectId);

        if (Objects.isNull(curProject)) {
            System.out.println("\nInvalid project ID selected.");
        }
    }

    private void listProject() {
        List<Project> projects = projectService.fetchAllProjects();
        System.out.println("\nProjects:");

        projects.forEach(p -> System.out.println("    " + p.getProjectId()
                                                 + ": " + p.getProjectName()));
    }

    private void createProject() {
        String projectName = getStringInput("Enter the project name");
        BigDecimal estimatedHours = getDecimalInput("Enter the estimated hours");
        BigDecimal actualHours = getDecimalInput("Enter the actual hours");
        Integer difficulty = getIntInput("Enter the project difficulty (1-5)");
        String notes = getStringInput("Enter the project notes");

        Project project = new Project();
        project.setProjectName(projectName);
        project.setEstimatedHours(estimatedHours);
        project.setActualHours(actualHours);
        project.setDifficulty(difficulty);
        project.setNotes(notes);

        Project dbProject = projectService.addProject(project);
        System.out.println("You have successfully created project: " + dbProject);
    }

    private BigDecimal getDecimalInput(String prompt) {
        String input = getStringInput(prompt);
        if (Objects.isNull(input)) {
            return null;
        }

        try {
            return new BigDecimal(input).setScale(2, RoundingMode.DOWN);
        } catch (NumberFormatException e) {
            throw new DbException(input + " is not a valid decimal.");
        }
    }

    private boolean exitMenu() {
        System.out.println("\nExiting the menu.");
        return true;
    }

    private int getUserSelection() {
        printOperations();
        Integer input = getIntInput("\nEnter a menu selection (press Enter to quit)");
        return input == null ? -1 : input;
    }

    private Integer getIntInput(String prompt) {
        String input = getStringInput(prompt);
        if (Objects.isNull(input)) {
            return null;
        }

        try {
            return Integer.parseInt(input);
        } catch (NumberFormatException e) {
            throw new DbException(input + " is not a valid number.");
        }
    }

    private String getStringInput(String prompt) {
        System.out.print(prompt + ": ");
        String input = scanner.nextLine();

        return input.isBlank() ? null : input.trim();
    }

    private void printOperations() {
        System.out.printf("%s%n%30s%n%s%n", "-".repeat(50),
                "MAIN MENU", "-".repeat(50)); // prints a header
        operations.forEach(e -> System.out.println("\t" + e)); // prints all operations

        if (Objects.isNull(curProject)) {
            System.out.println("\nYou are not working with a project.");
        } else {
            System.out.println("\nYou are working with a project: " + curProject);
        }
    }
}
