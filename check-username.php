<?php
session_start();

// Set JSON header before any output
header("Content-Type: application/json");
header("Access-Control-Allow-Origin: *"); // Allow cross-origin requests if needed
header("Access-Control-Allow-Methods: POST");
header("Access-Control-Allow-Headers: Content-Type");

// Include the database connection
include "connection.php"; 

// Ensure the request is a POST request
if ($_SERVER["REQUEST_METHOD"] !== "POST") {
    echo json_encode(["success" => false, "message" => "Invalid request method."]);
    exit();
}

// Capture request body as JSON and validate it
$input = json_decode(file_get_contents('php://input'), true);
if (!is_array($input) || !isset($input["username"])) {
    echo json_encode(["success" => false, "message" => "Invalid JSON input."]);
    exit();
}

// Validate and sanitize username input
$username = trim($input["username"]);
if (empty($username)) {
    echo json_encode(["success" => false, "message" => "Username is required."]);
    exit();
}

// Sanitize the username to prevent XSS
$username = htmlspecialchars(strip_tags($username));

try {
    // Prepare and execute the SQL statement
    $stmt = $conn->prepare("SELECT 1 FROM users WHERE username = ?");
    if (!$stmt) {
        throw new Exception("Failed to prepare SQL statement: " . $conn->error);
    }
    
    $stmt->bind_param("s", $username);
    $stmt->execute();
    $stmt->store_result();

    // Check if the username exists
    if ($stmt->num_rows > 0) {
        echo json_encode(["success" => true, "exists" => true, "message" => "The username has already been taken."]);
    } else {
        echo json_encode(["success" => true, "exists" => false, "message" => "Username available."]);
    }

    $stmt->close();
} catch (Exception $e) {
    echo json_encode(["success" => false, "message" => "Database error: " . $e->getMessage()]);
}

// Close the database connection
$conn->close();
exit();
?>