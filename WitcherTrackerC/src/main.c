#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "typedefs.h"


/*
 * Methods to compare the names of two things lexicographically, to utilize in the qsort function
*/
int compare_potions(const void *potion1, const void *potion2) {
    const potion *pot1 = *(const potion **) potion1;
    const potion *pot2 = *(const potion **) potion2;
    return strcmp(pot1->potion_name, pot2->potion_name);
}

int compare_trophies(const void *trophy1, const void *trophy2) {
    const trophy *trop1 = *(const trophy **) trophy1;
    const trophy *trop2 = *(const trophy **) trophy2;
    return strcmp(trop1->trophy_name, trop2->trophy_name);
}

int compare_ingredients(const void *ingredient1, const void *ingredient2) {
    const ingredient *ing1 = *(const ingredient **) ingredient1;
    const ingredient *ing2 = *(const ingredient **) ingredient2;
    return strcmp(ing1->name, ing2->name);
}

int compare_ingredients2(const void *ingredient1, const void *ingredient2) {
    const ingredient *ing1 = *(const ingredient **)ingredient1;
    const ingredient *ing2 = *(const ingredient **)ingredient2;

    if (ing1->quantity > ing2->quantity) return -1;
    if (ing1->quantity < ing2->quantity) return  1;

    return strcmp(ing1->name, ing2->name);
}

int compare_strings(const void *string1, const void *string2) {
    const char *str1 = *(const char **) string1;
    const char *str2 = *(const char **) string2;
    return strcmp(str1, str2);
}

char *i_to_a(int number) {
    char *string = check(malloc(12));
    sprintf(string, "%d", number);
    return string;
}


