<?php
session_start(); // Start the session

header("Content-Type: application/json"); // Set the content type to JSON
include "connection.php"; // Include the database connection

// Check if the user is logged in
if (!isset($_SESSION['id'])) {
    echo json_encode(["success" => false, "message" => "User  not logged in"]);
    exit;
}

// Check if request method is POST
if ($_SERVER["REQUEST_METHOD"] !== "POST") {
    echo json_encode(["success" => false, "message" => "Invalid request method"]);
    exit;
}

// Validate required fields
$requiredFields = ["owner_name", "pet_name", "breed", "age"];
foreach ($requiredFields as $field) {
    if (!isset($_POST[$field]) || empty(trim($_POST[$field]))) {
        echo json_encode(["success" => false, "message" => ucfirst(str_replace("_", " ", $field)) . " is required"]);
        exit;
    }
}

// Sanitize input data
$owner_name = htmlspecialchars(strip_tags($_POST["owner_name"]));
$pet_name = htmlspecialchars(strip_tags($_POST["pet_name"]));
$breed = htmlspecialchars(strip_tags($_POST["breed"]));
$age = (int) $_POST["age"]; // Ensure age is stored as an integer

$profile_image = null;

// Handle image upload if provided
if (isset($_FILES["profile_image"]) && $_FILES["profile_image"]["error"] === 0) {
    $targetDir = "uploads/"; // Ensure this folder exists and is writable
    if (!file_exists($targetDir)) {
        mkdir($targetDir, 0777, true);
    }

    $fileName = time() . "_" . basename($_FILES["profile_image"]["name"]);
    $targetFilePath = $targetDir . "/" . $fileName; // Added "/" to ensure correct path
    $fileType = strtolower(pathinfo($targetFilePath, PATHINFO_EXTENSION));

    // Allowed file types
    $allowedTypes = ["jpg", "jpeg", "png", "gif"];
    if (!in_array($fileType, $allowedTypes)) {
        echo json_encode(["success" => false, "message" => "Invalid image format. Allowed: JPG, JPEG, PNG, GIF"]);
        exit;
    }

    // Limit file size (e.g., 5MB)
    if ($_FILES["profile_image"]["size"] > 5 * 1024 * 1024) {
        echo json_encode(["success" => false, "message" => "File size exceeds 5MB limit"]);
        exit;
    }

    // Move uploaded file
    if (move_uploaded_file($_FILES["profile_image"]["tmp_name"], $targetFilePath)) {
        $profile_image = $fileName; // Store only the filename
    } else {
        echo json_encode(["success" => false, "message" => "Error uploading image"]);
        exit;
    }
}

// Get the user ID from the session
$userId = $_SESSION['id'];

// Insert or update data into the database
$stmt = $conn->prepare("INSERT INTO user_profile (owner_name, pet_name, breed, age, profile_image, user_id) 

VALUES (?, ?, ?, ?, ?, ?) ON DUPLICATE KEY UPDATE owner_name=?, pet_name=?, breed=?, age=?, profile_image=?");
$stmt->bind_param("ssssssiisss", 
$owner_name, 
$pet_name, 
$breed, 
$age, 
$profile_image, 
$userId, 
$owner_name, 
$pet_name, 
$breed, 
$age, 
$profile_image);

if ($stmt->execute()) {
    echo json_encode([
        "success" => true,
        "message" => "Profile saved successfully",
        "image_url" => $profile_image ? "http://192.168.100.18/fetch_chill/uploads/" . $profile_image : null
    ]);
} else {
    echo json_encode(["success" => false, "message" => "Database error: " . $stmt->error]);
}

// Close statement and database connection
$stmt->close();
$conn->close();
?>