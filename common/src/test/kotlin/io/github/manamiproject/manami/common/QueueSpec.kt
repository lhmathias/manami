package io.github.manamiproject.manami.common

import org.assertj.core.api.Assertions.assertThat
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on

class QueueSpec : Spek({

    given("an empty queue") {
        val queue : Queue<String> = Queue()

        on("isEmpty") {
            val result = queue.isEmpty()

            it("must returns true") {
                assertThat(result).isTrue()
            }
        }

        on("size") {
            val result = queue.size()

            it("must returns 0") {
                assertThat(result).isZero()
            }
        }

        on("peek") {
            val result = queue.peek()

            it("must returns null") {
                assertThat(result).isNull()
            }
        }

        on("dequeue") {
            val result = queue.dequeue()

            it("must returns null") {
                assertThat(result).isNull()
            }
        }

        on("enqueue") {
            val result = queue.enqueue("value")

            it("must increase size") {
                assertThat(queue.size()).isOne()
            }
        }
    }

    given("a queue with two entries") {
        val queue : Queue<String> = Queue()

        val firstEntry = "initialValue"
        queue.enqueue(firstEntry)

        val secondEntry = "anotherValue"
        queue.enqueue(secondEntry)

        on("peek") {
            val result = queue.peek()

            it("must have the same size as before") {
                assertThat(queue.size()).isEqualTo(2)
            }

            it("must return the entry which has been added first") {
                assertThat(result).isEqualTo(firstEntry)
            }
        }

        on("dequeue") {
            val result = queue.dequeue()

            it("must return the entry which has been added first") {
                assertThat(result).isEqualTo(firstEntry)
            }
        }
    }
})