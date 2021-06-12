package com.simonepirozzi.beverage.data.repository;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class MainRepository implements MainRepositoryInterface{

    private FirebaseFirestore instance;

    public MainRepository(){
        this.instance = FirestoreManager.getInstance();
    }


    @Override
    public Task<QuerySnapshot> getCollection(String collection) {
        return instance.collection(collection).get();
    }

    @Override
    public Task<DocumentSnapshot> getDocument(String collection, String document) {
        return instance.collection(collection).document(document).get();
    }
}
