package me.bannock.budgeting;

import com.formdev.flatlaf.FlatDarculaLaf;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import me.bannock.budgeting.ui.BudgetingForm;

public class Budgeting {

    static{
        // We want this to be executed before anything else
        // This is a styling library, calling this will do some internal
        // setup with swing to give the window a dark theme
        FlatDarculaLaf.setup();
    }

    /**
     * Application entry point
     * @param args Program arguments
     */
    public static void main(String[] args) {
        // We use guice to create are app instance before calling launch on it
        Injector injector = Guice.createInjector(new BudgetingModule());
        injector.getInstance(BudgetingForm.class).createWindow();
    }

}
