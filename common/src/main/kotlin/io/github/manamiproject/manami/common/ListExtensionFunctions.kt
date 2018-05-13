package io.github.manamiproject.manami.common

import java.security.SecureRandom

/**
 * Because simply shuffle a list once is not enough.
 */
fun <T> List<T>.randomizeOrder(): List<T> {
    val mutableList: MutableList<T> = this.toMutableList()
    mutableList.shuffle(SecureRandom())
    mutableList.shuffle(SecureRandom())
    mutableList.shuffle(SecureRandom())
    mutableList.shuffle(SecureRandom())
    return mutableList.toList()
}