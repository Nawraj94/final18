class Project:
    def __init__(self, project_id, project_name, estimated_hours, actual_hours, difficulty, notes):
        self.project_id = project_id
        self.project_name = project_name
        self.estimated_hours = estimated_hours
        self.actual_hours = actual_hours
        self.difficulty = difficulty
        self.notes = notes

    def __str__(self):
        return f"Project ID: {self.project_id}, Project Name: {self.project_name}"


class ProjectService:
    def __init__(self):
        self.projects = []
        self.project_id_counter = 1

    def add_project(self, project):
        project.project_id = self.project_id_counter
        self.projects.append(project)
        self.project_id_counter += 1
        return project

    def fetch_all_projects(self):
        return self.projects

    def fetch_project_by_id(self, project_id):
        for project in self.projects:
            if project.project_id == project_id:
                return project
        return None

    def modify_project_details(self, updated_project):
        for i, project in enumerate(self.projects):
            if project.project_id == updated_project.project_id:
                self.projects[i] = updated_project
                break

    def delete_project(self, project_id):
        self.projects = [project for project in self.projects if project.project_id != project_id]


class ProjectApp:
    def __init__(self):
        self.project_service = ProjectService()
        self.cur_project = None

    def main(self):
        self.process_user_selections()

    def process_user_selections(self):
        done = False

        while not done:
            try:
                selection = self.get_user_selection()

                if selection == -1:
                    done = self.exit_menu()
                elif selection == 1:
                    self.create_project()
                elif selection == 2:
                    self.list_project()
                elif selection == 3:
                    self.select_project()
                elif selection == 4:
                    self.update_project_details()
                elif selection == 5:
                    self.delete_project()
                else:
                    print(f"\n{selection} is not a valid selection. Try again.")
            except Exception as e:
                print(f"\nError {e}. Try again.")

    def update_project_details(self):
        if not self.cur_project:
            print("\nPlease select a project.")
            return

        print("\nCurrent Project Details:")
        print("Project ID:", self.cur_project.project_id)
        print("Project Name:", self.cur_project.project_name)

        updated_name = self.get_string_input("Enter updated project name (press Enter to keep current)")
        self.cur_project.project_name = updated_name if updated_name else self.cur_project.project_name

        self.project_service.modify_project_details(self.cur_project)

        print("\nProject details updated successfully.")

    def delete_project(self):
        self.list_project()

        project_id_to_delete = self.get_int_input("\nEnter the project ID to delete")
        self.project_service.delete_project(project_id_to_delete)

        if self.cur_project and self.cur_project.project_id == project_id_to_delete:
            self.cur_project = None

        print("\nProject deleted successfully.")

    def select_project(self):
        self.list_project()

        project_id = self.get_int_input("\nEnter a project ID to select a project")
        self.cur_project = self.project_service.fetch_project_by_id(project_id)

        if not self.cur_project:
            print("\nInvalid project ID selected.")

    def list_project(self):
        projects = self.project_service.fetch_all_projects()
        print("\nProjects:")

        for project in projects:
            print(f"    {project}")

    def create_project(self):
        project_name = self.get_string_input("Enter the project name")
        estimated_hours = self.get_decimal_input("Enter the estimated hours")
        actual_hours = self.get_decimal_input("Enter the actual hours")
        difficulty = self.get_int_input("Enter the project difficulty (1-5)")
        notes = self.get_string_input("Enter the project notes")

        project = Project(None, project_name, estimated_hours, actual_hours, difficulty, notes)
        db_project = self.project_service.add_project(project)

        print(f"You have successfully created project: {db_project}")

    def get_decimal_input(self, prompt):
        user_input = self.get_string_input(prompt)
        if not user_input:
            return None

        try:
            return round(float(user_input), 2)
        except ValueError:
            raise ValueError(f"{user_input} is not a valid decimal.")

    def exit_menu(self):
        print("\nExiting the menu.")
        return True

    def get_user_selection(self):
        self.print_operations()
        user_input = self.get_int_input("\nEnter a menu selection (press Enter to quit)")
        return user_input if user_input is not None else -1

    def get_int_input(self, prompt):
        user_input = self.get_string_input(prompt)
        if not user_input:
            return None

        try:
            return int(user_input)
        except ValueError:
            raise ValueError(f"{user_input} is not a valid number.")

    def get_string_input(self, prompt):
        user_input = input(f"{prompt}: ").strip()
        return None if not user_input else user_input

    def print_operations(self):
        print(f"{'-'.center(50, '-')}")  # prints a header
        print("MAIN MENU".center(50))
        print(f"{'-'.center(50, '-')}")  # prints a header

        for operation in operations:
            print(f"\t{operation}")

        if not self.cur_project:
            print("\nYou are not working with a project.")
        else:
            print("\nYou are working with a project:", self.cur_project)


if __name__ == "__main__":
    operations = [
        "1) Add a project",
        "2) List projects",
        "3) Select a project",
        "4) Update project details",
        "5) Delete a project"
    ]

    app = ProjectApp()
    app.main()
