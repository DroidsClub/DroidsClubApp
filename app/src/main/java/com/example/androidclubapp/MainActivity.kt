package com.example.androidclubapp

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.ui.onNavDestinationSelected
import com.example.androidclubapp.models.PokemonViewModel
import androidx.activity.viewModels

class MainActivity : AppCompatActivity() {

    private var out: String? = null
    private val results: Bundle? = null

    private val viewModel: PokemonViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.setDatabaseService(applicationContext){
            val userId = viewModel.generateUserId(applicationContext, async = true)

            it.getLatestUsersBoxesAsync(userId){ data ->

                viewModel.swapToDifferentUserData(data)
                viewModel.dbCreated(true)
            }
        }

        viewModel.doSomethingAfterDBCreated(this,::afterDBCreated)

       // setContentView(R.layout.activity_main)
       // setSupportActionBar(findViewById(R.id.toolbar))
    }

    fun afterDBCreated(dbCreated: Boolean){
        if(dbCreated) {
            setContentView(R.layout.activity_main)
            setSupportActionBar(findViewById(R.id.toolbar))
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
//        return when (item.itemId) {
//            R.id.action_settings -> true
//            R.id.action_Search -> true
//            else -> super.onOptionsItemSelected(item)
//        }


        val navController = findNavController(R.id.PokeSearchFragment)
        return item.onNavDestinationSelected(navController) || super.onOptionsItemSelected(item)
    }
}