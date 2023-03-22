package com.mitron.asterapp

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Colors
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.mitron.asterapp.ui.theme.AsterAppTheme
import kotlinx.coroutines.delay
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
                val navController= rememberNavController()

                NavHost(navController = navController,
                    startDestination = Screens.SplashScreen.route){

                    //nav graph
                    composable(route=Screens.SplashScreen.route){
                        Surface(color = Color.White) {
                            SplashScreen()
                        }
                    }
                    composable(route=Screens.dataScreen.route) {
                        Surface(color = Color.DarkGray, modifier = Modifier.fillMaxSize()) {
                        displayPosts()
                        }
                    }

                }

                LaunchedEffect(key1 = true ){
                    delay(3000)
                    navController.popBackStack()
                    navController.navigate(Screens.dataScreen.route){
                        popUpTo(0)
                    }
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
    Column{
        TopAppBar(title = {Text(text="Aster App", color = Color.DarkGray, fontWeight = FontWeight.SemiBold)}, backgroundColor = Color.White, elevation = 4.dp)

        LazyColumn(modifier = Modifier.padding(12.dp)) {
            items(postsList) { post ->
                ExpandableCard(
                    title = post.title,
                    id = post.id.toString(),
                    userId = post.userId.toString(),
                    body = post.body
                )
                Spacer(modifier = Modifier.padding(12.dp))
            }
        }
    }
}


@Composable
fun SplashScreen() {
    Box(modifier= Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
        Image(painter = painterResource(id = R.drawable.aster),contentDescription = "App Logo", modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth())
    }

}