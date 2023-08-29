.MODEL SMALL 
.CODE 
Inicio: 
mov ax, @Data 
mov ds, ax 
;asignacion
mov dx, offset a+2
mov si, dx
mov [si], 0
add [si], 30h
;asignacion
mov dx, offset b+2
mov si, dx
mov [si], 0
add [si], 30h
;asignacion
mov dx, offset c+2
mov si, dx
mov [si], 0
add [si], 30h
;asignacion
mov dx, offset x+2
mov si, dx
mov [si], 0
add [si], 30h
;impresion constante
mov ah, 09h 
mov dx, offset eti0
int 21h
;salto
mov ah, 09h 
mov dx, offset salto
int 21h
;zcan
mov ah, 0Ah 
mov dx, offset a
int 21h
;salto
mov ah, 09h 
mov dx, offset salto
int 21h
;impresion constante
mov ah, 09h 
mov dx, offset eti1
int 21h
;salto
mov ah, 09h 
mov dx, offset salto
int 21h
;zcan
mov ah, 0Ah 
mov dx, offset b
int 21h
;salto
mov ah, 09h 
mov dx, offset salto
int 21h
;impresion constante
mov ah, 09h 
mov dx, offset eti2
int 21h
;salto
mov ah, 09h 
mov dx, offset salto
int 21h
;zcan
mov ah, 0Ah 
mov dx, offset c
int 21h
;salto
mov ah, 09h 
mov dx, offset salto
int 21h
;zcan
mov ah, 0Ah 
mov dx, offset tu
int 21h
;salto
mov ah, 09h 
mov dx, offset salto
int 21h
;condicional if
xor ax,ax
mov si, offset a+2
mov ah, byte ptr [si]
mov di, offset b+2
mov al, byte ptr [di]
cmp ah,al
je etiif0:
;condicional if
xor ax,ax
mov si, offset b+2
mov ah, byte ptr [si]
mov di, offset c+2
mov al, byte ptr [di]
cmp ah,al
je etiif1:
;condicional if
xor ax,ax
mov si, offset c+2
mov ah, byte ptr [si]
mov di, offset a+2
mov al, byte ptr [di]
cmp ah,al
je etiif2:
;condicional if
xor ax,ax
mov si, offset a+2
mov ah, byte ptr [si]
mov di, offset b+2
mov al, byte ptr [di]
cmp ah,al
jbe etiif3:
;ciclo for
mov si, offset b+2
mov di, offset b+2
mov al, byte ptr [di]
mov byte ptr [si], al
xor bx, bx
mov bl, al
xor cx, cx
mov si, offset c+2
mov cl, byte ptr [si]
sub cl, bl
add cl, 1
for0:
push cx
;modulo id ct
xor ax, ax
mov si, offset b+2
mov al, byte ptr [si]
xor bx, bx
mov bl, 2
div bl
add ah, 30h
mov si, offset x+2
mov byte ptr [si], ah
;condicional if
xor ax,ax
mov si, offset x+2
mov ah, byte ptr [si]
mov di, offset 0+2
mov al, 0
add al, 30h
cmp ah,al
jne etiif4:
;impresion variable
mov dx, offset b+2
mov ah, 09h
int 21h
;salto
mov ah, 09h 
mov dx, offset salto
int 21h
etiif4:
mov si, offset b+2
add byte ptr [si], 1
pop cx
loop for0
etiif3:
;condicional if
xor ax,ax
mov si, offset b+2
mov ah, byte ptr [si]
mov di, offset a+2
mov al, byte ptr [di]
cmp ah,al
jbe etiif5:
;ciclo for
mov si, offset a+2
mov di, offset a+2
mov al, byte ptr [di]
mov byte ptr [si], al
xor bx, bx
mov bl, al
xor cx, cx
mov si, offset c+2
mov cl, byte ptr [si]
sub cl, bl
add cl, 1
for1:
push cx
;modulo id ct
xor ax, ax
mov si, offset a+2
mov al, byte ptr [si]
xor bx, bx
mov bl, 2
div bl
add ah, 30h
mov si, offset x+2
mov byte ptr [si], ah
;condicional if
xor ax,ax
mov si, offset x+2
mov ah, byte ptr [si]
mov di, offset 0+2
mov al, 0
add al, 30h
cmp ah,al
jne etiif6:
;impresion variable
mov dx, offset a+2
mov ah, 09h
int 21h
;salto
mov ah, 09h 
mov dx, offset salto
int 21h
etiif6:
mov si, offset a+2
add byte ptr [si], 1
pop cx
loop for1
etiif5:
etiif2:
etiif1:
etiif0:
mov ax, 4C00h
int 21h
.DATA
a db 5,?,5 dup (24h) ;variable declarada
b db 5,?,5 dup (24h) ;variable declarada
c db 5,?,5 dup (24h) ;variable declarada
x db 5,?,5 dup (24h) ;variable declarada
eti0 db ' ingrese a $'
eti1 db ' ingrese b $'
eti2 db ' ingrese c $'
salto db 10,13,'$'