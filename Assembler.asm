.386
.model flat, stdcall
option casemap :none
include \masm32\include\windows.inc
include \masm32\include\kernel32.inc
include \masm32\include\user32.inc
includelib \masm32\lib\kernel32.lib
includelib \masm32\lib\user32.lib
.data
x:main dq  ?
y:main dq  ?
z:main dd  ?
m:main db  ?
t1:main dq  ?
t:main dq  ?
a:main dq  ?
b:main dq  ?
c:main dq  ?
@cteDouble1 dq 1.0 
@cteDouble2 dq 3.0 
@cteDouble3 dq 5.0 
@cteDouble4 dq 6.0 
@cteDouble5 dq 2.0 
@cteUS1 db 10 
@cadena1 db cadena de caracteres 
@funcion1 db funcion1:main 
y:main:funcion1 dq  ?
x:main:funcion1 db  ?
variable:main db  ?
f:main:clase1 dq  ?
@funcion2 db funcionclase:main:clase1 
e:main:clase1:funcionclase dd  ?
variablelong:main:clase1 dd  ?
anteultima:main:clase1 db  ?
@funcion3 db funcion_uno:main:clase1 
ultima:main dd  ?
variablelong:main dd  ?
@cteLong1 dd 20 
@funcion4 db func_uno:main 
uno:main:func_uno dd  ?
@funcion5 db funcion_uno:main:func_uno 
@funcion6 db funcion_dos:main:func_uno:funcion_uno 
entero:main:func_uno:funcion_uno:funcion_dos db  ?
a1:main:func_uno:funcion_uno dq  ?
@cadena2 db chau 
@cadena3 db hola 
a2:main:func_uno dq  ?
a3:main dq  ?
ni_este:main:clase1:esta_tampoco db  ?
@cteUS2 db 4 
@cteUS3 db 5 
atributo:main:clase2 dq  ?
w:main:clase2:funcionclase dd  ?
entero_largo:main:clase2:funcionclase dd  ?
@funcion7 db metodo_dos:main:clase2 
.code
START:
MOV BL, _m
MOV BH, 0
MOV BX, _m
MOV ECX, 0
MOV CX, BX
MOV EBX, ECX
MOV EDX, @auximul EDX, [0]MOV @aux2, EDXMOV ST(O), @auxfadd ST(O), [2]MOV @aux3, ST(O)MOV ST(O), @aux3MOV @aux4, ST(O)MOV ST(O), _6fsub ST(O), _7MOV @aux5, ST(O)MOV ST(O), _8fadd ST(O), _9MOV @aux6, ST(O)MOV ST(O), _10fmul ST(O), _11MOV @aux7, ST(O)MOV ST(O), @aux7MOV @aux8, ST(O)MOV ST(O), _5fadd ST(O), _12MOV @aux9, ST(O)MOV ST(O), @aux9MOV @aux10, ST(O)MOV ST(O), _7fsub ST(O), _8MOV @aux11, ST(O)MOV ST(O), @aux11MOV @aux12, ST(O)MOV ST(O), _6fmul ST(O), _13MOV @aux13, ST(O)MOV ST(O), @aux13MOV @aux14, ST(O)MOV ST(O), _0MOV @aux15, ST(O)MOV BL, _10
MOV BH, 0
MOV BX, _10
MOV ECX, 0
MOV CX, BX
MOV EBX, ECX
MOV EAX, _10
MOV EDX, 0
MOV ST(O), _0MOV @aux16, ST(O)MOV EBX, _17MOV @aux17, EBXMOV ST(O), _21MOV @aux18, ST(O)MOV ECX, _24MOV @aux19, ECXEND START