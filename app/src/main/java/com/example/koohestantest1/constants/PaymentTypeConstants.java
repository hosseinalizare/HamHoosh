package com.example.koohestantest1.constants;

import java.util.LinkedHashMap;
import java.util.Map;

public class PaymentTypeConstants {
    private static final Map<Integer, String> mapPaymentTypes = new LinkedHashMap<>();

    public static Map<Integer, String> getPaymentTypes() {
        mapPaymentTypes.put(1, "پرداخت حضوری");
        mapPaymentTypes.put(2, "کارت به کارت");
        mapPaymentTypes.put(3, "پرداخت به پیک");
        mapPaymentTypes.put(4, "پرداخت آنلاین");
        mapPaymentTypes.put(5, "پرداخت اقساط");
        return mapPaymentTypes;
    }
}
