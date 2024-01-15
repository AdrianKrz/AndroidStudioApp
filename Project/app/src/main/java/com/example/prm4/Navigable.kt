package com.example.prm4

interface Navigable {
    enum class Destination {
        List, Add, Edit, Info
    }
    fun navigate(to: Destination)
}