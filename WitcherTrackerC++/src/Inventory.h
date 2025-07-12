//
// Created by Üstün Yılmaz on 15.05.2025.
//

#ifndef INVENTORY_H
#define INVENTORY_H
#include <map>
#include <vector>
#include <string>
#include <algorithm>


class Inventory {
private:
    std::map<std::string, int> ingredients; // Ingredient inventory
    std::map<std::string, int> potions; // Potion inventory
    std::map<std::string, int> trophies; // Trophy inventory
    std::map<std::string, std::map<std::string, int> > formulas; // Known potion formulas
    std::map<std::string, std::vector<std::string> > effectivePotions; // Effective potion list
    std::map<std::string, std::vector<std::string> > effectiveSigns; // Effective sign list
public:
    void addIngredient(const std::string &ingredientName, int quantity); // Method to add ingredients
    void consumeIngredient(const std::string &ingredientName, int quantity); // Method to consume ingredients
    void addTrophy(const std::string &trophyName, int quantity); // Method to add trophies
    void consumeTrophy(const std::string &trophyName, int quantity); // Method to consume trophies
    void addPotion(const std::string &potionName, int quantity); // Method to add potions
    void consumePotion(const std::string &potionName, int quantity); // Method to consume potions
    void addFormula(const std::string &potionName, const std::map<std::string, int>& ingredients); // Method to add a formula
    const std::map<std::string, int> &getFormula(const std::string &potionName) const; // Method to get a formula
    bool knowsFormula(const std::string &potionName) const;

    // Method to determine if Geralt knows the formula for a potion
    void addMonster(const std::string &monsterName); // Method to add a monster to the bestiary
    void addEffectivePotion(const std::string &monsterName, std::string effectivePotion);

    // Method to add an effective potion to a monster
    void addEffectiveSign(const std::string &monsterName, std::string effectiveSign);

    // Method to add an effective sign to a monster
    bool potionEffectiveAgainst(const std::string &monsterName, const std::string &potion);

    // Method to check if a potion is effective against the corresponding monster
    bool signEffectiveAgainst(const std::string &monsterName, const std::string &sign);

    // Method to check if a sign is effective against the corresponding monster
    bool knowsAnyCounter(const std::string &monsterName); // Method to determine if Geralt can defeat a monster
    bool hasAvailableCounter(const std::string &monsterName) const; // Method to determine if Geralt has an available counter
    std::vector<std::string> &getEffectivePotions(const std::string &monsterName);
    std::vector<std::string> &getEffectiveSigns(const std::string &monsterName);

    // Method to get the effective potions list of a monster
    int totalIngredient(const std::string &ingredientName) const; // Method to get the quantity of an ingredient
    int totalPotion(const std::string &potionName) const; // Method to get the quantity of a potion
    int totalTrophy(const std::string &trophyName) const; // Method to get the quantity of a trophy
    std::string listIngredients() const; // Method to list the ingredients as a standardized string
    std::string listPotions() const; // Method to list the potions as a standardized string
    std::string listTrophies() const; // Method to list the trophies as a standardized string
};


#endif //INVENTORY_H
