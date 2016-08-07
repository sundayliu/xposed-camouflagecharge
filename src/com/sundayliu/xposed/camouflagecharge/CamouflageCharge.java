package com.sundayliu.xposed.camouflagecharge;

import android.content.Intent;
import android.os.BatteryManager;
import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;

public class CamouflageCharge implements IXposedHookLoadPackage{

    @Override
    public void handleLoadPackage(LoadPackageParam lpparam) throws Throwable {
        XposedHelpers.findAndHookMethod("android.content.Intent", lpparam.classLoader, "getIntExtra",
                String.class,
                int.class,
                new XC_MethodHook(){
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable{
                Intent intent = (Intent)param.thisObject;
                final String action = intent.getAction();
                if (action.equals(Intent.ACTION_BATTERY_CHANGED)){
                    if (BatteryManager.EXTRA_LEVEL.equals(param.args[0] + "")){
                        param.setResult(1);
                    }
                    else if ("status".equals(param.args[0] + "")){
                        XposedBridge.log("[camouflagechange]status");
                        param.setResult(BatteryManager.BATTERY_STATUS_NOT_CHARGING);
                    }
                }
            }
            
            protected void afterHookedMethod(MethodHookParam param)
                throws Throwable{
                
            }
        });
        
    }
    
}
