package com.pogodizborovite
import android.app.Activity
import android.os.Bundle
import android.content.Context
import android.media.MediaPlayer
import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioTrack
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
import androidx.compose.ui.platform.LocalContext
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
  Puzzle("ТИГАР","Голема дива мачка со портокалово крзно и црни линии.","ЖИВОТНИ","https://commons.wikimedia.org/wiki/Special:FilePath/Bengal_tiger_from_front_next_to_tall_grass_at_Oakland_Zoo_(10544980325).jpg?width=900"),
  Puzzle("ЛИСИЦА","Диво животно со бушава опашка и остар муцун.","ЖИВОТНИ","https://commons.wikimedia.org/wiki/Special:FilePath/Wild_fox_(Unsplash).jpg?width=900"),
  Puzzle("ЗАЈАК","Мало животно со долги уши што скока.","ЖИВОТНИ","https://commons.wikimedia.org/wiki/Special:FilePath/White_rabbit_on_grass_(Unsplash).jpg?width=900"),
  Puzzle("КОЊ","Големо домашно животно на кое може да се јава.","ЖИВОТНИ","https://commons.wikimedia.org/wiki/Special:FilePath/Horse%27s_head_in_low_angle_(Unsplash).jpg?width=900"),
  Puzzle("КРАВА","Домашно животно што дава млеко.","ЖИВОТНИ","https://commons.wikimedia.org/wiki/Special:FilePath/Black_and_white_cow_portrait.jpg?width=900"),
  Puzzle("ОВЦА","Домашно животно со волнено крзно.","ЖИВОТНИ","https://commons.wikimedia.org/wiki/Special:FilePath/Sheep_and_lamb_on_hill_(Unsplash).jpg?width=900"),
  Puzzle("КОЗА","Домашно животно што се качува по карпи.","ЖИВОТНИ","https://commons.wikimedia.org/wiki/Special:FilePath/Goat.jpg?width=900"),
  Puzzle("ЖЕЛКА","Животно со тврд оклоп на грбот.","ЖИВОТНИ","https://commons.wikimedia.org/wiki/Special:FilePath/Small_yellow_green_turtle.jpg?width=900"),
  Puzzle("ПАПАГАЛ","Шарена птица што може да имитира звуци.","ЖИВОТНИ","https://commons.wikimedia.org/wiki/Special:FilePath/African_grey_parrot.jpg?width=900"),
  Puzzle("ОРЕЛ","Голема граблива птица со силни канџи.","ЖИВОТНИ","https://commons.wikimedia.org/wiki/Special:FilePath/Golden_eagle_closeup.jpg?width=900"),
  Puzzle("СЛОН","Големо животно со долга сурла и големи уши.","ЖИВОТНИ","https://commons.wikimedia.org/wiki/Special:FilePath/African_elephant.jpg?width=900"),
  Puzzle("ЖИРАФА","Високо африканско животно со долг врат.","ЖИВОТНИ","https://commons.wikimedia.org/wiki/Special:FilePath/Giraffe_at_Dierenpark_Amersfoort,_photo_6.JPG?width=900"),
  Puzzle("НОСОРОГ","Големо животно со дебела кожа и рог на носот.","ЖИВОТНИ","https://commons.wikimedia.org/wiki/Special:FilePath/White_Rhinoceros_(9114161448).jpg?width=900"),
  Puzzle("НИЛСКИ КОЊ","Големо африканско животно што живее во вода и покрај неа.","ЖИВОТНИ","https://loremflickr.com/900/600/hippopotamus?lock=16477"),
  Puzzle("КЕНГУР","Австралиско животно што скока и го носи младенчето во торба.","ЖИВОТНИ","https://commons.wikimedia.org/wiki/Special:FilePath/Cute_Kangaroo_(Unsplash).jpg?width=900"),
  Puzzle("ПИНГВИН","Птица што не лета, но одлично плива во студени мориња.","ЖИВОТНИ","https://commons.wikimedia.org/wiki/Special:FilePath/King_penguin_in_Edinburgh_Zoo_03.jpg?width=900"),
  Puzzle("БУВ","Ноќна птица со големи очи.","ЖИВОТНИ","https://commons.wikimedia.org/wiki/Special:FilePath/Eurasian_Eagle_Owl_Bubo_Bubo_Bird_Up_Close.jpg?width=900"),
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
  Puzzle("СВИЊА","Домашно животно што често живее на фарма и рие со муцката.","ЖИВОТНИ","https://loremflickr.com/900/600/pig?lock=32221"),
  Puzzle("ЛЕОПАРД","Голема дива мачка со дамки што се качува по дрвја.","ЖИВОТНИ","https://loremflickr.com/900/600/leopard?lock=50000"),
  Puzzle("ГЕПАРД","Најбрзото копнено животно, познато по трчањето.","ЖИВОТНИ","https://loremflickr.com/900/600/cheetah?lock=50001"),
  Puzzle("ХИЕНА","Диво животно што живее во групи и има силни вилици.","ЖИВОТНИ","https://loremflickr.com/900/600/hyena?lock=50002"),
  Puzzle("РИС","Дива мачка со кратка опашка и карактеристични уши со прамени.","ЖИВОТНИ","https://loremflickr.com/900/600/lynx?lock=50003"),
  Puzzle("ЈАЗОВЕЦ","Шумско животно со кратки нозе и издолжено тело.","ЖИВОТНИ","https://loremflickr.com/900/600/badger?lock=50004"),
  Puzzle("ВИДРА","Животно што плива во реки и има густо крзно.","ЖИВОТНИ","https://loremflickr.com/900/600/otter?lock=50005"),
  Puzzle("ФОКА","Морски цицач што има перки и живее во студени води.","ЖИВОТНИ","https://loremflickr.com/900/600/seal?lock=50006"),
  Puzzle("КИТ","Огромен морски цицач што дише преку отвор на главата.","ЖИВОТНИ","https://loremflickr.com/900/600/whale?lock=50007"),
  Puzzle("АЈКУЛА","Голема морска риба со остри заби.","ЖИВОТНИ","https://loremflickr.com/900/600/shark?lock=50008"),
  Puzzle("ФЛАМИНГО","Висока розова птица со долги нозе и свиткан клун.","ЖИВОТНИ","https://loremflickr.com/900/600/flamingo?lock=50009"),
  Puzzle("АНТИЛОПА","Брзо тревопасно животно со долги нозе и често со рогови.","ЖИВОТНИ","https://loremflickr.com/900/600/antelope?lock=62000"),
  Puzzle("КАМЕЛЕОН","Гуштер што може да ја менува бојата на кожата.","ЖИВОТНИ","https://loremflickr.com/900/600/chameleon?lock=62001"),
  Puzzle("ИГУАНА","Голем гуштер што често живее во топли краишта.","ЖИВОТНИ","https://loremflickr.com/900/600/iguana?lock=62002"),
  Puzzle("ТАРАНТУЛА","Голем влакнест пајак.","ЖИВОТНИ","https://loremflickr.com/900/600/tarantula?lock=62003"),
  Puzzle("СКОРПИЈА","Животно со клешти и отровно осило на опашката.","ЖИВОТНИ","https://loremflickr.com/900/600/scorpion?lock=62004"),
  Puzzle("ЛАСТОВИЦА","Птица преселница што лета брзо и гради гнездо под покриви.","ЖИВОТНИ","https://loremflickr.com/900/600/swallow%20bird?lock=62005"),
  Puzzle("ШТРК","Голема бела птица со долги нозе и црвен клун.","ЖИВОТНИ","https://loremflickr.com/900/600/stork?lock=62006"),
  Puzzle("ФАЗАН","Птица со шарени пердуви и долга опашка.","ЖИВОТНИ","https://loremflickr.com/900/600/pheasant?lock=62007"),
  Puzzle("ДИВА КОЗА","Планинско животно што се движи по стрмни карпи.","ЖИВОТНИ","https://loremflickr.com/900/600/wild%20goat?lock=62008"),
  Puzzle("МРАВКА","Мал инсект што живее во организирани колонии.","ЖИВОТНИ","https://loremflickr.com/900/600/ant?lock=62009"),
 )),
 GameCategory("ОВОШЈЕ И ХРАНА","🍎", listOf(
  Puzzle("ЈАБОЛКО","Црвено или зелено овошје што расте на дрво.","ОВОШЈЕ И ХРАНА","https://images.unsplash.com/photo-1560806887-1e4cd0b6cbd6?w=900"),
  Puzzle("БАНАНА","Жолто овошје што го лупиме пред јадење.","ОВОШЈЕ И ХРАНА","https://images.unsplash.com/photo-1571771894821-ce9b6c11b08e?w=900"),
  Puzzle("ПОРТОКАЛ","Тркалесто цитрусно овошје со портокалова кора.","ОВОШЈЕ И ХРАНА","https://images.unsplash.com/photo-1547514701-42782101795e?w=900"),
  Puzzle("ЈАГОДА","Црвено овошје со мали семки на површината.","ОВОШЈЕ И ХРАНА","https://images.unsplash.com/photo-1464965911861-746a04b4bca6?w=900"),
  Puzzle("ЛИМОН","Жолто кисело цитрусно овошје.","ОВОШЈЕ И ХРАНА","https://images.unsplash.com/photo-1590502593747-42a996133562?w=900"),
  Puzzle("ЛУБЕНИЦА","Големо летно овошје со црвена сочна внатрешност.","ОВОШЈЕ И ХРАНА","https://images.unsplash.com/photo-1563114773-84221bd62daa?w=900"),
  Puzzle("АНАНАС","Тропско овошје со тврда кора и зелени листови.","ОВОШЈЕ И ХРАНА","https://images.unsplash.com/photo-1550258987-190a2d41a8ba?w=900"),
  Puzzle("МОРКОВ","Портокалов коренест зеленчук што го јадеме свеж или варен.","ОВОШЈЕ И ХРАНА","https://images.unsplash.com/photo-1445282768818-728615cc910a?w=900"),
  Puzzle("КРУША","Сочно овошје што често има форма на ѕвонче.","ОВОШЈЕ И ХРАНА","https://loremflickr.com/900/600/pear?lock=1100"),
  Puzzle("ЦРЕША","Мало црвено овошје со тврда семка.","ОВОШЈЕ И ХРАНА","https://commons.wikimedia.org/wiki/Special:FilePath/Cherry_sliced_fruit.jpg?width=900"),
  Puzzle("ПРАСКА","Меко овошје со кадифена кора и голема семка.","ОВОШЈЕ И ХРАНА","https://commons.wikimedia.org/wiki/Special:FilePath/Autumn_Red_peaches.jpg?width=900"),
  Puzzle("ГРОЗЈЕ","Мали плодови што растат во гроздови.","ОВОШЈЕ И ХРАНА","https://commons.wikimedia.org/wiki/Special:FilePath/Green_grape_fruit.jpg?width=900"),
  Puzzle("КИВИ","Овошје со кафеава кора и зелена внатрешност.","ОВОШЈЕ И ХРАНА","https://loremflickr.com/900/600/kiwi%20fruit?lock=1104"),
  Puzzle("КАЈСИЈА","Портокалово овошје со мека кора и една семка.","ОВОШЈЕ И ХРАНА","https://loremflickr.com/900/600/apricot?lock=1105"),
  Puzzle("ДОМАТ","Црвен плод што често се користи во салати.","ОВОШЈЕ И ХРАНА","https://loremflickr.com/900/600/tomato?lock=1106"),
  Puzzle("КРАСТАВИЦА","Зелен долг зеленчук што често се јаде свеж.","ОВОШЈЕ И ХРАНА","https://commons.wikimedia.org/wiki/Special:FilePath/Food-vegetables-cucumbers_(23700040743).jpg?width=900"),
  Puzzle("КОМПИР","Скробест зеленчук што се вари или пече.","ОВОШЈЕ И ХРАНА","https://commons.wikimedia.org/wiki/Special:FilePath/Solanum_tuberosum_Edelgard_(02).jpg?width=900"),
  Puzzle("ОРИЗ","Мали зрна што се готват како прилог или јадење.","ОВОШЈЕ И ХРАНА","https://loremflickr.com/900/600/rice?lock=1109"),
  Puzzle("МАНДАРИНА","Мало цитрусно овошје што лесно се лупи.","ОВОШЈЕ И ХРАНА","https://commons.wikimedia.org/wiki/Special:FilePath/Dalanghita_(Mandarin_orange)_from_the_Philippines.jpg?width=900"),
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
  Puzzle("ЈОГУРТ","Млечен производ со киселкаст вкус.","ОВОШЈЕ И ХРАНА","https://loremflickr.com/900/600/yogurt?lock=40302"),
  Puzzle("КАРФИОЛ","Бел зеленчук со збиени цветови што се јаде варен или печен.","ОВОШЈЕ И ХРАНА","https://loremflickr.com/900/600/cauliflower?lock=50100"),
  Puzzle("БРОКУЛА","Зелен зеленчук со мали цветни главички.","ОВОШЈЕ И ХРАНА","https://loremflickr.com/900/600/broccoli?lock=50101"),
  Puzzle("ТИКВА","Голем портокалов плод што често се користи во супи и пити.","ОВОШЈЕ И ХРАНА","https://loremflickr.com/900/600/pumpkin?lock=50102"),
  Puzzle("ЦВЕКЛО","Коренест зеленчук со темноцрвена боја.","ОВОШЈЕ И ХРАНА","https://loremflickr.com/900/600/beetroot?lock=50103"),
  Puzzle("ПРАЗ","Зеленчук со долги зелени листови и бело стебло.","ОВОШЈЕ И ХРАНА","https://loremflickr.com/900/600/leek?lock=50104"),
  Puzzle("ЛЕШНИК","Јаткаст плод со тврда кафеава лушпа.","ОВОШЈЕ И ХРАНА","https://loremflickr.com/900/600/hazelnut?lock=50105"),
  Puzzle("ОРЕВ","Јаткаст плод со тврда набрана лушпа.","ОВОШЈЕ И ХРАНА","https://loremflickr.com/900/600/walnut?lock=50106"),
  Puzzle("БАДЕМ","Јаткаст плод со издолжена форма и кафеава кожица.","ОВОШЈЕ И ХРАНА","https://loremflickr.com/900/600/almond?lock=50107"),
  Puzzle("МЕД","Сладок природен производ што го создаваат пчелите.","ОВОШЈЕ И ХРАНА","https://loremflickr.com/900/600/honey?lock=50108"),
  Puzzle("ПАЛАЧИНКА","Тенко печено тесто што се јаде со сладок или солен фил.","ОВОШЈЕ И ХРАНА","https://loremflickr.com/900/600/pancake?lock=50109"),
  Puzzle("КАРАМБОЛА","Тропско овошје што на пресек има форма на ѕвезда.","ОВОШЈЕ И ХРАНА","https://loremflickr.com/900/600/starfruit?lock=62100"),
  Puzzle("ГРАНАДИЛА","Тропско овошје со ароматична пулпа и многу семки.","ОВОШЈЕ И ХРАНА","https://loremflickr.com/900/600/passionfruit?lock=62101"),
  Puzzle("ДУЊА","Жолто ароматично овошје што често се вари за слатко.","ОВОШЈЕ И ХРАНА","https://loremflickr.com/900/600/quince?lock=62102"),
  Puzzle("НЕШПУЛА","Мало портокалово овошје со неколку крупни семки.","ОВОШЈЕ И ХРАНА","https://loremflickr.com/900/600/loquat?lock=62103"),
  Puzzle("АРТИЧОКА","Зеленчук од кој се јаде месестиот дел од цветната пупка.","ОВОШЈЕ И ХРАНА","https://loremflickr.com/900/600/artichoke?lock=62104"),
  Puzzle("ЦЕЛЕР","Зеленчук со крцкави стебла и карактеристичен мирис.","ОВОШЈЕ И ХРАНА","https://loremflickr.com/900/600/celery?lock=62105"),
  Puzzle("СПАНАЌ","Лиснат зелен зеленчук богат со хранливи материи.","ОВОШЈЕ И ХРАНА","https://loremflickr.com/900/600/spinach?lock=62106"),
  Puzzle("ЛЕЌА","Мали мешунки што се варат во чорби и други јадења.","ОВОШЈЕ И ХРАНА","https://loremflickr.com/900/600/lentils?lock=62107"),
  Puzzle("НАУТ","Мешунка што се користи за хумус и варени јадења.","ОВОШЈЕ И ХРАНА","https://loremflickr.com/900/600/chickpeas?lock=62108"),
  Puzzle("ПРОСО","Ситно зрно што се готви како каша или прилог.","ОВОШЈЕ И ХРАНА","https://loremflickr.com/900/600/millet?lock=62109"),
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
  Puzzle("ШУМА","Голема површина покриена со многу дрвја.","ПРИРОДА","https://commons.wikimedia.org/wiki/Special:FilePath/Forest_sunrise.jpg?width=900"),
  Puzzle("ЦВЕТ","Шарен дел од растение што често има мирис.","ПРИРОДА","https://commons.wikimedia.org/wiki/Special:FilePath/Aspidistra_flower.jpg?width=900"),
  Puzzle("ДРВО","Високо растение со стебло и гранки.","ПРИРОДА","https://commons.wikimedia.org/wiki/Special:FilePath/Maple_tree_turku_2005.jpg?width=900"),
  Puzzle("ОБЛАК","Бела или сива маса од ситни капки на небото.","ПРИРОДА","https://commons.wikimedia.org/wiki/Special:FilePath/Sky_Atmosphere_Clouds.jpg?width=900"),
  Puzzle("ДОЖД","Вода што паѓа од облаците.","ПРИРОДА","https://commons.wikimedia.org/wiki/Special:FilePath/It%27s_raining_(Unsplash).jpg?width=900"),
  Puzzle("СНЕГ","Бели ледени кристали што паѓаат во зима.","ПРИРОДА","https://commons.wikimedia.org/wiki/Special:FilePath/Winter_snowy_mountains_of_Alberta,_Canada.jpg?width=900"),
  Puzzle("ВЕТЕР","Движење на воздухот.","ПРИРОДА","https://loremflickr.com/900/600/wind?lock=1206"),
  Puzzle("МОРЕ","Голема солена водена површина.","ПРИРОДА","https://commons.wikimedia.org/wiki/Special:FilePath/Pacific_ocean_coastal_area_51.jpg?width=900"),
  Puzzle("ПЕСОК","Ситни зрнца што често ги има на плажа.","ПРИРОДА","https://loremflickr.com/900/600/sand?lock=1208"),
  Puzzle("ЅВЕЗДА","Светло небесно тело што се гледа ноќе.","ПРИРОДА","https://loremflickr.com/900/600/star?lock=1209"),
  Puzzle("ДОЛИНА","Ниско земјиште меѓу ридови или планини.","ПРИРОДА","https://loremflickr.com/900/600/valley?lock=13290"),
  Puzzle("РИД","Издигнат дел од земјиштето, понизок од планина.","ПРИРОДА","https://loremflickr.com/900/600/hill?lock=10148"),
  Puzzle("ЛЕДНИК", "Голема маса мраз што бавно се движи низ планина.","ПРИРОДА","https://loremflickr.com/900/600/glacier?lock=31291"),
  Puzzle("КАНЈОН","Длабока и тесна долина со стрмни карпести страни.","ПРИРОДА","https://commons.wikimedia.org/wiki/Special:FilePath/Grand-canyon-national-park.jpg?width=900"),
  Puzzle("КАМЕН", "Цврст природен материјал што го има во карпите.","ПРИРОДА","https://loremflickr.com/900/600/stone%20rock?lock=30240"),
  Puzzle("МОЛЊА","Силен електричен блесок за време на бура.","ПРИРОДА","https://commons.wikimedia.org/wiki/Special:FilePath/Lightning_NOAA.jpg?width=900"),
  Puzzle("ВИНОЖИТО","Шарен лак на небото што се појавува по дожд.","ПРИРОДА","https://commons.wikimedia.org/wiki/Special:FilePath/Rainbow_over_Drexel_Heights,_AZ,_USA.jpg?width=900"),
  Puzzle("МАГЛА","Ситни капки вода во воздухот што ја намалуваат видливоста.","ПРИРОДА","https://loremflickr.com/900/600/fog?lock=12226"),
  Puzzle("ИЗВОР","Место каде што водата природно излегува од земјата.","ПРИРОДА","https://loremflickr.com/900/600/spring%20water?lock=12247"),
  Puzzle("КРАЈБРЕЖЈЕ","Појасот на копно покрај море или езеро.","ПРИРОДА","https://loremflickr.com/900/600/coastline?lock=17443"),
  Puzzle("ПУСТИНА","Сува област со многу малку дожд и малку растенија.","ПРИРОДА","https://commons.wikimedia.org/wiki/Special:FilePath/California_Desert.jpg?width=900"),
  Puzzle("МОЧУРИШТЕ","Влажно земјиште со плитка вода и водни растенија.","ПРИРОДА","https://loremflickr.com/900/600/wetland%20marsh?lock=36499"),
  Puzzle("ВОДЕНА ЛИЛЈАНА","Водно растение со големи листови што пловат на површината.","ПРИРОДА","https://loremflickr.com/900/600/water%20lily?lock=40625"),
  Puzzle("КОРАЛЕН ГРЕБЕН","Подводна градба создадена од корали.","ПРИРОДА","https://loremflickr.com/900/600/coral%20reef?lock=40664"),
  Puzzle("ГРОМ","Силен звук што се слуша за време на бура.","ПРИРОДА","https://loremflickr.com/900/600/thunderstorm?lock=31205"),
  Puzzle("МРАЗ","Тенок слој замрзната вода на студена површина.","ПРИРОДА","https://loremflickr.com/900/600/frost?lock=31195"),
  Puzzle("СЕНКА","Темна површина создадена кога нешто ја блокира светлината.","ПРИРОДА","https://loremflickr.com/900/600/tree%20shadow?lock=32245"),
  Puzzle("ПРИЛИВ","Покачување на нивото на морската вода.","ПРИРОДА","https://loremflickr.com/900/600/ocean%20tide?lock=33300"),
  Puzzle("ОСЕКА", "Период кога нивото на морската вода се намалува.","ПРИРОДА","https://loremflickr.com/900/600/low%20tide%20shore?lock=30246"),
  Puzzle("СЕВЕРНА СВЕТЛИНА","Шарени светлосни појави на небото во поларните области.","ПРИРОДА","https://loremflickr.com/900/600/aurora%20borealis?lock=42764"),
  Puzzle("ПОЛАРНА СВЕТЛИНА","Шарени светлосни појави што се гледаат на ноќното небо во поларните области.","ПРИРОДА","https://loremflickr.com/900/600/aurora%20borealis?lock=50200"),
  Puzzle("ГЕЈЗЕР","Топла вода и пареа што повремено избиваат од земјата.","ПРИРОДА","https://loremflickr.com/900/600/geyser?lock=50201"),
  Puzzle("САВАНА","Тревна област со ретки дрвја во топлите предели.","ПРИРОДА","https://loremflickr.com/900/600/savanna?lock=50202"),
  Puzzle("ЏУНГЛА","Густа тропска шума со многу растенија и животни.","ПРИРОДА","https://loremflickr.com/900/600/jungle?lock=50203"),
  Puzzle("БАМБУК","Високо растение со шупливи и цврсти стебла.","ПРИРОДА","https://loremflickr.com/900/600/bamboo%20forest?lock=50204"),
  Puzzle("КАКТУС","Растение приспособено на суша, често со боцки.","ПРИРОДА","https://loremflickr.com/900/600/cactus?lock=50205"),
  Puzzle("МАГМА","Растопена карпа што се наоѓа под површината на Земјата.","ПРИРОДА","https://loremflickr.com/900/600/magma%20lava?lock=50206"),
  Puzzle("ГЛИНА","Фина земја што станува пластична кога ќе се навлажни.","ПРИРОДА","https://loremflickr.com/900/600/clay%20soil?lock=50207"),
  Puzzle("ОСЕКА","Периодично намалување на нивото на морската вода.","ПРИРОДА","https://loremflickr.com/900/600/low%20tide?lock=50209"),
  Puzzle("ГЛАЦИЕР","Голема маса мраз што бавно се движи низ планините.","ПРИРОДА","https://loremflickr.com/900/600/glacier?lock=62200"),
  Puzzle("ЛАВИНА","Голема маса снег што брзо се спушта по планинска падина.","ПРИРОДА","https://loremflickr.com/900/600/avalanche?lock=62201"),
  Puzzle("СТАЛАКТИТ","Варовничка форма што виси од таванот на пештера.","ПРИРОДА","https://loremflickr.com/900/600/stalactite?lock=62202"),
  Puzzle("ДИНА","Рид од песок што го обликува ветерот.","ПРИРОДА","https://loremflickr.com/900/600/sand%20dune?lock=62203"),
  Puzzle("РОСА","Ситни капки вода што се појавуваат наутро на растенијата.","ПРИРОДА","https://loremflickr.com/900/600/dew%20drops?lock=62204"),
  Puzzle("ВЕТЕРНА БУРА","Силно движење на воздухот што може да соборува гранки и предмети.","ПРИРОДА","https://loremflickr.com/900/600/wind%20storm?lock=62205"),
  Puzzle("ПЕСКОВИТА ПЛАЖА","Крајбрежје покриено со ситен песок.","ПРИРОДА","https://loremflickr.com/900/600/sandy%20beach?lock=62206"),
 )),
 GameCategory("ПРЕВОЗ","🚗", listOf(
  Puzzle("АВТОМОБИЛ","Возило со четири тркала.","ПРЕВОЗ","https://images.unsplash.com/photo-1492144534655-ae79c964c9d7?w=900"),
  Puzzle("ВЕЛОСИПЕД","Превозно средство со две тркала и педали.","ПРЕВОЗ","https://images.unsplash.com/photo-1485965120184-e220f721d03e?w=900"),
  Puzzle("ВОЗ","Превозно средство што се движи по шини.","ПРЕВОЗ","https://commons.wikimedia.org/wiki/Special:FilePath/Train_passing_through_Maeklong_Railway_Market_(public_domain).jpg?width=900"),
  Puzzle("МОТОЦИКЛ","Моторно возило на две тркала.","ПРЕВОЗ","https://images.unsplash.com/photo-1558981806-ec527fa84c39?w=900"),
  Puzzle("ТРАМВАЈ","Градско превозно средство што се движи по шини.","ПРЕВОЗ","https://images.unsplash.com/photo-1519501025264-65ba15a82390?w=900"),
  Puzzle("ХЕЛИКОПТЕР","Летало со ротори што може да полетува вертикално.","ПРЕВОЗ","https://images.unsplash.com/photo-1474302770737-173ee21bab63?w=900"),
  Puzzle("ТРОТИНЕТ","Мало превозно средство на две тркала со платформа.","ПРЕВОЗ","https://images.unsplash.com/photo-1604868189265-219b5a8a1b6a?w=900"),
  Puzzle("КАМИОН","Големо моторно возило за превоз на товар.","ПРЕВОЗ","https://images.unsplash.com/photo-1601584115197-04ecc0da31d7?w=900"),
  Puzzle("АВИОН","Летало со крила што превезува патници.","ПРЕВОЗ","https://commons.wikimedia.org/wiki/Special:FilePath/Pan_am_707.jpg?width=900"),
  Puzzle("БРОД","Пловило што се движи по вода.","ПРЕВОЗ","https://commons.wikimedia.org/wiki/Special:FilePath/MSC_Oriane.jpg?width=900"),
  Puzzle("АВТОБУС","Големо патничко возило за јавен превоз.","ПРЕВОЗ","https://commons.wikimedia.org/wiki/Special:FilePath/London_Bus_on_Tower_Bridge_-_pixabay.jpg?width=900"),
  Puzzle("ТАКСИ","Автомобил што превезува патници за надомест.","ПРЕВОЗ","https://commons.wikimedia.org/wiki/Special:FilePath/Taxi_Cab_in_South_Korea.jpg?width=900"),
  Puzzle("МЕТРО","Подземен градски железнички превоз.","ПРЕВОЗ","https://loremflickr.com/900/600/subway%20train?lock=1304"),
  Puzzle("ЧАМЕЦ","Мало пловило за движење по вода.","ПРЕВОЗ","https://loremflickr.com/900/600/small%20boat?lock=1305"),
  Puzzle("ЕДРИЛИЦА","Пловило што се движи со помош на ветер.","ПРЕВОЗ","https://commons.wikimedia.org/wiki/Special:FilePath/Sailboat_in_the_ocean.jpg?width=900"),
  Puzzle("СКУТЕР","Мало моторно возило на две тркала.","ПРЕВОЗ","https://loremflickr.com/900/600/scooter?lock=1307"),
  Puzzle("ПРИКОЛКА","Возило без мотор што го влече друго возило.","ПРЕВОЗ","https://loremflickr.com/900/600/trailer?lock=1308"),
  Puzzle("БАГЕР","Работна машина што копа земја.","ПРЕВОЗ","https://loremflickr.com/900/600/excavator?lock=1309"),
  Puzzle("ТРОЛЕЈБУС","Градско електрично возило што се напојува преку жици.","ПРЕВОЗ","https://loremflickr.com/900/600/trolleybus?lock=15453"),
  Puzzle("ТРКАЛО","Кружен дел што му помага на возилото да се движи.","ПРЕВОЗ","https://loremflickr.com/900/600/wheel?lock=12309"),
  Puzzle("ПОДМОРНИЦА","Пловило што може да се движи под површината на водата.","ПРЕВОЗ","https://commons.wikimedia.org/wiki/Special:FilePath/I-68.jpg?width=900"),
  Puzzle("ЖИЧНИЦА","Превозно средство што се движи високо над земјата по кабел.","ПРЕВОЗ","https://loremflickr.com/900/600/cable%20car?lock=13360"),
  Puzzle("САНКА","Превозно средство што се лизга по снег.","ПРЕВОЗ","https://loremflickr.com/900/600/sled?lock=11240"),
  Puzzle("КАРАВАН","Приколка или возило опремено за патување и престој.","ПРЕВОЗ","https://loremflickr.com/900/600/caravan?lock=13321"),
  Puzzle("КОЧИЈА","Старо превозно средство што го влечат коњи.","ПРЕВОЗ","https://loremflickr.com/900/600/horse%20carriage?lock=12287"),
  Puzzle("БУЛДОЖЕР","Тешка машина што турка земја и урнатини.","ПРЕВОЗ","https://loremflickr.com/900/600/bulldozer?lock=14396"),
  Puzzle("КОМБЕ","Помало моторно возило за превоз на луѓе или товар.","ПРЕВОЗ","https://loremflickr.com/900/600/van?lock=11242"),
  Puzzle("ТРАКТОР","Работно возило што се користи најчесто во земјоделство.","ПРЕВОЗ","https://commons.wikimedia.org/wiki/Special:FilePath/John_Deere_tracked_tractor.jpg?width=900"),
  Puzzle("ЛОКОМОТИВА", "Дел од возот што ги влече или турка вагоните.","ПРЕВОЗ","https://loremflickr.com/900/600/train%20locomotive?lock=35503"),
  Puzzle("ТРИЦИКЛ", "Возило со три тркала.","ПРЕВОЗ","https://loremflickr.com/900/600/three%20wheeler%20vehicle?lock=32373"),
  Puzzle("САМОЛЕТ","Летало со мотори и крила.","ПРЕВОЗ","https://loremflickr.com/900/600/passenger%20airplane?lock=33357"),
  Puzzle("ГЛИСЕР","Брз моторен чамец што се движи по вода.","ПРЕВОЗ","https://loremflickr.com/900/600/speedboat?lock=32300"),
  Puzzle("ФЕРИБРОД","Голем брод што превезува патници и автомобили.","ПРЕВОЗ","https://loremflickr.com/900/600/ferry%20boat?lock=34404"),
  Puzzle("ДИРИЖАБЛ","Летало полесно од воздухот со голем наполнет балон.","ПРЕВОЗ","https://loremflickr.com/900/600/airship%20blimp?lock=34374"),
  Puzzle("ПАРЕН ВОЗ","Стар тип воз што се движел со помош на пареа.","ПРЕВОЗ","https://loremflickr.com/900/600/steam%20locomotive?lock=34424"),
  Puzzle("РОЛЕРИ","Обувки со тркалца што се користат за возење.","ПРЕВОЗ","https://loremflickr.com/900/600/roller%20skates?lock=32310"),
  Puzzle("СКЕЈТБОРД","Даска со четири тркалца на која се вози стоејќи.","ПРЕВОЗ","https://loremflickr.com/900/600/skateboard?lock=35437"),
  Puzzle("СНЕГОМОБИЛ","Моторно возило наменето за движење по снег.","ПРЕВОЗ","https://loremflickr.com/900/600/snowmobile?lock=36498"),
  Puzzle("МОТОР","Возило на две тркала со мотор.","ПРЕВОЗ","https://loremflickr.com/900/600/motorcycle?lock=50302"),
  Puzzle("КРАН","Машина што крева и преместува тешки товари.","ПРЕВОЗ","https://loremflickr.com/900/600/crane?lock=50306"),
  Puzzle("ЈАХТА","Луксузен брод за рекреација и пловење.","ПРЕВОЗ","https://loremflickr.com/900/600/yacht?lock=50307"),
  Puzzle("МОПЕД","Мало моторно возило на две тркала со слаб мотор.","ПРЕВОЗ","https://loremflickr.com/900/600/moped?lock=62300"),
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
  Puzzle("СОФТБОЛ","Тимски спорт сличен на бејзбол, со поголема топка.","СПОРТ","https://loremflickr.com/900/600/softball?lock=32375"),
  Puzzle("ПОЛО","Тимски спорт што се игра на коњи со долги палки.","СПОРТ","https://loremflickr.com/900/600/polo%20sport?lock=62400"),
  Puzzle("СКЕЛЕТОН","Зимски спорт во кој натпреварувачот се спушта со глава напред на санка.","СПОРТ","https://loremflickr.com/900/600/skeleton%20sport?lock=62401"),
  Puzzle("БОБ","Зимски спорт со тимско спуштање во специјална санка по ледена патека.","СПОРТ","https://loremflickr.com/900/600/bobsleigh?lock=62402"),
  Puzzle("КАЈТСЕРФИНГ","Воден спорт во кој спортистот се движи на даска со помош на змеј.","СПОРТ","https://loremflickr.com/900/600/kitesurfing?lock=62403"),
  Puzzle("ВАТЕРПОЛО","Тимски спорт што се игра во базен со топка и голови.","СПОРТ","https://loremflickr.com/900/600/water%20polo?lock=62404"),
  Puzzle("СКВОШ","Спорт со рекети што се игра во затворен терен со ѕидови.","СПОРТ","https://loremflickr.com/900/600/squash%20sport?lock=62405"),
  Puzzle("ПЛАНИНАРЕЊЕ","Активност на пешачење и искачување по планински патеки.","СПОРТ","https://loremflickr.com/900/600/hiking?lock=62406"),
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
  Puzzle("СВЕТИЛНИК","Висока кула покрај море што им помага на бродовите да се ориентираат.","МЕСТА","https://loremflickr.com/900/600/lighthouse?lock=34452"),
  Puzzle("МЕТРО СТАНИЦА","Место под или над земја каде што запира градскиот метро-воз.","МЕСТА","https://loremflickr.com/900/600/subway%20station?lock=50500"),
  Puzzle("МАНАСТИР","Религиозен комплекс во кој живеат монаси или монахињи.","МЕСТА","https://loremflickr.com/900/600/monastery?lock=50504"),
  Puzzle("СИНАГОГА","Верски објект во кој се собираат Евреите за молитва.","МЕСТА","https://loremflickr.com/900/600/synagogue?lock=62500"),
  Puzzle("АМФИТЕАТАР","Голем објект со редови седишта околу отворена сцена или арена.","МЕСТА","https://loremflickr.com/900/600/amphitheatre?lock=62501"),
  Puzzle("АКВАРИУМ","Место каде што се изложуваат риби и други водни животни.","МЕСТА","https://loremflickr.com/900/600/public%20aquarium?lock=62502"),
  Puzzle("ПЛАНЕТАРИУМ","Установа каде што се прикажува ѕвезденото небо и вселената.","МЕСТА","https://loremflickr.com/900/600/planetarium?lock=62503"),
  Puzzle("ОПСЕРВАТОРИЈА","Научно место со телескопи за набљудување на вселената.","МЕСТА","https://loremflickr.com/900/600/observatory?lock=62504"),
  Puzzle("МЕТЕОРОЛОШКА СТАНИЦА","Место каде што се мерат временските услови.","МЕСТА","https://loremflickr.com/900/600/weather%20station?lock=62505"),
  Puzzle("ФАРМАЦЕВТСКА ЛАБОРАТОРИЈА","Простор каде што се испитуваат и подготвуваат лекови.","МЕСТА","https://loremflickr.com/900/600/pharmaceutical%20laboratory?lock=62506"),
  Puzzle("СКЛАДИШТЕ","Голем простор каде што се чуваат стоки и материјали.","МЕСТА","https://loremflickr.com/900/600/warehouse?lock=62507"),
  Puzzle("ПАЗАРЕН ЦЕНТАР","Голем објект со многу продавници и услуги.","МЕСТА","https://loremflickr.com/900/600/shopping%20mall?lock=62508"),
  Puzzle("АВТОСЕРВИС","Место каде што се поправаат и одржуваат автомобили.","МЕСТА","https://loremflickr.com/900/600/auto%20repair%20shop?lock=62509"),
 ))
)
fun normalizeAnswer(value:String):String {
 var s=value.trim().lowercase(Locale.ROOT).replace("dzh","џ").replace("dž","џ").replace("gj","ѓ").replace("đ","ѓ").replace("zh","ж").replace("ž","ж").replace("dz","ѕ").replace("lj","љ").replace("nj","њ").replace("kj","ќ").replace("ć","ќ").replace("ch","ч").replace("č","ч").replace("sh","ш").replace("š","ш")
 val latin=mapOf('a' to 'а','b' to 'б','v' to 'в','g' to 'г','d' to 'д','e' to 'е','z' to 'з','i' to 'и','j' to 'ј','k' to 'к','l' to 'л','m' to 'м','n' to 'н','o' to 'о','p' to 'п','r' to 'р','s' to 'с','t' to 'т','u' to 'у','f' to 'ф','h' to 'х','c' to 'ц')
 s=s.map { latin[it] ?: it }.joinToString("")
 return s.uppercase(Locale.ROOT).filter { it.isLetter() }
}