/*
 * Method to parse a query command and print its respective outputs
*/
void parse_query(inventory *current_inventory, parsed_line *parsed_command0) {
    // Total potion case
    if (strcmp(parsed_command0->line_array[0], "Total") == 0 && (strcmp(parsed_command0->line_array[1], "potion") == 0 || strcmp(parsed_command0->line_array[1], "potion?") == 0)) {
        // Parse the line here
        int array_length = (int) (parsed_command0->size);
        int last_word_index = array_length - 1;
        // Remove any question marks that are parsed as separate tokens
        if (strcmp(parsed_command0->line_array[last_word_index], "?") == 0)
            last_word_index--;
        int potion_name_length = 0;
        // Calculate potion name length
        for (int i = 2; i <= last_word_index; i++) {
            potion_name_length += (int) strlen(parsed_command0->line_array[i]); // Account for words
            if (i < last_word_index)
                potion_name_length++; // Account for whitespaces
        }
        potion_name_length++; // Account for "\0"

        char *potion_name = check(malloc(potion_name_length));
        char *write_pointer = potion_name; // For more efficiency compared to strcat

        // Build the potion name
        for (int i = 2; i <= last_word_index; i++) {
            char *token = parsed_command0->line_array[i];
            int length = (int) strlen(token);

            if (i == last_word_index && length > 0 && token[length - 1] == '?')
                length--; // Remove any trailing question marks

            memcpy(write_pointer, token, length); // Copy the word
            write_pointer += length;

            if (i < last_word_index) {
                *write_pointer = ' '; // Account for whitespaces
                write_pointer++;
            }
        }

        *write_pointer = '\0'; // Account for terminator


        // If the query is asking for a specific potion
        if (strcmp(potion_name, "") != 0) {
            potion *query = NULL;
            potion *current_potion = current_inventory->potion_inventory;
            // Get the potion in Geralt's inventory
            while (current_potion != NULL) {
                if (strcmp(current_potion->potion_name, potion_name) == 0) {
                    query = current_potion;
                    break;
                }
                current_potion = current_potion->next_potion;
            }
            // Print the quantity of the potion
            if (query != NULL) {
                printf("%d\n", query->potion_quantity);
            } else {
                // Always exit to avoid null pointer dereference for query
                printf("0\n");
                free(potion_name);
                return;
            }
        }
        // If the query is asking for all potions in Geralt's inventory
        else {
            // Count the potions in the inventory
            int potion_count = 0;
            potion *current_potion = current_inventory->potion_inventory;
            while (current_potion != NULL) {
                if (current_potion->potion_quantity > 0) {
                    potion_count++;
                }
                current_potion = current_potion->next_potion;
            }

            // If the inventory has no positive‑quantity potions, print 'None'
            if (potion_count == 0) {
                printf("None\n");
                free(potion_name);
                return;
            }

            // Allocate the potions array
            potion **potions = check(malloc(potion_count * sizeof(potion *)));

            // Populate it with potions
            int index = 0;
            current_potion = current_inventory->potion_inventory;
            while (current_potion != NULL) {
                if (current_potion->potion_quantity > 0) {
                    potions[index++] = current_potion;
                }
                current_potion = current_potion->next_potion;
            }

            // Sort and print the potions
            qsort(potions, potion_count, sizeof(potion *), compare_potions);
            for (int i = 0; i < potion_count; i++) {
                if (i != potion_count - 1)
                    printf("%d %s, ", potions[i]->potion_quantity, potions[i]->potion_name);
                else
                    printf("%d %s\n", potions[i]->potion_quantity, potions[i]->potion_name);
            }

            free(potions);
            free(potion_name);
        }
    }
    // Total trophy case
    if (strcmp(parsed_command0->line_array[0], "Total") == 0 && (strcmp(parsed_command0->line_array[1], "trophy") == 0 || strcmp(parsed_command0->line_array[1], "trophy?") == 0)) {
        // Parse the line here
        int array_length = (int) (parsed_command0->size);
        int last_word_index = array_length - 1;
        // Remove any question marks that are parsed as separate tokens
        if (strcmp(parsed_command0->line_array[last_word_index], "?") == 0)
            last_word_index--;
        int trophy_name_length = 0;
        // Calculate trophy name length
        for (int i = 2; i <= last_word_index; i++) {
            trophy_name_length += (int) strlen(parsed_command0->line_array[i]); // Account for words
            if (i < last_word_index)
                trophy_name_length++; // Account for whitespaces
        }
        trophy_name_length++; // Account for "\0"

        char *trophy_name = check(malloc(trophy_name_length));
        char *write_pointer = trophy_name; // For more efficiency compared to strcat

        // Build the trophy name
        for (int i = 2; i <= last_word_index; i++) {
            char *token = parsed_command0->line_array[i];
            int length = (int) strlen(token);

            if (i == last_word_index && length > 0 && token[length - 1] == '?')
                length--; // Remove any trailing question marks

            memcpy(write_pointer, token, length); // Copy the word
            write_pointer += length;

            if (i < last_word_index) {
                *write_pointer = ' '; // Account for whitespaces
                write_pointer++;
            }
        }

        *write_pointer = '\0'; // Account for terminator

        // If the query is asking for a specific trophy
        if (strcmp(trophy_name, "") != 0) {
            trophy *query = NULL;
            trophy *current_trophy = current_inventory->trophy_inventory;
            // Get the trophy in Geralt's inventory
            while (current_trophy != NULL) {
                if (strcmp(current_trophy->trophy_name, trophy_name) == 0) {
                    query = current_trophy;
                    break;
                }
                current_trophy = current_trophy->next_trophy;
            }
            // Print the quantity of the trophy
            if (query != NULL) {
                printf("%d\n", query->trophy_quantity);
            } else {
                // Always exit to avoid null dereference on query
                printf("0\n");
                free(trophy_name);
                return;
            }
        }

        // If the query is asking for all trophies in Geralt's inventory
        else {
            // Count the trophies
            int trophy_count = 0;
            trophy *current_trophy = current_inventory->trophy_inventory;
            while (current_trophy != NULL) {
                if (current_trophy->trophy_quantity > 0) {
                    trophy_count++;
                }
                current_trophy = current_trophy->next_trophy;
            }

            // No trophy case
            if (trophy_count == 0) {
                printf("None\n");
                free(trophy_name);
                return;
            }

            // Allocate the trophies array
            trophy **trophies = check(malloc(trophy_count * sizeof(trophy *)));

            // Add the trophies to array
            int index = 0;
            current_trophy = current_inventory->trophy_inventory;
            while (current_trophy != NULL) {
                if (current_trophy->trophy_quantity > 0) {
                    trophies[index++] = current_trophy;
                }
                current_trophy = current_trophy->next_trophy;
            }

            // Sort and print the trophies
            qsort(trophies, trophy_count, sizeof(trophy *), compare_trophies);
            for (int i = 0; i < trophy_count; i++) {
                if (i != trophy_count - 1)
                    printf("%d %s, ", trophies[i]->trophy_quantity, trophies[i]->trophy_name);
                else
                    printf("%d %s\n", trophies[i]->trophy_quantity, trophies[i]->trophy_name);
            }

            free(trophies);
            free(trophy_name);
        }
    }
    // Total ingredient case
    if (strcmp(parsed_command0->line_array[0], "Total") == 0 && (strcmp(parsed_command0->line_array[1], "ingredient") == 0 || strcmp(parsed_command0->line_array[1], "ingredient?") == 0)) {
        // Parse the line here
        int array_length = (int) (parsed_command0->size);
        int last_word_index = array_length - 1;
        // Remove any question marks that are parsed as separate tokens
        if (strcmp(parsed_command0->line_array[last_word_index], "?") == 0)
            last_word_index--;
        int ingredient_name_length = 0;
        // Calculate ingredient name length
        for (int i = 2; i <= last_word_index; i++) {
            ingredient_name_length += (int) strlen(parsed_command0->line_array[i]); // Account for words
            if (i < last_word_index)
                ingredient_name_length++; // Account for whitespaces
        }
        ingredient_name_length++; // Account for "\0"

        char *ingredient_name = check(malloc(ingredient_name_length));
        char *write_pointer = ingredient_name; // For more efficiency compared to strcat

        // Build the ingredient name
        for (int i = 2; i <= last_word_index; i++) {
            char *token = parsed_command0->line_array[i];
            int length = (int) strlen(token);

            if (i == last_word_index && length > 0 && token[length - 1] == '?')
                length--; // Remove any trailing question marks

            memcpy(write_pointer, token, length); // Copy the word
            write_pointer += length;

            if (i < last_word_index) {
                *write_pointer = ' '; // Account for whitespaces
                write_pointer++;
            }
        }

        *write_pointer = '\0'; // Account for terminator

        // If the query is asking for a specific ingredient
        if (strcmp(ingredient_name, "") != 0) {
            ingredient *query = NULL;
            ingredient *current_ingredient = current_inventory->ingredient_inventory;
            // Get the ingredient in Geralt's inventory
            while (current_ingredient != NULL) {
                if (strcmp(current_ingredient->name, ingredient_name) == 0) {
                    query = current_ingredient;
                    break;
                }
                current_ingredient = current_ingredient->next;
            }
            // Print the quantity of the ingredient
            if (query != NULL) {
                printf("%d\n", query->quantity);
            } else {
                printf("0\n");
                free(ingredient_name);
                return;
            }
        }
        // If the query is asking for all ingredients in Geralt's inventory
        else {
            // Count the ingredients
            int ingredient_count = 0;
            ingredient *current_ingredient = current_inventory->ingredient_inventory;
            while (current_ingredient != NULL) {
                if (current_ingredient->quantity > 0) {
                    ingredient_count++;
                }
                current_ingredient = current_ingredient->next;
            }

            // If no ingredients print "None"
            if (ingredient_count == 0) {
                printf("None\n");
                free(ingredient_name);
                return;
            }

            // Allocate the ingredients array
            ingredient **ingredients = check(malloc(ingredient_count * sizeof(ingredient *)));

            // Add the ingredients to the array
            int index = 0;
            current_ingredient = current_inventory->ingredient_inventory;
            while (current_ingredient != NULL) {
                if (current_ingredient->quantity > 0) {
                    ingredients[index++] = current_ingredient;
                }
                current_ingredient = current_ingredient->next;
            }

            // Sort and print the ingredients
            qsort(ingredients, ingredient_count, sizeof(ingredient *), compare_ingredients);
            for (int i = 0; i < ingredient_count; i++) {
                if (i != ingredient_count - 1)
                    printf("%d %s, ", ingredients[i]->quantity, ingredients[i]->name);
                else
                    printf("%d %s\n", ingredients[i]->quantity, ingredients[i]->name);
            }

            free(ingredients);
            free(ingredient_name);
        }
    }
    // Effective query case
    if (strcmp(parsed_command0->line_array[0], "What") == 0 && strcmp(parsed_command0->line_array[1], "is") == 0 &&
        strcmp(parsed_command0->line_array[2], "effective") == 0 && strcmp(parsed_command0->line_array[3], "against") ==
        0) {
        // Parse the line here
        int array_length = (int) (parsed_command0->size);
        int last_word_index = array_length - 1;
        // Remove any question marks that are parsed as separate tokens
        if (strcmp(parsed_command0->line_array[last_word_index], "?") == 0)
            last_word_index--;
        int monster_name_length = 0;
        // Calculate monster name length
        for (int i = 4; i <= last_word_index; i++) {
            monster_name_length += (int) strlen(parsed_command0->line_array[i]); // Account for words
            if (i < last_word_index)
                monster_name_length++; // Account for whitespaces
        }
        monster_name_length++; // Account for "\0"

        char *monster_name = check(malloc(monster_name_length));
        char *write_pointer = monster_name; // For more efficiency compared to strcat

        // Build the potion name
        for (int i = 4; i <= last_word_index; i++) {
            char *token = parsed_command0->line_array[i];
            int length = (int) strlen(token);

            if (i == last_word_index && length > 0 && token[length - 1] == '?')
                length--; // Remove any trailing question marks

            memcpy(write_pointer, token, length); // Copy the word
            write_pointer += length;

            if (i < last_word_index) {
                *write_pointer = ' '; // Account for whitespaces
                write_pointer++;
            }
        }

        *write_pointer = '\0'; // Account for terminator

        // Get the monster in the bestiary
        bestiary *query = NULL;
        bestiary *current_monster = current_inventory->learned_bestiary;

        while (current_monster != NULL) {
            if (strcmp(current_monster->monster_name, monster_name) == 0) {
                query = current_monster;
                break;
            }
            current_monster = current_monster->next_bestiary;
        }

        // If monster does not exist, or no information is available; exit
        if (query == NULL) {
            printf("No knowledge of %s\n", monster_name);
            free(monster_name);
            return;
        }

        // Get the total counter count to form an array
        int potion_count = (int) query->effective_potions->size;

        int sign_count = (int) query->effective_signs->size;

        char **counters = check(malloc((potion_count + sign_count) * sizeof(char *)));

        // Form the array and sort it
        for (int i = 0; i < potion_count; i++) {
            char *current_potion = query->effective_potions->ptr_arr[i];
            counters[i] = current_potion;
        }

        for (int i = potion_count; i < potion_count + sign_count; i++) {
            char *current_sign = query->effective_signs->ptr_arr[i - potion_count];
            counters[i] = current_sign;
        }

        if (potion_count + sign_count > 0) {
            qsort(counters, potion_count + sign_count, sizeof(char *), compare_strings);
        }


        // Print every counter and free the allocated memory
        for (int i = 0; i < potion_count + sign_count; i++) {
            if (i != potion_count + sign_count - 1)
                printf("%s, ", counters[i]);
            else printf("%s\n", counters[i]);
        }

        free(counters);
        free(monster_name);
    }

    // Potion formula query case
    if (strcmp(parsed_command0->line_array[0], "What") == 0 && strcmp(parsed_command0->line_array[1], "is") == 0 &&
        strcmp(parsed_command0->line_array[2], "in") == 0) {
        // Parse the line here
        int array_length = (int) parsed_command0->size;
        int last_word_index = array_length - 1;
        // Remove any question marks that are parsed as separate tokens
        if (strcmp(parsed_command0->line_array[last_word_index], "?") == 0)
            last_word_index--;
        int potion_name_length = 0;
        // Calculate monster name length
        for (int i = 3; i <= last_word_index; i++) {
            potion_name_length += (int) strlen(parsed_command0->line_array[i]); // Account for words
            if (i < last_word_index)
                potion_name_length++; // Account for whitespaces
        }
        potion_name_length++; // Account for "\0"

        char *potion_name = check(malloc(potion_name_length));
        char *write_pointer = potion_name; // For more efficiency compared to strcat

        // Build the potion name
        for (int i = 3; i <= last_word_index; i++) {
            char *token = parsed_command0->line_array[i];
            int length = (int) strlen(token);

            if (i == last_word_index && length > 0 && token[length - 1] == '?')
                length--; // Remove any trailing question marks

            memcpy(write_pointer, token, length); // Copy the word
            write_pointer += length;

            if (i < last_word_index) {
                *write_pointer = ' '; // Account for whitespaces
                write_pointer++;
            }
        }

        *write_pointer = '\0'; // Account for terminator

        formula *query = NULL;
        formula *current_potion = current_inventory->formula_inventory;
        // Get the potion in Geralt's inventory
        while (current_potion != NULL) {
            if (strcmp(current_potion->potion_name, potion_name) == 0) {
                query = current_potion;
                break;
            }
            current_potion = current_potion->next_formula;
        }

        if (query == NULL) {
            printf("No formula for %s\n", potion_name);
            return;
        }


        // Allocate the formula ingredient array
        ingredient** ingredients = check(malloc(sizeof(ingredient*) * (int)query->ingredient_list->size));

        // Populate the array and sort using the SECOND ingredient compare function
        for (int i = 0; i < (int)query->ingredient_list->size; i++) {
            ingredients[i] = create_ingredient(query->ingredient_list->ptr_arr[i], query->ingredient_list->int_arr[i]);
        }
        qsort(ingredients,(int)query->ingredient_list->size, sizeof(ingredient *), compare_ingredients2);

        // Print every ingredient and free the ingredients array
        for (int i = 0; i < (int)query->ingredient_list->size; i++) {
            if (i != (int)query->ingredient_list->size - 1)
                printf("%d %s, ", ingredients[i]->quantity, ingredients[i]->name);
            else
                printf("%d %s\n", ingredients[i]->quantity, ingredients[i]->name);


            free(ingredients[i]->name);
            free(ingredients[i]);
        }

        free(ingredients);
        free(potion_name);
    }
}

