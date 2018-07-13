package io.github.manamiproject.manami.common

import java.io.BufferedReader
import java.nio.charset.Charset
import java.nio.charset.StandardCharsets.*
import java.nio.file.*
import java.nio.file.attribute.FileAttribute
import java.util.stream.Stream

// mapped functions
fun Path.isDirectory(): Boolean = Files.isDirectory(this)

fun Path.isNotDirectory(): Boolean = !this.isDirectory()

fun Path.exists(): Boolean = Files.exists(this)

fun Path.notExists(): Boolean = !this.exists()

fun Path.isRegularFile(vararg linkOption: LinkOption): Boolean = Files.isRegularFile(this, *linkOption)

fun Path.readAllLines(charset: Charset = UTF_8): List<String> = Files.readAllLines(this, charset)

fun Path.createDirectory(vararg fileAttribute: FileAttribute<*>): Path = Files.createDirectory(this, *fileAttribute)

fun Path.createDirectories(vararg fileAttribute: FileAttribute<*>): Path = Files.createDirectories(this, *fileAttribute)

fun Path.createFile(vararg fileAttribute: FileAttribute<*>): Path = Files.createFile(this, *fileAttribute)

fun Path.write(lines: List<String>, charset: Charset = UTF_8, vararg options: OpenOption): Path = Files.write(this, lines, charset, *options)

fun Path.walk(vararg options: FileVisitOption): Stream<Path> = Files.walk(this, *options)

fun Path.deleteIfExists(): Boolean = Files.deleteIfExists(this)

fun Path.readAllBytes(): ByteArray = Files.readAllBytes(this)

fun Path.list(): Stream<Path> = Files.list(this)

fun Path.copy(target: Path, vararg option: CopyOption): Path = Files.copy(this, target, *option)

fun Path.newDirectoryStream(): DirectoryStream<Path> = Files.newDirectoryStream(this)

fun Path.bufferedReader(charset: Charset = UTF_8): BufferedReader = this.toFile().bufferedReader(charset)


// new convenience functions from here on
fun Path.isValidFile(vararg linkOption: LinkOption): Boolean = exists() && isRegularFile(*linkOption)