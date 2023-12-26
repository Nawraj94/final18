package projects.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
// import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import projects.entity.Category;
import projects.entity.Material;
import projects.entity.Project;
import projects.entity.Step;
import projects.exception.DbException;

public class ProjectDao extends DaoBase {
    private static final String CATEGORY_TABLE = "category";
    private static final String MATERIAL_TABLE = "material";
    private static final String PROJECT_TABLE = "project";
    private static final String PROJECT_CATEGORY_TABLE = "project_category";
    private static final String STEP_TABLE = "step";

    public Project insertProject(Project project) {
        String insert = "INSERT INTO " + PROJECT_TABLE
                        + " (project_name, estimated_hours,"
                        + " actual_hours, difficulty, notes)"
                        + " VALUES (?, ?, ?, ?, ?);";

        try (Connection conn = DbConnection.getConnection()) {
            startTransaction(conn);
            try (PreparedStatement stmt = conn.prepareStatement(insert)) {
                setParameter(stmt, 1, project.getProjectName(), String.class);
                setParameter(stmt, 2, project.getEstimatedHours(), BigDecimal.class);
                setParameter(stmt, 3, project.getActualHours(), BigDecimal.class);
                setParameter(stmt, 4, project.getDifficulty(), Integer.class);
                setParameter(stmt, 5, project.getNotes(), String.class);

                stmt.executeUpdate();

                Integer projectId = getLastInsertId(conn, PROJECT_TABLE);

                commitTransaction(conn);

                project.setProjectId(projectId);
                return project;
            } catch (Exception e) {
                rollbackTransaction(conn);
                throw new DbException(e);
            }
        } catch (SQLException e) {
            throw new DbException(e);
        }
    }

    public List<Project> fetchAllProjects() {
        String query = "SELECT project_id, project_name, estimated_hours,"
                       + " actual_hours, difficulty, notes"
                       + " FROM " + PROJECT_TABLE
                       + " ORDER BY project_name;";

        try (Connection conn = DbConnection.getConnection()) {
            startTransaction(conn);

            try (PreparedStatement stmt = conn.prepareStatement(query)) {

                try (ResultSet rs = stmt.executeQuery()) {
                    commitTransaction(conn);

                    List<Project> projects = new ArrayList<>();
                    while (rs.next()) {
                        Project project = new Project();

                        project.setProjectId(rs.getObject("project_id", Integer.class));
                        project.setProjectName(rs.getString("project_name"));
                        project.setEstimatedHours(rs.getBigDecimal("estimated_hours"));
                        project.setActualHours(rs.getBigDecimal("actual_hours"));
                        project.setDifficulty(rs.getObject("difficulty", Integer.class));
                        project.setNotes("notes");

                        projects.add(project);
                    }

                    return projects;
                }
            } catch (Exception e) {
                rollbackTransaction(conn);
                throw new DbException(e);
            }
        } catch (SQLException e) {
            throw new DbException(e);
        }
    }

