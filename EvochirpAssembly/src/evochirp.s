.global _start

.section .data
new_line: .byte 10 # ascii value of \n
space: .asciz " " # space
bird_sparrow: .asciz "Sparrow"
bird_nightingale: .asciz "Nightingale"
bird_warbler: .asciz "Warbler"
const_TC: .asciz "T-C"
const_DT: .asciz "D-T"
const_C: .asciz "C"
const_D: .asciz "D"
const_T: .asciz "T"
gen_string: .asciz "Gen "
colon_and_space: .asciz ": "



.section .bss
input_buffer: .space 257 # maximum size of input
token_array: .space 8200 # maximum number of tokens
song_array: .space 8200 # maximum number of notes
song_length: .quad 0
gen_count: .quad 0
merge_buffer: .space 4100 # X - Y \0
merge_buffer_pointer: .quad 0
number_buffer: .space 32 # buffer for itoa
number_buffer_end: # end of number buffer (BSS trick)
harmonize_buffer: .space 8200
harmonize_buffer_pointer: .quad 0


.section .text
_start: # main function

leaq merge_buffer(%rip), %rax
movq %rax, merge_buffer_pointer(%rip)
leaq harmonize_buffer(%rip), %rax
movq %rax, harmonize_buffer_pointer(%rip)


movq $0, %rax # read mode
movq $0, %rdi # open standard input
leaq input_buffer(%rip), %rsi # load address of input_buffer to source input
movq $256, %rdx # max number of characters to read
syscall # read characters and put them to rax

test %rax, %rax # invalid input, will never be reached
jle tokenize_done

movq %rax, %rcx # store number of bytes read in rcx
leaq input_buffer(%rip), %rsi # load address of input_buffer to source input
addq %rcx, %rsi # go to the end of read line
movb $0, (%rsi) # null terminate the line

leaq input_buffer(%rip), %rsi # load address of input_buffer to source input
addq %rcx, %rsi # go to the end of read line
decq %rsi # go before the null terminator we put
movzbq (%rsi), %rax # load that character to rax
cmpb $10, %al # is it newline?
je strip_newline # if so strip it
cmpb $13, %al # is it newline (unix systems)?
jne parse_input # if not, skip stripping



strip_newline: # function to strip newline from input line
movb $0, (%rsi) # change newline to null terminator (lazy strip)



parse_input: # input parser function
xorq %rbx, %rbx # empty the token count
leaq input_buffer(%rip), %rsi # load address of input_buffer to source input
leaq token_array(%rip), %rdi # load address of input_buffer to destination input


next_character: # subfunction to traverse the input line
movzbq (%rsi), %rax # load contents of source input into rax
cmpb $0, %al # end of line
je tokenize_done

cmpb $' ', %al # skip space before reading token
je skip_space

movq %rsi, (%rdi, %rbx, 8) # store current character pointer when a token starts
incq %rbx # go to the next character (hence the name of the subfunction)


read_token: # subfunction to read a token
incq %rsi
movzbq (%rsi), %rax # load next byte to rax
cmpb $' ', %al # is it space?
je terminate_token # if so terminate the token

cmpb $0, %al # is it null terminator?
je tokenize_done # if so we are done since we replaced newlines etc with null terminator

jmp read_token # lazy while loop


terminate_token: # subfunction to terminate a token
movb $0, (%rsi) # overwrite the space with null terminator
incq %rsi # go to next token
jmp next_character # parse next token


skip_space: # subfunction to skip spaces
incq %rsi # go to the next token
jmp next_character # parse next token


tokenize_done: # function to parse the species
leaq token_array(%rip), %r11
movq %rbx, %r10
shlq $3, %r10
addq %r10, %r11
movq $0, (%r11) # null terminate array

movq token_array(%rip), %rsi # load the token array to source input
movzbq (%rsi), %rax # get the first token into rax
cmpb $'S', %al # first letter of Sparrow
je case_sparrow # go to the specific marker function for that bird
cmpb $'N', %al # first letter of Nightingale
je case_nightingale # go to the specific marker function for that bird
cmpb $'W', %al # first letter of Warbler
je case_warbler # go to the specific marker function for that bird


