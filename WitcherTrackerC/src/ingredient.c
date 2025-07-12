#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "typedefs.h"

ingredient* create_ingredient(const char *name, int quantity) {
    ingredient* new_ingredient = (ingredient*)malloc(sizeof(ingredient));
    if (new_ingredient == NULL) {
        return NULL;
    }
    new_ingredient->name = (char*)malloc(strlen(name) + 1);
    if (new_ingredient->name == NULL) {
        free(new_ingredient);
        return NULL;
    }

    strcpy(new_ingredient->name, name);
    new_ingredient->quantity = quantity;
    return new_ingredient;
}

ingredient** init_ingredient_array() {
    return check((ingredient**)malloc(sizeof(ingredient*) * 1024));
}

void add_ingredient_to_array(ingredient** ingredient_array, ingredient* new_ingredient, int* current_index) {
    ingredient_array[*current_index] = new_ingredient;
    *current_index = *current_index + 1;
}

ingredient* pop_ingredient_array(ingredient** ingredient_array, int* current_index) {
    *current_index = *current_index - 1;
    return ingredient_array[*current_index];
}



void print_ingredient(const ingredient* ingredient) {
    if (ingredient == NULL) {
        return;
    }

    printf("Name: %s\nQuantity: %d\n ", ingredient->name, ingredient->quantity);
}



dynamic_array* init_dynamic_array() {
    dynamic_array* new_dynamic_array = (dynamic_array*)malloc(sizeof(dynamic_array));
    if (new_dynamic_array == NULL) {
        return NULL;
    }
    new_dynamic_array->size = 0;
    new_dynamic_array->capacity = 16;
    new_dynamic_array->ptr_arr = (void**)malloc(sizeof(void*) * new_dynamic_array->capacity);
    if (new_dynamic_array->ptr_arr == NULL) {
        free(new_dynamic_array);
        return NULL;
    }
    new_dynamic_array->int_arr = (int*)malloc(sizeof(int) * new_dynamic_array->capacity);
    if (new_dynamic_array->int_arr == NULL) {
        free(new_dynamic_array);
        return NULL;
    }

    return new_dynamic_array;
}

// Add to a dynamic array by data and quantity 
void add_to_array(dynamic_array* dyn_arr, void* element, int quantity) {
    // Realloc part
    if (dyn_arr->size == dyn_arr->capacity) {
        dyn_arr->capacity *= 2;
        dyn_arr->ptr_arr = (void**)realloc(dyn_arr->ptr_arr, sizeof(void*) * dyn_arr->capacity);
        if (dyn_arr->ptr_arr == NULL) {
            free(dyn_arr->int_arr);
            free(dyn_arr);
            return;
        }
        dyn_arr->int_arr = (int*)realloc(dyn_arr->int_arr, sizeof(int) * dyn_arr->capacity);
        if (dyn_arr->int_arr == NULL) {
            free(dyn_arr->ptr_arr);
            free(dyn_arr);
            return;
        }
    }

    dyn_arr->ptr_arr[dyn_arr->size] = element;
    dyn_arr->int_arr[dyn_arr->size] = quantity;
    dyn_arr->size++;
}


void* pop_from_array(dynamic_array* dyn_arr) {
    void* element = dyn_arr->ptr_arr[dyn_arr->size - 1];
    dyn_arr->size--;
    return element;
}
