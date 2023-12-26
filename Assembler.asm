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
_clase1$main db  ?
_x$main$clase1 db  ?
_y$main$clase1 dq  ?
_d$main$clase1$f1$f2 db  ?
@ctePos6 db 2
@cadena7 db "Hola", 0
_t$main$clase1 dd  ?
@ctePos9 db 50
@ctePos10 db 40
_clase2$main$clase1 db  ?
_y$main$m dq  ?
_z$main$m dq  ?
@cte15 dq 2.5
@cadena16 db "z es igual a y", 0
@cadena17 db "son distintos", 0
_l$main$m dd  ?
@cadena19 db "Se hizo bien la conversion", 0
@cadena20 db "NO se hizo bien", 0
_zz$main$m$n dq  ?
_xx$main$m$n dq  ?
_ww$main$m$n dq  ?
_y$main$m$n dd  ?
_a$main$m$n dd  ?
@cteneg28 dd -500
@cadena29 db "cadena1", 0
@aux1 db  ?
@aux2 dq  ?
@aux3 dq  ?
@aux4 dq  ?
@aux5 dq  ?
@aux6 dq  ?
@aux7 db  ?
@aux8 dq  ?
@aux9 dq  ?
@aux10 dq  ?
@aux11 dq  ?
@aux12 dq  ?
@aux_sumaDouble dw 0 
  
@auxComp dw 0 
  
.code
 
n$main$m: 
FLD _zz$main$m$n
FLD _xx$main$m$n
FADD
FSTP @aux4
PUSHF 
FLD MaxNumDouble
FCOM @aux4
FSTSW ax 
SAHF 
JBE OFS 
POPF 
FILD dword ptr [_a$main$m$n] 
FLD @aux4
FLD @aux5
FADD
FSTP @aux6
PUSHF 
FLD MaxNumDouble
FCOM @aux6
FSTSW ax 
SAHF 
JBE OFS 
POPF 
FLD @aux6
FSTP _ww$main$m$n
ret 
 
f2$main$clase1$f1: 
MOV dl, 2
ADD dl, _d$main$clase1$f1$f2
MOV @aux7, dl
MOV dl, @aux7
MOVSX edx, dl
FILD dword ptr [@aux7] 
FSTP qword ptr [@aux7] 
FLD @aux8
FSTP _y$main$clase1
ret 
 
f1$main$clase1: 
CALL f2$main$clase1$f1
MOV dl, _x$main$clase1
MOVSX edx, dl
FILD dword ptr [_x$main$clase1] 
FSTP qword ptr [_x$main$clase1] 
FLD @aux9
FLD _y$main$clase1
FCOM 
FSTSW @auxComp 
MOV ax, @auxComp 
SAHF 
JLE Label13  
MOV dl, _x$main$clase1
MOV _d$main$clase1$f1$f2, dl
CALL f2$main$clase1$f1
JMP Label15
Label13:
invoke MessageBox, NULL, addr @cadena7, addr @cadena7, MB_OK
Label15:
ret 
 
m$main: 
FLD @cte15
FSTP _z$main$m
FLD @cte15
FSTP _y$main$m
FLD _z$main$m
FLD _y$main$m
FCOM 
FSTSW @auxComp 
MOV ax, @auxComp 
SAHF 
JNE Label31  
invoke MessageBox, NULL, addr @cadena16, addr @cadena16, MB_OK
JMP Label33
Label31:
invoke MessageBox, NULL, addr @cadena17, addr @cadena17, MB_OK
Label33:
FILD dword ptr [_l$main$m] 
FLD @aux10
FSTP _y$main$m
FILD dword ptr [_l$main$m] 
FLD _y$main$m
FLD @aux11
FCOM 
FSTSW @auxComp 
MOV ax, @auxComp 
SAHF 
JNE Label41  
invoke MessageBox, NULL, addr @cadena19, addr @cadena19, MB_OK
JMP Label43
Label41:
invoke MessageBox, NULL, addr @cadena20, addr @cadena20, MB_OK
Label43:
CALL n$main$m
Label50:
FILD dword ptr [@cteneg28] 
FLD @aux12
FSTP _y$main$m
FLD _y$main$m
FLD _z$main$m
FCOM 
FSTSW @auxComp 
MOV ax, @auxComp 
SAHF 
JE Label50  
invoke MessageBox, NULL, addr @cadena29, addr @cadena29, MB_OK
ret 
 
START:

MOV dl, 50
MOV _x$main$clase1, dl
MOV dl, _x$main$clase1
SUB dl, 40
JC OFR 
MOV @aux1, dl
MOV dl, @aux1
MOVSX edx, dl
FILD dword ptr [@aux1] 
FSTP qword ptr [@aux1] 
FLD _y$main$clase1
FLD @aux2
FADD
FSTP @aux3
PUSHF 
FLD MaxNumDouble
FCOM @aux3
FSTSW ax 
SAHF 
JBE OFS 
POPF 
FLD @aux3
FSTP _y$main$clase1
CALL f1$main$clase1
CALL m$main

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

