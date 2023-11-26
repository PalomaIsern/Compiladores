.386
.model flat, stdcall
option casemap :none
include \masm32\include\windows.inc
include \masm32\include\kernel32.inc
include \masm32\include\user32.inc
includelib \masm32\lib\kernel32.lib
includelib \masm32\lib\user32.lib
.data
MaxNumUSHORT db 255MinNumLong dd -2147483648MaxNumLong dd 2147483647MinNumDouble dq 2.2250738585072014e-308MaxNumDouble dq 1.7976931348623157e+308@cteUS1 db 1 
@cteUS2 db 10 
@cteDouble1 dq 2.0 
id:main dq  ?
@cadena1 db cadena", 
d:main:c dq  ?
.code
START:
MOV EDX, 1
CMP EDX, 10
JGE Label5
MOV ECX, _-
add ECX, 2.0
MOV @aux2, ECX
MOV EAX, _-
MOV _-, EAX
JMP Label7
Label5:
Label7:
END START