package com.kodonho.android.annotationtest;

import android.app.Activity;
import android.util.Log;
import android.view.View;

import java.lang.reflect.Field;

/**
 * Created by fastcampus on 2016-10-27.
 */

public class ButterSpoon {

    public static <T> void bind(T object){

        // 특정되지 않은 field 들을 모두 꺼내기 위해서 자바의 reflection 을 사용한다
        if( !(object instanceof Activity) )
            return;
        Activity activity = (Activity)object;

        try {
            // 반복문을 돌면서 class 에 들어 있는 멤버 field들을 모두 꺼내고
            for (Field field :activity.getClass().getDeclaredFields()) {
                // 멤버필드에 BindView 애너테이션이 적용되어 있으면
                if(field.isAnnotationPresent(com.kodonho.android.annotationtest.BindView.class)){

                    Log.i("ButterSpoon","Field="+field);

                    // 해당 필드를 통해
                    BindView bindView = field.getAnnotation(BindView.class);
                    //Type type = field.getGenericType();
                    // 뷰 ID를 가져오고
                    int viewID = bindView.viewID();

                    Log.i("ButterSpoon","ViewID="+viewID);

                    // 해당 뷰를 가져온다
//                    String identifierString = field.getName();
//                    int identifier = activity.getResources().getIdentifier(identifierString, "id", activity.getPackageName());
//                    if( identifier == 0 )
//                        continue;

                    View findedView = activity.findViewById(viewID);
                    Log.i("ButterSpoon","findedView="+findedView);

                    //if( findedView.getClass() == field.getType() ) {
                        try{
                            field.setAccessible(true);
                            field.set(object, findedView);
                        }catch (IllegalArgumentException e){
                            e.printStackTrace();
                        }catch (IllegalAccessException e){
                            e.printStackTrace();
                        }
                   // }
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    // 참고용 퍼왔음 출처 : https://softwaregeeks.org/2011/12/05/%EB%A6%AC%ED%94%8C%EB%A0%89%EC%85%98reflection%EC%9D%84-%ED%99%9C%EC%9A%A9%ED%95%9C-%EC%95%88%EB%93%9C%EB%A1%9C%EC%9D%B4%EB%93%9C-%EC%9C%84%EC%A0%AF-%EB%A7%A4%ED%95%91mapping-%EA%B0%84%EC%86%8C/
    public static void mappingViews(Object object)
    {
        if( !(object instanceof Activity) )
            return;

        Activity activity = (Activity)object;
        Field[] fields = activity.getClass().getDeclaredFields();
        for (Field field : fields)
        {
            String identifierString = field.getName();
            int identifier = activity.getResources().getIdentifier(identifierString, "id", activity.getPackageName());
            if( identifier == 0 )
                continue;

            View findedView = activity.findViewById(identifier);
            if( findedView == null )
                continue;

            if( findedView.getClass() == field.getType() ) {
                try
                {
                    field.setAccessible(true);
                    field.set(object, findedView);
                }
                catch (IllegalArgumentException e)
                {
                    e.printStackTrace();
                }
                catch (IllegalAccessException e)
                {
                    e.printStackTrace();
                }
            }
        }
    }
}
