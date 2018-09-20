package jdroplet.enums;

public enum PasswordFormat {

    CLEARTEXT,
    MD5HASH,
    SHA1HASH,
    ENCRYPTED;

    public static PasswordFormat get(String name) {
        return Enum.valueOf(PasswordFormat.class, name.trim());
    }
}