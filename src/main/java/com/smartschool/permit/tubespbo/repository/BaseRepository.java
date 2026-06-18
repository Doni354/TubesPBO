/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.smartschool.permit.tubespbo.repository;

/**
 *
 * @author Doni354
 */
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.smartschool.permit.tubespbo.app.FirestoreConnection;
import com.smartschool.permit.tubespbo.model.BaseModel;

public abstract class BaseRepository<T extends BaseModel> implements CrudRepository<T> {
    protected final String collectionName;
    protected final Firestore db;

    public BaseRepository(String collectionName) {
        this.collectionName = collectionName;
        // Kita panggil Singleton DB di sini
        this.db = FirestoreConnection.getInstance().getDb(); 
    }

    protected abstract T toEntity(DocumentSnapshot doc);

    @Override
    public List<T> getAll() {
        List<T> list = new ArrayList<>();
        try {
            ApiFuture<QuerySnapshot> future = db.collection(collectionName).get();
            List<QueryDocumentSnapshot> documents = future.get().getDocuments();
            for (QueryDocumentSnapshot document : documents) {
                list.add(toEntity(document));
            }
        } catch (Exception e) {
            System.err.println("Error getting all documents: " + e.getMessage());
        }
        return list;
    }

    @Override
    public T getById(String id) {
        try {
            DocumentSnapshot document = db.collection(collectionName).document(id).get().get();
            if (document.exists()) {
                return toEntity(document);
            }
        } catch (Exception e) {
            System.err.println("Error getting document by ID: " + e.getMessage());
        }
        return null;
    }

    @Override
    public String create(T entity) {
        try {
            ApiFuture<DocumentReference> future = db.collection(collectionName).add(entity.toMap());
            return future.get().getId();
        } catch (Exception e) {
            System.err.println("Error creating document: " + e.getMessage());
            return null;
        }
    }

    @Override
    public void update(String id, Map<String, Object> data) {
        db.collection(collectionName).document(id).update(data);
    }

    @Override
    public void delete(String id) {
        db.collection(collectionName).document(id).delete();
    }
}