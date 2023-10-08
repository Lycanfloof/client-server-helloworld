
*Integrantes: Ariel Eduardo Pabón - A00368734, Jesús David Rodríguez - A00369829.*

A continuación se describe la implementación del *hello-world*:

### Client:

La parte del cliente se encarga de crear un proxy para el servicio "RequestHandler" del servidor y crear otro de sí mismo para enviarlo y que le hagan callback. Para este proceso se emplea el archivo de configuración "config.client", definiendo los endpoints que se utilizan para la creación de ambos proxies. Al final, el cliente llama al proceso "handleRequest" del proxy del servidor para mandarle solicitudes en forma de comandos.

### Server:

El servidor se encarga de exponer un objeto de tipo "RequestHandler" a través de un adaptador de ICE, empleando el archivo de configuración "server.config" para establecer los endpoints del servicio. Este objeto implementa la interfaz "RequestHandler" definida en el archivo "RequestHandler.ice" y se encarga de procesar las solicitudes de los usuarios con la lista de comandos especificada en el tiempo de construcción. También se encarga de mantener contacto con los usuarios a través de sus proxies, empleando el patrón de callback para mandarles mensajes.

Los comandos soportados son:

- measure-performance [tiempo en milisegundos]
- register
- list-clients
- to [recibidor]: [mensaje]
- broadcast [mensaje]
- list-ifs
- list-ports [nombre de host]
- execute [comando de sistema]
- prime-factors [número entero]
