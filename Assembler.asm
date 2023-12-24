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
_ca$main db  ?
_a$main$ca db  ?
_b$main$ca$m dq  ?
@ctePos4 db 3
@cte5 dq 2.0
@cadena6 db "b vale 2.0", 0
_x$main db  ?
_y$main dq  ?
_z$main dq  ?
c1 db  ?
c2 db  ?
@cte12 dq 3.5
_a$c1$main db  ?
@ctePos14 db 1
@cadena15 db "c1_a igual a 3", 0
@aux_sumaDouble dw 0 
  
.code
 
m$main$ca: 
MOV dl, 3
MOV _a$main$ca, dl
MOV dl, _a$main$ca
MOV _a$main$ca, dl
FLD _b$main$ca$m
FLD @cte5
FCOM 
JNE Label7
invoke MessageBox, NULL, addr @cadena6, addr @cadena6, MB_OK
JMP Label8
Label7:
Label8:
ret 
 
START:

FLD @cte5
FSTP _y$main
FLD @cte12
FSTP _b$main$ca$m
MOV dl, 3
MOV _a$c1$main, dl
MOV dl, _a$c1$main
MOV _a$c1$main, dl
FLD _b$main$ca$m
FLD @cte5
FCOM 
JNE Label7
invoke MessageBox, NULL, addr @cadena6, addr @cadena6, MB_OK
JMP Label8
Label7:
Label8:
MOV dl, 1
MOV _a$c1$main, dl
FLD _y$main
FSTP _b$main$ca$m
MOV dl, 3
MOV _a$c1$main, dl
MOV dl, _a$c1$main
MOV _a$c1$main, dl
FLD _b$main$ca$m
FLD @cte5
FCOM 
JNE Label7
invoke MessageBox, NULL, addr @cadena6, addr @cadena6, MB_OK
JMP Label8
Label7:
Label8:
MOV dl, _a$c1$main
CMP dl, 3
JNE Label21
invoke MessageBox, NULL, addr @cadena15, addr @cadena15, MB_OK
JMP Label22
Label21:
Label22:

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

