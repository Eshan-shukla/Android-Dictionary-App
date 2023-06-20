package com.example.dict;

public class Word {
    private String name;
    private String category;
    private String meaning;

    public Word(String name, String category, String meaning) {
        this.name = name;
        this.category = category;
        this.meaning = meaning;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMeaning() {
        return meaning;
    }

    public void setMeaning(String meaning) {
        this.meaning = meaning;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
