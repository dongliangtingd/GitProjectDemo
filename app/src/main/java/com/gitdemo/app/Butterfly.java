package com.gitdemo.app;

import android.app.Activity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import java.lang.reflect.Field;

/**
 * Created by fuyoukache on 2018/8/22.
 */

public class Butterfly {

    public static void bind(Activity act) {
        Class c = act.getClass();
        // 获取这个activity中的所有字段
        Field[] fields = c.getDeclaredFields();

        for (Field field : fields) {
            // 循环拿到每一个字段
            if (field.isAnnotationPresent(BindView.class)) { // 如果这个字段有注入的注解
                // 获取注解对象
                BindView b = field.getAnnotation(BindView.class);
                int value = b.value();
                field.setAccessible(true);  // 即使私有的也可以设置数据
                Object view = null;
                try {
                    view = act.findViewById(value);
                    // 设置字段的属性
                    field.set(act, view);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                    Log.i("TAG", "注入属性失败：" + field.getClass().getName() + ":" + field.getName());
                }

                try {
                    if (view instanceof View) {
                        View v = (View) view;
                        String methodName = b.click(); // 获取点击事件的触发的方法名称
                        EventListener eventListener = null;
                        if (!TextUtils.isEmpty(methodName)) {
                            eventListener = new EventListener(act);
                            v.setOnClickListener(eventListener);
                            eventListener.setClickMethodName(methodName);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
