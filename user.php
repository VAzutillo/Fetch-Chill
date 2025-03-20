<?php

header("Content-Type: application/json");

require_once 'pet_connection.php';
require_once 'connection.php';

class User {
    private $conn;

    public function __construct() {
        $this->conn = PetDatabase::getInstance();
    }

    // Get user by ID
    public function GetUserById($userId) {
        $stmt = $this->conn->prepare("SELECT * FROM users WHERE user_id = ?");
        if ($stmt === false) {
            throw new mysqli_sql_exception("Prepare statement failed: " . $this->conn->error);
        }
        $stmt->bind_param('i', $userId);
        if ($stmt->execute() === false) {
            throw new mysqli_sql_exception("Execute statement failed: " . $stmt->error);
        }
        $result = $stmt->get_result();
        $user = $result->fetch_assoc();
        $stmt->close();
        return $user; // Returns user data or null if not found
    }
}
?>