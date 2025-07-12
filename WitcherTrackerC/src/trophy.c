//
// Created by Ulaş Sertan KEMEÇ on 31.03.2025.
//

#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#include "typedefs.h"

trophy* create_trophy(const char* trophy_name, int trophy_quantity) {
    trophy* new_trophy = (trophy*)malloc(sizeof(trophy));
    if (new_trophy == NULL) {
        return NULL;
    }
    new_trophy->trophy_name = (char*)malloc(strlen(trophy_name) + 1);
    if (new_trophy->trophy_name == NULL) {
        free(new_trophy);
        return NULL;
    }

    strcpy(new_trophy->trophy_name, trophy_name);
    new_trophy->trophy_quantity = trophy_quantity;
    new_trophy->next_trophy = NULL;
    return new_trophy;
}

trophy** init_trophy_array() {
    return check((trophy**)malloc(sizeof(trophy*) * 1024));
}

void add_trophy_to_array(trophy** trophy_array, trophy* new_trophy, int* current_index) {
    trophy_array[*current_index] = new_trophy;
    *current_index = *current_index + 1;
}

trophy* pop_trophy_array(trophy** trophy_array, int* current_index) {
    *current_index = *current_index - 1;
    return trophy_array[*current_index];
}

void print_trophy(const trophy* trophy) {
    if (trophy == NULL) {
        return;
    }
    printf("Trophy Name: %s\nQuantity: %d\n", trophy->trophy_name, trophy->trophy_quantity);
}


int is_valid_list(parsed_line* line, int start_index, int end_index) {

    char* previous_str = NULL;


    // i = digit or i = name
    for (int i = start_index; i < end_index; i++) {

        if (is_int_token(line->line_array[i]) || (is_int_token_with_comma(line->line_array[i]) &&
            previous_str != NULL)) {

            if (i+1 == end_index) return 0;

            if (line->line_array[i+1] == NULL) {
                return 0;
            }



            if (!is_word_token(line->line_array[i+1]) && !is_word_token_with_comma(line->line_array[i+1])) {
                return 0;
            }


            if (previous_str == NULL) {
                previous_str = line->line_array[i];
                continue;
            }

            if (is_word_token_with_comma(previous_str) || is_word_token(previous_str) || is_comma_token(previous_str)) {
                previous_str = line->line_array[i];
                continue;
            }

            return 0;

        }
        else if (!is_comma_token(line->line_array[i])&&
            (is_word_token_with_comma(line->line_array[i]) || is_word_token(line->line_array[i]))) {

            if (previous_str == NULL) {
                return 0;
            }

            if (is_word_token_with_comma(line->line_array[i]) && i == end_index-1) {
                return 0;
            }

            if (is_int_token(previous_str)) {
                previous_str = line->line_array[i];
                continue;
            }



            return 0;

        } else if (is_comma_token(line->line_array[i])) {

            if (previous_str == NULL) {
                return 0;
            }

            if (is_word_token(previous_str)) {
                previous_str = line->line_array[i];
                continue;
            }

            return 0;
        }

        else {
            return 0;
        }
    }
    return 1;
}
