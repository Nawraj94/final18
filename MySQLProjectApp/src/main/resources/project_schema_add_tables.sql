USE projects;

-- DROP TABLE IF EXISTS project;
-- Tables from MySQL WorkBench

CREATE TABLE project (
    project_id INT AUTO_INCREMENT NOT NULL,
    project_name VARCHAR(128) NOT NULL,
    estimated_hours DECIMAL(7 , 2 ),
    actual_hours DECIMAL(7 , 2 ),
    difficulty INT,
    notes TEXT,
    PRIMARY KEY (project_id)
);

CREATE TABLE `step` (
  `step_id` int AUTO_INCREMENT NOT NULL,
  `project_id` int DEFAULT NULL,
  `step_text` varchar(100) DEFAULT NULL,
  `step_order` int DEFAULT NULL,
  PRIMARY KEY (`step_id`),
  KEY `project_id_idx` (`project_id`),
  CONSTRAINT `project_id` FOREIGN KEY (`project_id`) REFERENCES `project` (`project_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `material` (
  `material_id` int AUTO_INCREMENT NOT NULL,
  `project_id` int DEFAULT NULL,
  `material_name` varchar(45) DEFAULT NULL,
  `num_required` int DEFAULT NULL,
  `cost` decimal(7,2) DEFAULT NULL,
  PRIMARY KEY (`material_id`),
  KEY `project_id_idx` (`project_id`),
  CONSTRAINT `meterial_project_id` FOREIGN KEY (`project_id`) REFERENCES `project` (`project_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `category` (
  `category_id` INT AUTO_INCREMENT NOT NULL,
  `category_name` VARCHAR(45) NULL,
  PRIMARY KEY (`category_id`));

CREATE TABLE `project_category` (
  `category_id` int NOT NULL,
  `project_id` int NOT NULL,
  KEY `categoryFK` (`category_id`),
  KEY `projectFK` (`project_id`),
  CONSTRAINT `categoryFK` FOREIGN KEY (`category_id`) REFERENCES `category` (`category_id`),
  CONSTRAINT `projectFK` FOREIGN KEY (`project_id`) REFERENCES `project` (`project_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


