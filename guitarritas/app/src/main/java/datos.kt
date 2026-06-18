package com.example.guitarritas

data class datos(
    val id : Int = 0, // lo necesitamos para poder editar/eliminar en la base de datos
    val modelo : String,
    val marca : String,
    val fabricante : String,
    val precio : Double,
    val pais : String,
    val telefono : String,
    val tipocuerpo : String,
    val tipopastillas : String,
    val puente : String,
    val madera : String,
    val cuerdas : String,
)