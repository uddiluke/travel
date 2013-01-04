// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

package com.tc.cg;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.flurry.android.FlurryAgent;
import com.tc.TCData;
import com.tc.TCUtil;
import com.touchchina.cityguide.sz.R;

// Referenced classes of package com.tc.cg:
//            CGBaseActivity, CGData, CGSiteItemActivity, CGUtil

public class CGSiteActivity extends CGBaseActivity implements android.view.View.OnClickListener {
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

	private class RegionAdapter extends BaseExpandableListAdapter {

		public Object getChild(int i, int j) {
			return ((Child) children.get(i)).items.get(j);
		}

		public long getChildId(int i, int j) {
			return (long) j;
		}

		public View getChildView(int i, int j, boolean flag, View view, ViewGroup viewgroup) {
			return getView(context, i, j, view, viewgroup);
		}

		public int getChildrenCount(int i) {
			return ((Child) children.get(i)).items.size();
		}

		public Object getGroup(int i) {
			return ((Child) children.get(i)).region;
		}

		public int getGroupCount() {
			return children.size();
		}

		public long getGroupId(int i) {
			return (long) i;
		}

		public View getGroupView(int i, boolean flag, View view, ViewGroup viewgroup) {
			TextView textview = (TextView) view;
			if (textview == null) {
				android.widget.AbsListView.LayoutParams layoutparams = new android.widget.AbsListView.LayoutParams(-1, -2);
				textview = new TextView(context);
				textview.setBackgroundResource(0x7f020052);
				textview.setLayoutParams(layoutparams);
				textview.setTextColor(getResources().getColor(0x7f06000b));
				textview.setTextSize(19F);
				textview.setShadowLayer(1.0F, 0.0F, 1.0F, 0xff000000);
				textview.setGravity(19);
				if (TCData.USE_2X)
					textview.setPadding(28, 0, 0, 0);
				else
					textview.setPadding(20, 0, 0, 0);
			}
			textview.setText(getGroup(i).toString());
			return textview;
		}

		public boolean hasStableIds() {
			return true;
		}

		public boolean isChildSelectable(int i, int j) {
			return true;
		}

		public void setCGSGData(CGData.CGSGData cgsgdata) {
			children = new ArrayList();
			String s1 = "";
			CGData.CGSGData.SGItem sgitem;
			for (Iterator iterator = cgsgdata.sgItems.iterator(); iterator.hasNext(); ) {
				sgitem = (CGData.CGSGData.SGItem) iterator.next();
				if (!sgitem.region.equals(s1)) {
					s1 = sgitem.region;
					child = new Child();
					child.region = sgitem.region;
					children.add(child);
				}
				child.items.add(sgitem);
			}

		}

		private Child child;
		private List children;
		private Context context;

		public RegionAdapter(Context context1) {
			context = context1;
		}

		private class Child {

			public List items;
			public String region;

			private Child() {

				items = new ArrayList();
			}

		}

	}

	private class SiteAdapter extends BaseAdapter {

		private Bitmap getBitmap(Context context1, CGData.CGSGData.SGItem sgitem) {
			return TCUtil.getRoundedCornerBitmap(TCUtil.getBitmap(context1, sgitem.icon));
		}

		public int getCount() {
			return cgSGData.sgItems.size();
		}

		public Object getItem(int i) {
			return cgSGData.sgItems.get(i);
		}

		public long getItemId(int i) {
			return (long) i;
		}

		public View getView(int i, View view, ViewGroup viewgroup) {
			return CGSiteActivity.this.getView(context, 0, i, view, viewgroup);
		}

		private Context context;

		public SiteAdapter(Context context1) {

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
			return cgSGData.sortTypes.size();
		}

		public Object getItem(int i) {
			return cgSGData.sortTypes.get(i);
		}

		public long getItemId(int i) {
			return (long) i;
		}

