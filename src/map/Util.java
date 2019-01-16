package map;

import org.json.JSONArray;
import org.json.JSONObject;

public class Util {

	public static String lookupPlace(String query) {
		JSONArray results = Lookup.extracted(query);
		if (results.isEmpty()) return null;
		
	    JSONObject best = results.getJSONObject(0);
	    double lat = best.getDouble("lat");
	    double lon = best.getDouble("lon");
	    return lat + "\t" + lon;
	}
	
}
