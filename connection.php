<?php
$host = "localhost";
$db_user = "root";
$db_pass = "";
$db_name = "fetch_chill_db";

// Improved error reporting for debugging
mysqli_report(MYSQLI_REPORT_ERROR | MYSQLI_REPORT_STRICT);

try {
    // Create connection with exception handling
    $conn = new mysqli($host, $db_user, $db_pass, $db_name);
    $conn->set_charset("utf8mb4"); // Set character encoding to avoid issues with special characters
} catch (Exception $e) {
    // Return a JSON error response instead of exposing raw errors
    header("Content-Type: application/json");
    echo json_encode(["success" => false, "message" => "Database connection failed.", "error" => $e->getMessage()]);
    exit();
}
?>
