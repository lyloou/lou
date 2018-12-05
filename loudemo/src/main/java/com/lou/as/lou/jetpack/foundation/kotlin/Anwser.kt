package com.lou.`as`.lou.jetpack.foundation.kotlin

enum class Answer {
    YES,
    NO,

    MAYBE {
        override fun toString() = """¯\_(ツ)_/¯"""
    }
}

fun main(args: Array<String>) {
    println(Answer.NO)
    println(Answer.YES)
    println(Answer.MAYBE)
}