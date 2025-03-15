package com.superterminais.rivermobile.utils

object StringFormatUtils {
    fun currencyFormat(value: Double): String {
        return "R$ $value"
//        val format: NumberFormat = NumberFormat.getCurrencyInstance()
//        format.maximumFractionDigits = 2
//        format.currency = Currency.getInstance("BRL")
//        return format.format(value)
    }

    fun percentageFormat(value: Double): String {
        return "$value%"
    }

    fun cnpjFormat(cnpj: String): String {
        if (cnpj.length != 14) {
            return cnpj
        }
        return "${cnpj.substring(0, 2)}.${cnpj.substring(2, 5)}.${
            cnpj.substring(
                5, 8
            )
        }/${cnpj.substring(8, 12)}-${cnpj.substring(12, 14)}"
    }

    fun cpfFormat(cpf: String): String {
        if (cpf.length != 11) {
            return cpf
        }
        return "${cpf.substring(0, 3)}.${cpf.substring(3, 6)}.${
            cpf.substring(
                6, 9
            )
        }-${cpf.substring(9, 11)}"
    }
}