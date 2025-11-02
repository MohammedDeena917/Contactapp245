package com.example.contactapp2.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@androidx.room.Dao
interface ContactDao {
    @androidx.room.Insert(onConflict = androidx.room.OnConflictStrategy.REPLACE)
    suspend fun insertContact(contact: Contact)

    @androidx.room.Delete
    suspend fun deleteContact(contact: Contact)

    @androidx.room.Query("SELECT * FROM contacts ORDER BY name ASC")
    fun getContactsOrderedByName(): Flow<List<Contact>>

    @androidx.room.Query("SELECT * FROM contacts ORDER BY name DESC")
    fun getContactsOrderedByNameDesc(): Flow<List<Contact>>
}
