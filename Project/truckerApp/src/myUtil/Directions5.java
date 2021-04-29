package myUtil;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.time.LocalDateTime;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

//?΄? κ³μ°? ?? ?΄??€, μ΅μ’? ?Όλ‘? μΆμ² ?κΈμ κ³μ°??€.
public class Directions5 {
	public String ETA; //?μ°©μκ°?
	public int distance;	//κ±°λ¦¬
	public double speed;	//?κ·? ??
	public int time;	//???κ°?(μ΄?)
	public int toll_cost;	//?¨λΉ?
	
	public double public_fuel_rate;	//κ³΅μΈ?°λΉ?
	public double fuel_rate;	//?°λΉ?
	public double fuel_cost_rate;	//λ¦¬ν°?Ή ?°λ£λ¨κ°?
	public double fuel_cost;	//?°λ£λΉ
	
	public double maintenance_rate;	//κ±°λ¦¬?Ή ? μ§?λΉ?
	public double maintenance_cost;	//? μ§?λΉ?
	
	public int help_cost;
	public int labor_rate;	//?κ°λΉ ?Έκ±΄λΉ
	public int labor_cost;	//?Έκ±΄λΉ
	
	public double sub_total;
	
	public double commission;	//??λ£?
	public double vat;			//λΆ?κ°??Έ
	
	public int recommend_cost;	//μΆμ² ?΄?
	// ?κ·? ?? = κ±°λ¦¬ * 1.5, MAX 80
	// ?°λΉ? = κ³΅μΈ?°λΉ? * μ°¨λλ¬΄κ² / μ°¨λ + ?λ¬Όλ¬΄κ²?
	
	// μΆμ²?κΈ? = ?¨λΉ? + ?°λ£λΉ + ? μ§?λΉ? + ?Έκ±΄λΉ + ??? + ??λ£? + λΆ?κ°??Έ ?? 1000?¨? λ―Έλ§ ? ?¬
	// ?°λ£λΉ = λ¦¬ν°?Ή ?°λ£λΉ * κ±°λ¦¬ / ?°λΉ?
	// ? μ§?λΉ? = κ±°λ¦¬ * κ±°λ¦¬?Ή ? μ§?λΉ?
	// ?Έκ±΄λΉ = ???κ°?  * ?κ°λΉ ?Έκ±΄λΉ + ???
	// ??λ£? = ( ?¨λΉ? + ?°λ£λΉ + ? μ§?λΉ? + ?Έκ±΄λΉ ) * 8%
	// λΆ?κ°??Έ = ( ?¨λΉ? + ?°λ£λΉ + ? μ§?λΉ? + ?Έκ±΄λΉ  + ??λ£?) * 10%
	
