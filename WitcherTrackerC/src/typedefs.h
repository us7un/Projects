#ifndef TYPEDEFS_H
#define TYPEDEFS_H
#define MAX_LINE_LENGTH 1025

typedef unsigned long size_t;


typedef struct dynamic_array {
    void** ptr_arr;
    int* int_arr;
    size_t size;
    size_t capacity;
}dynamic_array;

dynamic_array* init_dynamic_array();
void add_to_array(dynamic_array* dyn_arr, void* element, int quantity);
void* pop_from_array(dynamic_array* dyn_arr);



typedef struct ingredient {
    char *name;
    int quantity;
    struct ingredient *next;
}ingredient;

ingredient* create_ingredient(const char *name, int quantity);
void print_ingredient(const ingredient* ingredient);
ingredient** init_ingredient_array();
void add_ingredient_to_array(ingredient** ingredient_array, ingredient* new_ingredient, int* current_index);
ingredient* pop_ingredient_array(ingredient** ingredient_array, int* current_index);


typedef struct potion {
    char* potion_name;
    int potion_quantity;
    struct potion* next_potion;
}potion;

potion* create_potion(const char* potion_name, int potion_quantity);
void print_potion(potion* pot);


typedef struct formula {
    char* potion_name;
    dynamic_array* ingredient_list;
    struct formula* next_formula;
}formula;

formula* create_formula(const char* potion_name);
void print_formula(const formula* formula);


typedef struct sign {
    char* sign_name;
    struct sign* next_sign;
}sign;

sign* create_sign(const char* sign_name);
void print_sign(const sign* sign);


typedef struct bestiary{
    char* monster_name;
    dynamic_array* effective_signs;
    dynamic_array* effective_potions;
    int monster_count;
    struct bestiary* next_bestiary;
}bestiary;

bestiary* create_bestiary(const char* monster_name, int monster_count);
void print_bestiary(const bestiary* bestiary);
char* search_eff_potion(const bestiary* monster, const char* potion_name);
char* search_eff_sign(const bestiary* monster, const char* sign_name);


typedef struct trophy {
    char* trophy_name;
    int trophy_quantity;
    struct trophy* next_trophy;
}trophy;

trophy* create_trophy(const char* trophy_name, int trophy_quantity);
void print_trophy(const trophy* trophy);
trophy** init_trophy_array();
void add_trophy_to_array(trophy** trophy_array, trophy* new_trophy, int* current_index);
trophy* pop_trophy_array(trophy** trophy_array, int* current_index);



typedef struct inventory {
    ingredient* ingredient_inventory;
    potion* potion_inventory;
    trophy* trophy_inventory;
    formula* formula_inventory;
    bestiary* learned_bestiary;
    sign* learned_signs;
}inventory;

inventory* create_inventory();
ingredient* ingredient_in_inventory(inventory* kept_inventory, char* searched_ingredient);
void print_ingredients_in_inventory(inventory* kept_inventory);
potion* search_potion(inventory* kept_inventory, const char* potion_name);
trophy* search_trophy(inventory* kept_inventory, const char* trophy_name);
formula* search_formula(inventory* kept_inventory, const char* potion_name);
bestiary* search_bestiary(inventory* kept_inventory, const char* monster_name);

typedef struct parsed_line {
    char** line_array;
    size_t size;
}parsed_line;

parsed_line* parse(char* command);
void* check(void* ptr);
size_t str_size(const char* string);
void print_line(const parsed_line* p_line);
void remove_newline(char* line);
char* remove_coma(char* input_string);
int string_to_int(char* input_string);



int is_loot_sentence(parsed_line* parsed_line);
int is_trade_sentence(parsed_line* parsed_line);
int is_brew_sentence(parsed_line* parsed_line);
int is_learn_sentence(parsed_line* parsed_line);
int is_encounter_sentence(parsed_line* parsed_line);


int is_total_ingredient(parsed_line* parsed_line);
int is_total_potion(parsed_line* parsed_line);
int is_total_trophy(parsed_line* parsed_line);
int is_effective_query(parsed_line* parsed_line);
int is_potion_formula_query(parsed_line* parsed_line);


int is_exit(parsed_line* parsed_line);

int is_int_token_with_comma(const char* token);
int is_int_token(const char* token);
int is_comma_token(const char* token);
int is_question_mark_token(const char* token);
int is_word_token(const char* token);
int is_word_token_with_comma(const char* token);
int is_word_token_with_question_mark(const char* token);
int is_word_token_with_comma_and_int(const char* token);


int is_valid_list(parsed_line* line, int start_index, int end_index);

int test_brew_sentence(parsed_line* parsed_line, const char* command);
int test_learn_sentence(parsed_line* parsed_line, const char* command);
int test_trade_sentence(parsed_line* parsed_line);
int test_encounter_sentence(parsed_line *parsed_command);
void trim_trailing_spaces(char* input_string);

#endif //TYPEDEFS_H
