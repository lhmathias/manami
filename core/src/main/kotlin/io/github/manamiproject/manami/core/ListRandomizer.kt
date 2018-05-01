package io.github.manamiproject.manami.core

import java.security.SecureRandom
import java.util.Collections.shuffle

object ListRandomizer {

    fun <T> randomizeOrder(list: List<T>): List<T> {
        val mutableList = list.toMutableList()
        shuffle(mutableList, SecureRandom())
        shuffle(mutableList, SecureRandom())
        shuffle(mutableList, SecureRandom())
        shuffle(mutableList, SecureRandom())
        return mutableList.toList()
    }
}