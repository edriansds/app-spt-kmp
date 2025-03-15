package com.superterminais.rivermobile

import platform.UIKit.UIDevice

class IOSPlatform: Plataform {
    override val name: String = UIDevice.currentDevice.systemName() + " " + UIDevice.currentDevice.systemVersion
}

actual fun getPlataform(): Plataform = IOSPlatform()
