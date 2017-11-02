package com.afinalstone.androidstudy.pdfdemo;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.afinalstone.androidstudy.pdfdemo.permission.MPermission;
import com.afinalstone.androidstudy.pdfdemo.permission.annotation.OnMPermissionDenied;
import com.afinalstone.androidstudy.pdfdemo.permission.annotation.OnMPermissionGranted;
import com.afinalstone.androidstudy.pdfdemo.permission.annotation.OnMPermissionNeverAskAgain;


/***
 * 
 * @author SHI 
 * 所有Activity的父类
 * 2016-2-1 11:41:42
 *
 */
public abstract class MyBaseActivity extends AppCompatActivity {
	/**初始化布局文件**/
	public abstract void initView();
	/**初始化页面数据**/
	public abstract void initData();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initView();
		initData();
	}

	/**
	 * 基本权限管理
	 */
	private final String[] BASIC_PERMISSIONS = new String[]{
			Manifest.permission.WRITE_EXTERNAL_STORAGE,
			Manifest.permission.READ_EXTERNAL_STORAGE,
			//Manifest.permission.SYSTEM_ALERT_WINDOW
	};
	private final int BASIC_PERMISSION_REQUEST_CODE = 100;

	protected void requestBasicPermission() {
		MPermission.printMPermissionResult(true, this, BASIC_PERMISSIONS);
		MPermission.with(this)
				.setRequestCode(BASIC_PERMISSION_REQUEST_CODE)
				.permissions(BASIC_PERMISSIONS)
				.request();
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		MPermission.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
	}

	@OnMPermissionGranted(BASIC_PERMISSION_REQUEST_CODE)
	public void onBasicPermissionSuccess() {
		MPermission.printMPermissionResult(false, this, BASIC_PERMISSIONS);
	}

	@OnMPermissionDenied(BASIC_PERMISSION_REQUEST_CODE)
	@OnMPermissionNeverAskAgain(BASIC_PERMISSION_REQUEST_CODE)
	public void onBasicPermissionFailed() {
		Toast.makeText(this, "未全部授权，部分功能可能无法正常运行！", Toast.LENGTH_SHORT).show();
		MPermission.printMPermissionResult(false, this, BASIC_PERMISSIONS);
	}
	
}
