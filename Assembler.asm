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
OverflowMultiplicacion db "Overflow en multiplicacion de enteros"
OverflowResta db "Resultado negativo en resta de enteros sin signo"
_x$main dq  ?
_y$main dd  ?
_z$main db  ?
@cadenaInteriorIF db "InteriorIF"
@aux1 db  ?
@aux2 db  ?
@aux3 db  ?
@aux5 db  ?
@aux7 db  ?
@aux9 db  ?
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
MOV ECX, 6
MOV _y$main, ECX
MOV CL, _z$main
MOVSX ECX, CL
FLD _z$main
FST _z$main
FLD _x$main
FLD @aux4
FADD
FSTP @aux5
FLD @aux5
FSTP _x$main
MOV ECX, 4.0
CMP ECX, 5.0
JGE Label15
invoke MessageBox, NULL, addr @cadena11, addr @cadena11, MB_OK
JMP Label16
Label15:
Label16:
FLD _y$main
FLD _x$main
FLD @aux6
FSUB
FSTP @aux7
MOV EAX, @aux7
MOV _y$main, EAX
MOV AL, 3
MOVSX EAX, AL
MOV EAX, @aux8
IMUL EAX, _y$main
MOV @aux9, EAX
JO OverFlowMul 
JMP ContinuarSinOverFlowMul 
OverFlowMul: 
invoke  MessageBox, NULL, ADDR OverFlowMultiplicacion, ADDR OverFlowMultiplicacion, MB_OK 
invoke ExitProcess, 0
ContinuarSinOverFlowMul: 
MOV EAX, @aux9
MOV _z$main, EAX
END START 