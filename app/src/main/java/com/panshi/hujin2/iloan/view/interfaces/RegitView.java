package com.panshi.hujin2.iloan.view.interfaces;

/**
 * Created by Administrator on 2018/7/24 0024.
 */

public interface RegitView {
    void regitSucess();
    void yanma(long second);
    void checkVertifyCodeSucess();
    //注册过
    void  regited();
    //没有注册
    void noRegit();
    //没有注册(没离开编辑框)
    void noRegitFouse();

}
