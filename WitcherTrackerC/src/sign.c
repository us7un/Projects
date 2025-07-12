#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "typedefs.h"

sign* create_sign(const char* sign_name) {
    sign* new_sign = (sign*)malloc(sizeof(sign));
    if (new_sign == NULL) {
        return NULL;
    }
    new_sign->sign_name = (char*)malloc(strlen(sign_name) + 1);
    if (new_sign->sign_name == NULL) {
        free(new_sign);
        return NULL;
    }

    strcpy(new_sign->sign_name, sign_name);
    new_sign->next_sign = NULL;
    return new_sign;
}

void print_sign(const sign* sign) {
    if (sign == NULL) {
        return;
    }
    printf("Sign Name: %s", sign->sign_name);
}
