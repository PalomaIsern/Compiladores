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
us$main db  ?
@cteUS1 db 255 
clase db  ?
largo$main$clase db  ?
@cteLong1 dd 1000099 
func db  ?
l$main$clase$func db  ?
@cteLong2 dd 100000 
clase2 db  ?
dob1$main$clase2 db  ?
dob2$main$clase2 db  ?
@cteDouble1 dq 0.45 
@cteDouble2 dq 0.006 
func2 db  ?
objeto1 db  ?
objeto2 db  ?
@cteLong3 dd -2147483648 
@cteLong4 dd 200000 
@cadena1 db cadena de prueba, 
@aux2 db  ?
.code
func$main$clase:
ret
func2$main$clase2:
ret
START:
MOV EDX, _us$main
MOV _us$main, EDX
MOV EBX, _largo$main$clase
MOV _largo$main$clase, EBX
MOV ECX, _l$main$clase$func
ADD ECX, 100000
MOV @aux2, ECX
MOV EAX, _l$main$clase$func
MOV _l$main$clase$func, EAX
MOV ST(0), _dob1$main$clase2
MOV _dob1$main$clase2, ST(0)
MOV ST(0), _dob2$main$clase2
MOV _dob2$main$clase2, ST(0)
MOV  , @aux
MOV @aux,  
MOV  , _l$main$clase$func
MOV _l$main$clase$func,  
CALL func$main$clase
END START