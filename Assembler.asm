.386
.model flat, stdcall
option casemap :none
include \masm32\include\windows.inc
include \masm32\include\kernel32.inc
include \masm32\include\user32.inc
includelib \masm32\lib\kernel32.lib
includelib \masm32\lib\user32.lib
.data
x dq  ?
y dq  ?
z DD  ?
m db  ?
t1 dq  ?
t dq  ?
a dq  ?
b dq  ?
c dq  ?
@cteDouble db 1.0 
@cteDouble db 3.0 
@cteDouble db 5.0 
@cteDouble db 6.0 
@cteDouble db 2.0 
@cteUS db 10 
@cadena db cadena de caracteres 
y dq  ?
x db  ?
variable db  ?
f dq  ?
e DD  ?
variablelong DD  ?
anteultima db  ?
ultima DD  ?
variablelong DD  ?
@cteLong DD 20 
uno DD  ?
entero db  ?
a1 dq  ?
@cadena db chau 
@cadena db hola 
a2 dq  ?
a3 dq  ?
ni_este db  ?
@cteUS db 4 
@cteUS db 5 
atributo dq  ?
w DD  ?
entero_largo DD  ?
.code
START:
END START