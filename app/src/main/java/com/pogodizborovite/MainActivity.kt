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
  Puzzle("ДЕЛФИН","Паметно морско животно што плива и скока.","ЖИВОТНИ","https://images.unsplash.com/photo-1607153333879-c174d265f1d2?w=900"),
  Puzzle("ТИГАР","Голема дива мачка со портокалово крзно и црни линии.","ЖИВОТНИ","https://loremflickr.com/900/600/tiger?lock=1000"),
  Puzzle("ЛИСИЦА","Диво животно со бушава опашка и остар муцун.","ЖИВОТНИ","https://loremflickr.com/900/600/fox?lock=1001"),
  Puzzle("ЗАЈАК","Мало животно со долги уши што скока.","ЖИВОТНИ","https://loremflickr.com/900/600/rabbit?lock=1002"),
  Puzzle("КОЊ","Големо домашно животно на кое може да се јава.","ЖИВОТНИ","https://loremflickr.com/900/600/horse?lock=1003"),
  Puzzle("КРАВА","Домашно животно што дава млеко.","ЖИВОТНИ","https://loremflickr.com/900/600/cow?lock=1004"),
  Puzzle("ОВЦА","Домашно животно со волнено крзно.","ЖИВОТНИ","https://loremflickr.com/900/600/sheep?lock=1005"),
  Puzzle("КОЗА","Домашно животно што се качува по карпи.","ЖИВОТНИ","https://loremflickr.com/900/600/goat?lock=1006"),
  Puzzle("ЖЕЛКА","Животно со тврд оклоп на грбот.","ЖИВОТНИ","https://loremflickr.com/900/600/turtle?lock=1007"),
  Puzzle("ПАПАГАЛ","Шарена птица што може да имитира звуци.","ЖИВОТНИ","https://loremflickr.com/900/600/parrot?lock=1008"),
  Puzzle("ОРЕЛ","Голема граблива птица со силни канџи.","ЖИВОТНИ","https://loremflickr.com/900/600/eagle?lock=1009"),
  Puzzle("СЛОН","Големо животно со долга сурла и големи уши.","ЖИВОТНИ","https://loremflickr.com/900/600/elephant?lock=11215"),
  Puzzle("ЖИРАФА","Високо африканско животно со долг врат.","ЖИВОТНИ","https://loremflickr.com/900/600/giraffe?lock=13290"),
  Puzzle("НОСОРОГ","Големо животно со дебела кожа и рог на носот.","ЖИВОТНИ","https://loremflickr.com/900/600/rhinoceros?lock=14371"),
  Puzzle("НИЛСКИ КОЊ","Големо африканско животно што живее во вода и покрај неа.","ЖИВОТНИ","https://loremflickr.com/900/600/hippopotamus?lock=16477"),
  Puzzle("КЕНГУР","Австралиско животно што скока и го носи младенчето во торба.","ЖИВОТНИ","https://loremflickr.com/900/600/kangaroo?lock=13306"),
  Puzzle("ПИНГВИН","Птица што не лета, но одлично плива во студени мориња.","ЖИВОТНИ","https://loremflickr.com/900/600/penguin?lock=14342"),
  Puzzle("БУВ","Ноќна птица со големи очи.","ЖИВОТНИ","https://loremflickr.com/900/600/owl?lock=10142"),
  Puzzle("ПАУН","Птица позната по големата шарена опашка.","ЖИВОТНИ","https://loremflickr.com/900/600/peacock?lock=11207"),
  Puzzle("ЕЖ","Мало животно покриено со боцки.","ЖИВОТНИ","https://loremflickr.com/900/600/hedgehog?lock=9091"),
  Puzzle("ВЕРВЕРИЦА","Мало животно што се качува по дрвја и собира ореви.","ЖИВОТНИ","https://loremflickr.com/900/600/squirrel?lock=16436"),
  Puzzle("КАМИЛА","Животно со грпка што може долго да издржи без вода.","ЖИВОТНИ","https://loremflickr.com/900/600/camel?lock=33281"),
  Puzzle("ПАНДА","Црно-бело мече што најчесто јаде бамбус.","ЖИВОТНИ","https://loremflickr.com/900/600/giant%20panda?lock=32232"),
  Puzzle("КОАЛА","Австралиско торбарско животно што живее на дрвја.","ЖИВОТНИ","https://loremflickr.com/900/600/koala?lock=32235"),
  Puzzle("КРОКОДИЛ","Голем влекач со силни вилици што живее во вода и на копно.","ЖИВОТНИ","https://loremflickr.com/900/600/crocodile?lock=35407"),
  Puzzle("ЗМИЈА","Влекач без нозе што се движи ползејќи.","ЖИВОТНИ","https://loremflickr.com/900/600/snake?lock=32219"),
  Puzzle("ЖАБА","Мало водоземно животно што скока и квака.","ЖИВОТНИ","https://loremflickr.com/900/600/frog?lock=31167"),
  Puzzle("ПЧЕЛА","Инсект што собира нектар и произведува мед.","ЖИВОТНИ","https://loremflickr.com/900/600/honey%20bee?lock=32254"),
  Puzzle("ПЕПЕРУТКА","Инсект со големи шарени крилја.","ЖИВОТНИ","https://loremflickr.com/900/600/butterfly?lock=36463"),
  Puzzle("МИШКА","Мало глодарче со долга опашка.","ЖИВОТНИ","https://loremflickr.com/900/600/mouse?lock=32254"),
  Puzzle("СВИЊА","Домашно животно што често живее на фарма и рие со муцката.","ЖИВОТНИ","https://loremflickr.com/900/600/pig?lock=32221")
 )),
 GameCategory("ОВОШЈЕ И ХРАНА","🍎", listOf(
  Puzzle("ЈАБОЛКО","Црвено или зелено овошје што расте на дрво.","ОВОШЈЕ","https://images.unsplash.com/photo-1560806887-1e4cd0b6cbd6?w=900"),
  Puzzle("БАНАНА","Жолто овошје што го лупиме пред јадење.","ОВОШЈЕ","https://images.unsplash.com/photo-1571771894821-ce9b6c11b08e?w=900"),
  Puzzle("ПОРТОКАЛ","Тркалесто цитрусно овошје со портокалова кора.","ОВОШЈЕ","https://images.unsplash.com/photo-1547514701-42782101795e?w=900"),
  Puzzle("ЈАГОДА","Црвено овошје со мали семки на површината.","ОВОШЈЕ","https://images.unsplash.com/photo-1464965911861-746a04b4bca6?w=900"),
  Puzzle("ЛИМОН","Жолто кисело цитрусно овошје.","ОВОШЈЕ","https://images.unsplash.com/photo-1590502593747-42a996133562?w=900"),
  Puzzle("ЛУБЕНИЦА","Големо летно овошје со црвена сочна внатрешност.","ОВОШЈЕ","https://images.unsplash.com/photo-1563114773-84221bd62daa?w=900"),
  Puzzle("АНАНАС","Тропско овошје со тврда кора и зелени листови.","ОВОШЈЕ","https://images.unsplash.com/photo-1550258987-190a2d41a8ba?w=900"),
  Puzzle("МОРКОВ","Портокалов коренест зеленчук што го јадеме свеж или варен.","ХРАНА","https://images.unsplash.com/photo-1445282768818-728615cc910a?w=900"),
  Puzzle("КРУША","Сочно овошје што често има форма на ѕвонче.","ОВОШЈЕ И ХРАНА","https://loremflickr.com/900/600/pear?lock=1100"),
  Puzzle("ЦРЕША","Мало црвено овошје со тврда семка.","ОВОШЈЕ И ХРАНА","https://loremflickr.com/900/600/cherry?lock=1101"),
  Puzzle("ПРАСКА","Меко овошје со кадифена кора и голема семка.","ОВОШЈЕ И ХРАНА","https://loremflickr.com/900/600/peach?lock=1102"),
  Puzzle("ГРОЗЈЕ","Мали плодови што растат во гроздови.","ОВОШЈЕ И ХРАНА","https://loremflickr.com/900/600/grapes?lock=1103"),
  Puzzle("КИВИ","Овошје со кафеава кора и зелена внатрешност.","ОВОШЈЕ И ХРАНА","https://loremflickr.com/900/600/kiwi%20fruit?lock=1104"),
  Puzzle("КАЈСИЈА","Портокалово овошје со мека кора и една семка.","ОВОШЈЕ И ХРАНА","https://loremflickr.com/900/600/apricot?lock=1105"),
  Puzzle("ДОМАТ","Црвен плод што често се користи во салати.","ОВОШЈЕ И ХРАНА","https://loremflickr.com/900/600/tomato?lock=1106"),
  Puzzle("КРАСТАВИЦА","Зелен долг зеленчук што често се јаде свеж.","ОВОШЈЕ И ХРАНА","https://loremflickr.com/900/600/cucumber?lock=1107"),
  Puzzle("КОМПИР","Скробест зеленчук што се вари или пече.","ОВОШЈЕ И ХРАНА","https://loremflickr.com/900/600/potato?lock=1108"),
  Puzzle("ОРИЗ","Мали зрна што се готват како прилог или јадење.","ОВОШЈЕ И ХРАНА","https://loremflickr.com/900/600/rice?lock=1109"),
  Puzzle("МАНДАРИНА","Мало цитрусно овошје што лесно се лупи.","ОВОШЈЕ И ХРАНА","https://loremflickr.com/900/600/mandarin%20orange?lock=23426"),
  Puzzle("ЛОЗОВ ЛИСТ","Лист што се користи за виткање сарми.","ОВОШЈЕ И ХРАНА","https://loremflickr.com/900/600/grape%20leaf?lock=23494"),
  Puzzle("СМОКВА","Слатко овошје со мека внатрешност и многу ситни семки.","ОВОШЈЕ И ХРАНА","https://loremflickr.com/900/600/fig%20fruit?lock=20295"),
  Puzzle("КАЛИНКА","Овошје со црвени зрнца внатре.","ОВОШЈЕ И ХРАНА","https://loremflickr.com/900/600/pomegranate?lock=21332"),
  Puzzle("ДИЊА","Големо слатко летно овошје со миризлива внатрешност.","ОВОШЈЕ И ХРАНА","https://loremflickr.com/900/600/melon?lock=18166"),
  Puzzle("ПИПЕРКА","Зеленчук што може да биде црвен, жолт или зелен.","ОВОШЈЕ И ХРАНА","https://loremflickr.com/900/600/bell%20pepper?lock=21349"),
  Puzzle("КРОМИД","Зеленчук што често се сече за готвење и предизвикува солзи.","ОВОШЈЕ И ХРАНА","https://loremflickr.com/900/600/onion?lock=20304"),
  Puzzle("ЛУК","Зачинест зеленчук со силен мирис, составен од чешниња.","ОВОШЈЕ И ХРАНА","https://loremflickr.com/900/600/garlic?lock=17160"),
  Puzzle("СИРЕЊЕ","Млечен производ што се прави од млеко.","ОВОШЈЕ И ХРАНА","https://loremflickr.com/900/600/cheese?lock=20285"),
  Puzzle("ЛЕБ","Основна храна што се пече од брашно и вода.","ОВОШЈЕ И ХРАНА","https://loremflickr.com/900/600/bread?lock=17137"),
  Puzzle("МАЛИНА","Мало црвено или розово шумско овошје.","ОВОШЈЕ И ХРАНА","https://loremflickr.com/900/600/raspberry?lock=40284"),
  Puzzle("БОРОВИНКА","Мало темносино шумско овошје.","ОВОШЈЕ И ХРАНА","https://loremflickr.com/900/600/blueberry?lock=43438"),
  Puzzle("КАПИНА","Темно овошје што расте на бодликава грмушка.","ОВОШЈЕ И ХРАНА","https://loremflickr.com/900/600/blackberry?lock=40286"),
  Puzzle("ПАПАЈА","Тропско овошје со портокалова внатрешност и црни семки.","ОВОШЈЕ И ХРАНА","https://loremflickr.com/900/600/papaya?lock=40262"),
  Puzzle("МАНГО","Тропско овошје со жолто-портокалова сочна внатрешност.","ОВОШЈЕ И ХРАНА","https://loremflickr.com/900/600/mango%20fruit?lock=39242"),
  Puzzle("АВОКАДО","Зелено овошје со голема семка и кремаста внатрешност.","ОВОШЈЕ И ХРАНА","https://loremflickr.com/900/600/avocado?lock=41324"),
  Puzzle("ПЕЧУРКА","Јадлива габа што се користи во многу јадења.","ОВОШЈЕ И ХРАНА","https://loremflickr.com/900/600/mushroom?lock=41368"),
  Puzzle("ПЧЕНКА","Жолти зрна што растат на кочан.","ОВОШЈЕ И ХРАНА","https://loremflickr.com/900/600/corn%20cob?lock=40306"),
  Puzzle("ТЕСТЕНИНИ","Храна направена од тесто, често во форма на шпагети.","ОВОШЈЕ И ХРАНА","https://loremflickr.com/900/600/pasta?lock=43465"),
  Puzzle("ЈОГУРТ","Млечен производ со киселкаст вкус.","ОВОШЈЕ И ХРАНА","https://loremflickr.com/900/600/yogurt?lock=40302")
 )),
 GameCategory("ПРИРОДА","🌳", listOf(
  Puzzle("ПЛАНИНА","Висока природна форма на земјиште.","ПРИРОДА","https://images.unsplash.com/photo-1464822759023-fed622ff2c3b?w=900"),
  Puzzle("СОНЦЕ","Ѕвезда што ја осветлува Земјата.","ПРИРОДА","https://images.unsplash.com/photo-1500530855697-b586d89ba3ee?w=900"),
  Puzzle("РЕКА","Природна вода што тече низ корито.","ПРИРОДА","https://images.unsplash.com/photo-1433086966358-54859d0ed716?w=900"),
  Puzzle("ВОДОПАД","Вода што паѓа од висока карпа или праг.","ПРИРОДА","https://images.unsplash.com/photo-1432405972618-c60b0225b8f9?w=900"),
  Puzzle("ЕЗЕРО","Голема површина со вода опкружена со копно.","ПРИРОДА","https://images.unsplash.com/photo-1470770841072-f978cf4d019e?w=900"),
  Puzzle("ПЕШТЕРА","Природна шуплина во карпа или планина.","ПРИРОДА","https://images.unsplash.com/photo-1500534623283-312aade485b7?w=900"),
  Puzzle("ВУЛКАН","Планина од која може да избива лава.","ПРИРОДА","https://images.unsplash.com/photo-1462331940025-496dfbfc7564?w=900"),
  Puzzle("ОСТРОВ","Копно опкружено со вода од сите страни.","ПРИРОДА","https://images.unsplash.com/photo-1501785888041-af3ef285b470?w=900"),
  Puzzle("ШУМА","Голема површина покриена со многу дрвја.","ПРИРОДА","https://loremflickr.com/900/600/forest?lock=1200"),
  Puzzle("ЦВЕТ","Шарен дел од растение што често има мирис.","ПРИРОДА","https://loremflickr.com/900/600/flower?lock=1201"),
  Puzzle("ДРВО","Високо растение со стебло и гранки.","ПРИРОДА","https://loremflickr.com/900/600/tree?lock=1202"),
  Puzzle("ОБЛАК","Бела или сива маса од ситни капки на небото.","ПРИРОДА","https://loremflickr.com/900/600/cloud?lock=1203"),
  Puzzle("ДОЖД","Вода што паѓа од облаците.","ПРИРОДА","https://loremflickr.com/900/600/rain?lock=1204"),
  Puzzle("СНЕГ","Бели ледени кристали што паѓаат во зима.","ПРИРОДА","https://loremflickr.com/900/600/snow?lock=1205"),
  Puzzle("ВЕТЕР","Движење на воздухот.","ПРИРОДА","https://loremflickr.com/900/600/wind?lock=1206"),
  Puzzle("МОРЕ","Голема солена водена површина.","ПРИРОДА","https://loremflickr.com/900/600/sea?lock=1207"),
  Puzzle("ПЕСОК","Ситни зрнца што често ги има на плажа.","ПРИРОДА","https://loremflickr.com/900/600/sand?lock=1208"),
  Puzzle("ЅВЕЗДА","Светло небесно тело што се гледа ноќе.","ПРИРОДА","https://loremflickr.com/900/600/star?lock=1209"),
  Puzzle("ДОЛИНА","Ниско земјиште меѓу ридови или планини.","ПРИРОДА","https://loremflickr.com/900/600/valley?lock=13290"),
  Puzzle("РИД","Издигнат дел од земјиштето, понизок од планина.","ПРИРОДА","https://loremflickr.com/900/600/hill?lock=10148"),
  Puzzle("ВИНУГА","Природна ледена маса што бавно се движи низ планина.","ПРИРОДА","https://loremflickr.com/900/600/glacier?lock=13285"),
  Puzzle("КАНЈОН","Длабока и тесна долина со стрмни карпести страни.","ПРИРОДА","https://loremflickr.com/900/600/canyon?lock=13282"),
  Puzzle("ВИСТИНСКИ КАМЕН","Цврст природен материјал од кој се градат карпи.","ПРИРОДА","https://loremflickr.com/900/600/stone%20rock?lock=21733"),
  Puzzle("МОЛЊА","Силен електричен блесок за време на бура.","ПРИРОДА","https://loremflickr.com/900/600/lightning?lock=12231"),
  Puzzle("ВИНОЖИТО","Шарен лак на небото што се појавува по дожд.","ПРИРОДА","https://loremflickr.com/900/600/rainbow?lock=15403"),
  Puzzle("МАГЛА","Ситни капки вода во воздухот што ја намалуваат видливоста.","ПРИРОДА","https://loremflickr.com/900/600/fog?lock=12226"),
  Puzzle("ИЗВОР","Место каде што водата природно излегува од земјата.","ПРИРОДА","https://loremflickr.com/900/600/spring%20water?lock=12247"),
  Puzzle("КРАЈБРЕЖЈЕ","Појасот на копно покрај море или езеро.","ПРИРОДА","https://loremflickr.com/900/600/coastline?lock=17443"),
  Puzzle("ПУСТИНА","Сува област со многу малку дожд и малку растенија.","ПРИРОДА","https://loremflickr.com/900/600/desert%20landscape?lock=34370"),
  Puzzle("МОЧУРИШТЕ","Влажно земјиште со плитка вода и водни растенија.","ПРИРОДА","https://loremflickr.com/900/600/wetland%20marsh?lock=36499"),
  Puzzle("ВОДЕНА ЛИЛЈАНА","Водно растение со големи листови што пловат на површината.","ПРИРОДА","https://loremflickr.com/900/600/water%20lily?lock=40625"),
  Puzzle("КОРАЛЕН ГРЕБЕН","Подводна градба создадена од корали.","ПРИРОДА","https://loremflickr.com/900/600/coral%20reef?lock=40664"),
  Puzzle("ГРОМ","Силен звук што се слуша за време на бура.","ПРИРОДА","https://loremflickr.com/900/600/thunderstorm?lock=31205"),
  Puzzle("МРАЗ","Тенок слој замрзната вода на студена површина.","ПРИРОДА","https://loremflickr.com/900/600/frost?lock=31195"),
  Puzzle("СЕНКА","Темна површина создадена кога нешто ја блокира светлината.","ПРИРОДА","https://loremflickr.com/900/600/tree%20shadow?lock=32245"),
  Puzzle("ПРИЛИВ","Покачување на нивото на морската вода.","ПРИРОДА","https://loremflickr.com/900/600/ocean%20tide?lock=33300"),
  Puzzle("ПЛИМА","Период кога нивото на морската вода се намалува.","ПРИРОДА","https://loremflickr.com/900/600/low%20tide%20shore?lock=32246"),
  Puzzle("СЕВЕРНА СВЕТЛИНА","Шарени светлосни појави на небото во поларните области.","ПРИРОДА","https://loremflickr.com/900/600/aurora%20borealis?lock=42764")
 )),
 GameCategory("ПРЕВОЗ","🚗", listOf(
  Puzzle("АВТОМОБИЛ","Возило со четири тркала.","ПРЕВОЗ","https://images.unsplash.com/photo-1492144534655-ae79c964c9d7?w=900"),
  Puzzle("ВЕЛОСИПЕД","Превозно средство со две тркала и педали.","ПРЕВОЗ","https://images.unsplash.com/photo-1485965120184-e220f721d03e?w=900"),
  Puzzle("ВОЗ","Превозно средство што се движи по шини.","ПРЕВОЗ","https://loremflickr.com/900/600/railway%20station?lock=77123"),
  Puzzle("МОТОЦИКЛ","Моторно возило на две тркала.","ПРЕВОЗ","https://images.unsplash.com/photo-1558981806-ec527fa84c39?w=900"),
  Puzzle("ТРАМВАЈ","Градско превозно средство што се движи по шини.","ПРЕВОЗ","https://images.unsplash.com/photo-1519501025264-65ba15a82390?w=900"),
  Puzzle("ХЕЛИКОПТЕР","Летало со ротори што може да полетува вертикално.","ПРЕВОЗ","https://images.unsplash.com/photo-1474302770737-173ee21bab63?w=900"),
  Puzzle("ТРОТИНЕТ","Мало превозно средство на две тркала со платформа.","ПРЕВОЗ","https://images.unsplash.com/photo-1604868189265-219b5a8a1b6a?w=900"),
  Puzzle("КАМИОН","Големо моторно возило за превоз на товар.","ПРЕВОЗ","https://images.unsplash.com/photo-1601584115197-04ecc0da31d7?w=900"),
  Puzzle("АВИОН","Летало со крила што превезува патници.","ПРЕВОЗ","https://loremflickr.com/900/600/airplane?lock=1300"),
  Puzzle("БРОД","Пловило што се движи по вода.","ПРЕВОЗ","https://loremflickr.com/900/600/ship?lock=1301"),
  Puzzle("АВТОБУС","Големо патничко возило за јавен превоз.","ПРЕВОЗ","https://loremflickr.com/900/600/bus?lock=1302"),
  Puzzle("ТАКСИ","Автомобил што превезува патници за надомест.","ПРЕВОЗ","https://loremflickr.com/900/600/taxi?lock=1303"),
  Puzzle("МЕТРО","Подземен градски железнички превоз.","ПРЕВОЗ","https://loremflickr.com/900/600/subway%20train?lock=1304"),
  Puzzle("ЧАМЕЦ","Мало пловило за движење по вода.","ПРЕВОЗ","https://loremflickr.com/900/600/small%20boat?lock=1305"),
  Puzzle("ЕДРИЛИЦА","Пловило што се движи со помош на ветер.","ПРЕВОЗ","https://loremflickr.com/900/600/sailboat?lock=1306"),
  Puzzle("СКУТЕР","Мало моторно возило на две тркала.","ПРЕВОЗ","https://loremflickr.com/900/600/scooter?lock=1307"),
  Puzzle("ПРИКОЛКА","Возило без мотор што го влече друго возило.","ПРЕВОЗ","https://loremflickr.com/900/600/trailer?lock=1308"),
  Puzzle("БАГЕР","Работна машина што копа земја.","ПРЕВОЗ","https://loremflickr.com/900/600/excavator?lock=1309"),
  Puzzle("ТРОЛЕЈБУС","Градско електрично возило што се напојува преку жици.","ПРЕВОЗ","https://loremflickr.com/900/600/trolleybus?lock=15453"),
  Puzzle("ТРКАЛО","Кружен дел што му помага на возилото да се движи.","ПРЕВОЗ","https://loremflickr.com/900/600/wheel?lock=12309"),
  Puzzle("ПОДМОРНИЦА","Пловило што може да се движи под површината на водата.","ПРЕВОЗ","https://loremflickr.com/900/600/submarine?lock=16518"),
  Puzzle("ЖИЧНИЦА","Превозно средство што се движи високо над земјата по кабел.","ПРЕВОЗ","https://loremflickr.com/900/600/cable%20car?lock=13360"),
  Puzzle("САНКА","Превозно средство што се лизга по снег.","ПРЕВОЗ","https://loremflickr.com/900/600/sled?lock=11240"),
  Puzzle("КАРАВАН","Приколка или возило опремено за патување и престој.","ПРЕВОЗ","https://loremflickr.com/900/600/caravan?lock=13321"),
  Puzzle("КОЧИЈА","Старо превозно средство што го влечат коњи.","ПРЕВОЗ","https://loremflickr.com/900/600/horse%20carriage?lock=12287"),
  Puzzle("БУЛДОЖЕР","Тешка машина што турка земја и урнатини.","ПРЕВОЗ","https://loremflickr.com/900/600/bulldozer?lock=14396"),
  Puzzle("КОМБЕ","Помало моторно возило за превоз на луѓе или товар.","ПРЕВОЗ","https://loremflickr.com/900/600/van?lock=11242"),
  Puzzle("ТРАКТОР","Работно возило што се користи најчесто во земјоделство.","ПРЕВОЗ","https://loremflickr.com/900/600/tractor?lock=13372"),
  Puzzle("ЛИНЕЈКА","Долго возило со многу вагони што превезува патници или товар по шини.","ПРЕВОЗ","https://loremflickr.com/900/600/train%20locomotive?lock=33319"),
  Puzzle("ТРКАЛОЗ","Возило со три тркала.","ПРЕВОЗ","https://loremflickr.com/900/600/three%20wheeler%20vehicle?lock=33356"),
  Puzzle("САМОЛЕТ","Летало со мотори и крила.","ПРЕВОЗ","https://loremflickr.com/900/600/passenger%20airplane?lock=33357"),
  Puzzle("ГЛИСЕР","Брз моторен чамец што се движи по вода.","ПРЕВОЗ","https://loremflickr.com/900/600/speedboat?lock=32300"),
  Puzzle("ФЕРИБРОД","Голем брод што превезува патници и автомобили.","ПРЕВОЗ","https://loremflickr.com/900/600/ferry%20boat?lock=34404"),
  Puzzle("ДИРИЖАБЛ","Летало полесно од воздухот со голем наполнет балон.","ПРЕВОЗ","https://loremflickr.com/900/600/airship%20blimp?lock=34374"),
  Puzzle("ПАРЕН ВОЗ","Стар тип воз што се движел со помош на пареа.","ПРЕВОЗ","https://loremflickr.com/900/600/steam%20locomotive?lock=34424"),
  Puzzle("РОЛЕРИ","Обувки со тркалца што се користат за возење.","ПРЕВОЗ","https://loremflickr.com/900/600/roller%20skates?lock=32310"),
  Puzzle("СКЕЈТБОРД","Даска со четири тркалца на која се вози стоејќи.","ПРЕВОЗ","https://loremflickr.com/900/600/skateboard?lock=35437"),
  Puzzle("СНЕГОМОБИЛ","Моторно возило наменето за движење по снег.","ПРЕВОЗ","https://loremflickr.com/900/600/snowmobile?lock=36498")
 )),
 GameCategory("СПОРТ","⚽", listOf(
  Puzzle("ФУДБАЛ","Спорт што се игра со топка и нозе.","СПОРТ","https://images.unsplash.com/photo-1522778119026-d647f0596c20?w=900"),
  Puzzle("КОШАРКА","Спорт во кој топката се фрла во кош.","СПОРТ","https://images.unsplash.com/photo-1519861531473-9200262188bf?w=900"),
  Puzzle("ПЛИВАЊЕ","Спортска активност што се изведува во вода.","СПОРТ","https://images.unsplash.com/photo-1530549387789-4c1017266635?w=900"),
  Puzzle("ОДБОЈКА","Спорт во кој две екипи ја префрлаат топката преку мрежа.","СПОРТ","https://images.unsplash.com/photo-1612872087720-bb876e2e67d1?w=900"),
  Puzzle("СКИЈАЊЕ","Зимски спорт со лизгање по снег со скии.","СПОРТ","https://images.unsplash.com/photo-1517299321609-52687d1bc55a?w=900"),
  Puzzle("ВЕЛОСИПЕДИЗАМ","Спортско возење велосипед.","СПОРТ","https://images.unsplash.com/photo-1541625602330-2277e4c46182?w=900"),
  Puzzle("АТЛЕТИКА","Спорт со трчање, скокање и фрлање.","СПОРТ","https://images.unsplash.com/photo-1533561052604-c3beb6d55b8d?w=900"),
  Puzzle("ГИМНАСТИКА","Спорт со вежби за сила, рамнотежа и флексибилност.","СПОРТ","https://images.unsplash.com/photo-1518611012118-696072aa579a?w=900"),
  Puzzle("ТЕНИС","Спорт со рекет и топче преку мрежа.","СПОРТ","https://loremflickr.com/900/600/tennis?lock=1400"),
  Puzzle("БОКС","Боречки спорт во кој се удираат ракавици.","СПОРТ","https://loremflickr.com/900/600/boxing?lock=1401"),
  Puzzle("РАКОМЕТ","Тимски спорт во кој топката се фрла со рака.","СПОРТ","https://loremflickr.com/900/600/handball?lock=1402"),
  Puzzle("БЕЈЗБОЛ","Спорт со палка, топка и бази.","СПОРТ","https://loremflickr.com/900/600/baseball?lock=1403"),
  Puzzle("ГОЛФ","Спорт во кој топче се удира во дупка со палка.","СПОРТ","https://loremflickr.com/900/600/golf?lock=1404"),
  Puzzle("КАРАТЕ","Боречка вештина со удари и блокови.","СПОРТ","https://loremflickr.com/900/600/karate?lock=1405"),
  Puzzle("ЏУДО","Боречки спорт со фрлања и зафати.","СПОРТ","https://loremflickr.com/900/600/judo?lock=1406"),
  Puzzle("ЈАВАЊЕ","Спортска активност на коњ.","СПОРТ","https://loremflickr.com/900/600/horse%20riding?lock=1407"),
  Puzzle("КАЈАК","Веслачки спорт со тесен чамец.","СПОРТ","https://loremflickr.com/900/600/kayak?lock=1408"),
  Puzzle("КЛИЗГАЊЕ","Движење по мраз со лизгалки.","СПОРТ","https://loremflickr.com/900/600/ice%20skating?lock=1409"),
  Puzzle("ПИНГ-ПОНГ","Игра со мали рекети и топче на маса.","СПОРТ","https://loremflickr.com/900/600/table%20tennis?lock=13449"),
  Puzzle("КУГЛАЊЕ","Спорт во кој се соборуваат кегли со тешка топка.","СПОРТ","https://loremflickr.com/900/600/bowling?lock=12322"),
  Puzzle("СТРЕЛАШТВО","Спорт во кој се погодува цел со лак или оружје.","СПОРТ","https://loremflickr.com/900/600/archery%20target%20sport?lock=15525"),
  Puzzle("ФЕХТУВАЊЕ","Боречки спорт со мечеви и заштитна опрема.","СПОРТ","https://loremflickr.com/900/600/fencing?lock=14444"),
  Puzzle("РЕГБИ","Тимски спорт со овална топка.","СПОРТ","https://loremflickr.com/900/600/rugby?lock=10233"),
  Puzzle("КРИКЕТ","Тимски спорт со палка и топка, популарен во многу земји.","СПОРТ","https://loremflickr.com/900/600/cricket?lock=11307"),
  Puzzle("СНОУБОРД","Зимски спорт со една широка даска на снег.","СПОРТ","https://loremflickr.com/900/600/snowboard?lock=13418"),
  Puzzle("СУРФАЊЕ","Спорт во кој се вози на бранови со даска.","СПОРТ","https://loremflickr.com/900/600/surfing?lock=12351"),
  Puzzle("МАРАТОН","Долга трка во трчање на 42 километри.","СПОРТ","https://loremflickr.com/900/600/marathon%20running?lock=12353"),
  Puzzle("КАРЛИНГ","Зимски тимски спорт во кој камења се лизгаат по мраз.","СПОРТ","https://loremflickr.com/900/600/curling?lock=12341"),
  Puzzle("ФУТСАЛ","Варијанта на фудбал што се игра во сала со помал тим.","СПОРТ","https://loremflickr.com/900/600/futsal?lock=31325"),
  Puzzle("ВЕСЛАЊЕ","Спорт во кој спортистите придвижуваат чамец со весла.","СПОРТ","https://loremflickr.com/900/600/rowing%20sport?lock=32314"),
  Puzzle("ТРИАТЛОН","Натпревар што комбинира пливање, велосипедизам и трчање.","СПОРТ","https://loremflickr.com/900/600/triathlon?lock=33418"),
  Puzzle("ПАРКУР","Спортска активност со прескокнување пречки во урбана средина.","СПОРТ","https://loremflickr.com/900/600/parkour?lock=31316"),
  Puzzle("КАЧУВАЊЕ","Спорт во кој се искачуваат карпи или вештачки ѕидови.","СПОРТ","https://loremflickr.com/900/600/rock%20climbing?lock=33373"),
  Puzzle("ДИГАЊЕ ТЕГОВИ","Спорт во кој натпреварувачите креваат тешки тегови.","СПОРТ","https://loremflickr.com/900/600/weightlifting?lock=37576"),
  Puzzle("БОРЕЊЕ","Боречки спорт во кој двајца натпреварувачи се обидуваат да надвладеат.","СПОРТ","https://loremflickr.com/900/600/wrestling?lock=31275"),
  Puzzle("БАДМИНТОН","Спорт со рекети во кој се удира пердувесто топче.","СПОРТ","https://loremflickr.com/900/600/badminton?lock=34443"),
  Puzzle("СЕПАКТАКРАВ","Тимски спорт од Југоисточна Азија во кој топката се удира со нозе.","СПОРТ","https://loremflickr.com/900/600/sepak%20takraw?lock=36533"),
  Puzzle("СОФТБОЛ","Тимски спорт сличен на бејзбол, со поголема топка.","СПОРТ","https://loremflickr.com/900/600/softball?lock=32375")
 )),
 GameCategory("МЕСТА","🏙️", listOf(
  Puzzle("УЧИЛИШТЕ","Место каде што децата учат.","МЕСТА","https://images.unsplash.com/photo-1580582932707-520aed937b7b?w=900"),
  Puzzle("БИБЛИОТЕКА","Место каде што се позајмуваат книги.","МЕСТА","https://images.unsplash.com/photo-1507842217343-583bb7270b66?w=900"),
  Puzzle("БОЛНИЦА","Место каде што лекарите лекуваат болни луѓе.","МЕСТА","https://images.unsplash.com/photo-1586773860418-d37222d8fce3?w=900"),
  Puzzle("ТЕАТАР","Зграда каде што се изведуваат претстави.","МЕСТА","https://images.unsplash.com/photo-1503095396549-807759245b35?w=900"),
  Puzzle("СТАДИОН","Голем простор каде што се одржуваат спортски натпревари.","МЕСТА","https://images.unsplash.com/photo-1508098682722-e99c43a406b2?w=900"),
  Puzzle("АЕРОДРОМ","Место каде што полетуваат и слетуваат авиони.","МЕСТА","https://images.unsplash.com/photo-1436491865332-7a61a109cc05?w=900"),
  Puzzle("ЖЕЛЕЗНИЧКА СТАНИЦА","Место каде што запираат и тргнуваат возови.","МЕСТА","https://images.unsplash.com/photo-1474487548417-781cb71495f3?w=900"),
  Puzzle("ПАЗАР","Место каде што луѓето купуваат свежа храна и други производи.","МЕСТА","https://images.unsplash.com/photo-1488459716781-31db52582fe9?w=900"),
  Puzzle("МУЗЕЈ","Место каде што се изложуваат историски и уметнички предмети.","МЕСТА","https://loremflickr.com/900/600/museum?lock=1500"),
  Puzzle("ПЛАЖА","Место покрај море или езеро со песок или камчиња.","МЕСТА","https://loremflickr.com/900/600/beach?lock=1501"),
  Puzzle("ЦРКВА","Верски објект каде што се одржуваат христијански богослужби.","МЕСТА","https://loremflickr.com/900/600/church?lock=1502"),
  Puzzle("ЏАМИЈА","Верски објект каде што муслиманите се молат.","МЕСТА","https://loremflickr.com/900/600/mosque?lock=1503"),
  Puzzle("РЕСТОРАН","Место каде што се подготвува и служи храна.","МЕСТА","https://loremflickr.com/900/600/restaurant?lock=1504"),
  Puzzle("ХОТЕЛ","Место каде што патниците престојуваат и спијат.","МЕСТА","https://loremflickr.com/900/600/hotel?lock=1505"),
  Puzzle("БАНКА","Место каде што се чуваат и управуваат пари.","МЕСТА","https://loremflickr.com/900/600/bank%20building?lock=1506"),
  Puzzle("ПОШТА","Место каде што се испраќаат писма и пакети.","МЕСТА","https://loremflickr.com/900/600/post%20office?lock=1507"),
  Puzzle("ПАРК","Јавно зелено место за прошетка и одмор.","МЕСТА","https://loremflickr.com/900/600/city%20park?lock=1508"),
  Puzzle("ФАРМА","Место каде што се одгледуваат животни или растенија.","МЕСТА","https://loremflickr.com/900/600/farm?lock=1509"),
  Puzzle("БЕНЗИНСКА ПУМПА","Место каде што возилата полнат гориво.","МЕСТА","https://loremflickr.com/900/600/gas%20station?lock=19727"),
  Puzzle("АПТЕКА","Место каде што се купуваат лекови и медицински производи.","МЕСТА","https://loremflickr.com/900/600/pharmacy?lock=11288"),
  Puzzle("ПЕКАРНИЦА","Продавница каде што се продава свеж леб и печива.","МЕСТА","https://loremflickr.com/900/600/bakery?lock=14449"),
  Puzzle("СУПЕРМАРКЕТ","Голема продавница за храна и секојдневни производи.","МЕСТА","https://loremflickr.com/900/600/supermarket?lock=16573"),
  Puzzle("ПОЖАРНА СТАНИЦА","Место од каде што пожарникарите излегуваат на интервенции.","МЕСТА","https://loremflickr.com/900/600/fire%20station?lock=19734"),
  Puzzle("ПОЛИЦИСКА СТАНИЦА","Место каде што работи полицијата.","МЕСТА","https://loremflickr.com/900/600/police%20station?lock=21855"),
  Puzzle("ГРАДИНКА","Место каде што престојуваат и учат мали деца.","МЕСТА","https://loremflickr.com/900/600/kindergarten?lock=13374"),
  Puzzle("АВТОБУСКА СТАНИЦА","Место каде што пристигнуваат и поаѓаат автобуси.","МЕСТА","https://loremflickr.com/900/600/bus%20station?lock=21831"),
  Puzzle("ЗООЛОШКА ГРАДИНА","Место каде што посетителите гледаат различни животни.","МЕСТА","https://loremflickr.com/900/600/zoo?lock=20770"),
  Puzzle("БАЗЕН","Место исполнето со вода за пливање.","МЕСТА","https://loremflickr.com/900/600/swimming%20pool?lock=10226"),
  Puzzle("ОПШТИНА","Јавна установа што ги организира локалните работи во една општина.","МЕСТА","https://loremflickr.com/900/600/municipal%20building?lock=32372"),
  Puzzle("СУД","Институција каде што судии решаваат правни спорови.","МЕСТА","https://loremflickr.com/900/600/courthouse?lock=28160"),
  Puzzle("ЗАТВОР","Објект во кој се сместуваат лица осудени на затворска казна.","МЕСТА","https://loremflickr.com/900/600/prison%20building?lock=31297"),
  Puzzle("УНИВЕРЗИТЕТ","Високообразовна установа каде што се студира.","МЕСТА","https://loremflickr.com/900/600/university%20campus?lock=36559"),
  Puzzle("ЛАБОРАТОРИЈА","Просторија каде што се вршат научни испитувања и експерименти.","МЕСТА","https://loremflickr.com/900/600/science%20laboratory?lock=37570"),
  Puzzle("ОПЕРСКА КУЌА","Зграда каде што се изведуваат оперски претстави.","МЕСТА","https://loremflickr.com/900/600/opera%20house?lock=36574"),
  Puzzle("КИНО","Место каде што се прикажуваат филмови на голем екран.","МЕСТА","https://loremflickr.com/900/600/movie%20theater?lock=29205"),
  Puzzle("ЗАБАВЕН ПАРК","Место со атракции, вртелешки и тобогани.","МЕСТА","https://loremflickr.com/900/600/amusement%20park?lock=36541"),
  Puzzle("ПРИСТАНИШТЕ","Место покрај вода каде што бродовите пристануваат.","МЕСТА","https://loremflickr.com/900/600/harbor%20port?lock=36582"),
  Puzzle("СВЕТИЛНИК","Висока кула покрај море што им помага на бродовите да се ориентираат.","МЕСТА","https://loremflickr.com/900/600/lighthouse?lock=34452")
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
