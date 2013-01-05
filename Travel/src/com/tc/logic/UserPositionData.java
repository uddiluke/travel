//sample codes for lukeuddi(uddi.luke@gmail.com)



package com.tc.logic;

import java.util.HashMap;

import android.content.Context;
import android.location.Location;

import com.flurry.android.FlurryAgent;

public class UserPositionData {

	public UserPositionData() {
	}

	public static void userPosition(Context context, String s, Location location) {
		if (location != null) {
			HashMap hashmap = new HashMap();
			hashmap.put("lon", (new StringBuilder()).append(location.getLongitude()).append("").toString());
			hashmap.put("lat", (new StringBuilder()).append(location.getLatitude()).append("").toString());
			FlurryAgent.logEvent("session_location", hashmap);
		}
	}
}
