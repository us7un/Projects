//
// Created by Üstün Yılmaz on 28.05.2025.
//

#include "Game.h"
#include <sstream>
#include <string>
#include <vector>
#include <iostream>
#include <map>
#include <algorithm>
#include <cctype>

/*
 * Helper method to trim spaces from the left (implementation from StackOverflow)
 */
void Game::leftTrim(std::string &str) {
    str.erase(str.begin(), std::find_if(str.begin(), str.end(), [](unsigned char character) {
        return !std::isspace(character);
    }));
}

/*
 * Helper method to trim spaces from the right (implementation from StackOverflow)
 */
void Game::rightTrim(std::string &str) {
    str.erase(
        std::find_if(str.rbegin(), str.rend(), [](unsigned char character) { return !std::isspace(character); }).base(),
        str.end());
}

/*
 * Helper method to trim spaces
 */
void Game::trimSpaces(std::string &str) {
    leftTrim(str);
    rightTrim(str);
}

/*
 * Method to split a reconstructed uniform parsedLine and remove those nasty commas from it
 */
std::vector<std::string> Game::splitAndTrimCommas(const std::string &input) {
    // Declare the needed structures
    std::vector<std::string> splitLine;
    std::istringstream iStringStream(input);
    std::string token;

    // Trim the spaces and delimit by comma
    while (std::getline(iStringStream, token, ',')) {
        trimSpaces(token);
        if (!token.empty()) splitLine.push_back(token);
    }
    return splitLine;
}

/*
 * Method to handle a loot statement
 */
void Game::handleLoot(const std::vector<std::string> &parsedLine) {
    std::ostringstream stringBuilder; // To form a uniform version of the command with commas handled
    for (size_t i = 2; i < parsedLine.size(); ++i) {
        stringBuilder << parsedLine[i] << ' ';
    }
    std::string afterLootStatement = stringBuilder.str();
    std::vector<std::string> lineWithoutCommas = splitAndTrimCommas(afterLootStatement);
    // We will remove the commas here


    // Parse the quantity and ingredient name to be added to the inventory here
    for (std::string &token: lineWithoutCommas) {
        trimSpaces(token);
        std::istringstream iStringStream(token);
        int quantity;
        iStringStream >> quantity; // Put the quantity in the string buffer
        std::string ingredientName;
        std::getline(iStringStream, ingredientName);
        trimSpaces(ingredientName); // Trim spaces
        inventory.addIngredient(ingredientName, quantity); // Add the ingredient
    }

    std::cout << "Alchemy ingredients obtained" << std::endl; // Operation done
}

/*
 * Method to handle trading trophies for ingredients
 */
void Game::handleTrade(const std::vector<std::string> &parsedLine) {
    // Same logic in removing the commas as above
    std::ostringstream stringBuilder; // To form a uniform version of the command with commas handled
    for (size_t i = 2; i < parsedLine.size(); ++i) {
        stringBuilder << parsedLine[i] << ' ';
    }
    std::string afterTradeStatement = stringBuilder.str();

    size_t forPosition = afterTradeStatement.find(" for ");

    std::string trophyPart = afterTradeStatement.substr(0, forPosition); // Up until "for"
    std::string ingredientPart = afterTradeStatement.substr(forPosition + 5); // Right after "for"

    // Remove the commas and delimit by spaces
    std::vector<std::string> parsedTrophy = splitAndTrimCommas(trophyPart);
    std::vector<std::string> parsedIngredient = splitAndTrimCommas(ingredientPart);

    std::map<std::string, int> givenTrophies; // Trophies to be traded
    std::map<std::string, int> ingredientsRequested; // Ingredients to get

    // Handle the given trophies
    for (std::string &trophyToken: parsedTrophy) {
        std::istringstream iStringStream(trophyToken); // Buffer
        int quantity; // Quantity given
        iStringStream >> quantity; // Read the quantity
        std::string monsterName;
        std::getline(iStringStream, monsterName); // Get the name of the monster
        trimSpaces(monsterName); // Trim spaces
        const std::string trophy = "trophy";
        // Remove "trophy" at the end
        if (monsterName.size() > trophy.size() && monsterName.compare(monsterName.size() - trophy.size(), trophy.size(),
                                                                      trophy) == 0) {
            monsterName.erase(monsterName.size() - trophy.size());
        }
        trimSpaces(monsterName); // Trim spaces again at the last entry
        givenTrophies[monsterName] += quantity; // Form the map
    }

    // Handle the requested ingredients
    for (std::string &ingredientToken: parsedIngredient) {
        std::istringstream iStringStream(ingredientToken); // Buffer
        int quantity; // Quantity wanted
        iStringStream >> quantity; // Insert the quantity first
        std::string ingredientName;
        std::getline(iStringStream, ingredientName); // Get the name of the ingredient
        trimSpaces(ingredientName); // Trim spaces
        ingredientsRequested[ingredientName] += quantity; // Form the map
    }

    // Check if Geralt has enough trophies to trade
    for (auto &tuple: givenTrophies) {
        const std::string &monsterName = tuple.first;
        int neededTrophies = tuple.second;
        // Not enough trophies!
        if (inventory.totalTrophy(monsterName) < neededTrophies) {
            std::cout << "Not enough trophies" << std::endl;
            return;
        }
    }

    for (auto &tuple: givenTrophies) {
        inventory.consumeTrophy(tuple.first, tuple.second); // Give the trophies
    }

    for (auto &tuple: ingredientsRequested) {
        inventory.addIngredient(tuple.first, tuple.second); // Get the ingredients
    }

    std::cout << "Trade successful" << std::endl;
}

