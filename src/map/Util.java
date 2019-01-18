package map;

import org.json.JSONArray;
import org.json.JSONObject;

public class Util {

	public static Location lookupPlace(String query) {
		JSONArray results = Lookup.queryURL(query);
		if (results.isEmpty()) return null;
		
	    JSONObject best = results.getJSONObject(0);
	    double lat = best.getDouble("lat");
	    double lon = best.getDouble("lon");
	    return new Location(lat, lon);
	}	
}
