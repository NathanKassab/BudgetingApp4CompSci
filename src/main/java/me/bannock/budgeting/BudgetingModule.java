package me.bannock.budgeting;

import com.google.inject.AbstractModule;
import me.bannock.budgeting.money.MoneyService;
import me.bannock.budgeting.money.MoneyServiceImpl;
import me.bannock.budgeting.ui.BudgetingForm;

public class BudgetingModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(BudgetingForm.class);
        bind(MoneyService.class).to(MoneyServiceImpl.class);
    }
}
