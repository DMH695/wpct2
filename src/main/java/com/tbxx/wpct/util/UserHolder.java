package com.tbxx.wpct.util;

import com.tbxx.wpct.dto.UserDTO;

/**
 * @Author ZXX
 * @ClassName UserHolder
 * @Description
 * @DATE 2022/9/30 11:45
 */

public class UserHolder {

    private static final ThreadLocal<UserDTO> tl = new ThreadLocal<>();

    public static void saveUser(UserDTO user) {
        tl.set(user);
    }

    public static UserDTO getUser() {
        return tl.get();
    }

    public static void removeUser() {
        tl.remove();
    }
}
