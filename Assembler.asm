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
_us$main db  ?
_clase$main db  ?
_largo$main$clase dd  ?
_l$main$clase$func dd  ?
_clase2$main db  ?
_dob1$main$clase2 dq  ?
_dob2$main$clase2 dq  ?
objeto1 db  ?
objeto2 db  ?
@cadenacadena de prueba, db "cadena de prueba,"
@aux1 db  ?
.code
func$main$clase:
ret
func2$main$clase2:
ret
func2$main:
ret
START:
MOV EDX, 255
MOV _us$main, EDX
MOV EDX, 1000099
MOV _largo$main$clase, EDX
MOV EDX, _l$main$clase$func
ADD EDX, 100000
MOV @aux1, EDX
MOV EDX, @aux1
MOV _l$main$clase$func, EDX
FLD 0.45
FSTP _dob1$main$clase2
FLD 0.006
FSTP _dob2$main$clase2
MOV EDX, -2147483648
MOV  , EDX
MOV EDX, 200000
MOV _l$main$clase$func, EDX
CALL func$main$clase
invoke MessageBox, NULL, addr @cadena20, addr @cadena20, MB_OK
END START