.386
.model flat, stdcall
option casemap :none
include \masm32\include\windows.inc
include \masm32\include\kernel32.inc
include \masm32\include\user32.inc
includelib \masm32\lib\kernel32.lib
includelib \masm32\lib\user32.lib
.data
MaxNumUSHORT db 255
MinNumLong dd -2147483648
MaxNumLong dd 2147483647
MinNumDouble dq 2.2250738585072014e-308
MaxNumDouble dq 1.7976931348623157e+308
x$main db  ?
y$main db  ?
z$main db  ?
m$main db  ?
t1$main db  ?
t$main db  ?
a$main db  ?
b$main db  ?
c$main db  ?
@cteDouble1 dq 1.0 
@cteDouble2 dq 3.0 
@cteDouble3 dq 5.0 
@cteDouble4 dq 6.0 
@cteDouble5 dq 2.0 
@cteUS1 db 10 
@cadena1 db "cadena de caracteres"
@funcion1 db funcion1$main 
y$main$funcion1 db  ?
x$main$funcion1 db  ?
variable$main db  ?
clase1$main db  ?
f$main$clase1 db  ?
@funcion2 db funcionclase$main$clase1 
e$main$clase1$funcionclase db  ?
variablelong$main$clase1 db  ?
anteultima$main$clase1 db  ?
@funcion3 db funcion_uno$main$clase1 
ultima$main db  ?
variablelong$main db  ?
@cteLong1 dd 20 
@funcion4 db func_uno$main 
uno$main$func_uno db  ?
@funcion5 db funcion_uno$main$func_uno 
@funcion6 db funcion_dos$main$func_uno$funcion_uno 
entero$main$func_uno$funcion_uno$funcion_dos db  ?
objeto2 db  ?
a1$main$func_uno$funcion_uno db  ?
clase2$main$func_uno db  ?
@cadena2 db "chau"
@cadena3 db "hola"
a2$main$func_uno db  ?
a3$main db  ?
objeto3 db  ?
ni_este$main$clase1$esta_tampoco db  ?
objeto1 db  ?
obj4 db  ?
s db  ?
@cteUS2 db 4 
@cteUS3 db 5 
clase2$main db  ?
atributo$main$clase2 db  ?
w$main$clase2$funcionclase db  ?
entero_largo$main$clase2$funcionclase db  ?
@funcion7 db metodo_dos$main$clase2 
@aux2 db  ?
@aux3 db  ?
@aux4 db  ?
@aux5 db  ?
@aux6 db  ?
@aux7 db  ?
@aux8 db  ?
@aux9 db  ?
.code
funcion1$main:
ret
funcionclase$main$clase1:
ret
funcion_uno$main$clase1:
ret
func_uno$main:
ret
funcion_uno$main$func_uno:
ret
funcion_dos$main$func_uno$funcion_uno:
ret
metodo_dos$main$clase2:
ret
START:
MOV BL, _m
MOV BH, 0
MOV BX, _m
MOV ECX, 0
MOV CX, BX
MOV EBX, ECX
MOV EBX, _z$main
IMUL EBX, @aux
MOV @aux2, EBX
MOV ST(0), _y$main
FADD ST(0), @aux
MOV @aux3, ST(0)
MOV ST(0), _x$main
MOV _x$main, ST(0)
MOV ST(0), _a$main
FSUB ST(0), _b$main
MOV @aux4, ST(0)
MOV ST(0), _c$main
FADD ST(0), 1.0
MOV @aux5, ST(0)
MOV ST(0), @aux4
CMP ST(0), @aux5
JLE Label14
MOV ST(0), 3.0
FMUL ST(0), 5.0
MOV @aux6, ST(0)
MOV ST(0), _t$main
MOV _t$main, ST(0)
MOV ST(0), _t$main
FADD ST(0), 6.0
MOV @aux7, ST(0)
MOV ST(0), _t1$main
MOV _t1$main, ST(0)
JMP Label17
Label14:
MOV ST(0), _b$main
FSUB ST(0), _c$main
MOV @aux8, ST(0)
MOV ST(0), _a$main
MOV _a$main, ST(0)
Label17:
Label18:
MOV ST(0), _a$main
FMUL ST(0), 2.0
MOV @aux9, ST(0)
MOV ST(0), _a$main
MOV _a$main, ST(0)
MOV ST(0), _x$main
MOV _x$main, ST(0)
MOV BL, _10
MOV BH, 0
MOV BX, _10
MOV ECX, 0
MOV CX, BX
MOV EBX, ECX
MOV EAX, _10
MOV EDX, 0
MOV ST(0), _x$main
CMP ST(0), @aux
JLE Label18
MOV ST(0), _x$main
MOV _x$main, ST(0)
MOV  , _y$main$funcion1
MOV _y$main$funcion1,  
CALL funcion1$main
MOV ST(0), _f$main$clase1
MOV _f$main$clase1, ST(0)
CALL funcionclase$main$clase1
MOV  , _variablelong$main$clase1
MOV _variablelong$main$clase1,  
CALL funcion_dos$main$func_uno$funcion_uno
CALL funcionclase$main$clase1
END START