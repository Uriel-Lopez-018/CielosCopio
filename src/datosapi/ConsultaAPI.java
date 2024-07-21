package datosapi;

//package datos;
import java.nio.charset.StandardCharsets;
import com.google.gson.Gson;
import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Scanner;


// Clase principal
public class ConsultaAPI {

    private static final String API_KEY = "a29502b0f11671bebacc5c5c355b41c5";
    private static final String GEO_API_URL = "http://api.openweathermap.org/geo/1.0/direct?q=%s&limit=1&appid=%s";
    private static final String WEATHER_API_URL = "https://api.openweathermap.org/data/2.5/weather?lat=%s&lon=%s&units=metric&appid=%s";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ConsultaAPI consultaAPI = new ConsultaAPI();

        while (true) {
            System.out.println("Elige una ciudad para obtener los datos meteorologicos:");
            System.out.println("---------------------------------");
            System.out.println("1- Ciudad de Mexico");
            System.out.println("2- Buenos Aires");
            System.out.println("3- Bogota");
            System.out.println("4- Lima");
            System.out.println("5- Santiago");
            System.out.println("6- Desea consultar otra ciudad");
            System.out.println("7- Salir");
            System.out.println("---------------------------------");

            int opcion = scanner.nextInt();
            scanner.nextLine(); // Consumir la nueva línea

            if (opcion == 7) {
                System.out.println("Saliendo del programa...");
                break;
            }

            String ciudad = null;
            switch (opcion) {
                case 1: ciudad = "Ciudad de Mexico"; break;
                case 2: ciudad = "Buenos Aires"; break;
                case 3: ciudad = "Bogota"; break;
                case 4: ciudad = "Lima"; break;
                case 5: ciudad = "Santiago"; break;
                case 6:
                    System.out.print("Introduce el nombre de la ciudad: ");
                    ciudad = scanner.nextLine();
                    break;
                default:
                    System.out.println("Opción no válida, intenta de nuevo.");
                    continue;
            }

            Clima clima = consultaAPI.busquedaClima(ciudad);
            if (clima != null) {
                DatosClima datosClima = consultaAPI.obtenerDatosClima(clima.getLat(), clima.getLon());
                consultaAPI.mostrarDatosClima(ciudad, datosClima);
            } else {
                System.out.println("No se encontraron datos para la ciudad " + ciudad);
            }
        }

        scanner.close();
    }

    // Método para buscar la latitud y longitud de la ciudad



        // Método para buscar la latitud y longitud de la ciudad
        public Clima busquedaClima(String name) {
            try {
                String encodedCityName = URLEncoder.encode(name, StandardCharsets.UTF_8.toString());
                String url = String.format(GEO_API_URL, encodedCityName, API_KEY);
                HttpClient client = HttpClient.newHttpClient();
                HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).build();
                HttpResponse<String> response;

                try {
                    response = client.send(request, HttpResponse.BodyHandlers.ofString());
                } catch (IOException | InterruptedException e) {
                    throw new RuntimeException(e);
                }

                Clima[] climas = new Gson().fromJson(response.body(), Clima[].class);
                return climas.length > 0 ? climas[0] : null;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }





    // Método para obtener los datos del clima usando latitud y longitud
    public DatosClima obtenerDatosClima(double lat, double lon) {
        String url = String.format(WEATHER_API_URL, lat, lon, API_KEY);
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).build();
        HttpResponse<String> response;

        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }

        return new Gson().fromJson(response.body(), DatosClima.class);
    }

    // Método para mostrar los datos del clima
    public void mostrarDatosClima(String ciudad, DatosClima datosClima) {
        System.out.println("Ciudad: " + ciudad);
        System.out.println("Fecha: " + datosClima.getFecha());
        System.out.println("Horario: " + datosClima.getHorario());
        System.out.println("Temperatura: " + datosClima.getMain().getTemp());
        System.out.println("Temperatura Máxima: " + datosClima.getMain().getTemp_max());
        System.out.println("Precipitación: " + (datosClima.getRain() != null ? datosClima.getRain().get1h() : "valor no encontrado"));
        System.out.println("--------------------------------");
    }
}






