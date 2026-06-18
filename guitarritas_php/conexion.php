<?php
// Datos por default de XAMPP: usuario root sin contraseña
$host = "localhost";
$usuario = "root";
$clave = "";
$bd = "chijeu_db";

$conexion = new mysqli($host, $usuario, $clave, $bd);

// Si algo sale mal aquí, ningún otro php va a poder seguir, así que cortamos de una vez
if ($conexion->connect_error) {
    header('Content-Type: application/json');
    echo json_encode([
        "exito" => false,
        "mensaje" => "Error de conexión: " . $conexion->connect_error
    ]);
    exit;
}

$conexion->set_charset("utf8");
?>
