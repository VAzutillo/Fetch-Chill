<?php
class PetDatabase {
    private static $instance = null;
    private $conn;
    private $host = "localhost";
    private $db_name = "fetch_chill_db";
    private $db_user = "root";
    private $db_pass = "";

    // Singleton Pattern - Private constructor
    private function __construct() {
        // Improved error reporting for debugging
        mysqli_report(MYSQLI_REPORT_ERROR | MYSQLI_REPORT_STRICT);

        try {
            // Create connection with exception handling
            $this->conn = new mysqli($this->host, $this->db_user, $this->db_pass, $this->db_name);
            $this->conn->set_charset("utf8mb4"); // Set character encoding
        } catch (Exception $e) {
            // Return a JSON error response instead of exposing raw errors
            header("Content-Type: application/json");
            echo json_encode(["success" => false, "message" => "Database connection failed.", "error" => $e->getMessage()]);
            exit();
        }
    }

    // Get single instance of Database connection
    public static function getInstance() {
        if (self::$instance === null) {
            self::$instance = new PetDatabase();
        }
        return self::$instance->conn;
    }
}
?>