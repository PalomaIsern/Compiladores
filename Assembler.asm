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
_d1$main dq  ?
_d2$main dq  ?
_u1$main db  ?
_u2$main db  ?
_l1$main dd  ?
_l2$main dd  ?
@cte6 dq 1.0
@ctePos7 db 10
@cte8 dd 50
@cte9 dq 40.0
@cadena10 db "Bien", 0
@ctePos11 dd 2
@cte12 dd 58
@cadena13 db "bien", 0
@cte14 dq 59.0
@ctePos15 db 20
@cte16 dq 20.0
@cadena17 db "USHORT to DOUBLE anda", 0
@aux1 dd  ?
@aux2 dd  ?
@aux3 dq  ?
@aux4 dd  ?
@aux5 dd  ?
@aux6 dd  ?
@aux7 dd  ?
@aux8 dq  ?
@aux9 dq  ?
@aux10 dq  ?
@aux11 dd  ?
@aux12 dd  ?
@aux13 dq  ?
@aux_sumaDouble dw 0 
  
@auxComp dw 0 
  
@auxConversion dq 0 
.code
 
START:

FINIT 
FLD @cte6
FSTP _d1$main
MOV dl, @ctePos7
MOV _u1$main, dl
MOV edx, @cte8
MOV _l1$main, edx
MOV dl, _u1$main
MOVSX edx, dl
MOV @aux1, edx 
MOV edx, _l1$main
SUB edx, @aux1
MOV @aux2, edx
FILD dword ptr [@aux2] 
FSTP qword ptr [@aux3] 
FLD @aux3
FSTP _d2$main
FLD _d2$main
FLD @cte9
FCOM 
FSTSW @auxComp 
MOV ax, @auxComp 
SAHF 
JNE Label11  
invoke MessageBox, NULL, addr @cadena10, addr @cadena10, MB_OK
JMP Label12
Label11:
Label12:
MOV dl, _u1$main
MOVSX edx, dl
MOV @aux4, edx 
MOV edx, @aux4
ADD edx, _l1$main
MOV @aux5, edx
MOV dl, 2
MOVSX edx, dl
MOV @aux6, edx 
MOV edx, @aux5
SUB edx, @aux6
MOV @aux7, edx
MOV edx, @aux7
MOV _l1$main, edx
MOV edx, _l1$main
CMP edx, @cte12
JNE Label22  
invoke MessageBox, NULL, addr @cadena13, addr @cadena13, MB_OK
JMP Label23
Label22:
Label23:
FLD @cte14
FLD @cte6
FSUB
FSTP @aux8
FILD dword ptr [_l1$main] 
FSTP qword ptr [@aux9] 
FLD @aux9
FLD @aux8
FCOM 
FSTSW @auxComp 
MOV ax, @auxComp 
SAHF 
JNE Label30  
invoke MessageBox, NULL, addr @cadena13, addr @cadena13, MB_OK
JMP Label31
Label30:
Label31:
MOV dl, @ctePos15
MOVSX edx, dl
FILD dword ptr [@ctePos15] 
FSTP qword ptr [@aux10] 
FLD @aux10
FSTP _d2$main
FLD _d2$main
FLD @cte16
FCOM 
FSTSW @auxComp 
MOV ax, @auxComp 
SAHF 
JNE Label38  
invoke MessageBox, NULL, addr @cadena17, addr @cadena17, MB_OK
JMP Label39
Label38:
Label39:
MOV dl, 20
MOVSX edx, dl
MOV @aux11, edx 
MOV edx, @aux11
IMUL edx, @ctePos11
MOV @aux12, edx
JO OFML 
FILD dword ptr [@aux12] 
FSTP qword ptr [@aux13] 
FLD @aux13
FSTP _d2$main
FLD _d1$main
FLD _d2$main
FCOM 
FSTSW @auxComp 
MOV ax, @auxComp 
SAHF 
JNE Label48  
invoke MessageBox, NULL, addr @cadena13, addr @cadena13, MB_OK
JMP Label49
Label48:
Label49:

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