/*
 * Method to handle brewing potions
 */
void Game::handleBrew(const std::vector<std::string> &parsedLine) {
    std::ostringstream potionNameBuffer;
    for (size_t i = 2; i < parsedLine.size(); ++i) {
        if (i > 2) potionNameBuffer << ' '; // Single space except the beginning
        potionNameBuffer << parsedLine[i]; // Put the words
    }
    std::string potionName = potionNameBuffer.str();
    trimSpaces(potionName); // Just in case parsedLine was malformed, debug statement

    // Potion unknown
    if (!inventory.knowsFormula(potionName)) {
        std::cout << "No formula for " << potionName << std::endl;
        return;
    }

    // Geralt does not have enough ingredients
    const std::map<std::string, int> &formula = inventory.getFormula(potionName);
    for (auto &tuple: formula) {
        const std::string &ingredientName = tuple.first;
        const int quantityRequired = tuple.second;
        if (inventory.totalIngredient(ingredientName) < quantityRequired) {
            std::cout << "Not enough ingredients" << std::endl;
            return;
        }
    }

    // If all is good, consume the ingredients
    for (auto &tuple: formula) {
        inventory.consumeIngredient(tuple.first, tuple.second);
    }

    inventory.addPotion(potionName, 1); // Add the potion brewed
    std::cout << "Alchemy item created: " << potionName << std::endl;
}

/*
 * Method to handle learning effective potions & signs and potion formulas
 */
void Game::handleLearn(const std::vector<std::string> &parsedLine) {
    std::ostringstream stringBuilder; // Buffer to reconstruct the sentence in a standardized form
    for (size_t i = 2; i < parsedLine.size(); ++i) {
        if (i > 2) stringBuilder << ' '; // Single space except the beginning
        stringBuilder << parsedLine[i]; // Put the words
    }
    std::string standardized = stringBuilder.str();
    // Now we can move on to our comma removing operation for formulas and parsing for learning effectiveness
    trimSpaces(standardized); // Trim leading and following spaces using our implementation

    // Determine which type of sentence we are getting
    const std::string formulaCase = " potion consists of ";
    const std::string signCase = " sign is effective against ";
    const std::string potionCase = " potion is effective against ";

    // If the standardized sentence contains the effectiveness strings
    if (standardized.find(signCase) != std::string::npos || standardized.find(potionCase) != std::string::npos) {
        bool potionOrSign = false; // False for potion, true for sign
        std::string effectiveStatement;
        // Determine the marker type Geralt is learning
        if (standardized.find(signCase) != std::string::npos) {
            potionOrSign = true;
            effectiveStatement = signCase;
        } else effectiveStatement = potionCase;
        size_t counterPosition = standardized.find(effectiveStatement);
        std::string counterName = standardized.substr(0, counterPosition); // Extract the counter's name
        std::string monsterName = standardized.substr(counterPosition + effectiveStatement.size());
        // Extract the monster's name
        // Just in case
        trimSpaces(counterName);
        trimSpaces(monsterName);
        bool knowsMonster = inventory.knowsAnyCounter(monsterName);
        bool knowsCounter;
        if (potionOrSign) {
            inventory.addMonster(monsterName); // Add monster
            knowsCounter = inventory.signEffectiveAgainst(monsterName, counterName); // Already known effectiveness?
            inventory.addEffectiveSign(monsterName, counterName); // Add the effective sign
        } else {
            inventory.addMonster(monsterName);
            knowsCounter = inventory.potionEffectiveAgainst(monsterName, counterName); // Already known effectiveness?
            inventory.addEffectivePotion(monsterName, counterName); // Add the effective potion
        }
        if (!knowsMonster) std::cout << "New bestiary entry added: " << monsterName << std::endl;
        else if (knowsCounter) std::cout << "Already known effectiveness" << std::endl;
        else std::cout << "Bestiary entry updated: " << monsterName << std::endl;
    }

    // If the standardized sentence contains a potion formula
    if (standardized.find(formulaCase) != std::string::npos) {
        size_t potionPosition = standardized.find(formulaCase);
        std::string potionName = standardized.substr(0, potionPosition); // Extract the potion name
        std::string recipe = standardized.substr(potionPosition + formulaCase.size()); // Extract the recipe
        // Just in case
        trimSpaces(potionName);
        trimSpaces(recipe);

        std::vector<std::string> ingredients = splitAndTrimCommas(recipe);
        std::map<std::string, int> formula;
        // Same logic flow as handleTrade, handleLoot
        for (std::string &ingredient: ingredients) {
            std::istringstream buffer(ingredient);
            int quantity;
            buffer >> quantity; // Get the quantity in the buffer first
            std::string ingredientName;
            std::getline(buffer, ingredientName); // Get the ingredient name from the buffer
            trimSpaces(ingredientName); // Trim any spaces
            formula[ingredientName] = quantity; // Initialize the map
        }
        bool knowsFormula = inventory.knowsFormula(potionName);
        if (knowsFormula) std::cout << "Already known formula" << std::endl;
        else {
            inventory.addFormula(potionName, formula); // Add potion to the inventory
            std::cout << "New alchemy formula obtained: " << potionName << std::endl;
        }
    }
}