private fun startBackgroundMusic(volume: Float): AudioTrack {
 // Original upbeat, gentle game soundtrack: warm chords, plucked melody and soft rhythm.
 val sampleRate = 22050
 val seconds = 16
 val total = sampleRate * seconds
 val pcm = ShortArray(total)
 val chordProgression = listOf(
  listOf(261.63, 329.63, 392.00), // C
  listOf(220.00, 261.63, 329.63), // Am
  listOf(174.61, 220.00, 261.63), // F
  listOf(196.00, 246.94, 293.66)  // G
 )
 val melody = listOf(
  523.25, 587.33, 659.25, 587.33, 523.25, 392.00, 440.00, 523.25,
  587.33, 659.25, 783.99, 659.25, 587.33, 523.25, 440.00, 392.00
 )
 val beatSeconds = 0.5
 for (i in 0 until total) {
  val t = i.toDouble() / sampleRate
  val beat = (t / beatSeconds).toInt()
  val chord = chordProgression[(t / 4.0).toInt().coerceIn(0, 3)]
  var value = 0.0
  // Soft sustained chord pad.
  for (f in chord) {
   value += 0.075 * kotlin.math.sin(2.0 * Math.PI * f * t)
   value += 0.018 * kotlin.math.sin(2.0 * Math.PI * f * 2.0 * t)
  }
  // Gentle bass pulse on each beat.
  val bassFreq = chord[0] / 2.0
  val beatPhase = t % beatSeconds
  val bassEnv = kotlin.math.exp(-beatPhase * 5.0)
  value += 0.13 * bassEnv * kotlin.math.sin(2.0 * Math.PI * bassFreq * t)
  // Bright but soft plucked notes, two notes per second.
  val noteIndex = (t / 0.5).toInt() % melody.size
  val noteTime = t % 0.5
  val noteEnv = kotlin.math.exp(-noteTime * 5.5)
  val noteFreq = melody[noteIndex]
  value += 0.16 * noteEnv * (
   kotlin.math.sin(2.0 * Math.PI * noteFreq * noteTime) +
   0.24 * kotlin.math.sin(2.0 * Math.PI * noteFreq * 2.0 * noteTime)
  )
  // Very quiet soft percussion pulse, without harsh clicks.
  val percussionEnv = kotlin.math.exp(-beatPhase * 22.0)
  val noiseLike = kotlin.math.sin(2.0 * Math.PI * 1800.0 * beatPhase) *
   kotlin.math.sin(2.0 * Math.PI * 73.0 * t)
  value += 0.018 * percussionEnv * noiseLike
  // Seamless loop fade at the boundary.
  val edge = minOf(1.0, t / 0.35, (seconds - t) / 0.35).coerceAtLeast(0.0)
  pcm[i] = (value * edge * 32767.0).toInt().coerceIn(-32768, 32767).toShort()
 }
 val track = AudioTrack.Builder()
  .setAudioAttributes(AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_MEDIA).setContentType(AudioAttributes.CONTENT_TYPE_MUSIC).build())
  .setAudioFormat(AudioFormat.Builder().setSampleRate(sampleRate).setEncoding(AudioFormat.ENCODING_PCM_16BIT).setChannelMask(AudioFormat.CHANNEL_OUT_MONO).build())
  .setBufferSizeInBytes(pcm.size * 2)
  .setTransferMode(AudioTrack.MODE_STATIC)
  .build()
 track.write(pcm, 0, pcm.size)
 track.setLoopPoints(0, pcm.size, -1)
 track.setVolume(0.10f * volume.coerceIn(0f, 1f))
 track.play()
 return track
}

