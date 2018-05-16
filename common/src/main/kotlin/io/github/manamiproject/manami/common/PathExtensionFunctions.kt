package io.github.manamiproject.manami.common

import java.io.BufferedReader
import java.nio.charset.Charset
import java.nio.charset.StandardCharsets
import java.nio.charset.StandardCharsets.*
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

fun Path.readAllLines(charset: Charset = UTF_8): List<String> {
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

fun Path.write(lines: List<String>, charset: Charset = UTF_8, vararg options: OpenOption) {
    Files.write(this, lines, charset, *options)
}

fun Path.walk(vararg options: FileVisitOption): Stream<Path> {
    return Files.walk(this, *options)
}

fun Path.deleteIfExists() {
    Files.deleteIfExists(this)
}

fun Path.readAllBytes(): ByteArray {
    return Files.readAllBytes(this)
}

fun Path.list(): Stream<Path> {
    return Files.list(this)
}

fun Path.copy(target: Path, vararg option: CopyOption): Path {
    return Files.copy(this, target, *option)
}

fun Path.newDirectoryStream(): DirectoryStream<Path> {
    return Files.newDirectoryStream(this)
}

fun Path.bufferedReader(charset: Charset = UTF_8): BufferedReader {
    return this.toFile().bufferedReader(charset)
}