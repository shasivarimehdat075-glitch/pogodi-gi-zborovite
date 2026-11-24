package com.pogodizborovite
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.util.Locale

data class Puzzle(val word:String,val clue:String,val category:String)
val puzzles = listOf(
 Puzzle("ЈАБОЛКО","Црвено или зелено овошје што расте на дрво.","ОВОШЈЕ"),
 Puzzle("КУЧЕ","Домашно животно што лае.","ЖИВОТНИ"),
 Puzzle("УЧИЛИШТЕ","Место каде што децата учат.","МЕСТА"),
 Puzzle("ПЛАНИНА","Висока природна форма на земјиште.","ПРИРОДА"),
 Puzzle("ЅВЕЗДИ","Светат на ноќното небо.","ВСЕЛЕНА"),
 Puzzle("СОНЦЕ","Ѕвезда што ја осветлува Земјата.","ПРИРОДА"),
 Puzzle("РАДОСТ","Чувство кога сме многу среќни.","ЧУВСТВА")
)

// Accept both Macedonian Cyrillic and common Latin transliteration.
fun normalizeAnswer(value: String): String {
    var s = value.trim().lowercase(Locale.ROOT)
        .replace("dzh", "џ").replace("dž", "џ")
        .replace("gj", "ѓ").replace("zh", "ж").replace("dz", "ѕ")
        .replace("lj", "љ").replace("nj", "њ").replace("kj", "ќ")
        .replace("ch", "ч").replace("sh", "ш")
    val latin = mapOf(
        'a' to 'а', 'b' to 'б', 'v' to 'в', 'g' to 'г', 'd' to 'д',
        'e' to 'е', 'z' to 'з', 'i' to 'и', 'j' to 'ј', 'k' to 'к',
        'l' to 'л', 'm' to 'м', 'n' to 'н', 'o' to 'о', 'p' to 'п',
        'r' to 'р', 's' to 'с', 't' to 'т', 'u' to 'у', 'f' to 'ф',
        'h' to 'х', 'c' to 'ц'
    )
    s = s.map { latin[it] ?: it }.joinToString("")
    return s.uppercase(Locale.ROOT).filter { it.isLetter() }
}

class MainActivity:ComponentActivity(){ override fun onCreate(savedInstanceState:Bundle?){super.onCreate(savedInstanceState);setContent { GameScreen() }}}
@Composable fun GameScreen(){
 var level by remember { mutableIntStateOf(0) }; var answer by remember { mutableStateOf("") }
 var score by remember { mutableIntStateOf(0) }; var solved by remember { mutableStateOf(false) }
 var hint by remember { mutableStateOf(false) }; var message by remember { mutableStateOf("Внеси го точниот збор!") }
 val p=puzzles[level % puzzles.size]
 MaterialTheme(colorScheme=darkColorScheme(primary=Color(0xFF8DE1D2))){
 Column(Modifier.fillMaxSize().background(Color(0xFF101B3D)).padding(20.dp),horizontalAlignment=Alignment.CenterHorizontally,verticalArrangement=Arrangement.spacedBy(18.dp,Alignment.CenterVertically)){
 Text("🧩 ПОГОДИ ГИ ЗБОРОВИТЕ",color=Color.White,fontSize=24.sp,fontWeight=FontWeight.ExtraBold,textAlign=TextAlign.Center)
 Text("Македонска игра со зборови",color=Color(0xFFB8C8EA))
 Text("НИВО "+(level+1)+"     ПОЕНИ: "+score,color=Color(0xFFFFD782),fontWeight=FontWeight.Bold)
 Card(colors=CardDefaults.cardColors(containerColor=Color(0xFF24365F)),shape=RoundedCornerShape(22.dp)){
 Column(Modifier.padding(20.dp),horizontalAlignment=Alignment.CenterHorizontally,verticalArrangement=Arrangement.spacedBy(14.dp)){
 Text("КАТЕГОРИЈА: "+p.category,color=Color(0xFF8DE1D2),fontWeight=FontWeight.Bold)
 Text("Кој збор е ова?",color=Color.White,fontSize=21.sp,fontWeight=FontWeight.Bold)
 Text(p.clue,color=Color.White,fontSize=17.sp,textAlign=TextAlign.Center)
 Text("БРОЈ НА БУКВИ: "+p.word.length,color=Color(0xFFFFD782))
 OutlinedTextField(value=answer,onValueChange={answer=it.uppercase()},label={Text("Твојот одговор (кирилица или латиница)")},enabled=!solved,singleLine=true)
 Button(onClick={if(normalizeAnswer(answer)==normalizeAnswer(p.word)){score+=if(hint)5 else 10;solved=true;message="БРАВО! ТОЧЕН ОДГОВОР! 🎉"}else{message="Не е точно. Обиди се повторно!"}},enabled=!solved,modifier=Modifier.fillMaxWidth()){Text("ПРОВЕРИ")}
 OutlinedButton(onClick={hint=true;message="ПРВА БУКВА: "+p.word.first()},enabled=!solved && !hint){Text("💡 ПОМОШ")}
 Text(message,color=Color(0xFFFFD782),textAlign=TextAlign.Center)
 if(solved) Button(onClick={level++;answer="";solved=false;hint=false;message="Внеси го точниот збор!"},modifier=Modifier.fillMaxWidth()){Text("СЛЕДНО НИВО →")}
 }}
 Text("Погодувај, учи и забавувај се!",color=Color(0xFFB8C8EA))
 }}
}
