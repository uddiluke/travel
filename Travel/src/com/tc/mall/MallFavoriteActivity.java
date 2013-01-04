// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

package com.tc.mall;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.tc.TCUtil;
import com.tc.cg.CGUtil;

// Referenced classes of package com.tc.mall:
//            MallData, MallPOIActivity

public class MallFavoriteActivity extends Activity implements android.view.View.OnClickListener {
	private class FilterAdapter extends BaseAdapter {

		public int getCount() {
			return siteTypes.size();
		}

		public Object getItem(int i) {
			return siteTypes.get(i);
		}

		public long getItemId(int i) {
			return (long) i;
		}

		public View getView(int i, View view, ViewGroup viewgroup) {
			if (view == null)
				view = TCUtil.getLayoutInflater(context).inflate(0x7f030008, null);
			SiteType sitetype = (SiteType) getItem(i);
			ImageView imageview = (ImageView) view.findViewById(0x7f0a0044);
			TextView textview = (TextView) view.findViewById(0x7f0a0045);
			String s1 = sitetype.cnName;
			int j = 0x7f020025;
			if (sitetype.checked)
				j = 0x7f020024;
			imageview.setImageResource(j);
			textview.setText(s1);
			return view;
		}

		private Context context;

		public FilterAdapter(Context context1) {

			context = context1;
		}
	}

	private class POIAdapter extends BaseAdapter {

		private Bitmap getBitmap(Context context1, MallData.MallPOIData.POI poi) {
			return TCUtil.getRoundedCornerBitmap(TCUtil.getBitmap(context1, poi.listIcon));
		}

		public int getCount() {
			return mallPOIData.size();
		}

		public Object getItem(int i) {
			return mallPOIData.get(i);
		}

		public long getItemId(int i) {
			return (long) i;
		}

		public View getView(int i, View view, ViewGroup viewgroup) {
			if (view == null)
				view = TCUtil.getLayoutInflater(context).inflate(0x7f030050, null);
			MallData.MallPOIData.POI poi = (MallData.MallPOIData.POI) poiAdapter.getItem(i);
			((ImageView) view.findViewById(0x7f0a005e)).setImageBitmap(poiAdapter.getBitmap(context, poi));
			((TextView) view.findViewById(0x7f0a0001)).setText(poi.name);
			((TextView) view.findViewById(0x7f0a01d3)).setText((new StringBuilder()).append(MallData.getFloorName(poi.floorIndex))
					.append(getString(0x7f080032)).append(poi.number).toString());
			((RatingBar) view.findViewById(0x7f0a0061)).setRating(poi.commentGrade / 2.0F);
			ImageView imageview = (ImageView) view.findViewById(0x7f0a0064);
			if (poi.isFavorite())
				imageview.setVisibility(0);
			else
				imageview.setVisibility(4);
			return view;
		}

		private Context context;

		public POIAdapter(Context context1) {

			context = context1;
		}
	}

	private class SiteType {

		boolean checked;
		String cnName;
		String siteType;

		public SiteType(String s1, boolean flag) {

			checked = flag;
			String as[] = s1.split(":");
			if (as.length > 1) {
				cnName = as[0];
				siteType = as[1];
			} else {
				cnName = s1;
			}
		}
	}

	private class SortAdapter extends BaseAdapter {

		public int getCount() {
			return MallData.MALL_FAVORITE_DATA.sortTypes.size();
		}

		public Object getItem(int i) {
			return MallData.MALL_FAVORITE_DATA.sortTypes.get(i);
		}

		public long getItemId(int i) {
			return (long) i;
		}

		public View getView(int i, View view, ViewGroup viewgroup) {
			if (view == null)
				view = TCUtil.getLayoutInflater(context).inflate(0x7f030032, null);
			((Button) view.findViewById(0x7f0a00dd)).setText((CharSequence) MallData.MALL_FAVORITE_DATA.sortTypes.get(i));
			return view;
		}

		private Context context;

		public SortAdapter(Context context1) {

			context = context1;
		}
	}

