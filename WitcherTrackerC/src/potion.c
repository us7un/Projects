#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "typedefs.h"


potion* create_potion(const char* potion_name, int potion_quantity) {
    potion* new_potion = (potion*)malloc(sizeof(potion));
    if (!new_potion) {
        return NULL;
    }
    new_potion->potion_name = (char*)malloc(strlen(potion_name) + 1);
    if (!new_potion->potion_name) {
        free(new_potion);
        return NULL;
    }

    strcpy(new_potion->potion_name, potion_name);
    new_potion->potion_quantity = potion_quantity;
    return new_potion;
}

// Linked list search
potion* search_potion(inventory* kept_inventory, const char* potion_name) {
    potion* current = kept_inventory->potion_inventory;
    while (current != NULL) {
        if (strcmp(current->potion_name, potion_name) == 0)
            return current;
        current = current->next_potion;
    }
    return NULL;
}


void print_potion(potion* pot) {
    if (pot == NULL) {
        return;
    }
    printf("Name: %s\nQuantity: %d", pot->potion_name, pot->potion_quantity);
}
