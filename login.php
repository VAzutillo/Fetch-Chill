<?php
session_start();

header("Content-Type: application/json", true, 200);
include "connection.php"; 

$input = json_decode(file_get_contents('php://input'), true);


if ($_SERVER["REQUEST_METHOD"] == "POST") {
    
    if (isset($input["email"]) && isset($input["password"]) && 
        !empty($input["email"]) && !empty($input["password"])) {

        $email = $input["email"];
        $password = $input["password"];

        
        $stmt = $conn->prepare("SELECT id, name, password FROM users WHERE email = ?");
        $stmt->bind_param("s", $email);
        $stmt->execute();
        $stmt->store_result();

        
        if ($stmt->num_rows > 0) {
            $stmt->bind_result($id, $username, $hashed_password);
            $stmt->fetch();

            
            if (password_verify($password, $hashed_password)) {
                $_SESSION["id"] = $id;
                $_SESSION["name"] = $username;

                echo json_encode(["success" => true, "message" => "Login successful!"]);
            } else {
                echo json_encode(["success" => false, "message" => "Invalid credentials"]);
            }
        } else {
            echo json_encode(["success" => false, "message" => "No user found with that email"]);
        }

        
        $stmt->close();
    } else {
        echo json_encode(["success" => false, "message" => "Email and password are required"]);
    }
} else {
    echo json_encode(["success" => false, "message" => "Invalid request method"]);
}


$conn->close();
?>