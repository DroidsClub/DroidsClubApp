package com.example.androidclubapp.models

import android.content.Context
import androidx.lifecycle.*
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.TypeConverters
import com.example.androidclubapp.FavoritesAdapter
import com.example.androidclubapp.SearchViewAdapter
import java.time.LocalDateTime
import java.util.*
import com.example.androidclubapp.utils.GsonUtils
import com.example.androidclubapp.database.DatabaseService
import com.example.androidclubapp.utils.ioThread
import com.example.androidclubapp.utils.UserIdGeneration

class PokemonViewModel: ViewModel() {

    private val mutableSelectedPokemon = MutableLiveData<PokemonInfo>()
    private val mutableListPokemon = MutableLiveData<List<PokemonListItem>>()

    private val mutableFavorites = MutableLiveData<List<FavoritePokemon>>()

    private val mutableDatabaseService = MutableLiveData<DatabaseService>()
    private val mutableUserId = MutableLiveData<String>()
    private val mutableDatabaseCreated = MutableLiveData<Boolean>()

    val selectedPokemon: LiveData<PokemonInfo> get() = mutableSelectedPokemon
    val listPokemon: LiveData<List<PokemonListItem>> get() = mutableListPokemon
    val favoritePokemon: LiveData<List<FavoritePokemon>> get() = mutableFavorites
    val databaseService: LiveData<DatabaseService> get() = mutableDatabaseService
    val userId: LiveData<String> get() = mutableUserId
    val databaseCreated: LiveData<Boolean> get() = mutableDatabaseCreated

    fun initFavoritesList(): List<FavoritePokemon> = favoritePokemon.value ?: mutableListOf()

    fun selectPokemon(id: Int){
        mutableSelectedPokemon.value = PokemonInfo(id)
    }

    fun doSomethingWithPokemon(owner: LifecycleOwner, viewHolder: SearchViewAdapter.ViewHolder,
                               f:(p: List<PokemonListItem>, v: SearchViewAdapter.ViewHolder) -> Unit){
        listPokemon.observe(owner) {
            f(it, viewHolder)
        }
    }

    fun doSomethingWithFavorites(owner: LifecycleOwner, viewHolder: FavoritesAdapter.ViewHolder, f: (b: List<FavoritePokemon>, v: FavoritesAdapter.ViewHolder) -> Unit){

        favoritePokemon.observe(owner) { favorites ->
            f(favorites, viewHolder)
        }
    }

    fun removeFavoriteIfExistsForIndex(favIndex: Int) {

        val newList = initFavoritesList().filterIndexed { index, fav ->
            index != favIndex
        }

        mutableFavorites.value = newList
    }

    fun updateFavorite(favorite: FavoritePokemon){
        val newList = initFavoritesList().filterNot { it.id == favorite.id }

        val newListOfFavorites: List<FavoritePokemon> = newList.plus(favorite)

        mutableFavorites.value = newListOfFavorites.sortedBy { it.id }
    }

    fun removeFavoriteById(idToRemove: Int){
        val listWithRemovedFavorite = initFavoritesList().filterNot{ it.id == idToRemove}
        mutableFavorites.value = listWithRemovedFavorite
    }

    private fun allFavoritesIds(): List<Int> = initFavoritesList().map{ it.id }

    fun cycleFavorited(id: Int, context: Context){

        if(initFavoritesList().contains(FavoritePokemon(id))){
            removeFavoriteById(id)
        } else {
            saveFavorite(id, context)
        }
    }

    fun saveFavorite(favorite: Int, context: Context){

        val existingFavorite = initFavoritesList().find { it.id == favorite }

        if (existingFavorite != null) {
            removeFavoriteById(existingFavorite.id)

            val newList = initFavoritesList().filterNot { it.id == favorite }

            val newFavorite = FavoritePokemon(favorite)

            val newListOfFavorites: List<FavoritePokemon> = newList.plus(newFavorite)

            mutableFavorites.value = newListOfFavorites.sortedBy { it.id }

        } else {

            val newList = initFavoritesList().filterNot { it.id == favorite }

            val newFavorite = FavoritePokemon(favorite)

            val newListOfFavorites: List<FavoritePokemon> = newList.plus(newFavorite)

            mutableFavorites.value = newListOfFavorites.sortedBy { it.id }
        }

        println(mutableFavorites.value)
        saveUserDataToDatabase(initUserData(context))
    }

