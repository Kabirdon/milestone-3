import java.util.Scanner;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONArray;
import org.json.JSONObject;

public class WeatherApi3{
    public static String getClothingSuggestion(double temperature) {
        if (temperature < 10) {
            return "It's cold, wear a jacket and scarf.";
        } else if (temperature < 20) {
            return "It's cool, wear a light jacket or sweater.";
        } else {
            return "It's warm, wear a t-shirt and shorts.";
        }
    }

    public static void main(String[] args) {
        try {
            Scanner scanner = new Scanner(System.in);

            // Greet the user and ask for their name
            System.out.println("Hi there! What's your name?");
            String name = scanner.nextLine();
            System.out.println("How are you?");
            String hey = scanner.nextLine();

            // Respond to the user's input
            System.out.println("Nice to meet you, " + name + "!");

            // Get the city from the user
            System.out.println("What city would you like to get the weather forecast for?");
            String city = scanner.nextLine();

            // Set the API endpoint URL and your API key
            String endpointUrl = "https://api.openweathermap.org/data/2.5/forecast?q=" + city
                    + "&appid=30fc7ee828b5d4f90ce78df1ac686aca&units=metric";

            // Create a new HTTP connection to the endpoint URL
            URL url = new URL(endpointUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            // Read the response from the API endpoint
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            // Parse the JSON response into a Java object
            JSONObject jsonObject = new JSONObject(response.toString());

            // Extract the forecast list from the API response
            JSONArray forecastList = jsonObject.getJSONArray("list");

            // Iterate over the forecast list and print weather information for each day
            for (int i = 0; i < forecastList.length(); i++) {
                JSONObject forecast = forecastList.getJSONObject(i);

                // Extract the date, temperature, and weather description from the forecast
                String date = forecast.getString("dt_txt");
                double temperature = forecast.getJSONObject("main").getDouble("temp");
                String weatherDescription = forecast.getJSONArray("weather").getJSONObject(0).getString("description");

                // Get the clothing suggestion based on the temperature
                String clothingSuggestion = getClothingSuggestion(temperature);

                // Print the date, temperature, weather description, and clothing suggestion
                System.out.println("\nDate: " + date);
                System.out.println("Temperature: " + temperature + " degrees Celsius");
                System.out.println("Weather: " + weatherDescription);
                System.out.println("You should wear: " + clothingSuggestion);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