/*
 * Method to check if an input command is valid
 */
int validity_checker(char *command, const char *const_command, parsed_line *parsed_command) {
    // Loot sentence case
    if (is_loot_sentence(parsed_command)) {
        // Look at every token you encounter and move the previous token pointer as you progress,
        // catching invalid inputs along the way
        char *previous_token = NULL;
        for (int i = 2; i < (int) parsed_command->size; i++) {
            char *token = parsed_command->line_array[i];
            if (is_int_token(token) || (is_int_token_with_comma(token) && previous_token != NULL)) {
                if (previous_token == NULL) {
                    previous_token = token;
                } else if (is_comma_token(previous_token) || is_word_token_with_comma(previous_token)) {
                    previous_token = token;
                } else {
                    printf("INVALID\n");
                    return 0;
                }
            } else if (is_comma_token(token)) {
                if (previous_token == NULL) {
                    printf("INVALID\n");
                    return 0;
                }
                if (is_word_token(previous_token)) {
                    previous_token = token;
                } else {
                    printf("INVALID\n");
                    return 0;
                }
            } else if (is_word_token_with_comma(token)) {
                if (previous_token == NULL) {
                    printf("INVALID\n");
                    return 0;
                }
                if (is_int_token(previous_token) || is_int_token_with_comma(previous_token)) {
                    previous_token = token;
                } else {
                    printf("INVALID\n");
                    return 0;
                }
            } else if (is_word_token(token)) {
                if (previous_token == NULL) {
                    printf("INVALID\n");
                    return 0;
                }
                if (is_int_token(previous_token) || is_int_token_with_comma(previous_token)) {
                    previous_token = token;
                } else {
                    printf("INVALID\n");
                    return 0;
                }
            } else {
                printf("INVALID\n");
                return 0;
            }
        }
        if (!is_word_token(parsed_command->line_array[parsed_command->size-1])) {
            printf("INVALID\n");
            return 0;
        }
        return 1;
    }
    // Trade sentence case
    else if (is_trade_sentence(parsed_command)) {
        // If there is no "trophy" keyword, the input is invalid
        if (strcmp(parsed_command->line_array[2], "trophy") == 0) {
            printf("INVALID\n");
            return 0;
        }


        // Initialize the corresponding counters and boolean expressions
        int trophy_list = 0;
        int ingredient_list = 0;
        int is_keyword_found = 0;

        int end_index_trophy = 0;
        int start_index_ingredients = 0;

        // Traverse through the input to find invalid tokens
        for (int i = 2; i < (int) parsed_command->size; i++) {
            char *token = parsed_command->line_array[i];
            if (strcmp(token, "trophy") == 0) {
                is_keyword_found = 1;
                end_index_trophy = i;
                if (strcmp(parsed_command->line_array[i + 1], "for") != 0) {
                    printf("INVALID\n");
                    return 0;
                }
                if (i + 1 == (int)parsed_command->size - 1) {
                    printf("INVALID\n");
                    return 0;
                }
                start_index_ingredients = i + 2;
            }
        }

        // Check if the trophy and ingredient lists stated in the CFG are valid
        trophy_list = is_valid_list(parsed_command, 2, end_index_trophy);
        ingredient_list = is_valid_list(parsed_command, start_index_ingredients, (int) parsed_command->size);

        if (!(trophy_list && ingredient_list && is_keyword_found)) {
            printf("INVALID\n");
            return 0;
        }
        return 1;
    }
    // Brew sentence case
    else if (is_brew_sentence(parsed_command)) {
        // If there is more than one space between potion names, the input is invalid
        int word_count = 0;
        int space_count = 0;
        int is_in_word = 0;
        for (int i = 0; i < (int)strlen(const_command); i++) {
            if (const_command[i] == ' ') {
                if (space_count == 0) {
                    if (is_in_word){
                        word_count++;
                        is_in_word = 0;
                    }
                    space_count++;
                } else {
                    if (word_count > 2) {
                        printf("INVALID\n");
                        return 0;
                    }
                    space_count++;
                }
            } else {
                is_in_word = 1;
                space_count = 0;
            }
        }
        // Commence similar operations like the loot sentence case
        if (parsed_command->size <= 2) {
            printf("INVALID\n");
            return 0;
        }
        if (parsed_command->size == 3) {
            if (!is_word_token(parsed_command->line_array[2])) {
                printf("INVALID\n");
                return 0;
            }
        }
        for (int i = 2; i < (int) parsed_command->size - 1; i++) {
            char *token = parsed_command->line_array[i];
            char *token2 = parsed_command->line_array[i + 1];


            if (!(is_word_token(token) && is_word_token(token2))) {
                printf("INVALID\n");
                return 0;
            }
        }
        return 1;
    }
    // Learn sentence case
    else if (is_learn_sentence(parsed_command)) {
        // If there is no "sign" or "potion" keywords, the input is invalid
        if (strcmp(parsed_command->line_array[2], "sign") == 0) {
            printf("INVALID\n");
            return 0;
        }
        if (strcmp(parsed_command->line_array[2], "potion") == 0) {
            printf("INVALID\n");
            return 0;
        }


        if (parsed_command->size > 8 && strcmp(parsed_command->line_array[parsed_command->size-5], "sign") == 0) {
            printf("INVALID\n");
            return 0;
        }

        // Sign case, the logic is similar to loot sentence case
        if (parsed_command->size >= 8 && strcmp(parsed_command->line_array[3], "sign") == 0 ) {

            if (!is_word_token(parsed_command->line_array[2])) {
                printf("INVALID\n");
                return 0;
            }

            if (strcmp(parsed_command->line_array[4], "is") != 0) {
                printf("INVALID\n");
                return 0;
            }
            if (strcmp(parsed_command->line_array[5], "effective") != 0) {
                printf("INVALID\n");
                return 0;
            }
            if (strcmp(parsed_command->line_array[6], "against") != 0) {
                printf("INVALID\n");
                return 0;
            }
            if (parsed_command->size - 1 == 6) {
                printf("INVALID\n");
                return 0;
            }
            if (parsed_command->size - 1 > 7) {
                printf("INVALID\n");
                return 0;
            }

            if (!(is_word_token(parsed_command->line_array[5]) && is_word_token(parsed_command->line_array[7]))) {
                printf("INVALID\n");
                return 0;
            }

            return 1;
        }
        // Potion case, the code is a combination of the logic in loot and brew cases
        if (parsed_command->size >= 8 && strcmp(parsed_command->line_array[parsed_command->size - 2], "against") == 0) {
            if (!is_word_token(parsed_command->line_array[parsed_command->size - 1])) {
                printf("INVALID\n");
                return 0;
            }

            if (strcmp(parsed_command->line_array[parsed_command->size - 3], "effective") != 0) {
                printf("INVALID\n");
                return 0;
            }
            if (strcmp(parsed_command->line_array[parsed_command->size - 4], "is") != 0) {
                printf("INVALID\n");
                return 0;
            }
            if (strcmp(parsed_command->line_array[parsed_command->size - 5], "potion") != 0) {
                printf("INVALID\n");
                return 0;
            }

            int word_count = 0;
            int space_count = 0;
            int is_in_word = 0;
            for (int i = 0; i < (int)strlen(const_command); i++) {
                if (word_count >= parsed_command->size - 5)
                    break;

                if (const_command[i] == ' ') {
                    if (space_count == 0) {
                      if (is_in_word) {
                          is_in_word = 0;
                          word_count++;
                        }
                        space_count++;
                    } else {
                        if (word_count > 2) {
                            printf("INVALID\n");
                            return 0;
                        }
                        space_count++;
                    }
                } else {
                    is_in_word = 1;
                    space_count = 0;
                }
            }

            // Check if potion name is valid breaking it into tokens
            char *token1;
            char *token2;
            for (int i = 2; i < (int) parsed_command->size - 5; i++) {
                token1 = parsed_command->line_array[i];
                token2 = parsed_command->line_array[i + 1];

                if (!(is_word_token(token1) && is_word_token(token2))) {
                    printf("INVALID\n");
                    return 0;
                }
            }
            return 1;
        }

        if (parsed_command->size < 8) {
            printf("INVALID\n");
            return 0;
        }

        // Check if "potion" keyword is included
        int i;
        int is_potion_found = 0;
        for (i = 2; i < (int) parsed_command->size; i++) {
            char *token = parsed_command->line_array[i];

            if (!is_word_token(token)) {
                printf("INVALID\n");
                return 0;
            }
            if (strcmp(parsed_command->line_array[i + 1], "potion") == 0) {
                is_potion_found = 1;
                break;
            }
            if (!is_word_token(parsed_command->line_array[i + 1])) {
                printf("INVALID\n");
                return 0;
            }
        }

        if (is_potion_found != 1) {
            printf("INVALID\n");
            return 0;
        }
        // Check validity of potion name (i+1 is potion's index)

        if (i + 1 > parsed_command->size - 5) {
            printf("INVALID\n");
            return 0;
        }

        int word_count = 0;
        int space_count = 0;
        int is_in_word = 0;
        for (int j = 0; j < (int)strlen(const_command); j++) {
            if (word_count >= i + 1)
                break;

            if (const_command[j] == ' ') {
                if (space_count == 0) {
                    if (is_in_word) {
                        is_in_word = 0;
                        word_count++;
                    }
                    space_count++;
                } else {
                    if (word_count > 2) {
                        printf("INVALID\n");
                        return 0;
                    }
                    space_count++;
                }
            } else {
                is_in_word = 1;
                space_count = 0;
            }
        }


        if (strcmp(parsed_command->line_array[i + 2], "consists") != 0) {
            printf("INVALID\n");
            return 0;
        }
        if (strcmp(parsed_command->line_array[i + 3], "of") != 0) {
            printf("INVALID\n");
            return 0;
        }
        if (i + 3 == (int)parsed_command->size - 1) {
            printf("INVALID\n");
            return 0;
        }
        int is_valid_ingredient = is_valid_list(parsed_command, i + 4, (int) parsed_command->size);
        if (is_valid_ingredient == 0) {
            printf("INVALID\n");
            return 0;
        }
        return 1;
    }
    // Encounter case
    else if (is_encounter_sentence(parsed_command)) {
        if (strcmp(parsed_command->line_array[2], "a") != 0) {
            printf("INVALID\n");
            return 0;
        }
        if (!is_word_token(parsed_command->line_array[3])) {
            printf("INVALID\n");
            return 0;
        }
        if (parsed_command->size > 4) {
            printf("INVALID\n");
            return 0;
        }
        return 1;
    }

    // Potion query case
    else if (is_total_potion(parsed_command)) {
        int array_length = (int) parsed_command->size;
        char *previous_token = NULL;

        // The logic here is similar to brew case, with the addition of a question mark
        int word_count = 0;
        int is_in_word = 0;
        int space_count = 0;
        for (int i = 0; i < (int) strlen(const_command); i++) {
            if (const_command[i] == ' ') {
                if (is_in_word) {
                    word_count++;
                    is_in_word = 0;
                    space_count++;
                }
                else {
                    // The nested conditionals allow for easier readability and debugging
                    if (space_count == 1) {
                        if (word_count > 3) {
                            for (int j = i + 1; j < (int) strlen(const_command); j++) {
                                if (const_command[j] != ' ') {
                                    if (const_command[j] != '?') {
                                        printf("INVALID\n");
                                        return 0;
                                    }
                                }
                            }
                        }
                        space_count++;
                    }
                    space_count++;
                }
            }
            else {
                is_in_word = 1;
                space_count = 0;
            }
        }


        // Check if the last character is a question mark
        const char* question_mark_word = parsed_command->line_array[array_length - 1];

        if (question_mark_word[strlen(question_mark_word)-1] != '?') {
            printf("INVALID\n");
            return 0;
        }

        // After everything is handled, continue with checking the other words are valid
        if (array_length == 2 || (array_length == 3 && strcmp(parsed_command->line_array[2], "?") == 0)) {
            return 1;
        }

        // Same logic as loot case
        for (int i = 2; i < array_length; i++) {
            char *token = parsed_command->line_array[i];
            if (is_int_token(token)) {
                printf("INVALID\n");
                return 0;
            } else if (is_comma_token(token)) {
                printf("INVALID\n");
                return 0;
            } else if (is_question_mark_token(token)) {
                if (previous_token == NULL) {
                    printf("INVALID\n");
                    return 0;
                }
                if (is_word_token(previous_token)) {
                    previous_token = token;
                } else {
                    printf("INVALID\n");
                    return 0;
                }
            } else if (is_word_token(token)) {
                if (previous_token == NULL) {
                    previous_token = token;
                }
                else if (is_word_token(previous_token)) {
                    previous_token = token;
                } else {
                    printf("INVALID\n");
                    return 0;
                }
            } else if (is_word_token_with_comma(token)) {
                printf("INVALID\n");
                return 0;
            } else if (is_word_token_with_question_mark(token)) {
                if (previous_token == NULL) {
                    previous_token = token;
                }
                else if (is_word_token(previous_token)) {
                    previous_token = token;
                } else {
                    printf("INVALID\n");
                    return 0;
                }
            } else {
                printf("INVALID\n");
                return 0;
            }
        }
        return 1;
    }

    // Total ingredient or trophy query case, has the same logic as total potion query case without the potion name handling
    else if (is_total_ingredient(parsed_command) || is_total_trophy( parsed_command)) {
        int array_length = (int) parsed_command->size;
        char *previous_token = NULL;

        const char* question_mark_word = parsed_command->line_array[array_length - 1];

        if (question_mark_word[strlen(question_mark_word)-1] != '?') {
            printf("INVALID\n");
            return 0;
        }
        if (array_length == 2  || (array_length == 3 && strcmp(parsed_command->line_array[2], "?") == 0)) {
            return 1;
        }
        for (int i = 2; i < array_length; i++) {
            char *token = parsed_command->line_array[i];
            if (is_int_token(token)) {
                printf("INVALID\n");
                return 0;
            } else if (is_comma_token(token)) {
                printf("INVALID\n");
                return 0;
            } else if (is_question_mark_token(token)) {
                if (previous_token == NULL) {
                    printf("INVALID\n");
                    return 0;
                }
                if (is_word_token(previous_token)) {
                    previous_token = token;
                } else {
                    printf("INVALID\n");
                    return 0;
                }
            } else if (is_word_token(token)) {
                if (previous_token == NULL) {
                    previous_token = token;
                } else {
                    printf("INVALID\n");
                    return 0;
                }
            } else if (is_word_token_with_comma(token)) {
                printf("INVALID\n");
                return 0;
            } else if (is_word_token_with_question_mark(token)) {
                if (previous_token == NULL) {
                    previous_token = token;
                } else {
                    printf("INVALID\n");
                    return 0;
                }
            } else {
                printf("INVALID\n");
                return 0;
            }
        }
        return 1;
    }

    // Effective query case, handled using the same logic as before
    else if (is_effective_query(parsed_command)) {
        int array_length = (int) parsed_command->size;
        char *previous_token = NULL;
        const char* question_mark_word = parsed_command->line_array[array_length - 1];

        if (question_mark_word[strlen(question_mark_word)-1] != '?') {
            printf("INVALID\n");
            return 0;
        }
        for (int i = 4; i < array_length; i++) {
            char *token = parsed_command->line_array[i];
            if (is_word_token_with_question_mark(token)) {
                if (previous_token == NULL) {
                    previous_token = token;
                } else {
                    printf("INVALID\n");
                    return 0;
                }
            } else if (is_word_token(token)) {
                if (previous_token == NULL) {
                    previous_token = token;
                } else {
                    printf("INVALID\n");
                    return 0;
                }
            } else if (is_question_mark_token(token)) {
                if (previous_token == NULL) {
                    printf("INVALID\n");
                    return 0;
                }
                else if (is_word_token(previous_token)) {
                    previous_token = token;
                } else {
                    printf("INVALID\n");
                    return 0;
                }
            } else {
                printf("INVALID\n");
                return 0;
            }
        }
        return 1;
    }
    // Potion formula query case, handled using the same logic as total potion query
    else if (is_potion_formula_query(parsed_command)) {
        int array_length = (int) parsed_command->size;
        char *previous_token = NULL;
        const char* question_mark_word = parsed_command->line_array[array_length - 1];


        int word_count = 0;
        int is_in_word = 0;
        int space_count = 0;
        for (int i = 0; i < (int) strlen(const_command); i++) {
            if (const_command[i] == ' ') {
                if (is_in_word) {
                    word_count++;
                    is_in_word = 0;
                    space_count++;
                }
                else {
                    if (space_count == 1) {
                        if (word_count > 3) {
                            for (int j = i + 1; j < (int) strlen(const_command); j++) {
                                if (const_command[j] != ' ') {
                                    if (const_command[j] != '?') {
                                        printf("INVALID\n");
                                        return 0;
                                    }
                                }
                            }
                        }
                        space_count++;
                    }
                    space_count++;
                }
            }
            else {
                is_in_word = 1;
                space_count = 0;
            }
        }



        if (question_mark_word[strlen(question_mark_word)-1] != '?') {
            printf("INVALID\n");
            return 0;
        }
        for (int i = 3; i < array_length; i++) {
            char *token = parsed_command->line_array[i];
            if (is_word_token(token)) {
                if (previous_token == NULL) {
                    previous_token = token;
                } else if (is_word_token(previous_token)) {
                    previous_token = token;
                } else {
                    printf("INVALID\n");
                    return 0;
                }
            } else if (is_word_token_with_question_mark(token)) {
                if (previous_token == NULL) {
                    previous_token = token;
                } else if (is_word_token(previous_token)) {
                    previous_token = token;
                } else {
                    printf("INVALID\n");
                    return 0;
                }
            } else if (is_question_mark_token(token)) {
                if (previous_token == NULL) {
                    printf("INVALID\n");
                    return 0;
                }
                if (is_word_token(previous_token)) {
                    previous_token = token;
                } else {
                    printf("INVALID\n");
                    return 0;
                }
            } else {
                printf("INVALID\n");
                return 0;
            }
        }
        return 1;
    }
    // If the command does not fit into any case, it must be invalid
    else {
        printf("INVALID\n");
        return 0;
    }

}

