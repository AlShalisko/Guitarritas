<?php
header('Content-Type: application/json');
include 'conexion.php';

$id             = $_POST['id'] ?? '';
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

if (empty($id)) {
    $respuesta['exito'] = false;
    $respuesta['mensaje'] = "Falta el id de la guitarra";
    echo json_encode($respuesta);
    exit;
}

$sql = "UPDATE guitarras
        SET modelo=?, marca=?, fabricante=?, precio=?, pais=?, telefono=?,
            tipocuerpo=?, tipopastillas=?, puente=?, madera=?, cuerdas=?
        WHERE id=?";
$stmt = $conexion->prepare($sql);
$stmt->bind_param(
    "sssdsssssssi",
    $modelo, $marca, $fabricante, $precio, $pais, $telefono,
    $tipocuerpo, $tipopastillas, $puente, $madera, $cuerdas, $id
);

if ($stmt->execute()) {
    $respuesta['exito'] = true;
} else {
    $respuesta['exito'] = false;
    $respuesta['mensaje'] = "Error al actualizar: " . $stmt->error;
}

echo json_encode($respuesta);

$stmt->close();
$conexion->close();
?>
