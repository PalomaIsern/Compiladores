.386
.model flat, stdcall
option casemap :none
include \masm32\include\windows.inc
include \masm32\include\kernel32.inc
include \masm32\include\user32.inc
includelib \masm32\lib\kernel32.lib
includelib \masm32\lib\user32.lib
.data
MaxNumUSHORT db 255MinNumLong dd -2147483648MaxNumLong dd 2147483647MinNumDouble dq 2.2250738585072014e-308MaxNumDouble dq 1.7976931348623157e+308x:main dq  ?
y:main dd  ?
z:main db  ?
@cteDouble1 dq 2.0 
@cteDouble2 dq 1.0 
@cteLong1 dd 6 
@cteUS1 db 3 
.code
funcion1:main:
funcionclase:main:clase1:
funcion_uno:main:clase1:
func_uno:main:
funcion_uno:main:func_uno:
funcion_dos:main:func_uno:funcion_uno:
metodo_dos:main:clase2:
START:
MOV ST(O), 2.0
fadd ST(O), 1.0
MOV @aux2, ST(O)
MOV ST(O), _x:main
MOV _x:main, ST(O)
MOV EDX, _y:main
MOV _y:main, EDX
MOV ST(O), _x:main
fsub ST(O), @aux
MOV @aux3, ST(O)
MOV EBX, _y:main
MOV _y:main, EBX
MOV BL, _3
MOV BH, 0
MOV BX, _3
MOV ECX, 0
MOV CX, BX
MOV EBX, ECX
MOV EAX, @aux
imul EAX, _y:main
MOV @aux4, EAX
MOV  , _z:main
MOV _z:main,  
END START