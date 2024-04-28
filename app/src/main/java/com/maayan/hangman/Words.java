package com.maayan.hangman;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Words {

    private List<Word> words;

    public Words() {
        // Initialize the list of words
        words = new ArrayList<>();
        // Add words to the list
        initializeWords();
    }

    private void initializeWords() {
        Word word1 = new Word();
        word1.setName("Chair");
        word1.setCategory("Furniture");
        words.add(word1);

        Word word2 = new Word();
        word2.setName("Israel");
        word2.setCategory("Countries");
        words.add(word2);

        Word word3 = new Word();
        word3.setName("Elephant");
        word3.setCategory("Animals");
        words.add(word3);

        Word word4 = new Word();
        word4.setName("Pizza");
        word4.setCategory("Food");
        words.add(word4);

        Word word5 = new Word();
        word5.setName("Computer");
        word5.setCategory("Technology");
        words.add(word5);

        Word word6 = new Word();
        word6.setName("Guitar");
        word6.setCategory("Music");
        words.add(word6);

        Word word7 = new Word();
        word7.setName("Mountain");
        word7.setCategory("Geography");
        words.add(word7);

        Word word8 = new Word();
        word8.setName("Sunflower");
        word8.setCategory("Plants");
        words.add(word8);

        Word word9 = new Word();
        word9.setName("Soccer");
        word9.setCategory("Sports");
        words.add(word9);

        Word word10 = new Word();
        word10.setName("Book");
        word10.setCategory("Literature");
        words.add(word10);

        // Add more words as needed
    }


    public List<Word> getWords() {
        return words;
    }

    public String getRandomWord() {
        if (words != null && !words.isEmpty()) {
            Random random = new Random();
            int randomIndex = random.nextInt(words.size());
          //  Log.d("MSG33", ""+words.get(randomIndex).name);
            return words.get(randomIndex).name;
        } else {
            return null; // Return null if the list is empty
        }
    }
}