/*
 * Method to handle loot commands
 */
void handle_loot(inventory *inv, const parsed_line *line) {
    int index = 2;  // skip "Geralt loots"
    while (index < (int)line->size) {
        // Skip any commas
        if (strcmp(line->line_array[index], ",") == 0) {
            index++;
            continue;
        }

        // Parse the quantity
        int quantity = string_to_int(line->line_array[index]);
        if (quantity <= 0) {
            return;
        }
        index++;

        // Get the ingredient name
        if (index >= (int)line->size) {
            return;
        }
        char *ing_name = line->line_array[index++];
        ing_name = remove_coma(ing_name);  // The commas are removed in parse(), but this also handles any edge cases

        // Add the ingredient to inventory
        ingredient *found = ingredient_in_inventory(inv, ing_name);
        if (found) {
            found->quantity += quantity;
        } else {
            ingredient *new_ing = create_ingredient(ing_name, quantity);
            new_ing->next = inv->ingredient_inventory;
            inv->ingredient_inventory = new_ing;
        }

        // Skip any commas
        if (index < (int)line->size && strcmp(line->line_array[index], ",") == 0) {
            index++;
        }
    }

    printf("Alchemy ingredients obtained\n");
}

/*
 * Method to handle trade commands
 */
void handle_trade(inventory *kept_inventory, const parsed_line *line) {
    // Trophy part of the code
    char *trophy_name = (char *) malloc(sizeof(char) * 2);
    int current_index = 2; // Set pointer to quantity of first trophy
    int quantity;


    // Initialize a dynamic trophy array
    dynamic_array *trophy_array = init_dynamic_array();

    // Get the trophy name and build it
    while (strncmp(line->line_array[current_index], "trophy", 6) != 0) {
        if (strcmp(line->line_array[current_index], ",") == 0) {
            current_index++;
            continue;
        }

        quantity = string_to_int(line->line_array[current_index]);
        trophy_name = check(realloc(trophy_name, strlen(line->line_array[current_index + 1]) + 1));
        strcpy(trophy_name, line->line_array[current_index+1]);
        trophy_name = remove_coma(trophy_name);


        trophy *candidate_trophy = search_trophy(kept_inventory, trophy_name);

        // If Geralt does not have enough trophies
        if (candidate_trophy == NULL || candidate_trophy->trophy_quantity < quantity) {
            printf("Not enough trophies\n");
            return;
        }

        // Add the trophy to the array
        add_to_array(trophy_array, candidate_trophy, quantity);

        // Skip intermediate words
        current_index += 2;
    }

    // Remove trophy from Geralt's inventory
    for (int i = 0; i < (int)trophy_array->size; i++) {
        trophy *pop_trophy = trophy_array->ptr_arr[i];
        pop_trophy->trophy_quantity -= trophy_array->int_arr[i];
    }
    free(trophy_name);
    free(trophy_array->ptr_arr);
    free(trophy_array->int_arr);
    free(trophy_array);



    // Ingredient part
    current_index += 2; // Set current index to quantity of first ingredient
    char *ingredient_name = (char *) malloc(sizeof(char) * 2);

    // Build the ingredient name, the logic is the same as with trophies
    while (current_index < (int)line->size) {
        if (strcmp(line->line_array[current_index], ",") == 0) {
            current_index++;
            continue;
        }

        quantity = string_to_int(line->line_array[current_index]);
        ingredient_name = check(realloc(ingredient_name, strlen(line->line_array[current_index + 1]) + 1));
        strcpy(ingredient_name, line->line_array[current_index+1]);
        ingredient_name = remove_coma(ingredient_name);

        ingredient *candidate_ingredient = ingredient_in_inventory(kept_inventory, ingredient_name);

        if (candidate_ingredient == NULL) {
            candidate_ingredient = create_ingredient(ingredient_name, quantity);
            candidate_ingredient->next = kept_inventory->ingredient_inventory;
            kept_inventory->ingredient_inventory = candidate_ingredient;
        } else {
            candidate_ingredient->quantity += quantity;
        }
        current_index += 2;
    }
    free(ingredient_name);
    printf("Trade successful\n");
}

