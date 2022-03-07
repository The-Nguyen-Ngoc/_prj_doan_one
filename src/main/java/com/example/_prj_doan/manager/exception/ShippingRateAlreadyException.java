package com.example._prj_doan.manager.exception;

public class ShippingRateAlreadyException extends Exception {
    public ShippingRateAlreadyException(String shipping_rate_already_exists) {
        super(shipping_rate_already_exists);
    }
}
