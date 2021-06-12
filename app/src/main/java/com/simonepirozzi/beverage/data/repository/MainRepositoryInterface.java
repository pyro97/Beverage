package com.simonepirozzi.beverage.data.repository;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public interface MainRepositoryInterface {
    Task<QuerySnapshot> getCollection(String collection);
    Task<DocumentSnapshot> getDocument(String collection, String document);
}
