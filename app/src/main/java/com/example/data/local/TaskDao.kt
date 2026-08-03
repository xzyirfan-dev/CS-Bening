package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {
    @Query("SELECT * FROM checklist_tasks ORDER BY monthYearStr DESC, dayNum ASC, id ASC")
    fun getAllTasks(): Flow<List<ChecklistTaskEntity>>

    @Query("SELECT * FROM checklist_tasks WHERE monthYearStr = :monthYear ORDER BY dayNum ASC, id ASC")
    fun getTasksByMonth(monthYear: String): Flow<List<ChecklistTaskEntity>>

    @Query("SELECT * FROM checklist_tasks WHERE dayDateStr LIKE :dayFilter ORDER BY id ASC")
    fun getTasksByDay(dayFilter: String): Flow<List<ChecklistTaskEntity>>

    @Query("SELECT * FROM checklist_tasks WHERE id = :taskId LIMIT 1")
    suspend fun getTaskById(taskId: Long): ChecklistTaskEntity?

    @Query("SELECT * FROM checklist_tasks WHERE isSynced = 0")
    suspend fun getUnsyncedTasks(): List<ChecklistTaskEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTasks(tasks: List<ChecklistTaskEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: ChecklistTaskEntity): Long

    @Update
    suspend fun updateTask(task: ChecklistTaskEntity)

    @Query("UPDATE checklist_tasks SET isSynced = 1 WHERE isSynced = 0")
    suspend fun markAllSynced()

    @Query("DELETE FROM checklist_tasks")
    suspend fun clearAll()
}

@Dao
interface ActivityDao {
    @Query("SELECT * FROM activity_logs ORDER BY timestampMs DESC LIMIT 50")
    fun getRecentLogs(): Flow<List<ActivityLogEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLog(log: ActivityLogEntity)
}

@Dao
interface NotificationDao {
    @Query("SELECT * FROM notifications ORDER BY id DESC")
    fun getAllNotifications(): Flow<List<NotificationEntity>>

    @Query("SELECT * FROM notifications WHERE recipientRole = :role OR recipientRole = 'ALL' ORDER BY id DESC")
    fun getNotificationsForRole(role: String): Flow<List<NotificationEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNotification(notification: NotificationEntity)

    @Query("UPDATE notifications SET isRead = 1 WHERE id = :id")
    suspend fun markAsRead(id: Long)

    @Query("UPDATE notifications SET isRead = 1 WHERE recipientRole = :role OR recipientRole = 'ALL'")
    suspend fun markAllAsReadForRole(role: String)
}

@Dao
interface UserDao {
    @Query("SELECT * FROM users")
    fun getAllUsers(): Flow<List<UserEntity>>

    @Query("SELECT * FROM users WHERE username = :username LIMIT 1")
    suspend fun getUserByUsername(username: String): UserEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUsers(users: List<UserEntity>)
}
