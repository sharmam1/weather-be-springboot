package webApp.Weather.controller;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.json.JSONArray;
import org.json.JSONObject;

@RestController
public class WeatherController {

	@Autowired
	private RestTemplate restTemplate;

	@Value("${spring.application.apiKey}")
	private String apiKey;

	@Bean
	public RestTemplate restTemplate(RestTemplateBuilder builder) {

		return builder.build();
	}

	private String FORECAST_URL = "http://api.openweathermap.org/data/2.5/forecast?q={city}&APPID={apiKey}";

	
	//Approach 1 via HttpURLConnection.
	@GetMapping("/weather")
	@CrossOrigin(origins = "http://localhost:3000")
	public Weather current(@RequestParam(value = "city") String city) {
		String content = null;
		try {

			String url = "http://api.openweathermap.org/data/2.5/weather?units=metric&q=" + city
					+ "&appid=" + apiKey;
			URL obj = new URL(url);

			HttpURLConnection con = (HttpURLConnection) obj.openConnection();

			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();
			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}

			in.close();
			content = response.toString();

		} catch (Exception e) {
			System.out.print("ERROR : " + e);
			 return new Weather("Not Found","Not Found","Not Found","Not Found",0);
		}
		JSONObject root = new JSONObject(content);
		// coordinates
		// temperature-humidity-pressure
		JSONObject main = root.getJSONObject("main");
		// system
		JSONObject sys = root.getJSONObject("sys");
		// weather
		JSONArray wea = root.getJSONArray("weather");
		JSONObject weas = wea.getJSONObject(0);

		return new Weather(weas.getString("main"), weas.getString("description"), sys.getString("country"),
				root.getString("name"), main.getInt("temp"));
	}
	

	// //Approach 2 via restTemplate.
	// http://localhost:8080/weather?city=Melbourne,Au
	@GetMapping("/weather1")
	@CrossOrigin(origins = "http://localhost:3000")
	@ResponseBody
	public List<Object> getWeather(@RequestParam String city) {

		FORECAST_URL = FORECAST_URL.replace("{city}", city);
		FORECAST_URL = FORECAST_URL.replace("{apiKey}", apiKey);
		System.out.println(FORECAST_URL);

		Object[] weather = restTemplate.getForObject(FORECAST_URL, Object[].class);
		return Arrays.asList(weather);
	}

	// http://localhost:8080/healthcheck
	@GetMapping("/healthcheck")
	public List<WeatherHealth> healthcheck() {
		return Arrays.asList(new WeatherHealth(200, "Up"));
	}
}
