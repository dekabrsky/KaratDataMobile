package com.example.karatdatamobile.Models

object Titles {
    const val V = "V"
    const val M = "M"
    const val T = "T"
    const val P = "P"
    const val G = "G"
    const val C1 = "C1"
    const val C2 = "C2"
    const val C3 = "C3"
    const val C4 = "C4"
    const val NARABOTKAS = "Наработка"
    const val ERRORS = "Ошибки"
    const val T_MIN = "t_min"
    const val T_MAX = "t_max"
    const val T_DT = "t_dt"
    const val T_F = "t_f"
    const val T_EP = "t_ep"

    @JvmField var cfgCodeToTitle: HashMap<Char, String> = hashMapOf(
            '1' to V,
            '2' to M,
            '3' to T,
            '4' to P,
            '5' to G,
            '6' to C1,
            '7' to C2,
            '8' to C3,
            '9' to C4,
            'b' to NARABOTKAS,
            'c' to ERRORS
    )

    @JvmField var cfgSubCodeToTitle: HashMap<Char, String> = hashMapOf(
            '1' to T_MIN,
            '2' to T_MAX,
            '3' to T_DT,
            '4' to T_F,
            '5' to T_EP
    )

    @JvmField var titlesWithLongValues: ArrayList<String> = arrayListOf(
            NARABOTKAS,
            ERRORS,
            T_MIN, T_MAX, T_DT, T_F, T_EP
    )

    @JvmField var titlesWithFloatValues: ArrayList<String> = arrayListOf(
            V, M, P, T, G, C1, C2, C3, C4
    )
}