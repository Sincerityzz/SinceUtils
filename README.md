# SinceTools
 ![since.png](https://github.com/Sincerityzz/SinceUtils/blob/master/img/bg.jpg?raw=true)
## SinceTools这个库的主要目的就是看完一些经典的Android框架后自己手动去模仿简化版的一个工具库.当然也可以尝试去使用这个工具库
```java
  implementation 'com.sincerity:SinceTools:1.0.2'
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
后续会持续更新和对本库的bug修正

## 历史版本
### 1.0.2 添加BannerView 添加可以切换网络引擎的Httputils类 
### 1.0.1 最初版本 实现基础ioc 和基础Volley功能 


---
### [我的简书](https://www.jianshu.com/u/ebad2728e6c7)
