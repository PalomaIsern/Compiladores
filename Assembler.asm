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
OverflowSuma db "Overflow en suma de punto flotante"
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
@aux2 db  ?
.code
 
f2$main$clase1$f1: 
MOV dl, 2
ADD dl, _d$main$clase1$f1$f2
MOV @aux3, dl
FLD @aux3
FSTP _y$main$clase1
ret 
 
f1$main$clase1: 
MOV dl, 2
ADD dl, _d$main$clase1$f1$f2
MOV @aux4, dl
FLD @aux4
FSTP _y$main$clase1
MOV dl, _x$main$clase1
MOVSX edx, dl
FLD _x$main$clase1
FST _x$main$clase1
MOV edx, @aux5
CMP edx, _y$main$clase1
JLE Label12
MOV dl, _x$main$clase1
MOV _d$main$clase1$f1$f2, dl
CALL f2$main$clase1$f1
JMP Label14
Label12:
invoke MessageBox, NULL, addr @cadena7, addr @cadena7, MB_OK
Label14:
ret 
 
n$main$m: 
FLD _zz$main$m$n
FLD _xx$main$m$n
FADD
FSTP @aux6
FSTSW aux_sumaDouble 
MOV bx, aux_sumaDouble
SAHF 
JO OverFlowSuma 
FLD _a$main$m$n
FLD @aux6
FLD @aux7
FADD
FSTP @aux8
FSTSW aux_sumaDouble 
MOV bx, aux_sumaDouble
SAHF 
JO OverFlowSuma 
FLD @aux8
FSTP _ww$main$m$n
ret 
 
m$main: 
FLD _zz$main$m$n
FLD _xx$main$m$n
FADD
FSTP @aux9
FSTSW aux_sumaDouble 
MOV bx, aux_sumaDouble
SAHF 
JO OverFlowSuma 
FLD _a$main$m$n
FLD @aux9
FLD @aux10
FADD
FSTP @aux11
FSTSW aux_sumaDouble 
MOV bx, aux_sumaDouble
SAHF 
JO OverFlowSuma 
FLD @aux11
FSTP _ww$main$m$n
Label28:
FLD -500
FSTP _y$main$clase1
JLE Label28
invoke MessageBox, NULL, addr @cadena22, addr @cadena22, MB_OK
ret 
 
START:

MOV dl, _x$main$clase1
SUB dl, 40
JC OverFlowResta 
MOV @aux1, dl
MOV edx, _y$main$clase1
ADD edx, @aux1
MOV @aux2, edx
FLD @aux2
FSTP _y$main$clase1
CALL f1$main$clase1
CALL m$main

OverFlowMul: 
invoke  MessageBox, NULL, ADDR OverFlowMultiplicacion, ADDR OverFlowMultiplicacion, MB_OK 
invoke ExitProcess, 0

OverFlowResta: 
invoke  MessageBox, NULL, ADDR OverFlowResta, ADDR OverFlowResta, MB_OK 
invoke ExitProcess, 0

OverFlowSuma: 
invoke  MessageBox, NULL, ADDR OverFlowSuma, ADDR OverFlowSuma, MB_OK 
invoke ExitProcess, 0

END START