/*
 * Method to handle learn commands
 */
void handle_learn(inventory *kept_inventory, const parsed_line *line) {
    int id = 2;

    // Skip the "potion" or "sign" keyword
    if (strcmp(line->line_array[id], "potion") == 0 || strcmp(line->line_array[id], "sign") == 0) {
        id++;
    }

    int name_start = id;
    size_t total_length = 0;
    size_t word_count = 0;

    // Count name words until reaching "is" or another known keyword (depends on your format)
    while (id < (int)line->size && (strcmp(line->line_array[id], "is") != 0 && strcmp(line->line_array[id], "consists") != 0)) {
        total_length += strlen(line->line_array[id]) + 1; // +1 for space/null
        word_count++;
        id++;
    }
    total_length -= strlen(line->line_array[id-1]);
    word_count--;

    // Get the name and the type, then build it
    char *type = malloc(strlen(line->line_array[id-1]) + 1);
    strcpy(type, line->line_array[id-1]);

    char *name = malloc(total_length);
    char *current_pos = name;

    for (int i = 0; i < (int)word_count; ++i) {
        if (i > 0) {
            *current_pos++ = ' ';
        }
        size_t len = strlen(line->line_array[name_start + i]);
        memcpy(current_pos, line->line_array[name_start + i], len);
        current_pos += len;
    }

    *current_pos = '\0';


    int is_effective = strcmp(line->line_array[line->size-3], "effective") != 0;

    // Potion learning
    if (is_effective != 0) {
        formula *candidate_formula = search_formula(kept_inventory, name);
        if (candidate_formula != NULL) {
            printf("Already known formula\n");
            return;
        }
        candidate_formula = create_formula(name);
        int index = id + 2;  // skip over "consists of"
        while (index < (int)line->size) {
            // Skip commas
            if (strcmp(line->line_array[index], ",") == 0) {
                index++;
                continue;
            }

            // Parse quantity
            int qty = string_to_int(line->line_array[index]);
            index++;

            // Parse ingredient name
            char *ing_name = line->line_array[index++];
            ing_name = remove_coma(ing_name);

            // Add to formula
            add_to_array(candidate_formula->ingredient_list, ing_name, qty);

            // Skip a trailing comma if present
            if (index < (int)line->size && strcmp(line->line_array[index], ",") == 0) {
                index++;
            }
        }
        candidate_formula->next_formula = kept_inventory->formula_inventory;
        kept_inventory->formula_inventory = candidate_formula;
        printf("New alchemy formula obtained: %s\n", name);
        return;
    }


    // Effectiveness
    char *monster_name = (char *) malloc(sizeof(char) * strlen(line->line_array[line->size - 1]) + 1);
    strcpy(monster_name, line->line_array[line->size-1]);

    // Get the monster from the bestiary and if does not exist, create a new bestiary
    bestiary *candidate_monster = search_bestiary(kept_inventory, monster_name);

    int is_known_monster = 1;

    if (candidate_monster == NULL) {
        printf("New bestiary entry added: %s\n", monster_name);
        candidate_monster = create_bestiary(monster_name, 1);
        candidate_monster->next_bestiary = kept_inventory->learned_bestiary;
        kept_inventory->learned_bestiary = candidate_monster;
        is_known_monster = 0;
    }

    // Learn potion effectiveness
    if (strcmp(type, "potion") == 0) {
        const char *candidate_potion = search_eff_potion(candidate_monster, name);
        if (candidate_potion != NULL) {
            printf("Already known effectiveness\n");
            return;
        }

        add_to_array(candidate_monster->effective_potions, name, 1);
        potion* inventory_potion = search_potion(kept_inventory, name);

        if (inventory_potion == NULL) {
            inventory_potion = create_potion(name, 0);
            inventory_potion->next_potion = kept_inventory->potion_inventory;
            kept_inventory->potion_inventory = inventory_potion;
        }
        if (is_known_monster)
            printf("Bestiary entry updated: %s\n", monster_name);
        return;
    }

    // Learn sign effectiveness
    if (strcmp(type, "sign") == 0) {
        const char* candidate_sign = search_eff_sign(candidate_monster, name);
        if (candidate_sign != NULL) {
            printf("Already known effectiveness\n");
            return;
        }
        add_to_array(candidate_monster->effective_signs, name, 1);
        if (is_known_monster)
            printf("Bestiary entry updated: %s\n", monster_name);
        return;
    }
}


