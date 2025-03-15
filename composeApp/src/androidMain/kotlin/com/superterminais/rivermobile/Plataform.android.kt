package com.superterminais.rivermobile

import android.os.Build

class AndroidPlatform : Plataform {
    override val name: String = "Android ${Build.VERSION.SDK_INT}"
}

actual fun getPlataform(): Plataform = AndroidPlatform()