package com.example.contactapp2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.contactapp2.ui.ContactScreen
import com.example.contactapp2.ui.theme.ContactApp2Theme
import com.example.contactapp2.viewmodel.ContactViewModel
import com.example.contactapp2.viewmodel.ContactViewModelFactory

class MainActivity : ComponentActivity() {

    
    private val viewModel: ContactViewModel by viewModels {
      
        ContactViewModelFactory((application as ContactsApplication).repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
           
            ContactApp2Theme {
                
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ContactScreen(viewModel = viewModel)
                }
            }
        }
    }
}