private fun playGameSound(context: Context, soundType: String, volume: Float) {
 val sampleRate = 22050
 val notes = when (soundType) {
  "correct" -> listOf(523.25, 659.25, 783.99, 1046.50, 1318.51)
  "wrong" -> listOf(392.00, 349.23, 293.66)
  "hint" -> listOf(440.00, 554.37, 659.25, 880.00)
  else -> listOf(880.0, 1174.66)
 }
 val durations = when (soundType) {
  "correct" -> listOf(0.07, 0.07, 0.08, 0.09, 0.18)
  "wrong" -> listOf(0.07, 0.08, 0.16)
  "hint" -> listOf(0.07, 0.07, 0.08, 0.14)
  else -> listOf(0.045, 0.055)
 }
 val pcm = ShortArray((durations.sum() * sampleRate).toInt())
 var cursor = 0
 for (n in notes.indices) {
  val count = (durations[n] * sampleRate).toInt()
  for (i in 0 until count) {
   val t = i.toDouble() / sampleRate
   val attack = (i.toDouble() / (sampleRate * 0.008)).coerceAtMost(1.0)
   val release = ((count - i).toDouble() / (sampleRate * 0.035)).coerceAtMost(1.0)
   val envelope = attack * release
   val fundamental = kotlin.math.sin(2.0 * Math.PI * notes[n] * t)
   val harmonic = kotlin.math.sin(2.0 * Math.PI * notes[n] * 2.0 * t)
   val tone = fundamental * 0.88 + harmonic * 0.12
   pcm[cursor++] = (tone * envelope * 10500.0).toInt().coerceIn(-32768, 32767).toShort()
  }
 }
 val track = AudioTrack.Builder()
  .setAudioAttributes(AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_ASSISTANCE_SONIFICATION).setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION).build())
  .setAudioFormat(AudioFormat.Builder().setSampleRate(sampleRate).setEncoding(AudioFormat.ENCODING_PCM_16BIT).setChannelMask(AudioFormat.CHANNEL_OUT_MONO).build())
  .setBufferSizeInBytes(pcm.size * 2)
  .setTransferMode(AudioTrack.MODE_STATIC)
  .build()
 track.write(pcm, 0, pcm.size)
 track.setVolume(0.72f * volume.coerceIn(0f, 1f))
 track.setNotificationMarkerPosition(pcm.size)
 track.setPlaybackPositionUpdateListener(object : AudioTrack.OnPlaybackPositionUpdateListener {
  override fun onMarkerReached(player: AudioTrack) { player.release() }
  override fun onPeriodicNotification(player: AudioTrack) {}
 })
 track.play()
}
class MainActivity:ComponentActivity(){override fun onCreate(savedInstanceState:Bundle?){super.onCreate(savedInstanceState);setContent{GameScreen()}}}
@Composable fun GameScreen(){
 val context = LocalContext.current
 val prefs = remember { context.getSharedPreferences("pogodi_progress", Context.MODE_PRIVATE) }
 var selected by remember { mutableIntStateOf(prefs.getInt("selected", -1)) }
 var level by remember { mutableIntStateOf(prefs.getInt("level", 0)) }
 var answer by remember { mutableStateOf("") }
 var score by remember { mutableIntStateOf(prefs.getInt("score", 0)) }
 var soundEnabled by remember { mutableStateOf(prefs.getBoolean("sound_enabled", true)) }
 var soundVolume by remember { mutableFloatStateOf(prefs.getFloat("sound_volume", 0.65f)) }
 var darkMode by remember { mutableStateOf(prefs.getBoolean("dark_mode", true)) }
 var backgroundMusic by remember { mutableStateOf<AudioTrack?>(null) }
 DisposableEffect(soundEnabled, soundVolume) {
  val music = if (soundEnabled) startBackgroundMusic(soundVolume) else null
  backgroundMusic = music
  onDispose {
   backgroundMusic?.let { track ->
    runCatching { track.pause() }
    runCatching { track.flush() }
    runCatching { track.release() }
   }
   backgroundMusic = null
  }
 }
 LaunchedEffect(selected, level, score) {
  prefs.edit().putInt("selected", selected).putInt("level", level).putInt("score", score).apply()
 }
 var solved by remember { mutableStateOf(false) }; var hint by remember { mutableStateOf(false) }
 var message by remember { mutableStateOf(if (prefs.getInt("selected", -1) >= 0) "Внеси го точниот збор!" else "Избери категорија за да започнеш!") }
 LaunchedEffect(solved) { if (solved && selected >= 0) { delay(900); val cat=categories[selected]; if (level+1 < cat.puzzles.size) { level++; message="Внеси го точниот збор!" } else { selected=-1; message="Категоријата е завршена! Браво! 🎉" }; answer=""; solved=false; hint=false } }
 val pageBackground = if (darkMode) Color(0xFF101B3D) else Color(0xFFF3F6FC)
 val cardBackground = if (darkMode) Color(0xFF24365F) else Color.White
 val primaryText = if (darkMode) Color.White else Color(0xFF18233A)
 val secondaryText = if (darkMode) Color(0xFFB8C8EA) else Color(0xFF52627A)
 val accent = if (darkMode) Color(0xFF8DE1D2) else Color(0xFF087F75)
 MaterialTheme(colorScheme=if (darkMode) darkColorScheme(primary=accent) else lightColorScheme(primary=accent, background=pageBackground, surface=cardBackground)){
 Column(Modifier.fillMaxSize().background(pageBackground).verticalScroll(rememberScrollState()).padding(18.dp),horizontalAlignment=Alignment.CenterHorizontally,verticalArrangement=Arrangement.spacedBy(14.dp)){
 Text("🧩 ПОГОДИ ГИ ЗБОРОВИТЕ",color=primaryText,fontSize=24.sp,fontWeight=FontWeight.ExtraBold,textAlign=TextAlign.Center)
 Text("Македонска игра со зборови",color=secondaryText)
 Row(Modifier.fillMaxWidth(), horizontalArrangement=Arrangement.End) {
  TextButton(onClick={
   backgroundMusic?.let { track ->
    runCatching { track.pause() }
    runCatching { track.flush() }
    runCatching { track.release() }
   }
   backgroundMusic = null
   (context as? Activity)?.finishAndRemoveTask()
  }) { Text("🚪 ИЗЛЕЗ ОД ИГРАТА", color=accent, fontWeight=FontWeight.Bold) }
 }
 Row(Modifier.fillMaxWidth(),horizontalArrangement=Arrangement.SpaceBetween,verticalAlignment=Alignment.CenterVertically){
  Text("ПОЕНИ: "+score,color=if(darkMode) Color(0xFFFFD782) else Color(0xFF9A6500),fontWeight=FontWeight.Bold)
  TextButton(onClick={soundEnabled=!soundEnabled;prefs.edit().putBoolean("sound_enabled",soundEnabled).apply()}){Text(if(soundEnabled) "🔊 ЗВУК ВКЛ." else "🔇 ЗВУК ИСКЛ.",color=accent)}
 }
 Card(colors=CardDefaults.cardColors(containerColor=cardBackground), modifier=Modifier.fillMaxWidth()) {
  Column(Modifier.padding(14.dp), verticalArrangement=Arrangement.spacedBy(8.dp)) {
   Text("⚙️ ПАРАМЕТРИ", color=primaryText, fontWeight=FontWeight.Bold)
   Text("Јачина на звук: ${(soundVolume * 100).toInt()}%", color=secondaryText)
   Slider(value=soundVolume, onValueChange={soundVolume=it}, onValueChangeFinished={prefs.edit().putFloat("sound_volume", soundVolume).apply()}, valueRange=0f..1f, enabled=soundEnabled)
   Row(Modifier.fillMaxWidth(), horizontalArrangement=Arrangement.SpaceBetween, verticalAlignment=Alignment.CenterVertically) {
    Text("Заднина", color=primaryText)
    TextButton(onClick={darkMode=!darkMode;prefs.edit().putBoolean("dark_mode",darkMode).apply()}) {
     Text(if(darkMode) "🌙 ТЕМЕН РЕЖИМ" else "☀️ СВЕТОЛ РЕЖИМ", color=accent)
    }
   }
  }
 }
 if(selected<0){
  Text("ИЗБЕРИ КАТЕГОРИЈА",color=primaryText,fontSize=20.sp,fontWeight=FontWeight.Bold)
  categories.forEachIndexed { i,cat ->
   Card(onClick={if(soundEnabled) playGameSound(context, "tap", soundVolume);selected=i;level=0;answer="";solved=false;hint=false;message="Внеси го точниот збор!"},colors=CardDefaults.cardColors(containerColor=cardBackground),shape=RoundedCornerShape(18.dp),modifier=Modifier.fillMaxWidth()){
    Row(Modifier.padding(18.dp),verticalAlignment=Alignment.CenterVertically,horizontalArrangement=Arrangement.spacedBy(14.dp)){
     Text(cat.emoji,fontSize=32.sp)
     Column(Modifier.weight(1f)){Text(cat.title,color=primaryText,fontWeight=FontWeight.Bold,fontSize=18.sp);Text(cat.puzzles.size.toString()+" зборови со фотографии",color=secondaryText,fontSize=13.sp)}
     Text("›",color=Color(0xFF8DE1D2),fontSize=28.sp)
    }
   }
  }
 }else{
  val cat=categories[selected.coerceIn(0, categories.lastIndex)]
  val safeLevel = level.coerceIn(0, cat.puzzles.lastIndex)
  val p=cat.puzzles[safeLevel]
  var imageFailed by remember(p.photo) { mutableStateOf(false) }
  Row(Modifier.fillMaxWidth(),horizontalArrangement=Arrangement.SpaceBetween,verticalAlignment=Alignment.CenterVertically){
   TextButton(onClick={selected=-1;answer="";solved=false;hint=false}){Text("← КАТЕГОРИИ", color=accent)}
   Text("НИВО "+(level+1)+"/"+cat.puzzles.size,color=Color(0xFFFFD782),fontWeight=FontWeight.Bold)
  }
  Text(cat.title,color=accent,fontWeight=FontWeight.Bold)
  Card(colors=CardDefaults.cardColors(containerColor=Color(0xFF24365F)),shape=RoundedCornerShape(22.dp),modifier=Modifier.fillMaxWidth()){
   Column(Modifier.padding(16.dp),horizontalAlignment=Alignment.CenterHorizontally,verticalArrangement=Arrangement.spacedBy(12.dp)){
    if (imageFailed) {
     Box(Modifier.fillMaxWidth().height(190.dp).background(Color(0xFF172747)), contentAlignment=Alignment.Center) {
      Text("СЛИКАТА НЕ Е ДОСТАПНА", color=Color(0xFFB8C8EA), fontWeight=FontWeight.Bold)
     }
    } else {
     AsyncImage(model=p.photo,contentDescription=p.category,modifier=Modifier.fillMaxWidth().height(190.dp),contentScale=ContentScale.Crop,onError={ imageFailed=true })
    }
    Text("КОЈ ЗБОР Е ОВА?",color=primaryText,fontSize=21.sp,fontWeight=FontWeight.Bold)
    Text(p.clue,color=primaryText,fontSize=17.sp,textAlign=TextAlign.Center)
    Text("БРОЈ НА БУКВИ: "+p.word.count { it != ' ' },color=Color(0xFFFFD782),fontWeight=FontWeight.Bold)
    OutlinedTextField(value=answer,onValueChange={answer=it.uppercase()},label={Text("Одговор: кирилица или латиница")},enabled=!solved,singleLine=true)
    Button(onClick={if(normalizeAnswer(answer)==normalizeAnswer(p.word)){if(soundEnabled) playGameSound(context, "correct", soundVolume);score+=if(hint)5 else 10;solved=true;message="БРАВО! ТОЧЕН ОДГОВОР! 🎉"}else{if(soundEnabled) playGameSound(context, "wrong", soundVolume);message="Не е точно. Обиди се повторно!"}},enabled=!solved,modifier=Modifier.fillMaxWidth()){Text("ПРОВЕРИ")}
    OutlinedButton(onClick={if(soundEnabled) playGameSound(context, "hint", soundVolume);hint=true;message="ПРВА БУКВА: "+p.word.first()},enabled=!solved&&!hint){Text("💡 ПОМОШ")}
    Text(message,color=Color(0xFFFFD782),textAlign=TextAlign.Center)
    if(solved) Text("СЛЕДНОТО НИВО СЕ ОТВОРА АВТОМАТСКИ…",color=Color(0xFF8DE1D2),fontWeight=FontWeight.Bold,textAlign=TextAlign.Center)
   }
  }
 }
 Text("Погодувај, учи и забавувај се!",color=secondaryText,fontSize=13.sp)
 }
 }
}
