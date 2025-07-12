# Variables
EXEC        := evochirp
SRC         := src/evochirp.s
TEST_DIR    := test-cases
OUTPUT_DIR  := my-outputs
CFLAGS      := -nostartfiles -no-pie

.PHONY: default all testcases grade clean

default: all

all: $(EXEC)

$(EXEC): $(SRC)
	@echo "Assembling $< into $@..."
	gcc $(CFLAGS) -o $@ $<

testcases: $(EXEC)
	@echo "Running test cases..."
	@mkdir -p $(OUTPUT_DIR)
	@for infile in $(TEST_DIR)/input_*.txt; do \
		base=$$(basename $$infile); \
		outfile=$$(echo $$base | sed 's/input_/output_/'); \
		echo "  > $$infile -> $(OUTPUT_DIR)/$$outfile"; \
		./$(EXEC) < $$infile > $(OUTPUT_DIR)/$$outfile; \
	done

grade: $(EXEC) testcases
	python3 test/grader.py ./$(EXEC) $(TEST_DIR)

clean:
	@rm -f $(EXEC)
	@rm -rf $(OUTPUT_DIR)
