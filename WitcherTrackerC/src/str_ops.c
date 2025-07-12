#include "typedefs.h"
#include <stdio.h>
#include <stdlib.h>
#include <string.h>

void trim_trailing_spaces(char *str) {
    int length = strlen(str);

    while (length > 0 && str[length - 1] == ' ') {
        length--;
    }

    str[length] = '\0';
}

// Function to check return values of allocations
void* check(void* ptr) {
    if (ptr == NULL)
    {
        perror("Null pointer exception / memory allocation failed.");
        exit(EXIT_FAILURE);
    }
    return ptr;
}



// Function for finding size of a string excluding spaces
size_t str_size(const char* string) {
    int length = 0;

    for (int i = 0; string[i] != '\0'; i++)
    {
        if (string[i] != ' ' && string[i] != '\n')
            length++;
    }

    return length;

}

// Prints parsed_line elements
void print_line(const parsed_line* p_line) {
    if (p_line == NULL || p_line->line_array == NULL)
        return;

    for (int i = 0; i < p_line->size; i++)
    {

        if (p_line->line_array[i] != NULL)
            printf("%s\n", p_line->line_array[i]);
    }
}

void remove_newline(char* line) {
    const size_t length = strlen(line);
    if (length > 0 && line[length - 1] == '\n')
        line[length - 1] = '\0';
}

 
char* remove_coma(char* input_string) {
    if (input_string == NULL || strlen(input_string) == 0)
        return NULL;

    const size_t length = strlen(input_string);
    if (input_string[length - 1] == ',')
        input_string[length -1] = '\0';
    if (input_string[0] == ',')
        input_string++;
    return input_string;
}

// Changes a string to an integer
int string_to_int(char* input_string) {
    if (input_string[0] == ',')
        input_string++;

    char* end_ptr;
    const int return_value = (int)strtol(input_string, &end_ptr, 10);
    if (*end_ptr != '\0')
    {
        printf("Conversion failed in string to integer conversion.\n");
        return -1;
    }
    return return_value;
}

// Parsees string by whitespaces
parsed_line* parse(char* line){
    if (line == NULL)
        return NULL;

    parsed_line* p_line = check((parsed_line*)malloc(sizeof(parsed_line)));
    p_line->size = 0;
    char** line_array = check((char**)malloc(sizeof(char*) * MAX_LINE_LENGTH));
    size_t str_length = str_size(line);


    // Delete new line character
    if (line[str_length] == '\n')
    {
        line[str_length] = '\0';
    }

    char* token = strtok(line, " "); // Get first word


    while (token != NULL)
    {    
        // Removes comma between elements
        if (is_word_token_with_comma_and_int(token)) {
            char* comma = strchr(token, ',');
            size_t length = comma - token;
            size_t num_length = strlen(comma + 1);

            line_array[p_line->size] = check((char*)malloc(length + 1));
            strncpy(line_array[p_line->size], token, length);
            line_array[p_line->size][length] = '\0';
            p_line->size++;

            line_array[p_line->size] = check((char*)malloc(2));
            line_array[p_line->size][0] = ',';
            line_array[p_line->size][1] = '\0';
            p_line->size++;

            line_array[p_line->size] = check((char*)malloc(num_length + 1));
            strcpy(line_array[p_line->size], comma + 1);
            p_line->size++;

            token = strtok(NULL, " \n");
        }
        else if (is_int_token_with_comma(token)) {
            char* comma = strchr(token, ',');
            if (comma != NULL && comma == token) {
                size_t num_length = strlen(comma + 1);


                line_array[p_line->size] = check((char*)malloc(2));
                line_array[p_line->size][0] = ',';
                line_array[p_line->size][1] = '\0';
                p_line->size++;


                line_array[p_line->size] = check((char*)malloc(num_length + 1));
                strcpy(line_array[p_line->size], comma + 1);
                p_line->size++;

                token = strtok(NULL, " \n");
            }
        }
        else {
            // Copy the word to array
            line_array[p_line->size] = check((char*)malloc(strlen(token) + 1)); // Initialize pointers
            strcpy(line_array[p_line->size], token);
            p_line->size++;
            // Parse next word
            token = strtok(NULL, " \n");
        }

    }

    p_line->line_array = line_array;
    return p_line;


}
