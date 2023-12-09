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
@aux3 dq  ?
@aux4 dq  ?
@aux5 dq  ?
@aux6 dq  ?
@aux7 db  ?
@aux8 dq  ?
@aux9 dq  ?
@aux10 dq  ?
@aux_sumaDouble dq ? 
  
.code
 
n$main$m: 
FLD _zz$main$m$n
FLD _xx$main$m$n
FADD
FSTP @aux4
FSTSW @aux_sumaDouble 
MOV bx, @aux_sumaDouble
SAHF 
JO OFS 
FLD _a$main$m$n
FLD @aux4
FLD @aux5
FADD
FSTP @aux6
FSTSW @aux_sumaDouble 
MOV bx, @aux_sumaDouble
SAHF 
JO OFS 
FLD @aux6
FSTP _ww$main$m$n
ret 
 
f2$main$clase1$f1: 
MOV dl, 2
ADD dl, _d$main$clase1$f1$f2
MOV @aux7, dl
MOV dl, @aux7
MOVSX edx, dl
FLD @aux7
FST @aux7
FLD @aux8
FSTP _y$main$clase1
ret 
 
f1$main$clase1: 
CALL f2$main$clase1$f1
MOV dl, _x$main$clase1
MOVSX edx, dl
FLD _x$main$clase1
FST _x$main$clase1
FILD @aux9
FILD _y$main$clase1
FCOM 
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
CALL n$main$m
Label30:
FLD -500
FLD @aux10
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
FLD _y$main$clase1
FLD @aux2
ADD
FSTP @aux3
FSTSW @aux_sumaDouble 
MOV bx, @aux_sumaDouble
SAHF 
JO OFS 
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