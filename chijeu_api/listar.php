<?php
header('Content-Type: application/json');
include 'conexion.php';

$respuesta = array();
$guitarras = array();

$sql = "SELECT * FROM guitarras ORDER BY id DESC";
$resultado = $conexion->query($sql);

if ($resultado && $resultado->num_rows > 0) {
    while ($fila = $resultado->fetch_assoc()) {
        $guitarras[] = $fila;
    }
}

$respuesta['exito'] = true;
$respuesta['guitarras'] = $guitarras;

echo json_encode($respuesta);

$conexion->close();
?>
