package io.github.manamiproject.manami.core

import java.security.SecureRandom
import java.util.Collections.shuffle

object ListRandomizer {

    fun <T> randomizeOrder(list: List<T>): List<T> {
        val mutableList: MutableList<T> = list.toMutableList()
        mutableList.shuffle(SecureRandom())
        mutableList.shuffle(SecureRandom())
        mutableList.shuffle(SecureRandom())
        mutableList.shuffle(SecureRandom())
        return mutableList.toList()
    }
}