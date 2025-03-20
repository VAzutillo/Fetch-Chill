<?php
session_start(); // Start the session

header("Content-Type: application/json"); // Set the content type to JSON
include "connection.php"; // Include the database connection

// Check if the user is logged in
if (!isset($_SESSION['id'])) {
    echo json_encode(["status" => "error", "message" => "User  not logged in"]);
    exit;
}

// Fetch the profile for the logged-in user
$userId = $_SESSION['id']; // Get the user ID from the session
$sql = "SELECT owner_name, pet_name, breed, age, profile_image FROM user_profile WHERE user_id = ? LIMIT 1";
$stmt = $conn->prepare($sql);
$stmt->bind_param("i", $userId);
$stmt->execute();
$result = $stmt->get_result();

if ($result->num_rows > 0) {
    $row = $result->fetch_assoc();

    // Base URL of your server (make sure it matches your API base URL)
    $base_url = "http://192.168.100.18/fetch_chill/";

    // Append the correct path for the profile image
    $row['image_url'] = (!empty($row['profile_image'])) ? $base_url . "uploads/" . $row['profile_image'] : null;

    echo json_encode([
        "status" => "success",
        "profile" => $row
    ]);
} else {
    echo json_encode([
        "status" => "error",
        "message" => "No profile found"
    ]);
}

$stmt->close(); // Close the statement
$conn->close(); // Close the database connection
?>