package ru.invictus.mystories.servlets;

public enum FileType {
    PDF("content"),
    JPEG("image");

    private String type;

    FileType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
