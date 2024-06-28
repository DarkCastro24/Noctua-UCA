## BACKEND DE PROYECTO NOCTUA UCA 

Para lanzar un proyecto de API backend usando MongoDB, se comienza instalando yarn y las dependencias necesarias como express y nodemon para la gestión del servidor. 

La estructura del proyecto se organiza en carpetas clave: Config para la configuración de la base de datos, Models para definir las colecciones de la base de datos, Controllers para las operaciones CRUD, y Routes para el manejo de rutas y acceso a las funciones. 

## COMANDOS PARA CORRER EL PROYECTO 

-- Instalar dependencias 
npm install 

-- Iniciar la ejecucion del servidor (requisito tener instaladas las herramientas) 
yarn dev 

## COMANDOS PARA INSTALAR HERRAMIENTAS DE DESARROLLO 

-- Instalar yarn de manera global 
npm install -g yarn 

-- Instalar express yarn 
add express o npm install express 

-- Instalar nodemon 
yarn global add nodemon 

-- Iniciar la ejecucion del servidor 
yarn dev 

## ESTRUCTURA DE BACKEND API DE MONGODB

# Archivos escenciales para el desarrollo del backend

Config: Contiene el archivo database.config.js donde esta la cadena de conexion con 
la base de datos de base trae la URI de mongo.db y las funciones para conectar y desconectar a la base.

Index.js: Archivo que permite levantar el server es el primer archivo que se ejecuta al 
levantar el servidor contiene la ruta base que es "/api"

# Carpetas a modificar para hacer CRUDS 

Models: Los archivos tienen el nombre de las tablas (colecciones) y contienen la estructura 
que tendra la base de datos: campos con su tipo de dato, importaciones necesarias y otras validaciones. 

Controllers: Contiene archivos con el nombre de las tablas (colecciones) y contienen todas las 
funciones que se van a realizar con la tabla: Crear, Modificar, Mostrar, Eliminar, Buscar por 
algun criterio.

Routes: El archivo principal es index.router.js importa todos los demas archivos de la carpeta 
y asigna la ruta que tendra cada archivo por ejemplo "/user" lo que generaria la ruta "api/user/"
donde luego de la / se agregaria el nombre de la funcion que se desea ejecutar siempre en ingles
para seguir el estandar establecido. 

Middlewares: Esta carpeta contiene funciones que se ejecutan entre las peticiones HTTP recibidas y 
los controladores que finalmente procesan estas peticiones,realizan diversas tareas como autenticación de usuarios, 
registro de actividad, manejo de errores, y validación de entradas.

Validators: Incluye un conjuntos de funciones que se encargan de validar los datos enviados a la aplicación antes de 
que sean procesados por los controladores. Estos validadores aseguran que los datos recibidos cumplan con todos 
los requisitos y formatos necesarios para su procesamiento, minimizando así errores y problemas de integridad de datos. 

Los otros archivos router clasifican el tipo de peticion de cada funcion de lo que realiza, las siguientes que tenemos 
disponibles son:

.Get: Select / Search / Get One
.Post: Insert
.Patch: Update
.Delete: Delete 

El estandar para las rutas sera "server/api/user/create" es muy importante respetar el estandar para tener 
una buena consistencia de datos y mejorar la legibilidad del codigo :D. Aporte de: Diego Castro