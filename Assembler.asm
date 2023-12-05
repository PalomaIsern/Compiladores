.386
.model flat, stdcall
option casemap :none
include \masm32\include\windows.inc
include \masm32\include\kernel32.inc
include \masm32\include\masm32.inc
include \masm32\include\user32.inc
includelib \masm32\lib\kernel32.lib
includelib \masm32\lib\masm32.lib
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
@cadenaInteriorIF db "InteriorIF"
@aux1 db  ?
@aux2 db  ?
@aux3 db  ?
@aux5 db  ?
@aux6 db  ?
.code
START:
FLD 2.0
FLD 1.0
FADD
FSTP @aux1
FLD @aux1
FSTP _x$main
FLD _x$main
FLD 3.0
FDIV
FSTP @aux2
FLD @aux2
FSTP _x$main
FLD _x$main
FLD _x$main
FMUL
FSTP @aux3
FLD @aux3
FSTP _x$main
FLD -5.0
FSTP _x$main
MOV EDX, 6
MOV _y$main, EDX
MOV EDX, 4.0
CMP EDX, 5.0
JGE Label12
invoke MessageBox, NULL, addr @cadena11, addr @cadena11, MB_OK
JMP Label13
Label12:
Label13:
FILD _y$main
FLD _x$main
FLD @aux4
FSUB
FSTP @aux5
MOV EDX, @aux5
MOV _y$main, EDX
MOV DL, 3
MOVSX EDX, DL
MOV EDX,  
IMUL EDX, _y$main
MOV @aux6, EDX
MOV EDX, @aux6
MOV _z$main, EDX
END START