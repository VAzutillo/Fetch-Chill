<?php
session_start();
header("Content-Type: application/json");
include "connection.php"; // Ensure this file contains a valid database connection

// Check if request method is POST
if ($_SERVER["REQUEST_METHOD"] !== "POST") {
    echo json_encode(["success" => false, "message" => "Invalid request method"]);
    exit;
}

// Get the input data
$input = json_decode(file_get_contents('php://input'), true);

// Validate that the email is provided
if (!isset($input["email"]) || empty(trim($input["email"]))) {
    echo json_encode(["success" => false, "message" => "Email is required"]);
    exit;
}

// Sanitize the email
$email = htmlspecialchars(strip_tags(trim($input["email"])));

// Prepare the SQL statement to check for the email
$stmt = $conn->prepare("SELECT * FROM users WHERE email = ?");
$stmt->bind_param("s", $email);
$stmt->execute();
$result = $stmt->get_result();

// Check if the email exists
if ($result->num_rows > 0) {
    echo json_encode(["success" => false, "exists" => true, "message" => "The email has already been taken."]);
} else {
    echo json_encode(["success" => true, "exists" => false, "message" => "Email is available."]);
}

// Close the statement and database connection
$stmt->close();
$conn->close();
?>