/*
 * Method to handle encountering and defeating beasts
 */
void Game::handleEncounter(const std::vector<std::string> &parsedLine) {
    std::ostringstream monsterNameBuffer;
    monsterNameBuffer << parsedLine[3]; // Extract monster name
    std::string monsterName = monsterNameBuffer.str();
    trimSpaces(monsterName); // Trim all spaces

    if (!inventory.hasAvailableCounter(monsterName)) {
        // No potion left or monster unknown, handled internally in Inventory.cpp
        std::cout << "Geralt is unprepared and barely escapes with his life" << std::endl;
        return;
    }

    const std::vector<std::string> &effectivePotions = inventory.getEffectivePotions(monsterName);
    for (const std::string &potion : effectivePotions){
        if (inventory.totalPotion(potion) > 0){
            inventory.consumePotion(potion, 1);
        }
    }

    // Consume every effective potion in the inventory (internal logic will remove entry if 0 left)
    inventory.addTrophy(monsterName, 1); // Geralt has defeated the monster

    std::cout << "Geralt defeats " << monsterName << std::endl;
}

/*
 * Method to handle classic total ingredient query
 */
void Game::handleTotalIngredientClassic() const {
    const std::string ingredients = inventory.listIngredients();
    std::cout << ingredients << std::endl;
}

/*
 * Method to handle classic total trophy query
 */
void Game::handleTotalTrophyClassic() const {
    const std::string trophies = inventory.listTrophies();
    std::cout << trophies << std::endl;
}

/*
 * Method to handle classic total potion query
 */
void Game::handleTotalPotionClassic() const {
    const std::string potions = inventory.listPotions();
    std::cout << potions << std::endl;
}

/*
 * Method to handle specific total ingredient query
 */
void Game::handleTotalIngredientSpecific(const std::vector<std::string> &parsedLine) const {
    std::string ingredientName = parsedLine[2];
    trimSpaces(ingredientName);

    if (ingredientName.back() == '?') {
        ingredientName.pop_back(); // Remove any trailing nasty question marks
        trimSpaces(ingredientName);
    }

    int quantity = inventory.totalIngredient(ingredientName);
    std::cout << quantity << std::endl;
}

/*
 * Method to handle specific total trophy query
 */
void Game::handleTotalTrophySpecific(const std::vector<std::string> &parsedLine) const {
    std::string trophyName = parsedLine[2];
    trimSpaces(trophyName);

    if (trophyName.back() == '?') {
        trophyName.pop_back(); // Remove any trailing nasty question marks
        trimSpaces(trophyName);
    }

    int quantity = inventory.totalTrophy(trophyName);
    std::cout << quantity << std::endl;
}

/*
 * Method to handle specific total potion query
 */
void Game::handleTotalPotionSpecific(const std::vector<std::string> &parsedLine) const {
    std::ostringstream stringBuilder;
    for (size_t i = 2; i < parsedLine.size(); ++i) {
        if (i > 2) stringBuilder << ' '; // Append spaces except first word
        stringBuilder << parsedLine[i];
    }
    std::string potionName = stringBuilder.str();
    trimSpaces(potionName);

    if (potionName.back() == '?') {
        potionName.pop_back(); // Remove any nasty question mark
        trimSpaces(potionName);
    }

    const int quantity = inventory.totalPotion(potionName);
    std::cout << quantity << std::endl;
}

