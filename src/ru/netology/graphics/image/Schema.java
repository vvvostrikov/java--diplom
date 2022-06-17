package ru.netology.graphics.image;

public class Schema implements TextColorSchema{

    char[] symbols = {'#', '$', '@', '%', '*', '+', '-', '\''};

    @Override
    public char convert(int color) {
        return symbols[color / 32];
    }
}
