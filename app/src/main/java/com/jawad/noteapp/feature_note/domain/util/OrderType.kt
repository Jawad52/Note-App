package com.jawad.noteapp.feature_note.domain.util

sealed class OrderType {
    object AscendingOrderType : OrderType()
    object DescendingOrderType : OrderType()
}