case_sparrow: # marker function for Sparrow
movq $0, %r13 # the value for Sparrow is 0 (i picked it)
jmp species_done # species part has been handled


case_nightingale: # marker function for Nightingale
movq $1, %r13 # the value for Nightingale is 1
jmp species_done # species part has been handled


case_warbler: # marker function for Warbler
movq $2, %r13 # the value for Warbler is 2


species_done: # function to parse other tokens
movq $0, gen_count(%rip) # initialize generation counter
movq $0, song_length(%rip) # initialize song length
movq $1, %rbx # initialize token counter


process_token: # function to process remaining tokens
leaq token_array(%rip), %r11
movq (%r11 ,%rbx ,8), %rsi
cmpq $0, %rsi # is it a null terminator?
je exit_evochirp # if so, exit the program

movzbq (%rsi), %rax # get the token to rax to inspect it if its a note or an operator
cmpb $'C', %al # if its a chirp note
je add_note # process it
cmpb $'T', %al # if its a trill note
je add_note # process it
cmpb $'D', %al # if its a deep call note
je add_note # process it

movb %al, %r9b # move operator to r9b for easier handling and debugging
cmpb $'+', %r9b # if its a plus operator
je handle_plus # process it
cmpb $'-', %r9b # if its a minus operator
je handle_minus # process it
cmpb $'*', %r9b # if its a star operator
je handle_star # process it
cmpb $'H', %r9b # if its a harmony operator
je handle_harmony # process it


add_note: # function to append a note to the current sequence
movq song_length(%rip), %rax # load the song length counter to rax
leaq song_array(%rip), %rdx # load the address of next note to rdx
movq %rax, %rcx # load the current song length to rcx
shlq $3, %rcx # logical shift of 3 bits i.e. multiply by sizeof(pointer) = 8 to get the current index
addq %rcx, %rdx # get the location in the song_array
movq %rsi, (%rdx) # add the token (note) to that index
incq song_length(%rip) # increment song length
incq %rbx # increment token index
jmp process_token # return to while loop


handle_plus: # main handler method for plus operator
cmpq $0, %r13 # if its a Sparrow
je sparrow_plus # process it
cmpq $1, %r13 # if its a Nightingale
je nightingale_plus # process it
cmpq $2, %r13 # if its a Warbler
je warbler_plus # process it


handle_minus: # main handler method for minus operator
cmpq $0, %r13 # if its a Sparrow
je sparrow_minus # process it
cmpq $1, %r13 # if its a Nightingale
je nightingale_minus # process it
cmpq $2, %r13 # if its a Warbler
je warbler_minus # process it


handle_star: # main handler method for star operator
cmpq $0, %r13 # if its a Sparrow
je sparrow_star # process it
cmpq $1, %r13 # if its a Nightingale
je nightingale_star # process it
cmpq $2, %r13 # if its a Warbler
je warbler_star # process it




handle_harmony: # main handler method for harmony operator
cmpq $0, %r13 # if its a Sparrow
je sparrow_harmony # process it
cmpq $1, %r13 # if its a Nightingale
je nightingale_harmony # process it
cmpq $2, %r13 # if its a Warbler
je warbler_harmony # process it





sparrow_plus: # plus operator handler for Sparrow interpreter
movq song_length(%rip), %rax # load the song length counter to rax
cmpq $2, %rax # are there enough notes?
jl skip_operation # if not, do nothing

movq song_length(%rip), %rcx # load the current song length to rcx
decq %rcx # since we are merging two notes - 1st note
leaq song_array(%rip), %rdx # load the song array into rdx
movq (%rdx, %rcx, 8), %rdi # load the last note into destination input
decq %rcx # since we are merging two notes - 2nd note
movq (%rdx, %rcx, 8), %rsi # load the second last note into source input

subq $2, song_length(%rip) # delete the last two notes since we are merging them

movq merge_buffer_pointer(%rip), %rcx # load the merge buffer array into rcx
movb (%rsi), %al # load contents of first character into al
movb %al, (%rcx) # first character is the second last note
movb $'-', 1(%rcx) # second character is the dash
movb (%rdi), %al # load contents of last character into al
movb %al, 2(%rcx) # last character is the last note
movb $0, 3(%rcx) # null terminate the buffer

