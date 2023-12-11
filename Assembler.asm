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
_us$main db  ?
_clase$main db  ?
_largo$main$clase dd  ?
@cte4 dd 1000099
_l$main$clase$func dd  ?
@cte7 dd 100000
_clase2$main db  ?
_dob1$main$clase2 dq  ?
_dob2$main$clase2 dq  ?
@cte11 dq 0.45
@cte12 dq 0.006
objeto1 db  ?
objeto2 db  ?
_largo$objeto1$main dd  ?
@cte17 dd 29999
@cte18 dd 200000
@cadena19 db "cadena de prueba", 0
_variable_doble$main dq  ?
_variable_doble$main$prueba dd  ?
@cte23 dd 5000
_aux$main dd  ?
_corto$main db  ?
@cadena27 db "mensaje uno", 0
@cte28 dd 40000
@cadena29 db "es menor a cuarenta mil", 0
@cadena30 db "es mayor a cuarenta mil", 0
@cte32 dd 50000
_ca$main db  ?
_a$main$ca db  ?
@cadena36 db "estoy en m", 0
_cb$main db  ?
_b$main$cb dq  ?
_c$main$cb dq  ?
@cadena41 db "estoy en n", 0
a1 db  ?
a2 db  ?
b1 db  ?
b2 db  ?
b3 db  ?
_a$a1$main db  ?
_b$b1$main dq  ?
@cte50 dq 1.2
_a$b2$main db  ?
_formal$main$asigna_valor dd  ?
@cte55 dd 60000
_real$main dd  ?
@cte57 dd 100
@cadena58 db "esta bien", 0
@cadena59 db "esta mal", 0
_aux$main$main dd  ?
_aux_2$main$main dq  ?
@cte63 dq 0.005
_d$main$funcion_main dq  ?
_us$main$funcion_main db  ?
_xus$main db  ?
_xl$main dd  ?
@cte70 dd 10000
_xd$main dq  ?
@cte72 dq 1000000.0
@aux1 db  ?
@aux2 dd  ?
@aux3 dd  ?
@aux4 dq  ?
@aux5 dd  ?
@aux_sumaDouble dw ? 
  
.code
 
main$main: 
FLD @cte63
FSTP _aux_2$main$main
ret 
 
func2$main$clase2: 
invoke MessageBox, NULL, addr @cadena19, addr @cadena19, MB_OK
ret 
 
m$main$ca: 
invoke MessageBox, NULL, addr @cadena36, addr @cadena36, MB_OK
ret 
 
prueba$main: 
FILD dword ptr [@cte23] 
FLD @aux4
FSTP _variable_doble$main
ret 
 
prueba2$main: 
MOV edx, 50000
MOV _largo$main$clase, edx
ret 
 
func$main$clase: 
MOV edx, _l$main$clase$func
ADD edx, 100000
MOV @aux5, edx
MOV edx, @aux5
MOV _l$main$clase$func, edx
ret 
 
n$main$cb: 
invoke MessageBox, NULL, addr @cadena41, addr @cadena41, MB_OK
ret 
 
asigna_valor$main: 
MOV edx, 60000
MOV _formal$main$asigna_valor, edx
ret 
 
funcion_main$main: 
ret 
 
START:

MOV dl, 255
MOV _us$main, dl
MOV edx, 1000099
MOV _largo$main$clase, edx
FLD @cte11
FSTP _dob1$main$clase2
FLD @cte12
FSTP _dob2$main$clase2
MOV edx, 29999
MOV _largo$main$clase, edx
MOV edx, 200000
MOV _l$main$clase$func, edx
CALL func$main$clase
MOV edx, 29999
MOV _aux$main, edx
MOV dl, 50
MOV _corto$main, dl
MOV dl, _corto$main
ADD dl, 255
MOV @aux1, dl
MOV dl, @aux1
MOV _corto$main, dl
MOV dl, _corto$main
CMP dl, 255
JLE Label28
invoke MessageBox, NULL, addr @cadena27, addr @cadena27, MB_OK
JMP Label30
Label28:
invoke MessageBox, NULL, addr @cadena27, addr @cadena27, MB_OK
Label30:
MOV edx, _aux$main
CMP edx, 40000
JGE Label35
invoke MessageBox, NULL, addr @cadena29, addr @cadena29, MB_OK
JMP Label37
Label35:
invoke MessageBox, NULL, addr @cadena30, addr @cadena30, MB_OK
Label37:
MOV dl, 3
MOV _a$main$ca, dl
FLD @cte50
FSTP _b$main$cb
MOV dl, _a$main$ca
MOV _a$main$ca, dl
MOV dl, 5
MOV _a$main$ca, dl
CALL m$main$ca
CALL n$main$cb
CALL m$main$ca
MOV edx, 100
MOV _real$main, edx
MOV edx, _real$main
MOV _formal$main$asigna_valor, edx
CALL asigna_valor$main
MOV edx, _real$main
CMP edx, 60000
JNE Label64
invoke MessageBox, NULL, addr @cadena58, addr @cadena58, MB_OK
JMP Label66
Label64:
invoke MessageBox, NULL, addr @cadena59, addr @cadena59, MB_OK
Label66:
CALL funcion_main$main
MOV dl, 100
MOV _xus$main, dl
MOV edx, 10000
MOV _xl$main, edx
FLD @cte72
FSTP _xd$main
MOV dl, _xus$main
MOVSX edx, dl
MOV edx, @aux2
ADD edx, 10000
MOV @aux3, edx
MOV edx, @aux3
MOV _xl$main, edx

JMP fin 
OFM: 
invoke  MessageBox, NULL, ADDR OverFlowMultiplicacion, ADDR OverFlowMultiplicacion, MB_OK 
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

