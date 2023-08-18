-- create table
-- table users
CREATE TABLE IF NOT EXISTS users (
    user_id int NOT NULL AUTO_INCREMENT,
    name varchar(21) NOT NULL,
    password varchar(32) NOT NULL,
    created_at timestamp NOT NULL,
    updated_at timestamp NOT NULL,
    primary key (user_id)
);
-- table tasks
CREATE TABLE IF NOT EXISTS tasks (
    task_id int NOT NULL AUTO_INCREMENT,
    user_id int NOT NULL,
    task_date timestamp NOT NULL,
    created_at timestamp NOT NULL,
    updated_at timestamp NOT NULL,
    primary key (task_id)
);
-- table todos
CREATE TABLE IF NOT EXISTS todos (
    todo_id int NOT NULL AUTO_INCREMENT,
    task_id int NOT NULL,
    todo varchar(200) NOT NULL,
    status varchar(20) NOT NULL,
    created_at timestamp NOT NULL,
    updated_at timestamp NOT NULL,
    primary key (todo_id)
);
-- table pomodoros
CREATE TABLE IF NOT EXISTS pomodoros (
    pomodoro_id int NOT NULL AUTO_INCREMENT,
    todo_id int NOT NULL,
    perform_count int NOT NULL,
    plan_count int NOT NULL,
    crated_at timestamp NOT NULL,
    updated_at timestamp NOT NULL,
    primary key (pomodoro_id)
);

-- sef UNIQUE
ALTER TABLE IF EXISTS users
    ADD CONSTRAINT uniq_users_name UNIQUE (name)
;

-- set FK
-- tasks
ALTER TABLE IF EXISTS tasks
    ADD CONSTRAINT fk_users_tasks
    FOREIGN KEY (user_id)
    REFERENCES users
    ON DELETE CASCADE
;

-- todos
ALTER TABLE IF EXISTS todos
    ADD CONSTRAINT fk_tasks_todos
    FOREIGN KEY (task_id)
    REFERENCES tasks
    ON DELETE CASCADE
;

-- pomodoros
ALTER TABLE IF EXISTS pomodoros
    ADD CONSTRAINT fk_todos_pomodoros
    FOREIGN KEY (todo_id)
    REFERENCES todos
    ON DELETE CASCADE
;

