CREATE TABLE IF NOT EXISTS tasks (
  id VARCHAR(36) NOT NULL PRIMARY KEY,
  user_id VARCHAR(36) NOT NULL,
  name VARCHAR(50) NOT NULL,
  description VARCHAR(50) NOT NULL,
  priority ENUM('LOW', 'NORMAL', 'HIGH') NOT NULL,
  status ENUM('PENDING', 'COMPLETED', 'CANCELLED') NOT NULL,
  due_date DATETIME(6) NOT NULL,
  created_at DATETIME(6) NOT NULL,
  updated_at DATETIME(6) NOT NULL,
  INDEX idx_user_id (user_id)
);