movq song_length(%rip), %rax # load the song length counter to rax
leaq song_array(%rip), %rdx # load the song array into rdx
shlq $3, %rax # multiply by 8
addq %rax, %rdx # go to the index of last note before the merged ones
movq %rcx, (%rdx) # store the merged note at that index
incq song_length(%rip) # increment the song length
addq $4, merge_buffer_pointer(%rip) # increment merge buffer pointer
jmp operation_done




nightingale_plus: # plus operator handler for Nightingale interpreter
movq song_length(%rip), %rax # load the song length counter to rax
cmpq $2, %rax # are there enough notes?
jl skip_operation # if not, do nothing

movq song_length(%rip), %rcx # load the current song length to rcx
decq %rcx # since we are duplicating two notes - 1st note
leaq song_array(%rip), %rdx # load the song array into rdx
movq (%rdx, %rcx, 8), %rdi # load the last note into destination input
decq %rcx # since we are duplicating two notes - 2nd note
movq (%rdx, %rcx, 8), %rsi # load the second last note into source input

movq song_length(%rip), %r10 # load the song length counter to r10
leaq song_array(%rip), %r11 # load the song array to r11
movq %r10, %r12 # move the counter to r12 for pointer arithmetic
shlq $3, %r12 # multiply by 8
addq %r12, %r11 # go to the position at song_array
movq %rsi, (%r11) # get the second last note to r11 (array)
incq song_length(%rip) # the first duplication has been handled

movq song_length(%rip), %r10 # load the song length counter to r10
leaq song_array(%rip), %r11 # load the song array to r11
movq %r10, %r12 # move the counter to r12 for pointer arithmetic
shlq $3, %r12 # multiply by 8
addq %r12, %r11 # go to the position at song_array
movq %rdi, (%r11) # get the last note to r11 (array)
incq song_length(%rip) # the second duplication has been handled
jmp operation_done



warbler_plus: # plus operator handler for Warbler interpreter
movq song_length(%rip), %rax # load the song length counter to rax
cmpq $2, %rax # are there enough notes?
jl skip_operation

subq $2, song_length(%rip) # delete the last two notes

movq song_length(%rip), %rax # load the song length counter to rax
leaq song_array(%rip), %rdx # load the song_array into rdx
shlq $3, %rax # multiply by 8
addq %rdx, %rax # go to the position at song_array
leaq const_TC(%rip), %rsi # get the T-C to source input
movq %rsi, (%rax) # put it in the array
incq song_length(%rip) # increment song_length
jmp operation_done


sparrow_minus: # minus operator handler for Sparrow interpreter
movq song_length(%rip), %rax # load the song length counter to rax
cmpq $1, %rax # are there enough notes?
jl skip_operation # if not, skip operation


movq %rax, %r15 # last index
decq %rax
movq %rax, %r14 # index of last note
xorq %r8, %r8 # we will use this as loop index
movq $-1, %r9 # max softness is -1
xorq %r10, %r10 # index of the softest

scan_softest: # subfunction to scan for softest note
cmpq %r15, %r8 # end of loop?
jge shift_array
leaq song_array(%rip), %r11 # load the song array into r11
movq (%r11, %r8, 8), %rsi # load the current note into source input
movzbq (%rsi), %rcx # move the note to rcx
cmpb $'C', %cl # is it chirp?
je soft # its soft
cmpb $'T', %cl # is it trill?
je medium # its medium
cmpb $'D', %cl # is it deep call?
je hard # its hard
jmp softness_done

soft:
movq $2, %r12 # highest softness
jmp softness_done

medium:
movq $1, %r12 # medium softness
jmp softness_done

hard:
xorq %r12, %r12 # lowest softness
jmp softness_done

softness_done: # loopback subfunction
cmpq %r12, %r9 # is the current note the softest?
jge next_note # if not, go to the next one
movq %r12, %r9 # if it's the softest, update softest index
movq %r8, %r10 # store the softest index