	public String result; //κ²°κ³Ό JSON
	public void Direction(String from_where, String to_where, String truck_type, int cargo_weight, String cargo_help, String depart_time) {
		//μΆλ°μ§?? ?μ°©μ? μ’ν λ°μ?€κΈ?
		GeoCode from = new GeoCode();
		from.getGPS(from_where);
		System.out.println(from.x);
		System.out.println(from.y);
		
		GeoCode to = new GeoCode();
		to.getGPS(to_where);
		System.out.println(to.x);
		System.out.println(to.y);
		
		//κ±°λ¦¬ κ³μ°?°?΄?° λ°κΈ°
		try {
			//URL?΄? μ£Όμ ?? ₯, μ£Όμ? UTF-8λ‘? ?Έμ½λ©
			URL url = new URL("https://naveropenapi.apigw.ntruss.com/map-direction/v1/driving?X-NCP-APIGW-API-KEY-ID=tzchnboziv&X-NCP-APIGW-API-KEY=svQaeTRI6EoOAufCfliJfjFQlKpoNcjl4ZY5UMX7&start=" 
			+ from.x + "," + from.y + "&goal=" + to.x + "," + from.y + "&cartype=4");
			
			HttpURLConnection con = (HttpURLConnection)url.openConnection();
			con.setRequestMethod("GET");
			
			//μ‘°ν? ?°?΄?°λ₯? responseλ‘? λ°κΈ°
			int responseCode = con.getResponseCode();
			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();
			
			while((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();
			
			//JSON ?°?΄?° ??±
			JSONParser parser = new JSONParser();
			JSONObject jsonObj = (JSONObject) parser.parse(response.toString());
			System.out.println(jsonObj.toString());
			jsonObj = (JSONObject) jsonObj.get("route");
			JSONArray jsonArray = (JSONArray) jsonObj.get("traoptimal");
			jsonObj = (JSONObject) jsonArray.get(0);
			jsonObj = (JSONObject) jsonObj.get("summary");
			
			// κ°?? Έ?€? ?°?΄?°? κ±°λ¦¬? ?¨λΉ?
			distance = Integer.valueOf(jsonObj.get("distance").toString()) * 1609 / 1000;		//λ°?λ¦? λ§μΌ ?¨?λ‘? κ³μ°?? ?―, 1.609 κ³±νλ©? ? ?Ή? ??¬ ?―
			toll_cost = Integer.valueOf(jsonObj.get("tollFare").toString());
			// κ°? λ³?? κ³μ°
			// ??
			speed = distance * 1.5;
			if(speed > 80) speed = 80;
			//?κ°?
			time = (int)( distance / 1000 / speed + 1 ) * 60 * 60; // ?κ°? = κ±°λ¦¬ / ?? + 1?κ°?, ?¨?? μ΄?
			//?μ°©μ? ?Ό?
			ETA = LocalDateTime.parse(depart_time).plusSeconds(time).toString();
			//?°λΉ?, ?°λ£λ¨κ°?, ? μ§??¨κ°?
			if(truck_type.equals("damas")) {
				public_fuel_rate = 8.8;
				fuel_rate = public_fuel_rate * 865 / ( 865 + cargo_weight);
				fuel_cost_rate = 950;
				maintenance_rate = 2000000 / 30000;	//?° ? μ§?λΉ? 200λ§μ / 5λ§ν€λ‘?
			}
			if(truck_type.equals("labo")) {
				public_fuel_rate = 8.6;
				fuel_rate = public_fuel_rate * 760 / ( 760 + cargo_weight);
				fuel_cost_rate = 950;
				maintenance_rate = 2000000 / 30000;
			}
			if(truck_type.equals("1ton")) {
				public_fuel_rate = 9.9;
				fuel_rate = public_fuel_rate * 1825 / ( 1825 + cargo_weight);
				fuel_cost_rate = 1300;
				maintenance_rate = 3500000 / 50000;
			}
			if(truck_type.equals("1.4ton")) {
				public_fuel_rate = 9.0;
				fuel_rate = public_fuel_rate * 1800 / ( 1800 + cargo_weight);
				fuel_cost_rate = 1300;
				maintenance_rate = 4000000 / 50000;
			}
			if(truck_type.equals("2.5ton")) {
				public_fuel_rate = 7.5;
				fuel_rate = public_fuel_rate * 2500 / ( 2500 + cargo_weight);
				fuel_cost_rate = 1300;
				maintenance_rate = 5000000 / 70000;
			}
			//?°λ£λΉ
			fuel_cost = distance / 1000 * fuel_cost_rate / fuel_rate; 
			//? μ§?λΉ?
			maintenance_cost = distance / 1000 * maintenance_rate;
			//?Ή?μ°? ???
			if(cargo_help.equals("drive_only")){
				help_cost = 0;
			}
			if(cargo_help.equals("load_and_discharge")){
				help_cost = 10000;
			}
			if(cargo_help.equals("to_door")){
				help_cost = 30000;
			}
			//?Έκ±΄λΉ
			labor_rate = 10000;
			labor_cost = time * labor_rate / 60 / 60 + help_cost;

			//??λ£?, λΆ?κ°??Έ λ°? ?©κ³?
			sub_total = toll_cost + fuel_cost + maintenance_cost + labor_cost;
			commission = sub_total * 0.08;
			sub_total += commission;
			vat = sub_total * 0.1;
			recommend_cost = (int)(sub_total + vat)/1000*1000;
			System.out.println("μ°¨λ μ’λ₯ : " + truck_type);
			System.out.println("?λ¬? λ¬΄κ² : " + cargo_weight);
			System.out.println("?Ή?μ°? ??? : " + cargo_help);
			System.out.println("κ±°λ¦¬ : " + distance);
			System.out.println("?κ·? ?? : " + speed);
			System.out.println("?? ?κ°? : " + time);
			System.out.println("?μ°? ??  : " + ETA);
			System.out.println("κ³΅μΈ?°λΉ? : " + public_fuel_rate);
			System.out.println("?°λΉ? : " + fuel_rate);
			System.out.println("?°λ£λ¨κ°? : " + fuel_cost_rate);
			System.out.println("κ±°λ¦¬?Ή ? μ§?λΉ? : " + maintenance_rate);
			System.out.println("?Ή?μ°? ?Έκ±΄λΉ : " + help_cost);
			System.out.println("?κ°λΉ ?Έκ±΄λΉ : " + labor_rate);
			System.out.println("?¨λΉ? : " + toll_cost);
			System.out.println("?°λ£λΉ : " + fuel_cost);
			System.out.println("? μ§?λΉ? : " + maintenance_cost);
			System.out.println("?Έκ±΄λΉ : " + labor_cost);
			System.out.println("??λ£? : " + commission);
			System.out.println("λΆ?κ°??Έ : " + vat);
			System.out.println("μΆμ² ?΄? : " + recommend_cost);
			
			System.out.println("κ±°λ¦¬/?κΈκ³?° ?±κ³?");
		}
		catch(Exception e){
			System.out.println("κ±°λ¦¬/?κΈκ³?° ?€?¨");
		}
	}
}
