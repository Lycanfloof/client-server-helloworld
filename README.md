
*Integrantes: Ariel Eduardo Pabón - A00368734, Jesús David Rodríguez - A00369829.*

A continuación se describe la implementación del *hello-world*:

### Client:
La parte del cliente se encarga de crear un proxy al emplear el archivo de configuración "config.client" en el que se define el host con el que se va a conectar (el servidor), el protocolo de comunicación, el puerto y un identificador del objeto al que se va a conectar. En este caso se conecta con el objeto "SimplePrinter" y emplea el método "printString" para enviar comandos al servidor en un bucle. Este último se acaba al escribir "exit" en la consola.

### Server:
El servidor se encarga de crear un adaptador con la propiedad "Printer", la que establece el protocolo de comunicación a emplear y el puerto. También se define un host por defecto que se emplea para tomarlo como punto de conexión. Luego, se crea un objeto del tipo "PrinterI" que implementa la interfaz "Printer", definida en el archivo "Printer.ice". Este objeto se encarga de procesar el mensaje, ejecutar un comando si es del caso e imprimir el resultado por consola con el hostname remoto y el usuario, para finalmente mandárselo al cliente.

### PrinterI:

Esta clase se encarga de recibir la solicitud, procesarla empleando la clase "Request" y retornar e imprimir la respuesta. También es responsable de calcular las métricas de rendimiento del servidor con el método *getPerfomanceReport*.

### Request:

Esta clase se encarga de leer y procesar la solicitud del cliente a través del método principal *executeRequest*. Este emplea los métodos auxiliares *executeCommand*, *getNumber* y *getPrimeFactors* para ejecutar comandos en la terminal, convertir una cadena de texto a un número y obtener los factores primos de un número respectivamente.

Los comandos soportados son:
- *listifs* - Lista las interfaces lógicas configuradas en el servidor.
- *listports [host o dirección_ip]* - Lista los puertos abiertos del host especificado.
- *[número entero]* - Retorna los factores primos del número.
- *![comando]* - Ejecuta el comando escrito por el usuario.

### Métricas calculadas por software:

$Latencia=TiempoFinal-TiempoInicial$

- Tiempo_Final hace referencia al instante en el que se acabó de procesar la solicitud. 

- Tiempo_Inicial hace referencia al instante en el que se empezó a procesar la solicitud.

- La medición se hizo en nanosegundos y se convirtió a milisegundos al multiplicar la $Latencia$ por $10^{-6}$.

$Throughput=\frac{CantidadSolicitudes}{Tiempo}$

- En este caso, la cantidad de solicitudes va aumentando a medida que las va procesando, y el tiempo es la suma de las latencias por parte del servidor para cada solicitud. La medición se muestra en segundos, ya que se multiplicó el tiempo por $10^{-9}$.

$ReceivedRequests=ProcessedRequests+UnprocessedRequests$

$SuccessRate=\frac{ProcessedRequests}{ReceivedRequests}\cdot100$%

$UnprocessedRate=\frac{UnprocessedRequests}{ReceivedRequests}\cdot100$%

