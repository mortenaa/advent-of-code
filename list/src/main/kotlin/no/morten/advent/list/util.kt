package no.morten.advent.list

import java.io.File

fun readResourceFile(filename: String): String =
        File("list/src/main/resources/${filename}").readText()