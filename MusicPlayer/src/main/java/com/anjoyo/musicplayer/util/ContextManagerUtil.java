package com.anjoyo.musicplayer.util;

import java.util.List;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;

public class ContextManagerUtil {

	/** 
	 * @param context 上下文
	 * @param className 服务类的名称
	 * @return 返回服务是否还在后台运行
	 */
	public static boolean isServiceRunning(Context context,Class<?> obj) {

        boolean isRunning = false;
        ActivityManager activityManager = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE); 
        List<ActivityManager.RunningServiceInfo> serviceList = activityManager.getRunningServices(30);

        if (!(serviceList.size()>0)) {
            return false;
        }

        for (int i=0; i<serviceList.size(); i++) {
            if (serviceList.get(i).service.getClassName().equals(obj.getName()) == true) {
                isRunning = true;
                break;
            }
        }
        return isRunning;
    }
	
	/** 
	 * 返回本程序中Task中顶端的Activity
	 * @param context
	 * @return 
	 */
	public static ComponentName getTopActivity(Context context) {
		//需要android.permission.GET_TASKS权限
		ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		int maxNum = 10;//获取的正在运行的activity的最大数量
		List<ActivityManager.RunningTaskInfo> runningTasks = activityManager.getRunningTasks(maxNum);
		for (ActivityManager.RunningTaskInfo runningTaskInfo : runningTasks) {
			ComponentName componentName = runningTaskInfo.topActivity;
			if (componentName != null && componentName.getClassName().startsWith("com.anjoyo.musicplayer")) {
				return componentName;
			}
		}
		
		return null;
	}
	
//	/** 
//	 * 返回Task中Activity的数目
//	 * @param context
//	 * @return 
//	 */
//	public static int getActivityCount(Context context) {
//		int count = 0;
//		//需要android.permission.GET_TASKS权限
//		ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
//		int maxNum = 10;//获取的正在运行的activity的最大数量
//		List<ActivityManager.RunningTaskInfo> runningTasks = activityManager.getRunningTasks(maxNum);
//		for (ActivityManager.RunningTaskInfo runningTaskInfo : runningTasks) {
//			int num = runningTaskInfo.numActivities;
//			if (num > count) {
//				count = num;
//			}
//		}
//		return count;
//	}
	
}
