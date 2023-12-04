.386
.model flat, stdcall
option casemap :none
include \masm32\include\windows.inc
include \masm32\include\kernel32.inc
include \masm32\include\user32.inc
includelib \masm32\lib\kernel32.lib
includelib \masm32\lib\user32.lib
.data
MaxNumUSHORT db 255
MinNumLong dd -2147483648
MaxNumLong dd 2147483647
MinNumDouble dq 2.2250738585072014e-308
MaxNumDouble dq 1.7976931348623157e+308
_x$main dq  ?
_y$main dd  ?
_z$main db  ?
@cadena1 db "InteriorIF"
@aux2 db  ?
@aux3 db  ?
@aux4 db  ?
.code
START:
FLD 2.0
FADD ST(0), 1.0
FSTP @aux2
FLD @aux2
FSTP _x$main
FLD -5.0
FSTP _x$main
MOV EDX, 6
MOV _y$main, EDX
MOV ST(0), 4.0
CMP ST(0), 5.0
JGE Label8
JMP Label9
Label8:
Label9:
FILD _y$main
FLD _x$main
FSUB ST(0), @aux
FSTP @aux3
MOV  , @aux3
MOV _y$main,  
MOV BL, 3
MOV BH, 0
MOV BX, 3
MOV ECX, 0
MOV CX, BX
MOV EBX, ECX
MOV  , @aux
IMUL  , _y$main
MOV @aux4,  
MOV  , @aux4
MOV _z$main,  
END START