/*
 * Method to handle brew commands
 */
void handle_brew(inventory *kept_inventory, const parsed_line *line) {
    int id = 2; // Starting name
    size_t total_length = 0;
    size_t word_count = 0;

    // Skip the "potion" keyword
    if (strcmp(line->line_array[id], "potion") == 0) {
        id++; // move past "potion"
    }

    int name_start = id;

    // Count words for potion name until end of line
    while (id < (int)line->size) {
        total_length += strlen(line->line_array[id]) + 1;
        word_count++;
        id++;
    }

    // Build the potion name using memcpy and pointers for more efficiency
    char *potion_name = (char *) malloc(total_length);
    char *current_pos = potion_name;

    for (int i = 0; i < (int)word_count; ++i) {
        if (i > 0) {
            *current_pos++ = ' ';
        }
        size_t len = strlen(line->line_array[name_start + i]);
        memcpy(current_pos, line->line_array[name_start + i], len);
        current_pos += len;
    }

    *current_pos = '\0';


    // Get the formula for the potion to brew
    formula *candidate_formula = search_formula(kept_inventory, potion_name);

    if (candidate_formula == NULL) {
        printf("No formula for %s\n", potion_name);
        free(potion_name); // Don't forget to free!
        return;
    }

    // Initialize the ingredient array and fill it with ingredients
    dynamic_array *ingredient_array = init_dynamic_array();

    for (int i = 0; i < (int)candidate_formula->ingredient_list->size; i++) {
        int needed_quantity = candidate_formula->ingredient_list->int_arr[i];
        char *ingredient_needed = (char *) candidate_formula->ingredient_list->ptr_arr[i];
        ingredient *candidate_ingredient = ingredient_in_inventory(kept_inventory, ingredient_needed);

        // Not enough ingredients case
        if (candidate_ingredient == NULL || candidate_ingredient->quantity < needed_quantity) {
            printf("Not enough ingredients\n");
            free(ingredient_array->int_arr);
            free(ingredient_array->ptr_arr);
            free(ingredient_array);
            return;
        }

        // Else add everything to ingredient array
        add_to_array(ingredient_array, candidate_ingredient, needed_quantity);
    }

    for (int i = 0; i < (int)ingredient_array->size; i++) {
        ingredient *curr_ingredient = (ingredient *) ingredient_array->ptr_arr[i];
        curr_ingredient->quantity -= ingredient_array->int_arr[i];
    }

    // Add potion to inventory
    potion *searched_potion = search_potion(kept_inventory, potion_name);
    if (searched_potion == NULL) {
        searched_potion = create_potion(potion_name, 1);
        searched_potion->next_potion = kept_inventory->potion_inventory;
        kept_inventory->potion_inventory = searched_potion;
        printf("Alchemy item created: %s\n", potion_name);
        free(ingredient_array->int_arr);
        free(ingredient_array->ptr_arr);
        free(ingredient_array);
        return;
    }

    // Increment the quantity if already exists in inventory
    searched_potion->potion_quantity++;
    free(ingredient_array->ptr_arr);
    free(ingredient_array->int_arr);
    free(ingredient_array);
    printf("Alchemy item created: %s\n", potion_name);
}

