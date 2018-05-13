package io.github.manamiproject.manami.common

import java.nio.charset.Charset
import java.nio.charset.StandardCharsets
import java.nio.file.*
import java.nio.file.attribute.FileAttribute
import java.util.stream.Stream

fun Path.isDirectory(): Boolean {
    return Files.isDirectory(this)
}

fun Path.isNotDirectory(): Boolean {
    return !this.isDirectory()
}

fun Path.exists(): Boolean {
    return Files.exists(this)
}

fun Path.notExists(): Boolean {
    return !this.exists()
}

fun Path.isRegularFile(vararg linkOption: LinkOption): Boolean {
    return Files.isRegularFile(this, *linkOption)
}

fun Path.readAllLines(charset: Charset = StandardCharsets.UTF_8): List<String> {
    return Files.readAllLines(this, charset)
}

fun Path.createDirectory(vararg fileAttribute: FileAttribute<*>) {
    Files.createDirectory(this, *fileAttribute)
}

fun Path.createDirectories(vararg fileAttribute: FileAttribute<*>) {
    Files.createDirectories(this, *fileAttribute)
}

fun Path.createFile(vararg fileAttribute: FileAttribute<*>) {
    Files.createFile(this, *fileAttribute)
}

fun Path.write(lines: List<String>, charset: Charset = StandardCharsets.UTF_8, vararg options: OpenOption) {
    Files.write(this, lines, charset, *options)
}

fun Path.walk(vararg options: FileVisitOption): Stream<Path> {
    return Files.walk(this, *options)
}