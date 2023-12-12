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
OverFlowMultiplicacionL db "Overflow en multiplicacion de enteros largos con signo (LONG)", 0 
OverFlowMultiplicacionU db "Overflow en multiplicacion de enteros cortos sin signo (USHORT)", 0 
OverFlowResta db "Resultado negativo en resta de enteros sin signo", 0
OverFlowSuma db "Overflow en suma de punto flotante", 0 
_x$main dq  ?
_y$main dd  ?
_z$main db  ?
@cte3 dq 4.0
@cte4 dq 5.0
@cadena5 db "InteriorIF", 0
@cte6 dq 2.0
@cte7 dq 1.0
@cte8 dq 3.0
@cte9 dq -5.0
@cte10 dd 6
@ctePos11 db 1
@aux1 dq  ?
@aux2 dq  ?
@aux3 dq  ?
@aux4 dq  ?
@aux5 dq  ?
@aux_sumaDouble dw 0 
  
.code
 
START:

FLD @cte3
FLD @cte4
FCOM 
JGE Label4
invoke MessageBox, NULL, addr @cadena5, addr @cadena5, MB_OK
JMP Label5
Label4:
Label5:
FLD @cte6
FLD @cte7
FADD
FSTP @aux1
PUSHF 
FLD MaxNumDouble
FCOM @aux1
FSTSW ax 
SAHF 
JBE OFS 
POPF 
FLD @aux1
FSTP _x$main
FLD _x$main
FLD @cte8
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
FLD @cte9
FSTP _x$main
MOV edx, 6
MOV _y$main, edx
MOV dl, 1
MOV _z$main, dl
MOV dl, _z$main
MOVSX edx, dl
FILD dword ptr [_z$main] 
FSTP qword ptr [_z$main] 
FLD _x$main
FLD @aux4
FADD
FSTP @aux5
PUSHF 
FLD MaxNumDouble
FCOM @aux5
FSTSW ax 
SAHF 
JBE OFS 
POPF 
FLD @aux5
FSTP _x$main

JMP fin 
OFMU: 
invoke  MessageBox, NULL, ADDR OverFlowMultiplicacionU, ADDR OverFlowMultiplicacionU, MB_OK 
invoke ExitProcess, 0

OFML: 
invoke  MessageBox, NULL, ADDR OverFlowMultiplicacionL, ADDR OverFlowMultiplicacionL, MB_OK 
invoke ExitProcess, 0

OFR: 
invoke  MessageBox, NULL, ADDR OverFlowResta, ADDR OverFlowResta, MB_OK 
invoke ExitProcess, 0

OFS: 
invoke  MessageBox, NULL, ADDR OverFlowSuma, ADDR OverFlowSuma, MB_OK 
invoke ExitProcess, 0

fin: 
END START 
invoke ExitProcess, 0

