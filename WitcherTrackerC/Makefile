# Compiler and flags
CC      := gcc

TARGET  := witchertracker

# Source files
SRCS    := src/main.c \
           src/bestiary.c \
           src/formula.c \
           src/ingredient.c \
           src/inventory.c \
           src/potion.c \
           src/sign.c \
           src/str_ops.c \
           src/trophy.c \
           src/bools.c

# Object files from source
OBJS    := $(SRCS:.c=.o)

.PHONY: default all grade clean

# Default build source codes
default: $(TARGET)

all: default

# Linking files
$(TARGET): $(OBJS)
	$(CC) -o $@ $^

# Compile in case any file is changed
%.o: %.c typedefs.h
	$(CC) -c $< -o $@

# Run the grader
grade: $(TARGET)
	python3 test/grader.py ./$(TARGET) test-cases

# Cleaning object files 
clean:
	@rm -f $(OBJS) $(TARGET)