    public Optional<Project> fetchProjectById(Integer projectId) {
        String query = "SELECT * FROM " + PROJECT_TABLE
                       + " WHERE project_id = ?;";

        try (Connection conn = DbConnection.getConnection()) {
            startTransaction(conn);

            try {
                Project project = null;

                try (PreparedStatement stmt = conn.prepareStatement(query)) {
                    setParameter(stmt, 1, projectId, Integer.class);

                    try (ResultSet rs = stmt.executeQuery()) {
                        if (rs.next()) {
                            project = new Project();

                            project.setProjectId(rs.getObject("project_id", Integer.class));
                            project.setProjectName(rs.getString("project_name"));
                            project.setEstimatedHours(rs.getBigDecimal("estimated_hours"));
                            project.setActualHours(rs.getBigDecimal("actual_hours"));
                            project.setDifficulty(rs.getObject("difficulty", Integer.class));
                            project.setNotes("notes");
                        }
                    }
                }

                if (Objects.nonNull(project)) {
                    project.getMaterials().addAll(fetchMaterialsForProject(conn, projectId));
                    project.getSteps().addAll(fetchStepsForProject(conn, projectId));
                    project.getCategories().addAll(fetchCategoriesForProject(conn, projectId));
                }

                commitTransaction(conn);

                return Optional.ofNullable(project);
            } catch (Exception e) {
                rollbackTransaction(conn);
                throw new DbException(e);
            }
        } catch (SQLException e) {
            throw new DbException(e);
        }
    }
    public boolean modifyProjectDetails(Project project) {
        String sql = "UPDATE projects SET project_name=?, project_description=?, start_date=?, end_date=?, status=? WHERE project_id=?";
        
        try (Connection connection = DbConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, project.getProjectName());
            preparedStatement.setString(2, project.getProjectDescription());
            preparedStatement.setDate(3, project.getStartDate());
            preparedStatement.setDate(4, project.getEndDate());
            preparedStatement.setString(5, project.getStatus());
            preparedStatement.setInt(6, project.getProjectId());
    
            int rowsAffected = preparedStatement.executeUpdate();
            connection.commit();
            
            return rowsAffected == 1;
        } catch (SQLException e) {
            // DbConnection.rollbackTransaction();
            throw new DbException("Error modifying project details.", e);
        }
    }
    
    public boolean deleteProject(Integer projectId) {
        String sql = "DELETE FROM projects WHERE project_id=?";
        
        try (Connection connection = DbConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, projectId);
    
            int rowsAffected = preparedStatement.executeUpdate();
            connection.commit();
            
            return rowsAffected == 1;
        } catch (SQLException e) {
            // DbConnection.rollbackTransaction();
            throw new DbException("Error deleting project.", e);
        }
    }
    
    private List<Category> fetchCategoriesForProject(
            Connection conn, Integer projectId) throws SQLException {
        String query = "SELECT c.* FROM " + CATEGORY_TABLE + " c"
                       + " JOIN " + PROJECT_CATEGORY_TABLE + " pc USING (category_id)"
                       + " WHERE project_id = ?;";

        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            setParameter(stmt, 1, projectId, Integer.class);

            try (ResultSet rs = stmt.executeQuery()) {
                List<Category> categories = new ArrayList<>();

                while (rs.next()) {
                    Category category = new Category();

                    category.setCategoryId(rs.getObject("category_id", Integer.class));
                    category.setCategoryName(rs.getString("category_name"));

                    categories.add(category);
                }

                return categories;
            }
        }
    }

    private List<Step> fetchStepsForProject(
            Connection conn, Integer projectId) throws SQLException {
        String query = "SELECT * FROM " + STEP_TABLE
                       + " WHERE project_id = ?;";

        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            setParameter(stmt, 1, projectId, Integer.class);

            try (ResultSet rs = stmt.executeQuery()) {
                List<Step> steps = new ArrayList<>();

                while (rs.next()) {
                    Step step = new Step();

                    step.setStepId(rs.getObject("step_id", Integer.class));
                    step.setStepText(rs.getString("step_text"));
                    step.setProjectId(rs.getObject("project_id", Integer.class));
                    step.setStepOrder(rs.getObject("step_order", Integer.class));

                    steps.add(step);
                }

                return steps;
            }
        }
    }

    private List<Material> fetchMaterialsForProject(
            Connection conn, Integer projectId) throws SQLException {
        String query = "SELECT * FROM " + MATERIAL_TABLE
                       + " WHERE project_id = ?;";

        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            setParameter(stmt, 1, projectId, Integer.class);

            try (ResultSet rs = stmt.executeQuery()) {
                List<Material> materials = new ArrayList<>();

                while (rs.next()) {
                    Material material = new Material();

                    material.setMaterialId(rs.getObject("material_id", Integer.class));
                    material.setMaterialName(rs.getString("material_name"));
                    material.setProjectId(rs.getObject("project_id", Integer.class));
                    material.setNumRequired(rs.getObject("num_required", Integer.class));
                    material.setCost(rs.getBigDecimal("cost"));

                    materials.add(material);
                }

                return materials;
            }
        }
    }

}
