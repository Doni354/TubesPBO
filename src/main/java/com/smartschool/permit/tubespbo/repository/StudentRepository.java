package com.smartschool.permit.tubespbo.repository;

import com.google.cloud.firestore.DocumentSnapshot;
import com.smartschool.permit.tubespbo.model.Student;

/**
 * Repository untuk collection "students" di Firestore.
 */
public class StudentRepository extends BaseRepository<Student> {

    public StudentRepository() {
        super("students");
    }

    @Override
    protected Student toEntity(DocumentSnapshot doc) {
        Student student = new Student();
        student.fromMap(doc.getData());
        student.setId(doc.getId());
        return student;
    }

    /**
     * Get student by Firebase Auth UID (doc ID = UID).
     */
    public Student getByUid(String uid) {
        return getById(uid);
    }

    /**
     * Create student document with specific ID (Firebase Auth UID).
     * Berbeda dengan create() bawaan yang auto-generate ID.
     */
    public void createWithId(String uid, Student student) {
        try {
            db.collection(collectionName).document(uid).set(student.toMap()).get();
        } catch (Exception e) {
            System.err.println("Error creating student with ID: " + e.getMessage());
            throw new RuntimeException("Gagal menyimpan data siswa ke database!", e);
        }
    }
}
