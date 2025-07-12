CXX      := g++
CXXFLAGS := -std=c++11 -Wall -Wextra

TARGET   := witchertracker

# Source files
SRCS     := src/main.cpp \
            src/Inventory.cpp \
            src/Game.cpp

# Object files
OBJS     := $(SRCS:.cpp=.o)

.PHONY: all clean grade

all: $(TARGET)

$(TARGET): $(OBJS)
	$(CXX) $(CXXFLAGS) -o $@ $^

%.o: %.cpp
	$(CXX) $(CXXFLAGS) -c $< -o $@

grade: $(TARGET)
	python3 test/grader.py ./$(TARGET) test-cases

clean:
	rm -f $(OBJS) $(TARGET)
