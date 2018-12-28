package cc.yelinvan.photographhome.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jaeger.library.StatusBarUtil;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshFooter;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.xutils.common.Callback;
import org.xutils.x;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import cc.yelinvan.photographhome.Constant;
import cc.yelinvan.photographhome.R;
import cc.yelinvan.photographhome.activity.base.BaseActivity;
import cc.yelinvan.photographhome.adapter.BaseRecyclerAdapter;
import cc.yelinvan.photographhome.adapter.SmartViewHolder;
import cc.yelinvan.photographhome.bean.ProjectBean;
import cc.yelinvan.photographhome.bean.ResponseBean;
import cc.yelinvan.photographhome.utils.NetParams;
import cc.yelinvan.photographhome.utils.ToastUtil;

//import static android.R.layout.simple_list_item_2;

/**
 * 相册列表activity
 * Create by Johnson on 2018年12月26日17:05:06
 */
public class ProjectListActivity extends BaseActivity {
    private BaseRecyclerAdapter<ProjectBean> mAdapter;
    @BindView(R.id.listView)
    AbsListView listView;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    private List<ProjectBean> projectList;  //相册列表

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getProjectList();
        uiInit();
        refreshLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    @Override
    protected void init() {
        setContentView(R.layout.activity_project);
        //设置状态栏颜色
        StatusBarUtil.setColor(this, getResources().getColor(R.color.colorAccent), 0);

    }

    /**
     * UI组件初始化
     */
    private void uiInit() {
        listView.setAdapter(mAdapter = new BaseRecyclerAdapter<ProjectBean>(R.layout.item_project) {
            @Override
            protected void onBindViewHolder(SmartViewHolder holder, ProjectBean model, int position) {
                holder.text(R.id.tv_projectname, model.getProject_name());
                holder.text(R.id.tv_desc, model.getDesc());
                holder.text(R.id.tv_starttime,"开始时间：" + model.getStart_time());
                holder.text(R.id.tv_endtime, "结束时间：" + model.getEnd_time());
                holder.setOnclick(R.id.ib_delete , position, new SmartViewHolder.OnMmoduleClickListener.OnClickListener(){

                    @Override
                    public void onMmoduleClick(SmartViewHolder holder, int position) {

                    }
                });

            }
        });

        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            int SCROLL_STATE_IDLE = 0;
            int SCROLL_STATE_TOUCH_SCROLL = 1;
            int SCROLL_STATE_FLING = 2;

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == SCROLL_STATE_IDLE) {
                    System.out.println("SCROLL_STATE_IDLE");
                } else if (scrollState == SCROLL_STATE_TOUCH_SCROLL) {
                    System.out.println("SCROLL_STATE_TOUCH_SCROLL");
                } else if (scrollState == SCROLL_STATE_FLING) {
                    System.out.println("SCROLL_STATE_FLING");
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });

        refreshLayout.setEnableAutoLoadMore(true);//开启自动加载功能（非必须）
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull final RefreshLayout refreshLayout) {
                ProjectListActivity.this.getProjectList();
                /*refreshLayout.getLayout().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ProjectListActivity.this.getProjectList();
//                        mAdapter.refresh(initData());
//                        refreshLayout.finishRefresh();
//                        refreshLayout.resetNoMoreData();//setNoMoreData(false);
                    }
                }, 0);*/
            }
        });


        //触发自动刷新
        refreshLayout.autoRefresh();
        //item 点击测试
        mAdapter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                BottomSheetDialog dialog = new BottomSheetDialog(ProjectListActivity.this);
                View dialogView = View.inflate(getBaseContext(), R.layout.activity_project, null);
                RefreshLayout refreshLayout = dialogView.findViewById(R.id.refreshLayout);
                RecyclerView recyclerView = new RecyclerView(getBaseContext());
                recyclerView.setLayoutManager(new LinearLayoutManager(getBaseContext()));
                recyclerView.setAdapter(mAdapter);
                refreshLayout.setEnableRefresh(false);
                refreshLayout.setEnableNestedScroll(false);
                refreshLayout.setRefreshContent(recyclerView);
                dialog.setContentView(dialogView);
                dialog.show();
            }
        });

        //点击测试
        RefreshFooter footer = refreshLayout.getRefreshFooter();
        if (footer != null) {
            refreshLayout.getRefreshFooter().getView().findViewById(ClassicsFooter.ID_TEXT_TITLE).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getBaseContext(), "点击测试", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    protected List<ProjectBean> getProjectList() {
        NetParams netParams = new NetParams(this, Constant.Url.GETPROJECT, 5000);
        x.http().post(netParams, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                if (!result.isEmpty()) {
                    ResponseBean<List<ProjectBean>> ResponseBean = new Gson().fromJson(result,
                            new TypeToken<ResponseBean<List<ProjectBean>>>() {
                            }.getType());
                    if (ResponseBean.isCode()) {
                        projectList = ResponseBean.getData();
                        mAdapter.refresh(projectList);
                        refreshLayout.finishRefresh();
                        refreshLayout.resetNoMoreData();//setNoMoreData(false);
                        refreshLayout.finishLoadMoreWithNoMoreData();//将不会再次触发加载更多事件
                    } else {
                        ToastUtil.showLong(ProjectListActivity.this,"数据请求失败！");
                    }

                }else {
                    ToastUtil.showLong(ProjectListActivity.this,"请求数据为空！");
                }

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                requestError(ex);
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
        return null;
    }
}
