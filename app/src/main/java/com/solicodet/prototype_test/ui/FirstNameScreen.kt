package com.solicodet.prototype_test.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.*


val persons = mutableStateListOf<Person>()

@Composable
fun FirstNameScreen(){
    var Name by rememberSaveable { mutableStateOf("") }

    Column (
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .padding(top = 30.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ){
        Row (horizontalArrangement = Arrangement.spacedBy(8.dp)) {

            TextField(
                value = Name,
                onValueChange = { Name = it},
                label = { Text("Enter first name") },
                modifier = Modifier.weight(1f)
            )
            Button(onClick = {
                if (Name.isNotBlank()){
                    persons.add(Person(Name))
                    Name = "" // clear the input
                }
            } ) {
                Text("Add Person")
            }
        }

        persons.forEach { p ->
            Text(
                "\uD83D\uDC64 ${p.name}",
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}