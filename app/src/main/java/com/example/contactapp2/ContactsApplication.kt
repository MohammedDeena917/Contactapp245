package com.example.contactapp2

import android.app.Application
// >>> PERFECT CHANGE: Yahan se galat privacysandbox wala import hata diya gaya hai <<<
import androidx.room.Room
import com.example.contactapp2.data.ContactDatabase
import com.example.contactapp2.repository.ContactRepository

class ContactsApplication : Application() {
    private val database by lazy {
        Room.databaseBuilder(
            this,
            ContactDatabase::class.java,
            "contact_db"
        ).build()
    }

    val repository by lazy { ContactRepository(database.contactDao()) }
}