next_note:
incq %r8 # increment loop index
jmp scan_softest

shift_array: # prepare for removal of softest note
movq %r14, %r11 # index of length
movq %r10, %r8 # softest index


removal_loop: # remove the softest note
cmpq %r14, %r8 # have we shifted until the softest note
jge finish_removal # if so we are done
leaq song_array(%rip), %rcx # load the song array into rcx
movq (%rcx, %r8, 8), %rdi # load the current note to destination input
movq 8(%rcx, %r8, 8), %rsi # load the next note into source input
movq %rsi, (%rcx, %r8, 8) # the current is replaced with the next
incq %r8 # increment loop index
jmp removal_loop

finish_removal: # we are done
decq song_length(%rip)
jmp operation_done


nightingale_minus: # minus operator handler for Nightingale interpreter
movq song_length(%rip), %rax # load the song length counter to rax
cmpq $2, %rax # are there enough notes?
jl skip_operation # if not, skip

leaq song_array(%rip), %rcx # load the song_array into rcx
movq %rax, %r10 # store the last index at r10
decq %r10 # index of last note
movq %r10, %r11 # store the last note's index at r11
decq %r11 # index of second last note

movq (%rcx, %r11, 8), %rsi # move the second last note into source input
movq (%rcx, %r10, 8), %rdi # move the last note into destination input
movzbq (%rsi), %r11 # move second last note into r11
movzbq (%rdi), %r10 # move last note into r10
cmpb %r11b, %r10b # are they a repetition?
jne skip_operation # if not skip

decq song_length(%rip) # remove the last note if its a repetition
jmp operation_done




warbler_minus: # minus operator handler for Warbler interpreter
movq song_length(%rip), %rax # load the song length counter to rax
cmpq $1, %rax # are there enough notes?
jl skip_operation # if not skip

decq song_length(%rip) # delete last note
jmp operation_done




sparrow_star: # star operator handler for Sparrow interpreter
movq song_length(%rip), %rcx # load the song length counter to rcx
cmpq $1, %rcx # are there enough notes?
jl skip_operation
decq %rcx # index of last note
leaq song_array(%rip), %rdx # load the song_array into rdx
movq (%rdx, %rcx, 8), %rdi # load the last note into destination input

movq song_length(%rip), %r10 # load the song length counter to r10
leaq song_array(%rip), %r11 # load the song array into r11
movq %r10, %r12 # move the counter to r12 for pointer arithmetic
shlq $3, %r12 # multiply by 8
addq %r12, %r11 # go to the position in song_array
movq %rdi, (%r11) # insert the duplicated note
incq song_length(%rip) # increment the song_length
jmp operation_done


nightingale_star: # star operator handler for Nightingale interpreter
movq song_length(%rip), %rcx # load the song length counter to rcx
cmpq $1, %rcx # are there enough notes?
jl skip_operation
leaq song_array(%rip), %rdx # load the song_array into rdx
movq song_length(%rip), %r10 # load the song length counter to r10
xorq %r15, %r15 # empty the r15 register, will use as loop index

repeat_loop: # main loop for nightingale_star
cmpq %r10, %r15 # have we duplicated the entire sequence?
jge repeat_done # if so we are done
movq (%rdx, %r15, 8), %rdi # load the current note to destination input
movq song_length(%rip), %rsi # load the song length to source input so we can modify it
shlq $3, %rsi # multiply by 8
addq %rdx, %rsi # go to the last index
movq %rdi, (%rsi) # append the repeated note
incq song_length(%rip) # increment the song_length
incq %r15 # increment the loop intex
jmp repeat_loop


warbler_star: # star operator handler for Warbler interpreter
jmp nightingale_plus # the same operation as nightingale_plus


sparrow_harmony: # harmony operator handler for Sparrow interpreter
movq song_length(%rip), %rcx # load the song length counter to rcx
cmpq $1, %rcx # are there enough notes?
jl skip_operation
leaq song_array(%rip), %rdx # load the song_array into rdx
xorq %r14, %r14 # loop index

