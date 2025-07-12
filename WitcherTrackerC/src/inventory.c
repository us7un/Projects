#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "typedefs.h"



inventory* create_inventory() {
    inventory* new_inventory = (inventory*)malloc(sizeof(inventory));
    if (!new_inventory) {
        return NULL;
    }

    new_inventory->ingredient_inventory = NULL;
    new_inventory->formula_inventory = NULL;
    new_inventory->learned_bestiary = NULL;
    new_inventory->learned_signs = NULL;
    new_inventory->potion_inventory = NULL;
    new_inventory->trophy_inventory = NULL;

    return new_inventory;

}

// Linked list search
ingredient* ingredient_in_inventory(inventory* kept_inventory, char* searched_ingredient) {
    ingredient* current = kept_inventory->ingredient_inventory;

    while (current != NULL)
    {
        if (strcmp(searched_ingredient, current->name) == 0) {
            return current;
        }

        current = current->next;
    }
    return NULL;
}

// Linked list search
trophy* search_trophy(inventory* kept_inventory, const char* trophy_name) {
    trophy* current = kept_inventory->trophy_inventory;

    while (current != NULL) {
        if (strncmp(current->trophy_name, trophy_name, strlen(trophy_name)) == 0)
            return current;
        current = current->next_trophy;
    }
    return NULL;
}


void print_ingredients_in_inventory(inventory* kept_inventory) {
    ingredient* current = kept_inventory->ingredient_inventory;

    while (current != NULL)
    {
        print_ingredient(current);
        current = current->next;
    }
}
