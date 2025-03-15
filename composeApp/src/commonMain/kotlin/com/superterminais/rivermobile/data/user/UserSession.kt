package com.superterminais.rivermobile.data.user

object UserSession {
    private var token: String? = null

    private var id: Int = 0

    private var permissions: List<String> = emptyList()

    private var mainPermission: String = ""

    private var name: String = ""

    fun initialize(id: Int, token: String, name: String = "", permissions: List<String> = emptyList(), mainPermission: String = "") {
        UserSession.id = id
        UserSession.token = token
        UserSession.name = name
        UserSession.permissions = permissions
        UserSession.mainPermission = mainPermission
    }

    fun getId(): Int {
        return id
    }

    fun getToken(): String? {
        return token
    }

    fun getName(): String {
        if (name.isEmpty()) {
            return "Usuário"
        }
        return name
    }

    fun getMainPermission(): String {
        return mainPermission
    }

    fun clear() {
        id = 0
        token = null
        permissions = emptyList()
        mainPermission = ""
    }
}