		public View getView(int i, View view, ViewGroup viewgroup) {
			if (view == null)
				view = TCUtil.getLayoutInflater(context).inflate(0x7f030032, null);
			TextView textview = (TextView) view.findViewById(0x7f0a00dd);
			textview.setText((CharSequence) cgSGData.sortTypes.get(i));
			if (i == sortType) {
				textview.setTextColor(-1);
				textview.setBackgroundResource(0x7f020139);
			} else {
				textview.setTextColor(0xff000000);
				textview.setBackgroundResource(0x7f02013c);
			}
			return view;
		}

		private Context context;

		public SortAdapter(Context context1) {

			context = context1;
		}
	}

	public CGSiteActivity() {
		sortType = 1;
		sgItemComparator = new Comparator() {

			public int compare(CGData.CGSGData.SGItem sgitem, CGData.CGSGData.SGItem sgitem1) {
				int i = 0;
				if (isFavorite) {
					switch (sortType) {
					case 0:
						i = CGUtil.compare(sgitem.name, sgitem1.name);
						FlurryAgent.logEvent("sortNameInFavorList");
						break;
					case 1:
						FlurryAgent.logEvent("sortFromNewToOldInFavorList");
						i = -1 * CGUtil.compare(Integer.valueOf(sgitem.current), Integer.valueOf(sgitem1.current));
						break;
					case 2:
						FlurryAgent.logEvent("sortFromOldToNewInFavorList");
						i = CGUtil.compare(Integer.valueOf(sgitem.current), Integer.valueOf(sgitem1.current));
						break;
					case 3:
						i = CGUtil.compare(Double.valueOf(sgitem.distance), Double.valueOf(sgitem1.distance));
						FlurryAgent.logEvent("sortDistanceInFavorList");
						break;
					case 4:
						i = CGUtil.compare(sgitem.region, sgitem1.region);
						FlurryAgent.logEvent("sortRegionInFavorList");
						break;
					}

				} else {
					switch (sortType) {
					case 0: // '\0'
						i = CGUtil.compare(sgitem.name, sgitem1.name);
						FlurryAgent.logEvent((new StringBuilder()).append("sortNameIn").append(cgSGData.type).append("List").toString());
						break;

					case 1: // '\001'
						i = -1 * CGUtil.compare(Float.valueOf(sgitem.commentGrade), Float.valueOf(sgitem1.commentGrade));
						FlurryAgent.logEvent((new StringBuilder()).append("sortFavorIn").append(cgSGData.type).append("List").toString());
						break;

					case 2: // '\002'
						i = CGUtil.compare(Double.valueOf(sgitem.distance), Double.valueOf(sgitem1.distance));
						FlurryAgent.logEvent((new StringBuilder()).append("sortDistanceIn").append(cgSGData.type).append("List").toString());
						break;

					case 3: // '\003'
						i = CGUtil.compare(sgitem.region, sgitem1.region);
						FlurryAgent.logEvent((new StringBuilder()).append("sortRegionIn").append(cgSGData.type).append("List").toString());
						break;

					case 4: // '\004'
						i = CGUtil.compare(Float.valueOf(sgitem.price), Float.valueOf(sgitem1.price));
						FlurryAgent.logEvent((new StringBuilder()).append("sortPriceASCIn").append(cgSGData.type).append("List").toString());
						break;

					case 5: // '\005'
						i = -1 * CGUtil.compare(Float.valueOf(sgitem.price), Float.valueOf(sgitem1.price));
						FlurryAgent.logEvent((new StringBuilder()).append("sortPriceDESIn").append(cgSGData.type).append("List").toString());
						break;
					}
				}
				return i;

			}

			public int compare(Object obj, Object obj1) {
				return compare((CGData.CGSGData.SGItem) obj, (CGData.CGSGData.SGItem) obj1);
			}

		};
		itemSelectListener = new android.widget.AdapterView.OnItemSelectedListener() {

			public void onItemSelected(AdapterView adapterview, View view, int i, long l) {
				if (adapterview.getId() == 0x7f0a00e0 && adapterview.getTag().equals("sort")) {
					sortType = i;
					sortAdapter.notifyDataSetChanged();
					searchFilterSort();
				}
			}

			public void onNothingSelected(AdapterView adapterview) {
			}

		};
		position = 0;
		itemListener = new android.widget.AdapterView.OnItemClickListener() {

			public void onItemClick(AdapterView adapterview, View view, int i, long l) {
				if (adapterview.getId() == 0x7f0a00e1) // goto _L2; else goto
														// _L1
				{
					TCUtil.flurryLog((new StringBuilder()).append("EnterDetailIn").append(((CGData.CGSGData.SGItem) cgSGData.sgItems.get(i)).type)
							.append("List").toString(), ((CGData.CGSGData.SGItem) cgSGData.sgItems.get(i)).name);
					startSGItemActivity(i);
				} else if (adapterview.getId() == 0x7f0a00e0) {
					if (adapterview.getTag().equals("sort")) {
						sortType = i;
						sortAdapter.notifyDataSetChanged();

					} else if (adapterview.getTag().equals("filter")) {
						position = i;
						SiteType sitetype = (SiteType) siteTypes.get(i);
						TCUtil.flurryLog((new StringBuilder()).append("filterIn").append(cgSGData.type).append("List").toString(), sitetype.cnName);
						if (i == 0) {
							for (Iterator iterator1 = siteTypes.iterator(); iterator1.hasNext();)
								((SiteType) iterator1.next()).checked = false;

							((SiteType) siteTypes.get(0)).checked = true;
							filterAdapter.notifyDataSetChanged();
						} else {
							((SiteType) siteTypes.get(0)).checked = true;
							sitetype.checked = !sitetype.checked;
							boolean flag = true;
							for (Iterator iterator = siteTypes.iterator(); iterator.hasNext();)
								flag &= ((SiteType) iterator.next()).checked;
							if (!flag)
								((SiteType) siteTypes.get(0)).checked = false;

						}
					}
				}
				searchFilterSort();

			}

		};
		s = "";
		locationListener = new LocationListener() {

			public void onLocationChanged(Location location) {
				updateLocation(location);
			}

			public void onProviderDisabled(String s1) {
			}

			public void onProviderEnabled(String s1) {
			}

			public void onStatusChanged(String s1, int i, Bundle bundle) {
			}

		};
		textWatcher = new TextWatcher() {

			public void afterTextChanged(Editable editable) {
				FlurryAgent.logEvent((new StringBuilder()).append("searchIn").append(cgSGData.type).append("List").toString());
				s = editable.toString();
				searchFilterSort();
			}

			public void beforeTextChanged(CharSequence charsequence, int i, int j, int k) {
			}

			public void onTextChanged(CharSequence charsequence, int i, int j, int k) {
			}

		};
		childListener = new android.widget.ExpandableListView.OnChildClickListener() {

			public boolean onChildClick(ExpandableListView expandablelistview, View view, int i, int j, long l) {
				CGData.CGSGData.SGItem sgitem = (CGData.CGSGData.SGItem) regionAdapter.getChild(i, j);
				Bundle bundle = new Bundle();
				bundle.putInt("sg_id", sgitem.id);
				bundle.putString("sg_type", sgitem.type);
				startActivity(CGSiteActivity.this, CGSiteItemActivity.class, bundle);
				return true;
			}

		};
		siteTypes = new ArrayList<SiteType>();
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
		if ("filter".equals(s1)) {

			filterOrSortGallery.setAdapter(filterAdapter);
			filterOrSortGallery.setVisibility(0);
			filterOrSortGallery.setSelection(position);
			filterOrSortGallery.setUnselectedAlpha(1.0F);
		} else if ("sort".equals(s1)) {
			filterOrSortGallery.setAdapter(sortAdapter);
			filterOrSortGallery.setVisibility(0);
			filterOrSortGallery.setSelection(sortType);
		} else if (s1.length() == 0)
			filterOrSortGallery.setVisibility(8);

	}

