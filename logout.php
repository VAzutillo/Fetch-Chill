<?php
session_start(); // Start the session

header("Content-Type: application/json", true, 200); // Set the content type to JSON

// Check if the user is logged in by checking the session variable
if (isset($_SESSION['id'])) {
    // Clear session variables
    unset($_SESSION['id']);
    unset($_SESSION['name']); // Clear other session variables if needed

    // Destroy the session
    session_destroy();

    // Return a success response
    echo json_encode(["success" => true, "message" => "Logged out successfully"]);
} else {
    // Return an error response if the user is not logged in
    echo json_encode(["success" => false, "message" => "No user is logged in"]);
}
?>