    private fun smallestUnusedIndex(ids: List<Int>): Int {
        var smallestIndex = 1

        if(ids.isEmpty()) return smallestIndex

        val sortedList = ids.sorted()

        if(sortedList[0] > 1) return smallestIndex
        if(sortedList[sortedList.size - 1] <= 0) return smallestIndex

        for (element in sortedList) {
            if(element == smallestIndex){
                smallestIndex++
            }
        }

        return smallestIndex
    }

    fun lastFavoriteId(): Int {
        return smallestUnusedIndex(allFavoritesIds())
    }

    fun setDatabaseService(context: Context, callback: (DatabaseService) -> Unit) = ioThread {
        val db = DatabaseService(context)
        mutableDatabaseService.postValue(db)
        callback(db)
    }

    fun generateUserId(context: Context, overrideMakeNewUserId: Boolean = false, async: Boolean = false): String {
        return initUserId(overrideMakeNewUserId,async){ UserIdGeneration().getOrGenerateUserId(context, mutableUserId.value, overrideMakeNewUserId) }
    }

    fun initUserId(overrideMakeNewUserId:Boolean, async: Boolean, f:() -> String): String = run {

        return if(mutableUserId.value != null && !overrideMakeNewUserId){
            mutableUserId.value!!
        } else {
            val newId = f()
            println("new user id = $newId")

            if(async){
                mutableUserId.postValue(newId)
            } else {
                mutableUserId.value = newId
            }

            println("new saved user id = ${mutableUserId.value}")
            newId
        }
    }

    fun dbCreated(created: Boolean){
        mutableDatabaseCreated.postValue(created)
    }


    fun swapToDifferentUserData(_userData: UserData?){

        val userData : UserData? = if(_userData != null){
            GsonUtils.gson.fromJson(GsonUtils.gson.toJson(_userData) ?: "{}", UserData::class.java)
        } else {
            null
        }

        if(userData != null){
            println("swapToDifferentUserData userdata is defined as ${userData.userId}")

            mutableFavorites.postValue(userData.favourites?.sortedBy {it.id})

        } else {
            println("swapToDifferentUserData userdata is null")
            mutableFavorites.postValue(emptyList())

        }
    }

    fun doSomethingAfterDBCreated(owner: LifecycleOwner, f: (b: Boolean) -> Unit){
        databaseCreated.observe(owner, { b ->
            f(b)
        })
    }

    fun initUserData(context: Context): UserData {
        return UserData(
            userId = generateUserId(context),
            updatedAt = LocalDateTime.now().toString(),
            favourites = initFavoritesList()
        )
    }

    fun saveUserDataToDatabase(userData: UserData){

        println("saving imported data to database")
        println("database: ${mutableDatabaseService.value}")

        val _userData = userData.copy(
            favourites = userData.favourites?.sortedBy { it.id }
        )

        println(_userData)

        if(mutableDatabaseService.value != null){
            ioThread {
                mutableDatabaseService.value!!.insertUserData(_userData)
            }
        }
    }
}

data class PokemonInfo(
    val id: Int
)

data class FavoritePokemon(
    val id: Int
)

class FavConverter: GsonUtils.GsonTypeConverters<FavoritePokemon>()

@Entity(primaryKeys = ["userId"])
data class UserData(
    @ColumnInfo(name = "userId") val userId: String,
    @ColumnInfo(name = "updatedAt") val updatedAt: String? = LocalDateTime.now().toString(),
    @TypeConverters(FavConverter::class)
    @ColumnInfo(name = "favourites") val favourites: List<FavoritePokemon>? = emptyList()
)