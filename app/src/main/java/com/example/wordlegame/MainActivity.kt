package com.example.wordlegame

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.setMargins
import androidx.core.view.setPadding

class MainActivity : AppCompatActivity() {

    private lateinit var tvHint: TextView
    private lateinit var etGuess: EditText
    private lateinit var btnSubmit: Button
    private lateinit var guessesLayout: LinearLayout
    private lateinit var spinnerDifficulty: Spinner
    private lateinit var tvScore: TextView

    private var wordIndex = 0
    private var attempt = 0
    private var score = 0
    private val maxAttempts = 6


    private val easyWordsWithHints = listOf(
        "ARRAY" to "A collection of elements.",
        "FLOAT" to "A number with decimal points.",
        "STRING" to "A sequence of characters."
    )

    private val mediumWordsWithHints = listOf(
        "CLASS" to "A blueprint for something.",
        "EVENT" to "An action or occurrence recognized by software.",
        "LOGIN" to "The process of gaining access to a system."
    )

    private val hardWordsWithHints = listOf(
        "PATCH" to "A piece of software designed to update or fix a program.",
        "QUERY" to "A request for data or information from a database.",
        "TRACE" to "To follow or track something."
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tvHint = findViewById(R.id.tvHint)
        etGuess = findViewById(R.id.etGuess)
        btnSubmit = findViewById(R.id.btnSubmit)
        guessesLayout = findViewById(R.id.guessesLayout)
        spinnerDifficulty = findViewById(R.id.spinnerDifficulty)
        tvScore = findViewById(R.id.tvScore)


        ArrayAdapter.createFromResource(
            this,
            R.array.difficulty_levels,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnerDifficulty.adapter = adapter
        }

        loadNewWord() // Load the first word after spinner setup

        // Set a listener for the spinner
        spinnerDifficulty.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                loadNewWord()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        })

        btnSubmit.setOnClickListener {
            val guess = etGuess.text.toString().uppercase()
            if (guess.length == 5) {
                checkGuess(guess)
                etGuess.text.clear()
            }
        }
    }

    private fun loadNewWord() {
        if (wordIndex >= getCurrentWordList().size) {
            Toast.makeText(this, "You've completed all the words!", Toast.LENGTH_LONG).show()
            return
        }

        // Reset the layout and attempts for the new word
        guessesLayout.removeAllViews()
        attempt = 0

        // Load the hint and new word
        val (word, hint) = getCurrentWordList()[wordIndex]
        tvHint.text = "Hint: $hint"
    }

    private fun checkGuess(guess: String) {
        if (attempt >= maxAttempts) {
            Toast.makeText(this, "No more attempts left!", Toast.LENGTH_SHORT).show()
            return
        }

        val row = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply { setMargins(8) }
        }

        val (correctWord, _) = getCurrentWordList()[wordIndex]

        for (i in guess.indices) {
            val letterView = TextView(this).apply {
                text = guess[i].toString()
                textSize = 24f
                setPadding(16)
                setBackgroundColor(getColorForLetter(guess[i], i, correctWord))
                layoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f)
                gravity = android.view.Gravity.CENTER
            }
            row.addView(letterView)
        }

        guessesLayout.addView(row)
        attempt++

        if (guess == correctWord) {

            score += 10
            tvScore.text = "Score: $score"
            Toast.makeText(this, "Correct! Moving to the next word.", Toast.LENGTH_SHORT).show()
            wordIndex++
            loadNewWord()
        } else {
            // Deduct points for incorrect guess
            score -= 2
            tvScore.text = "Score: $score"

            if (attempt >= maxAttempts) {
                Toast.makeText(this, "Game Over! The word was $correctWord.", Toast.LENGTH_LONG).show()
                wordIndex++
                loadNewWord()
            }
        }
    }

    private fun getColorForLetter(letter: Char, index: Int, correctWord: String): Int {
        return when {
            correctWord[index] == letter -> Color.GREEN // Correct position
            correctWord.contains(letter) -> Color.YELLOW // Wrong position
            else -> Color.GRAY // Incorrect letter
        }
    }

    private fun getCurrentWordList(): List<Pair<String, String>> {
        return when (spinnerDifficulty.selectedItemPosition) {
            0 -> easyWordsWithHints // Easy
            1 -> mediumWordsWithHints // Medium
            2 -> hardWordsWithHints // Hard
            else -> easyWordsWithHints // Default to easy
        }
    }
}
