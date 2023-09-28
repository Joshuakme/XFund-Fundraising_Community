package com.example.xfund.util

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.SetOptions

class FirebaseHelper {
    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance("https://xfund-89b19-default-rtdb.asia-southeast1.firebasedatabase.app")


    // Example method to get a reference to the Firestore collection
    fun getFirestoreCollection(collectionName: String) = firestore.collection(collectionName)

    // Example method to retrieve data from Firestore
    fun fetchDataFromFirestore(
        collectionName: String,
        query: Query? = null,
        onSuccess: (QuerySnapshot) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        val collectionRef = firestore.collection(collectionName)
        val finalQuery = query ?: collectionRef // Optionally, you can pass a custom query

        finalQuery.get()
            .addOnSuccessListener { querySnapshot ->
                onSuccess(querySnapshot)
            }
            .addOnFailureListener { exception ->
                onFailure(exception)
            }
    }

    // Example method to upload data to Firestore
    fun uploadDataToFirestore(
        collectionName: String,
        documentId: String,
        data: Map<String, Any>,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        val collectionRef = firestore.collection(collectionName)
        val documentRef = collectionRef.document(documentId)

        documentRef.set(data, SetOptions.merge())
            .addOnSuccessListener {
                onSuccess()
            }
            .addOnFailureListener { exception ->
                onFailure(exception)
            }
    }
}