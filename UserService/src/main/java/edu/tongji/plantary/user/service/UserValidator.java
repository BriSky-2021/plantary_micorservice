package edu.tongji.plantary.user.service;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class UserValidator {
    public static void validateName(String name) {
        Objects.requireNonNull(name, "Name cannot be null.");
        if (name.length() < 1 || name.length() > 30) {
            throw new IllegalArgumentException("Name length must be between 1 and 30.");
        }
    }

    public static void validateAge(Integer age) {
        Objects.requireNonNull(age, "Age cannot be null.");
        if (age < 0 || age > 200) {
            throw new IllegalArgumentException("Age must be between 0 and 200.");
        }
    }

    public static void validatePassword(String passwd) {
        Objects.requireNonNull(passwd, "Password cannot be null.");
        if (passwd.length() < 4 || passwd.length() > 50) {
            throw new IllegalArgumentException("Password length must be between 4 and 50.");
        }
    }

    public static void validatePhone(String phone) {
        Objects.requireNonNull(phone, "Phone number cannot be null.");
        if (!phone.matches("\\d{11}")) {
            throw new IllegalArgumentException("Phone number must be 11 digits.");
        }
    }

    public static void validateBracelet(String bracelet) {
        Objects.requireNonNull(bracelet, "Bracelet cannot be null.");
        if (bracelet.isEmpty()) {
            throw new IllegalArgumentException("Bracelet cannot be empty.");
        }
    }

    public static void validateSex(String sex) {
        Objects.requireNonNull(sex, "Sex cannot be null.");
        List<String> availableSexes = Arrays.asList("男", "女", "非二元性别");
        if (!availableSexes.contains(sex)) {
            throw new IllegalArgumentException("Invalid sex. Available options are: 男, 女, 非二元性别.");
        }
    }

    public static void validateAvatar(String avatar) {
        Objects.requireNonNull(avatar, "Avatar cannot be null.");
        if (avatar.isEmpty()) {
            throw new IllegalArgumentException("Avatar cannot be empty.");
        }
        if (avatar.length() >= 2083) {
            throw new IllegalArgumentException("Avatar length must be less than 2083.");
        }
    }

    public static void validateWeight(Double weight) {
        Objects.requireNonNull(weight, "Weight cannot be null.");
        if (weight < 0 || weight > 500) {
            throw new IllegalArgumentException("Weight must be between 0 and 500.");
        }
    }

    public static void validateHeight(Double height) {
        Objects.requireNonNull(height, "Height cannot be null.");
        if (height < 0 || height > 300) {
            throw new IllegalArgumentException("Height must be between 0 and 300.");
        }
    }
}
