package com.example.contactapp2.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.contactapp2.data.Contact
import com.example.contactapp2.repository.ContactRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class ContactViewModel(private val repository: ContactRepository) : ViewModel() {

    private val _sortAscending = MutableStateFlow(true)
    private val _searchQuery = MutableStateFlow("")
    private val _contactName = MutableStateFlow("")
    private val _contactPhone = MutableStateFlow("")
    private val _nameError = MutableStateFlow<String?>(null)
    private val _phoneError = MutableStateFlow<String?>(null)
    private val _showDeleteDialog = MutableStateFlow(false)
    private val _contactToDelete = MutableStateFlow<Contact?>(null)

    val sortAscending: StateFlow<Boolean> = _sortAscending
    val searchQuery: StateFlow<String> = _searchQuery
    val contactName: StateFlow<String> = _contactName
    val contactPhone: StateFlow<String> = _contactPhone
    val nameError: StateFlow<String?> = _nameError
    val phoneError: StateFlow<String?> = _phoneError
    val showDeleteDialog: StateFlow<Boolean> = _showDeleteDialog

    val contacts: StateFlow<List<Contact>> = _sortAscending
        .flatMapLatest { isAsc -> if (isAsc) repository.getContactsOrderedByName() else repository.getContactsOrderedByNameDesc() }
        .combine(_searchQuery) { contacts, query ->
            if (query.isBlank()) {
                contacts
            } else {
                contacts.filter { it.name.contains(query, ignoreCase = true) }
            }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun onNameChange(name: String) {
        _contactName.value = name
        if (name.isNotBlank()) _nameError.value = null
    }

    fun onPhoneChange(phone: String) {
        _contactPhone.value = phone
        if (validatePhoneNumber(phone)) _phoneError.value = null
    }

    fun onSearchQueryChange(query: String) { _searchQuery.value = query }

    fun setSortOrder(ascending: Boolean) { _sortAscending.value = ascending }

    fun onAddContact() {
        val name = _contactName.value.trim()
        val phone = _contactPhone.value.trim()

        val isNameValid = name.isNotBlank()
        _nameError.value = if (isNameValid) null else "Name cannot be blank"

        val isPhoneValid = validatePhoneNumber(phone)
        _phoneError.value = if (isPhoneValid) null else "Invalid format (e.g., 999.999.9999)"

        if (isNameValid && isPhoneValid) {
            viewModelScope.launch {
                repository.insertContact(Contact(name = name, phoneNumber = phone))
                _contactName.value = ""
                _contactPhone.value = ""
            }
        }
    }

    fun showDeleteConfirmation(contact: Contact) {
        _contactToDelete.value = contact
        _showDeleteDialog.value = true
    }

    fun hideDeleteConfirmation() {
        _contactToDelete.value = null
        _showDeleteDialog.value = false
    }

    fun deleteConfirmedContact() {
        _contactToDelete.value?.let { contact ->
            viewModelScope.launch {
                repository.deleteContact(contact)
                hideDeleteConfirmation()
            }
        }
    }

    private fun validatePhoneNumber(phone: String): Boolean {
        return phone.matches("""^\d{3}\.\d{3}\.\d{4}$""".toRegex())
    }
}
