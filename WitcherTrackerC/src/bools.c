#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#include "typedefs.h"

// Method to determine if a token is an integer token with a comma attached to it
int is_int_token_with_comma(const char* token) {
   if (token[0] != ',')
      return 0;

   const char* new = token+1;
   return is_int_token(new);
}

// Method to determine if a token is an integer token
int is_int_token(const char *token) {
   if (*token == '\0') return 0;
   const char *current_character = token;
   if (*current_character == '0') return 0;
   while (*current_character) {
      if (*current_character < '0' || *current_character > '9') return 0;
      current_character++;
   }
   return 1;
}

// Method to determine if a token is a comma token
int is_comma_token(const char *token) {
   if (strcmp(token, ",") == 0) return 1;
   return 0;
}

// Method to determine if a token is a question mark token
int is_question_mark_token(const char *token) {
   if (strcmp(token, "?") == 0) return 1;
   return 0;
}

// Method to determine if a token is a word token
int is_word_token(const char *token) {
   if (*token == '\0') return 0;
   const char *current_character = token;
   while (*current_character) {
      if ((*current_character < 'A' || *current_character > 'Z') && (
             *current_character < 'a' || *current_character > 'z'))
         return 0;
      current_character++;
   }
   return 1;
}

// Method to determine if a token is a word token with a comma attached at the end
int is_word_token_with_comma(const char *token) {
   if (*token == '\0') return 0;
   const char *current_character = token;
   while (*current_character) current_character++; // Go to the end of the token
   if (*(current_character - 1) != ',') return 0;
   current_character = token;
   while (*(current_character + 1)) {
      if ((*current_character < 'A' || *current_character > 'Z') && (
             *current_character < 'a' || *current_character > 'z'))
         return 0;
      current_character++;
   }
   return 1;
}

// Method to determine if a token is a word token with a comma attached at the end
int is_word_token_with_question_mark(const char *token) {
   if (*token == '?') return 0;
   if (*token == '\0') return 0;
   const char *current_character = token;
   while (*current_character) current_character++; // Go to the end of the token
   if (*(current_character - 1) != '?') return 0;
   current_character = token;
   while (*(current_character + 1)) {
      if ((*current_character < 'A' || *current_character > 'Z') && (
             *current_character < 'a' || *current_character > 'z'))
         return 0;
      current_character++;
   }
   return 1;
}

// Method to determine annoying tokens like "Rebis,3" etc.
int is_word_token_with_comma_and_int(const char* token) {
   const char* comma = strchr(token, ',');
   if (!comma || comma == token || *(comma+1) == '\0')
      return 0;

   for (const char *p = token; p < comma; ++p) {
      if (! ( (*p >= 'A' && *p <= 'Z') || (*p >= 'a' && *p <= 'z') ) ) {
         return 0;
      }
   }

   for (const char *p = comma + 1; *p; ++p) {
      if (!(*p >= '0' && *p <= '9')) {
         return 0;
      }
   }

   return 1;
}

// Method to determine if a command is an exit command
int is_exit(parsed_line* parsed_command) {
   const int size = (int) parsed_command->size;
   if (size == 1 && (strcmp(parsed_command->line_array[0], "Exit") == 1 || strcmp(parsed_command->line_array[0], "exit") == 1 )) {
      return 1;
   }
   return 0;
}

// Method to determine if a command is a loot command
int is_loot_sentence(parsed_line *parsed_command) {
   const int size = (int) parsed_command->size;
   if (size > 2 && strcmp(parsed_command->line_array[0], "Geralt") == 0 && strcmp(parsed_command->line_array[1], "loots") == 0) {
      return 1;
   }
   return 0;
}

// Method to determine if a command is a trade command
int is_trade_sentence(parsed_line *parsed_command) {
   const int size = (int) parsed_command->size;
   if (size > 2 && strcmp(parsed_command->line_array[0], "Geralt") == 0 && strcmp(parsed_command->line_array[1], "trades") == 0) {
      return 1;
   }
   return 0;
}

// Method to determine if a command is a brew command
int is_brew_sentence(parsed_line *parsed_command) {
   const int size = (int) parsed_command->size;
   if (size > 2 && strcmp(parsed_command->line_array[0], "Geralt") == 0 && strcmp(parsed_command->line_array[1], "brews") == 0) {
      return 1;
   }
   return 0;
}

// Method to determine if a command is a learn command
int is_learn_sentence(parsed_line *parsed_command) {
   const int size = (int) parsed_command->size;
   if (size > 2 && strcmp(parsed_command->line_array[0], "Geralt") == 0 && strcmp(parsed_command->line_array[1], "learns") == 0) {
      return 1;
   }
   return 0;
}

// Method to determine if a command is an encounter command
int is_encounter_sentence(parsed_line* parsed_command) {
   const int size = (int) parsed_command->size;
   if (size > 3 && strcmp(parsed_command->line_array[0], "Geralt") == 0 && strcmp(parsed_command->line_array[1], "encounters") == 0 && strcmp(parsed_command->line_array[2], "a") == 0) {
      return 1;
   }
   return 0;
}

// Method to determine if a command is an ingredient query
int is_total_ingredient(parsed_line *parsed_command) {
   const int size = (int) parsed_command->size;
   if (size >= 2 && strcmp(parsed_command->line_array[0], "Total") == 0 && ((strcmp(parsed_command->line_array[1], "ingredient") == 0 || strcmp(parsed_command->line_array[1], "ingredient?") == 0))) {
      return 1;
   }
   return 0;
}

// Method to determine if a command is a potion query
int is_total_potion(parsed_line *parsed_command) {
   const int size = (int) parsed_command->size;
   if (size >= 2 && strcmp(parsed_command->line_array[0], "Total") == 0 && (strcmp(parsed_command->line_array[1], "potion") == 0 || strcmp(parsed_command->line_array[1], "potion?") == 0)) {
      return 1;
   }
   return 0;
}

// Method to determine if a command is a trophy query
int is_total_trophy(parsed_line *parsed_command) {
   const int size = (int) parsed_command->size;
   if (size >= 2 && strcmp(parsed_command->line_array[0], "Total") == 0 && (strcmp(parsed_command->line_array[1], "trophy") == 0 || strcmp(parsed_command->line_array[1], "trophy?") == 0)) {
      return 1;
   }
   return 0;
}

// Method to determine if a command is an effective query
int is_effective_query(parsed_line *parsed_command) {
   const int size = (int) parsed_command->size;
   if (size > 4 && strcmp(parsed_command->line_array[0], "What") == 0 && strcmp(parsed_command->line_array[1], "is") == 0 && strcmp(parsed_command->line_array[2], "effective") == 0 && strcmp(parsed_command->line_array[3], "against") == 0) {
      return 1;
   }
   return 0;
}

// Method to determine if a command is a potion formula query
int is_potion_formula_query(parsed_line *parsed_command) {
   const int size = (int) parsed_command->size;
   if (size > 3 && strcmp(parsed_command->line_array[0], "What") == 0 && strcmp(parsed_command->line_array[1], "is") == 0 && strcmp(parsed_command->line_array[2], "in") == 0) {
      return 1;
   }
   return 0;
}
