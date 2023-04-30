

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONArray;
import org.json.JSONObject;

public class WeatherApiGUI3 extends JFrame implements ActionListener {
    private JLabel nameLabel, cityLabel;
    private JTextField nameTextField, cityTextField;
    private JTextArea weatherTextArea;
    private JButton submitButton;

    public WeatherApiGUI3() {
        // Set up the JFrame
        setTitle("Weather Forecast");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(700, 400);
        setResizable(true);
        setLocationRelativeTo(null);

        // Create components
        nameLabel = new JLabel("Name:");
        nameTextField = new JTextField();
        cityLabel = new JLabel("City:");
        cityTextField = new JTextField();
        weatherTextArea = new JTextArea();
        submitButton = new JButton("Get Weather");
        submitButton.addActionListener(this);

        // Set layout
        setLayout(new BorderLayout());

        // Create a panel for the input components
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(2,3 , 7, 5));
        inputPanel.add(nameLabel);
        inputPanel.add(nameTextField);
        inputPanel.add(cityLabel);
        inputPanel.add(cityTextField);
        add(inputPanel, BorderLayout.NORTH);

        // Add the weather text area to a scroll pane for more space
        JScrollPane scrollPane = new JScrollPane(weatherTextArea);
        add(scrollPane, BorderLayout.CENTER);

        // Add the submit button to the bottom of the frame
        add(submitButton, BorderLayout.SOUTH);

        pack(); // Adjusts the frame size based on the components
        setLocationRelativeTo(null); // Centers the frame on the screen
        setVisible(true);

        // Make the weather text area non-editable
        weatherTextArea.setEditable(false);

        // Display the JFrame
        setVisible(true);



    }

    public String getClothingSuggestion(double temperature) {
        if (temperature < 10) {
            return "It's cold, wear a jacket and scarf.";
        } else if (temperature < 20) {
            return "It's cool, wear a light jacket or sweater.";
        } else {
            return "It's warm, wear a t-shirt and shorts.";
        }
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == submitButton) {
            try {
                // Get the name and city from the text fields
                String name = nameTextField.getText();
                String city = cityTextField.getText();

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

                // Clear the weather text area
                weatherTextArea.setText("");

                // Specify the number of forecast days to display
                int numDays = 5;

                // Append weather information for each day to the text area
                for (int i = 0; i < forecastList.length(); i += 5) {
                    if (i / 5>= numDays) {
                        break;
                    }

                    JSONObject forecast = forecastList.getJSONObject(i);

                    // Extract the date, temperature, and weather description from the forecast
                    String date = forecast.getString("dt_txt");
                    double temperature = forecast.getJSONObject("main").getDouble("temp");
                    String weatherDescription = forecast.getJSONArray("weather").getJSONObject(0).getString("description");

                    // Get the clothing suggestion based on the temperature
                    String clothingSuggestion = getClothingSuggestion(temperature);

                    // Append the weather information to the text area
                    weatherTextArea.append("Date: " + date + "\n");
                    weatherTextArea.append("Temperature: " + temperature + " degrees Celsius\n");
                    weatherTextArea.append("Weather: " + weatherDescription + "\n");
                    weatherTextArea.append("Clothing Suggestion: " + clothingSuggestion + "\n\n");

                    if ((i + 8) >= forecastList.length()) {
                        break;
                    }
                }

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new WeatherApiGUI3();

            }
        });
    }
}

