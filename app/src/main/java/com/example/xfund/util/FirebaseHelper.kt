package com.example.xfund.util

import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.xfund.model.CommunityDiscussion
import com.example.xfund.model.Project
import com.example.xfund.model.User
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.Calendar
import java.util.Date
import java.util.UUID

class FirebaseHelper {
    // Variables
    private val auth = FirebaseAuth.getInstance()
    private val storage = FirebaseStorage.getInstance()
    private val firestore = FirebaseFirestore.getInstance()


    // Coroutines Functions
    // User-Related Coroutines
    suspend fun logInUser(email: String, password: String): AuthResult? {
        return withContext(Dispatchers.IO) {
            try {
                val result = auth.signInWithEmailAndPassword(email, password).await()
                result.let { authResult ->
                    authResult
                }
            } catch (e: Exception) {
                null // Handle login errors gracefully
            }
        }
    }

    suspend fun fetchUserDetails(uid: String): User? {
        return withContext(Dispatchers.IO) {
            try {
                val documentSnapshot = firestore.collection("users").document(uid).get().await()
                if (documentSnapshot.exists()) {
                    val user = documentSnapshot.toObject(User::class.java)
                    user // Return the user object
                } else {
                    null // User document doesn't exist
                }
            } catch (e: Exception) {
                null // Handle exceptions and errors gracefully
            }
        }
    }

    suspend fun getAuthorName(authorUid: String): String {
        return withContext(Dispatchers.IO) {
            try {
                val userDoc = firestore.collection("users").document(authorUid).get().await()
                if (userDoc.exists()) {
                    return@withContext userDoc.getString("displayName") ?: "Author"
                } else {
                    return@withContext "N/A"
                }
            } catch (e: Exception) {
                // Handle exceptions
                return@withContext ""
            }
        }
    }

    suspend fun uploadUserImage(imageUri: Uri): String? {
        return withContext(Dispatchers.IO) {
            try {
                val currentUser = auth.currentUser
                if (currentUser != null) {
                    val imageFileName = currentUser.uid.toString()
                    val imageRef = storage.reference.child("images/users/$imageFileName")

                    // Upload the image to Firebase Storage
                    val uploadTask = imageRef.putFile(imageUri)
                    uploadTask.await()

                    // Get the download URL of the uploaded image
                    val downloadUrl = imageRef.downloadUrl.await()

                    // Store the download URL in Firestore
                    val imageUrl = downloadUrl.toString()
                    val userId = currentUser.uid
                    val imageDoc = firestore.collection("users").document(userId)
                    imageDoc.update("imageUrl", imageUrl).await()

                    imageUrl
                } else {
                    null // User not authenticated
                }
            } catch (e: Exception) {
                null // Handle exceptions
            }
        }
    }


