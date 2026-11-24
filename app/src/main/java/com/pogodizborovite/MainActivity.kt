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
  Puzzle("ЗЕБРА","Африканско животно со црно-бели линии.","ЖИВОТНИ","https://images.unsplash.com/photo-1501706362039-c6e08d6c6f1b?w=900"),
  Puzzle("МАЈМУН","Животно што се качува по дрвја.","ЖИВОТНИ","https://images.unsplash.com/photo-1540573133985-87b6da6d54a9?w=900"),
  Puzzle("МЕЧКА","Големо шумско животно што оди на четири нозе.","ЖИВОТНИ","https://images.unsplash.com/photo-1530595467537-0b5996c41f2d?w=900"),
  Puzzle("ВОЛК","Диво животно слично на куче што живее во глутница.","ЖИВОТНИ","https://images.unsplash.com/photo-1564466809058-bf4114d55352?w=900"),
  Puzzle("ДЕЛФИН","Паметно морско животно што плива и скока.","ЖИВОТНИ","https://images.unsplash.com/photo-1607153333879-c174d265f1d2?w=900")
 )),
 GameCategory("ОВОШЈЕ И ХРАНА","🍎", listOf(
  Puzzle("ЈАБОЛКО","Црвено или зелено овошје што расте на дрво.","ОВОШЈЕ","https://images.unsplash.com/photo-1560806887-1e4cd0b6cbd6?w=900"),
  Puzzle("БАНАНА","Жолто овошје што го лупиме пред јадење.","ОВОШЈЕ","https://images.unsplash.com/photo-1571771894821-ce9b6c11b08e?w=900"),
  Puzzle("ПОРТОКАЛ","Тркалесто цитрусно овошје со портокалова кора.","ОВОШЈЕ","https://images.unsplash.com/photo-1547514701-42782101795e?w=900"),
  Puzzle("ЈАГОДА","Црвено овошје со мали семки на површината.","ОВОШЈЕ","https://images.unsplash.com/photo-1464965911861-746a04b4bca6?w=900"),
  Puzzle("ЛИМОН","Жолто кисело цитрусно овошје.","ОВОШЈЕ","https://images.unsplash.com/photo-1590502593747-42a996133562?w=900"),
  Puzzle("ЛУБЕНИЦА","Големо летно овошје со црвена сочна внатрешност.","ОВОШЈЕ","https://images.unsplash.com/photo-1563114773-84221bd62daa?w=900"),
  Puzzle("АНАНАС","Тропско овошје со тврда кора и зелени листови.","ОВОШЈЕ","https://images.unsplash.com/photo-1550258987-190a2d41a8ba?w=900"),
  Puzzle("МОРКОВ","Портокалов коренест зеленчук што го јадеме свеж или варен.","ХРАНА","https://images.unsplash.com/photo-1445282768818-728615cc910a?w=900")
 )),
 GameCategory("ПРИРОДА","🌳", listOf(
  Puzzle("ПЛАНИНА","Висока природна форма на земјиште.","ПРИРОДА","https://images.unsplash.com/photo-1464822759023-fed622ff2c3b?w=900"),
  Puzzle("СОНЦЕ","Ѕвезда што ја осветлува Земјата.","ПРИРОДА","https://images.unsplash.com/photo-1500530855697-b586d89ba3ee?w=900"),
  Puzzle("РЕКА","Природна вода што тече низ корито.","ПРИРОДА","https://images.unsplash.com/photo-1433086966358-54859d0ed716?w=900"),
  Puzzle("ВОДОПАД","Вода што паѓа од висока карпа или праг.","ПРИРОДА","https://images.unsplash.com/photo-1432405972618-c60b0225b8f9?w=900"),
  Puzzle("ЕЗЕРО","Голема површина со вода опкружена со копно.","ПРИРОДА","https://images.unsplash.com/photo-1470770841072-f978cf4d019e?w=900"),
  Puzzle("ПЕШТЕРА","Природна шуплина во карпа или планина.","ПРИРОДА","https://images.unsplash.com/photo-1500534623283-312aade485b7?w=900"),
  Puzzle("ВУЛКАН","Планина од која може да избива лава.","ПРИРОДА","https://images.unsplash.com/photo-1462331940025-496dfbfc7564?w=900"),
  Puzzle("ОСТРОВ","Копно опкружено со вода од сите страни.","ПРИРОДА","https://images.unsplash.com/photo-1501785888041-af3ef285b470?w=900")
 )),
 GameCategory("ПРЕВОЗ","🚗", listOf(
  Puzzle("АВТОМОБИЛ","Возило со четири тркала.","ПРЕВОЗ","https://images.unsplash.com/photo-1492144534655-ae79c964c9d7?w=900"),
  Puzzle("ВЕЛОСИПЕД","Превозно средство со две тркала и педали.","ПРЕВОЗ","https://images.unsplash.com/photo-1485965120184-e220f721d03e?w=900"),
  Puzzle("ВОЗ","Превозно средство што се движи по шини.","ПРЕВОЗ","https://images.unsplash.com/photo-1474487548417-781cb71495f3?w=900"),
  Puzzle("МОТОЦИКЛ","Моторно возило на две тркала.","ПРЕВОЗ","https://images.unsplash.com/photo-1558981806-ec527fa84c39?w=900"),
  Puzzle("ТРАМВАЈ","Градско превозно средство што се движи по шини.","ПРЕВОЗ","https://images.unsplash.com/photo-1519501025264-65ba15a82390?w=900"),
  Puzzle("ХЕЛИКОПТЕР","Летало со ротори што може да полетува вертикално.","ПРЕВОЗ","https://images.unsplash.com/photo-1474302770737-173ee21bab63?w=900"),
  Puzzle("ТРОТИНЕТ","Мало превозно средство на две тркала со платформа.","ПРЕВОЗ","https://images.unsplash.com/photo-1604868189265-219b5a8a1b6a?w=900"),
  Puzzle("КАМИОН","Големо моторно возило за превоз на товар.","ПРЕВОЗ","https://images.unsplash.com/photo-1601584115197-04ecc0da31d7?w=900")
 )),
 GameCategory("СПОРТ","⚽", listOf(
  Puzzle("ФУДБАЛ","Спорт што се игра со топка и нозе.","СПОРТ","https://images.unsplash.com/photo-1522778119026-d647f0596c20?w=900"),
  Puzzle("КОШАРКА","Спорт во кој топката се фрла во кош.","СПОРТ","https://images.unsplash.com/photo-1519861531473-9200262188bf?w=900"),
  Puzzle("ПЛИВАЊЕ","Спортска активност што се изведува во вода.","СПОРТ","https://images.unsplash.com/photo-1530549387789-4c1017266635?w=900"),
  Puzzle("ОДБОЈКА","Спорт во кој две екипи ја префрлаат топката преку мрежа.","СПОРТ","https://images.unsplash.com/photo-1612872087720-bb876e2e67d1?w=900"),
  Puzzle("СКИЈАЊЕ","Зимски спорт со лизгање по снег со скии.","СПОРТ","https://images.unsplash.com/photo-1517299321609-52687d1bc55a?w=900"),
  Puzzle("ВЕЛОСИПЕДИЗАМ","Спортско возење велосипед.","СПОРТ","https://images.unsplash.com/photo-1541625602330-2277e4c46182?w=900"),
  Puzzle("АТЛЕТИКА","Спорт со трчање, скокање и фрлање.","СПОРТ","https://images.unsplash.com/photo-1533561052604-c3beb6d55b8d?w=900"),
  Puzzle("ГИМНАСТИКА","Спорт со вежби за сила, рамнотежа и флексибилност.","СПОРТ","https://images.unsplash.com/photo-1518611012118-696072aa579a?w=900")
 )),
 GameCategory("МЕСТА","🏙️", listOf(
  Puzzle("УЧИЛИШТЕ","Место каде што децата учат.","МЕСТА","https://images.unsplash.com/photo-1580582932707-520aed937b7b?w=900"),
  Puzzle("БИБЛИОТЕКА","Место каде што се позајмуваат книги.","МЕСТА","https://images.unsplash.com/photo-1507842217343-583bb7270b66?w=900"),
  Puzzle("БОЛНИЦА","Место каде што лекарите лекуваат болни луѓе.","МЕСТА","https://images.unsplash.com/photo-1586773860418-d37222d8fce3?w=900"),
  Puzzle("ТЕАТАР","Зграда каде што се изведуваат претстави.","МЕСТА","https://images.unsplash.com/photo-1503095396549-807759245b35?w=900"),
  Puzzle("СТАДИОН","Голем простор каде што се одржуваат спортски натпревари.","МЕСТА","https://images.unsplash.com/photo-1508098682722-e99c43a406b2?w=900"),
  Puzzle("АЕРОДРОМ","Место каде што полетуваат и слетуваат авиони.","МЕСТА","https://images.unsplash.com/photo-1436491865332-7a61a109cc05?w=900"),
  Puzzle("ЖЕЛЕЗНИЧКА СТАНИЦА","Место каде што запираат и тргнуваат возови.","МЕСТА","https://images.unsplash.com/photo-1474487548417-781cb71495f3?w=900"),
  Puzzle("ПАЗАР","Место каде што луѓето купуваат свежа храна и други производи.","МЕСТА","https://images.unsplash.com/photo-1488459716781-31db52582fe9?w=900")
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
