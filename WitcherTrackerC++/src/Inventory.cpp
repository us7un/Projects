//
// Created by Üstün Yılmaz on 15.05.2025.
//

#include "Inventory.h"
#include <map>
#include <vector>
#include <string>
#include <algorithm>
#include <sstream>
/*
 * Method to add an ingredient to the inventory
 */
void Inventory::addIngredient(const std::string &ingredientName, int quantity) {
    ingredients[ingredientName] += quantity;
}

/*
 * Method to add a potion to the inventory
 */
void Inventory::addPotion(const std::string &potionName, int quantity) {
    potions[potionName] += quantity;
}

/*
 * Method to add a trophy to the inventory
 */
void Inventory::addTrophy(const std::string &trophyName, int quantity) {
    trophies[trophyName] += quantity;
}

/*
 * Method to consume an ingredient from the inventory
 */
void Inventory::consumeIngredient(const std::string &ingredientName, int quantity) {
    const auto inventory = ingredients.find(ingredientName);
    if (inventory == ingredients.end()) return;
    inventory->second -= quantity;
    if (inventory->second == 0) ingredients.erase(ingredientName);
}

/*
 * Method to consume an potion from the inventory
 */
void Inventory::consumePotion(const std::string &potionName, int quantity) {
    const auto inventory = potions.find(potionName);
    if (inventory == potions.end()) return;
    inventory->second -= quantity;
    if (inventory->second == 0) potions.erase(potionName);
}

/*
 * Method to consume a trophy from the inventory
 */
void Inventory::consumeTrophy(const std::string &trophyName, int quantity) {
    const auto inventory = trophies.find(trophyName);
    if (inventory == trophies.end()) return;
    inventory->second -= quantity;
    if (inventory->second == 0) trophies.erase(trophyName);
}

/*
 * Method to add a monster to the bestiary
 */
void Inventory::addMonster(const std::string &monsterName) {
    if (effectivePotions.find(monsterName) == effectivePotions.end()) {
        // If it does not exist in the current bestiary
        effectivePotions[monsterName] = std::vector<std::string>();
        effectiveSigns[monsterName] = std::vector<std::string>();
    }
}

/*
 * Method to add an effective potion to the corresponding monster
 */
void Inventory::addEffectivePotion(const std::string &monsterName, std::string effectivePotion) {
    const auto monster = effectivePotions.find(monsterName);
    for (const auto& potion : monster->second)
        if (potion == effectivePotion) return;
    monster->second.push_back(std::move(effectivePotion));
}

/*
 * Method to add an effective sign to the corresponding monster
 */
void Inventory::addEffectiveSign(const std::string &monsterName, std::string effectiveSign) {
    const auto monster = effectiveSigns.find(monsterName);
    for (const auto& sign : monster->second)
        if (sign == effectiveSign) return;
    monster->second.push_back(std::move(effectiveSign));
}

/*
 * Method to check if a potion is effective against a certain monster
 */
bool Inventory::potionEffectiveAgainst(const std::string &monsterName, const std::string &potion) {
    const auto monster = effectivePotions.find(monsterName);
    if (monster == effectivePotions.end()) return false; // The monster is unknown
    return std::find(monster->second.begin(), monster->second.end(), potion) != monster->second.end();
}

/*
 * Method to check if a sign is effective against a certain monster
 */
bool Inventory::signEffectiveAgainst(const std::string &monsterName, const std::string &sign) {
    const auto monster = effectiveSigns.find(monsterName);
    if (monster == effectiveSigns.end()) return false; // The monster is unknown
    return std::find(monster->second.begin(), monster->second.end(), sign) != monster->second.end();
}

/*
 * Method to check if Geralt knows any counter to a certain monster
 */
bool Inventory::knowsAnyCounter(const std::string &monsterName) {
    return !effectivePotions[monsterName].empty() || !effectiveSigns[monsterName].empty();
}

/*
 * Method to determine if Geralt has any available counter
 */
bool Inventory::hasAvailableCounter(const std::string &monsterName) const {
    auto monsterSigns = effectiveSigns.find(monsterName);
    if (monsterSigns != effectiveSigns.end() && !monsterSigns->second.empty()) {
        return true;
    }

    // Has to have potions in inventory too!!!
    auto monsterPotions = effectivePotions.find(monsterName);
    if (monsterPotions != effectivePotions.end()) {
        for (const auto &potion : monsterPotions->second) {
            if (totalPotion(potion) > 0) {
                return true;
            }
        }
    }
    return false;
}

/*
 * Method to get the effective potions list of a monster, used for decrementing purposes
 */
std::vector<std::string> &Inventory::getEffectivePotions(const std::string &monsterName) {
    return effectivePotions[monsterName];
}

/*
 * Method to get the effective signs list of a monster, used for decrementing purposes
 */
std::vector<std::string> &Inventory::getEffectiveSigns(const std::string &monsterName) {
    return effectiveSigns[monsterName];
}

/*
 * Method to determine if Geralt knows a specific potion formula
 */
bool Inventory::knowsFormula(const std::string &potionName) const {
    return formulas.find(potionName) != formulas.end();
}

/*
 * Method to add a formula to the database
 */
void Inventory::addFormula(const std::string &potionName, const std::map<std::string, int>& ingredients) {
    if (knowsFormula(potionName)) return;
    formulas[potionName] = ingredients;
}

const std::map<std::string, int> &Inventory::getFormula(const std::string &potionName) const {
    const auto formula = formulas.find(potionName);
    return formula->second;
}



/*
 * Method to return the quantity of an ingredient
 */
int Inventory::totalIngredient(const std::string &ingredientName) const {
    const auto ingredient = ingredients.find(ingredientName);
    if (ingredient == ingredients.end()) return 0;
    return ingredient->second;
}

/*
 * Method to return the quantity of a potion
 */
int Inventory::totalPotion(const std::string &potionName) const {
    const auto potion = potions.find(potionName);
    if (potion == potions.end()) return 0;
    return potion->second;
}

/*
 * Method to return the quantity of a potion
 */
int Inventory::totalTrophy(const std::string &trophyName) const {
    const auto trophy = trophies.find(trophyName);
    if (trophy == trophies.end()) return 0;
    return trophy->second;
}

/*
 * Method to create a list of the desired type for printing
 */
static std::string createList(const std::map<std::string, int> &map) {
    if (map.empty()) return "None";

    std::vector<std::pair<std::string, int>> elements(map.begin(), map.end()); // Elements of the map
    std::sort(elements.begin(), elements.end(), [](const std::pair<std::string, int> &a, const std::pair<std::string, int> &b) {return a.first < b.first;}); // Sort by element names (utilizing lambda functions and closures :) )

    std::ostringstream stringBuilder; // This will build our list
    bool firstElement = true;
    for (auto &tuple : elements) {
        if (!firstElement) stringBuilder << ", "; // Append a comma before if it's not the first one
        stringBuilder << tuple.second << " " << tuple.first; // "<quantity> <stuff>"
        firstElement = false;
    }
    return stringBuilder.str(); // toString
}

/*
 * Method to create a list of ingredients
 */
std::string Inventory::listIngredients() const {
    return createList(ingredients);
}

/*
 * Method to create a list of potions
 */
std::string Inventory::listPotions() const {
    return createList(potions);
}

/*
 * Method to create a list of trophies
 */
std::string Inventory::listTrophies() const {
    return createList(trophies);
}
