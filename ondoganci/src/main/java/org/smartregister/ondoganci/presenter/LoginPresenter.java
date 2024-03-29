package org.smartregister.ondoganci.presenter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;

import org.smartregister.configurableviews.model.LoginConfiguration;
import org.smartregister.configurableviews.model.ViewConfiguration;
import org.smartregister.login.model.BaseLoginModel;
import org.smartregister.login.presenter.BaseLoginPresenter;
import org.smartregister.ondoganci.R;
import org.smartregister.ondoganci.application.OndoganciApplication;
import org.smartregister.ondoganci.interactor.LoginInteractor;
import org.smartregister.ondoganci.util.AppConstants;
import org.smartregister.ondoganci.util.ImageLoaderRequest;
import org.smartregister.view.contract.BaseLoginContract;

import java.lang.ref.WeakReference;

public class LoginPresenter extends BaseLoginPresenter implements BaseLoginContract.Presenter {

    private LoginConfiguration metadata;
    public LoginPresenter(BaseLoginContract.View loginView) {
        mLoginView = new WeakReference<>(loginView);
        mLoginInteractor = new LoginInteractor(this);
        mLoginModel = new BaseLoginModel();
    }

    @Override
    public void canvasGlobalLayoutListenerProcessor(ScrollView canvasSV,
                                                    ViewTreeObserver.OnGlobalLayoutListener layoutListener) {
        // do nothing
    }



    @Override
    public void processViewCustomizations() {
        try {
            String jsonString = getJsonViewFromPreference(
                    AppConstants.KEY.VIEW_CONFIGURATION_PREFIX + AppConstants.CONFIGURATION.LOGIN);
            if (jsonString == null) {
                return;
            }

            ViewConfiguration loginView = OndoganciApplication.getJsonSpecHelper().getConfigurableView(jsonString);
            metadata = (LoginConfiguration) loginView.getMetadata();
            LoginConfiguration.Background background = metadata.getBackground();
            CheckBox showPasswordCheckBox = getLoginView().getActivityContext()
                    .findViewById(R.id.login_show_password_checkbox);

            TextView showPasswordTextView = getLoginView().getActivityContext()
                    .findViewById(R.id.login_show_password_text_view);
            if (!metadata.getShowPasswordCheckbox()) {
                showPasswordCheckBox.setVisibility(View.GONE);
                showPasswordTextView.setVisibility(View.GONE);
            } else {
                showPasswordCheckBox.setVisibility(View.VISIBLE);
                showPasswordTextView.setVisibility(View.VISIBLE);
            }

            if (background.getOrientation() != null && background.getStartColor() != null && background
                    .getEndColor() != null) {
                View loginLayout = getLoginView().getActivityContext().findViewById(R.id.login_layout);
                GradientDrawable gradientDrawable = new GradientDrawable();
                gradientDrawable.setShape(GradientDrawable.RECTANGLE);
                gradientDrawable.setOrientation(
                        GradientDrawable.Orientation.valueOf(background.getOrientation()));
                gradientDrawable.setColors(new int[]{Color.parseColor(background.getStartColor()),
                        Color.parseColor(background.getEndColor())});
                loginLayout.setBackground(gradientDrawable);
            }

            if (metadata.getLogoUrl() != null) {
                ImageView imageView = getLoginView().getActivityContext().findViewById(R.id.login_logo);
                ImageLoaderRequest.getInstance(getLoginView().getActivityContext()).getImageLoader()
                        .get(metadata.getLogoUrl(), ImageLoader.getImageListener(imageView,
                                R.drawable.ic_who_logo, R.drawable.ic_who_logo)).getBitmap();
            }

        } catch (Exception e) {
            Log.d("processView", e.getMessage());
        }
    }

    @Override
    public boolean isServerSettingsSet() {
        return true;
    }

}
