package com.shakilpatel.techsavvy.ui.discussion

import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DiscussionViewModel @Inject constructor(
    private val db : FirebaseFirestore
) :ViewModel(){


}