package com.rick.morty.models

// QuizQuestions.kt
data class QuizQuestion(
    val question: String,
    val options: List<String>,
    val answer: String
)

val quizQuestions = listOf(
    QuizQuestion(
        question = "What is the name of Rick's daughter?",
        options = listOf("Beth", "Summer", "Jessica"),
        answer = "Beth"
    ),
    QuizQuestion(
        question = "What is Morty's last name?",
        options = listOf("Smith", "Sanchez", "Johnson"),
        answer = "Smith"
    ),
    QuizQuestion(
        question = "What is the name of Rick's catchphrase?",
        options = listOf("Wubba Lubba Dub Dub", "Aw Geez", "Schwifty"),
        answer = "Wubba Lubba Dub Dub"
    ),
    QuizQuestion(
        question = "Who is Morty's sister?",
        options = listOf("Beth", "Jessica", "Summer"),
        answer = "Summer"
    ),
    QuizQuestion(
        question = "What color is Rick's portal gun?",
        options = listOf("Green", "Blue", "Red"),
        answer = "Green"
    ),
    QuizQuestion(
        question = "Which planet is Rick originally from?",
        options = listOf("Earth", "Cronenberg World", "Bird World"),
        answer = "Earth"
    ),
    QuizQuestion(
        question = "What type of scientist is Rick?",
        options = listOf("Biologist", "Physicist", "Mad Scientist"),
        answer = "Mad Scientist"
    ),
    QuizQuestion(
        question = "What is Morty's main characteristic?",
        options = listOf("Intelligence", "Naivety", "Courage"),
        answer = "Naivety"
    ),
    QuizQuestion(
        question = "What creature is Mr. Meeseeks?",
        options = listOf("Alien", "Robot", "Humanoid"),
        answer = "Humanoid"
    ),
    QuizQuestion(
        question = "What is the name of Rick's car?",
        options = listOf("The Ship", "The Rickmobile", "The Rover"),
        answer = "The Ship"
    ),
    QuizQuestion(
        question = "Who created the show 'Rick and Morty'?",
        options = listOf("Justin Roiland and Dan Harmon", "Seth MacFarlane", "Matt Groening"),
        answer = "Justin Roiland and Dan Harmon"
    ),
    QuizQuestion(
        question = "What color is Rick's hair?",
        options = listOf("Blue", "White", "Gray"),
        answer = "Blue"
    ),
    QuizQuestion(
        question = "What dimension is the original Rick from?",
        options = listOf("C-137", "D-99", "M-564"),
        answer = "C-137"
    ),
    QuizQuestion(
        question = "What is the name of the council of Ricks?",
        options = listOf("Council of Ricks", "Federation of Ricks", "Union of Ricks"),
        answer = "Council of Ricks"
    ),
    QuizQuestion(
        question = "What food does Rick love in the 'Szechuan sauce' episode?",
        options = listOf("Chicken nuggets", "French fries", "Szechuan sauce"),
        answer = "Szechuan sauce"
    ),
    QuizQuestion(
        question = "What does Rick turn himself into in a famous episode?",
        options = listOf("A pickle", "A tomato", "A potato"),
        answer = "A pickle"
    ),
    QuizQuestion(
        question = "Who is the main enemy of Rick in the series?",
        options = listOf("Birdperson", "Tammy", "Evil Morty"),
        answer = "Evil Morty"
    ),
    QuizQuestion(
        question = "What is the name of Morty's high school?",
        options = listOf("Harry Herpson High School", "Morton High", "Springfield High"),
        answer = "Harry Herpson High School"
    ),
    QuizQuestion(
        question = "Who does Summer have a crush on?",
        options = listOf("Ethan", "Jerry", "Morty"),
        answer = "Ethan"
    ),
    QuizQuestion(
        question = "What kind of animal is Squanchy?",
        options = listOf("Cat", "Dog", "Bird"),
        answer = "Cat"
    ),
    QuizQuestion(
        question = "Who is Rick's best friend?",
        options = listOf("Birdperson", "Jerry", "Morty"),
        answer = "Birdperson"
    ),
    QuizQuestion(
        question = "What does Rick frequently drink?",
        options = listOf("Beer", "Soda", "Whiskey"),
        answer = "Beer"
    ),
    QuizQuestion(
        question = "What is Morty often forced to do on adventures?",
        options = listOf("Fight aliens", "Pick up slack", "Pilot the ship"),
        answer = "Pick up slack"
    ),
    QuizQuestion(
        question = "What is the purpose of Mr. Meeseeks?",
        options = listOf("To entertain", "To fulfill a task", "To teach"),
        answer = "To fulfill a task"
    ),
    QuizQuestion(
        question = "What is the family dog’s name?",
        options = listOf("Snuffles", "Buddy", "Snowball"),
        answer = "Snuffles"
    ),
    QuizQuestion(
        question = "What is the name of the agency that monitors dimensions?",
        options = listOf("Galactic Federation", "Council of Ricks", "Department of Time"),
        answer = "Galactic Federation"
    ),
    QuizQuestion(
        question = "Who kills Birdperson?",
        options = listOf("Tammy", "Rick", "Morty"),
        answer = "Tammy"
    ),
    QuizQuestion(
        question = "What is Morty’s crush’s name?",
        options = listOf("Jessica", "Beth", "Tammy"),
        answer = "Jessica"
    ),
    QuizQuestion(
        question = "What’s the name of the planet where Birdperson is from?",
        options = listOf("Bird World", "Gazorpazorp", "Pluto"),
        answer = "Bird World"
    ),
    QuizQuestion(
        question = "What is Rick’s IQ?",
        options = listOf("300", "Infinity", "200"),
        answer = "Infinity"
    ),
    QuizQuestion(
        question = "What does Rick use for interdimensional travel?",
        options = listOf("Portal Gun", "Time Machine", "Teleportation Helmet"),
        answer = "Portal Gun"
    ),
    QuizQuestion(
        question = "What kind of creature is Unity?",
        options = listOf("Parasite", "Hivemind", "Alien"),
        answer = "Hivemind"
    ),
    QuizQuestion(
        question = "What is the main ingredient in Plumbus?",
        options = listOf("Dinglebop", "Fleeb", "Grumbo"),
        answer = "Fleeb"
    ),
    QuizQuestion(
        question = "What is Morty’s full name?",
        options = listOf("Mortimer Smith", "Morty Smith", "Morty Sanchez"),
        answer = "Morty Smith"
    ),
    QuizQuestion(
        question = "Who is the leader of the Galactic Federation?",
        options = listOf("Tammy", "Evil Morty", "Galactic President"),
        answer = "Galactic President"
    ),
    QuizQuestion(
        question = "What is Summer’s dream job?",
        options = listOf("Vet", "Salesperson", "Fashion Designer"),
        answer = "Vet"
    ),
    QuizQuestion(
        question = "What does Jerry do for work?",
        options = listOf("Marketing", "Mortician", "Insurance"),
        answer = "Marketing"
    ),
    QuizQuestion(
        question = "What game do Rick and Morty play at Blips and Chitz?",
        options = listOf("Roy", "Galaga", "Zaxxon"),
        answer = "Roy"
    ),
    QuizQuestion(
        question = "Who does Rick turn into a teenager?",
        options = listOf("Beth", "Jerry", "Summer"),
        answer = "Summer"
    )
)

