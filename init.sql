-- Tabla de usuarios
CREATE TABLE users (
                       user_id SERIAL PRIMARY KEY,
                       username VARCHAR(50) UNIQUE NOT NULL,
                       email VARCHAR(100) UNIQUE NOT NULL,
                       password VARCHAR(100) NOT NULL
);

-- Tabla de tareas
CREATE TABLE tasks (
                       task_id SERIAL PRIMARY KEY,
                       user_id INTEGER NOT NULL REFERENCES db_users(user_id) ON DELETE CASCADE,
                       title VARCHAR(255) NOT NULL,
                       description TEXT,
                       status VARCHAR(20) DEFAULT 'pending',
                       due_date TIMESTAMP,
                       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);


-- Tabla de etiquetas (tags)
CREATE TABLE tags (
                      tag_id SERIAL PRIMARY KEY,
                      name VARCHAR(50) UNIQUE NOT NULL
);

-- Relación muchos a muchos: tareas <-> etiquetas
CREATE TABLE task_tags (
                           task_id INTEGER NOT NULL REFERENCES tasks(task_id) ON DELETE CASCADE,
                           tag_id INTEGER NOT NULL REFERENCES tags(tag_id) ON DELETE CASCADE,
                           PRIMARY KEY (task_id, tag_id)
);
