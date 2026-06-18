<?php
header('Content-Type: application/json');
include 'conexion.php';

// nombre y clave llegan igual que en el login viejo de SharedPreferences
$nombre = $_POST['nombre'] ?? '';
$clave  = $_POST['clave'] ?? '';

$respuesta = array();

if (empty($nombre) || empty($clave)) {
    $respuesta['exito'] = false;
    $respuesta['mensaje'] = "Faltan datos";
    echo json_encode($respuesta);
    exit;
}

$sql = "SELECT rol FROM usuarios WHERE usuario = ? AND clave = ?";
$stmt = $conexion->prepare($sql);
$stmt->bind_param("ss", $nombre, $clave);
$stmt->execute();
$resultado = $stmt->get_result();

if ($resultado->num_rows > 0) {
    $fila = $resultado->fetch_assoc();
    $respuesta['exito'] = true;
    $respuesta['rol'] = $fila['rol']; // "Administrador" o "Trabajador"
} else {
    $respuesta['exito'] = false;
    $respuesta['mensaje'] = "Usuario o contraseña incorrectos";
}

echo json_encode($respuesta);

$stmt->close();
$conexion->close();
?>
