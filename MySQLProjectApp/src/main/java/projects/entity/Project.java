/**
 *
 */
package projects.entity;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;
import java.sql.Date;

/**
 * @author Promineo
 *
 */
public class Project {
    private Integer projectId;
    private String projectName;
    private BigDecimal estimatedHours;
    private BigDecimal actualHours;
    private Integer difficulty;
    private String notes;

    private List<Material> materials = new LinkedList<>();
    private List<Step> steps = new LinkedList<>();
    private List<Category> categories = new LinkedList<>();
    private Date startDate;  // Add this line
    private Date endDate;    // Add this line
    private String status;   // Add this line

    // Existing methods...

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    public Integer getProjectId() {
        return projectId;
    }

    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
    }
//
private String projectDescription;

    // existing methods...

    public String getProjectDescription() {
        return projectDescription;
    }

    public void setProjectDescription(String projectDescription) {
        this.projectDescription = projectDescription;
    }
//
    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public BigDecimal getEstimatedHours() {
        return estimatedHours;
    }

    public void setEstimatedHours(BigDecimal estimatedHours) {
        this.estimatedHours = estimatedHours;
    }

    public BigDecimal getActualHours() {
        return actualHours;
    }

    public void setActualHours(BigDecimal actualHours) {
        this.actualHours = actualHours;
    }

    public Integer getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(Integer difficulty) {
        this.difficulty = difficulty;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public List<Material> getMaterials() {
        return materials;
    }

    public List<Step> getSteps() {
        return steps;
    }

    public List<Category> getCategories() {
        return categories;
    }

    @Override
    public String toString() {
        String result = "";

        result += "\n   ID=" + projectId;
        result += "\n   name=" + projectName;
        result += "\n   estimatedHours=" + estimatedHours;
        result += "\n   actualHours=" + actualHours;
        result += "\n   difficulty=" + difficulty;
        result += "\n   notes=" + notes;

        result += "\n   Materials:";

        for (Material material : materials) {
            result += "\n      " + material;
        }

        result += "\n   Steps:";

        for (Step step : steps) {
            result += "\n      " + step;
        }

        result += "\n   Categories:";

        for (Category category : categories) {
            result += "\n      " + category;
        }

        return result;
    }
}