    // Discussion-Related Coroutines
    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun getAllDiscussions(): List<CommunityDiscussion> {
        return withContext(Dispatchers.IO) {
            try {
                val querySnapshot = firestore.collection("discussions")
                    .orderBy("createdOn", Query.Direction.DESCENDING)
                    .get()
                    .await()

                val discussions = createCommunityDiscussions(querySnapshot)
                discussions
            } catch (e: Exception) {
                // Handle exceptions
                emptyList()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun fetchPostedDiscussions(): List<Map<String, CommunityDiscussion>> {
        val currentUser = FirebaseAuth.getInstance().currentUser
        return withContext(Dispatchers.IO) {
            try {
                if (currentUser != null) {
                    val userId = currentUser.uid
                    val querySnapshot = firestore.collection("discussions")
                        .whereEqualTo("author", userId)
                        //.orderBy("createdOn", Query.Direction.DESCENDING)
                        .get()
                        .await()

                    var discussions : List<Map<String, CommunityDiscussion>> = createCommunityDiscussionMaps(querySnapshot)

                    discussions
                } else {
                    emptyList()
                }
            } catch (e: Exception) {
                emptyList() // Handle errors gracefully
            }
        }
    }

    suspend fun updateDiscussion(discussionId: String, updatedDiscussion: List<Any>): Int {
        return withContext(Dispatchers.IO) {
            try {
                val user = auth.currentUser
                if (user != null) {
                    // Check if the discussion belongs to the current user
                    val discussionRef = firestore.collection("discussions").document(discussionId)
                    val discussionSnapshot = discussionRef.get().await()
                    val userId = discussionSnapshot.getString("author")


                    Log.d("Author TAK SAMA Ke???", (userId == user.uid).toString())

                    if (userId == user.uid) {
                        Log.d("Mana DISCUSSION ID!!", discussionId)
                        // Update the discussion content
                        discussionRef.update("title", updatedDiscussion[0]).await()
                        discussionRef.update("desc", updatedDiscussion[1]).await()
                        discussionRef.update("tags", updatedDiscussion[2]).await()
                        0 // Update successful
                    } else {
                        1 // Discussion doesn't belong to the current user
                    }
                } else {
                    2 // User not authenticated
                }
            } catch (e: Exception) {
                3 // Handle exceptions
                Log.d("APA HALLLL", e.toString())
            }
        }
    }

    suspend fun deleteDiscussion(discussionId: String): Int{
        val currentUser = auth.currentUser
        return withContext(Dispatchers.IO) {
            try {
                if (currentUser != null) {
                    // Check if the discussion belongs to the current user
                    val discussionRef = firestore.collection("discussions").document(discussionId)
                    val discussionSnapshot = discussionRef.get().await()
                    val userId = discussionSnapshot.getString("author")

                    if (userId == currentUser.uid) {
                        // Delete the discussion
                        discussionRef.delete().await()
                        0 // Deletion successful
                    } else {
                        1 // Discussion doesn't belong to the current user
                    }
                } else {
                    2 // User not authenticated
                }
            } catch (e: Exception) {
                3 // Handle exceptions
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun createCommunityDiscussions(querySnapshot: QuerySnapshot): List<CommunityDiscussion> {
        val communityDiscussions = mutableListOf<CommunityDiscussion>()

        for (document in querySnapshot.documents) {
            val author = document.getString("author") ?: ""
            val title = document.getString("title") ?: ""
            val desc = document.getString("desc") ?: ""
            val tags = document.get("tags") as? ArrayList<String> ?: emptyList()
            val timestamp = document.getTimestamp("createdOn")

            val createdOn = if (timestamp != null) {
                timestamp.toDate().toInstant().atZone(ZoneOffset.UTC).toLocalDateTime()
            } else {
                LocalDateTime.now()
            }

            val communityDiscussion = CommunityDiscussion(author, title, desc, tags, createdOn)
            communityDiscussions.add(communityDiscussion)
        }

        return communityDiscussions.toList()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun createCommunityDiscussionMaps(querySnapshot: QuerySnapshot): List<Map<String, CommunityDiscussion>> {
        val communityDiscussions = mutableListOf<Map<String, CommunityDiscussion>>()

        for (document in querySnapshot.documents) {
            val author = document.getString("author") ?: ""
            val title = document.getString("title") ?: ""
            val desc = document.getString("desc") ?: ""
            val tags = document.get("tags") as? ArrayList<String> ?: emptyList()
            val timestamp = document.getTimestamp("createdOn")

            val createdOn = if (timestamp != null) {
                timestamp.toDate().toInstant().atZone(ZoneOffset.UTC).toLocalDateTime()
            } else {
                LocalDateTime.now()
            }

            val communityDiscussion =  mapOf(document.id to CommunityDiscussion(author, title, desc, tags, createdOn))
            communityDiscussions.add(communityDiscussion)
        }

        return communityDiscussions.toList()
    }



    // Project-Related Coroutines
    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun getAllProjects(): List<Project> {
        return withContext(Dispatchers.IO) {
            try {
                val querySnapshot = firestore.collection("projects")
                    .get()
                    .await()

                val projects = createProject(querySnapshot)
                projects
            } catch (e: Exception) {
                // Handle exceptions
                emptyList()
            }
        }
    }

    private fun createProject(querySnapshot: QuerySnapshot): List<Project> {
        val adminProjects = mutableListOf<Project>()

        for (document in querySnapshot.documents) {
            val cover = document.getString("cover") ?: ""
            val name = document.getString("name") ?: ""
            val description = document.getString("description") ?: ""
            val startLocalDateTime = document.getDate("start_date")
            val endLocalDateTime = document.getDate("end_date")
            val fund_collected = document.getDouble("fund_collected") ?: 0.0
            val fund_target = document.getDouble("fund_target") ?: 0.0

            // Convert Date objects to LocalDateTime using Calendar
            val startCalendar = Calendar.getInstance()
            startCalendar.time = startLocalDateTime ?: Date(0) // Use some default date if start_date is null
            val start_date = LocalDateTime.of(
                startCalendar.get(Calendar.YEAR),
                startCalendar.get(Calendar.MONTH) + 1,
                startCalendar.get(Calendar.DAY_OF_MONTH),
                startCalendar.get(Calendar.HOUR_OF_DAY),
                startCalendar.get(Calendar.MINUTE)
            )

            val endCalendar = Calendar.getInstance()
            endCalendar.time = endLocalDateTime ?: Date(0) // Use some default date if end_date is null
            val end_date = LocalDateTime.of(
                endCalendar.get(Calendar.YEAR),
                endCalendar.get(Calendar.MONTH) + 1,
                endCalendar.get(Calendar.DAY_OF_MONTH),
                endCalendar.get(Calendar.HOUR_OF_DAY),
                endCalendar.get(Calendar.MINUTE)
            )

            val adminProject = Project(cover, name, description, start_date, end_date, fund_collected, fund_target)
            adminProjects.add(adminProject)
        }

        return adminProjects.toList()
    }

}