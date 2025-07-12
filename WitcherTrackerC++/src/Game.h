//
// Created by Üstün Yılmaz on 28.05.2025.
//

#ifndef GAME_H
#define GAME_H

#include <vector>
#include <string>
#include "Inventory.h"


class Game {
private:
    Inventory inventory; // Geralt's inventory
    // Space and comma trimming utilities
    static void leftTrim(std::string &str);

    static void rightTrim(std::string &str);

    static void trimSpaces(std::string &str);

    static std::vector<std::string> splitAndTrimCommas(const std::string &input);

    // Action handling functions
    void handleLoot(const std::vector<std::string> &parsedLine);

    void handleTrade(const std::vector<std::string> &parsedLine);

    void handleBrew(const std::vector<std::string> &parsedLine);

    void handleLearn(const std::vector<std::string> &parsedLine);

    void handleEncounter(const std::vector<std::string> &parsedLine);


    // Query handling functions
    void handleTotalIngredientClassic() const;

    void handleTotalTrophyClassic() const;

    void handleTotalPotionClassic() const;

    void handleTotalIngredientSpecific(const std::vector<std::string> &parsedLine) const;

    void handleTotalTrophySpecific(const std::vector<std::string> &parsedLine) const;

    void handleTotalPotionSpecific(const std::vector<std::string> &parsedLine) const;

    void handleEffectivenessQuery(const std::vector<std::string> &parsedLine);

    void handleFormulaQuery(const std::vector<std::string> &parsedLine) const;


public:
    // General handler
    void classifyAndExecute(const std::vector<std::string> &parsedLine);
};


#endif //GAME_H
