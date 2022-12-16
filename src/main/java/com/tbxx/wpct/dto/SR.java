package com.tbxx.wpct.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;
import java.util.Map;

/**
 * @ClassName SR
 * @Description TODO
 * @Author ZXX
 * @DATE 2022/10/29 15:57
 */

@Accessors(chain = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SR {
    private Boolean success;
    private String msg1;
    private Object data1;
    private String msg2;
    private Object data2;

    public static SR ok(String msg1, List<?> one, String msg2, List<?> two) {
        return new SR(true, msg1, one, msg2,two);
    }
    public static SR ok(String msg1, String one, String msg2, List<?> two) {
        return new SR(true, msg1, one, msg2,two);
    }

    public static SR ok() {
        return new SR(true, null, null, null,null);
    }
    public static SR ok(Map<Object,Object> map) {
        return new SR(true, null, map, null,null);
    }

    public static SR fail(String msg2) {
        return new SR(false, msg2, null, null,null);
    }
}
