
package lex;
import java.util.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.nio.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author bacay
 */

public class Lex {
static int numL;
public static String lineas [] = new String [100];
public static List <String> codigo  = new ArrayList <String>();
public static List <String> data  = new ArrayList <String>();
public static List <Integer> weti = new ArrayList <Integer>();
public static Stack <String> tknf = new Stack ();
public static Stack <String> tknif = new Stack ();
public static Stack <String> tknw = new Stack ();

public static int conif,contfor,contwhile,fconif,fcontfor,fcontwhile,errif,errfor,errwhile;
public static int etiif=0;
public static int etifor=0;
public static int etiwhile=0;

public static String tkn2for ="";

public static int fetiif=0;
public static int fetifor=0;
public static int fetiwhile=0;

public static int contadorsaltos=0;

public static int finalw = 0;
public static int finalf= 0;
public static int finalif= 0;

public static int itp = 0;

public static String petiif="";
public static String petifor="";
public static String petiwhile="";

public static int loopw=0;
public static int loopf=0;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here


        
        leer("comp.txt");
        ordenarreserv();
escribir();



   
       
   
    }

public static void escribir (){//------------------------------escribimos en el asm


try{

PrintWriter escribe = new PrintWriter ("escritura.asm");


for (String i : codigo){
escribe.print(i);
}
for (String i : data){

escribe.print(i);
}




escribe.close();



    System.err.println("script generado");



}catch (IOException ex){

ex.printStackTrace(System.out); 

}



}







public static String leer (String direct){//----------------------------------------------------------------------------------------->

numL=0;


String completo="";

try{

BufferedReader bf = new BufferedReader (new FileReader (direct));

String guarda="";
String bfRead;

while ((bfRead = bf.readLine()) != null){
numL++;
guarda = guarda +bfRead+"\n";
for(int i=0;i<numL;i++){
String Line = Files.readAllLines(Paths.get(direct)).get(i);
lineas[i] = Line;
}
}


completo = guarda;


}catch (Exception e){System.err.println("no se encontro el archivo");}


return completo;


}




public static String tabla [][] = new String [100][3];

public static String detectarerrv (String archivo){//------------------------------------------------------------------------->
String apartir="";
String bfRead;
try{
BufferedReader bf = new BufferedReader (new FileReader (archivo));
while ((bfRead = bf.readLine()) != null){
apartir = apartir +bfRead+"\n";

}

}catch(Exception e){System.err.println("no se encotro el archivo");}
return apartir;
}


public static boolean vertabla (String variable){//-------------------------------------------------------------------------->verificar en tabla de valores

boolean vyaex=true;

for(int l=0;l<100;l++){
if(variable.equals(tabla [l][1])){
 vyaex = true;
}else{
vyaex = false;
}
}

return vyaex;

}


 


public static boolean varex (String variable){
boolean vex = true;


for (int i = 0; i<100; i++){

if (variable.equals(tabla [i][1])){
vex = true;
}else{
vex = false;
}


}
return vex;
}

public static String tipodato (String Nvariable){//---------------------------------------------------------------->extraccion de tipo de dato

String tipodd="";

for(int i=0;i<100;i++){
if(Nvariable.equals(tabla[i][1])){
tipodd = tabla [i][0];
}
}
return tipodd;
}

public static String valorvariable (String Nvariable){//---------------------------------------------------------------->extraccion del valor de una variable

String valor="";

for(int i=0;i<100;i++){
if(Nvariable.equals(tabla[i][1])){
valor = tabla [i][2];
}
}
return valor;
}






public static String Scontimpre="";
public static int contimpre = 0;
public static String Scontetis="";
public static int contetis = 0;