/*
 * Method to handle encounter commands
 */
void handle_encounter(inventory *kept_inventory, const parsed_line *line) {
    // Monster unknown case
    char *monster_name = (char *) malloc(sizeof(strlen(line->line_array[3])) + 1);
    strcpy(monster_name, line->line_array[3]);

    // If it's unknown, escape
    bestiary *candidate_monster = search_bestiary(kept_inventory, monster_name);
    if (candidate_monster == NULL) {
        free(monster_name);
        printf("Geralt is unprepared and barely escapes with his life\n");
        return;
    }

    // If does not know potions and no known signs against monster
    if (candidate_monster->effective_potions->size == 0 && candidate_monster->effective_signs->size == 0) {
        free(monster_name);
        printf("Geralt is unprepared and barely escapes with his life\n");
        return;
    }

    // Geralt defeats the monster if he knows a sign and doesn't know potions
    if (candidate_monster->effective_signs->size > 0) {
        printf("Geralt defeats %s\n", monster_name);
        trophy* trophy = search_trophy(kept_inventory, monster_name);
        if (trophy == NULL) {
            trophy = create_trophy(monster_name, 1);
            trophy->next_trophy = kept_inventory->trophy_inventory;
            kept_inventory->trophy_inventory = trophy;
        }
        else {
            trophy->trophy_quantity+=1;
        }
        free(monster_name);
        return;
    }

    int total_used_potion = 0;
    // Has potions
    for (int i = 0; i < (int)candidate_monster->effective_potions->size; i++) {
        potion* candidate_potion = search_potion(kept_inventory, ((char*)(candidate_monster->effective_potions->ptr_arr[i])));
        if (candidate_potion->potion_quantity > 0) {
            total_used_potion++;
            candidate_potion->potion_quantity--;
        }
    }

    if (total_used_potion == 0) {
        printf("Geralt is unprepared and barely escapes with his life\n");
        return;
    }
    printf("Geralt defeats %s\n", monster_name);
    trophy* trophy = search_trophy(kept_inventory, monster_name);
    if (trophy == NULL) {
        trophy = create_trophy(monster_name, 1);
        trophy->next_trophy = kept_inventory->trophy_inventory;
        kept_inventory->trophy_inventory = trophy;
    }
    else {
        trophy->trophy_quantity+=1;
    }
    free(monster_name);
}

