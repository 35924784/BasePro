package www.andysong.com.basepro;

import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.widget.Toast;

import com.blankj.utilcode.util.LogUtils;
import com.huantansheng.easyphotos.EasyPhotos;
import com.luseen.spacenavigation.SpaceItem;
import com.luseen.spacenavigation.SpaceNavigationView;
import com.luseen.spacenavigation.SpaceOnClickListener;



import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import io.reactivex.Observable;
import www.andysong.com.basepro.core.base.BaseActivity;
import www.andysong.com.basepro.core.base.BaseFragment;
import www.andysong.com.basepro.example.MainPagerFragmentAdapter;

import www.andysong.com.basepro.utils.GlideEngine;
import www.andysong.com.basepro.utils.PermissionHelper;

public class MainActivity extends BaseActivity {

    @BindView(R.id.viewPager)
    ViewPager viewPager;
    @BindView(R.id.bottomBar)
    SpaceNavigationView bottomBar;



    private BaseFragment[] mFragments = new BaseFragment[4];

    int count = 1;
    int mTabIndex;

    private static final int REQUEST_CODE_CHOOSE = 23;

    @Override
    protected int getLayout() {
        return R.layout.fragment_ex_main;
    }

    @Override
    protected void initEventAndData(Bundle savedInstanceState) {
        initPermission();



        bottomBar.initWithSaveInstanceState(savedInstanceState);
        bottomBar.addSpaceItem(new SpaceItem("HOME", R.drawable.ic_home));
        bottomBar.addSpaceItem(new SpaceItem("SEARCH", R.drawable.ic_search));
        bottomBar.addSpaceItem(new SpaceItem("FAVORITE", R.drawable.ic_favorite));
        bottomBar.addSpaceItem(new SpaceItem("PERSON", R.drawable.ic_person));
        bottomBar.showIconOnly();
        bottomBar.setCentreButtonIconColorFilterEnabled(false);
        bottomBar.setSpaceOnClickListener(new SpaceOnClickListener() {
            @Override
            public void onCentreButtonClick() {
                startWithMatisse();
            }

            @Override
            public void onItemClick(int itemIndex, String itemName) {
                mTabIndex = itemIndex;
                LogUtils.e("onItemClick>>> itemIndex" + itemIndex + "----itemName" + itemName);
                viewPager.setCurrentItem(itemIndex);
            }

            @Override
            public void onItemReselected(int itemIndex, String itemName) {
                mTabIndex = itemIndex;
            }
        });


        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                bottomBar.changeCurrentItem(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        viewPager.setOffscreenPageLimit(5);
        viewPager.setAdapter(new MainPagerFragmentAdapter(getSupportFragmentManager(), mFragments));
    }

    private void startWithMatisse() {
        EasyPhotos.createAlbum(this, true, GlideEngine.getInstance())//参数说明：上下文，是否显示相机按钮，[配置Glide为图片加载引擎](https://github.com/HuanTanSheng/EasyPhotos/wiki/12-%E9%85%8D%E7%BD%AEImageEngine%EF%BC%8C%E6%94%AF%E6%8C%81%E6%89%80%E6%9C%89%E5%9B%BE%E7%89%87%E5%8A%A0%E8%BD%BD%E5%BA%93)
                .setFileProviderAuthority("www.andysong.com.basepro.fileprovider")//参数说明：见下方`FileProvider的配置`
                .start(101);
    }

    private void initPermission() {
        PermissionHelper.requestStorage(null);
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        bottomBar.onSaveInstanceState(outState);
    }

    @Override
    public void onBackPressedSupport() {
        if (mTabIndex!=0)
        {
            bottomBar.changeCurrentItem(0);
        }else {
            if (count != 2) {
                Toast.makeText(this, "再按一次退出应用", Toast.LENGTH_SHORT).show();
            } else {
                ActivityCompat.finishAfterTransition(this);
            }
            count = 2;
            Observable.timer(2, TimeUnit.SECONDS)
                    .compose(bindToLifecycle())
                    .subscribe(aLong -> count = 1);
        }

    }
}
