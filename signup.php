<?php
session_start(); // Start the session

header("Content-Type: application/json"); // Set the content type to JSON

include "connection.php"; // Include the database connection

// Get the input data (assuming it's a POST request)
$input = json_decode(file_get_contents('php://input'), true);

// Log the input data for debugging
error_log(print_r($input, true));

// Validate input fields
if (isset($input["name"], $input["email"], $input["password"]) && 
    !empty($input["name"]) && !empty($input["email"]) && !empty($input["password"])) {

    $name = $input["name"];
    $email = $input["email"];
    $password = $input["password"];
    
    // Validate email domain
    $allowedDomains = ['gmail.com', 'yahoo.com'];
    $emailDomain = substr(strrchr($email, "@"), 1); // Get the domain part of the email

    if (!in_array($emailDomain, $allowedDomains)) {
        echo json_encode(["success" => false, "message" => "Email must be from one of the following domains: " . implode(", ", $allowedDomains)]);
        http_response_code(400); // Bad Request
        return;
    }

    // Check if the email already exists
    $userCheck = $conn->prepare("SELECT * FROM users WHERE email = ?");
    $userCheck->bind_param("s", $email);
    $userCheck->execute();
    $result = $userCheck->get_result();

    if ($result->num_rows > 0) {
        echo json_encode(["success" => false, "message" => "Email already exists!"]);
        http_response_code(400); // Bad Request
        return;
    } 

    // Hash the password
    $hashed_password = password_hash($password, PASSWORD_DEFAULT);

    // Insert the new user
    $stmt = $conn->prepare("INSERT INTO users (name, email, password) VALUES (?, ?, ?)");
    $stmt->bind_param("sss", $name, $email, $hashed_password);

    if ($stmt->execute()) {
        // Get the inserted user ID
        $userId = $stmt->insert_id; // Get the ID of the newly created user
        
        // Set session variables
        $_SESSION['id'] = $userId; // Store user ID in session
        $_SESSION['name'] = $name; // Store user name in session
        
        echo json_encode(["success" => true, "message" => "Registered Successfully", "user" => ["id" => $userId, "name" => $name, "email" => $email]]);
    } else {
        echo json_encode(["success" => false, "message" => "Error: " . $stmt->error]);
        http_response_code(500); // Internal Server Error
    }

    $stmt->close(); // Close the statement
} else {
    echo json_encode(["success" => false, "message" => "All fields are required"]);
    http_response_code(400); // Bad Request
}

$conn->close(); // Close the database connection
?>