sparrow_harmony_loop: # main loop for sparrow harmony
cmpq %rcx, %r14 # end of loop
jge operation_done
movq (%rdx, %r14, 8), %rsi # move current note to source input
movzbq (%rsi), %rax # move current note to rax
cmpb $'C', %al # if its a chirp note
je chirp_to_trill
cmpb $'T', %al # if its a trill note
je trill_to_chirp
cmpb $'D', %al # if its a deep call note
je deepcall_to_dt
jmp loopback

chirp_to_trill: # subfunction to transform C to T 
leaq const_T(%rip), %rsi # load 'T' to source input (current note)
jmp modify_note

trill_to_chirp: # subfunction to transform T to C
leaq const_C(%rip), %rsi # load 'C' to source input (current note)
jmp modify_note

deepcall_to_dt: # subfunction to transform D to D-T
leaq const_DT(%rip), %rsi # load 'D-T' to source input (current note)
jmp modify_note

modify_note: # subfunction to change a note
movq %rsi, (%rdx, %r14, 8) # modify the note
jmp loopback

loopback:
incq %r14 # increment loop index
jmp sparrow_harmony_loop


nightingale_harmony: # harmony operator handler for Nightingale interpreter
movq song_length(%rip), %rcx # load song length counter to rcx
cmpq $3, %rcx # are there enough notes?
jl skip_operation
leaq song_array(%rip), %rdx # load song array into rdx

movq %rcx, %r10 # end index
decq %r10 # last note
movq (%rdx, %r10, 8), %rdi # put last note into destination input
decq %r10 # second last note
movq (%rdx, %r10, 8), %rsi # put second last note into source input
decq %r10 # third last note
movq (%rdx, %r10, 8), %r11 # put third last note into r11

movq harmonize_buffer_pointer(%rip), %rax # put harmonize_buffer into rax
movb (%r11), %r8b # third last note
movb %r8b, 0(%rax) # put it in buffer
movb $'-', 1(%rax) # dash
movb (%rdi), %r8b # last note
movb %r8b, 2(%rax) # put it in buffer
movb $0, 3(%rax) # null terminate
addq $4, harmonize_buffer_pointer(%rip) # increment pointer

movq harmonize_buffer_pointer(%rip), %r9 # put harmonize_buffer into r9
movb (%rsi), %r8b # second last note
movb %r8b, 0(%r9) # put it in buffer
movb $'-', 1(%r9) # dash
movb (%r11), %r8b # third last note
movb %r8b, 2(%r9) # put it in buffer
movb $0, 3(%r9) # null terminate
addq $4, harmonize_buffer_pointer(%rip) # increment pointer

subq $3, song_length(%rip) # remove last three notes

movq harmonize_buffer_pointer(%rip), %rax # load the second last harmonized note
subq $8, %rax
movq song_length(%rip), %rcx # load song length counter into rcx
shlq $3, %rcx # multiply by 8
leaq song_array(%rip), %rdx # load song array into rdx
addq %rcx, %rdx # go to desired location
movq %rax, (%rdx) # store harmonized note there
incq song_length(%rip) # increment song length

movq harmonize_buffer_pointer(%rip), %rax # load the last harmonized note
subq $4, %rax
movq song_length(%rip), %rcx # load song length counter into rcx
shlq $3, %rcx # multiply by 8
leaq song_array(%rip), %rdx # load song array into rdx
addq %rcx, %rdx # go to desired location
movq %rax, (%rdx) # store harmonized note there
incq song_length(%rip) # increment song length

jmp operation_done



warbler_harmony: # harmony operator handler for Warbler interpreter
movq song_length(%rip), %rax # load the song length counter to rax
cmpq $1, %rax # are there enough notes?
jl skip_operation # if not, skip
movq song_length(%rip), %r10 # load the song length counter to r10
leaq song_array(%rip), %r11 # load the song_array to r11
movq %r10, %r12
shlq $3, %r12 # multiply by 8
addq %r12, %r11 # go to the last index

leaq const_T(%rip), %rsi # load 'T' into source input
movq %rsi, (%r11) # put it at the last index

incq song_length(%rip) # increment song length

jmp operation_done



repeat_done: # just for a cleaner logic
jmp operation_done



