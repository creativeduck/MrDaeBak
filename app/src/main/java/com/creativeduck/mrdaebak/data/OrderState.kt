package com.creativeduck.mrdaebak.data

enum class OrderState {
    // 미접수, 조리중, 조리 완료, 배달중, 배달 완료, 주문 취소
    NOT_RECEIVED, COOKING, FINISH_COOK, DELIVERING, DELIVERED, CANCELED
}