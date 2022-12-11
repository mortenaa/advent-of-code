package no.morten.advent.util

import java.io.File

fun readResourceFile(filename: String): String =
        File("advent/src/main/resources/${filename}").readText()