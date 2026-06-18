<?php
header('Content-Type: application/json');
include 'conexion.php';

// Mismos nombres que manda Volley desde MainActivity
$modelo         = $_POST['modelo'] ?? '';
$marca          = $_POST['marca'] ?? '';
$fabricante     = $_POST['fabricante'] ?? '';
$precio         = $_POST['precio'] ?? '';
$pais           = $_POST['pais'] ?? '';
$telefono       = $_POST['telefono'] ?? '';
$tipocuerpo     = $_POST['tipocuerpo'] ?? '';
$tipopastillas  = $_POST['tipopastillas'] ?? '';
$puente         = $_POST['puente'] ?? '';
$madera         = $_POST['madera'] ?? '';
$cuerdas        = $_POST['cuerdas'] ?? '';

$respuesta = array();

if (empty($modelo) || empty($marca) || empty($fabricante) || empty($precio) || empty($pais) || empty($telefono)) {
    $respuesta['exito'] = false;
    $respuesta['mensaje'] = "Faltan datos obligatorios";
    echo json_encode($respuesta);
    exit;
}

$sql = "INSERT INTO guitarras (modelo, marca, fabricante, precio, pais, telefono, tipocuerpo, tipopastillas, puente, madera, cuerdas)
        VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
$stmt = $conexion->prepare($sql);
$stmt->bind_param(
    "sssdsssssss",
    $modelo, $marca, $fabricante, $precio, $pais, $telefono,
    $tipocuerpo, $tipopastillas, $puente, $madera, $cuerdas
);

if ($stmt->execute()) {
    $respuesta['exito'] = true;
    $respuesta['id'] = $conexion->insert_id;
} else {
    $respuesta['exito'] = false;
    $respuesta['mensaje'] = "Error al guardar: " . $stmt->error;
}

echo json_encode($respuesta);

$stmt->close();
$conexion->close();
?>
