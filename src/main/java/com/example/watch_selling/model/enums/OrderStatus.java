package com.example.watch_selling.model.enums;

public enum OrderStatus {
    PENDING("Chờ duyệt"),
    PREPARING("Đang chuẩn bị hàng"),
    ON_DELIVERY("Đang giao hàng"),
    COMPLETED("Hoàn thành"),
    CANCELED("Đã hủy");

    private final String value;

    OrderStatus(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return this.value;
    }

    public static Boolean isValid(String value) {
        for (OrderStatus status: OrderStatus.values()) {
            if (status.toString().equalsIgnoreCase(value)) {
                return true;
            }
        }
        return false;
    }
}
