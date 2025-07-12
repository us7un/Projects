#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#include "typedefs.h"

 
formula* create_formula(const char* potion_name) {
    formula* new_formula = (formula*)malloc(sizeof(formula));
    if (new_formula == NULL) {
        return NULL;
    }

    new_formula->potion_name = (char*)malloc(strlen(potion_name) + 1);
    strcpy(new_formula->potion_name, potion_name);

    new_formula->ingredient_list = init_dynamic_array();
    new_formula->next_formula = NULL;
    return new_formula;
}

// Linkwd list seacrh
formula* search_formula(inventory* kept_inventory, const char* potion_name) {
    formula* current = kept_inventory->formula_inventory;

    while (current != NULL) {
        if (strcmp(current->potion_name, potion_name) == 0) {
            return current;
        }
        current = current->next_formula;
    }
    return NULL;

}

void add_ingredient_to_formula(formula* formula, ingredient* new_ingredient, int quantity) {
    add_to_array(formula->ingredient_list, new_ingredient, quantity);
}


ingredient* search_ingredient_in_formula(formula* formula, const char* ingredient_name) {
    for (int i = 0; i < formula->ingredient_list->size; i++) {
        if (strcmp(((ingredient*)formula->ingredient_list->ptr_arr[i])->name, ingredient_name) == 0)
            return (ingredient*)formula->ingredient_list->ptr_arr[i];
    }
    return NULL;
}


void print_formula(const formula* formula) {
    if (formula == NULL) {
        return;
    }
    printf("Potion Name in Formula: %s", formula->potion_name);
}