/*
 * Method to handle effective stuff against a beast
 */
void Game::handleEffectivenessQuery(const std::vector<std::string> &parsedLine) {
    std::string monsterName = parsedLine[4];
    trimSpaces(monsterName);
    if (monsterName.back() == '?') {
        monsterName.pop_back(); // Remove any nasty question marks
        trimSpaces(monsterName);
    }

    // Fill up the counters arrayList
    std::vector<std::string> effectiveCounters;
    const std::vector<std::string> effectivePotions = inventory.getEffectivePotions(monsterName);
    const std::vector<std::string> effectiveSigns = inventory.getEffectiveSigns(monsterName);
    effectiveCounters.insert(effectiveCounters.end(), effectivePotions.begin(), effectivePotions.end());
    effectiveCounters.insert(effectiveCounters.end(), effectiveSigns.begin(), effectiveSigns.end());

    // Geralt is unfamiliar with the beast
    if (effectiveCounters.empty()) {
        std::cout << "No knowledge of " << monsterName << std::endl;
        return;
    }

    std::sort(effectiveCounters.begin(), effectiveCounters.end()); // Sort alphabetically

    for (size_t i = 0; i < effectiveCounters.size(); ++i) {
        if (i != 0) std::cout << ", "; // Commas inbetween
        std::cout << effectiveCounters[i]; // The counters
    }
    std::cout << std::endl;
}

/*
 * Method to handle potion formula query
 */
void Game::handleFormulaQuery(const std::vector<std::string> &parsedLine) const {
    std::ostringstream stringBuilder;
    for (size_t i = 3; i < parsedLine.size(); ++i) {
        if (i > 3) stringBuilder << ' '; // Spaces except first word
        stringBuilder << parsedLine[i];
    }
    std::string potionName = stringBuilder.str();
    trimSpaces(potionName);

    if (potionName.back() == '?') {
        potionName.pop_back(); // Remove nasty question mark
        trimSpaces(potionName);
    }

    // Geralt doesn't know any formula
    if (!inventory.knowsFormula(potionName)) {
        std::cout << "No formula for " << potionName << std::endl;
        return;
    }

    std::map<std::string, int> formula = inventory.getFormula(potionName);
    std::vector<std::pair<std::string, int> > ingredients(formula.begin(), formula.end());
    // Map returns an iterator but vector doesn't work well with <auto> so I explicitly cast it here
    // Sort first by quantity, then tiebreak by name (utilizing lambda function property of C++11) (I hope it's correct)
    std::sort(ingredients.begin(), ingredients.end(), [](const std::pair<std::string, int> &a, const std::pair<std::string, int> &b) {
        if (a.second != b.second) return a.second > b.second;
        return a.first < b.first;
    });

    for (size_t i = 0; i < ingredients.size(); ++i) {
        if (i != 0) std::cout << ", "; // Split by commas
        std::cout << ingredients[i].second << " " << ingredients[i].first;
    }
    std::cout << std::endl;
}

/*
 * Driver method for any command
 */
void Game::classifyAndExecute(const std::vector<std::string> &parsedLine) {
    if (parsedLine.empty()) return;

    const std::string &firstWord = parsedLine[0];
    // Action commands
    if (firstWord == "Geralt") {
        const std::string &action = parsedLine[1];
        if (action == "loots") handleLoot(parsedLine);
        else if (action == "trades") handleTrade(parsedLine);
        else if (action == "brews") handleBrew(parsedLine);
        else if (action == "learns") handleLearn(parsedLine);
        else handleEncounter(parsedLine);
        return;
    }
    // Total query commands
    if (firstWord == "Total") {
        const std::string &type = parsedLine[1];
        if (type == "ingredient") {
            bool specific = false;
            if (parsedLine.size() > 2 && parsedLine[2] != "?") specific = true;
            if (specific) handleTotalIngredientSpecific(parsedLine);
            else handleTotalIngredientClassic();
            return;
        }
        if (type == "potion") {
            bool specific = false;
            if (parsedLine.size() > 2 && parsedLine[2] != "?") specific = true;
            if (specific) handleTotalPotionSpecific(parsedLine);
            else handleTotalPotionClassic();
            return;
        }
        if (type == "trophy") {
            bool specific = false;
            if (parsedLine.size() > 2 && parsedLine[2] != "?") specific = true;
            if (specific) handleTotalTrophySpecific(parsedLine);
            else handleTotalTrophyClassic();
            return;
        }
    }

    // What query commands
    if (firstWord == "What") {
        const std::string &thirdWord = parsedLine[2];
        if (thirdWord == "effective") {
            handleEffectivenessQuery(parsedLine);
        }
        else handleFormulaQuery(parsedLine);
    }
}

