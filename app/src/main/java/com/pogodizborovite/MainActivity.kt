package com.pogodizborovite
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import java.util.Locale
import kotlinx.coroutines.delay

data class Puzzle(val word:String,val clue:String,val category:String,val photo:String)
data class GameCategory(val title:String,val emoji:String,val puzzles:List<Puzzle>)
val categories = listOf(
 GameCategory("ЖИВОТНИ","🐾", listOf(
  Puzzle("КУЧЕ","Домашно животно што лае.","ЖИВОТНИ","https://images.unsplash.com/photo-1552053831-71594a27632d?w=900"),
  Puzzle("МАЧКА","Домашно животно што мјаука.","ЖИВОТНИ","https://images.unsplash.com/photo-1514888286974-6c03e2ca1dba?w=900"),
  Puzzle("ЛАВ","Голема дива мачка што живее во Африка.","ЖИВОТНИ","https://images.unsplash.com/photo-1516026672322-bc52d61a55d5?w=900"),
  Puzzle("ЖИРАФА","Животно со многу долг врат.","ЖИВОТНИ","https://images.unsplash.com/photo-1547721064-da6cfb341d50?w=900")
 )),
 GameCategory("ОВОШЈЕ И ХРАНА","🍎", listOf(
  Puzzle("ЈАБОЛКО","Црвено или зелено овошје што расте на дрво.","ОВОШЈЕ","https://images.unsplash.com/photo-1560806887-1e4cd0b6cbd6?w=900"),
  Puzzle("БАНАНА","Жолто овошје што го лупиме пред јадење.","ОВОШЈЕ","https://images.unsplash.com/photo-1571771894821-ce9b6c11b08e?w=900"),
  Puzzle("ПОРТОКАЛ","Тркалесто цитрусно овошје со портокалова кора.","ОВОШЈЕ","https://images.unsplash.com/photo-1547514701-42782101795e?w=900"),
  Puzzle("ГРОЗЈЕ","Мали плодови што растат во грозд.","ОВОШЈЕ","https://images.unsplash.com/photo-1537640538966-79f369143f8f?w=900")
 )),
 GameCategory("ПРИРОДА","🌳", listOf(
  Puzzle("ПЛАНИНА","Висока природна форма на земјиште.","ПРИРОДА","https://images.unsplash.com/photo-1464822759023-fed622ff2c3b?w=900"),
  Puzzle("СОНЦЕ","Ѕвезда што ја осветлува Земјата.","ПРИРОДА","https://images.unsplash.com/photo-1500530855697-b586d89ba3ee?w=900"),
  Puzzle("РЕКА","Природна вода што тече низ корито.","ПРИРОДА","https://images.unsplash.com/photo-1433086966358-54859d0ed716?w=900"),
  Puzzle("ШУМА","Голема површина покриена со многу дрвја.","ПРИРОДА","https://images.unsplash.com/photo-144森林?w=900")
 )),
 GameCategory("ПРЕВОЗ","🚗", listOf(
  Puzzle("АВТОМОБИЛ","Возило со четири тркала.","ПРЕВОЗ","https://images.unsplash.com/photo-1492144534655-ae79c964c9d7?w=900"),
  Puzzle("ВЕЛОСИПЕД","Превозно средство со две тркала и педали.","ПРЕВОЗ","https://images.unsplash.com/photo-1485965120184-e220f721d03e?w=900"),
  Puzzle("ВОЗ","Превозно средство што се движи по шини.","ПРЕВОЗ","https://images.unsplash.com/photo-1474487548417-781cb71495f3?w=900"),
  Puzzle("БРОД","Големо превозно средство што плови по вода.","ПРЕВОЗ","https://images.unsplash.com/photo-1500375592092-40eb2168fd21?w=900")
 )),
 GameCategory("СПОРТ","⚽", listOf(
  Puzzle("ФУДБАЛ","Спорт што се игра со топка и нозе.","СПОРТ","https://images.unsplash.com/photo-1522778119026-d647f0596c20?w=900"),
  Puzzle("КОШАРКА","Спорт во кој топката се фрла во кош.","СПОРТ","https://images.unsplash.com/photo-1519861531473-9200262188bf?w=900"),
  Puzzle("ПЛИВАЊЕ","Спортска активност што се изведува во вода.","СПОРТ","https://images.unsplash.com/photo-1530549387789-4c1017266635?w=900"),
  Puzzle("БОКС","Боречки спорт со ракавици.","СПОРТ","https://images.unsplash.com/photo-1549719386-74dfcbf7dbed?w=900")
 )),
 GameCategory("МЕСТА","🏙️", listOf(
  Puzzle("УЧИЛИШТЕ","Место каде што децата учат.","МЕСТА","https://images.unsplash.com/photo-1580582932707-520aed937b7b?w=900"),
  Puzzle("БИБЛИОТЕКА","Место каде што се позајмуваат книги.","МЕСТА","https://images.unsplash.com/photo-1507842217343-583bb7270b66?w=900"),
  Puzzle("БОЛНИЦА","Место каде што лекарите лекуваат болни луѓе.","МЕСТА","https://images.unsplash.com/photo-1586773860418-d37222d8fce3?w=900"),
  Puzzle("МУЗЕЈ","Место каде што се изложуваат историски и уметнички предмети.","МЕСТА","https://images.unsplash.com/photo-1565060169194-19fabf63012e?w=900")
 ))
)
fun normalizeAnswer(value:String):String {
 var s=value.trim().lowercase(Locale.ROOT).replace("dzh","џ").replace("dž","џ").replace("gj","ѓ").replace("zh","ж").replace("dz","ѕ").replace("lj","љ").replace("nj","њ").replace("kj","ќ").replace("ch","ч").replace("sh","ш")
 val latin=mapOf('a' to 'а','b' to 'б','v' to 'в','g' to 'г','d' to 'д','e' to 'е','z' to 'з','i' to 'и','j' to 'ј','k' to 'к','l' to 'л','m' to 'м','n' to 'н','o' to 'о','p' to 'п','r' to 'р','s' to 'с','t' to 'т','u' to 'у','f' to 'ф','h' to 'х','c' to 'ц')
 s=s.map { latin[it] ?: it }.joinToString("")
 return s.uppercase(Locale.ROOT).filter { it.isLetter() }
}
class MainActivity:ComponentActivity(){override fun onCreate(savedInstanceState:Bundle?){super.onCreate(savedInstanceState);setContent{GameScreen()}}}
@Composable fun GameScreen(){
 var selected by remember { mutableIntStateOf(-1) }; var level by remember { mutableIntStateOf(0) }
 var answer by remember { mutableStateOf("") }; var score by remember { mutableIntStateOf(0) }
 var solved by remember { mutableStateOf(false) }; var hint by remember { mutableStateOf(false) }
 var message by remember { mutableStateOf("Избери категорија за да започнеш!") }
 LaunchedEffect(solved) { if (solved && selected >= 0) { delay(900); val cat=categories[selected]; if (level+1 < cat.puzzles.size) { level++; message="Внеси го точниот збор!" } else { selected=-1; message="Категоријата е завршена! Браво! 🎉" }; answer=""; solved=false; hint=false } }
 MaterialTheme(colorScheme=darkColorScheme(primary=Color(0xFF8DE1D2))){
 Column(Modifier.fillMaxSize().background(Color(0xFF101B3D)).verticalScroll(rememberScrollState()).padding(18.dp),horizontalAlignment=Alignment.CenterHorizontally,verticalArrangement=Arrangement.spacedBy(14.dp)){
 Text("🧩 ПОГОДИ ГИ ЗБОРОВИТЕ",color=Color.White,fontSize=24.sp,fontWeight=FontWeight.ExtraBold,textAlign=TextAlign.Center)
 Text("Македонска игра со зборови",color=Color(0xFFB8C8EA))
 Text("ПОЕНИ: "+score,color=Color(0xFFFFD782),fontWeight=FontWeight.Bold)
 if(selected<0){
  Text("ИЗБЕРИ КАТЕГОРИЈА",color=Color.White,fontSize=20.sp,fontWeight=FontWeight.Bold)
  categories.forEachIndexed { i,cat ->
   Card(onClick={selected=i;level=0;answer="";solved=false;hint=false;message="Внеси го точниот збор!"},colors=CardDefaults.cardColors(containerColor=Color(0xFF24365F)),shape=RoundedCornerShape(18.dp),modifier=Modifier.fillMaxWidth()){
    Row(Modifier.padding(18.dp),verticalAlignment=Alignment.CenterVertically,horizontalArrangement=Arrangement.spacedBy(14.dp)){
     Text(cat.emoji,fontSize=32.sp)
     Column(Modifier.weight(1f)){Text(cat.title,color=Color.White,fontWeight=FontWeight.Bold,fontSize=18.sp);Text(cat.puzzles.size.toString()+" зборови со фотографии",color=Color(0xFFB8C8EA),fontSize=13.sp)}
     Text("›",color=Color(0xFF8DE1D2),fontSize=28.sp)
    }
   }
  }
 }else{
  val cat=categories[selected]; val p=cat.puzzles[level % cat.puzzles.size]
  Row(Modifier.fillMaxWidth(),horizontalArrangement=Arrangement.SpaceBetween,verticalAlignment=Alignment.CenterVertically){
   TextButton(onClick={selected=-1;answer="";solved=false;hint=false}){Text("← КАТЕГОРИИ")}
   Text("НИВО "+(level+1)+"/"+cat.puzzles.size,color=Color(0xFFFFD782),fontWeight=FontWeight.Bold)
  }
  Text(cat.title,color=Color(0xFF8DE1D2),fontWeight=FontWeight.Bold)
  Card(colors=CardDefaults.cardColors(containerColor=Color(0xFF24365F)),shape=RoundedCornerShape(22.dp),modifier=Modifier.fillMaxWidth()){
   Column(Modifier.padding(16.dp),horizontalAlignment=Alignment.CenterHorizontally,verticalArrangement=Arrangement.spacedBy(12.dp)){
    AsyncImage(model=p.photo,contentDescription=p.category,modifier=Modifier.fillMaxWidth().height(190.dp),contentScale=ContentScale.Crop)
    Text("КОЈ ЗБОР Е ОВА?",color=Color.White,fontSize=21.sp,fontWeight=FontWeight.Bold)
    Text(p.clue,color=Color.White,fontSize=17.sp,textAlign=TextAlign.Center)
    Text("БРОЈ НА БУКВИ: "+p.word.length,color=Color(0xFFFFD782),fontWeight=FontWeight.Bold)
    OutlinedTextField(value=answer,onValueChange={answer=it.uppercase()},label={Text("Одговор: кирилица или латиница")},enabled=!solved,singleLine=true)
    Button(onClick={if(normalizeAnswer(answer)==normalizeAnswer(p.word)){score+=if(hint)5 else 10;solved=true;message="БРАВО! ТОЧЕН ОДГОВОР! 🎉"}else{message="Не е точно. Обиди се повторно!"}},enabled=!solved,modifier=Modifier.fillMaxWidth()){Text("ПРОВЕРИ")}
    OutlinedButton(onClick={hint=true;message="ПРВА БУКВА: "+p.word.first()},enabled=!solved&&!hint){Text("💡 ПОМОШ")}
    Text(message,color=Color(0xFFFFD782),textAlign=TextAlign.Center)
    if(solved) Text("СЛЕДНОТО НИВО СЕ ОТВОРА АВТОМАТСКИ…",color=Color(0xFF8DE1D2),fontWeight=FontWeight.Bold,textAlign=TextAlign.Center)
   }
  }
 }
 Text("Погодувај, учи и забавувај се!",color=Color(0xFFB8C8EA),fontSize=13.sp)
 }
 }
}
