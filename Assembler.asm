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
_b$main$ca dd  ?
_z$main$ca dq  ?
_do$main$ca dq  ?
_tre$main$ca dq  ?
_b$main$ca$m dq  ?
@ctePos8 db 3
@cte9 dq 2.0
@cadena10 db "b vale 2.0", 0
@cte12 dq 10.0
@ctePos13 db 10
@cadena14 db "si", 0
@cadena15 db "no", 0
_x$main db  ?
_y$main dq  ?
_z$main dq  ?
c1 db  ?
c2 db  ?
_a$c1$main db  ?
_a$c2$main db  ?
_b$c1$main dd  ?
_b$c2$main dd  ?
_z$c1$main dq  ?
_z$c2$main dq  ?
_do$c1$main dq  ?
_do$c2$main dq  ?
_tre$c1$main dq  ?
_tre$c2$main dq  ?
@cte31 dq 3.5
@ctePos32 db 1
@cadena33 db "c1_a igual a 3", 0
objetonuevo db  ?
_a$objetonuevo$main$preu db  ?
_b$objetonuevo$main$preu dd  ?
_z$objetonuevo$main$preu dq  ?
_do$objetonuevo$main$preu dq  ?
_tre$objetonuevo$main$preu dq  ?
@cadena41 db "es 10.0", 0
@cadena42 db "no es 10.0", 0
@aux1 dq  ?
@aux_sumaDouble dw 0 
  
.code
 
otro$main$ca: 
MOV dl, @ctePos13
MOVSX edx, dl
FILD dword ptr [@ctePos13] 
FSTP qword ptr [@ctePos13] 
FLD @cte12
FLD @aux1
FCOM 
JNE Label17  
invoke MessageBox, NULL, addr @cadena14, addr @cadena14, MB_OK
JMP Label19
Label17:
invoke MessageBox, NULL, addr @cadena15, addr @cadena15, MB_OK
Label19:
ret 
 
preu$main: 
FLD @cte12
FSTP _tre$objetonuevo$main$preu
FLD _tre$objetonuevo$main$preu
FLD @cte12
FCOM 
JNE Label39  
invoke MessageBox, NULL, addr @cadena41, addr @cadena41, MB_OK
JMP Label41
Label39:
invoke MessageBox, NULL, addr @cadena42, addr @cadena42, MB_OK
Label41:
ret 
 
m$main$ca: 
MOV dl, 3
MOV _a$main$ca, dl
MOV dl, _a$main$ca
MOV _a$main$ca, dl
FLD _b$main$ca$m
FLD @cte9
FCOM 
JNE Label7  
invoke MessageBox, NULL, addr @cadena10, addr @cadena10, MB_OK
JMP Label8
Label7:
Label8:
ret 
 
START:

FLD @cte9
FSTP _y$main
FLD @cte31
FSTP _b$main$ca$m
MOV dl, 3
MOV _a$c1$main, dl
MOV dl, _a$c1$main
MOV _a$c1$main, dl
FLD _b$main$ca$m
FLD @cte9
FCOM 
JNE Label7191
invoke MessageBox, NULL, addr @cadena10, addr @cadena10, MB_OK
JMP Label8191
Label7191:
Label8191:
MOV dl, 1
MOV _a$c1$main, dl
FLD _y$main
FSTP _b$main$ca$m
MOV dl, 3
MOV _a$c1$main, dl
MOV dl, _a$c1$main
MOV _a$c1$main, dl
FLD _b$main$ca$m
FLD @cte9
FCOM 
JNE Label7192
invoke MessageBox, NULL, addr @cadena10, addr @cadena10, MB_OK
JMP Label8192
Label7192:
Label8192:
MOV dl, _a$c1$main
CMP dl, 3
JNE Label31  
invoke MessageBox, NULL, addr @cadena33, addr @cadena33, MB_OK
JMP Label32
Label31:
Label32:
CALL preu$main

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

