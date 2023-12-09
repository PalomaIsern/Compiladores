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
OverFlowMultiplicacion db "Overflow en multiplicacion de enteros"
OverFlowResta db "Resultado negativo en resta de enteros sin signo"
OverFlowSuma db "Overflow en suma de punto flotante"
_clase1$main db  ?
_x$main$clase1 db  ?
_y$main$clase1 dq  ?
_d$main$clase1$f1$f2 db  ?
@cadena7 db "Hola"
_t$main$clase1 dd  ?
_clase2$main$clase1 db  ?
_y$main$m dq  ?
_z$main$m dq  ?
_zz$main$m$n dq  ?
_xx$main$m$n dq  ?
_ww$main$m$n dq  ?
_y$main$m$n dd  ?
_a$main$m$n dd  ?
@cadena22 db "cadena1"
@aux1 db  ?
@aux2 dq  ?
@aux3 db  ?
.code
 
f2$main$clase1$f1: 
MOV dl, 2
ADD dl, _d$main$clase1$f1$f2
MOV @aux4, dl
MOV dl, @aux4
MOVSX edx, dl
FLD @aux4
FST @aux4
FLD @aux5
FSTP _y$main$clase1
ret 
 
f1$main$clase1: 
MOV dl, 2
ADD dl, _d$main$clase1$f1$f2
MOV @aux6, dl
MOV dl, @aux6
MOVSX edx, dl
FLD @aux6
FST @aux6
FLD @aux7
FSTP _y$main$clase1
MOV dl, _x$main$clase1
MOVSX edx, dl
FLD _x$main$clase1
FST _x$main$clase1
MOV edx, @aux8
CMP edx, _y$main$clase1
JLE Label13
MOV dl, _x$main$clase1
MOV _d$main$clase1$f1$f2, dl
CALL f2$main$clase1$f1
JMP Label15
Label13:
invoke MessageBox, NULL, addr @cadena7, addr @cadena7, MB_OK
Label15:
ret 
 
n$main$m: 
FLD _zz$main$m$n
FLD _xx$main$m$n
FADD
FSTP @aux9
FSTSW aux_sumaDouble 
MOV bx, aux_sumaDouble
SAHF 
JO OFS 
FLD _a$main$m$n
FLD @aux9
FLD @aux10
FADD
FSTP @aux11
FSTSW aux_sumaDouble 
MOV bx, aux_sumaDouble
SAHF 
JO OFS 
FLD @aux11
FSTP _ww$main$m$n
ret 
 
m$main: 
FLD _zz$main$m$n
FLD _xx$main$m$n
FADD
FSTP @aux12
FSTSW aux_sumaDouble 
MOV bx, aux_sumaDouble
SAHF 
JO OFS 
FLD _a$main$m$n
FLD @aux12
FLD @aux13
FADD
FSTP @aux14
FSTSW aux_sumaDouble 
MOV bx, aux_sumaDouble
SAHF 
JO OFS 
FLD @aux14
FSTP _ww$main$m$n
Label30:
FLD _-
FLD @aux15
FSTP _y$main$clase1
JLE Label30
invoke MessageBox, NULL, addr @cadena22, addr @cadena22, MB_OK
ret 
 
START:

MOV dl, _x$main$clase1
SUB dl, 40
JC OFR 
MOV @aux1, dl
MOV dl, @aux1
MOVSX edx, dl
FLD @aux1
FST @aux1
MOV edx, _y$main$clase1
ADD edx, @aux2
MOV @aux3, edx
FLD @aux3
FSTP _y$main$clase1
CALL f1$main$clase1
CALL m$main

OFM: 
invoke  MessageBox, NULL, ADDR OverFlowMultiplicacion, ADDR OverFlowMultiplicacion, MB_OK 
invoke ExitProcess, 0

OFR: 
invoke  MessageBox, NULL, ADDR OverFlowResta, ADDR OverFlowResta, MB_OK 
invoke ExitProcess, 0

OFS: 
invoke  MessageBox, NULL, ADDR OverFlowSuma, ADDR OverFlowSuma, MB_OK 
invoke ExitProcess, 0

END START