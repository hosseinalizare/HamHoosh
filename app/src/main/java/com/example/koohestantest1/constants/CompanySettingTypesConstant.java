package com.example.koohestantest1.constants;

import java.util.LinkedHashMap;
import java.util.Map;

import com.example.koohestantest1.classDirectory.BaseCodeClass;

public class CompanySettingTypesConstant {
    // this is check for git
    private static final Map<Integer, String> mapGroupTypes = new LinkedHashMap<>();

    static {
        mapGroupTypes.put(0, "عمومی");
        mapGroupTypes.put(1, "محصولات");
        mapGroupTypes.put(2, "کارمندان");
        mapGroupTypes.put(3, "سفارشات");
        mapGroupTypes.put(4, "نمایش های گرافیکی");
        mapGroupTypes.put(5, "صف  انتشار");
        mapGroupTypes.put(6, "پیام ها");
        mapGroupTypes.put(7, "پیشرفته");
        mapGroupTypes.put(8, "زمان تحویل کالا");
        mapGroupTypes.put(9, "زمان فعالیت فروشگاه");
        mapGroupTypes.put(10, "اطلاعات تماس");
        mapGroupTypes.put(11, "پروفایل");
        mapGroupTypes.put(12, "اطلاعات مالی");
        mapGroupTypes.put(13, "ادرس فروشگاه");
        mapGroupTypes.put(14, "غیر قابل نمایش");
    }

    public static String getGroupTypeTitle(int index) {
        return mapGroupTypes.get(index);
    }

    public static int getGroupTypeKey(String value) {
        for (Map.Entry<Integer, String> map :
                mapGroupTypes.entrySet()) {
            if (map.getValue().equals(value))
                return map.getKey();
        }
        return -1;
    }

    public static int getValueType(BaseCodeClass.variableType variableType) {
        return variableType.getValue();
    }
}
