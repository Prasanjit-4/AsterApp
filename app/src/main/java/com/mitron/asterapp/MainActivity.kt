package com.mitron.asterapp

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Colors
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.TopAppBar
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mitron.asterapp.ui.theme.AsterAppTheme
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AsterAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color.DarkGray
                ) {

                    displayPosts()
                }
            }
        }
    }
}

fun getJsonData(postsList:MutableList<AsterResponseModel>,ct: Context){
    val retrofit=Retrofit.Builder()
        .baseUrl("https://jsonplaceholder.typicode.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val postsApi=retrofit.create(PostsApi::class.java)

    val call: Call<ArrayList<AsterResponseModel>> = postsApi.getPosts()

    call!!.enqueue(object : Callback<ArrayList<AsterResponseModel>?>{
        override fun onResponse(
            call: Call<ArrayList<AsterResponseModel>?>,
            response: Response<ArrayList<AsterResponseModel>?>
        ) {
            if(response.isSuccessful){
                var lst:ArrayList<AsterResponseModel> = ArrayList()
                lst = response.body()!!

                for(i in 0 until lst.size){
                    postsList.add(lst.get(i))
                }
            }
        }

        override fun onFailure(call: Call<ArrayList<AsterResponseModel>?>, t: Throwable) {

        }
    })
}

@Preview
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun displayPosts() {
    val context = LocalContext.current
    val postsList = remember{
        mutableStateListOf<AsterResponseModel>()
    }

    getJsonData(postsList,context)


    LazyColumn(modifier = Modifier.padding(12.dp)){
        items(postsList){post->
            ExpandableCard(title = post.title, id = post.id.toString(), userId =post.userId.toString() , body =post.body )
            Spacer(modifier = Modifier.padding(12.dp))
        }
    }
}

