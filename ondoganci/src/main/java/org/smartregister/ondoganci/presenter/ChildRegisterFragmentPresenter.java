package org.smartregister.ondoganci.presenter;

import org.smartregister.child.contract.ChildRegisterFragmentContract;
import org.smartregister.child.presenter.BaseChildRegisterFragmentPresenter;
import org.smartregister.child.util.Constants;
import org.smartregister.child.util.Utils;
import org.smartregister.ondoganci.util.DBQueryHelper;

public class ChildRegisterFragmentPresenter extends BaseChildRegisterFragmentPresenter {

    public ChildRegisterFragmentPresenter(ChildRegisterFragmentContract.View view, ChildRegisterFragmentContract.Model model,
                                          String viewConfigurationIdentifier) {
        super(view, model, viewConfigurationIdentifier);
    }

    @Override
    public String getMainCondition() {
        return String.format("((%s is null AND %s == '0') OR %s == '0')",
                Utils.metadata().getRegisterQueryProvider().getDemographicTable() + "." + Constants.KEY.DATE_REMOVED,
                Utils.metadata().getRegisterQueryProvider().getDemographicTable() + "." + Constants.KEY.IS_CLOSED,
                Utils.metadata().getRegisterQueryProvider().getChildDetailsTable() + "." + Constants.KEY.IS_CLOSED);
    }

//    @Override
//    public void initializeQueries(String mainCondition){
//
//    }

    @Override
    public String getDefaultSortQuery() {
        return DBQueryHelper.getSortQuery();
    }
}
