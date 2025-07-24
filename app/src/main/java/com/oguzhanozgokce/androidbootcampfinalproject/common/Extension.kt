package com.oguzhanozgokce.androidbootcampfinalproject.common

fun String?.orEmpty(): String = this ?: ""
fun Int?.orZero(): Int = this ?: 0
fun Long?.orZero(): Long = this ?: 0L
fun Boolean?.orFalse(): Boolean = this ?: false
fun Boolean?.orTrue(): Boolean = this ?: true