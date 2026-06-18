<?php
header('Content-Type: application/json');
include 'conexion.php';

$id = $_POST['id'] ?? '';

$respuesta = array();

if (empty($id)) {
    $respuesta['exito'] = false;
    $respuesta['mensaje'] = "Falta el id de la guitarra";
    echo json_encode($respuesta);
    exit;
}

$sql = "DELETE FROM guitarras WHERE id=?";
$stmt = $conexion->prepare($sql);
$stmt->bind_param("i", $id);

if ($stmt->execute()) {
    $respuesta['exito'] = true;
} else {
    $respuesta['exito'] = false;
    $respuesta['mensaje'] = "Error al eliminar: " . $stmt->error;
}

echo json_encode($respuesta);

$stmt->close();
$conexion->close();
?>
