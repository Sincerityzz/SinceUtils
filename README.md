[ ![Download](https://api.bintray.com/packages/sincerityzz/maven/SinceTools/images/download.svg?version=1.0.3) ](https://bintray.com/sincerityzz/maven/SinceTools/1.0.3/link)
# SinceTools
 ![since.png](https://github.com/Sincerityzz/SinceUtils/blob/master/img/bg.jpg?raw=true)
## SinceTools这个库的主要目的就是看完一些经典的Android框架后自己手动去模仿简化版的一个工具库.当然也可以尝试去使用这个工具库
[ ![Download](https://api.bintray.com/packages/sincerityzz/maven/SinceTools/images/download.svg?version=1.0.3) ](https://bintray.com/sincerityzz/maven/SinceTools/1.0.3/link)
```java
  implementation 'com.sincerity:SinceTools:1.0.3'
```
## 目前版本主要包括 
 ### 1. buttonKnife的手动版本 ioc 
 绑定一个View 
  ```java
  public class MainActivity extends AppCompatActivity {
    @BindView(R.id.tvResponse)
   private TextView mTvResponse
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
           ViewUtils.bind(this);
        }
        
  }
  ```
  注册一个点击事件
  ```java
  @OnClick(R.id.tvResponse)
    private void setTvResponse(){
        
    }

  ```
  拓展功能 点击按钮时判断网络连接
  ```java
  @OnClick(R.id.tvResponse)
    @CheckNet("添加没网提示信息")
    private void setTvResponse(){

    }
  ```
 ### 2. volley简化版 SVooley 
```java
 SVolley.sendJsonRequest(url, null, new ResponseEntity() {
                    @Override
                    public void onSuccess(Object object) {
//                        JSONObject jsonObject = new JSONObject(object.toString())
                        tvResponse.setText(object.toString());
                    }

                    @Override
                    public void onFail(String errorString) {
                        tvResponse.setText(errorString);
                    }
                });
                
```
这个网络请求内部实现了对json数据的处理 当然也可以不用去处理
```java
    /**
     *
     * @param url 必须字段 不能为空
     * @param requestInfo 请求参数 可以为空 但是不能省略
     * @param method  请求方式 GET POST HTTP 等.. 可以为空 /省略默认空值为GET
     * @param response 请求返回的实体类 如user.class  可为省略
     * @param dataListener 成功失败的回调 不能为空/或者去省略
     * @param <T>
     */
 public static <T> void sendJsonRequest(String url, T requestInfo, String method, Class response, ResponseEntity dataListener)
```
### 3. BannerView 实现无限轮播
```java
 //设置适配器
 mBannerView.setBannerAdapter(new BannerAdapter() {
            @Override
            public View getView(int position) { //返回的是ViewPager的ItemView
                ImageView view = new ImageView(MainActivity.this);
                String imagePath = bean.getData().get(position).getImagePath();
                Glide.with(MainActivity.this).load(imagePath).centerCrop().into(view);
                return view;
            }

            @Override
            public int getCount() {
                return bean.getData().size(); //这里返回的Item的总条目
            }

            @Override
            public String getBannerDesc(int position) {
                return bean.getData().get(position).getTitle(); //返回的是Banner的页面描述 如果没有描述可以忽略
            }
        });
        mBannerView.setScrollerDuration(950); //设置动画的播放速率
        mBannerView.setCurrentSecond(3500); //设置间隔
        mBannerView.startAutoScroll();//开启无限轮播
```
添加自定义属性 
```xml
  <!--指示器的颜色-->
        <attr name="dotIndicatorFocus" format="color|reference" />
        <attr name="dotIndicatorNormal" format="color|reference" />
        <!--指示器大小-->
        <attr name="dotIndicatorSize" format="dimension" />
        <!--指示器的间距-->
        <attr name="dotIndicatorSpacing" format="dimension" />
        <!--指示器的位置-->
        <attr name="dotIndicatorGravity" format="enum">
            <enum name="left" value="0" />
            <enum name="center" value="1" />
            <enum name="right" value="2" />
        </attr>
        <!--底部颜色-->
        <attr name="bottomColor" format="color" />
```
### 4. RecycleView 
- 首先在XML中添加WrapperRecycleView
```java
    <com.sincerity.utilslibrary.view.RecycleView.adapter.WrapperRecycleView
        android:id="@+id/rv_infoMain"
        android:layout_width="match_parent"
        android:layout_height="match_parent" />
```
- 新建adapter
```java
class Adapter extends BaseAdapter<ChartData> {

        public Adapter(Context context, List data) {
        //多布局实现 单一布局不需要返回MultTypeSupport
            super(context, data, new MultTypeSupport<ChartData>() {
                @Override
                public int getLayoutId(ChartData item) {
                    if (item.isMe) {
                        return R.layout.item_right;
                    }
                    return R.layout.item_left;
                }
            });
        }


        @Override
        protected void setData(BaseViewHolder baseViewHolder, ChartData data, int i) {
            if (data.isMe) {
                baseViewHolder.setText(R.id.item_tv_right, data.chartContent);
            } else {
                baseViewHolder.setText(R.id.item_tv_left, data.chartContent);
            }

        }
    }
```
- 设置适配器以及添加和移除头,尾部
```java
       //适配器
        final Adapter mAdapter = new Adapter(getActivity(), list);
        //设置适配器
        mRecycleView.setAdapter(mAdapter);
        //头部View
        final View mHeaderView = LayoutInflater.from(getActivity()).inflate(R.layout.header_view, mRecycleView, false);
        //添加头部
        mRecycleView.addHeaderView(mHeaderView);
        //头部的点击事件
        mHeaderView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               //移除头部
                mRecycleView.removeHeaderView(mHeaderView);
                mAdapter.notifyItemRemoved(0);
            }
        });
```
后续会持续更新和对本库的bug修正

## 历史版本
### 1.0.3 
#### 1.添加RecycleView的封装,包含对多布局头部尾部侧滑和分割线的封装,
#### 2.添加异常拦截上报类
#### 3.添加热修复的手动实现和阿里Spohix3的支持 
#### 4.添加通用评论消息的SinceDialog

### 1.0.2 添加BannerView 添加可以切换网络引擎的Httputils类 
### 1.0.1 最初版本 实现基础ioc 和基础Volley功能 


---
### [我的简书](https://www.jianshu.com/u/ebad2728e6c7)
