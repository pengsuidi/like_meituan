# like_meituan
初学安卓,仿制美团
依赖添加:
权限:

    <uses-permission android:name="android.permission.INTERNET" />
    <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
    <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE"/>
    
application中添加:

    android:networkSecurityConfig="@xml/network_security_config"

build.gradle:


allprojects {
    repositories {
        google()
        jcenter()
        maven { url "https://dl.bintray.com/lingguoding/maven";; }
    }
}


dependencies {

    implementation 'pl.droidsonroids.gif:android-gif-drawable:1.2.2'
    implementation fileTree(dir: 'libs', include: ['*.jar'])
    implementation 'androidx.appcompat:appcompat:1.1.0'
    implementation 'androidx.constraintlayout:constraintlayout:1.1.3'
    implementation 'com.google.android.material:material:1.1.0'
    implementation "androidx.viewpager2:viewpager2:1.0.0"
    implementation 'androidx.legacy:legacy-support-v4:1.0.0'
    implementation 'com.google.android.material:material:1.1.0'
    implementation 'androidx.navigation:navigation-fragment:2.3.0'
    implementation 'androidx.navigation:navigation-ui:2.3.0'
    implementation 'androidx.lifecycle:lifecycle-extensions:2.2.0'
    androidTestImplementation 'androidx.test.ext:junit:1.1.1'
    androidTestImplementation 'androidx.test.espresso:espresso-core:3.2.0'
    /* OKHttp、Okio */
    implementation "com.squareup.okhttp3:okhttp:4.7.2"
    implementation "com.squareup.okio:okio:2.7.0"
    /* FastJson */
    implementation "com.alibaba:fastjson:1.2.70"
    implementation 'com.google.android.material:material:1.1.0'
    testImplementation 'junit:junit:4.12'
    androidTestImplementation 'androidx.test:runner:1.2.0'
    androidTestImplementation 'androidx.test.espresso:espresso-core:3.2.0'
    implementation 'com.google.android.material:material:1.1.0'
    implementation 'androidx.vectordrawable:vectordrawable:1.1.0'
    implementation 'androidx.navigation:navigation-fragment:2.0.0'
    implementation 'androidx.navigation:navigation-ui:2.0.0'
    implementation 'androidx.lifecycle:lifecycle-extensions:2.2.0'
    androidTestImplementation 'androidx.test.ext:junit:1.1.1'
    androidTestImplementation 'androidx.test.espresso:espresso-core:3.2.0'
    //noinspection GradleCompatible
    implementation 'com.wihaohao:PageGridView:1.0.0'
    implementation 'com.youth.banner:banner:1.4.10'
    //glide，比较好的版本，Matisse版本，配合glide3.7.0，因为后面的glide版本没有了asbitmap等方法
    implementation 'com.github.bumptech.glide:glide:3.7.0'
    implementation 'com.android.support:recyclerview-v7:30.0.0-alpha1'
    implementation 'pl.droidsonroids.gif:android-gif-drawable:1.2.6'

}

代码注释:
1. 与后端服务器的连接方法已经封装在OkHttpUtil类中,常使用post方法,post(String url,Map<Strirng,Object> map,Handler handler);

url为相应在服务器上写的servlet,如"http://106.54.87.185:8080/ServletTest/BecomeSeller"; 

map为服务器需求的参数,handler重写HandlerMessage来处理收到服务器发送的数据.

2. 在handler中处理收到的数据时,将服务器发送的JSONObject转化为Result对象:

    private Handler handler = new Handler(Looper.myLooper()) {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Config.STATUS_OK://网络请求成功，但响应需要根据实际来操作
                    Result result = JSONObject.parseObject(msg.obj.toString(), Result.class);
                    break;
                default:
                    break;
            }
        }
    }
    
    将返回的数据转化为所需的List,同理将JSONArray换成JSONObject即可转化为我们自定的数据类:
    
  
    JSONArray jsonArray = (JSONArray) result.getData();List<MyEntity> datas = jsonArray.toJavaList(MyEntity.class);
    
3.
 
