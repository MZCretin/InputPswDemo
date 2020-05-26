# InputPswDemo
[![](https://jitpack.io/v/MZCretin/InputPswDemo.svg)](https://jitpack.io/#MZCretin/InputPswDemo)

这是一个支付密码的demo，点击输入密码，弹出一个popwindow进行密码的输入，可以对密码的长度进行设置，可以自定义展示的效果

### 使用方式
+ Step 1. Add the JitPack repository to your build file 
```
    Add it in your root build.gradle at the end of repositories:

    allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
```
+ Step 2. Add the dependency
```
    dependencies {
	    implementation 'com.github.MZCretin:InputPswDemo:v1.0.0'
	}
```

### API
```
    PswInputDialog pswInputDialog = new PswInputDialog(this);
    //showPswDialog()一定要在最前面执行
    pswInputDialog.showPswDialog();
    
    //隐藏忘记密码的入口
    pswInputDialog.hideForgetPswClickListener();
    
    //设置忘记密码的点击事件
    pswInputDialog.setOnForgetPswClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Toast.makeText(MainActivity.this, "您点击了忘记密码", Toast.LENGTH_SHORT).show();
        }
    });
    
    //设置密码长度
    pswInputDialog.setPswCount(pswCount);
    //设置密码输入完成监听
    pswInputDialog.setListener(new PswInputDialog.OnPopWindowClickListener() {
        @Override
        public void onPopWindowClickListener(String psw, boolean complete) {
            if (complete)
                Toast.makeText(MainActivity.this, "你输入的密码是：" + psw, Toast.LENGTH_SHORT).show();
        }
    });
```

![image](https://github.com/MZCretin/InputPswDemo/blob/master/pics/cover.png)