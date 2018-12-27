package cc.yelinvan.photographhome.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jaeger.library.StatusBarUtil;

import org.xutils.common.Callback;
import org.xutils.x;

import java.util.List;

import cc.yelinvan.photographhome.Constant;
import cc.yelinvan.photographhome.R;
import cc.yelinvan.photographhome.activity.base.BaseActivity;
import cc.yelinvan.photographhome.bean.ProjectBean;
import cc.yelinvan.photographhome.bean.ResponseBean;
import cc.yelinvan.photographhome.utils.NetParams;

/**
 * 相册列表activity
 *  Create by Johnson on 2018年12月26日17:05:06
 */
public class ProjectListActivity extends BaseActivity {
    private List<ProjectBean> projectList;  //相册列表

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getProjectList();
    }

    @Override
    protected void init() {
        setContentView(R.layout.activity_project);
        //设置状态栏颜色
        StatusBarUtil.setColor(this, getResources().getColor(R.color.colorAccent), 0);

    }

    protected List<ProjectBean> getProjectList(){
        NetParams netParams = new NetParams(this,Constant.Url.GETPROJECT,5000);
        x.http().post(netParams, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                if(!result.isEmpty()){
                    ResponseBean<List<ProjectBean>> ResponseBean = new Gson().fromJson(result,
                            new TypeToken<ResponseBean<List<ProjectBean>>>(){}.getType());
                    if(ResponseBean.isCode()){
                        projectList = ResponseBean.getData();
                    }else {

                    }

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
