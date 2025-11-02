package com.example.contactapp2.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.contactapp2.data.Contact
import com.example.contactapp2.viewmodel.ContactViewModel

@Composable
fun ContactScreen(viewModel: ContactViewModel) {
    val contacts by viewModel.contacts.collectAsState()
    val name by viewModel.contactName.collectAsState()
    val phone by viewModel.contactPhone.collectAsState()
    val nameError by viewModel.nameError.collectAsState()
    val phoneError by viewModel.phoneError.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val sortAscending by viewModel.sortAscending.collectAsState()
    val showDialog by viewModel.showDeleteDialog.collectAsState()

    if (showDialog) {
        DeleteConfirmationDialog(
            onConfirm = { viewModel.deleteConfirmedContact() },
            onDismiss = { viewModel.hideDeleteConfirmation() }
        )
    }

    // 🩵 No TopAppBar — clean layout
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // --------------------
        // NAME FIELD
        // --------------------
        OutlinedTextField(
            value = name,
            onValueChange = viewModel::onNameChange,
            label = { Text("Name") },
            singleLine = true,
            isError = nameError != null,
            modifier = Modifier.fillMaxWidth()
        )
        if (nameError != null) {
            Text(
                text = nameError ?: "",
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // --------------------
        // PHONE FIELD
        // --------------------
        OutlinedTextField(
            value = phone,
            onValueChange = viewModel::onPhoneChange,
            label = { Text("Phone Number") },
            placeholder = { Text("999.999.9999") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
            singleLine = true,
            isError = phoneError != null,
            modifier = Modifier.fillMaxWidth()
        )
        if (phoneError != null) {
            Text(
                text = phoneError ?: "",
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // --------------------
        // ADD + SORT BUTTONS (All Blue)
        // --------------------
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            val buttonColor = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF1976D2), // Bright blue
                contentColor = Color.White
            )

            Button(
                onClick = viewModel::onAddContact,
                colors = buttonColor,
                modifier = Modifier
                    .weight(1f)
                    .height(50.dp)
            ) {
                Text("ADD")
            }

            Spacer(modifier = Modifier.width(8.dp))

            Button(
                onClick = { viewModel.setSortOrder(true) },
                colors = buttonColor,
                modifier = Modifier
                    .weight(1f)
                    .height(50.dp)
            ) {
                Text("Sort ASC")
            }

            Spacer(modifier = Modifier.width(8.dp))

            Button(
                onClick = { viewModel.setSortOrder(false) },
                colors = buttonColor,
                modifier = Modifier
                    .weight(1f)
                    .height(50.dp)
            ) {
                Text("Sort DESC")
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // --------------------
        // SEARCH FIELD
        // --------------------
        OutlinedTextField(
            value = searchQuery,
            onValueChange = viewModel::onSearchQueryChange,
            label = { Text("Search") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(20.dp))

        // --------------------
        // "CONTACTS" TITLE
        // --------------------
        Text(
            text = "Contacts",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(10.dp))

        // --------------------
        // CONTACT LIST
        // --------------------
        ContactList(
            contacts = contacts,
            onDeleteClick = { viewModel.showDeleteConfirmation(it) },
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
fun ContactList(
    contacts: List<Contact>,
    onDeleteClick: (Contact) -> Unit,
    modifier: Modifier = Modifier
) {
    if (contacts.isEmpty()) {
        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "No contacts yet!",
                color = Color.Gray,
                fontSize = 16.sp
            )
        }
    } else {
        LazyColumn(modifier = modifier.fillMaxWidth()) {
            items(contacts, key = { it.id }) { contact ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    elevation = CardDefaults.cardElevation(2.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = contact.name,
                                style = MaterialTheme.typography.titleMedium
                            )
                            Text(
                                text = contact.phoneNumber,
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color.Gray
                            )
                        }

                        IconButton(onClick = { onDeleteClick(contact) }) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Delete",
                                tint = MaterialTheme.colorScheme.error
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun DeleteConfirmationDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Confirm Deletion") },
        text = { Text("Are you sure you want to delete this contact?") },
        confirmButton = {
            TextButton(
                onClick = onConfirm,
                colors = ButtonDefaults.textButtonColors(
                    contentColor = MaterialTheme.colorScheme.error
                )
            ) {
                Text("DELETE")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("CANCEL")
            }
        }
    )
}