public static void ordenarreserv (){//--------------------------------------------------------------------------------------->

codigo.add(".MODEL SMALL "
             + "\n.CODE "
             + "\nInicio: "
             + "\nmov ax, @Data "
             + "\nmov ds, ax ");

String [] PReserv = {"Zcan","Printz","	Printz","Else","	While","MOD"," ","==","!=","=","'","(",")","++","if;","for;","while;","->","-",">="};//0,1,2,3,4,5....

boolean sino=false;
boolean zyaex=false;
boolean vyaex = false;

boolean intyaex=false;
boolean floatyaex=false;
boolean equ=false;
boolean numlierrztring = false;

int o;

String uniontksZtring;
String vzint="";
String vzfloat="";
String vZtring="";
String vZtringerr="";
String declaracionZtring;
String declaracionzint;


String id = "[a-z]|[A-Z]|[0-9]+";
String idasm = "[a-zA-Z][a-zA-Z0-9]*";

String ctzint = "[0-9]+";
String ctzfloat = ctzint+"\\."+ctzint;
String ctZtring = "\'.*\'";
String ctZtringz = "\'.*";
String ctZtringzz = ".*\'";

String ctzintasm = "-?[0-9]+";
String ctzfloatasm = ctzintasm+"\\."+ctzintasm;


String pasm = "\\('\\.?";
String pasm2 = "'\\)";

String pasmsinc = "\\(";
String pasmsinc2 = "\\)";


String ctZtringprintz2 = "\\('.*'\\)";
String ctZtringzprintz2 = "\\(.*";
String ctZtringzzprintz2 = ".*\\)";
String aritmetico = "[+|-|*|/|%|^]";
String opz = "=|<|>|<>|!|<=";
String rxf = "++|--";
String indentacion="	";
String indentacion2="		";
String opZtring = "==|!=";
String opzfi = "==|!=|>|<|>=|<=";



String impresiones = "("+ctZtringprintz2+")|("+ctZtringzprintz2+")|("+ctZtringzzprintz2+")";
String ctz = "("+ctzint+")|("+ctzfloat+")|("+ctZtring+")|("+ctZtringz+")||("+ctZtringzz+")";
String oplog = "("+id+")("+opz+")("+ctz+")|("+indentacion+")("+id+")("+opz+")("+ctz+")|("+indentacion2+")("+id+")("+opz+")("+ctz+")";
String oparit = "("+id+")("+aritmetico+")("+ctz+")|("+ctz+")("+aritmetico+")("+id+")|("+id+")("+aritmetico+")("+id+")|("+ctz+")("+aritmetico+")("+ctz+")";
String asignar = "("+id+")("+opz+")("+oparit+")";
//String oplog = "[A-Z].[0-9]";

String aumentoi ="("+id+")\\++|("+id+")\\--";
String zfor ="For("+id+")("+oplog+")\\//("+oplog+")\\//("+aumentoi+")";
String zwhile="While("+oplog+")";
String sepfor="\\//";

String baseetis = "";




try{


//----------------------------------------
 


/*   
String cadenaDeTexto=leer("comp.txt");
String cadenatxt2=leer("comp.txt");
String patron = "Printz ('";
String patron2 ="Printz\\s*\\(\\s*(-?\\d+(\\.\\d+)?)\\s*";
  String patron3 = "Printz ( ";
String patron4 ="[a-zA-Z][a-zA-Z0-9]*";
int indice = 0;

while (   ((indice = cadenaDeTexto.indexOf(patron, indice)) != -1)   ) {
            contimpre++;
            indice += patron.length();
            
        }

indice = 0;
Pattern pattern = Pattern.compile(patron2);
Matcher matcher = pattern.matcher(cadenaDeTexto);
while (matcher.find()) {
            contimpre++;
        }

    System.err.println(contimpre);


for (int i = 0; i<contimpre;i++){

Scontimpre = Integer.toString(i);

    codigo.add(";impresion constante"
 + "\nmov ah, 09h "
       + "\nmov dx, offset eti"+Scontimpre+""
       + "\nint 21h");

}


*/

//---kñ

    
/*
    
 Pattern pattern2 = Pattern.compile("Printz \\( ([a-zA-Z][a-zA-Z0-9]*)");
        Matcher matcher2 = pattern2.matcher(cadenatxt2);
        int count = 0;
        while (matcher2.find()) {
            String subcadena = matcher2.group(1);
            
escribir (""
 + "\nmov "+subcadena+", 5"      //+valorvariable(subcadena)
 + "\nadd "+subcadena+", 30h"
 + "\nmov dx, offset "+subcadena
 + "\nmov ah, 09h"
 + "\nint 21h"
 + "\nsub "+subcadena+", 30h");



            count++;
        }

*/

//---kñ


        



//-------------------------modificar el contador para tomar en cuenta 
//---------------------------- la llamada de etiquetas de variables

//-------------------------------------

data.add ("\nmov ax, 4C00h\n" +
"int 21h"
 + "\n.DATA");
    

for(int i=0; i<numL;i++){//--------------------------INICIO LECTOR

    System.out.println("Linea: "+(i+1));

String sp[] = lineas[i].split(" ");
int palabras = sp.length;
for(int x=0;x<(palabras);x++){
    System.out.println(sp[x]);

//--------------!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!


for(int zz=0;zz<100;zz++){
for(int y=0;y<20;y++){

if (sp[x].equals(PReserv[y]) || sp[x].matches(ctz) || sp[x].equals(tabla [zz][1]) || sp[x].matches(opz) || sp[x].matches(impresiones) || sp[x].matches(oparit) || sp[x].matches(oplog) || sp[x].matches(zfor) || sp[x].matches(zwhile) || sp[x].matches(asignar) || sp[x].matches(sepfor)){//----------------------------------------------------------checar palabras rservadas
   sino = true;
}
}
}
//---------------!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!

if(sino){
sino = false;  






 ///


if (sp[x].equals(PReserv[0])){//---zcan

if (sp[x+1].matches(idasm)){

codigo.add("\n;zcan\nmov ah, 0Ah "
 + "\nmov dx, offset "+sp[x+1]+""
 + "\nint 21h");


codigo.add("\n;salto"
 + "\nmov ah, 09h "
       + "\nmov dx, offset salto"
       + "\nint 21h");
}else{
    System.err.println("error sintactico zcan linea "+(i+1));
}

}



 if(sp[x].equals(PReserv[1])){//--------------------------------------------------------------match con palabra reservada Printz

String pc = sp[x+1];
String v = sp[x+2];
String ctentfloid = sp[x+2];//--------nvariable o id 

if(sp[x+1].matches(ctZtringprintz2)){//----------------------------------------------------------------xd
    System.out.println("impresion simple valida de:  "+sp[x+1]);
} 

else if (sp[x+1].matches(ctZtringzprintz2) && !(vertabla(v))){

    


String aux1 =sp[x+1];
String aux2="";
int iaux =1;
while(!aux2.matches(ctZtringzzprintz2)){
iaux++;
aux2 = sp[x+iaux];
aux1 = aux1+" "+aux2;
}


sp[x+1] = aux1;

x=x+iaux;


 System.out.println("impresion compuesta valida para: "+aux1);




if (pc.matches(pasm)  || ctentfloid.matches(ctzintasm) || ctentfloid.matches(ctzfloatasm)  ){



aux1 = aux1.replaceAll(pasm, "");
aux1 = aux1.replaceAll(pasm2, "");
aux1 = aux1.replaceAll(pasmsinc, "");
aux1 = aux1.replaceAll(pasmsinc2, "");




    System.out.println("impresion compuesta valida para: "+aux1); // !!!!


    Scontetis = Integer.toString(contetis);


    //escribir("\nmov ah, 09h "
     //  + "\nmov dx, offset eti"+Scontetis+""
      // + "\nint 21h");


codigo.add("\n;impresion constante"
 + "\nmov ah, 09h "
       + "\nmov dx, offset eti"+Scontetis+""
       + "\nint 21h");

codigo.add("\n;salto"
 + "\nmov ah, 09h "
       + "\nmov dx, offset salto"
       + "\nint 21h");
    
    data.add("\neti"+Scontetis+" db '"+aux1+"$'");
    
contetis++;




}

//xdx

else if (ctentfloid.matches(idasm)){


  //[a-zA-Z][a-zA-Z0-9]*
       
            
            
codigo.add ("\n;impresion variable"
 //+ "\nmov "+ctentfloid+", "+valorvariable(ctentfloid) //ojo!!
 
 + "\nmov dx, offset "+ctentfloid+"+2"
 + "\nmov ah, 09h"
 + "\nint 21h");

codigo.add("\n;salto"
 + "\nmov ah, 09h "
       + "\nmov dx, offset salto"
       + "\nint 21h");
            
        



}

    System.out.println(ctentfloid+varex(ctentfloid));



}//--------------------------------------------------------------------------------------------------- validacion de impresion compuesta
else{
    System.err.println("la variable "+v+" no existe");
}
}//--------------------------------------------------------------------------------------------------validacion de impresion simple 

}









else if (sp[x].equals("zint") || sp[x].equals("	zint")  ){//---------------------------------------------------------------inicio variables int
if (sp[x+1].matches(idasm) && sp[x+2].equals("=") && sp[x+3].matches(ctzint)){
vzint = sp[x+1];

    System.out.println(vzint+" es una variable int");  

for(int l=0;l<100;l++){
if(vzint.equals(tabla [l][1])){
zyaex = true;
}
}

if(zyaex){


System.err.println("error en la linea "+(i+1)+" la variable "+vzint+" ya existe");
zyaex = false;
}else{

tabla [i][0]=sp[x];// tipo dato
tabla [i][1]=sp[x+1];// nombre
tabla [i][2]=sp[x+3];// valor

        System.out.println("------------------------------------------------------------------------->"+tabla [i][0]);
        System.out.println("------------------------------------------------------------------------->"+tabla [i][1]);
        System.out.println("------------------------------------------------------------------------->"+tabla [i][2]);



codigo.add("\n;asignacion\nmov dx, offset "+sp[x+1]+"+2"
 + "\nmov si, dx"
 + "\nmov [si], "+sp[x+3]+""
 + "\nadd [si], 30h");

data.add("\n"+vzint+" db 5,?,5 dup (24h) ;variable declarada" );

}
  } else{
    System.err.println("error sintactico linea "+(i+1));
} 
}//------------------------------------------------------------------------------------------------------fin variables int
else if (sp[x].equals("zfloat")){//--------------------------------------------------------------inicio variables float
vzfloat = sp[x+1];
    System.out.println(vzfloat+" es una variable float");

for(int l=0;l<100;l++){
if(vzfloat.equals(tabla [l][1])){
zyaex = true;
}
}

if(zyaex){


System.err.println("error en la linea "+(i+1)+" la variable "+vzfloat+" ya existe");
zyaex = false;
}else{

tabla [i][0]=sp[x];
tabla [i][1]=sp[x+1];
tabla [i][2]=sp[x+3];

        System.out.println("------------------------------------------------------------------------->"+tabla [i][0]);
        System.out.println("------------------------------------------------------------------------->"+tabla [i][1]);
        System.out.println("------------------------------------------------------------------------->"+tabla [i][2]);

}
    
}//-------------------------------------------------------------------------------------------------------------------fin variable float

else if (sp[x].equals("Ztring")){//--------------------------------------------------------------inicio variable string
vZtring = sp[x+1];
    System.out.println(vZtring+" es una variable String");
    

for(int l=0;l<100;l++){
if(vZtring.equals(tabla [l][1])){//----------------------------------------------------!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
zyaex = true;
}
}

if(zyaex){


System.err.println("error en la linea "+(i+1)+" la variable "+vZtring+" ya existe");
zyaex = false;
}else{

tabla [i][0]=sp[x];
tabla [i][1]=sp[x+1];

// deja de pegar las cadenas de caracteres de los arreglos hasta que haga matches con diicha expresion regular

if(sp[x+3].matches(ctZtring)){
tabla [i][2] = sp[x+3];
}

else{
String aux1 =sp[x+3];
String aux2="";
int iaux =3;
while(!aux2.matches(ctZtringzz)){//-------------------------- pegar cadenas de variable ztring
iaux++;
aux2 = sp[x+iaux];
aux1 = aux1+" "+aux2;
    System.out.println(iaux);
    System.out.println(aux1);
}
tabla [i][2]=aux1;

}






        System.out.println("------------------------------------------------------------------------->"+tabla [i][0]);
        System.out.println("------------------------------------------------------------------------->"+tabla [i][1]);
        System.out.println("------------------------------------------------------------------------->"+tabla [i][2]);

}



}//---------------------------------------------------------------------------------------fin variable string


else if(sp[x].equals(">>")){//---------------------------------------------------------------------------------verificar coincidencia de tipos
 
String simbolo = sp[x+4];

String car1 = sp[x+1];
String car2 = sp[x+3];
String car3 = sp[x+5];

if (car1.matches(ctzint) || car1.matches(ctzfloat)){
    System.err.println("error en la linea "+(i+1)+", el caracter '"+car1+"' debe ser un ID");
}else{


if (!vertabla(car1)){

/*
    if( (tipodato(car1).equals ("Ztring")  ) && (tipodato(car2).equals ("Ztring" ) ) && (tipodato(car3).equals ("Ztring")  ) || ( (tipodato(car1)=="" && !vertabla(car1) )  || (tipodato(car2)=="" && !vertabla(car2))  || (tipodato(car3)=="" && !vertabla(car3)) )){//-------coincidencia de ztrings
      



if(!simbolo.equals("+") && tipodato(car1).equals("Ztring") && tipodato(car2).equals("Ztring") && tipodato(car3).equals("Ztring")){//-----------------------------------!!!23/02/2023

     System.err.println("error en la linea, "+(i+1)+" solo es posible sumar con tipos de dato Ztring");
}else {
 System.out.println("todo ok juas juas");


}


}//--------------------------------------------------------------------------validacion de id y ct ztring 

*/

/*else*/if ( (tipodato(car1).equals ("zint")  ) && (tipodato(car2).equals ("zint" )  ) && (tipodato(car3).equals ("zint")  ) || ((tipodato(car1)=="" ) || (tipodato(car2)=="" ) || (tipodato(car3)==""  ))){
        
System.out.println("todo ok zint");

if(simbolo.equals("+") && car2.matches(idasm) && car3.matches(idasm)){//-----------------------op aritmeticas con id

codigo.add ("\n;suma ids \nxor ax, ax"
 + "\nmov si, offset "+car2+"+2" // hacer metodo que resiva como parametro el token ya sea id o constante y retorne
 + "\nmov al, byte ptr [si]"         // su valor  en caso de ser id retorne su valor y en caso de ser constante retorne
 + "\nxor bx, bx"                     // simplemente la constante
 + "\nmov si, offset "+car3+"+2"
 + "\nmov bl, byte ptr [si]"
 + "\nadd ax, bx"
 + "\nsub al, 30h"
 + "\nmov si, offset "+car1+"+2"
 + "\nmov byte ptr [si], al"
 + "\nmov byte ptr [si+1], '$'");

}
else if (simbolo.equals("-") && car2.matches(idasm) && car3.matches(idasm)){

codigo.add ("\n;resta ids\nxor ax, ax"
 + "\nmov si, offset "+car2+"+2"
 + "\nmov al, byte ptr [si]"
 + "\nxor bx, bx"
 + "\nmov si, offset "+car3+"+2"
 + "\nmov bl, byte ptr [si]"
 + "\nsub ax, bx"
 + "\nadd al, 30h"
 + "\nmov si, offset "+car1+"+2"
 + "\nmov byte ptr [si], al"
 + "\nmov byte ptr [si+1], '$'");


}
else if(simbolo.equals("*") && car2.matches(idasm) && car3.matches(idasm)){

codigo.add("\n;multi ids\nmov si, offset "+car2+"+2"
         + "\nmov di, offset "+car3+"+2"
        + "\nmov ah, 0"
        + "\nmov al, byte ptr [si]"
        + "\nsub ax, 30h"
        + "\nmov bh, 0"
        + "\nmov bl, byte ptr [di]"
        + "\nsub bx, 30h"
        + "\nmul bl"
        + "\nadd ax, 30h"
        + "\nmov "+car1+", al"
        + "\nmov si, offset "+car1+"+2"
        + "\nmov byte ptr [si], al");


}
else if (simbolo.equals("/") && car2.matches(idasm) && car3.matches(idasm)){

codigo.add("\n;div ids"
        + "\nmov si, offset "+car2+"+2"
         + "\nmov di, offset "+car3+"+2"
        + "\nmov ah, 0"
        + "\nmov al, byte ptr [si]"
        + "\nsub ax, 30h"
        + "\nmov bh, 0"
        + "\nmov bl, byte ptr [di]"
        + "\nsub bx, 30h"
        + "\ndiv bl"
        + "\nadd al, 30h"
        + "\nmov "+car1+", al"
        + "\nmov si, offset "+car1+"+2"
        + "\nmov byte ptr [si], al");

}

else if (simbolo.equals("^") && car2.matches(idasm) && car3.matches(idasm)){




    codigo.add("\n;potencia ids"
 + "\nmov si, offset "+car2+"+2"
 + "\nmov di, offset "+car3+"+2"
 + "\nmov ah, 0"
 + "\nmov al, byte ptr [si]"
 + "\nsub ax, 30h"
 + "\nmov bh, 0"
 + "\nmov bl, byte ptr [di]"
 + "\nsub bx, 30h"
+"\ncmp bl, 0"
+"\nje evaluar"+itp
 + "\nmov cx, bx"
 + "\nxor bx, bx"
 + "\nmov bx, ax"
 + "\nxor ax, ax"
 + "\nmov al, 1"
 + "\npotencia:"
 + "\nmul bl"
 + "\nloop potencia"
 //+ "\nmov byte ptr [si], al"
 //+ "\nmov byte ptr [si+1],'$'"
 + "\nadd ax, 30h"
 + "\nmov si, offset "+car1+"+2"
 + "\nmov byte ptr [si], al"
+"\njmp fin_evaluar_0"
+"\nevaluar"+itp+":"
+"\nmov dx, offset "+car1+"+2"
+"\nmov si, dx"
+"\nmov [si], 1"
+"\nadd [si], 30h"
+"\njmp fin_evaluar_0"
+"\nfin_evaluar_0:");

itp++;




}


else if (simbolo.equals("%") && car2.matches(idasm) && car3.matches(idasm)){

    codigo.add("\n;modulo ids"
 + "\nxor ax, ax"
 + "\nmov si, offset "+car2+"+2"
 + "\nmov al, byte btr [si]"
 + "\nxor bx, bx"
 + "\nmov bl, "+car3
 +"\ndiv bl"
 + "\nadd ah, 30h"
 + "\nmov si, offset "+car1+"+2"
 + "\nmov byte ptr [si], ah");

}







else if (simbolo.equals("+") && car2.matches(ctzint) && car3.matches(ctzint)){//--------------op aritmeticas con cte

codigo.add("\n;suma constantes"
 + "\nxor ax, ax"
 + "\nmov si, offset "+car2+"+2"
 + "\nmov al, "+car2
 + "\nadd al, 30h"
 + "\nxor bx, bx"
 + "\nmov si, offset "+car3+"+2"
 + "\nmov bl, "+car3
 + "\nadd bl, 30h"
 + "\nadd ax, bx"
 + "\nsub al, 30h"
 + "\nmov si, offset "+car1+"+2"
 + "\nmov byte ptr [si], al"
 + "\nmov byte ptr [si+1],'$'");
}
else if (simbolo.equals("-") && car2.matches(ctzint) && car3.matches(ctzint)){

codigo.add ("\n;resta cts\nxor ax, ax"
 + "\nmov si, offset "+car2+"+2"
 + "\nmov al, "+car2
+ "\nadd al, 30h"
 + "\nxor bx, bx"
 + "\nmov si, offset "+car3+"+2"
 + "\nmov bl, "+car3
+"\nadd bl, 30h"
 + "\nsub ax, bx"
 + "\nadd al, 30h"
 + "\nmov si, offset "+car1+"+2"
 + "\nmov byte ptr [si], al"
 + "\nmov byte ptr [si+1], '$'");

}
else if(simbolo.equals("*") && car2.matches(ctzint) && car3.matches(ctzint)){

codigo.add("\n;multi cts\nmov si, offset "+car2+"+2"
         + "\nmov di, offset "+car3+"+2"
        + "\nmov ah, 0"
        + "\nmov al, "+car2
+"\nadd al, 30h"
        + "\nsub ax, 30h"
        + "\nmov bh, 0"
        + "\nmov bl, "+car3
+"\nadd bl, 30h"
        + "\nsub bx, 30h"
        + "\nmul bl"
        + "\nadd ax, 30h"
        + "\nmov "+car1+", al"
        + "\nmov si, offset "+car1+"+2"
        + "\nmov byte ptr [si], al");

}
else if (simbolo.equals("/") && car2.matches(ctzint) && car3.matches(ctzint)){

codigo.add("\n;div cts"
         + "\nmov si, offset "+car2+"+2"
         + "\nmov di, offset "+car3+"+2"
        + "\nmov ah, 0"
        + "\nmov al, "+car2
        +"\nadd ax, 30h"
        + "\nsub ax, 30h"
        + "\nmov bh, 0"
        + "\nmov bl, "+car3
        +"\nadd bx, 30h "
        + "\nsub bx, 30h"
        + "\ndiv bl"
        + "\nadd al, 30h"
        + "\nadd bl, 30h"
        + "\nmov "+car1+", al"
        + "\nmov si, offset "+car1+"+2"
        + "\nmov byte ptr [si], al");

}



else if (simbolo.equals("^") && car2.matches(ctzint) && car3.matches(ctzint)){// xd

    codigo.add("\n;potencia cts"
 + "\nmov si, offset "+car2+"+2"
 + "\nmov di, offset "+car3+"+2"
 + "\nmov ah, 0"
 + "\nmov al, "+car2
+"\nadd al, 30h"
 + "\nsub ax, 30h"
 + "\nmov bh, 0"
 + "\nmov bl, "+car3
+"\nadd bl, 30h"
 + "\nsub bx, 30h"
+"\ncmp bl, 0"
+"\nje evaluar"+itp
 + "\nmov cx, bx"
 + "\nxor bx, bx"
 + "\nmov bx, ax"
 + "\nxor ax, ax"
 + "\nmov al, 1"
 + "\npotencia:"
 + "\nmul bl"
 + "\nloop potencia"
 //+ "\nmov byte ptr [si], al"
 //+ "\nmov byte ptr [si+1],'$'"
 + "\nadd ax, 30h"
 + "\nmov si, offset "+car1+"+2"
 + "\nmov byte ptr [si], al"
+"\njmp fin_evaluar_0"
+"\nevaluar"+itp+":"
+"\nmov dx, offset "+car1+"+2"
+"\nmov si, dx"
+"\nmov [si], 1"
+"\nadd [si], 30h"
+"\njmp fin_evaluar_0"
+"\nfin_evaluar_0:");

itp++;

}


else if (simbolo.equals("%") && car2.matches(ctzint) && car3.matches(ctzint)){

    codigo.add("\n;modulo cts"
 + "\nxor ax, ax"
 + "\nmov si, offset "+car2+"+2"
 + "\nmov al, "+car2
+"\nadd al, 30h"
 + "\nxor bx, bx"
 + "\nmov bl, "+car3
 +"\ndiv bl"
 + "\nadd ah, 30h"
 + "\nmov si, offset "+car1+"+2"
 + "\nmov byte ptr [si], ah");

}









else if (simbolo.equals("+") && car2.matches(ctzint) && car3.matches(idasm)){//--------------op aritmeticas con cte y id

codigo.add("\n;suma ct + id"
 + "\nxor ax, ax"
 + "\nmov si, offset "+car2+"+2"
 + "\nmov al, "+car2
 + "\nadd al, 30h"
 + "\nxor bx, bx"
 + "\nmov si, offset "+car3+"+2"
 + "\nmov bl, byte ptr [si]"
 + "\nadd ax, bx"
 + "\nsub al, 30h"
 + "\nmov si, offset "+car1+"+2"
 + "\nmov byte ptr [si], al"
 + "\nmov byte ptr [si+1],'$'");


}
else if (simbolo.equals("-") && car2.matches(ctzint) && car3.matches(idasm)){
codigo.add ("\n;resta ct  - id\nxor ax, ax"
 + "\nmov si, offset "+car2+"+2"
 + "\nmov al, "+car2
+ "\nadd al, 30h"
 + "\nxor bx, bx"
 + "\nmov si, offset "+car3+"+2"
 + "\nmov bl, byte ptr [si]"
 + "\nsub ax, bx"
 + "\nadd al, 30h"
 + "\nmov si, offset "+car1+"+2"
 + "\nmov byte ptr [si], al"
 + "\nmov byte ptr [si+1], '$'");


}
else if(simbolo.equals("*") && car2.matches(ctzint) && car3.matches(idasm)){

codigo.add("\n;multi ct + id"
         + "\nmov si, offset "+car2+"+2"
         + "\nmov di, offset "+car3+"+2"
        + "\nmov ah, 0"
        + "\nmov al, "+car2
        +"\nadd al, 30h"
        + "\nsub ax, 30h"
        + "\nmov bh, 0"
        + "\nmov bl, byte ptr [di]"
        + "\nsub bx, 30h"
        + "\nmul bl"
        + "\nadd ax, 30h"
        + "\nmov "+car1+", al"
        + "\nmov si, offset "+car1+"+2"
        + "\nmov byte ptr [si], al");


}
else if (simbolo.equals("/") && car2.matches(ctzint) && car3.matches(idasm)){


codigo.add("\n;div ct + id"
         + "\nmov si, offset "+car2+"+2"
         + "\nmov di, offset "+car3+"+2"
        + "\nmov ah, 0"
        + "\nmov al, "+car2
        +"\nadd ax, 30h"
        + "\nsub ax, 30h"
        + "\nmov bh, 0"
        + "\nmov bl, byte ptr [di]"
        + "\nsub bx, 30h"
        + "\ndiv bl"
        + "\nadd al, 30h"
        + "\nmov "+car1+", al"
        + "\nmov si, offset "+car1+"+2"
        + "\nmov byte ptr [si], al");

}

else if (simbolo.equals("^") && car2.matches(ctzint) && car3.matches(idasm)){// xd

    codigo.add("\n;potencia ct id"
 + "\nmov si, offset "+car2+"+2"
 + "\nmov di, offset "+car3+"+2"
 + "\nmov ah, 0"
 + "\nmov al, "+car2
+"\nadd al, 30h"
 + "\nsub ax, 30h"
 + "\nmov bh, 0"
 + "\nmov bl, "+"byte ptr [di]"
 + "\nsub bx, 30h"
+"\ncmp bl, 0"
+"\nje evaluar"+itp
 + "\nmov cx, bx"
 + "\nxor bx, bx"
 + "\nmov bx, ax"
 + "\nxor ax, ax"
 + "\nmov al, 1"
 + "\npotencia:"
 + "\nmul bl"
 + "\nloop potencia"
 //+ "\nmov byte ptr [si], al"
 //+ "\nmov byte ptr [si+1],'$'"
 + "\nadd ax, 30h"
 + "\nmov si, offset "+car1+"+2"
 + "\nmov byte ptr [si], al"
+"\njmp fin_evaluar_0"
+"\nevaluar"+itp+":"
+"\nmov dx, offset "+car1+"+2"
+"\nmov si, dx"
+"\nmov [si], 1"
+"\nadd [si], 30h"
+"\njmp fin_evaluar_0"
+"\nfin_evaluar_0:");

itp++;

}


else if (simbolo.equals("%") && car2.matches(ctzint) && car3.matches(idasm)){//ojo

    codigo.add("\n;modulo ct id"
 + "\nxor ax, ax"
 + "\nmov si, offset "+car2+"+2"
 + "\nmov al, "+car2
+"\nadd al, 30h"
 + "\nxor bx, bx"
 + "\nmov bl, "+car3
 +"\ndiv bl"
 + "\nadd ah, 30h"
 + "\nmov si, offset "+car1+"+2"
 + "\nmov byte ptr [si], ah");

}








else if (simbolo.equals("+") && car2.matches(idasm) && car3.matches(ctzint)){//--------------op aritmeticas con id y cte


codigo.add("\n;suma id + cte"
 + "\nxor ax, ax"
 + "\nmov si, offset "+car2+"+2"
 + "\nmov al, byte ptr [si]"
 + "\nxor bx, bx"
 + "\nmov si, offset "+car3+"+2"
 + "\nmov bl, "+car3
 + "\nadd bl, 30h"
 + "\nadd ax, bx"
 + "\nsub al, 30h"
 + "\nmov si, offset "+car1+"+2"
 + "\nmov byte ptr [si], al"
 + "\nmov byte ptr [si+1],'$'");


}
else if (simbolo.equals("-") && car2.matches(idasm) && car3.matches(ctzint)){

codigo.add ("\n;resta id  - ct\nxor ax, ax"
 + "\nmov si, offset "+car2+"+2"
 + "\nmov al, byte ptr [si]"
 + "\nxor bx, bx"
 + "\nmov si, offset "+car3+"+2"
 + "\nmov bl, "+car3
+ "\nadd bl, 30h"
 + "\nsub ax, bx"
 + "\nadd al, 30h"
 + "\nmov si, offset "+car1+"+2"
 + "\nmov byte ptr [si], al"
 + "\nmov byte ptr [si+1], '$'");
}
else if(simbolo.equals("*") && car2.matches(idasm) && car3.matches(ctzint)){

codigo.add("\n;multi id * ct"
         + "\nmov si, offset "+car3+"+2"
         + "\nmov di, offset "+car2+"+2"
        + "\nmov ah, 0"
        + "\nmov al, "+car3
        +"\nadd al, 30h"
        + "\nsub ax, 30h"
        + "\nmov bh, 0"
        + "\nmov bl, byte ptr [di]"
        + "\nsub bx, 30h"
        + "\nmul bl"
        + "\nadd ax, 30h"
        + "\nmov "+car1+", al"
        + "\nmov si, offset "+car1+"+2"
        + "\nmov byte ptr [si], al");


}
else if (simbolo.equals("/") && car2.matches(idasm) && car3.matches(ctzint)){

codigo.add("\n;div id / ct"
         + "\nmov si, offset "+car3+"+2"
         + "\nmov di, offset "+car2+"+2"
        + "\nmov ah, 0"
        + "\nmov al, "+car3
        +"\nadd ax, 30h"
        + "\nsub ax, 30h"
        + "\nmov bh, 0"
        + "\nmov bl, byte ptr [di]"
        + "\nsub bx, 30h"
        + "\ndiv bl"
        + "\nadd al, 30h"
        + "\nmov "+car1+", al"
        + "\nmov si, offset "+car1+"+2"
        + "\nmov byte ptr [si], al");

}



else if (simbolo.equals("^") && car2.matches(idasm) && car3.matches(ctzint)){// xd

    codigo.add("\n;potencia id ct"
 + "\nmov si, offset "+car2+"+2"
 + "\nmov di, offset "+car3+"+2"
 + "\nmov ah, 0"
 + "\nmov al, byte ptr [si]"
 + "\nsub ax, 30h"
 + "\nmov bh, 0"
 + "\nmov bl, "+car3
+"\nadd bl, 30h"
 + "\nsub bx, 30h"
+"\ncmp bl, 0"
+"\nje evaluar"+itp
 + "\nmov cx, bx"
 + "\nxor bx, bx"
 + "\nmov bx, ax"
 + "\nxor ax, ax"
 + "\nmov al, 1"
 + "\npotencia:"
 + "\nmul bl"
 + "\nloop potencia"
 //+ "\nmov byte ptr [si], al"
 //+ "\nmov byte ptr [si+1],'$'"
 + "\nadd ax, 30h"
 + "\nmov si, offset "+car1+"+2"
 + "\nmov byte ptr [si], al"
+"\njmp fin_evaluar_0"
+"\nevaluar"+itp+":"
+"\nmov dx, offset "+car1+"+2"
+"\nmov si, dx"
+"\nmov [si], 1"
+"\nadd [si], 30h"
+"\njmp fin_evaluar_0"
+"\nfin_evaluar_0:");

itp++;

}


else if (simbolo.equals("%") && car2.matches(idasm) && car3.matches(ctzint)){//ojo

    codigo.add("\n;modulo id ct"
 + "\nxor ax, ax"
 + "\nmov si, offset "+car2+"+2"
 + "\nmov al, byte ptr [si]"
 + "\nxor bx, bx"
 + "\nmov bl, "+car3
 +"\ndiv bl"
 + "\nadd ah, 30h"
 + "\nmov si, offset "+car1+"+2"
 + "\nmov byte ptr [si], ah");

}
else
{
   System.err.println("error en la linea, "+(i+1)+" los tipos de datos no coinciden");
}










} 

else if ((tipodato(car1).equals ("zfloat")  ) && (tipodato(car2).equals ("zfloat" )  ) && (tipodato(car3).equals ("zfloat") ) || (( tipodato(car1)=="" ) || ( tipodato(car2)=="" ) || ( tipodato(car3)==""  ))){
        System.err.println(car1 +" es "+tipodato(car1));
        System.err.println(car2 +" es "+tipodato(car2));
        System.err.println(car3 +" es "+tipodato(car3));

        System.out.println("todo ok zfloat linea "+(i+1));

if(simbolo.equals("+") && car2.matches(idasm) && car3.matches(idasm)){//-----------------------op aritmeticas con id



}
else if (simbolo.equals("_") && car2.matches(idasm) && car3.matches(idasm)){


}
else if(simbolo.equals("*") && car2.matches(idasm) && car3.matches(idasm)){


}
else if (simbolo.equals("/") && car2.matches(idasm) && car3.matches(idasm)){


}
else if (simbolo.equals("+") && car2.matches(ctzfloat) && car3.matches(ctzfloat)){//--------------op aritmeticas con cte


}
else if (simbolo.equals("_") && car2.matches(ctzfloat) && car3.matches(ctzfloat)){


}
else if(simbolo.equals("*") && car2.matches(ctzfloat) && car3.matches(ctzfloat)){


}
else if (simbolo.equals("/") && car2.matches(ctzfloat) && car3.matches(ctzfloat)){


}
else if (simbolo.equals("+") && car2.matches(ctzfloat) && car3.matches(idasm)){//--------------op aritmeticas con cte y id


}
else if (simbolo.equals("_") && car2.matches(ctzfloat) && car3.matches(idasm)){


}
else if(simbolo.equals("*") && car2.matches(ctzfloat) && car3.matches(idasm)){


}
else if (simbolo.equals("/") && car2.matches(ctzfloat) && car3.matches(idasm)){


}
else if (simbolo.equals("+") && car2.matches(idasm) && car3.matches(ctzfloat)){//--------------op aritmeticas con id y cte


}
else if (simbolo.equals("_") && car2.matches(idasm) && car3.matches(ctzfloat)){


}
else if(simbolo.equals("*") && car2.matches(idasm) && car3.matches(ctzfloat)){


}
else if (simbolo.equals("/") && car2.matches(idasm) && car3.matches(ctzfloat)){


}



}

else {


       

        System.err.println("error en la linea, "+(i+1)+" los tipos de datos no coinciden");
}
}else{
    System.err.println("error en la linea "+(i+1)+", la variable "+car1+" no existe");
}

}


}


else if (sp[x].equals(">>>")){//-------------------------------------------------------------------------------------------nueva asignacion

String asig = sp[x+1];
String nasig = sp[x+3];

if (asig.matches(ctzint) || asig.matches(ctzfloat)){
    System.err.println("error en la linea "+(i+1)+", el caracter '"+asig+"' debe ser un ID");
}else if (!vertabla(asig)){


if(tipodato(asig).equals ("Ztring") ){

if(nasig.matches(ctZtring)){

//tabla [contenido(asig)][2] = nasig;

 System.out.println("------------------------------------------------------------------------->"+tabla [contenido(asig)][0]);
        System.out.println("------------------------------------------------------------------------->"+tabla [contenido(asig)][1]);
        System.out.println("------------------------------------------------------------------------->"+tabla [contenido(asig)][2]);

}else{
    System.err.println("error en la linea "+(i+1)+" los tipos de dato no coinciden");
}

}

else if(tipodato(asig).equals ("zint") ){

if(nasig.matches(ctzint)){

//tabla [contenido(asig)][2] = nasig;
codigo.add("\n;asignacion\nmov dx, offset "+asig+"+2"
 + "\nmov si, dx"
 + "\nmov [si], "+nasig+""
 + "\nadd [si], 30h");

 System.out.println("------------------------------------------------------------------------->"+tabla [contenido(asig)][0]);
        System.out.println("------------------------------------------------------------------------->"+tabla [contenido(asig)][1]);
        System.out.println("------------------------------------------------------------------------->"+tabla [contenido(asig)][2]);

}else{
    System.err.println("error en la linea "+(i+1)+" los tipos de dato no coinciden");
}

}

else if(tipodato(asig).equals ("zfloat") ){

if(nasig.matches(ctzfloat)){

//tabla [contenido(asig)][2] = nasig;


 System.out.println("------------------------------------------------------------------------->"+tabla [contenido(asig)][0]);
        System.out.println("------------------------------------------------------------------------->"+tabla [contenido(asig)][1]);
        System.out.println("------------------------------------------------------------------------->"+tabla [contenido(asig)][2]);

}else{
    System.err.println("error en la linea "+(i+1)+" los tipos de dato no coinciden");
}

}




/*
tabla [contenido(asig)][2] = nasig;

 System.out.println("------------------------------------------------------------------------->"+tabla [contenido(asig)][0]);
        System.out.println("------------------------------------------------------------------------->"+tabla [contenido(asig)][1]);
        System.out.println("------------------------------------------------------------------------->"+tabla [contenido(asig)][2]);

*/


}





}





else if(sp[x].equals("if")){

String op= sp[x+3];

String tkn1 = sp[x+2];
String tkn2 = sp[x+4];



if (tipodato(tkn1).equals("Ztrig") || tkn1.matches(ctZtring)){
if(op.matches(opZtring)){
if(tipodato(tkn2).equals("Ztrig") || tkn2.matches(ctZtring)){
    System.out.println("todo ok con if ztring");
}else if (vertabla(tkn1) && vertabla(tkn2)){
    System.err.println("error en la linea "+(i+1)+" los tipos de dato no coinciden");
}
}else if( !tkn2.matches(ctZtring) ){
    System.err.println("error en la linea "+(i+1)+" los tipos de dato no coinciden");    
}else{
System.err.println("error en la linea "+(i+1)+" operador logico "+op+" no valido para Ztring");
}
}


else if (tipodato(tkn1).equals("zint") || tkn1.matches(ctzint)){
if(op.matches(opzfi)){
if(tipodato(tkn2).equals("zint") || tkn2.matches(ctzint)){
    System.out.println("todo ok con if int");

codigo.add("\n;condicional if"
 + "\nxor ax,ax"
 + "\nmov si, offset "+ sp[x+2]+"+2");//---------------------------------------------------------------genif

if(sp[x+2].matches(ctzint)){
codigo.add("\nmov ah, "+sp[x+2]+""
 + "\nadd ah, 30h");
}else{
codigo.add("\nmov ah, byte ptr [si]");
}

codigo.add("\nmov di, offset "+sp[x+4]+"+2");

if(sp[x+4].matches(ctzint)){
codigo.add("\nmov al, "+sp[x+4]+""
 + "\nadd al, 30h");
}else{
codigo.add("\nmov al, byte ptr [di]");
}


codigo.add("\ncmp ah,al");


petiif=("etiif"+etiif);
etiif++;
tknif.push(petiif);//--------------------------------------------------------------------------------


switch (sp[x+3]){

case ">":
codigo.add("\njbe "+petiif+":");
break;

case "<":
codigo.add("\njae "+petiif+":");
break;

case "==":
codigo.add("\njne "+petiif+":");
break;

case "!=":
codigo.add("\nje "+petiif+":");
break;

case ">=":
codigo.add("\njb "+petiif+":");
break;

case "<=":
codigo.add("\nja "+petiif+":");
break;

}









}else if (vertabla(tkn1) && vertabla(tkn2) ){
    System.err.println("error en la linea "+(i+1)+" los tipos de dato no coinciden");
}else if (!tkn2.matches(ctzint)){
    System.err.println("error en la linea "+(i+1)+" los tipos de dato no coinciden");
}
}else{
    System.err.println("error en la linea "+(i+1)+" operador logico "+op+" no valido para zint");
}
}



else if(tipodato(tkn1).equals("zfloat") || tkn1.matches(ctzfloat)){
if(op.matches(opzfi)){
if(tipodato(tkn2).equals("zfloat") || tkn2.matches(ctzfloat)){
    System.out.println("todo ok con if zfloat");
}else if (vertabla(tkn1) && vertabla(tkn2)){
    System.err.println("error en la linea "+(i+1)+" los tipos de dato no coinciden");
}
}else if (!tkn2.matches(ctzint)){
    System.err.println("error en la linea "+(i+1)+" los tipos de dato no coinciden");
}else{
}System.err.println("error en la linea "+(i+1)+" operador logico "+op+" no valido para zfloat");
}
}


else if(sp[x].equals("while")){


String op2= sp[x+3];

String tkn12 = sp[x+2];
String tkn22 = sp[x+4];



if (tipodato(tkn12).equals("Ztrig") || tkn12.matches(ctZtring)){
if(op2.matches(opZtring)){
if(tipodato(tkn22).equals("Ztrig") || tkn22.matches(ctZtring)){
    System.out.println("todo ok con if ztring");
}else if (vertabla(tkn12) && vertabla(tkn22)){
    System.err.println("error en la linea "+(i+1)+" los tipos de dato no coinciden");
}
}else if( !tkn22.matches(ctZtring) ){
    System.err.println("error en la linea "+(i+1)+" los tipos de dato no coinciden");    
}else{
System.err.println("error en la linea "+(i+1)+" operador logico "+op2+" no valido para Ztring");
}
}


else if (tipodato(tkn12).equals("zint") || tkn12.matches(ctzint)){
if(op2.matches(opzfi)){
if(tipodato(tkn22).equals("zint") || tkn22.matches(ctzint)){
    System.out.println("todo ok con while int");

codigo.add("\n;condicional while"
 + "\nxor ax,ax"
 + "\nwhile"+loopw+":"
 + "\nmov si, offset "+ sp[x+2]+"+2");//---------------------------------------------------------------genwhile

if(sp[x+2].matches(ctzint)){
codigo.add("\nmov ah, "+sp[x+2]+""
 + "\nadd ah, 30h");
}else{
codigo.add("\nmov ah, byte ptr [si]");
}

codigo.add("\nmov di, offset "+sp[x+4]+"+2");

if(sp[x+4].matches(ctzint)){
codigo.add("\nmov al, "+sp[x+4]+""
 + "\nadd al, 30h");
}else{
codigo.add("\nmov al, byte ptr [di]");
}


codigo.add("\ncmp ah,al");

petiif=("etiwhile"+loopw);//loopw++;
loopw++;




switch (sp[x+3]){

case ">":
codigo.add("\njbe "+petiif+":");
break;

case "<":
codigo.add("\njae "+petiif+":");
break;

case "==":
codigo.add("\njne "+petiif+":");
break;

case "!=":
codigo.add("\nje "+petiif+":");
break;

case ">=":
codigo.add("\njb "+petiif+":");
break;

case "<=":
codigo.add("\nja "+petiif+":");
break;

}




}else if (vertabla(tkn12) && vertabla(tkn22) ){
    System.err.println("error en la linea "+(i+1)+" los tipos de dato no coinciden");
}else if (!tkn22.matches(ctzint)){
    System.err.println("error en la linea "+(i+1)+" los tipos de dato no coinciden");
}
}else{
    System.err.println("error en la linea "+(i+1)+" operador logico "+op2+" no valido para zint");
}
}



else if(tipodato(tkn12).equals("zfloat") || tkn12.matches(ctzfloat)){
if(op2.matches(opzfi)){
if(tipodato(tkn22).equals("zfloat") || tkn22.matches(ctzfloat)){
    System.out.println("todo ok con if zfloat");
}else if (vertabla(tkn12) && vertabla(tkn22)){
    System.err.println("error en la linea "+(i+1)+" los tipos de dato no coinciden");
}
}else if (!tkn22.matches(ctzint)){
    System.err.println("error en la linea "+(i+1)+" los tipos de dato no coinciden");
}else{
}System.err.println("error en la linea "+(i+1)+" operador logico "+op2+" no valido para zfloat");
}

}

else if (sp[x].equals("for") || sp[x].equals("	for")){//-------------------------------------------genfor

tkn2for = sp[x+2];
    
if (sp[x+3].equals("//")){
    System.out.println("correcto primer bloque de for");

String opf= sp[x+5];

String tkn1f = sp[x+4];
String tkn2f = sp[x+6];

if (tipodato(tkn1f).equals("zint") || tkn1f.matches(ctzint)){
if(opf.equals("->")){
if(tipodato(tkn2f).equals("zint") || tkn2f.matches(ctzint)){
    System.out.println("todo ok con if int");



}else if (vertabla(tkn1f) && vertabla(tkn2f) ){
    System.err.println("error en la linea "+(i+1)+" los tipos de dato no coinciden");
}else if (!tkn2f.matches(ctzint)){
    System.err.println("error en la linea "+(i+1)+" los tipos de dato no coinciden");
}



}else{
    System.err.println("error en la linea "+(i+1)+" operador "+opf+" no valido ");
}

if(sp[x+7].equals("//")){
    System.out.println("todo ok segundo bloque");

if( (sp[x+9].equals("++") || sp[x+9].equals("--")) ){
    System.out.println("todo ok con operador de iteracion");


petifor = "\nfor"+loopf;
loopf++;

codigo.add("\n;ciclo for"
 + "\nmov si, offset "+ sp[x+2]+"+2");

tknf.push(sp[x+2]);
    //System.err.println(sp[x+2]);

if (sp[x+4].matches(idasm)){
codigo.add("\nmov di, offset "+sp[x+4]+"+2"
 + "\nmov al, byte ptr [di]"
 + "\nmov byte ptr [si], al"
 + "\nxor bx, bx"
 + "\nmov bl, al");

}else{
codigo.add("\nmov byte ptr [si], "+sp[x+4]+"+30h");// spx+2 xpx+4 spx+6 spx+8
}
if(sp[x+6].matches(ctzint)){
codigo.add("\nmov cx, "+sp[x+6]);
if(sp[x+4].matches(idasm)){
codigo.add("\nmov bh, 0"
 + "\nmov bl, byte ptr [si]"
 + "\nsub bx, 30h"
 + "\nsub cx, bx"
 + "\nadd cx, 1");
}else{
codigo.add("\nsub cx, "+sp[x+4]+"-1");
}
}else{
codigo.add("\nxor cx, cx"
 + "\nmov si, offset "+sp[x+6]+"+2"
 + "\nmov cl, byte ptr [si]");
if(sp[x+4].matches(ctzint)){
codigo.add("\nsub cl, 30h"
 + "\nsub cl, "+sp[x+4]+"-1");
}else{
codigo.add("\nsub cl, bl"
 + "\nadd cl, 1");
}

}

codigo.add(petifor+":"
 + "\npush cx");





}else{
    System.err.println("error en la linea"+(i+1)+" operador de iteracion de for no valido");
}

}else{
System.err.println("error en la linea "+(i+1)+" '//' entre el segundo bloque y el tercero");
}

}




}else{
System.err.println("error en la linea "+(i+1)+" '//' entre el primer bloque y el segundo");
}



}












else if (sp[x].matches(idasm)){


    System.err.println("error en la linea "+(i+1)+", "+"la variable '"+sp[x]+"' no existe");
}else{
    System.err.println("error en la linea "+(i+1)+", "+"caracter '"+sp[x]+"' no valido");
}




if (sp[x].equals("if")){
conif++;//4-6
errif = i;
finalif = conif;//4-6

}


if(sp[x].equals("if;")){

fconif++;//de errores
finalif = finalif-1;//-- y hago decremento
fetiif++;// de etiquetas
codigo.add("\n"+tknif.pop()+":");
}


if (sp[x].equals("for")){
contfor++;
errfor = i;
finalf=contfor;
}

if (sp[x].equals("for;")){
fcontfor++;
    
finalf = finalf -1;
codigo.add("\nmov si, offset "+tknf.pop()+"+2"
 + "\nadd byte ptr [si], 1"
 + "\npop cx"
 + "\nloop for"+finalf);

}





if (sp[x].equals("while")){
contwhile++;
errwhile = i;
finalw = contwhile;//--guardo el numero maximo de ciclos while para poder escribirlos del mayor al menor
}
if (sp[x].equals("while;")){
fcontwhile++;//de errores

finalw = finalw-1;//-- y hago decremento

codigo.add("\njmp while"+(finalw)+":");
codigo.add("\netiwhile"+(finalw)+":");

fetiwhile++;//de etiquetas

  

    

}





}



}//fin leector

if (conif != fconif){
    System.err.println("ciclo if sin cerrar en la linea"+(errif+1));
}

if (contfor != fcontfor){
    System.err.println("ciclo for sin cerrar en la linea"+(errfor+1));
}

if (contwhile != fcontwhile){
    System.err.println("ciclo while sin cerrar en la linea"+(errwhile+1));
}

data.add("\nsalto db 10,13,'$'");

}catch (Exception e){
System.err.println("algo paso");

}

}

public static int contenido (String Nvariable){//-----------------------------------------------------------------> extraccion del contenido

int x=0;

for(int i=0;i<100;i++){
if(Nvariable.equals(tabla[i][1])){
x = i;
}
}

return x;
}

}
