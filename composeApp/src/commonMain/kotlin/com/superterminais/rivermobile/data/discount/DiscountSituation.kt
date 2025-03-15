package com.superterminais.rivermobile.data.discount

sealed class DiscountSituation(val value: String, val description: String) {
    data object Approved : DiscountSituation("APROVADO", "Aprovado") {
        override fun toString(): String {
            return description
        }
    }
    data object Pending : DiscountSituation("PENDENTE", "Pendente") {
        override fun toString(): String {
            return description
        }
    }
    data object Rejected : DiscountSituation("REPROVADO", "Reprovado") {
        override fun toString(): String {
            return description
        }
    }

    companion object {
        val values = listOf(Pending, Approved, Rejected)

        fun fromValue(value: String?): DiscountSituation {
            return when (value) {
                Approved.value -> Approved
                Rejected.value -> Rejected
                else -> Pending
            }
        }

        fun fromDescription(description: String?): DiscountSituation {
            return when (description) {
                Approved.description -> Approved
                Rejected.description -> Rejected
                else -> Pending
            }
        }
    }
}