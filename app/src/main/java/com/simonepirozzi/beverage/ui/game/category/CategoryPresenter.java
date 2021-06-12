package com.simonepirozzi.beverage.ui.game.category;

import android.app.Activity;
import android.content.Intent;

import androidx.annotation.NonNull;

import com.anjlab.android.iab.v3.BillingProcessor;
import com.anjlab.android.iab.v3.TransactionDetails;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.simonepirozzi.beverage.ui.game.GameActivity;
import com.simonepirozzi.beverage.R;
import com.simonepirozzi.beverage.data.db.TinyDB;
import com.simonepirozzi.beverage.data.db.TinyManager;
import com.simonepirozzi.beverage.data.db.model.Bonus;
import com.simonepirozzi.beverage.data.db.model.Category;
import com.simonepirozzi.beverage.data.db.model.Character;
import com.simonepirozzi.beverage.data.repository.FirestoreManager;
import com.simonepirozzi.beverage.data.repository.MainRepository;
import com.simonepirozzi.beverage.utils.Constants;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class CategoryPresenter implements CategoryContract.Presenter, BillingProcessor.IBillingHandler {

    private CategoryContract.View view;
    private Activity activity;
    private BillingProcessor bp;
    private TinyDB tinyDB;
    private MainRepository repo;

    public CategoryPresenter(Activity activity, CategoryContract.View view) {
        this.activity = activity;
        this.view = view;
        this.tinyDB = TinyManager.getInstance(activity);
        this.repo = new MainRepository();
    }

    @Override
    public void onProductPurchased(String productId, TransactionDetails details) {
        view.startDialog(activity.getString(R.string.product_buy_title), activity.getString(R.string.ok_billing_dialog), SweetAlertDialog.SUCCESS_TYPE);
    }

    @Override
    public void onPurchaseHistoryRestored() {
    }

    @Override
    public void onBillingError(int errorCode, Throwable error) {
        view.startDialog(activity.getString(R.string.error_buy_title), activity.getString(R.string.error_billing_dialog), SweetAlertDialog.ERROR_TYPE);
    }

    @Override
    public void onBillingInitialized() {
    }

    @Override
    public void initializeBilling() {
        bp = new BillingProcessor(activity, Constants.LICENSE_KEY, this);
        bp.initialize();
    }

    @Override
    public void subscribeBilling(String productId) {
        if (!bp.isSubscribed(productId)) {
            bp.subscribe(activity, productId);
        }
    }

    @Override
    public void releaseBilling() {
        if (bp != null) {
            bp.release();
        }
    }

    @Override
    public boolean handleActivityBilling(int requestCode, int resultCode, Intent data) {
        return bp.handleActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void getCategories() {
        repo.getCollection(FirestoreManager.CATEGORY_COLLECTION).addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                view.createCategoriesLayout(task.getResult().toObjects(Category.class));
            }
        });
    }

    @Override
    public void getCategoryCharacters(String category) {
        repo.getCollection(category).addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isComplete()){
                    ArrayList<Object> personaggi2 = new ArrayList<>();
                    for(int i=0;i<task.getResult().toObjects(Character.class).size();i++){
                        personaggi2.add(task.getResult().toObjects(Character.class).get(i));
                    }
                    Collections.shuffle(personaggi2);

                    getBonusDocument(FirestoreManager.BONUS_DOC).addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            Bonus bonus = task.getResult().toObject(Bonus.class);
                            if (bonus != null && bonus.getNome() != null){
                                FirestoreManager.setBONUSARRAY(bonus.getNome());
                            }
                            getBonusDocument(FirestoreManager.MALUS_DOC).addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    Bonus bonus=task.getResult().toObject(Bonus.class);
                                    if (bonus != null && bonus.getNome() != null){
                                        FirestoreManager.setMALUSARRAY(bonus.getNome());
                                    }
                                }
                            });
                        }
                    });

                    tinyDB.putListObject(TinyManager.GAME_LIST,personaggi2);
                    tinyDB.putInt(TinyManager.QUESTION_INDEX,0);
                    view.cancelDialog();
                    activity.startActivity(new Intent(activity, GameActivity.class));
                }
            }
        });
    }

    @Override
    public Task<DocumentSnapshot> getBonusDocument(String document) {
        return repo.getDocument(FirestoreManager.BONUS_COLLECTION,document);
    }

    @Override
    public boolean isBillingSubscribed(String productId) {
        return bp.isSubscribed(productId);
    }

    @Override
    public void sortCategory(List<Category> categoryList) {
        Collections.sort(categoryList, new Comparator<Category>() {
            @Override
            public int compare(Category o1, Category o2) {
                if (o1.getStato().equalsIgnoreCase(FirestoreManager.FREE_STATE) && o2.getStato().equalsIgnoreCase(FirestoreManager.FREE_STATE)) {
                    return 0;
                } else if (o1.getStato().equalsIgnoreCase(FirestoreManager.PREMIUM_STATE) && o2.getStato().equalsIgnoreCase(FirestoreManager.FREE_STATE)) {

                    if(isBillingSubscribed(o1.getId())){
                        return 0;
                    }else{
                        return 1;
                    }

                } else if (o1.getStato().equalsIgnoreCase(FirestoreManager.FREE_STATE) && o2.getStato().equalsIgnoreCase(FirestoreManager.PREMIUM_STATE)) {
                    if(isBillingSubscribed(o2.getId())){
                        return 0;
                    }else{
                        return -1;
                    }

                } else if (o1.getStato().equalsIgnoreCase(FirestoreManager.PREMIUM_STATE) && o2.getStato().equalsIgnoreCase(FirestoreManager.PREMIUM_STATE)) {
                    if(isBillingSubscribed(o1.getId()) && isBillingSubscribed(o2.getId())){
                        return 0;
                    }else if(isBillingSubscribed(o1.getId()) && !isBillingSubscribed(o2.getId())){
                        return -1;
                    }else if(!isBillingSubscribed(o1.getId()) && isBillingSubscribed(o2.getId())){
                        return 1;
                    }else{
                        return 0;
                    }
                }
                return 0;
            }
        });
        view.addCategoriesView(categoryList);
    }
}