	public MallFavoriteActivity() {
		sortType = 1;
		poiComparator = new Comparator() {

			public int compare(MallData.MallPOIData.POI poi, MallData.MallPOIData.POI poi1) {
				int i = 0;
				switch (sortType) {
				case 0:
					i = CGUtil.compare(poi.name, poi1.name);
					break;
				case 1:
					i = -1 * CGUtil.compare(Integer.valueOf(poi.current), Integer.valueOf(poi1.current));
					break;
				case 2:
					i = CGUtil.compare(Integer.valueOf(poi.current), Integer.valueOf(poi1.current));
					break;
				}
                return i;
			}

			public int compare(Object obj, Object obj1) {
				return compare((MallData.MallPOIData.POI) obj, (MallData.MallPOIData.POI) obj1);
			}

		};
		itemListener = new android.widget.AdapterView.OnItemClickListener() {

			public void onItemClick(AdapterView adapterview, View view, int i, long l) {
				if (adapterview.getId() == 0x7f0a01ca) { // goto _L2; else goto
															// _L1
					startSGItemActivity(i);
				} else if (adapterview.getId() == 0x7f0a00e0) {
					if (adapterview.getTag().equals("sort")) {
						sortType = i;
						filterOrSortGallery.setSelection(sortType);
						filterOrSortGallery.setUnselectedAlpha(0.2F);
						sortAdapter.notifyDataSetChanged();

					}
					if (adapterview.getTag().equals("filter")) {
						position = i;
						SiteType sitetype = (SiteType) siteTypes.get(i);
						if (i == 0) {
							for (Iterator iterator1 = siteTypes.iterator(); iterator1.hasNext();)
								((SiteType) iterator1.next()).checked = false;

							((SiteType) siteTypes.get(0)).checked = true;
						} else {
							((SiteType) siteTypes.get(0)).checked = false;
							boolean flag;
							boolean flag1;
							if (!sitetype.checked)
								flag = true;
							else
								flag = false;
							sitetype.checked = flag;
							flag1 = false;
							for (Iterator iterator = siteTypes.iterator(); iterator.hasNext();)
								flag1 |= ((SiteType) iterator.next()).checked;

							if (!flag1)
								((SiteType) siteTypes.get(0)).checked = true;
						}
						filterAdapter.notifyDataSetChanged();
					}
				}
				searchFilterSort();

			}

		};
		s = "";
		textWatcher = new TextWatcher() {

			public void afterTextChanged(Editable editable) {
				s = editable.toString();
				searchFilterSort();
			}

			public void beforeTextChanged(CharSequence charsequence, int i, int j, int k) {
			}

			public void onTextChanged(CharSequence charsequence, int i, int j, int k) {
			}

		};

		itemSelectedlistener = new android.widget.AdapterView.OnItemSelectedListener() {

			public void onItemSelected(AdapterView adapterview, View view, int i, long l) {
				if (adapterview.getTag().equals("sort")) {
					sortType = i;
					filterOrSortGallery.setSelection(sortType);
					filterOrSortGallery.setUnselectedAlpha(0.2F);
					sortAdapter.notifyDataSetChanged();
					searchFilterSort();
				}
			}

			public void onNothingSelected(AdapterView adapterview) {
			}

		};
		siteTypes = new ArrayList();
	}

	private void filterButtonTouch() {
		if ("filter".equals((String) filterOrSortGallery.getTag())) {
			filterOrSortGallery.setTag("");
			filterButton.setImageResource(0x7f020090);
		} else {
			filterOrSortGallery.setTag("filter");
			sortButton.setImageResource(0x7f020095);
			filterButton.setImageResource(0x7f020091);
		}
		galleryTouch();
	}

	private void galleryTouch() {
		String s1 = (String) filterOrSortGallery.getTag();
		if ("filter".equals(s1)) { // goto _L2; else goto _L1

			filterOrSortGallery.setAdapter(filterAdapter);
			filterOrSortGallery.setVisibility(0);
			filterOrSortGallery.setSelection(position);
			filterOrSortGallery.setUnselectedAlpha(1.0F);
		} else if ("sort".equals(s1)) {
			filterOrSortGallery.setAdapter(sortAdapter);
			filterOrSortGallery.setVisibility(0);
			filterOrSortGallery.setSelection(sortType);
			filterOrSortGallery.setUnselectedAlpha(0.2F);
		} else if (s1.length() == 0)
			filterOrSortGallery.setVisibility(8);

	}

	private void searchFilterSort() {
		mallPOIData = new ArrayList();
		Iterator iterator = srcMallPOIData.iterator();
		do {
			if (!iterator.hasNext())
				break;
			MallData.MallPOIData.POI poi = (MallData.MallPOIData.POI) iterator.next();
			if (poi.name.toLowerCase().contains(s.toLowerCase())) {
				int i = 0;
				while (i < siteTypes.size()) {
					SiteType sitetype = (SiteType) siteTypes.get(i);
					if ((((SiteType) siteTypes.get(0)).checked || sitetype.checked) && MallData.getTypeId(sitetype.cnName) == poi.typeId)
						mallPOIData.add(poi);
					i++;
				}
			}
		} while (true);
		Collections.sort(mallPOIData, poiComparator);
		poiAdapter.notifyDataSetChanged();
	}

