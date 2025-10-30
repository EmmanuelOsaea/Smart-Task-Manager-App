package com.example.smarttask.sync

object SyncManager {
    // Placeholder: implement Firestore / REST sync logic here
    fun startSync() { /* schedule sync with WorkManager */ }
}

package com.example.smarttask.sync

import com.example.smarttask.data.TaskEntity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class SyncManager(private val firestore: FirebaseFirestore, private val repo: com.example.smarttask.data.TaskRepository) {

    private val auth get() = FirebaseAuth.getInstance()
    private fun userTasksRef(uid: String) = firestore.collection("users").document(uid).collection("tasks")

    // Push local changes to Firestore
    suspend fun pushLocalChanges() {
        val uid = auth.currentUser?.uid ?: return
        val local = repo.getAllNow() // you need a synchronous list or suspend DAO method to get current items
        val ref = userTasksRef(uid)

        for (task in local) {
            val docId = task.id.toString()
            val doc = ref.document(docId)
            val remoteSnap = doc.get().await()

            if (!remoteSnap.exists()) {
                // create remote
                doc.set(task.toMap()).await()
            } else {
                val remoteLast = remoteSnap.getLong("lastModified") ?: 0L
                // last-modified-wins: if local newer -> overwrite remote
                if (task.lastModified > remoteLast) {
                    doc.set(task.toMap()).await()
                } else {
                    // If remote newer, update local (handled in pull)
                }
            }
        }
    }

    // Pull remote changes and merge to local
    suspend fun pullRemoteChanges() {
        val uid = auth.currentUser?.uid ?: return
        val ref = userTasksRef(uid)
        val snapshots = ref.get().await()
        val remoteList = snapshots.documents.mapNotNull { doc ->
            doc.data?.let { TaskEntity.fromMap(it) } // implement fromMap
        }

        // Merge into local DB using lastModified
        for (remote in remoteList) {
            val local = repo.getByIdNow(remote.id) // provide repo suspend method
            if (local == null) {
                // create local
                repo.insert(remote)
            } else {
                if (remote.lastModified > local.lastModified) {
                    // remote more recent -> replace local
                    repo.update(remote)
                }
                // else local more recent -> already pushed in pushLocalChanges
            }
        }
    }

    // Full sync: push then pull (or pull then push depending on strategy)
    suspend fun syncOnce() {
        pushLocalChanges()
        pullRemoteChanges()
    }
}
