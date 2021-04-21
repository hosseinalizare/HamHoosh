package com.example.koohestantest1;

import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.FragmentActivity;

public class BaseActivity extends FragmentActivity{
//    private ViewTreeObserver.OnGlobalLayoutListener keyboardLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {
//        @Override
//        public void onGlobalLayout() {
//            try {
//                int heightDiff = rootLayout.getRootView().getHeight() - rootLayout.getHeight();
//                int contentViewTop = getWindow().findViewById(Window.ID_ANDROID_CONTENT).getTop();
//
//                LocalBroadcastManager broadcastManager = LocalBroadcastManager.getInstance(BaseActivity.this);
//
//                if(heightDiff <= contentViewTop){
//                    onHideKeyboard();
//
//                    Intent intent = new Intent("KeyboardWillHide");
//                    broadcastManager.sendBroadcast(intent);
//                } else {
//                    int keyboardHeight = heightDiff - contentViewTop;
//                    onShowKeyboard(keyboardHeight);
//
//                    Intent intent = new Intent("KeyboardWillShow");
//                    intent.putExtra("KeyboardHeight", keyboardHeight);
//                    broadcastManager.sendBroadcast(intent);
//                }
//            }catch (Exception e){
//                logMessage("BaseActivity 100 >> " + e.getMessage(), BaseActivity.this);
//            }
//        }
//    };

//    private boolean keyboardListenersAttached = true;
    private CoordinatorLayout rootLayout;

    protected void onShowKeyboard(int keyboardHeight) {}
    protected void onHideKeyboard() {}

//    protected void attachKeyboardListeners() {
//        if (keyboardListenersAttached) {
//            return;
//        }
//
//        try {
//            rootLayout = (CoordinatorLayout) findViewById(R.id.rootLayout);
//            rootLayout.getViewTreeObserver().addOnGlobalLayoutListener(keyboardLayoutListener);
//
//            keyboardListenersAttached = true;
//        }catch (Exception e){
//            logMessage("BaseActivity 100 >> " + e.getMessage(), BaseActivity.this);
//        }
//
//    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

//        if (keyboardListenersAttached) {
//            rootLayout.getViewTreeObserver().removeGlobalOnLayoutListener(keyboardLayoutListener);
//        }
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