	private void sortButtonTouch() {
		if ("sort".equals((String) filterOrSortGallery.getTag())) {
			filterOrSortGallery.setTag("");
			sortButton.setImageResource(0x7f020095);
		} else {
			filterOrSortGallery.setTag("sort");
			filterButton.setImageResource(0x7f020090);
			sortButton.setImageResource(0x7f020096);
		}
		galleryTouch();
	}

	public void onClick(View view) {
		switch (view.getId()) {
		case 2131361792:
			onBackPressed();
			break;
		case 2131361906:
			filterButtonTouch();
			break;
		case 2131362013:
			sortButtonTouch();
			break;
		}

	}

	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(0x7f030048);
		((TextView) findViewById(0x7f0a0001)).setText(MallData.MALL_FAVORITE_DATA.chName);
		findViewById(0x7f0a0000).setOnClickListener(this);
		filterButton = (ImageView) findViewById(0x7f0a0072);
		filterButton.setImageResource(0x7f020090);
		filterButton.setOnClickListener(this);
		sortButton = (ImageView) findViewById(0x7f0a00dd);
		sortButton.setImageResource(0x7f020095);
		sortButton.setOnClickListener(this);
		MallData.MallFavoriteData _tmp = MallData.MALL_FAVORITE_DATA;
		srcMallPOIData = MallData.MallFavoriteData.POIS;
		mallPOIData = new ArrayList();
		MallData.MallPOIData.POI poi;
		for (Iterator iterator = srcMallPOIData.iterator(); iterator.hasNext(); mallPOIData.add(poi))
			poi = (MallData.MallPOIData.POI) iterator.next();

		poiList = (ListView) findViewById(0x7f0a01ca);
		poiAdapter = new POIAdapter(this);
		poiList.setAdapter(poiAdapter);
		poiList.setOnItemClickListener(itemListener);
		String s1 = "";
		if (srcMallPOIData.size() > 0) {
			String s2 = ((MallData.MallPOIData.POI) srcMallPOIData.get(0)).name;
			if (s2.contains("("))
				s2 = s2.substring(0, s2.indexOf("("));
			s1 = (new StringBuilder()).append(getString(0x7f080019)).append("\"").append(s2).append("\"").toString();
		}
		filterOrSortGallery = (Gallery) findViewById(0x7f0a00e0);
		sortAdapter = new SortAdapter(this);
		siteTypes.add(new SiteType(getString(0x7f08001a), true));
		for (int i = 0; i < MallData.MALL_FAVORITE_DATA.siteTypes.size(); i++)
			siteTypes.add(new SiteType((String) MallData.MALL_FAVORITE_DATA.siteTypes.get(i), false));

		filterAdapter = new FilterAdapter(this);
		filterOrSortGallery.setOnItemClickListener(itemListener);
		filterOrSortGallery.setOnItemSelectedListener(itemSelectedlistener);
		filterOrSortGallery.setVisibility(8);
		searchEdit = (EditText) findViewById(0x7f0a00df);
		searchEdit.setHint(s1);
		searchEdit.addTextChangedListener(textWatcher);
	}

	protected void onDestroy() {
		super.onDestroy();
	}

	protected void onPause() {
		super.onPause();
	}

	protected void onResume() {
		super.onResume();
		searchFilterSort();
	}

	public void startSGItemActivity(int i) {
		MallData.MallPOIData.POI poi = (MallData.MallPOIData.POI) poiAdapter.getItem(i);
		Bundle bundle = new Bundle();
		bundle.putInt("poi_id", poi.id);
		bundle.putInt("poi_type", poi.typeId);
		TCUtil.startActivity(this, MallPOIActivity.class, bundle);
	}

	private static final String TAG = MallFavoriteActivity.class.getSimpleName();
	private FilterAdapter filterAdapter;
	private ImageView filterButton;
	private Gallery filterOrSortGallery;
	private android.widget.AdapterView.OnItemClickListener itemListener;
	private android.widget.AdapterView.OnItemSelectedListener itemSelectedlistener;
	private List mallPOIData;
	private POIAdapter poiAdapter;
	private Comparator poiComparator;
	private ListView poiList;
	private int position;
	private String s;
	private EditText searchEdit;
	private List siteTypes;
	private SortAdapter sortAdapter;
	private ImageView sortButton;
	private int sortType;
	private List srcMallPOIData;
	private TextWatcher textWatcher;

	/*
	 * static int access$002(MallFavoriteActivity mallfavoriteactivity, int i) {
	 * mallfavoriteactivity.sortType = i; return i; }
	 */

	/*
	 * static int access$602(MallFavoriteActivity mallfavoriteactivity, int i) {
	 * mallfavoriteactivity.position = i; return i; }
	 */

	/*
	 * static String access$902(MallFavoriteActivity mallfavoriteactivity,
	 * String s1) { mallfavoriteactivity.s = s1; return s1; }
	 */
}
