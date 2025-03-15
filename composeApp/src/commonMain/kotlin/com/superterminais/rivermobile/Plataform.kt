package com.superterminais.rivermobile

interface Plataform {
    val name: String
}

expect fun getPlataform(): Plataform