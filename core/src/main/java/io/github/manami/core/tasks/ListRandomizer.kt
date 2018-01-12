package io.github.manami.core.tasks

import java.security.SecureRandom
import java.util.Collections.shuffle

object ListRandomizer {

    fun randomizeOrder(list: MutableList<out Any>) {
        shuffle(list, SecureRandom())
        shuffle(list, SecureRandom())
        shuffle(list, SecureRandom())
        shuffle(list, SecureRandom())
    }
}