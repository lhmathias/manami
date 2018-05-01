package io.github.manamiproject.manami.common

class Queue<T>(list: List<T> = listOf()) {

    private var items: MutableList<T> = list.toMutableList()

    fun isEmpty(): Boolean = this.items.isEmpty()

    fun size(): Int = this.items.count()

    fun enqueue(element: T) {
        this.items.add(element)
    }

    fun dequeue(): T? {
        return if (this.isEmpty()) {
            null
        } else {
            this.items.removeAt(0)
        }
    }

    fun peek(): T? {
        return if(isEmpty()) {
            null
        } else {
            this.items[0]
        }
    }
}