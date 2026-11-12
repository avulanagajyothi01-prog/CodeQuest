CREATE DATABASE IF NOT EXISTS codequest_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE codequest_db;

CREATE TABLE IF NOT EXISTS users (
  user_id INT AUTO_INCREMENT PRIMARY KEY,
  username VARCHAR(50) NOT NULL UNIQUE,
  email VARCHAR(100) NOT NULL UNIQUE,
  password_hash VARCHAR(255) NOT NULL,
  xp INT NOT NULL DEFAULT 0,
  level INT NOT NULL DEFAULT 1,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS admins (
  admin_id INT AUTO_INCREMENT PRIMARY KEY,
  username VARCHAR(50) NOT NULL UNIQUE,
  password_hash VARCHAR(255) NOT NULL
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS missions (
  mission_id INT AUTO_INCREMENT PRIMARY KEY,
  title VARCHAR(200) NOT NULL,
  description TEXT NOT NULL,
  category VARCHAR(30) NOT NULL,
  difficulty VARCHAR(20) NOT NULL,
  option_a VARCHAR(500) NOT NULL,
  option_b VARCHAR(500) NOT NULL,
  option_c VARCHAR(500) NOT NULL,
  option_d VARCHAR(500) NOT NULL,
  correct_answer CHAR(1) NOT NULL,
  xp_reward INT NOT NULL,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS mission_submissions (
  submission_id INT AUTO_INCREMENT PRIMARY KEY,
  user_id INT NOT NULL,
  mission_id INT NOT NULL,
  selected_answer CHAR(1) NOT NULL,
  is_correct BOOLEAN NOT NULL,
  xp_earned INT NOT NULL DEFAULT 0,
  submitted_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT fk_submit_user FOREIGN KEY(user_id) REFERENCES users(user_id),
  CONSTRAINT fk_submit_mission FOREIGN KEY(mission_id) REFERENCES missions(mission_id),
  CONSTRAINT uq_attempt UNIQUE(user_id,mission_id)
) ENGINE=InnoDB;

INSERT INTO missions (title,description,category,difficulty,option_a,option_b,option_c,option_d,correct_answer,xp_reward)
SELECT 'Java Basics: Main Method','Which method is the entry point for a Java application?','Java','Easy',
       'start()','main()','run()','init()','B',50
WHERE NOT EXISTS (SELECT 1 FROM missions WHERE title='Java Basics: Main Method');

INSERT INTO missions (title,description,category,difficulty,option_a,option_b,option_c,option_d,correct_answer,xp_reward)
SELECT 'SQL: Filter Rows','Which SQL clause filters rows based on a condition?','SQL','Easy',
       'ORDER BY','GROUP BY','WHERE','LIMIT','C',40
WHERE NOT EXISTS (SELECT 1 FROM missions WHERE title='SQL: Filter Rows');

INSERT INTO missions (title,description,category,difficulty,option_a,option_b,option_c,option_d,correct_answer,xp_reward)
SELECT 'DSA: Binary Search','What is the average time complexity of binary search on a sorted array?','DSA','Medium',
       'O(n)','O(log n)','O(n²)','O(1)','B',70
WHERE NOT EXISTS (SELECT 1 FROM missions WHERE title='DSA: Binary Search');

INSERT INTO missions (title,description,category,difficulty,option_a,option_b,option_c,option_d,correct_answer,xp_reward)
SELECT 'Java: Inheritance','Which keyword lets one Java class inherit from another class?','Java','Medium',
       'implements','inherits','extends','instanceof','C',60
WHERE NOT EXISTS (SELECT 1 FROM missions WHERE title='Java: Inheritance');

INSERT INTO missions (title,description,category,difficulty,option_a,option_b,option_c,option_d,correct_answer,xp_reward)
SELECT 'SQL: Count Records','Which SQL aggregate function counts matching rows?','SQL','Easy',
       'TOTAL()','COUNT()','NUMBER()','SUM()','B',40
WHERE NOT EXISTS (SELECT 1 FROM missions WHERE title='SQL: Count Records');

INSERT INTO missions (title,description,category,difficulty,option_a,option_b,option_c,option_d,correct_answer,xp_reward)
SELECT 'DSA: Stack','Which ordering principle describes a stack data structure?','DSA','Easy',
       'FIFO','Random','Sorted','LIFO','D',50
WHERE NOT EXISTS (SELECT 1 FROM missions WHERE title='DSA: Stack');