skip_operation: # not enough notes means no effect
jmp operation_done


operation_done: # subfunction to finalize processing of plus operator
jmp print_gen


print_gen: # function to print generations
cmpq $0, %r13 # if its a Sparrow
je print_sparrow # process it
cmpq $1, %r13 # if its a Nightingale
je print_nightingale # process it
cmpq $2, %r13 # if its a Warbler
je print_warbler # process it

print_sparrow:
leaq bird_sparrow(%rip), %rsi # load "Sparrow" to source input
movq $7, %rdx # length of "Sparrow"
jmp print_common

print_nightingale:
leaq bird_nightingale(%rip), %rsi # load "Nightingale" to source input
movq $11, %rdx # length of "Nightingale"
jmp print_common

print_warbler:
leaq bird_warbler(%rip), %rsi # load "Warbler" to source input
movq $7, %rdx # length of "Warbler"
jmp print_common

print_common:
movq $1, %rax # write mode
movq $1, %rdi # standard output
syscall # print

leaq space(%rip), %rsi # put space to source input
movq $1, %rdx # length of space
movq $1, %rax
movq $1, %rdi
syscall # print it

leaq gen_string(%rip), %rsi # put "Gen " to source input
movq $4, %rdx # length of "Gen "
movq $1, %rax
movq $1, %rdi
syscall # print it


movq gen_count(%rip), %rax # load generation count to rax
leaq number_buffer_end(%rip), %rcx # prepare for itoa operation
xorq %r9, %r9 # empty r9 (we used it for operators before, now we use it for digit count)
cmpq $0, %rax # gen 0 is a special case, we don't want DIV:0 errors
jne itoa # go on with processing itoa operation
decq %rcx # go to the last letter of number_buffer
movb $'0', (%rcx) # make it '0'
movq $1, %r9 # '0' has 1 digit
jmp after_itoa

itoa: # integer to ascii
xorq %rdx, %rdx # empty rdx
movq $10, %r10 # divisor is 10 for decimal numbers
divq %r10 # now our quotient will go to rax and remainder to rdx
addb $'0', %dl # convert remainder to ascii
decq %rcx # go to the last letter of number_buffer
movb %dl, (%rcx) # make it that digit
incq %r9 # increment digit count
cmpq $0, %rax # is the whole number processed?
jne itoa

after_itoa: # finish integer to ascii and print the gen number
movq $1, %rax
movq $1, %rdi
movq %rcx, %rsi # pointer to first digit
movq %r9, %rdx # string length is the number of digits
syscall # print

leaq colon_and_space(%rip), %rsi
movq $2, %rdx
movq $1, %rax
movq $1, %rdi
syscall # print ": "

incq gen_count(%rip) # increment gen_count

movq song_length(%rip), %r12 # move number of notes to r12
xorq %r14, %r14 # empty the r14 register, will use as for loop index

note_loop: # print the notes one by one
cmpq %r12, %r14 # have we reached the end?
jge after_notes # if so, terminate loop
leaq song_array(%rip), %rdx # load song array to rdx
movq (%rdx, %r14, 8), %rsi # move current note to source input

xorq %r9, %r9 # empty the r9 register, will use as strlen
strlen: # compute length of note to be printed
movzbq (%rsi, %r9, 1), %rax # get the character at the nth index
cmpb $0, %al # is it a null terminator?
je after_strlen
incq %r9 # keep traversing
jmp strlen # loop

after_strlen: # print the note
movq $1, %rax
movq $1, %rdi
# implicit movq rsi to rsi here!
movq %r9, %rdx # length of note
syscall # print note

leaq space(%rip), %rsi
movq $1, %rdx
movq $1, %rax
movq $1, %rdi
syscall # print space

incq %r14 # increment loop index
jmp note_loop # loop

after_notes: # print newline and continue
leaq new_line(%rip), %rsi
movq $1, %rdx
movq $1, %rax
movq $1, %rdi
syscall # print newline

incq %rbx # increment token index for operators
jmp process_token # loop back to process the next note / operator



exit_evochirp:
movq $60, %rax # exit mode
xorq %rdi, %rdi # exit code (0)
syscall # exit program

