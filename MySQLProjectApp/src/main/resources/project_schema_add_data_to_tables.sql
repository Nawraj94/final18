USE projects;

INSERT INTO category (category_name) VALUES ('Doors and Windows');
INSERT INTO category (category_name) VALUES ('Roof');

INSERT INTO material (project_id, material_name, num_required)
VALUES (1, '2-inch screws', 20);

INSERT INTO step (project_id, step_text, step_order) 
VALUES (1, 'Screw door hangers on the top and bottom of each side of the doorframe', 1);

INSERT INTO project_category (project_id, category_id) VALUES (1, 1);
INSERT INTO project_category (project_id, category_id) VALUES (1, 2);