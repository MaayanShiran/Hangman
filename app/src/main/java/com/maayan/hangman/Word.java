package com.maayan.hangman;

public class Word {

    public String name;
    public String category;

    public Word()
    {

    }

    public int getWordLength()
    {
        return name.length();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
