package com.simonepirozzi.beverage.ui.game.category;

import android.content.Intent;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.simonepirozzi.beverage.data.db.model.Category;

import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class CategoryContract {

    interface Presenter{
        void initializeBilling();
        void subscribeBilling(String productId);
        void releaseBilling();
        boolean handleActivityBilling(int requestCode, int resultCode, Intent data);
        void getCategories();
        void getCategoryCharacters(String category);
        Task<DocumentSnapshot> getBonusDocument(String document);
        boolean isBillingSubscribed(String productId);
        void sortCategory(List<Category> categoryList);
    }

    interface View{
        SweetAlertDialog startDialog(String title, String message, int tipo);
        void cancelDialog();
        void createCategoriesLayout(List<Category> categoryList);
        void addCategoriesView(List<Category> categoryList);
    }
}
