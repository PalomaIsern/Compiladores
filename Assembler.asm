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
_variablecuyonombre__$main db  ?
_nombrecorto2$main db  ?
_x$main$funcion_main db  ?
@cadenaNo es una suma valida db "No es una suma valida"
@cadenaNO es un cadena valida, db "NO es un cadena valida,"
hola db  ?
_y$main dq  ?
@aux1 db  ?
@aux3 db  ?
.code
funcion_main$main:
ret
START:
MOV EDX, _x$main$funcion_main
CMP EDX, 5
JG Label7
MOV EDX, _x$main$funcion_main
ADD EDX, 255
MOV @aux1, EDX
MOV EDX, @aux1
MOV _x$main$funcion_main, EDX
invoke MessageBox, NULL, addr @cadena6, addr @cadena6, MB_OK
JMP Label11
Label7:
invoke MessageBox, NULL, addr @cadena7, addr @cadena7, MB_OK
MOV DL, _-
MOVSX EDX, DL
FLD _-
FST _-
MOV EDX, @aux2
MOV _-, EDX
Label11:
Label14:
MOV EDX, _y$main
ADD EDX, 1.0
MOV @aux3, EDX
FLD @aux3
FSTP _y$main
MOV EDX, _y$main
CMP EDX, 133.0
JL Label14
END START