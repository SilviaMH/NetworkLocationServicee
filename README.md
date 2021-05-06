# Servicio de Localización 

Aplicación para rastrear la ubicación del usuario y actualizarla en un intervalo y una distancia mínima personalizada.

## Construcción 🛠️

Se desarrolló la app en Android Studio y se tomaron en cuenta los siguientes aspectos.

* Solicitar algunos de los siguientes permisos para acceder a la ubicación:
  - Manifest.permission.ACCESS_COARSE_LOCATION
  - Manifest.permission.ACCESS_FINE_LOCATION
  - Manifest.permission.INTERNET

* Utilizar un MapsActivity para visualizar la ubicación del usuario en un mapa y sobrecargar el método onMapReady().
* Registrar en el LocationListener las actualizaciones de ubicación que solicita el LocationManager.
* Cada vez que el usuario cambia alguno de los parámetros de la frecuencia de actualización (tiempo y distancia), se debe remover la petición anterior y solicitar una nueva con los métodos removeUpdates() y requestLocationUpdates() correspondientemente. Si no se remueve la petición anterior, el LocationListener no registrará las peticiones con los nuevos parámetros.

## Autor ✒️

**Silvia Martínez H** 
