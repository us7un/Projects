#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#include "typedefs.h"

// Creates an unlinked bestiary
bestiary* create_bestiary(const char* monster_name, int monster_count) {

    // Allocate memory
    bestiary* new_bestiary = (bestiary*)malloc(sizeof(bestiary));
    if (new_bestiary == NULL) {
        return NULL;
    }

    // Write values to struct
    new_bestiary->monster_name = (char*)malloc(strlen(monster_name) + 1);
    if (new_bestiary->monster_name == NULL) {
        free(new_bestiary);
        return NULL;
    }

    strcpy(new_bestiary->monster_name, monster_name);
    new_bestiary->monster_count = monster_count;
    new_bestiary->next_bestiary = NULL;
    new_bestiary->effective_potions = init_dynamic_array();
    new_bestiary->effective_signs = init_dynamic_array();

    return new_bestiary;

}


// Function for searching a bestiary by monster name
bestiary* search_bestiary(inventory* kept_inventory, const char* monster_name) {
    // Linked list search
    bestiary* current = kept_inventory->learned_bestiary;

    while (current != NULL) {
        if (strcmp(current->monster_name, monster_name) == 0)
            return current;
        current = current->next_bestiary;
    }
    return NULL;
}

// Function for searching an effective potion against a monster by potion name
char* search_eff_potion(const bestiary* monster, const char* potion_name) {
    // Linked list search

    for (int i = 0; i < monster->effective_potions->size; i++) {
        if (strcmp(monster->effective_potions->ptr_arr[i], potion_name) == 0)
            return monster->effective_potions->ptr_arr[i];
    }
    return NULL;
}

// Function for searching an effective sign against a monster by sign name
char* search_eff_sign(const bestiary* monster, const char* sign_name) {



    for (int i = 0; i < monster->effective_signs->size; i++) {;
        if (strcmp(monster->effective_signs->ptr_arr[i], sign_name) == 0)
            return monster->effective_signs->ptr_arr[i];
    }
    return NULL;
}

// Function to print bestiaries inforaation
void print_bestiary(const bestiary* bestiary) {
    if (bestiary == NULL) {
        return;
    }
    printf("Monster name: %s\nMonster count: %d", bestiary->monster_name, bestiary->monster_count);
}