/*
 * Method to simulate the witcher tracker
 */
void driver_code(inventory *inventory, char *command, char *const_command, parsed_line *parsed_command) {
    if (!validity_checker(command, const_command, parsed_command)) return;
    if (is_brew_sentence(parsed_command)) handle_brew(inventory, parsed_command);
    if (is_encounter_sentence(parsed_command)) handle_encounter(inventory, parsed_command);
    if (is_learn_sentence(parsed_command)) handle_learn(inventory, parsed_command);
    if (is_loot_sentence(parsed_command)) handle_loot(inventory, parsed_command);
    if (is_trade_sentence(parsed_command)) handle_trade(inventory, parsed_command);
    if (is_effective_query(parsed_command) || is_potion_formula_query( parsed_command) ||
        is_total_ingredient(parsed_command) || is_total_potion( parsed_command) ||
        is_total_trophy(parsed_command)) parse_query(inventory, parsed_command);
    if (is_exit(parsed_command)) exit(0);
}




int main(void) {

    char line[MAX_LINE_LENGTH]; // Line read from input
    parsed_line *parsed_command; // Parsed version of line
    inventory *kept_inventory = create_inventory();
    char command[MAX_LINE_LENGTH];

    // While the standard input is still running, take inputs
    while (1) {
        printf(">> ");
        fflush(stdout);

        if (!fgets(line, sizeof(line), stdin)) {
            break; // EOF REACHED !!!
        }

        line[strcspn(line, "\n")] = '\0'; // Trim any newline

        strcpy(command, line);
        trim_trailing_spaces(command); // Trim any trailing spaces for easier debugging
        parsed_command = parse(line); // Tokenize the input

        if (strcmp(parsed_command->line_array[0], "Exit") == 0 || strcmp(parsed_command->line_array[0], "exit") == 0) {
            break;
        }

        driver_code(kept_inventory, line, command, parsed_command);
        free(parsed_command->line_array);
        free(parsed_command);
    }

    return 0;
}
