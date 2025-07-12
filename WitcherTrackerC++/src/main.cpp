#include <iostream>
#include <string>
#include <sstream>
#include <vector>
#include <regex>

#include "Game.h"

// Regexes for validity checking
static const std::string word = "[A-Za-z]+"; // Ingredients, signs and monster names consist of single alphabetical words
static const std::string potionWord = "[A-Za-z]+(?: [A-Za-z]+)*";
// Potion names consist of 1+ words split by single spaces
static const std::string quantity = "[1-9]\\d*"; // Quantities are positive integers without leading zeroes

static const std::string ingredientToken = quantity + "\\s+" + word; // <quantity> <ingredient>
static const std::string ingredientList = ingredientToken + "(?:\\s*,\\s*" + ingredientToken + ")*";
// <quantity> <ingredient> | <quantity> <ingredient> <ingredient_list>

// Same as ingredients just with the word "trophy at the end"
static const std::string qtyMonster = quantity + "\\s+" + word;
static const std::string trophyList = qtyMonster + "(?:\\s*,\\s*" + qtyMonster + ")*\\s+trophy";

// Valid statements and their regular expression patterns
static const std::vector<std::regex> validStatements = {
        std::regex("^\\s*(?:Exit|exit)\\s*$"), // Exit command

        // Action commands
        std::regex("^\\s*Geralt\\s+loots\\s+" + ingredientList + "\\s*$"),
        std::regex("^\\s*Geralt\\s+trades\\s+" + trophyList + "\\s+for\\s+" + ingredientList + "\\s*$"),
        std::regex("^\\s*Geralt\\s+brews\\s+" + potionWord + "\\s*$"),
        std::regex("^\\s*Geralt\\s+learns\\s+" + word + "\\s+sign\\s+is\\s+effective\\s+against\\s+" + word + "\\s*$"),
        std::regex("^\\s*Geralt\\s+learns\\s+" + potionWord + "\\s+potion\\s+is\\s+effective\\s+against\\s+" + word + "\\s*$"),
        std::regex("^\\s*Geralt\\s+learns\\s+" + potionWord + "\\s+potion\\s+consists\\s+of\\s+" + ingredientList + "\\s*$"),
        std::regex("^\\s*Geralt\\s+encounters\\s+a\\s+" + word + "\\s*$"),

        // Query commands
        std::regex(R"(^\s*Total\s+(?:ingredient|potion|trophy)\s*\?\s*$)"),
        std::regex(R"(^\s*Total\s+(?:ingredient|trophy)\s+)" + word + R"(\s*\?\s*$)"),
        std::regex(R"(^\s*Total\s+potion\s+)" + potionWord + R"(\s*\?\s*$)"),
        std::regex("^\\s*What\\s+is\\s+effective\\s+against\\s+" + word + R"(\s*\?\s*$)"),
        std::regex("^\\s*What\\s+is\\s+in\\s+" + potionWord + R"(\s*\?\s*$)")
    };

/*
 * Method to determine if an input command is valid
 */
bool isValid(const std::string &command) {
    // Traverse the validStatements "arrayList" to find a match
    for (const std::regex &rx: validStatements) {
        if (std::regex_match(command, rx)) return true;
    }
    return false; // INVALID
}

int main() {
    Game witcher; // Initialize the game

    while (true) {
        // Read the command from stdin
        std::string currentLine;
        std::cout << ">> ";
        std::getline(std::cin, currentLine);


        // Prepare for tokenization, utilizing istringstream
        std::istringstream iStringStream(currentLine);
        std::vector<std::string> parsedLine;
        std::string token;
        while (iStringStream >> token) {
            // While there are tokens to read
            parsedLine.push_back(token); // Add the token to the arrayList
        }

        if (parsedLine.empty()) {
            std::cout << "INVALID" << std::endl;
            continue;
        }

        bool valid = isValid(currentLine);

        if (valid && (parsedLine[0] == "Exit" || parsedLine[0] == "exit")) break;

        if (valid) {
            witcher.classifyAndExecute(parsedLine);
        }
        else {
            std::cout << "INVALID" << std::endl;
            continue;
        }
    }
}
