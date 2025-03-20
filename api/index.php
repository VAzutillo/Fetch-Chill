<?php
header("Content-Type: application/json");

require_once '../controller/appointmentcontroller.php';
//require_once '../controller/petrecordscontroller.php';

$requestMethod = $_SERVER['REQUEST_METHOD'];
$uri = $_SERVER['REQUEST_URI'];
$contentType = $_SERVER['CONTENT_TYPE'] ?? '';

if (strpos($contentType, 'application/json') === false) {
    echo json_encode(['success' => false, 'message' => 'Invalid JSON content type.']);
    exit;
}

$input = json_decode(file_get_contents('php://input'), true);
$appointmentController = new AppointmentController();
//$petrecordsController = new PetRecordsController();

// Handle appointments
function handleAppointments($appointmentController, $requestMethod, $uri, $input) {
    switch ($requestMethod) {
        case 'GET':
            if (preg_match('/\/appointment\/(\d+)/', $uri, $matches)) {
                $appointmentController->GetAppointment($matches[1]);
            } elseif (preg_match('/\/appointment/', $uri)) {
                $appointmentController->GetAllAppointments();
            } else {
                echo json_encode(['success' => false, 'message' => 'Invalid appointment endpoint']);
            }
            break;
        case 'POST':
            if (preg_match('/\/appointment/', $uri)) {
                $appointmentController->CreateAppointment($input);
            } else {
                echo json_encode(['success' => false, 'message' => 'Invalid endpoint']);
            }
            break;
        case 'PATCH':
            if (preg_match('/\/appointment\/(\d+)/', $uri, $matches)) {
                $appointmentController->UpdateAppointmentsStatus($matches[1], $input);
            } else {
                echo json_encode(['success' => false, 'message' => 'Invalid endpoint']);
            }
            break;
        default:
            echo json_encode(['success' => false, 'message' => 'Invalid request method']);
    }
}

// Handle pet records
function handlePetRecords($petrecordsController, $requestMethod, $uri, $input) {
    switch ($requestMethod) {
        case "POST":
            if (preg_match('/\/petrecords/', $uri)) {
                $petrecordsController->createPet($input);
            } else {
                echo json_encode(['success' => false, 'message' => 'Invalid endpoint']);
            }
            break;
        case "PUT":
            if (preg_match('/\/petrecords\/(\d+)/', $uri, $matches)) {
                $petrecordsController->updatePet($matches[1], $input);
            } else {
                echo json_encode(['success' => false, 'message' => 'Invalid endpoint']);
            }
            break;
        case "DELETE":
            if (preg_match('/\/petrecords\/(\d+)/', $uri, $matches)) {
                $petrecordsController->deletePet($matches[1]);
            } else {
                echo json_encode(['success' => false, 'message' => 'Invalid endpoint']);
            }
            break;
        case "GET":
            if (isset($_GET['ownername'])) {
                $petrecordsController->searchPetByOwner($_GET['ownername']);
            } elseif (preg_match('/\/petrecords\/(\d+)/', $uri, $matches)) {
                $petrecordsController->getPet($matches[1]);
            } else {
                $petrecordsController->getAllPets();
            }
            break;
        default:
            echo json_encode(['success' => false, 'message' => 'Invalid request method']);
            break;
    }
}

// Route the request
if (preg_match('/\/appointment/', $uri)) {
    handleAppointments($appointmentController, $requestMethod, $uri, $input);
} elseif (preg_match('/\/petrecords/', $uri)) {
    handlePetRecords($petrecordsController, $requestMethod, $uri, $input);
} else {
    echo json_encode(['success' => false, 'message' => 'Invalid endpoint']);
}
?>