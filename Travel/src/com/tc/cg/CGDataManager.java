//sample codes for lukeuddi(uddi.luke@gmail.com)



package com.tc.cg;

import java.util.ArrayList;
import java.util.List;

// Referenced classes of package com.tc.cg:
//            CGData

public class CGDataManager {

	public CGDataManager() {
	}

	public static void addCGData(CGData cgdata) {
		CGDatas.add(cgdata);
	}

	public static CGData getCGData(int i) {
		CGData cgdata = null;
		for (int j = 0; j < CGDatas.size(); j++)
			if (i == ((CGData) CGDatas.get(j)).CG_ID)
				cgdata = (CGData) CGDatas.get(j);

		return cgdata;
	}

	public static void removeCGData(CGData cgdata) {
		int i = 0;
		do {
			label0: {
				if (i < CGDatas.size()) {
					if (cgdata.CG_ID != ((CGData) CGDatas.get(i)).CG_ID)
						break label0;
					CGDatas.remove(i);
				}
				return;
			}
			i++;
		} while (true);
	}

	private static List CGDatas = new ArrayList();
	private static final String TAG = CGDataManager.class.getSimpleName();

}
