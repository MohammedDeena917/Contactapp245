package com.example.contactapp2.repository

import com.example.contactapp2.data.Contact
import com.example.contactapp2.data.ContactDao
import kotlinx.coroutines.flow.Flow

class ContactRepository(private val contactDao: ContactDao) {
    suspend fun insertContact(contact: Contact) {
        contactDao.insertContact(contact)
    }

    suspend fun deleteContact(contact: Contact) {
        contactDao.deleteContact(contact)
    }

    fun getContactsOrderedByName(): Flow<List<Contact>> {
        return contactDao.getContactsOrderedByName()
    }

    fun getContactsOrderedByNameDesc(): Flow<List<Contact>> {
        return contactDao.getContactsOrderedByNameDesc()
    }
}