	private void removeLoactionLinistener() {
		if (locationManager != null)
			locationManager.removeUpdates(locationListener);
	}

	private void requestLoactionLinistener() {
		if (locationManager != null) {
			String s1 = TCUtil.getBestProvider(locationManager);
			locationManager.requestLocationUpdates(s1, 1000L, 0.0F, locationListener);
		}
	}

	private void searchFilterSort() {
		if (srcCGSGData.sgItems.size() > 0) // goto _L2; else goto _L1
		{

			cgSGData.sgItems = new ArrayList<CGData.CGSGData.SGItem>();
			if (isFavorite) {
				for (CGData.CGSGData.SGItem sgitem1 : srcCGSGData.sgItems) {
					if (sgitem1.current > 0 || sgitem1.name.toLowerCase().contains(s.toLowerCase())) {
						for (SiteType siteType : siteTypes) {
							if (((SiteType) siteTypes.get(0)).checked || (siteType.checked && sgitem1.type.equals(siteType.siteType))) {
								cgSGData.sgItems.add(sgitem1);
								break;
							}
						}
					}
				}

			} else {
				for (CGData.CGSGData.SGItem sgitem1 : srcCGSGData.sgItems) {
					if (sgitem1.current > 0 || sgitem1.name.toLowerCase().contains(s.toLowerCase())) {
						for (SiteType siteType : siteTypes) {
							if (((SiteType) siteTypes.get(0)).checked || (siteType.checked && sgitem1.siteType.contains(siteType.cnName))) {
								cgSGData.sgItems.add(sgitem1);
								break;
							}
						}
					}
				}
			}

			Collections.sort(cgSGData.sgItems, sgItemComparator);
			if (isFavorite) {
				if (sortType == 4) {
					siteList.setVisibility(8);
					regionAdapter.setCGSGData(cgSGData);
					for (int j = 0; j < regionAdapter.getGroupCount(); j++)
						siteExpandableList.expandGroup(j);

					regionAdapter.notifyDataSetChanged();
					siteExpandableList.setVisibility(0);
				} else {
					siteExpandableList.setVisibility(8);
					siteAdapter.notifyDataSetChanged();
					siteList.setVisibility(0);
				}
			} else if (sortType == 3) {
				siteList.setVisibility(8);
				regionAdapter.setCGSGData(cgSGData);
				for (int i = 0; i < regionAdapter.getGroupCount(); i++)
					siteExpandableList.expandGroup(i);

				regionAdapter.notifyDataSetChanged();
				siteExpandableList.setVisibility(0);
			} else {
				siteExpandableList.setVisibility(8);
				siteAdapter.notifyDataSetChanged();
				siteList.setVisibility(0);
			}

		}

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

	public View getView(Context context, int i, int j, View view, ViewGroup viewgroup) {
		if (view == null)
			view = TCUtil.getLayoutInflater(context).inflate(0x7f03000c, null);
		CGData.CGSGData.SGItem sgitem;
		TextView textview;
		String s1;
		TextView textview1;
		String s2;
		RatingBar ratingbar;
		ImageView imageview;
		ImageView imageview1;
		TextView textview2;
		if (i == 0)
			sgitem = (CGData.CGSGData.SGItem) siteAdapter.getItem(j);
		else
			sgitem = (CGData.CGSGData.SGItem) regionAdapter.getChild(i, j);
		((ImageView) view.findViewById(0x7f0a005e)).setImageBitmap(siteAdapter.getBitmap(context, sgitem));
		textview = (TextView) view.findViewById(0x7f0a0001);
		s1 = sgitem.name;
		if (s1.length() > 8)
			s1 = (new StringBuilder()).append(s1.substring(0, 8)).append("...").toString();
		textview.setText(s1);
		textview1 = (TextView) view.findViewById(0x7f0a0060);
		s2 = sgitem.siteType;
		if (s2.length() > 10)
			s2 = (new StringBuilder()).append(s2.substring(0, 10)).append("...").toString();
		textview1.setText(s2);
		ratingbar = (RatingBar) view.findViewById(0x7f0a005f);
		if (sgitem.price > 0.0F) {
			ratingbar.setVisibility(0);
			ratingbar.setRating(sgitem.price / 2.0F);
		} else {
			ratingbar.setVisibility(8);
		}
		((RatingBar) view.findViewById(0x7f0a0061)).setRating(sgitem.commentGrade / 2.0F);
		imageview = (ImageView) view.findViewById(0x7f0a0063);
		if (sgitem.isMain)
			imageview.setVisibility(0);
		else
			imageview.setVisibility(4);
		imageview1 = (ImageView) view.findViewById(0x7f0a0064);
		if (sgitem.isFavorite())
			imageview1.setVisibility(0);
		else
			imageview1.setVisibility(8);
		textview2 = (TextView) view.findViewById(0x7f0a0062);
		if (sgitem.distance > 0.0D) {
			textview2.setVisibility(0);
			textview2.setText(TCUtil.getDistance(sgitem.distance));
		} else {
			textview2.setVisibility(0);
		}
		return view;
	}

	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.backButton:
			TCUtil.hideSoftKeyBroad(this, searchEdit);
			onBackPressed();
			break;
		case R.id.filterButton:
			FlurryAgent.logEvent((new StringBuilder()).append("showFilterIn").append(cgSGData.type).append("List").toString());
			filterButtonTouch();
			break;
		case R.id.sortButton:
			FlurryAgent.logEvent((new StringBuilder()).append("showSortIn").append(cgSGData.type).append("List").toString());
			sortButtonTouch();
			break;
		}

	}

	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(0x7f030030);
		locationManager = (LocationManager) getSystemService("location");
		String s1 = getIntent().getExtras().getString(KEY_CG_TYPE);
		isFavorite = "Favorite".equals(s1);
		srcCGSGData = CGData.getCGSGData(CG_DATA, s1);
		cgSGData = new CGData.CGSGData(srcCGSGData);
		((TextView) findViewById(0x7f0a0001)).setText(cgSGData.chName);
		findViewById(0x7f0a0000).setOnClickListener(this);
		filterButton = (ImageView) findViewById(0x7f0a0072);
		filterButton.setImageResource(0x7f020090);
		filterButton.setTag("");
		filterButton.setOnClickListener(this);
		sortButton = (ImageView) findViewById(0x7f0a00dd);
		sortButton.setOnClickListener(this);
		siteList = (ListView) findViewById(0x7f0a00e1);
		siteAdapter = new SiteAdapter(this);
		siteList.setAdapter(siteAdapter);
		siteList.setOnItemClickListener(itemListener);
		String s2 = "";
		if (cgSGData.sgItems.size() > 0) {
			String s3 = ((CGData.CGSGData.SGItem) cgSGData.sgItems.get(0)).name;
			if (s3.contains("("))
				s3 = s3.substring(0, s3.indexOf("("));
			if (s3.contains(" "))
				s3 = s3.substring(0, s3.indexOf(" "));
			s2 = (new StringBuilder()).append(getString(0x7f080019)).append("\"").append(s3).append("\"").toString();
		}
		siteExpandableList = (ExpandableListView) findViewById(0x7f0a00e2);
		siteExpandableList.setGroupIndicator(null);
		siteExpandableList.setOnGroupClickListener(new android.widget.ExpandableListView.OnGroupClickListener() {

			public boolean onGroupClick(ExpandableListView expandablelistview, View view, int j, long l) {
				if (!expandablelistview.isGroupExpanded(j))
					expandablelistview.expandGroup(j);
				return true;
			}

		});
		siteExpandableList.setVisibility(8);
		regionAdapter = new RegionAdapter(this);
		regionAdapter.setCGSGData(cgSGData);
		siteExpandableList.setAdapter(regionAdapter);
		siteExpandableList.setOnChildClickListener(childListener);
		filterOrSortGallery = (Gallery) findViewById(0x7f0a00e0);
		sortAdapter = new SortAdapter(this);
		siteTypes.add(new SiteType(getString(0x7f08001a), true));
		for (int i = 0; i < cgSGData.siteTypes.size(); i++)
			siteTypes.add(new SiteType((String) cgSGData.siteTypes.get(i), false));

		filterAdapter = new FilterAdapter(this);
		filterOrSortGallery.setOnItemClickListener(itemListener);
		filterOrSortGallery.setOnItemSelectedListener(itemSelectListener);
		filterOrSortGallery.setVisibility(8);
		searchEdit = (EditText) findViewById(0x7f0a00df);
		searchEdit.setHint(s2);
		searchEdit.addTextChangedListener(textWatcher);
		getIntent().getExtras();
	}

	protected void onDestroy() {
		super.onDestroy();
	}

	protected void onPause() {
		removeLoactionLinistener();
		super.onPause();
	}

	protected void onResume() {
		super.onResume();
		requestLoactionLinistener();
		searchFilterSort();
	}

	public void startSGItemActivity(int i) {
		CGData.CGSGData.SGItem sgitem = (CGData.CGSGData.SGItem) siteAdapter.getItem(i);
		Bundle bundle = new Bundle();
		bundle.putInt("sg_id", sgitem.id);
		bundle.putString("sg_type", sgitem.type);
		startActivity(this, CGSiteItemActivity.class, bundle);
	}

	protected void updateLocation(Location location) {
//		if (location != null && srcCGSGData.sgItems.size() > 0) {
//			for (Iterator iterator = srcCGSGData.sgItems.iterator(); iterator.hasNext();) {
//				CGData.CGSGData.SGItem sgitem = (CGData.CGSGData.SGItem) iterator.next();
//				sgitem.distance = TCUtil.getDistance(location.getLatitude(), location.getLongitude(), sgitem.lat, sgitem.lon);
//			}
//
//			searchFilterSort();
//		}
	}

	public static String KEY_CG_TYPE = "cg_type";
	public static String KEY_SG_ID = "sg_id";
	private static final String TAG = CGSiteActivity.class.getSimpleName();
	private CGData.CGSGData cgSGData;
	private android.widget.ExpandableListView.OnChildClickListener childListener;
	private FilterAdapter filterAdapter;
	private ImageView filterButton;
	private Gallery filterOrSortGallery;
	private boolean isFavorite;
	private android.widget.AdapterView.OnItemClickListener itemListener;
	private android.widget.AdapterView.OnItemSelectedListener itemSelectListener;
	private LocationListener locationListener;
	private LocationManager locationManager;
	private int position;
	private RegionAdapter regionAdapter;
	private String s;
	private EditText searchEdit;
	private Comparator sgItemComparator;
	private SiteAdapter siteAdapter;
	private ExpandableListView siteExpandableList;
	private ListView siteList;
	private List<SiteType> siteTypes;
	private SortAdapter sortAdapter;
	private ImageView sortButton;
	private int sortType;
	private CGData.CGSGData srcCGSGData;
	private TextWatcher textWatcher;

	/*
	 * static String access$1002(CGSiteActivity cgsiteactivity, String s1) {
	 * cgsiteactivity.s = s1; return s1; }
	 */

	/*
	 * static int access$102(CGSiteActivity cgsiteactivity, int i) {
	 * cgsiteactivity.sortType = i; return i; }
	 */

	/*
	 * static int access$802(CGSiteActivity cgsiteactivity, int i) {
	 * cgsiteactivity.position = i; return i; }
	 */

}
