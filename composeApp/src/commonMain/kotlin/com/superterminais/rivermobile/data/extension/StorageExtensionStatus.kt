package com.superterminais.rivermobile.data.extension

sealed class StorageExtensionStatus(val value: String, val description: String) {
    data object Approved : StorageExtensionStatus("APROVADO", "Aprovado") {
        override fun toString(): String {
            return description;
        }
    }

    data object Pending : StorageExtensionStatus("PENDENTE", "Pendente") {
        override fun toString(): String {
            return description;
        }
    }

    data object Rejected : StorageExtensionStatus("REPROVADO", "Reprovado") {
        override fun toString(): String {
            return description;
        }
    }

    companion object {
        val values = listOf(Pending, Approved, Rejected)
    }
}