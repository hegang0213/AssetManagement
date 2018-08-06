package part3rd.ortiz.touch;


import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.bdwater.assetmanagement.R;
import com.bdwater.assetmanagement.common.AppCompatSwipeBackActivity;
import com.bdwater.assetmanagement.common.IconicsHelper;
import com.bdwater.assetmanagement.common.NetworkUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.mikepenz.community_material_typeface_library.CommunityMaterial;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ViewPagerExampleActivity extends AppCompatSwipeBackActivity {

	/**
	 * Step 1: Download and set up v4 support library: http://developer.android.com/tools/support-library/setup.html
	 * Step 2: Create ExtendedViewPager wrapper which calls TouchImageView.canScrollHorizontallyFroyo
	 * Step 3: ExtendedViewPager is a custom view and must be referred to by its full package name in XML
	 * Step 4: Write TouchImageAdapter, located below
	 * Step 5. The ViewPager in the XML should be ExtendedViewPager
	 */
	public static final String PARAM_IMAGE_ARRAY = "images";
    public static final String PARAM_CURRENT = "current";

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    int current = 0;
    ArrayList<String> images;

	@Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_preview);

        ButterKnife.bind(this);

        images = getIntent().getStringArrayListExtra(PARAM_IMAGE_ARRAY);
        current = getIntent().getIntExtra(PARAM_CURRENT, 0);

        setTitle();
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ExtendedViewPager mViewPager = (ExtendedViewPager) findViewById(R.id.view_pager);
        mViewPager.setAdapter(new TouchImageAdapter(images));
        mViewPager.setCurrentItem(current);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                current = position;
                setTitle();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }
    void setTitle() {
	    String s = Integer.toString(current + 1);
	    s += " / " +  Integer.toString(images.size());
	    toolbar.setTitle(s);
    }

    static class TouchImageAdapter extends PagerAdapter {
        ArrayList<String> imageList = new ArrayList<>();

        public TouchImageAdapter(ArrayList<String> images) {
            this.imageList = images;
        }

        @Override
        public int getCount() {
        	return imageList.size();
        }

        @Override
        public View instantiateItem(ViewGroup container, int position) {
            TouchImageView img = new TouchImageView(container.getContext());
            img.setBackgroundColor(Color.BLACK);
            RequestOptions requestOptions = new RequestOptions();
            requestOptions.placeholder(IconicsHelper.getBigIcon(container.getContext(), CommunityMaterial.Icon.cmd_image));
            Glide.with(container.getContext())
                    .setDefaultRequestOptions(requestOptions)
                    .load(NetworkUtils.IMAGE_URL + imageList.get(position))
                    .into(img);
            container.addView(img, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
            return img;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

    }
}
