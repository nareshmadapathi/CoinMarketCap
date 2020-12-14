package CoinMarketCap.CoinMarketCap;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class JsonReader {

	public String getJsonValue(String jsonString, String matchString) throws ParseException {
		String str = null;
		try {
			JSONParser jsonParser = new JSONParser();
			Object obj = jsonParser.parse(jsonString);
			Object value = null;
			if (obj instanceof JSONObject) {
				JSONObject jsonObj = (JSONObject) obj;
				for (Object keyObject : jsonObj.keySet()) {
					Object key = keyObject;
					value = jsonObj.get(key);
					if (key.toString().equals(matchString)) {
						str = value.toString();
						break;
					} else {
						if (value instanceof JSONObject) {
							str = getJsonValue(value.toString(), matchString);
						} else {
							if (value instanceof JSONArray)
								str = jsonArray(value.toString(), matchString);
						}
					}

				}
			}
		} catch (Exception e) {
			
		}

		return str;
	}

	public String jsonArray(String jsonString, String matchString) throws ParseException {

		JSONParser jsonParser = new JSONParser();
		JSONArray jsonArray = (JSONArray) jsonParser.parse(jsonString);
		String value = null;
		for (int i = 0; i < jsonArray.size(); i++) {
			if (JSONValue.parse(jsonArray.get(i).toString()) instanceof JSONArray)
				value = jsonArray(((JSONArray) jsonArray).get(i).toString(), matchString);
			else
				value = getJsonValue(jsonString, matchString);
		}

		return value;
	}

}